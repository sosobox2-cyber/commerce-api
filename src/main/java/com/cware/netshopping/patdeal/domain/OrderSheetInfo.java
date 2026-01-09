package com.cware.netshopping.patdeal.domain;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderSheetInfo {
	
	private String orderSheetId ;

	private List<PayProducts> payProducts;
	
	private ApplyCoupon applyCoupon;

	public String getOrderSheetId() {
		return orderSheetId;
	}

	public void setOrderSheetId(String orderSheetId) {
		this.orderSheetId = orderSheetId;
	}

	public List<PayProducts> getPayProducts() {
		return payProducts;
	}

	public void setPayProducts(List<PayProducts> payProducts) {
		this.payProducts = payProducts;
	}

	public ApplyCoupon getApplyCoupon() {
		return applyCoupon;
	}

	public void setApplyCoupon(ApplyCoupon applyCoupon) {
		this.applyCoupon = applyCoupon;
	}

	@Override
	public String toString() {
		return "OrderSheetInfo [orderSheetId=" + orderSheetId + ", payProducts=" + payProducts + ", applyCoupon="
				+ applyCoupon + "]";
	}

	
}
