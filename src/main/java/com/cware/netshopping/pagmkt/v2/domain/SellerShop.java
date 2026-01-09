package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SellerShop {

	// 판매자 카테고리코드
	private String catCode;
	// 판매자 카테고리명
	private String catName;
	// 판매자 브랜드코드
	private String brandCode;
	// 판매자 브랜드명
	private String brandName;

	public String getCatCode() {
		return catCode;
	}

	public void setCatCode(String catCode) {
		this.catCode = catCode;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
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

	@Override
	public String toString() {
		return "SellerShop [catCode=" + catCode + ", catName=" + catName + ", brandCode=" + brandCode + ", brandName="
				+ brandName + "]";
	}

}
