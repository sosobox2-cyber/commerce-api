package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Settlement {
	
	private String id;
	private String transactionType;
	private String orderId;
	private String orderLineGroupId;
	private Share share;
	private Payment payment;
	private sellerCompensation sellerCompensation;
	private String brandCode;
	private Base base;
	private String includedBookId;
	private String baseDate;
	private String orderLineId;
	private String salesType;
	@JsonProperty("product")
	private SettleProduct product;
	private Profit profit;
	private long eventAtMillis;
	private long deliveryCompletedAtMillis;
	private DiscountDetail discountDetail;
	private String memo;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderLineGroupId() {
		return orderLineGroupId;
	}
	public void setOrderLineGroupId(String orderLineGroupId) {
		this.orderLineGroupId = orderLineGroupId;
	}
	public Share getShare() {
		return share;
	}
	public void setShare(Share share) {
		this.share = share;
	}
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public sellerCompensation getSellerCompensation() {
		return sellerCompensation;
	}
	public void setSellerCompensation(sellerCompensation sellerCompensation) {
		this.sellerCompensation = sellerCompensation;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public Base getBase() {
		return base;
	}
	public void setBase(Base base) {
		this.base = base;
	}
	public String getIncludedBookId() {
		return includedBookId;
	}
	public void setIncludedBookId(String includedBookId) {
		this.includedBookId = includedBookId;
	}
	public String getBaseDate() {
		return baseDate;
	}
	public void setBaseDate(String baseDate) {
		this.baseDate = baseDate;
	}
	public String getOrderLineId() {
		return orderLineId;
	}
	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}
	public String getSalesType() {
		return salesType;
	}
	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}
	public SettleProduct getProduct() {
		return product;
	}
	public void setProduct(SettleProduct product) {
		this.product = product;
	}
	public Profit getProfit() {
		return profit;
	}
	public void setProfit(Profit profit) {
		this.profit = profit;
	}
	public long getEventAtMillis() {
		return eventAtMillis;
	}
	public void setEventAtMillis(long eventAtMillis) {
		this.eventAtMillis = eventAtMillis;
	}
	public long getDeliveryCompletedAtMillis() {
		return deliveryCompletedAtMillis;
	}
	public void setDeliveryCompletedAtMillis(long deliveryCompletedAtMillis) {
		this.deliveryCompletedAtMillis = deliveryCompletedAtMillis;
	}
	public DiscountDetail getDiscountDetail() {
		return discountDetail;
	}
	public void setDiscountDetail(DiscountDetail discountDetail) {
		this.discountDetail = discountDetail;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
}
