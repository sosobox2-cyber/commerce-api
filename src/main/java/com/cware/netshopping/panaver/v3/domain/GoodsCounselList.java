package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GoodsCounselList {
		
	// 상품 문의 목록
	@JsonProperty("contents")
	private List<GoodsCounsel> goodsCounselList;
	
	// 페이지 번호
	private int page;	
	// 페이지 크기
	private int size;
	// 전체 개수
	private long totalElements;
	// 전체 페이지 수
	private int totalPages;
	// 정렬 정보
	private Sort sort;
	// 첫 번째 페이지 여부
	private boolean first;
	// 마지막 페이지 여부
	private boolean last;
	
	
	public List<GoodsCounsel> getGoodsCounselList() {
		return goodsCounselList;
	}
	public void setGoodsCounselList(List<GoodsCounsel> goodsCounselList) {
		this.goodsCounselList = goodsCounselList;
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
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public Sort getSort() {
		return sort;
	}
	public void setSort(Sort sort) {
		this.sort = sort;
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
		return "GoodsCounselList [goodsCounselList=" + goodsCounselList + ", page=" + page + ", size=" + size
				+ ", totalElements=" + totalElements + ", totalPages=" + totalPages + ", sort=" + sort + ", first="
				+ first + ", last=" + last + "]";
	}
	
}
