package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ImmediateDiscountPolicy {
		
	// PC 할인 혜택
	private DiscountMethod discountMethod;
	// 모바일 할인 혜택
	private MobileDiscountMethod mobileDiscountMethod;

	
	public DiscountMethod getDiscountMethod() {
		return discountMethod;
	}

	public void setDiscountMethod(DiscountMethod discountMethod) {
		this.discountMethod = discountMethod;
	}

	public MobileDiscountMethod getMobileDiscountMethod() {
		return mobileDiscountMethod;
	}

	public void setMobileDiscountMethod(MobileDiscountMethod mobileDiscountMethod) {
		this.mobileDiscountMethod = mobileDiscountMethod;
	}

	@Override
	public String toString() {
		return "ImmediateDiscountPolicy [discountMethod=" + discountMethod +"mobileDiscountMethod=" + mobileDiscountMethod +"]";
	}

}
