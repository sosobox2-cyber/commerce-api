package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Models {

	// 전체 카테고리명
	private String wholeCategoryName;
	// 카테고리 ID
	private String categoryId;
	// 제조사 코드
	private String manufacturerCode;
	// 제조사명
	private String manufacturerName;
	// 브랜드 코드
	private String brandCode;
	// 브랜드명
	private String brandName;
	// 모델 ID
	private String id;
	// 모델명
	private String name;
		
	public String getWholeCategoryName() {
		return wholeCategoryName;
	}

	public void setWholeCategoryName(String wholeCategoryName) {
		this.wholeCategoryName = wholeCategoryName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getManufacturerCode() {
		return manufacturerCode;
	}

	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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

	@Override
	public String toString() {
		return "Models [wholeCategoryName=" + wholeCategoryName + ", categoryId=" + categoryId + ", manufacturerCode=" + manufacturerCode + ", manufacturerName=" + manufacturerName + ", brandCode=" + brandCode + ", brandName=" + brandName + ", id=" + id + ", name=" + name + "]";
	}

}
