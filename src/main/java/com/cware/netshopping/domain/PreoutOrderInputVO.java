package com.cware.netshopping.domain;

public class PreoutOrderInputVO extends OrderInputVO {
	private static final long serialVersionUID = 1L;
	
	private String orgOrderNo;
	private String orgOrderGSeq;
	private String orgOrderDSeq;
	private String orgOrderWSeq;
	private String custNo;
	private String receiverSeq;
	private double dcAmt;
	private double orgSalePrice;
	private double remark3N;
	private double remark4N;
	
	public String getOrgOrderNo() {
		return orgOrderNo;
	}
	public void setOrgOrderNo(String orgOrderNo) {
		this.orgOrderNo = orgOrderNo;
	}
	public String getOrgOrderGSeq() {
		return orgOrderGSeq;
	}
	public void setOrgOrderGSeq(String orgOrderGSeq) {
		this.orgOrderGSeq = orgOrderGSeq;
	}
	public String getOrgOrderDSeq() {
		return orgOrderDSeq;
	}
	public void setOrgOrderDSeq(String orgOrderDSeq) {
		this.orgOrderDSeq = orgOrderDSeq;
	}
	public String getOrgOrderWSeq() {
		return orgOrderWSeq;
	}
	public void setOrgOrderWSeq(String orgOrderWSeq) {
		this.orgOrderWSeq = orgOrderWSeq;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getReceiverSeq() {
		return receiverSeq;
	}
	public void setReceiverSeq(String receiverSeq) {
		this.receiverSeq = receiverSeq;
	}
	public double getDcAmt() {
		return dcAmt;
	}
	public void setDcAmt(double dcAmt) {
		this.dcAmt = dcAmt;
	}
	public double getOrgSalePrice() {
		return orgSalePrice;
	}
	public void setOrgSalePrice(double orgSalePrice) {
		this.orgSalePrice = orgSalePrice;
	}
	public double getRemark3N() {
		return remark3N;
	}
	public void setRemark3N(double remark3n) {
		remark3N = remark3n;
	}
	
	public double getRemark4N() {
		return remark4N;
	}
	public void setRemark4N(double remark4n) {
		remark4N = remark4n;
	}
	
	
	
}
