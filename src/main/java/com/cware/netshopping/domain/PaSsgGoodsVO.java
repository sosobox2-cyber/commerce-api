package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaSsgGoods;

public class PaSsgGoodsVO extends PaSsgGoods {
	private static final long serialVersionUID = 1L;
	
	private String salesEndDate;
	private String imageUrl;
	private String imageP;
	private String imageAP;
	private String imageBP;
	private String imageCP;
	private String imageDP;
	private String topImage;
	private String bottomImage;
	private String describeExt;
	private double salePrice;
	private double dcAmt;
	private double lumpSumDcAmt;
	private double lumpSumOwnDcAmt;
	private double lumpSumEntpDcAmt;
	private String originCode;
	private String collectYn;
	private String noIslandYn;
	private String noJejuYn;
	private String whoutCost;
	private String retCost;
	private String isCost;
	private String jejuCost;
	private String whoutAddr;
	private String snbAddr;
	private String shppItemDivCd;		//배송상품구분코드 
	private String importedYn;
	private Timestamp applyDate;
	private double feeRate;
	private String ssgSaleStartDate;
	private String ssgSaleEndDate;
	private String promoNo;
	private String promoSeq;
	private double promoDcAmt;
	private Timestamp transDate;
	private String taxYn;
	private String noReturnYn;
	private String goodsCom;
	private String noticeExt;
	private String ssgFoodYn;
	
	private String collectImage;
	
	public String getTaxYn() {
		return taxYn;
	}
	public void setTaxYn(String taxYn) {
		this.taxYn = taxYn;
	}
	public String getShppItemDivCd() {
		return shppItemDivCd;
	}
	public void setShppItemDivCd(String shppItemDivCd) {
		this.shppItemDivCd = shppItemDivCd;
	}
	public String getWhoutCost() {
		return whoutCost;
	}
	public void setWhoutCost(String whoutCost) {
		this.whoutCost = whoutCost;
	}
	public String getRetCost() {
		return retCost;
	}
	public void setRetCost(String retCost) {
		this.retCost = retCost;
	}
	public String getIsCost() {
		return isCost;
	}
	public void setIsCost(String isCost) {
		this.isCost = isCost;
	}
	public String getJejuCost() {
		return jejuCost;
	}
	public void setJejuCost(String jejuCost) {
		this.jejuCost = jejuCost;
	}
	public String getWhoutAddr() {
		return whoutAddr;
	}
	public void setWhoutAddr(String whoutAddr) {
		this.whoutAddr = whoutAddr;
	}
	public String getSnbAddr() {
		return snbAddr;
	}
	public void setSnbAddr(String snbAddr) {
		this.snbAddr = snbAddr;
	}
	public String getNoIslandYn() {
		return noIslandYn;
	}
	public void setNoIslandYn(String noIslandYn) {
		this.noIslandYn = noIslandYn;
	}
	public String getNoJejuYn() {
		return noJejuYn;
	}
	public void setNoJejuYn(String noJejuYn) {
		this.noJejuYn = noJejuYn;
	}
	public String getSalesEndDate() {
		return salesEndDate;
	}
	public void setSalesEndDate(String salesEndDate) {
		this.salesEndDate = salesEndDate;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageP() {
		return imageP;
	}
	public void setImageP(String imageP) {
		this.imageP = imageP;
	}
	public String getImageAP() {
		return imageAP;
	}
	public void setImageAP(String imageAP) {
		this.imageAP = imageAP;
	}
	public String getImageBP() {
		return imageBP;
	}
	public void setImageBP(String imageBP) {
		this.imageBP = imageBP;
	}
	public String getImageCP() {
		return imageCP;
	}
	public void setImageCP(String imageCP) {
		this.imageCP = imageCP;
	}
	public String getImageDP() {
		return imageDP;
	}
	public void setImageDP(String imageDP) {
		this.imageDP = imageDP;
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
	public String getDescribeExt() {
		return describeExt;
	}
	public void setDescribeExt(String describeExt) {
		this.describeExt = describeExt;
	}
	public double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}
	public double getDcAmt() {
		return dcAmt;
	}
	public void setDcAmt(double dcAmt) {
		this.dcAmt = dcAmt;
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
	public String getOriginCode() {
		return originCode;
	}
	public void setOriginCode(String originCode) {
		this.originCode = originCode;
	}
	public String getCollectYn() {
		return collectYn;
	}
	public void setCollectYn(String collectYn) {
		this.collectYn = collectYn;
	}
	public String getImportedYn() {
		return importedYn;
	}
	public void setImportedYn(String importedYn) {
		this.importedYn = importedYn;
	}
	public Timestamp getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Timestamp applyDate) {
		this.applyDate = applyDate;
	}
	public double getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(double feeRate) {
		this.feeRate = feeRate;
	}
	public String getSsgSaleStartDate() {
		return ssgSaleStartDate;
	}
	public void setSsgSaleStartDate(String ssgSaleStartDate) {
		this.ssgSaleStartDate = ssgSaleStartDate;
	}
	public String getSsgSaleEndDate() {
		return ssgSaleEndDate;
	}
	public void setSsgSaleEndDate(String ssgSaleEndDate) {
		this.ssgSaleEndDate = ssgSaleEndDate;
	}
	public String getPromoNo() {
		return promoNo;
	}
	public void setPromoNo(String promoNo) {
		this.promoNo = promoNo;
	}
	public String getPromoSeq() {
		return promoSeq;
	}
	public void setPromoSeq(String promoSeq) {
		this.promoSeq = promoSeq;
	}
	public double getPromoDcAmt() {
		return promoDcAmt;
	}
	public void setPromoDcAmt(double promoDcAmt) {
		this.promoDcAmt = promoDcAmt;
	}
	public Timestamp getTransDate() {
		return transDate;
	}
	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
	}
	public String getNoReturnYn() {
		return noReturnYn;
	}
	public void setNoReturnYn(String noReturnYn) {
		this.noReturnYn = noReturnYn;
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
	public String getCollectImage() {
		return collectImage;
	}
	public void setCollectImage(String collectImage) {
		this.collectImage = collectImage;
	}
	public String getSsgFoodYn() {
		return ssgFoodYn;
	}
	public void setSsgFoodYn(String ssgFoodYn) {
		this.ssgFoodYn = ssgFoodYn;
	}
	
}
