package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaSsgOrderList;

public class PaSsgOrderListVO extends PaSsgOrderList {
	
	private static final long serialVersionUID = 1L;
	private String paCode;
	private String paOrderGb;
	private String goodsCode;
	private String goodsDtCode;
	
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
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsDtCode() {
		return goodsDtCode;
	}
	public void setGoodsDtCode(String goodsDtCode) {
		this.goodsDtCode = goodsDtCode;
	}
	
}
