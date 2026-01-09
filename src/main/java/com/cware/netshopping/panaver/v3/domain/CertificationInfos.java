package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CertificationInfos {
		
	// 인증 카테고리 ID
	private String id;	
	// 인증명
	private String name;	
	// 인증 종류 목록	
	private List<String> kindTypes;	
		
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<String> getKindTypes() {
		return kindTypes;
	}

	public void setKindTypes(List<String> kindTypes) {
		this.kindTypes = kindTypes;
	}

	@Override
	public String toString() {
		return "CertificationInfos [id=" + id + "name="+ name + "kindTypes="+ kindTypes+"]";
	}

}
