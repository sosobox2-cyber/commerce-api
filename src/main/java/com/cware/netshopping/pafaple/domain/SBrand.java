package com.cware.netshopping.pafaple.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class SBrand {
	
	@JsonProperty("sBrandname")
	private String saleBrandName;
	@JsonProperty("SBCategoryID")
	private Integer kindId;
	@JsonProperty("DisBrandID")
	private Integer disBrandId;
	@JsonProperty("IsUse")
	private String isUse;
	@JsonProperty("IsFpsrch")
	private String isFpsrch;
	@JsonProperty("StartDate")
	private String startDate;
	@JsonProperty("EndDate")
	private String endDate;
	@JsonProperty("SenderID")
	private Integer senderID;
	@JsonProperty("Isshowbrand")
	private String isshowbrand;
	@JsonProperty("IsSCM")
	private String isSCM;
	@JsonProperty("Isused_item")
	private String Isused_Item;
	@JsonProperty("Product_co")
	private String productCo;
		
	public String getSaleBrandName() {
		return saleBrandName;
	}
	public void setSaleBrandName(String saleBrandName) {
		this.saleBrandName = saleBrandName;
	}
	public Integer getKindId() {
		return kindId;
	}
	public void setKindId(Integer kindId) {
		this.kindId = kindId;
	}
	public Integer getDisBrandId() {
		return disBrandId;
	}
	public void setDisBrandId(Integer disBrandId) {
		this.disBrandId = disBrandId;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getIsFpsrch() {
		return isFpsrch;
	}
	public void setIsFpsrch(String isFpsrch) {
		this.isFpsrch = isFpsrch;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getSenderID() {
		return senderID;
	}
	public void setSenderID(Integer senderID) {
		this.senderID = senderID;
	}
	public String getIsshowbrand() {
		return isshowbrand;
	}
	public void setIsshowbrand(String isshowbrand) {
		this.isshowbrand = isshowbrand;
	}
	public String getIsSCM() {
		return isSCM;
	}
	public void setIsSCM(String isSCM) {
		this.isSCM = isSCM;
	}
	public String getIsusedItem() {
		return Isused_Item;
	}
	public void setIsusedItem(String Isused_Item) {
		this.Isused_Item = Isused_Item;
	}
	public String getProductCo() {
		return productCo;
	}
	public void setProductCo(String productCo) {
		this.productCo = productCo;
	}
	
}
