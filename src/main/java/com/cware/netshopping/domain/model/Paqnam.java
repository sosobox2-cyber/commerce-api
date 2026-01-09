package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Paqnam extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private String paCounselSeq;
    
    private String procGb;
    
    private String paCode;
    
    private String paCounselNo;
    
    private String counselSeq;
    
    private Timestamp counselDate;
    
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
    
    private String transYn;
    
    private Timestamp transDate;
    private String refNo;
    
    private String msgGb;
    
    private String token;
    
    private String paGroupCode;

    public String getPaCounselSeq() {
        return paCounselSeq;
    }

    public String getProcGb() {
        return procGb;
    }

    public String getPaCode() {
        return paCode;
    }

    public String getPaCounselNo() {
        return paCounselNo;
    }

    public String getCounselSeq() {
        return counselSeq;
    }

    public Timestamp getCounselDate() {
        return counselDate;
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

    public String getTransYn() {
        return transYn;
    }

    public Timestamp getTransDate() {
        return transDate;
    }

    public void setPaCounselSeq(String paCounselSeq) {
        this.paCounselSeq = paCounselSeq;
    }

    public void setProcGb(String procGb) {
        this.procGb = procGb;
    }

    public void setPaCode(String paCode) {
        this.paCode = paCode;
    }

    public void setPaCounselNo(String paCounselNo) {
        this.paCounselNo = paCounselNo;
    }

    public void setCounselSeq(String counselSeq) {
        this.counselSeq = counselSeq;
    }

    public void setCounselDate(Timestamp counselDate) {
        this.counselDate = counselDate;
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

    public void setTransYn(String transYn) {
        this.transYn = transYn;
    }

    public void setTransDate(Timestamp transDate) {
        this.transDate = transDate;
    }

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getMsgGb() {
		return msgGb;
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
