package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaHalfGoods;

public class PaHalfGoodsVO extends PaHalfGoods {
	private static final long serialVersionUID = 1L; 

	private String prdGroupNo;
	private double salePrice;
	private String saleStartDate;
	private String saleEndDate;
	private int orderMaxQty;
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
	private String topImage;
	private String bottomImage;
	private String goodsCom;
	private String noticeExt;
	private Timestamp applyDate;
	private String entpCode;
	private String shipCostCode;
	private String collectImage;
	private long   priceApplySeq;
	private String currentTime;
	private String noShipJejuIsland;
	private String delyNoImage;
	private String lgroup;

	
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

	public String getLgroup() {
		return lgroup;
	}

	public void setLgroup(String lgroup) {
		this.lgroup = lgroup;
	}
	
	 
}
