package com.cware.netshopping.panaver.v3.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnOrderInfo {
	
	//반품 배송비 청구액
	private double claimDeliveryFeeDemandAmount;
	//반품 배송비 결제 수단
	private String claimDeliveryFeePayMeans;
	//반품 배송비 결제 방법
	private String claimDeliveryFeePayMethod;
	//클레임 요청일
	private Timestamp claimRequestDate;
	//클레임 상태.
	private String claimStatus;
	//회수지
	private CollectAddress collectAddress;//object
	//수거 완료일
	private Timestamp collectCompletedDate;
	//택배사 코드
	private String collectDeliveryCompany;
	//배송 방법 코드
	private String collectDeliveryMethod;
	//수거 상태
	private String collectStatus;
	//수거 송장 번호
	private String collectTrackingNumber;
	//기타 비용 청구액
	private double etcFeeDemandAmount;
	//기타 비용 결제 수단
	private String etcFeePayMeans;
	//기타 비용 결제 방법
	private String etcFeePayMethod;
	//보류 상세 사유
	private String holdbackDetailedReason;
	//보류 유형
	private String holdbackReason;
	//보류 상태(HOLDBACK 보류 중, RELEASED 보류 해제)
	private String holdbackStatus;
	//환불 예정일
	private Timestamp refundExpectedDate;
	//환불 대기 사유
	private String refundStandbyReason;
	//환불 대기 상태
	private String refundStandbyStatus;
	//접수 채널
	private String requestChannel;
	//반품 상세 사유
	private String returnDetailedReason;
	//클레임 요청 사유
	private String returnReason;
	//수거지
	private ReturnReceiveAddress returnReceiveAddress;//object
	//반품 완료일
	private Timestamp returnCompletedDate;
	//보류 설정일
	private Timestamp holdbackConfigDate;
	//보류 설정자(구매자/판매자/관리자/시스템)
	private String holdbackConfigurer;
	//보류 해제일
	private Timestamp holdbackReleaseDate;
	//보류 해제자(구매자/판매자/관리자/시스템).
	private String holdbackReleaser;
	//반품 배송비 묶음 청구 상품 주문 번호(여러 개면 쉼표로 구분).
	private String claimDeliveryFeeProductOrderIds;
	//반품 배송비 할인액
	private double claimDeliveryFeeDiscountAmount;
	//반품 도서산간 배송비
	private double remoteAreaCostChargeAmount;
	public double getClaimDeliveryFeeDemandAmount() {
		return claimDeliveryFeeDemandAmount;
	}
	public void setClaimDeliveryFeeDemandAmount(double claimDeliveryFeeDemandAmount) {
		this.claimDeliveryFeeDemandAmount = claimDeliveryFeeDemandAmount;
	}
	public String getClaimDeliveryFeePayMeans() {
		return claimDeliveryFeePayMeans;
	}
	public void setClaimDeliveryFeePayMeans(String claimDeliveryFeePayMeans) {
		this.claimDeliveryFeePayMeans = claimDeliveryFeePayMeans;
	}
	public String getClaimDeliveryFeePayMethod() {
		return claimDeliveryFeePayMethod;
	}
	public void setClaimDeliveryFeePayMethod(String claimDeliveryFeePayMethod) {
		this.claimDeliveryFeePayMethod = claimDeliveryFeePayMethod;
	}
	public Timestamp getClaimRequestDate() {
		return claimRequestDate;
	}
	public void setClaimRequestDate(Timestamp claimRequestDate) {
		this.claimRequestDate = claimRequestDate;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public CollectAddress getCollectAddress() {
		return collectAddress;
	}
	public void setCollectAddress(CollectAddress collectAddress) {
		this.collectAddress = collectAddress;
	}
	public Timestamp getCollectCompletedDate() {
		return collectCompletedDate;
	}
	public void setCollectCompletedDate(Timestamp collectCompletedDate) {
		this.collectCompletedDate = collectCompletedDate;
	}
	public String getCollectDeliveryCompany() {
		return collectDeliveryCompany;
	}
	public void setCollectDeliveryCompany(String collectDeliveryCompany) {
		this.collectDeliveryCompany = collectDeliveryCompany;
	}
	public String getCollectDeliveryMethod() {
		return collectDeliveryMethod;
	}
	public void setCollectDeliveryMethod(String collectDeliveryMethod) {
		this.collectDeliveryMethod = collectDeliveryMethod;
	}
	public String getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(String collectStatus) {
		this.collectStatus = collectStatus;
	}
	public String getCollectTrackingNumber() {
		return collectTrackingNumber;
	}
	public void setCollectTrackingNumber(String collectTrackingNumber) {
		this.collectTrackingNumber = collectTrackingNumber;
	}
	public double getEtcFeeDemandAmount() {
		return etcFeeDemandAmount;
	}
	public void setEtcFeeDemandAmount(double etcFeeDemandAmount) {
		this.etcFeeDemandAmount = etcFeeDemandAmount;
	}
	public String getEtcFeePayMeans() {
		return etcFeePayMeans;
	}
	public void setEtcFeePayMeans(String etcFeePayMeans) {
		this.etcFeePayMeans = etcFeePayMeans;
	}
	public String getEtcFeePayMethod() {
		return etcFeePayMethod;
	}
	public void setEtcFeePayMethod(String etcFeePayMethod) {
		this.etcFeePayMethod = etcFeePayMethod;
	}
	public String getHoldbackDetailedReason() {
		return holdbackDetailedReason;
	}
	public void setHoldbackDetailedReason(String holdbackDetailedReason) {
		this.holdbackDetailedReason = holdbackDetailedReason;
	}
	public String getHoldbackReason() {
		return holdbackReason;
	}
	public void setHoldbackReason(String holdbackReason) {
		this.holdbackReason = holdbackReason;
	}
	public String getHoldbackStatus() {
		return holdbackStatus;
	}
	public void setHoldbackStatus(String holdbackStatus) {
		this.holdbackStatus = holdbackStatus;
	}
	public Timestamp getRefundExpectedDate() {
		return refundExpectedDate;
	}
	public void setRefundExpectedDate(Timestamp refundExpectedDate) {
		this.refundExpectedDate = refundExpectedDate;
	}
	public String getRefundStandbyReason() {
		return refundStandbyReason;
	}
	public void setRefundStandbyReason(String refundStandbyReason) {
		this.refundStandbyReason = refundStandbyReason;
	}
	public String getRefundStandbyStatus() {
		return refundStandbyStatus;
	}
	public void setRefundStandbyStatus(String refundStandbyStatus) {
		this.refundStandbyStatus = refundStandbyStatus;
	}
	public String getRequestChannel() {
		return requestChannel;
	}
	public void setRequestChannel(String requestChannel) {
		this.requestChannel = requestChannel;
	}
	public String getReturnDetailedReason() {
		return returnDetailedReason;
	}
	public void setReturnDetailedReason(String returnDetailedReason) {
		this.returnDetailedReason = returnDetailedReason;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public ReturnReceiveAddress getReturnReceiveAddress() {
		return returnReceiveAddress;
	}
	public void setReturnReceiveAddress(ReturnReceiveAddress returnReceiveAddress) {
		this.returnReceiveAddress = returnReceiveAddress;
	}
	public Timestamp getReturnCompletedDate() {
		return returnCompletedDate;
	}
	public void setReturnCompletedDate(Timestamp returnCompletedDate) {
		this.returnCompletedDate = returnCompletedDate;
	}
	public Timestamp getHoldbackConfigDate() {
		return holdbackConfigDate;
	}
	public void setHoldbackConfigDate(Timestamp holdbackConfigDate) {
		this.holdbackConfigDate = holdbackConfigDate;
	}
	public String getHoldbackConfigurer() {
		return holdbackConfigurer;
	}
	public void setHoldbackConfigurer(String holdbackConfigurer) {
		this.holdbackConfigurer = holdbackConfigurer;
	}
	public Timestamp getHoldbackReleaseDate() {
		return holdbackReleaseDate;
	}
	public void setHoldbackReleaseDate(Timestamp holdbackReleaseDate) {
		this.holdbackReleaseDate = holdbackReleaseDate;
	}
	public String getHoldbackReleaser() {
		return holdbackReleaser;
	}
	public void setHoldbackReleaser(String holdbackReleaser) {
		this.holdbackReleaser = holdbackReleaser;
	}
	public String getClaimDeliveryFeeProductOrderIds() {
		return claimDeliveryFeeProductOrderIds;
	}
	public void setClaimDeliveryFeeProductOrderIds(String claimDeliveryFeeProductOrderIds) {
		this.claimDeliveryFeeProductOrderIds = claimDeliveryFeeProductOrderIds;
	}
	public double getClaimDeliveryFeeDiscountAmount() {
		return claimDeliveryFeeDiscountAmount;
	}
	public void setClaimDeliveryFeeDiscountAmount(double claimDeliveryFeeDiscountAmount) {
		this.claimDeliveryFeeDiscountAmount = claimDeliveryFeeDiscountAmount;
	}
	public double getRemoteAreaCostChargeAmount() {
		return remoteAreaCostChargeAmount;
	}
	public void setRemoteAreaCostChargeAmount(double remoteAreaCostChargeAmount) {
		this.remoteAreaCostChargeAmount = remoteAreaCostChargeAmount;
	}
	@Override
	public String toString() {
		return "ReturnOrderInfo [claimDeliveryFeeDemandAmount=" + claimDeliveryFeeDemandAmount
				+ ", claimDeliveryFeePayMeans=" + claimDeliveryFeePayMeans + ", claimDeliveryFeePayMethod="
				+ claimDeliveryFeePayMethod + ", claimRequestDate=" + claimRequestDate + ", claimStatus=" + claimStatus
				+ ", collectAddress=" + collectAddress + ", collectCompletedDate=" + collectCompletedDate
				+ ", collectDeliveryCompany=" + collectDeliveryCompany + ", collectDeliveryMethod="
				+ collectDeliveryMethod + ", collectStatus=" + collectStatus + ", collectTrackingNumber="
				+ collectTrackingNumber + ", etcFeeDemandAmount=" + etcFeeDemandAmount + ", etcFeePayMeans="
				+ etcFeePayMeans + ", etcFeePayMethod=" + etcFeePayMethod + ", holdbackDetailedReason="
				+ holdbackDetailedReason + ", holdbackReason=" + holdbackReason + ", holdbackStatus=" + holdbackStatus
				+ ", refundExpectedDate=" + refundExpectedDate + ", refundStandbyReason=" + refundStandbyReason
				+ ", refundStandbyStatus=" + refundStandbyStatus + ", requestChannel=" + requestChannel
				+ ", returnDetailedReason=" + returnDetailedReason + ", returnReason=" + returnReason
				+ ", returnReceiveAddress=" + returnReceiveAddress + ", returnCompletedDate=" + returnCompletedDate
				+ ", holdbackConfigDate=" + holdbackConfigDate + ", holdbackConfigurer=" + holdbackConfigurer
				+ ", holdbackReleaseDate=" + holdbackReleaseDate + ", holdbackReleaser=" + holdbackReleaser
				+ ", claimDeliveryFeeProductOrderIds=" + claimDeliveryFeeProductOrderIds
				+ ", claimDeliveryFeeDiscountAmount=" + claimDeliveryFeeDiscountAmount + ", remoteAreaCostChargeAmount="
				+ remoteAreaCostChargeAmount + "]";
	}
	
}
