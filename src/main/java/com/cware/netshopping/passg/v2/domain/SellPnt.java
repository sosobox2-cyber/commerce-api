package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SellPnt {
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
	String sellpntId; // 셀링 포인트 ID
	String sellpntNm; // 셀링 포인트 명
	String dispStrtDts; // 전시 시작 일시 (YYYYMMDD)
	String dispEndDts; // 전시 종료 일시 (YYYYMMDD)
	String useYn; // 사용여부

	public String getSiteNo() {
		return siteNo;
	}

	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}

	public String getSellpntId() {
		return sellpntId;
	}

	public void setSellpntId(String sellpntId) {
		this.sellpntId = sellpntId;
	}

	public String getSellpntNm() {
		return sellpntNm;
	}

	public void setSellpntNm(String sellpntNm) {
		this.sellpntNm = sellpntNm;
	}

	public String getDispStrtDts() {
		return dispStrtDts;
	}

	public void setDispStrtDts(String dispStrtDts) {
		this.dispStrtDts = dispStrtDts;
	}

	public String getDispEndDts() {
		return dispEndDts;
	}

	public void setDispEndDts(String dispEndDts) {
		this.dispEndDts = dispEndDts;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	@Override
	public String toString() {
		return "SellPnt [siteNo=" + siteNo + ", sellpntId=" + sellpntId + ", sellpntNm=" + sellpntNm + ", dispStrtDts="
				+ dispStrtDts + ", dispEndDts=" + dispEndDts + ", useYn=" + useYn + "]";
	}

}
