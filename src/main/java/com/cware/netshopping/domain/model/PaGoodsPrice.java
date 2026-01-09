package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaGoodsPrice extends AbstractModel {
	
	private static final long serialVersionUID = 1L;

	private String goodsCode;
	private String paCode;
	private Timestamp applyDate;
	private String transId;
	private Timestamp transDate;
	private String salePrice;
	private String supplyPrice;
	private String commision;
	private String dcAmt;
	private String transTargetYn;
	private Timestamp lastSyncDate;
	private String priceSeq;
	private String supplySeq;
	private String lumpSumDcAmt;
	private String lumpSumEntpDcAmt;
	private String lumpSumOwnDcAmt;
	
	
	public String getSupplySeq() {
		return supplySeq;
	}
	public void setSupplySeq(String supplySeq) {
		this.supplySeq = supplySeq;
	}
	public String getPriceSeq() {
		return priceSeq;
	}
	public void setPriceSeq(String priceSeq) {
		this.priceSeq = priceSeq;
	}
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
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public Timestamp getTransDate() {
		return transDate;
	}
	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public String getSupplyPrice() {
		return supplyPrice;
	}
	public void setSupplyPrice(String supplyPrice) {
		this.supplyPrice = supplyPrice;
	}
	public String getCommision() {
		return commision;
	}
	public void setCommision(String commision) {
		this.commision = commision;
	}
	public String getDcAmt() {
		return dcAmt;
	}
	public void setDcAmt(String dcAmt) {
		this.dcAmt = dcAmt;
	}
	public String getTransTargetYn() {
		return transTargetYn;
	}
	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}
	public Timestamp getLastSyncDate() {
		return lastSyncDate;
	}
	public void setLastSyncDate(Timestamp lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
	public String getLumpSumDcAmt() {
		return lumpSumDcAmt;
	}
	public void setLumpSumDcAmt(String lumpSumDcAmt) {
		this.lumpSumDcAmt = lumpSumDcAmt;
	}
	public String getLumpSumEntpDcAmt() {
		return lumpSumEntpDcAmt;
	}
	public void setLumpSumEntpDcAmt(String lumpSumEntpDcAmt) {
		this.lumpSumEntpDcAmt = lumpSumEntpDcAmt;
	}
	public String getLumpSumOwnDcAmt() {
		return lumpSumOwnDcAmt;
	}
	public void setLumpSumOwnDcAmt(String lumpSumOwnDcAmt) {
		this.lumpSumOwnDcAmt = lumpSumOwnDcAmt;
	}
	
	
}