package com.cware.netshopping.pawemp.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ShipInfo {

	// 반품/교환 배송비(편도) (필수)
	Long claimShipFee;
	// 출고지-우편번호(5자리) (필수)
	String releaseZipCode;
	// 출고지-기본주소(도로명) (필수)
	String releaseRoadAddress1;
	// 출고지-상세주소(도로명) (필수)
	String releaseRoadAddress2;
	// 출고지-기본주소(지번) (필수)
	String releaseAddress1;
	// 출고지-상세주소(지번) (필수)
	String releaseAddress2;
	// 회수지-우편번호 (5자리) (필수)
	String returnZipCode;
	// 회수지-기본주소(도로명) (필수)
	String returnRoadAddress1;
	// 회수지-상세주소(도로명) (필수)
	String returnRoadAddress2;
	// 회수지-기본주소(지번) (필수)
	String returnAddress1;
	// 회수지-상세주소(지번) (필수)
	String returnAddress2;
	// 출고 기한 (1~15) (필수)
	Integer releaseDay;
	// 출고기한 1일 인 경우 출고시간 (1~24)
	Integer releaseTime;
	// 휴일제외여부 (필수)
	// (Y:주말&공휴일 제외, A:주말&공휴일 및 전일제외, N:주말/공휴일 포함)
	String holidayExceptYn;

	public Long getClaimShipFee() {
		return claimShipFee;
	}

	public void setClaimShipFee(Long claimShipFee) {
		this.claimShipFee = claimShipFee;
	}

	public String getReleaseZipCode() {
		return releaseZipCode;
	}

	public void setReleaseZipCode(String releaseZipCode) {
		this.releaseZipCode = releaseZipCode;
	}

	public String getReleaseRoadAddress1() {
		return releaseRoadAddress1;
	}

	public void setReleaseRoadAddress1(String releaseRoadAddress1) {
		this.releaseRoadAddress1 = releaseRoadAddress1;
	}

	public String getReleaseRoadAddress2() {
		return releaseRoadAddress2;
	}

	public void setReleaseRoadAddress2(String releaseRoadAddress2) {
		this.releaseRoadAddress2 = releaseRoadAddress2;
	}

	public String getReleaseAddress1() {
		return releaseAddress1;
	}

	public void setReleaseAddress1(String releaseAddress1) {
		this.releaseAddress1 = releaseAddress1;
	}

	public String getReleaseAddress2() {
		return releaseAddress2;
	}

	public void setReleaseAddress2(String releaseAddress2) {
		this.releaseAddress2 = releaseAddress2;
	}

	public String getReturnZipCode() {
		return returnZipCode;
	}

	public void setReturnZipCode(String returnZipCode) {
		this.returnZipCode = returnZipCode;
	}

	public String getReturnRoadAddress1() {
		return returnRoadAddress1;
	}

	public void setReturnRoadAddress1(String returnRoadAddress1) {
		this.returnRoadAddress1 = returnRoadAddress1;
	}

	public String getReturnRoadAddress2() {
		return returnRoadAddress2;
	}

	public void setReturnRoadAddress2(String returnRoadAddress2) {
		this.returnRoadAddress2 = returnRoadAddress2;
	}

	public String getReturnAddress1() {
		return returnAddress1;
	}

	public void setReturnAddress1(String returnAddress1) {
		this.returnAddress1 = returnAddress1;
	}

	public String getReturnAddress2() {
		return returnAddress2;
	}

	public void setReturnAddress2(String returnAddress2) {
		this.returnAddress2 = returnAddress2;
	}

	public Integer getReleaseDay() {
		return releaseDay;
	}

	public void setReleaseDay(Integer releaseDay) {
		this.releaseDay = releaseDay;
	}

	public Integer getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Integer releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getHolidayExceptYn() {
		return holidayExceptYn;
	}

	public void setHolidayExceptYn(String holidayExceptYn) {
		this.holidayExceptYn = holidayExceptYn;
	}

	@Override
	public String toString() {
		return "ShipInfo [claimShipFee=" + claimShipFee + ", releaseZipCode=" + releaseZipCode
				+ ", releaseRoadAddress1=" + releaseRoadAddress1 + ", releaseRoadAddress2=" + releaseRoadAddress2
				+ ", releaseAddress1=" + releaseAddress1 + ", releaseAddress2=" + releaseAddress2 + ", returnZipCode="
				+ returnZipCode + ", returnRoadAddress1=" + returnRoadAddress1 + ", returnRoadAddress2="
				+ returnRoadAddress2 + ", returnAddress1=" + returnAddress1 + ", returnAddress2=" + returnAddress2
				+ ", releaseDay=" + releaseDay + ", releaseTime=" + releaseTime + ", holidayExceptYn=" + holidayExceptYn
				+ "]";
	}

}
