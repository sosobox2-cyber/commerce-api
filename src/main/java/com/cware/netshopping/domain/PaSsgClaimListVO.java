package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaSsgClaimList;

public class PaSsgClaimListVO extends PaSsgClaimList implements Cloneable {
	
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
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
