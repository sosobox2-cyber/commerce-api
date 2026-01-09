package com.cware.netshopping.pacopn.delivery.process.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PacopnorderlistVO;
import com.cware.netshopping.domain.model.Pacopnorderitemlist;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pacommon.order.process.PaOrderProcess;
import com.cware.netshopping.pacopn.delivery.process.PaCopnDeliveryProcess;
import com.cware.netshopping.pacopn.delivery.repository.PaCopnDeliveryDAO;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service("pacopn.delivery.paCopnDeliveryProcess")
public class PaCopnDeliveryProcessImpl extends AbstractService implements PaCopnDeliveryProcess{
	
	@Resource(name = "pacopn.delivery.paCopnDeliveryDAO")
	private PaCopnDeliveryDAO paCopnDeliveryDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "pacommon.order.paorderProcess")
	private PaOrderProcess paorderprocess;
	
	
	@Override
	public String savePaCopnOrder(PacopnorderlistVO paCopnOrderList, List<Pacopnorderitemlist> paCopnOrderitemList) throws Exception{
		Pacopnorderitemlist paCopnOrderitem = null;
		Paorderm paorderm = null;
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		int existsCnt   = 0;
		int executedRtn = 0;
		
		existsCnt = paCopnDeliveryDAO.selectPaCopnOrderListExists(paCopnOrderList);
		if(existsCnt > 0){ return Constants.SAVE_FAIL; }
		
		//부분 출고하여 배송번호 바뀐건 주문 있는지 체크.
		for(int i=0; i<paCopnOrderitemList.size(); i++){
			existsCnt = paCopnDeliveryDAO.selectPaCopnOrderItemListExists(paCopnOrderitemList.get(i));
			if(existsCnt > 0) return Constants.SAVE_FAIL; 
		}
		
		executedRtn = paCopnDeliveryDAO.insertPaCopnOrderList(paCopnOrderList);
		if(executedRtn < 1){
			throw processException("msg.cannot_save", new String[]{"TPACOPNORDERLIST INSERT"});
		}
		for(int i=0; i<paCopnOrderitemList.size(); i++){
			paCopnOrderitem = paCopnOrderitemList.get(i);
			
			executedRtn = paCopnDeliveryDAO.insertPaCopnOrderitemList(paCopnOrderitem);
			if(executedRtn < 1){
				throw processException("msg.cannot_save", new String[]{"TPACOPNORDERITEMLIST INSERT"});
			}
			
			paorderm = new Paorderm();
			paorderm.setPaOrderGb    ("10");
			paorderm.setPaCode       (paCopnOrderList.getPaCode());
			paorderm.setPaOrderNo    (paCopnOrderitem.getOrderId());
			paorderm.setPaOrderSeq   (paCopnOrderitem.getItemSeq());
			paorderm.setPaShipNo     (paCopnOrderitem.getShipmentBoxId());
			paorderm.setPaProcQty    (Long.toString(paCopnOrderitem.getShippingCount()));
			paorderm.setPaDoFlag     ("30");
			paorderm.setOutBefClaimGb("0");
			paorderm.setPaGroupCode  ("05");
			paorderm.setModifyId     (Constants.PA_COPN_PROC_ID);
			paorderm.setModifyDate   (paCopnOrderList.getModifyDate());
			paorderm.setInsertDate   (paCopnOrderList.getInsertDate());
			
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			if(executedRtn < 1){
				throw processException("msg.cannot_save", new String[]{"TPAORDERM INSERT"});
			}
		}
		return rtnMsg;
	}
	
	@Override
	public List<Object> selectBeforeInvoiceList() throws Exception{
		return paCopnDeliveryDAO.selectBeforeInvoiceList();
	}
	
	@SuppressWarnings("unchecked")
	public ParamMap shippingInvoiceProc(HashMap<String, String> apiInfo, HashMap<String, Object> orderInvoice) throws Exception {
		ParamMap resultMap = new ParamMap();
		
		List<Object> shippingInvoiceList = null;
		HashMap<String, Object> shippingInvoice = null;
		Paorderm paorderm = null;
		
		JsonObject requestObj       = null;
		JsonObject responseObj      = null;
		JsonArray  InvoiceList      = null;
		JsonObject invoice          = null;
		boolean splitShipping		= false;
		boolean preShipping			= false;
		JsonArray  responseList     = null;
		JsonObject response         = null;
		
		String shipmentBoxId = orderInvoice.get("PA_SHIP_NO").toString();
		String sysdate       = null;
		
		String[] apiKeys = null;
		String paName = null;
		
		int invoicesPerShipmentBoxId = 0;
		int executedCheck            = 0;
		int executedRtn              = 0;
		
		List<HashMap<String, Object>> shippingInfoList = new ArrayList<HashMap<String,Object>>();
		
		try{
			sysdate = DateUtil.getCurrentDateTimeAsString();
			
			if(Constants.PA_COPN_BROAD_CODE.equals(orderInvoice.get("PA_CODE").toString())) {
				paName = Constants.PA_BROAD;
			} else {
				paName = Constants.PA_ONLINE;
			}
			
			apiKeys = apiInfo.get(paName).split(";");
			
			// 분리배송 대상 여부 확인
			invoicesPerShipmentBoxId = paCopnDeliveryDAO.selectInvoicesPerShipmentBoxId(shipmentBoxId);
			
			// 송장업로드 처리 대상 조회
			shippingInvoiceList = paCopnDeliveryDAO.selectBeforeInvoiceDtList(shipmentBoxId);
			
			if(invoicesPerShipmentBoxId > 1){
				splitShipping = true;
			}
			if(splitShipping && invoicesPerShipmentBoxId > shippingInvoiceList.size()) {
				preShipping = true;
			}
			
			requestObj  = new JsonObject();
			InvoiceList = new JsonArray();
			
			try {
			
				for(int i=0; i<shippingInvoiceList.size(); i++) {
					shippingInvoice = (HashMap<String, Object>) shippingInvoiceList.get(i);
					executedCheck = paCopnDeliveryDAO.selectAlreadyExecuteShipping(shippingInvoice.get("MAPPING_SEQ").toString());
					
					if(executedCheck > 0) {
						resultMap.put("paDoFlag",  "40");
						resultMap.put("result_code", "200");
						resultMap.put("result_text", "송장업로드 처리 성공");
					}else{
						invoice = new JsonObject();
						invoice.addProperty("shipmentBoxId"        , Long.parseLong(shippingInvoice.get("PA_SHIP_NO").toString()));
						invoice.addProperty("orderId"              , Long.parseLong(shippingInvoice.get("PA_ORDER_NO").toString()));
						invoice.addProperty("vendorItemId"         , Long.parseLong(shippingInvoice.get("VENDOR_ITEM_ID").toString()));
						if(Long.parseLong(shippingInvoice.get("SLIP_PROC").toString()) >= 40){
							if(shippingInvoice.get("SLIP_NO") == null || "".equals(shippingInvoice.get("SLIP_NO").toString()) || !ComUtil.isNumber(shippingInvoice.get("SLIP_NO").toString())){
								invoice.addProperty("estimatedShippingDate", "");
								invoice.addProperty("deliveryCompanyCode"  , "DIRECT");
								invoice.addProperty("invoiceNumber"    , shippingInvoice.get("SLIP_I_NO").toString());
							}else{
								invoice.addProperty("estimatedShippingDate", "");
								invoice.addProperty("deliveryCompanyCode"  , shippingInvoice.get("PA_DELY_GB").toString());
								invoice.addProperty("invoiceNumber"    , shippingInvoice.get("SLIP_NO").toString());
							}
						}else{
							invoice.addProperty("estimatedShippingDate", shippingInvoice.get("ESTIMATED_DELIVERY_TIME").toString());
							invoice.addProperty("deliveryCompanyCode"  , shippingInvoice.get("PA_DELY_GB").toString());
							invoice.addProperty("invoiceNumber"        , "");
						}
						invoice.addProperty("splitShipping"        , splitShipping);
						invoice.addProperty("preSplitShipped"      , preShipping);
						
						InvoiceList.add(invoice);
						
						//운송장 변경을 위한 연동 운송장 정보 저장 (TPASLIPINFO)
						settingTpaSlipInfo(shippingInfoList , shippingInvoice);

					}
				}
				requestObj.addProperty("vendorId", apiKeys[0]);
				requestObj.add("orderSheetInvoiceApplyDtos", InvoiceList);
				
				log.info("COPN송장업로드 API 호출");
				responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(
						apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0])), "POST", new GsonBuilder().create().toJson(requestObj));
				
				if("200".equals(responseObj.get("code").getAsString())){
					responseList = responseObj.get("data").getAsJsonObject().get("responseList").getAsJsonArray();
					if(responseList.size() > 0){
						for(int j=0; j<responseList.size(); j++){
							response = responseList.get(j).getAsJsonObject();
						}
					}
				} else {
					resultMap.put("paDoFlag", "");
					resultMap.put("result_code", "404");
					resultMap.put("result_text", getMessage("errors.no.select"));
				}
				
				if(response != null){
					if("OK".equals(response.get("resultCode").getAsString())){
						resultMap.put("paDoFlag"   , "40");
						resultMap.put("result_code", "200");
						resultMap.put("result_text", "송장업로드 처리 성공");
					/*
					}else {
						resendToDirectDelivery(response, shippingInvoiceList, apiInfo , apiKeys , paName , splitShipping , preShipping , resultMap  );
					}
					*/
					
					}else if(response.get("resultMessage").toString().replace(" ","").indexOf("이미저장된송장번호") >= 0
						   ||response.get("resultMessage").toString().replace(" ","").indexOf("송장번호가유효하지") >= 0	
						   ||response.get("resultMessage").toString().replace(" ","").indexOf("Thereturninvoiceformatisincorrect") >= 0    //The return invoice format is incorrect
							){
					
						requestObj  = new JsonObject();
						InvoiceList = new JsonArray();
						
						for(int i=0; i<shippingInvoiceList.size(); i++) {
							shippingInvoice = (HashMap<String, Object>) shippingInvoiceList.get(i);
							
							invoice = new JsonObject();
							invoice.addProperty("shipmentBoxId"        , Long.parseLong(shippingInvoice.get("PA_SHIP_NO").toString()));
							invoice.addProperty("orderId"              , Long.parseLong(shippingInvoice.get("PA_ORDER_NO").toString()));
							invoice.addProperty("vendorItemId"         , Long.parseLong(shippingInvoice.get("VENDOR_ITEM_ID").toString()));
							invoice.addProperty("estimatedShippingDate", "");
							
							invoice.addProperty("deliveryCompanyCode"  , "DIRECT");
							invoice.addProperty("invoiceNumber"    , shippingInvoice.get("SLIP_I_NO").toString());
							
							invoice.addProperty("splitShipping"        , splitShipping);
							invoice.addProperty("preSplitShipped"      , preShipping);
							
							InvoiceList.add(invoice);							
						}
						
						//운송장 변경을 위한 연동 운송장 정보 저장 (TPASLIPINFO)
						settingTpaSlipInfo(shippingInfoList , shippingInvoiceList );
						
						requestObj.addProperty("vendorId", apiKeys[0]);
						requestObj.add("orderSheetInvoiceApplyDtos", InvoiceList);
						
						log.info("COPN송장업로드 API 재호출");
						responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(
								apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0])), "POST", new GsonBuilder().create().toJson(requestObj));
						
						if("200".equals(responseObj.get("code").getAsString())){
							responseList = responseObj.get("data").getAsJsonObject().get("responseList").getAsJsonArray();
							if(responseList.size() > 0){
								for(int j=0; j<responseList.size(); j++){
									response = responseList.get(j).getAsJsonObject();
									if("OK".equals(response.get("resultCode").getAsString())){
										resultMap.put("paDoFlag"   , "40");
										resultMap.put("result_code", "200");
										resultMap.put("result_text", "송장업로드 처리 성공");
									}else{
										resultMap.put("paDoFlag"   , "");
										resultMap.put("result_code", "500");
										resultMap.put("result_text", response.get("resultMessage").toString());
									}
								}
							}
						} else {
							resultMap.put("paDoFlag", "");
							resultMap.put("result_code", "404");
							resultMap.put("result_text", getMessage("errors.no.select"));
						}
						
					}else if(response.get("resultMessage").toString().replace(" ","").indexOf("업체직송은분리배송") >= 0){
						
						
						requestObj  = new JsonObject();
						InvoiceList = new JsonArray();
						//for(int i=0; i<shippingInvoiceList.size(); i++) {
						shippingInvoice = (HashMap<String, Object>) shippingInvoiceList.get(0);
						
						invoice = new JsonObject();
						invoice.addProperty("shipmentBoxId"        , Long.parseLong(shippingInvoice.get("PA_SHIP_NO").toString()));
						invoice.addProperty("orderId"              , Long.parseLong(shippingInvoice.get("PA_ORDER_NO").toString()));
						invoice.addProperty("vendorItemId"         , Long.parseLong(shippingInvoice.get("VENDOR_ITEM_ID").toString()));
						invoice.addProperty("estimatedShippingDate", "");
						
						invoice.addProperty("deliveryCompanyCode"  , "DIRECT");
						invoice.addProperty("invoiceNumber"    , shippingInvoice.get("SLIP_I_NO").toString());
						
						invoice.addProperty("splitShipping"        , false);
						invoice.addProperty("preSplitShipped"      , preShipping);
						
						InvoiceList.add(invoice);
						//운송장 변경을 위한 연동 운송장 정보 저장 (TPASLIPINFO)
						settingTpaSlipInfo(shippingInfoList , shippingInvoiceList);
						
						//}
						requestObj.addProperty("vendorId", apiKeys[0]);
						requestObj.add("orderSheetInvoiceApplyDtos", InvoiceList);
						
						log.info("COPN송장업로드 API 재호출");
						responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(
								apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0])), "POST", new GsonBuilder().create().toJson(requestObj));
						
						if("200".equals(responseObj.get("code").getAsString())){
							responseList = responseObj.get("data").getAsJsonObject().get("responseList").getAsJsonArray();
							if(responseList.size() > 0){
								for(int j=0; j<responseList.size(); j++){
									response = responseList.get(j).getAsJsonObject();
									if("OK".equals(response.get("resultCode").getAsString())){
										resultMap.put("paDoFlag"   , "40");
										resultMap.put("result_code", "200");
										resultMap.put("result_text", "송장업로드 처리 성공");
									}else{
										resultMap.put("paDoFlag"   , "");
										resultMap.put("result_code", "500");
										resultMap.put("result_text", response.get("resultMessage").toString());
									}
								}
							}
						} else {
							resultMap.put("paDoFlag", "");
							resultMap.put("result_code", "404");
							resultMap.put("result_text", getMessage("errors.no.select"));
						}
						
					}else{
						resultMap.put("paDoFlag"   , "");
						resultMap.put("result_code", "500");
						resultMap.put("result_text", response.get("resultMessage").toString());
					}
						
				}
			} catch(Exception e){
				resultMap.put("paDoFlag"   , "");
				resultMap.put("result_code", "500");
				resultMap.put("result_text", e.getMessage());
			}finally{
				for(int i=0; i<shippingInvoiceList.size(); i++) {
					shippingInvoice = (HashMap<String, Object>) shippingInvoiceList.get(i);
					
					if(Long.parseLong(shippingInvoice.get("SLIP_PROC").toString()) >= 40) {
						
						paorderm = new Paorderm();
						paorderm.setMappingSeq      (shippingInvoice.get("MAPPING_SEQ").toString());
						paorderm.setApiResultCode   (resultMap.getString("result_code"));
						paorderm.setApiResultMessage(resultMap.getString("result_text"));
						paorderm.setPaDoFlag        (resultMap.getString("paDoFlag"));
						paorderm.setProcDate        (DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
						
						executedRtn = paCopnDeliveryDAO.updatePaOrderResult(paorderm);
						if(executedRtn != 1){
							throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
						}else{
							if("200".equals(resultMap.getString("result_code"))){
								resultMap.put("rtnMsg", Constants.SAVE_SUCCESS);
							}else{
								resultMap.put("rtnMsg", Constants.SAVE_FAIL);
							}
						}
					}
				}
				
				if("200".equals(resultMap.getString("result_code")) && "40".equals(resultMap.getString("paDoFlag")) ) {
					for(Map<String, Object> shippingInfo :  shippingInfoList) {
						paorderprocess.insertTpaSlipInfo(shippingInfo);
					}
				}
			}
		}catch(Exception e){
			resultMap.put("rtnMsg", Constants.SAVE_FAIL);
		}
		
		return resultMap;
	}
	
	@Override
	public List<Object> selectShippingComplete(ParamMap paramMap) throws Exception{
		return paCopnDeliveryDAO.selectShippingComplete(paramMap);
	}
	
	@Override
	public ParamMap shippingCompleteProc(HashMap<String, String> apiInfo, HashMap<String, Object> shippingComplete) throws Exception{
		ParamMap resultMap = new ParamMap();
		
		Paorderm paorderm = null;
		
		JsonObject requestObj  = null;
		JsonObject responseObj = null;
		
		String[] apiKeys = null;
		String paName = null;
		
		String sysdate = null;
		
		int executedRtn = 0;
		
		try{
			sysdate = DateUtil.getCurrentDateTimeAsString();
			
			if(Constants.PA_COPN_BROAD_CODE.equals(shippingComplete.get("PA_CODE").toString())) {
				paName = Constants.PA_BROAD;
			} else {
				paName = Constants.PA_ONLINE;
			}
			
			apiKeys = apiInfo.get(paName).split(";");
			
			requestObj = new JsonObject();
			
			requestObj.addProperty("shipmentBoxId", shippingComplete.get("PA_SHIP_NO").toString());
			requestObj.addProperty("invoiceNumber", shippingComplete.get("SLIP_NO").toString());
			
			responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(
					apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0])), "POST", new GsonBuilder().create().toJson(requestObj));
			
			if("200".equals(responseObj.get("code").getAsString()) && responseObj.get("data").getAsBoolean() || 
				responseObj.get("message").toString().contains("배송상태: FINAL_DELIVERY")){
				resultMap.put("paDoFlag"   , "80");
				resultMap.put("result_code", "200");
				resultMap.put("result_text", "장기미배송 배송완료 처리 성공");
			}else{
				resultMap.put("paDoFlag"   , "");
				resultMap.put("result_code", "500");
				resultMap.put("result_text", responseObj.get("message").getAsString());
				log.info("장기미배송 배송완료 처리 결과" + responseObj.get("message").getAsString());
			}
			
		}catch(Exception e){
			resultMap.put("paDoFlag"   , "");
			resultMap.put("result_code", "500");
			resultMap.put("result_text", e.getMessage());
		}finally{
			paorderm = new Paorderm();
			paorderm.setMappingSeq      (shippingComplete.get("MAPPING_SEQ").toString());
			paorderm.setApiResultCode   (resultMap.getString("result_code"));
			paorderm.setApiResultMessage(resultMap.getString("result_text"));
			paorderm.setPaDoFlag        (resultMap.getString("paDoFlag"));
			paorderm.setProcDate        (DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			
			executedRtn = paCopnDeliveryDAO.updatePaOrderResult(paorderm);
			if(executedRtn != 1){
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}else{
				if("200".equals(resultMap.getString("result_code"))){
					resultMap.put("rtnMsg", Constants.SAVE_SUCCESS);
				}else{
					resultMap.put("rtnMsg", Constants.SAVE_FAIL);
				}
			}
		}
		return resultMap;
	}
	
	@Override
	public List<HashMap<String, Object>> selectDeliveryCompleteList() throws Exception{
		return paCopnDeliveryDAO.selectDeliveryCompleteList();
	}
	
	public String updateDeliveryComplete(Paorderm paOrderm) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		executedRtn = paCopnDeliveryDAO.updateDeliveryComplete(paOrderm);
		if(executedRtn != 1){
			throw processException("msg.cannot_save", new String[]{"TPAORDERM UPDATE"});
		}
		
		return rtnMsg;
	}
	
	
	@SuppressWarnings("unchecked")
	public void resendToDirectDelivery(JsonObject response, List<Object> shippingInvoiceList , HashMap<String, String> apiInfo ,String[] apiKeys, String paName, boolean splitShipping, boolean preShipping , ParamMap resultMap ) {
		
		if(shippingInvoiceList.size() < 1) return;
		
		
		//1) Retrun Message Check
		if(response.get("resultMessage").toString().replace(" ","").indexOf("이미저장된송장번호") >= 0
		 ||response.get("resultMessage").toString().replace(" ","").indexOf("송장번호가유효하지") >= 0	
		 ||response.get("resultMessage").toString().replace(" ","").indexOf("Thereturninvoiceformatisincorrect") >= 0 ) {  //The return invoice format is incorrect)
		
		}else if(response.get("resultMessage").toString().replace(" ","").indexOf("업체직송은분리배송") >= 0) {
			splitShipping = false;
		
		}else { //기타 에러의 경우는 일단 재연동 하지 않음
			resultMap.put("paDoFlag"   , "");
			resultMap.put("result_code", "500");
			resultMap.put("result_text", response.get("resultMessage").toString());
			return;
		}
		
		//1) ReSending
		try {
			JsonObject requestObj   = new JsonObject();
			JsonArray InvoiceList   = new JsonArray();
			JsonObject responseObj  = null;
			JsonArray  responseList = null;
			int totalSize 		    = shippingInvoiceList.size();
			JsonObject reResponse   = null;
			
			for(int i=0; i< totalSize ; i++) {
				HashMap<String, Object> shippingInvoice = (HashMap<String, Object>) shippingInvoiceList.get(i);
				
				JsonObject invoice = new JsonObject();
				invoice.addProperty("shipmentBoxId"        	, Long.parseLong(shippingInvoice.get("PA_SHIP_NO").toString()));
				invoice.addProperty("orderId"              	, Long.parseLong(shippingInvoice.get("PA_ORDER_NO").toString()));
				invoice.addProperty("vendorItemId"         	, Long.parseLong(shippingInvoice.get("VENDOR_ITEM_ID").toString()));
				invoice.addProperty("estimatedShippingDate"	, "");
				invoice.addProperty("deliveryCompanyCode"  	, "DIRECT");
				invoice.addProperty("invoiceNumber"    	   	, shippingInvoice.get("SLIP_I_NO").toString());
				invoice.addProperty("splitShipping"         , splitShipping);	//업체직송은분리배송 false 처리
				invoice.addProperty("preSplitShipped"       , preShipping);
				InvoiceList.add(invoice);
				
				if(!splitShipping) break; //업체직송은분리배송
				
			}
			requestObj.addProperty("vendorId", apiKeys[0]);
			requestObj.add("orderSheetInvoiceApplyDtos", InvoiceList);
			
			log.info("COPN송장업로드 API 재호출");
			responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder( apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0])), "POST", new GsonBuilder().create().toJson(requestObj));
			
			if("200".equals(responseObj.get("code").getAsString())){
				responseList = responseObj.get("data").getAsJsonObject().get("responseList").getAsJsonArray();
				if(responseList.size() > 0){
					for(int j=0; j<responseList.size(); j++){
						reResponse = responseList.get(j).getAsJsonObject();
						if("OK".equals(reResponse.get("resultCode").getAsString())){
							resultMap.put("paDoFlag"   , "40");
							resultMap.put("result_code", "200");
							resultMap.put("result_text", "송장업로드 처리 성공");
						}else{
							resultMap.put("paDoFlag"   , "");
							resultMap.put("result_code", "500");
							resultMap.put("result_text", reResponse.get("resultMessage").toString());
						}
					}
				}
			} else {
				resultMap.put("paDoFlag", "");
				resultMap.put("result_code", "404");
				resultMap.put("result_text", getMessage("errors.no.select"));
			}
			
		}catch (Exception e) {
			log.error(e.toString());
		}
	}
	
	
	public ParamMap shippingInvoiceUpdateProc(HashMap<String, String> apiInfo, Map<String, Object> orderInvoice) throws Exception{
		ParamMap resultMap									= new ParamMap();
		//List<HashMap<String, Object>> shippingInvoiceList 	= null;
		//HashMap<String, Object> shippingInvoice 			= null;
		JsonObject requestObj       						= null;
		JsonObject responseObj      						= null;
		JsonArray  InvoiceList      						= null;
		JsonObject invoice          						= null;
		boolean splitShipping								= false;
		boolean preShipping									= false;
		JsonArray  responseList     						= null;
		JsonObject response         						= null;
		String shipmentBoxId 								= orderInvoice.get("PA_SHIP_NO").toString();		
		String[] apiKeys 									= null;
		String paName 										= null;
		int invoicesPerShipmentBoxId 						= 0;	
		int sendInvoicePerShipmentBoxId						= 0; 
		boolean isTransfailYn		 						= false;
		List<HashMap<String, Object>> shippingInfoList 		= new ArrayList<HashMap<String,Object>>();
		requestObj  										= new JsonObject();
		InvoiceList 										= new JsonArray();
		
		try{	
			if(Constants.PA_COPN_BROAD_CODE.equals(orderInvoice.get("PA_CODE").toString())) {
				paName = Constants.PA_BROAD;
			} else {
				paName = Constants.PA_ONLINE;
			}
			apiKeys = apiInfo.get(paName).split(";");
			
			
			// 송장 연동 테이블 합,분리 배송 여부 조회     - COUNT(TPASLIPINFO.TRANS_PA_DELY_GB || TPASLIPINFO.TRANS_SLIP_NO )
			sendInvoicePerShipmentBoxId = paCopnDeliveryDAO.selectSendInvoicesPerShipmentBoxId(shipmentBoxId);  //sendInvoicePerShipmentBoxId - 1:합배송 , 2:분리배송
			// 영업 테이블 	     합, 분리 배송 여부 조회 -COUNT(TSLIPM.DELY_GB || TSLIPM.SLIP_NO)
			invoicesPerShipmentBoxId 	= paCopnDeliveryDAO.selectInvoicesPerShipmentBoxId(shipmentBoxId);		//invoicesPerShipmentBoxId    - 1:합배송 2: 분리배송
			

			//쿠팡 송장업데이트 정책 : 합배송 -> 분리배송 ,  분리배송 -> 합배송 불가 처리 CHECK
			if((sendInvoicePerShipmentBoxId == 1 && invoicesPerShipmentBoxId != 1)  || (sendInvoicePerShipmentBoxId != 1 && invoicesPerShipmentBoxId == 1) ) {
				//if((sendInvoicePerShipmentBoxId ! invoicesPerShipmentBoxId) ) { 분리배송 수가 다를수 있으므로 해당 로직 하면 안된다.
				shippingInfoList 	 = paCopnDeliveryDAO.selectUpdatingInvoiceDtList(shipmentBoxId);
				for(Map<String,Object> shippingInfo :  shippingInfoList) {
					shippingInfo.put("PA_GROUP_CODE"	, "05");
					shippingInfo.put("REMARK1_V"		, "합배송을 분리배송,분리배송을 합배송할  수 없습니다");
					shippingInfo.put("TRANS_YN"	 		, "0");
					shippingInfo.put("TRANS_PA_DELY_GB"	, shippingInfo.get("PA_DELY_GB").toString());
					shippingInfo.put("TRANS_INVOICE_NO"	, shippingInfo.get("INVOICE_NO").toString());
					paorderprocess.insertTpaSlipInfo(shippingInfo);
				}
				return resultMap;
			}	
			
			//송장업로드 처리 대상 조회 및 데이터 세팅
			shippingInfoList 	 = paCopnDeliveryDAO.selectUpdatingInvoiceDtList(shipmentBoxId);
			
			if(invoicesPerShipmentBoxId > 1){
				splitShipping = true; //분리배송 처리
			}
			if(splitShipping && invoicesPerShipmentBoxId > shippingInfoList.size()) {
				preShipping = true;  //preShipping == 이미 배송중인 분리배송건이 있으면 true
			}
							
			for(HashMap<String, Object> shippingInvoice :  shippingInfoList) {
				invoice = new JsonObject();
				invoice.addProperty("shipmentBoxId"        	, Long.parseLong(shippingInvoice.get("PA_SHIP_NO").toString()));
				invoice.addProperty("orderId"              	, Long.parseLong(shippingInvoice.get("PA_ORDER_NO").toString()));
				invoice.addProperty("deliveryCompanyCode"  	, shippingInvoice.get("PA_DELY_GB").toString());
				invoice.addProperty("invoiceNumber"    		, shippingInvoice.get("INVOICE_NO").toString());
				invoice.addProperty("vendorItemId"         	, Long.parseLong(shippingInvoice.get("VENDOR_ITEM_ID").toString()));
				invoice.addProperty("splitShipping"        	, splitShipping);
				invoice.addProperty("preSplitShipped"      	, preShipping);
				invoice.addProperty("estimatedShippingDate"	, "");
				InvoiceList.add(invoice);
					
				//settingTpaSlipInfo(shippingInfoList , shippingInvoice);
			}
				
			requestObj.addProperty("vendorId"			, apiKeys[0]);
			requestObj.add("orderSheetInvoiceApplyDtos"	, InvoiceList);
			
			responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0])), "POST", new GsonBuilder().create().toJson(requestObj));
				
			//운송장 UPDATE 연동 결과처리 
			if("200".equals(responseObj.get("code").getAsString())){
				responseList = responseObj.get("data").getAsJsonObject().get("responseList").getAsJsonArray();
				for(JsonElement responseElemnt: responseList) {
					response = responseElemnt.getAsJsonObject();
				}
			} else {
				isTransfailYn = true;
			}
			
			if(response != null){
				if(!"OK".equals(response.get("resultCode").getAsString())) {
					isTransfailYn = true;
				}
			}
			
			//송장연동 정보테이블(TPASLIPINFO) INSERT
			for(Map<String,Object> shippingInfo :   shippingInfoList) {
				if(isTransfailYn) {
					shippingInfo.put("REMARK1_V"	, "운송장 변경 실패");	
					shippingInfo.put("TRANS_YN"		, "0");	
				}else {
					shippingInfo.put("REMARK1_V"	, "운송장 변경 성공");	
					shippingInfo.put("TRANS_YN"		, "1");	
				}
				
				shippingInfo.put("TRANS_PA_DELY_GB"	, shippingInfo.get("PA_DELY_GB").toString());
				shippingInfo.put("TRANS_INVOICE_NO"	, shippingInfo.get("INVOICE_NO").toString());
				shippingInfo.put("PA_GROUP_CODE"	, "05");
				paorderprocess.insertTpaSlipInfo(shippingInfo);	
			}
			
		}catch(Exception e){
			resultMap.put("rtnMsg", Constants.SAVE_FAIL);
		}
		
		resultMap.put("rtnMsg", Constants.SAVE_SUCCESS); //TODO API MAKING
		
		return resultMap;
	}

	private void settingTpaSlipInfo(List<HashMap<String, Object>> shippingInfoList , HashMap<String, Object> shippingInvoice ) {
		HashMap<String, Object> shippingInfo		   = new HashMap<String, Object>();
		
		try {
			
			if(Long.parseLong(shippingInvoice.get("SLIP_PROC").toString()) >= 40){
				if(shippingInvoice.get("SLIP_NO") == null || "".equals(shippingInvoice.get("SLIP_NO").toString()) || !ComUtil.isNumber(shippingInvoice.get("SLIP_NO").toString())) {
					shippingInfo.put("TRANS_PA_DELY_GB"		, "DIRECT");
					shippingInfo.put("TRANS_INVOICE_NO"		, shippingInvoice.get("SLIP_I_NO").toString());	
					shippingInfo.put("PA_DELY_GB"			, shippingInvoice.get("PA_DELY_GB").toString());	
					shippingInfo.put("INVOICE_NO"			, "");
					shippingInfo.put("REMARK1_V"			, "운송장이 없는 배송건");
					shippingInfo.put("TRANS_YN"				, "1");
				}else {
					shippingInfo.put("TRANS_PA_DELY_GB"		, shippingInvoice.get("PA_DELY_GB").toString());
					shippingInfo.put("TRANS_INVOICE_NO"		, shippingInvoice.get("SLIP_NO").toString());
					shippingInfo.put("PA_DELY_GB"			, shippingInvoice.get("PA_DELY_GB").toString());
					shippingInfo.put("INVOICE_NO"			, shippingInvoice.get("SLIP_NO").toString());
					shippingInfo.put("REMARK1_V"			, "발송처리완료");
					shippingInfo.put("TRANS_YN"				, "1");
				}
				
				shippingInfo.put("PA_GROUP_CODE"	, "05");
				shippingInfo.put("MAPPING_SEQ"		, shippingInvoice.get("MAPPING_SEQ").toString());
			}
			shippingInfoList.add(shippingInfo);			
		}catch (Exception e) {
			log.error(e.toString());
		}
	
	}
	
	@SuppressWarnings("unchecked")
	private void settingTpaSlipInfo(List<HashMap<String, Object>> shippingInfoList , List<Object> shippingInvoiceList ) {
		Map<String, Object> shippingInfo		= new HashMap<String, Object>();
		HashMap<String, Object> shippingInvoice = null;
		shippingInfoList.clear();
		String slipNo = "";

		try {
			for(Object ob : shippingInvoiceList) {
				shippingInvoice = (HashMap<String, Object>) ob;
				if(shippingInvoice.get("SLIP_NO") != null && !"".equals(shippingInvoice.get("SLIP_NO").toString())) {
					slipNo  = shippingInvoice.get("SLIP_NO").toString();
				}else {
					slipNo  = "";
				}
				
				shippingInfo.put("PA_GROUP_CODE"	, "05");
				shippingInfo.put("MAPPING_SEQ"		, shippingInvoice.get("MAPPING_SEQ").toString());
				shippingInfo.put("TRANS_PA_DELY_GB"	, "DIRECT");
				shippingInfo.put("TRANS_INVOICE_NO"	, shippingInvoice.get("SLIP_I_NO").toString());	
				shippingInfo.put("PA_DELY_GB"		, shippingInvoice.get("PA_DELY_GB").toString());	
				shippingInfo.put("INVOICE_NO"		, slipNo);
				shippingInfo.put("TRANS_YN"			, "1");
				shippingInfo.put("REMARK1_V"		, "에러로 인한 재 배송처리");
			}	
		}catch (Exception e) {
			log.error(e.toString());
		}
	}

	@Override
	public List<Map<String, Object>> selectShippingUpdateList() throws Exception {
		return paCopnDeliveryDAO.selectShippingUpdateList();
	}

	
}
