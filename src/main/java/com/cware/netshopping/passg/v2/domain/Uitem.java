package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Uitem {
	String tempUitemId; // 단품 ID (임시 번호)
	String uitemId; // 단품 ID
	String uitemOptnTypeNm1; // 단품 옵션 유형명 1
	String uitemOptnNm1; // 단품 옵션 명 1
	String uitemOptnTypeNm2; // 단품 옵션 유형명 2
	String uitemOptnNm2; // 단품 옵션 명 2
	String uitemOptnTypeNm3; // 단품 옵션 유형명 3
	String uitemOptnNm3; // 단품 옵션 명 3
	String uitemOptnTypeNm4; // 단품 옵션 유형명 4
	String uitemOptnNm4; // 단품 옵션 명 4
	String uitemOptnTypeNm5; // 단품 옵션 유형명 5
	String uitemOptnNm5; // 단품 옵션 명 5
	Integer baseInvQty; // 재고 수량
	Integer rstctInvQty; // 예약 판매 수량
	String useYn; // 사용여부

	// 판매 상태 코드
	// 20 : 판매중
	// 80 : 일시판매중지
	String sellStatCd;

	public String getTempUitemId() {
		return tempUitemId;
	}

	public void setTempUitemId(String tempUitemId) {
		this.tempUitemId = tempUitemId;
	}

	public String getUitemId() {
		return uitemId;
	}

	public void setUitemId(String uitemId) {
		this.uitemId = uitemId;
	}

	public String getUitemOptnTypeNm1() {
		return uitemOptnTypeNm1;
	}

	public void setUitemOptnTypeNm1(String uitemOptnTypeNm1) {
		this.uitemOptnTypeNm1 = uitemOptnTypeNm1;
	}

	public String getUitemOptnNm1() {
		return uitemOptnNm1;
	}

	public void setUitemOptnNm1(String uitemOptnNm1) {
		this.uitemOptnNm1 = uitemOptnNm1;
	}

	public String getUitemOptnTypeNm2() {
		return uitemOptnTypeNm2;
	}

	public void setUitemOptnTypeNm2(String uitemOptnTypeNm2) {
		this.uitemOptnTypeNm2 = uitemOptnTypeNm2;
	}

	public String getUitemOptnNm2() {
		return uitemOptnNm2;
	}

	public void setUitemOptnNm2(String uitemOptnNm2) {
		this.uitemOptnNm2 = uitemOptnNm2;
	}

	public String getUitemOptnTypeNm3() {
		return uitemOptnTypeNm3;
	}

	public void setUitemOptnTypeNm3(String uitemOptnTypeNm3) {
		this.uitemOptnTypeNm3 = uitemOptnTypeNm3;
	}

	public String getUitemOptnNm3() {
		return uitemOptnNm3;
	}

	public void setUitemOptnNm3(String uitemOptnNm3) {
		this.uitemOptnNm3 = uitemOptnNm3;
	}

	public String getUitemOptnTypeNm4() {
		return uitemOptnTypeNm4;
	}

	public void setUitemOptnTypeNm4(String uitemOptnTypeNm4) {
		this.uitemOptnTypeNm4 = uitemOptnTypeNm4;
	}

	public String getUitemOptnNm4() {
		return uitemOptnNm4;
	}

	public void setUitemOptnNm4(String uitemOptnNm4) {
		this.uitemOptnNm4 = uitemOptnNm4;
	}

	public String getUitemOptnTypeNm5() {
		return uitemOptnTypeNm5;
	}

	public void setUitemOptnTypeNm5(String uitemOptnTypeNm5) {
		this.uitemOptnTypeNm5 = uitemOptnTypeNm5;
	}

	public String getUitemOptnNm5() {
		return uitemOptnNm5;
	}

	public void setUitemOptnNm5(String uitemOptnNm5) {
		this.uitemOptnNm5 = uitemOptnNm5;
	}

	public Integer getBaseInvQty() {
		return baseInvQty;
	}

	public void setBaseInvQty(Integer baseInvQty) {
		this.baseInvQty = baseInvQty;
	}

	public Integer getRstctInvQty() {
		return rstctInvQty;
	}

	public void setRstctInvQty(Integer rstctInvQty) {
		this.rstctInvQty = rstctInvQty;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getSellStatCd() {
		return sellStatCd;
	}

	public void setSellStatCd(String sellStatCd) {
		this.sellStatCd = sellStatCd;
	}

	@Override
	public String toString() {
		return "Uitem [uitemId=" + uitemId + ", uitemOptnTypeNm1=" + uitemOptnTypeNm1 + ", uitemOptnNm1=" + uitemOptnNm1
				+ ", uitemOptnTypeNm2=" + uitemOptnTypeNm2 + ", uitemOptnNm2=" + uitemOptnNm2 + ", uitemOptnTypeNm3="
				+ uitemOptnTypeNm3 + ", uitemOptnNm3=" + uitemOptnNm3 + ", uitemOptnTypeNm4=" + uitemOptnTypeNm4
				+ ", uitemOptnNm4=" + uitemOptnNm4 + ", uitemOptnTypeNm5=" + uitemOptnTypeNm5 + ", uitemOptnNm5="
				+ uitemOptnNm5 + ", baseInvQty=" + baseInvQty + ", rstctInvQty=" + rstctInvQty + ", useYn=" + useYn
				+ ", sellStatCd=" + sellStatCd + "]";
	}

}
