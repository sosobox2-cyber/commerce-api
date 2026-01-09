package com.cware.netshopping.panaver.v3.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderInfo {
	
	//충전금 최종 결제 금액
	private double chargeAmountPaymentAmount ;
	//네이버페이 적립금 최종 결제 금액
	private double checkoutAccumulationPaymentAmount ;
	//일반 결제 수단 최종 결제 금액
	private double generalPaymentAmount ;
	//네이버페이 포인트 최종 결제 금액
	private double naverMileagePaymentAmount ;
	//주문 일시
	private Timestamp orderDate;
	//주문 할인액
	private double orderDiscountAmount ;
	//주문 번호
	private String orderId;
	//주문자 ID
	private String ordererId;
	//주문자 이름
	private String ordererName;
	//주문자 연락처
	private String ordererTel;
	//결제 일시(최종 결제).
	private Timestamp paymentDate;
	//결제 기한
	private Timestamp paymentDueDate;
	//결제 수단
	private String paymentMeans;
	//배송 메모 개별 입력 여부
	private String isDeliveryMemoParticularInput;
	//결제 위치 구분(PC/MOBILE)
	private String payLocationType;
	//주문자 번호
	private String ordererNo;
	//후불 결제 최종 결제 금액
	private double payLaterPaymentAmount;
	
	public double getChargeAmountPaymentAmount() {
		return chargeAmountPaymentAmount;
	}
	public void setChargeAmountPaymentAmount(double chargeAmountPaymentAmount) {
		this.chargeAmountPaymentAmount = chargeAmountPaymentAmount;
	}
	public double getCheckoutAccumulationPaymentAmount() {
		return checkoutAccumulationPaymentAmount;
	}
	public void setCheckoutAccumulationPaymentAmount(double checkoutAccumulationPaymentAmount) {
		this.checkoutAccumulationPaymentAmount = checkoutAccumulationPaymentAmount;
	}
	public double getGeneralPaymentAmount() {
		return generalPaymentAmount;
	}
	public void setGeneralPaymentAmount(double generalPaymentAmount) {
		this.generalPaymentAmount = generalPaymentAmount;
	}
	public double getNaverMileagePaymentAmount() {
		return naverMileagePaymentAmount;
	}
	public void setNaverMileagePaymentAmount(double naverMileagePaymentAmount) {
		this.naverMileagePaymentAmount = naverMileagePaymentAmount;
	}
	public Timestamp getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	public double getOrderDiscountAmount() {
		return orderDiscountAmount;
	}
	public void setOrderDiscountAmount(double orderDiscountAmount) {
		this.orderDiscountAmount = orderDiscountAmount;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrdererId() {
		return ordererId;
	}
	public void setOrdererId(String ordererId) {
		this.ordererId = ordererId;
	}
	public String getOrdererName() {
		return ordererName;
	}
	public void setOrdererName(String ordererName) {
		this.ordererName = ordererName;
	}
	public String getOrdererTel() {
		return ordererTel;
	}
	public void setOrdererTel(String ordererTel) {
		this.ordererTel = ordererTel;
	}
	public Timestamp getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Timestamp paymentDate) {
		this.paymentDate = paymentDate;
	}
	public Timestamp getPaymentDueDate() {
		return paymentDueDate;
	}
	public void setPaymentDueDate(Timestamp paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}
	public String getPaymentMeans() {
		return paymentMeans;
	}
	public void setPaymentMeans(String paymentMeans) {
		this.paymentMeans = paymentMeans;
	}
	public String getIsDeliveryMemoParticularInput() {
		return isDeliveryMemoParticularInput;
	}
	public void setIsDeliveryMemoParticularInput(String isDeliveryMemoParticularInput) {
		this.isDeliveryMemoParticularInput = isDeliveryMemoParticularInput;
	}
	public String getPayLocationType() {
		return payLocationType;
	}
	public void setPayLocationType(String payLocationType) {
		this.payLocationType = payLocationType;
	}
	public String getOrdererNo() {
		return ordererNo;
	}
	public void setOrdererNo(String ordererNo) {
		this.ordererNo = ordererNo;
	}
	public double getPayLaterPaymentAmount() {
		return payLaterPaymentAmount;
	}
	public void setPayLaterPaymentAmount(double payLaterPaymentAmount) {
		this.payLaterPaymentAmount = payLaterPaymentAmount;
	}
	@Override
	public String toString() {
		return "OrderInfo [chargeAmountPaymentAmount=" + chargeAmountPaymentAmount
				+ ", checkoutAccumulationPaymentAmount=" + checkoutAccumulationPaymentAmount + ", generalPaymentAmount="
				+ generalPaymentAmount + ", naverMileagePaymentAmount=" + naverMileagePaymentAmount + ", orderDate="
				+ orderDate + ", orderDiscountAmount=" + orderDiscountAmount + ", orderId=" + orderId + ", ordererId="
				+ ordererId + ", ordererName=" + ordererName + ", ordererTel=" + ordererTel + ", paymentDate="
				+ paymentDate + ", paymentDueDate=" + paymentDueDate + ", paymentMeans=" + paymentMeans
				+ ", isDeliveryMemoParticularInput=" + isDeliveryMemoParticularInput + ", payLocationType="
				+ payLocationType + ", ordererNo=" + ordererNo + ", payLaterPaymentAmount=" + payLaterPaymentAmount
				+ "]";
	}
	

}
