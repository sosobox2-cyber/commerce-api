package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class VenAddrDelInfo extends VenAddr {

	String doroAddrId; // 도로명 주소 ID
	String jibunAddrId; // 지번 주소 ID

	// 승인결과코드 (commCd:I307)
	// 10 : 승인요청
	// 20 : 수정승인요청
	// 30 : 확인중
	// 40 : 승인
	// 50 : 거부
	// 88 : 실패
	// 99 : 비대상
	String aprvRstCd;

	// 연동결과코드 (commCd:I308)
	// 10 : 신청요청
	// 20 : 수정요청
	// 30 : 취소요청
	// 40 : 연동완료
	String lnkgRstCd;

	// 승인구분코드 (commCd:I309)
	// 10 : 일반
	// 20 : 강제승인
	String aprvDivCd;

	public String getDoroAddrId() {
		return doroAddrId;
	}

	public void setDoroAddrId(String doroAddrId) {
		this.doroAddrId = doroAddrId;
	}

	public String getJibunAddrId() {
		return jibunAddrId;
	}

	public void setJibunAddrId(String jibunAddrId) {
		this.jibunAddrId = jibunAddrId;
	}

	public String getAprvRstCd() {
		return aprvRstCd;
	}

	public void setAprvRstCd(String aprvRstCd) {
		this.aprvRstCd = aprvRstCd;
	}

	public String getLnkgRstCd() {
		return lnkgRstCd;
	}

	public void setLnkgRstCd(String lnkgRstCd) {
		this.lnkgRstCd = lnkgRstCd;
	}

	public String getAprvDivCd() {
		return aprvDivCd;
	}

	public void setAprvDivCd(String aprvDivCd) {
		this.aprvDivCd = aprvDivCd;
	}

	@Override
	public String toString() {
		return "VenAddrDelInfo [doroAddrId=" + doroAddrId + ", jibunAddrId=" + jibunAddrId + ", aprvRstCd=" + aprvRstCd
				+ ", lnkgRstCd=" + lnkgRstCd + ", aprvDivCd=" + aprvDivCd + "]";
	}

}
