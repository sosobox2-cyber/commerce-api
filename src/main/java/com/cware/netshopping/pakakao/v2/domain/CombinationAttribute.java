package com.cware.netshopping.pakakao.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CombinationAttribute {

	// 조합형 옵션명 (옵션명 숫자만 입력 불가)
	String name;

	// 조합형 옵션값
	String value;

	// 옵션 이미지
	OptionImage optionImages;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public OptionImage getOptionImages() {
		return optionImages;
	}

	public void setOptionImages(OptionImage optionImages) {
		this.optionImages = optionImages;
	}

	@Override
	public String toString() {
		return "CombinationAttribute [name=" + name + ", value=" + value + ", optionImages=" + optionImages + "]";
	}

}
