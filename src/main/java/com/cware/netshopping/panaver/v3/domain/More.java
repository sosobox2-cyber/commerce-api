package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class More {
	
	// 최대 응답 개수 제한 때문에 제공하지 못한 항목 중 첫 항목 일시
	private String moreFrom;
	// 같은 일시 항목의 구분 값
	private String moreSequence;
	
	public String getMoreFrom() {
		return moreFrom;
	}
	public void setMoreFrom(String moreFrom) {
		this.moreFrom = moreFrom;
	}
	public String getMoreSequence() {
		return moreSequence;
	}
	public void setMoreSequence(String moreSequence) {
		this.moreSequence = moreSequence;
	}
	
	@Override
	public String toString() {
		return "More [moreFrom=" + moreFrom + ", moreSequence=" + moreSequence + "]";
	}
}
