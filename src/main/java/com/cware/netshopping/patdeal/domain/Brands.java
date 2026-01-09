package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Brands {
	
	// 브랜드명
	private String brandName;

	// 영문 브랜드명(최대 200자)
	private String brandNameEn;

	// 한글 브랜드명(최대 200자)
	private String brandNameKo;
	
	// 몰별 전시브랜드 번호 
	private String displayBrandNo;
	
	// 표준 브랜드 번호 
	private String brandNo;
	
	// 브랜드명 타입 [ NAME_KO: Korean, NAME_EN: English,	NONE: none ] (기본값: NAME_KO / NONE 타입은 사용하지 않습니다.)
	private String brandNameType;
	
	public String getDisplayBrandNo() {
		return displayBrandNo;
	}
	public void setDisplayBrandNo(String displayBrandNo) {
		this.displayBrandNo = displayBrandNo;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandNameEn() {
		return brandNameEn;
	}
	public void setBrandNameEn(String brandNameEn) {
		this.brandNameEn = brandNameEn;
	}
	public String getBrandNameKo() {
		return brandNameKo;
	}
	public void setBrandNameKo(String brandNameKo) {
		this.brandNameKo = brandNameKo;
	}
	public String getBrandNo() {
		return brandNo;
	}
	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
	public String getBrandNameType() {
		return brandNameType;
	}
	public void setBrandNameType(String brandNameType) {
		this.brandNameType = brandNameType;
	}
	@Override
	public String toString() {
		return "Brands [brandName=" + brandName + ", brandNameEn=" + brandNameEn + ", brandNameKo=" + brandNameKo
				+ ", displayBrandNo=" + displayBrandNo + ", brandNo=" + brandNo + ", brandNameType=" + brandNameType
				+ "]";
	}
	
	
	
}
