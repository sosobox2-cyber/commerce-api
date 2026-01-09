package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SndBgtDdInfo {
	String nldySndCloseTm; // 평일 발송마감시간 [HH24MI ex) 1000]
	String satSndPsbYn; // 토요일 발송가능여부 [Y, N]

	// 토요일 발송마감시간 [HH24MI ex) 1000]
	// 토요일 발송 가능여부 Y인 경우 필수
	String satSndCloseTm;

	public String getNldySndCloseTm() {
		return nldySndCloseTm;
	}

	public void setNldySndCloseTm(String nldySndCloseTm) {
		this.nldySndCloseTm = nldySndCloseTm;
	}

	public String getSatSndPsbYn() {
		return satSndPsbYn;
	}

	public void setSatSndPsbYn(String satSndPsbYn) {
		this.satSndPsbYn = satSndPsbYn;
	}

	public String getSatSndCloseTm() {
		return satSndCloseTm;
	}

	public void setSatSndCloseTm(String satSndCloseTm) {
		this.satSndCloseTm = satSndCloseTm;
	}

	@Override
	public String toString() {
		return "SndBgtDdInfo [nldySndCloseTm=" + nldySndCloseTm + ", satSndPsbYn=" + satSndPsbYn + ", satSndCloseTm="
				+ satSndCloseTm + "]";
	}

}
