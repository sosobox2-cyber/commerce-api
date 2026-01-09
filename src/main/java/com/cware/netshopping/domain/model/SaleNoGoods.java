package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class SaleNoGoods extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String goodsCode;
	private String goodsdtCode;
	private String saleNoSeq;
	private String saleGb;
	private String saleNoCode;
	private String saleNoNote;
	
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
	public String getSaleNoSeq() {
		return saleNoSeq;
	}
	public void setSaleNoSeq(String saleNoSeq) {
		this.saleNoSeq = saleNoSeq;
	}
	public String getSaleGb() {
		return saleGb;
	}
	public void setSaleGb(String saleGb) {
		this.saleGb = saleGb;
	}
	public String getSaleNoCode() {
		return saleNoCode;
	}
	public void setSaleNoCode(String saleNoCode) {
		this.saleNoCode = saleNoCode;
	}
	public String getSaleNoNote() {
		return saleNoNote;
	}
	public void setSaleNoNote(String saleNoNote) {
		this.saleNoNote = saleNoNote;
	}
	
	
}
