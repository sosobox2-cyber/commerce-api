package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaIntpGoods;

public class PaIntpGoodsVO extends PaIntpGoods {

    private static final long serialVersionUID = 1L;

    private String makecoName;
    private String originName;
    private String groupCode;
    private String paAddrSeq;
    private String paBrandName;
    
    private Timestamp applyDate;
    private double salePrice;
    private double dcAmt;
    
    //일시불 금액
  	private double lumpSumDcAmt;
  	private double lumpSumOwnDcAmt;
  	private double lumpSumEntpDcAmt;
  	
  	private double ordCost;
    private double returnCost;
    private double jejuCost;
    private double islandCost;
       
    private String topImage;
    private String bottomImage;
    private String describeExt;

    private String imageP; 
    private String imageAP;
    private String imageBP;
    private String imageCP;
    private String imageDP;
    private String imageUrl;
    private String imageNm;
    
    private String intpSaleStartDate;
    private String intpSaleEndDate;
    
    private String lifeCertYn;
	private String electricCertYn;
	private String childCertYn;
	private String medicalYn;
	private String healthYn;
	private String foodYn;
	private String collectYn;
	private String imageTransYn;
    
  	private Timestamp lastModifyDate;
  	private String massTargetYn;
  	
  	private String noticeExt;
  	private String goodsCom;
  	
  	private String collectImage;
  	
  	private String installYn;
  	
  	private String stoaBrandCode;
  	
  	private String lgroup;
  	
	public String getMassTargetYn() {
		return massTargetYn;
	}
	public void setMassTargetYn(String massTargetYn) {
		this.massTargetYn = massTargetYn;
	}
	public String getImageTransYn() {
		return imageTransYn;
	}
	public void setImageTransYn(String imageTransYn) {
		this.imageTransYn = imageTransYn;
	}
	public double getOrdCost() {
		return ordCost;
	}
	public void setOrdCost(double ordCost) {
		this.ordCost = ordCost;
	}
	public String getCollectYn() {
		return collectYn;
	}
	public void setCollectYn(String collectYn) {
		this.collectYn = collectYn;
	}
	public String getMakecoName() {
		return makecoName;
	}
	public void setMakecoName(String makecoName) {
		this.makecoName = makecoName;
	}
	public String getOriginName() {
		return originName;
	}
	public void setOriginName(String originName) {
		this.originName = originName;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getPaAddrSeq() {
		return paAddrSeq;
	}
	public void setPaAddrSeq(String paAddrSeq) {
		this.paAddrSeq = paAddrSeq;
	}
	public String getPaBrandName() {
		return paBrandName;
	}
	public void setPaBrandName(String paBrandName) {
		this.paBrandName = paBrandName;
	}
	public Timestamp getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Timestamp applyDate) {
		this.applyDate = applyDate;
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
	public double getReturnCost() {
		return returnCost;
	}
	public void setReturnCost(double returnCost) {
		this.returnCost = returnCost;
	}
	public double getJejuCost() {
		return jejuCost;
	}
	public void setJejuCost(double jejuCost) {
		this.jejuCost = jejuCost;
	}
	public double getIslandCost() {
		return islandCost;
	}
	public void setIslandCost(double islandCost) {
		this.islandCost = islandCost;
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
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageNm() {
		return imageNm;
	}
	public void setImageNm(String imageNm) {
		this.imageNm = imageNm;
	}
	public String getIntpSaleStartDate() {
		return intpSaleStartDate;
	}
	public void setIntpSaleStartDate(String intpSaleStartDate) {
		this.intpSaleStartDate = intpSaleStartDate;
	}
	public String getIntpSaleEndDate() {
		return intpSaleEndDate;
	}
	public void setIntpSaleEndDate(String intpSaleEndDate) {
		this.intpSaleEndDate = intpSaleEndDate;
	}
	public String getLifeCertYn() {
		return lifeCertYn;
	}
	public void setLifeCertYn(String lifeCertYn) {
		this.lifeCertYn = lifeCertYn;
	}
	public String getElectricCertYn() {
		return electricCertYn;
	}
	public void setElectricCertYn(String electricCertYn) {
		this.electricCertYn = electricCertYn;
	}
	public String getChildCertYn() {
		return childCertYn;
	}
	public void setChildCertYn(String childCertYn) {
		this.childCertYn = childCertYn;
	}
	public String getMedicalYn() {
		return medicalYn;
	}
	public void setMedicalYn(String medicalYn) {
		this.medicalYn = medicalYn;
	}
	public String getHealthYn() {
		return healthYn;
	}
	public void setHealthYn(String healthYn) {
		this.healthYn = healthYn;
	}
	public String getFoodYn() {
		return foodYn;
	}
	public void setFoodYn(String foodYn) {
		this.foodYn = foodYn;
	}
	public Timestamp getLastModifyDate() {
		return lastModifyDate;
	}
	public void setLastModifyDate(Timestamp lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	public String getNoticeExt() {
		return noticeExt;
	}
	public void setNoticeExt(String noticeExt) {
		this.noticeExt = noticeExt;
	}
	public String getGoodsCom() {
		return goodsCom;
	}
	public void setGoodsCom(String goodsCom) {
		this.goodsCom = goodsCom;
	}
	public String getCollectImage() {
		return collectImage;
	}
	public void setCollectImage(String collectImage) {
		this.collectImage = collectImage;
	}
	public String getInstallYn() {
		return installYn;
	}
	public void setInstallYn(String installYn) {
		this.installYn = installYn;
	}
	public String getStoaBrandCode() {
		return stoaBrandCode;
	}
	public void setStoaBrandCode(String stoaBrandCode) {
		this.stoaBrandCode = stoaBrandCode;
	}
	public String getLgroup() {
		return lgroup;
	}
	public void setLgroup(String lgroup) {
		this.lgroup = lgroup;
	}
	
}
