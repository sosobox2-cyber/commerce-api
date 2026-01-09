package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AdtnPd {
	String adtnPdNo; // 추가상품번호
	String adtnPdNm; // 추가상품명
	String epdNo; // 업체상품번호
	Integer epsrPrirRnkg; // 노출우선순위
	Long slPrc; // 판매가
	Long stkQty; // 재고수량
	String useYn; // 사용여부 [Y, N]

	public String getAdtnPdNo() {
		return adtnPdNo;
	}

	public void setAdtnPdNo(String adtnPdNo) {
		this.adtnPdNo = adtnPdNo;
	}

	public String getAdtnPdNm() {
		return adtnPdNm;
	}

	public void setAdtnPdNm(String adtnPdNm) {
		this.adtnPdNm = adtnPdNm;
	}

	public String getEpdNo() {
		return epdNo;
	}

	public void setEpdNo(String epdNo) {
		this.epdNo = epdNo;
	}

	public Integer getEpsrPrirRnkg() {
		return epsrPrirRnkg;
	}

	public void setEpsrPrirRnkg(Integer epsrPrirRnkg) {
		this.epsrPrirRnkg = epsrPrirRnkg;
	}

	public Long getSlPrc() {
		return slPrc;
	}

	public void setSlPrc(Long slPrc) {
		this.slPrc = slPrc;
	}

	public Long getStkQty() {
		return stkQty;
	}

	public void setStkQty(Long stkQty) {
		this.stkQty = stkQty;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	@Override
	public String toString() {
		return "AdtnPd [adtnPdNo=" + adtnPdNo + ", adtnPdNm=" + adtnPdNm + ", epdNo=" + epdNo + ", epsrPrirRnkg="
				+ epsrPrirRnkg + ", slPrc=" + slPrc + ", stkQty=" + stkQty + ", useYn=" + useYn + "]";
	}

}
