package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaTmonGoods;

public class PaTmonGoodsVO extends PaTmonGoods {

	private static final long serialVersionUID = 1L;

	private String paShipPolicyNo; 
	private String productType; 
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
	private Timestamp applyDate;
	private String paLgroup;
	private String originCode;
	private String delyGb;
	private String collectYn;
	
	private String goodsCom;
	private String noticeExt;
	
	private String collectImage;
	
	private int exceptYn;
	
	private String lgroup;
		
	public String getCollectYn() {
		return collectYn;
	}
	public void setCollectYn(String collectYn) {
		this.collectYn = collectYn;
	}
	public String getDelyGb() {
		return delyGb;
	}
	public void setDelyGb(String delyGb) {
		this.delyGb = delyGb;
	}
	public String getPaShipPolicyNo() {
		return paShipPolicyNo;
	}
	public void setPaShipPolicyNo(String paShipPolicyNo) {
		this.paShipPolicyNo = paShipPolicyNo;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
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
	public String getPaLgroup() {
		return paLgroup;
	}
	public void setPaLgroup(String paLgroup) {
		this.paLgroup = paLgroup;
	}
	public String getOriginCode() {
		return originCode;
	}
	public void setOriginCode(String originCode) {
		this.originCode = originCode;
	}
	public Timestamp getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Timestamp applyDate) {
		this.applyDate = applyDate;
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
	public int getExceptYn() {
		return exceptYn;
	}
	public void setExceptYn(int exceptYn) {
		this.exceptYn = exceptYn;
	}
	public String getLgroup() {
		return lgroup;
	}
	public void setLgroup(String lgroup) {
		this.lgroup = lgroup;
	}
	
}
