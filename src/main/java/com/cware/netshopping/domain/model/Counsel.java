package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Counsel extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String whCode;
	private String goodsCode;
	private String goodsdtCode;
	private String orderNo;
	private String orderGSeq;
	private String orderDSeq;
	private String orderWSeq;
	private String goodsSelectNo;
	private long counselQty;
	private String mediaGb;
	private String mediaCode;

	public String getWhCode() { 
		return this.whCode;
	}
	public String getGoodsCode() { 
		return this.goodsCode;
	}
	public String getGoodsdtCode() { 
		return this.goodsdtCode;
	}
	public String getOrderNo() { 
		return this.orderNo;
	}
	public String getOrderGSeq() { 
		return this.orderGSeq;
	}
	public String getOrderDSeq() { 
		return this.orderDSeq;
	}
	public String getOrderWSeq() { 
		return this.orderWSeq;
	}
	public String getGoodsSelectNo() { 
		return this.goodsSelectNo;
	}
	public long getCounselQty() { 
		return this.counselQty;
	}
	public String getMediaGb() { 
		return this.mediaGb;
	}
	public String getMediaCode() { 
		return this.mediaCode;
	}

	public void setWhCode(String whCode) { 
		this.whCode = whCode;
	}
	public void setGoodsCode(String goodsCode) { 
		this.goodsCode = goodsCode;
	}
	public void setGoodsdtCode(String goodsdtCode) { 
		this.goodsdtCode = goodsdtCode;
	}
	public void setOrderNo(String orderNo) { 
		this.orderNo = orderNo;
	}
	public void setOrderGSeq(String orderGSeq) { 
		this.orderGSeq = orderGSeq;
	}
	public void setOrderDSeq(String orderDSeq) { 
		this.orderDSeq = orderDSeq;
	}
	public void setOrderWSeq(String orderWSeq) { 
		this.orderWSeq = orderWSeq;
	}
	public void setGoodsSelectNo(String goodsSelectNo) { 
		this.goodsSelectNo = goodsSelectNo;
	}
	public void setCounselQty(long counselQty) { 
		this.counselQty = counselQty;
	}
	public void setMediaGb(String mediaGb) { 
		this.mediaGb = mediaGb;
	}
	public void setMediaCode(String mediaCode) { 
		this.mediaCode = mediaCode;
	}
}
