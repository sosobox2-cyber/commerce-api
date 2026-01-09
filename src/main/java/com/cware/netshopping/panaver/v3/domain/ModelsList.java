package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ModelsList {
	
	// 모델 목록
	@JsonProperty("contents")
	private List<Models> modelList;
	
	// 페이지 번호	
	private int page;
	// 페이지 크기
	private int size;
	// 전체 개수
	private int totalElements;
	// 전체 페이지 수
	private int totalPages;
	// 첫 번째 페이지 여부
	private boolean first;
	// 마지막 페이지 여부
	private boolean last;
	
	public List<Models> getModelList() {
		return modelList;
	}
	
	public void setModelList(List<Models> modelList) {
		this.modelList = modelList;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	@Override
	public String toString() {
		return "ModelsList [modelList=" + modelList + ", page=" + page + ", size=" + size + ", totalElements=" + totalElements + ", totalPages=" + totalPages + ", first=" + first + ", last=" + last + "]";
	}

}
