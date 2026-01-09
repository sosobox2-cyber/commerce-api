package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaLtonCLaimList;

public class PaLtonClaimListVO extends PaLtonCLaimList {
	
	private static final long serialVersionUID = 1L;
	private String paCode;
	private String paOrderGb;
	private String claimStep;
	private long   claimQty;
	private String outClaimGb;
	
	private String goodsCode;
	private String goodsdtCode;
	
	
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsdtCode() {
		return goodsdtCode;
	}
	public void setGoodsdtCode(String goodsDtCode) {
		this.goodsdtCode = goodsDtCode;
	}
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
	public String getClaimStep() {
		return claimStep;
	}
	public void setClaimStep(String claimStep) {
		this.claimStep = claimStep;
	}
	
}
