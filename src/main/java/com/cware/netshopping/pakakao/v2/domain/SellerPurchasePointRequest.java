package com.cware.netshopping.pakakao.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SellerPurchasePointRequest {

	// 구매시 포인트 적립 설정 여부
	boolean usePoint;

	// 구매시 포인트 적립 정책 아이디
	Long pointId;

	// 구매시 포인트 적립 단위(원/%)
	// RATE 적립률(%)
	// PRICE 적립액(원)
	String unit;

	// 구매시 포인트 적립액(률)
	Integer value;

	// 구매시 포인트 적립기간
	Period period;

	public boolean isUsePoint() {
		return usePoint;
	}

	public void setUsePoint(boolean usePoint) {
		this.usePoint = usePoint;
	}

	public Long getPointId() {
		return pointId;
	}

	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	@Override
	public String toString() {
		return "SellerPurchasePointRequest [usePoint=" + usePoint + ", pointId=" + pointId + ", unit=" + unit
				+ ", value=" + value + ", period=" + period + "]";
	}

}
