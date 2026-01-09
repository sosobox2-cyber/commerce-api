package com.cware.netshopping.panaver.v3.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryOrderInfo {
	
	// 배송 완료 일시
	private Timestamp deliveredDate;
	// 택배사 코드
	private String deliveryCompany;
	//배송 방법 코드
	private String deliveryMethod;
	//배송 상세 상태
	private String deliveryStatus;
	//오류 송장 여부
	private Boolean isWrongTrackingNumber;
	//집화 일시
	private Timestamp pickupDate;
	//발송 일시
	private Timestamp sendDate;
	//송장 번호
	private String trackingNumber;
	//오류 송장 등록 일시
	private Timestamp wrongTrackingNumberRegisteredDate;
	//오류 사유
	private String wrongTrackingNumberType;
	
	public Timestamp getDeliveredDate() {
		return deliveredDate;
	}
	public void setDeliveredDate(Timestamp deliveredDate) {
		this.deliveredDate = deliveredDate;
	}
	public String getDeliveryCompany() {
		return deliveryCompany;
	}
	public void setDeliveryCompany(String deliveryCompany) {
		this.deliveryCompany = deliveryCompany;
	}
	public String getDeliveryMethod() {
		return deliveryMethod;
	}
	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public Boolean getIsWrongTrackingNumber() {
		return isWrongTrackingNumber;
	}
	public void setIsWrongTrackingNumber(Boolean isWrongTrackingNumber) {
		this.isWrongTrackingNumber = isWrongTrackingNumber;
	}
	public Timestamp getPickupDate() {
		return pickupDate;
	}
	public void setPickupDate(Timestamp pickupDate) {
		this.pickupDate = pickupDate;
	}
	public Timestamp getSendDate() {
		return sendDate;
	}
	public void setSendDate(Timestamp sendDate) {
		this.sendDate = sendDate;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public Timestamp getWrongTrackingNumberRegisteredDate() {
		return wrongTrackingNumberRegisteredDate;
	}
	public void setWrongTrackingNumberRegisteredDate(Timestamp wrongTrackingNumberRegisteredDate) {
		this.wrongTrackingNumberRegisteredDate = wrongTrackingNumberRegisteredDate;
	}
	public String getWrongTrackingNumberType() {
		return wrongTrackingNumberType;
	}
	public void setWrongTrackingNumberType(String wrongTrackingNumberType) {
		this.wrongTrackingNumberType = wrongTrackingNumberType;
	}
	@Override
	public String toString() {
		return "DeliveryOrderInfo [deliveredDate=" + deliveredDate + ", deliveryCompany=" + deliveryCompany
				+ ", deliveryMethod=" + deliveryMethod + ", deliveryStatus=" + deliveryStatus
				+ ", isWrongTrackingNumber=" + isWrongTrackingNumber + ", pickupDate=" + pickupDate + ", sendDate="
				+ sendDate + ", trackingNumber=" + trackingNumber + ", wrongTrackingNumberRegisteredDate="
				+ wrongTrackingNumberRegisteredDate + ", wrongTrackingNumberType=" + wrongTrackingNumberType + "]";
	}
	
}
