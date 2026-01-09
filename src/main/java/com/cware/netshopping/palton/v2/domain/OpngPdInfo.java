package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OpngPdInfo {
	// 이동통신사코드 [공통코드 : MV_CMCO_CD]
	// SKT SKT
	// KT KT
	// LGT LGT
	// CJH CJ헬로비전
	// S1 에스원
	// MLG 미디어로그
	// KTM KTM모바일
	// SKLINK SK텔링크
	// RFRBSH 리퍼비쉬
	// PNPLAY 핀플레이
	// SLF_SFC 자급제폰
	String mvCmcoCd;

	Long owhPrc; // 출고가
	String joinAplUrl; // 가입신청서 URL
	Long sptAmt; // 지원금액
	Long adtnSptAmt; // 추가지원금액

	public String getMvCmcoCd() {
		return mvCmcoCd;
	}

	public void setMvCmcoCd(String mvCmcoCd) {
		this.mvCmcoCd = mvCmcoCd;
	}

	public Long getOwhPrc() {
		return owhPrc;
	}

	public void setOwhPrc(Long owhPrc) {
		this.owhPrc = owhPrc;
	}

	public String getJoinAplUrl() {
		return joinAplUrl;
	}

	public void setJoinAplUrl(String joinAplUrl) {
		this.joinAplUrl = joinAplUrl;
	}

	public Long getSptAmt() {
		return sptAmt;
	}

	public void setSptAmt(Long sptAmt) {
		this.sptAmt = sptAmt;
	}

	public Long getAdtnSptAmt() {
		return adtnSptAmt;
	}

	public void setAdtnSptAmt(Long adtnSptAmt) {
		this.adtnSptAmt = adtnSptAmt;
	}

}
