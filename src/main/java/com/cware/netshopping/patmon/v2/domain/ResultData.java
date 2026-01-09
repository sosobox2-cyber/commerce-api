package com.cware.netshopping.patmon.v2.domain;

public class ResultData {
	Boolean success; // 성공여부
	Boolean skip; // 프로세스를 진행했는지 여부. success의 부가정보
	String value; // 결과 값
	String message; // 실패시 메시지

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Boolean getSkip() {
		return skip;
	}

	public void setSkip(Boolean skip) {
		this.skip = skip;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ResultData [success=" + success + ", skip=" + skip + ", value=" + value + ", message=" + message + "]";
	}

}
