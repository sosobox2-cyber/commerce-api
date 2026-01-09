package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NaverShoppingSearchInfo {
		
	// 모델명 ID
	private Long modelId;
	// 제조사명
	private String manufacturerName;
	// 브랜드명
	private String brandName;
	// 모델명
	private String modelName;
	// 카탈로그 매칭 여부 추가
	private boolean catalogMatchingYn;
		
	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public boolean isCatalogMatchingYn() {
		return catalogMatchingYn;
	}

	public void setCatalogMatchingYn(boolean catalogMatchingYn) {
		this.catalogMatchingYn = catalogMatchingYn;
	}

	@Override
	public String toString() {
		return "NaverShoppingSearchInfo [modelId=" + modelId +"manufacturerName=" + manufacturerName + "brandName=" + brandName + "modelName=" + modelName	+ "catalogMatchingYn"+ catalogMatchingYn+ "]";
	}

}
