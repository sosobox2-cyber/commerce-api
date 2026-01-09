package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Stock extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String goodsCode;
	private String goodsdtCode;
	private String whCode;
	private long aqty;
	private long bqty;
	private long baljuQty;
	private long eoutQuestAqty;
	private long eoutQuestBqty;
	private long delyHopeQty;

	public String getGoodsCode() { 
		return this.goodsCode;
	}
	public String getGoodsdtCode() { 
		return this.goodsdtCode;
	}
	public String getWhCode() { 
		return this.whCode;
	}
	public long getAqty() { 
		return this.aqty;
	}
	public long getBqty() { 
		return this.bqty;
	}
	public long getBaljuQty() { 
		return this.baljuQty;
	}
	public long getEoutQuestAqty() { 
		return this.eoutQuestAqty;
	}
	public long getEoutQuestBqty() { 
		return this.eoutQuestBqty;
	}
	public long getDelyHopeQty() { 
		return this.delyHopeQty;
	}

	public void setGoodsCode(String goodsCode) { 
		this.goodsCode = goodsCode;
	}
	public void setGoodsdtCode(String goodsdtCode) { 
		this.goodsdtCode = goodsdtCode;
	}
	public void setWhCode(String whCode) { 
		this.whCode = whCode;
	}
	public void setAqty(long aqty) { 
		this.aqty = aqty;
	}
	public void setBqty(long bqty) { 
		this.bqty = bqty;
	}
	public void setBaljuQty(long baljuQty) { 
		this.baljuQty = baljuQty;
	}
	public void setEoutQuestAqty(long eoutQuestAqty) { 
		this.eoutQuestAqty = eoutQuestAqty;
	}
	public void setEoutQuestBqty(long eoutQuestBqty) { 
		this.eoutQuestBqty = eoutQuestBqty;
	}
	public void setDelyHopeQty(long delyHopeQty) { 
		this.delyHopeQty = delyHopeQty;
	}
}
