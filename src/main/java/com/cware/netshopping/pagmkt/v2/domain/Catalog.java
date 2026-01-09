package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Catalog {

	// 상품의 모델명
	private String modelName;

	// 상품의 브랜드 코드
	private Integer brandNo;

	// 상품의 바코드
	private String barCode;

	// ESM 상품분류코드, Document에는 현재 API에서 지원하지 않는 기능이라고 함
	private String epinCode;

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Integer getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(Integer brandNo) {
		this.brandNo = brandNo;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getEpinCode() {
		return epinCode;
	}

	public void setEpinCode(String epinCode) {
		this.epinCode = epinCode;
	}

	@Override
	public String toString() {
		return "Catalog [modelName=" + modelName + ", brandNo=" + brandNo + ", barCode=" + barCode + ", epinCode="
				+ epinCode + "]";
	}

}
