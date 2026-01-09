package com.cware.netshopping.domain;

import com.cware.framework.core.basic.AbstractVO;

public class OrderStockInVO extends AbstractVO {

	private static final long serialVersionUID = 1L;

    private long   row;
    private String selectFlag;
    private String goodsCode;
    private String goodsdtCode;
    private String goodsdtInfo;
    private String goodsName;
    private String custNo;
	private String orderNo;
    private String orderGSeq;
    private String orderDSeq;
    private String orderWSeq;
    private String goodsSelectNo;
    private String insertId;
    private long   counselQty;
    private String delyType;
    private String stockChkPlace;
    private String delyGb;
    private String postNo;
    private String postSeq;
    private String whCode;
    private String receiverSeq;
    
    private String roadAddrNo;
    private String stdPostNo;
    
    
    
    

	public String getRoadAddrNo() {
		return roadAddrNo;
	}
	public void setRoadAddrNo(String roadAddrNo) {
		this.roadAddrNo = roadAddrNo;
	}
	public String getStdPostNo() {
		return stdPostNo;
	}
	public void setStdPostNo(String stdPostNo) {
		this.stdPostNo = stdPostNo;
	}
	public String getGoodsdtInfo() {
		return goodsdtInfo;
	}
	public void setGoodsdtInfo(String goodsdtInfo) {
		this.goodsdtInfo = goodsdtInfo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getWhCode() {
		return whCode;
	}
	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}
	public long getRow() {
		return row;
	}
	public void setRow(long row) {
		this.row = row;
	}
	public String getSelectFlag() {
		return selectFlag;
	}
	public void setSelectFlag(String selectFlag) {
		this.selectFlag = selectFlag;
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
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
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
	public String getGoodsSelectNo() {
		return goodsSelectNo;
	}
	public void setGoodsSelectNo(String goodsSelectNo) {
		this.goodsSelectNo = goodsSelectNo;
	}
	public String getInsertId() {
		return insertId;
	}
	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}
	public long getCounselQty() {
		return counselQty;
	}
	public void setCounselQty(long counselQty) {
		this.counselQty = counselQty;
	}
	public String getDelyType() {
		return delyType;
	}
	public void setDelyType(String delyType) {
		this.delyType = delyType;
	}
	public String getStockChkPlace() {
		return stockChkPlace;
	}
	public void setStockChkPlace(String stockChkPlace) {
		this.stockChkPlace = stockChkPlace;
	}
	public String getDelyGb() {
		return delyGb;
	}
	public void setDelyGb(String delyGb) {
		this.delyGb = delyGb;
	}
	public String getPostNo() {
		return postNo;
	}
	public void setPostNo(String postNo) {
		this.postNo = postNo;
	}
	public String getPostSeq() {
		return postSeq;
	}
	public void setPostSeq(String postSeq) {
		this.postSeq = postSeq;
	}
	public String getReceiverSeq() {
		return receiverSeq;
	}
	public void setReceiverSeq(String receiverSeq) {
		this.receiverSeq = receiverSeq;
	}
	
}
