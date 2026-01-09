package com.cware.netshopping.pakakao.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductOriginAreaInfo {

	// 원산지 종류
	// LOCAL 국내산
	// IMPORT 수입산
	// DEEP_SEA 원양산
	// USER_INPUT 혼합/기타
	String originAreaType;

	// 혼합/기타 표시내용
	String originAreaContent;

	// 원산지 상세 지역코드
	String originAreaCode;

	// 복수원산지여부
	boolean registerWithOtherOriginArea;

	public String getOriginAreaType() {
		return originAreaType;
	}

	public void setOriginAreaType(String originAreaType) {
		this.originAreaType = originAreaType;
	}

	public String getOriginAreaContent() {
		return originAreaContent;
	}

	public void setOriginAreaContent(String originAreaContent) {
		this.originAreaContent = originAreaContent;
	}

	public String getOriginAreaCode() {
		return originAreaCode;
	}

	public void setOriginAreaCode(String originAreaCode) {
		this.originAreaCode = originAreaCode;
	}

	public boolean isRegisterWithOtherOriginArea() {
		return registerWithOtherOriginArea;
	}

	public void setRegisterWithOtherOriginArea(boolean registerWithOtherOriginArea) {
		this.registerWithOtherOriginArea = registerWithOtherOriginArea;
	}

	@Override
	public String toString() {
		return "ProductOriginAreaInfo [originAreaType=" + originAreaType + ", originAreaContent=" + originAreaContent
				+ ", originAreaCode=" + originAreaCode + ", registerWithOtherOriginArea=" + registerWithOtherOriginArea
				+ "]";
	}
}
