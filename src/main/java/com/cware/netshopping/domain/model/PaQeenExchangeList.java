package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaQeenExchangeList extends AbstractModel {

	private static final long serialVersionUID = 1L;

	private String ticketId;
	private String orderId;
	private String groupId;
	private String orderItemId;
	private String ordererId;
	private Timestamp requestedAtMillis;
	private String paOrderGb;
	private String reasonType;
	private String reason;
	private String paymentMethod;
	private String state;
	private Long finalPurchaseAmount;
	private String customerNegligence;
	private Long quantity;
	private String exchangeOptionId;
	private String phoneNumber;
	private String recipientName;
	private String zipCode;
	private String address;
	private String detailedAddress;
	private String deliverFeeNormal;
	private String deliverFeeJeju;
	private String deliverFeeBackCountry;
	private String salesType;
	private String prodcutName;
	private String productId;
	private String mallProductCode;
	private String optionId;
	private Long optionQuantity;
	private String freeExchangeTarget;
	private String freeReturnTarget;

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getOrdererId() {
		return ordererId;
	}

	public void setOrdererId(String ordererId) {
		this.ordererId = ordererId;
	}

	public Timestamp getRequestedAtMillis() {
		return requestedAtMillis;
	}

	public void setRequestedAtMillis(Timestamp requestedAtMillis) {
		this.requestedAtMillis = requestedAtMillis;
	}

	public String getPaOrderGb() {
		return paOrderGb;
	}

	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}

	public String getReasonType() {
		return reasonType;
	}

	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getFinalPurchaseAmount() {
		return finalPurchaseAmount;
	}

	public void setFinalPurchaseAmount(Long finalPurchaseAmount) {
		this.finalPurchaseAmount = finalPurchaseAmount;
	}

	public String getCustomerNegligence() {
		return customerNegligence;
	}

	public void setCustomerNegligence(String customerNegligence) {
		this.customerNegligence = customerNegligence;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getExchangeOptionId() {
		return exchangeOptionId;
	}

	public void setExchangeOptionId(String exchangeOptionId) {
		this.exchangeOptionId = exchangeOptionId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetailedAddress() {
		return detailedAddress;
	}

	public void setDetailedAddress(String detailedAddress) {
		this.detailedAddress = detailedAddress;
	}

	public String getDeliverFeeNormal() {
		return deliverFeeNormal;
	}

	public void setDeliverFeeNormal(String deliverFeeNormal) {
		this.deliverFeeNormal = deliverFeeNormal;
	}

	public String getDeliverFeeJeju() {
		return deliverFeeJeju;
	}

	public void setDeliverFeeJeju(String deliverFeeJeju) {
		this.deliverFeeJeju = deliverFeeJeju;
	}

	public String getDeliverFeeBackCountry() {
		return deliverFeeBackCountry;
	}

	public void setDeliverFeeBackCountry(String deliverFeeBackCountry) {
		this.deliverFeeBackCountry = deliverFeeBackCountry;
	}

	public String getSalesType() {
		return salesType;
	}

	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}

	public String getProdcutName() {
		return prodcutName;
	}

	public void setProdcutName(String prodcutName) {
		this.prodcutName = prodcutName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getMallProductCode() {
		return mallProductCode;
	}

	public void setMallProductCode(String mallProductCode) {
		this.mallProductCode = mallProductCode;
	}

	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public Long getOptionQuantity() {
		return optionQuantity;
	}

	public void setOptionQuantity(Long optionQuantity) {
		this.optionQuantity = optionQuantity;
	}

	public String getFreeExchangeTarget() {
		return freeExchangeTarget;
	}

	public void setFreeExchangeTarget(String freeExchangeTarget) {
		this.freeExchangeTarget = freeExchangeTarget;
	}

	public String getFreeReturnTarget() {
		return freeReturnTarget;
	}

	public void setFreeReturnTarget(String freeReturnTarget) {
		this.freeReturnTarget = freeReturnTarget;
	}

}
