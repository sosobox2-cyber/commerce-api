package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class IndependentDetail {

	// 상품명
	private MultiLanguage value;

	// 조합형 옵션항목1
	private MultiLanguage value1;

	// 조합형 옵션항목2
	private MultiLanguage value2;

	// 품절여부
	// 옵션 사용일 경우 필수 옵션의 품절여부 제어(옵션 재고수량으로 제어하지 않음)
	// true : 품절 false : 판매
	private Boolean isSoldOut;

	// 전시여부
	// 옵션 사용일 경우 필수 옵션의 노출여부 제어
	// true : 노출 false : 미노출
	private Boolean isDisplay;

	// 재고 수량 ( 옥션, G마켓 )
	private Qty qty;

	// 옵션 사용일 경우 필수 해당 옵션의 판매자가 관리하는 코드 입력
	private String manageCode;

	// 현재 API 제공하지 않아 null로 호출
	private String epinCode;
	
	// 추전옵션코드
	private int recommendedOptValueNo;
	
	// 추전옵션 상품명
	private RecommendMultiLanguage recommendedOptValue;


	public MultiLanguage getValue() {
		return value;
	}

	public MultiLanguage getValue1() {
		return value1;
	}

	public void setValue1(MultiLanguage value1) {
		this.value1 = value1;
	}

	public MultiLanguage getValue2() {
		return value2;
	}

	public void setValue2(MultiLanguage value2) {
		this.value2 = value2;
	}

	public void setValue(MultiLanguage value) {
		this.value = value;
	}

	@JsonProperty("isSoldOut")
	public Boolean isSoldOut() {
		return isSoldOut;
	}

	@JsonProperty("isSoldOut")
	public void setSoldOut(Boolean isSoldOut) {
		this.isSoldOut = isSoldOut;
	}

	@JsonProperty("isDisplay")
	public Boolean isDisplay() {
		return isDisplay;
	}

	@JsonProperty("isDisplay")
	public void setDisplay(Boolean isDisplay) {
		this.isDisplay = isDisplay;
	}

	public Qty getQty() {
		return qty;
	}

	public void setQty(Qty qty) {
		this.qty = qty;
	}

	public String getManageCode() {
		return manageCode;
	}

	public void setManageCode(String manageCode) {
		this.manageCode = manageCode;
	}

	public String getEpinCode() {
		return epinCode;
	}

	public void setEpinCode(String epinCode) {
		this.epinCode = epinCode;
	}	

	public int getRecommendedOptValueNo() {
		return recommendedOptValueNo;
	}

	public void setRecommendedOptValueNo(int recommendedOptValueNo) {
		this.recommendedOptValueNo = recommendedOptValueNo;
	}

	public RecommendMultiLanguage getRecommendedOptValue() {
		return recommendedOptValue;
	}

	public void setRecommendedOptValue(RecommendMultiLanguage recommendedOptValue) {
		this.recommendedOptValue = recommendedOptValue;
	}

	@Override
	public String toString() {
		return "IndependentDetail [value=" + value + ", isSoldOut=" + isSoldOut + ", isDisplay=" + isDisplay + ", qty=" + qty 
				+ ", manageCode=" + manageCode + ", epinCode=" + epinCode + ", recommendedOptValueNo=" + recommendedOptValueNo
				+ ", recommendedOptValue=" + recommendedOptValue + "]";
	}

}
