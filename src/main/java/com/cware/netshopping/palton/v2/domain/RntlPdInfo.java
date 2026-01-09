package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RntlPdInfo {
	Integer dutyUsePrd; // 의무사용기간
	Long instCst; // 설치비용
	Long regCst; // 등록비용
	Long cnsmrSlPrc; // 소비자판매가
	Long mmRntlCst; // 월렌탈비용

	// 부가세포함여부 [Y, N]
	// 디폴트 : Y
	String vatInclYn;

	public Integer getDutyUsePrd() {
		return dutyUsePrd;
	}

	public void setDutyUsePrd(Integer dutyUsePrd) {
		this.dutyUsePrd = dutyUsePrd;
	}

	public Long getInstCst() {
		return instCst;
	}

	public void setInstCst(Long instCst) {
		this.instCst = instCst;
	}

	public Long getRegCst() {
		return regCst;
	}

	public void setRegCst(Long regCst) {
		this.regCst = regCst;
	}

	public Long getCnsmrSlPrc() {
		return cnsmrSlPrc;
	}

	public void setCnsmrSlPrc(Long cnsmrSlPrc) {
		this.cnsmrSlPrc = cnsmrSlPrc;
	}

	public Long getMmRntlCst() {
		return mmRntlCst;
	}

	public void setMmRntlCst(Long mmRntlCst) {
		this.mmRntlCst = mmRntlCst;
	}

	public String getVatInclYn() {
		return vatInclYn;
	}

	public void setVatInclYn(String vatInclYn) {
		this.vatInclYn = vatInclYn;
	}

	@Override
	public String toString() {
		return "RntlPdInfo [dutyUsePrd=" + dutyUsePrd + ", instCst=" + instCst + ", regCst=" + regCst + ", cnsmrSlPrc="
				+ cnsmrSlPrc + ", mmRntlCst=" + mmRntlCst + ", vatInclYn=" + vatInclYn + "]";
	}

}
