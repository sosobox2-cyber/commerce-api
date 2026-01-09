package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "requestVenAddrInsert")
@XmlAccessorType(XmlAccessType.FIELD)
public class VenAddr {
	String grpAddrId; // 그룹 주소 ID
	String addrlcAntnmNm; // 주소지별칭명
	String zipcd; // 우편번호
	String doroAddrBasc; // 도로명 기본주소
	String doroAddrDtl; // 도로명 상세주소
	String jibunAddrBasc; // 지번 기본주소
	String jibunAddrDtl; // 지번 상세주소
	String bascAddrYn; // 기본주소여부
	String cnts; // 연락처 (format : XXXX-XXXX-XXXX)
	String delicoVenId; // 택배사 코드
	String delicoTrscNo; // 택배사 계약 코드

	// 택배사 업체코드
	// - "우체국택배"인 경우, 우체국 고객번호 필수 입력
	// - "CJ대한통운"인 경우, 발송고객코드(B코드) 사용 시 필수 입력 (발송고객코드 미사용 시 미입력)
	String goodsMngCd;
	Integer delicoContrAmt; // 계약요금 (택배사가 8대 택배사일 시 필수)

	public String getGrpAddrId() {
		return grpAddrId;
	}

	public void setGrpAddrId(String grpAddrId) {
		this.grpAddrId = grpAddrId;
	}

	public String getAddrlcAntnmNm() {
		return addrlcAntnmNm;
	}

	public void setAddrlcAntnmNm(String addrlcAntnmNm) {
		this.addrlcAntnmNm = addrlcAntnmNm;
	}

	public String getZipcd() {
		return zipcd;
	}

	public void setZipcd(String zipcd) {
		this.zipcd = zipcd;
	}

	public String getDoroAddrBasc() {
		return doroAddrBasc;
	}

	public void setDoroAddrBasc(String doroAddrBasc) {
		this.doroAddrBasc = doroAddrBasc;
	}

	public String getDoroAddrDtl() {
		return doroAddrDtl;
	}

	public void setDoroAddrDtl(String doroAddrDtl) {
		this.doroAddrDtl = doroAddrDtl;
	}

	public String getJibunAddrBasc() {
		return jibunAddrBasc;
	}

	public void setJibunAddrBasc(String jibunAddrBasc) {
		this.jibunAddrBasc = jibunAddrBasc;
	}

	public String getJibunAddrDtl() {
		return jibunAddrDtl;
	}

	public void setJibunAddrDtl(String jibunAddrDtl) {
		this.jibunAddrDtl = jibunAddrDtl;
	}

	public String getBascAddrYn() {
		return bascAddrYn;
	}

	public void setBascAddrYn(String bascAddrYn) {
		this.bascAddrYn = bascAddrYn;
	}

	public String getCnts() {
		return cnts;
	}

	public void setCnts(String cnts) {
		this.cnts = cnts;
	}

	public String getDelicoVenId() {
		return delicoVenId;
	}

	public void setDelicoVenId(String delicoVenId) {
		this.delicoVenId = delicoVenId;
	}

	public String getDelicoTrscNo() {
		return delicoTrscNo;
	}

	public void setDelicoTrscNo(String delicoTrscNo) {
		this.delicoTrscNo = delicoTrscNo;
	}

	public String getGoodsMngCd() {
		return goodsMngCd;
	}

	public void setGoodsMngCd(String goodsMngCd) {
		this.goodsMngCd = goodsMngCd;
	}

	public Integer getDelicoContrAmt() {
		return delicoContrAmt;
	}

	public void setDelicoContrAmt(Integer delicoContrAmt) {
		this.delicoContrAmt = delicoContrAmt;
	}

	@Override
	public String toString() {
		return "VenAddr [grpAddrId=" + grpAddrId + ", addrlcAntnmNm=" + addrlcAntnmNm + ", zipcd=" + zipcd
				+ ", doroAddrBasc=" + doroAddrBasc + ", doroAddrDtl=" + doroAddrDtl + ", jibunAddrBasc=" + jibunAddrBasc
				+ ", jibunAddrDtl=" + jibunAddrDtl + ", bascAddrYn=" + bascAddrYn + ", cnts=" + cnts + ", delicoVenId="
				+ delicoVenId + ", delicoTrscNo=" + delicoTrscNo + ", goodsMngCd=" + goodsMngCd + ", delicoContrAmt="
				+ delicoContrAmt + "]";
	}

}
