package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Planshipcost extends AbstractModel {
	private static final long serialVersionUID = 1L;

	private String orderNo;
	private String orderGSeq;
	private String orderDSeq;
	private String orderWSeq;
	private String receiverSeq;
	private String type;
	private String entpCode;
	private String delyType;
	private String shpfeeCode;
	private double shipfeeCost;
	private String confirmYn;
	private String cancelYn;
	private String claimCancelYn;
	private String refundYn;
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
	public String getReceiverSeq() {
		return receiverSeq;
	}
	public void setReceiverSeq(String receiverSeq) {
		this.receiverSeq = receiverSeq;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEntpCode() {
		return entpCode;
	}
	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}
	public String getDelyType() {
		return delyType;
	}
	public void setDelyType(String delyType) {
		this.delyType = delyType;
	}
	public String getShpfeeCode() {
		return shpfeeCode;
	}
	public void setShpfeeCode(String shpfeeCode) {
		this.shpfeeCode = shpfeeCode;
	}
	public double getShipfeeCost() {
		return shipfeeCost;
	}
	public void setShipfeeCost(double shipfeeCost) {
		this.shipfeeCost = shipfeeCost;
	}
	public String getConfirmYn() {
		return confirmYn;
	}
	public void setConfirmYn(String confirmYn) {
		this.confirmYn = confirmYn;
	}
	public String getCancelYn() {
		return cancelYn;
	}
	public void setCancelYn(String cancelYn) {
		this.cancelYn = cancelYn;
	}
	public String getClaimCancelYn() {
		return claimCancelYn;
	}
	public void setClaimCancelYn(String claimCancelYn) {
		this.claimCancelYn = claimCancelYn;
	}
	public String getRefundYn() {
		return refundYn;
	}
	public void setRefundYn(String refundYn) {
		this.refundYn = refundYn;
	}
	
	

}
