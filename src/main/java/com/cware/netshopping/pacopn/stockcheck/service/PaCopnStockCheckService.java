package com.cware.netshopping.pacopn.stockcheck.service;

import org.json.JSONObject;

import com.cware.framework.core.basic.ParamMap;

public interface PaCopnStockCheckService {
	
	public JSONObject selectOrderableGoods(ParamMap paramMap) throws Exception;

}
