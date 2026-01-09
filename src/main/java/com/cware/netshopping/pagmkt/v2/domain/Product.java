package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Product {

	// 판매상태 변경
	// true : 판매가능 상태로 전환
	// false : 판매중지 상태로 전환
	// *중지상태로 1개월 유지할 경우 삭제됨
	// 판매중지 상태로 정보 호출 시 반영되지 않음
	private SiteValue isSell;

	// 상품조회 API에서만 내려가는 항목
	// true : 노출대기상태
	// false : 노출대기상태 아님(정상상태)
	private Boolean isDisplayExclude;

	// 상품 기본 정보
	private ItemBasicInfo itemBasicInfo;

	// 상품 추가 정보
	private ItemAddtionalInfo itemAddtionalInfo;

	// 가격 추가 정보
	private AddtionalInfo addtionalInfo;

	public SiteValue getIsSell() {
		return isSell;
	}

	public void setIsSell(SiteValue isSell) {
		this.isSell = isSell;
	}

	@JsonProperty("isDisplayExclude")
	public Boolean isDisplayExclude() {
		return isDisplayExclude;
	}

	@JsonProperty("isDisplayExclude")
	public void setDisplayExclude(Boolean isDisplayExclude) {
		this.isDisplayExclude = isDisplayExclude;
	}

	public ItemBasicInfo getItemBasicInfo() {
		return itemBasicInfo;
	}

	public void setItemBasicInfo(ItemBasicInfo itemBasicInfo) {
		this.itemBasicInfo = itemBasicInfo;
	}

	public ItemAddtionalInfo getItemAddtionalInfo() {
		return itemAddtionalInfo;
	}

	public void setItemAddtionalInfo(ItemAddtionalInfo itemAddtionalInfo) {
		this.itemAddtionalInfo = itemAddtionalInfo;
	}

	public AddtionalInfo getAddtionalInfo() {
		return addtionalInfo;
	}

	public void setAddtionalInfo(AddtionalInfo addtionalInfo) {
		this.addtionalInfo = addtionalInfo;
	}

	@Override
	public String toString() {
		return "Product [isSell=" + isSell + ", isDisplayExclude=" + isDisplayExclude + ", itemBasicInfo="
				+ itemBasicInfo + ", itemAddtionalInfo=" + itemAddtionalInfo + ", addtionalInfo=" + addtionalInfo + "]";
	}

}
