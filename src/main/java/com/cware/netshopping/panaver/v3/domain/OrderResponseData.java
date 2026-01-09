package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponseData {
	
	// 성공 상품 주문 번호
	private List<String> successProductOrderIds;
	// 실패 상품 주문 번호
	private List<FailProductOrderInfo> failProductOrderInfos;
	
	public List<String> getSuccessProductOrderIds() {
		return successProductOrderIds;
	}
	
	public void setSuccessProductOrderIds(List<String> successProductOrderIds) {
		this.successProductOrderIds = successProductOrderIds;
	}
	
	public List<FailProductOrderInfo> getFailProductOrderInfos() {
		return failProductOrderInfos;
	}
	
	public void setFailProductOrderInfos(List<FailProductOrderInfo> failProductOrderInfos) {
		this.failProductOrderInfos = failProductOrderInfos;
	}
	
	@Override
	public String toString() {
		return "OrderResponseData [successProductOrderIds=" + successProductOrderIds + ", failProductOrderInfos="
				+ failProductOrderInfos + "]";
	}
}
