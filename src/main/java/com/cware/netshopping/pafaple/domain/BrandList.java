package com.cware.netshopping.pafaple.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class BrandList {
	
	@JsonProperty("BrandId")
	private Integer BrandId;
	@JsonProperty("BrandName")
    private String BrandName;
	@JsonProperty("SenderName")
    private String SenderName;
	@JsonProperty("SenderID")
    private Integer SenderID;
	@JsonProperty("FreeSalesPrice")
    private Integer FreeSalesPrice;
	@JsonProperty("SenderFee")
    private Integer SenderFee;
	@JsonProperty("ExtraFee")
    private Integer ExtraFee;
	
	@JsonProperty("Max_OverSeaDeliveryDay")
	public Integer getBrandId() {
		return BrandId;
	}
	public void setBrandId(Integer brandId) {
		BrandId = brandId;
	}
	@JsonProperty("BrandName")
	public String getBrandName() {
		return BrandName;
	}
	public void setBrandName(String brandName) {
		BrandName = brandName;
	}
	@JsonProperty("SenderName")
	public String getSenderName() {
		return SenderName;
	}
	public void setSenderName(String senderName) {
		SenderName = senderName;
	}
	@JsonProperty("SenderID")
	public Integer getSenderID() {
		return SenderID;
	}
	public void setSenderID(Integer senderID) {
		SenderID = senderID;
	}
	@JsonProperty("FreeSalesPrice")
	public Integer getFreeSalesPrice() {
		return FreeSalesPrice;
	}
	public void setFreeSalesPrice(Integer freeSalesPrice) {
		FreeSalesPrice = freeSalesPrice;
	}
	@JsonProperty("SenderFee")
	public Integer getSenderFee() {
		return SenderFee;
	}
	public void setSenderFee(Integer senderFee) {
		SenderFee = senderFee;
	}
	@JsonProperty("ExtraFee")
	public Integer getExtraFee() {
		return ExtraFee;
	}
	public void setExtraFee(Integer extraFee) {
		ExtraFee = extraFee;
	}
    
}
