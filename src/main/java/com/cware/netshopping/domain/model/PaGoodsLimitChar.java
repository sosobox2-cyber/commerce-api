package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaGoodsLimitChar extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 

	private String seq;
	private String paGroupCode;
	private String limitChar;
	private String replaceChar;
	private String useYn;
	private String addChar;
	
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getLimitChar() {
		return limitChar;
	}
	public void setLimitChar(String limitChar) {
		this.limitChar = limitChar;
	}
	public String getReplaceChar() {
		return replaceChar;
	}
	public void setReplaceChar(String replaceChar) {
		this.replaceChar = replaceChar;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getAddChar() {
		return addChar;
	}
	public void setAddChar(String addChar) {
		this.addChar = addChar;
	}
}