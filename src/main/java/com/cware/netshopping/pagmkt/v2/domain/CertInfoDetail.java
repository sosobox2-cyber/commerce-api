package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CertInfoDetail {

	// true : 신고대상
	// false: 신고대상 아님
	private Boolean isUse;

	// 신고기관명
	private String certOfficeName;

	// 신고번호
	private String certId;

	// 사전광고 심의번호
	private String adDeliberationNo;

	// 방송통신기기인증정보상품상세별도표기
	// 방송통신기기 인증 사용 여부 true일 경우 필수
	private String isAddtionalCondition;

	public CertInfoDetail(boolean isUse) {
		this.isUse = isUse;
	}

	@JsonProperty("isUse")
	public Boolean isUse() {
		return isUse;
	}

	@JsonProperty("isUse")
	public void setUse(Boolean isUse) {
		this.isUse = isUse;
	}

	public String getCertOfficeName() {
		return certOfficeName;
	}

	public void setCertOfficeName(String certOfficeName) {
		this.certOfficeName = certOfficeName;
	}

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public String getAdDeliberationNo() {
		return adDeliberationNo;
	}

	public void setAdDeliberationNo(String adDeliberationNo) {
		this.adDeliberationNo = adDeliberationNo;
	}

	public String getIsAddtionalCondition() {
		return isAddtionalCondition;
	}

	public void setIsAddtionalCondition(String isAddtionalCondition) {
		this.isAddtionalCondition = isAddtionalCondition;
	}

	@Override
	public String toString() {
		return "CertInfoDetail [isUse=" + isUse + ", certOfficeName=" + certOfficeName + ", certId=" + certId
				+ ", adDeliberationNo=" + adDeliberationNo + ", isAddtionalCondition=" + isAddtionalCondition + "]";
	}

}
