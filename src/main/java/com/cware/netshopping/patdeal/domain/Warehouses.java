package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Warehouses {
	
	// 대표 출고지 여부
	private boolean defaultReleaseWarehouse;
	// 대체문구 사용 여부
	private boolean usesSubstitutionText;
	// 주소(대체문구 사용하지 않을 시)
	private Address address;
	// 입출고 주소 명
	private String name;
	// 대체문구(대체문구 사용 시)
	private String substitutionText;
	// 대표 반품/교환지 여부
	private boolean defaultReturnWarehouse;
	
	public boolean isDefaultReleaseWarehouse() {
		return defaultReleaseWarehouse;
	}
	public void setDefaultReleaseWarehouse(boolean defaultReleaseWarehouse) {
		this.defaultReleaseWarehouse = defaultReleaseWarehouse;
	}
	public boolean isUsesSubstitutionText() {
		return usesSubstitutionText;
	}
	public void setUsesSubstitutionText(boolean usesSubstitutionText) {
		this.usesSubstitutionText = usesSubstitutionText;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubstitutionText() {
		return substitutionText;
	}
	public void setSubstitutionText(String substitutionText) {
		this.substitutionText = substitutionText;
	}
	public boolean isDefaultReturnWarehouse() {
		return defaultReturnWarehouse;
	}
	public void setDefaultReturnWarehouse(boolean defaultReturnWarehouse) {
		this.defaultReturnWarehouse = defaultReturnWarehouse;
	}
	
	@Override
	public String toString() {
		return "Warehouses [defaultReleaseWarehouse=" + defaultReleaseWarehouse + ", usesSubstitutionText="
				+ usesSubstitutionText + ", address=" + address + ", name=" + name + ", substitutionText="
				+ substitutionText + ", defaultReturnWarehouse=" + defaultReturnWarehouse + "]";
	}
	
}
