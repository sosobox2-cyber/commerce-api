package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaGmktGoods;

public class PaGmktGoodsVO extends PaGmktGoods {

	private static final long serialVersionUID = 1L;

	private String itemNoExtra;
	private double doCost;
	private double doOwnCost;
	private double doEntpCost;
	private String PromoNo;
	private String seq;
	private String isModifyYn;
	//GOODS TARGET관리
    private Timestamp lastModifyDate;
    
	public String getPromoNo() {
		return PromoNo;
	}

	public void setPromoNo(String promoNo) {
		PromoNo = promoNo;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public double getDoCost() {
		return doCost;
	}

	public void setDoCost(double doCost) {
		this.doCost = doCost;
	}

	public double getDoOwnCost() {
		return doOwnCost;
	}

	public void setDoOwnCost(double doOwnCost) {
		this.doOwnCost = doOwnCost;
	}

	public double getDoEntpCost() {
		return doEntpCost;
	}

	public void setDoEntpCost(double doEntpCost) {
		this.doEntpCost = doEntpCost;
	}

	public String getItemNoExtra() {
		return itemNoExtra;
	}

	public void setItemNoExtra(String itemNoExtra) {
		this.itemNoExtra = itemNoExtra;
	}

	public String getIsModifyYn() {
		return isModifyYn;
	}

	public void setIsModifyYn(String isModifyYn) {
		this.isModifyYn = isModifyYn;
	}

	public Timestamp getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Timestamp lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}


	
}
