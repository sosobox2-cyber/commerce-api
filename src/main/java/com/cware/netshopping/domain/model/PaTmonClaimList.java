package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaTmonClaimList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	private String	paOrderGb; //제휴주문구분[J007]
	private String	claimNo;
	private String  userId;
	private String	userName;
	private String	mobilePhone;
	private String  tmonOrderNo;
	private String  deliveryNo;
	private String  orderDate;
	private String  claimType;
	private String  claimStatus;
	private String  approvalReason;
	private String  approvalReasonDetail;
	private String  requestedDate;
	private String  approvedDate;
	private String  completedDate;
	private String  holding;
	private String  holdingReason;
	private String  holdingDate;
	private String  clearHoldingDate;
	private String partnerRtnDeliveryProcess;
	private String autoRefundWaiting;
	private String  confirmer;
	private String  originDeliveryCorp;
	private String  originInvoiceNo;
	private String  originAdditionalInvoices;
	private String  returnDeliveryCorp;
	private String  returnInvoiceNo;
	private String  returnAdditionalInvoices;
	private String  returnDeliveryStatus;
	private String  returnDeliveryCompletedDate;
	private String  reDeliveryCorp;
	private String  reInvoiceNo;
	private String  reAdditionalInvoices;
	private String  tmonDealNo;
	private String  dealTitle;
	private String  requestReason;
	private String  requestReasonDetail;
	private String  tmonDealOptionNo;
	private String  dealOptionTitle;
	private long	qty;
	private Boolean claimImageRegistration;
	private String  returnDeliveryName;
	private String  returnDeliveryZipCode;
	private String  returnDeliveryFirstAddress;
	private String  returnDeliverySecondAddress;
	private String  returnDeliveryStreetAddress;
	private double	deduction;
	private double  returnDeliveryFee;
	private double  refundAmount;
	private String  pictures1;
	private String  pictures2;
	private String  pictures3;
	private String  pictures4;
	private String  pictures5;
	private String  returnDeliveryMobile;
	
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getTmonOrderNo() {
		return tmonOrderNo;
	}
	public void setTmonOrderNo(String tmonOrderNo) {
		this.tmonOrderNo = tmonOrderNo;
	}
	public String getDeliveryNo() {
		return deliveryNo;
	}
	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getApprovalReason() {
		return approvalReason;
	}
	public void setApprovalReason(String approvalReason) {
		this.approvalReason = approvalReason;
	}
	public String getApprovalReasonDetail() {
		return approvalReasonDetail;
	}
	public void setApprovalReasonDetail(String approvalReasonDetail) {
		this.approvalReasonDetail = approvalReasonDetail;
	}
	public String getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}
	public String getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}
	public String getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(String completedDate) {
		this.completedDate = completedDate;
	}
	public String getHolding() {
		return holding;
	}
	public void setHolding(String holding) {
		this.holding = holding;
	}
	public String getHoldingReason() {
		return holdingReason;
	}
	public void setHoldingReason(String holdingReason) {
		this.holdingReason = holdingReason;
	}
	public String getHoldingDate() {
		return holdingDate;
	}
	public void setHoldingDate(String holdingDate) {
		this.holdingDate = holdingDate;
	}
	public String getClearHoldingDate() {
		return clearHoldingDate;
	}
	public void setClearHoldingDate(String clearHoldingDate) {
		this.clearHoldingDate = clearHoldingDate;
	}
	public String getPartnerRtnDeliveryProcess() {
		return partnerRtnDeliveryProcess;
	}
	public void setPartnerRtnDeliveryProcess(String partnerRtnDeliveryProcess) {
		this.partnerRtnDeliveryProcess = partnerRtnDeliveryProcess;
	}
	public String getAutoRefundWaiting() {
		return autoRefundWaiting;
	}
	public void setAutoRefundWaiting(String autoRefundWaiting) {
		this.autoRefundWaiting = autoRefundWaiting;
	}
	public String getConfirmer() {
		return confirmer;
	}
	public void setConfirmer(String confirmer) {
		this.confirmer = confirmer;
	}
	public String getOriginDeliveryCorp() {
		return originDeliveryCorp;
	}
	public void setOriginDeliveryCorp(String originDeliveryCorp) {
		this.originDeliveryCorp = originDeliveryCorp;
	}
	public String getOriginInvoiceNo() {
		return originInvoiceNo;
	}
	public void setOriginInvoiceNo(String originInvoiceNo) {
		this.originInvoiceNo = originInvoiceNo;
	}
	public String getOriginAdditionalInvoices() {
		return originAdditionalInvoices;
	}
	public void setOriginAdditionalInvoices(String originAdditionalInvoices) {
		this.originAdditionalInvoices = originAdditionalInvoices;
	}
	public String getReturnDeliveryCorp() {
		return returnDeliveryCorp;
	}
	public void setReturnDeliveryCorp(String returnDeliveryCorp) {
		this.returnDeliveryCorp = returnDeliveryCorp;
	}
	public String getReturnInvoiceNo() {
		return returnInvoiceNo;
	}
	public void setReturnInvoiceNo(String returnInvoiceNo) {
		this.returnInvoiceNo = returnInvoiceNo;
	}
	public String getReturnAdditionalInvoices() {
		return returnAdditionalInvoices;
	}
	public void setReturnAdditionalInvoices(String returnAdditionalInvoices) {
		this.returnAdditionalInvoices = returnAdditionalInvoices;
	}
	public String getReturnDeliveryStatus() {
		return returnDeliveryStatus;
	}
	public void setReturnDeliveryStatus(String returnDeliveryStatus) {
		this.returnDeliveryStatus = returnDeliveryStatus;
	}
	public String getReturnDeliveryCompletedDate() {
		return returnDeliveryCompletedDate;
	}
	public void setReturnDeliveryCompletedDate(String returnDeliveryCompletedDate) {
		this.returnDeliveryCompletedDate = returnDeliveryCompletedDate;
	}
	public String getReDeliveryCorp() {
		return reDeliveryCorp;
	}
	public void setReDeliveryCorp(String reDeliveryCorp) {
		this.reDeliveryCorp = reDeliveryCorp;
	}
	public String getReInvoiceNo() {
		return reInvoiceNo;
	}
	public void setReInvoiceNo(String reInvoiceNo) {
		this.reInvoiceNo = reInvoiceNo;
	}
	public String getReAdditionalInvoices() {
		return reAdditionalInvoices;
	}
	public void setReAdditionalInvoices(String reAdditionalInvoices) {
		this.reAdditionalInvoices = reAdditionalInvoices;
	}
	public String getTmonDealNo() {
		return tmonDealNo;
	}
	public void setTmonDealNo(String tmonDealNo) {
		this.tmonDealNo = tmonDealNo;
	}
	public String getDealTitle() {
		return dealTitle;
	}
	public void setDealTitle(String dealTitle) {
		this.dealTitle = dealTitle;
	}
	public String getRequestReason() {
		return requestReason;
	}
	public void setRequestReason(String requestReason) {
		this.requestReason = requestReason;
	}
	public String getRequestReasonDetail() {
		return requestReasonDetail;
	}
	public void setRequestReasonDetail(String requestReasonDetail) {
		this.requestReasonDetail = requestReasonDetail;
	}
	public String getTmonDealOptionNo() {
		return tmonDealOptionNo;
	}
	public void setTmonDealOptionNo(String tmonDealOptionNo) {
		this.tmonDealOptionNo = tmonDealOptionNo;
	}
	public String getDealOptionTitle() {
		return dealOptionTitle;
	}
	public void setDealOptionTitle(String dealOptionTitle) {
		this.dealOptionTitle = dealOptionTitle;
	}
	public long getQty() {
		return qty;
	}
	public void setQty(long qty) {
		this.qty = qty;
	}
	public Boolean getClaimImageRegistration() {
		return claimImageRegistration;
	}
	public void setClaimImageRegistration(Boolean claimImageRegistration) {
		this.claimImageRegistration = claimImageRegistration;
	}
	public String getReturnDeliveryName() {
		return returnDeliveryName;
	}
	public void setReturnDeliveryName(String returnDeliveryName) {
		this.returnDeliveryName = returnDeliveryName;
	}
	public String getReturnDeliveryZipCode() {
		return returnDeliveryZipCode;
	}
	public void setReturnDeliveryZipCode(String returnDeliveryZipCode) {
		this.returnDeliveryZipCode = returnDeliveryZipCode;
	}
	public String getReturnDeliveryFirstAddress() {
		return returnDeliveryFirstAddress;
	}
	public void setReturnDeliveryFirstAddress(String returnDeliveryFirstAddress) {
		this.returnDeliveryFirstAddress = returnDeliveryFirstAddress;
	}
	public String getReturnDeliverySecondAddress() {
		return returnDeliverySecondAddress;
	}
	public void setReturnDeliverySecondAddress(String returnDeliverySecondAddress) {
		this.returnDeliverySecondAddress = returnDeliverySecondAddress;
	}
	public String getReturnDeliveryStreetAddress() {
		return returnDeliveryStreetAddress;
	}
	public void setReturnDeliveryStreetAddress(String returnDeliveryStreetAddress) {
		this.returnDeliveryStreetAddress = returnDeliveryStreetAddress;
	}
	public double getDeduction() {
		return deduction;
	}
	public void setDeduction(double deduction) {
		this.deduction = deduction;
	}
	public double getReturnDeliveryFee() {
		return returnDeliveryFee;
	}
	public void setReturnDeliveryFee(double returnDeliveryFee) {
		this.returnDeliveryFee = returnDeliveryFee;
	}
	public double getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getPictures1() {
		return pictures1;
	}
	public void setPictures1(String pictures1) {
		this.pictures1 = pictures1;
	}
	public String getPictures2() {
		return pictures2;
	}
	public void setPictures2(String pictures2) {
		this.pictures2 = pictures2;
	}
	public String getPictures3() {
		return pictures3;
	}
	public void setPictures3(String pictures3) {
		this.pictures3 = pictures3;
	}
	public String getPictures4() {
		return pictures4;
	}
	public void setPictures4(String pictures4) {
		this.pictures4 = pictures4;
	}
	public String getPictures5() {
		return pictures5;
	}
	public void setPictures5(String pictures5) {
		this.pictures5 = pictures5;
	}
	public String getReturnDeliveryMobile() {
		return returnDeliveryMobile;
	}
	public void setReturnDeliveryMobile(String returnDeliveryMobile) {
		this.returnDeliveryMobile = returnDeliveryMobile;
	}

}
