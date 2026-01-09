package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaFapleGoodsDt extends AbstractModel {
	private static final long serialVersionUID = 1L; 
	String goodsCode;
	String goodsdtCode;
	String goodsdtSeq;
	String paCode;
	String paOptionCode;
	String goodsdtInfo;
	String useYn;
	int transOrderAbleQty;
	String transStockYn;
	String transSaleYn;
	String insertId;
	Timestamp insertDate;
	String modifyId;
	Timestamp modifyDate;
	
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsdtCode() {
		return goodsdtCode;
	}
	public void setGoodsdtCode(String goodsdtCode) {
		this.goodsdtCode = goodsdtCode;
	}
	public String getGoodsdtSeq() {
		return goodsdtSeq;
	}
	public void setGoodsdtSeq(String goodsdtSeq) {
		this.goodsdtSeq = goodsdtSeq;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaOptionCode() {
		return paOptionCode;
	}
	public void setPaOptionCode(String paOptionCode) {
		this.paOptionCode = paOptionCode;
	}
	public String getGoodsdtInfo() {
		return goodsdtInfo;
	}
	public void setGoodsdtInfo(String goodsdtInfo) {
		this.goodsdtInfo = goodsdtInfo;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public int getTransOrderAbleQty() {
		return transOrderAbleQty;
	}
	public void setTransOrderAbleQty(int transOrderAbleQty) {
		this.transOrderAbleQty = transOrderAbleQty;
	}
	public String getTransStockYn() {
		return transStockYn;
	}
	public void setTransStockYn(String transStockYn) {
		this.transStockYn = transStockYn;
	}
	public String getTransSaleYn() {
		return transSaleYn;
	}
	public void setTransSaleYn(String transSaleYn) {
		this.transSaleYn = transSaleYn;
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

}
