/**
 *    Copyright 2023 Sven Loesekann
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package ch.xxx.aidoclibchat.usecase.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import ch.xxx.aidoclibchat.domain.common.MetaData;
import ch.xxx.aidoclibchat.domain.common.MetaData.DataType;
import ch.xxx.aidoclibchat.domain.common.MetaData.ImageType;
import ch.xxx.aidoclibchat.domain.model.dto.ImageDto;
import ch.xxx.aidoclibchat.domain.model.dto.ImageQueryDto;
import ch.xxx.aidoclibchat.domain.model.entity.DocumentVsRepository;
import ch.xxx.aidoclibchat.domain.model.entity.Image;
import ch.xxx.aidoclibchat.domain.model.entity.ImageRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ImageService {
	private static final Logger LOG = LoggerFactory.getLogger(ImageService.class);
	private ChatClient chatClient;
	private ImageRepository imageRepository;
	private DocumentVsRepository documentVsRepository;

	private record ResultData(String answer, ImageQueryDto imageQueryDto) {
	}

	public ImageService(ChatClient chatClient, ImageRepository imageRepository,
			DocumentVsRepository documentVsRepository) {
		this.chatClient = chatClient;
		this.imageRepository = imageRepository;
		this.documentVsRepository = documentVsRepository;
	}

	public ImageDto importImage(ImageQueryDto imageDto, Image image) {
		var resultData = this.createAIResult(imageDto);
		image.setImageContent(resultData.imageQueryDto().getImageContent());
		var myImage = this.imageRepository.save(image);
		var aiDocument = new Document(resultData.answer());
		aiDocument.getMetadata().put(MetaData.ID, myImage.getId().toString());
		aiDocument.getMetadata().put(MetaData.DATATYPE, MetaData.DataType.IMAGE.toString());
		this.documentVsRepository.add(List.of(aiDocument));
		return new ImageDto(resultData.answer(),
				Base64.getEncoder().encodeToString(resultData.imageQueryDto().getImageContent()),
				resultData.imageQueryDto().getImageType());
	}

	public List<ImageDto> queryImage(ImageQueryDto imageDto) {
		var aiDocuments = this.documentVsRepository.retrieve(imageDto.getQuery(), MetaData.DataType.IMAGE).stream()
				.filter(myDoc -> myDoc.getMetadata().get(MetaData.DATATYPE).equals(DataType.IMAGE.toString()))
				.sorted((myDocA, myDocB) -> ((Float) myDocA.getMetadata().get(MetaData.DISTANCE))
						.compareTo(((Float) myDocB.getMetadata().get(MetaData.DISTANCE))))
				.toList();
		var imageMap = this.imageRepository
				.findAllById(aiDocuments.stream().map(myDoc -> (String) myDoc.getMetadata().get(MetaData.ID))
						.map(myUuid -> UUID.fromString(myUuid)).limit(20).toList())
				.stream().collect(Collectors.toMap(myDoc -> myDoc.getId(), myDoc -> myDoc));
		record Container(Document document, Image image, Float distance) {
		}		
		var containers = imageMap.entrySet().stream().map(myEntry -> new Container(
				aiDocuments.stream()
						.filter(myDoc -> myEntry.getKey().toString()
								.equals((String) myDoc.getMetadata().get(MetaData.ID)))
						.findFirst().orElseThrow(),
				myEntry.getValue(),
				aiDocuments.stream()
						.filter(myDoc -> myEntry.getKey().toString()
								.equals((String) myDoc.getMetadata().get(MetaData.ID)))
						.map(myDoc -> (Float) myDoc.getMetadata().get(MetaData.DISTANCE)).findFirst().orElseThrow()))
				.toList();
		return containers.stream()
				.sorted((containerA, containerB) -> containerA.distance().compareTo(containerB.distance()))
				.map(myContainer -> new ImageDto(myContainer.document().getContent(),
						Base64.getEncoder().encodeToString(myContainer.image().getImageContent()),
						myContainer.image().getImageType()))				
				.toList();
	}

	private ResultData createAIResult(ImageQueryDto imageDto) {
		if (ImageType.JPEG.equals(imageDto.getImageType()) || ImageType.PNG.equals(imageDto.getImageType())) {
			imageDto = this.resizeImage(imageDto);
		}
		var prompt = new Prompt(new UserMessage(imageDto.getQuery(), List
				.of(new Media(MimeType.valueOf(imageDto.getImageType().getMediaType()), imageDto.getImageContent()))));
		var response = this.chatClient.call(prompt);
		var resultData = new ResultData(response.getResult().getOutput().getContent(), imageDto);
		return resultData;
	}

	private ImageQueryDto resizeImage(ImageQueryDto imageDto) {
		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageDto.getImageContent()));
			int targetHeight = image.getHeight();
			int targetWidth = image.getWidth();
			if (image.getHeight() > 672 && image.getWidth() > 672) {
				if (image.getHeight() < image.getWidth()) {
					targetHeight = Math.round(image.getHeight() / (image.getHeight() / 672.0f));
					targetWidth = Math.round(image.getWidth() / (image.getHeight() / 672.0f));
				} else {
					targetHeight = Math.round(image.getHeight() / (image.getWidth() / 672.0f));
					targetWidth = Math.round(image.getWidth() / (image.getWidth() / 672.0f));
				}
			}
			var outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
			outputImage.getGraphics().drawImage(
					image.getScaledInstance(targetWidth, targetHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
			var ios = new ByteArrayOutputStream();
			ImageIO.write(outputImage, imageDto.getImageType().toString(), ios);
			imageDto.setImageContent(ios.toByteArray());
			imageDto.setContentSize(ios.toByteArray().length);
			LOG.info("Resized image to x: {}, y: {}", targetWidth, targetHeight);
		} catch (IOException e) {
			LOG.info("Image resize failed.", e);
		}
		return imageDto;
	}
}
