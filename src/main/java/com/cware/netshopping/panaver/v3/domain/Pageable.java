package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Pageable {

	// 페이지 번호
	private int pageNumber;
	// 페이지 크기
	private int pageSize;
	// 정렬 정보
	private Sort sort;
	// 페이징 여부
	private boolean paged;
	// 페이징 여부
	private boolean unpaged;
	
	private long offset;
	

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public boolean isPaged() {
		return paged;
	}

	public void setPaged(boolean paged) {
		this.paged = paged;
	}

	public boolean isUnpaged() {
		return unpaged;
	}

	public void setUnpaged(boolean unpaged) {
		this.unpaged = unpaged;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "Pageable [pageNumber=" + pageNumber + ", pageSize=" + pageSize + ", sort=" + sort + ", paged=" + paged
				+ ", unpaged=" + unpaged + ", offset=" + offset + "]";
	}

}
