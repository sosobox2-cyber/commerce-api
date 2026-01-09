package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AfterServiceInfo {
		
	// A/S 전화번호
	private String afterServiceTelephoneNumber;
	// A/S 안내
	private String  afterServiceGuideContent;
	
	
	public String getAfterServiceTelephoneNumber() {
		return afterServiceTelephoneNumber;
	}
	public void setAfterServiceTelephoneNumber(String afterServiceTelephoneNumber) {
		this.afterServiceTelephoneNumber = afterServiceTelephoneNumber;
	}
	public String getAfterServiceGuideContent() {
		return afterServiceGuideContent;
	}
	public void setAfterServiceGuideContent(String afterServiceGuideContent) {
		this.afterServiceGuideContent = afterServiceGuideContent;
	}

	@Override
	public String toString() {
		return "AfterServiceInfo [afterServiceTelephoneNumber=" + afterServiceTelephoneNumber +"afterServiceGuideContent=" + afterServiceGuideContent + "]";
	}

}
