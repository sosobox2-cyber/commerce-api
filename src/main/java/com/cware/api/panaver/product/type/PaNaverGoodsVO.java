package com.cware.api.panaver.product.type;

import java.sql.Date;
import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaNaverGoodsVO extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 
	
	//상품 TGOODS
	private String goodsCode;
	private String entpCode;
	private int termOrderQty;
	private String custOrdQtyCheckYN;
	private String lmsdKey;
	private String installYN;
	private Date saleStartDate;
	private Date saleEndDate;
	private String orderMakeYN; 
	private String mixpackYN;

	//오픈마켓 상품(공통) TPAGOODS
	private String brandName;
	private String taxYN;
	private String TaxSmallYN;
	private String adultYN;
	private int orderMinQty;
	private int orderMaxQty;
	private int custOrdQtyCheckTerm;
	private String doNotIslandDelyYN;
	private String originName;
	private String saleGb;
	
	//네이버 상품 TPANAVERGOODS
	private String paCode;
	private String paGroupCode;
	private String productId;
	private String paStatus;
	private String paLmsdKey;
	private String paSaleGb;
	private int transOrderAbleQty;
	private String naverOriginCode;
	private String transTargetYn;
	private String transSaleYn;
	private String returnNote;
	private String paGoodsName;
	//네이버 상품 '원상품코드'
	private String orgProductNo;
	
	
	//네이버 상품이미지 TPANAVERGOODSIMAGE
	private String imageNaverP;
	private String imageNaverAp;
	private String imageNaverBp;
	private String imageNaverCp;
	private String imageNaverDp;
	
	//오픈마켓 상품가격(공통) TPAGOODSPRICE
	private Date applyDate;
	private int salePrice;
	private int dcAmt;

	//오픈마켓 배송비(공통) TPACUSTSHIPCOST
	private String shipEntpCode;
	private String shipCostCode;
	private int ordCost;
	private int returnCost;
	private int changeCost;
	private int shipCostBaseAmt;
	private int jejuCost;
	private int islandCost;
	
	//상품부가정보 TGOODSADDINFO
	private String orderCreateYN;
	
	//공통코드 TCODE
	private String csTel;
	private String csDetail;
	
	//제조업체 TMAKECOMP
	private String makecoCode;
	private String makecoName;
	
	//단품(공통) TPAGOODSDT
	private int goodsdtCnt;
	
	// 인증코드 ( KC, GRN, CHI)
	private String exceptCode;

	// 프로모션
	private int lumpSumDcAmt;
	private int lumpSumEntpDcAmt;
	private int lumpSumOwnDcAmt;
	
	//상품 모델명
	private String modelNo;
	
	private String describeExt;
	private String topImage;
    private String bottomImage;
    private String collectImage;
    
    private String collectYn;
    
    private String noticeExt;
    private String goodsCom;
    
    // TARGET관리
    private Timestamp lastModifyDate;
    
    // 예외인증 여부
    private int exceptYn;
    
	public String getDescribeExt() {
		return describeExt;
	}

	public void setDescribeExt(String describeExt) {
		this.describeExt = describeExt;
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

	public String getCollectImage() {
		return collectImage;
	}

	public void setCollectImage(String collectImage) {
		this.collectImage = collectImage;
	}

	public String getExceptCode() {
		return exceptCode;
	}

	public void setExceptCode(String exceptCode) {
		this.exceptCode = exceptCode;
	}

	public String getCsDetail() {
		return csDetail;
	}

	public void setCsDetail(String csDetail) {
		this.csDetail = csDetail;
	}

	public String getSaleGb() {
		return saleGb;
	}

	public void setSaleGb(String saleGb) {
		this.saleGb = saleGb;
	}

	public String getMakecoName() {
		return makecoName;
	}

	public void setMakecoName(String makecoName) {
		this.makecoName = makecoName;
	}

	public String getShipCostCode() {
		return shipCostCode;
	}

	public void setShipCostCode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}

	public int getGoodsdtCnt() {
		return goodsdtCnt;
	}

	public void setGoodsdtCnt(int goodsdtCnt) {
		this.goodsdtCnt = goodsdtCnt;
	}

	public String getTransTargetYn() {
		return transTargetYn;
	}

	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}

	public String getTransSaleYn() {
		return transSaleYn;
	}

	public void setTransSaleYn(String transSaleYn) {
		this.transSaleYn = transSaleYn;
	}

	public String getReturnNote() {
		return returnNote;
	}

	public void setReturnNote(String returnNote) {
		this.returnNote = returnNote;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getMixpackYN() {
		return mixpackYN;
	}

	public void setMixpackYN(String mixpackYN) {
		this.mixpackYN = mixpackYN;
	}

	public String getLmsdKey() {
		return lmsdKey;
	}

	public void setLmsdKey(String lmsdKey) {
		this.lmsdKey = lmsdKey;
	}

	public String getMakecoCode() {
		return makecoCode;
	}

	public void setMakecoCode(String makecoCode) {
		this.makecoCode = makecoCode;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getPaGoodsName() {
		return paGoodsName;
	}

	public void setPaGoodsName(String paGoodsName) {
		this.paGoodsName = paGoodsName;
	}
	
	public String getOrgProductNo() {
		return orgProductNo;
	}

	public void setOrgProductNo(String orgProductNo) {
		this.orgProductNo = orgProductNo;
	}

	public String getEntpCode() {
		return entpCode;
	}

	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}

	public int getTermOrderQty() {
		return termOrderQty;
	}

	public void setTermOrderQty(int termOrderQty) {
		this.termOrderQty = termOrderQty;
	}

	public String getCustOrdQtyCheckYN() {
		return custOrdQtyCheckYN;
	}

	public void setCustOrdQtyCheckYN(String custOrdQtyCheckYN) {
		this.custOrdQtyCheckYN = custOrdQtyCheckYN;
	}

	public String getInstallYN() {
		return installYN;
	}

	public void setInstallYN(String installYN) {
		this.installYN = installYN;
	}

	public Date getSaleStartDate() {
		return saleStartDate;
	}

	public void setSaleStartDate(Date saleStartDate) {
		this.saleStartDate = saleStartDate;
	}

	public Date getSaleEndDate() {
		return saleEndDate;
	}

	public void setSaleEndDate(Date saleEndDate) {
		this.saleEndDate = saleEndDate;
	}

	public String getOrderMakeYN() {
		return orderMakeYN;
	}

	public void setOrderMakeYN(String orderMakeYN) {
		this.orderMakeYN = orderMakeYN;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getTaxYN() {
		return taxYN;
	}

	public void setTaxYN(String taxYN) {
		this.taxYN = taxYN;
	}

	public String getTaxSmallYN() {
		return TaxSmallYN;
	}

	public void setTaxSmallYN(String taxSmallYN) {
		TaxSmallYN = taxSmallYN;
	}

	public String getAdultYN() {
		return adultYN;
	}

	public void setAdultYN(String adultYN) {
		this.adultYN = adultYN;
	}

	public int getOrderMinQty() {
		return orderMinQty;
	}

	public void setOrderMinQty(int orderMinQty) {
		this.orderMinQty = orderMinQty;
	}

	public int getOrderMaxQty() {
		return orderMaxQty;
	}

	public void setOrderMaxQty(int orderMaxQty) {
		this.orderMaxQty = orderMaxQty;
	}

	public int getCustOrdQtyCheckTerm() {
		return custOrdQtyCheckTerm;
	}

	public void setCustOrdQtyCheckTerm(int custOrdQtyCheckTerm) {
		this.custOrdQtyCheckTerm = custOrdQtyCheckTerm;
	}

	public String getDoNotIslandDelyYN() {
		return doNotIslandDelyYN;
	}

	public void setDoNotIslandDelyYN(String doNotIslandDelyYN) {
		this.doNotIslandDelyYN = doNotIslandDelyYN;
	}

	public String getNaverOriginCode() {
		return naverOriginCode;
	}

	public void setNaverOriginCode(String naverOriginCode) {
		this.naverOriginCode = naverOriginCode;
	}

	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}

	public String getPaGroupCode() {
		return paGroupCode;
	}

	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPaStatus() {
		return paStatus;
	}

	public void setPaStatus(String paStatus) {
		this.paStatus = paStatus;
	}

	public String getPaLmsdKey() {
		return paLmsdKey;
	}

	public void setPaLmsdKey(String paLmsdKey) {
		this.paLmsdKey = paLmsdKey;
	}

	public String getPaSaleGb() {
		return paSaleGb;
	}

	public void setPaSaleGb(String paSaleGb) {
		this.paSaleGb = paSaleGb;
	}

	public int getTransOrderAbleQty() {
		return transOrderAbleQty;
	}

	public void setTransOrderAbleQty(int transOrderAbleQty) {
		this.transOrderAbleQty = transOrderAbleQty;
	}

	public String getImageNaverP() {
		return imageNaverP;
	}

	public void setImageNaverP(String imageNaverP) {
		this.imageNaverP = imageNaverP;
	}

	public String getImageNaverAp() {
		return imageNaverAp;
	}

	public void setImageNaverAp(String imageNaverAp) {
		this.imageNaverAp = imageNaverAp;
	}

	public String getImageNaverBp() {
		return imageNaverBp;
	}

	public void setImageNaverBp(String imageNaverBp) {
		this.imageNaverBp = imageNaverBp;
	}

	public String getImageNaverCp() {
		return imageNaverCp;
	}

	public void setImageNaverCp(String imageNaverCp) {
		this.imageNaverCp = imageNaverCp;
	}

	public String getImageNaverDp() {
		return imageNaverDp;
	}

	public void setImageNaverDp(String imageNaverDp) {
		this.imageNaverDp = imageNaverDp;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public int getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}

	public int getDcAmt() {
		return dcAmt;
	}

	public void setDcAmt(int dcAmt) {
		this.dcAmt = dcAmt;
	}

	public String getShipEntpCode() {
		return shipEntpCode;
	}

	public void setShipEntpCode(String shipEntpCode) {
		this.shipEntpCode = shipEntpCode;
	}

	public String getShipCostcode() {
		return shipCostCode;
	}

	public void setShipCostcode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}

	public int getOrdCost() {
		return ordCost;
	}

	public void setOrdCost(int ordCost) {
		this.ordCost = ordCost;
	}

	public int getReturnCost() {
		return returnCost;
	}

	public void setReturnCost(int returnCost) {
		this.returnCost = returnCost;
	}

	public int getChangeCost() {
		return changeCost;
	}

	public void setChangeCost(int changeCost) {
		this.changeCost = changeCost;
	}

	public int getShipCostBaseAmt() {
		return shipCostBaseAmt;
	}

	public void setShipCostBaseAmt(int shipCostBaseAmt) {
		this.shipCostBaseAmt = shipCostBaseAmt;
	}

	public int getJejuCost() {
		return jejuCost;
	}

	public void setJejuCost(int jejuCost) {
		this.jejuCost = jejuCost;
	}

	public int getIslandCost() {
		return islandCost;
	}

	public void setIslandCost(int islandCost) {
		this.islandCost = islandCost;
	}

	public String getOrderCreateYN() {
		return orderCreateYN;
	}

	public void setOrderCreateYN(String orderCreateYN) {
		this.orderCreateYN = orderCreateYN;
	}

	public String getCsTel() {
		return csTel;
	}

	public void setCsTel(String csTel) {
		this.csTel = csTel;
	}

	public int getLumpSumDcAmt() {
		return lumpSumDcAmt;
	}

	public void setLumpSumDcAmt(int lumpSumDcAmt) {
		this.lumpSumDcAmt = lumpSumDcAmt;
	}

	public int getLumpSumEntpDcAmt() {
		return lumpSumEntpDcAmt;
	}

	public void setLumpSumEntpDcAmt(int lumpSumEntpDcAmt) {
		this.lumpSumEntpDcAmt = lumpSumEntpDcAmt;
	}

	public int getLumpSumOwnDcAmt() {
		return lumpSumOwnDcAmt;
	}

	public void setLumpSumOwnDcAmt(int lumpSumOwnDcAmt) {
		this.lumpSumOwnDcAmt = lumpSumOwnDcAmt;
	}
	
	public String getModelNo() {
		return modelNo;
	}

	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}

	public String getCollectYn() {
		return collectYn;
	}

	public void setCollectYn(String collectYn) {
		this.collectYn = collectYn;
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

	public int getExceptYn() {
		return exceptYn;
	}

	public void setExceptYn(int exceptYn) {
		this.exceptYn = exceptYn;
	}	
	
}