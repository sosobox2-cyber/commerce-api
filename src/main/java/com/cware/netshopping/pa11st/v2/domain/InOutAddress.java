package com.cware.netshopping.pa11st.v2.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "InOutAddress")
@XmlAccessorType(XmlAccessType.FIELD)
public class InOutAddress {

	// 주소명 (필수)
	private String addrNm;

	// 이름 (필수)
	private String rcvrNm;

	// 일반전화번호 (필수)
	// 입력형식 예시) 02-000-0000
	private String gnrlTlphnNo;

	// 휴대전화번호 (필수)
	// 입력형식 예시) 010-1111-2222
	private String prtblTlphnNo;

	// 건물관리번호
	// 건물관리번호 또는 관련 지번 순번 둘중에 하나는 필수
	private String buildMngNO;

	// 관련 지번 순번
	// 입력시 건물관리번호 빈값
	private String lnmAddrSeq;

	// 상세주소 (필수)
	// 최대 한글 100자 영/숫자 200자
	private String dtlsAddr;

	// 주소 구분
	// 기본값은 도로명. 도로명인 경우 01, 지번인 경우 02
	private String addrClfCd;

	// 주소 순번
	// 상품등록시 주소 순번으로 출고지, 반품/교환지를 정하실 수 있습니다.
	private String addrSeq;

	// 회원번호
	// 해외 통합 배송일경우는 통합셀러 회원번호를 필수로 넣어주셔야 합니다.
	private String memNo;

	// 2개이상 필수등록
	private List<AddrBasiDlvCst> addrBasiDlvCst;

	public String getAddrNm() {
		return addrNm;
	}

	public void setAddrNm(String addrNm) {
		this.addrNm = addrNm;
	}

	public String getRcvrNm() {
		return rcvrNm;
	}

	public void setRcvrNm(String rcvrNm) {
		this.rcvrNm = rcvrNm;
	}

	public String getGnrlTlphnNo() {
		return gnrlTlphnNo;
	}

	public void setGnrlTlphnNo(String gnrlTlphnNo) {
		this.gnrlTlphnNo = gnrlTlphnNo;
	}

	public String getPrtblTlphnNo() {
		return prtblTlphnNo;
	}

	public void setPrtblTlphnNo(String prtblTlphnNo) {
		this.prtblTlphnNo = prtblTlphnNo;
	}

	public String getBuildMngNO() {
		return buildMngNO;
	}

	public void setBuildMngNO(String buildMngNO) {
		this.buildMngNO = buildMngNO;
	}

	public String getLnmAddrSeq() {
		return lnmAddrSeq;
	}

	public void setLnmAddrSeq(String lnmAddrSeq) {
		this.lnmAddrSeq = lnmAddrSeq;
	}

	public String getDtlsAddr() {
		return dtlsAddr;
	}

	public void setDtlsAddr(String dtlsAddr) {
		this.dtlsAddr = dtlsAddr;
	}

	public String getAddrClfCd() {
		return addrClfCd;
	}

	public void setAddrClfCd(String addrClfCd) {
		this.addrClfCd = addrClfCd;
	}

	public String getAddrSeq() {
		return addrSeq;
	}

	public void setAddrSeq(String addrSeq) {
		this.addrSeq = addrSeq;
	}

	public String getMemNo() {
		return memNo;
	}

	public void setMemNo(String memNo) {
		this.memNo = memNo;
	}

	public List<AddrBasiDlvCst> getAddrBasiDlvCst() {
		return addrBasiDlvCst;
	}

	public void setAddrBasiDlvCst(List<AddrBasiDlvCst> addrBasiDlvCst) {
		this.addrBasiDlvCst = addrBasiDlvCst;
	}

	@Override
	public String toString() {
		return "InOutAddress [addrNm=" + addrNm + ", rcvrNm=" + rcvrNm + ", gnrlTlphnNo=" + gnrlTlphnNo
				+ ", prtblTlphnNo=" + prtblTlphnNo + ", buildMngNO=" + buildMngNO + ", lnmAddrSeq=" + lnmAddrSeq
				+ ", dtlsAddr=" + dtlsAddr + ", addrClfCd=" + addrClfCd + ", addrSeq=" + addrSeq + ", memNo=" + memNo
				+ ", addrBasiDlvCst=" + addrBasiDlvCst + "]";
	}

}
