package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaNoticeTarget extends AbstractModel {

    private static final long serialVersionUID = 1L;
    private String noticeNo;
    private String noticeSeq;
    private String goodsCode;
    private String targetGb;    
    
	public String getNoticeNo() {
		return noticeNo;
	}
	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}
	public String getNoticeSeq() {
		return noticeSeq;
	}
	public void setNoticeSeq(String noticeSeq) {
		this.noticeSeq = noticeSeq;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getTargetGb() {
		return targetGb;
	}
	public void setTargetGb(String targetGb) {
		this.targetGb = targetGb;
	}
	
}
