package com.cware.netshopping.pacopn.stockcheck.process;

import org.json.JSONObject;

import com.cware.framework.core.basic.ParamMap;


public interface PaCopnStockCheckProcess {
	
	public JSONObject selectOrderableGoods(ParamMap paramMap) throws Exception;
}