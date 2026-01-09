package com.cware.netshopping.pafaple.process.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.pafaple.controller.PaFapleAsyncController;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pafaple.domain.PaFapleExchangeListVO;
import com.cware.netshopping.pafaple.domain.PaFapleReturnListVO;
import com.cware.netshopping.pafaple.process.PaFapleClaimProcess;
import com.cware.netshopping.pafaple.repository.PaFapleCancelDAO;
import com.cware.netshopping.pafaple.repository.PaFapleClaimDAO;
import com.cware.netshopping.pafaple.service.PaFapleClaimService;
import com.cware.netshopping.pafaple.util.PaFapleApiRequest;
import com.cware.netshopping.pafaple.util.PaFapleConnectUtil;

@Service("pafaple.claim.paFapleClaimProcess")
public class PaFapleClaimProcessImpl extends AbstractProcess implements PaFapleClaimProcess {

	@Autowired
	private PaFapleConnectUtil paFapleConnectUtil;
	
	@Autowired
	private PaFapleApiRequest paFapleApiRequest;
	
	@Autowired
	private PaFapleClaimDAO paFapleClaimDAO;
	
	@Autowired
	private PaFapleCancelDAO paFapleCancelDAO;
	
	@Autowired
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Autowired
	@Qualifier("pafaple.claim.paFapleClaimService")
	private PaFapleClaimService paFapleClaimService;
	
	@Autowired
	private PaFapleAsyncController paFapleAsyncController;
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getReturnList(String processFlag, String fromDate, String toDate, HttpServletRequest request)
			throws Exception {
		String apiCode = "";
		if(processFlag.equals("1")) {
			apiCode = "IF_PAFAPLEAPI_03_016";
		}else {
			apiCode = "IF_PAFAPLEAPI_03_005";
		}
		String duplicateCheck = "";
		String paCode = "";
		
		ParamMap retrieveDate = null;
		ParamMap apiInfoMap = new ParamMap();
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		List<Map<String, Object>> returnList = null;
		
		apiInfoMap.put("apiCode", apiCode);
		
		try {
			log.info("======= 패션플러스 반품목록조회 API Start - {} =======");
			
			paFapleApiRequest.getApiInfo(apiInfoMap);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			apiInfoMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			retrieveDate = paFapleConnectUtil.setRetrieveDateFormat(fromDate, toDate, "1");
			fromDate = retrieveDate.getString("startDate");
			toDate = retrieveDate.getString("endDate");
			
			Map<String, String> apiRequestMap  = new HashMap<String, String>();
			apiRequestMap.put("ProcessFlag", processFlag);
			apiRequestMap.put("Sdate", fromDate);
			apiRequestMap.put("Edate", toDate);
			apiRequestMap.put("ChargeSelect", "0");
			
			int paFapleContractCnt = Constants.PA_FAPLE_CONTRACT_CNT;
			//로컬 세팅
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				paFapleContractCnt = 1;
			}
			
			for(int i = 0; i< paFapleContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				
				responseMap = paFapleConnectUtil.callPaFapleAPILegacy(apiInfoMap, apiRequestMap);
				
				if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(String.valueOf(responseMap.get("Status"))) && "".equals(String.valueOf(responseMap.get("Message")))) {
					if("1".equals(processFlag) || "2".equals(processFlag) ) {	// 1: 반품완료 , 2 : 반품미처리
						returnList = (List<Map<String, Object>>)responseMap.get("Recall");
						for(Map<String, Object> returnMap : returnList) {
							try {
								
								ParamMap param = new ParamMap();
								param.put("paCode", paCode);
								param.put("processFlag", processFlag);
								
								paFapleClaimService.savePaFapleReturnTx(returnMap, param);
								
							} catch(Exception e) {
								apiInfoMap.put("code", "500");
								apiInfoMap.put("message", e.getMessage());
								log.error("Exception occurs : " + e.getMessage());
							}
						}
					}
				}
			}
			log.info("======= 패션플러스 반품목록조회 API End - {} =======");
		} catch (Exception e) {
			log.error(e.getMessage());
			paFapleConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paFapleConnectUtil.closeApi(request, apiInfoMap);
		}
		
		returnInputMain(request);
		
		return new ResponseMsg(apiInfoMap.get("code").toString(),
				apiInfoMap.get("message").toString());
	}

	public void returnInputMain(HttpServletRequest request) throws Exception {
		String apiCode 			= "PAFAPLE_RETURN_INPUT";
		String duplicateCheck 	= "";
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start",apiCode);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("paOrderGb", "30");
			List<HashMap<String, Object>> claimInputTargetList = paFapleClaimService.selectClaimTargetList(paramMap);
			
			if (claimInputTargetList != null) {
				for(HashMap<String, Object> claimInputTarget : claimInputTargetList) {
					try {
						paFapleAsyncController.returnClaimAsync(claimInputTarget,request);
					} catch (Exception e) {
						log.error( " 패션플러스 반품 생성 오류", claimInputTarget.get("PA_ORDER_NO"),e.getMessage());
						continue;
					}
				}
			}
		}catch (Exception e) {
			log.info("{} : {}","패션플러스 반품 생성 오류", e.getMessage());
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", apiCode);
		}
	}

	private int insertPaOrderm(PaFapleReturnListVO returnListVO, Paorderm paorderm)  throws Exception {
		if (paorderm == null)
			paorderm = new Paorderm();

		Map<String, String> voMap = null;

		voMap = BeanUtils.describe(returnListVO);
		voMap.put("outBefClaimGb", "0");
		paorderm.setPaOrderNo(voMap.get("orderId"));
		paorderm.setPaClaimNo(voMap.get("sendBackId"));
		paorderm.setPaOrderSeq(voMap.get("itemId"));
		paorderm.setPaShipNo(voMap.get("sendBackSeq"));
		
		// 반품 인입 시 원주문 doFlag 확인
		if ("30".equals(String.valueOf(voMap.get("paOrderGb")))) {
			HashMap<String, Object> claim = paFapleClaimDAO.selectPaFapleClaimInfo(returnListVO);
			// 출하지시 상태일 경우 출고전반품 데이터로 세팅
			if ("30".equals(String.valueOf(claim.get("DO_FLAG")))) {
				paorderm.setRemark1V("출고전반품 대상");
			}
		}
		
		int executedRtn = 0;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		String paOrderGb = voMap.get("paOrderGb");
		String outClaimGb = voMap.get("outBefClaimGb");
		
		paorderm.setPaGroupCode(Constants.PA_FAPLE_GROUP_CODE);
		paorderm.setPaOrderGb(paOrderGb);
		paorderm.setPaCode(voMap.get("paCode"));
		
		paorderm.setPaProcQty(String.valueOf(voMap.get("quantity")));
		
		// 테스트 해보면서 이건 수정해야할 듯 .
		if ("반품완료".equals(ComUtil.objToStr(voMap.get("processFlagName")))) {
			paorderm.setPaDoFlag("60"); // 반품 완료일때
		} else if ("반품미처리".equals(ComUtil.objToStr(voMap.get("processFlagName")))) {
			paorderm.setPaDoFlag("20");
		} 

		paorderm.setOutBefClaimGb(outClaimGb);
		paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId(Constants.PA_FAPLE_PROC_ID);
		
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		
		if (executedRtn != 1) {
			log.error("Failed to INSERT orderNo(" + returnListVO.getOrderId() + ") into TPAORDERM");
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		
		return executedRtn;
	}
	
	private int insertPaOrdermExchange(PaFapleExchangeListVO exchangeListVO, Paorderm paorderm)  throws Exception {
		if (paorderm == null)
			paorderm = new Paorderm();
		
		Map<String, String> voMap = null;
		
		voMap = BeanUtils.describe(exchangeListVO);
		voMap.put("outBefClaimGb", "0");
		paorderm.setPaOrderNo(voMap.get("orderId"));
		paorderm.setPaClaimNo(voMap.get("claimId"));
		paorderm.setPaOrderSeq(voMap.get("itemId"));
		paorderm.setPaShipNo(voMap.get("exchangeOrderId"));
		paorderm.setPaShipSeq(voMap.get("exchange_item_id"));
		
		int executedRtn = 0;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		String paOrderGb = voMap.get("paOrderGb");
		String outClaimGb = voMap.get("outBefClaimGb");
		
		paorderm.setPaGroupCode(Constants.PA_FAPLE_GROUP_CODE);
		paorderm.setPaOrderGb(paOrderGb);
		paorderm.setPaCode(voMap.get("paCode"));
		
		paorderm.setPaProcQty(String.valueOf(voMap.get("orderQuantity")));
		paorderm.setChangeFlag("00");
		paorderm.setPaDoFlag("20");
		paorderm.setOutBefClaimGb(outClaimGb);
		paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId(Constants.PA_FAPLE_PROC_ID);
		
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		
		if (executedRtn != 1) {
			log.error("Failed to INSERT orderNo(" + exchangeListVO.getOrderId() + ") into TPAORDERM");
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		
		if ("40".equals(paOrderGb) || "41".equals(paOrderGb)) { // 교환일경우 45(회수) or 46(회수취소) 도 같이 생성
			paOrderGb = paOrderGb.equals("40") ? "45" : "46";
			paorderm.setPaOrderGb(paOrderGb);
			paorderm.setChangeFlag("00");
			paorderm.setPaDoFlag("20");
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		}
		
		return executedRtn;
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paFapleClaimDAO.selectClaimTargetList(paramMap);
	}

	@Override
	public List<Object> selectOrderClaimTargetDt30List(ParamMap paramMap) throws Exception {
		return paFapleClaimDAO.selectOrderClaimTargetDt30List(paramMap);
	}

	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paFapleClaimDAO.selectOrderCalimTargetDt20List(paramMap);
	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paFapleClaimDAO.compareAddress(paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg returnCompleProc(HttpServletRequest request) throws Exception {
		String apiCode = "IF_PAFAPLEAPI_03_006";
		String duplicateCheck = "";
		String resultCode = "";
		String errMsg = "";
		String paCode = "";
		
		ParamMap paramMap = new ParamMap();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		log.info("======= 패션플러스 반품완료 및 회수송장등록  API Start - {} =======");
		
		try {
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paFapleApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paFapleContractCnt = Constants.PA_FAPLE_CONTRACT_CNT;
			//로컬 세팅
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				paFapleContractCnt = 1;
			}
			
			for(int i = 0; i<paFapleContractCnt; i ++) {
				List<HashMap<String, Object>> RecallList = new ArrayList<HashMap<String,Object>>();
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				paramMap.put("paCode", paCode);
			
				// 대상조회 
				List<HashMap<String, Object>> fapleReturnCompleList = paFapleClaimDAO.selectFapleReturnCompleList(paramMap);
				
				if (fapleReturnCompleList == null) {
					return new ResponseMsg("200","반품 대상 없음 ");
				}
				
				log.info("03.API Request Setting");
				
				for (HashMap<String, Object> fapleReturnComple : fapleReturnCompleList) {
					
					HashMap<String, Object> returnCompleMap = new HashMap<String, Object>();
					
					returnCompleMap.put("SendbackId", Integer.parseInt(fapleReturnComple.get("PA_CLAIM_NO").toString())); //  반품ID
					returnCompleMap.put("SendbackIdseq", Integer.parseInt(fapleReturnComple.get("PA_SHIP_NO").toString())); //  반품순번
					returnCompleMap.put("ProcessFlag", "1"); //  반품완료여부
					returnCompleMap.put("ReturnInvoiceCo", fapleReturnComple.get("PA_DELY_GB").toString()); // 택배사코드
					returnCompleMap.put("ReturnInvoiceNo", fapleReturnComple.get("SLIP_NO").toString()); // 반품회수송장
					
					RecallList.add(returnCompleMap);
				}
				
				Map<String, Object> apiRequestMap  = new HashMap<String, Object>();
				
				apiRequestMap.put("RecallList", RecallList); 
				
				log.info("04.API Call");
				responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiRequestMap);	
				
				if (responseMap != null) {
					RecallList = new ArrayList<HashMap<String, Object>>();
					RecallList = (List<HashMap<String, Object>>)responseMap.get("RecallList");
					
					paramMap.put("API_RESULT_CODE", "200");
					paramMap.put("API_RESULT_MESSAGE", "반품 운송장 등록 완료");
					
					for (Map<String, Object> Recall : RecallList) {
						
						resultCode = String.valueOf(Recall.get("Status"));
						
						Map<String, Object> recallMap = paFapleClaimDAO.selectPaFapleClaimSendBack(Recall);
						
						try {
							if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(resultCode)) {
								
								recallMap.put("PA_DO_FLAG", "60");
								recallMap.put("API_RESULT_CODE", "200");
								recallMap.put("API_RESULT_MESSAGE", "반품 운송장 등록 완료");
								
								paFapleClaimDAO.updatePaOrderMDoFlag(recallMap);
							}else {
								recallMap.put("API_RESULT_CODE", String.valueOf(Recall.get("Status")));
								recallMap.put("API_RESULT_MESSAGE", String.valueOf(Recall.get("Message")));
								
								paFapleClaimDAO.updatePaOrderMDoFlag(recallMap);
							}
						} catch (Exception e) {
							//paramMap.put("API_RESULT_CODE", String.valueOf(Recall.get("Status")));
							//paramMap.put("API_RESULT_MESSAGE", "TPAORDERM RETURN COMPLETE UPDATE ("+ recallMap.get("PA_ORDER_NO") +")");
							errMsg += "PA_ORDER_NO : " + recallMap.get("PA_ORDER_NO") + "TPAORDERM DO_FLAG UPDATE FAIL";
							//throw processException("msg.cannot_save", new String[] { "TPAORDERM RETURN COMPLETE UPDATE ("+ recallMap.get("PA_ORDER_NO") +")" });
						}
						
					}
				}
			}
			if (StringUtils.isNotBlank(errMsg)) {
				paramMap.put("API_RESULT_CODE", "500");
				paramMap.put("API_RESULT_MESSAGE", errMsg);
				log.error(errMsg);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			paFapleConnectUtil.checkException(paramMap, e);
		} finally {
			paFapleConnectUtil.closeApi(request, paramMap);
		}
		log.info("======= 패션플러스 반품완료 및 회수송장등록  API End - {} =======");
		
		return new ResponseMsg(paramMap.get("API_RESULT_CODE").toString(),
				paramMap.get("API_RESULT_MESSAGE").toString());
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public ResponseMsg getReturnCancelList(HttpServletRequest request, String fromDate, String toDate) throws Exception {
		String apiCode = "IF_PAFAPLEAPI_03_007";
		String duplicateCheck = "";
		String resultCode = "";
		String paCode = "";
		int executedRtn = 0;
		
		ParamMap retrieveDate = null;
		ParamMap paramMap = new ParamMap();
		List<HashMap<String,Object>> ReturnWithdrawList = null;
		
		PaFapleReturnListVO returnListVO = null;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		
		log.info("======= 패션플러스 반품 철회 조회  API Start - {} =======");
		try {
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paFapleApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			retrieveDate = paFapleConnectUtil.setRetrieveDateFormat(fromDate, toDate, "1");
			fromDate = retrieveDate.getString("startDate");
			toDate = retrieveDate.getString("endDate");
			
			log.info("03.API Parameter Setting");
			Map<String, String> apiRequestMap  = new HashMap<String, String>();
			apiRequestMap.put("Sdate", fromDate);
			apiRequestMap.put("Edate", toDate);
			
			int paFapleContractCnt = Constants.PA_FAPLE_CONTRACT_CNT;
			//로컬 세팅
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				paFapleContractCnt = 1;
			}
			
			paramMap.put("API_RESULT_CODE", "200");
			paramMap.put("API_RESULT_MESSAGE", "조회된 내역이 없습니다.");
			
			for (int i = 0; i < paFapleContractCnt; i++) {
				paCode = (i == 0) ? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				
				log.info("04.API Call");
				responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiRequestMap);	
				
				if (responseMap != null) {
					ReturnWithdrawList = new ArrayList<HashMap<String, Object>>();
					ReturnWithdrawList = (List<HashMap<String, Object>>)responseMap.get("ReturnWithdrawList");
					
					paramMap.put("API_RESULT_CODE", "200");
					paramMap.put("API_RESULT_MESSAGE", "반품 철회 내역 조회 성공");
					
					resultCode = String.valueOf(responseMap.get("Status"));
					if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(String.valueOf(responseMap.get("Status")))) {
						for (Map<String, Object> ReturnWithdraw : ReturnWithdrawList) {
							try {
								ReturnWithdraw.put("pa_order_gb","31");
								
								ParamMap param = new ParamMap();
								param.put("paCode", paCode);
								paFapleClaimService.savePaFapleReturnCancelTx(ReturnWithdraw, param);
								
							} catch(Exception e) {
								paramMap.put("code", "500");
								paramMap.put("message", e.getMessage());
								log.error("Exception occurs : " + e.getMessage());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			paramMap.put("API_RESULT_CODE", "500");
			paramMap.put("API_RESULT_MESSAGE", e.getMessage());
			log.error("Exception occurs : " + e.getMessage());
			log.error(e.getMessage());
			paFapleConnectUtil.checkException(paramMap, e);
		} finally {
			paFapleConnectUtil.closeApi(request, paramMap);
		}
		log.info("======= 패션플러스 반품 철회 조회  API End - {} =======");
		
		claimCancelInputMain("31",request);
		
		return new ResponseMsg(paramMap.get("API_RESULT_CODE").toString(),
				paramMap.get("API_RESULT_MESSAGE").toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getExchangeList(HttpServletRequest request, String fromDate, String toDate, String claimGb) throws Exception {
		String duplicateCheck = "";
		String paCode = "";
		String apiCode = "";
		String claimStatus = "";
		int dateType = 0;
		
		Map<String, Object> responseMap = new HashMap<String, Object>();		//42: 교환 배송중 조회, 40: 교환 접수, 41 :교환 철회
		
		ParamMap retrieveDate = null;
		ParamMap apiInfoMap = new ParamMap();
		
		try {
			switch(claimGb) {
			case "40" : 	// 교환배송
				apiCode = "IF_PAFAPLEAPI_03_008";
				claimStatus = "039001";
				dateType = 1;
				break;
			case "42" : 	// 교환 배송중 
				apiCode = "IF_PAFAPLEAPI_03_014";
				claimStatus = "039013";
				dateType = 4;
				break;
			case "41" : 	// 교환 철회 대상 조회	
				apiCode = "IF_PAFAPLEAPI_03_013";
				claimStatus = "039011";
				dateType = 5;
				break;
			default:
				throw processException("msg.cannot_date", new String[] { " CLAIM_GB :: " + claimGb});
			}	
			
			apiInfoMap.put("apiCode", apiCode);
			
			log.info("======= 패션플러스 교환 접수 대상 조회 API Start - {} =======");
			
			paFapleApiRequest.getApiInfo(apiInfoMap);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			apiInfoMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			retrieveDate = paFapleConnectUtil.setRetrieveDateFormat(fromDate, toDate, "2");
			fromDate = retrieveDate.getString("startDate");
			toDate = retrieveDate.getString("endDate");
			
			int paFapleContractCnt = Constants.PA_FAPLE_CONTRACT_CNT;
			//로컬 세팅
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				paFapleContractCnt = 1;
			}
			
			for(int i = 0; i< paFapleContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				
				Map<String, Object> apiRequestMap  = new HashMap<String, Object>();
				
				apiRequestMap.put("PartnerLoginID", paCode.equals("E1") ? "skstoa" : "skstoaec");
				apiRequestMap.put("DateType", dateType);
				apiRequestMap.put("Sdate", fromDate);
				apiRequestMap.put("Edate", toDate);
				apiRequestMap.put("ClaimStatus", claimStatus);
		
				responseMap = paFapleConnectUtil.callPaFapleAPILegacy(apiInfoMap, apiRequestMap);

				switch(claimGb) {
				case "40" : case "41" :	// 교환배송
					if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(String.valueOf(responseMap.get("Status")))) {
						List<Map<String, Object>> exchageList = null;
						exchageList = (List<Map<String, Object>>)responseMap.get("ExchangeList");
						for(Map<String, Object> exchangeMap : exchageList) {
							try {
								paFapleClaimService.savePaFapleExchangeTx(exchangeMap,apiInfoMap);
							} catch (Exception e) {
								apiInfoMap.put("code", "500");
								apiInfoMap.put("message", e.getMessage());
								log.error("Exception occurs : " + e.getMessage());
							}
						}
					}
					break;
				case "42" : 
					exchangeUpdate(responseMap,apiRequestMap, apiInfoMap);
					break;
				}
				
				log.info("======= 패션플러스 교환 접수 대상 조회 API End - {} =======");
				
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			paFapleConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paFapleConnectUtil.closeApi(request, apiInfoMap);
		}

		switch(claimGb) {
		case "40" :
			exchangeInputMain(request);
			break;
		case "41" : 
			claimCancelInputMain("41",request);
			break;
		}
		
		return new ResponseMsg(apiInfoMap.get("code").toString(),
				apiInfoMap.get("message").toString());
	}
	
	@SuppressWarnings("unchecked")
	private ParamMap exchangeUpdate(Map<String, Object> responseMap, Map<String, Object> apiRequestMap, ParamMap apiInfoMap)  throws Exception {
		List<Map<String, Object>> exchageList = null;
		int executedRtn = 0;
		String errMsg = "";
		
		try {
			if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(String.valueOf(responseMap.get("Status")))) {
				exchageList = (List<Map<String, Object>>)responseMap.get("ExchangeList");
				for(Map<String, Object> exchangeMap : exchageList) {
					try {
						exchangeMap.put("paCode", apiInfoMap.get("paCode").toString());
						
						// tpaorderm UPDATE 
						executedRtn = paFapleClaimDAO.updateExchangeOrderNo(exchangeMap);
						if (executedRtn != 1) {
							throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE FAIL [ paOrderNo = "+ exchangeMap.get("order_id") + "]" });
						}
						
					}catch (Exception e) {
						errMsg += e.getMessage();
						log.error("Exception occurs : " + e.getMessage());
					}
				}
				
				if (StringUtils.isNotBlank(errMsg)) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", errMsg);
					log.error(errMsg);
				}
			}
		}catch(Exception e) {
			apiInfoMap.put("code", "500");
			apiInfoMap.put("message", e.getMessage());
			log.error("Exception occurs : " + e.getMessage());
		}
		
		return apiInfoMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getExchangeCancelList(HttpServletRequest request, String fromDate, String toDate) throws Exception {
		String apiCode = "IF_PAFAPLEAPI_03_013";
		String duplicateCheck = "";
		String paCode = "";
		
		Map<String, Object> responseMap = new HashMap<String, Object>();
		
		ParamMap retrieveDate = null;
		ParamMap apiInfoMap = new ParamMap();
		
		apiInfoMap.put("apiCode", apiCode);
		apiInfoMap.put("code", "200");
		apiInfoMap.put("message", "패션플러스 교환 철회 접수 대상 조회 API End");
		try {
			log.info("======= 패션플러스 교환 철회 접수 대상 조회 API Start - {} =======");
			
			paFapleApiRequest.getApiInfo(apiInfoMap);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			apiInfoMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			retrieveDate = paFapleConnectUtil.setRetrieveDateFormat(fromDate, toDate, "1");
			fromDate = retrieveDate.getString("startDate");
			toDate = retrieveDate.getString("endDate");
			
			int paFapleContractCnt = Constants.PA_FAPLE_CONTRACT_CNT;
			//로컬 세팅
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				paFapleContractCnt = 1;
			}
			
			for(int i = 0; i< paFapleContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				
				Map<String, Object> apiRequestMap  = new HashMap<String, Object>();
				
				apiRequestMap.put("PartnerLoginID", paCode.equals("E1") ? "skstoa" : "skstoaec");
				apiRequestMap.put("DateType", 5); // 5: 교환철회일
				apiRequestMap.put("Sdate", fromDate);
				apiRequestMap.put("Edate", toDate);
				apiRequestMap.put("ClaimStatus", "039011"); // 039011: 교환철회
				
				responseMap = paFapleConnectUtil.callPaFapleAPILegacy(apiInfoMap, apiRequestMap);
				
				if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(String.valueOf(responseMap.get("Status")))) {		
					List<Map<String, Object>> exchageList =	(List<Map<String, Object>>)responseMap.get("ExchangeList");
					for(Map<String, Object> exchangeMap : exchageList) {
						try {
							paFapleClaimService.savePaFapleExchangeTx(exchangeMap,apiInfoMap);
						} catch (Exception e) {
							apiInfoMap.put("code", "500");
							apiInfoMap.put("message", e.getMessage());
							log.error("Exception occurs : " + e.getMessage());
						} 
					} 
				}
				
				log.info("======= 패션플러스 교환 철회 접수 대상 조회 API End - {} =======");
				
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			paFapleConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paFapleConnectUtil.closeApi(request, apiInfoMap);
		}

		claimCancelInputMain("41",request);
		
		return new ResponseMsg(apiInfoMap.get("code").toString(),
				apiInfoMap.get("message").toString());
	}
	
	private void exchangeInputMain(HttpServletRequest request) throws Exception {
		String prg_id 			= "PAFAPLE_EXCAHANGE_INPUT";
		String duplicateCheck 	= "";
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("paOrderGb", "40");
			List<HashMap<String, Object>> claimInputTargetList = paFapleClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> claimInputTarget : claimInputTargetList) {
				try {
						paFapleAsyncController.changeClaimAsync(claimInputTarget,request);
				} catch (Exception e) {
						log.error( " TDEAL 반품 생성 오류", claimInputTarget.get("PA_ORDER_NO"),e.getMessage());
					continue;
				}
			}
			
		}catch (Exception e) {
			log.info("{} : {}","티딜 교환 생성 오류", e.getMessage());
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}

	private void claimCancelInputMain(String claimStatus,HttpServletRequest request) throws Exception {
		String return_prg_id 			= "PAFAPLE_RETURN_CANCEL_INPUT";
		String exchange_prg_id 			= "PAFAPLE_EXCHANGE_CANCEL_INPUT";
		String prg_id = "";
		String duplicateCheck 	= "";
		
		try {
			prg_id = claimStatus.equals("31")?  return_prg_id : exchange_prg_id;
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("paOrderGb", claimStatus);
			List<HashMap<String, Object>> cancelInputTargetList = paFapleClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> cancelInputTarget : cancelInputTargetList) {
				try {
					paFapleAsyncController.claimCancelAsync(cancelInputTarget,request);
				} catch (Exception e) {
					log.error( "FAPLE 클레임 업데이트 오류", e);
					continue;
				}
			}
			
		}catch (Exception e) {
			log.info("{} : {}","패션플러스 클레임 철회 생성 오류", e.getMessage());
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}
	
	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paFapleClaimDAO.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("changeFlag", changeFlag);
		paramMap.put("mappingSeq", mappingSeq);
		
		return paFapleClaimDAO.updatePaOrdermChangeFlag(paramMap);
	}

	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paFapleClaimDAO.selectClaimCancelTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paFapleClaimDAO.selectChangeCancelTargetDtList(paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg exchangeSlipOutProc(HttpServletRequest request) throws Exception {
		String apiCode = "IF_PAFAPLEAPI_03_009";
		String duplicateCheck = "";
		int executedRtn = 0;
		String paCode = "";
		String errMsg = "";
		String paShipNo = "";
		
		ParamMap apiInfoMap = new ParamMap();
		
		apiInfoMap.put("apiCode", apiCode);
		apiInfoMap.put("code", "200");
		apiInfoMap.put("message", " 패션플러스 교환 회수 송장 등록 Api");
		
		try {
			log.info("======= 패션플러스 교환 회수 송장 등록 Api Start - {} =======");
			
			paFapleApiRequest.getApiInfo(apiInfoMap);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			apiInfoMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paFapleContractCnt = Constants.PA_FAPLE_CONTRACT_CNT;
			//로컬 세팅
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				paFapleContractCnt = 1;
			}
			
			for(int i = 0; i< paFapleContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				
				Map<String, Object> responseMap = new HashMap<String, Object>();
				ArrayList<Map<String, Object>> ExReturnList = new ArrayList<Map<String, Object>>();
				ArrayList<Map<String, Object>> ResExReturnList = new ArrayList<Map<String, Object>>();
				
				List<HashMap<String,Object>> fapleExchangeSlipOutList = paFapleClaimDAO.selectFapleExchangeSlipOutList(apiInfoMap);
				
				if(fapleExchangeSlipOutList != null) {
					for(HashMap<String, Object> fapleExchangeSlipOut : fapleExchangeSlipOutList) {
						
						Map<String,Object> ExReturn = new HashMap<String, Object>();
						
						ExReturn.put("ClaimID", Integer.parseInt(fapleExchangeSlipOut.get("PA_CLAIM_NO").toString()));
						ExReturn.put("order_id", Integer.parseInt(fapleExchangeSlipOut.get("PA_ORDER_NO").toString()));
						ExReturn.put("item_id", Integer.parseInt(fapleExchangeSlipOut.get("PA_ORDER_SEQ").toString()));
						ExReturn.put("ExReturnInvoiceCo", fapleExchangeSlipOut.get("PA_DELY_GB"));
						ExReturn.put("ExReturnInvoiceNo", fapleExchangeSlipOut.get("SLIP_NO"));
						
						ExReturnList.add(ExReturn);
					}
						Map<String, Object> apiRequestMap  = new HashMap<String, Object>();
						apiRequestMap.put("ExReturnList", ExReturnList);
						
						responseMap = paFapleConnectUtil.callPaFapleAPILegacy(apiInfoMap, apiRequestMap);

						if (responseMap != null) {
							ResExReturnList = (ArrayList<Map<String, Object>>) responseMap.get("ExReturnList");
							
							for(Map<String, Object> ResExReturn : ResExReturnList) {
								try {
									// 기존에 있는 주문인지 확인 
									PaFapleExchangeListVO exchangeListVO = new PaFapleExchangeListVO();
									exchangeListVO.setItemId(ResExReturn.get("item_id").toString());
									exchangeListVO.setOrderId(ResExReturn.get("order_id").toString());
									exchangeListVO.setClaimId(ResExReturn.get("ClaimID").toString());
									exchangeListVO.setPaOrderGb("45");
									
									executedRtn = paFapleClaimDAO.countExchangeList(exchangeListVO);
									if (executedRtn < 1)
										continue;
									
									paShipNo = fapleExchangeSlipOutList.stream()
											.filter(slipOutProc -> String.valueOf(slipOutProc.get("PA_ORDER_NO"))
													.equals(String.valueOf(ResExReturn.get("order_id")))
													&& String.valueOf(slipOutProc.get("PA_CLAIM_NO"))
															.equals(String.valueOf(ResExReturn.get("ClaimID"))))
											.map(slipOutProc -> String.valueOf(slipOutProc.get("PA_SHIP_NO"))).findFirst()
											.orElse(null);
									
									
									if(Constants.PA_FAPLE_SUCCESS_STATUS.equals(ResExReturn.get("Status").toString())) {
										ResExReturn.put("PA_DO_FLAG", "60");
										ResExReturn.put("API_RESULT_CODE", "204");
										ResExReturn.put("API_RESULT_MESSAGE", "교환 회수 완료");
									}else {
										ResExReturn.put("API_RESULT_CODE", "500" );
										ResExReturn.put("API_RESULT_MESSAGE", "("+ ResExReturn.get("Status") +")"+ ResExReturn.get("Message"));
									}
										ResExReturn.put("PA_CLAIM_NO",  ResExReturn.get("ClaimID"));
										ResExReturn.put("PA_ORDER_NO",  ResExReturn.get("order_id"));
										ResExReturn.put("PA_ORDER_SEQ", ResExReturn.get("item_id"));
										ResExReturn.put("PA_ORDER_GB", "45");
										ResExReturn.put("PA_CODE", apiInfoMap.get("paCode"));
										ResExReturn.put("PA_SHIP_NO", paShipNo);
										
										executedRtn = paFapleClaimDAO.updatePaOrderMDoFlag(ResExReturn);
										if (executedRtn != 1)
											throw processException("msg.cannot_save", new String[] { " TPAORDERM(PA_DO_FLAG) UPDATE FAIL" });
								}catch(Exception e) {
									errMsg += "PA_ORDER_NO : " + ResExReturn.get("order_id") + " | 교환회수송장 등록 실패 :: (" + e.getMessage() + ") / ";
								}
								
							}
						}
				}else {
					errMsg += " 패션플러스 교환 회수 송장 등록 조회된 결과가 없습니다.";
				}
			}
			
			
			if (StringUtils.isNotBlank(errMsg)) {
				apiInfoMap.put("code", "500");
				apiInfoMap.put("message", errMsg);
				log.error(errMsg);
			}
			
			log.info("=======  패션플러스 교환 회수 송장 등록 Api End - {} =======");
		} catch (Exception e) {
			log.error(e.getMessage());
			paFapleConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paFapleConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseMsg(apiInfoMap.get("code").toString(),
				apiInfoMap.get("message").toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg exchangeSlipOutProcStart(HttpServletRequest request) throws Exception {
		String apiCode = "IF_PAFAPLEAPI_03_010";
		String duplicateCheck = "";
		int executedRtn = 0;
		String paCode  = "";
		String apiResultStatus ="";
		String mappingSeq = "";
		String paDoFlag = "";
		String errMsg = "";
		
		ParamMap apiInfoMap = new ParamMap();
		
		apiInfoMap.put("apiCode", apiCode);
		apiInfoMap.put("code", "200");
		apiInfoMap.put("message", " 패션플러스 교환 발송  Api");
		
		try {
			log.info("=======  패션플러스 교환 발송 Api Start - {} =======");
			paFapleApiRequest.getApiInfo(apiInfoMap);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			apiInfoMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paFapleContractCnt = Constants.PA_FAPLE_CONTRACT_CNT;
			//로컬 세팅
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				paFapleContractCnt = 1;
			}
			
			for(int i = 0; i< paFapleContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				
				Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
				
				
				List<HashMap<String,Object>> fapleExchangeSlipOutStartList = paFapleClaimDAO.selectFapleExchangeSlipOutStartList(apiInfoMap);
				
				if(fapleExchangeSlipOutStartList != null) {
					for (Map<String, Object> fapleExchangeSlipOutStart : fapleExchangeSlipOutStartList) {
						List<Map<String, Object>> ExchangeList = new ArrayList<Map<String,Object>>();
						Map<String, Object> fExchangeSlipOut 	= new HashMap<String, Object>() ;
						fExchangeSlipOut.put("ClaimID", Integer.parseInt(fapleExchangeSlipOutStart.get("PA_CLAIM_NO").toString()));
						fExchangeSlipOut.put("OrderID", Integer.parseInt(fapleExchangeSlipOutStart.get("PA_ORDER_NO").toString()));
						fExchangeSlipOut.put("ExchangeInvoicedate", fapleExchangeSlipOutStart.get("OUT_CLOSE_DATE"));
						fExchangeSlipOut.put("ExchangeInvoiceCo", fapleExchangeSlipOutStart.get("PA_DELY_GB"));
						fExchangeSlipOut.put("ExchangeInvoiceNo", fapleExchangeSlipOutStart.get("SLIP_NO"));
						
						ExchangeList.add(fExchangeSlipOut);
						
						Map<String, Object> apiRequestMap  = new HashMap<String, Object>();
						apiRequestMap.put("ExchangeList", ExchangeList);
						
						if(ExchangeList.size() > 0 ) {
							
							responseMap = paFapleConnectUtil.callPaFapleAPILegacy(apiInfoMap, apiRequestMap);
							
							if (responseMap != null) {
								ExchangeList = (List<Map<String, Object>>) responseMap.get("ExchangeList");
								
								for (Map<String, Object> ExchangeStart : ExchangeList) {
									
									try {
										apiResultStatus = String.valueOf(ExchangeStart.get("Status"));
										
										if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(apiResultStatus)){
											paDoFlag = "40";
											apiInfoMap.put("code", "000000");
											apiInfoMap.put("message", "교환 배송 운송장등록 완료");
										}else {
											paDoFlag = null;
											apiInfoMap.put("message", String.valueOf(ExchangeStart.get("Message")));
											errMsg += "FFAPLE_MAPPING_SEQ : " + mappingSeq + " | 교환 발송 실패 :: (" + String.valueOf(ExchangeStart.get("Message"))
											+ ") / ";
										}
										
										ExchangeStart.put("paDoFlag", paDoFlag);
									//	ExchangeStart.put("mappingSeq", fapleExchangeSlipOutStart.get("MAPPING_SEQ"));
										ExchangeStart.put("code", apiInfoMap.get("code"));
										ExchangeStart.put("message", apiInfoMap.get("message"));
										ExchangeStart.put("paCode", fapleExchangeSlipOutStart.get("PA_CODE"));
										ExchangeStart.put("paOrderNo", fapleExchangeSlipOutStart.get("PA_ORDER_NO"));
										ExchangeStart.put("paClaimNo", fapleExchangeSlipOutStart.get("PA_CLAIM_NO"));
										
										executedRtn = paFapleClaimDAO.updateExchangePaorderm(ExchangeStart);
										if(executedRtn < 1){
											errMsg += "FAPLE_ORDER_ID : " + ExchangeStart.get("OrderID") + " | TPAORDERM(PA_DO_FLAG) UPDATE FAIL / ";
										}
									}catch (Exception e) {
										errMsg += "FAPLE_ORDER_ID : " + ExchangeStart.get("OrderID") + " | TPAORDERM(PA_DO_FLAG) UPDATE FAIL / ";
									}
								}
							}
						}
					}
				}
			}
			
			if (StringUtils.isNotBlank(errMsg)) {
				apiInfoMap.put("code", "500");
				apiInfoMap.put("message", errMsg);
				log.error(errMsg);
			}
			
			log.info("=======  패션플러스 교환 발송 Api End - {} =======");
			
		}catch (Exception e) {
			log.error(e.getMessage());
			paFapleConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paFapleConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message"));
	}
	
	@Override
	public String compareAddressExchange(ParamMap paramMap) throws Exception {
		return paFapleClaimDAO.compareAddressExchange(paramMap);
	}


	@Override
	public int savePaFapleReturn(Map<String, Object> returnMap, ParamMap param) throws Exception {

		int executedRtn = 0;
		int checkReturnCnt = 0;
		int checkClaimDoFlag = 0;

		String returningTypeCode = "";
		Timestamp sysdateTime = systemService.getSysdatetime();
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		
		PaFapleReturnListVO returnListVO = new PaFapleReturnListVO();
		returnListVO.setPaCode(param.getString("paCode")); 
		returnListVO.setPaOrderGb("30"); // TCODE[J007] - 30(반품)
		returnListVO.setOrderId(ComUtil.objToStr(returnMap.get("order_id")));
		returnListVO.setItemId(ComUtil.objToStr(returnMap.get("item_id")));
		returnListVO.setProcessFlag(param.getString("processFlag")); 
		returnListVO.setProcessFlagName(ComUtil.objToStr(returnMap.get("ProcessFlagName")));
		returnListVO.setSendBackId(ComUtil.objToStr(returnMap.get("SendbackId")));
		returnListVO.setSendBackSeq(ComUtil.objToStr(returnMap.get("SendbackSeq")));
		returnListVO.setReceiptDate(DateUtil.toTimestamp(ComUtil.objToStr(returnMap.get("ReceiptDate")), DateUtil.FAPLE_DATE_FORMAT));
		returnListVO.setQuantity(ComUtil.objToLong(returnMap.get("quantity")));
		returnListVO.setReturningType(ComUtil.objToStr(returnMap.get("ReturningType")));
		returnListVO.setReturningTypeName(ComUtil.objToStr(returnMap.get("ReturningTypeName")));
		returnListVO.setChargeRateType(ComUtil.objToStr(returnMap.get("charge_rate_type")));
		returnListVO.setChargeRate(ComUtil.objToLong(returnMap.get("charge_rate")));
		returnListVO.setFe2(ComUtil.objToLong(returnMap.get("fe2")));
		returnListVO.setFe3(ComUtil.objToLong(returnMap.get("fe3")));
		returnListVO.setRecallCharge(ComUtil.objToStr(returnMap.get("recall_Charge")));
		returnListVO.setGoodsFlow(ComUtil.objToStr(returnMap.get("goodsflow")));
		returnListVO.setDeliveryName(ComUtil.objToStr(returnMap.get("delivery_name")));
		returnListVO.setInvoiceNo(ComUtil.objToStr(returnMap.get("invoice_no")));
		returnListVO.setInvoiceDate(DateUtil.toTimestamp(ComUtil.objToStr(returnMap.get("invoice_date").toString().trim()), DateUtil.FAPLE_DATE_FORMAT));
		returnListVO.setDlvLastDay(ComUtil.objToStr(returnMap.get("DlvLastday")));
		returnListVO.setProcessDate2(ComUtil.objToStr(returnMap.get("processDate2")));
		returnListVO.setReturnDeferYn(ComUtil.objToStr(returnMap.get("ReturnDeferYN")));
		returnListVO.setHoldDate(DateUtil.toTimestamp(ComUtil.objToStr(returnMap.get("HoldDate")), DateUtil.FAPLE_DATETIME_FORMAT));
		returnListVO.setHoldType(ComUtil.objToStr(returnMap.get("HoldType")));
		returnListVO.setProcessDate(DateUtil.toTimestamp(ComUtil.objToStr(returnMap.get("ProcessDate")), DateUtil.FAPLE_DATE_FORMAT));
		returnListVO.setProcessEmpN(ComUtil.objToStr(returnMap.get("ProcessEmpN")));
		returnListVO.setIsReProcYn(ComUtil.objToStr(returnMap.get("isReProcYN")));
		returnListVO.setReProcCreated(DateUtil.toTimestamp(ComUtil.objToStr(returnMap.get("reProcCreated")), DateUtil.FAPLE_DATETIME_FORMAT));
		returnListVO.setReCompleteEmpN(ComUtil.objToStr(returnMap.get("ReCompleteEmpN")));
		returnListVO.setIsRecallCancelYn(ComUtil.objToStr(returnMap.get("isRecallCancelYN")));
		returnListVO.setCancelCreated(DateUtil.toTimestamp(ComUtil.objToStr(returnMap.get("cancelCreated")), DateUtil.FAPLE_DATETIME_FORMAT));
		returnListVO.setClaimId(ComUtil.objToStr(returnMap.get("claimid")));
		returnListVO.setReShipToName(ComUtil.objToStr(returnMap.get("re_ship_to_name")));
		returnListVO.setReShipToZipcode(ComUtil.objToStr(returnMap.get("re_ship_to_zipcode")));
		returnListVO.setReShipToMobile(ComUtil.objToStr(returnMap.get("re_ship_to_mobile")));
		returnListVO.setReShipToPhone(ComUtil.objToStr(returnMap.get("re_ship_to_phone")));
		returnListVO.setReShipToStreet1(ComUtil.objToStr(returnMap.get("re_ship_to_street1")));
		returnListVO.setReShipToStreet2(ComUtil.objToStr(returnMap.get("re_ship_to_street2")));
		returnListVO.setInsertId(Constants.PA_FAPLE_PROC_ID);
		returnListVO.setInsertDate(sysdateTime);
		returnListVO.setModifyDate(sysdateTime);
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("claimGb", "30");
		hashMap.put("paClaimName", returnListVO.getReturningType());
		
		returningTypeCode = paFapleCancelDAO.selectCancelOrderReasonType(hashMap);
		returnListVO.setReturningTypeCode(returningTypeCode);
		
		// 1. 기존에 있는 주문인지 확인 
		checkReturnCnt = paFapleClaimDAO.countOrderList(returnListVO);
		if (checkReturnCnt < 1){
			// 교환 후 반품의 경우 기존 주문이 없음 
			if(paFapleClaimDAO.checkPaFapleExcahngeOrderId(returnListVO) > 0) {
				HashMap<String, Object > exchangeOrderIdMap = paFapleClaimDAO.selectPaFapleExchangeOrderId(returnListVO);
				
				if(exchangeOrderIdMap != null) {
					
					executedRtn = paFapleClaimDAO.insertPaFapleReturnList(returnListVO);
					if (executedRtn != 1) {
						throw processException("msg.cannot_save", new String[] { "TPAFAPLERETURNLIST INSERT" });
					}
					
					Paorderm paorderm = new Paorderm();
					
					paorderm.setPaOrderNo(exchangeOrderIdMap.get("PA_ORDER_NO").toString());
					paorderm.setPaClaimNo(returnListVO.getSendBackId());
					paorderm.setPaOrderSeq(exchangeOrderIdMap.get("PA_ORDER_SEQ").toString());
					paorderm.setPaShipNo(returnListVO.getSendBackSeq());
					paorderm.setRemark3N(Integer.parseInt(String.valueOf(returnListVO.getItemId())));
					paorderm.setRemark4N(Integer.parseInt(String.valueOf(returnListVO.getOrderId())));
					paorderm.setPaGroupCode(Constants.PA_FAPLE_GROUP_CODE);
					paorderm.setPaOrderGb("30");
					paorderm.setPaCode(exchangeOrderIdMap.get("PA_CODE").toString());
					paorderm.setPaProcQty(String.valueOf(returnListVO.getQuantity()));
					paorderm.setPaDoFlag("20");
					paorderm.setOutBefClaimGb("0");
					paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
					paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
					paorderm.setModifyId(Constants.PA_FAPLE_PROC_ID);
					
					executedRtn = paFapleClaimDAO.insertPaOrderMReturn(paorderm);
					
					if (executedRtn != 1) {
						log.error("Failed to INSERT orderNo(" + returnListVO.getOrderId() + ") into TPAORDERM");
						throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
					}
				}
				return 0;
			}
			return 0;
		}
		
		// 2. 중복데이터 등록되어 있는지 확인 , PA_CLAIM_NO 나중에 넘어오는거 확인하고 쿼리문 수정 필요할 듯 
		checkClaimDoFlag = paFapleClaimDAO.selectPaFapleReturnListCount(returnListVO);
		// 3. TPAFAPLERETURNLIST INSERT
		if (checkClaimDoFlag > 0) {
			if ("1".equals(param.getString("processFlag")) && checkClaimDoFlag != 60) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("PA_DO_FLAG", "60");
				map.put("API_RESULT_CODE", "000000");
				map.put("API_RESULT_MESSAGE", "직권 반품 완료");
				map.put("PA_CLAIM_NO", returnListVO.getSendBackId());
				map.put("PA_SHIP_SEQ", returnListVO.getSendBackSeq());
				
				map.put("PA_CODE", returnListVO.getPaCode());
				map.put("PA_ORDER_NO", returnListVO.getOrderId());
				map.put("PA_ORDER_SEQ", returnListVO.getItemId());
				map.put("PA_SHIP_NO", returnListVO.getInvoiceNo());	// 반품송장번호 임 
				map.put("PA_ORDER_GB", returnListVO.getPaOrderGb());
				map.put("PA_PROC_QTY", returnListVO.getQuantity());
				paFapleClaimDAO.updatePaOrderMDoFlag(map);
			}
			return 0;
		}else {
			executedRtn = paFapleClaimDAO.insertPaFapleReturnList(returnListVO);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAFAPLERETURNLIST INSERT" });
			}
		}
		
		executedRtn = insertPaOrderm(returnListVO, new Paorderm());
		if (executedRtn != 1)
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT(CLAIM30)" });
		
		return executedRtn;
	}

	@Override
	public int savePaFapleReturnCancel(Map<String, Object> returnWithdraw, ParamMap param) throws Exception {
		int executedRtn = 0;
		
		if(paFapleClaimDAO.countClaimList(returnWithdraw) > 0) return 0;
		
	 	PaFapleReturnListVO returnListVO = new PaFapleReturnListVO();
		returnListVO.setOrderId(ComUtil.objToStr(returnWithdraw.get("order_id")));
		returnListVO.setItemId(ComUtil.objToStr(returnWithdraw.get("item_id")));
		returnListVO.setSendBackId(ComUtil.objToStr(returnWithdraw.get("SendbackId")));
		returnListVO.setSendBackSeq(ComUtil.objToStr(returnWithdraw.get("SendbackSeq")));
		returnListVO.setWithdrawDate(DateUtil.toTimestamp(ComUtil.objToStr(returnWithdraw.get("WithdrawDate")), DateUtil.FAPLE_DATE_FORMAT));
		returnListVO.setPaOrderGb("31"); 
		returnListVO.setPaCode(param.getString("paCode"));
		returnListVO.setQuantity(ComUtil.objToLong(returnWithdraw.get("PA_PROC_QTY")));
		
		if ("31".equals(returnListVO.getPaOrderGb())) { // 반품철회API 수량 미제공
			Integer claimQty = paFapleClaimDAO.selectClaimQty(returnListVO);
			if(claimQty == null) claimQty = 1;
			
			returnListVO.setQuantity((long)claimQty);
		}
		
		executedRtn = paFapleClaimDAO.insertPaFapleReturnList(returnListVO);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAFAPLERETURNLIST INSERT" });
		}
		
		executedRtn = insertPaOrderm(returnListVO, new Paorderm());
		if (executedRtn != 1)
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT(CLAIM31)" });	

		return executedRtn;
	}

	@Override
	public int savePaFapleExchange(Map<String, Object> exchangeMap, ParamMap apiInfoMap) throws Exception {
		int executedRtn = 0;
		String reasonType = "";
		Timestamp sysdateTime = systemService.getSysdatetime();
		PaFapleExchangeListVO exchangeListVO = new PaFapleExchangeListVO();
		
		exchangeListVO.setPaCode(ComUtil.objToStr(apiInfoMap.get("paCode"))); 
		exchangeListVO.setPaOrderGb("IF_PAFAPLEAPI_03_008".equals(apiInfoMap.get("apiCode"))? "40" : "41");
		exchangeListVO.setOrderId(ComUtil.objToStr(exchangeMap.get("order_id")));
		exchangeListVO.setItemId(ComUtil.objToStr(exchangeMap.get("item_id")));
		exchangeListVO.setClaimStatus(ComUtil.objToStr(exchangeMap.get("ClaimStatus")));
		exchangeListVO.setClaimId(ComUtil.objToStr(exchangeMap.get("ClaimID")));
		exchangeListVO.setExchangeOrderId(ComUtil.objToStr(exchangeMap.get("exchange_order_id")));
		exchangeListVO.setDeliveryAmt(ComUtil.objToLong(exchangeMap.get("delivery_amt")));
		exchangeListVO.setBillToName(ComUtil.objToStr(exchangeMap.get("bill_to_name")));
		exchangeListVO.setOrderQuantity(ComUtil.objToInt(exchangeMap.get("order_quantity")));
		exchangeListVO.setReasonTypeName(ComUtil.objToStr(exchangeMap.get("ReasonTypeName")));
		exchangeListVO.setReturnInvoiceType(ComUtil.objToStr(exchangeMap.get("ReturnInvoce_type")));
		exchangeListVO.setInvoiceCoName(ComUtil.objToStr(exchangeMap.get("InvoiceCoName")));
		exchangeListVO.setInvoiceNo(ComUtil.objToStr(exchangeMap.get("invoice_no")));
		exchangeListVO.setCreatedReturn(DateUtil.toTimestamp(ComUtil.objToStr(exchangeMap.get("Created_Return")), DateUtil.FAPLE_DATETIME_FORMAT));
		exchangeListVO.setCreatedRegist(DateUtil.toTimestamp(ComUtil.objToStr(exchangeMap.get("Created_Regist")), DateUtil.FAPLE_DATETIME_FORMAT));
		exchangeListVO.setModified(DateUtil.toTimestamp(ComUtil.objToStr(exchangeMap.get("modified")), DateUtil.FAPLE_DATETIME_FORMAT));
		exchangeListVO.setProductName(ComUtil.objToStr(exchangeMap.get("product_name")));
		exchangeListVO.setSku(ComUtil.objToStr(exchangeMap.get("sku")));
		exchangeListVO.setAttrText(ComUtil.objToStr(exchangeMap.get("attr_text")));
		exchangeListVO.setOadjustAdjustedprice(ComUtil.objToLong(exchangeMap.get("oadjust_adjustedprice")));
		exchangeListVO.setFeeTarget(ComUtil.objToStr(exchangeMap.get("fee_target")));
		exchangeListVO.setRateTypeName(ComUtil.objToStr(exchangeMap.get("RateTypeName")));
		exchangeListVO.setRegPath(ComUtil.objToStr(exchangeMap.get("RegPath")));
		exchangeListVO.setDeliveryDate(DateUtil.toTimestamp(ComUtil.objToStr(exchangeMap.get("delivery_date")), DateUtil.FAPLE_DATETIME_FORMAT));
		exchangeListVO.setDeliveryCoName(ComUtil.objToStr(exchangeMap.get("DeliveryCoName")));
		exchangeListVO.setExInvoiceNo(ComUtil.objToStr(exchangeMap.get("ex_invoice_no")));
		exchangeListVO.setDlvLastProcTime(DateUtil.toTimestamp(ComUtil.objToStr(exchangeMap.get("DlvLastProcTime")), DateUtil.FAPLE_DATETIME_FORMAT));
		exchangeListVO.setExShipToName(ComUtil.objToStr(exchangeMap.get("ex_ship_to_name")));
		exchangeListVO.setExShipToMobile(ComUtil.objToStr(exchangeMap.get("ex_ship_to_mobile")));
		exchangeListVO.setExShipToPhone(ComUtil.objToStr(exchangeMap.get("ex_ship_to_phone")));
		exchangeListVO.setExShipToStreet(ComUtil.objToStr(exchangeMap.get("ex_ship_to_street")).trim());
		exchangeListVO.setExShipToZip(ComUtil.objToStr(exchangeMap.get("ex_ship_to_zip")).trim());
		exchangeListVO.setCreatedDefer(DateUtil.toTimestamp(ComUtil.objToStr(exchangeMap.get("Created_Defer")), DateUtil.FAPLE_DATETIME_FORMAT));
		exchangeListVO.setExchangeStepName(ComUtil.objToStr(exchangeMap.get("ExchangeStepName")));
		exchangeListVO.setCreatedSend(DateUtil.toTimestamp(ComUtil.objToStr(exchangeMap.get("Created_Send")), DateUtil.FAPLE_DATETIME_FORMAT));
		exchangeListVO.setCreatedWithdrawal(DateUtil.toTimestamp(ComUtil.objToStr(exchangeMap.get("Created_Withdrawal")), DateUtil.FAPLE_DATETIME_FORMAT));
		exchangeListVO.setWithdrawalReason(ComUtil.objToStr(exchangeMap.get("WithdrawalReason")));
		exchangeListVO.setWithdrawalEmpName(ComUtil.objToStr(exchangeMap.get("WithdrawalEmpName")));
		exchangeListVO.setExSku(ComUtil.objToStr(exchangeMap.get("ex_sku")));
		exchangeListVO.setExName(ComUtil.objToStr(exchangeMap.get("ex_name")));
		exchangeListVO.setExAttrText(ComUtil.objToStr(exchangeMap.get("ex_attr_text")));
		exchangeListVO.setExGoodsId(ComUtil.objToStr(exchangeMap.get("ex_goods_id")));
		exchangeListVO.setReShipToName(ComUtil.objToStr(exchangeMap.get("re_ship_to_name")));
		exchangeListVO.setReShipToZipcode(ComUtil.objToStr(exchangeMap.get("re_ship_to_zipcode")));
		exchangeListVO.setReShipToMobile(ComUtil.objToStr(exchangeMap.get("re_ship_to_mobile")));
		exchangeListVO.setReShipToPhone(ComUtil.objToStr(exchangeMap.get("re_ship_to_phone")));
		exchangeListVO.setReShipToStreet1(ComUtil.objToStr(exchangeMap.get("re_ship_to_street1")));
		exchangeListVO.setReShipToStreet2(ComUtil.objToStr(exchangeMap.get("re_ship_to_street2")));
		
		exchangeListVO.setInsertId(Constants.PA_FAPLE_PROC_ID);
		exchangeListVO.setInsertDate(sysdateTime);
		exchangeListVO.setModifyDate(sysdateTime);
		
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("paClaimName", exchangeListVO.getReasonTypeName());
		hashMap.put("claimGb", "40");
		
		// 교환 사유가 한글로만 나오기 때문에 코드 조회 추가 
		reasonType = paFapleCancelDAO.selectCancelOrderReasonType(hashMap);
		exchangeListVO.setReasonType(reasonType);
		
		executedRtn = paFapleClaimDAO.countExchangeList(exchangeListVO);
		if (executedRtn > 0) {
			throw processException("msg.cannot_save", new String[] { "TPAFAPLEEXCHANGELIST IS EXIST" });
		}
		
		executedRtn = paFapleClaimDAO.selectPaFapleExchangeListCount(exchangeListVO);
		
		if (executedRtn == 0) {
			executedRtn = paFapleClaimDAO.insertPaFapleExchangeList(exchangeListVO);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAFAPLEEXCHANGELIST INSERT" });
			}
			executedRtn = insertPaOrdermExchange(exchangeListVO, new Paorderm());
			if (executedRtn != 1)
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT(CLAIM40 or CLAIM41)" });
		}
		
		return executedRtn;
	}

}
