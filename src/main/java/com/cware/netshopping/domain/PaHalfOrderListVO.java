package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaHalfOrderList;

public class PaHalfOrderListVO extends PaHalfOrderList {
	
	private static final long serialVersionUID = 1L;
	private String paCode;
	private String orderGb;
	
	private String orgPaOrderGb;
	
	public String getOrgPaOrderGb() {
		return orgPaOrderGb;
	}
	public void setOrgPaOrderGb(String orgPaOrderGb) {
		this.orgPaOrderGb = orgPaOrderGb;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getOrderGb() {
		return orderGb;
	}
	public void setOrderGb(String orderGb) {
		this.orderGb = orderGb;
	}
	
	
	
}
