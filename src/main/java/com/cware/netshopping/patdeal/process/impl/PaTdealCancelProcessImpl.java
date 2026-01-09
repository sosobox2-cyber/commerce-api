package com.cware.netshopping.patdeal.process.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaTdealClaimListVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.patdeal.domain.ClaimInfos;
import com.cware.netshopping.patdeal.domain.OrderDetail;
import com.cware.netshopping.patdeal.process.PaTdealCancelProcess;
import com.cware.netshopping.patdeal.process.PaTdealClaimProcess;
import com.cware.netshopping.patdeal.repository.PaTdealCancelDAO;
import com.cware.netshopping.patdeal.repository.PaTdealDeliveryDAO;
import com.cware.netshopping.patdeal.util.PaTdealApiRequest;
import com.cware.netshopping.patdeal.util.PaTdealConnectUtil;

@Service("patdeal.cancel.paTdealCancelProcess")
public class PaTdealCancelProcessImpl extends AbstractProcess implements PaTdealCancelProcess {
	@Resource(name = "patdeal.cancel.paTdealCancelDAO")
	PaTdealCancelDAO paTdealCancelDAO;
	
	@Resource(name = "patdeal.claim.paTdealClaimProcess")
	PaTdealClaimProcess paTdealClaimProcess;

	@Autowired
	private PaTdealConnectUtil paTdealConnectUtil;
	
	@Autowired
	private PaCommonDAO paCommonDAO;

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private PaOrderService paorderService;
	
	@Autowired
	private PaCommonService paCommonService;
	
	@Autowired
	private PaTdealApiRequest paTdealApiRequest;
	
	@Autowired
	private PaTdealDeliveryDAO paTdealDeliveryDAO;
	
	@Override
	public int saveTdealCancelList(String claimStatus, PaTdealClaimListVO paTdealCancelVo) throws Exception {
		int executedRtn 	 = 0;
		int checkCancelCnt 	 = 0;
		int doFlag = 0;
		// 원주문 데이터 체크
		checkCancelCnt = paTdealCancelDAO.countOrderList(paTdealCancelVo);
		if(checkCancelCnt < 1) return 0; //주문이 없는 데이터는 스킵
		 
		// TPATDEALCANCELLSIT 중복 데이터 유무 체크
		checkCancelCnt = paTdealCancelDAO.selectPaTdealCancelListCount(paTdealCancelVo);
		if(checkCancelCnt > 0) {
			return 0; //이미 저장됐었던 취소 자료이니 스킵 
		}else {
			
			if("21".equals(claimStatus)) {
				paTdealCancelVo.setProcFlag("10");
			}else {
				paTdealCancelVo.setProcFlag("00");
			}
			executedRtn = paTdealCancelDAO.insertPaTdealCancelList(paTdealCancelVo);	
			
			if(executedRtn != 1){
				throw processException("msg.cannot_save", new String[] { "TPATDEALCANCELLSIT INSERT" });
			}
		}
		
		//취소 완료건은 TPAORDERM에도 넣어줘야됨.
		// INSERT TPAORDERM
		// 주문은 접수 됐지만 취소 데이터가 없던 취소 완료건 => 티딜 관리자페이지에서 바로 취소한 데이터
		if("21".equals(claimStatus)  ) {
			Paorderm paorderm = new Paorderm();
			Map<String, String> voMap = null;
			
			voMap = BeanUtils.describe(paTdealCancelVo);
			voMap.put("claimQty", voMap.get("orderCnt"));
			doFlag = paTdealCancelDAO.selectOrderDoflag(paTdealCancelVo);
			
			//원주문 doFlag 확인
			if(doFlag < 30) {
				voMap.put("outBefClaimGb", "0");
			} else {
				voMap.put("outBefClaimGb", "1");
			}
			paorderm.setPaClaimNo (voMap.get("claimNo"));
			
			paorderm.setPaOrderNo  (voMap.get("orderNo"));
			paorderm.setPaOrderSeq (voMap.get("optionManagementCd").replaceAll("_",""));
		
			executedRtn   = 0;
			String sysdate 	  = DateUtil.getCurrentDateTimeAsString();
			String paOrderGb  = "20";
			String outClaimGb = voMap.get("outBefClaimGb");
			
			paorderm.setPaGroupCode(Constants.PA_TDEAL_GROUP_CODE);
			paorderm.setPaOrderGb  (paOrderGb);
			paorderm.setPaCode	   (voMap.get("paCode"));
			
			paorderm.setPaProcQty  (String.valueOf(voMap.get("orderCnt")));
			
			if("0".equals(outClaimGb)) {
				paorderm.setPaDoFlag("20");
			} else {
				paorderm.setPaDoFlag("60");
			}
			
			paorderm.setOutBefClaimGb(outClaimGb);
			paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setModifyId  (Constants.PA_TDEAL_PROC_ID);
			
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			
			if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		} 
		
		return executedRtn;
	}
	
	/**
	 * 취소승인처리
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<?> cancelConfirmProc(HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		ParamMap returnParamMap = new ParamMap();
		HashMap<String, String> map 		= new HashMap<String, String>();
		int doFlag = 0;
		String orderCancelYn = "";
		String mobileAutoCancelYn = "";
		try {
			List<Map<String, Object>> cancelList = paTdealCancelDAO.selectPaTdealOrderCancelList();
			
			for(Map<String, Object> cancel : cancelList) {
				try {
					doFlag = Integer.parseInt(String.valueOf(cancel.get("DO_FLAG")));
					orderCancelYn = cancel.get("ORDER_CANCEL_YN").toString();
					mobileAutoCancelYn = cancel.get("REMARK3_N").toString();
					if (orderCancelYn.equals("05") || orderCancelYn.equals("06")) { //BO 품절취소반품, 일괄취소반품 사용
						if (doFlag < 30) {
							cancel.put("OUT_BEF_CLAIM_GB", "0"); // 출고전 취소
						} else {
							cancel.put("OUT_BEF_CLAIM_GB", "1"); // 출고전 반품
						}
						map.put("PA_GROUP_CODE", "13");
						map.put("PA_ORDER_NO", cancel.get("ORDER_NO").toString());
						map.put("PA_ORDER_SEQ", cancel.get("OPTION_MANAGEMENT_CD").toString());
						map.put("ORDER_NO", cancel.get("SK_ORDER_NO").toString());
						map.put("SITE_GB", cancel.get("SITE_GB").toString());
						map.put("PA_CODE", cancel.get("PA_CODE").toString());
						map.put("ORG_ORD_CAN_YN", cancel.get("ORDER_CANCEL_YN").toString());
						
						returnParamMap = cancelConfirm(cancel, request); // 취소 승인
						
						if(returnParamMap.get("status").equals("204")) { //취소승인 성공
							map.put("ORDER_CANCEL_YN", "10");
							map.put("RSLT_MESSAGE", "판매취소 성공");
						}else { //취소승인 실패
							map.put("ORDER_CANCEL_YN", "90");
							map.put("RSLT_MESSAGE", returnParamMap.get("message").toString());
						}
						//상담생성 & 문자발송 & 상태값 업데이트
						paorderService.updateOrderCancelYnTx(map);
						paCommonService.saveOrderCancelCounselTx(map);
					}else if (doFlag < 30) {
						cancel.put("OUT_BEF_CLAIM_GB", "0"); // 출고전 취소
						returnParamMap = cancelConfirm(cancel, request); // 취소 승인
					}else if(doFlag > 30) {
						returnParamMap = alreadyDelivery(cancel, request); // 취소 거절
					}else if(mobileAutoCancelYn.equals("10")) { // 모바일 자동취소 여부(출하지시 상태)
						cancel.put("OUT_BEF_CLAIM_GB", "1"); // 출고전 반품
						returnParamMap = cancelConfirm(cancel, request); // 취소 승인
					}
				}catch (Exception e) {
					paramMap.put("code"	  , "500");
					paramMap.put("message", e);
				}
			}
		} catch(Exception e) {
			paramMap.put("code"	  , "500");
			paramMap.put("message", e);
		} 
		
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	public ParamMap cancelConfirm(Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PATDEALAPI_03_011";
		ParamMap apiDataObject = new ParamMap();
		ParamMap apiInfoMap	= new ParamMap();
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String, String> queryParameters = new HashMap<String, String>();
		Map<String,Object> returnMap = new HashMap<String, Object>();
		int executedRtn = 0;
		log.info("======= 티딜 취소 승인 API Start - {} =======");
		try {
				// 취소 승인  
//				String duplicateCheck = "";
				apiInfoMap.put("paCode", requestMap.get("PA_CODE"));
				apiInfoMap.put("apiCode", apiCode);
				apiDataObject.put("body", map);
				apiInfoMap = paTdealApiRequest.getApiInfo(apiCode);
//				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode); 클레임 승인의 경우 여러 클레임을 한번에 승인 할 수 있으므로 중복실행 체크를 하지 않는다
//				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
//				apiInfoMap.put("duplicateCheck", duplicateCheck);
				returnMap = paTdealConnectUtil.getCommonLegacy(requestMap.get("PA_CODE").toString(), apiCode,ComUtil.objToStr(requestMap.get("CLAIM_NO")),queryParameters,apiDataObject);
				
				if((returnMap.get("status").equals("204") && !returnMap.containsKey("code")) || returnMap.get("message").toString().indexOf("당월이전거래") > -1 || returnMap.get("message").toString().indexOf("상점으로") > -1) { //상태값이 204이며 code가 없으면 취소승인 정상 처리. || KCP 고객센터에서 처리해야 하는 건도 정상 처리
					Paorderm paorderm = new Paorderm();
					paorderm.setPaClaimNo (requestMap.get("CLAIM_NO").toString());
					paorderm.setPaOrderNo  (requestMap.get("ORDER_NO").toString());
					paorderm.setPaOrderSeq (requestMap.get("OPTION_MANAGEMENT_CD").toString());
				
					executedRtn   = 0;
					String sysdate 	  = DateUtil.getCurrentDateTimeAsString();
					
					paorderm.setPaGroupCode(Constants.PA_TDEAL_GROUP_CODE);
					paorderm.setPaOrderGb  ("20");
					paorderm.setPaCode	   (requestMap.get("PA_CODE").toString());
					paorderm.setPaProcQty  (String.valueOf(requestMap.get("ORDER_CNT")));
					if ("1".equals(requestMap.get("OUT_BEF_CLAIM_GB").toString())) {
						paorderm.setPaDoFlag("60");
					} else {
						paorderm.setPaDoFlag("20");
					}
					paorderm.setOutBefClaimGb(requestMap.get("OUT_BEF_CLAIM_GB").toString());
					paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
					paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
					paorderm.setModifyId  (Constants.PA_TDEAL_PROC_ID);
					
					executedRtn = paTdealCancelDAO.updatePaTdealCancelStatus(paorderm);
					if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPATDEALCANCELLIST UPDATE" });
						
					executedRtn = paCommonDAO.insertPaOrderM(paorderm);
					if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
					
					apiInfoMap.put("code", "200");
					apiInfoMap.put("message", "티딜 취소 승인 성공");
					apiInfoMap.put("status", returnMap.get("status").toString());
				
				}	else { // 상태값 변경시 철회 하기위해 재조회
					OrderDetail detailResponseMsg = null;
					detailResponseMsg = paTdealClaimProcess.orderInfoDetail(requestMap.get("PA_CODE").toString(), requestMap.get("ORDER_NO").toString(), request);
					List<ClaimInfos> responseData = detailResponseMsg.getClaimInfos();
					    // 클레임 수량, 단품번호를 알아 내기 위해 탐색
					for(ClaimInfos m : responseData) {
						if(requestMap.get("CLAIM_NO").toString().equals(ComUtil.objToStr(m.getClaimNo()))) {
							if(m.getClaimStatusType() == null) { //ClaimStatusType가 NULL일경우 철회 된 건
								executedRtn = paTdealCancelDAO.updatePaTdealCancelwithdrawYn(requestMap);
								if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TpaTdealClaimList withdrawYn UPDATE" });
							} else if(m.getClaimStatusType().equals("CANCEL_DONE")) { // 수집 API가 돌기 전에 이미 취소 완료가 되었던 건(직권취소 등)
								// 취소완료 조회가 돌아가면 자동 처리 되므로 아무처리 하지않는다
							}
						}
					}
					apiInfoMap.put("code", "200");
					apiInfoMap.put("message", returnMap.get("message").toString());
				}
				
		}catch (Exception e) {
			
			// 상태값 확인을 위한 재조회
			OrderDetail detailResponseMsg = null;
			detailResponseMsg = paTdealClaimProcess.orderInfoDetail(requestMap.get("PA_CODE").toString(), requestMap.get("ORDER_NO").toString(), request);
			List<ClaimInfos> responseData = detailResponseMsg.getClaimInfos();
			    // 클레임 수량, 단품번호를 알아 내기 위해 탐색
			for(ClaimInfos m : responseData) {
				if(requestMap.get("CLAIM_NO").toString().equals(ComUtil.objToStr(m.getClaimNo()))) {
					if(m.getClaimStatusType() == null) { //ClaimStatusType가 NULL일경우 철회 된 건
						executedRtn = paTdealCancelDAO.updatePaTdealCancelwithdrawYn(requestMap);
						
						if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TpaTdealClaimList withdrawYn UPDATE" });
						
						apiInfoMap.put("code", "200");
						apiInfoMap.put("message", returnMap.get("message").toString());
					} else if(m.getClaimStatusType().equals("CANCEL_REQUEST")) { //취소 요청건 일경우 API 오류
						log.error(e.getMessage());
						paTdealConnectUtil.checkException(apiInfoMap, e);
					}
				}
			}
		}finally {
			paTdealConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info("======= 티딜 취소 승인 API End - {} =======");
		return apiInfoMap;
	}

	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paTdealCancelDAO.selectCancelInputTargetDtList(paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> cancelRequest(Object cancelItem, HttpServletRequest request) throws Exception {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		HashMap<String, String> cancelMap = (HashMap<String, String>) cancelItem;
		ParamMap apiInfoMap	= new ParamMap();
		Map<String, String> orderOptionParams = new HashMap<String, String>();
		List<Map<String, String>>  orderOptionParamsList =  new ArrayList<Map<String, String>>();
		
		String paCode = cancelMap.get("PA_CODE").toString();
		String apiCode = "IF_PATDEALAPI_03_013";
		String pathParameters = "";
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		ParamMap apiDataObject = new ParamMap();
		Map<String,Object> map = new HashMap<String, Object>();
		
		String reasonDetail ="";
		
		log.info("======= 티딜 취소 요청 API Start - {} =======");
		try {
			apiInfoMap.put("paCode", paCode);
			apiInfoMap.put("apiCode", apiCode);
			
			
			// 고객보상관리, 일괄취소반품(BO)에 등록된 경우, 등록된 사유 가져와서 세팅 
			if("06".equals(cancelMap.get("ORDER_CANCEL_YN")) ) {
				reasonDetail = paTdealCancelDAO.selectCustCmpstnReason(cancelMap);
			}
			if(reasonDetail!=null &&!reasonDetail.isEmpty()) {
				cancelMap.put("REASON_DETAIL", reasonDetail);
			}else if(!cancelMap.containsKey("REASON_DETAIL")) {
				cancelMap.put("REASON_DETAIL", "재고부족");
			}
			
			//취소 단품 세팅
			orderOptionParams.put("orderOptionNo", cancelMap.get("ORDER_PRODUCT_OPTION_NO"));
			orderOptionParams.put("claimCnt", cancelMap.get("PA_PROC_QTY"));
			orderOptionParams.put("reasonType", "OTHERS_SELLER"); 
			orderOptionParams.put("reasonDetail", cancelMap.get("REASON_DETAIL"));
			orderOptionParamsList.add(orderOptionParams);
			
			//취소 주문 세팅
			map.put("orderNo", cancelMap.get("PA_ORDER_NO"));
			map.put("responsibleObjectType", "SELLER");
			map.put("sellerPaysClaimedDelivery", true);
			map.put("inflowType", "SHOPBY_ADMIN");
			
			//주문 안에 단품이 배열로 들어가 있는 구조
			map.put("orderOptionParams", orderOptionParamsList);
			//body 세팅
			apiDataObject.put("body", map);
			apiInfoMap = paTdealApiRequest.getApiInfo(apiCode);
			String duplicateCheck = "";
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			apiInfoMap.put("duplicateCheck", duplicateCheck);
			
			//API통신
			returnMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode,pathParameters,queryParameters,apiDataObject);
			apiInfoMap.put("code", returnMap.get("status").toString());
			if( "204".equals(returnMap.get("status").toString()) ){
				apiInfoMap.put("message", "취소요청 완료");
			} else {
				apiInfoMap.put("message", "취소요청 실패");
			}
		
		}catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTdealConnectUtil.closeApi(request, apiInfoMap);
			
			if( !"204".equals(String.valueOf(apiInfoMap.get("code"))) && 
				( "05".equals(cancelMap.get("ORDER_CANCEL_YN")) || "06".equals(cancelMap.get("ORDER_CANCEL_YN") ))){
				
				HashMap<String, String> m 		= new HashMap<String, String>();
				m.put("PA_GROUP_CODE", "13");
				m.put("PA_ORDER_NO", cancelMap.get("PA_ORDER_NO"));
				m.put("PA_ORDER_SEQ", cancelMap.get("PA_ORDER_SEQ"));
				m.put("PA_CODE", paCode);
				m.put("ORG_ORD_CAN_YN", cancelMap.get("ORDER_CANCEL_YN"));
				m.put("SITE_GB", cancelMap.get("SITE_GB"));
				m.put("ORDER_NO", cancelMap.get("ORDER_NO"));
				m.put("ORDER_CANCEL_YN", "90");
				m.put("RSLT_MESSAGE","판매취소요청(일괄취소반품,품절취소반품) 실패(api 연동 실패)");
				paorderService.updateOrderCancelYnTx(m);
				paCommonService.saveOrderCancelCounselTx(m);

			}
			
		}
		log.info("======= 티딜 취소 요청 API End - {} =======");
		return returnMap;
	}
	
	/**
	 * 티딜 품절 취소처리(상품준비중 이전 단계에서만 가능/ 주문승인 api에서 품절, 취소 건 리스트 받아와서 처리)
	 */
	@Override
	public ResponseMsg soldOutCancelProc(List<Map<String, Object>> soldOutList, HttpServletRequest request) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PATDEALAPI_03_012";
		String duplicateCheck = "";
		String paCode   = "";
		int executedRtn = 0;
		
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		
		log.info("======= 티딜 품절 취소처리 API Start - {} =======");
		
		try {
			
			paramMap = paTdealApiRequest.getApiInfo(apiCode);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();
			
			// Body 세팅S
			ParamMap apiDataObject = null;
			
			ArrayList<String> orderOptionNos = null;
			Map<String,Object> map = null;
			for(Map<String, Object> soldOutMap : soldOutList) { 
				
				try {
					apiDataObject = new ParamMap();
					
					paCode = soldOutMap.get("PA_CODE").toString();
					paramMap.put("paCode", paCode);
					
					orderOptionNos = new ArrayList<String>();
					orderOptionNos.add(soldOutMap.get("ORDER_PRODUCT_OPTION_NO").toString());
					
					map = new HashMap<String, Object>();
					map.put("orderOptionNos", orderOptionNos);
					
					apiDataObject.put("body", map);
					// Body 세팅E
					
					//통신
					responseMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters,  queryParameters,  apiDataObject);
					
					if( !"204".equals(responseMap.get("status")) ){
						updatePaOrderm(soldOutMap, null, "품절취소처리 실패", Constants.SAVE_FAIL); 
						paramMap.put("code", responseMap.get("status"));
						paramMap.put("message", "품절취소처리 승인 실패");
						continue;//실패건은 스킵
					}
					
					soldOutMap.put("preCancelYn", "1");
					executedRtn = updatePaOrderm(soldOutMap, null, "품절 취소처리 성공", Constants.SAVE_SUCCESS);
					
					if(executedRtn>0) {
						paramMap.put("code", "200");
						paramMap.put("message", "품절 취소처리 성공 및 저장 완료");
					}else {
						paramMap.put("code", "400");
						paramMap.put("message", "품절 취소처리 저장 실패");
					}
					
				} catch (TransApiException e) {
					
					updatePaOrderm(soldOutMap, null, "품절취소처리 실패",Constants.SAVE_FAIL); 
					paramMap.put("code", e.getCode());
					paramMap.put("message", "품절취소처리 실패 : " + e.getMessage());
					
				}  catch (Exception e) {
					updatePaOrderm(soldOutMap, null, "품절취소처리 실패", Constants.SAVE_FAIL); 
					paramMap.put("code", responseMap.get("status"));
					paramMap.put("message", "품절취소처리 승인 실패 : " + e.getMessage());
				}
				
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(paramMap ,  e);
		}finally {
			if(paramMap.get("code") == null || "".equals(paramMap.get("code")) ) {
				paramMap.put("code", "200");
				paramMap.put("message", "품절 취소처리 대상 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			paTdealConnectUtil.closeApi(request, paramMap);
			log.info("======= 티딜 품절 취소처리 API End - {} =======");
		}

		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
	
	/**
	 * 티딜  취소처리(주문승인 이전단계에서만 가능/ 주문승인 api에서 품절, 취소 건 리스트 받아와서 처리)
	 */
	@Override
	public ResponseMsg cancelApprovalProc(List<Map<String, Object>> cancelList, HttpServletRequest request) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PATDEALAPI_03_014";
		String duplicateCheck = "";
		String paCode   = "";
		int executedRtn = 0;
		
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		
		log.info("======= 티딜 취소처리 API Start - {} =======");
		
		try {
			
			paramMap = paTdealApiRequest.getApiInfo(apiCode);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();
			
			// Body 세팅
			ParamMap apiDataObject = null;
			
			Map<String,Object> map = null;
			for(Map<String, Object> cancelMap : cancelList) { 
				
				try {
					apiDataObject = new ParamMap();
					
					paCode = cancelMap.get("PA_CODE").toString();
					paramMap.put("paCode", paCode);
					String reasonDetail =  cancelMap.containsKey("REASON_DETAIL")?  cancelMap.get("REASON_DETAIL").toString() : "판매자 취소(기타)"   ;
					map = new HashMap<String, Object>();
					map.put("orderNo", cancelMap.get("ORDER_NO").toString());
					map.put("reasonType", "OTHERS_SELLER");
					map.put("reasonDetail", reasonDetail);
					map.put("responsibleObjectType", "SELLER");
					map.put("refundType", "PG");
					map.put("inflowType", "SHOPBY_ADMIN");
					
					apiDataObject.put("body", map);
					// Body 세팅E
					
					//통신
					responseMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters,  queryParameters,  apiDataObject);
					
					if( !"204".equals(responseMap.get("status")) ){
						updatePaOrderm(cancelMap, null, "취소처리 실패", Constants.SAVE_FAIL); 
						paramMap.put("code", responseMap.get("status"));
						paramMap.put("message", "취소처리 승인 실패");
						continue;//실패건은 스킵
					}
					
					cancelMap.put("preCancelYn", "1");
					executedRtn = updatePaOrderm(cancelMap, null, "취소처리 성공", Constants.SAVE_SUCCESS);
					
					if(executedRtn>0) {
						paramMap.put("code", "200");
						paramMap.put("message", "취소처리 성공 및 저장 완료");
					}else {
						paramMap.put("code", "400");
						paramMap.put("message", "취소처리 저장 실패");
					}
					
				} catch (TransApiException e) {
					
					updatePaOrderm(cancelMap, null, "취소처리 실패",Constants.SAVE_FAIL); 
					paramMap.put("code", e.getCode());
					paramMap.put("message", "취소처리 실패 : " + e.getMessage());
					
				}  catch (Exception e) {
					updatePaOrderm(cancelMap, null, "취소처리 실패", Constants.SAVE_FAIL); 
					paramMap.put("code", responseMap.get("status"));
					paramMap.put("message", "취소처리 승인 실패 : " + e.getMessage());
				}
				
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(paramMap ,  e);
		}finally {
			if(paramMap.get("code") == null || "".equals(paramMap.get("code")) ) {
				paramMap.put("code", "200");
				paramMap.put("message", "취소처리 대상 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			paTdealConnectUtil.closeApi(request, paramMap);
			log.info("======= 티딜 취소처리 API End - {} =======");
		}

		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
	
	private int updatePaOrderm(Map<String, Object> confrimTarget, String paDoFlag, String message, String resultCode) {
		int executedRtn = 0;
		
		try {
			Map<String, Object> m = new HashMap<String,Object>();
			
			m.put("paDoFlag"		, paDoFlag);
			m.put("resultMessage"	, message);
			m.put("resultCode"		, resultCode);
			m.put("mappingSeq"		, confrimTarget.get("MAPPING_SEQ"));
			
			if(confrimTarget.containsKey("preCancelYn") && !"".equals(confrimTarget.get("preCancelYn"))) { 
				m.put("preCancelYn"		, confrimTarget.get("preCancelYn"));
			}
			
			executedRtn = paTdealDeliveryDAO.updatePaorderm(m);
		} catch (Exception e) {
			log.info("{} : {}", "티딜 제휴 주문 테이블 업데이트 오류",  e.getMessage() );
		}
		return executedRtn;
	}

	public ParamMap alreadyDelivery(Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PATDEALAPI_03_020";
		ParamMap apiDataObject = new ParamMap();
		ParamMap apiInfoMap	= new ParamMap();
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String, String> queryParameters = new HashMap<String, String>();
		Map<String,Object> returnMap = new HashMap<String, Object>();
		Map<String,Object> slipInfoMap = new HashMap<String, Object>();
		int executedRtn = 0;
		log.info("======= 티딜 이미발송(취소철회) API Start - {} =======");
		try {
				String duplicateCheck = "";
				map = new HashMap<String, Object>();
				apiInfoMap.put("paCode", requestMap.get("PA_CODE"));
				apiInfoMap.put("apiCode", apiCode);
				slipInfoMap = paTdealCancelDAO.selectSlipInfo(requestMap);
				if(slipInfoMap == null) {
					return null;
				}
				map.put("releaseYmd", DateUtil.toDateString(DateUtil.TDEAL_DATE_FORMAT));
				
				map.put("invoiceNo", slipInfoMap.get("SLIP_NO").toString());
				map.put("deliveryCompanyType", slipInfoMap.get("PA_DELY_GB").toString());
				apiDataObject.put("body", map);
				
				apiInfoMap = paTdealApiRequest.getApiInfo(apiCode);
				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
				apiInfoMap.put("duplicateCheck", duplicateCheck);
				returnMap = paTdealConnectUtil.getCommonLegacy(requestMap.get("PA_CODE").toString(), apiCode,ComUtil.objToStr(requestMap.get("CLAIM_NO")),queryParameters,apiDataObject);
				
				if((returnMap.get("status").equals("204") && !returnMap.containsKey("code"))) {
					requestMap.put("PROC_NOTE", "이미 발송");
					requestMap.put("PROC_FLAG", "20");
					executedRtn = paTdealCancelDAO.updatePaTdealCancelReject(requestMap);
					if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPATDEALCANCELLIST UPDATE" });
					
					executedRtn = updatePaOrderm(requestMap, "40", "발송완료", Constants.SAVE_SUCCESS);
					if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
					
					apiInfoMap.put("code", "200");
					apiInfoMap.put("message", "티딜 이미 발송 처리");
					apiInfoMap.put("status", returnMap.get("status").toString());
				}else if(returnMap.get("code").equals("CL114")){ //자동 철회 대기
					return null;
				} else {
					
					// 상태값 확인을 위한 재조회
					OrderDetail detailResponseMsg = null;
					detailResponseMsg = paTdealClaimProcess.orderInfoDetail(requestMap.get("PA_CODE").toString(), requestMap.get("ORDER_NO").toString(), request);
					List<ClaimInfos> responseData = detailResponseMsg.getClaimInfos();
					    // 클레임 수량, 단품번호를 알아 내기 위해 탐색
					for(ClaimInfos m : responseData) {
						if(requestMap.get("CLAIM_NO").toString().equals(ComUtil.objToStr(m.getClaimNo()))) {
							if(m.getClaimStatusType() == null) { //ClaimStatusType가 NULL일경우 철회 된 건
								executedRtn = paTdealCancelDAO.updatePaTdealCancelwithdrawYn(requestMap);
								
								if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TpaTdealClaimList withdrawYn UPDATE" });
								
								apiInfoMap.put("code", "200");
								apiInfoMap.put("message", returnMap.get("message").toString());
							} else if(m.getClaimStatusType().equals("CANCEL_DONE")) { //취소 요청건 일경우 API 오류
								
							}
						}
					}
				}
				
		}catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTdealConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info("======= 티딜 이미발송(취소철회) API End - {} =======");
		return apiInfoMap;
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception {
		return paTdealCancelDAO.selectPaMobileOrderAutoCancelList();
	}

	
	@Override
	public Map<String, Object> mobliecancelRequest(HashMap<String, String> cancelItem, HttpServletRequest request) throws Exception {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		HashMap<String, String> cancelMap = (HashMap<String, String>) cancelItem;
		ParamMap apiInfoMap	= new ParamMap();
		Map<String, String> orderOptionParams = new HashMap<String, String>();
		List<Map<String, String>>  orderOptionParamsList =  new ArrayList<Map<String, String>>();
		String paCode = cancelMap.get("PA_CODE").toString();
		String apiCode = "IF_PATDEALAPI_03_021";
		String pathParameters = "";
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		ParamMap apiDataObject = new ParamMap();
		Map<String,Object> map = new HashMap<String, Object>();
		
		HashMap<String, String> m 		= new HashMap<String, String>();
		
		log.info("======= 티딜 모바일 자동취소 (품절취소반품) 요청 API Start - {} =======");
		try {
			apiInfoMap.put("paCode", paCode);
			apiInfoMap.put("apiCode", apiCode);
			
			
			cancelMap.put("REASON_DETAIL", "재고부족");
			
			//취소 단품 세팅
			orderOptionParams.put("orderOptionNo", cancelMap.get("ORDER_PRODUCT_OPTION_NO"));
			orderOptionParams.put("claimCnt", cancelMap.get("PA_PROC_QTY"));
			orderOptionParams.put("reasonType", "OUT_OF_STOCK"); // OTHERS_SELLER <- 판매자 기타사유 였으나 재고부족으로 사유 변경하였음
			orderOptionParams.put("reasonDetail", cancelMap.get("REASON_DETAIL"));
			orderOptionParamsList.add(orderOptionParams);
			
			//취소 주문 세팅
			map.put("orderNo", cancelMap.get("PA_ORDER_NO"));
			map.put("responsibleObjectType", "SELLER");
			map.put("sellerPaysClaimedDelivery", true);
			map.put("inflowType", "SHOPBY_ADMIN");
			
			//주문 안에 단품이 배열로 들어가 있는 구조
			map.put("orderOptionParams", orderOptionParamsList);
			//body 세팅
			apiDataObject.put("body", map);
			apiInfoMap = paTdealApiRequest.getApiInfo(apiCode);
			String duplicateCheck = "";
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			apiInfoMap.put("duplicateCheck", duplicateCheck);
			
			m.put("PA_GROUP_CODE", Constants.PA_TDEAL_GROUP_CODE);
			m.put("PA_CODE", paCode);
			m.put("PA_ORDER_NO", cancelMap.get("PA_ORDER_NO"));
			m.put("PA_ORDER_SEQ", cancelMap.get("PA_ORDER_SEQ"));
			m.put("ORDER_NO", cancelMap.get("ORDER_NO"));
			m.put("ORDER_G_SEQ", cancelMap.get("ORDER_G_SEQ"));
			m.put("PROC_ID", Constants.PA_TDEAL_PROC_ID);
			
			//API통신
			returnMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode,pathParameters,queryParameters,apiDataObject);
			apiInfoMap.put("code", returnMap.get("status").toString());
			if( "204".equals(returnMap.get("status").toString()) ){
				m.put("REMARK3_N", "10");
				m.put("RSLT_MESSAGE", "모바일자동취소 성공");
				m.put("PA_GROUP_CODE", Constants.PA_TDEAL_GROUP_CODE);
				paorderService.updateRemark3NTx(m);
				
				//상담생성 & 문자발송 & 상품품절처리
				paCommonService.savePaMobileOrderCancelTx(m);
				apiInfoMap.put("message", "취소요청 완료");
			} else {
				m.put("REMARK3_N", "90");
				m.put("RSLT_MESSAGE", "모바일자동취소 실패 " + returnMap.get("message").toString());
				paorderService.updateRemark3NTx(m);
				apiInfoMap.put("message", "취소요청 실패" + returnMap.get("message").toString());
			}
		}catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(apiInfoMap, e);
			m.put("REMARK3_N", "90");
			m.put("RSLT_MESSAGE", "모바일자동취소 실패 " + e.getMessage());
			paorderService.updateRemark3NTx(m);
		}finally {
			paTdealConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info("======= 티딜 모바일 자동취소 (품절취소반품) 요청 API End - {} =======");
		return returnMap;
	}
}
