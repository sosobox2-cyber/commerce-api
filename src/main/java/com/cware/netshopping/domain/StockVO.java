package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Stock;

/**
 * @author Commerceware
 */
public class StockVO extends Stock {

	private static final long serialVersionUID = 1L;
    private StockVO od = null;
    public int compareTo(Object o){
        int  rtn = 0;
    	od = (StockVO)o;		
    	rtn = (this.getGoodsCode()+this.getGoodsdtCode()).compareTo(od.getGoodsCode()+od.getGoodsdtCode());
    	return rtn; 
    }
    
    private long   sType;
    private String doFlag;
    private String doFlagOrg;
    private String doFlagNew;
    private String preoutGb;
    private String claimGb;
    private long   orderQty;
    private long   syslast;
    private long   cancelQty;
    private long   claimQty;
	private String delyHopeYn;
    private String temp1;
    private String temp2;
    private String entpCode;
    private long delyQty;
    private String rackCode;
    private String buyMed;
    private String stockCheckYn;
    
    
	public String getStockCheckYn() {
		return stockCheckYn;
	}
	public void setStockCheckYn(String stockCheckYn) {
		this.stockCheckYn = stockCheckYn;
	}
	public String getBuyMed() {
		return buyMed;
	}
	public void setBuyMed(String buyMed) {
		this.buyMed = buyMed;
	}
	public long getSType() {
		return sType;
	}
	public void setSType(long sType) {
		this.sType = sType;
	}
	public String getDoFlag() {
		return doFlag;
	}
	public void setDoFlag(String doFlag) {
		this.doFlag = doFlag;
	}
	public String getDoFlagOrg() {
		return doFlagOrg;
	}
	public void setDoFlagOrg(String doFlagOrg) {
		this.doFlagOrg = doFlagOrg;
	}
	public String getDoFlagNew() {
		return doFlagNew;
	}
	public void setDoFlagNew(String doFlagNew) {
		this.doFlagNew = doFlagNew;
	}
	public String getPreoutGb() {
		return preoutGb;
	}
	public void setPreoutGb(String preoutGb) {
		this.preoutGb = preoutGb;
	}
	public String getClaimGb() {
		return claimGb;
	}
	public void setClaimGb(String claimGb) {
		this.claimGb = claimGb;
	}
	public long getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(long orderQty) {
		this.orderQty = orderQty;
	}
	public long getSyslast() {
		return syslast;
	}
	public void setSyslast(long syslast) {
		this.syslast = syslast;
	}
	public long getCancelQty() {
		return cancelQty;
	}
	public void setCancelQty(long cancelQty) {
		this.cancelQty = cancelQty;
	}
	public long getClaimQty() {
		return claimQty;
	}
	public void setClaimQty(long claimQty) {
		this.claimQty = claimQty;
	}
	public String getDelyHopeYn() {
		return delyHopeYn;
	}
	public void setDelyHopeYn(String delyHopeYn) {
		this.delyHopeYn = delyHopeYn;
	}
	public String getTemp1() {
		return temp1;
	}
	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}
	public String getTemp2() {
		return temp2;
	}
	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}
	public String getEntpCode() {
		return entpCode;
	}
	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}
	public long getDelyQty() {
		return delyQty;
	}
	public void setDelyQty(long delyQty) {
		this.delyQty = delyQty;
	}
	public String getRackCode() {
		return rackCode;
	}
	public void setRackCode(String rackCode) {
		this.rackCode = rackCode;
	}
}