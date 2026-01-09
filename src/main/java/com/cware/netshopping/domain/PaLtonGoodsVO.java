package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaLtonGoods;

public class PaLtonGoodsVO extends PaLtonGoods {

	private static final long serialVersionUID = 1L;

	private String trNo;
	private String lrtrNo;
	private String spdNo;
	private String epdNo;
	private String spdNm;
	private String scatNo;
	private String lfDcatNo;
	private String brdNo;
	private String mfcrNm;
	private String oplcCd;

	private Timestamp applyDate;
	private double salePrice;
	private double dcAmt;

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
	private String approvalStatus;

	private String imageP;
	private String imageAP;
	private String imageBP;
	private String imageCP;
	private String imageDP;
	private String imageUrl;
	private String imageChangeYn;

	private String slStrtDttm;
	private String slEndDttm;

	private int delynoAreaCnt;
	private String installYn;
	private String orderCreateYn;
	private String owhpNo;
	private String rtrpNo;
	private String groupCode; // 배송비정책번호
	private String dvCstPolNo; // 추가배송비정책번호
	private String newTransQty;

	private Timestamp lastModifyDate;
	private String massTargetYn;

	private String collectYn;
	private String shipCostReceipt; // 착불여부

	private String goodsCom;
	private String noticeExt;

	private String collectImage;

	private String returnNoYn;
	private String dlcrtYn;

	public String getMassTargetYn() {
		return massTargetYn;
	}

	public void setMassTargetYn(String massTargetYn) {
		this.massTargetYn = massTargetYn;
	}

	public String getNewTransQty() {
		return newTransQty;
	}

	public void setNewTransQty(String newTransQty) {
		this.newTransQty = newTransQty;
	}

	public String getTrNo() {
		return trNo;
	}

	public void setTrNo(String trNo) {
		this.trNo = trNo;
	}

	public String getLrtrNo() {
		return lrtrNo;
	}

	public void setLrtrNo(String lrtrNo) {
		this.lrtrNo = lrtrNo;
	}

	public String getSpdNo() {
		return spdNo;
	}

	public void setSpdNo(String spdNo) {
		this.spdNo = spdNo;
	}

	public String getEpdNo() {
		return epdNo;
	}

	public void setEpdNo(String epdNo) {
		this.epdNo = epdNo;
	}

	public String getSpdNm() {
		return spdNm;
	}

	public void setSpdNm(String spdNm) {
		this.spdNm = spdNm;
	}

	public String getScatNo() {
		return scatNo;
	}

	public void setScatNo(String scatNo) {
		this.scatNo = scatNo;
	}

	public String getLfDcatNo() {
		return lfDcatNo;
	}

	public void setLfDcatNo(String lfDcatNo) {
		this.lfDcatNo = lfDcatNo;
	}

	public String getBrdNo() {
		return brdNo;
	}

	public void setBrdNo(String brdNo) {
		this.brdNo = brdNo;
	}

	public String getMfcrNm() {
		return mfcrNm;
	}

	public void setMfcrNm(String mfcrNm) {
		this.mfcrNm = mfcrNm;
	}

	public String getOplcCd() {
		return oplcCd;
	}

	public void setOplcCd(String oplcCd) {
		this.oplcCd = oplcCd;
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

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
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

	public String getSlStrtDttm() {
		return slStrtDttm;
	}

	public void setSlStrtDttm(String slStrtDttm) {
		this.slStrtDttm = slStrtDttm;
	}

	public String getSlEndDttm() {
		return slEndDttm;
	}

	public void setSlEndDttm(String slEndDttm) {
		this.slEndDttm = slEndDttm;
	}

	public int getDelynoAreaCnt() {
		return delynoAreaCnt;
	}

	public void setDelynoAreaCnt(int delynoAreaCnt) {
		this.delynoAreaCnt = delynoAreaCnt;
	}

	public String getInstallYn() {
		return installYn;
	}

	public void setInstallYn(String installYn) {
		this.installYn = installYn;
	}

	public String getOrderCreateYn() {
		return orderCreateYn;
	}

	public void setOrderCreateYn(String orderCreateYn) {
		this.orderCreateYn = orderCreateYn;
	}

	public String getOwhpNo() {
		return owhpNo;
	}

	public void setOwhpNo(String owhpNo) {
		this.owhpNo = owhpNo;
	}

	public String getRtrpNo() {
		return rtrpNo;
	}

	public void setRtrpNo(String rtrpNo) {
		this.rtrpNo = rtrpNo;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getDvCstPolNo() {
		return dvCstPolNo;
	}

	public void setDvCstPolNo(String dvCstPolNo) {
		this.dvCstPolNo = dvCstPolNo;
	}

	public Timestamp getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Timestamp lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public String getCollectYn() {
		return collectYn;
	}

	public void setCollectYn(String collectYn) {
		this.collectYn = collectYn;
	}
	
	public String getShipCostReceipt() {
		return shipCostReceipt;
	}

	public void setShipCostReceipt(String shipCostReceipt) {
		this.shipCostReceipt = shipCostReceipt;
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

	public String getReturnNoYn() {
		return returnNoYn;
	}

	public void setReturnNoYn(String returnNoYn) {
		this.returnNoYn = returnNoYn;
	}

	public String getImageChangeYn() {
		return imageChangeYn;
	}

	public void setImageChangeYn(String imageChangeYn) {
		this.imageChangeYn = imageChangeYn;
	}

	public String getDlcrtYn() {
		return dlcrtYn;
	}

	public void setDlcrtYn(String dlcrtYn) {
		this.dlcrtYn = dlcrtYn;
	}
}
