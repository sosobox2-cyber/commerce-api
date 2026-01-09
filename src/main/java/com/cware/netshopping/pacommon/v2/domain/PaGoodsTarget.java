package com.cware.netshopping.pacommon.v2.domain;

import java.sql.Timestamp;

public class PaGoodsTarget {

	private String paGroupCode; // 대표제휴사코드 O500
	private String paCode; // 제휴사코드 O501
	private String goodsCode;
	private String paGoodsCode; // 제휴사상품코드
	private String paSaleGb; // 제휴사판매상태
	private String autoYn;
	private String insertId;
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;

	public String getPaGroupCode() {
		return paGroupCode;
	}

	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}

	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
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

	public String getPaSaleGb() {
		return paSaleGb;
	}

	public void setPaSaleGb(String paSaleGb) {
		this.paSaleGb = paSaleGb;
	}

	public String getAutoYn() {
		return autoYn;
	}

	public void setAutoYn(String autoYn) {
		this.autoYn = autoYn;
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

	public String getModifyId() {
		return modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Override
	public String toString() {
		return "PaGoodsTarget [paGroupCode=" + paGroupCode + ", paCode=" + paCode + ", goodsCode=" + goodsCode
				+ ", paGoodsCode=" + paGoodsCode + ", paSaleGb=" + paSaleGb + ", autoYn=" + autoYn + ", insertId="
				+ insertId + ", insertDate=" + insertDate + ", modifyId=" + modifyId + ", modifyDate=" + modifyDate
				+ "]";
	}

}
