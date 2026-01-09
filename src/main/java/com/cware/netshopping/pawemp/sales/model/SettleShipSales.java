package com.cware.netshopping.pawemp.sales.model;

import java.sql.Timestamp;

/**
 * 정상일마감 배송비 매출
 */
public class SettleShipSales {
	
	private String paWempSalesNo;
	private String paCode;
	private String basicDt;
	private String bundleNo;
	private String claimBundleNo;
	private String gubun;
	private Long originalShipAmt;
	private Long shipAmt;
	private Long wmpChargeCouponAmt;
	private Long sellerChargeCouponAmt;
	private Long cardChargeCouponAmt;
	private Long claimChargeCust;
	private Long claimChargeSeller;
	private Long claimChargeWmp;
	private Long custChargeAddShipAmt;
	private Long wmpChargeAddShipAmt;
	private Long sellerChargeAddShipAmt;
	private Long custChargeReturnShipAmt;
	private Long wmpChargeReturnShipAmt;
	private Long sellerChargeReturnShipAmt;
	private String shipMng;
	private Long claimDeductAmt;
	private String insertId;
	private Timestamp insertDate;
	
	public String getBasicDt() {
		return basicDt;
	}
	public void setBasicDt(String basicDt) {
		this.basicDt = basicDt;
	}
	public String getBundleNo() {
		return bundleNo;
	}
	public void setBundleNo(String bundleNo) {
		this.bundleNo = bundleNo;
	}
	public String getClaimBundleNo() {
		return claimBundleNo;
	}
	public void setClaimBundleNo(String claimBundleNo) {
		this.claimBundleNo = claimBundleNo;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public Long getOriginalShipAmt() {
		return originalShipAmt;
	}
	public void setOriginalShipAmt(Long originalShipAmt) {
		this.originalShipAmt = originalShipAmt;
	}
	public Long getShipAmt() {
		return shipAmt;
	}
	public void setShipAmt(Long shipAmt) {
		this.shipAmt = shipAmt;
	}
	public Long getWmpChargeCouponAmt() {
		return wmpChargeCouponAmt;
	}
	public void setWmpChargeCouponAmt(Long wmpChargeCouponAmt) {
		this.wmpChargeCouponAmt = wmpChargeCouponAmt;
	}
	public Long getSellerChargeCouponAmt() {
		return sellerChargeCouponAmt;
	}
	public void setSellerChargeCouponAmt(Long sellerChargeCouponAmt) {
		this.sellerChargeCouponAmt = sellerChargeCouponAmt;
	}
	public Long getCardChargeCouponAmt() {
		return cardChargeCouponAmt;
	}
	public void setCardChargeCouponAmt(Long cardChargeCouponAmt) {
		this.cardChargeCouponAmt = cardChargeCouponAmt;
	}
	public Long getClaimChargeCust() {
		return claimChargeCust;
	}
	public void setClaimChargeCust(Long claimChargeCust) {
		this.claimChargeCust = claimChargeCust;
	}
	public Long getClaimChargeSeller() {
		return claimChargeSeller;
	}
	public void setClaimChargeSeller(Long claimChargeSeller) {
		this.claimChargeSeller = claimChargeSeller;
	}
	public Long getClaimChargeWmp() {
		return claimChargeWmp;
	}
	public void setClaimChargeWmp(Long claimChargeWmp) {
		this.claimChargeWmp = claimChargeWmp;
	}
	public Long getCustChargeAddShipAmt() {
		return custChargeAddShipAmt;
	}
	public void setCustChargeAddShipAmt(Long custChargeAddShipAmt) {
		this.custChargeAddShipAmt = custChargeAddShipAmt;
	}
	public Long getWmpChargeAddShipAmt() {
		return wmpChargeAddShipAmt;
	}
	public void setWmpChargeAddShipAmt(Long wmpChargeAddShipAmt) {
		this.wmpChargeAddShipAmt = wmpChargeAddShipAmt;
	}
	public Long getSellerChargeAddShipAmt() {
		return sellerChargeAddShipAmt;
	}
	public void setSellerChargeAddShipAmt(Long sellerChargeAddShipAmt) {
		this.sellerChargeAddShipAmt = sellerChargeAddShipAmt;
	}
	public Long getCustChargeReturnShipAmt() {
		return custChargeReturnShipAmt;
	}
	public void setCustChargeReturnShipAmt(Long custChargeReturnShipAmt) {
		this.custChargeReturnShipAmt = custChargeReturnShipAmt;
	}
	public Long getWmpChargeReturnShipAmt() {
		return wmpChargeReturnShipAmt;
	}
	public void setWmpChargeReturnShipAmt(Long wmpChargeReturnShipAmt) {
		this.wmpChargeReturnShipAmt = wmpChargeReturnShipAmt;
	}
	public Long getSellerChargeReturnShipAmt() {
		return sellerChargeReturnShipAmt;
	}
	public void setSellerChargeReturnShipAmt(Long sellerChargeReturnShipAmt) {
		this.sellerChargeReturnShipAmt = sellerChargeReturnShipAmt;
	}
	public String getShipMng() {
		return shipMng;
	}
	public void setShipMng(String shipMng) {
		this.shipMng = shipMng;
	}
	public Long getClaimDeductAmt() {
		return claimDeductAmt;
	}
	public void setClaimDeductAmt(Long claimDeductAmt) {
		this.claimDeductAmt = claimDeductAmt;
	}
	public String getPaWempSalesNo() {
		return paWempSalesNo;
	}
	public void setPaWempSalesNo(String paWempSalesNo) {
		this.paWempSalesNo = paWempSalesNo;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getInsertId() {
		return insertId;
	}
	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}
	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}

}
