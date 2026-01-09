package com.cware.netshopping.pawemp.common.model;

import java.util.List;

public class GetNoticeResponse {
	private long groupNoticeNo;
	private String groupNoticeNoName;
	private List<NoticeList> noticeList;
	
	
	
	public long getGroupNoticeNo() {
		return groupNoticeNo;
	}



	public void setGroupNoticeNo(long groupNoticeNo) {
		this.groupNoticeNo = groupNoticeNo;
	}



	public String getGroupNoticeNoName() {
		return groupNoticeNoName;
	}



	public void setGroupNoticeNoName(String groupNoticeNoName) {
		this.groupNoticeNoName = groupNoticeNoName;
	}



	public List<NoticeList> getNoticeList() {
		return noticeList;
	}



	public void setNoticeList(List<NoticeList> noticeList) {
		this.noticeList = noticeList;
	}
	
	

}
