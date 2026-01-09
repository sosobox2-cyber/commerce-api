package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductCertificationInfos {

	// 인증 유형 ID
	private long certificationInfoId;
	// 인증 정보 종류 코드
	private String certificationKindType;
	// 인증 기관명
	private String name;
	// 인증번호
	private String certificationNumber;
	// 인증마크 사용 여부
	private String certificationMark;
	// 인증 상호명
	private String companyName;
	// 인증일자
	private String certificationDate;
	
	
	public long getCertificationInfoId() {
		return certificationInfoId;
	}

	public void setCertificationInfoId(long certificationInfoId) {
		this.certificationInfoId = certificationInfoId;
	}

	public String getCertificationKindType() {
		return certificationKindType;
	}

	public void setCertificationKindType(String certificationKindType) {
		this.certificationKindType = certificationKindType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCertificationNumber() {
		return certificationNumber;
	}

	public void setCertificationNumber(String certificationNumber) {
		this.certificationNumber = certificationNumber;
	}

	public String getCertificationMark() {
		return certificationMark;
	}

	public void setCertificationMark(String certificationMark) {
		this.certificationMark = certificationMark;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCertificationDate() {
		return certificationDate;
	}

	public void setCertificationDate(String certificationDate) {
		this.certificationDate = certificationDate;
	}

	@Override
	public String toString() {
		return "ProductCertificationInfos [certificationInfoId=" + certificationInfoId + ", certificationKindType=" + certificationKindType + ", name=" + name + ", certificationNumber=" + certificationNumber 
				+"certificationMark=" + certificationMark + ", companyName=" + companyName + ", certificationDate=" + certificationDate + "]";
	}

}
