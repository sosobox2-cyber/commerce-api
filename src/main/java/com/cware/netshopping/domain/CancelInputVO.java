package com.cware.netshopping.domain;

import com.cware.framework.core.basic.AbstractVO;

public class CancelInputVO extends AbstractVO {
	private static final long serialVersionUID = 1L;
	
	private String mappingSeq;
	private String orderNo;
	private String orderGSeq;
	private int cancelQty;
	private String cancelCode;
	private String procId;
	private String paOrderNo;
	private String paGoodsCode;
	private String preCancelYn = "";
	
	
	public String getProcId() {
		return procId;
	}
	public void setProcId(String procId) {
		this.procId = procId;
	}
	public String getMappingSeq() {
		return mappingSeq;
	}
	public void setMappingSeq(String mappingSeq) {
		this.mappingSeq = mappingSeq;
	}
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
	public int getCancelQty() {
		return cancelQty;
	}
	public void setCancelQty(int cancelQty) {
		this.cancelQty = cancelQty;
	}
	public String getCancelCode() {
		return cancelCode;
	}
	public void setCancelCode(String cancelCode) {
		this.cancelCode = cancelCode;
	}
	public String getPaOrderNo() {
		return paOrderNo;
	}
	public void setPaOrderNo(String paOrderNo) {
		this.paOrderNo = paOrderNo;
	}
	public String getPaGoodsCode() {
		return paGoodsCode;
	}
	public void setPaGoodsCode(String paGoodsCode) {
		this.paGoodsCode = paGoodsCode;
	}
	public String getPreCancelYn() {
		return preCancelYn;
	}
	public void setPreCancelYn(String preCancelYn) {
		this.preCancelYn = preCancelYn;
	}
	
}
