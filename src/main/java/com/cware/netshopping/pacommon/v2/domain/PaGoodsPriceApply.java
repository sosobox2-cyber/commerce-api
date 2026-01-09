package com.cware.netshopping.pacommon.v2.domain;

import java.sql.Timestamp;

public class PaGoodsPriceApply  {

	private String goodsCode;
	private String paGroupCode;
	private String paCode;
	private Timestamp applyDate;
	private int priceApplySeq;

	private String priceSeq;
	private double salePrice;
	private double arsDcAmt;
	private double arsOwnDcAmt;
	private double arsEntpDcAmt;
	private double lumpSumDcAmt;
	private double lumpSumOwnDcAmt;
	private double lumpSumEntpDcAmt;

	private String couponPromoNo;
	private double couponDcAmt;
	private double couponOwnCost;
	private double couponEntpCost;

	private double supplyPrice;
	private double commission;

	private String insertId;
	private Timestamp insertDate;
	private String transId;
	private Timestamp transDate;

	private double bestPrice; // 최종혜택가

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

	public int getPriceApplySeq() {
		return priceApplySeq;
	}

	public void setPriceApplySeq(int priceApplySeq) {
		this.priceApplySeq = priceApplySeq;
	}

	public String getPriceSeq() {
		return priceSeq;
	}

	public void setPriceSeq(String priceSeq) {
		this.priceSeq = priceSeq;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public double getArsDcAmt() {
		return arsDcAmt;
	}

	public void setArsDcAmt(double arsDcAmt) {
		this.arsDcAmt = arsDcAmt;
	}

	public double getArsOwnDcAmt() {
		return arsOwnDcAmt;
	}

	public void setArsOwnDcAmt(double arsOwnDcAmt) {
		this.arsOwnDcAmt = arsOwnDcAmt;
	}

	public double getArsEntpDcAmt() {
		return arsEntpDcAmt;
	}

	public void setArsEntpDcAmt(double arsEntpDcAmt) {
		this.arsEntpDcAmt = arsEntpDcAmt;
	}

	public double getLumpSumDcAmt() {
		return lumpSumDcAmt;
	}

	public void setLumpSumDcAmt(double lumpSumDcAmt) {
		this.lumpSumDcAmt = lumpSumDcAmt;
	}

	public double getLumpSumOwnDcAmt() {
		return lumpSumOwnDcAmt;
	}

	public void setLumpSumOwnDcAmt(double lumpSumOwnDcAmt) {
		this.lumpSumOwnDcAmt = lumpSumOwnDcAmt;
	}

	public double getLumpSumEntpDcAmt() {
		return lumpSumEntpDcAmt;
	}

	public void setLumpSumEntpDcAmt(double lumpSumEntpDcAmt) {
		this.lumpSumEntpDcAmt = lumpSumEntpDcAmt;
	}

	public String getCouponPromoNo() {
		return couponPromoNo;
	}

	public void setCouponPromoNo(String couponPromoNo) {
		this.couponPromoNo = couponPromoNo;
	}

	public double getCouponDcAmt() {
		return couponDcAmt;
	}

	public void setCouponDcAmt(double couponDcAmt) {
		this.couponDcAmt = couponDcAmt;
	}

	public double getCouponOwnCost() {
		return couponOwnCost;
	}

	public void setCouponOwnCost(double couponOwnCost) {
		this.couponOwnCost = couponOwnCost;
	}

	public double getCouponEntpCost() {
		return couponEntpCost;
	}

	public void setCouponEntpCost(double couponEntpCost) {
		this.couponEntpCost = couponEntpCost;
	}

	public double getSupplyPrice() {
		return supplyPrice;
	}

	public void setSupplyPrice(double supplyPrice) {
		this.supplyPrice = supplyPrice;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public String getInsertId() {
		return insertId;
	}

	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}

	public Timestamp getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
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

	public double getBestPrice() {
		return bestPrice;
	}

	public void setBestPrice(double bestPrice) {
		this.bestPrice = bestPrice;
	}

	@Override
	public String toString() {
		return "PaGoodsPriceApply [goodsCode=" + goodsCode + ", paGroupCode=" + paGroupCode + ", paCode=" + paCode
				+ ", applyDate=" + applyDate + ", priceApplySeq=" + priceApplySeq + ", priceSeq=" + priceSeq
				+ ", salePrice=" + salePrice + ", arsDcAmt=" + arsDcAmt + ", arsOwnDcAmt=" + arsOwnDcAmt
				+ ", arsEntpDcAmt=" + arsEntpDcAmt + ", lumpSumDcAmt=" + lumpSumDcAmt + ", lumpSumOwnDcAmt="
				+ lumpSumOwnDcAmt + ", lumpSumEntpDcAmt=" + lumpSumEntpDcAmt + ", couponPromoNo=" + couponPromoNo
				+ ", couponDcAmt=" + couponDcAmt + ", couponOwnCost=" + couponOwnCost + ", couponEntpCost="
				+ couponEntpCost + ", supplyPrice=" + supplyPrice + ", commission=" + commission + ", insertId="
				+ insertId + ", insertDate=" + insertDate + ", transId=" + transId + ", transDate=" + transDate
				+ ", bestPrice=" + bestPrice + "]";
	}

}