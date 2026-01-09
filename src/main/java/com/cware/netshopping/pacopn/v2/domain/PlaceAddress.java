package com.cware.netshopping.pacopn.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PlaceAddress {
	
	// 주소 타입
	// JIBUN, ROADNAME, OVERSEA
	// JIBUN: 지번
	// ROADNAME: 도로명
	// OVERSEA: 해외
	// 도로명 주소 등록 시, 반드시 지번 주소를 함께 등록해야함
	String addressType; 

	// 국가 코드, 국내의 경우 "KR"입력. 유효길이는 2
	String countryCode;

	// 전화번호, e.g. : xx-yyy-zzzz,
	// x: 2~4자
	// y: 3~4자
	// z: 4자
	String companyContactNumber;

	// 보조 전화번호 (형식 : 전화번호1과 같음)
	String phoneNumber2;

	// 우편번호 : 숫자, 최소길이 5, 최대길이 6
	String returnZipCode;
	
	// 주소, 최대길이는 150
	String returnAddress;
	
	// 상세주소, 최대길이는 200
	String returnAddressDetail;

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCompanyContactNumber() {
		return companyContactNumber;
	}

	public void setCompanyContactNumber(String companyContactNumber) {
		this.companyContactNumber = companyContactNumber;
	}

	public String getPhoneNumber2() {
		return phoneNumber2;
	}

	public void setPhoneNumber2(String phoneNumber2) {
		this.phoneNumber2 = phoneNumber2;
	}

	public String getReturnZipCode() {
		return returnZipCode;
	}

	public void setReturnZipCode(String returnZipCode) {
		this.returnZipCode = returnZipCode;
	}

	public String getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}

	public String getReturnAddressDetail() {
		return returnAddressDetail;
	}

	public void setReturnAddressDetail(String returnAddressDetail) {
		this.returnAddressDetail = returnAddressDetail;
	}

	@Override
	public String toString() {
		return "PlaceAddress [addressType=" + addressType + ", countryCode=" + countryCode + ", companyContactNumber="
				+ companyContactNumber + ", phoneNumber2=" + phoneNumber2 + ", returnZipCode=" + returnZipCode
				+ ", returnAddress=" + returnAddress + ", returnAddressDetail=" + returnAddressDetail + "]";
	}

}
