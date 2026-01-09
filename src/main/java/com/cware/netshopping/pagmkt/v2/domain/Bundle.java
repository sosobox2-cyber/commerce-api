package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Bundle {

	// 묶음배송비정책번호
	// 동일 출하지인 경우 묶음배송으로 구매 가능
	// 묶음 배송비의 배송비가 적은 금액으로 적용
	private String deliveryTmplId;

	public String getDeliveryTmplId() {
		return deliveryTmplId;
	}

	public void setDeliveryTmplId(String deliveryTmplId) {
		this.deliveryTmplId = deliveryTmplId;
	}

	@Override
	public String toString() {
		return "Bundle [deliveryTmplId=" + deliveryTmplId + "]";
	}

}
