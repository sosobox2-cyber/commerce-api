package com.cware.netshopping.panaver.v3.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.v3.domain.OrderResponse;
import com.cware.netshopping.panaver.v3.domain.OrderResponseData;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoAll;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoMsg;
import com.cware.netshopping.panaver.v3.process.PaNaverV3ClaimProcess;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3ClaimDAO;
import com.cware.netshopping.panaver.v3.service.PaNaverV3ClaimService;
import com.cware.netshopping.panaver.v3.service.PaNaverV3InfoCommonService;
import com.cware.netshopping.panaver.v3.util.PaNaverV3ConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("panaver.v3.claim.paNaverV3ClaimProcess")
public class PaNaverV3ClaimProcessImpl extends AbstractService implements PaNaverV3ClaimProcess{
	
	@Autowired
	private PaNaverV3ClaimDAO paNaverV3ClaimDAO;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	private PaNaverV3ConnectUtil paNaverV3ConnectUtil;
	
	@Autowired
	@Qualifier("panaver.v3.claim.paNaverV3ClaimService")
	private PaNaverV3ClaimService paNaverV3ClaimService;
	
	@Resource(name = "panaver.v3.infocommon.paNaverV3InfoCommonService")
	private PaNaverV3InfoCommonService paNaverV3InfoCommonService;
	
	/**
	 * 반품 승인
	 */
	@Override
	public ResponseMsg returnConfirmProc(String procId, HttpServletRequest request, String productOrderId) throws Exception {
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PANAVERAPI_V3_03_008";
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("productOrderId", productOrderId);
		
		StringBuffer sb = new StringBuffer();
		
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		
		ParamMap  apiDataObject             = null;
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		ObjectMapper objectMapper = null;
		OrderResponseData responseData  = null;
		
		log.info("======= 네이버 반품 승인 처리 API Start =======");
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			//반품 요청 건 조회
			List<HashMap<String, Object>> claimList = paNaverV3ClaimDAO.selectPaNaverClaimApprovalList(paramMap);
			
			for(Map<String, Object> claimMap :claimList) {
				try{
					if(claimMap.get("OUT_BEF_CLAIM_GB").equals("1")) { //제휴취소승인처리 후 변경주문내역조회(claim_completed)까지 한 케이스 처리
						claimMap.put("paDoFlag","60");
						claimMap.put("result_text","출고전 반품 성공");
						
						paramMap.put("code", "200");
						paramMap.put("message", "(naverProductOrderID " + claimMap.get("PRODUCT_ORDER_ID").toString() + ")" + " : 출고전 반품 성공");
						
					}else if(claimMap.get("PA_DO_FLAG").equals("90")) {
						claimMap.put("paDoFlag","60");
						claimMap.put("result_text","직권취소 성공");
						
						paramMap.put("code", "200");
						paramMap.put("message", "(naverProductOrderID " + claimMap.get("PRODUCT_ORDER_ID").toString() + ")" + " : 직권취소 성공");
					}else {
						//필요한 파라미터 및 통신 세팅
						apiDataObject = new ParamMap();
						
						pathParameters = claimMap.get("PRODUCT_ORDER_ID").toString();//상품 주문 번호
						// VO 선언
						OrderResponse response = new OrderResponse();
						
						Thread.sleep(500); // 네이버 호출량 제어 (1초당 2건)
						
						responseMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
						
						// Map -> VO 변환
						objectMapper = new ObjectMapper();
						response = objectMapper.convertValue(responseMap, OrderResponse.class);
						
						responseData = response.getData();
						
						if(responseData != null && responseData.getSuccessProductOrderIds() != null && !responseData.getSuccessProductOrderIds().isEmpty()) {
							
							paramMap.put("code", "200");
							paramMap.put("message", "(naverProductOrderId " + claimMap.get("PRODUCT_ORDER_ID").toString() + ")" + "requestId : " + response.getTraceId()+ " : 반품 승인 처리 완료");
							
							claimMap.put("paDoFlag", "60");
							claimMap.put("result_text", "반품승인처리 성공");
						}else {
							if(responseData != null && responseData.getFailProductOrderInfos() != null && !responseData.getFailProductOrderInfos().isEmpty()) {
								if(responseData.getFailProductOrderInfos().get(0).getMessage().contains("처리상태를 확인해 주세요")) {
									ProductOrderInfoMsg detailResponse =  paNaverV3InfoCommonService.orderDetailInfo(claimMap.get("PRODUCT_ORDER_ID").toString(), procId, request);
									
									if("200".equals(detailResponse.getCode()) && detailResponse.getProductOrderInfoList() != null && !detailResponse.getProductOrderInfoList().isEmpty()) {
										
										List<ProductOrderInfoAll> productOrderInfoList = detailResponse.getProductOrderInfoList();
										if(productOrderInfoList.get(0).getReturnOrder() != null  && "RETURN_DONE".equals(productOrderInfoList.get(0).getReturnOrder().getClaimStatus())) {
											
											paramMap.put("code","200");
											paramMap.put("message", "(naverProductOrderId " + claimMap.get("PRODUCT_ORDER_ID").toString() + ")" + "requestId : " + response.getTraceId()+ " : 반품 승인 API처리 하였으나, 이미 네이버 측 반품 완료 처리되어 있어  반품 완료 동기화");
											
											claimMap.put("result_text", "반품승인처리 성공");
											claimMap.put("paDoFlag", "60");
										}else { 
											paramMap.put("code", "400");
											paramMap.put("message", "(naverProductOrderId " + claimMap.get("PRODUCT_ORDER_ID").toString()+ ")" + " " + responseMap.get("traceId") + " (message:" + responseData.getFailProductOrderInfos().get(0).getMessage() + ") (code:" + responseData.getFailProductOrderInfos().get(0).getCode() +") : 반품 승인 처리 실패");
											log.info(response.getTraceId() + " : : 반품 승인 처리 실패 ");
											sb.append(paramMap.getString("message") + ", ");
										}
									}
								}else {
									paramMap.put("code", "400");
									paramMap.put("message", "(naverProductOrderId " + claimMap.get("PRODUCT_ORDER_ID").toString()+ ")" + " " + responseMap.get("traceId") + " (message:" + responseData.getFailProductOrderInfos().get(0).getMessage() + ") (code:" + responseData.getFailProductOrderInfos().get(0).getCode() +") : 반품 승인 처리 실패");
									log.info(response.getTraceId() + " : : 반품 승인 처리 실패 ");
									sb.append(paramMap.getString("message") + ", ");
								}
							}else {
								paramMap.put("code", "400");
								paramMap.put("message", "(naverProductOrderId " + claimMap.get("PRODUCT_ORDER_ID").toString()+ ")" + " " + responseMap.get("traceId") + " : 반품 승인 처리 실패");
								log.info(response.getTraceId() + " : : 반품 승인 처리 실패 ");
								sb.append(paramMap.getString("message") + ", ");
							}
						} 
					}
				} catch (TransApiException ex) {
					
					paramMap.put("code", "500");
					paramMap.put("message", ex.getMessage());
					
					log.error(ex.getMessage(), ex);
					sb.append("(naverProductOrderId " + claimMap.get("PRODUCT_ORDER_ID").toString() + ")" +claimMap.get("MAPPING_SEQ").toString() +"(message: "+ex.getMessage().toString()+ ") : 발품 승인 처리 실패, ");
					continue;
				} catch (Exception e) {
					paramMap.put("code", "500");
					paramMap.put("message", e.getMessage());
					
					log.error(e.getMessage(), e);
					sb.append("(naverProductOrderId " + claimMap.get("PRODUCT_ORDER_ID").toString() + ")" +claimMap.get("MAPPING_SEQ").toString() +"(message: "+e.getMessage().toString()+ ") : 반품 승인 처리 실패, ");
					continue;
				} finally {
					if(paramMap.get("code") != null && "200".equals(paramMap.get("code"))) {
						paNaverV3ClaimService.saveClaimApprovalProcTx(claimMap);	
					}
				}
			}
			
		}  catch (Exception e) {
			
			sb.append(e.getMessage());
			paramMap.put("code", "500");
			paramMap.put("message", sb.toString());
			
			paNaverV3ConnectUtil.dealException(e, paramMap);
			
			log.error(e.getMessage(), e);
			
		} finally {
			try {
				if(paramMap.get("code") == null) {
					paramMap.put("code", "200");
					paramMap.put("message", "수거완료처리 대상 없음");
				}else {
					paramMap.put("message", paramMap.get("message") + sb.toString());
				}
				paNaverV3ConnectUtil.dealSuccess(paramMap, request);
			} catch(Exception e) {
				log.error("ApiTracking Insertion Error : {}", e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode")); 
		}
		
		log.info("======= 네이버 반품 승인 처리 API End =======");
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public ParamMap saveClaimApprovalProc(Map<String, Object> claimMap) throws Exception {
		Paorderm paorderm = null;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		ParamMap resultMap = new ParamMap();
		try{
			
			paorderm = new Paorderm();
			paorderm.setMappingSeq(claimMap.get("MAPPING_SEQ").toString());
			paorderm.setPaCode(claimMap.get("PA_CODE").toString());
			paorderm.setPaOrderNo(claimMap.get("PA_ORDER_NO").toString());
			paorderm.setPaOrderSeq(claimMap.get("PRODUCT_ORDER_ID").toString());
			paorderm.setPaDoFlag(claimMap.get("paDoFlag").toString());
			paorderm.setApiResultCode("000000");
			paorderm.setApiResultMessage(claimMap.get("result_text").toString());
			paorderm.setProcDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setModifyId("PANAVER");
			
			if (paNaverV3ClaimDAO.updatePaOrdermResult(paorderm) != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
			else {
				resultMap.put("result_text", "반품승인처리 성공");
				resultMap.put("paDoFlag", "60");
			}
			
		}catch(Exception e){
			log.info(getMessage("errors.process") + " : fail ["+claimMap.get("PA_ORDER_NO")+"/"+claimMap.get("PA_ORDER_SEQ")+"]");
		}
		return resultMap;
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnClaimTargetList(ParamMap paramMap) throws Exception {
		return paNaverV3ClaimDAO.selectReturnClaimTargetList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnClaimTargetDt30List(ParamMap paramMap) throws Exception {
		return paNaverV3ClaimDAO.selectReturnClaimTargetDt30List(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnClaimTargetDt20List(ParamMap paramMap) throws Exception {
		return paNaverV3ClaimDAO.selectReturnClaimTargetDt20List(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverV3ClaimDAO.selectReturnCancelTargetDtList(paramMap);
	}
	
	/**
	 * 반품 보류
	 */
	@Override
	public ResponseMsg returnHoldbackProc(String productOrderId, String holdbackClassType, String holdbackReturnDetailReason, Double extraReturnFeeAmount, String procId, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PANAVERAPI_V3_03_009";
		String duplicateCheck = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// API 트래킹 로그 데이터 세팅
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", procId);

		log.info("======= 네이버 반품 보류 API Start =======");
		
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
			apiDataObject.put("holdbackClassType", holdbackClassType);
			apiDataObject.put("holdbackReturnDetailReason", holdbackReturnDetailReason);
			apiDataObject.put("extraReturnFeeAmount", extraReturnFeeAmount);
			
			// VO 선언
			OrderResponse response = new OrderResponse();
			
			/** 2) 네이버 반품 보류 호출 **/
			resultMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.convertValue(resultMap, OrderResponse.class);
			
			OrderResponseData responseData = response.getData();
			
			if (responseData != null && responseData.getSuccessProductOrderIds() != null && !responseData.getSuccessProductOrderIds().isEmpty()) {
				paramMap.put("code", "200");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 반품 보류 처리 성공");		
			} else if (responseData != null && responseData.getFailProductOrderInfos() != null && !responseData.getFailProductOrderInfos().isEmpty()) {
				log.debug("Return Holdback Error Msg : ", response.getData().getFailProductOrderInfos().get(0).getMessage());
				paramMap.put("code", "400");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 반품 보류 처리 실패 / ID : "+response.getTraceId() +" / Msg : "+ response.getData().getFailProductOrderInfos().get(0).getMessage());
			} else {
				paramMap.put("code", "400");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 반품 보류 처리 실패 / ID : "+response.getTraceId());
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
			
			log.info("======= 네이버 반품 보류 API End =======");
		}
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	/**
	 * 반품 보류 해제
	 */
	@Override
	public ResponseMsg returnHoldbackReleaseProc(String productOrderId,  String procId, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PANAVERAPI_V3_03_010";
		String duplicateCheck = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// API 트래킹 로그 데이터 세팅
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", procId);
		
		log.info("======= 네이버 반품 보류 해제 API Start =======");
		
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
			
			// VO 선언
			OrderResponse response = new OrderResponse();
			
			/** 2) 네이버 반품 보류 해제 호출 **/
			resultMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.convertValue(resultMap, OrderResponse.class);
			
			OrderResponseData responseData = response.getData();
			
			if (responseData != null && responseData.getSuccessProductOrderIds() != null && !responseData.getSuccessProductOrderIds().isEmpty()) {
				paramMap.put("code", "200");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 반품 보류 해제 처리 성공");		
			} else if (responseData != null && responseData.getFailProductOrderInfos() != null && !responseData.getFailProductOrderInfos().isEmpty()) {
				log.debug("Exchange Holdback Error Msg : ", response.getData().getFailProductOrderInfos().get(0).getMessage());
				paramMap.put("code", "400");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 반품 보류 해제 처리 실패 / ID : "+response.getTraceId() +" / Msg : "+ response.getData().getFailProductOrderInfos().get(0).getMessage());
			} else {
				paramMap.put("code", "400");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 반품 보류 해제 처리 실패 / ID : "+response.getTraceId());
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
			
			log.info("======= 네이버 반품 보류 해제 API End =======");
		}
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
}	

