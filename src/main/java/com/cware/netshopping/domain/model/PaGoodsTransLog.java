package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaGoodsTransLog extends AbstractModel {
	
	private static final long serialVersionUID = 1L;

	private String tranSeq; 
	private String goodsCode;
	private String paCode;
	private String itemNo;
	private String successYn;
	private String rtnCode;
	private String rtnMsg;
	private Timestamp procDate;
	private String procId;
	public String getTranSeq() {
		return tranSeq;
	}
	public void setTranSeq(String tranSeq) {
		this.tranSeq = tranSeq;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getSuccessYn() {
		return successYn;
	}
	public void setSuccessYn(String successYn) {
		this.successYn = successYn;
	}
	public String getRtnCode() {
		return rtnCode;
	}
	public void setRtnCode(String rtnCode) {
		this.rtnCode = rtnCode;
	}
	public String getRtnMsg() {
		return rtnMsg;
	}
	public void setRtnMsg(String rtnMsg) {
		this.rtnMsg = rtnMsg;
	}
	public Timestamp getProcDate() {
		return procDate;
	}
	public void setProcDate(Timestamp procDate) {
		this.procDate = procDate;
	}
	public String getProcId() {
		return procId;
	}
	public void setProcId(String procId) {
		this.procId = procId;
	}
	
	
	
}