package com.cware.netshopping.domain.model;

public class PaKakaoGoods extends PaGoods {
	private static final long serialVersionUID = 1L;
	
	private String paCode;
	private String paGroupCode;
	private String productId;
	private String goodsName;
	private String paSaleGb;
	private String paStatus;
	private String transOrderAbleQty;
	private String categoryId;
	private String manufacture;
	private String brand;
	private String productOrginAreaInfo;
	private String transTargetYn;
	private String transSaleYn;
	private String returnNote;
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getPaSaleGb() {
		return paSaleGb;
	}
	public void setPaSaleGb(String paSaleGb) {
		this.paSaleGb = paSaleGb;
	}
	public String getPaStatus() {
		return paStatus;
	}
	public void setPaStatus(String paStatus) {
		this.paStatus = paStatus;
	}
	public String getTransOrderAbleQty() {
		return transOrderAbleQty;
	}
	public void setTransOrderAbleQty(String transOrderAbleQty) {
		this.transOrderAbleQty = transOrderAbleQty;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getManufacture() {
		return manufacture;
	}
	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getProductOrginAreaInfo() {
		return productOrginAreaInfo;
	}
	public void setProductOrginAreaInfo(String productOrginAreaInfo) {
		this.productOrginAreaInfo = productOrginAreaInfo;
	}
	public String getTransTargetYn() {
		return transTargetYn;
	}
	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}
	public String getTransSaleYn() {
		return transSaleYn;
	}
	public void setTransSaleYn(String transSaleYn) {
		this.transSaleYn = transSaleYn;
	}
	public String getReturnNote() {
		return returnNote;
	}
	public void setReturnNote(String returnNote) {
		this.returnNote = returnNote;
	}
	
}
