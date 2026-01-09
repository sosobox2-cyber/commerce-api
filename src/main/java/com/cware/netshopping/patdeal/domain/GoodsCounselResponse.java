package com.cware.netshopping.patdeal.domain;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class GoodsCounselResponse {


	@JsonProperty("Data")
	List<TdealGoodsCounsel> data ;

	private String status;

	public List<TdealGoodsCounsel> getData() {
		return data;
	}

	public void setData(List<TdealGoodsCounsel> data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GoodsCounselResponse [data=" + data + ", status=" + status + "]";
	}
	
	
}
