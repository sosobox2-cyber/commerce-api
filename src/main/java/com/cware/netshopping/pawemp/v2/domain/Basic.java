package com.cware.netshopping.pawemp.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Basic {
	// 상품명 (필수)
	// (최대 70자 - 초과 시 70자까지만 저장)
	String productName;
	// 상품유형 (필수)
	// (N:새상품, U:중고, R:리퍼, B:반품(리세일), O:주문제작, E:E티켓, T:렌탈, M:마트상품)
	String productType;
	// 세부 카테고리 (필수)
	// (기초데이터-카테고리 조회 API 참고)
	String dcateCode;
	// 배송 정책번호
	// (기초데이터-배송정책정보 조회 API 참고)
	String shipPolicyNo;
	// 배송정보
	// (배송 정책번호가 [상품별 배송]인 경우 필수값입력 / [묶음 배송]인 경우 미입력)
	ShipInfo shipInfo;
	// 19금 제한여부
	// (Y:제한사용, N:제한미사용-기본값 / ※ 카테고리 기준에 따라 19금제한여부는 변경 적용될 수 있음)
	String adultLimitYn;
	// 노출정보 (필수)
	// (Y:노출, D:비노출-상세접근가능, N:비노출-상세접근불가)
	String displayYn;
	// 사업자 전용상품여부
	// (Y: 대상 / N: 대상아님(기본값) (등록 후 수정불가)
	String bizYn;
	// 브랜드번호 (기초데이터-브랜드 조회 API 참고)
	String brandNo;
	// 제조사번호 (기초데이터-제조사 조회 API 참고)
	String makerNo;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getDcateCode() {
		return dcateCode;
	}

	public void setDcateCode(String dcateCode) {
		this.dcateCode = dcateCode;
	}

	public String getShipPolicyNo() {
		return shipPolicyNo;
	}

	public void setShipPolicyNo(String shipPolicyNo) {
		this.shipPolicyNo = shipPolicyNo;
	}

	public ShipInfo getShipInfo() {
		return shipInfo;
	}

	public void setShipInfo(ShipInfo shipInfo) {
		this.shipInfo = shipInfo;
	}

	public String getAdultLimitYn() {
		return adultLimitYn;
	}

	public void setAdultLimitYn(String adultLimitYn) {
		this.adultLimitYn = adultLimitYn;
	}

	public String getDisplayYn() {
		return displayYn;
	}

	public void setDisplayYn(String displayYn) {
		this.displayYn = displayYn;
	}

	public String getBizYn() {
		return bizYn;
	}

	public void setBizYn(String bizYn) {
		this.bizYn = bizYn;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getMakerNo() {
		return makerNo;
	}

	public void setMakerNo(String makerNo) {
		this.makerNo = makerNo;
	}

	@Override
	public String toString() {
		return "Basic [productName=" + productName + ", productType=" + productType + ", dcateCode=" + dcateCode
				+ ", shipPolicyNo=" + shipPolicyNo + ", shipInfo=" + shipInfo + ", adultLimitYn=" + adultLimitYn
				+ ", displayYn=" + displayYn + ", bizYn=" + bizYn + ", brandNo=" + brandNo + ", makerNo=" + makerNo
				+ "]";
	}

}
