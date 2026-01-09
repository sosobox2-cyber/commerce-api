package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DispCtg {
	// 사이트 번호
	// 6001 : 이마트몰
	// 6002 : 트레이더스몰
	// 6003 : 분스몰
	// 6004 : 신세계몰
	// 6005 : SSG.COM
	// 6009 : 신세계백화점몰
	// 6200 : 신세계TV쇼핑몰
	// 7013 : TRIP
	String siteNo;
	String dispCtgId; // 전시 카테고리 ID

	public String getSiteNo() {
		return siteNo;
	}

	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}

	public String getDispCtgId() {
		return dispCtgId;
	}

	public void setDispCtgId(String dispCtgId) {
		this.dispCtgId = dispCtgId;
	}

	@Override
	public String toString() {
		return "DispCtg [siteNo=" + siteNo + ", dispCtgId=" + dispCtgId + "]";
	}

}
