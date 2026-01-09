package com.cware.netshopping.domain;

import com.cware.framework.core.basic.AbstractVO;

public class StockCheckVO extends AbstractVO{

	private static final long serialVersionUID = 1L;
	
	private String prdNo;	
	private String stockNo;
	private String addPrc;
	private String stockStat;
	
	public String getPrdNo() {
	    return prdNo;
	}
	public void setPrdNo(String prdNo) {
	    this.prdNo = prdNo;
	}
	public String getStockNo() {
	    return stockNo;
	}
	public void setStockNo(String stockNo) {
	    this.stockNo = stockNo;
	}
	public String getAddPrc() {
	    return addPrc;
	}
	public void setAddPrc(String addPrc) {
	    this.addPrc = addPrc;
	}
	public String getStockStat() {
	    return stockStat;
	}
	public void setStockStat(String stockStat) {
	    this.stockStat = stockStat;
	}
	
	
	
}