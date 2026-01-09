package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class SpAuthTransInfo extends AbstractModel {
	
	/**
	 * SP_PAGOODS_SYNC_PROC [ CUR_GOODS_AUTH_TRANS ]
	 */

	private static final long serialVersionUID = 1L;
	private String goodsCode;
	private String paCode;
	private String paGroupCode;
	private String dateTime;
	
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
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
}
