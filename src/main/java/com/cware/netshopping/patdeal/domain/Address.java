package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Address {
	
	// State(해외)
	private String overseaRegion;
	// City(해외)
	private String overseaCity;
	// 전체 주소
	private String addressStr;
	// 기본 주소
	private String address;
	// 국가명
	private String countryCdLabel;
	// 국가코드
	private String countryCd;
	// 지번 주소
	private String jibunAddress;
	// 상세 주소
	private String detailAddress;
	// 우편번호(zip/postal code)
	private String zipCd;
	// Street Address2(해외)
	private String overseaAddress2;
	// Street Address1(해외)
	private String overseaAddress1;
	
	public String getOverseaRegion() {
		return overseaRegion;
	}
	public void setOverseaRegion(String overseaRegion) {
		this.overseaRegion = overseaRegion;
	}
	public String getOverseaCity() {
		return overseaCity;
	}
	public void setOverseaCity(String overseaCity) {
		this.overseaCity = overseaCity;
	}
	public String getAddressStr() {
		return addressStr;
	}
	public void setAddressStr(String addressStr) {
		this.addressStr = addressStr;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountryCdLabel() {
		return countryCdLabel;
	}
	public void setCountryCdLabel(String countryCdLabel) {
		this.countryCdLabel = countryCdLabel;
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
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	public String getZipCd() {
		return zipCd;
	}
	public void setZipCd(String zipCd) {
		this.zipCd = zipCd;
	}
	public String getOverseaAddress2() {
		return overseaAddress2;
	}
	public void setOverseaAddress2(String overseaAddress2) {
		this.overseaAddress2 = overseaAddress2;
	}
	public String getOverseaAddress1() {
		return overseaAddress1;
	}
	public void setOverseaAddress1(String overseaAddress1) {
		this.overseaAddress1 = overseaAddress1;
	}
	
	@Override
	public String toString() {
		return "Address [overseaRegion=" + overseaRegion + ", overseaCity=" + overseaCity + ", addressStr=" + addressStr
				+ ", address=" + address + ", countryCdLabel=" + countryCdLabel + ", countryCd=" + countryCd
				+ ", jibunAddress=" + jibunAddress + ", detailAddress=" + detailAddress + ", zipCd=" + zipCd
				+ ", overseaAddress2=" + overseaAddress2 + ", overseaAddress1=" + overseaAddress1 + "]";
	}
	
}
