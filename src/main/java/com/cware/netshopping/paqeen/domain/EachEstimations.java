package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class EachEstimations {
     
     private String orderItemId;
     private long quantity;
     private long totalProductPrice;
     private long cartCouponDiscountAmount;
     private long productCouponDiscountAmount;
     private long finalPurchaseAmount;
     private long totalDiscountAmount;
     private long usePoint;
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public long getTotalProductPrice() {
		return totalProductPrice;
	}
	public void setTotalProductPrice(long totalProductPrice) {
		this.totalProductPrice = totalProductPrice;
	}
	public long getCartCouponDiscountAmount() {
		return cartCouponDiscountAmount;
	}
	public void setCartCouponDiscountAmount(long cartCouponDiscountAmount) {
		this.cartCouponDiscountAmount = cartCouponDiscountAmount;
	}
	public long getProductCouponDiscountAmount() {
		return productCouponDiscountAmount;
	}
	public void setProductCouponDiscountAmount(long productCouponDiscountAmount) {
		this.productCouponDiscountAmount = productCouponDiscountAmount;
	}
	public long getFinalPurchaseAmount() {
		return finalPurchaseAmount;
	}
	public void setFinalPurchaseAmount(long finalPurchaseAmount) {
		this.finalPurchaseAmount = finalPurchaseAmount;
	}
	public long getTotalDiscountAmount() {
		return totalDiscountAmount;
	}
	public void setTotalDiscountAmount(long totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}
	public long getUsePoint() {
		return usePoint;
	}
	public void setUsePoint(long usePoint) {
		this.usePoint = usePoint;
	}
     
}
