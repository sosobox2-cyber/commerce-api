package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RsvSaleInfo {
	String aplStrtDt; // 예약판매 시작일 (YYYYMMDD)
	String aplEndDt; // 예약판매 종료일 (YYYYMMDD)
	String whoutStrtDt; // 출고 시작 일자 (YYYYMMDD)
	Integer rstctInvQty; // 예약 판매 수량

	public String getAplStrtDt() {
		return aplStrtDt;
	}

	public void setAplStrtDt(String aplStrtDt) {
		this.aplStrtDt = aplStrtDt;
	}

	public String getAplEndDt() {
		return aplEndDt;
	}

	public void setAplEndDt(String aplEndDt) {
		this.aplEndDt = aplEndDt;
	}

	public String getWhoutStrtDt() {
		return whoutStrtDt;
	}

	public void setWhoutStrtDt(String whoutStrtDt) {
		this.whoutStrtDt = whoutStrtDt;
	}

	public Integer getRstctInvQty() {
		return rstctInvQty;
	}

	public void setRstctInvQty(Integer rstctInvQty) {
		this.rstctInvQty = rstctInvQty;
	}

	@Override
	public String toString() {
		return "RsvSaleInfo [aplStrtDt=" + aplStrtDt + ", aplEndDt=" + aplEndDt + ", whoutStrtDt=" + whoutStrtDt
				+ ", rstctInvQty=" + rstctInvQty + "]";
	}

}
