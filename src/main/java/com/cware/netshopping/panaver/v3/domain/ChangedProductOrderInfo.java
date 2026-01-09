package com.cware.netshopping.panaver.v3.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class ChangedProductOrderInfo {
		
	// 주문 ID
	private String orderId;
	// 상품 주문 ID
	private String productOrderId;
	// 최종 변경 구분
	private String lastChangedType;
	// 결제 일시
	private Timestamp paymentDate;
	// 최종 변경 일시
	private Timestamp lastChangedDate;
	// 상품 주문 상태
	private String productOrderStatus;
	// 클레임 타입
	private String claimType;
	// 클레임 상태
	private String claimStatus;
	// 배송지 정보 변경 여부
	private String receiverAddressChanged;
	// 선물 수락 상태 구분
	private String giftReceivingStatus;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProductOrderId() {
		return productOrderId;
	}

	public void setProductOrderId(String productOrderId) {
		this.productOrderId = productOrderId;
	}

	public String getLastChangedType() {
		return lastChangedType;
	}

	public void setLastChangedType(String lastChangedType) {
		this.lastChangedType = lastChangedType;
	}

	public Timestamp getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Timestamp paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Timestamp getLastChangedDate() {
		return lastChangedDate;
	}

	public void setLastChangedDate(Timestamp lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}

	public String getProductOrderStatus() {
		return productOrderStatus;
	}

	public void setProductOrderStatus(String productOrderStatus) {
		this.productOrderStatus = productOrderStatus;
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

	public String getReceiverAddressChanged() {
		return receiverAddressChanged;
	}

	public void setReceiverAddressChanged(String receiverAddressChanged) {
		this.receiverAddressChanged = receiverAddressChanged;
	}

	public String getGiftReceivingStatus() {
		return giftReceivingStatus;
	}

	public void setGiftReceivingStatus(String giftReceivingStatus) {
		this.giftReceivingStatus = giftReceivingStatus;
	}

	@Override
	public String toString() {
		return "ChangedProductOrderInfo [orderId=" + orderId + ", productOrderId=" + productOrderId
				+ ", lastChangedType=" + lastChangedType + ", paymentDate=" + paymentDate + ", lastChangedDate="
				+ lastChangedDate + ", productOrderStatus=" + productOrderStatus + ", claimType=" + claimType
				+ ", claimStatus=" + claimStatus + ", receiverAddressChanged=" + receiverAddressChanged
				+ ", giftReceivingStatus=" + giftReceivingStatus + "]";
	}
}
