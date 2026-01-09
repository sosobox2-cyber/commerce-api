package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Categories {
		
	// 카테고리 이름
	private String wholeCategoryName;	
	// 카테고리 ID
	private String id;
	// 카테고리 명
	private String name;
	// 리프카테고리 여부
	private boolean last;
	// 예외 카테고리 목록
	private List<String> exceptionalCategories;	
	// 인증 정보 목록
	private List<CertificationInfos> certificationInfos;	
		
	public String getWholeCategoryName() {
		return wholeCategoryName;
	}

	public void setWholeCategoryName(String wholeCategoryName) {
		this.wholeCategoryName = wholeCategoryName;
	}	
		
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

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public List<String> getExceptionalCategories() {
		return exceptionalCategories;
	}

	public void setExceptionalCategories(List<String> exceptionalCategories) {
		this.exceptionalCategories = exceptionalCategories;
	}

	public List<CertificationInfos> getCertificationInfos() {
		return certificationInfos;
	}

	public void setCertificationInfos(List<CertificationInfos> certificationInfos) {
		this.certificationInfos = certificationInfos;
	}

	@Override
	public String toString() {
		return "Categories [wholeCategoryName=" + wholeCategoryName + ", id=" + id + ", name=" + name + ", last=" + last + "]";
	}

}
