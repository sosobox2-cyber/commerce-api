package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrderListResponse {

	private long totalPageCount;
	private long totalElementCount;

	@JsonProperty("list")
	List<OrderList> list ;

	public long getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(long totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public long getTotalElementCount() {
		return totalElementCount;
	}

	public void setTotalElementCount(long totalElementCount) {
		this.totalElementCount = totalElementCount;
	}

	public List<OrderList> getList() {
		return list;
	}

	public void setList(List<OrderList> list) {
		this.list = list;
	}


	
}
