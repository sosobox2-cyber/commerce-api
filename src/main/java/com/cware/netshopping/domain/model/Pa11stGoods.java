package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaGoods;

public class Pa11stGoods extends PaGoods {
	
	private static final long serialVersionUID = 1L; 

	private String goodsCode;
	private String paCode;
	private String paGroupCode;
	private String productNo;
	private String paSaleGb;
	private String transOrderAbleQty;
	private String paStatus;
	private String paLmsdKey;
	private String orgnTypCd;
	private String orgnTypDtlsCd;
	private String orgnNmVal;
	private String beefTraceNo;     
	private String medicalKey;    
	private String medicalRetail;
	private String medicalAd;
	private String transTargetYn;   
	private Timestamp lastSyncDate;
	private String termOrderQty;
	private int custOrdQtyCheckYn;
	private String returnNote;
	private String transSaleYn;
	
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
	public int getCustOrdQtyCheckYn() {
		return custOrdQtyCheckYn;
	}
	public void setCustOrdQtyCheckYn(int custOrdQtyCheckYn) {
		this.custOrdQtyCheckYn = custOrdQtyCheckYn;
	}
	public String getTermOrderQty() {
		return termOrderQty;
	}
	public void setTermOrderQty(String termOrderQty) {
		this.termOrderQty = termOrderQty;
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
	public String getPaLmsdKey() {
		return paLmsdKey;
	}
	public void setPaLmsdKey(String paLmsdKey) {
		this.paLmsdKey = paLmsdKey;
	}
	public String getOrgnTypCd() {
		return orgnTypCd;
	}
	public void setOrgnTypCd(String orgnTypCd) {
		this.orgnTypCd = orgnTypCd;
	}
	public String getOrgnTypDtlsCd() {
		return orgnTypDtlsCd;
	}
	public void setOrgnTypDtlsCd(String orgnTypDtlsCd) {
		this.orgnTypDtlsCd = orgnTypDtlsCd;
	}
	public String getOrgnNmVal() {
		return orgnNmVal;
	}
	public void setOrgnNmVal(String orgnNmVal) {
		this.orgnNmVal = orgnNmVal;
	}
	public String getBeefTraceNo() {
		return beefTraceNo;
	}
	public void setBeefTraceNo(String beefTraceNo) {
		this.beefTraceNo = beefTraceNo;
	}
	public String getMedicalKey() {
		return medicalKey;
	}
	public void setMedicalKey(String medicalKey) {
		this.medicalKey = medicalKey;
	}
	public String getMedicalRetail() {
		return medicalRetail;
	}
	public void setMedicalRetail(String medicalRetail) {
		this.medicalRetail = medicalRetail;
	}
	public String getMedicalAd() {
		return medicalAd;
	}
	public void setMedicalAd(String medicalAd) {
		this.medicalAd = medicalAd;
	}
	public String getTransTargetYn() {
		return transTargetYn;
	}
	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}
	public Timestamp getLastSyncDate() {
		return lastSyncDate;
	}
	public void setLastSyncDate(Timestamp lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}  
	
	
	
	
}