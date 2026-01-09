package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmProcResponseData {
	
	// 성공 상품 주문 번호
	private List<SuccessProductOrderInfo> successProductOrderInfos;
	// 실패 상품 주문 번호
	private List<FailProductOrderInfo> failProductOrderInfos;
	
	public List<SuccessProductOrderInfo> getSuccessProductOrderInfos() {
		return successProductOrderInfos;
	}
	public void setSuccessProductOrderInfos(List<SuccessProductOrderInfo> successProductOrderInfos) {
		this.successProductOrderInfos = successProductOrderInfos;
	}
	public List<FailProductOrderInfo> getFailProductOrderInfos() {
		return failProductOrderInfos;
	}
	public void setFailProductOrderInfos(List<FailProductOrderInfo> failProductOrderInfos) {
		this.failProductOrderInfos = failProductOrderInfos;
	}
	@Override
	public String toString() {
		return "ConfirmProc [successProductOrderInfos=" + successProductOrderInfos + ", failProductOrderInfos="
				+ failProductOrderInfos + "]";
	}
	

}
