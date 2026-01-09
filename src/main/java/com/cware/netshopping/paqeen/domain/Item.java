package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Item {
	private String id;

	@JsonProperty("product")
	private ProductGoods productGoods;

	private ProductItem productItem;
	private Long singlePrice;
	private int quantity;
	private Long singleDirectDiscountAmount;
	private Long totalPrice;
	private Long totalProductPrice;
	private Long finalPurchaseAmount;
	private Long usePoint;
	private Long totalCartCouponDiscountAmount;
	private Long totalProductCouponDiscountAmount;
	private Long totalDiscountAmount;
	private Long totalDirectDiscountAmount;
	private Long finalSinglePrice;
	private String state;
	private String purchaseState;
	private Delivery delivery;
	private Brand brand;
	private boolean receiptConfirmation;
	private boolean freeReturnTarget;
	private boolean freeExchangeTarget;
	private boolean isFreeReturnTargetUser;
	private boolean isFreeReturnTargetProduct;
	private DiscountDutyAmount discountDutyAmount;
	private PrimaryCancelTicket primaryCancelTicket;
	private PrimaryReturnTicket primaryReturnTicket;
	private PrimaryExchangeTicket primaryExchangeTicket;
	private Long receiptConfirmedAtMillis;
	private int delayDays;
	private String estimateShipmentAt;
	private BundledProductItemInfos bundledProductItemInfos;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProductGoods getProductGoods() {
		return productGoods;
	}

	public void setProductGoods(ProductGoods productGoods) {
		this.productGoods = productGoods;
	}

	public ProductItem getProductItem() {
		return productItem;
	}

	public void setProductItem(ProductItem productItem) {
		this.productItem = productItem;
	}

	public Long getSinglePrice() {
		return singlePrice;
	}

	public void setSinglePrice(Long singlePrice) {
		this.singlePrice = singlePrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Long getSingleDirectDiscountAmount() {
		return singleDirectDiscountAmount;
	}

	public void setSingleDirectDiscountAmount(Long singleDirectDiscountAmount) {
		this.singleDirectDiscountAmount = singleDirectDiscountAmount;
	}

	public Long getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Long totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Long getTotalProductPrice() {
		return totalProductPrice;
	}

	public void setTotalProductPrice(Long totalProductPrice) {
		this.totalProductPrice = totalProductPrice;
	}

	public Long getFinalPurchaseAmount() {
		return finalPurchaseAmount;
	}

	public void setFinalPurchaseAmount(Long finalPurchaseAmount) {
		this.finalPurchaseAmount = finalPurchaseAmount;
	}

	public Long getUsePoint() {
		return usePoint;
	}

	public void setUsePoint(Long usePoint) {
		this.usePoint = usePoint;
	}

	public Long getTotalCartCouponDiscountAmount() {
		return totalCartCouponDiscountAmount;
	}

	public void setTotalCartCouponDiscountAmount(Long totalCartCouponDiscountAmount) {
		this.totalCartCouponDiscountAmount = totalCartCouponDiscountAmount;
	}

	public Long getTotalProductCouponDiscountAmount() {
		return totalProductCouponDiscountAmount;
	}

	public void setTotalProductCouponDiscountAmount(Long totalProductCouponDiscountAmount) {
		this.totalProductCouponDiscountAmount = totalProductCouponDiscountAmount;
	}

	public Long getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(Long totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public Long getTotalDirectDiscountAmount() {
		return totalDirectDiscountAmount;
	}

	public void setTotalDirectDiscountAmount(Long totalDirectDiscountAmount) {
		this.totalDirectDiscountAmount = totalDirectDiscountAmount;
	}

	public Long getFinalSinglePrice() {
		return finalSinglePrice;
	}

	public void setFinalSinglePrice(Long finalSinglePrice) {
		this.finalSinglePrice = finalSinglePrice;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPurchaseState() {
		return purchaseState;
	}

	public void setPurchaseState(String purchaseState) {
		this.purchaseState = purchaseState;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public boolean isReceiptConfirmation() {
		return receiptConfirmation;
	}

	public void setReceiptConfirmation(boolean receiptConfirmation) {
		this.receiptConfirmation = receiptConfirmation;
	}

	public boolean isFreeReturnTarget() {
		return freeReturnTarget;
	}

	public void setFreeReturnTarget(boolean freeReturnTarget) {
		this.freeReturnTarget = freeReturnTarget;
	}

	public boolean isFreeExchangeTarget() {
		return freeExchangeTarget;
	}

	public void setFreeExchangeTarget(boolean freeExchangeTarget) {
		this.freeExchangeTarget = freeExchangeTarget;
	}

	public DiscountDutyAmount getDiscountDutyAmount() {
		return discountDutyAmount;
	}

	public void setDiscountDutyAmount(DiscountDutyAmount discountDutyAmount) {
		this.discountDutyAmount = discountDutyAmount;
	}

	public PrimaryCancelTicket getPrimaryCancelTicket() {
		return primaryCancelTicket;
	}

	public void setPrimaryCancelTicket(PrimaryCancelTicket primaryCancelTicket) {
		this.primaryCancelTicket = primaryCancelTicket;
	}

	public PrimaryReturnTicket getPrimaryReturnTicket() {
		return primaryReturnTicket;
	}

	public void setPrimaryReturnTicket(PrimaryReturnTicket primaryReturnTicket) {
		this.primaryReturnTicket = primaryReturnTicket;
	}

	public PrimaryExchangeTicket getPrimaryExchangeTicket() {
		return primaryExchangeTicket;
	}
	
	public void setPrimaryExchangeTicket(PrimaryExchangeTicket primaryExchangeTicket) {
		this.primaryExchangeTicket = primaryExchangeTicket;
	}

	public Long getReceiptConfirmedAtMillis() {
		return receiptConfirmedAtMillis;
	}

	public void setReceiptConfirmedAtMillis(Long receiptConfirmedAtMillis) {
		this.receiptConfirmedAtMillis = receiptConfirmedAtMillis;
	}

	public int getDelayDays() {
		return delayDays;
	}

	public void setDelayDays(int delayDays) {
		this.delayDays = delayDays;
	}

	public String getEstimateShipmentAt() {
		return estimateShipmentAt;
	}

	public void setEstimateShipmentAt(String estimateShipmentAt) {
		this.estimateShipmentAt = estimateShipmentAt;
	}

	public BundledProductItemInfos getBundledProductItemInfos() {
		return bundledProductItemInfos;
	}

	public void setBundledProductItemInfos(BundledProductItemInfos bundledProductItemInfos) {
		this.bundledProductItemInfos = bundledProductItemInfos;
	}

	public boolean isFreeReturnTargetUser() {
		return isFreeReturnTargetUser;
	}

	public void setFreeReturnTargetUser(boolean isFreeReturnTargetUser) {
		this.isFreeReturnTargetUser = isFreeReturnTargetUser;
	}

	public boolean isFreeReturnTargetProduct() {
		return isFreeReturnTargetProduct;
	}

	public void setFreeReturnTargetProduct(boolean isFreeReturnTargetProduct) {
		this.isFreeReturnTargetProduct = isFreeReturnTargetProduct;
	}
}
