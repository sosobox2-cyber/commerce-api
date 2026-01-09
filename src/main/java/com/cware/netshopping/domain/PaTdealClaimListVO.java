package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaTdealClaimList;

public class PaTdealClaimListVO extends PaTdealClaimList {
	private static final long serialVersionUID = 1L; 
	private String paCode;
	private String paDoFlag;
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaDoFlag() {
		return paDoFlag;
	}
	public void setPaDoFlag(String paDoFlag) {
		this.paDoFlag = paDoFlag;
	}
	
	
}
