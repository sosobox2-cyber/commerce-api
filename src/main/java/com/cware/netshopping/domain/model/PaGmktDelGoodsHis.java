package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaGmktDelGoodsHis extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private String goodsCode;
    private String seq;
    private String paCode;
    private String esmGoodsCode;
    private String itemNo;
    
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
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
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
    
}
