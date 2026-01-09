package com.cware.netshopping.pafaple.process.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.pafaple.controller.PaFapleAsyncController;
import com.cware.framework.core.basic.AbstractModel;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pafaple.domain.PaFapleCancelListVO;
import com.cware.netshopping.pafaple.process.PaFapleCancelProcess;
import com.cware.netshopping.pafaple.repository.PaFapleCancelDAO;
import com.cware.netshopping.pafaple.service.PaFapleCancelService;
import com.cware.netshopping.pafaple.util.PaFapleApiRequest;
import com.cware.netshopping.pafaple.util.PaFapleComUtil;
import com.cware.netshopping.pafaple.util.PaFapleConnectUtil;

@Service("pafaple.cancel.paFapleCancelProcess")
public class PaFapleCancelProcessImpl extends AbstractProcess implements PaFapleCancelProcess {

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Autowired
	private PaFapleConnectUtil paFapleConnectUtil;
	
	@Autowired
	private PaFapleApiRequest paFapleApiRequest;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Autowired
	private PaFapleCancelDAO paFapleCancelDAO;
	
	@Autowired
	private PaFapleAsyncController paFapleAsyncController;
	
	@Autowired
	@Qualifier("pafaple.cancel.paFapleCancelService")
	private PaFapleCancelService paFapleCancelService;
	
	@Autowired
	@Qualifier("pafaple.claim.paFapleClaimProcess")
	private PaFapleClaimProcessImpl paFapleClaimProcess;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paorderService;
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getCancelList(String cancelStatus, String fromDate, String toDate, HttpServletRequest request) throws Exception {
		
		String apiCode = "IF_PAFAPLEAPI_03_003";
		String duplicateCheck = "";
		String paCode = "";
		Timestamp sysdateTime = systemService.getSysdatetime();
		ParamMap paramMap = new ParamMap();
		ParamMap retrieveDate = null;
		
		List<Map<String, Object>> cancelList = null;
		PaFapleCancelListVO cancelListVO = null;
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		
		try {
			
			switch (cancelStatus) {
			case "2": //취소승인 대상 수집
				apiCode = "IF_PAFAPLEAPI_03_003";
				break;
			default:
				throw new Exception("CancelState is wrong");
			}

			log.info("======= 패션플러스 취소승인 대상 수집 API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paFapleApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			log.info("03.API Request Setting");
			retrieveDate = paFapleConnectUtil.setRetrieveDateFormat(fromDate, toDate, "1");
			fromDate = retrieveDate.getString("startDate");
			toDate = retrieveDate.getString("endDate");
			
			Map<String, Object> apiRequestMap  = new HashMap<String, Object>();
			apiRequestMap.put("CancelState", Integer.parseInt(cancelStatus));
			apiRequestMap.put("Sdate", fromDate);
			apiRequestMap.put("Edate", toDate);
			
			int paFapleContractCnt = Constants.PA_FAPLE_CONTRACT_CNT;
			//로컬 세팅
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				paFapleContractCnt = 1;
			}
			
			for(int i = 0; i< paFapleContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				paramMap.put("paCode", paCode);

				log.info("04.API Call");
				responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiRequestMap);
				
				log.info("05.Processing");
				if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(String.valueOf(responseMap.get("Status"))) && "".equals(String.valueOf(responseMap.get("Message")))) {
					if("2".equals(cancelStatus)) { // 취소 승인만 처리 
						cancelList = (List<Map<String, Object>>)responseMap.get("OrderCancel");
					
						for(Map<String, Object> cancel : cancelList) {
							try {
								cancelListVO = new PaFapleCancelListVO();
								cancelListVO.setProcFlag("10"); // TCODE[O513] - 10(승인)
								cancelListVO.setPaCode(paCode); 
								
								cancelListVO.setPaOrderGb("20"); // TCODE[J007] - 20(취소)
								cancelListVO.setOutBefClaimGb("0"); 
								cancelListVO.setOrderId(ComUtil.objToStr(cancel.get("order_id")));
								cancelListVO.setItemId(ComUtil.objToStr(cancel.get("item_id")));
								cancelListVO.setCancelCreated(DateUtil.toTimestamp(ComUtil.objToStr(cancel.get("cancelCreated")), DateUtil.FAPLE_DATETIME_FORMAT));
								cancelListVO.setCancelState(cancelStatus);
								cancelListVO.setCancelStateName(ComUtil.objToStr(cancel.get("cancelState_name")));
								cancelListVO.setReasonTypeName(ComUtil.objToStr(cancel.get("ReasonType_name")));;
								cancelListVO.setAutoCancelDate(DateUtil.toTimestamp(ComUtil.objToStr(cancel.get("AutoCancel_date")), DateUtil.FAPLE_DATETIME_FORMAT));
								cancelListVO.setAutoCancelEmpName(ComUtil.objToStr(cancel.get("AutoCancel_empName")));
								cancelListVO.setApprovalDate(DateUtil.toTimestamp(ComUtil.objToStr(cancel.get("approval_date")), DateUtil.FAPLE_DATETIME_FORMAT));
								cancelListVO.setApprovalEmpName(ComUtil.objToStr(cancel.get("approval_empName")));
								cancelListVO.setRefusalDate(DateUtil.toTimestamp(ComUtil.objToStr(cancel.get("refusal_date")), DateUtil.FAPLE_DATETIME_FORMAT));
								cancelListVO.setRefusalEmpName(ComUtil.objToStr(cancel.get("refusal_empName")));
								cancelListVO.setPaOrderGb("20");
								cancelListVO.setInsertId(Constants.PA_FAPLE_PROC_ID);
								cancelListVO.setInsertDate(sysdateTime);
								cancelListVO.setModifyDate(sysdateTime);
								
								// TPAFAPLECANCELLIST, TPAORDERM INSERT
								saveFapleCancelList(cancelListVO);
							
							} catch(Exception e) {
								paramMap.put("code", "500");
								paramMap.put("message", e.getMessage());
								log.error("Exception occurs : " + e.getMessage());
							}
						}
					}
				} else { // Message가 있는 경우 => Error
					log.info("API returnCode : " + responseMap.get("Status").toString() + " returnMsg : " + responseMap.get("Message").toString());
					
					if (responseMap.get("Status").toString().equals("Err-Dat-002")) {
						paramMap.put("code", "404");
						paramMap.put("message", getMessage("pa.not_exists_process_list"));
					} else if (responseMap.get("Status").toString().equals("Err-Dat-902")) { // Err-Dat-902 : 발주확인할 주문이 없습니다.
						paramMap.put("code", "200");
						paramMap.put("message", getMessage("errors.no.select"));
					} else {
						paramMap.put("code", "500");
						paramMap.put("message", responseMap.get("Message").toString());
					}
				}
			}
		} catch(Exception e) {
			paFapleConnectUtil.checkException(paramMap, e);
		} finally {
			try {
				paFapleConnectUtil.closeApi(request, paramMap);
				log.info("패션플러스 취소승인 대상 수집 API End");
			} catch(Exception e) {
				log.error("ApiTracking/CloseHistory process Error : " + e.getMessage());
			}
		}
		
		if("2".equals(cancelStatus)) {
			cancelInputMain(request);
		}
		getSoldOutList(request, fromDate, toDate);
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
	public void saveFapleCancelList(PaFapleCancelListVO paFapleCancelListVO) throws Exception {
		int executedRtn = 0;
		int checkCancelCnt = 0;
		int existsYn = 0;
		
		// 주문 없는 데이터 스킵 
		checkCancelCnt = paFapleCancelDAO.countOrderList(paFapleCancelListVO);
		if(checkCancelCnt < 1) return;
		
		// 직택배 조회
		String delyType = paFapleCancelDAO.selectCancelOrderDelyGb(paFapleCancelListVO);
		int doFlag = Integer.parseInt(ComUtil.NVL(paFapleCancelDAO.selectDoflag(paFapleCancelListVO),"0")) ;
		
		// TPAFSPL_CANCELLIST 중복 데이터 유무 체크
		checkCancelCnt = paFapleCancelDAO.selectPaFapleCancelListCount(paFapleCancelListVO);
		if (checkCancelCnt > 0) {
			log.info("orderNo(" + paFapleCancelListVO.getOrderId() + ") is already exists in TPAFAPLECANCELLIST.");
			return;
		}
		
		// paFapleCancelListVO.setPaProcQty("1"); ????
		paFapleCancelListVO.setOutBefClaimGb(ComUtil.NVL(paFapleCancelDAO.selectOutBefClaimGb(paFapleCancelListVO), "0")); // 반품생성대상 여부 조회
		
		if ("1".equals(paFapleCancelListVO.getOutBefClaimGb())) {
			paFapleCancelListVO.setCancelProcNote("취소승인완료(반품전환)");
		} else {
			paFapleCancelListVO.setCancelProcNote("취소승인완료");
		}
		
		HashMap<String,Object> hashMap = new HashMap<String, Object>();
		hashMap.put("paClaimName", paFapleCancelListVO.getReasonTypeName());
		hashMap.put("claimGb", paFapleCancelListVO.getPaOrderGb());
		String reason_type = paFapleCancelDAO.selectCancelOrderReasonType(hashMap);

		if(reason_type == null || reason_type.isEmpty()) {
			throw processException("msg.cannot_save", new String[] { "취소사유명 (ReasonType_name) NULL. " 	+ paFapleCancelListVO.getOrderId() + " TPAFAPLECANCELLIST" });
		}

		paFapleCancelListVO.setReasonType(reason_type);
		
		if("10".equals(delyType) && doFlag >= 30 ) {
			paFapleCancelListVO.setPaOrderGb("30");
			paFapleCancelListVO.setOutBefClaimGb("0");
		}
		Integer claimQty = paFapleCancelDAO.selectClaimQty(paFapleCancelListVO);
		if(claimQty == null) claimQty = 1;
		
		paFapleCancelListVO.setPaProcQty(String.valueOf(claimQty));
		
		
		// TPAFAPLECANCALLIST 데이터 유무 확인
		existsYn = paFapleCancelDAO.checkCancelExistYn(paFapleCancelListVO);
		if (existsYn < 1) {
			executedRtn = paFapleCancelDAO.insertTpaFapleCancelList(paFapleCancelListVO);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAFAPLECANCELLIST INSERT" });
			}
		}
		
		Paorderm paorderm = setPaorderM(paFapleCancelListVO);
		//INSERT TPAORDERM
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		if (executedRtn != 1) {
			log.error("Failed to INSERT orderNo(" + paFapleCancelListVO.getOrderId() + ") into TPAORDERM");
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		
	}

	private Paorderm setPaorderM(AbstractModel abstractVO) throws Exception {
		
		String paOrderGb     = null;
		String outBefCliamGb = null;
		String sysdate       = DateUtil.getCurrentDateTimeAsString();
		Paorderm paorderm    = new Paorderm();
		
		if (abstractVO instanceof PaFapleCancelListVO) { // 취소 요청/완료 조회 저장 시
			paOrderGb = ((PaFapleCancelListVO)abstractVO).getPaOrderGb();
			outBefCliamGb = ((PaFapleCancelListVO)abstractVO).getOutBefClaimGb();
			
			if("1".equals(outBefCliamGb)) {
				paorderm.setPaDoFlag("60");
			}else {
				paorderm.setPaDoFlag("20");
			}
			
			// 직택배 조회
			String delyType = paFapleCancelDAO.selectCancelOrderDelyGb((PaFapleCancelListVO)abstractVO);
			int doFlag = Integer.parseInt(ComUtil.NVL(paFapleCancelDAO.selectDoflag((PaFapleCancelListVO)abstractVO),"0")) ;
			if("10".equals(delyType) && doFlag >= 30 ) {
				paorderm.setPaDoFlag("60");
			}
			
			paorderm.setPaCode(((PaFapleCancelListVO)abstractVO).getPaCode());
			paorderm.setPaGroupCode("14");
			paorderm.setPaOrderNo(((PaFapleCancelListVO)abstractVO).getOrderId());
			paorderm.setPaOrderSeq(((PaFapleCancelListVO)abstractVO).getItemId());
			paorderm.setPaProcQty(((PaFapleCancelListVO)abstractVO).getPaProcQty());
			paorderm.setPaClaimNo("0");
			paorderm.setPreCancelYn("0");
			
		} 
		
		else {
			throw processException("errors.cannot" , new String[] { "abstractVO is not suitable." });
		}
		
		paorderm.setPaOrderGb(paOrderGb);
		paorderm.setOutBefClaimGb(outBefCliamGb);
		paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId(Constants.PA_FAPLE_PROC_ID);
		
		return paorderm;
	}
	
	/* 취소 생성 */
	private void cancelInputMain(HttpServletRequest request) throws Exception {

		String apiCode = "PAFAPLE_CANCEL_INPUT";
		String duplicateCheck = "";
		ParamMap paramMap = new ParamMap();
		
		try {
			log.info("===== " + apiCode + " Start =====");
		
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paFapleApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			paramMap.put("paOrderGb", "20");
			// 취소 생성 대상 조회
			List<HashMap<String, Object>> cancelInputTargetList = paFapleCancelService.selectClaimTargetList(paramMap);
			for(HashMap<String, Object> cancelInputTarget : cancelInputTargetList) {
				try {
					// BO 취소 생성
					paFapleAsyncController.cancelInputAsync(cancelInputTarget, request);
					
				} catch(Exception e) {
					log.error("Exception occurs : " + e.getMessage());
				}
			}
		} catch (Exception e) {
			log.error("Exception occurs : " + e.getMessage());
		} finally {
				paFapleConnectUtil.closeApi(request, paramMap);
		}
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paFapleCancelDAO.selectClaimTargetList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paFapleCancelDAO.selectCancelInputTargetDtList(paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParamMap cancelApprovalProcBo(HttpServletRequest request) throws Exception {
		String apiCode = "IF_PAFAPLEAPI_03_012_BO";
		String duplicateCheck = "";
		String resultMsg = null;
		String resultCode = null;
		Timestamp sysdateTime = systemService.getSysdatetime();
		
		ParamMap paramMap = new ParamMap();		
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> cancelInfo = new HashMap<String, Object>();

		try {
			
			log.info("======= 패션플러스 품절주문 등록  API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paramMap.put("paGroupCode", "14");
			paFapleApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			List<Object> cancelList = paCommonService.selectPaSoldOutordList(paramMap);
			
			if(cancelList.size() > 0) {
				for(int i = 0; i < cancelList.size(); i++) {
					HashMap<String, String> map 		= new HashMap<String, String>();
					
					try {
						List<HashMap<String, Object>> refusalInfoList = new ArrayList<HashMap<String,Object>>();
						List<Map<String, Object>> outOfStockProcList = null;
						HashMap<String, Object> cancelMap = (HashMap<String, Object>)cancelList.get(i);
						
						log.info("03.API Request Setting");
						
						cancelInfo.put("item_id", Integer.parseInt(cancelMap.get("PA_ORDER_SEQ").toString())); // 패션플러스 주문순번
						cancelInfo.put("order_id", Integer.parseInt(cancelMap.get("PA_ORDER_NO").toString())); // 패션플러스 주문ID
						
						refusalInfoList.add((HashMap<String, Object>) cancelInfo);
						ParamMap apiRequestMap  = new ParamMap();
						apiRequestMap.put("OutOfStock_list", refusalInfoList); 
						paramMap.put("paCode", cancelMap.get("PA_CODE"));
						log.info("04.API Call");
						responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiRequestMap);
						
						if (responseMap != null) {
							log.info("04.Processing");
							outOfStockProcList = new ArrayList<Map<String, Object>>();
							outOfStockProcList = (List<Map<String, Object>>)responseMap.get("OutOfStockProc");
							
							for (Map<String, Object> outOfStockProc : outOfStockProcList) {
								resultCode = Constants.PA_FAPLE_SUCCESS_STATUS.equals(String.valueOf(outOfStockProc.get("Status"))) ?  String.valueOf(outOfStockProc.get("Status")) : "500" ;
								
								if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(resultCode)) {
									paramMap.put("code", "200");
									paramMap.put("message", "판매취소 성공");
									
									map.put("ORDER_CANCEL_YN", "10");
									
									// 품절취소는 취소조회에서 값을 주지 않기 때문에 임의 데이터 생성 
									PaFapleCancelListVO cancelListVO = new PaFapleCancelListVO();
									
									cancelListVO.setProcFlag("10"); // TCODE[O513] - 10(승인)
									cancelListVO.setPaCode(cancelMap.get("PA_CODE").toString()); 
									cancelListVO.setPaOrderGb("20"); // TCODE[J007] - 20(취소)
									cancelListVO.setOutBefClaimGb("0"); 
									cancelListVO.setOrderId(ComUtil.objToStr(cancelMap.get("PA_ORDER_NO")));
									cancelListVO.setItemId(ComUtil.objToStr(cancelMap.get("PA_ORDER_SEQ").toString()));
									cancelListVO.setCancelCreated(sysdateTime);
									cancelListVO.setCancelState("2");
									cancelListVO.setCancelStateName(ComUtil.objToStr("품절취소"));
									cancelListVO.setReasonTypeName(ComUtil.objToStr("품절"));
									cancelListVO.setAutoCancelDate(sysdateTime);
									cancelListVO.setAutoCancelEmpName("패션플러스");
									cancelListVO.setPaOrderGb("20");
									cancelListVO.setInsertId(Constants.PA_FAPLE_PROC_ID);
									cancelListVO.setInsertDate(sysdateTime);
									cancelListVO.setModifyDate(sysdateTime);
									
									// TPAFAPLECANCLELIST, TPAORDERM INSERT
									saveFapleCancelList(cancelListVO);
									
								} else {
									resultMsg = String.valueOf(outOfStockProc.get("Message"));
									log.info("API returnCode : " + resultCode + " returnMsg : " + resultMsg);
									
									paramMap.put("code", "500");
									paramMap.put("message", "API returnCode : " + resultCode + " returnMsg : " + resultMsg);
									
									map.put("ORDER_CANCEL_YN", "90");
								}
								
								map.put("PA_GROUP_CODE", paramMap.get("paGroupCode").toString());
								map.put("PA_ORDER_NO", cancelMap.get("PA_ORDER_NO").toString());
								map.put("PA_ORDER_SEQ", cancelMap.get("PA_ORDER_SEQ").toString());
								map.put("ORDER_NO", cancelMap.get("ORDER_NO").toString());
								map.put("SITE_GB", cancelMap.get("SITE_GB").toString());
								map.put("PA_CODE", cancelMap.get("PA_CODE").toString());
								map.put("ORG_ORD_CAN_YN", cancelMap.get("ORDER_CANCEL_YN").toString());
								map.put("RSLT_MESSAGE", paramMap.get("message").toString());	
							}
						}
					}catch(Exception e) {
						resultMsg = PaFapleComUtil.getErrorMessage(e);
						map.put("ORDER_CANCEL_YN", "90");
						map.put("RSLT_MESSAGE", resultMsg);
						continue;
					} finally {
						paorderService.updateOrderCancelYnTx(map);
						//상담생성 & 문자발송
						paCommonService.saveOrderCancelCounselTx(map);
					}
				}
			}else {
				paramMap.put("code", "200");
				paramMap.put("message", "조회대상이 존재하지 않습니다.");
				return paramMap;
			}
			log.info("======= 패션플러스 품절주문 등록  API End - {} =======");
		}catch (Exception e) {
			log.error(e.getMessage());
		}finally {
			paFapleConnectUtil.closeApi(request, paramMap);
		}
		return paramMap;
	}

	@SuppressWarnings("unchecked")
	public ResponseMsg getSoldOutList(HttpServletRequest request, String fromDate, String toDate ) throws Exception {
		
		String apiCode = "IF_PAFAPLEAPI_03_015";
		String duplicateCheck = "";
		String paCode = "";
		Timestamp sysdateTime = systemService.getSysdatetime();
		ParamMap paramMap = new ParamMap();
		ParamMap retrieveDate = null;
		
		List<Map<String, Object>> deliveryList = null;
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		
		try {
			log.info("======= 패션플러스 배송주문조회 API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paFapleApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			log.info("03.API Request Setting");
			retrieveDate = paFapleConnectUtil.setRetrieveDateFormat(fromDate, toDate, "1");
			fromDate = retrieveDate.getString("startDate");
			toDate = retrieveDate.getString("endDate");
			
			Map<String, Object> apiRequestMap  = new HashMap<String, Object>();
			apiRequestMap.put("DeliveryType", "E");
			apiRequestMap.put("SDate", fromDate);
			apiRequestMap.put("EDate", toDate);
			
			int paFapleContractCnt = Constants.PA_FAPLE_CONTRACT_CNT;
			//로컬 세팅
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				paFapleContractCnt = 1;
			}
			
			for(int i = 0; i< paFapleContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				paramMap.put("paCode", paCode);

				log.info("04.API Call");
				responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiRequestMap);
				
				log.info("05.Processing");
				if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(String.valueOf(responseMap.get("Status"))) && "".equals(String.valueOf(responseMap.get("Message")))) {
					
				deliveryList = (List<Map<String, Object>>)responseMap.get("DeliveryList");
				
					for(Map<String, Object> delivery : deliveryList) {
						try {
							// 품절취소는 취소조회에서 값을 주지 않기 때문에 임의 데이터 생성 
							PaFapleCancelListVO cancelListVO = new PaFapleCancelListVO();
							
							cancelListVO.setProcFlag("10"); // TCODE[O513] - 10(승인)
							cancelListVO.setPaCode(paramMap.get("paCode").toString()); 
							cancelListVO.setPaOrderGb("20"); // TCODE[J007] - 20(취소)
							cancelListVO.setOutBefClaimGb("0");
							cancelListVO.setOrderId(ComUtil.objToStr(delivery.get("order_id")));
							cancelListVO.setItemId(ComUtil.objToStr(delivery.get("item_id")));
							cancelListVO.setCancelCreated(sysdateTime);
							cancelListVO.setCancelState("2");
							cancelListVO.setCancelStateName(ComUtil.objToStr("품절취소"));
							cancelListVO.setReasonTypeName(ComUtil.objToStr("품절"));
							cancelListVO.setAutoCancelDate(sysdateTime);
							cancelListVO.setAutoCancelEmpName("패션플러스");
							cancelListVO.setPaOrderGb("20");
							cancelListVO.setInsertId(Constants.PA_FAPLE_PROC_ID);
							cancelListVO.setInsertDate(sysdateTime);
							cancelListVO.setModifyDate(sysdateTime);
							
							// TPAFAPLECANCLELIST, TPAORDERM INSERT
							saveFapleCancelList(cancelListVO);
							
						} catch(Exception e) {
							paramMap.put("code", "500");
							paramMap.put("message", e.getMessage());
							log.error("Exception occurs : " + e.getMessage());
						}
					}
				
				} else { // Message가 있는 경우 => Error
					log.info("API returnCode : " + responseMap.get("Status").toString() + " returnMsg : " + responseMap.get("Message").toString());
					
					if (responseMap.get("Status").toString().equals("Err-Dat-002")) {
						paramMap.put("code", "404");
						paramMap.put("message", getMessage("pa.not_exists_process_list"));
					}  else if (responseMap.get("Status").toString().equals("Err-ECL-903")) { // Err-Dat-902 : 발주확인할 주문이 없습니다.
						paramMap.put("code", "200");
						paramMap.put("message", getMessage("errors.no.select"));
					} else {
						paramMap.put("code", "500");
						paramMap.put("message", responseMap.get("Message").toString());
					}
				}
			}
		} catch(Exception e) {
			paFapleConnectUtil.checkException(paramMap, e);
		} finally {
			try {
				paFapleConnectUtil.closeApi(request, paramMap);
				log.info("패션플러스 배송주문조회  API End");
			} catch(Exception e) {
				log.error("ApiTracking/CloseHistory process Error : " + e.getMessage());
			}
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	/**
	 * 모바일 자동취소 (품절취소반품)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ParamMap mobileOrderSoldOut(HttpServletRequest request) throws Exception {
		String apiCode = "IF_PAFAPLEAPI_03_017";
		String duplicateCheck = "";
		String resultMsg = null;
		String resultCode = null;
		String paGroupCode = "";
		String paCode = "";
		String paOrderNo = "";
		String paOrderSeq = "";
		Timestamp sysdateTime = systemService.getSysdatetime();
		
		ParamMap paramMap = new ParamMap();		
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> cancelInfo = new HashMap<String, Object>();

		try {
			
			log.info("======= 패션플러스 모바일자동취소 API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paramMap.put("paGroupCode", "14");
			paFapleApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			List<HashMap<String, String>> cancelList = paFapleCancelDAO.selectPaMobileOrderAutoCancelList();
			
			if(cancelList.size() > 0) {
				for(HashMap<String, String> cancelMap : cancelList) {
					HashMap<String, String> map 		= new HashMap<String, String>();
					
					try {
						List<HashMap<String, Object>> refusalInfoList = new ArrayList<HashMap<String,Object>>();
						List<Map<String, Object>> outOfStockProcList = null;
						
						log.info("03.API Request Setting");
						
						paCode = cancelMap.get("PA_CODE").toString();
						paOrderNo = cancelMap.get("PA_ORDER_NO").toString();
						paOrderSeq = cancelMap.get("PA_ORDER_SEQ").toString();
						 
						map.put("PA_GROUP_CODE", paGroupCode);
						map.put("PA_CODE", paCode);
						map.put("PA_ORDER_NO", paOrderNo);
						map.put("PA_ORDER_SEQ", paOrderSeq);
						map.put("ORDER_NO", cancelMap.get("ORDER_NO"));
						map.put("ORDER_G_SEQ", cancelMap.get("ORDER_G_SEQ"));
					 	map.put("PROC_ID", Constants.PA_COPN_PROC_ID);
						
						cancelInfo.put("item_id", Integer.parseInt(cancelMap.get("PA_ORDER_SEQ").toString())); // 패션플러스 주문순번
						cancelInfo.put("order_id", Integer.parseInt(cancelMap.get("PA_ORDER_NO").toString())); // 패션플러스 주문ID
						
						refusalInfoList.add((HashMap<String, Object>) cancelInfo);
						ParamMap apiRequestMap  = new ParamMap();
						apiRequestMap.put("OutOfStock_list", refusalInfoList); 
						paramMap.put("paCode", cancelMap.get("PA_CODE"));
						log.info("04.API Call");
						responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiRequestMap);
						
						if (responseMap != null) {
							log.info("04.Processing");
							outOfStockProcList = new ArrayList<Map<String, Object>>();
							outOfStockProcList = (List<Map<String, Object>>)responseMap.get("OutOfStockProc");
							paGroupCode = paramMap.get("paGroupCode").toString();
							
							for (Map<String, Object> outOfStockProc : outOfStockProcList) {
								resultCode = Constants.PA_FAPLE_SUCCESS_STATUS.equals(String.valueOf(outOfStockProc.get("Status"))) ?  String.valueOf(outOfStockProc.get("Status")) : "500" ;
								
								if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(resultCode)) {
									paramMap.put("code", "200");
									paramMap.put("message", "모바일자동취소 성공");
									
									map.put("REMARK3_N", "10");
									map.put("RSLT_MESSAGE", "모바일자동취소 성공");
									paorderService.updateRemark3NTx(map);
									
									//상담생성 & 문자발송 & 상품품절처리
									paCommonService.savePaMobileOrderCancelTx(map);
									
									// 품절취소는 취소조회에서 값을 주지 않기 때문에 임의 데이터 생성 
									PaFapleCancelListVO cancelListVO = new PaFapleCancelListVO();
									
									cancelListVO.setProcFlag("10"); // TCODE[O513] - 10(승인)
									cancelListVO.setPaCode(paCode); 
									cancelListVO.setPaOrderGb("20"); // TCODE[J007] - 20(취소)
									cancelListVO.setOutBefClaimGb("0"); 
									cancelListVO.setOrderId(ComUtil.objToStr(paOrderNo));
									cancelListVO.setItemId(ComUtil.objToStr(paOrderSeq));
									cancelListVO.setCancelCreated(sysdateTime);
									cancelListVO.setCancelState("2");
									cancelListVO.setCancelStateName(ComUtil.objToStr("품절취소"));
									cancelListVO.setReasonTypeName(ComUtil.objToStr("품절"));
									cancelListVO.setAutoCancelDate(sysdateTime);
									cancelListVO.setAutoCancelEmpName("패션플러스");
									cancelListVO.setPaOrderGb("20");
									cancelListVO.setInsertId(Constants.PA_FAPLE_PROC_ID);
									cancelListVO.setInsertDate(sysdateTime);
									cancelListVO.setModifyDate(sysdateTime);
									
									// TPAFAPLECANCLELIST, TPAORDERM INSERT
									saveFapleCancelList(cancelListVO);
									
								} else {
									resultMsg = String.valueOf(outOfStockProc.get("Message"));
									log.info("API returnCode : " + resultCode + " returnMsg : " + resultMsg);
									
									paramMap.put("code", "500");
									paramMap.put("message", "API returnCode : " + resultCode + " returnMsg : " + resultMsg);
									
									map.put("REMARK3_N", "90");
									map.put("RSLT_MESSAGE", "모바일자동취소 실패 " +  resultCode + " returnMsg : " + resultMsg);
									paorderService.updateRemark3NTx(map);
								}
							}
						}
					}catch(Exception e) {
						resultMsg = PaFapleComUtil.getErrorMessage(e);
						map.put("REMARK3_N", "90");
						map.put("RSLT_MESSAGE", "모바일자동취소 실패 " + resultMsg);
						paorderService.updateRemark3NTx(map);
						continue;
					}
				}
			}else {
				paramMap.put("code", "200");
				paramMap.put("message", "조회대상이 존재하지 않습니다.");
				return paramMap;
			}
			log.info("======= 패션플러스 모바일자동취소  API End - {} =======");
		}catch (Exception e) {
			log.error(e.getMessage());
		}finally {
			paFapleConnectUtil.closeApi(request, paramMap);
			paFapleClaimProcess.returnInputMain(request);
		}
		return paramMap;
	}
}
