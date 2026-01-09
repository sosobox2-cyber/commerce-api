package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Price {
	// 상품 조회할땐 Gmkt
	// 상품 등록할땐 gmkt ?
	// G마켓에서 판매될 금액 입력(십억이상 입력 불가)
	// 10원단위로 등록
	// 판매자할인 등록 시,
	// 판매가격 기준으로 할인 적용됨
	private Double gmkt;

	// 상품 조회할땐 Iac
	// 상품 등록할땐 iac ?
	// 옥션에서 판매될 금액 입력(십억이상 입력 불가)
	// 10원단위로 등록
	// 판매자할인 등록 시,
	// 판매가격 기준으로 할인 적용됨
	private Double iac;

	@JsonProperty("Gmkt")
	public Double getGmkt() {
		return gmkt;
	}

	@JsonProperty("Gmkt")
	public void setGmkt(Double gmkt) {
		this.gmkt = gmkt;
	}

	@JsonProperty("Iac")
	public Double getIac() {
		return iac;
	}

	@JsonProperty("Iac")
	public void setIac(Double iac) {
		this.iac = iac;
	}

	@Override
	public String toString() {
		return "Price [gmkt=" + gmkt + ", iac=" + iac + "]";
	}

}
