package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Pacopnclaimlist extends AbstractModel{
	private static final long serialVersionUID = 1L;
	
	private String paOrderGb;
	private String receiptId;
	private String orderId;
	private String paymentId;
	private String receiptType;
	private String receiptStatus;
	private String requesterName;
	private String requesterPhoneNumber;
	private String requesterRealPhoneNumber;
	private String requesterAddress;
	private String requesterAddressDetail;
	private String requesterZipCode;
	private String cancelReasonCategory1;
	private String cancelReasonCategory2;
	private String cancelReason;
	private String returnDeliveryId;
	private String returnDeliveryType;
	private String releaseStopStatus;
	private String faultByType;
	private String preRefund;
	private String completeConfirmType;
	private String returnDeliveryDtos;
	private Long returnShippingCharge;
	
	private long cancelCountSum;
	private long enclosePrice;
	
	private Timestamp createdAt;
	private Timestamp modifiedAt;
	private Timestamp completeConfirmDate;
	
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getReceiptType() {
		return receiptType;
	}
	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}
	public String getReceiptStatus() {
		return receiptStatus;
	}
	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
	}
	public String getRequesterName() {
		return requesterName;
	}
	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}
	public String getRequesterPhoneNumber() {
		return requesterPhoneNumber;
	}
	public void setRequesterPhoneNumber(String requesterPhoneNumber) {
		this.requesterPhoneNumber = requesterPhoneNumber;
	}
	public String getRequesterRealPhoneNumber() {
		return requesterRealPhoneNumber;
	}
	public void setRequesterRealPhoneNumber(String requesterRealPhoneNumber) {
		this.requesterRealPhoneNumber = requesterRealPhoneNumber;
	}
	public String getRequesterAddress() {
		return requesterAddress;
	}
	public void setRequesterAddress(String requesterAddress) {
		this.requesterAddress = requesterAddress;
	}
	public String getRequesterAddressDetail() {
		return requesterAddressDetail;
	}
	public void setRequesterAddressDetail(String requesterAddressDetail) {
		this.requesterAddressDetail = requesterAddressDetail;
	}
	public String getRequesterZipCode() {
		return requesterZipCode;
	}
	public void setRequesterZipCode(String requesterZipCode) {
		this.requesterZipCode = requesterZipCode;
	}
	public String getCancelReasonCategory1() {
		return cancelReasonCategory1;
	}
	public void setCancelReasonCategory1(String cancelReasonCategory1) {
		this.cancelReasonCategory1 = cancelReasonCategory1;
	}
	public String getCancelReasonCategory2() {
		return cancelReasonCategory2;
	}
	public void setCancelReasonCategory2(String cancelReasonCategory2) {
		this.cancelReasonCategory2 = cancelReasonCategory2;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getReturnDeliveryId() {
		return returnDeliveryId;
	}
	public void setReturnDeliveryId(String returnDeliveryId) {
		this.returnDeliveryId = returnDeliveryId;
	}
	public String getReturnDeliveryType() {
		return returnDeliveryType;
	}
	public void setReturnDeliveryType(String returnDeliveryType) {
		this.returnDeliveryType = returnDeliveryType;
	}
	public String getReleaseStopStatus() {
		return releaseStopStatus;
	}
	public void setReleaseStopStatus(String releaseStopStatus) {
		this.releaseStopStatus = releaseStopStatus;
	}
	public String getFaultByType() {
		return faultByType;
	}
	public void setFaultByType(String faultByType) {
		this.faultByType = faultByType;
	}
	public String getPreRefund() {
		return preRefund;
	}
	public void setPreRefund(String preRefund) {
		this.preRefund = preRefund;
	}
	public String getCompleteConfirmType() {
		return completeConfirmType;
	}
	public void setCompleteConfirmType(String completeConfirmType) {
		this.completeConfirmType = completeConfirmType;
	}
	public String getReturnDeliveryDtos() {
		return returnDeliveryDtos;
	}
	public void setReturnDeliveryDtos(String returnDeliveryDtos) {
		this.returnDeliveryDtos = returnDeliveryDtos;
	}
	public long getCancelCountSum() {
		return cancelCountSum;
	}
	public void setCancelCountSum(long cancelCountSum) {
		this.cancelCountSum = cancelCountSum;
	}
	public long getEnclosePrice() {
		return enclosePrice;
	}
	public void setEnclosePrice(long enclosePrice) {
		this.enclosePrice = enclosePrice;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getModifiedAt() {
		return modifiedAt;
	}
	public void setModifiedAt(Timestamp modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	public Timestamp getCompleteConfirmDate() {
		return completeConfirmDate;
	}
	public void setCompleteConfirmDate(Timestamp completeConfirmDate) {
		this.completeConfirmDate = completeConfirmDate;
	}
	public Long getReturnShippingCharge() {
		return returnShippingCharge;
	}
	public void setReturnShippingCharge(Long returnShippingCharge) {
		this.returnShippingCharge = returnShippingCharge;
	}
}
