package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Paqnamoment extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private String paCode;
    private String paCounselNo;
    private Timestamp counselDate;
    private String title;
    private String questComment;
    private String answerComment;
    private String counselGb;
    private String paGoodsCode;
    private String paGoodsDtCode;
    private String paOrderNo;
    private String orderYn;
    private String displayYn;
    private String paCustNo;
    private String custName;
    private String custTel;
    private Timestamp receiptDate;
    private String msgGb;
    private String token;
    private String paGroupCode;
    
    public String getPaCode() {
        return paCode;
    }
    public String getPaCounselNo() {
        return paCounselNo;
    }
    public Timestamp getCounselDate() {
        return counselDate;
    }
    public String getTitle() {
        return title;
    }
    public String getQuestComment() {
        return questComment;
    }
    public String getAnswerComment() {
        return answerComment;
    }
    public String getCounselGb() {
        return counselGb;
    }
    public String getPaGoodsCode() {
        return paGoodsCode;
    }
    public String getPaGoodsDtCode() {
        return paGoodsDtCode;
    }
    public String getPaOrderNo() {
        return paOrderNo;
    }
    public String getOrderYn() {
        return orderYn;
    }
    public String getDisplayYn() {
        return displayYn;
    }
    public String getPaCustNo() {
        return paCustNo;
    }
    public String getCustName() {
        return custName;
    }
    public String getCustTel() {
        return custTel;
    }
    public Timestamp getReceiptDate() {
        return receiptDate;
    }
    public String getMsgGb() {
		return msgGb;
	}
    public void setPaCode(String paCode) {
        this.paCode = paCode;
    }
    public void setPaCounselNo(String paCounselNo) {
        this.paCounselNo = paCounselNo;
    }
    public void setCounselDate(Timestamp counselDate) {
        this.counselDate = counselDate;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setQuestComment(String questComment) {
        this.questComment = questComment;
    }
    public void setAnswerComment(String answerComment) {
        this.answerComment = answerComment;
    }
    public void setCounselGb(String counselGb) {
        this.counselGb = counselGb;
    }
    public void setPaGoodsCode(String paGoodsCode) {
        this.paGoodsCode = paGoodsCode;
    }
    public void setPaGoodsDtCode(String paGoodsDtCode) {
        this.paGoodsDtCode = paGoodsDtCode;
    }
    public void setPaOrderNo(String paOrderNo) {
        this.paOrderNo = paOrderNo;
    }
    public void setOrderYn(String orderYn) {
        this.orderYn = orderYn;
    }
    public void setDisplayYn(String displayYn) {
        this.displayYn = displayYn;
    }
    public void setPaCustNo(String paCustNo) {
        this.paCustNo = paCustNo;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }
    public void setCustTel(String custTel) {
        this.custTel = custTel;
    }
    public void setReceiptDate(Timestamp receiptDate) {
        this.receiptDate = receiptDate;
    }
	public void setMsgGb(String msgGb) {
		this.msgGb = msgGb;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
    
}
