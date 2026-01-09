package com.cware.netshopping.pacopn.claim.process.impl;

import java.sql.Timestamp;
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
import com.cware.netshopping.domain.model.Pacopnclaimitemlist;
import com.cware.netshopping.domain.model.Pacopnclaimlist;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pacommon.order.process.PaOrderProcess;
import com.cware.netshopping.pacopn.claim.process.PaCopnClaimProcess;
import com.cware.netshopping.pacopn.claim.repository.PaCopnClaimDAO;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Service("pacopn.claim.paCopnClaimProcess")
public class PaCopnClaimProcessImpl extends AbstractService implements PaCopnClaimProcess{
	
	@Resource(name = "pacopn.claim.paCopnClaimDAO")
	private PaCopnClaimDAO paCopnClaimDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "pacommon.order.paorderProcess")
	private PaOrderProcess paorderprocess;
	
	public String savePaCopnClaim(Pacopnclaimlist paCopnClaim, List<Pacopnclaimitemlist> paCopnClaimitemList, ParamMap paramMap) throws Exception{
		Pacopnclaimitemlist paCopnClaimitem = null;
//		Paorderm paorderm = null;
		
//		HashMap<String, Object> orderMap = null;
		
		String rtnMsg  = Constants.SAVE_SUCCESS;
		
		int existsCnt   = 0;
		int executedRtn = 0;
		
		//출하지시로 인한 대기 상태에서 직권취소가 인입된 경우
		if("RETURNS_COMPLETED".equals(paCopnClaim.getReceiptStatus())) {
			int waitClaimcnt = paCopnClaimDAO.selectCopnClaimList4AdminClaim(paCopnClaim);  //TPAORDERM.PA_ORDER_GB = 30이 없는 상태
			if(waitClaimcnt> 0) {
				//UPDATE TPACOPNCLAIMLIST.RECEIPT_STATUS = 05 AND TPACOPNCLAIMLIST.CANCEL_PROC_NOTE = '출하지시 중 직권 취소 완료'
				paCopnClaimDAO.updatePaCopnClaimListReceiptStatus(paCopnClaim);
				return Constants.SAVE_SUCCESS;
			}
		}
		
		existsCnt = paCopnClaimDAO.selectPaCopnClaimListExists(paCopnClaim);
		if(existsCnt > 0){
			return Constants.SAVE_FAIL; 
		}
		
		executedRtn = paCopnClaimDAO.insertPaCopnClaimList(paCopnClaim);
		if(executedRtn < 1){
			throw processException("msg.cannot_save", new String[]{"TPACOPNCLAIMLIST INSERT"});
		}
		
		for(int i=0; i<paCopnClaimitemList.size(); i++){
			paCopnClaimitem = paCopnClaimitemList.get(i);
			
			executedRtn = paCopnClaimDAO.insertPaCopnClaimitemList(paCopnClaimitem);
			if(executedRtn < 1){
				throw processException("msg.cannot_save", new String[]{"TPACOPNCLAIMITEMLIST INSERT"});
			}
			
			int preCancelYn = paCopnClaimDAO.selectPreCancelYn(paCopnClaimitem);
			
			if(preCancelYn == 1) {
				ParamMap paramMap1 = new ParamMap();
				paramMap1.put("paOrderNo"       , paCopnClaimitem.getOrderId());
				paramMap1.put("paShipNo"        , paCopnClaimitem.getShipmentBoxId());
				paramMap1.put("paOrderSeq"      , paCopnClaimitem.getItemSeq());
				paramMap1.put("paCode"		    , paramMap.getString("paCode"));
				paramMap1.put("apiResultCode"   , Constants.SAVE_SUCCESS);
				paramMap1.put("apiResultMessage", "주문 생성 전 취소인입");
				
				rtnMsg = saveOrderRejectProc(paramMap1);
				if(!Constants.SAVE_SUCCESS.equals(rtnMsg)){
					throw processException("msg.cannot_save", new String[]{"TPAORDERM PRE_CANCEL_YN UPDATE"});
				}
			}
			/*
			paorderm = new Paorderm();
			paorderm.setPaOrderGb    ("30");
			paorderm.setPaCode       (paramMap.getString("paCode"));
			paorderm.setPaOrderNo    (paCopnClaimitem.getOrderId());
			paorderm.setPaOrderSeq   (paCopnClaimitem.getItemSeq());
			paorderm.setPaShipNo     (paCopnClaimitem.getOrgShipmentBoxId());
			paorderm.setPaClaimNo    (paCopnClaimitem.getReceiptId());
			paorderm.setPaProcQty    (Long.toString(paCopnClaimitem.getCancelCount()));
			paorderm.setPaDoFlag     ("10");
			paorderm.setOutBefClaimGb("0");
			paorderm.setPaGroupCode  ("05");
			paorderm.setModifyId     (Constants.PA_COPN_PROC_ID);
			paorderm.setModifyDate   (paCopnClaim.getModifyDate());
			paorderm.setInsertDate   (paCopnClaim.getInsertDate());
			paorderm.setInsertId     ("PACOPN");
			
			if("RETURNS_COMPLETED".equals(paCopnClaim.getReceiptStatus())){
				orderMap = paCopnClaimDAO.selectPrentsOrderCreated(paCopnClaimitem);
				if("1".equals(orderMap.get("PRE_CANCEL_YN").toString()) && "0".equals(orderMap.get("CREATE_YN"))){
					paorderm.setPreCancelYn("1");
				}
			}
			
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}*/
			
		}
		return rtnMsg;
	}
	
	public List<Object> selectCancelList() throws Exception {
		return paCopnClaimDAO.selectCancelList();
	}
	
	public List<Object> selectCopnSoldOutordList() throws Exception {
		return paCopnClaimDAO.selectCopnSoldOutordList();
	}
	
	public ParamMap makeCancelListProc(HashMap<String, String> apiInfo, HashMap<String, Object> cancelMap) throws Exception{
		ParamMap resultMap 		= new ParamMap();
		ParamMap paramMap 		= new ParamMap();
		JsonObject responseObj  = null;
		JsonObject requestObj 	= null;
		String[] apiKeys 		= null;
		
		try{
			
			String paName = null;
			if(cancelMap.get("PA_CODE").toString().equals(Constants.PA_COPN_BROAD_CODE)) {
				apiKeys = apiInfo.get(Constants.PA_BROAD).split(";");
				paName = Constants.PA_BROAD;
			}
			else {
				apiKeys = apiInfo.get(Constants.PA_ONLINE).split(";");
				paName = Constants.PA_ONLINE;
			}
			
			paramMap.setParamMap(cancelMap);
			paramMap.replaceCamel();
			
			if(!("RETURNS_COMPLETED".equals(cancelMap.get("RECEIPT_STATUS").toString()) || "CANCEL".equals(cancelMap.get("RECEIPT_TYPE").toString()))) {
				requestObj = new JsonObject();
				
				requestObj.addProperty("vendorId"   , apiKeys[0]);
				requestObj.addProperty("receiptId"  , Long.valueOf(cancelMap.get("PA_CLAIM_NO").toString()));
				requestObj.addProperty("cancelCount", Long.valueOf(cancelMap.get("PA_PROC_QTY").toString()));
				
				responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(
						apiInfo.get("API_URL")
						.replaceAll("#vendorId#", apiKeys[0])
						.replaceAll("#receiptId#", cancelMap.get("PA_CLAIM_NO").toString())), "PATCH", new GsonBuilder().create().toJson(requestObj));
				
				if("200".equals(responseObj.get("code").getAsString())){
					if("SUCCESS".equals(responseObj.get("data").getAsJsonObject().get("resultCode").getAsString())){
						resultMap.put("result_code", Constants.SAVE_SUCCESS);
						resultMap.put("result_text", responseObj.get("data").getAsJsonObject().get("resultMessage").getAsString());
					}else{
						resultMap.put("result_code", "500");
						resultMap.put("result_text", responseObj.get("data").getAsJsonObject().get("resultMessage").getAsString());
					}
					//tpacopnclaimlist proc_flag 10
					paramMap.put("procFlag", "10");
					paCopnClaimDAO.updatePaCopnClaimListProcFlag(paramMap);
				}else{
					resultMap.put("result_code", "500");
					resultMap.put("result_text", responseObj.get("message").getAsString());
					//tpacopnclaimlist proc_flag 90
					paramMap.put("procFlag", "90");
					paCopnClaimDAO.updatePaCopnClaimListProcFlag(paramMap);
				}
			}
		}catch(Exception e){
			resultMap.put("result_code", "500");
			resultMap.put("result_text", e.getMessage());
		}finally{
			insertPaorderM(paramMap,resultMap);		
		}
		return resultMap;
	}
	
	@Override
	public List<Object> selectCancelInputTargetList() throws Exception{
		return paCopnClaimDAO.selectCancelInputTargetList();
	}
	
	@Override
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception{
		return paCopnClaimDAO.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception{
		return paCopnClaimDAO.updatePreCancelYn(preCancelMap);
	}
	
	@Override
	public List<Object> selectClaimList() throws Exception{
		return paCopnClaimDAO.selectClaimList();
	}
	
	@Override
	public ParamMap makeClaimListProc(HashMap<String, String> apiInfo, HashMap<String, Object> claimMap) throws Exception{
		ParamMap resultMap = new ParamMap();
		
		Paorderm paorderm = null;
		
		JsonObject responseObj = null;
		JsonObject requestObj  = null;
		
		String[] apiKeys = null;
		
		String sysdate  = null;
		
		int executedRtn = 0;
		
		HashMap<String, String> claimDetailList = new HashMap<String, String>();
		
		String sendSlipNo = "";
		
		try{
			sysdate = DateUtil.getCurrentDateTimeAsString();
			
			String paName = null;
			if(claimMap.get("PA_CODE").toString().equals(Constants.PA_COPN_BROAD_CODE)) {
				apiKeys = apiInfo.get(Constants.PA_BROAD).split(";");
				paName = Constants.PA_BROAD;
			}
			else {
				apiKeys = apiInfo.get(Constants.PA_ONLINE).split(";");
				paName = Constants.PA_ONLINE;
			}
			
			claimDetailList = paCopnClaimDAO.selectClaimDetailList(claimMap.get("PA_CLAIM_NO").toString());
			
			requestObj = new JsonObject();
			
			requestObj.addProperty("vendorId"           , apiKeys[0]);
			requestObj.addProperty("receiptId"          , Long.valueOf(claimMap.get("PA_CLAIM_NO").toString()));
			requestObj.addProperty("deliveryCompanyCode", claimDetailList.get("PA_DELY_GB").toString());
			if(claimDetailList.get("SLIP_NO") == null || "".equals(claimDetailList.get("SLIP_NO").toString()) || !ComUtil.isNumber(claimDetailList.get("SLIP_NO").toString())){
				requestObj.addProperty("invoiceNumber"    , claimDetailList.get("SLIP_I_NO").toString());
				sendSlipNo = claimDetailList.get("SLIP_I_NO").toString();
			}else{
				requestObj.addProperty("invoiceNumber"    , claimDetailList.get("SLIP_NO").toString());
				sendSlipNo = claimDetailList.get("SLIP_NO").toString();
			}
			
			responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(
					apiInfo.get("API_URL")
					.replaceAll("#vendorId#", apiKeys[0])
					.replaceAll("#receiptId#", claimMap.get("PA_CLAIM_NO").toString())), "PATCH", new GsonBuilder().create().toJson(requestObj));
			
			if("200".equals(responseObj.get("code").getAsString())){
				if("SUCCESS".equals(responseObj.get("data").getAsJsonObject().get("resultCode").getAsString())){
					resultMap.put("result_code", Constants.SAVE_SUCCESS);
					resultMap.put("result_text", responseObj.get("data").getAsJsonObject().get("resultMessage").getAsString());
				}else{
					resultMap.put("result_code", "500");
					resultMap.put("result_text", responseObj.get("data").getAsJsonObject().get("resultMessage").getAsString());
				}
			}else{
				resultMap.put("result_code", "500");
				resultMap.put("result_text", responseObj.get("message").getAsString());
			}
		}catch(Exception e){
			resultMap.put("paOrderGb"  , "");
			resultMap.put("result_code", "500");
			resultMap.put("result_text", e.getMessage());
		}finally{
			paorderm = new Paorderm();
			
			paorderm.setPaCode          (claimMap.get("PA_CODE").toString());
			//paorderm.setPaOrderNo       (claimMap.get("PA_ORDER_NO").toString());
			//paorderm.setPaOrderSeq      (claimMap.get("PA_ORDER_SEQ").toString());
			paorderm.setPaClaimNo       (claimMap.get("PA_CLAIM_NO").toString());
			//paorderm.setPaShipNo        (claimMap.get("PA_SHIP_NO").toString());
			paorderm.setApiResultCode   (resultMap.getString("result_code"));
			paorderm.setApiResultMessage(resultMap.getString("result_text"));
			paorderm.setPaDoFlag        ("40");
			paorderm.setProcDate        (DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setModifyId        ("PACOPN");
			
			executedRtn = paCopnClaimDAO.updatePaOrderResultForClaim(paorderm);
			if(executedRtn < 1){
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}else{
				if(Constants.SAVE_SUCCESS.equals(resultMap.getString("result_code"))){
					resultMap.put("rtnMsg", Constants.SAVE_SUCCESS);
				}else{
					resultMap.put("rtnMsg", Constants.SAVE_FAIL);
				}
			}
			
			//송장 이력 저장
			if("000000".equals(resultMap.getString("result_code"))) {
				Map<String, Object> shippingInfo = new HashMap<String, Object>();
				try {
					shippingInfo.put("MAPPING_SEQ"		,	claimDetailList.get("MAPPING_SEQ").toString());
					shippingInfo.put("PA_DELY_GB"		,	claimDetailList.get("PA_DELY_GB").toString()); //
					shippingInfo.put("INVOICE_NO"		,	claimDetailList.get("SLIP_NO").toString());
					shippingInfo.put("TRANS_PA_DELY_GB"	,	claimDetailList.get("PA_DELY_GB").toString());
					shippingInfo.put("TRANS_INVOICE_NO"	,	sendSlipNo); //해당 경우가 찍히는 경우가 있을까??? claimDetailList.get("SLIP_NO").toString()와 sendSlipNo가 다른경우..
					shippingInfo.put("REMARK1_V"		,	"취소거부 출고처리");
					shippingInfo.put("TRANS_YN"			,	"1");
					paorderprocess.insertTpaSlipInfo(shippingInfo);
				}catch (Exception e) {
					log.error(e.toString());
				}
			}
			
			
		}
		
		return resultMap;
	}
	
	@Override
	public List<Object> selectOrderClaimTargetList() throws Exception{
		return paCopnClaimDAO.selectOrderClaimTargetList();
	}
	
	@Override
	public List<Object> selectOrderClaimTargetDtList(ParamMap paramMap) throws Exception{
		return paCopnClaimDAO.selectOrderClaimTargetDtList(paramMap);
	}
	
	@Override
	public String selectClaimDelyGb(String companyName) throws Exception{
		return paCopnClaimDAO.selectClaimDelyGb(companyName);
	}
	
	@Override
	public List<Object> selectPickupList() throws Exception{
		return paCopnClaimDAO.selectPickupList();
	}
	
	@Override
	public HashMap<String, String> selectPickupDetailList(String paClaimNo) throws Exception{
		return paCopnClaimDAO.selectPickupDetailList(paClaimNo);
	}
	
	@Override
	public int updatePickupResult(HashMap<String, Object> pickup) throws Exception{
		return paCopnClaimDAO.updatePickupResult(pickup);
	}
	
	@Override
	public List<Object> selectReceiveConfirmList() throws Exception{
		return paCopnClaimDAO.selectReceiveConfirmList();
	}
	
	@Override
	public int updateReceiveConfirmResult(HashMap<String, Object> receive) throws Exception{
		return paCopnClaimDAO.updateReceiveConfirmResult(receive);
	}
	
	@Override
	public List<Object> selectReturnApprovalList() throws Exception{
		return paCopnClaimDAO.selectReturnApprovalList();
	}
	
	@Override
	public int updateReturnApprovalResult(HashMap<String, Object> returnApproval) throws Exception{
		return paCopnClaimDAO.updateReturnApprovalResult(returnApproval);
	}
	
	@Override
	public List<Object> selectReturnWithdrawList(String paCode) throws Exception{
		return paCopnClaimDAO.selectReturnWithdrawList(paCode);
	}
	
	@Override
	public String saveWithdrawList(HashMap<String, Object> resultWithdraw) throws Exception{
		Paorderm paorderm = null;
		
		String rtnMsg    = Constants.SAVE_SUCCESS;
		String paOrderGb = "";
		
		int executedRtn = 0;
			
		// insert TPACOPNCLAIMLIST
		executedRtn = paCopnClaimDAO.insertClaimCancel(resultWithdraw);
		if(executedRtn != 1){
			throw processException("msg.cannot_save", new String[] { "TPACOPNCLAIMLIST INSERT" });
		}
		
		paorderm = new Paorderm();
		
		paorderm.setPaCode       (resultWithdraw.get("paCode").toString());
		paorderm.setPaOrderNo    (resultWithdraw.get("orderId").toString());
		paorderm.setPaClaimNo    (resultWithdraw.get("cancelId").toString());
		paorderm.setPaDoFlag     ("20");
		paorderm.setPaOrderGb    ("31");
		paorderm.setInsertDate   ((Timestamp)resultWithdraw.get("insertDate"));
		paorderm.setModifyDate   ((Timestamp)resultWithdraw.get("insertDate"));
		paorderm.setPaGroupCode  ("05");
		paorderm.setInsertId     ("PACOPN");
		paorderm.setModifyId     ("PACOPN");
		
		executedRtn = paCopnClaimDAO.insertClaimCancelOrderM(paorderm);
		
		if(executedRtn < 0){
			throw processException("msg.cannot_save", new String[] { "TPAORDERM " + ("20".equals(paOrderGb) ? "UPDATE" : "INSERT")});
		}
		
		return rtnMsg;
	}
	
	@Override
	public List<Object> selectClaimCancelTargetList() throws Exception{
		return paCopnClaimDAO.selectClaimCancelTargetList();
	}
	
	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception{
		return paCopnClaimDAO.selectClaimCancelTargetDtList(paramMap);
	}
	
	@Override
	public int updatePaDoFlag(ParamMap paramMap) throws Exception{
		return paCopnClaimDAO.updatePaDoFlag(paramMap);
	}
	
	@Override
	public String saveOrderRejectProc(ParamMap paramMap) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		int executedRtn = 0;
		
		executedRtn = paCopnClaimDAO.updatePaOrdermPreCancel(paramMap);
		if(executedRtn != 1){
			throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
		}
		
		return rtnMsg;
	}
	
	@Override
	public HashMap<String, String> selectOrgShipmentBoxId(ParamMap paramMap) throws Exception{
		return paCopnClaimDAO.selectOrgShipmentBoxId(paramMap);
	}
	
	@Override
	public ParamMap makeClaimPaorderm() throws Exception {
		
		ParamMap resultMap = new ParamMap();
		ParamMap paramMap = new ParamMap();
		
		Paorderm paorderm = null;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		
		List<HashMap<String, Object>> claimInputTargetList = paCopnClaimDAO.selectClaimInputTargetList();
		
		int insertCnt = 0;
		
		for(HashMap<String, Object> claimInputTarget : claimInputTargetList) {
			
			try {
				
				paorderm = new Paorderm();
				
				paorderm.setPaCode          (claimInputTarget.get("PA_CODE").toString());
				paorderm.setPaOrderNo       (claimInputTarget.get("PA_ORDER_NO").toString());
				paorderm.setPaClaimNo       (claimInputTarget.get("PA_CLAIM_NO").toString());
				paorderm.setApiResultCode   (resultMap.getString("result_code"));
				paorderm.setApiResultMessage(resultMap.getString("result_text"));
				paorderm.setProcDate        (DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
				paorderm.setPaOrderGb    	("30");
				paorderm.setPaOrderSeq   	(claimInputTarget.get("PA_ORDER_SEQ").toString());
				paorderm.setPaProcQty    	(claimInputTarget.get("PA_PROC_QTY").toString());
				paorderm.setPaShipNo	 	(claimInputTarget.get("PA_SHIP_NO").toString());
				paorderm.setPaDoFlag     	("20");
				paorderm.setOutBefClaimGb	("0");
				paorderm.setPaGroupCode  	("05");
				paorderm.setModifyId     	(Constants.PA_COPN_PROC_ID);
				paorderm.setModifyDate   	(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
				paorderm.setInsertDate   	(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
				paorderm.setInsertId     	(Constants.PA_COPN_PROC_ID);
				
				paramMap.put("orderId", claimInputTarget.get("PA_ORDER_NO").toString());
				paramMap.put("paOrderSeq", claimInputTarget.get("PA_ORDER_SEQ").toString());
				paramMap.put("shipmentBoxId", claimInputTarget.get("PA_SHIP_NO").toString());
				
				if("RETURNS_COMPLETED".equals(claimInputTarget.get("RECEIPT_STATUS"))){
					HashMap<String, Object> orderMap = paCopnClaimDAO.selectPrentsOrderCreated(paramMap);
					if("1".equals(orderMap.get("PRE_CANCEL_YN").toString()) && "0".equals(orderMap.get("CREATE_YN"))){
						paorderm.setPreCancelYn("1");
					}
					paorderm.setPaDoFlag     	("60");
					paorderm.setApiResultMessage("직권취소로 인한 반품 생성");
				}
				
				int executedRtn = paCommonDAO.insertPaOrderM(paorderm);
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] {"TPAORDERM INSERT"});
				}
				else {
					insertCnt++;
				}
				
				paramMap.put("procFlag", "20");
				paramMap.put("paOrderNo", claimInputTarget.get("PA_ORDER_NO").toString());
				paramMap.put("paClaimNo", claimInputTarget.get("PA_CLAIM_NO").toString());
				paCopnClaimDAO.updatePaCopnClaimListProcFlag(paramMap);
				
			} catch (Exception e) {
				resultMap.put("rtnMsg", Constants.SAVE_FAIL);
			}
			
		}
		
		if(insertCnt != claimInputTargetList.size()) {
			resultMap.put("rtnMsg", Constants.SAVE_FAIL);
		}
		else {
			resultMap.put("rtnMsg", Constants.SAVE_SUCCESS);
		}
		
		return resultMap;
	}
	
	public ParamMap makeCancelProc(HashMap<String, String> apiInfo, ParamMap paramMap) throws Exception{
		ParamMap resultMap = new ParamMap();
		
		Paorderm paorderm = null;
		
		JsonObject responseObj = null;
		JsonObject requestObj  = null;
		
		String[] apiKeys = null;
		
		String sysdate = null;
		
		int executedRtn = 0;
		
		try{
			sysdate = DateUtil.getCurrentDateTimeAsString();
			
			String paName = null;
			if(paramMap.get("PA_CODE").toString().equals(Constants.PA_COPN_BROAD_CODE)) {
				apiKeys = apiInfo.get(Constants.PA_BROAD).split(";");
				paName = Constants.PA_BROAD;
			}
			else {
				apiKeys = apiInfo.get(Constants.PA_ONLINE).split(";");
				paName = Constants.PA_ONLINE;
			}
			
			requestObj = new JsonObject();
			
			requestObj.addProperty("vendorId"   , apiKeys[0]);
			requestObj.addProperty("receiptId"  , Long.valueOf(paramMap.get("PA_CLAIM_NO").toString()));
			requestObj.addProperty("cancelCount", Long.valueOf(paramMap.get("PA_PROC_QTY").toString()));
			
			responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(
					apiInfo.get("API_URL")
					.replaceAll("#vendorId#", apiKeys[0])
					.replaceAll("#receiptId#", paramMap.get("PA_CLAIM_NO").toString())), "PATCH", new GsonBuilder().create().toJson(requestObj));
			
			if("200".equals(responseObj.get("code").getAsString())){
				if("SUCCESS".equals(responseObj.get("data").getAsJsonObject().get("resultCode").getAsString())){
					resultMap.put("result_code", Constants.SAVE_SUCCESS);
					resultMap.put("result_text", responseObj.get("data").getAsJsonObject().get("resultMessage").getAsString());
					
					List<HashMap<String, Object>> cancelInfoList = paCopnClaimDAO.selectOrgItemInfoByCancelInfo(paramMap);
					
					int successCnt = 0;
					for(HashMap<String, Object> cancelInfo : cancelInfoList) {
						
						try {
							paorderm = new Paorderm();
							
							paorderm.setPaCode          (cancelInfo.get("PA_CODE").toString());
							paorderm.setPaOrderNo       (cancelInfo.get("PA_ORDER_NO").toString());
							paorderm.setPaClaimNo       (cancelInfo.get("PA_CLAIM_NO").toString());
							paorderm.setApiResultCode   (resultMap.getString("result_code"));
							paorderm.setApiResultMessage(resultMap.getString("result_text"));
							paorderm.setProcDate        (DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
							paorderm.setPaOrderGb    	("30");
							paorderm.setPaOrderSeq   	(cancelInfo.get("PA_ORDER_SEQ").toString());
							paorderm.setPaProcQty    	(cancelInfo.get("PA_PROC_QTY").toString());
							paorderm.setPaShipNo	 	(cancelInfo.get("PA_SHIP_NO").toString());
							paorderm.setPaDoFlag     	("20");
							paorderm.setOutBefClaimGb	("1");
							paorderm.setPaGroupCode  	("05");
							paorderm.setModifyId     	(Constants.PA_COPN_PROC_ID);
							paorderm.setModifyDate   	(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
							paorderm.setInsertDate   	(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
							paorderm.setInsertId     	(Constants.PA_COPN_PROC_ID);
							
							executedRtn = paCommonDAO.insertPaOrderM(paorderm);
							if (executedRtn < 0) {
								throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
							}
							else {
								successCnt++;
							}
						} catch (Exception e) {
							resultMap.put("result_code", Constants.SAVE_FAIL);
							resultMap.put("result_text", e.getMessage());
						}
						
					}
					
					if(successCnt != cancelInfoList.size()) {
						resultMap.put("result_code", Constants.SAVE_FAIL);
						resultMap.put("result_text", "PAORDERM 이 제대로 생성되지 않았습니다.");
					}
					
				}else{
					resultMap.put("result_code", "500");
					resultMap.put("result_text", responseObj.get("data").getAsJsonObject().get("resultMessage").getAsString());
				}
			}else{
				resultMap.put("result_code", "500");
				resultMap.put("result_text", responseObj.get("message").getAsString());
			}
		}catch(Exception e){
			resultMap.put("result_code", "500");
			resultMap.put("result_text", e.getMessage());
		}
		
		return resultMap;
	}
	
	@Override
	public String compareAddress(ParamMap param) throws Exception{
		return paCopnClaimDAO.compareAddress(param);
	}
	
	@Override
	public String compareExAddress(ParamMap param) throws Exception{
		return paCopnClaimDAO.compareExAddress(param);
	}
	
	
	private void insertPaorderM(ParamMap paramMap, ParamMap resultMap) throws Exception{

		List<HashMap<String, Object>> cancelInfoList = paCopnClaimDAO.selectOrgItemInfoByCancelInfo(paramMap);
		
		int successCnt  = 0;
		int executedRtn = 0;
		String sysdate  = DateUtil.getCurrentDateTimeAsString();
		
		
		for(HashMap<String, Object> cancelInfo : cancelInfoList) {
			
			try {
				Paorderm paorderm = new Paorderm();
				
				paorderm.setPaCode          (cancelInfo.get("PA_CODE").toString());
				paorderm.setPaOrderNo       (cancelInfo.get("PA_ORDER_NO").toString());
				paorderm.setPaClaimNo       (cancelInfo.get("PA_CLAIM_NO").toString());
				paorderm.setApiResultCode   (resultMap.getString("result_code"));
				paorderm.setApiResultMessage(resultMap.getString("result_text"));
				paorderm.setProcDate        (DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
				paorderm.setPaOrderGb    	("30");
				paorderm.setPaOrderSeq   	(cancelInfo.get("PA_ORDER_SEQ").toString());
				paorderm.setPaProcQty    	(cancelInfo.get("PA_PROC_QTY").toString());
				paorderm.setPaShipNo	 	(cancelInfo.get("PA_SHIP_NO").toString());
				paorderm.setPaDoFlag     	("20");
				paorderm.setOutBefClaimGb	("0");
				paorderm.setPaGroupCode  	("05");
				paorderm.setModifyId     	(Constants.PA_COPN_PROC_ID);
				paorderm.setModifyDate   	(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
				paorderm.setInsertDate   	(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
				paorderm.setInsertId     	(Constants.PA_COPN_PROC_ID);
				
				paramMap.put("orderId"			, cancelInfo.get("PA_ORDER_NO").toString());
				paramMap.put("paOrderSeq"		, cancelInfo.get("PA_ORDER_SEQ").toString());
				paramMap.put("shipmentBoxId"	, cancelInfo.get("PA_SHIP_NO").toString());
				
				if("RETURNS_COMPLETED".equals(cancelInfo.get("RECEIPT_STATUS").toString())){
					HashMap<String, Object> orderMap = paCopnClaimDAO.selectPrentsOrderCreated(paramMap);
					HashMap<String, Object> goodsMap = paCopnClaimDAO.selectOrderGoodsInfo(paramMap);
					
					String delyType = goodsMap.get("DELY_TYPE").toString();
					
					if("1".equals(orderMap.get("PRE_CANCEL_YN").toString()) && "0".equals(orderMap.get("CREATE_YN"))){
						paorderm.setPreCancelYn("1");
					}
					//직권 취소로 인한 출고전 반품 처리 및 취소 처리
					if("20".equals(cancelInfo.get("DO_FLAG").toString()) || "25".equals(cancelInfo.get("DO_FLAG").toString())) {
						paorderm.setOutBefClaimGb	("0");
						paorderm.setApiResultMessage("직권취소로 인한 취소처리");
					} else if ("10".equals(delyType)) {
						paorderm.setOutBefClaimGb	("0");
						paorderm.setApiResultMessage("당사 직송 상품 직권취소");
					} else {
						paorderm.setOutBefClaimGb	("1");
						paorderm.setApiResultMessage("직권취소로 인한 출고전 반품처리");
					}
				}
				
				executedRtn = paCommonDAO.insertPaOrderM(paorderm);
				if (executedRtn < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
				else {
					successCnt++;
				}
			} catch (Exception e) {
				resultMap.put("rtnMsg", Constants.SAVE_FAIL);
			}
		}
		
		
		if(successCnt != cancelInfoList.size()) {
			resultMap.put("rtnMsg", Constants.SAVE_FAIL);
		}
		else {
			resultMap.put("rtnMsg", Constants.SAVE_SUCCESS);
		}
	
	}

	@Override
	public List<HashMap<String, Object>> selectMailAlertEntpList(ParamMap paramMap) throws Exception {
		return paCopnClaimDAO.selectMailAlertEntpList(paramMap);
	}

	@Override
	public String saveMailAlert(ParamMap mailMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			executedRtn = paCopnClaimDAO.insertUms(mailMap);
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TUMS INSERT" });
			}
			
			// TPAORDERM REMARK6_V UPDATE
			executedRtn = paCopnClaimDAO.updateAlertStatus(mailMap);
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM REMARK6_V UPDATE" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		
		return rtnMsg;
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception {
		return paCopnClaimDAO.selectPaMobileOrderAutoCancelList(paramMap);
	}

}
