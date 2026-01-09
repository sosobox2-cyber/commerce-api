package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SellerDiscount {

	// 판매자 할인
	// 판매자 할인 사용 여부
	// true : 할인적용
	// false : 할인 미적용
	private Boolean isUse;

	// 판매자 할인 ( G마켓 )
	private SellerDiscountSite gmkt;

	// 판매자 할인 ( 옥션 )
	private SellerDiscountSite iac;

	@JsonProperty("isUse")
	public Boolean isUse() {
		return isUse;
	}

	@JsonProperty("isUse")
	public void setUse(Boolean isUse) {
		this.isUse = isUse;
	}

	public SellerDiscountSite getGmkt() {
		return gmkt;
	}

	public void setGmkt(SellerDiscountSite gmkt) {
		this.gmkt = gmkt;
	}

	public SellerDiscountSite getIac() {
		return iac;
	}

	public void setIac(SellerDiscountSite iac) {
		this.iac = iac;
	}

	@Override
	public String toString() {
		return "SellerDiscount [isUse=" + isUse + ", gmkt=" + gmkt + ", iac=" + iac + "]";
	}

	
}
