package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class SuccessProductOrderInfo {
		
	// (실패) 상품 주문 번호
	private String productOrderId;
	// 오류 코드
	private boolean isReceiverAddressChanged;
	public String getProductOrderId() {
		return productOrderId;
	}
	public void setProductOrderId(String productOrderId) {
		this.productOrderId = productOrderId;
	}
	public boolean isReceiverAddressChanged() {
		return isReceiverAddressChanged;
	}
	public void setReceiverAddressChanged(boolean isReceiverAddressChanged) {
		this.isReceiverAddressChanged = isReceiverAddressChanged;
	}
	
	@Override
	public String toString() {
		return "SuccessProductOrderInfo [productOrderId=" + productOrderId + ", isReceiverAddressChanged="
				+ isReceiverAddressChanged + "]";
	}
}
