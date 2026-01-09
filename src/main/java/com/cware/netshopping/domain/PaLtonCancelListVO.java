package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaLtonCancelList;

public class PaLtonCancelListVO extends PaLtonCancelList {
	
	private static final long serialVersionUID = 1L;
	private String paCode;
	private String paOrderGb;
	private long   claimQty;
	private String outClaimGb;
	
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
	public long getClaimQty() {
		return claimQty;
	}
	public void setClaimQty(long claimQty) {
		this.claimQty = claimQty;
	}
	public String getOutClaimGb() {
		return outClaimGb;
	}
	public void setOutClaimGb(String outClaimGb) {
		this.outClaimGb = outClaimGb;
	}
}
