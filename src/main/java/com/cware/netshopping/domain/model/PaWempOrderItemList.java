package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

public class PaWempOrderItemList {
	private static final long serialVersionUID = 1L;
	
	private String paOrderNo;
	private String paShipNo;
	private String paOrderSeq;
	private String orderProductNo;
	private String productNo;
	private String productName;
	private long productOriginPrice;
	private long productPrice;
	private long productCommissionPrice;
	private long productQty;
	private long wmpChargeDiscount;
	private long sellerChargeDiscount;
	private long cardChargeDiscount;
	private String goodsCode;
	private String orderOptionNo;
	private String optionNo;
	private String optionName;
	private long optionQty;
	private String goodsdtCode;
	private String insertId;
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;
	
	public String getPaOrderNo() {
		return paOrderNo;
	}
	public void setPaOrderNo(String paOrderNo) {
		this.paOrderNo = paOrderNo;
	}
	public String getPaShipNo() {
		return paShipNo;
	}
	public void setPaShipNo(String paShipNo) {
		this.paShipNo = paShipNo;
	}
	public String getPaOrderSeq() {
		return paOrderSeq;
	}
	public void setPaOrderSeq(String paOrderSeq) {
		this.paOrderSeq = paOrderSeq;
	}
	public String getOrderProductNo() {
		return orderProductNo;
	}
	public void setOrderProductNo(String orderProductNo) {
		this.orderProductNo = orderProductNo;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public long getProductOriginPrice() {
		return productOriginPrice;
	}
	public void setProductOriginPrice(long productOriginPrice) {
		this.productOriginPrice = productOriginPrice;
	}
	public long getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(long productPrice) {
		this.productPrice = productPrice;
	}
	public long getProductCommissionPrice() {
		return productCommissionPrice;
	}
	public void setProductCommissionPrice(long productCommissionPrice) {
		this.productCommissionPrice = productCommissionPrice;
	}
	public long getProductQty() {
		return productQty;
	}
	public void setProductQty(long productQty) {
		this.productQty = productQty;
	}
	public long getWmpChargeDiscount() {
		return wmpChargeDiscount;
	}
	public void setWmpChargeDiscount(long wmpChargeDiscount) {
		this.wmpChargeDiscount = wmpChargeDiscount;
	}
	public long getSellerChargeDiscount() {
		return sellerChargeDiscount;
	}
	public void setSellerChargeDiscount(long sellerChargeDiscount) {
		this.sellerChargeDiscount = sellerChargeDiscount;
	}
	public long getCardChargeDiscount() {
		return cardChargeDiscount;
	}
	public void setCardChargeDiscount(long cardChargeDiscount) {
		this.cardChargeDiscount = cardChargeDiscount;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getOrderOptionNo() {
		return orderOptionNo;
	}
	public void setOrderOptionNo(String orderOptionNo) {
		this.orderOptionNo = orderOptionNo;
	}
	public String getOptionNo() {
		return optionNo;
	}
	public void setOptionNo(String optionNo) {
		this.optionNo = optionNo;
	}
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public long getOptionQty() {
		return optionQty;
	}
	public void setOptionQty(long optionQty) {
		this.optionQty = optionQty;
	}
	public String getGoodsdtCode() {
		return goodsdtCode;
	}
	public void setGoodsdtCode(String goodsdtCode) {
		this.goodsdtCode = goodsdtCode;
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
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getModifyId() {
		return modifyId;
	}
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
