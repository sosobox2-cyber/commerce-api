package com.cware.netshopping.pacopn.stockcheck.process.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pacopn.stockcheck.process.PaCopnStockCheckProcess;
import com.cware.netshopping.pacopn.stockcheck.repository.PaCopnStockCheckDAO;

@Service("pacopn.stockcheck.paCopnStockCheckProcess")
public class PaCopnStockCheckProcessImpl extends AbstractService implements PaCopnStockCheckProcess {
	
	@Resource(name = "pacopn.stockcheck.paCopnStockCheckDAO")
	private PaCopnStockCheckDAO paCopnStockCheckDAO;

	@Override
	public JSONObject selectOrderableGoods(ParamMap paramMap) throws Exception {
		
		JSONObject returnJson = new JSONObject();
		HashMap<String, Object> resultMap = paCopnStockCheckDAO.selectOrderableGoods(paramMap);
		
		if(resultMap == null) {
			returnJson.put("resultCode", "INVALID_CODES");
			returnJson.put("isOrderable", "N");
			returnJson.put("productId", paramMap.getString("sellerProductId"));
			returnJson.put("quantity", paramMap.getString("quantity"));
			returnJson.put("itemId", paramMap.getString("vendorItemId"));
		} else {
			if(resultMap.get("SALE_PRICE").toString().equals(paramMap.getString("salePrice")) == false) {
				returnJson.put("resultCode", "PRICE_MISMATCH");
			}
			else {
				returnJson.put("resultCode", resultMap.get("STOCK_STAT"));
			}
			
			if(returnJson.get("resultCode").equals("OK")) {
				returnJson.put("isOrderable", "Y");
			}
			else {
				returnJson.put("isOrderable", "N");
			}
			returnJson.put("productId", resultMap.get("PRODUCT_ID"));
			returnJson.put("quantity", resultMap.get("QUANTITY"));
			returnJson.put("itemId", resultMap.get("VENDOR_ITEM_ID"));
		}
		
		
		return returnJson;
	}
	
}