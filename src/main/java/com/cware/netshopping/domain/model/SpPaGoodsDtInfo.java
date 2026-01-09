package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class SpPaGoodsDtInfo extends AbstractModel {
	
	/**
	 * SP_PAGOODS_SYNC_PROC [ CUR_GOODSDT_INFO ]
	 */

	private static final long serialVersionUID = 1L;
	private String procGb;
	private String paCode;
	private String goodsCode;
	private String goodsdtCode;
	private String goodsdtInfo;
	private String goodsdtInfoKind;
	private String sortType;
	private String saleGb;
	private String dateTime;
	
	public String getProcGb() {
		return procGb;
	}
	public void setProcGb(String procGb) {
		this.procGb = procGb;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
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
	public String getGoodsdtInfo() {
		return goodsdtInfo;
	}
	public void setGoodsdtInfo(String goodsdtInfo) {
		this.goodsdtInfo = goodsdtInfo;
	}
	public String getGoodsdtInfoKind() {
		return goodsdtInfoKind;
	}
	public void setGoodsdtInfoKind(String goodsdtInfoKind) {
		this.goodsdtInfoKind = goodsdtInfoKind;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public String getSaleGb() {
		return saleGb;
	}
	public void setSaleGb(String saleGb) {
		this.saleGb = saleGb;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
}
