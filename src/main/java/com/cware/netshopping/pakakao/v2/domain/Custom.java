package com.cware.netshopping.pakakao.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Custom {

	// 작성형 옵션명 (옵션명 숫자만 입력 불가)
	String name;

	// 사용여부
	boolean usable;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUsable() {
		return usable;
	}

	public void setUsable(boolean usable) {
		this.usable = usable;
	}

	@Override
	public String toString() {
		return "Custom [name=" + name + ", usable=" + usable + "]";
	}

}
