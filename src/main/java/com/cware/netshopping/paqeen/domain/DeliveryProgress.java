package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryProgress {
	private String vendorName;
	private String vendorDeliveryNumber;
	private String detailUrl;
	private String state;
	private Long shippedAtMillis;

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorDeliveryNumber() {
		return vendorDeliveryNumber;
	}

	public void setVendorDeliveryNumber(String vendorDeliveryNumber) {
		this.vendorDeliveryNumber = vendorDeliveryNumber;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getShippedAtMillis() {
		return shippedAtMillis;
	}

	public void setShippedAtMillis(Long shippedAtMillis) {
		this.shippedAtMillis = shippedAtMillis;
	}

}
