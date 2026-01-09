package com.cware.netshopping.domain;

public class PaLtonStockResponseVO {
	
	private static final long serialVersionUID = 1L;
	
	private String resultCode;
	private String resultMessage;
	private String spdNo;
	private String sitmNo;
	private int stkQty;
	private String orderYn;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public String getSpdNo() {
		return spdNo;
	}
	public void setSpdNo(String spdNo) {
		this.spdNo = spdNo;
	}
	public String getSitmNo() {
		return sitmNo;
	}
	public void setSitmNo(String sitmNo) {
		this.sitmNo = sitmNo;
	}
	public int getStkQty() {
		return stkQty;
	}
	public void setStkQty(int stkQty) {
		this.stkQty = stkQty;
	}
	public String getOrderYn() {
		return orderYn;
	}
	public void setOrderYn(String orderYn) {
		this.orderYn = orderYn;
	}
	
}
