package com.cware.netshopping.pagmkt.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SafetyCert {

	// 통합 어린이 인증 타입
	// 통합인증대상 상품 아닐경우 ""인증대상아님""으로 입력
	// 0:인증대상,
	// 1:인증대상아님,
	// 2:상품상세별도표기
	private int type;

	// 병행 수입 여부
	// 0: UnknownOrNone 해당사항없음
	// 1: ParallelImport 병행수입
	// 2: BuyingAgent 구매대행
	private Integer mandatorySafetySign;

	// 인증항목
	private List<SafetyCertDetail> details;

	// harmful
	// 인증코드 입력
	// 100byte
	private List<String> certId;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getMandatorySafetySign() {
		return mandatorySafetySign;
	}

	public void setMandatorySafetySign(Integer mandatorySafetySign) {
		this.mandatorySafetySign = mandatorySafetySign;
	}

	public List<SafetyCertDetail> getDetails() {
		return details;
	}

	public void setDetails(List<SafetyCertDetail> details) {
		this.details = details;
	}

	public List<String> getCertId() {
		return certId;
	}

	public void setCertId(List<String> certId) {
		this.certId = certId;
	}

	@Override
	public String toString() {
		return "SafetyCert [type=" + type + ", mandatorySafetySign=" + mandatorySafetySign + ", details=" + details
				+ ", certId=" + certId + "]";
	}

}
