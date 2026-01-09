package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Sort {

	// 데이터 정렬 적용 여부
	private boolean sorted;	
	// 정렬 적용 필드 정보
	private List<Field> fields;
	// 데이터 정렬 적용 여부
	private boolean unsorted;
	// 데이터 존재 여부
	private boolean empty;
	
	
	public boolean isSorted() {
		return sorted;
	}
	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	public boolean isUnsorted() {
		return unsorted;
	}
	public void setUnsorted(boolean unsorted) {
		this.unsorted = unsorted;
	}
	public boolean isEmpty() {
		return empty;
	}
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	
	@Override
	public String toString() {
		return "Sort [sorted=" + sorted + ", fields=" + fields + ", unsorted=" + unsorted + ", empty=" + empty + "]";
	}

}
