package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PurchasePointPolicy {
		
	// 상품 구매 포인트 값
	private double value;
	// 상품 구매 포인트 단위
	private String unitType;
	// 적립 시작일
	private String startDate;
	// 적립 종료일
	private String endDate;
	
	
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "PurchasePointPolicy [value=" + value +"unitType=" + unitType + "startDate=" + startDate + "endDate=" + endDate +"]";
	}

}
