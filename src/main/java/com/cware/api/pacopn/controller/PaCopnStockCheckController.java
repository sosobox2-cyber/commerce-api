package com.cware.api.pacopn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.BufferedReader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.pacopn.stockcheck.service.PaCopnStockCheckService;

@Api(value = "/pacopn/stockcheck", description="상품 재고 확인")
@Controller("com.cware.api.pacopn.PaCopnStockChckController")
@RequestMapping(value = "/pacopn/stockcheck")
public class PaCopnStockCheckController extends AbstractController {
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pacopn.stockcheck.paCopnStockCheckService")
	private PaCopnStockCheckService paCopnStockCheckService;
	
	@ApiOperation(value = "주문 상품 재고 확인", notes = "주문 상품 재고 확인", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	
	@RequestMapping(value = "/checkOrderGoodsInfo", method = RequestMethod.POST, headers="Accept=*/*", produces="application/json")
	@ResponseBody
	public String checkOrderGoodsInfo(HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		JSONObject jsonObject = null;
		JSONObject reqObject = null;
		
	 
        try{
        	log.info("===== 주문 상품 재고 확인 API Start =====");
        	
        	StringBuffer json = new StringBuffer();
        	String line = null;
        	
        	BufferedReader reader = request.getReader();
        	while((line = reader.readLine()) != null) {
        		json.append(line);
        	}
        	
        	reqObject = new JSONObject(json.toString());
        	
        	paramMap.put("sellerProductId", reqObject.getString("productId"));
        	paramMap.put("companyCode", reqObject.getString("companyCode"));
        	paramMap.put("salePrice", reqObject.getString("salePrice"));
        	paramMap.put("quantity", reqObject.getString("quantity"));
        	paramMap.put("vendorItemId", reqObject.getString("itemId"));
        	//paramMap.put("externalVendorSku", reqObject.getString("externalVendorSku"));
        	
        	jsonObject = paCopnStockCheckService.selectOrderableGoods(paramMap);
        	paramMap.put("code", "200");
        	paramMap.put("message", "OK");
        	
        	log.debug(jsonObject.toString());
        	
		}catch(Exception e){
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage());
			if(jsonObject == null) {
				jsonObject = new JSONObject();
        		jsonObject.put("productId", reqObject.getString("productId"));
        		jsonObject.put("isOrderable", "N");
        		jsonObject.put("resultCode", "INVALID_CODES");
        		jsonObject.put("quantity", reqObject.getString("quantity"));
        		jsonObject.put("itemId", reqObject.getString("itemId"));
        	}
			
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return jsonObject.toString();
		    
        }finally{
        	try{
        		paramMap.put("apiCode", "IF_PACOPN_STOCKCHECK");
				paramMap.put("resultCode", "200");
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", "PACOPN");
				paramMap.put("startDate", systemService.getSysdatetimeToString());
				paramMap.put("procId", "PACOPN");
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
        	
        	if(jsonObject == null) {
        		jsonObject = new JSONObject();
        		jsonObject.put("productId", reqObject.getString("productId"));
        		jsonObject.put("isOrderable", "N");
        		jsonObject.put("resultCode", "INVALID_CODES");
        		jsonObject.put("quantity", reqObject.getString("quantity"));
        		jsonObject.put("itemId", reqObject.getString("itemId"));
        	}
			
			log.info("===== 주문 상품 재고 확인 API END =====");  
        }

        return jsonObject.toString();
	}

}
