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

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDto {
	private String id;
	@JsonProperty("COUNTRY")
	private String country;
	private Long retailer;
	@JsonProperty("retailer_s")
	private String retailerS;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "M/d/yyyy")
	private LocalDate date;
	private int day;
	private int month;
	private int year;
	private double price;
	@JsonProperty("price_online")
	private double priceOnline;
	private int imputed;
	@JsonProperty("DEVICEID")
	private String deviceid;
	@JsonProperty("TIME")
	private String time;
	@JsonProperty("ZIPCODE")
	private String zipcode;
	@JsonProperty("PHOTO")
	private String photo;
	@JsonProperty("OTHERSKUITEM")
	private String otherskuitem;
	@JsonProperty("COMMENTS")
	private String comments;
	@JsonProperty("PRICETYPE")
	private String pricetype;
	@JsonProperty("CODE")
	private String code;
	@JsonProperty("sale_online")
	private String saleOnline;
	@JsonProperty("country_s")
	private String countryS;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Long getRetailer() {
		return retailer;
	}
	public void setRetailer(Long retailer) {
		this.retailer = retailer;
	}
	public String getRetailerS() {
		return retailerS;
	}
	public void setRetailerS(String retailerS) {
		this.retailerS = retailerS;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPriceOnline() {
		return priceOnline;
	}
	public void setPriceOnline(double priceOnline) {
		this.priceOnline = priceOnline;
	}
	public int getImputed() {
		return imputed;
	}
	public void setImputed(int imputed) {
		this.imputed = imputed;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getOtherskuitem() {
		return otherskuitem;
	}
	public void setOtherskuitem(String otherskuitem) {
		this.otherskuitem = otherskuitem;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getPricetype() {
		return pricetype;
	}
	public void setPricetype(String pricetype) {
		this.pricetype = pricetype;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSaleOnline() {
		return saleOnline;
	}
	public void setSaleOnline(String saleOnline) {
		this.saleOnline = saleOnline;
	}
	public String getCountryS() {
		return countryS;
	}
	public void setCountryS(String countryS) {
		this.countryS = countryS;
	}
	
	@Override
	public String toString() {
		return "ProductDto [id=" + id + ", country=" + country + ", retailer=" + retailer + ", retailerS=" + retailerS
				+ ", date=" + date + ", day=" + day + ", month=" + month + ", year=" + year + ", price=" + price
				+ ", priceOnline=" + priceOnline + ", imputed=" + imputed + ", deviceid=" + deviceid + ", time=" + time
				+ ", zipcode=" + zipcode + ", photo=" + photo + ", otherskuitem=" + otherskuitem + ", comments="
				+ comments + ", pricetype=" + pricetype + ", code=" + code + ", saleOnline=" + saleOnline
				+ ", countryS=" + countryS + "]";
	}
}
