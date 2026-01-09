package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class QeenCounselResponse {
	
	private List<QeenCounselSummary> list;
	private int totalPageCount;
	private int totalElementCount;
	
	public List<QeenCounselSummary> getList() {
		return list;
	}
	public void setList(List<QeenCounselSummary> list) {
		this.list = list;
	}
	public int getTotalPageCount() {
		return totalPageCount;
	}
	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	public int getTotalElementCount() {
		return totalElementCount;
	}
	public void setTotalElementCount(int totalElementCount) {
		this.totalElementCount = totalElementCount;
	}
	
	
	
	
}
