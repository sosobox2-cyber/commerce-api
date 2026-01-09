package com.cware.netshopping.paintp.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 반품배송지
 * 
 * http://www.interpark.com/openapi/site/APIDelvInsertSpec.jsp
 * http://www.interpark.com/openapi/site/APIDelvUpdateSpec.jsp
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DelvItem {

	// 반품배송지번호(수정시 필수)
	private String entrDelvInfoNo;

	// 반품배송지관리명(필수)
	private String delvNm;

	// 반품배송지 우편번호(필수)
	private String delvZipcd;

	// 반품배송지 주소1(필수)
	private String delvAddr1;

	// 반품배송지 주소2(필수)
	private String delvAddr2;

	// 반품배송지 전화번호(필수)
	private String delvTelno;

	// 반품배송지 핸드폰번호(필수)
	private String delvHp;

	// 사용여부(수정시 필수) (Y : 사용 N : 사용안함)
	private String useYn;

	public String getEntrDelvInfoNo() {
		return entrDelvInfoNo;
	}

	public void setEntrDelvInfoNo(String entrDelvInfoNo) {
		this.entrDelvInfoNo = entrDelvInfoNo;
	}

	public String getDelvNm() {
		return delvNm;
	}

	public void setDelvNm(String delvNm) {
		this.delvNm = delvNm;
	}

	public String getDelvZipcd() {
		return delvZipcd;
	}

	public void setDelvZipcd(String delvZipcd) {
		this.delvZipcd = delvZipcd;
	}

	public String getDelvAddr1() {
		return delvAddr1;
	}

	public void setDelvAddr1(String delvAddr1) {
		this.delvAddr1 = delvAddr1;
	}

	public String getDelvAddr2() {
		return delvAddr2;
	}

	public void setDelvAddr2(String delvAddr2) {
		this.delvAddr2 = delvAddr2;
	}

	public String getDelvTelno() {
		return delvTelno;
	}

	public void setDelvTelno(String delvTelno) {
		this.delvTelno = delvTelno;
	}

	public String getDelvHp() {
		return delvHp;
	}

	public void setDelvHp(String delvHp) {
		this.delvHp = delvHp;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	@Override
	public String toString() {
		return "DelvItem [entrDelvInfoNo=" + entrDelvInfoNo + ", delvNm=" + delvNm + ", delvZipcd=" + delvZipcd
				+ ", delvAddr1=" + delvAddr1 + ", delvAddr2=" + delvAddr2 + ", delvTelno=" + delvTelno + ", delvHp="
				+ delvHp + ", useYn=" + useYn + "]";
	}

}
