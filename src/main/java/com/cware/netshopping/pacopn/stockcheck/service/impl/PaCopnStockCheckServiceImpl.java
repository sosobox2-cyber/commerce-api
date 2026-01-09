package com.cware.netshopping.pacopn.stockcheck.service.impl;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pacopn.stockcheck.process.PaCopnStockCheckProcess;
import com.cware.netshopping.pacopn.stockcheck.service.PaCopnStockCheckService;

@Service("pacopn.stockcheck.paCopnStockCheckService")
public class PaCopnStockCheckServiceImpl extends AbstractService implements PaCopnStockCheckService {
	
	@Resource(name = "pacopn.stockcheck.paCopnStockCheckProcess")
	private PaCopnStockCheckProcess paCopnStockCheckProcess;

	@Override
	public JSONObject selectOrderableGoods(ParamMap paramMap) throws Exception {
		return paCopnStockCheckProcess.selectOrderableGoods(paramMap);
	}
	
}