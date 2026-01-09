package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class ShippingAddress {
		
	//배송지 타입(DOMESTIC국내,FOREIGN국외)
	private String addressType;
	//기본 주소
	private String baseAddress;
	//도시. 국내 주소에는 빈 문자열('')을 입력합니다
	private String city;
	//국가
	private String country;
	//상세 주소
	private String detailedAddress;
	//이름
	private String name;
	//주(state). 국내 주소에는 빈 문자열('')을 입력합니다
	private String state;
	//연락처 1
	private String tel1;
	//연락처 2
	private String tel2;
	//우편번호
	private String zipCode;
	//도로명 주소 여부
	private boolean isRoadNameAddress;
	//수령 위치(FRONT_OF_DOOR, MANAGEMENT_OFFICE, DIRECT_RECEIVE, OTHER)
	private String pickupLocationType;
	//수령 위치
	private String pickupLocationContent;
	//출입 방법(LOBBY_PW, MANAGEMENT_OFFICE, FREE, OTHER)
	private String entryMethod;
	//출입 방법
	private String entryMethodContent;
	
	
	private String addressSeq;


	public String getAddressType() {
		return addressType;
	}


	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}


	public String getBaseAddress() {
		return baseAddress;
	}


	public void setBaseAddress(String baseAddress) {
		this.baseAddress = baseAddress;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getDetailedAddress() {
		return detailedAddress;
	}


	public void setDetailedAddress(String detailedAddress) {
		this.detailedAddress = detailedAddress;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getTel1() {
		return tel1;
	}


	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}


	public String getTel2() {
		return tel2;
	}


	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}


	public String getZipCode() {
		return zipCode;
	}


	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public boolean getIsRoadNameAddress() {
		return isRoadNameAddress;
	}


	public void setRoadNameAddress(boolean isRoadNameAddress) {
		this.isRoadNameAddress = isRoadNameAddress;
	}


	public String getPickupLocationType() {
		return pickupLocationType;
	}


	public void setPickupLocationType(String pickupLocationType) {
		this.pickupLocationType = pickupLocationType;
	}


	public String getPickupLocationContent() {
		return pickupLocationContent;
	}


	public void setPickupLocationContent(String pickupLocationContent) {
		this.pickupLocationContent = pickupLocationContent;
	}


	public String getEntryMethod() {
		return entryMethod;
	}


	public void setEntryMethod(String entryMethod) {
		this.entryMethod = entryMethod;
	}


	public String getEntryMethodContent() {
		return entryMethodContent;
	}


	public void setEntryMethodContent(String entryMethodContent) {
		this.entryMethodContent = entryMethodContent;
	}


	public String getAddressSeq() {
		return addressSeq;
	}


	public void setAddressSeq(String addressSeq) {
		this.addressSeq = addressSeq;
	}


	@Override
	public String toString() {
		return "ShippingAddress [addressType=" + addressType + ", baseAddress=" + baseAddress + ", city=" + city
				+ ", country=" + country + ", detailedAddress=" + detailedAddress + ", name=" + name + ", state="
				+ state + ", tel1=" + tel1 + ", tel2=" + tel2 + ", zipCode=" + zipCode + ", isRoadNameAddress="
				+ isRoadNameAddress + ", pickupLocationType=" + pickupLocationType + ", pickupLocationContent="
				+ pickupLocationContent + ", entryMethod=" + entryMethod + ", entryMethodContent=" + entryMethodContent
				+ ", addressSeq=" + addressSeq + "]";
	}
	
	
	
}
