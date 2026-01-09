package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaPromoTargetHistory extends AbstractModel {

    private static final long serialVersionUID = 1L;
    private String promoNo;
    private String paGroupCode;
    private String goodsCode;
    private String seq;
    private String procGb;
    private String alcoutPromoYn;
    
	public String getPromoNo() {
		return promoNo;
	}
	public void setPromoNo(String promoNo) {
		this.promoNo = promoNo;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
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
	public String getProcGb() {
		return procGb;
	}
	public void setProcGb(String procGb) {
		this.procGb = procGb;
	}
	public String getAlcoutPromoYn() {
		return alcoutPromoYn;
	}
	public void setAlcoutPromoYn(String alcoutPromoYn) {
		this.alcoutPromoYn = alcoutPromoYn;
	}
}
