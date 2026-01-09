package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

public class PaCopnGoods extends PaGoods {
	private static final long serialVersionUID = 1L; 

	private String generalProductName;
	private String sellerProductId;
	private String goodsCode;
	private String paCode;
	private String paGroupCode;
	private String productNo;
	private String paSaleGb;
	private String transOrderAbleQty;
	private String paStatus;
	private String displayCategoryCode;
	private String displayProductName;
	private String transTargetYn;
	private String transSaleYn;
	private Timestamp lastSyncDate;
	private String approvalStatus;   
	private String returnNote;
	private String paCopnDelyLeadtime;
    private String remark;
    private String certificationType;
    private String certificationCode;
    private int delynoAreaCnt;
	
	public String getGeneralProductName() {
		return generalProductName;
	}
	public void setGeneralProductName(String generalProductName) {
		this.generalProductName = generalProductName;
	}
	public String getSellerProductId() {
		return sellerProductId;
	}
	public void setSellerProductId(String sellerProductId) {
		this.sellerProductId = sellerProductId;
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
	public String getDisplayCategoryCode() {
		return displayCategoryCode;
	}
	public void setDisplayCategoryCode(String displayCategoryCode) {
		this.displayCategoryCode = displayCategoryCode;
	}
	public String getDisplayProductName() {
		return displayProductName;
	}
	public void setDisplayProductName(String displayProductName) {
		this.displayProductName = displayProductName;
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
	public Timestamp getLastSyncDate() {
		return lastSyncDate;
	}
	public void setLastSyncDate(Timestamp lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getReturnNote() {
		return returnNote;
	}
	public void setReturnNote(String returnNote) {
		this.returnNote = returnNote;
	}
	public String getPaCopnDelyLeadtime() {
		return paCopnDelyLeadtime;
	}
	public void setPaCopnDelyLeadtime(String paCopnDelyLeadtime) {
		this.paCopnDelyLeadtime = paCopnDelyLeadtime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCertificationType() {
		return certificationType;
	}
	public void setCertificationType(String certificationType) {
		this.certificationType = certificationType;
	}
	public String getCertificationCode() {
		return certificationCode;
	}
	public void setCertificationCode(String certificationCode) {
		this.certificationCode = certificationCode;
	}
	public int getDelynoAreaCnt() {
		return delynoAreaCnt;
	}
	public void setDelynoAreaCnt(int delynoAreaCnt) {
		this.delynoAreaCnt = delynoAreaCnt;
	}
	
}
