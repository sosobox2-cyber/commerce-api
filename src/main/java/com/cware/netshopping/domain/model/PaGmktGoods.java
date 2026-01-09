package com.cware.netshopping.domain.model;

public class PaGmktGoods extends PaGoods {

	private static final long serialVersionUID = 1L;

	private String paCode;
	private String paGroupCode;
	private String itemNo;
	private String paSaleGb;
	private long transOrderAbleQty;
	private String paStatus;
	private String paSgroup;
	private String originEnum;
	private String brandNo;
	private String makerNo;
	private String transTargetYn;
	private String returnNote;
	private String esmGoodsCode;
	private String esmCategoryCode;
	private String siteCategoryCode;
	private String transTargetNameYn;
	private String transSaleYn;
	private String originDt;
	private String transCompleteDate;
	private String autoYn;
	private String orderCreateYn;

	public String getOriginDt() {
		return originDt;
	}

	public void setOriginDt(String originDt) {
		this.originDt = originDt;
	}

	public String getTransCompleteDate() {
		return transCompleteDate;
	}

	public void setTransCompleteDate(String transCompleteDate) {
		this.transCompleteDate = transCompleteDate;
	}

	public String getEsmGoodsCode() {
		return esmGoodsCode;
	}

	public void setEsmGoodsCode(String esmGoodsCode) {
		this.esmGoodsCode = esmGoodsCode;
	}

	public String getEsmCategoryCode() {
		return esmCategoryCode;
	}

	public void setEsmCategoryCode(String esmCategoryCode) {
		this.esmCategoryCode = esmCategoryCode;
	}

	public String getTransTargetNameYn() {
		return transTargetNameYn;
	}

	public void setTransTargetNameYn(String transTargetNameYn) {
		this.transTargetNameYn = transTargetNameYn;
	}

	public String getTransSaleYn() {
		return transSaleYn;
	}

	public void setTransSaleYn(String transSaleYn) {
		this.transSaleYn = transSaleYn;
	}

	public String getPaCode() {
		return this.paCode;
	}

	public String getPaGroupCode() {
		return this.paGroupCode;
	}

	public String getItemNo() {
		return this.itemNo;
	}

	public String getPaSaleGb() {
		return this.paSaleGb;
	}

	public long getTransOrderAbleQty() {
		return this.transOrderAbleQty;
	}

	public String getPaStatus() {
		return this.paStatus;
	}

	public String getPaSgroup() {
		return this.paSgroup;
	}

	public String getOriginEnum() {
		return this.originEnum;
	}

	public String getBrandNo() {
		return this.brandNo;
	}

	public String getMakerNo() {
		return this.makerNo;
	}

	public String getTransTargetYn() {
		return this.transTargetYn;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}

	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public void setPaSaleGb(String paSaleGb) {
		this.paSaleGb = paSaleGb;
	}

	public void setTransOrderAbleQty(long transOrderAbleQty) {
		this.transOrderAbleQty = transOrderAbleQty;
	}

	public void setPaStatus(String paStatus) {
		this.paStatus = paStatus;
	}

	public void setPaSgroup(String paSgroup) {
		this.paSgroup = paSgroup;
	}

	public void setOriginEnum(String originEnum) {
		this.originEnum = originEnum;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public void setMakerNo(String makerNo) {
		this.makerNo = makerNo;
	}

	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}

	public String getReturnNote() {
		return returnNote;
	}

	public void setReturnNote(String returnNote) {
		this.returnNote = returnNote;
	}

	public String getAutoYn() {
		return autoYn;
	}

	public void setAutoYn(String autoYn) {
		this.autoYn = autoYn;
	}

	public String getSiteCategoryCode() {
		return siteCategoryCode;
	}

	public void setSiteCategoryCode(String siteCategoryCode) {
		this.siteCategoryCode = siteCategoryCode;
	}

	public String getOrderCreateYn() {
		return orderCreateYn;
	}

	public void setOrderCreateYn(String orderCreateYn) {
		this.orderCreateYn = orderCreateYn;
	}
	
}
