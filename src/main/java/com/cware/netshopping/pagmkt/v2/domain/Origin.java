package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Origin {
	// 원산지 타입
	// 해당 상품 유형 타입 입력
	// 0:원산지표시대상아님(식품이외),
	// 1:상세설명참조,
	// 2:가공품,
	// 3:농산물,
	// 4:수산물
	private int goodsType;

	// 원산지 지역 타입
	// 원산지지역 타입 입력
	// 0:없음,
	// 1:국내산,
	// 2:수입산,
	// 3:기타

	// *상세설명참조일 경우 0~3 모두 입력 가능함
	// 단, 상품조회 시, 상세설명참조는 0으로 내려감
	private int type;

	// 원산지 지역 상세코드
	// 원산지지역 상세코드 입력
	private String code;

	// 복수 원산지 여부
	// 단순 복수원산지 여부 체크 기능/추가 입력 정보 받지 않음
	// true : 복수원산지 상품
	// false: 단일원산지 상품
	// 가이드는 필수라고 되어있는데 필수 아님
	private Boolean isMultipleOrigin;

	public int getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonProperty("isMultipleOrigin")
	public Boolean isMultipleOrigin() {
		return isMultipleOrigin;
	}

	@JsonProperty("isMultipleOrigin")
	public void setMultipleOrigin(Boolean isMultipleOrigin) {
		this.isMultipleOrigin = isMultipleOrigin;
	}

	@Override
	public String toString() {
		return "Origin [goodsType=" + goodsType + ", type=" + type + ", code=" + code + ", isMultipleOrigin="
				+ isMultipleOrigin + "]";
	}

}
