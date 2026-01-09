package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CertificationTargetExcludeContent {
		
	// 어린이제품 인증 대상 제외 여부
	private String childCertifiedProductExclusionYn;
	// KC 면제 대상 타입 코드
	private String kcExemptionType;
	// KC 상품 인증 대상 제외 타입
	private String kcCertifiedProductExclusionYn;
	// 친환경 인증 대상 제외 여부
	private String greenCertifiedProductExclusionYn;
	
	
	public String getChildCertifiedProductExclusionYn() {
		return childCertifiedProductExclusionYn;
	}

	public void setChildCertifiedProductExclusionYn(String childCertifiedProductExclusionYn) {
		this.childCertifiedProductExclusionYn = childCertifiedProductExclusionYn;
	}

	public String getKcExemptionType() {
		return kcExemptionType;
	}

	public void setKcExemptionType(String kcExemptionType) {
		this.kcExemptionType = kcExemptionType;
	}

	public String getKcCertifiedProductExclusionYn() {
		return kcCertifiedProductExclusionYn;
	}

	public void setKcCertifiedProductExclusionYn(String kcCertifiedProductExclusionYn) {
		this.kcCertifiedProductExclusionYn = kcCertifiedProductExclusionYn;
	}

	public String getGreenCertifiedProductExclusionYn() {
		return greenCertifiedProductExclusionYn;
	}

	public void setGreenCertifiedProductExclusionYn(String greenCertifiedProductExclusionYn) {
		this.greenCertifiedProductExclusionYn = greenCertifiedProductExclusionYn;
	}

	@Override
	public String toString() {
		return "CertificationTargetExcludeContent [childCertifiedProductExclusionYn=" + childCertifiedProductExclusionYn +"kcExemptionType=" + kcExemptionType + "kcCertifiedProductExclusionYn=" + kcCertifiedProductExclusionYn + "greenCertifiedProductExclusionYn=" + greenCertifiedProductExclusionYn +"]";
	}

}
