package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaLtonGoodsdtMapping extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String goodsCode;
	private String goodsdtCode;
	private String goodsdtSeq;
	private String paCode;
	private String paOptionCode;
	private String goodsdtInfo;
	private String useYn;
	private String transOrderAbleQty;
	private String transStockYn;
	private String transSaleYn;
	
	public String getTransStockYn() {
		return transStockYn;
	}
	public void setTransStockYn(String transStockYn) {
		this.transStockYn = transStockYn;
	}
	public String getTransSaleYn() {
		return transSaleYn;
	}
	public void setTransSaleYn(String transSaleYn) {
		this.transSaleYn = transSaleYn;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsdtCode() {
		return goodsdtCode;
	}
	public void setGoodsdtCode(String goodsdtCode) {
		this.goodsdtCode = goodsdtCode;
	}
	public String getGoodsdtSeq() {
		return goodsdtSeq;
	}
	public void setGoodsdtSeq(String goodsdtSeq) {
		this.goodsdtSeq = goodsdtSeq;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaOptionCode() {
		return paOptionCode;
	}
	public void setPaOptionCode(String paOptionCode) {
		this.paOptionCode = paOptionCode;
	}
	public String getGoodsdtInfo() {
		return goodsdtInfo;
	}
	public void setGoodsdtInfo(String goodsdtInfo) {
		this.goodsdtInfo = goodsdtInfo;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getTransOrderAbleQty() {
		return transOrderAbleQty;
	}
	public void setTransOrderAbleQty(String transOrderAbleQty) {
		this.transOrderAbleQty = transOrderAbleQty;
	}
	
}