package com.cware.netshopping.domain;

import com.cware.framework.core.basic.AbstractModel;

public class PaWempTargetVO extends AbstractModel {
	private static final long serialVersionUID = 1L;
	
	private String paClaimNo;
	private String paOrderNo;
	private String paShipNo;
	private String paOrderSeq;
	private String orderOptionNo;
	private long optionQty;
	private long targetCnt;
	private String paCode;
	private String goodsCode;
	private String goodsdtCode;
	private String productNo;
	private String orderCancelYn;
	
	public String getPaClaimNo() {
		return paClaimNo;
	}
	public void setPaClaimNo(String paClaimNo) {
		this.paClaimNo = paClaimNo;
	}
	public String getPaOrderNo() {
		return paOrderNo;
	}
	public void setPaOrderNo(String paOrderNo) {
		this.paOrderNo = paOrderNo;
	}
	public String getPaShipNo() {
		return paShipNo;
	}
	public void setPaShipNo(String paShipNo) {
		this.paShipNo = paShipNo;
	}
	public String getPaOrderSeq() {
		return paOrderSeq;
	}
	public void setPaOrderSeq(String paOrderSeq) {
		this.paOrderSeq = paOrderSeq;
	}
	public String getOrderOptionNo() {
		return orderOptionNo;
	}
	public void setOrderOptionNo(String orderOptionNo) {
		this.orderOptionNo = orderOptionNo;
	}
	public long getOptionQty() {
		return optionQty;
	}
	public void setOptionQty(long optionQty) {
		this.optionQty = optionQty;
	}
	public long getTargetCnt() {
		return targetCnt;
	}
	public void setTargetCnt(long targetCnt) {
		this.targetCnt = targetCnt;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
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
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getOrderCancelYn() {
		return orderCancelYn;
	}
	public void setOrderCancelYn(String orderCancelYn) {
		this.orderCancelYn = orderCancelYn;
	}
	
}
