package com.cware.netshopping.manage.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaNaverClaimList extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String claimSeq;
	private String claimStatus;
	private Timestamp claimRequestDate;
	private String requestChannel;
	private String claimReason;
	private String claimDetailedReason;
	private String holdbackStatus;
	private String holdbackReason;
	private String holdbackDetailedReason;
	private String collectAddressSeq;
	private String returnReceiveAddress;
	private String collectStatus;
	private String collectDeliveryMethod;
	private String collectDeliveryCompany;
	private String collectTrackingNumber;
	private Timestamp collectCompletedDate;
	private long deliveryFeeDemandAmount;
	private String deliveryFeePayMethod;
	private String deliveryFeePayMeans;
	private long etcFeeDemandAmount;
	private String etcFeePayMethod;
	private String etcFeePayMeans;
	private String refundStandbyStatus;
	private String refundStandbyReason;
	private Timestamp refundExpectedDate;
	private Timestamp returnCompletedDate;
	private Timestamp holdbackConfigDate;
	private String holdbackConfigurer;
	private Timestamp holdbackReleaseDate;
	private String holdbackReleaser;
	private String deliveryFeeProductOrderIds;
	private long deliveryFeeDiscountAmount;
	private String reDeliveryStatus;
	private String reDeliveryMethod;
	private String reDeliveryCompany;
	private String reDeliveryTrackingNumber;
	private Timestamp reDeliveryOperationDate;
	private Timestamp cancelCompletedDate;
	private Timestamp cancelApprovalDate;
	private String decisionHoldbackTreatMemo;
	private Timestamp sellerOperationDate;

	public String getClaimSeq() { 
		return this.claimSeq;
	}
	public String getClaimStatus() { 
		return this.claimStatus;
	}
	public Timestamp getClaimRequestDate() { 
		return this.claimRequestDate;
	}
	public String getRequestChannel() { 
		return this.requestChannel;
	}
	public String getClaimReason() { 
		return this.claimReason;
	}
	public String getClaimDetailedReason() { 
		return this.claimDetailedReason;
	}
	public String getHoldbackStatus() { 
		return this.holdbackStatus;
	}
	public String getCollectAddressSeq() { 
		return this.collectAddressSeq;
	}
	public String getReturnReceiveAddress() { 
		return this.returnReceiveAddress;
	}
	public String getCollectStatus() { 
		return this.collectStatus;
	}
	public String getCollectDeliveryMethod() { 
		return this.collectDeliveryMethod;
	}
	public String getCollectDeliveryCompany() { 
		return this.collectDeliveryCompany;
	}
	public String getCollectTrackingNumber() { 
		return this.collectTrackingNumber;
	}
	public Timestamp getCollectCompletedDate() { 
		return this.collectCompletedDate;
	}
	public long getDeliveryFeeDemandAmount() { 
		return this.deliveryFeeDemandAmount;
	}
	public String getDeliveryFeePayMethod() { 
		return this.deliveryFeePayMethod;
	}
	public String getDeliveryFeePayMeans() { 
		return this.deliveryFeePayMeans;
	}
	public long getEtcFeeDemandAmount() { 
		return this.etcFeeDemandAmount;
	}
	public String getEtcFeePayMethod() { 
		return this.etcFeePayMethod;
	}
	public String getEtcFeePayMeans() { 
		return this.etcFeePayMeans;
	}
	public String getRefundStandbyStatus() { 
		return this.refundStandbyStatus;
	}
	public String getRefundStandbyReason() { 
		return this.refundStandbyReason;
	}
	public Timestamp getRefundExpectedDate() { 
		return this.refundExpectedDate;
	}
	public Timestamp getReturnCompletedDate() { 
		return this.returnCompletedDate;
	}
	public Timestamp getHoldbackConfigDate() { 
		return this.holdbackConfigDate;
	}
	public String getHoldbackConfigurer() { 
		return this.holdbackConfigurer;
	}
	public Timestamp getHoldbackReleaseDate() { 
		return this.holdbackReleaseDate;
	}
	public String getHoldbackReleaser() { 
		return this.holdbackReleaser;
	}
	public String getDeliveryFeeProductOrderIds() { 
		return this.deliveryFeeProductOrderIds;
	}
	public long getDeliveryFeeDiscountAmount() { 
		return this.deliveryFeeDiscountAmount;
	}
	public String getReDeliveryStatus() { 
		return this.reDeliveryStatus;
	}
	public String getReDeliveryMethod() { 
		return this.reDeliveryMethod;
	}
	public String getReDeliveryCompany() { 
		return this.reDeliveryCompany;
	}
	public String getReDeliveryTrackingNumber() { 
		return this.reDeliveryTrackingNumber;
	}
	public Timestamp getCancelCompletedDate() { 
		return this.cancelCompletedDate;
	}
	public Timestamp getCancelApprovalDate() { 
		return this.cancelApprovalDate;
	}
	public String getDecisionHoldbackTreatMemo() { 
		return this.decisionHoldbackTreatMemo;
	}
	public Timestamp getSellerOperationDate() { 
		return this.sellerOperationDate;
	}

	public void setClaimSeq(String claimSeq) { 
		this.claimSeq = claimSeq;
	}
	public void setClaimStatus(String claimStatus) { 
		this.claimStatus = claimStatus;
	}
	public void setClaimRequestDate(Timestamp claimRequestDate) { 
		this.claimRequestDate = claimRequestDate;
	}
	public void setRequestChannel(String requestChannel) { 
		this.requestChannel = requestChannel;
	}
	public void setClaimReason(String claimReason) { 
		this.claimReason = claimReason;
	}
	public void setClaimDetailedReason(String claimDetailedReason) { 
		this.claimDetailedReason = claimDetailedReason;
	}
	public void setHoldbackStatus(String holdbackStatus) { 
		this.holdbackStatus = holdbackStatus;
	}
	public void setCollectAddressSeq(String collectAddressSeq) { 
		this.collectAddressSeq = collectAddressSeq;
	}
	public void setReturnReceiveAddress(String returnReceiveAddress) { 
		this.returnReceiveAddress = returnReceiveAddress;
	}
	public void setCollectStatus(String collectStatus) { 
		this.collectStatus = collectStatus;
	}
	public void setCollectDeliveryMethod(String collectDeliveryMethod) { 
		this.collectDeliveryMethod = collectDeliveryMethod;
	}
	public void setCollectDeliveryCompany(String collectDeliveryCompany) { 
		this.collectDeliveryCompany = collectDeliveryCompany;
	}
	public void setCollectTrackingNumber(String collectTrackingNumber) { 
		this.collectTrackingNumber = collectTrackingNumber;
	}
	public void setCollectCompletedDate(Timestamp collectCompletedDate) { 
		this.collectCompletedDate = collectCompletedDate;
	}
	public void setDeliveryFeeDemandAmount(long deliveryFeeDemandAmount) { 
		this.deliveryFeeDemandAmount = deliveryFeeDemandAmount;
	}
	public void setDeliveryFeePayMethod(String deliveryFeePayMethod) { 
		this.deliveryFeePayMethod = deliveryFeePayMethod;
	}
	public void setDeliveryFeePayMeans(String deliveryFeePayMeans) { 
		this.deliveryFeePayMeans = deliveryFeePayMeans;
	}
	public void setEtcFeeDemandAmount(long etcFeeDemandAmount) { 
		this.etcFeeDemandAmount = etcFeeDemandAmount;
	}
	public void setEtcFeePayMethod(String etcFeePayMethod) { 
		this.etcFeePayMethod = etcFeePayMethod;
	}
	public void setEtcFeePayMeans(String etcFeePayMeans) { 
		this.etcFeePayMeans = etcFeePayMeans;
	}
	public void setRefundStandbyStatus(String refundStandbyStatus) { 
		this.refundStandbyStatus = refundStandbyStatus;
	}
	public void setRefundStandbyReason(String refundStandbyReason) { 
		this.refundStandbyReason = refundStandbyReason;
	}
	public void setRefundExpectedDate(Timestamp refundExpectedDate) { 
		this.refundExpectedDate = refundExpectedDate;
	}
	public void setReturnCompletedDate(Timestamp returnCompletedDate) { 
		this.returnCompletedDate = returnCompletedDate;
	}
	public void setHoldbackConfigDate(Timestamp holdbackConfigDate) { 
		this.holdbackConfigDate = holdbackConfigDate;
	}
	public void setHoldbackConfigurer(String holdbackConfigurer) { 
		this.holdbackConfigurer = holdbackConfigurer;
	}
	public void setHoldbackReleaseDate(Timestamp holdbackReleaseDate) { 
		this.holdbackReleaseDate = holdbackReleaseDate;
	}
	public void setHoldbackReleaser(String holdbackReleaser) { 
		this.holdbackReleaser = holdbackReleaser;
	}
	public void setDeliveryFeeProductOrderIds(String deliveryFeeProductOrderIds) { 
		this.deliveryFeeProductOrderIds = deliveryFeeProductOrderIds;
	}
	public void setDeliveryFeeDiscountAmount(long deliveryFeeDiscountAmount) { 
		this.deliveryFeeDiscountAmount = deliveryFeeDiscountAmount;
	}
	public void setReDeliveryStatus(String reDeliveryStatus) { 
		this.reDeliveryStatus = reDeliveryStatus;
	}
	public void setReDeliveryMethod(String reDeliveryMethod) { 
		this.reDeliveryMethod = reDeliveryMethod;
	}
	public void setReDeliveryCompany(String reDeliveryCompany) { 
		this.reDeliveryCompany = reDeliveryCompany;
	}
	public void setReDeliveryTrackingNumber(String reDeliveryTrackingNumber) { 
		this.reDeliveryTrackingNumber = reDeliveryTrackingNumber;
	}
	public void setCancelCompletedDate(Timestamp cancelCompletedDate) { 
		this.cancelCompletedDate = cancelCompletedDate;
	}
	public void setCancelApprovalDate(Timestamp cancelApprovalDate) { 
		this.cancelApprovalDate = cancelApprovalDate;
	}
	public void setDecisionHoldbackTreatMemo(String decisionHoldbackTreatMemo) { 
		this.decisionHoldbackTreatMemo = decisionHoldbackTreatMemo;
	}
	public void setSellerOperationDate(Timestamp sellerOperationDate) { 
		this.sellerOperationDate = sellerOperationDate;
	}
	public String getHoldbackReason() {
		return holdbackReason;
	}
	public void setHoldbackReason(String holdbackReason) {
		this.holdbackReason = holdbackReason;
	}
	public String getHoldbackDetailedReason() {
		return holdbackDetailedReason;
	}
	public void setHoldbackDetailedReason(String holdbackDetailedReason) {
		this.holdbackDetailedReason = holdbackDetailedReason;
	}
	public Timestamp getReDeliveryOperationDate() {
		return reDeliveryOperationDate;
	}
	public void setReDeliveryOperationDate(Timestamp reDeliveryOperationDate) {
		this.reDeliveryOperationDate = reDeliveryOperationDate;
	}
}
