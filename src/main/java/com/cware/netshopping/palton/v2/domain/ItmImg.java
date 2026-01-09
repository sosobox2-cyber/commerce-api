package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ItmImg {
	// 노출유형코드 [공통코드 : EPSR_TYP_CD]
	// IMG 이미지
	String epsrTypCd;

	// 노출유형상세코드 [공통코드 : EPSR_TYP_DTL_CD]
	// IMG_SQRE 노출유형:이미지 > 정사각형
	// IMG_LNTH 노출유형:이미지 > 세로형
	String epsrTypDtlCd;

	// 원본이미지파일명(경로명)
	// 파일명을 포함한 다운로드가 가능한 경로를 입력한다.
	// ex) http://abc.com/12/34/56/78_90.jpg
	String origImgFileNm;

	// 대표이미지여부 [Y, N]
	// 대표이미지는 하나만 설정 가능
	String rprtImgYn;

	public String getEpsrTypCd() {
		return epsrTypCd;
	}

	public void setEpsrTypCd(String epsrTypCd) {
		this.epsrTypCd = epsrTypCd;
	}

	public String getEpsrTypDtlCd() {
		return epsrTypDtlCd;
	}

	public void setEpsrTypDtlCd(String epsrTypDtlCd) {
		this.epsrTypDtlCd = epsrTypDtlCd;
	}

	public String getOrigImgFileNm() {
		return origImgFileNm;
	}

	public void setOrigImgFileNm(String origImgFileNm) {
		this.origImgFileNm = origImgFileNm;
	}

	public String getRprtImgYn() {
		return rprtImgYn;
	}

	public void setRprtImgYn(String rprtImgYn) {
		this.rprtImgYn = rprtImgYn;
	}

	@Override
	public String toString() {
		return "ItmImg [epsrTypCd=" + epsrTypCd + ", epsrTypDtlCd=" + epsrTypDtlCd + ", origImgFileNm=" + origImgFileNm
				+ ", rprtImgYn=" + rprtImgYn + "]";
	}

}
