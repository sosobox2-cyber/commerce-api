package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaGoodsdtMapping;

public class PaCopnGoodsdtMappingVO extends PaGoodsdtMapping {

    private static final long serialVersionUID = 1L;

    
    private String sellerProductId;
    
    private String goodsdtInfoKind;
    
    private String goodsdtInfo;
    private String paSaleGb;
    
    private String remark1;
    private String remark2;
    
	private String transId;
	private Timestamp transDate;
	private Timestamp applyDate;
	private String transSaleYn;
	
	public String getSellerProductId() {
		return sellerProductId;
	}
	public void setSellerProductId(String sellerProductId) {
		this.sellerProductId = sellerProductId;
	}
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
	public String getPaSaleGb() {
		return paSaleGb;
	}
	public void setPaSaleGb(String paSaleGb) {
		this.paSaleGb = paSaleGb;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
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
	public String getTransSaleYn() {
		return transSaleYn;
	}
	public void setTransSaleYn(String transSaleYn) {
		this.transSaleYn = transSaleYn;
	}
}
