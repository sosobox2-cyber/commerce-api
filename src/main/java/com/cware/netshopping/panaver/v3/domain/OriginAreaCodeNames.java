package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OriginAreaCodeNames {

	// 원산지 지역코드
	private String code;	
	// 원산지 지역이름 
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
		return "OriginAreaCodeNames [code=" + code + ", name=" + name + "]";
	}

}
