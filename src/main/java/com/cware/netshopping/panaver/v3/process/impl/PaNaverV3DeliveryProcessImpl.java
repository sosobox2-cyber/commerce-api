package com.cware.netshopping.panaver.v3.process.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.panaver.v3.domain.ConfirmProcResponse;
import com.cware.netshopping.panaver.v3.domain.ConfirmProcResponseData;
import com.cware.netshopping.panaver.v3.domain.DeliveryMethodType;
import com.cware.netshopping.panaver.v3.domain.FailProductOrderInfo;
import com.cware.netshopping.panaver.v3.domain.OrderResponse;
import com.cware.netshopping.panaver.v3.domain.OrderResponseData;
import com.cware.netshopping.panaver.v3.domain.PaNaverV3AddressVO;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoAll;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoMsg;
import com.cware.netshopping.panaver.v3.domain.ShippingAddress;
import com.cware.netshopping.panaver.v3.domain.TakingAddress;
import com.cware.netshopping.panaver.v3.process.PaNaverV3DeliveryProcess;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3CancelDAO;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3DeliveryDAO;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3InfoCommonDAO;
import com.cware.netshopping.panaver.v3.service.PaNaverV3DeliveryService;
import com.cware.netshopping.panaver.v3.service.PaNaverV3InfoCommonService;
import com.cware.netshopping.panaver.v3.util.PaNaverV3ConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("panaver.v3.delivery.paNaverV3DeliveryProcess")
public class PaNaverV3DeliveryProcessImpl extends AbstractService implements PaNaverV3DeliveryProcess{
	
	
	@Autowired
	private PaNaverV3DeliveryDAO paNaverV3DeliveryDAO;
	
	@Autowired
	private PaNaverV3InfoCommonDAO paNaverV3InfoCommonDAO;
	
	@Autowired
	PaNaverV3CancelDAO paNaverV3CancelDAO;
	
	@Autowired
	private PaCommonDAO paCommonDAO;
	
	@Autowired
	private SystemDAO systemDAO;
    
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	private PaNaverV3ConnectUtil paNaverV3ConnectUtil;
	
	@Resource(name = "panaver.v3.infocommon.paNaverV3InfoCommonService")
	private PaNaverV3InfoCommonService paNaverV3InfoCommonService;
	
	@Autowired
	@Qualifier("panaver.v3.delivery.paNaverV3DeliveryService")
	private PaNaverV3DeliveryService paNaverV3DeliveryService;
	
	/**
	 * 발주 처리
	 */
	@Override
	public ResponseMsg orderConfirmProc(String productOrderId, String procId, HttpServletRequest request) throws Exception {
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PANAVERAPI_V3_03_003";
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PANAVER");
		
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		log.info("======= 네이버 주문 발주 확인 처리 API Start =======");
		try {
			ProductOrderInfoMsg response =  paNaverV3InfoCommonService.orderDetailInfo(productOrderId, procId, request);
	
			if("200".equals(response.getCode()) && response.getProductOrderInfoList() != null && !response.getProductOrderInfoList().isEmpty()) {
				
				List<ProductOrderInfoAll> productOrderInfoList = response.getProductOrderInfoList();
				
				if(productOrderInfoList.get(0).getProductOrder().getGiftReceivingStatus() == null 
						|| (productOrderInfoList.get(0).getProductOrder().getGiftReceivingStatus() != null 
						&& productOrderInfoList.get(0).getProductOrder().getGiftReceivingStatus().equals("RECEIVED"))) {
					//선물하기랑 상관없는 주문이거나    OR  선물 받기 수락한 상태인 경우(수락 완료)
					try {
						duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
						if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
						
						//필요한 파라미터 및 통신 세팅
						// Body 세팅S
						ParamMap apiDataObject = new ParamMap();
						String [] productOrderIdsArr = {productOrderId};
						apiDataObject.put("productOrderIds",productOrderIdsArr);
						// Body 세팅 E
						// VO 선언
						ConfirmProcResponse confirmProcResponse = new ConfirmProcResponse();
						
						responseMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
						
						// Map -> VO 변환
						ObjectMapper objectMapper = new ObjectMapper();
						confirmProcResponse = objectMapper.convertValue(responseMap, ConfirmProcResponse.class);
						ConfirmProcResponseData confirmProcResponseData  = confirmProcResponse.getData();
						if(confirmProcResponseData != null && confirmProcResponseData.getSuccessProductOrderInfos().size() > 0) {
							if(paNaverV3DeliveryService.updateOrderChangePlaceOrderTx(productOrderInfoList.get(0)) > 0) {
								paramMap.put("code", "200");
								paramMap.put("message", "발주 요청, 저장 성공 " + responseMap.get("traceId") + "(naverProductOrderID " + productOrderId+ ")");
							
							}
							else {
								paramMap.put("code", "500");
								paramMap.put("message", "발주 요청 성공, 저장 실패 " + responseMap.get("traceId") + "(naverProductOrderID " + productOrderId+ ")");
							}
							
						}else {
							log.error("getOrderConfirmProc requestId :: {} failed", responseMap.get("traceId") + "(naverProductOrderID " + productOrderId+ ")");
							paramMap.put("code", "400");
						
							if(confirmProcResponseData != null && confirmProcResponseData.getFailProductOrderInfos().size()>0) {
								paramMap.put("message", "(naverProductOrderID " + productOrderId+ ")" + " " + responseMap.get("traceId") + " (message:" + confirmProcResponseData.getFailProductOrderInfos().get(0).getMessage() + ") (code:" + confirmProcResponseData.getFailProductOrderInfos().get(0).getCode() +") : 발주 처리 실패");
							}else {
								paramMap.put("message", "(naverProductOrderID " + productOrderId+ ")" + " " + responseMap.get("traceId") + " : 발주 처리 실패");
							}
						}
						
					} catch (TransApiException ex) {
						paramMap.put("code", "500");
						paramMap.put("message", ex.getMessage());
						
						log.error(ex.getMessage(), ex);
					} catch (Exception e) {
						
						paramMap.put("code", "500");
						paramMap.put("message", e.getMessage());
						
						paNaverV3ConnectUtil.dealException(e, paramMap);
						
						log.error(e.getMessage(), e);
					}finally {
						try {
							paNaverV3ConnectUtil.dealSuccess(paramMap, request);
						}
						catch(Exception e) {
							log.error("ApiTracking Insertion Error : {}", e.getMessage());
						}
						if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
					}
				}
			}else {
				log.error("getProductOrderInfoList while placeOrder failed");
				paramMap.put("code", "400");
				paramMap.put("message", "발주처리 전 주문상세내역 조회 에러"  + "(naverProductOrderID " + productOrderId+ ")");
			}
			
		}  catch (Exception e) {
			
			log.error("getProductOrderInfoList failed naverProductOrderID : " + productOrderId);
			paramMap.put("code", "500");
			paramMap.put("message", "발주 처리 실패" + "(naverProductOrderID " + productOrderId+ "), " + e.getMessage());
			
			return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		} 
		
		log.info("======= 네이버 주문 발주 확인 처리 API End =======");
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
    /**
	 * 발주 처리 결과 저장
	 */
	@Override
	public int updateOrderChangePlaceOrder(ProductOrderInfoAll productOrderInfoAll) throws Exception {
		int resultCnt = 0;
	
		ParamMap paramMap = new ParamMap();
		if(paNaverV3DeliveryDAO.updateOrderChangePlaceOrder(productOrderInfoAll.getProductOrder().getProductOrderId()) > 0) { //TPANAVERORDERCHANGE의  PLACE_DATE 업데이트
			if(productOrderInfoAll.getOrder()!= null) {
				if(paNaverV3DeliveryDAO.mergeNaverOrderm(productOrderInfoAll.getOrder()) < 1) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERORDERM INSERT" });
				}
				if(paNaverV3DeliveryDAO.mergeOrderList(productOrderInfoAll.getProductOrder()) < 1) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST INSERT" });
				}

				ShippingAddress shippingAddress = productOrderInfoAll.getProductOrder().getShippingAddress();
				if(null != shippingAddress) {
					paramMap.put("shippingAddressSeq", paNaverV3InfoCommonDAO.selectAddressSeq());

					PaNaverV3AddressVO address = new PaNaverV3AddressVO();
					address.setAddressSeq(paramMap.getString("shippingAddressSeq"));	
					address.setShippingAddress(shippingAddress);
					
					if(paNaverV3InfoCommonDAO.insertPaNaverAddress(address) < 1) {
						throw processException("msg.cannot_save", new String[] { "TPANAVERADDRESS SHIPPINGADDRESS INSERT" });
					}
				}
				
				TakingAddress takingAddress = productOrderInfoAll.getProductOrder().getTakingAddress();
				if(null != takingAddress) {
					paramMap.put("takingAddressSeq", paNaverV3InfoCommonDAO.selectAddressSeq());
					
					PaNaverV3AddressVO address = new PaNaverV3AddressVO();
					address.setAddressSeq(paramMap.getString("takingAddressSeq"));	
					address.setTakingAddress(takingAddress);

					if(paNaverV3InfoCommonDAO.insertPaNaverAddress(address) < 1) {
						throw processException("msg.cannot_save", new String[] { "TPANAVERADDRESS TAKINGADDRESS INSERT" });
					}
				}
				paramMap.put("productOrderId", productOrderInfoAll.getProductOrder().getProductOrderId());
				
				if(paramMap.get("shippingAddressSeq") != null || paramMap.get("takingAddressSeq") != null) {
					if(paNaverV3InfoCommonDAO.updateOrderListAddress(paramMap) < 0) {
						throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST ADDRESS UPDATE" });
					}
				}
				paramMap.put("orderId", productOrderInfoAll.getOrder().getOrderId());
				paramMap.put("quantity", productOrderInfoAll.getProductOrder().getQuantity());
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String sysdate = systemDAO.getSysdatetime();
				Paorderm paorderm = new Paorderm();
				paorderm.setPaOrderGb("10");
				paorderm.setPaCode("41");
				paorderm.setPaOrderNo(productOrderInfoAll.getOrder().getOrderId());
				paorderm.setPaOrderSeq(productOrderInfoAll.getProductOrder().getProductOrderId());
				paorderm.setPaShipNo("");
				paorderm.setPaProcQty(String.valueOf(productOrderInfoAll.getProductOrder().getQuantity()));
				if(productOrderInfoAll.getProductOrder().getGiftReceivingStatus() == null) {
					paorderm.setPaDoFlag("30");
				}
				else {
					if(productOrderInfoAll.getProductOrder().getGiftReceivingStatus().equals("WAIT_FOR_RECEIVING")) {
						paorderm.setPaDoFlag("10");
					}else {
						paorderm.setPaDoFlag("30");
					} 
				}
				paorderm.setOutBefClaimGb("0");
				paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
				paorderm.setInsertDate(new Timestamp(dateFormat.parse(sysdate).getTime()));
				paorderm.setModifyDate(new Timestamp(dateFormat.parse(sysdate).getTime()));
				paorderm.setPaGroupCode("04");
				paorderm.setModifyId("PANAVER");
				paorderm.setApiResultCode("000000");
				paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
				
				if(paNaverV3InfoCommonDAO.selectWaitingPaordermCnt(paramMap) < 1) { //TPAORDERM에 해당 주문번호로 주문, 접수 (PA_ORDER_GB = '10' AND PA_DO_FLAG = '10')
					if(paNaverV3DeliveryDAO.selectNotPlacedAndPayedOrder(productOrderInfoAll.getProductOrder().getProductOrderId()) == null) {
						//TPANAVERORDERCHANGE에 발주처리되지 않은 주문(PLACE_DATE IS NULL)이 존재하지 않으면
						//(해당 주문으로 발주처리까지 된 주문인 경우 place_date값이 맨위 updateOrderChangePlaceOrder 에서 place_date 업데이트 해주기 때문에 null 값으로 나옴 )
						resultCnt = paCommonDAO.insertPaOrderM(paorderm);
					}
				}
				
				if(paNaverV3InfoCommonDAO.updatePaOrdermOrderPlace(paramMap) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM PA_PROC_QTY UPDATE" });
				}
				if(paNaverV3InfoCommonDAO.updateOrderListOrderId(paramMap) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST ORDER_ID UPDATE" });
				}
				
			}	
			return resultCnt;
		}
		else {
			throw processException("msg.cannot_save", new String[] { "TPANAVERORDERCHANGE UPDATE" });
		}
	} 
	
	
	/**
	 * 발송 처리
	 */
	@Override
	public ResponseMsg slipOutProc( String procId, HttpServletRequest request, String productOrderId) throws Exception {
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PANAVERAPI_V3_03_005";
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
		
		List<Map<String, Object>> dispatchProductOrdersList          = null;
		List<Map<String, Object>> dispatchRetryProductOrdersList     = null;
		ObjectMapper objectMapper = null;
		OrderResponseData responseData  = null;
		
		log.info("======= 네이버 주문 발송 처리 API Start =======");
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			List<HashMap<String, Object>> slipProcList = paNaverV3DeliveryDAO.selectSlipProcList(paramMap);
			List<HashMap<String, Object>> slipSubProcList = null; 

			int size   = slipProcList.size(); 
			int std    = 30;
			int cnt    = (int) Math.ceil(size/(double)std); // 소수점 있으면 무조건 올림처리 2.3 -> 3
			int fIndex = 0;
			int tIndex = 0;
			
			for(int i = 0 ; i< cnt ; i++) {
				
				try {
					/*
					ex) 
						size = 15, std = 3 ---> cnt = 5
						1. 15 > 0 + 3 ---> tIndex = 3 --- > subList(0, 3) --> fIndex = 0 + 3 = 3
						2. 15 > 3 + 3 ---> tIndex = 6 --- > subList(3, 6) --> fIndex = 3 + 3 = 6
						3. 15 > 6 + 3 ---> tIndex = 9 --- > subList(6, 9) --> fIndex = 6 + 3 = 9
						4. 15 > 9 + 3 ---> tIndex = 12 --- > subList(9, 12) --> fIndex = 9 + 3 = 12
						5. 15 = 12 + 3 ---> (tIndex = size ) tIndex = 15 --- > subList(12, 15) 
						end
					 */
					if(size > tIndex + std) {
						tIndex = tIndex + std;
					}else {// 작거나 같은 경우 
						tIndex = size;
					}
					
					slipSubProcList = slipProcList.subList(fIndex, tIndex);
					
					dispatchProductOrdersList = settingDispatch(slipSubProcList, DeliveryMethodType.DELIVERY.toString());
					
					if(dispatchProductOrdersList!=null && !dispatchProductOrdersList.isEmpty()) {
						
						apiDataObject = new ParamMap();
						
						apiDataObject.put("dispatchProductOrders", dispatchProductOrdersList);
						
						// VO 선언
						OrderResponse response = new OrderResponse();
						
						Thread.sleep(500); // 네이버 호출량 제어 (1초당 2건)
						
						responseMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
						// Map -> VO 변환
						objectMapper = new ObjectMapper();
						response = objectMapper.convertValue(responseMap, OrderResponse.class);
						
						responseData = response.getData();
						
						List<String> successList = responseData.getSuccessProductOrderIds(); 
						
						for(String proudctOrderId :successList) {
							
							Optional<HashMap<String, Object>>  result =  slipSubProcList.stream().filter(x -> x.get("PRODUCT_ORDER_ID").equals(proudctOrderId)).findAny();
							if (result.isPresent() == true) {
								paramMap.put("apiResultCode", "000000");
								paramMap.put("resultMessage", "발송 처리 성공");
								paramMap.put("code", "200");
								paramMap.put("message", "(naverProductOrderId " + proudctOrderId + ")" + "requestId : " + response.getTraceId()+ " : 발송 처리 완료");
								
								paNaverV3DeliveryService.updatePaOrdermResultTx(paramMap, result.get());	
							} 
						}
						List<FailProductOrderInfo> failProductOrderInfos = responseData.getFailProductOrderInfos();
						List<HashMap<String, Object>> failRetryList = new ArrayList<HashMap<String, Object>>();
						
						if(failProductOrderInfos != null && !failProductOrderInfos.isEmpty()) {
							
							for(FailProductOrderInfo failInfo : failProductOrderInfos) {
								Optional<HashMap<String, Object>>  result =  slipSubProcList.stream().filter(x -> x.get("PRODUCT_ORDER_ID").equals(failInfo.getProductOrderId())).findAny();
								
								if (result.isPresent() == true) {
									failRetryList.add(result.get());
								} 
								
							}// 배송방법이 DELIVERY 인경우 송장번호 유효하지 않으면 발송처리 반려하여, 실패 케이스들 DIRECT_DELIVERY(직접배송,송장번호 필요없음) 방식으로 다시한번 발송처리 
							dispatchRetryProductOrdersList = settingDispatch(failRetryList, DeliveryMethodType.DIRECT_DELIVERY.toString());
							// retry
							if(dispatchRetryProductOrdersList != null && !dispatchRetryProductOrdersList.isEmpty()) {
								
								apiDataObject = new ParamMap();
								apiDataObject.put("dispatchProductOrders", dispatchRetryProductOrdersList);
								
								// VO 선언
								OrderResponse response2 = new OrderResponse();
								
								responseMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
								//Thread.sleep(600);
								// Map -> VO 변환
								objectMapper = new ObjectMapper();
								response2 = objectMapper.convertValue(responseMap, OrderResponse.class);
								
								responseData = response2.getData();
								
								List<String> successRertyList = responseData.getSuccessProductOrderIds(); 
								
								for(String proudctOrderId :successRertyList) {
									Optional<HashMap<String, Object>>  result =  slipSubProcList.stream().filter(x -> x.get("PRODUCT_ORDER_ID").equals(proudctOrderId)).findAny();
									if (result.isPresent() == true) {
										paramMap.put("apiResultCode", "000000");
										paramMap.put("resultMessage", "발송 처리 성공");
										paramMap.put("code", "200");
										paramMap.put("message", "(naverProductOrderId " + proudctOrderId + ")" + "requestId : " + response2.getTraceId()+ " : 발송 처리 완료");
										
										paNaverV3DeliveryService.updatePaOrdermResultTx(paramMap, result.get());	
									} 
								}
								for(FailProductOrderInfo failInfo : responseData.getFailProductOrderInfos()) {
									paramMap.put("code", "400");
									paramMap.put("message", "(naverProductOrderId " + failInfo.getProductOrderId()+ ")" + " requestId : " + response.getTraceId() + " (message:" + failInfo.getMessage() + ") (code:" + failInfo.getCode() +") : 발송 처리 실패");
									
									sb.append(paramMap.getString("message") + ", ");
								}
							}
						}
					}
					
				} catch (TransApiException ex) {
					
					paramMap.put("code", "500");
					paramMap.put("message", ex.getMessage());
					sb.append(paramMap.get("message"));
					log.error(ex.getMessage(), ex);
				} catch (Exception e) {
					paramMap.put("code", "500");
					paramMap.put("message", e.getMessage());
					sb.append(paramMap.get("message"));
					log.error(e.getMessage(), e);
				}finally {
					fIndex  = fIndex + std;
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
					paramMap.put("message", "발송 대상 없음");
				}
				else {
					paramMap.put("message", paramMap.get("message") + sb.toString());
				}
				paNaverV3ConnectUtil.dealSuccess(paramMap, request);
			} catch(Exception e) {
				log.error("ApiTracking Insertion Error : {}", e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode")); 
		}
		
		log.info("======= 네이버 주문 발송 확인 처리 API End =======");
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
	
	public List<Map<String, Object>> settingDispatch(List<HashMap<String, Object>> slipProcList, String deliveryMethod) throws Exception {
		
		List<Map<String,Object>> dispatchProductOrdersList = new ArrayList<Map<String,Object>>();
		Map<String,Object> dispatchProductOrdersMap = null;
		
		String trackingNumber = "";
		String dispatchDate = "";
		
		for(Map<String, Object> procMap :slipProcList) {
			
			dispatchProductOrdersMap = new HashMap<String, Object>();
			dispatchProductOrdersMap.put("productOrderId",procMap.get("PRODUCT_ORDER_ID").toString());//상품 주문 번호
			dispatchProductOrdersMap.put("deliveryMethod",deliveryMethod);//배송 방법 코드
			dispatchProductOrdersMap.put("deliveryCompanyCode",procMap.get("DELIVERY_COMPANY_CODE").toString());//택배사 코드
			
			if(DeliveryMethodType.DELIVERY.toString().equals(deliveryMethod)) {
				trackingNumber = procMap.get("TRACKING_NUMBER").toString();
				if(null != trackingNumber && !trackingNumber.equals("")) {
					dispatchProductOrdersMap.put("trackingNumber",trackingNumber);//송장 번호
				}
			}
			
			dispatchDate = procMap.get("DISPATCH_DATE").toString();
			if(null != dispatchDate && !dispatchDate.equals("")) {
				Calendar dispatchCalendar = Calendar.getInstance();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				dispatchCalendar.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(dispatchDate));
				dispatchProductOrdersMap.put("dispatchDate",dateFormat.format(dispatchCalendar.getTime()));//배송일 string <date-time>
			}
			
			dispatchProductOrdersList.add(dispatchProductOrdersMap);
		}
		
		
		return dispatchProductOrdersList;
	}
	
	@Override
	public int updatePaOrdermResult(ParamMap paramMap, Map<String, Object> procMap) throws Exception {
		try {
			Paorderm paorderm = new Paorderm();
			paorderm.setMappingSeq(procMap.get("MAPPING_SEQ").toString());
			paorderm.setApiResultCode(paramMap.getString("apiResultCode"));
			paorderm.setApiResultMessage(paramMap.getString("resultMessage"));
			paorderm.setPaDoFlag(ComUtil.objToStr(procMap.get("PA_DO_FLAG")));
			paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
			paorderm.setPaOrderNo(procMap.get("PA_ORDER_NO").toString());
			paorderm.setPaOrderSeq(procMap.get("PRODUCT_ORDER_ID").toString());
			paorderm.setModifyId("PANAVER");
			if(paramMap.get("slipDelayed") != null) paorderm.setRemark1V("발송예정일 연기 " + procMap.get("DELY_DATE").toString());
			if(null != paramMap.get("isAll")) {
				paorderm.setIsAll(paramMap.get("isAll").toString());
			}
			
			if(paNaverV3DeliveryDAO.updatePaOrdermResult(paorderm) != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
			else {
				if(paNaverV3DeliveryDAO.selectOrderCancelCnt(paorderm) > 0) {  
					
					ParamMap parmamMap2 = new ParamMap();
					parmamMap2.put("paOrderGb", "20");
					parmamMap2.put("preCancelYn", "1");
					parmamMap2.put("preCancelReason", "발송처리로 인한 기취소 처리");
					parmamMap2.put("orderId", procMap.get("PA_ORDER_NO").toString());
					parmamMap2.put("productOrderId", procMap.get("PRODUCT_ORDER_ID").toString());
					parmamMap2.put("mappingSeq", paNaverV3DeliveryDAO.selectMappingSeqByProductOrderInfo(parmamMap2));
					if(paNaverV3CancelDAO.updatePreCancelYn(parmamMap2) != 1) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM PRE_CANCEL_YN UPDATE" });
					}
					
				}
				paramMap.put("productOrderId", procMap.get("PRODUCT_ORDER_ID").toString());
				paramMap.put("shippingDueDate", procMap.get("DELY_DATE").toString());
				if(paNaverV3DeliveryDAO.updateShippingDueDate(paramMap) != 1) {
					throw processException("msg.cannot_save", new String[] { "TPNAVERORDERLIST UPDATE" });
				}
				else {
					return 1;
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;			
		}
	}
	
	@Override
	public HashMap<String, String> selectOrderMappingInfoByMappingSeq(String mappingSeq) throws Exception {
		return paNaverV3DeliveryDAO.selectOrderMappingInfoByMappingSeq(mappingSeq);
	}
	
	@Override
	public List<HashMap<String, Object>> selectOrderInputTargetDtList(String orderId) throws Exception {
		return paNaverV3DeliveryDAO.selectOrderInputTargetDtList(orderId);
	}
	
	/**
	 * 발송 지연 처리
	 */
	@Override
	public ResponseMsg deliveryDelayProc( String procId, HttpServletRequest request, String productOrderId) throws Exception {
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PANAVERAPI_V3_03_004";
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("productOrderId", productOrderId);
		
		StringBuffer sb = new StringBuffer();
		
		// Path Parameters
		String pathParameters = "";
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		OrderResponseData responseData      = null;
		
		ParamMap  apiDataObject             = null;
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		String dispatchDueDate ="";
		ObjectMapper objectMapper = null;
		
		log.info("======= 네이버 주문 발송 지연 처리 API Start =======");
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			List<HashMap<String, Object>> delayProcList = paNaverV3DeliveryDAO.selectDelayProcList(paramMap);
			
			for(Map<String, Object> procMap :delayProcList) {
				try{
					//필요한 파라미터 및 통신 세팅
					//pathParameters세팅
					pathParameters = procMap.get("PRODUCT_ORDER_ID").toString();
					
					apiDataObject = new ParamMap();
					// Body 세팅S
					
					dispatchDueDate = procMap.get("DELY_DATE").toString();
					if(null != dispatchDueDate && !dispatchDueDate.equals("")) {
						Calendar dispatchCalendar = Calendar.getInstance();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
						dispatchCalendar.setTime(dateFormat.parse(dispatchDueDate));
						apiDataObject.put("dispatchDueDate", dispatchCalendar);
					}
					
					apiDataObject.put("delayedDispatchReason", "ETC");
					apiDataObject.put("dispatchDelayedDetailedReason",procMap.get("REASON_DETAIL").toString() );
					// Body 세팅 E
					
					// VO 선언
					OrderResponse response = new OrderResponse();
					
					Thread.sleep(500); // 네이버 호출량 제어 (1초당 2건)
					
					// 통신 
					responseMap = paNaverV3ConnectUtil.getCommonLegacy(apiCode, pathParameters, queryParameters, apiDataObject);
					
					objectMapper = new ObjectMapper();
					response = objectMapper.convertValue(responseMap, OrderResponse.class);
					
					responseData = response.getData();
			
					if(responseData != null && responseData.getSuccessProductOrderIds() != null && !responseData.getSuccessProductOrderIds().isEmpty()) {
						paramMap.put("apiResultCode", "000000");
						paramMap.put("resultMessage", "발송 지연 처리 완료");
						paramMap.put("slipDelayed", "1");
						paramMap.put("code", "200");
						paramMap.put("message", "(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString() + ")" + "requestID : " + responseMap.get("traceId") + " : 발송 지연 처리 완료");
						
						paNaverV3DeliveryService.updatePaOrdermResultTx(paramMap, procMap);	
					}else { 

						log.info(responseMap.get("traceId") + " : 발송 지연 처리 실패 ");
						paramMap.put("code", "400");
						if(responseData != null && responseData.getFailProductOrderInfos() != null && !responseData.getFailProductOrderInfos().isEmpty()) {
							paramMap.put("message", "(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString()+ ")" + "requestID : " + response.getTraceId() + " (message:" + responseData.getFailProductOrderInfos().get(0).getMessage() + ") (code:" + responseData.getFailProductOrderInfos().get(0).getCode() +") : 발송 지연 처리 실패");
						}else {
							paramMap.put("message", "(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString()+ ")" + " " + response.getTraceId() + ": 발송 지연 처리 실패");
						}
						sb.append(paramMap.getString("message")+", ");
					}
					
				} catch (TransApiException ex) {
					paramMap.put("code", "500");
					paramMap.put("message", ex.getMessage());
					
					log.error(ex.getMessage(), ex);
					sb.append("(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString() + ")" +procMap.get("MAPPING_SEQ").toString() +"(message: "+ex.getMessage().toString()+ ") : 발송 지연 처리 실패, ");
				} catch (Exception e) {
					paramMap.put("code", "500");
					paramMap.put("message", e.getMessage());
					log.error(e.getMessage(), e);
					sb.append("(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString() + ")" +procMap.get("MAPPING_SEQ").toString() +"(message: "+e.getMessage().toString() + ") : 발송 지연 처리 실패, ");
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
					paramMap.put("message", "발송 대상 없음");
				}else {
					paramMap.put("message", paramMap.get("message") + sb.toString());
				}
				paNaverV3ConnectUtil.dealSuccess(paramMap, request);
			} catch(Exception e) {
				log.error("ApiTracking Insertion Error : {}", e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode")); 
		}
		
		log.info("======= 네이버 주문 발송 지연 처리 API End =======");
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
}	

