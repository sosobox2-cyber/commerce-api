package com.cware.netshopping.panaver.v3.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.v3.domain.DeliveryMethodType;
import com.cware.netshopping.panaver.v3.domain.OrderResponse;
import com.cware.netshopping.panaver.v3.domain.OrderResponseData;
import com.cware.netshopping.panaver.v3.process.PaNaverV3ExchangeProcess;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3DeliveryDAO;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3ExchangeDAO;
import com.cware.netshopping.panaver.v3.service.PaNaverV3ExchangeService;
import com.cware.netshopping.panaver.v3.util.PaNaverV3ConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("panaver.v3.exchange.paNaverV3ExchangeProcess")
public class PaNaverV3ExchangeProcessImpl extends AbstractProcess implements PaNaverV3ExchangeProcess {
	
	@Autowired
	SystemService systemService;
	
	@Autowired
	@Qualifier("panaver.v3.exchange.paNaverV3ExchangeService")
	PaNaverV3ExchangeService paNaverV3ExchangeService;
	
	@Autowired
	PaNaverV3DeliveryDAO paNaverV3DeliveryDAO;
	
	@Autowired
	PaNaverV3ExchangeDAO paNaverV3ExchangeDAO;
	
	@Autowired
	private PaNaverV3ConnectUtil paNaverV3ConnectUtil;
	
	@Override
	public ResponseMsg approvalCollect(String targetProductOrderId, String procId, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PANAVERAPI_V3_03_011";
		String duplicateCheck = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer(); // 수거완료 처리 실패 상품 목록
		
		Paorderm paorderm = null;
		String productOrderId = "";
		
		// API 트래킹 로그 데이터 세팅
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", procId);

		log.info("======= 네이버 교환 수거 완료 API Start =======");

		try {
			// API 중복실행 체크
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			// 교환 수거 완료 처리 대상 조회
			List<Paorderm> exchangeList = paNaverV3ExchangeDAO.selectExchangeReturnConfirmList();
			
			// 교환 수거 완료 처리 대상 없는 경우 체크
			if (exchangeList == null || exchangeList.isEmpty()) {
				paramMap.put("message", "교환 수거 완료 처리 대상 없음");
				log.info("Error msg : No Data Selected");
			} 

			for (int i=0; i<exchangeList.size(); i++) {
				try {
					paorderm = exchangeList.get(i);
					productOrderId = paorderm.getPaOrderSeq();
					
					// 타켓 상품주문번호 존재하는 경우, 해당 상품주문번호만 처리
					if (!targetProductOrderId.isEmpty() && !productOrderId.equals(targetProductOrderId)) {
						continue;
					}
					
					/** 1) 전문 데이터 세팅 및 호출 **/
					// Query Parameters 세팅
					Map<String, String> queryParameters = new HashMap<String, String>();
					
					// Path Parameters
					String pathParameters = productOrderId;
					
					// VO 선언
					OrderResponse response = new OrderResponse();
					
					Thread.sleep(500); // 네이버 호출량 제어 (1초당 2건)
					
					// 네이버 교환 수거 완료 호출
					resultMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, null);
					
					// Map -> VO 변환
					ObjectMapper objectMapper = new ObjectMapper();
					response = objectMapper.convertValue(resultMap, OrderResponse.class);
					
					OrderResponseData responseData = response.getData();

					// API 처리 결과 코드/메시지 세팅
					if (responseData != null && responseData.getSuccessProductOrderIds() != null && !responseData.getSuccessProductOrderIds().isEmpty()) {
						paramMap.put("code", "200");
						
						paorderm.setApiResultCode("000000");
						paorderm.setApiResultMessage("(naverProductOrderID " + productOrderId + ") 수거 완료 처리 성공");
						
						log.info("(naverProductOrderID " + productOrderId + ") " + paorderm.getMappingSeq() + " 수거 완료 처리 성공 ");
					} else if(responseData != null && responseData.getFailProductOrderInfos() != null && !responseData.getFailProductOrderInfos().isEmpty()) {
						log.info("Error msg : " + response.getData().getFailProductOrderInfos().get(0).getMessage());
						log.debug("ApproveCollectedExchange traceId :: {} failed", response.getTraceId());

						paorderm.setApiResultCode("999999");
						paorderm.setApiResultMessage(response.getData().getFailProductOrderInfos().get(0).getMessage() + " " + response.getTraceId());

						log.info("(naverProductOrderID " + productOrderId + ") " + paorderm.getMappingSeq() + " 수거 완료 처리 실패 ");
						sb.append("(naverProductOrderID " + productOrderId + ") " + paorderm.getMappingSeq() + " : 수거 완료 처리 실패 ");
					} else {
						paorderm.setApiResultCode("999999");
						paorderm.setApiResultMessage("(naverProductOrderID " + productOrderId + ") " + paorderm.getMappingSeq() + " : 수거 완료 처리 실패 ");
						
						log.info(paorderm.getApiResultMessage());
						sb.append(paorderm.getApiResultMessage());
					}
										
					/** 2) TPAORDERM 업데이트 (교환 수거 완료 처리 결과) **/
					paNaverV3ExchangeService.updatePaOrdermResultTx(paorderm);
				} catch (TransApiException ex) {
					log.info("(naverProductOrderID " + productOrderId + ") " + paorderm.getMappingSeq() + " : 수거 완료 처리 실패");
					sb.append("(naverProductOrderID " + productOrderId + ") " + paorderm.getMappingSeq() + " : 수거 완료 처리 실패");
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					sb.append("(naverProductOrderID " + productOrderId + ") " + paorderm.getMappingSeq() + " : 수거 완료 처리 실패");
				}
			}
			
			// 교환 수거 완료 처리 모두 성공시 200, 하나라도 실패하면 500
			if (sb.length() == 0) {
				paramMap.put("code", "200");
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", sb.toString()); // 수거완료 처리 실패 상품 목록
			}
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
			
			log.info("======= 네이버 교환 수거 완료 API End =======");
		}
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public int updatePaOrdermResult(Paorderm paorderm) throws Exception {
		String resultCode = paorderm.getApiResultCode();
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		
		if("000000".equals(resultCode)) {
			paorderm.setPaDoFlag("60");
			paorderm.setProcDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmss"));
		}
		
		if (paNaverV3ExchangeDAO.updatePaOrdermResult(paorderm) != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
		}
		
		if (resultCode.equals("999999")) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public ResponseMsg dispatch(String targetProductOrderId, String procId, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PANAVERAPI_V3_03_012";
		String duplicateCheck = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer(); // 교환 재배송 처리 실패 상품 목록
		
		HashMap<String, Object> reDeliveryMap = null;
		String productOrderId = "";
		String mappingSeq = "";
		
		// API 트래킹 로그 데이터 세팅
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", procId);

		log.info("======= 네이버 교환 재배송 처리 API Start =======");

		try {
			// API 중복실행 체크
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			// 교환 재배송 처리 대상 조회
			List<HashMap<String, Object>> reDeliveryList = paNaverV3ExchangeDAO.selectRedeliveryExchangeList();
			
			// 교환 재배송 처리 대상 없는 경우 체크
			if (reDeliveryList == null || reDeliveryList.isEmpty()) {
				paramMap.put("message", "교환 재배송 처리 대상 없음");
				log.info("Error msg : No Data Selected");
			} 

			for (int i=0; i<reDeliveryList.size(); i++) {
				try {
					reDeliveryMap = (HashMap<String, Object>)reDeliveryList.get(i);
					
					productOrderId = reDeliveryMap.get("PRODUCT_ORDER_ID").toString();
					mappingSeq = reDeliveryMap.get("MAPPING_SEQ").toString();
					
					// 타켓 상품주문번호 존재하는 경우, 해당 상품주문번호만 처리
					if (!targetProductOrderId.isEmpty() && !productOrderId.equals(targetProductOrderId)) {
						continue;
					}
					
					/** 1) 전문 데이터 세팅 및 호출 **/
					// Query Parameters 세팅
					Map<String, String> queryParameters = new HashMap<String, String>();
					
					// Path Parameters
					String pathParameters = productOrderId;
					
					// Body 세팅
					ParamMap apiDataObject = new ParamMap();
					apiDataObject.put("reDeliveryMethod", DeliveryMethodType.DELIVERY.toString()); // 택배, 등기, 소포
					apiDataObject.put("reDeliveryCompany", reDeliveryMap.get("PA_DELY_GB").toString());
					apiDataObject.put("reDeliveryTrackingNumber", reDeliveryMap.get("SLIP_NO").toString());
					
					// VO 선언
					OrderResponse response = new OrderResponse();
					
					Thread.sleep(500); // 네이버 호출량 제어 (1초당 2건)
					
					// 네이버 교환 재배송 처리 호출
					resultMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
					
					// Map -> VO 변환
					ObjectMapper objectMapper = new ObjectMapper();
					response = objectMapper.convertValue(resultMap, OrderResponse.class);
					
					OrderResponseData responseData = response.getData();
					Paorderm paorderm = new Paorderm();

					// API 처리 결과 코드/메시지 세팅
					if (responseData != null && responseData.getSuccessProductOrderIds() != null && !responseData.getSuccessProductOrderIds().isEmpty()) {
						paramMap.put("code", "200");
						
						paorderm.setPaDoFlag("40");
						paorderm.setPaOrderGb("40");
						paorderm.setApiResultCode("000000");
						paorderm.setApiResultMessage("교환재발송 성공");
						paorderm.setMappingSeq(mappingSeq);
					} else {
						// 재배송 처리 실패시 직접 전달로 재호출
						apiDataObject = new ParamMap();
						apiDataObject.put("reDeliveryMethod", DeliveryMethodType.DIRECT_DELIVERY.toString()); // 직접 전달 (일반택배가 아닌 판매자가 직접 또는 자체 발송을 통해 상품을 전달하는 방법으로 배송 현황 조회가 불가)
						
						// 네이버 교환 재배송 처리 호출
						resultMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
						
						// Map -> VO 변환
						response = objectMapper.convertValue(resultMap, OrderResponse.class);
						
						responseData = response.getData();
						
						if (responseData != null && responseData.getSuccessProductOrderIds() != null && !responseData.getSuccessProductOrderIds().isEmpty()) {
							paramMap.put("code", "200");
							
							paorderm.setPaDoFlag("40");
							paorderm.setPaOrderGb("40");
							paorderm.setApiResultCode("000000");
							paorderm.setApiResultMessage("교환재발송 성공");
							paorderm.setMappingSeq(mappingSeq);
						} else if(responseData != null && responseData.getFailProductOrderInfos() != null && !responseData.getFailProductOrderInfos().isEmpty()) {
							log.info("ReDeliveryExchange Error Msg : " + response.getData().getFailProductOrderInfos().get(0).getMessage());
							log.info("(naverProductOrderID " + productOrderId + ") " + mappingSeq + " : 교환재발송 실패");

							paorderm.setPaOrderGb("40");
							paorderm.setMappingSeq(mappingSeq);
							paorderm.setApiResultCode("999999");
							paorderm.setApiResultMessage("교환재발송 실패");
							
							sb.append("(naverProductOrderID " + productOrderId + ") " + mappingSeq + " : 교환재발송 실패 ("+response.getData().getFailProductOrderInfos().get(0).getMessage()+")");
						} else {
							log.info("(naverProductOrderID " + productOrderId + ") " + mappingSeq + " : 교환재발송 실패");
							
							paorderm.setPaOrderGb("40");
							paorderm.setMappingSeq(mappingSeq);
							paorderm.setApiResultCode("999999");
							paorderm.setApiResultMessage("교환재발송 실패");
							
							sb.append("(naverProductOrderID " + productOrderId + ") " + mappingSeq + " : 교환재발송 실패");
						}
					}
					
					/** 2) TPAORDERM 업데이트 (교환 재배송 처리 처리 결과) **/
					paNaverV3ExchangeService.updateExchangePaOrdermResultTx(paorderm);
				} catch (TransApiException ex) {
					log.info("(naverProductOrderID " + productOrderId + ") " + mappingSeq + " : 교환재발송 실패 ");
					sb.append("(naverProductOrderID " + productOrderId + ") " + mappingSeq + " : 교환재발송 실패");
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					sb.append("(naverProductOrderID " + productOrderId + ") " + mappingSeq + " : 교환재발송 실패");
				}
			}
			
			// 교환 재배송 처리 모두 성공시 200, 하나라도 실패하면 500
			if (sb.length() == 0) {
				paramMap.put("code", "200");
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", sb.toString()); // 교환 재배송 처리 실패 상품 목록
			}
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
			
			log.info("======= 네이버 교환 재배송 처리 API End =======");
		}
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public int updateExchangePaOrdermResult(Paorderm paorderm) throws Exception {
		return paNaverV3ExchangeDAO.updateExchangePaOrdermResult(paorderm);
	}

	@Override
	public ResponseMsg reject(String productOrderId, String rejectExchangeReason, String procId, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PANAVERAPI_V3_03_015";
		String duplicateCheck = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// API 트래킹 로그 데이터 세팅
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", procId);

		log.info("======= 네이버 교환 거부(철회) API Start =======");
		
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
			apiDataObject.put("rejectExchangeReason", rejectExchangeReason);
			
			// VO 선언
			OrderResponse response = new OrderResponse();
			
			/** 2) 네이버 교환 거부(철회) 호출 **/
			resultMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.convertValue(resultMap, OrderResponse.class);
			
			OrderResponseData responseData = response.getData();
			
			if (responseData != null && responseData.getSuccessProductOrderIds() != null && !responseData.getSuccessProductOrderIds().isEmpty()) {
				paramMap.put("code", "200");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 교환 거부 처리 완료");
				paramMap.put("apiResultCode", "00");
				paramMap.put("apiResultMessage", "교환 거부 처리 완료");
				paramMap.put("preCancelReason", "교환거부 처리");				
			} else if (responseData != null && responseData.getFailProductOrderInfos() != null && !responseData.getFailProductOrderInfos().isEmpty()) {
				log.debug("RejectExchange Error Msg : ", response.getData().getFailProductOrderInfos().get(0).getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") ID : "+response.getTraceId() +"/ Msg : "+ response.getData().getFailProductOrderInfos().get(0).getMessage() +"교환 거부 처리 실패");
				paramMap.put("apiResultCode", "99");
				paramMap.put("apiResultMessage", "교환 거부 처리 실패");
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") ID : "+response.getTraceId() +"교환 거부 처리 실패");
			}
			paramMap.put("productOrderId", productOrderId);
			paramMap.put("paOrderGb", "40");
			paramMap.put("mappingSeq", paNaverV3DeliveryDAO.selectMappingSeqByProductOrderInfo(paramMap));
			
			HashMap<String, String> orderMap = paNaverV3DeliveryDAO.selectOrderMappingInfoByMappingSeq(paramMap.getString("mappingSeq"));
			
			if (orderMap != null) {
				paramMap.put("claimId", orderMap.get("PA_CLAIM_NO").toString());
			}
			
			/** 3) 교환 거부(철회) 처리결과 업데이트 **/
			paNaverV3ExchangeService.updatePaOrdermPreCancelTx(paramMap);
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
			
			log.info("======= 네이버 교환 거부(철회) API End =======");
		}
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public int updatePaOrdermPreCancel(ParamMap paramMap) throws Exception {
		return paNaverV3ExchangeDAO.updatePaOrdermPreCancel(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverV3ExchangeDAO.selectChangeTargetDtList(paramMap);
	}

	@Override
	public HashMap<String, String> selectExchangeRejectInfo(String mappingSeq) throws Exception {
		return paNaverV3ExchangeDAO.selectExchangeRejectInfo(mappingSeq);
	}

	@Override
	public int checkOrderChangeTargetList(String orderId, String claimId) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("orderId", orderId);
		paramMap.put("claimId", claimId);	
		return paNaverV3ExchangeDAO.checkOrderChangeTargetList(paramMap);
	}

	@Override
	public int updatePaOrdermPreChangeCancel(Paorderm paorderm) throws Exception {
		return paNaverV3ExchangeDAO.updatePaOrdermPreChangeCancel(paorderm);
	}

	@Override
	public List<HashMap<String, Object>> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverV3ExchangeDAO.selectChangeCancelTargetDtList(paramMap);
	}

	@Override
	public ResponseMsg holdback(String productOrderId, String holdbackClassType, String holdbackExchangeDetailReason, Double extraExchangeFeeAmount, String procId, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PANAVERAPI_V3_03_013";
		String duplicateCheck = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// API 트래킹 로그 데이터 세팅
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", procId);

		log.info("======= 네이버 교환 보류 API Start =======");
		
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
			apiDataObject.put("holdbackExchangeDetailReason", holdbackExchangeDetailReason);
			apiDataObject.put("extraExchangeFeeAmount", extraExchangeFeeAmount);
			
			// VO 선언
			OrderResponse response = new OrderResponse();
			
			/** 2) 네이버 교환 보류 호출 **/
			resultMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.convertValue(resultMap, OrderResponse.class);
			
			OrderResponseData responseData = response.getData();
			
			if (responseData != null && responseData.getSuccessProductOrderIds() != null && !responseData.getSuccessProductOrderIds().isEmpty()) {
				paramMap.put("code", "200");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 교환 보류 처리 성공");		
			} else if (responseData != null && responseData.getFailProductOrderInfos() != null && !responseData.getFailProductOrderInfos().isEmpty()) {
				log.debug("Exchange Holdback Error Msg : ", response.getData().getFailProductOrderInfos().get(0).getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 교환 보류 처리 실패 / ID : "+response.getTraceId() +" / Msg : "+ response.getData().getFailProductOrderInfos().get(0).getMessage());
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 교환 보류 처리 실패 / ID : "+response.getTraceId());
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
			
			log.info("======= 네이버 교환 보류 API End =======");
		}
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public ResponseMsg releaseHoldback(String productOrderId, String procId, HttpServletRequest request) throws Exception {
		String apiCode = "IF_PANAVERAPI_V3_03_014";
		String duplicateCheck = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// API 트래킹 로그 데이터 세팅
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", procId);

		log.info("======= 네이버 교환 보류 해제 API Start =======");
		
		try {
			// API 중복실행 체크
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			/** 1) 전문 데이터 세팅 **/
			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();
			
			// Path Parameters
			String pathParameters = productOrderId;
			
			// VO 선언
			OrderResponse response = new OrderResponse();
			
			/** 2) 네이버 교환 보류 해제 호출 **/
			resultMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, null);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.convertValue(resultMap, OrderResponse.class);
			
			OrderResponseData responseData = response.getData();
			
			if (responseData != null && responseData.getSuccessProductOrderIds() != null && !responseData.getSuccessProductOrderIds().isEmpty()) {
				paramMap.put("code", "200");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 교환 보류 해제 처리 성공");		
			} else if (responseData != null && responseData.getFailProductOrderInfos() != null && !responseData.getFailProductOrderInfos().isEmpty()) {
				log.debug("Exchange Holdback Release Error Msg : ", response.getData().getFailProductOrderInfos().get(0).getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 교환 보류 해제 처리 실패 / ID : "+response.getTraceId() +" / Msg : "+ response.getData().getFailProductOrderInfos().get(0).getMessage());
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") 교환 보류 해제 처리 실패 / ID : "+response.getTraceId());
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
			
			log.info("======= 네이버 교환 보류 해제 API End =======");
		}
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public int checkOrderChangeInputTargetList(String orderId, String claimId) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("orderId", orderId);
		paramMap.put("claimId", claimId);	
		return paNaverV3ExchangeDAO.checkOrderChangeInputTargetList(paramMap);
	}
}
