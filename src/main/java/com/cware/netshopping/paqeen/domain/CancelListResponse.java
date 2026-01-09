package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class CancelListResponse {

	private long totalPageCount;
	private long totalElementCount;

	@JsonProperty("list")
	List<CancelList> list ;

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

	public List<CancelList> getList() {
		return list;
	}

	public void setList(List<CancelList> list) {
		this.list = list;
	}


	
}
