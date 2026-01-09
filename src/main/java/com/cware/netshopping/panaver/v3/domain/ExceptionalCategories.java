package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ExceptionalCategories {
		
	// 예외 카테고리 목록
	private List<String> exceptionalCategories;	
	
	
	public List<String> getExceptionalCategories() {
		return exceptionalCategories;
	}


	public void setExceptionalCategories(List<String> exceptionalCategories) {
		this.exceptionalCategories = exceptionalCategories;
	}

	@Override
	public String toString() {
		return "ExceptionalCategories [exceptionalCategories=" + exceptionalCategories + "]";
	}

}
