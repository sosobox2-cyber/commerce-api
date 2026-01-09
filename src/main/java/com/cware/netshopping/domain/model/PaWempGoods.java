package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

public class PaWempGoods extends PaGoods {
	private static final long serialVersionUID = 1L; 

	private String goodsCode;
	private String paCode;
	private String paGroupCode;
	private String paSaleGb;
	private String paStatus;
	private String paLmsdKey;
	private String productNo;
	private String transOrderAbleQty;
	private String shipPolicyNo;
	private String displayYn;
	private String brandNo;
	private String makerNo;
	private String transTargetYn; 
	private String transSaleYn;
	private String returnNote;
	private Timestamp lastSyncDate;
	private String paProductGroupNoticeNo;
	private String paSaleStartDate; //제휴 상품 판매개시일
	private String paSaleEndDate;   //제휴 상품 판매종료일
	
	public String getPaLmsdKey() {
		return paLmsdKey;
	}
	public void setPaLmsdKey(String paLmsdKey) {
		this.paLmsdKey = paLmsdKey;
	}
	public String getShipPolicyNo() {
		return shipPolicyNo;
	}
	public void setShipPolicyNo(String shipPolicyNo) {
		this.shipPolicyNo = shipPolicyNo;
	}
	public String getDisplayYn() {
		return displayYn;
	}
	public void setDisplayYn(String displayYn) {
		this.displayYn = displayYn;
	}
	public String getBrandNo() {
		return brandNo;
	}
	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
	public String getMakerNo() {
		return makerNo;
	}
	public void setMakerNo(String makerNo) {
		this.makerNo = makerNo;
	}
	public Timestamp getLastSyncDate() {
		return lastSyncDate;
	}
	public void setLastSyncDate(Timestamp lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
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
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getPaSaleGb() {
		return paSaleGb;
	}
	public void setPaSaleGb(String paSaleGb) {
		this.paSaleGb = paSaleGb;
	}
	public String getTransOrderAbleQty() {
		return transOrderAbleQty;
	}
	public void setTransOrderAbleQty(String transOrderAbleQty) {
		this.transOrderAbleQty = transOrderAbleQty;
	}
	public String getPaStatus() {
		return paStatus;
	}
	public void setPaStatus(String paStatus) {
		this.paStatus = paStatus;
	}
	public String getTransTargetYn() {
		return transTargetYn;
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
	public String getPaSaleStartDate() {
		return paSaleStartDate;
	}
	public void setPaSaleStartDate(String paSaleStartDate) {
		this.paSaleStartDate = paSaleStartDate;
	}
	public String getPaSaleEndDate() {
		return paSaleEndDate;
	}
	public void setPaSaleEndDate(String paSaleEndDate) {
		this.paSaleEndDate = paSaleEndDate;
	}
	public String getPaProductGroupNoticeNo() {
		return paProductGroupNoticeNo;
	}
	public void setPaProductGroupNoticeNo(String paProductGroupNoticeNo) {
		this.paProductGroupNoticeNo = paProductGroupNoticeNo;
	}
	public String getTransSaleYn() {
		return transSaleYn;
	}
	public void setTransSaleYn(String transSaleYn) {
		this.transSaleYn = transSaleYn;
	}
}
