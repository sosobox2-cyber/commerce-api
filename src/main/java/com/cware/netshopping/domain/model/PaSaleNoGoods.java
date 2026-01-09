package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;


public class PaSaleNoGoods extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 

	private String paGroupCode;
	private String paCode;
	private String goodsCode;
	private String seqNo;
	private String paGoodsCode;
	private String productNo;
	private String paSaleGb;
	private String note;
	
	
	public String getPaGoodsCode() {
		return paGoodsCode;
	}
	public void setPaGoodsCode(String paGoodsCode) {
		this.paGoodsCode = paGoodsCode;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
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
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getPaSaleGb() {
		return paSaleGb;
	}
	public void setPaSaleGb(String paSaleGb) {
		this.paSaleGb = paSaleGb;
	}
	
	
}