package com.cware.netshopping.palton.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Itm {
	String sitmNo; // 판매자단품번호
	String eitmNo; // 업체단품번호
	String sitmNm; // 판매자단품명

	// 판매상태코드 [공통코드 : SL_STAT_CD]
	// SALE 판매중
	// SOUT 품절
	String slStatCd;

	// 판매상태사유코드 [공통코드 : SL_STAT_RSN_CD]
	// SOUT_ADMR 관리자 수동품절처리
	// SOUT_ITM 모든단품품절로 상품 품절처리
	// SOUT_RSV 예약상품 품절처리
	// SOUT_STK 재고수량 0 품절처리
	// SOUT_LMT 한정수량 0 품절처리
	// SOUT_THDY 명절 품절예약
	String slStatRsnCd;

	String dpYn; // 전시여부 [Y, N]

	// 정렬순번 (Front에 노출되는 정렬되는 조건은 아니며 Front 노출 순서 조정 필요 할 경우 optSrtLst 필드로 등록해야 함)
	Integer sortSeq;

	// 단품속성목록
	// 판매자단품여부가 Y인 경우 필수값
	List<ItmOpt> itmOptLst;

	// 단품이미지목록
	// 단품당 하나 이상의 이미지를 등록하여야 한다.
	// 단품당 최대 10개의 이미지를 등록할 수 있다.
	List<ItmImg> itmImgLst;

	List<Clrchip> clrchipLst; // 컬러칩이미지목록

	PdUtStdInfo pdUtStdInfo; // 상품단위기준정보

	Long slPrc; // 판매가

	// 단품할인유형코드 [공통코드 : ITM_DC_TYP_CD]
	// GNRL 일반가
	// DC 세일가
	String itmDcTypCd;

	// 재고수량
	// 재고관리여부가 Y인 경우에는 필수값
	Long stkQty;

	public String getSitmNo() {
		return sitmNo;
	}

	public void setSitmNo(String sitmNo) {
		this.sitmNo = sitmNo;
	}

	public String getEitmNo() {
		return eitmNo;
	}

	public void setEitmNo(String eitmNo) {
		this.eitmNo = eitmNo;
	}

	public String getSitmNm() {
		return sitmNm;
	}

	public void setSitmNm(String sitmNm) {
		this.sitmNm = sitmNm;
	}

	public String getSlStatCd() {
		return slStatCd;
	}

	public void setSlStatCd(String slStatCd) {
		this.slStatCd = slStatCd;
	}

	public String getSlStatRsnCd() {
		return slStatRsnCd;
	}

	public void setSlStatRsnCd(String slStatRsnCd) {
		this.slStatRsnCd = slStatRsnCd;
	}

	public String getDpYn() {
		return dpYn;
	}

	public void setDpYn(String dpYn) {
		this.dpYn = dpYn;
	}

	public List<ItmOpt> getItmOptLst() {
		return itmOptLst;
	}

	public void setItmOptLst(List<ItmOpt> itmOptLst) {
		this.itmOptLst = itmOptLst;
	}

	public List<ItmImg> getItmImgLst() {
		return itmImgLst;
	}

	public void setItmImgLst(List<ItmImg> itmImgLst) {
		this.itmImgLst = itmImgLst;
	}

	public List<Clrchip> getClrchipLst() {
		return clrchipLst;
	}

	public void setClrchipLst(List<Clrchip> clrchipLst) {
		this.clrchipLst = clrchipLst;
	}

	public PdUtStdInfo getPdUtStdInfo() {
		return pdUtStdInfo;
	}

	public void setPdUtStdInfo(PdUtStdInfo pdUtStdInfo) {
		this.pdUtStdInfo = pdUtStdInfo;
	}

	public Long getSlPrc() {
		return slPrc;
	}

	public void setSlPrc(Long slPrc) {
		this.slPrc = slPrc;
	}

	public String getItmDcTypCd() {
		return itmDcTypCd;
	}

	public void setItmDcTypCd(String itmDcTypCd) {
		this.itmDcTypCd = itmDcTypCd;
	}

	public Long getStkQty() {
		return stkQty;
	}

	public void setStkQty(Long stkQty) {
		this.stkQty = stkQty;
	}

	public Integer getSortSeq() {
		return sortSeq;
	}

	public void setSortSeq(Integer sortSeq) {
		this.sortSeq = sortSeq;
	}

	@Override
	public String toString() {
		return "Itm [sitmNo=" + sitmNo + ", eitmNo=" + eitmNo + ", sitmNm=" + sitmNm + ", slStatCd=" + slStatCd
				+ ", slStatRsnCd=" + slStatRsnCd + ", dpYn=" + dpYn + ", itmOptLst=" + itmOptLst + ", itmImgLst="
				+ itmImgLst + ", clrchipLst=" + clrchipLst + ", pdUtStdInfo=" + pdUtStdInfo + ", slPrc=" + slPrc
				+ ", itmDcTypCd=" + itmDcTypCd + ", stkQty=" + stkQty + "]";
	}

}
