package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaTmonCancelList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	private String claimNo;
	private String tmonOrderNo;
	private String deliveryNo;
	private String userId;
	private String userName;
	private String orderDate;
	private String claimType;
	private String claimStatus;
	private String tmonDealNo;
	private String dealTitle;
	private String requestReason;
	private String requestReasonDetail;
	private String tmonDealOptionNo;
	private String dealOptionTitle;
	private long qty;	
	private String approvalReason;
	private String approvalReasonDetail;
	private String requestedDate;
	private String approvedDate;
	private String completedDate;
	private String holding;
	private String holdingReason;
	private String holdingDate;
	private String clearHoldingDate;
	private String partnerRtnDeliveryProcess;
	private String autoRefundWaiting;
	private String confirmer;
	private String deliveryCorp;
	private String invoiceNo;	
	private String returnDeliveryStatus;
	private String returnDeliveryCompletedDate;
	private boolean claimImageRegistration;
	private String withdrawYn;
	private Timestamp withdrawDate;
	private String procFlag;
	private String cancelProcNote;
	private String cancelProcId;
	private String procNote;
	
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
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
	public String getDeliveryCorp() {
		return deliveryCorp;
	}
	public void setDeliveryCorp(String deliveryCorp) {
		this.deliveryCorp = deliveryCorp;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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
	public boolean getClaimImageRegistration() {
		return claimImageRegistration;
	}
	public void setClaimImageRegistration(boolean claimImageRegistration) {
		this.claimImageRegistration = claimImageRegistration;
	}
	public String getWithdrawYn() {
		return withdrawYn;
	}
	public void setWithdrawYn(String withdrawYn) {
		this.withdrawYn = withdrawYn;
	}
	public Timestamp getWithdrawDate() {
		return withdrawDate;
	}
	public void setWithdrawDate(Timestamp withdrawDate) {
		this.withdrawDate = withdrawDate;
	}
	public String getProcFlag() {
		return procFlag;
	}
	public void setProcFlag(String procFlag) {
		this.procFlag = procFlag;
	}
	public String getCancelProcNote() {
		return cancelProcNote;
	}
	public void setCancelProcNote(String cancelProcNote) {
		this.cancelProcNote = cancelProcNote;
	}
	public String getCancelProcId() {
		return cancelProcId;
	}
	public void setCancelProcId(String cancelProcId) {
		this.cancelProcId = cancelProcId;
	}
	public String getProcNote() {
		return procNote;
	}
	public void setProcNote(String procNote) {
		this.procNote = procNote;
	}
}
