package com.cware.netshopping.pawemp.sales.model;

import java.sql.Timestamp;

/**
 * 정상일마감 상품 매출
 */
public class SettleProdSales {
	
	private String paWempSalesNo;
	private String paCode;
	private String basicDt;
	private String orderNo;
	private String claimNo;
	private String prodNo;
	private String gubun;
	private String paymentNo;
	private String bundleNo;
	private String purchaseNo;
	private String transType;
	private String settleCycleType;
	private String prodName;
	private String prodType;
	private String shipMng;
	private Long prodSaleAmt;
	private Long completeQty;
	private Long completeAmt;
	private Long saleAgencyFee;
	private Long feeAmt;
	private String feeRate;
	private String feeFlag; 
	private String vatFlag;
	private Long wmpChargeCouponAmt;
	private Long sellerChargeCouponAmt;
	private Long cardChargeCouponAmt;
	private Long wmpCartCouponAmt;
	private Long sellerCartCouponAmt;
	private Long cardCartCouponAmt;
	private String paymentCompleteDt;
	private String settlePreDt;
	private Long claimDeductAmt;
	private Long wmpDiscountBurdenFee;
	private Long wmpEpAmt;
	private String insertId;
	private Timestamp insertDate;
	
	public String getBasicDt() {
		return basicDt;
	}
	public void setBasicDt(String basicDt) {
		this.basicDt = basicDt;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getProdNo() {
		return prodNo;
	}
	public void setProdNo(String prodNo) {
		this.prodNo = prodNo;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getPaymentNo() {
		return paymentNo;
	}
	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}
	public String getBundleNo() {
		return bundleNo;
	}
	public void setBundleNo(String bundleNo) {
		this.bundleNo = bundleNo;
	}
	public String getPurchaseNo() {
		return purchaseNo;
	}
	public void setPurchaseNo(String purchaseNo) {
		this.purchaseNo = purchaseNo;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getSettleCycleType() {
		return settleCycleType;
	}
	public void setSettleCycleType(String settleCycleType) {
		this.settleCycleType = settleCycleType;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getProdType() {
		return prodType;
	}
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	public String getShipMng() {
		return shipMng;
	}
	public void setShipMng(String shipMng) {
		this.shipMng = shipMng;
	}
	public Long getProdSaleAmt() {
		return prodSaleAmt;
	}
	public void setProdSaleAmt(Long prodSaleAmt) {
		this.prodSaleAmt = prodSaleAmt;
	}
	public Long getCompleteQty() {
		return completeQty;
	}
	public void setCompleteQty(Long completeQty) {
		this.completeQty = completeQty;
	}
	public Long getCompleteAmt() {
		return completeAmt;
	}
	public void setCompleteAmt(Long completeAmt) {
		this.completeAmt = completeAmt;
	}
	public Long getSaleAgencyFee() {
		return saleAgencyFee;
	}
	public void setSaleAgencyFee(Long saleAgencyFee) {
		this.saleAgencyFee = saleAgencyFee;
	}
	public Long getFeeAmt() {
		return feeAmt;
	}
	public void setFeeAmt(Long feeAmt) {
		this.feeAmt = feeAmt;
	}
	public String getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(String feeRate) {
		this.feeRate = feeRate;
	}
	public String getFeeFlag() {
		return feeFlag;
	}
	public void setFeeFlag(String feeFlag) {
		this.feeFlag = feeFlag;
	}
	public String getVatFlag() {
		return vatFlag;
	}
	public void setVatFlag(String vatFlag) {
		this.vatFlag = vatFlag;
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
	public Long getWmpCartCouponAmt() {
		return wmpCartCouponAmt;
	}
	public void setWmpCartCouponAmt(Long wmpCartCouponAmt) {
		this.wmpCartCouponAmt = wmpCartCouponAmt;
	}
	public Long getSellerCartCouponAmt() {
		return sellerCartCouponAmt;
	}
	public void setSellerCartCouponAmt(Long sellerCartCouponAmt) {
		this.sellerCartCouponAmt = sellerCartCouponAmt;
	}
	public Long getCardCartCouponAmt() {
		return cardCartCouponAmt;
	}
	public void setCardCartCouponAmt(Long cardCartCouponAmt) {
		this.cardCartCouponAmt = cardCartCouponAmt;
	}
	public String getPaymentCompleteDt() {
		return paymentCompleteDt;
	}
	public void setPaymentCompleteDt(String paymentCompleteDt) {
		this.paymentCompleteDt = paymentCompleteDt;
	}
	public String getSettlePreDt() {
		return settlePreDt;
	}
	public void setSettlePreDt(String settlePreDt) {
		this.settlePreDt = settlePreDt;
	}
	public Long getClaimDeductAmt() {
		return claimDeductAmt;
	}
	public void setClaimDeductAmt(Long claimDeductAmt) {
		this.claimDeductAmt = claimDeductAmt;
	}
	public Long getWmpDiscountBurdenFee() {
		return wmpDiscountBurdenFee;
	}
	public void setWmpDiscountBurdenFee(Long wmpDiscountBurdenFee) {
		this.wmpDiscountBurdenFee = wmpDiscountBurdenFee;
	}
	public Long getWmpEpAmt() {
		return wmpEpAmt;
	}
	public void setWmpEpAmt(Long wmpEpAmt) {
		this.wmpEpAmt = wmpEpAmt;
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
