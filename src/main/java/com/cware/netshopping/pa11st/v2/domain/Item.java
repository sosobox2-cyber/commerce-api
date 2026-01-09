package com.cware.netshopping.pa11st.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

	// 항목코드 (필수)
	private String code;

	// 항목값 (필수)
	// 날짜입력 방식은 YYYY/MM/DD (년/월/일) 형식으로 입력해야 합니다.
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Item [code=" + code + ", name=" + name + "]";
	}

}
