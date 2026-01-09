package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChldCert {
	String chldCertYn; // 어린이인증 여부

	// 어린이인증 구분 (commCd:I368)
	// (어린이인증 여부가 Y일 경우에만 필수)
	// 10 : 안전인증대상
	// 20 : 안전확인대상
	// 30 : 공급자적합성확인
	String chldCertDivCd;

	// 인증번호
	// 어린이인증 구분이 10, 20 일경우에만 필수
	String chldCertNo;

	public String getChldCertYn() {
		return chldCertYn;
	}

	public void setChldCertYn(String chldCertYn) {
		this.chldCertYn = chldCertYn;
	}

	public String getChldCertDivCd() {
		return chldCertDivCd;
	}

	public void setChldCertDivCd(String chldCertDivCd) {
		this.chldCertDivCd = chldCertDivCd;
	}

	public String getChldCertNo() {
		return chldCertNo;
	}

	public void setChldCertNo(String chldCertNo) {
		this.chldCertNo = chldCertNo;
	}

	@Override
	public String toString() {
		return "ChldCert [chldCertYn=" + chldCertYn + ", chldCertDivCd=" + chldCertDivCd + ", chldCertNo=" + chldCertNo
				+ "]";
	}

}
