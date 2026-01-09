package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DiscountMethod {
		
	// 할인 값
	private long value;
	// 할인 단위
	private String unitType;
	// 할인 시작일
	private String startDate;
	// 할인 종료일
	private String endDate;

	
	public long getValue() {
		return value;
	}

	public void setValue(long value) {
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
		return "DiscountMethod [value=" + value +"unitType=" + unitType +"startDate=" + startDate +"endDate=" + endDate +"]";
	}

}
