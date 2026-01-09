package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaQeenReturnList extends AbstractModel {

	private static final long serialVersionUID = 1L;

	private String ticketId;
	private String isCustomerNegligence;
	private String recipientName;
	private String phoneNumber;
	private String zipCode;
	private String address;
	private String detailedAddress;
	private String state;
	private String returnState;
	private String reason;
	private String id;
	private String productId;
	private String mallProductCode;
	private String name;
	private long finalPrice;
	private long originalPrice;
	private long discountPercentage;
	private long defaultCost;
	private long jejuIsland;
	private long backCountry;
	
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	public String getIsCustomerNegligence() {
		return isCustomerNegligence;
	}
	public void setIsCustomerNegligence(String isCustomerNegligence) {
		this.isCustomerNegligence = isCustomerNegligence;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getReturnState() {
		return returnState;
	}
	public void setReturnState(String returnState) {
		this.returnState = returnState;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(long finalPrice) {
		this.finalPrice = finalPrice;
	}
	public long getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(long originalPrice) {
		this.originalPrice = originalPrice;
	}
	public long getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(long discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	public long getDefaultCost() {
		return defaultCost;
	}
	public void setDefaultCost(long defaultCost) {
		this.defaultCost = defaultCost;
	}
	public long getJejuIsland() {
		return jejuIsland;
	}
	public void setJejuIsland(long jejuIsland) {
		this.jejuIsland = jejuIsland;
	}
	public long getBackCountry() {
		return backCountry;
	}
	public void setBackCountry(long backCountry) {
		this.backCountry = backCountry;
	}
	
}
