package com.cware.netshopping.pafaple.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaFapleQna extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String bbsKind;
	private String bbsId;
	private String numm	;
	private String ref;
	private String step	;
	private String relevel;
	
	public String getBbsKind() {
		return bbsKind;
	}
	public void setBbsKind(String bbsKind) {
		this.bbsKind = bbsKind;
	}
	public String getBbsId() {
		return bbsId;
	}
	public void setBbsId(String bbsId) {
		this.bbsId = bbsId;
	}
	public String getNumm() {
		return numm;
	}
	public void setNumm(String numm) {
		this.numm = numm;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getRelevel() {
		return relevel;
	}
	public void setRelevel(String relevel) {
		this.relevel = relevel;
	}
	
}
