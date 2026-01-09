package com.cware.netshopping.pafaple.domain;

import com.cware.netshopping.pafaple.domain.model.PaFapleOrderList;

public class PaFapleOrderListVO extends PaFapleOrderList {
	private static final long serialVersionUID = 1L;
	
	private String paCode;
	private String orderGb;
	private String goodsCode;
	private String goodsdtCode;
	private String mappingSeq;
	private String startDelivery;
	private String completeDate;
	private String deliveryCo;
	private String invoiceNum;
		
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
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsdtCode() {
		return goodsdtCode;
	}
	public void setGoodsdtCode(String goodsdtCode) {
		this.goodsdtCode = goodsdtCode;
	}
	public String getMappingSeq() {
		return mappingSeq;
	}
	public void setMappingSeq(String mappingSeq) {
		this.mappingSeq = mappingSeq;
	}
	public String getStartDelivery() {
		return startDelivery;
	}
	public void setStartDelivery(String startDelivery) {
		this.startDelivery = startDelivery;
	}
	public String getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}
	public String getDeliveryCo() {
		return deliveryCo;
	}
	public void setDeliveryCo(String deliveryCo) {
		this.deliveryCo = deliveryCo;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
}
