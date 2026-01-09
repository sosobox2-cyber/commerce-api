package com.cware.netshopping.pawemp.claim.model;

public class SetClaimPending {
	private long claimBundleNo;
	private String claimType;
	private String pendingReasonCode;
	private String pendingReasonDetail;
	
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
	public String getPendingReasonCode() {
		return pendingReasonCode;
	}
	public void setPendingReasonCode(String pendingReasonCode) {
		this.pendingReasonCode = pendingReasonCode;
	}
	public String getPendingReasonDetail() {
		return pendingReasonDetail;
	}
	public void setPendingReasonDetail(String pendingReasonDetail) {
		this.pendingReasonDetail = pendingReasonDetail;
	}
}
