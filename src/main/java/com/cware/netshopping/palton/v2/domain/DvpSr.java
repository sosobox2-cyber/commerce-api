package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DvpSr {
	String dvpNo; // 'PLO'+배송지번호
	String dvpTypCd; // 배송지유형코드 [회수지:01, 출고지:02]
	String dvpNm; // 배송지명
	String zipNo; // 우편번호 (없는경우 '99999')
	String zipAddr; // 우편주소 (없는경우 '없음')
	String dtlAddr; // 상세주소 (없는경우 '-')
	String stnmZipNo; // 도로명우편번호 (없는경우 '99999')
	String stnmZipAddr; // 도로명우편주소 (없는경우 '없음')
	String stnmDtlAddr; // 도로명상세주소 (없는경우 '-')
	String rpbtrNm; // 담당자명
	String mphnNatnNoCd; // 휴대폰국가코드 [default:'82']
	String mphnNo; // 휴대폰번호
	String telNatnNoCd; // 연락처국가코드 [default:'82']
	String telNo; // 연락처
	String afflTrCd; // 상위거래처번호
	String afflLrtrCd; // 하위거래처번호
	String useYn; // 사용여부 [default:'Y']

	public String getDvpNo() {
		return dvpNo;
	}

	public void setDvpNo(String dvpNo) {
		this.dvpNo = dvpNo;
	}

	public String getDvpTypCd() {
		return dvpTypCd;
	}

	public void setDvpTypCd(String dvpTypCd) {
		this.dvpTypCd = dvpTypCd;
	}

	public String getDvpNm() {
		return dvpNm;
	}

	public void setDvpNm(String dvpNm) {
		this.dvpNm = dvpNm;
	}

	public String getZipNo() {
		return zipNo;
	}

	public void setZipNo(String zipNo) {
		this.zipNo = zipNo;
	}

	public String getZipAddr() {
		return zipAddr;
	}

	public void setZipAddr(String zipAddr) {
		this.zipAddr = zipAddr;
	}

	public String getDtlAddr() {
		return dtlAddr;
	}

	public void setDtlAddr(String dtlAddr) {
		this.dtlAddr = dtlAddr;
	}

	public String getStnmZipNo() {
		return stnmZipNo;
	}

	public void setStnmZipNo(String stnmZipNo) {
		this.stnmZipNo = stnmZipNo;
	}

	public String getStnmZipAddr() {
		return stnmZipAddr;
	}

	public void setStnmZipAddr(String stnmZipAddr) {
		this.stnmZipAddr = stnmZipAddr;
	}

	public String getStnmDtlAddr() {
		return stnmDtlAddr;
	}

	public void setStnmDtlAddr(String stnmDtlAddr) {
		this.stnmDtlAddr = stnmDtlAddr;
	}

	public String getRpbtrNm() {
		return rpbtrNm;
	}

	public void setRpbtrNm(String rpbtrNm) {
		this.rpbtrNm = rpbtrNm;
	}

	public String getMphnNatnNoCd() {
		return mphnNatnNoCd;
	}

	public void setMphnNatnNoCd(String mphnNatnNoCd) {
		this.mphnNatnNoCd = mphnNatnNoCd;
	}

	public String getMphnNo() {
		return mphnNo;
	}

	public void setMphnNo(String mphnNo) {
		this.mphnNo = mphnNo;
	}

	public String getTelNatnNoCd() {
		return telNatnNoCd;
	}

	public void setTelNatnNoCd(String telNatnNoCd) {
		this.telNatnNoCd = telNatnNoCd;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getAfflTrCd() {
		return afflTrCd;
	}

	public void setAfflTrCd(String afflTrCd) {
		this.afflTrCd = afflTrCd;
	}

	public String getAfflLrtrCd() {
		return afflLrtrCd;
	}

	public void setAfflLrtrCd(String afflLrtrCd) {
		this.afflLrtrCd = afflLrtrCd;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	@Override
	public String toString() {
		return "DvpSr [dvpNo=" + dvpNo + ", dvpTypCd=" + dvpTypCd + ", dvpNm=" + dvpNm + ", zipNo=" + zipNo
				+ ", zipAddr=" + zipAddr + ", dtlAddr=" + dtlAddr + ", stnmZipNo=" + stnmZipNo + ", stnmZipAddr="
				+ stnmZipAddr + ", stnmDtlAddr=" + stnmDtlAddr + ", rpbtrNm=" + rpbtrNm + ", mphnNatnNoCd="
				+ mphnNatnNoCd + ", mphnNo=" + mphnNo + ", telNatnNoCd=" + telNatnNoCd + ", telNo=" + telNo
				+ ", afflTrCd=" + afflTrCd + ", afflLrtrCd=" + afflLrtrCd + "]";
	}

}
