package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class FreeInterestPolicy {
		
	// 무이자 할부 개월 수
	private int value;
	// 무이자 할부 개월 수
	private String startDate;
	// 무이자 할부 개월 수
	private String endDate;
	
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
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
		return "FreeInterestPolicy [value=" + value +"startDate=" + startDate + "endDate=" + endDate +"]";
	}

}
