package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.api.panaver.order.seller.SellerServiceStub.CancelInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.ExchangeInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReturnInfo;
import com.cware.netshopping.manage.domain.model.PaNaverClaimList;

public class PaNaverClaimListVO extends PaNaverClaimList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1125970939719051703L;
	
	private String productOrderID;

	public void setCancelInfo(CancelInfo cancelInfo) {
		if(cancelInfo != null) {
			if(cancelInfo.getClaimStatus() != null) this.setClaimStatus(cancelInfo.getClaimStatus().getValue());
			if(cancelInfo.getClaimRequestDate() != null) this.setClaimRequestDate(new Timestamp(cancelInfo.getClaimRequestDate().getTimeInMillis()));
			if(cancelInfo.getRequestChannel() != null) this.setRequestChannel(cancelInfo.getRequestChannel());
			if(cancelInfo.getCancelReason() != null) this.setClaimReason(cancelInfo.getCancelReason().getValue());
			if(cancelInfo.getCancelDetailedReason() != null) this.setClaimDetailedReason(cancelInfo.getCancelDetailedReason());
			if(cancelInfo.getCancelCompletedDate() != null) this.setCancelCompletedDate(new Timestamp(cancelInfo.getCancelCompletedDate().getTimeInMillis()));
			if(cancelInfo.getCancelApprovalDate() != null) this.setCancelApprovalDate(new Timestamp(cancelInfo.getCancelApprovalDate().getTimeInMillis()));
			if(cancelInfo.getRefundExpectedDate() != null) this.setRefundExpectedDate(new Timestamp(cancelInfo.getRefundExpectedDate().getTimeInMillis()));
			if(cancelInfo.getRefundStandbyStatus() != null) this.setRefundStandbyStatus(cancelInfo.getRefundStandbyStatus());
			if(cancelInfo.getRefundStandbyReason() != null) this.setRefundStandbyReason(cancelInfo.getRefundStandbyReason());
		}
	}
	
	public void setExchangeInfo(ExchangeInfo exchangeInfo) {
		if(exchangeInfo != null) {
			if(exchangeInfo.getClaimStatus() != null) this.setClaimStatus(exchangeInfo.getClaimStatus().getValue());
			if(exchangeInfo.getClaimRequestDate() != null) this.setClaimRequestDate(new Timestamp(exchangeInfo.getClaimRequestDate().getTimeInMillis()));
			if(exchangeInfo.getRequestChannel() != null) this.setRequestChannel(exchangeInfo.getRequestChannel());
			if(exchangeInfo.getExchangeReason() != null) this.setClaimReason(exchangeInfo.getExchangeReason().getValue());
			if(exchangeInfo.getExchangeDetailedReason() != null) this.setClaimDetailedReason(exchangeInfo.getExchangeDetailedReason());
			if(exchangeInfo.getHoldbackStatus() != null) this.setHoldbackStatus(exchangeInfo.getHoldbackStatus().getValue());
			if(exchangeInfo.getHoldbackReason() != null) this.setHoldbackReason(exchangeInfo.getHoldbackReason().getValue());
			if(exchangeInfo.getHoldbackDetailedReason() != null) this.setHoldbackDetailedReason(exchangeInfo.getHoldbackDetailedReason());
			if(exchangeInfo.getCollectStatus() != null) this.setCollectStatus(exchangeInfo.getCollectStatus());
			if(exchangeInfo.getCollectDeliveryMethod() != null) this.setCollectDeliveryMethod(exchangeInfo.getCollectDeliveryMethod().getValue());
			if(exchangeInfo.getCollectDeliveryCompany() != null) this.setCollectDeliveryCompany(exchangeInfo.getCollectDeliveryCompany());
			if(exchangeInfo.getCollectTrackingNumber() != null) this.setCollectTrackingNumber(exchangeInfo.getCollectTrackingNumber());
			if(exchangeInfo.getCollectCompletedDate() != null) this.setCollectCompletedDate(new Timestamp(exchangeInfo.getCollectCompletedDate().getTimeInMillis()));
			if(exchangeInfo.getReDeliveryStatus() != null) this.setReDeliveryStatus(exchangeInfo.getReDeliveryStatus());
			if(exchangeInfo.getReDeliveryCompany() != null) this.setReDeliveryCompany(exchangeInfo.getReDeliveryCompany());
			if(exchangeInfo.getReDeliveryTrackingNumber() != null) this.setReDeliveryTrackingNumber(exchangeInfo.getReDeliveryTrackingNumber());
			if(exchangeInfo.getClaimDeliveryFeeDemandAmount() != Integer.MIN_VALUE) this.setDeliveryFeeDemandAmount(exchangeInfo.getClaimDeliveryFeeDemandAmount());
			if(exchangeInfo.getClaimDeliveryFeePayMethod() != null) this.setDeliveryFeePayMethod(exchangeInfo.getClaimDeliveryFeePayMethod());
			if(exchangeInfo.getClaimDeliveryFeePayMeans() != null) this.setDeliveryFeePayMeans(exchangeInfo.getClaimDeliveryFeePayMeans());
			if(exchangeInfo.getEtcFeeDemandAmount() != Integer.MIN_VALUE) this.setEtcFeeDemandAmount(exchangeInfo.getEtcFeeDemandAmount());
			if(exchangeInfo.getEtcFeePayMethod() != null) this.setEtcFeePayMethod(exchangeInfo.getEtcFeePayMethod());
			if(exchangeInfo.getEtcFeePayMeans() != null) this.setEtcFeePayMeans(exchangeInfo.getEtcFeePayMeans());
			if(exchangeInfo.getHoldbackConfigDate() != null) this.setHoldbackConfigDate(new Timestamp(exchangeInfo.getHoldbackConfigDate().getTimeInMillis()));
			if(exchangeInfo.getHoldbackConfigurer() != null) this.setHoldbackConfigurer(exchangeInfo.getHoldbackConfigurer());
			if(exchangeInfo.getHoldbackReleaseDate() != null) this.setHoldbackReleaseDate(new Timestamp(exchangeInfo.getHoldbackReleaseDate().getTimeInMillis()));
			if(exchangeInfo.getHoldbackReleaser() != null) this.setHoldbackReleaser(exchangeInfo.getHoldbackReleaser());
			if(exchangeInfo.getReDeliveryOperationDate() != null) this.setReDeliveryOperationDate(new Timestamp(exchangeInfo.getReDeliveryOperationDate().getTimeInMillis()));
			if(exchangeInfo.getClaimDeliveryFeeProductOrderIds() != null) this.setDeliveryFeeProductOrderIds(exchangeInfo.getClaimDeliveryFeeProductOrderIds());
			if(exchangeInfo.getClaimDeliveryFeeDiscountAmount() != Integer.MIN_VALUE) this.setDeliveryFeeDiscountAmount(exchangeInfo.getClaimDeliveryFeeDiscountAmount());
		}
	}
	
	public void setReturnInfo(ReturnInfo returnInfo) {
		if(returnInfo != null) {
			if(returnInfo.getClaimStatus() != null) this.setClaimStatus(returnInfo.getClaimStatus().getValue());
			if(returnInfo.getClaimRequestDate() != null) this.setClaimRequestDate(new Timestamp(returnInfo.getClaimRequestDate().getTimeInMillis()));
			if(returnInfo.getRequestChannel() != null) this.setRequestChannel(returnInfo.getRequestChannel());
			if(returnInfo.getReturnReason() != null) this.setClaimReason(returnInfo.getReturnReason().getValue());
			if(returnInfo.getReturnDetailedReason() != null) this.setClaimDetailedReason(returnInfo.getReturnDetailedReason());
			if(returnInfo.getHoldbackStatus() != null) this.setHoldbackStatus(returnInfo.getHoldbackStatus().getValue());
			if(returnInfo.getHoldbackReason() != null) this.setHoldbackReason(returnInfo.getHoldbackReason().getValue());
			if(returnInfo.getHoldbackDetailedReason() != null) this.setHoldbackDetailedReason(returnInfo.getHoldbackDetailedReason());
			if(returnInfo.getCollectStatus() != null) this.setCollectStatus(returnInfo.getCollectStatus());
			if(returnInfo.getCollectDeliveryMethod() != null) this.setCollectDeliveryMethod(returnInfo.getCollectDeliveryMethod().getValue());
			if(returnInfo.getCollectDeliveryCompany() != null) this.setCollectDeliveryCompany(returnInfo.getCollectDeliveryCompany());
			if(returnInfo.getCollectTrackingNumber() != null) this.setCollectTrackingNumber(returnInfo.getCollectTrackingNumber());
			if(returnInfo.getCollectCompletedDate() != null) this.setCollectCompletedDate(new Timestamp(returnInfo.getCollectCompletedDate().getTimeInMillis()));
			if(returnInfo.getClaimDeliveryFeeDemandAmount() != Integer.MIN_VALUE) this.setDeliveryFeeDemandAmount(returnInfo.getClaimDeliveryFeeDemandAmount());
			if(returnInfo.getClaimDeliveryFeePayMethod() != null) this.setDeliveryFeePayMethod(returnInfo.getClaimDeliveryFeePayMethod());
			if(returnInfo.getClaimDeliveryFeePayMeans() != null) this.setDeliveryFeePayMeans(returnInfo.getClaimDeliveryFeePayMeans());
			if(returnInfo.getEtcFeeDemandAmount() != Integer.MIN_VALUE) this.setEtcFeeDemandAmount(returnInfo.getEtcFeeDemandAmount());
			if(returnInfo.getEtcFeePayMethod() != null) this.setEtcFeePayMethod(returnInfo.getEtcFeePayMethod());
			if(returnInfo.getEtcFeePayMeans() != null) this.setEtcFeePayMeans(returnInfo.getEtcFeePayMeans());
			if(returnInfo.getRefundStandbyStatus() != null) this.setRefundStandbyStatus(returnInfo.getRefundStandbyStatus());
			if(returnInfo.getRefundStandbyReason() != null) this.setRefundStandbyReason(returnInfo.getRefundStandbyReason());
			if(returnInfo.getRefundExpectedDate() != null) this.setRefundExpectedDate(new Timestamp(returnInfo.getRefundExpectedDate().getTimeInMillis()));
			if(returnInfo.getReturnCompletedDate() != null) this.setReturnCompletedDate(new Timestamp(returnInfo.getReturnCompletedDate().getTimeInMillis()));
			if(returnInfo.getHoldbackConfigDate() != null) this.setHoldbackConfigDate(new Timestamp(returnInfo.getHoldbackConfigDate().getTimeInMillis()));
			if(returnInfo.getHoldbackConfigurer() != null) this.setHoldbackConfigurer(returnInfo.getHoldbackConfigurer());
			if(returnInfo.getHoldbackReleaseDate() != null) this.setHoldbackReleaseDate(new Timestamp(returnInfo.getHoldbackReleaseDate().getTimeInMillis()));
			if(returnInfo.getHoldbackReleaser() != null) this.setHoldbackReleaser(returnInfo.getHoldbackReleaser());
			if(returnInfo.getClaimDeliveryFeeProductOrderIds() != null) this.setDeliveryFeeProductOrderIds(returnInfo.getClaimDeliveryFeeProductOrderIds());
			if(returnInfo.getClaimDeliveryFeeDiscountAmount() != Integer.MIN_VALUE) this.setDeliveryFeeDiscountAmount(returnInfo.getClaimDeliveryFeeDiscountAmount());
		}
	}


	public String getProductOrderID() {
		return productOrderID;
	}


	public void setProductOrderID(String productOrderID) {
		this.productOrderID = productOrderID;
	}
}
