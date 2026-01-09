package com.cware.netshopping.patmon.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DealOptionsPageInfo {
	Integer totalCount; // 옵션 총 개수
	Integer totalPage; // 전체 페이지 수
	Integer currentPage; // 현재 페이지

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public String toString() {
		return "DealOptionsPageInfo [totalCount=" + totalCount + ", totalPage=" + totalPage + ", currentPage="
				+ currentPage + "]";
	}

}
