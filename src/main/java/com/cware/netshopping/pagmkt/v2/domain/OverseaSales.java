package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OverseaSales {

	// true : 해외판매 진행
	// false: 해외판매 진행하지 않음
	private Boolean isAgree;

	@JsonProperty("isAgree")
	public Boolean isAgree() {
		return isAgree;
	}

	@JsonProperty("isAgree")
	public void setAgree(Boolean isAgree) {
		this.isAgree = isAgree;
	}

	@Override
	public String toString() {
		return "OverseaSales [isAgree=" + isAgree + "]";
	}

}
