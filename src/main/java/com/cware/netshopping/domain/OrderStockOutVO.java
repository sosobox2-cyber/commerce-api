package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractVO;

public class OrderStockOutVO extends AbstractVO {

	private static final long serialVersionUID = 1L;

    private String stockFlag;
    private String stockKey;
    private Timestamp firstPlanDate;
    private Timestamp outPlanDate;
    private Timestamp delyHopeDate;
    private String fromDate;
    private String toDate;
    private long   orderQty;
    private String resultGeneral;
    private long   problemRow;
    private long   problemQty;
    private String resultSum;
    private String orderNo;
    private String orderGSeq;
    private String orderDSeq;
    private String orderWSeq;
    private long   orderSeq;
    private long   stockQty;
    private String goodsSelectNo;
    private String whCode;
    private String delyGb;
    private String rDelyGb;
    private String rWhCode;
    
    private String problemGoodsCode;
    private String problemGoodsDtCode;
    
    private String saleGb;
    private String goodsCode;
    private String goodsdtCode;
    
    
	public String getStockFlag() {
		return stockFlag;
	}
	public void setStockFlag(String stockFlag) {
		this.stockFlag = stockFlag;
	}
	public String getStockKey() {
		return stockKey;
	}
	public void setStockKey(String stockKey) {
		this.stockKey = stockKey;
	}
	public Timestamp getFirstPlanDate() {
		return firstPlanDate;
	}
	public void setFirstPlanDate(Timestamp firstPlanDate) {
		this.firstPlanDate = firstPlanDate;
	}
	public Timestamp getOutPlanDate() {
		return outPlanDate;
	}
	public void setOutPlanDate(Timestamp outPlanDate) {
		this.outPlanDate = outPlanDate;
	}
	public Timestamp getDelyHopeDate() {
		return delyHopeDate;
	}
	public void setDelyHopeDate(Timestamp delyHopeDate) {
		this.delyHopeDate = delyHopeDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public long getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(long orderQty) {
		this.orderQty = orderQty;
	}
	public String getResultGeneral() {
		return resultGeneral;
	}
	public void setResultGeneral(String resultGeneral) {
		this.resultGeneral = resultGeneral;
	}
	public long getProblemRow() {
		return problemRow;
	}
	public void setProblemRow(long problemRow) {
		this.problemRow = problemRow;
	}
	public long getProblemQty() {
		return problemQty;
	}
	public void setProblemQty(long problemQty) {
		this.problemQty = problemQty;
	}
	public String getResultSum() {
		return resultSum;
	}
	public void setResultSum(String resultSum) {
		this.resultSum = resultSum;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderGSeq() {
		return orderGSeq;
	}
	public void setOrderGSeq(String orderGSeq) {
		this.orderGSeq = orderGSeq;
	}
	public String getOrderDSeq() {
		return orderDSeq;
	}
	public void setOrderDSeq(String orderDSeq) {
		this.orderDSeq = orderDSeq;
	}
	public String getOrderWSeq() {
		return orderWSeq;
	}
	public void setOrderWSeq(String orderWSeq) {
		this.orderWSeq = orderWSeq;
	}
	public long getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(long orderSeq) {
		this.orderSeq = orderSeq;
	}
	public long getStockQty() {
		return stockQty;
	}
	public void setStockQty(long stockQty) {
		this.stockQty = stockQty;
	}
	public String getGoodsSelectNo() {
		return goodsSelectNo;
	}
	public void setGoodsSelectNo(String goodsSelectNo) {
		this.goodsSelectNo = goodsSelectNo;
	}
	public String getWhCode() {
		return whCode;
	}
	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}
	public String getDelyGb() {
		return delyGb;
	}
	public void setDelyGb(String delyGb) {
		this.delyGb = delyGb;
	}
	public String getrDelyGb() {
		return rDelyGb;
	}
	public void setrDelyGb(String rDelyGb) {
		this.rDelyGb = rDelyGb;
	}
	public String getrWhCode() {
		return rWhCode;
	}
	public void setrWhCode(String rWhCode) {
		this.rWhCode = rWhCode;
	}
	public String getProblemGoodsCode() {
		return problemGoodsCode;
	}
	public void setProblemGoodsCode(String problemGoodsCode) {
		this.problemGoodsCode = problemGoodsCode;
	}
	public String getProblemGoodsDtCode() {
		return problemGoodsDtCode;
	}
	public void setProblemGoodsDtCode(String problemGoodsDtCode) {
		this.problemGoodsDtCode = problemGoodsDtCode;
	}
	public String getSaleGb() {
		return saleGb;
	}
	public void setSaleGb(String saleGb) {
		this.saleGb = saleGb;
	}
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
	
}
