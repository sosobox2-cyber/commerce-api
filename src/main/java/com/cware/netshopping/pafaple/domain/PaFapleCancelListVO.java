package com.cware.netshopping.pafaple.domain;

import com.cware.netshopping.pafaple.domain.model.PaFapleCancelList;

public class PaFapleCancelListVO extends PaFapleCancelList {

	private static final long serialVersionUID = 1L;
	
	private String paCode;
	private String paOrderGb;
	private String paProcQty;
	private String outBefClaimGb;
	private String orderNo; //w쇼핑 주문번호

	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getPaProcQty() {
		return paProcQty;
	}
	public void setPaProcQty(String paProcQty) {
		this.paProcQty = paProcQty;
	}
	public String getOutBefClaimGb() {
		return outBefClaimGb;
	}
	public void setOutBefClaimGb(String outBefClaimGb) {
		this.outBefClaimGb = outBefClaimGb;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}
