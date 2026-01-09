package com.cware.netshopping.pawemp.exchange.model;

public class SetClaimCompleteRequest {
	
	private long claimBundleNo;
	private String claimType;
	
	public long getClaimBundleNo() {
		return claimBundleNo;
	}
	public void setClaimBundleNo(long claimBundleNo) {
		this.claimBundleNo = claimBundleNo;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
}
