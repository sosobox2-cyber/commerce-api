package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Promocounsel extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String promoNo;
	private String orderNo;
	private String orderGSeq;
	private String goodsSelectNo;
	private long counselQty;

	public String getPromoNo() { 
		return this.promoNo;
	}
	public String getOrderNo() { 
		return this.orderNo;
	}
	public String getOrderGSeq() { 
		return this.orderGSeq;
	}
	public String getGoodsSelectNo() { 
		return this.goodsSelectNo;
	}
	public long getCounselQty() { 
		return this.counselQty;
	}

	public void setPromoNo(String promoNo) { 
		this.promoNo = promoNo;
	}
	public void setOrderNo(String orderNo) { 
		this.orderNo = orderNo;
	}
	public void setOrderGSeq(String orderGSeq) { 
		this.orderGSeq = orderGSeq;
	}
	public void setGoodsSelectNo(String goodsSelectNo) { 
		this.goodsSelectNo = goodsSelectNo;
	}
	public void setCounselQty(long counselQty) { 
		this.counselQty = counselQty;
	}
}
