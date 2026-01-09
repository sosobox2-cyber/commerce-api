package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class SpPaOfferInfo extends AbstractModel {
	
	/**
	 * SP_PAGOODS_SYNC_PROC [ CUR_OFFER_INFO ]
	 */

	private static final long serialVersionUID = 1L;
	
	private String procGb;
	private String goodsCode;
	private String paGroupCode;
	private String offerType;
	private String offerCode;
	private String paOfferType;
	private String paOfferCode;
	private String offerExt;
	private String dateTime;
	
	public String getProcGb() {
		return procGb;
	}
	public void setProcGb(String procGb) {
		this.procGb = procGb;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getOfferType() {
		return offerType;
	}
	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	public String getOfferCode() {
		return offerCode;
	}
	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}
	public String getPaOfferType() {
		return paOfferType;
	}
	public void setPaOfferType(String paOfferType) {
		this.paOfferType = paOfferType;
	}
	public String getPaOfferCode() {
		return paOfferCode;
	}
	public void setPaOfferCode(String paOfferCode) {
		this.paOfferCode = paOfferCode;
	}
	public String getOfferExt() {
		return offerExt;
	}
	public void setOfferExt(String offerExt) {
		this.offerExt = offerExt;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
}
