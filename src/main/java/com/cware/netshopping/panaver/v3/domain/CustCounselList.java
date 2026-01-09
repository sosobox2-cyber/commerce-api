package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CustCounselList {
	
	// 고객 문의 목록
	@JsonProperty("content")
	private List<CustCounsel> custCounselList;
		
	// 전체 페이지 수
	private int totalPages;
	// 전체 개수
	private long totalElements;
	// 페이지 정보
	private Pageable pageable;
	// 첫 번째 페이지 여부
	private boolean first;
	// 마지막 페이지 여부
	private boolean last;
	
	private int number;
	// 정렬 정보
	private Sort sort;
	// 전체 개수
	private int numberOfElements;
	// 페이지 크기
	private int size;
	// 데이터 존재 여부
	private boolean empty;
	

	public List<CustCounsel> getCustCounselList() {
		return custCounselList;
	}

	public void setCustCounselList(List<CustCounsel> custCounselList) {
		this.custCounselList = custCounselList;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public Pageable getPageable() {
		return pageable;
	}

	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
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

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	@Override
	public String toString() {
		return "CustCounselList [custCounselList=" + custCounselList + ", totalPages=" + totalPages + ", totalElements="
				+ totalElements + ", pageable=" + pageable + ", first=" + first + ", last=" + last + ", number="
				+ number + ", sort=" + sort + ", numberOfElements=" + numberOfElements + ", size=" + size + ", empty="
				+ empty + "]";
	}
	
}
