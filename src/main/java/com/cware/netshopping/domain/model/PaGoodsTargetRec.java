package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaGoodsTargetRec extends AbstractModel {

	private static final long serialVersionUID = 1L;
	
	private String goodsCode;
	private String sourcingMedia;
	private String paGroupCode;
	private String paCode;
	private String paLmsdKey;
	private String originCode;
	private String offerType;
	private String shipCostCode;
	private String entpCode;
	private double marginRate;
	private double salePrice;
	private String eventYn;
	private String brandCode;
	private String lmsdCode;
	private String epgoodsName;
	private String goodsName;
	private String makecoCode;
	private String entpBuyMed;
	private String goodsNameMc;
	private String delyType;
	private String paLmsdnKey;
	private String makecoName;
	private String mdKind;
	private String mobileEtvYn;
	private String taxYn;
	
	//--------------------------------------
	
	private int shipCnt;
	private int offer_chk;
	private int describeChk;
	private int describeSubChk;
	private int imageChk;
	private int stockChk;
	private int shipcostChk;
	private int minDataChk;
	private int originChk;
	private int mobGiftGbChk;
	private int totalCnt;
	private int successCnt;
	private int failCnt;
	private String describeCode;
	private String orgnTypCd = "";
	private String originEnum = "";
	private String makerNo;
	private String newPagoodsYn;
	private String orgnTypDtlsCd = "";
	private String brandNo = "";          
	private String naverOriginCode = "";
	private String paOriginCode = "";
	private String lfDcatNo;
	
	private String codeGroup = "";
	private String compareGroup = "";

	private String massTargetYn;		//대량 입점 
	private String massTargetCode;		//대량 입점
	private String sourcingCode;
	
	//--------- 쿠팡 ---------

	private String paOfferTypeName;
	private int paCtgChk;
	private int paCtgEtcChk;
	private String paCtgEtcName;
	private int chkGoodsOffer;
	private int entpuserChk;
	private int goodsdtChk;
	private int goodsdtLength;

	//--------- 티몬 ---------
	private String categoryNo;
	private String brandName;
	
	public String getLfDcatNo() {
		return lfDcatNo;
	}
	public void setLfDcatNo(String lfDcatNo) {
		this.lfDcatNo = lfDcatNo;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getSourcingMedia() {
		return sourcingMedia;
	}
	public void setSourcingMedia(String sourcingMedia) {
		this.sourcingMedia = sourcingMedia;
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
	public String getPaLmsdKey() {
		return paLmsdKey;
	}
	public void setPaLmsdKey(String paLmsdKey) {
		this.paLmsdKey = paLmsdKey;
	}
	public String getOriginCode() {
		return originCode;
	}
	public void setOriginCode(String originCode) {
		this.originCode = originCode;
	}
	public String getOfferType() {
		return offerType;
	}
	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	public String getShipCostCode() {
		return shipCostCode;
	}
	public void setShipCostCode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}
	public String getEntpCode() {
		return entpCode;
	}
	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}
	public double getMarginRate() {
		return marginRate;
	}
	public void setMarginRate(double marginRate) {
		this.marginRate = marginRate;
	}
	public double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}
	public String getEventYn() {
		return eventYn;
	}
	public void setEventYn(String eventYn) {
		this.eventYn = eventYn;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getLmsdCode() {
		return lmsdCode;
	}
	public void setLmsdCode(String lmsdCode) {
		this.lmsdCode = lmsdCode;
	}
	public String getEpgoodsName() {
		return epgoodsName;
	}
	public void setEpgoodsName(String epgoodsName) {
		this.epgoodsName = epgoodsName;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getMakecoCode() {
		return makecoCode;
	}
	public void setMakecoCode(String makecoCode) {
		this.makecoCode = makecoCode;
	}
	public String getEntpBuyMed() {
		return entpBuyMed;
	}
	public void setEntpBuyMed(String entpBuyMed) {
		this.entpBuyMed = entpBuyMed;
	}
	public String getGoodsNameMc() {
		return goodsNameMc;
	}
	public void setGoodsNameMc(String goodsNameMc) {
		this.goodsNameMc = goodsNameMc;
	}
	public String getDelyType() {
		return delyType;
	}
	public void setDelyType(String delyType) {
		this.delyType = delyType;
	}
	public String getPaLmsdnKey() {
		return paLmsdnKey;
	}
	public void setPaLmsdnKey(String paLmsdnKey) {
		this.paLmsdnKey = paLmsdnKey;
	}
	public String getMakecoName() {
		return makecoName;
	}
	public void setMakecoName(String makecoName) {
		this.makecoName = makecoName;
	}
	public String getMdKind() {
		return mdKind;
	}
	public void setMdKind(String mdKind) {
		this.mdKind = mdKind;
	}
	public String getTaxYn() {
		return taxYn;
	}
	public void setTaxYn(String taxYn) {
		this.taxYn = taxYn;
	}
	
	//-----------------------------------------
	
	public int getShipCnt() {
		return shipCnt;
	}
	public void setShipCnt(int shipCnt) {
		this.shipCnt = shipCnt;
	}
	public int getOffer_chk() {
		return offer_chk;
	}
	public void setOffer_chk(int offer_chk) {
		this.offer_chk = offer_chk;
	}
	public int getDescribeChk() {
		return describeChk;
	}
	public void setDescribeChk(int describeChk) {
		this.describeChk = describeChk;
	}
	public int getDescribeSubChk() {
		return describeSubChk;
	}
	public void setDescribeSubChk(int describeSubChk) {
		this.describeSubChk = describeSubChk;
	}
	public int getImageChk() {
		return imageChk;
	}
	public void setImageChk(int imageChk) {
		this.imageChk = imageChk;
	}
	public int getStockChk() {
		return stockChk;
	}
	public void setStockChk(int stockChk) {
		this.stockChk = stockChk;
	}
	public int getShipcostChk() {
		return shipcostChk;
	}
	public void setShipcostChk(int shipcostChk) {
		this.shipcostChk = shipcostChk;
	}
	public int getMinDataChk() {
		return minDataChk;
	}
	public void setMinDataChk(int minDataChk) {
		this.minDataChk = minDataChk;
	}
	public int getOriginChk() {
		return originChk;
	}
	public void setOriginChk(int originChk) {
		this.originChk = originChk;
	}
	public int getMobGiftGbChk() {
		return mobGiftGbChk;
	}
	public void setMobGiftGbChk(int mobGiftGbChk) {
		this.mobGiftGbChk = mobGiftGbChk;
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public int getSuccessCnt() {
		return successCnt;
	}
	public void setSuccessCnt(int successCnt) {
		this.successCnt = successCnt;
	}
	public int getFailCnt() {
		return failCnt;
	}
	public void setFailCnt(int failCnt) {
		this.failCnt = failCnt;
	}
	public String getDescribeCode() {
		return describeCode;
	}
	public void setDescribeCode(String describeCode) {
		this.describeCode = describeCode;
	}
	public String getOrgnTypCd() {
		return orgnTypCd;
	}
	public void setOrgnTypCd(String orgnTypCd) {
		this.orgnTypCd = orgnTypCd;
	}
	public String getOriginEnum() {
		return originEnum;
	}
	public void setOriginEnum(String originEnum) {
		this.originEnum = originEnum;
	}
	public String getMakerNo() {
		return makerNo;
	}
	public void setMakerNo(String makerNo) {
		this.makerNo = makerNo;
	}
	public String getNewPagoodsYn() {
		return newPagoodsYn;
	}
	public void setNewPagoodsYn(String newPagoodsYn) {
		this.newPagoodsYn = newPagoodsYn;
	}
	public String getOrgnTypDtlsCd() {
		return orgnTypDtlsCd;
	}
	public void setOrgnTypDtlsCd(String orgnTypDtlsCd) {
		this.orgnTypDtlsCd = orgnTypDtlsCd;
	}
	public String getBrandNo() {
		return brandNo;
	}
	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
	public String getNaverOriginCode() {
		return naverOriginCode;
	}
	public void setNaverOriginCode(String naverOriginCode) {
		this.naverOriginCode = naverOriginCode;
	}
	public String getPaOriginCode() {
		return paOriginCode;
	}
	public void setPaOriginCode(String paOriginCode) {
		this.paOriginCode = paOriginCode;
	}
	public String getCodeGroup() {
		return codeGroup;
	}
	public void setCodeGroup(String codeGroup) {
		this.codeGroup = codeGroup;
	}
	public String getCompareGroup() {
		return compareGroup;
	}
	public void setCompareGroup(String compareGroup) {
		this.compareGroup = compareGroup;
	}
	public String getPaOfferTypeName() {
		return paOfferTypeName;
	}
	public void setPaOfferTypeName(String paOfferTypeName) {
		this.paOfferTypeName = paOfferTypeName;
	}
	public int getPaCtgChk() {
		return paCtgChk;
	}
	public void setPaCtgChk(int paCtgChk) {
		this.paCtgChk = paCtgChk;
	}
	public int getPaCtgEtcChk() {
		return paCtgEtcChk;
	}
	public void setPaCtgEtcChk(int paCtgEtcChk) {
		this.paCtgEtcChk = paCtgEtcChk;
	}
	public String getPaCtgEtcName() {
		return paCtgEtcName;
	}
	public void setPaCtgEtcName(String paCtgEtcName) {
		this.paCtgEtcName = paCtgEtcName;
	}
	public int getChkGoodsOffer() {
		return chkGoodsOffer;
	}
	public void setChkGoodsOffer(int chkGoodsOffer) {
		this.chkGoodsOffer = chkGoodsOffer;
	}
	public int getEntpuserChk() {
		return entpuserChk;
	}
	public void setEntpuserChk(int entpuserChk) {
		this.entpuserChk = entpuserChk;
	}
	public int getGoodsdtChk() {
		return goodsdtChk;
	}
	public void setGoodsdtChk(int goodsdtChk) {
		this.goodsdtChk = goodsdtChk;
	}
	public int getGoodsdtLength() {
		return goodsdtLength;
	}
	public void setGoodsdtLength(int goodsdtLength) {
		this.goodsdtLength = goodsdtLength;
	}

	
	//-------------------------------------------------
	
	public String getCategoryNo() {
		return categoryNo;
	}
	public void setCategoryNo(String categoryNo) {
		this.categoryNo = categoryNo;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getMassTargetYn() {
		return massTargetYn;
	}
	public void setMassTargetYn(String massTargetYn) {
		this.massTargetYn = massTargetYn;
	}
	public String getMassTargetCode() {
		return massTargetCode;
	}
	public void setMassTargetCode(String massTargetCode) {
		this.massTargetCode = massTargetCode;
	}
	public String getSourcingCode() {
		return sourcingCode;
	}
	public void setSourcingCode(String sourcingCode) {
		this.sourcingCode = sourcingCode;
	}
	public String getMobileEtvYn() {
		return mobileEtvYn;
	}
	public void setMobileEtvYn(String mobileEtvYn) {
		this.mobileEtvYn = mobileEtvYn;
	}
	
	
}
