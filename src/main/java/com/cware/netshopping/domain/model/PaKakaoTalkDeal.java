package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaKakaoTalkDeal extends AbstractModel {

	private static final long serialVersionUID = 1L;

	private String promoNo;
	private String goodsCode;
	private String dealId;
	private double price;
	private Timestamp dealBdate;
	private Timestamp dealEdate;
	private Timestamp transBdate;
	private Timestamp transEdate;
	private String useYn;
	
	public String getPromoNo() {
		return promoNo;
	}
	public void setPromoNo(String promoNo) {
		this.promoNo = promoNo;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getDealId() {
		return dealId;
	}
	public void setDealId(String dealId) {
		this.dealId = dealId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Timestamp getDealBdate() {
		return dealBdate;
	}
	public void setDealBdate(Timestamp dealBdate) {
		this.dealBdate = dealBdate;
	}
	public Timestamp getDealEdate() {
		return dealEdate;
	}
	public void setDealEdate(Timestamp dealEdate) {
		this.dealEdate = dealEdate;
	}
	public Timestamp getTransBdate() {
		return transBdate;
	}
	public void setTransBdate(Timestamp transBdate) {
		this.transBdate = transBdate;
	}
	public Timestamp getTransEdate() {
		return transEdate;
	}
	public void setTransEdate(Timestamp transEdate) {
		this.transEdate = transEdate;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	
}
