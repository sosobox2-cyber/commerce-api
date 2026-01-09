package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SftyAthn {

	// 안전인증유형코드 [공통코드 : SFTY_ATHN_TYP_CD]
	// CHL_SUPS [어린이제품]공급자적합성확인
	// CHL_ATHN [어린이제품]안전인증
	// CHL_CFM [어린이제품]안전확인
	// CMCN_TNTT [방송통신기자재]잠정인증
	// CMCN_REG [방송통신기자재]적합등록
	// CMCN_ATHN [방송통신기자재]적합인증
	// LIFE_SUPS [생활용품]공급자적합성확인
	// LIFE_ATHN [생활용품]안전인증
	// LIFE_CFM [생활용품]안전확인
	// ELC_SUPS [전기용품]공급자적합성확인
	// ELC_ATHN [전기용품]안전인증
	// ELC_CFM [전기용품]안전확인
	// LIFE_STD [생활용품]안전기준준수
	// CHEM_LIFE [화학제품] 생활화학제품 안전기준적합확인신고번호 / 승인번호
	// CHEM_BIOC [화학제품] 살생물제품 승인번호
	// ETC 기타
	String sftyAthnTypCd;

	// 안전인증기관명
	String sftyAthnOrgnNm;

	// 안전인증번호
	String sftyAthnNo;

	public String getSftyAthnTypCd() {
		return sftyAthnTypCd;
	}

	public void setSftyAthnTypCd(String sftyAthnTypCd) {
		this.sftyAthnTypCd = sftyAthnTypCd;
	}

	public String getSftyAthnOrgnNm() {
		return sftyAthnOrgnNm;
	}

	public void setSftyAthnOrgnNm(String sftyAthnOrgnNm) {
		this.sftyAthnOrgnNm = sftyAthnOrgnNm;
	}

	public String getSftyAthnNo() {
		return sftyAthnNo;
	}

	public void setSftyAthnNo(String sftyAthnNo) {
		this.sftyAthnNo = sftyAthnNo;
	}

	@Override
	public String toString() {
		return "SftyAthn [sftyAthnTypCd=" + sftyAthnTypCd + ", sftyAthnOrgnNm=" + sftyAthnOrgnNm + ", sftyAthnNo="
				+ sftyAthnNo + "]";
	}

}
