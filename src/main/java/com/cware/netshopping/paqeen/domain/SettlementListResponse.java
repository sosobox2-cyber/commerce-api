package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettlementListResponse {

	private long totalPageCount;
	private long totalElementCount;

	@JsonProperty("list")
	List<Settlement> list ;

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

	public List<Settlement> getList() {
		return list;
	}

	public void setList(List<Settlement> list) {
		this.list = list;
	}


	
}
