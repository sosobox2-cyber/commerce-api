package com.cware.netshopping.pacommon.v2.domain;

import java.sql.Timestamp;

public class PaRetentionGoods {

	private String paGroupCode;
	private String paCode;
	private String goodsCode;
	private String paGoodsCode;
	private String seq;
	private String status;
	private String targetYn;
	private String memo;
	private String insertId;
	private Timestamp insertDate;

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

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTargetYn() {
		return targetYn;
	}

	public void setTargetYn(String targetYn) {
		this.targetYn = targetYn;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	@Override
	public String toString() {
		return "PaRetentionGoods [paGroupCode=" + paGroupCode + ", paCode=" + paCode + ", goodsCode=" + goodsCode
				+ ", paGoodsCode=" + paGoodsCode + ", seq=" + seq + ", status=" + status + ", targetYn=" + targetYn
				+ ", memo=" + memo + ", insertId=" + insertId + ", insertDate=" + insertDate + "]";
	}

}
