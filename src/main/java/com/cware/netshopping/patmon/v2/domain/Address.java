package com.cware.netshopping.patmon.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Address {
	String zipCode; // 우편번호

	// 지번주소
	// 우편번호에 해당하는 지번주소
	String address;

	String addressDetail; // 주소지상세

	// 도로명주소
	// 지번주소에 해당하는 도로명 주소
	String streetAddress;

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	@Override
	public String toString() {
		return "Address [zipCode=" + zipCode + ", address=" + address + ", addressDetail=" + addressDetail
				+ ", streetAddress=" + streetAddress + "]";
	}

}
