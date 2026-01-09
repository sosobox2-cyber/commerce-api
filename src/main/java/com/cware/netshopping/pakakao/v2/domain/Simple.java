package com.cware.netshopping.pakakao.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Simple {

	// 옵션명 (옵션명 숫자만 입력 불가)
	String name;

	// 옵션값
	String value;

	// 사용여부
	boolean usable;

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

	public boolean isUsable() {
		return usable;
	}

	public void setUsable(boolean usable) {
		this.usable = usable;
	}

	@Override
	public String toString() {
		return "Simple [name=" + name + ", value=" + value + ", usable=" + usable + "]";
	}

}
