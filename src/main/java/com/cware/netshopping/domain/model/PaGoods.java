package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaGoods extends AbstractModel {

	private static final long serialVersionUID = 1L;

	private String goodsCode;
	private String goodsName;
	private String saleGb;
	private String lmsdCode;
	private String brandName;
	private String originName;
	private String taxYn;
	private String taxSmallYn;
	private String adultYn;
	private long orderMinQty;
	private long orderMaxQty;
	private long custOrdQtyCheckTerm;
	private String doNotIslandDelyYn;
	private String entpCode;
	private String shipManSeq;
	private String returnManSeq;
	private String shipCostCode;
	private String salePaCode;
	private long avgDelyLeadtime;
	private Timestamp lastSyncDate;
	private Timestamp lastDescribeSyncDate;
	private String directShipYn;
	private int custOrdQtyCheckYn;
	private String termOrderQty;
	private String onAirSaleYn;
	private String collectYn;
	private String goodsStts;

	public String getDirectShipYn() {
		return directShipYn;
	}

	public void setDirectShipYn(String directShipYn) {
		this.directShipYn = directShipYn;
	}

	public int getCustOrdQtyCheckYn() {
		return custOrdQtyCheckYn;
	}

	public void setCustOrdQtyCheckYn(int custOrdQtyCheckYn) {
		this.custOrdQtyCheckYn = custOrdQtyCheckYn;
	}

	public String getTermOrderQty() {
		return termOrderQty;
	}

	public void setTermOrderQty(String termOrderQty) {
		this.termOrderQty = termOrderQty;
	}

	public String getOnAirSaleYn() {
		return onAirSaleYn;
	}

	public void setOnAirSaleYn(String onAirSaleYn) {
		this.onAirSaleYn = onAirSaleYn;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getSaleGb() {
		return saleGb;
	}

	public void setSaleGb(String saleGb) {
		this.saleGb = saleGb;
	}

	public String getLmsdCode() {
		return lmsdCode;
	}

	public void setLmsdCode(String lmsdCode) {
		this.lmsdCode = lmsdCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getTaxYn() {
		return taxYn;
	}

	public void setTaxYn(String taxYn) {
		this.taxYn = taxYn;
	}

	public String getTaxSmallYn() {
		return taxSmallYn;
	}

	public void setTaxSmallYn(String taxSmallYn) {
		this.taxSmallYn = taxSmallYn;
	}

	public String getAdultYn() {
		return adultYn;
	}

	public void setAdultYn(String adultYn) {
		this.adultYn = adultYn;
	}

	public long getOrderMinQty() {
		return orderMinQty;
	}

	public void setOrderMinQty(long orderMinQty) {
		this.orderMinQty = orderMinQty;
	}

	public long getOrderMaxQty() {
		return orderMaxQty;
	}

	public void setOrderMaxQty(long orderMaxQty) {
		this.orderMaxQty = orderMaxQty;
	}

	public long getCustOrdQtyCheckTerm() {
		return custOrdQtyCheckTerm;
	}

	public void setCustOrdQtyCheckTerm(long custOrdQtyCheckTerm) {
		this.custOrdQtyCheckTerm = custOrdQtyCheckTerm;
	}

	public String getDoNotIslandDelyYn() {
		return doNotIslandDelyYn;
	}

	public void setDoNotIslandDelyYn(String doNotIslandDelyYn) {
		this.doNotIslandDelyYn = doNotIslandDelyYn;
	}

	public String getEntpCode() {
		return entpCode;
	}

	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}

	public String getShipManSeq() {
		return shipManSeq;
	}

	public void setShipManSeq(String shipManSeq) {
		this.shipManSeq = shipManSeq;
	}

	public String getReturnManSeq() {
		return returnManSeq;
	}

	public void setReturnManSeq(String returnManSeq) {
		this.returnManSeq = returnManSeq;
	}

	public String getShipCostCode() {
		return shipCostCode;
	}

	public void setShipCostCode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}

	public String getSalePaCode() {
		return salePaCode;
	}

	public void setSalePaCode(String salePaCode) {
		this.salePaCode = salePaCode;
	}

	public long getAvgDelyLeadtime() {
		return avgDelyLeadtime;
	}

	public void setAvgDelyLeadtime(long avgDelyLeadtime) {
		this.avgDelyLeadtime = avgDelyLeadtime;
	}

	public Timestamp getLastSyncDate() {
		return lastSyncDate;
	}

	public void setLastSyncDate(Timestamp lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}

	public Timestamp getLastDescribeSyncDate() {
		return lastDescribeSyncDate;
	}

	public void setLastDescribeSyncDate(Timestamp lastDescribeSyncDate) {
		this.lastDescribeSyncDate = lastDescribeSyncDate;
	}

	public String getCollectYn() {
		return collectYn;
	}

	public void setCollectYn(String collectYn) {
		this.collectYn = collectYn;
	}

	public String getGoodsStts() {
		return goodsStts;
	}

	public void setGoodsStts(String goodsStts) {
		this.goodsStts = goodsStts;
	}
	
}