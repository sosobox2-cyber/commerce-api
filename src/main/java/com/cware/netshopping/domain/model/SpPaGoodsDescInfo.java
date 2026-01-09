package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class SpPaGoodsDescInfo extends AbstractModel {
	
	/**
	 * SP_PAGOODS_SYNC_PROC [ CUR_GOODS_DESCRIBE ]
	 */

	private static final long serialVersionUID = 1L;
	private String goodsCode;
	private Timestamp lastDescribeSyncDate;
	private String dateTime;
	
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public Timestamp getLastDescribeSyncDate() {
		return lastDescribeSyncDate;
	}
	public void setLastDescribeSyncDate(Timestamp lastDescribeSyncDate) {
		this.lastDescribeSyncDate = lastDescribeSyncDate;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
}
