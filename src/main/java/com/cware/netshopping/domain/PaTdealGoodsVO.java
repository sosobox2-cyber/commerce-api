package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaTdealGoods;

public class PaTdealGoodsVO extends PaTdealGoods {
	private static final long serialVersionUID = 1L; 

	private String prdGroupNo;
	private double salePrice;
	private String saleStartDate;
	private String saleEndDate;
	private int orderMaxQty;
	private int orderMinQty;
	private double weight;
	private String taxYn;
	private String adultYn;
	private String describeExt;
	private String imageUrl;
	private String imageP;
	private String imageAP;
	private String imageBP;
	private String imageCP;
	private String imageDP;
	private String imageList;
	private String imageMC;
	private String infoImageG;
	private String topImage;
	private String bottomImage;
	private String goodsCom;
	private String noticeExt;
	private Timestamp applyDate;
	private String entpCode;
	private String shipCostCode;
	private String collectImage;
	private String collectYn;
	private long   priceApplySeq;
	private String currentTime;
	private String noShipJejuIsland;
	private String delyNoImage;

	private String keyWord;
	private String returnNoYn;
	
	private String displayCategoryId;
	private String deliveryTemplateNo;
	
	private String custOrdQtyCheckYn;
	private int custOrdQtyCheckTerm;
	private int termOrderQty;
	
	private String lmsdCode;
	private Double commission;
	private String freshYn;
	
	private String brandName;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getFreshYn() {
		return freshYn;
	}

	public void setFreshYn(String freshYn) {
		this.freshYn = freshYn;
	}

	public String getImageList() {
		return imageList;
	}

	public void setImageList(String imageList) {
		this.imageList = imageList;
	}

	public String getImageMC() {
		return imageMC;
	}

	public void setImageMC(String imageMC) {
		this.imageMC = imageMC;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public int getTermOrderQty() {
		return termOrderQty;
	}

	public void setTermOrderQty(int termOrderQty) {
		this.termOrderQty = termOrderQty;
	}

	public String getCustOrdQtyCheckYn() {
		return custOrdQtyCheckYn;
	}

	public void setCustOrdQtyCheckYn(String custOrdQtyCheckYn) {
		this.custOrdQtyCheckYn = custOrdQtyCheckYn;
	}

	public int getCustOrdQtyCheckTerm() {
		return custOrdQtyCheckTerm;
	}

	public void setCustOrdQtyCheckTerm(int custOrdQtyCheckTerm) {
		this.custOrdQtyCheckTerm = custOrdQtyCheckTerm;
	}

	public String getPrdGroupNo() {
		return prdGroupNo;
	}

	public void setPrdGroupNo(String prdGroupNo) {
		this.prdGroupNo = prdGroupNo;
	}
	
	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public String getSaleStartDate() {
		return saleStartDate;
	}

	public void setSaleStartDate(String saleStartDate) {
		this.saleStartDate = saleStartDate;
	}

	public int getOrderMaxQty() {
		return orderMaxQty;
	}

	public void setOrderMaxQty(int orderMaxQty) {
		this.orderMaxQty = orderMaxQty;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getTaxYn() {
		return taxYn;
	}

	public void setTaxYn(String taxYn) {
		this.taxYn = taxYn;
	}

	public String getAdultYn() {
		return adultYn;
	}

	public void setAdultYn(String adultYn) {
		this.adultYn = adultYn;
	}

	public String getDescribeExt() {
		return describeExt;
	}

	public void setDescribeExt(String describeExt) {
		this.describeExt = describeExt;
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

	public String getSaleEndDate() {
		return saleEndDate;
	}

	public void setSaleEndDate(String saleEndDate) {
		this.saleEndDate = saleEndDate;
	}

	public Timestamp getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Timestamp applyDate) {
		this.applyDate = applyDate;
	}

	public String getEntpCode() {
		return entpCode;
	}

	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}

	public String getShipCostCode() {
		return shipCostCode;
	}

	public void setShipCostCode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}

	public long getPriceApplySeq() {
		return priceApplySeq;
	}

	public void setPriceApplySeq(long priceApplySeq) {
		this.priceApplySeq = priceApplySeq;
	}

	public String getCollectImage() {
		return collectImage;
	}

	public void setCollectImage(String collectImage) {
		this.collectImage = collectImage;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getNoShipJejuIsland() {
		return noShipJejuIsland;
	}

	public void setNoShipJejuIsland(String noShipJejuIsland) {
		this.noShipJejuIsland = noShipJejuIsland;
	}

	public String getDelyNoImage() {
		return delyNoImage;
	}

	public void setDelyNoImage(String delyNoImage) {
		this.delyNoImage = delyNoImage;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getReturnNoYn() {
		return returnNoYn;
	}

	public void setReturnNoYn(String returnNoYn) {
		this.returnNoYn = returnNoYn;
	}

	public int getOrderMinQty() {
		return orderMinQty;
	}

	public void setOrderMinQty(int orderMinQty) {
		this.orderMinQty = orderMinQty;
	}

	public String getDisplayCategoryId() {
		return displayCategoryId;
	}

	public void setDisplayCategoryId(String displayCategoryId) {
		this.displayCategoryId = displayCategoryId;
	}

	public String getDeliveryTemplateNo() {
		return deliveryTemplateNo;
	}

	public void setDeliveryTemplateNo(String deliveryTemplateNo) {
		this.deliveryTemplateNo = deliveryTemplateNo;
	}

	public String getCollectYn() {
		return collectYn;
	}

	public void setCollectYn(String collectYn) {
		this.collectYn = collectYn;
	}

	public String getLmsdCode() {
		return lmsdCode;
	}

	public void setLmsdCode(String lmsdCode) {
		this.lmsdCode = lmsdCode;
	}

	public String getInfoImageG() {
		return infoImageG;
	}

	public void setInfoImageG(String infoImageG) {
		this.infoImageG = infoImageG;
	}
	 
	
}
