package com.cware.netshopping.pafaple.domain;

import com.cware.netshopping.pafaple.domain.model.PaFapleReturnList;

public class PaFapleReturnListVO extends PaFapleReturnList {

	private static final long serialVersionUID = 1L;
	
	private String paCode;
	private String paClaimGb;
	private String returningTypeCode;

	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaClaimGb() {
		return paClaimGb;
	}
	public void setPaClaimGb(String paClaimGb) {
		this.paClaimGb = paClaimGb;
	}
	public String getReturningTypeCode() {
		return returningTypeCode;
	}
	public void setReturningTypeCode(String returningTypeCode) {
		this.returningTypeCode = returningTypeCode;
	}
	
}
