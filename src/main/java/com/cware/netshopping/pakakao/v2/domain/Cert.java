package com.cware.netshopping.pakakao.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Cert {

	// 상품 인증 유형
	// NOT_APPLICABLE 해당없음(인증대상아님) 입력금지
	// DETAIL_REF 상품상세설명 참조 입력금지
	// KC_1 [생활용품] 안전인증 필수
	// KC_2 [생활용품] 안전확인 필수
	// KC_3 [생활용품] 어린이보호포장 필수
	// KC_4 [생활용품] 공급자적합성확인 입력금지
	// KC_5 [전기용품] 안전인증 필수
	// KC_6 [전기용품] 안전확인 필수
	// KC_7 [전기용품] 공급자적합성확인 입력금지
	// KC_8 [어린이제품] 안전인증 필수
	// KC_9 [어린이제품] 안전확인 필수
	// KC_10 [어린이제품] 공급자적합성확인 입력금지
	// RRA_1 [방송통신기자재] 적합성평가 필수
	// FOOD_1 [친환경농축산물인증] 무농약농산물 필수
	// FOOD_2 [친환경농축산물인증] 유기축산물 필수
	// FOOD_3 [친환경농축산물인증] 유기농산물 필수
	// FOOD_5 [친환경농축산물] 무항생제축산물 필수
	// FOOD_6 [친환경수산물] 유기수산물 필수
	// FOOD_10 [친환경농축산물] 유기가공식품 필수
	// FOOD_14 건강기능식품 광고심의 필수
	// FOOD_16 [친환경수산물] 무항생제수산물 필수
	// FOOD_17 [친환경수산물] 활성처리제비사용수산물 필수
	// ECOLIFE_1 생활화학/살생물제품 필수
	String certType;

	// 상품 인증 번호
	String certCode;

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCertCode() {
		return certCode;
	}

	public void setCertCode(String certCode) {
		this.certCode = certCode;
	}

	@Override
	public String toString() {
		return "Cert [certType=" + certType + ", certCode=" + certCode + "]";
	}

}
