package com.cware.netshopping.domain;

import com.cware.framework.core.basic.AbstractVO;

public class OrderShipCostDt extends AbstractVO{

	private static final long serialVersionUID = 1L;
	
	private String seq;	
	private String goodsCode;
	private String entpCode;
	private String shipCostNo;
	private String setGoodsCode;
	private String receiverSeq;
	
	
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getEntpCode() {
		return entpCode;
	}
	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}
	public String getShipCostNo() {
		return shipCostNo;
	}
	public void setShipCostNo(String shipCostNo) {
		this.shipCostNo = shipCostNo;
	}
	public String getSetGoodsCode() {
		return setGoodsCode;
	}
	public void setSetGoodsCode(String setGoodsCode) {
		this.setGoodsCode = setGoodsCode;
	}
	public String getReceiverSeq() {
		return receiverSeq;
	}
	public void setReceiverSeq(String receiverSeq) {
		this.receiverSeq = receiverSeq;
	}
	
	
	
	
}