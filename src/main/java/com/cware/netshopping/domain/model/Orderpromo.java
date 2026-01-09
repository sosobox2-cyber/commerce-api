package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class Orderpromo extends AbstractModel {
	private static final long serialVersionUID = 1L;

	private String seq;
	private String promoNo;
	private String doType;
	private String orderNo;
	private String orderGSeq;
	private String orderDSeq;
	private String orderWSeq;
	private String cancelYn;
	private Timestamp cancelDate;
	private String cancelId;
	private String remark;
	private String entpCode;
    private double procAmt;			//= 프로모션 적용금액
    private double cancelAmt;		//= 프로모션 취소금액
    private double claimAmt;		//= 프로모션 반품금액
    private double procCost;		//= 프로모션 적용단가
    private double ownProcCost;		//= 프로모션 당사부담단가
    private double entpProcCost;	//= 프로모션 업체부담단가


	public String getSeq() {
		return this.seq;
	}
	public String getPromoNo() {
		return this.promoNo;
	}
	public String getDoType() {
		return this.doType;
	}
	public String getOrderNo() {
		return this.orderNo;
	}
	public String getOrderGSeq() {
		return this.orderGSeq;
	}
	public String getOrderDSeq() {
		return this.orderDSeq;
	}
	public String getOrderWSeq() {
		return this.orderWSeq;
	}
	public String getCancelYn() {
		return this.cancelYn;
	}
	public Timestamp getCancelDate() {
		return this.cancelDate;
	}
	public String getCancelId() {
		return this.cancelId;
	}
	public String getRemark() {
		return this.remark;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}
	public void setPromoNo(String promoNo) {
		this.promoNo = promoNo;
	}
	public void setDoType(String doType) {
		this.doType = doType;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public void setOrderGSeq(String orderGSeq) {
		this.orderGSeq = orderGSeq;
	}
	public void setOrderDSeq(String orderDSeq) {
		this.orderDSeq = orderDSeq;
	}
	public void setOrderWSeq(String orderWSeq) {
		this.orderWSeq = orderWSeq;
	}
	public void setCancelYn(String cancelYn) {
		this.cancelYn = cancelYn;
	}
	public void setCancelDate(Timestamp cancelDate) {
		this.cancelDate = cancelDate;
	}
	public void setCancelId(String cancelId) {
		this.cancelId = cancelId;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public double getProcAmt() {
		return procAmt;
	}
	public void setProcAmt(double procAmt) {
		this.procAmt = procAmt;
	}
	public double getCancelAmt() {
		return cancelAmt;
	}
	public void setCancelAmt(double cancelAmt) {
		this.cancelAmt = cancelAmt;
	}
	public double getClaimAmt() {
		return claimAmt;
	}
	public void setClaimAmt(double claimAmt) {
		this.claimAmt = claimAmt;
	}
	public double getProcCost() {
		return procCost;
	}
	public void setProcCost(double procCost) {
		this.procCost = procCost;
	}
	public double getOwnProcCost() {
		return ownProcCost;
	}
	public void setOwnProcCost(double ownProcCost) {
		this.ownProcCost = ownProcCost;
	}
	public double getEntpProcCost() {
		return entpProcCost;
	}
	public void setEntpProcCost(double entpProcCost) {
		this.entpProcCost = entpProcCost;
	}
	public String getEntpCode() {
		return entpCode;
	}
	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}
}
