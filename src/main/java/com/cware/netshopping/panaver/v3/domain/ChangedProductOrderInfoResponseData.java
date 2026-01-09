package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangedProductOrderInfoResponseData {
	
	// 변경 상품 주문 내역 리스트
	@JsonProperty("lastChangeStatuses")
	private List<ChangedProductOrderInfo> changedProductOrderInfoList;
	// 추가 응답 항목
	private More more;
	// 변경 상품 주문 내역 개수
	private int count;
	
	public List<ChangedProductOrderInfo> getChangedProductOrderInfoList() {
		return changedProductOrderInfoList;
	}
	
	public void setChangedProductOrderInfoList(List<ChangedProductOrderInfo> changedProductOrderInfoList) {
		this.changedProductOrderInfoList = changedProductOrderInfoList;
	}
	
	public More getMore() {
		return more;
	}
	
	public void setMore(More more) {
		this.more = more;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "ChangedProductOrderInfoResponseData [changedProductOrderInfoList=" + changedProductOrderInfoList
				+ ", more=" + more + ", count=" + count + "]";
	}
}
