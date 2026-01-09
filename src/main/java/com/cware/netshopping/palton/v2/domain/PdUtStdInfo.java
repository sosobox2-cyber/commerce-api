package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PdUtStdInfo {
	// 상품용량
	// 기준단위와 기준용량은 표준카테고리 매핑 정보를 따른다.
	// ex) 표준카테고리에 기준단위가 ml, 기준용량이 100으로 매핑되어 있는 경우 100ml당 가격이 표시된다.
	Integer pdCapa;

	public Integer getPdCapa() {
		return pdCapa;
	}

	public void setPdCapa(Integer pdCapa) {
		this.pdCapa = pdCapa;
	}

	@Override
	public String toString() {
		return "PdUtStdInfo [pdCapa=" + pdCapa + "]";
	}

}
