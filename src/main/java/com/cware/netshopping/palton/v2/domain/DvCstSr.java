package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DvCstSr {
	String dvCstPolNo; // 배송비정책번호
	String aplyStrtDt; // 적용시작일자
	String aplyEndDt; // 적용종료일자
	String ctrtTypCd; // 계약유형코드[중계:A, 위탁:B, 판매분매입:C, 직매입:D]
	String dvTypCd; // 배송유형코드 [직접배송:DRECT] 직접배송 default
	String dvProcTypCd; // 배송처리유형코드 [업체배송:LO_ENTP] 업체배송 default
	String dvCstTypCd; // 배송비유형코드 [배송비 : DV_CST, 추가배송비 : ADTN_DV_CST]
	String dvCstDvsCd; // 배송비구분코드 [유료:A, 무료:B, 조건부:C] (배송비유형코드가 '추가배송비' 인 경우 조건부 선택 불가)
	Double dvCst; // 배송비
	Double rcst; // 반품비
	String rtngXchgDvCstImpsMthdCd; // 반품교환배송비부과방법코드 [default:'N']
	Double cndlFreeStdAmt; // 조건부무료기준금액 (배송비유형코드가 '배송비' 인 경우 필수)
	Double inrmAdtnDvCst; // 도서산간추가배송비 (배송비유형코드가 '추가배송비' 인 경우 필수)
	Double jejuAdtnDvCst; // 제주도추가배송비 (배송비유형코드가 '추가배송비' 인 경우 필수)
	String afflTrCd; // 상위거래처번호
	String afflLrtrCd; // 하위거래처번호
	String useYn; // 사용여부 [default:'Y']

	public String getDvCstPolNo() {
		return dvCstPolNo;
	}

	public void setDvCstPolNo(String dvCstPolNo) {
		this.dvCstPolNo = dvCstPolNo;
	}

	public String getAplyStrtDt() {
		return aplyStrtDt;
	}

	public void setAplyStrtDt(String aplyStrtDt) {
		this.aplyStrtDt = aplyStrtDt;
	}

	public String getAplyEndDt() {
		return aplyEndDt;
	}

	public void setAplyEndDt(String aplyEndDt) {
		this.aplyEndDt = aplyEndDt;
	}

	public String getCtrtTypCd() {
		return ctrtTypCd;
	}

	public void setCtrtTypCd(String ctrtTypCd) {
		this.ctrtTypCd = ctrtTypCd;
	}

	public String getDvTypCd() {
		return dvTypCd;
	}

	public void setDvTypCd(String dvTypCd) {
		this.dvTypCd = dvTypCd;
	}

	public String getDvProcTypCd() {
		return dvProcTypCd;
	}

	public void setDvProcTypCd(String dvProcTypCd) {
		this.dvProcTypCd = dvProcTypCd;
	}

	public String getDvCstTypCd() {
		return dvCstTypCd;
	}

	public void setDvCstTypCd(String dvCstTypCd) {
		this.dvCstTypCd = dvCstTypCd;
	}

	public String getDvCstDvsCd() {
		return dvCstDvsCd;
	}

	public void setDvCstDvsCd(String dvCstDvsCd) {
		this.dvCstDvsCd = dvCstDvsCd;
	}

	public Double getDvCst() {
		return dvCst;
	}

	public void setDvCst(Double dvCst) {
		this.dvCst = dvCst;
	}

	public Double getRcst() {
		return rcst;
	}

	public void setRcst(Double rcst) {
		this.rcst = rcst;
	}

	public String getRtngXchgDvCstImpsMthdCd() {
		return rtngXchgDvCstImpsMthdCd;
	}

	public void setRtngXchgDvCstImpsMthdCd(String rtngXchgDvCstImpsMthdCd) {
		this.rtngXchgDvCstImpsMthdCd = rtngXchgDvCstImpsMthdCd;
	}

	public Double getCndlFreeStdAmt() {
		return cndlFreeStdAmt;
	}

	public void setCndlFreeStdAmt(Double cndlFreeStdAmt) {
		this.cndlFreeStdAmt = cndlFreeStdAmt;
	}

	public Double getInrmAdtnDvCst() {
		return inrmAdtnDvCst;
	}

	public void setInrmAdtnDvCst(Double inrmAdtnDvCst) {
		this.inrmAdtnDvCst = inrmAdtnDvCst;
	}

	public Double getJejuAdtnDvCst() {
		return jejuAdtnDvCst;
	}

	public void setJejuAdtnDvCst(Double jejuAdtnDvCst) {
		this.jejuAdtnDvCst = jejuAdtnDvCst;
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
		return "DvCstSr [dvCstPolNo=" + dvCstPolNo + ", aplyStrtDt=" + aplyStrtDt + ", aplyEndDt=" + aplyEndDt
				+ ", ctrtTypCd=" + ctrtTypCd + ", dvTypCd=" + dvTypCd + ", dvProcTypCd=" + dvProcTypCd + ", dvCstTypCd="
				+ dvCstTypCd + ", dvCstDvsCd=" + dvCstDvsCd + ", dvCst=" + dvCst + ", rcst=" + rcst
				+ ", rtngXchgDvCstImpsMthdCd=" + rtngXchgDvCstImpsMthdCd + ", cndlFreeStdAmt=" + cndlFreeStdAmt
				+ ", inrmAdtnDvCst=" + inrmAdtnDvCst + ", jejuAdtnDvCst=" + jejuAdtnDvCst + ", afflTrCd=" + afflTrCd
				+ ", afflLrtrCd=" + afflLrtrCd + ", useYn=" + useYn + "]";
	}

}
