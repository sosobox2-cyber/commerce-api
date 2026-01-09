package com.cware.netshopping.pagmkt.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CertInfoGmkt {

	// G마켓용
	// 상품과 관련된 인증/보증/증명 정보 입력
	// 의료기기/방송통신기기/식품제조가공업/건강식품인증/친환경
	// 상품일 경우 인증정보 입력
	private List<String> certId;

	// G마켓용
	// 영업허가/신고코드 입력
	private String licenseSeq;

	public List<String> getCertId() {
		return certId;
	}

	public void setCertId(List<String> certId) {
		this.certId = certId;
	}

	public String getLicenseSeq() {
		return licenseSeq;
	}

	public void setLicenseSeq(String licenseSeq) {
		this.licenseSeq = licenseSeq;
	}

	@Override
	public String toString() {
		return "CertInfoGmkt [certId=" + certId + ", licenseSeq=" + licenseSeq + "]";
	}

}
