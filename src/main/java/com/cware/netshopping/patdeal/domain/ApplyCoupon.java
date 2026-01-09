package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyCoupon {
	
	private List<Object> productCoupons; //무슨 정보 주는지 몰라서...
	private Long cartCouponIssueNo;
	private boolean hasCouponIssueNo;
	private String cartCouponLimitPayType;
	private String promotionCode;
	private CartCouponUseKey cartCouponUseKey;
	
	public List<Object> getProductCoupons() {
		return productCoupons;
	}
	public void setProductCoupons(List<Object> productCoupons) {
		this.productCoupons = productCoupons;
	}
	public Long getCartCouponIssueNo() {
		return cartCouponIssueNo;
	}
	public void setCartCouponIssueNo(Long cartCouponIssueNo) {
		this.cartCouponIssueNo = cartCouponIssueNo;
	}
	public boolean isHasCouponIssueNo() {
		return hasCouponIssueNo;
	}
	public void setHasCouponIssueNo(boolean hasCouponIssueNo) {
		this.hasCouponIssueNo = hasCouponIssueNo;
	}
	public String getCartCouponLimitPayType() {
		return cartCouponLimitPayType;
	}
	public void setCartCouponLimitPayType(String cartCouponLimitPayType) {
		this.cartCouponLimitPayType = cartCouponLimitPayType;
	}
	public String getPromotionCode() {
		return promotionCode;
	}
	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}
	public CartCouponUseKey getCartCouponUseKey() {
		return cartCouponUseKey;
	}
	public void setCartCouponUseKey(CartCouponUseKey cartCouponUseKey) {
		this.cartCouponUseKey = cartCouponUseKey;
	}
	
	@Override
	public String toString() {
		return "ApplyCoupon [productCoupons=" + productCoupons + ", cartCouponIssueNo=" + cartCouponIssueNo
				+ ", hasCouponIssueNo=" + hasCouponIssueNo + ", cartCouponLimitPayType=" + cartCouponLimitPayType
				+ ", promotionCode=" + promotionCode + ", cartCouponUseKey=" + cartCouponUseKey + "]";
	}
	
}
