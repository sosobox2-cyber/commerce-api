package com.cware.netshopping.pakakao.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Discount {
	// 할인종류
	// RATE 할인율
	// PRICE 할인가격
	String type;

	// 할인 값
	Integer value;

	// 할인 기간
	Period discountPeriod;

	// 할인적용여부
	boolean useDiscount;

	// 할인기간적용여부(판매자 즉시할인 설정시에는 필수)
	boolean useDiscountPeriod;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Period getDiscountPeriod() {
		return discountPeriod;
	}

	public void setDiscountPeriod(Period discountPeriod) {
		this.discountPeriod = discountPeriod;
	}

	public boolean isUseDiscount() {
		return useDiscount;
	}

	public void setUseDiscount(boolean useDiscount) {
		this.useDiscount = useDiscount;
	}

	public boolean isUseDiscountPeriod() {
		return useDiscountPeriod;
	}

	public void setUseDiscountPeriod(boolean useDiscountPeriod) {
		this.useDiscountPeriod = useDiscountPeriod;
	}

	@Override
	public String toString() {
		return "Discount [type=" + type + ", value=" + value + ", discountPeriod=" + discountPeriod + ", useDiscount="
				+ useDiscount + ", useDiscountPeriod=" + useDiscountPeriod + "]";
	}

}
