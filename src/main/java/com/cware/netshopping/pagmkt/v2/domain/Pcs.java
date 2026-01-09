package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Pcs {

	// 가격비교 사이트 노출 여부
	private Boolean isUse;
	// 가격비교 사이트 쿠폰 적용 여부 ( 옥션 )
	private Boolean isUseIacPcsCoupon;
	// 가격비교 사이트 쿠폰 적용 여부 ( G마켓 )
	private Boolean isUseGmkPcsCoupon;

	@JsonProperty("isUse")
	public Boolean isUse() {
		return isUse;
	}

	@JsonProperty("isUse")
	public void setUse(Boolean isUse) {
		this.isUse = isUse;
	}

	@JsonProperty("isUseIacPcsCoupon")
	public Boolean isUseIacPcsCoupon() {
		return isUseIacPcsCoupon;
	}

	@JsonProperty("isUseIacPcsCoupon")
	public void setUseIacPcsCoupon(Boolean isUseIacPcsCoupon) {
		this.isUseIacPcsCoupon = isUseIacPcsCoupon;
	}

	@JsonProperty("isUseGmkPcsCoupon")
	public Boolean isUseGmkPcsCoupon() {
		return isUseGmkPcsCoupon;
	}

	@JsonProperty("isUseGmkPcsCoupon")
	public void setUseGmkPcsCoupon(Boolean isUseGmkPcsCoupon) {
		this.isUseGmkPcsCoupon = isUseGmkPcsCoupon;
	}

	@Override
	public String toString() {
		return "Pcs [isUse=" + isUse + ", isUseIacPcsCoupon=" + isUseIacPcsCoupon + ", isUseGmkPcsCoupon="
				+ isUseGmkPcsCoupon + "]";
	}

}
