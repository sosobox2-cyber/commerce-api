package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CertInfo {
	// 인증종류 (commCd:I387)
	// 인증대상 카테고리 일 경우 필수
	// 6000000001 : 어린이인증 대상여부
	// 6000000002 : 안전인증 대상여부
	// 6000000003 : 전파인증 적합성평가 대상여부
	// 6000000004 : 위해우려제품 표시대상여부
	String certKind;

	// 인증 대상 코드 (commCd:I426)
	// 해외직구 인증표시면제는 인증종류가 안전인증일 경우에만 해당
	// 10 : 대상
	// 20 : 비대상
	// 30 : 해외직구 인증표시면제
	String certTgtCd;

	// 인증 구분 (commCd:I368)
	// 인증여부가 Y이고 인증종류가 (certKind=6000000001 | 6000000002) 일 경우 필수
	// 10 : 안전인증대상
	// 20 : 안전확인대상
	// 30 : 공급자적합성확인
	String certDivCd;

	// 인증번호
	// 인증 구분이 10, 20 일경우에만 필수
	String certNo;

	public String getCertKind() {
		return certKind;
	}

	public void setCertKind(String certKind) {
		this.certKind = certKind;
	}

	public String getCertTgtCd() {
		return certTgtCd;
	}

	public void setCertTgtCd(String certTgtCd) {
		this.certTgtCd = certTgtCd;
	}

	public String getCertDivCd() {
		return certDivCd;
	}

	public void setCertDivCd(String certDivCd) {
		this.certDivCd = certDivCd;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	@Override
	public String toString() {
		return "CertInfo [certKind=" + certKind + ", certTgtCd=" + certTgtCd + ", certDivCd=" + certDivCd + ", certNo="
				+ certNo + "]";
	}

}
