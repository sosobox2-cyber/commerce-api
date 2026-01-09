package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SellerDiscountSite {

	// 판매자 할인 사용여부
	// 0:사용안함
	// 1:정액
	// 2:정률
	public int type;
	// 할인액(율)
	public Double priceOrRate1;
	// 할인액(율)_SD2
	public Double priceOrRate2;
	// 할인 시작일자
	// 입력형식 YYYY-MM-DD
	String startDate;
	// 할인 종료일자
	// 입력형식 YYYY-MM-DD
	String endDate;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Double getPriceOrRate1() {
		return priceOrRate1;
	}

	public void setPriceOrRate1(Double priceOrRate1) {
		this.priceOrRate1 = priceOrRate1;
	}

	public Double getPriceOrRate2() {
		return priceOrRate2;
	}

	public void setPriceOrRate2(Double priceOrRate2) {
		this.priceOrRate2 = priceOrRate2;
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
		return "SellerDiscountSite [type=" + type + ", priceOrRate1=" + priceOrRate1 + ", priceOrRate2=" + priceOrRate2
				+ ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
