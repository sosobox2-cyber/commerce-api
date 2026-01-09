package com.cware.netshopping.panaver.v3.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.v3.domain.ClaimRequestReasonType;
import com.cware.netshopping.panaver.v3.domain.OrderResponse;
import com.cware.netshopping.panaver.v3.domain.OrderResponseData;
import com.cware.netshopping.panaver.v3.process.PaNaverV3CancelProcess;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3CancelDAO;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3InfoCommonDAO;
import com.cware.netshopping.panaver.v3.service.PaNaverV3CancelService;
import com.cware.netshopping.panaver.v3.util.PaNaverV3ConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("panaver.v3.cancel.paNaverV3CancelProcess")
public class PaNaverV3CancelProcessImpl extends AbstractProcess implements PaNaverV3CancelProcess {
	
	@Autowired
	PaNaverV3CancelDAO paNaverV3CancelDAO;
	
	@Autowired
	PaNaverV3InfoCommonDAO paNaverV3InfoCommonDAO;
	
	@Autowired
	private PaNaverV3ConnectUtil paNaverV3ConnectUtil;
	
	@Resource(name = "panaver.v3.cancel.paNaverV3CancelService")
	private PaNaverV3CancelService paNaverV3CancelService;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Override
	public ResponseMsg approvalCancel(String orderId, String productOrderId, String outBefClaimGb, String procId, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PANAVERAPI_V3_03_007";
		String duplicateCheck = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// API 트래킹 로그 데이터 세팅
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", procId);

		log.info("======= 네이버 취소 요청 승인 API Start =======");
		
		try {
			// API 중복실행 체크
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			/** 1) 전문 데이터 세팅 및 호출 **/
			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();
			
			// Path Parameters
			String pathParameters = productOrderId;
			
			// VO 선언
			OrderResponse response = new OrderResponse();
			
			// 네이버 취소요청승인 호출 (단건)
			resultMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, null);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.convertValue(resultMap, OrderResponse.class);
			
			OrderResponseData responseData = response.getData();
			
			if (responseData != null && responseData.getSuccessProductOrderIds() != null && !responseData.getSuccessProductOrderIds().isEmpty()) {
				paramMap.put("code", "200");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 취소승인 성공");
				
				/** 2) 취소요청승인 대상 조회 (TPANAVERORDERLIST) **/
				ParamMap parameterMap = new ParamMap();
				parameterMap.put("productOrderId", productOrderId);
				parameterMap.put("orderId", orderId);
				
				Map<String, Object> cancelMap = null;
				cancelMap = paNaverV3CancelDAO.selectPaNaverOrdCancelApprovalList(parameterMap);
				cancelMap.put("resultCode", "00");
				cancelMap.put("resultMessage", paramMap.getString("message"));
				cancelMap.put("outBefClaimGb", outBefClaimGb);
				cancelMap.put("procId", procId);
				
				/** 3) 취소요청승인 데이터 생성 및 업데이트 **/
				paNaverV3CancelService.saveCancelApprovalProcTx(cancelMap);
			} else if (responseData != null && responseData.getFailProductOrderInfos() != null && !responseData.getFailProductOrderInfos().isEmpty()) {
				log.debug("CancelApprovalProc Error Msg : ", response.getData().getFailProductOrderInfos().get(0).getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 취소승인 실패 / ID : "+response.getTraceId() +"/ Msg : "+ response.getData().getFailProductOrderInfos().get(0).getMessage());
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 취소승인 실패 /  ID : "+response.getTraceId());
			}
		} catch (TransApiException ex) {
			paramMap.put("code", "500");
			paramMap.put("message", ex.getMessage());
		} catch (Exception e) {
			paNaverV3ConnectUtil.dealException(e, paramMap);	
			log.error(e.getMessage(), e);
		} finally {
			try {
				paNaverV3ConnectUtil.dealSuccess(paramMap, request);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			
			if (duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
			
			log.info("======= 네이버 취소 요청 승인 API End =======");
		}
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public String saveCancelApprovalProc(Map<String, Object> cancelMap) throws Exception {
		Paorderm paorderm = null;
		int executedRtn = 0;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		String rtnMsg = Constants.SAVE_SUCCESS;

		/** 취소승인처리 결과 INSERT & UPDATE **/
		//= TPANAVERCLAIMLIST INSERT
		String claimSeq = paNaverV3InfoCommonDAO.selectClaimSeq();
		ParamMap paramMap = new ParamMap();
		paramMap.put("productOrderId", cancelMap.get("PA_ORDER_SEQ"));
		paramMap.put("claimSeq", claimSeq);
		paramMap.put("claimId", cancelMap.get("CLAIM_ID"));
		
		if (paNaverV3CancelDAO.insertCancelDoneClaim(paramMap) != 1) {
			throw processException("msg.cannot_save", new String[] { "TPANAVERCLAIMLIST INSERT" });
		}
		
		//= TPANAVERORDERLIST UPDATE
		paramMap.put("claimType", "CANCEL");
		paramMap.put("claimStatus", "CANCEL_DONE");

		if (paNaverV3InfoCommonDAO.updateOrderListClaimInfo(paramMap) != 1) {
			throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST UPDATE" });
		}
		
		//= TPAORDERM INSERT
		paorderm = new Paorderm();
		paorderm.setPaCode(cancelMap.get("PA_CODE").toString());
		paorderm.setPaOrderGb("20");
		paorderm.setPaOrderNo(cancelMap.get("PA_ORDER_NO").toString());
		paorderm.setPaOrderSeq(cancelMap.get("PA_ORDER_SEQ").toString());
		paorderm.setPaShipNo(cancelMap.get("PA_SHIP_NO").toString());
		paorderm.setPaClaimNo(cancelMap.get("CLAIM_ID").toString());
		paorderm.setPaProcQty(cancelMap.get("PA_PROC_QTY").toString());
		paorderm.setApiResultCode(cancelMap.get("resultCode").toString());
		paorderm.setApiResultMessage(cancelMap.get("resultMessage").toString());
		paorderm.setPaDoFlag("20");
		paorderm.setOutBefClaimGb(cancelMap.get("outBefClaimGb").toString());
		paorderm.setPaGroupCode("04");
		paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId(cancelMap.get("procId").toString());
		
		executedRtn = paNaverV3CancelDAO.insertPaOrderM(paorderm);
		
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		return rtnMsg;
	}

	@Override
	public ResponseMsg requestCancel(String productOrderId, String paOrderNo, String paOrderSeq, String paCode, int cancelReasonCode, String procId, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PANAVERAPI_V3_03_006";
		String duplicateCheck = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// API 트래킹 로그 데이터 세팅
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", procId);

		log.info("======= 네이버 취소 요청 API Start =======");
		
		try {
			// API 중복실행 체크
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			/** 1) 전문 데이터 세팅 **/
			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();
			
			// Path Parameters
			String pathParameters = productOrderId;
			
			// Body 세팅
			ParamMap apiDataObject = new ParamMap();
			switch(cancelReasonCode) {
				case 0: apiDataObject.put("cancelReason", ClaimRequestReasonType.PRODUCT_UNSATISFIED.toString()); break;
				case 1: apiDataObject.put("cancelReason", ClaimRequestReasonType.DELAYED_DELIVERY.toString()); break;
				case 2: apiDataObject.put("cancelReason", ClaimRequestReasonType.SOLD_OUT.toString()); break;
			}
			
			// VO 선언
			OrderResponse response = new OrderResponse();
			
			/** 2) 네이버 취소요청 호출 **/
			resultMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.convertValue(resultMap, OrderResponse.class);
			
			OrderResponseData responseData = response.getData();
			
			if (responseData != null && responseData.getSuccessProductOrderIds() != null && !responseData.getSuccessProductOrderIds().isEmpty()) {
				paramMap.put("code", "200");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 취소요청 성공");
				paramMap.put("paOrderNo", paOrderNo);
				paramMap.put("paOrderSeq", ComUtil.objToStr(paOrderSeq));
				paramMap.put("paCode", paCode);
				paramMap.put("apiResultCode", "000000");
				paramMap.put("apiResultMessage",paramMap.getString("message"));
				paramMap.put("preCancelReason", apiDataObject.getString("cancelReason"));				

				/** 3) 취소요청 처리결과 업데이트 **/
				paNaverV3CancelService.saveCancelSaleTx(paramMap);
			} else if (responseData != null && responseData.getFailProductOrderInfos() != null && !responseData.getFailProductOrderInfos().isEmpty()) {
				log.debug("CancelRequestProc Error Msg : ", response.getData().getFailProductOrderInfos().get(0).getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") ID : "+response.getTraceId() +"/ Msg : "+ response.getData().getFailProductOrderInfos().get(0).getMessage() +"취소요청 실패");
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") ID : "+response.getTraceId() +"취소요청 실패");
			}
		} catch (TransApiException ex) {
			paramMap.put("code", "500");
			paramMap.put("message", ex.getMessage());
		} catch (Exception e) {
			paNaverV3ConnectUtil.dealException(e, paramMap);	
			log.error(e.getMessage(), e);
		} finally {
			try {
				paNaverV3ConnectUtil.dealSuccess(paramMap, request);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			
			if (duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
			
			log.info("======= 네이버 취소 요청 API End =======");
		}
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public String saveCancelSale(ParamMap paramMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			executedRtn = paNaverV3CancelDAO.updateCancelSale(paramMap);
			
			if (executedRtn == 1) {
				log.info("TPAORDERM update success");
			} else {
				log.info("TPAORDERM update fail");
				log.info("ordNo : " + paramMap.getString("ordNo") + ", ordPrdSeq : " + paramMap.getString("ordPrdSeq"));
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public int updatePreCancelYnTx(ParamMap paramMap) throws Exception {
		return paNaverV3CancelDAO.updatePreCancelYn(paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverV3CancelDAO.selectCancelInputTargetDtList(paramMap);
	}
}
