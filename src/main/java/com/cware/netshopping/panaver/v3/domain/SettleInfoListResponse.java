package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettleInfoListResponse {
	
	// 정산 내역 리스트
	@JsonProperty("elements")
	private List<SettleInfo> settleInfoInfoList;
	
	// 페이징 정보
	private Pagination pagination;
	
	// 오류코드
	@JsonProperty("code")
	private String errorCode;

	// 오류 메시지
	private String message;

	public List<SettleInfo> getSettleInfoInfoList() {
		return settleInfoInfoList;
	}

	public void setSettleInfoInfoList(List<SettleInfo> settleInfoInfoList) {
		this.settleInfoInfoList = settleInfoInfoList;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "SettleInfoListResponse [settleInfoInfoList=" + settleInfoInfoList + ", pagination=" + pagination
				+ ", errorCode=" + errorCode + ", message=" + message + "]";
	}
}
