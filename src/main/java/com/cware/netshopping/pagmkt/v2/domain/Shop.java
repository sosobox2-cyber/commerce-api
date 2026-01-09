package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Shop {

	// 미니샵 카테고리 등록을 위한 사이트 선택
	// 미니샵 카테고리 세팅은 ESM에서 판매자가 설정 가능하고 API로 미니샵 카테고리 지정 시 반영됨
	// 1. 옥션
	// 2. G마켓
	private int siteType;

	// 미니샵 대카테고리 코드 등록
	// * 미니샵 카테고리 조회 API 참고
	private String largeCatCode;

	// 미니샵 중카테고리 코드 등록
	// 미니샵 카테고리 조회 API 참고
	private String middleCatCode;

	// 미니샵 소카테고리 코드 등록
	// 미니샵 카테고리 조회 API 참고
	private String smallCatCode;

	public int getSiteType() {
		return siteType;
	}

	public void setSiteType(int siteType) {
		this.siteType = siteType;
	}

	public String getLargeCatCode() {
		return largeCatCode;
	}

	public void setLargeCatCode(String largeCatCode) {
		this.largeCatCode = largeCatCode;
	}

	public String getMiddleCatCode() {
		return middleCatCode;
	}

	public void setMiddleCatCode(String middleCatCode) {
		this.middleCatCode = middleCatCode;
	}

	public String getSmallCatCode() {
		return smallCatCode;
	}

	public void setSmallCatCode(String smallCatCode) {
		this.smallCatCode = smallCatCode;
	}

	@Override
	public String toString() {
		return "Shop [siteType=" + siteType + ", largeCatCode=" + largeCatCode + ", middleCatCode=" + middleCatCode
				+ ", smallCatCode=" + smallCatCode + "]";
	}

}
