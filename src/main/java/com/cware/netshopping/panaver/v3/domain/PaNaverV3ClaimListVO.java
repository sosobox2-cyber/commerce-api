package com.cware.netshopping.panaver.v3.domain;

import com.cware.netshopping.manage.domain.model.PaNaverClaimList;

public class PaNaverV3ClaimListVO extends PaNaverClaimList {

	private static final long serialVersionUID = 1L;
	
	private String productOrderId;
	private String claimGb;

	public void setCancelInfo(CancelOrderInfo cancelOrderInfo) {
		if (cancelOrderInfo != null) {
			if (cancelOrderInfo.getClaimStatus() != null) this.setClaimStatus(cancelOrderInfo.getClaimStatus());
			if (cancelOrderInfo.getClaimRequestDate() != null) this.setClaimRequestDate(cancelOrderInfo.getClaimRequestDate());
			if (cancelOrderInfo.getRequestChannel() != null) this.setRequestChannel(cancelOrderInfo.getRequestChannel());
			if (cancelOrderInfo.getCancelReason() != null) this.setClaimReason(cancelOrderInfo.getCancelReason());
			if (cancelOrderInfo.getCancelDetailedReason() != null) this.setClaimDetailedReason(cancelOrderInfo.getCancelDetailedReason());
			if (cancelOrderInfo.getCancelCompletedDate() != null) this.setCancelCompletedDate(cancelOrderInfo.getCancelCompletedDate());
			if (cancelOrderInfo.getCancelApprovalDate() != null) this.setCancelApprovalDate(cancelOrderInfo.getCancelApprovalDate());
			if (cancelOrderInfo.getRefundExpectedDate() != null) this.setRefundExpectedDate(cancelOrderInfo.getRefundExpectedDate());
			if (cancelOrderInfo.getRefundStandbyStatus() != null) this.setRefundStandbyStatus(cancelOrderInfo.getRefundStandbyStatus());
			if (cancelOrderInfo.getRefundStandbyReason() != null) this.setRefundStandbyReason(cancelOrderInfo.getRefundStandbyReason());
		}
	}
	
	public void setReturnInfo(ReturnOrderInfo returnInfo) {
		if (returnInfo != null) {
			if (returnInfo.getClaimStatus() != null) this.setClaimStatus(returnInfo.getClaimStatus());
			if (returnInfo.getClaimRequestDate() != null) this.setClaimRequestDate(returnInfo.getClaimRequestDate());
			if (returnInfo.getRequestChannel() != null) this.setRequestChannel(returnInfo.getRequestChannel());
			if (returnInfo.getReturnReason() != null) this.setClaimReason(returnInfo.getReturnReason());
			if (returnInfo.getReturnDetailedReason() != null) this.setClaimDetailedReason(returnInfo.getReturnDetailedReason());
			if (returnInfo.getHoldbackStatus() != null) this.setHoldbackStatus(returnInfo.getHoldbackStatus());
			if (returnInfo.getHoldbackReason() != null) this.setHoldbackReason(returnInfo.getHoldbackReason());
			if (returnInfo.getHoldbackDetailedReason() != null) this.setHoldbackDetailedReason(returnInfo.getHoldbackDetailedReason());
			if (returnInfo.getCollectStatus() != null) this.setCollectStatus(returnInfo.getCollectStatus());
			if (returnInfo.getCollectDeliveryMethod() != null) this.setCollectDeliveryMethod(returnInfo.getCollectDeliveryMethod());
			if (returnInfo.getCollectDeliveryCompany() != null) this.setCollectDeliveryCompany(returnInfo.getCollectDeliveryCompany());
			if (returnInfo.getCollectTrackingNumber() != null) this.setCollectTrackingNumber(returnInfo.getCollectTrackingNumber());
			if (returnInfo.getCollectCompletedDate() != null) this.setCollectCompletedDate(returnInfo.getCollectCompletedDate());
			if (returnInfo.getClaimDeliveryFeeDemandAmount() != Integer.MIN_VALUE) this.setDeliveryFeeDemandAmount((long) returnInfo.getClaimDeliveryFeeDemandAmount());
			if (returnInfo.getClaimDeliveryFeePayMethod() != null) this.setDeliveryFeePayMethod(returnInfo.getClaimDeliveryFeePayMethod());
			if (returnInfo.getClaimDeliveryFeePayMeans() != null) this.setDeliveryFeePayMeans(returnInfo.getClaimDeliveryFeePayMeans());
			if (returnInfo.getEtcFeeDemandAmount() != Integer.MIN_VALUE) this.setEtcFeeDemandAmount((long) returnInfo.getEtcFeeDemandAmount());
			if (returnInfo.getEtcFeePayMethod() != null) this.setEtcFeePayMethod(returnInfo.getEtcFeePayMethod());
			if (returnInfo.getEtcFeePayMeans() != null) this.setEtcFeePayMeans(returnInfo.getEtcFeePayMeans());
			if (returnInfo.getRefundStandbyStatus() != null) this.setRefundStandbyStatus(returnInfo.getRefundStandbyStatus());
			if (returnInfo.getRefundStandbyReason() != null) this.setRefundStandbyReason(returnInfo.getRefundStandbyReason());
			if (returnInfo.getRefundExpectedDate() != null) this.setRefundExpectedDate(returnInfo.getRefundExpectedDate());
			if (returnInfo.getReturnCompletedDate() != null) this.setReturnCompletedDate(returnInfo.getReturnCompletedDate());
			if (returnInfo.getHoldbackConfigDate() != null) this.setHoldbackConfigDate(returnInfo.getHoldbackConfigDate());
			if (returnInfo.getHoldbackConfigurer() != null) this.setHoldbackConfigurer(returnInfo.getHoldbackConfigurer());
			if (returnInfo.getHoldbackReleaseDate() != null) this.setHoldbackReleaseDate(returnInfo.getHoldbackReleaseDate());
			if (returnInfo.getHoldbackReleaser() != null) this.setHoldbackReleaser(returnInfo.getHoldbackReleaser());
			if (returnInfo.getClaimDeliveryFeeProductOrderIds() != null) this.setDeliveryFeeProductOrderIds(returnInfo.getClaimDeliveryFeeProductOrderIds());
			if (returnInfo.getClaimDeliveryFeeDiscountAmount() != Integer.MIN_VALUE) this.setDeliveryFeeDiscountAmount((long) returnInfo.getClaimDeliveryFeeDiscountAmount());
		}
	}
	
	public void setExchangeInfo(ExchangeOrderInfo exchangeInfo) {
		if (exchangeInfo != null) {
			if (exchangeInfo.getClaimStatus() != null) this.setClaimStatus(exchangeInfo.getClaimStatus());
			if (exchangeInfo.getClaimRequestDate() != null) this.setClaimRequestDate(exchangeInfo.getClaimRequestDate());
			if (exchangeInfo.getRequestChannel() != null) this.setRequestChannel(exchangeInfo.getRequestChannel());
			if (exchangeInfo.getExchangeReason() != null) this.setClaimReason(exchangeInfo.getExchangeReason());
			if (exchangeInfo.getExchangeDetailedReason() != null) this.setClaimDetailedReason(exchangeInfo.getExchangeDetailedReason());
			if (exchangeInfo.getHoldbackStatus() != null) this.setHoldbackStatus(exchangeInfo.getHoldbackStatus());
			if (exchangeInfo.getHoldbackReason() != null) this.setHoldbackReason(exchangeInfo.getHoldbackReason());
			if (exchangeInfo.getHoldbackDetailedReason() != null) this.setHoldbackDetailedReason(exchangeInfo.getHoldbackDetailedReason());
			if (exchangeInfo.getCollectStatus() != null) this.setCollectStatus(exchangeInfo.getCollectStatus());
			if (exchangeInfo.getCollectDeliveryMethod() != null) this.setCollectDeliveryMethod(exchangeInfo.getCollectDeliveryMethod());
			if (exchangeInfo.getCollectDeliveryCompany() != null) this.setCollectDeliveryCompany(exchangeInfo.getCollectDeliveryCompany());
			if (exchangeInfo.getCollectTrackingNumber() != null) this.setCollectTrackingNumber(exchangeInfo.getCollectTrackingNumber());
			if (exchangeInfo.getCollectCompletedDate() != null) this.setCollectCompletedDate(exchangeInfo.getCollectCompletedDate());
			if (exchangeInfo.getReDeliveryStatus() != null) this.setReDeliveryStatus(exchangeInfo.getReDeliveryStatus());
			if (exchangeInfo.getReDeliveryCompany() != null) this.setReDeliveryCompany(exchangeInfo.getReDeliveryCompany());
			if (exchangeInfo.getReDeliveryTrackingNumber() != null) this.setReDeliveryTrackingNumber(exchangeInfo.getReDeliveryTrackingNumber());
			if (exchangeInfo.getClaimDeliveryFeeDemandAmount() != Integer.MIN_VALUE) this.setDeliveryFeeDemandAmount((long) exchangeInfo.getClaimDeliveryFeeDemandAmount());
			if (exchangeInfo.getClaimDeliveryFeePayMethod() != null) this.setDeliveryFeePayMethod(exchangeInfo.getClaimDeliveryFeePayMethod());
			if (exchangeInfo.getClaimDeliveryFeePayMeans() != null) this.setDeliveryFeePayMeans(exchangeInfo.getClaimDeliveryFeePayMeans());
			if (exchangeInfo.getEtcFeeDemandAmount() != Integer.MIN_VALUE) this.setEtcFeeDemandAmount((long) exchangeInfo.getEtcFeeDemandAmount());
			if (exchangeInfo.getEtcFeePayMethod() != null) this.setEtcFeePayMethod(exchangeInfo.getEtcFeePayMethod());
			if (exchangeInfo.getEtcFeePayMeans() != null) this.setEtcFeePayMeans(exchangeInfo.getEtcFeePayMeans());
			if (exchangeInfo.getHoldbackConfigDate() != null) this.setHoldbackConfigDate(exchangeInfo.getHoldbackConfigDate());
			if (exchangeInfo.getHoldbackConfigurer() != null) this.setHoldbackConfigurer(exchangeInfo.getHoldbackConfigurer());
			if (exchangeInfo.getHoldbackReleaseDate() != null) this.setHoldbackReleaseDate(exchangeInfo.getHoldbackReleaseDate());
			if (exchangeInfo.getHoldbackReleaser() != null) this.setHoldbackReleaser(exchangeInfo.getHoldbackReleaser());
			if (exchangeInfo.getReDeliveryOperationDate() != null) this.setReDeliveryOperationDate(exchangeInfo.getReDeliveryOperationDate());
			if (exchangeInfo.getClaimDeliveryFeeProductOrderIds() != null) this.setDeliveryFeeProductOrderIds(exchangeInfo.getClaimDeliveryFeeProductOrderIds());
			if (exchangeInfo.getClaimDeliveryFeeDiscountAmount() != Integer.MIN_VALUE) this.setDeliveryFeeDiscountAmount((long) exchangeInfo.getClaimDeliveryFeeDiscountAmount());
		}
	}

	public String getProductOrderId() {
		return productOrderId;
	}

	public void setProductOrderId(String productOrderId) {
		this.productOrderId = productOrderId;
	}

	public String getClaimGb() {
		return claimGb;
	}

	public void setClaimGb(String claimGb) {
		this.claimGb = claimGb;
	}
}
