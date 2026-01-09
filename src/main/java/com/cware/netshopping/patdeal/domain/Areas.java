package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Areas {
	
	// 주소
	private String address;
	// 기본으로 제공되는 지역 여부
	@JsonProperty("isDefaultArea")
	private boolean isDefaultArea;
	// 지역 번호
	private String areaNo;
	// 도로명 주소
	private String roadAddress;
	// 국가코드
	private String countryCd;
	// 지번 주소
	private String jibunAddress;
	// 우편번호
	private String zipCd;
	// 주
	private String state;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public boolean isDefaultArea() {
		return isDefaultArea;
	}
	public void setDefaultArea(boolean isDefaultArea) {
		this.isDefaultArea = isDefaultArea;
	}
	public String getAreaNo() {
		return areaNo;
	}
	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}
	public String getRoadAddress() {
		return roadAddress;
	}
	public void setRoadAddress(String roadAddress) {
		this.roadAddress = roadAddress;
	}
	public String getCountryCd() {
		return countryCd;
	}
	public void setCountryCd(String countryCd) {
		this.countryCd = countryCd;
	}
	public String getJibunAddress() {
		return jibunAddress;
	}
	public void setJibunAddress(String jibunAddress) {
		this.jibunAddress = jibunAddress;
	}
	public String getZipCd() {
		return zipCd;
	}
	public void setZipCd(String zipCd) {
		this.zipCd = zipCd;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "Areas [address=" + address + ", isDefaultArea=" + isDefaultArea + ", areaNo=" + areaNo
				+ ", roadAddress=" + roadAddress + ", countryCd=" + countryCd + ", jibunAddress=" + jibunAddress
				+ ", zipCd=" + zipCd + ", state=" + state + "]";
	}
	
}
