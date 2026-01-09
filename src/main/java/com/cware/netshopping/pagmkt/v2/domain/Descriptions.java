package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Descriptions {

	// 상품 상세 정보
	private Description kor;

	public Description getKor() {
		return kor;
	}

	public void setKor(Description kor) {
		this.kor = kor;
	}

	@Override
	public String toString() {
		return "Descriptions [kor=" + kor + "]";
	}

}
