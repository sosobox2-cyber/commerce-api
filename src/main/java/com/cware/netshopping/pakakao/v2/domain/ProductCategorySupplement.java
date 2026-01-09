package com.cware.netshopping.pakakao.v2.domain;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductCategorySupplement {

	// 부가정보 종류
	// LIQUOR 전통주 CategorySupplement-전통주 참고
	// DEDUCT_CULTURE 문화비 소득공제 선택형 CategorySupplement-문화비 소득공제 선택형 참고
	// REVIEW_UNEXPOSE 상품 리뷰 노출 선택형 CategorySupplement-상품 리뷰 노출 선택형 참고
	// E_COUPON e쿠폰 상품 CategorySupplement-e쿠폰 상품 참고
	// BOOK 도서상품 CategorySupplement-도서상품 참고
	String categoryType;

	// 타입별 json
	Map<String, Object> supplement;

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public Map<String, Object> getSupplement() {
		return supplement;
	}

	public void setSupplement(Map<String, Object> supplement) {
		this.supplement = supplement;
	}

	@Override
	public String toString() {
		return "ProductCategorySupplement [categoryType=" + categoryType + ", supplement=" + supplement + "]";
	}

}
