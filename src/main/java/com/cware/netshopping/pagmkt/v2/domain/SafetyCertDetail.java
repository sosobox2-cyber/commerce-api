package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SafetyCertDetail {

	// 통합 어린이 인증 코드
	// 통합 전기 인증 코드
	// 통합 생활용품 인증 코드
	private String certId;

	// 통합 어린이 인증 품목
	// 통합어린이인증 타입 0번일 경우 필수
	// 아래 인증품목중 해당하는 품목 입력
	// 0:안전인증,
	// 1:안전확인,
	// 2:공급자적합성확인

	// 통합전기인증 타입 0번일 경우 필수
	// 아래 인증품목중 해당하는 품목 입력
	// 0:안전인증,
	// 1:안전확인,
	// 3:공급자적합성확인

	// 통합생활용품인증 타입 0번일 경우 필수
	// 아래 인증품목중 해당하는 품목 입력
	// 0:안전인증,
	// 1:안전확인,
	// 3:공급자적합성확인
	private Integer certTargetCode;

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public Integer getCertTargetCode() {
		return certTargetCode;
	}

	public void setCertTargetCode(Integer certTargetCode) {
		this.certTargetCode = certTargetCode;
	}

	@Override
	public String toString() {
		return "SafetyCertDetail [certId=" + certId + ", certTargetCode=" + certTargetCode + "]";
	}

}
