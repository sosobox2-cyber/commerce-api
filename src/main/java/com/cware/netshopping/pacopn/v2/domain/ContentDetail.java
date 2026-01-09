package com.cware.netshopping.pacopn.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ContentDetail {

	// 내용
	String content;

	// 세부타입
	// IMAGE 이미지
	// TEXT 텍스트
	String detailType;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDetailType() {
		return detailType;
	}

	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}

	@Override
	public String toString() {
		return "ContentDetail [content=" + content + ", detailType=" + detailType + "]";
	}
	
}
