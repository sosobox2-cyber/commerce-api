package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Inputs {
	
	// 구매자작성형 번호(수정)
	private String mallProductInputNo;
	// 구매자작성형 매칭타입 [ OPTION: By Option, PRODUCT: By Product, AMOUNT: By quantity ]
	private String inputMatchingType;
	// 구매자작성형 텍스트 내용
	private String inputText;
	// 사용여부
	private String useYn;
	// 필수 여부(default: true)
	private boolean required;
	
	public String getMallProductInputNo() {
		return mallProductInputNo;
	}
	public void setMallProductInputNo(String mallProductInputNo) {
		this.mallProductInputNo = mallProductInputNo;
	}
	public String getInputMatchingType() {
		return inputMatchingType;
	}
	public void setInputMatchingType(String inputMatchingType) {
		this.inputMatchingType = inputMatchingType;
	}
	public String getInputText() {
		return inputText;
	}
	public void setInputText(String inputText) {
		this.inputText = inputText;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	@Override
	public String toString() {
		return "Inputs [mallProductInputNo=" + mallProductInputNo + ", inputMatchingType=" + inputMatchingType
				+ ", inputText=" + inputText + ", useYn=" + useYn + ", required=" + required + "]";
	}
	
}
