package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MultiPurchaseDiscountPolicy {
		
	// 판매자 즉시 할인 정책
	private DiscountMethod discountMethod;
	// 주문 금액(수량) 값
	private double orderValue;
	// 주문 금액(수량) 단위
	private String orderValueUnitType;

	
	public DiscountMethod getDiscountMethod() {
		return discountMethod;
	}

	public void setDiscountMethod(DiscountMethod discountMethod) {
		this.discountMethod = discountMethod;
	}

	public double getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(double orderValue) {
		this.orderValue = orderValue;
	}

	public String getOrderValueUnitType() {
		return orderValueUnitType;
	}

	public void setOrderValueUnitType(String orderValueUnitType) {
		this.orderValueUnitType = orderValueUnitType;
	}

	@Override
	public String toString() {
		return "MultiPurchaseDiscountPolicy [discountMethod=" + discountMethod +"orderValue=" + orderValue + "orderValueUnitType=" + orderValueUnitType +"]";
	}

}
