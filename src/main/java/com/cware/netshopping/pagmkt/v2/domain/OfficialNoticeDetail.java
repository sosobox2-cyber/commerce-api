package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OfficialNoticeDetail {

	private String officialNoticeItemelementCode;
	private String value;
	private Boolean isExtraMark;

	public String getOfficialNoticeItemelementCode() {
		return officialNoticeItemelementCode;
	}

	public void setOfficialNoticeItemelementCode(String officialNoticeItemelementCode) {
		this.officialNoticeItemelementCode = officialNoticeItemelementCode;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@JsonProperty("isExtraMark")
	public Boolean isExtraMark() {
		return isExtraMark;
	}

	public void setExtraMark(Boolean isExtraMark) {
		this.isExtraMark = isExtraMark;
	}

	@Override
	public String toString() {
		return "OfficialNoticeDetail [officialNoticeItemelementCode=" + officialNoticeItemelementCode + ", value="
				+ value + ", isExtraMark=" + isExtraMark + "]";
	}

}
