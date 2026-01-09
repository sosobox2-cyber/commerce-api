package com.cware.netshopping.pagmkt.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OfficialNotice {

	private String officialNoticeNo;
	private List<OfficialNoticeDetail> details;

	public String getOfficialNoticeNo() {
		return officialNoticeNo;
	}

	public void setOfficialNoticeNo(String officialNoticeNo) {
		this.officialNoticeNo = officialNoticeNo;
	}

	public List<OfficialNoticeDetail> getDetails() {
		return details;
	}

	public void setDetails(List<OfficialNoticeDetail> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "OfficialNotice [officialNoticeNo=" + officialNoticeNo + ", details=" + details + "]";
	}

}
