package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class EsmSite {

	// 최하위(Leaf)카테고리 코드 등록
	private String catCode;

	public EsmSite(String catCode) {
		this.catCode = catCode;
	}

	public String getCatCode() {
		return catCode;
	}

	public void setCatCode(String catCode) {
		this.catCode = catCode;
	}

	@Override
	public String toString() {
		return "EsmSite [catCode=" + catCode + "]";
	}

}
