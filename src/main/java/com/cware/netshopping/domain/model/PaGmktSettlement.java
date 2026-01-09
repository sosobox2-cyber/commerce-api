package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaGmktSettlement extends AbstractModel {
	private static final long serialVersionUID = 1L; 
	
	private String gatherDate;
	private String siteType;
	private String totalCount;
	private String pageSize;
	private String pageNo;
	private String payNo;
	private String contrNo;
	private String goodsNo;
	private String siteGoodsNo;
	private Timestamp orderDate;
	private Timestamp payDate;
	private Timestamp shippingDate;
	private Timestamp shippingCmplDate;
	private Timestamp buyDecisionDate;
	private Timestamp settleExpectDate;
	private String settleExceptName;
	private Timestamp remitDate;
	private Timestamp refundDate;
	private String settleType;
	private String orderUnitPrice;
	private String orderQty;
	private String sellOrderPrice;
	private String optionPrice;
	private String goodsCost;
	private String optionCost;
	private String basicCommission;
	private String optionCommission;
	private String deductTaxPrice;
	private String totCommission;
	private String deductNonTaxPrice;
	private String settlementPrice;
	private String serviceFee;
	private String corpDiscountTotalPrice;
	private String overProfitDiscountPrice;
	private String feeDiscountPrice;
	private String sellerDiscountTotalPrice;
	private String buyerPayAmt;
	private String branchPrice;
	private String branchCode;
	private String taxYn;
	private String sellerDiscountPrice1;
	private String sellerDiscountPrice2;
	private String sellerDiscountPrice;
	private String sellerPcsFee;
	private String delFeeOverseaAmt;
	private String kind;
	private String settlementType;
	private String delFeeAmt;
	private String delFeeNo;
	private String delFeePayWay;
	private String deliveryGroupNo;
	private String delFeeType;
	private Timestamp revenueBaseDate;
	private Timestamp RevenueDate;
	private String DelFeeCommission;
	
	
	public String getGatherDate() {
		return gatherDate;
	}
	public void setGatherDate(String gatherDate) {
		this.gatherDate = gatherDate;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public String getContrNo() {
		return contrNo;
	}
	public void setContrNo(String contrNo) {
		this.contrNo = contrNo;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getSiteGoodsNo() {
		return siteGoodsNo;
	}
	public void setSiteGoodsNo(String siteGoodsNo) {
		this.siteGoodsNo = siteGoodsNo;
	}
	public Timestamp getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	public Timestamp getPayDate() {
		return payDate;
	}
	public void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}
	public Timestamp getShippingDate() {
		return shippingDate;
	}
	public void setShippingDate(Timestamp shippingDate) {
		this.shippingDate = shippingDate;
	}
	public Timestamp getShippingCmplDate() {
		return shippingCmplDate;
	}
	public void setShippingCmplDate(Timestamp shippingCmplDate) {
		this.shippingCmplDate = shippingCmplDate;
	}
	public Timestamp getBuyDecisionDate() {
		return buyDecisionDate;
	}
	public void setBuyDecisionDate(Timestamp buyDecisionDate) {
		this.buyDecisionDate = buyDecisionDate;
	}
	public Timestamp getSettleExpectDate() {
		return settleExpectDate;
	}
	public void setSettleExpectDate(Timestamp settleExpectDate) {
		this.settleExpectDate = settleExpectDate;
	}
	public String getSettleExceptName() {
		return settleExceptName;
	}
	public void setSettleExceptName(String settleExceptName) {
		this.settleExceptName = settleExceptName;
	}
	public Timestamp getRemitDate() {
		return remitDate;
	}
	public void setRemitDate(Timestamp remitDate) {
		this.remitDate = remitDate;
	}
	public Timestamp getRefundDate() {
		return refundDate;
	}
	public void setRefundDate(Timestamp refundDate) {
		this.refundDate = refundDate;
	}
	public String getSettleType() {
		return settleType;
	}
	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}
	public String getOrderUnitPrice() {
		return orderUnitPrice;
	}
	public void setOrderUnitPrice(String orderUnitPrice) {
		this.orderUnitPrice = orderUnitPrice;
	}
	public String getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}
	public String getSellOrderPrice() {
		return sellOrderPrice;
	}
	public void setSellOrderPrice(String sellOrderPrice) {
		this.sellOrderPrice = sellOrderPrice;
	}
	public String getOptionPrice() {
		return optionPrice;
	}
	public void setOptionPrice(String optionPrice) {
		this.optionPrice = optionPrice;
	}
	public String getGoodsCost() {
		return goodsCost;
	}
	public void setGoodsCost(String goodsCost) {
		this.goodsCost = goodsCost;
	}
	public String getOptionCost() {
		return optionCost;
	}
	public void setOptionCost(String optionCost) {
		this.optionCost = optionCost;
	}
	public String getBasicCommission() {
		return basicCommission;
	}
	public void setBasicCommission(String basicCommission) {
		this.basicCommission = basicCommission;
	}
	public String getOptionCommission() {
		return optionCommission;
	}
	public void setOptionCommission(String optionCommission) {
		this.optionCommission = optionCommission;
	}
	public String getDeductTaxPrice() {
		return deductTaxPrice;
	}
	public void setDeductTaxPrice(String deductTaxPrice) {
		this.deductTaxPrice = deductTaxPrice;
	}
	public String getTotCommission() {
		return totCommission;
	}
	public void setTotCommission(String totCommission) {
		this.totCommission = totCommission;
	}
	public String getDeductNonTaxPrice() {
		return deductNonTaxPrice;
	}
	public void setDeductNonTaxPrice(String deductNonTaxPrice) {
		this.deductNonTaxPrice = deductNonTaxPrice;
	}
	public String getSettlementPrice() {
		return settlementPrice;
	}
	public void setSettlementPrice(String settlementPrice) {
		this.settlementPrice = settlementPrice;
	}
	public String getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}
	public String getCorpDiscountTotalPrice() {
		return corpDiscountTotalPrice;
	}
	public void setCorpDiscountTotalPrice(String corpDiscountTotalPrice) {
		this.corpDiscountTotalPrice = corpDiscountTotalPrice;
	}
	public String getOverProfitDiscountPrice() {
		return overProfitDiscountPrice;
	}
	public void setOverProfitDiscountPrice(String overProfitDiscountPrice) {
		this.overProfitDiscountPrice = overProfitDiscountPrice;
	}
	public String getFeeDiscountPrice() {
		return feeDiscountPrice;
	}
	public void setFeeDiscountPrice(String feeDiscountPrice) {
		this.feeDiscountPrice = feeDiscountPrice;
	}
	public String getSellerDiscountTotalPrice() {
		return sellerDiscountTotalPrice;
	}
	public void setSellerDiscountTotalPrice(String sellerDiscountTotalPrice) {
		this.sellerDiscountTotalPrice = sellerDiscountTotalPrice;
	}
	public String getBuyerPayAmt() {
		return buyerPayAmt;
	}
	public void setBuyerPayAmt(String buyerPayAmt) {
		this.buyerPayAmt = buyerPayAmt;
	}
	public String getBranchPrice() {
		return branchPrice;
	}
	public void setBranchPrice(String branchPrice) {
		this.branchPrice = branchPrice;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getTaxYn() {
		return taxYn;
	}
	public void setTaxYn(String taxYn) {
		this.taxYn = taxYn;
	}
	public String getSellerDiscountPrice1() {
		return sellerDiscountPrice1;
	}
	public void setSellerDiscountPrice1(String sellerDiscountPrice1) {
		this.sellerDiscountPrice1 = sellerDiscountPrice1;
	}
	public String getSellerDiscountPrice2() {
		return sellerDiscountPrice2;
	}
	public void setSellerDiscountPrice2(String sellerDiscountPrice2) {
		this.sellerDiscountPrice2 = sellerDiscountPrice2;
	}
	public String getSellerDiscountPrice() {
		return sellerDiscountPrice;
	}
	public void setSellerDiscountPrice(String sellerDiscountPrice) {
		this.sellerDiscountPrice = sellerDiscountPrice;
	}
	public String getSellerPcsFee() {
		return sellerPcsFee;
	}
	public void setSellerPcsFee(String sellerPcsFee) {
		this.sellerPcsFee = sellerPcsFee;
	}
	public String getDelFeeOverseaAmt() {
		return delFeeOverseaAmt;
	}
	public void setDelFeeOverseaAmt(String delFeeOverseaAmt) {
		this.delFeeOverseaAmt = delFeeOverseaAmt;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getSettlementType() {
		return settlementType;
	}
	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}
	public String getDelFeeAmt() {
		return delFeeAmt;
	}
	public void setDelFeeAmt(String delFeeAmt) {
		this.delFeeAmt = delFeeAmt;
	}
	public String getDelFeeNo() {
		return delFeeNo;
	}
	public void setDelFeeNo(String delFeeNo) {
		this.delFeeNo = delFeeNo;
	}
	public String getDelFeePayWay() {
		return delFeePayWay;
	}
	public void setDelFeePayWay(String delFeePayWay) {
		this.delFeePayWay = delFeePayWay;
	}
	public String getDeliveryGroupNo() {
		return deliveryGroupNo;
	}
	public void setDeliveryGroupNo(String deliveryGroupNo) {
		this.deliveryGroupNo = deliveryGroupNo;
	}
	public String getDelFeeType() {
		return delFeeType;
	}
	public void setDelFeeType(String delFeeType) {
		this.delFeeType = delFeeType;
	}
	public Timestamp getRevenueBaseDate() {
		return revenueBaseDate;
	}
	public void setRevenueBaseDate(Timestamp revenueBaseDate) {
		this.revenueBaseDate = revenueBaseDate;
	}
	public Timestamp getRevenueDate() {
		return RevenueDate;
	}
	public void setRevenueDate(Timestamp revenueDate) {
		RevenueDate = revenueDate;
	}
	public String getDelFeeCommission() {
		return DelFeeCommission;
	}
	public void setDelFeeCommission(String delFeeCommission) {
		DelFeeCommission = delFeeCommission;
	}
	
}
