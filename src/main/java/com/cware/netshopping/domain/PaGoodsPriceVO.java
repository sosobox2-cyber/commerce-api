package com.cware.netshopping.domain;


import com.cware.netshopping.domain.model.PaGoodsPrice;


public class PaGoodsPriceVO extends PaGoodsPrice {
	private static final long serialVersionUID = 1L;

	//PaGoodsPrice.java 금액이 String 형이라 어쩔수 없이... 맞춰줌
	private String productNo;
	private String beSalePrice;
	private String marginRate;
	private String eventYn;
	private String paGoodsCode;
	private String dateTime;
	private String sourcingMedia;
	private String mdKind;
	private String mobileEtvYn;
	private String taxYn;
	
	public String getMdKind() {
		return mdKind;
	}
	public void setMdKind(String mdKind) {
		this.mdKind = mdKind;
	}
	public String getSourcingMedia() {
		return sourcingMedia;
	}
	public void setSourcingMedia(String sourcingMedia) {
		this.sourcingMedia = sourcingMedia;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getPaGoodsCode() {
		return paGoodsCode;
	}
	public void setPaGoodsCode(String paGoodsCode) {
		this.paGoodsCode = paGoodsCode;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getBeSalePrice() {
		return beSalePrice;
	}
	public void setBeSalePrice(String beSalePrice) {
		this.beSalePrice = beSalePrice;
	}
	public String getMarginRate() {
		return marginRate;
	}
	public void setMarginRate(String marginRate) {
		this.marginRate = marginRate;
	}
	public String getEventYn() {
		return eventYn;
	}
	public void setEventYn(String eventYn) {
		this.eventYn = eventYn;
	}
	public String getMobileEtvYn() {
		return mobileEtvYn;
	}
	public void setMobileEtvYn(String mobileEtvYn) {
		this.mobileEtvYn = mobileEtvYn;
	}
	public String getTaxYn() {
		return taxYn;
	}
	public void setTaxYn(String taxYn) {
		this.taxYn = taxYn;
	}	
	
}