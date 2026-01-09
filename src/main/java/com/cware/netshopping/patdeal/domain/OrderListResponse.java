package com.cware.netshopping.patdeal.domain;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class OrderListResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1751028916365942840L;
	
	private long totalCount;

	@JsonProperty("contents")
	List<OrderList> contents ;

	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<OrderList> getContents() {
		return contents;
	}

	public void setContents(List<OrderList> contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		return "OrderListResponse [totalCount=" + totalCount + ", contents=" + contents + ", status=" + status + "]";
	}

	
}
