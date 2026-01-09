package com.cware.netshopping.pawemp.counsel.model;

/**
 * 위메프 공지사항 조회 결과
 */
public class GetNoticeResponse {
	
	private String type;
	private String title;
	private String contents;
	private String regDate;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
}

