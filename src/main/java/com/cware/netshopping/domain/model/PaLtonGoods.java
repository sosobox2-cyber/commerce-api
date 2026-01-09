package com.cware.netshopping.domain.model;

public class PaLtonGoods extends PaGoods {
	private static final long serialVersionUID = 1L; 

	private String paCode;
	private String paGroupCode;
	private String paSaleGb;
	private String transOrderAbleQty;
	private String paStatus;	
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
