package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Pacopnsettlement extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String pacopnSalesNo;
	private String paCode;
	private String orderId;
	private String saleType;
	private String saleDate;
	private Timestamp recognitionDate;
	private String settlementDate;
	private String finalSettlementDate;
	private double amount;
	private double fee;
	private double feeVat;
	private double feeRatio;
	private double settlementAmountDeli;
	private double baseAmount;
	private double baseFee;
	private double baseFeeVat;
	private double remoteAmount;
	private double remoteFee;
	private double remoteFeeVat;
	private String taxType;
	private String productId;
	private String productName;
	private String vendorItemId;
	private String vendorItemName;
	private double salePrice;
	private double quantity;
	private double coupangDiscountCoupon;
	private String discountCouponpolicyagreement;
	private double saleAmount;
	private double sellerDiscountCoupon;
	private double downloadableCoupon;
	private double serviceFee;
	private double serviceFeeVat;
	private double serviceFeeRatio;
	private double settlementAmount;
	private double couranteeFeeRatio;
	private double couranteeFee;
	private double couranteeFeeVat;
	private double storeFeeDiscountVat;
	private double storeFeeDiscount;
	private String externalSellerSkuCode;

	public String getPacopnSalesNo() { 
		return this.pacopnSalesNo;
	}
	public String getPaCode() { 
		return this.paCode;
	}
	public String getOrderId() { 
		return this.orderId;
	}
	public String getSaleType() { 
		return this.saleType;
	}
	public String getSaleDate() { 
		return this.saleDate;
	}
	public Timestamp getRecognitionDate() { 
		return this.recognitionDate;
	}
	public String getSettlementDate() { 
		return this.settlementDate;
	}
	public String getFinalSettlementDate() { 
		return this.finalSettlementDate;
	}
	public double getAmount() { 
		return this.amount;
	}
	public double getFee() { 
		return this.fee;
	}
	public double getFeeVat() { 
		return this.feeVat;
	}
	public double getFeeRatio() { 
		return this.feeRatio;
	}
	public double getSettlementAmountDeli() { 
		return this.settlementAmountDeli;
	}
	public double getBaseAmount() { 
		return this.baseAmount;
	}
	public double getBaseFee() { 
		return this.baseFee;
	}
	public double getBaseFeeVat() { 
		return this.baseFeeVat;
	}
	public double getRemoteAmount() { 
		return this.remoteAmount;
	}
	public double getRemoteFee() { 
		return this.remoteFee;
	}
	public double getRemoteFeeVat() { 
		return this.remoteFeeVat;
	}
	public String getTaxType() { 
		return this.taxType;
	}
	public String getProductId() { 
		return this.productId;
	}
	public String getProductName() { 
		return this.productName;
	}
	public String getVendorItemId() { 
		return this.vendorItemId;
	}
	public String getVendorItemName() { 
		return this.vendorItemName;
	}
	public double getSalePrice() { 
		return this.salePrice;
	}
	public double getQuantity() { 
		return this.quantity;
	}
	public double getCoupangDiscountCoupon() { 
		return this.coupangDiscountCoupon;
	}
	public String getDiscountCouponpolicyagreement() { 
		return this.discountCouponpolicyagreement;
	}
	public double getSaleAmount() { 
		return this.saleAmount;
	}
	public double getSellerDiscountCoupon() { 
		return this.sellerDiscountCoupon;
	}
	public double getDownloadableCoupon() { 
		return this.downloadableCoupon;
	}
	public double getServiceFee() { 
		return this.serviceFee;
	}
	public double getServiceFeeVat() { 
		return this.serviceFeeVat;
	}
	public double getServiceFeeRatio() { 
		return this.serviceFeeRatio;
	}
	public double getSettlementAmount() { 
		return this.settlementAmount;
	}
	public double getCouranteeFeeRatio() { 
		return this.couranteeFeeRatio;
	}
	public double getCouranteeFee() { 
		return this.couranteeFee;
	}
	public double getCouranteeFeeVat() { 
		return this.couranteeFeeVat;
	}
	public double getStoreFeeDiscountVat() { 
		return this.storeFeeDiscountVat;
	}
	public double getStoreFeeDiscount() { 
		return this.storeFeeDiscount;
	}
	public String getExternalSellerSkuCode() { 
		return this.externalSellerSkuCode;
	}

	public void setPacopnSalesNo(String pacopnSalesNo) { 
		this.pacopnSalesNo = pacopnSalesNo;
	}
	public void setPaCode(String paCode) { 
		this.paCode = paCode;
	}
	public void setOrderId(String orderId) { 
		this.orderId = orderId;
	}
	public void setSaleType(String saleType) { 
		this.saleType = saleType;
	}
	public void setSaleDate(String saleDate) { 
		this.saleDate = saleDate;
	}
	public void setRecognitionDate(Timestamp recognitionDate) { 
		this.recognitionDate = recognitionDate;
	}
	public void setSettlementDate(String settlementDate) { 
		this.settlementDate = settlementDate;
	}
	public void setFinalSettlementDate(String finalSettlementDate) { 
		this.finalSettlementDate = finalSettlementDate;
	}
	public void setAmount(double amount) { 
		this.amount = amount;
	}
	public void setFee(double fee) { 
		this.fee = fee;
	}
	public void setFeeVat(double feeVat) { 
		this.feeVat = feeVat;
	}
	public void setFeeRatio(double feeRatio) { 
		this.feeRatio = feeRatio;
	}
	public void setSettlementAmountDeli(double settlementAmountDeli) { 
		this.settlementAmountDeli = settlementAmountDeli;
	}
	public void setBaseAmount(double baseAmount) { 
		this.baseAmount = baseAmount;
	}
	public void setBaseFee(double baseFee) { 
		this.baseFee = baseFee;
	}
	public void setBaseFeeVat(double baseFeeVat) { 
		this.baseFeeVat = baseFeeVat;
	}
	public void setRemoteAmount(double remoteAmount) { 
		this.remoteAmount = remoteAmount;
	}
	public void setRemoteFee(double remoteFee) { 
		this.remoteFee = remoteFee;
	}
	public void setRemoteFeeVat(double remoteFeeVat) { 
		this.remoteFeeVat = remoteFeeVat;
	}
	public void setTaxType(String taxType) { 
		this.taxType = taxType;
	}
	public void setProductId(String productId) { 
		this.productId = productId;
	}
	public void setProductName(String productName) { 
		this.productName = productName;
	}
	public void setVendorItemId(String vendorItemId) { 
		this.vendorItemId = vendorItemId;
	}
	public void setVendorItemName(String vendorItemName) { 
		this.vendorItemName = vendorItemName;
	}
	public void setSalePrice(double salePrice) { 
		this.salePrice = salePrice;
	}
	public void setQuantity(double quantity) { 
		this.quantity = quantity;
	}
	public void setCoupangDiscountCoupon(double coupangDiscountCoupon) { 
		this.coupangDiscountCoupon = coupangDiscountCoupon;
	}
	public void setDiscountCouponpolicyagreement(String discountCouponpolicyagreement) { 
		this.discountCouponpolicyagreement = discountCouponpolicyagreement;
	}
	public void setSaleAmount(double saleAmount) { 
		this.saleAmount = saleAmount;
	}
	public void setSellerDiscountCoupon(double sellerDiscountCoupon) { 
		this.sellerDiscountCoupon = sellerDiscountCoupon;
	}
	public void setDownloadableCoupon(double downloadableCoupon) { 
		this.downloadableCoupon = downloadableCoupon;
	}
	public void setServiceFee(double serviceFee) { 
		this.serviceFee = serviceFee;
	}
	public void setServiceFeeVat(double serviceFeeVat) { 
		this.serviceFeeVat = serviceFeeVat;
	}
	public void setServiceFeeRatio(double serviceFeeRatio) { 
		this.serviceFeeRatio = serviceFeeRatio;
	}
	public void setSettlementAmount(double settlementAmount) { 
		this.settlementAmount = settlementAmount;
	}
	public void setCouranteeFeeRatio(double couranteeFeeRatio) { 
		this.couranteeFeeRatio = couranteeFeeRatio;
	}
	public void setCouranteeFee(double couranteeFee) { 
		this.couranteeFee = couranteeFee;
	}
	public void setCouranteeFeeVat(double couranteeFeeVat) { 
		this.couranteeFeeVat = couranteeFeeVat;
	}
	public void setStoreFeeDiscountVat(double storeFeeDiscountVat) { 
		this.storeFeeDiscountVat = storeFeeDiscountVat;
	}
	public void setStoreFeeDiscount(double storeFeeDiscount) { 
		this.storeFeeDiscount = storeFeeDiscount;
	}
	public void setExternalSellerSkuCode(String externalSellerSkuCode) { 
		this.externalSellerSkuCode = externalSellerSkuCode;
	}
}
