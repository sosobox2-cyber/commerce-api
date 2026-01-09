package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractVO;

public class PaTdealSlackGoods extends AbstractVO {

	private static final long serialVersionUID = 1L;
	
	private String paGroupCode;
	private String paCode;
	private String goodsCode;
	private String mallProductNo;
	private Timestamp asisTransDate;
	private Timestamp tobeTransDate;
	private int asisBestPrice;
	private int tobeBestPrice;
	private int priceDiff;
	private String asisGoodsName;
	private String tobeGoodsName;
	private Timestamp iudDate;
	private String asisPaLmsdKey;
	private String tobePaLmsdKey;
	private String asisDispCatId;
	private String tobeDispCatId;
	private String asisCouponDcAmt;
	private String tobeCouponDcAmt;
	private String asisMarginRate;
	private String tobeMarginRate;
	private String stopPaSaleGb;
	private String startPaSaleGb;
	
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
	public String getMallProductNo() {
		return mallProductNo;
	}
	public void setMallProductNo(String mallProductNo) {
		this.mallProductNo = mallProductNo;
	}
	public Timestamp getAsisTransDate() {
		return asisTransDate;
	}
	public void setAsisTransDate(Timestamp asisTransDate) {
		this.asisTransDate = asisTransDate;
	}
	public Timestamp getTobeTransDate() {
		return tobeTransDate;
	}
	public void setTobeTransDate(Timestamp tobeTransDate) {
		this.tobeTransDate = tobeTransDate;
	}
	public int getAsisBestPrice() {
		return asisBestPrice;
	}
	public void setAsisBestPrice(int asisBestPrice) {
		this.asisBestPrice = asisBestPrice;
	}
	public int getTobeBestPrice() {
		return tobeBestPrice;
	}
	public void setTobeBestPrice(int tobeBestPrice) {
		this.tobeBestPrice = tobeBestPrice;
	}
	public int getPriceDiff() {
		return priceDiff;
	}
	public void setPriceDiff(int priceDiff) {
		this.priceDiff = priceDiff;
	}
	public String getAsisGoodsName() {
		return asisGoodsName;
	}
	public void setAsisGoodsName(String asisGoodsName) {
		this.asisGoodsName = asisGoodsName;
	}
	public String getTobeGoodsName() {
		return tobeGoodsName;
	}
	public void setTobeGoodsName(String tobeGoodsName) {
		this.tobeGoodsName = tobeGoodsName;
	}
	public Timestamp getIudDate() {
		return iudDate;
	}
	public void setIudDate(Timestamp iudDate) {
		this.iudDate = iudDate;
	}
	public String getAsisPaLmsdKey() {
		return asisPaLmsdKey;
	}
	public void setAsisPaLmsdKey(String asisPaLmsdKey) {
		this.asisPaLmsdKey = asisPaLmsdKey;
	}
	public String getTobePaLmsdKey() {
		return tobePaLmsdKey;
	}
	public void setTobePaLmsdKey(String tobePaLmsdKey) {
		this.tobePaLmsdKey = tobePaLmsdKey;
	}
	public String getAsisDispCatId() {
		return asisDispCatId;
	}
	public void setAsisDispCatId(String asisDispCatId) {
		this.asisDispCatId = asisDispCatId;
	}
	public String getTobeDispCatId() {
		return tobeDispCatId;
	}
	public void setTobeDispCatId(String tobeDispCatId) {
		this.tobeDispCatId = tobeDispCatId;
	}
	public String getAsisCouponDcAmt() {
		return asisCouponDcAmt;
	}
	public void setAsisCouponDcAmt(String asisCouponDcAmt) {
		this.asisCouponDcAmt = asisCouponDcAmt;
	}
	public String getTobeCouponDcAmt() {
		return tobeCouponDcAmt;
	}
	public void setTobeCouponDcAmt(String tobeCouponDcAmt) {
		this.tobeCouponDcAmt = tobeCouponDcAmt;
	}
	public String getAsisMarginRate() {
		return asisMarginRate;
	}
	public void setAsisMarginRate(String asisMarginRate) {
		this.asisMarginRate = asisMarginRate;
	}
	public String getTobeMarginRate() {
		return tobeMarginRate;
	}
	public void setTobeMarginRate(String tobeMarginRate) {
		this.tobeMarginRate = tobeMarginRate;
	}
	public String getStopPaSaleGb() {
		return stopPaSaleGb;
	}
	public void setStopPaSaleGb(String stopPaSaleGb) {
		this.stopPaSaleGb = stopPaSaleGb;
	}
	public String getStartPaSaleGb() {
		return startPaSaleGb;
	}
	public void setStartPaSaleGb(String startPaSaleGb) {
		this.startPaSaleGb = startPaSaleGb;
	}
	
}