package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ReturnAndExchange {

	// 판매자 주소 등록한 번호입력
	private String addrNo;
	// 원배송 택배사와 동일한 택배사로 노출
	private String shippingCompany;
	// 편도배송비 
	// 편도배송비 기준으로 G마켓/옥션 (조건부 배송비 깨지는지 여부 등) 반영되어 고객 부담 배송비 계산
	// 왕복배송비를 입력할 경우 과도하게 배송비 부과되어 강성 CS 인입되므로 주의
	// 미입력, 0 : 무료
	// 금액입력 : 편도배송비로 반영
	private Double fee;

	public String getAddrNo() {
		return addrNo;
	}

	public void setAddrNo(String addrNo) {
		this.addrNo = addrNo;
	}

	public String getShippingCompany() {
		return shippingCompany;
	}

	public void setShippingCompany(String shippingCompany) {
		this.shippingCompany = shippingCompany;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	@Override
	public String toString() {
		return "ReturnAndExchange [addrNo=" + addrNo + ", shippingCompany=" + shippingCompany + ", fee=" + fee + "]";
	}

}
