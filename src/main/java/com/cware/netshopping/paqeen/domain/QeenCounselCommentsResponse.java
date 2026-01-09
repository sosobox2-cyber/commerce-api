package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class QeenCounselCommentsResponse {
	
	private List<QeenCounselComments> list;
	private String nextCursor;
	private boolean last;
	private int totalElementCount;
	
	public List<QeenCounselComments> getList() {
		return list;
	}
	public void setList(List<QeenCounselComments> list) {
		this.list = list;
	}
	public String getNextCursor() {
		return nextCursor;
	}
	public void setNextCursor(String nextCursor) {
		this.nextCursor = nextCursor;
	}
	public boolean isLast() {
		return last;
	}
	public void setLast(boolean last) {
		this.last = last;
	}
	public int getTotalElementCount() {
		return totalElementCount;
	}
	public void setTotalElementCount(int totalElementCount) {
		this.totalElementCount = totalElementCount;
	}
	
	
	
	
	
}
