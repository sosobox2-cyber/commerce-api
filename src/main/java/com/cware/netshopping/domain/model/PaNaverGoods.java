package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaNaverGoods extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 

	private String goodsCode;
	private String paCode;
	private String paGroupCode;
	private String productId;
	private String paSaleGb;
	private int transOrderAbleQty;
	private String paStatus;
	private String paLmsdKey;
	private String naverOriginCode;
	private String transTargetYn;
	private String transSaleYn;
	private String returnNote;
	
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getPaSaleGb() {
		return paSaleGb;
	}
	public void setPaSaleGb(String paSaleGb) {
		this.paSaleGb = paSaleGb;
	}
	public int getTransOrderAbleQty() {
		return transOrderAbleQty;
	}
	public void setTransOrderAbleQty(int transOrderAbleQty) {
		this.transOrderAbleQty = transOrderAbleQty;
	}
	public String getPaStatus() {
		return paStatus;
	}
	public void setPaStatus(String paStatus) {
		this.paStatus = paStatus;
	}
	public String getPaLmsdKey() {
		return paLmsdKey;
	}
	public void setPaLmsdKey(String paLmsdKey) {
		this.paLmsdKey = paLmsdKey;
	}
	public String getNaverOriginCode() {
		return naverOriginCode;
	}
	public void setNaverOriginCode(String naverOriginCode) {
		this.naverOriginCode = naverOriginCode;
	}
	public String getTransTargetYn() {
		return transTargetYn;
	}
	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}
	public String getTransSaleYn() {
		return transSaleYn;
	}
	public void setTransSaleYn(String transSaleYn) {
		this.transSaleYn = transSaleYn;
	}
	public String getReturnNote() {
		return returnNote;
	}
	public void setReturnNote(String returnNote) {
		this.returnNote = returnNote;
	}
}