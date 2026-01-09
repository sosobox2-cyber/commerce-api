package com.cware.netshopping.palton.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AdtnPdInfo {
	// 추가상품정렬코드 [공통코드 : ADTN_PD_SORT_CD]
	// REGIST_ASC 등록30
	// NAME_ASC 가나다순
	// PRICE_ASC 낮은가격순
	// PRICE_DESC 높은가격순
	String sortCd;

	List<AdtnType> adtnTypeLst; // 추가유형목록

	public String getSortCd() {
		return sortCd;
	}

	public void setSortCd(String sortCd) {
		this.sortCd = sortCd;
	}

	public List<AdtnType> getAdtnTypeLst() {
		return adtnTypeLst;
	}

	public void setAdtnTypeLst(List<AdtnType> adtnTypeLst) {
		this.adtnTypeLst = adtnTypeLst;
	}

	@Override
	public String toString() {
		return "AdtnPdInfo [sortCd=" + sortCd + ", adtnTypeLst=" + adtnTypeLst + "]";
	}

}
