package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Orderstock extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String goodsCode;
	private String goodsdtCode;
	private String whCode;
	private long orderQty;
	private long outPlanQty;
	private long broadQty;
	private long totSaleQty;

	public String getGoodsCode() { 
		return this.goodsCode;
	}
	public String getGoodsdtCode() { 
		return this.goodsdtCode;
	}
	public String getWhCode() { 
		return this.whCode;
	}
	public long getOrderQty() { 
		return this.orderQty;
	}
	public long getOutPlanQty() { 
		return this.outPlanQty;
	}
	public long getBroadQty() { 
		return this.broadQty;
	}
	public long getTotSaleQty() { 
		return this.totSaleQty;
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
	public void setOrderQty(long orderQty) { 
		this.orderQty = orderQty;
	}
	public void setOutPlanQty(long outPlanQty) { 
		this.outPlanQty = outPlanQty;
	}
	public void setBroadQty(long broadQty) { 
		this.broadQty = broadQty;
	}
	public void setTotSaleQty(long totSaleQty) { 
		this.totSaleQty = totSaleQty;
	}
}
