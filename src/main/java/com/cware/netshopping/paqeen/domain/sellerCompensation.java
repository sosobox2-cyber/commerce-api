package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class sellerCompensation {
	private long sellerCompensationAmount;

	public long getSellerCompensationAmount() {
		return sellerCompensationAmount;
	}

	public void setSellerCompensationAmount(long sellerCompensationAmount) {
		this.sellerCompensationAmount = sellerCompensationAmount;
	}
}
