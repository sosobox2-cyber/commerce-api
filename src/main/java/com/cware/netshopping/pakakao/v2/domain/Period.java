package com.cware.netshopping.pakakao.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Period {

	// 시작시간
	// yyyyMMddHHmmss
	String from;

	// 종료시간
	// yyyyMMddHHmmss
	String to;

	@Override
	public String toString() {
		return "Period [from=" + from + ", to=" + to + "]";
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
