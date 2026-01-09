package com.cware.netshopping.patdeal.domain;


import java.util.List;

import com.cware.netshopping.domain.model.PaTdealSettlement;
import com.fasterxml.jackson.annotation.JsonProperty;


public class PaTdealSettlementResponse {

	private long totalCount;

	@JsonProperty("Data")
	List<PaTdealSettlement> Data ;

	private String status;

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<PaTdealSettlement> getData() {
		return Data;
	}

	public void setData(List<PaTdealSettlement> data) {
		Data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "PaTdealSettlementResponse [totalCount=" + totalCount + ", Data=" + Data + ", status=" + status + "]";
	}
	
	
}
