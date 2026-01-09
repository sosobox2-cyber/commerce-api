package com.cware.netshopping.pawemp.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NoticeGroup {

	// 상품에 저장된 상품정보고시 번호
	// (입력안하면 등록, 입력하면 수정 - 상품조회API로 확인가능)
	String productGroupNoticeNo;
	// 정책번호 (필수) (기초데이터-상품정보고시 조회 API 참고)
	String groupNoticeNo;
	// 항목정보 (필수) (기초데이터-상품정보고시 조회 API 참고
	List<Notice> noticeList;

	public String getProductGroupNoticeNo() {
		return productGroupNoticeNo;
	}

	public void setProductGroupNoticeNo(String productGroupNoticeNo) {
		this.productGroupNoticeNo = productGroupNoticeNo;
	}

	public String getGroupNoticeNo() {
		return groupNoticeNo;
	}

	public void setGroupNoticeNo(String groupNoticeNo) {
		this.groupNoticeNo = groupNoticeNo;
	}

	public List<Notice> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<Notice> noticeList) {
		this.noticeList = noticeList;
	}

	@Override
	public String toString() {
		return "NoticeGroup [productGroupNoticeNo=" + productGroupNoticeNo + ", groupNoticeNo=" + groupNoticeNo
				+ ", noticeList=" + noticeList + "]";
	}

}
