package com.cware.netshopping.pawemp.counsel.model;

import java.sql.Timestamp;

/**
 * 공지사항 조회 아이템 
 */
public class NoticeData {
	
	private String noticeSeq;
	private String paCode;
	private String paType;
	private String paTitle;
	private String paContents;
	private Timestamp paRegDate;
	private String insertId;
	private Timestamp insertDate;
	
	public String getNoticeSeq() {
		return noticeSeq;
	}
	public void setNoticeSeq(String noticeSeq) {
		this.noticeSeq = noticeSeq;
	}
	public String getPaType() {
		return paType;
	}
	public void setPaType(String paType) {
		this.paType = paType;
	}
	public String getPaTitle() {
		return paTitle;
	}
	public void setPaTitle(String paTitle) {
		this.paTitle = paTitle;
	}
	public String getPaContents() {
		return paContents;
	}
	public void setPaContents(String paContents) {
		this.paContents = paContents;
	}
	public String getInsertId() {
		return insertId;
	}
	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}
	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
	public Timestamp getPaRegDate() {
		return paRegDate;
	}
	public void setPaRegDate(Timestamp paRegDate) {
		this.paRegDate = paRegDate;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
}
