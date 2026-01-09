package com.cware.netshopping.pacopn.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Notice {

	//	상품고시정보카테고리명
	//	카테고리별 입력 가능한 상품고시정보 카테고리 중 하나를 입력
	String noticeCategoryName;

	//	상품고시정보카테고리상세명
	String noticeCategoryDetailName;

	//	내용
	String content;

	public String getNoticeCategoryName() {
		return noticeCategoryName;
	}

	public void setNoticeCategoryName(String noticeCategoryName) {
		this.noticeCategoryName = noticeCategoryName;
	}

	public String getNoticeCategoryDetailName() {
		return noticeCategoryDetailName;
	}

	public void setNoticeCategoryDetailName(String noticeCategoryDetailName) {
		this.noticeCategoryDetailName = noticeCategoryDetailName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Notice [noticeCategoryName=" + noticeCategoryName + ", noticeCategoryDetailName="
				+ noticeCategoryDetailName + ", content=" + content + "]";
	}

	
}
