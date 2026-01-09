package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaNoticeTarget;

public class PaNoticeApplyVO extends PaNoticeTarget {
	private static final long serialVersionUID = 1L;
	
	private String procGb; 
	private Timestamp noticeBdate; 
	private Timestamp noticeEdate;  
	private String paGroupCode;  
	private String dateTime;
	
	
	public String getProcGb() {
		return procGb;
	}
	public void setProcGb(String procGb) {
		this.procGb = procGb;
	}	
	public Timestamp getnoticeBdate() {
		return noticeBdate;
	}
	public void setnoticeBdate(Timestamp noticeBdate) {
		this.noticeBdate = noticeBdate;
	}
	public Timestamp getnoticeEdate() {
		return noticeEdate;
	}
	public void setnoticeEdate(Timestamp noticeEdate) {
		this.noticeEdate = noticeEdate;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	
}
