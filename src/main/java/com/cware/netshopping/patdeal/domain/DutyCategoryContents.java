package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DutyCategoryContents {
	
	// 항목 타입
	private String dataType;
	// 노출 순서
	private String displayOrder;
	// 설명
	private List<Object> descriptions;
	// 최대 길이
	private int maxLength;
	// 항목값
	private String contentName;
	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	public List<Object> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<Object> descriptions) {
		this.descriptions = descriptions;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	
	@Override
	public String toString() {
		return "DutyCategoryContent [dataType=" + dataType + ", displayOrder=" + displayOrder + ", descriptions="
				+ descriptions + ", maxLength=" + maxLength + ", contentName=" + contentName + "]";
	}
	
}
