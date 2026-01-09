package com.cware.netshopping.pafaple.domain;

import com.cware.netshopping.pafaple.domain.model.PaFapleClaimList;

public class PaFapleClaimListVO extends PaFapleClaimList {

	private static final long serialVersionUID = 1L;
	
	private String paCode;
	private String paClaimGb;

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
}
