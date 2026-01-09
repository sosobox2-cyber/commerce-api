package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaMonitering extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String moniteringSeq;
	private String checkGb;
	private String checkNo;
	private String checkTxt;
	private String goodsCode;
	private String paGoodsCode;
	
	private String procYn;
	private Timestamp procDate;
	private String procTxt;
	private String  remark1V;
	private long remark1N;
	
	private Timestamp insertDate;
	private String insertId;
	private Timestamp modifyDate;
	private String modifyId;
	
	
	
	
	public String getMoniteringSeq() {
		return moniteringSeq;
	}
	public void setMoniteringSeq(String moniteringSeq) {
		this.moniteringSeq = moniteringSeq;
	}
	public String getCheckGb() {
		return checkGb;
	}
	public void setCheckGb(String checkGb) {
		this.checkGb = checkGb;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public String getCheckTxt() {
		return checkTxt;
	}
	public void setCheckTxt(String checkTxt) {
		this.checkTxt = checkTxt;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getPaGoodsCode() {
		return paGoodsCode;
	}
	public void setPaGoodsCode(String paGoodsCode) {
		this.paGoodsCode = paGoodsCode;
	}
	public String getProcYn() {
		return procYn;
	}
	public void setProcYn(String procYn) {
		this.procYn = procYn;
	}
	public Timestamp getProcDate() {
		return procDate;
	}
	public void setProcDate(Timestamp procDate) {
		this.procDate = procDate;
	}
	public String getProcTxt() {
		return procTxt;
	}
	public void setProcTxt(String procTxt) {
		this.procTxt = procTxt;
	}
	public String getRemark1V() {
		return remark1V;
	}
	public void setRemark1V(String remark1v) {
		remark1V = remark1v;
	}
	public long getRemark1N() {
		return remark1N;
	}
	public void setRemark1N(long remark1n) {
		remark1N = remark1n;
	}
	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
	public String getInsertId() {
		return insertId;
	}
	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getModifyId() {
		return modifyId;
	}
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}

}
