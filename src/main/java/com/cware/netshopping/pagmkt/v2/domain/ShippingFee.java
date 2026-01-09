package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ShippingFee {

	private static final int MIN_COST = 1_000;
	private static final int MAX_COST = 1_000_000;

	// 조건부 기준금액
	// feeType이 3일 경우 필수
	// 금액입력
	// * 1000원 이상 10,000,000원 미만으로 설정 가능
	private Integer condition;

	public ShippingFee(Integer condition) {
		setCondition(condition);
	}

	public Integer getCondition() {
		return condition;
	}

	public void setCondition(Integer condition) {
		if (condition < MIN_COST) {
			this.condition = MIN_COST;
		} else if (condition > MAX_COST) {
			this.condition = MAX_COST;
		} else {
			this.condition = condition;
		}
	}

	@Override
	public String toString() {
		return "ShippingFee [condition=" + condition + "]";
	}

}
