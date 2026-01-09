package com.cware.netshopping.domain;

import com.cware.framework.core.basic.AbstractModel;

public class PaIntpTargetVO extends AbstractModel {
	private static final long serialVersionUID = 1L;
	
	private String paClaimNo;
	private String paOrderNo;
	private String paShipNo;
	private String paOrderSeq;
	private String orderOptionNo;
	private long optionQty;
	private long targetCnt;
	private String paCode;
	
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
}
