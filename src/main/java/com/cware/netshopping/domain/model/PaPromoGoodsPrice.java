package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaPromoGoodsPrice extends AbstractModel {
	
	private static final long serialVersionUID = 1L;

	private String goodsCode;
	private String paCode;
	private Timestamp applyDate;
	private String promoSeq;
	private String alcoutPromoYn;
	private String transId;
	private String transDate;
	private String promoNo;
	private double doAmt;
	private double ownCost;
	private double entpCost;
	
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
	public Timestamp getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Timestamp applyDate) {
		this.applyDate = applyDate;
	}
	public String getPromoSeq() {
		return promoSeq;
	}
	public void setPromoSeq(String promoSeq) {
		this.promoSeq = promoSeq;
	}
	public String getAlcoutPromoYn() {
		return alcoutPromoYn;
	}
	public void setAlcoutPromoYn(String alcoutPromoYn) {
		this.alcoutPromoYn = alcoutPromoYn;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getPromoNo() {
		return promoNo;
	}
	public void setPromoNo(String promoNo) {
		this.promoNo = promoNo;
	}
	public double getDoAmt() {
		return doAmt;
	}
	public void setDoAmt(double doAmt) {
		this.doAmt = doAmt;
	}
	public double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(double ownCost) {
		this.ownCost = ownCost;
	}
	public double getEntpCost() {
		return entpCost;
	}
	public void setEntpCost(double entpCost) {
		this.entpCost = entpCost;
	}
}