package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CustomProperty {
	
	// 항목명
	private String propName;
	// 항목값
	private List<PropValue> propValues;
	
	public List<PropValue> getPropValues() {
		return propValues;
	}
	public void setPropValues(List<PropValue> propValues) {
		this.propValues = propValues;
	}
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}

}
