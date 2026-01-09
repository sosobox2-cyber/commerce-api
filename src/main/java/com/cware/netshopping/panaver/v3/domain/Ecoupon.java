package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Ecoupon {
	
	// E쿠폰 유효기간 구분 코드
	private String periodType;
	// E쿠폰 유효기간 시작일
	private String validStartDate;
	// E쿠폰 유효기간 종료일
	private String validEndDate;
	// E쿠폰 유효기간 내용(구매일로부터 00일)
	private int periodDays;
	// E쿠폰 발행처 내용
	private String publicInformationContents;
	// E쿠폰 연락처 내용
	private String contactInformationContents;
	// E쿠폰 사용 장소 구분 코드
	private String usePlaceType;	
	// 사용 장소 내용
	private String usePlaceContents;
	// 장바구니 구매 불가 여부
	private String restrictCart;
	// 사이트명
	private String siteName;

	
	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public String getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(String validStartDate) {
		this.validStartDate = validStartDate;
	}

	public String getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(String validEndDate) {
		this.validEndDate = validEndDate;
	}

	public int getPeriodDays() {
		return periodDays;
	}

	public void setPeriodDays(int periodDays) {
		this.periodDays = periodDays;
	}

	public String getPublicInformationContents() {
		return publicInformationContents;
	}

	public void setPublicInformationContents(String publicInformationContents) {
		this.publicInformationContents = publicInformationContents;
	}

	public String getContactInformationContents() {
		return contactInformationContents;
	}

	public void setContactInformationContents(String contactInformationContents) {
		this.contactInformationContents = contactInformationContents;
	}

	public String getUsePlaceType() {
		return usePlaceType;
	}

	public void setUsePlaceType(String usePlaceType) {
		this.usePlaceType = usePlaceType;
	}

	public String getUsePlaceContents() {
		return usePlaceContents;
	}

	public void setUsePlaceContents(String usePlaceContents) {
		this.usePlaceContents = usePlaceContents;
	}

	public String getRestrictCart() {
		return restrictCart;
	}

	public void setRestrictCart(String restrictCart) {
		this.restrictCart = restrictCart;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@Override
	public String toString() {
		return "Ecoupon [periodType=" + periodType + ", validStartDate=" + validStartDate + ", validEndDate=" + validEndDate + ", periodDays=" + periodDays + ", publicInformationContents=" +publicInformationContents + ", contactInformationContents=" + contactInformationContents + ", usePlaceType=" + usePlaceType + ", usePlaceContents=" + usePlaceContents
				+ "restrictCart=" + restrictCart + "siteName=" + siteName + "]";
	}
}
