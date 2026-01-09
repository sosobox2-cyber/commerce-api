package com.cware.netshopping.pa11st.v2.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductCertGroup {

	// 인증정보그룹번호 (필수)
	// 인증정보그룹번호가 존재하지 않는 식품 인증의 경우, 해당 값을 입력하지 않습니다.
	// 전기용품/생활용품, 어린이제품, 방송통신기자재, 생활화학 및 살생물제품에 대한 인증정보 입력이 필수인 카테고리일 경우 01, 02, 03,
	// 04의 인증정보를 모두 입력해주세요.
	// 01 : 전기용품/생활용품 KC인증
	// 02 : 어린이제품 KC인증
	// 03 : 방송통신기자재 KC인증
	// 04 : 생활화학 및 살생물제품
	private String crtfGrpTypCd;

	// KC인증대상여부 (필수)
	// 인증정보그룹번호가 01, 02, 03, 04인 경우 인증대상여부 값을 필수 입력해야 합니다.
	// (인증정보그룹번호 01 : 인증대상여부 01, 02, 03 택 1 사용 가능 / 인증정보그룹번호 02 : 인증대상여부 01, 03 택 1
	// 사용 가능 / 인증정보그룹번호 03 : 인증대상여부 01, 03 택 1 사용 가능 / 인증정보그룹번호 04 : 인증대상여부 04, 05 택
	// 1 사용 가능)
	// 01 : KC인증대상
	// 02 : KC면제대상
	// 03 : KC인증대상 아님
	// 04 : 생활화학 및 살생물제품 대상
	// 05 : 생활화학 및 살생물제품 대상 아님
	private String crtfGrpObjClfCd;

	// KC면제유형 (필수)
	// KC인증대상여부가 02인 경우 KC면제유형 값을 필수 입력해야 합니다.
	// 02 : 구매대행면제대상
	// 03 : 병행수입면제대상
	private String crtfGrpExptTypCd;

	// 인증정보 (필수)
	// 인증정보는 최대 100개 까지만 제공합니다.
    @XmlElement(name = "ProductCert")
	private List<ProductCert> productCert;

	public String getCrtfGrpTypCd() {
		return crtfGrpTypCd;
	}

	public void setCrtfGrpTypCd(String crtfGrpTypCd) {
		this.crtfGrpTypCd = crtfGrpTypCd;
	}

	public String getCrtfGrpObjClfCd() {
		return crtfGrpObjClfCd;
	}

	public void setCrtfGrpObjClfCd(String crtfGrpObjClfCd) {
		this.crtfGrpObjClfCd = crtfGrpObjClfCd;
	}

	public String getCrtfGrpExptTypCd() {
		return crtfGrpExptTypCd;
	}

	public void setCrtfGrpExptTypCd(String crtfGrpExptTypCd) {
		this.crtfGrpExptTypCd = crtfGrpExptTypCd;
	}

	@JsonProperty("ProductCert")
	public List<ProductCert> getProductCert() {
		return productCert;
	}

	@JsonProperty("ProductCert")
	public void setProductCert(List<ProductCert> productCert) {
		this.productCert = productCert;
	}

	@Override
	public String toString() {
		return "ProductCertGroup [crtfGrpTypCd=" + crtfGrpTypCd + ", crtfGrpObjClfCd=" + crtfGrpObjClfCd
				+ ", crtfGrpExptTypCd=" + crtfGrpExptTypCd + ", productCert=" + productCert + "]";
	}

}
