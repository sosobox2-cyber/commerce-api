package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PdAprvStatInfo {

	// 상품승인요청코드 [공통코드 : PD_APRV_REQ_CD]
	// APRV_REQ 승인요청
	// APRV_CMPT 승인완료
	// GVBK 반려
	// PD_REG 상품등록
	// NONE 해당없음
	String pdAprvReqCd;

	// 상품승인요청반려사유코드
	String pdAprvReqGvbkRsnCd;

	// 카테고리승인상태코드 [공통코드 : CAT_APRV_STAT_CD]
	// APRV_WT 승인대기
	// APRV_CMPT 승인완료
	// GVBK 반려
	// ADMR_TCTL 관리자이관
	// NONE 해당없음
	String catAprvStatCd;

	String catGvbkRsnCd; // 카테고리반려사유코드
	String catGvbkRsnCnts; // 카테고리반려사유내용

	// 상품정보승인상태코드 [공통코드 : PD_INFO_APRV_STAT_CD]
	// APRV_WT 승인대기
	// APRV_CMPT 승인완료
	// GVBK 반려
	// NONE 해당없음
	String pdInfoAprvStatCd;

	String pdInfoGvbkRsnCd; // 상품정보반려사유코드
	String pdInfoGvbkRsnCnts; // 상품정보반려사유내용
	String fnlAprvYn; // 최종승인여부

	public String getPdAprvReqCd() {
		return pdAprvReqCd;
	}

	public void setPdAprvReqCd(String pdAprvReqCd) {
		this.pdAprvReqCd = pdAprvReqCd;
	}

	public String getPdAprvReqGvbkRsnCd() {
		return pdAprvReqGvbkRsnCd;
	}

	public void setPdAprvReqGvbkRsnCd(String pdAprvReqGvbkRsnCd) {
		this.pdAprvReqGvbkRsnCd = pdAprvReqGvbkRsnCd;
	}

	public String getCatAprvStatCd() {
		return catAprvStatCd;
	}

	public void setCatAprvStatCd(String catAprvStatCd) {
		this.catAprvStatCd = catAprvStatCd;
	}

	public String getCatGvbkRsnCd() {
		return catGvbkRsnCd;
	}

	public void setCatGvbkRsnCd(String catGvbkRsnCd) {
		this.catGvbkRsnCd = catGvbkRsnCd;
	}

	public String getCatGvbkRsnCnts() {
		return catGvbkRsnCnts;
	}

	public void setCatGvbkRsnCnts(String catGvbkRsnCnts) {
		this.catGvbkRsnCnts = catGvbkRsnCnts;
	}

	public String getPdInfoAprvStatCd() {
		return pdInfoAprvStatCd;
	}

	public void setPdInfoAprvStatCd(String pdInfoAprvStatCd) {
		this.pdInfoAprvStatCd = pdInfoAprvStatCd;
	}

	public String getPdInfoGvbkRsnCd() {
		return pdInfoGvbkRsnCd;
	}

	public void setPdInfoGvbkRsnCd(String pdInfoGvbkRsnCd) {
		this.pdInfoGvbkRsnCd = pdInfoGvbkRsnCd;
	}

	public String getPdInfoGvbkRsnCnts() {
		return pdInfoGvbkRsnCnts;
	}

	public void setPdInfoGvbkRsnCnts(String pdInfoGvbkRsnCnts) {
		this.pdInfoGvbkRsnCnts = pdInfoGvbkRsnCnts;
	}

	public String getFnlAprvYn() {
		return fnlAprvYn;
	}

	public void setFnlAprvYn(String fnlAprvYn) {
		this.fnlAprvYn = fnlAprvYn;
	}

	@Override
	public String toString() {
		return "PdAprvStatInfo [pdAprvReqCd=" + pdAprvReqCd + ", pdAprvReqGvbkRsnCd=" + pdAprvReqGvbkRsnCd
				+ ", catAprvStatCd=" + catAprvStatCd + ", catGvbkRsnCd=" + catGvbkRsnCd + ", catGvbkRsnCnts="
				+ catGvbkRsnCnts + ", pdInfoAprvStatCd=" + pdInfoAprvStatCd + ", pdInfoGvbkRsnCd=" + pdInfoGvbkRsnCd
				+ ", pdInfoGvbkRsnCnts=" + pdInfoGvbkRsnCnts + ", fnlAprvYn=" + fnlAprvYn + "]";
	}

}
