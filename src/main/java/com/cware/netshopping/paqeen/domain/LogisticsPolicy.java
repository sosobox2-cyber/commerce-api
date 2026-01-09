package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class LogisticsPolicy {
	private String shippingPlace;
	private String returnPlace;
	private String courier;
	private String vendor;
	
	public String getShippingPlace() {
		return shippingPlace;
	}
	public void setShippingPlace(String shippingPlace) {
		this.shippingPlace = shippingPlace;
	}
	public String getReturnPlace() {
		return returnPlace;
	}
	public void setReturnPlace(String returnPlace) {
		this.returnPlace = returnPlace;
	}
	public String getCourier() {
		return courier;
	}
	public void setCourier(String courier) {
		this.courier = courier;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
}
