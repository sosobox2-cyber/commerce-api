package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;


public class PaGoodsAuthYnLog extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 

	private String paGroupCode;
	private String paCode;
	private String goodsCode;
	private String seqNo;
	private String autoYn;
	private String note;
	
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
	public String getAutoYn() {
		return autoYn;
	}
	public void setAutoYn(String autoYn) {
		this.autoYn = autoYn;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}