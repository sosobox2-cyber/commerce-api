package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaKakaoOrderList;

public class PaKakaoOrderListVO extends PaKakaoOrderList {
	
	private static final long serialVersionUID = 1L;

	private String paCode;
	private String paOrderGb;
	private String outBefClaimGb;
	private String claimStatus;
	private String paClaimGb;
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getOutBefClaimGb() {
		return outBefClaimGb;
	}
	public void setOutBefClaimGb(String outBefClaimGb) {
		this.outBefClaimGb = outBefClaimGb;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getPaClaimGb() {
		return paClaimGb;
	}
	public void setPaClaimGb(String paClaimGb) {
		this.paClaimGb = paClaimGb;
	}
	
}
