package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Site {
	// 사이트 번호
	// 6001 : 이마트몰
	// 6002 : 트레이더스몰
	// 6003 : 분스몰
	// 6004 : 신세계몰
	// 6009 : 신세계백화점몰
	// 6200 : 신세계TV쇼핑몰
	// 7013 : TRIP
	String siteNo;

	// 판매 상태 코드
	// 20 : 판매중
	// 80 : 일시판매중지
	String sellStatCd;

	public String getSiteNo() {
		return siteNo;
	}

	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}

	public String getSellStatCd() {
		return sellStatCd;
	}

	public void setSellStatCd(String sellStatCd) {
		this.sellStatCd = sellStatCd;
	}

	@Override
	public String toString() {
		return "Site [siteNo=" + siteNo + ", sellStatCd=" + sellStatCd + "]";
	}

}
