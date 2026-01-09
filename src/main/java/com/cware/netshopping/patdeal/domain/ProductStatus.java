package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductStatus {
	
	// 전시여부
	private boolean display;
	// 품절처리 : TRUE일 경우만 품절처리
	private boolean soldout;
	// 판매상태 (기존 판매금지인 상품은 변경불가하고, 판매금지로 변경 시 전시여부가 false로 저장됨) [ READY: 판매가능, STOP: 판매중지, PROHIBITION: 판매금지 ]
	private String saleStatusType;
	
	public boolean isDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	public boolean isSoldout() {
		return soldout;
	}
	public void setSoldout(boolean soldout) {
		this.soldout = soldout;
	}
	public String getSaleStatusType() {
		return saleStatusType;
	}
	public void setSaleStatusType(String saleStatusType) {
		this.saleStatusType = saleStatusType;
	}
	
	@Override
	public String toString() {
		return "ProductStatus [display=" + display + ", soldout=" + soldout + ", saleStatusType=" + saleStatusType
				+ "]";
	}
	
}
