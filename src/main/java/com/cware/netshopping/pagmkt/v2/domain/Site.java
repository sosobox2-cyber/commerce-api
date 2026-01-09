package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Site {

	// G마켓/옥션 카테고리 등록을 위한 사이트 선택
	// 1. 옥션
	// 2. G마켓
	private int siteType;

	// 최하위(Leaf)카테고리 코드 등록
	private String catCode;

	public Site(int siteType, String catCode) {
		this.siteType = siteType;
		this.catCode = catCode;
	}

	public int getSiteType() {
		return siteType;
	}

	public void setSiteType(int siteType) {
		this.siteType = siteType;
	}

	public String getCatCode() {
		return catCode;
	}

	public void setCatCode(String catCode) {
		this.catCode = catCode;
	}

	@Override
	public String toString() {
		return "Site [siteType=" + siteType + ", catCode=" + catCode + "]";
	}

}
