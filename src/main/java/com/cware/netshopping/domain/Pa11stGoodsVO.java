package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.Pa11stGoods;

public class Pa11stGoodsVO extends Pa11stGoods {
	private static final long serialVersionUID = 1L;
	// 상세기술서
	private String describeExt;
	private String describeExt1;
	// 배송비
	private String shipEntpCode;
	private long shipCostBaseAmt;
	private long ordCost;
	private long returnCost;
	private long changeCost;
	private long islandCost;
	private long jejuCost;
	// 출고지
	private String addrSeqOut;
	// 반품,회수지
	private String addrSeqIn;
	// 상품이미지
	private String paGroupCode;
	private String imageP;
	private String imageAP;
	private String imageBP;
	private String imageCP;
	private String imageDP;
	private String imageUrl;
	private String imageNm;
	private String imageChangeYn;
	// 상품가격
	private Timestamp applyDate;
	private String transId;
	private String transDate;
	private double salePrice;
	private double dcAmt;
	private double supplyPrice;
	private double commision;
	// 인증여부
	private String certYn;
	// 대분류
	private String paLgroup;
	// as안내,반품교환안내 전화번호
	private String csTel;
	private int shipCostCnt;
	private String brandNo;

	private double doCost;
	private double doOwnCost;
	private double doEntpCost;
	private String PromoNo;
	private String seq;

	private String installYn; // 발송 정책 템플릿 용(설치 배송 여부)
	private String paPolicyNo; // 발송 정책 템플릿 NO

	private String autoYn;

	// 일시불 금액
	private long lumpSumDcAmt;
	private long lumpSumOwnDcAmt;
	private long lumpSumEntpDcAmt;

	// 동영상 url
	private String vodUrl;
	private String vodDisplayGb;

	private String topImage;
	private String bottomImage;
	private long describeLen;
	private String collectImage;

	// 착불
	private String collectYn; // 이전에 사용하던 기준
    private String shipCostReceipt; // 신규 구분 기준

	private String transTargetYn;
	private String alcoutDealCode;

	private String massTargetYn; // 대량 입점

	// 11STGOODS TARGET관리
	private Timestamp lastModifyDate;

	private String noticeExt;
	private long noticeExtLen;
	private String goodsCom;
	private long goodsComLen;

	// 주문제작여부
	private String orderCreateYn;
	
	// 축산물 이력번호
	private String beefTraceYn;

	public String getMassTargetYn() {
		return massTargetYn;
	}

	public void setMassTargetYn(String massTargetYn) {
		this.massTargetYn = massTargetYn;
	}

	public String getCollectImage() {
		return collectImage;
	}

	public void setCollectImage(String collectImage) {
		this.collectImage = collectImage;
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

	public long getDescribeLen() {
		return describeLen;
	}

	public void setDescribeLen(long describeLen) {
		this.describeLen = describeLen;
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

	public String getDescribeExt1() {
		return describeExt1;
	}

	public void setDescribeExt1(String describeExt1) {
		this.describeExt1 = describeExt1;
	}

	public int getShipCostCnt() {
		return shipCostCnt;
	}

	public void setShipCostCnt(int shipCostCnt) {
		this.shipCostCnt = shipCostCnt;
	}

	public String getDescribeExt() {
		return describeExt;
	}

	public void setDescribeExt(String describeExt) {
		this.describeExt = describeExt;
	}

	public String getShipEntpCode() {
		return shipEntpCode;
	}

	public void setShipEntpCode(String shipEntpCode) {
		this.shipEntpCode = shipEntpCode;
	}

	public long getShipCostBaseAmt() {
		return shipCostBaseAmt;
	}

	public void setShipCostBaseAmt(long shipCostBaseAmt) {
		this.shipCostBaseAmt = shipCostBaseAmt;
	}

	public long getOrdCost() {
		return ordCost;
	}

	public void setOrdCost(long ordCost) {
		this.ordCost = ordCost;
	}

	public long getReturnCost() {
		return returnCost;
	}

	public void setReturnCost(long returnCost) {
		this.returnCost = returnCost;
	}

	public long getChangeCost() {
		return changeCost;
	}

	public void setChangeCost(long changeCost) {
		this.changeCost = changeCost;
	}

	public long getIslandCost() {
		return islandCost;
	}

	public void setIslandCost(long islandCost) {
		this.islandCost = islandCost;
	}

	public long getJejuCost() {
		return jejuCost;
	}

	public void setJejuCost(long jejuCost) {
		this.jejuCost = jejuCost;
	}

	public String getAddrSeqOut() {
		return addrSeqOut;
	}

	public void setAddrSeqOut(String addrSeqOut) {
		this.addrSeqOut = addrSeqOut;
	}

	public String getAddrSeqIn() {
		return addrSeqIn;
	}

	public void setAddrSeqIn(String addrSeqIn) {
		this.addrSeqIn = addrSeqIn;
	}

	public String getPaGroupCode() {
		return paGroupCode;
	}

	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
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

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
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

	public double getSupplyPrice() {
		return supplyPrice;
	}

	public void setSupplyPrice(double supplyPrice) {
		this.supplyPrice = supplyPrice;
	}

	public double getCommision() {
		return commision;
	}

	public void setCommision(double commision) {
		this.commision = commision;
	}

	public String getCertYn() {
		return certYn;
	}

	public void setCertYn(String certYn) {
		this.certYn = certYn;
	}

	public String getPaLgroup() {
		return paLgroup;
	}

	public void setPaLgroup(String paLgroup) {
		this.paLgroup = paLgroup;
	}

	public String getCsTel() {
		return csTel;
	}

	public void setCsTel(String csTel) {
		this.csTel = csTel;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getInstallYn() {
		return installYn;
	}

	public void setInstallYn(String installYn) {
		this.installYn = installYn;
	}

	public String getPaPolicyNo() {
		return paPolicyNo;
	}

	public void setPaPolicyNo(String paPolicyNo) {
		this.paPolicyNo = paPolicyNo;
	}

	public String getAutoYn() {
		return autoYn;
	}

	public void setAutoYn(String autoYn) {
		this.autoYn = autoYn;
	}

	public long getLumpSumDcAmt() {
		return lumpSumDcAmt;
	}

	public void setLumpSumDcAmt(long lumpSumDcAmt) {
		this.lumpSumDcAmt = lumpSumDcAmt;
	}

	public long getLumpSumOwnDcAmt() {
		return lumpSumOwnDcAmt;
	}

	public void setLumpSumOwnDcAmt(long lumpSumOwnDcAmt) {
		this.lumpSumOwnDcAmt = lumpSumOwnDcAmt;
	}

	public long getLumpSumEntpDcAmt() {
		return lumpSumEntpDcAmt;
	}

	public void setLumpSumEntpDcAmt(long lumpSumEntpDcAmt) {
		this.lumpSumEntpDcAmt = lumpSumEntpDcAmt;
	}

	public String getVodUrl() {
		return vodUrl;
	}

	public void setVodUrl(String vodUrl) {
		this.vodUrl = vodUrl;
	}

	public String getVodDisplayGb() {
		return vodDisplayGb;
	}

	public void setVodDisplayGb(String vodDisplayGb) {
		this.vodDisplayGb = vodDisplayGb;
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

	public String getTransTargetYn() {
		return transTargetYn;
	}

	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}

	public String getAlcoutDealCode() {
		return alcoutDealCode;
	}

	public void setAlcoutDealCode(String alcoutDealCode) {
		this.alcoutDealCode = alcoutDealCode;
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

	public long getNoticeExtLen() {
		return noticeExtLen;
	}

	public void setNoticeExtLen(long noticeExtLen) {
		this.noticeExtLen = noticeExtLen;
	}

	public String getGoodsCom() {
		return goodsCom;
	}

	public void setGoodsCom(String goodsCom) {
		this.goodsCom = goodsCom;
	}

	public long getGoodsComLen() {
		return goodsComLen;
	}

	public void setGoodsComLen(long goodsComLen) {
		this.goodsComLen = goodsComLen;
	}

	public String getOrderCreateYn() {
		return orderCreateYn;
	}

	public void setOrderCreateYn(String orderCreateYn) {
		this.orderCreateYn = orderCreateYn;
	}

	public String getImageChangeYn() {
		return imageChangeYn;
	}

	public void setImageChangeYn(String imageChangeYn) {
		this.imageChangeYn = imageChangeYn;
	}

	public String getBeefTraceYn() {
		return beefTraceYn;
	}

	public void setBeefTraceYn(String beefTraceYn) {
		this.beefTraceYn = beefTraceYn;
	}
}
