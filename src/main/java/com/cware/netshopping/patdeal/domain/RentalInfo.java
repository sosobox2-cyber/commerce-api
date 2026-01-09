package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RentalInfo {

	//월 렌탈 금액
	private long monthlyRentalAmount;
	//렌탈 기간
	private int rentalPeriod;
	//서비스 가능 최저 신용 등급
	private int creditRating;
	public long getMonthlyRentalAmount() {
		return monthlyRentalAmount;
	}
	public void setMonthlyRentalAmount(long monthlyRentalAmount) {
		this.monthlyRentalAmount = monthlyRentalAmount;
	}
	public int getRentalPeriod() {
		return rentalPeriod;
	}
	public void setRentalPeriod(int rentalPeriod) {
		this.rentalPeriod = rentalPeriod;
	}
	public int getCreditRating() {
		return creditRating;
	}
	public void setCreditRating(int creditRating) {
		this.creditRating = creditRating;
	}
	

	
}
