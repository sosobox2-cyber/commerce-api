package com.cware.netshopping.palton.v2.domain;

import java.util.List;

public class DvCstSrPost {
	String returnCode; // 결과코드
	String message; // 결과메세지
	List<String> subMessages; // 응답 상세 하위 메세지 배열
	Integer dataCount; // 응답 데이터 객체 크기
	List<DvCstSr> data; // 응답 데이터

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getSubMessages() {
		return subMessages;
	}

	public void setSubMessages(List<String> subMessages) {
		this.subMessages = subMessages;
	}

	public Integer getDataCount() {
		return dataCount;
	}

	public void setDataCount(Integer dataCount) {
		this.dataCount = dataCount;
	}

	public List<DvCstSr> getData() {
		return data;
	}

	public void setData(List<DvCstSr> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "DvCstSrPost [returnCode=" + returnCode + ", message=" + message + ", subMessages=" + subMessages
				+ ", dataCount=" + dataCount + ", data=" + data + "]";
	}

}
