package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class Ordergoods extends AbstractModel {
	private static final long serialVersionUID = 1L;

	private String orderNo;
	private String orderGSeq;
	private String custNo;
	private Timestamp orderDate;
	private String orderGb;
	private String promoNo;
	private String setYn;
	private String goodsCode;
	private long orderQty;
	private long cancelQty;
	private long claimQty;
	private long returnQty;
	private long claimCanQty;
	private long exchCnt;
	private long asCnt;
	private double salePrice;
	private double dcRate;
	private double dcAmtGoods;
	private double dcAmtMemb;
	private double dcAmtDiv;
	private double dcAmtCard;
	private double dcAmt;
	private String norestAllotMonths;
	private String shipCostCode;
	private String counseltel;
	
	


	public String getCounseltel() {
		return counseltel;
	}
	public void setCounseltel(String counseltel) {
		this.counseltel = counseltel;
	}
	public String getShipCostCode() {
		return shipCostCode;
	}
	public void setShipCostCode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}
	public String getOrderNo() {
		return this.orderNo;
	}
	public String getOrderGSeq() {
		return this.orderGSeq;
	}
	public String getCustNo() {
		return this.custNo;
	}
	public Timestamp getOrderDate() {
		return this.orderDate;
	}
	public String getOrderGb() {
		return this.orderGb;
	}
	public String getPromoNo() {
		return this.promoNo;
	}
	public String getSetYn() {
		return this.setYn;
	}
	public String getGoodsCode() {
		return this.goodsCode;
	}
	public long getOrderQty() {
		return this.orderQty;
	}
	public long getCancelQty() {
		return this.cancelQty;
	}
	public long getClaimQty() {
		return this.claimQty;
	}
	public long getReturnQty() {
		return this.returnQty;
	}
	public long getClaimCanQty() {
		return this.claimCanQty;
	}
	public long getExchCnt() {
		return this.exchCnt;
	}
	public long getAsCnt() {
		return this.asCnt;
	}
	public double getSalePrice() {
		return this.salePrice;
	}
	public double getDcRate() {
		return this.dcRate;
	}
	public double getDcAmtGoods() {
		return this.dcAmtGoods;
	}
	public double getDcAmtMemb() {
		return this.dcAmtMemb;
	}
	public double getDcAmtDiv() {
		return this.dcAmtDiv;
	}
	public double getDcAmtCard() {
		return this.dcAmtCard;
	}
	public double getDcAmt() {
		return this.dcAmt;
	}
	public String getNorestAllotMonths() {
		return this.norestAllotMonths;
	}
	
			
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public void setOrderGSeq(String orderGSeq) {
		this.orderGSeq = orderGSeq;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	public void setOrderGb(String orderGb) {
		this.orderGb = orderGb;
	}
	public void setPromoNo(String promoNo) {
		this.promoNo = promoNo;
	}
	public void setSetYn(String setYn) {
		this.setYn = setYn;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public void setOrderQty(long orderQty) {
		this.orderQty = orderQty;
	}
	public void setCancelQty(long cancelQty) {
		this.cancelQty = cancelQty;
	}
	public void setClaimQty(long claimQty) {
		this.claimQty = claimQty;
	}
	public void setReturnQty(long returnQty) {
		this.returnQty = returnQty;
	}
	public void setClaimCanQty(long claimCanQty) {
		this.claimCanQty = claimCanQty;
	}
	public void setExchCnt(long exchCnt) {
		this.exchCnt = exchCnt;
	}
	public void setAsCnt(long asCnt) {
		this.asCnt = asCnt;
	}
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}
	public void setDcRate(double dcRate) {
		this.dcRate = dcRate;
	}
	public void setDcAmtGoods(double dcAmtGoods) {
		this.dcAmtGoods = dcAmtGoods;
	}
	public void setDcAmtMemb(double dcAmtMemb) {
		this.dcAmtMemb = dcAmtMemb;
	}
	public void setDcAmtDiv(double dcAmtDiv) {
		this.dcAmtDiv = dcAmtDiv;
	}
	public void setDcAmtCard(double dcAmtCard) {
		this.dcAmtCard = dcAmtCard;
	}
	public void setDcAmt(double dcAmt) {
		this.dcAmt = dcAmt;
	}
	public void setNorestAllotMonths(String norestAllotMonths) {
		this.norestAllotMonths = norestAllotMonths;
	}
}
