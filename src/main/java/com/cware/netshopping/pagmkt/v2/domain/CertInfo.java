package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CertInfo {

	private CertInfoGmkt gmkt;
	private CertInfoIac iac;
	// 통합인증 정보
	private SafetyCerts safetyCerts;

	public CertInfoGmkt getGmkt() {
		return gmkt;
	}

	public void setGmkt(CertInfoGmkt gmkt) {
		this.gmkt = gmkt;
	}

	public CertInfoIac getIac() {
		return iac;
	}

	public void setIac(CertInfoIac iac) {
		this.iac = iac;
	}

	public SafetyCerts getSafetyCerts() {
		return safetyCerts;
	}

	public void setSafetyCerts(SafetyCerts safetyCerts) {
		this.safetyCerts = safetyCerts;
	}

	@Override
	public String toString() {
		return "CertInfo [gmkt=" + gmkt + ", iac=" + iac + ", safetyCerts=" + safetyCerts + "]";
	}

}
