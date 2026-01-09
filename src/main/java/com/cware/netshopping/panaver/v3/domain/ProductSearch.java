package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductSearch {
	
	// 콘텐츠 목록
	private List<Contents> contents;		
	// 페이지 번호
	private int page;
	// 페이지 크기
	private int size;
	// 전체 개수
	private long totalElements;
	// 전체 페이지 수
	private long totalPages;
	// 정렬 정보
	private Sort sort;	
	// 첫 번째 페이지 여부
	private String first;
	// 마지막 페이지 여부
	private String last;
	
	
	public List<Contents> getContents() {
		return contents;
	}

	public void setContents(List<Contents> contents) {
		this.contents = contents;
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

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	@Override
	public String toString() {
		return "ProductSearch [page=" + page + "size=" + size + "totalElements=" + totalElements + ", totalPages=" + totalPages + ", sort=" + sort + ", first=" + first + ", last=" + last + "]";
	}
	
}
