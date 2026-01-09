package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Mobileordercancelhis extends AbstractModel {

	private static final long serialVersionUID = 1L;
	
	private String orderNo;
	private String orderGSeq;
	private String orderDSeq;
	private String orderWSeq;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderGSeq() {
		return orderGSeq;
	}
	public void setOrderGSeq(String orderGSeq) {
		this.orderGSeq = orderGSeq;
	}
	public String getOrderDSeq() {
		return orderDSeq;
	}
	public void setOrderDSeq(String orderDSeq) {
		this.orderDSeq = orderDSeq;
	}
	public String getOrderWSeq() {
		return orderWSeq;
	}
	public void setOrderWSeq(String orderWSeq) {
		this.orderWSeq = orderWSeq;
	}
}
