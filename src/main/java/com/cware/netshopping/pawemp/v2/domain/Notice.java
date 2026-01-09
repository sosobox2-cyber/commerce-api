package com.cware.netshopping.pawemp.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Notice {

	// 항목번호 (필수) (기초데이터-상품정보고시 조회 API 참고)
	String noticeNo;
	// 설명 (필수) (최대 1000자 - 초과 시 1000자까지만 저장
	String description;

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Notice [noticeNo=" + noticeNo + ", description=" + description + "]";
	}

}
