package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaCopnGoods;

public class PaCopnGoodsVO extends PaCopnGoods {

    private static final long serialVersionUID = 1L;

    //상세기술서
    private String describeExt;

    //상품 이미지
    private String imageP; 
    private String imageAP;
    private String imageBP;
    private String imageCP;
    private String imageDP;
    private String imageUrl;
    private String imageNm;
    private String infoImageG;
	
    //as안내,반품교환안내 전화번호
    private String csTel;
    //G마켓 배송비 그룹코드
    private String groupCode;
    //G마켓 묶음번호
    private String bundleNo;
    //제휴사 주소 순번
    private String outBoundShippingPlaceCode;
    
    //배송비
    private long shipCostBaseAmt;
    private long ordCost;
    private long returnCost;
    private long changeCost;
    
    //상품가격
    private Timestamp applyDate;
    private String transId;
    private Timestamp transDate;
    private long salePrice;
    private long supplyPrice;
    private long commision;
    //인증여부
    private String certYn;
    
    //부가세 면세여부
    private String tax;
    
    //상단이미지
    private String topImage;
    //하단이미지
    private String bottomImage;
    //배송비업체코드
    private String shipCostEntpCode;
    
    private String installYn;
    private String createYn;
    
    private long dcAmt;

    private String companyContactNumber;
    private String returnZipCode;
    private String returnAddress;
    private String returnAddressDetail;
    private String returnChargeName;
    
    private String makecoName;
    
    private Timestamp saleStartDate;
    private Timestamp saleEndDate;
    
    private String copnFreshYn;
    
    //일시불 금액
  	private long lumpSumDcAmt;
  	private long lumpSumOwnDcAmt;
  	private long lumpSumEntpDcAmt;
  	
  	private String collectImage;

  	//착불
  	private String collectYn;
  	
  	private String keyWord;
  	
  	private Timestamp lastModifyDate;
  	
  	private String massTargetYn;
  	
  	private String goodsCom;
  	private String noticeExt;
  	
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
	public String getCsTel() {
		return csTel;
	}
	public void setCsTel(String csTel) {
		this.csTel = csTel;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getBundleNo() {
		return bundleNo;
	}
	public void setBundleNo(String bundleNo) {
		this.bundleNo = bundleNo;
	}
	public String getOutBoundShippingPlaceCode() {
		return outBoundShippingPlaceCode;
	}
	public void setOutBoundShippingPlaceCode(String outBoundShippingPlaceCode) {
		this.outBoundShippingPlaceCode = outBoundShippingPlaceCode;
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
	public Timestamp getTransDate() {
		return transDate;
	}
	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
	}
	public long getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(long salePrice) {
		this.salePrice = salePrice;
	}
	public long getSupplyPrice() {
		return supplyPrice;
	}
	public void setSupplyPrice(long supplyPrice) {
		this.supplyPrice = supplyPrice;
	}
	public long getCommision() {
		return commision;
	}
	public void setCommision(long commision) {
		this.commision = commision;
	}
	public String getCertYn() {
		return certYn;
	}
	public void setCertYn(String certYn) {
		this.certYn = certYn;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
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
	public String getShipCostEntpCode() {
		return shipCostEntpCode;
	}
	public void setShipCostEntpCode(String shipCostEntpCode) {
		this.shipCostEntpCode = shipCostEntpCode;
	}
	public String getInstallYn() {
		return installYn;
	}
	public void setInstallYn(String installYn) {
		this.installYn = installYn;
	}
	public long getDcAmt() {
		return dcAmt;
	}
	public void setDcAmt(long dcAmt) {
		this.dcAmt = dcAmt;
	}
	public String getCompanyContactNumber() {
		return companyContactNumber;
	}
	public void setCompanyContactNumber(String companyContactNumber) {
		this.companyContactNumber = companyContactNumber;
	}
	public String getReturnZipCode() {
		return returnZipCode;
	}
	public void setReturnZipCode(String returnZipCode) {
		this.returnZipCode = returnZipCode;
	}
	public String getReturnAddress() {
		return returnAddress;
	}
	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}
	public String getReturnAddressDetail() {
		return returnAddressDetail;
	}
	public void setReturnAddressDetail(String returnAddressDetail) {
		this.returnAddressDetail = returnAddressDetail;
	}
	public String getReturnChargeName() {
		return returnChargeName;
	}
	public void setReturnChargeName(String returnChargeName) {
		this.returnChargeName = returnChargeName;
	}
	public String getMakecoName() {
		return makecoName;
	}
	public void setMakecoName(String makecoName) {
		this.makecoName = makecoName;
	}
	public Timestamp getSaleStartDate() {
		return saleStartDate;
	}
	public void setSaleStartDate(Timestamp saleStartDate) {
		this.saleStartDate = saleStartDate;
	}
	public Timestamp getSaleEndDate() {
		return saleEndDate;
	}
	public void setSaleEndDate(Timestamp saleEndDate) {
		this.saleEndDate = saleEndDate;
	}
	public String getCreateYn() {
		return createYn;
	}
	public void setCreateYn(String createYn) {
		this.createYn = createYn;
	}
	public String getCopnFreshYn() {
		return copnFreshYn;
	}
	public void setCopnFreshYn(String copnFreshYn) {
		this.copnFreshYn = copnFreshYn;
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
	public String getCollectYn() {
		return collectYn;
	}
	public void setCollectYn(String collectYn) {
		this.collectYn = collectYn;
	}
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Timestamp getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Timestamp lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
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

	public String getInfoImageG() {
		return infoImageG;
	}

	public void setInfoImageG(String infoImageG) {
		this.infoImageG = infoImageG;
	}

}
