package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaGoodsdtMapping;

public class PaWempGoodsdtMappingVO extends PaGoodsdtMapping {
	private static final long serialVersionUID = 1L;

	private String productNo;
	private String goodsdtInfoKind;
	private String goodsdtInfo;
	private String paSaleGb;
    private String transId;
	private Timestamp transDate;
	private Timestamp applyDate;
	
	public String getGoodsdtInfoKind() {
		return goodsdtInfoKind;
	}

	public void setGoodsdtInfoKind(String goodsdtInfoKind) {
		this.goodsdtInfoKind = goodsdtInfoKind;
	}
    
	public String getGoodsdtInfo() {
		return goodsdtInfo;
	}

	public void setGoodsdtInfo(String goodsdtInfo) {
		this.goodsdtInfo = goodsdtInfo;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public Timestamp getTransDate() {
		return transDate;
	}

	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
	}

	public Timestamp getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Timestamp applyDate) {
		this.applyDate = applyDate;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getPaSaleGb() {
		return paSaleGb;
	}
	public void setPaSaleGb(String paSaleGb) {
		this.paSaleGb = paSaleGb;
	}
}