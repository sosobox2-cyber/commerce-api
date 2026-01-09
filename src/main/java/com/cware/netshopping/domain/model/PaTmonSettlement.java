package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaTmonSettlement extends AbstractModel {

	private static final long serialVersionUID = 1L;

	private String txSeqNo;
	private String tmonOrderNo;
	private String tmonOrderSubNo;
	private String individualOrderNo;
	private String tmonDealNo;
	private String tmonDealOptionNo;
	private String managedTitle;
	private String dealOptionTitle;
	private String partnerNo;
	private String settleDealType;
	private String settleDealDetailType;
	private String salesDateTime;
	private String payDueDate;
	private double sellAmount;
	private double payBaseAmount;
	private double payRate;
	private double vendorAmount;
	private double tmonAmount;
	private String adjustCode;
	private String discountPolicyNo;
	private String discountPolicyName;

	
	
	public String getSalesDateTime() {
		return salesDateTime;
	}

	public void setSalesDateTime(String salesDateTime) {
		this.salesDateTime = salesDateTime;
	}

	public String getPayDueDate() {
		return payDueDate;
	}

	public void setPayDueDate(String payDueDate) {
		this.payDueDate = payDueDate;
	}

	public String getTxSeqNo() {
		return txSeqNo;
	}

	public void setTxSeqNo(String txSeqNo) {
		this.txSeqNo = txSeqNo;
	}

	public String getTmonOrderNo() {
		return tmonOrderNo;
	}

	public void setTmonOrderNo(String tmonOrderNo) {
		this.tmonOrderNo = tmonOrderNo;
	}

	public String getTmonOrderSubNo() {
		return tmonOrderSubNo;
	}

	public void setTmonOrderSubNo(String tmonOrderSubNo) {
		this.tmonOrderSubNo = tmonOrderSubNo;
	}

	public String getIndividualOrderNo() {
		return individualOrderNo;
	}

	public void setIndividualOrderNo(String individualOrderNo) {
		this.individualOrderNo = individualOrderNo;
	}

	public String getTmonDealNo() {
		return tmonDealNo;
	}

	public void setTmonDealNo(String tmonDealNo) {
		this.tmonDealNo = tmonDealNo;
	}

	public String getTmonDealOptionNo() {
		return tmonDealOptionNo;
	}

	public void setTmonDealOptionNo(String tmonDealOptionNo) {
		this.tmonDealOptionNo = tmonDealOptionNo;
	}

	public String getManagedTitle() {
		return managedTitle;
	}

	public void setManagedTitle(String managedTitle) {
		this.managedTitle = managedTitle;
	}

	public String getDealOptionTitle() {
		return dealOptionTitle;
	}

	public void setDealOptionTitle(String dealOptionTitle) {
		this.dealOptionTitle = dealOptionTitle;
	}

	public String getPartnerNo() {
		return partnerNo;
	}

	public void setPartnerNo(String partnerNo) {
		this.partnerNo = partnerNo;
	}

	public String getSettleDealType() {
		return settleDealType;
	}

	public void setSettleDealType(String settleDealType) {
		this.settleDealType = settleDealType;
	}

	public String getSettleDealDetailType() {
		return settleDealDetailType;
	}

	public void setSettleDealDetailType(String settleDealDetailType) {
		this.settleDealDetailType = settleDealDetailType;
	}

	public double getSellAmount() {
		return sellAmount;
	}

	public void setSellAmount(double sellAmount) {
		this.sellAmount = sellAmount;
	}

	public double getPayBaseAmount() {
		return payBaseAmount;
	}

	public void setPayBaseAmount(double payBaseAmount) {
		this.payBaseAmount = payBaseAmount;
	}

	public double getPayRate() {
		return payRate;
	}

	public void setPayRate(double payRate) {
		this.payRate = payRate;
	}

	public double getVendorAmount() {
		return vendorAmount;
	}

	public void setVendorAmount(double vendorAmount) {
		this.vendorAmount = vendorAmount;
	}

	public double getTmonAmount() {
		return tmonAmount;
	}

	public void setTmonAmount(double tmonAmount) {
		this.tmonAmount = tmonAmount;
	}

	public String getAdjustCode() {
		return adjustCode;
	}

	public void setAdjustCode(String adjustCode) {
		this.adjustCode = adjustCode;
	}

	public String getDiscountPolicyNo() {
		return discountPolicyNo;
	}

	public void setDiscountPolicyNo(String discountPolicyNo) {
		this.discountPolicyNo = discountPolicyNo;
	}

	public String getDiscountPolicyName() {
		return discountPolicyName;
	}

	public void setDiscountPolicyName(String discountPolicyName) {
		this.discountPolicyName = discountPolicyName;
	}

}
