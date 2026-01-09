package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaGoodsPrice;

public class PaGmktGoodsPriceVO extends PaGoodsPrice {

    private static final long serialVersionUID = 1L;

    private String itemNo; 
    private String esmGoodsCode; 
    private long transOrderAbleQty;
    private String goodsdtCode; 
    private String paCode; 
    private String paOptionCode;
    private String displayDate;
    private String discountStartDate;
    private String newSupplyPrice;
    private String lumpSumDcAmt;
    
    public String getNewSupplyPrice() {
		return newSupplyPrice;
	}

	public void setNewSupplyPrice(String newSupplyPrice) {
		this.newSupplyPrice = newSupplyPrice;
	}

	public String getEsmGoodsCode() {
		return esmGoodsCode;
	}

	public void setEsmGoodsCode(String esmGoodsCode) {
		this.esmGoodsCode = esmGoodsCode;
	}

	public String getItemNo() {
        return itemNo;
    }

    public long getTransOrderAbleQty() {
        return transOrderAbleQty;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public void setTransOrderAbleQty(long transOrderAbleQty) {
        this.transOrderAbleQty = transOrderAbleQty;
    }

    public String getGoodsdtCode() {
        return goodsdtCode;
    }

    public String getPaCode() {
        return paCode;
    }

    public String getPaOptionCode() {
        return paOptionCode;
    }

    public void setGoodsdtCode(String goodsdtCode) {
        this.goodsdtCode = goodsdtCode;
    }

    public void setPaCode(String paCode) {
        this.paCode = paCode;
    }

    public void setPaOptionCode(String paOptionCode) {
        this.paOptionCode = paOptionCode;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    public String getDiscountStartDate() {
        return discountStartDate;
    }

    public void setDiscountStartDate(String discountStartDate) {
        this.discountStartDate = discountStartDate;
    }

	public String getLumpSumDcAmt() {
		return lumpSumDcAmt;
	}

	public void setLumpSumDcAmt(String lumpSumDcAmt) {
		this.lumpSumDcAmt = lumpSumDcAmt;
	}       

}
