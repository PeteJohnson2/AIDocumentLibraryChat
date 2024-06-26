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
package ch.xxx.aidoclibchat.domain.model.dto;

import ch.xxx.aidoclibchat.domain.common.MetaData.ImageType;

public class ImageDto {
	private String answer;
	private String b64Image;
	private ImageType imageType;
	
	public ImageDto() { }
	
	public ImageDto(String answer, String b64Image, ImageType imageType) {
		super();
		this.answer = answer;
		this.b64Image = b64Image;
		this.imageType = imageType;
	}

	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getB64Image() {
		return b64Image;
	}
	public void setB64Image(String b64Image) {
		this.b64Image = b64Image;
	}
	public ImageType getImageType() {
		return imageType;
	}
	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}
}
