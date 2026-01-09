package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaQeenClaimList;

public class PaQeenClaimListVO extends PaQeenClaimList {
	private static final long serialVersionUID = 1L; 
	private String paCode;
	private String paDoFlag;
	private String requiredDeposit;
	private String claimStatus;
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
	public String getRequiredDeposit() {
		return requiredDeposit;
	}
	public void setRequiredDeposit(String requiredDeposit) {
		this.requiredDeposit = requiredDeposit;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
}
