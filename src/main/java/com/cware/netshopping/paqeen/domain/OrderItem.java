package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItem {
	
    private String id;
    private Product product;
    private ProductItem productItem; 
    private Long singlePrice;
    private int quantity;
    private Long singleDirectDiscountAmount;
    private Long totalPrice;
    private Long totalProductPrice;
    private Long finalPurchaseAmount;
    private int usePoint;
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
    private PrimaryCancelTicket primaryCancelTicket;
    private PrimaryReturnTicket primaryReturnTicket; 
    private PrimaryExchangeTicket primaryExchangeTicket;
    private Long receiptConfirmedAtMillis;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
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
	public int getUsePoint() {
		return usePoint;
	}
	public void setUsePoint(int usePoint) {
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
