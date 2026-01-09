package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartCouponUseKey {
	
	private Long couponIssueNo;
	private String promotionCode;
	
	public Long getCouponIssueNo() {
		return couponIssueNo;
	}
	public void setCouponIssueNo(Long couponIssueNo) {
		this.couponIssueNo = couponIssueNo;
	}
	public String getPromotionCode() {
		return promotionCode;
	}
	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}
	
	@Override
	public String toString() {
		return "CartCouponUseKey [couponIssueNo=" + couponIssueNo + ", promotionCode=" + promotionCode + "]";
	}
	
	
}
