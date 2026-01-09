package com.cware.netshopping.patmon.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PartnerAddress {
	String type; // 타입(D: 배송지, R: 반송지)

	// 관리주소명
	// 다른관리주소명과 동일할 수 없음
	String addressName;

	Address address; // 주소
	String managerName; // 관리자명
	String managerPhone; // 관리자연락처

	// 기본배송지여부
	// 기본 배송지 설정시 기존내용은 자동으로 설정취소됨
	// default: false
	Boolean defaultAddress;

	String no; // 주소지 번호
	String partnerNo; // 파트너 번호
	Boolean available; // 사용여부

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerPhone() {
		return managerPhone;
	}

	public void setManagerPhone(String managerPhone) {
		this.managerPhone = managerPhone;
	}

	public Boolean getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(Boolean defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getPartnerNo() {
		return partnerNo;
	}

	public void setPartnerNo(String partnerNo) {
		this.partnerNo = partnerNo;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	@Override
	public String toString() {
		return "PartnerAddress [type=" + type + ", addressName=" + addressName + ", address=" + address
				+ ", managerName=" + managerName + ", managerPhone=" + managerPhone + ", defaultAddress="
				+ defaultAddress + ", no=" + no + ", partnerNo=" + partnerNo + ", available=" + available + "]";
	}

}
