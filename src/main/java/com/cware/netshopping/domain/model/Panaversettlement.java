package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class Panaversettlement extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String panavrSalesNo;
	private String paCode;
	private Timestamp settleBasicDate;
	private Timestamp settleExpectDate;
	private Timestamp settleCompleteDate;
	private String orderId;
	private String productOrderId;
	private String productOrderType;
	private String settleType;
	private String productId;
	private String productName;
	private String purchaserName;
	private double paySettleAmt;
	private double totalCommiAmt;
	private double totalPayCommiAmt;
	private String primaryPayMean;
	private double primaryPayMeanBasicAmt;
	private double primaryPayMeanPayCommiAmt;
	private String subPayMean;
	private double subPayMeanBasicAmt;
	private double subPayMeanPayCommiAmt;
	private double channelCommiAmt;
	private double freeInstallmentCommiAmt;
	private double saleCommissionAmount;
	private double sellingInterlockCommiAmt;
	private double benefitSettleAmt;
	private double settleExpectAmt;
	private String sub2PayMean;
	private double sub2PayMeanBasicAmt;
	private double sub2PayMeanPayCommiAmt;
	private String sub3PayMean;
	private double sub3PayMeanBasicAmt;
	private double sub3PayMeanPayCommiAmt;
	private String quickSettleCreated;
	
	private Timestamp payDate;
	
	public String getPanavrSalesNo() { 
		return this.panavrSalesNo;
	}
	public String getPaCode() { 
		return this.paCode;
	}
	public Timestamp getSettleBasicDate() { 
		return this.settleBasicDate;
	}
	public Timestamp getSettleExpectDate() { 
		return this.settleExpectDate;
	}
	public Timestamp getSettleCompleteDate() { 
		return this.settleCompleteDate;
	}
	public String getOrderId() { 
		return this.orderId;
	}
	public String getProductOrderId() { 
		return this.productOrderId;
	}
	public String getProductOrderType() { 
		return this.productOrderType;
	}
	public String getSettleType() { 
		return this.settleType;
	}
	public String getProductId() { 
		return this.productId;
	}
	public String getProductName() { 
		return this.productName;
	}
	public String getPurchaserName() { 
		return this.purchaserName;
	}
	public double getPaySettleAmt() { 
		return this.paySettleAmt;
	}
	public double getTotalCommiAmt() { 
		return this.totalCommiAmt;
	}
	public double getTotalPayCommiAmt() { 
		return this.totalPayCommiAmt;
	}
	public String getPrimaryPayMean() { 
		return this.primaryPayMean;
	}
	public double getPrimaryPayMeanBasicAmt() { 
		return this.primaryPayMeanBasicAmt;
	}
	public double getPrimaryPayMeanPayCommiAmt() { 
		return this.primaryPayMeanPayCommiAmt;
	}
	public String getSubPayMean() { 
		return this.subPayMean;
	}
	public double getSubPayMeanBasicAmt() { 
		return this.subPayMeanBasicAmt;
	}
	public double getSubPayMeanPayCommiAmt() { 
		return this.subPayMeanPayCommiAmt;
	}
	public double getChannelCommiAmt() { 
		return this.channelCommiAmt;
	}
	public double getFreeInstallmentCommiAmt() { 
		return this.freeInstallmentCommiAmt;
	}
	public double getSaleCommissionAmount() { 
		return this.saleCommissionAmount;
	}
	public double getSellingInterlockCommiAmt() { 
		return this.sellingInterlockCommiAmt;
	}
	public double getBenefitSettleAmt() { 
		return this.benefitSettleAmt;
	}
	public double getSettleExpectAmt() { 
		return this.settleExpectAmt;
	}
	public void setPanavrSalesNo(String panavrSalesNo) { 
		this.panavrSalesNo = panavrSalesNo;
	}
	public void setPaCode(String paCode) { 
		this.paCode = paCode;
	}
	public void setSettleBasicDate(Timestamp settleBasicDate) { 
		this.settleBasicDate = settleBasicDate;
	}
	public void setSettleExpectDate(Timestamp settleExpectDate) { 
		this.settleExpectDate = settleExpectDate;
	}
	public void setSettleCompleteDate(Timestamp settleCompleteDate) { 
		this.settleCompleteDate = settleCompleteDate;
	}
	public void setOrderId(String orderId) { 
		this.orderId = orderId;
	}
	public void setProductOrderId(String productOrderId) { 
		this.productOrderId = productOrderId;
	}
	public void setProductOrderType(String productOrderType) { 
		this.productOrderType = productOrderType;
	}
	public void setSettleType(String settleType) { 
		this.settleType = settleType;
	}
	public void setProductId(String productId) { 
		this.productId = productId;
	}
	public void setProductName(String productName) { 
		this.productName = productName;
	}
	public void setPurchaserName(String purchaserName) { 
		this.purchaserName = purchaserName;
	}
	public void setPaySettleAmt(double paySettleAmt) { 
		this.paySettleAmt = paySettleAmt;
	}
	public void setTotalCommiAmt(double totalCommiAmt) { 
		this.totalCommiAmt = totalCommiAmt;
	}
	public void setTotalPayCommiAmt(double totalPayCommiAmt) { 
		this.totalPayCommiAmt = totalPayCommiAmt;
	}
	public void setPrimaryPayMean(String primaryPayMean) { 
		this.primaryPayMean = primaryPayMean;
	}
	public void setPrimaryPayMeanBasicAmt(double primaryPayMeanBasicAmt) { 
		this.primaryPayMeanBasicAmt = primaryPayMeanBasicAmt;
	}
	public void setPrimaryPayMeanPayCommiAmt(double primaryPayMeanPayCommiAmt) { 
		this.primaryPayMeanPayCommiAmt = primaryPayMeanPayCommiAmt;
	}
	public void setSubPayMean(String subPayMean) { 
		this.subPayMean = subPayMean;
	}
	public void setSubPayMeanBasicAmt(double subPayMeanBasicAmt) { 
		this.subPayMeanBasicAmt = subPayMeanBasicAmt;
	}
	public void setSubPayMeanPayCommiAmt(double subPayMeanPayCommiAmt) { 
		this.subPayMeanPayCommiAmt = subPayMeanPayCommiAmt;
	}
	public void setChannelCommiAmt(double channelCommiAmt) { 
		this.channelCommiAmt = channelCommiAmt;
	}
	public void setFreeInstallmentCommiAmt(double freeInstallmentCommiAmt) { 
		this.freeInstallmentCommiAmt = freeInstallmentCommiAmt;
	}
	public void setSaleCommissionAmount(double saleCommissionAmount) { 
		this.saleCommissionAmount = saleCommissionAmount;
	}
	public void setSellingInterlockCommiAmt(double sellingInterlockCommiAmt) { 
		this.sellingInterlockCommiAmt = sellingInterlockCommiAmt;
	}
	public void setBenefitSettleAmt(double benefitSettleAmt) { 
		this.benefitSettleAmt = benefitSettleAmt;
	}
	public void setSettleExpectAmt(double settleExpectAmt) { 
		this.settleExpectAmt = settleExpectAmt;
	}
	public Timestamp getPayDate() {
		return payDate;
	}
	public void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}
	public String getSub2PayMean() {
		return sub2PayMean;
	}
	public void setSub2PayMean(String sub2PayMean) {
		this.sub2PayMean = sub2PayMean;
	}
	public double getSub2PayMeanBasicAmt() {
		return sub2PayMeanBasicAmt;
	}
	public void setSub2PayMeanBasicAmt(double sub2PayMeanBasicAmt) {
		this.sub2PayMeanBasicAmt = sub2PayMeanBasicAmt;
	}
	public double getSub2PayMeanPayCommiAmt() {
		return sub2PayMeanPayCommiAmt;
	}
	public void setSub2PayMeanPayCommiAmt(double sub2PayMeanPayCommiAmt) {
		this.sub2PayMeanPayCommiAmt = sub2PayMeanPayCommiAmt;
	}
	public String getSub3PayMean() {
		return sub3PayMean;
	}
	public void setSub3PayMean(String sub3PayMean) {
		this.sub3PayMean = sub3PayMean;
	}
	public double getSub3PayMeanBasicAmt() {
		return sub3PayMeanBasicAmt;
	}
	public void setSub3PayMeanBasicAmt(double sub3PayMeanBasicAmt) {
		this.sub3PayMeanBasicAmt = sub3PayMeanBasicAmt;
	}
	public double getSub3PayMeanPayCommiAmt() {
		return sub3PayMeanPayCommiAmt;
	}
	public void setSub3PayMeanPayCommiAmt(double sub3PayMeanPayCommiAmt) {
		this.sub3PayMeanPayCommiAmt = sub3PayMeanPayCommiAmt;
	}
	public String getQuickSettleCreated() {
		return quickSettleCreated;
	}
	public void setQuickSettleCreated(String quickSettleCreated) {
		this.quickSettleCreated = quickSettleCreated;
	}
}
