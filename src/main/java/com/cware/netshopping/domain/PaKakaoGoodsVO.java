package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaKakaoGoods;

public class PaKakaoGoodsVO extends PaKakaoGoods {
	private static final long serialVersionUID = 1L;
	
	private String taxYn;
	private double salePrice;
	private double arsDcAmt;
	private double lumpSumDcAmt;
	private String shippingAddressId;
	private String returnAddressId;
	private double shipCostBaseAmt;
	private double ordCost;
	private double returnCost;
	private double changeCost;
	private String describeExt;
	private String asTelNo;
	private String topImage;
	private String bottomImage;
	private Timestamp applyDate;
	private String goodsCom;
	private String noticeExt;
	private double couponDcAmt;
	private int priceApplySeq;
	private String certYn;	
	private String noIslandYn;
	private double addIslandCost;
	private double addJejuCost;	
	private String lgroup;
	
	private String collectImage;
	private String kcInfo;
	
	public String getTaxYn() {
		return taxYn;
	}
	public void setTaxYn(String taxYn) {
		this.taxYn = taxYn;
	}
	public double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}
	public double getLumpSumDcAmt() {
		return lumpSumDcAmt;
	}
	public void setLumpSumDcAmt(double lumpSumDcAmt) {
		this.lumpSumDcAmt = lumpSumDcAmt;
	}
	public String getShippingAddressId() {
		return shippingAddressId;
	}
	public void setShippingAddressId(String shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}
	public String getReturnAddressId() {
		return returnAddressId;
	}
	public void setReturnAddressId(String returnAddressId) {
		this.returnAddressId = returnAddressId;
	}
	public double getShipCostBaseAmt() {
		return shipCostBaseAmt;
	}
	public void setShipCostBaseAmt(double shipCostBaseAmt) {
		this.shipCostBaseAmt = shipCostBaseAmt;
	}
	public double getOrdCost() {
		return ordCost;
	}
	public void setOrdCost(double ordCost) {
		this.ordCost = ordCost;
	}
	public double getReturnCost() {
		return returnCost;
	}
	public void setReturnCost(double returnCost) {
		this.returnCost = returnCost;
	}
	public double getChangeCost() {
		return changeCost;
	}
	public void setChangeCost(double changeCost) {
		this.changeCost = changeCost;
	}
	public String getDescribeExt() {
		return describeExt;
	}
	public void setDescribeExt(String describeExt) {
		this.describeExt = describeExt;
	}
	public String getAsTelNo() {
		return asTelNo;
	}
	public void setAsTelNo(String asTelNo) {
		this.asTelNo = asTelNo;
	}
	public Timestamp getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Timestamp applyDate) {
		this.applyDate = applyDate;
	}
	public String getTopImage() {
		return topImage;
	}
	public void setTopImage(String topImage) {
		this.topImage = topImage;
	}
	public String getBottomImage() {
		return bottomImage;
	}
	public void setBottomImage(String bottomImage) {
		this.bottomImage = bottomImage;
	}
	public String getGoodsCom() {
		return goodsCom;
	}
	public void setGoodsCom(String goodsCom) {
		this.goodsCom = goodsCom;
	}
	public String getNoticeExt() {
		return noticeExt;
	}
	public void setNoticeExt(String noticeExt) {
		this.noticeExt = noticeExt;
	}
	public double getArsDcAmt() {
		return arsDcAmt;
	}
	public void setArsDcAmt(double arsDcAmt) {
		this.arsDcAmt = arsDcAmt;
	}
	public double getCouponDcAmt() {
		return couponDcAmt;
	}
	public void setCouponDcAmt(double couponDcAmt) {
		this.couponDcAmt = couponDcAmt;
	}
	public int getPriceApplySeq() {
		return priceApplySeq;
	}
	public void setPriceApplySeq(int priceApplySeq) {
		this.priceApplySeq = priceApplySeq;
	}
	public String getCertYn() {
		return certYn;
	}
	public void setCertYn(String certYn) {
		this.certYn = certYn;
	}
	public String getNoIslandYn() {
		return noIslandYn;
	}
	public void setNoIslandYn(String noIslandYn) {
		this.noIslandYn = noIslandYn;
	}
	public double getAddIslandCost() {
		return addIslandCost;
	}
	public void setAddIslandCost(double addIslandCost) {
		this.addIslandCost = addIslandCost;
	}
	public double getAddJejuCost() {
		return addJejuCost;
	}
	public void setAddJejuCost(double addJejuCost) {
		this.addJejuCost = addJejuCost;
	}
	public String getCollectImage() {
		return collectImage;
	}
	public void setCollectImage(String collectImage) {
		this.collectImage = collectImage;
	}
	public String getLgroup() {
		return lgroup;
	}
	public void setLgroup(String lgroup) {
		this.lgroup = lgroup;
	}
	public String getKcInfo() {
		return kcInfo;
	}
	public void setKcInfo(String kcInfo) {
		this.kcInfo = kcInfo;
	}
	
}
