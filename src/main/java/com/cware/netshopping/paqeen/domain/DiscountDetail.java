package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class DiscountDetail {
	private long sellerDirectCouponDiscountAmount;
    private long sellerIssuedCouponDiscountAmount;
    private long queenitDirectCouponDiscountAmount;
    private long queenitIssuedCouponDiscountAmount;
    private long queenitDiscountAmountByCoupon;
    private long queenitPointDiscountAmount;
	public long getSellerDirectCouponDiscountAmount() {
		return sellerDirectCouponDiscountAmount;
	}
	public void setSellerDirectCouponDiscountAmount(long sellerDirectCouponDiscountAmount) {
		this.sellerDirectCouponDiscountAmount = sellerDirectCouponDiscountAmount;
	}
	public long getSellerIssuedCouponDiscountAmount() {
		return sellerIssuedCouponDiscountAmount;
	}
	public void setSellerIssuedCouponDiscountAmount(long sellerIssuedCouponDiscountAmount) {
		this.sellerIssuedCouponDiscountAmount = sellerIssuedCouponDiscountAmount;
	}
	public long getQueenitDirectCouponDiscountAmount() {
		return queenitDirectCouponDiscountAmount;
	}
	public void setQueenitDirectCouponDiscountAmount(long queenitDirectCouponDiscountAmount) {
		this.queenitDirectCouponDiscountAmount = queenitDirectCouponDiscountAmount;
	}
	public long getQueenitIssuedCouponDiscountAmount() {
		return queenitIssuedCouponDiscountAmount;
	}
	public void setQueenitIssuedCouponDiscountAmount(long queenitIssuedCouponDiscountAmount) {
		this.queenitIssuedCouponDiscountAmount = queenitIssuedCouponDiscountAmount;
	}
	public long getQueenitDiscountAmountByCoupon() {
		return queenitDiscountAmountByCoupon;
	}
	public void setQueenitDiscountAmountByCoupon(long queenitDiscountAmountByCoupon) {
		this.queenitDiscountAmountByCoupon = queenitDiscountAmountByCoupon;
	}
	public long getQueenitPointDiscountAmount() {
		return queenitPointDiscountAmount;
	}
	public void setQueenitPointDiscountAmount(long queenitPointDiscountAmount) {
		this.queenitPointDiscountAmount = queenitPointDiscountAmount;
	}
    
    
}
