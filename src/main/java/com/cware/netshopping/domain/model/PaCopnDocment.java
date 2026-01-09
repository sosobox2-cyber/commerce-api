package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaCopnDocment extends AbstractModel{
	
	private static final long serialVersionUID = 1L;
	
	private String paLmsdKey;
	private String docsSeq;
	private String templeteNm;
	private String requiredYn;
	
	public String getPaLmsdKey() {
		return paLmsdKey;
	}
	public void setPaLmsdKey(String paLmsdKey) {
		this.paLmsdKey = paLmsdKey;
	}
	public String getDocsSeq() {
		return docsSeq;
	}
	public void setDocsSeq(String docsSeq) {
		this.docsSeq = docsSeq;
	}
	public String getTempleteNm() {
		return templeteNm;
	}
	public void setTempleteNm(String templeteNm) {
		this.templeteNm = templeteNm;
	}
	public String getRequiredYn() {
		return requiredYn;
	}
	public void setRequiredYn(String requiredYn) {
		this.requiredYn = requiredYn;
	}

}
