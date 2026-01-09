package com.cware.netshopping.pawemp.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Certification {

	// 인증유형 (코드값 입력)
	String certificationType;
	// 인증번호
	String certificationNo;

	public String getCertificationType() {
		return certificationType;
	}

	public void setCertificationType(String certificationType) {
		this.certificationType = certificationType;
	}

	public String getCertificationNo() {
		return certificationNo;
	}

	public void setCertificationNo(String certificationNo) {
		this.certificationNo = certificationNo;
	}

	@Override
	public String toString() {
		return "Certification [certificationType=" + certificationType + ", certificationNo=" + certificationNo + "]";
	}

}
