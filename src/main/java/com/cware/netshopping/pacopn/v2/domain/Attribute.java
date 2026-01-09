package com.cware.netshopping.pacopn.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Attribute {

	//	옵션타입명
	String attributeTypeName;

	//	옵션값
	String attributeValueName;

	//	구매옵션/검색옵션 구분필드.
	//	EXPOSED : 구매옵션
	//	NONE : 검색옵션
	String exposed;

	//	수정 여부 구분필드.
	//	true : 수정가능
	//	false : 수정불가
	String editable;

	public String getAttributeTypeName() {
		return attributeTypeName;
	}

	public void setAttributeTypeName(String attributeTypeName) {
		this.attributeTypeName = attributeTypeName;
	}

	public String getAttributeValueName() {
		return attributeValueName;
	}

	public void setAttributeValueName(String attributeValueName) {
		this.attributeValueName = attributeValueName;
	}

	public String getExposed() {
		return exposed;
	}

	public void setExposed(String exposed) {
		this.exposed = exposed;
	}

	public String getEditable() {
		return editable;
	}

	public void setEditable(String editable) {
		this.editable = editable;
	}

	@Override
	public String toString() {
		return "Attribute [attributeTypeName=" + attributeTypeName + ", attributeValueName=" + attributeValueName
				+ ", exposed=" + exposed + ", editable=" + editable + "]";
	}

	
}
