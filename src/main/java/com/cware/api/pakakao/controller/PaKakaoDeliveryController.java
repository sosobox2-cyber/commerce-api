package com.cware.api.pakakao.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaKakaoOrderListVO;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pakakao.claim.service.PaKakaoClaimService;
import com.cware.netshopping.pakakao.delivery.service.PaKakaoDeliveryService;
import com.cware.netshopping.pakakao.util.PaKakaoComUtill;
import com.cware.netshopping.pakakao.util.PaKakaoConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value="/pakakao/delivery", description="주문")
@Controller("com.cware.api.pakakao.PaKakaoDeliveryController")
@RequestMapping(value = "/pakakao/delivery")
public class PaKakaoDeliveryController extends AbstractController  {
	
	@Autowired
	private PaKakaoConnectUtil paKakaoConnectUtil;
	
	@Autowired
	private PaKakaoDeliveryService paKakaoDeliveryService;
	
	@Autowired
	private PaKakaoAsyncController paKakaoAsyncController;
	
	@Autowired
	private PaKakaoClaimController paKakaoClaimController;
	
	@Autowired
	private PaKakaoClaimService paKakaoClaimService;
	
	@Autowired
	private PaOrderService paOrderService;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	/**
	 * 카카오 변경 주문내역 조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiIgnore
	@ApiOperation(value = "변경 주문내역 조회", notes = "변경 주문내역 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/order-list", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public  ResponseEntity<ResponseMsg> modifyOrderList(HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "기간시작일[yyyyMMddHHmmss]", defaultValue = "") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate",   value = "기간종료일[yyyyMMddHHmmss]", defaultValue = "") @RequestParam(value = "toDate",   required = false) String toDate
			) throws Exception {
		ParamMap 			   	  apiInfoMap  = new ParamMap();
		String 				   	  paCode	  = "";
		String 					  prg_id 	  = "IF_PAKAKAOAPI_03_001";
		Map<String, Object>       map		  = new HashMap<String, Object>();
		String					  rtnMsg	  = "";
		ParamMap 				  errorMap	  = null;
		List<Map<String, Object>> orderList	  = null;
		
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
			
			long startTime = System.currentTimeMillis();
			log.info("카카오 변경 주문내역 조회 start :" + simpleDateFormat.format(startTime));
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			fromDate = ComUtil.NVL(fromDate).length() == 14 ? fromDate : DateUtil.addHour(DateUtil.getCurrentDateTimeAsString(), -2, DateUtil.DEFAULT_JAVA_DATE_FORMAT); // 조회시작일
			toDate   = ComUtil.NVL(toDate).length()   == 14 ? toDate   : DateUtil.getCurrentDateTimeAsString(); // 조회종료일
			
			String url = apiInfoMap.get("url").toString();
			
			for(int i = 0; i < Constants.PA_KAKAO_CONTRACT_CNT; i++) {
				
				paCode = (i == 0) ? Constants.PA_KAKAO_BROAD_CODE : Constants.PA_KAKAO_ONLINE_CODE;
				
				apiInfoMap.put("paCode", paCode);
				int page = 1;
				boolean	lastYn = false;
				
				while(!lastYn) {
					
					apiInfoMap.put("url", url.replace("{fromDate}", fromDate).replace("{toDate}", toDate).replace("{page}", Integer.toString(page)));
					
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
					
					if(!"200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
						
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						apiInfoMap.put("code", "500");
						rtnMsg += errorMap.get("errorMsg").toString() + " ";
						
						break;
					}
					
					orderList = (List<Map<String, Object>>)(map.get("content"));
					
					for(int j=0; j < orderList.size(); j++) {
						try {
							String orderStatus = orderList.get(j).get("orderStatus").toString();
							
							//if(!"1704970398".equals(orderList.get(j).get("orderId").toString())) {
							//	continue;
							//}
							
							 if("ShippingWaiting".equals(orderStatus) || "ShippingCancelRequestBuyer".equals(orderStatus)
								|| "PayCancelComplete".equals(orderStatus) || "ShippingCancelComplete".equals(orderStatus) || "ShippingRefundComplete".equals(orderStatus)
								|| "ReturnRequest".equals(orderStatus)   || "ReturnCancelComplete".equals(orderStatus) 		|| "ShippingProgress".equals(orderStatus)  || "ExchangeRequest".equals(orderStatus)
								|| "ShippingComplete".equals(orderStatus) || "ExchangeShippingComplete".equals(orderStatus) 
								|| "BuyDecision".equals(orderStatus) ){
								//배송 준비 중, 구매자 배송 취소 요청
								//결제 취소 완료, 환불 완료 (직권취소)
								//반품 요청, 반품 완료, 반품 철회, 교환 요청, 교환 철회
								//배송완료
								//구매결정
								orderInfo(request, paCode, orderList.get(j).get("orderId").toString(), orderStatus);
							} else {
								continue;
							}
							
						}catch (Exception e) {
							apiInfoMap.put("code", "500");
							rtnMsg += "orderId :" + orderList.get(j).get("orderId").toString() +" "+PaKakaoComUtill.getErrorMessage(e)+"/";
						}					
					}
					
					lastYn = "true".equals(map.get("last").toString())?true:false;
					page++;
				}		
			}
			
			long endTime = System.currentTimeMillis();
			log.info("카카오 변경 주문내역 조회 end :" + simpleDateFormat.format(endTime));

			double diffTime = (endTime - startTime)/1000.0;
			
			log.info("카카오 변경 주문내역 조회 실행시간 : " + diffTime + "(sec)");
			
			//주문
			orderInputMain(request);
			//취소
			paKakaoClaimController.cancelConfirmProc(request); // 취소승인 or 거절
			paKakaoClaimController.cancelInputMain(request);
			//반품
			//반품 상태에서 반품철회 후 바로 반품 시 반품 데이터 먼저 생성하면 반품 대상 수 부족으로 오류 후 2번째 요청 때 성공하기 때문에 "30", "31" 순서변경
			paKakaoClaimController.orderClaimMain(request, "31"); 
			paKakaoClaimController.orderClaimMain(request, "30");
			paKakaoClaimController.orderClaimMain(request, "40");
			paKakaoClaimController.orderClaimMain(request, "41");
			
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 카카오 변경 주문내역 조회 ( 주문확인 건 )
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "변경 주문확인 처리", notes = "변경 주문확인 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/order-list-confirm", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings({ "unchecked", "null" })
	public  ResponseEntity<ResponseMsg> modifyOrderListConfirm(HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "기간시작일", defaultValue = "") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate",   value = "기간종료일", defaultValue = "") @RequestParam(value = "toDate",   required = false) String toDate
			) throws Exception {
		ParamMap 			   	  apiInfoMap  = new ParamMap();
		String 				   	  paCode	  = "";
		String 					  prg_id 	  = "IF_PAKAKAOAPI_03_006";
		Map<String, Object>       map		  = new HashMap<String, Object>();
		String					  rtnMsg	  = "";
		ParamMap 				  errorMap	  = null;
		List<Map<String, Object>> orderList	  = null;
		
		try {
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			fromDate = ComUtil.NVL(fromDate).length() == 14 ? fromDate : DateUtil.addHour(DateUtil.getCurrentDateTimeAsString(), -23, DateUtil.DEFAULT_JAVA_DATE_FORMAT); // 조회시작일
			toDate   = ComUtil.NVL(toDate).length()   == 14 ? toDate   : DateUtil.getCurrentDateTimeAsString(); // 조회종료일
			
			String newUrl = apiInfoMap.get("url").toString();
			
			for(int i = 0; i < Constants.PA_KAKAO_CONTRACT_CNT; i++) {
				
				paCode = (i == 0) ? Constants.PA_KAKAO_BROAD_CODE : Constants.PA_KAKAO_ONLINE_CODE;
				
				apiInfoMap.put("paCode", paCode);
				String lastOrderId = "";
				String lastModifiedAt = "";

				boolean	lastYn = false;
				
				while(!lastYn) {
					String url = newUrl;
					
					if (!lastOrderId.isEmpty() && !lastModifiedAt.isEmpty()) {
						url += "&lastOrderId=" + lastOrderId + "&lastModifiedAt=" + lastModifiedAt;
				    }
					
					apiInfoMap.put("url", url.replace("{fromDate}", fromDate).replace("{toDate}", toDate).replace("{status}", "ShippingRequest"));
					
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
					
					if(!"200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
						
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						apiInfoMap.put("code", "500");
						rtnMsg += errorMap.get("errorMsg").toString() + " ";
						
						break;
					}
					
					orderList = (List<Map<String, Object>>)(map.get("contents"));
					
					if (orderList == null) break;
						
					for(int j=0; j < orderList.size(); j++) {
						try {
							orderConfirm(request, paCode, orderList.get(j).get("orderId").toString());
						}catch (Exception e) {
							apiInfoMap.put("code", "500");
							rtnMsg += "orderId :" + orderList.get(j).get("orderId").toString() +" "+PaKakaoComUtill.getErrorMessage(e)+"/";
						}					
					}
					
					if (orderList != null && !orderList.isEmpty()) {
				        Map<String, Object> lastItem = orderList.get(orderList.size() - 1);
				        lastOrderId = ComUtil.objToStr(lastItem.get("orderId"));
				        lastModifiedAt = ComUtil.objToStr(lastItem.get("modifiedAt"));
				    }
				
					lastYn = orderList.isEmpty() ? true : false;
					//lastYn = "true".equals(map.get("last").toString())?true:false;
				}		
			}
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "배송상품주문확인처리", notes = "배송상품주문확인처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"), 
	@ApiResponse(code = 500, message = "시스템 오류") })
	@RequestMapping(value = "/order-confirm", method = RequestMethod.GET)
	@ResponseBody
	private void orderConfirm(HttpServletRequest request,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = true) String paCode,
			@ApiParam(name = "orderId",   value = "카카오주문번호", defaultValue = "") @RequestParam(value = "orderId",required = true) String orderId
			) throws Exception {
		
		Map<String, Object> resultMap   = new HashMap<String, Object>();
		ParamMap 			apiDataMap  = new ParamMap();
		ParamMap 			apiInfoMap  = new ParamMap();
		ParamMap 			errorMap	= null;
		String 	 			prg_id 	    = "IF_PAKAKAOAPI_03_002";
		String 				rtnMsg 		= "";

		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			List<String> orderIdsList = new ArrayList<String>();
			orderIdsList.add(orderId);

			apiDataMap.put("orderIds", orderIdsList);
			apiInfoMap.put("paCode", paCode);
			
			resultMap = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
			
			if(!"200".equals(ComUtil.objToStr(resultMap.get("statusCode"))) ) {
				
				errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(resultMap);

				apiInfoMap.put("code", "500");
				rtnMsg += "orderId : " + orderId + " | "+ errorMap.get("errorMsg").toString() + " ";
				
			}
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
	}
	
	@ApiOperation(value = "주문정보 조회", notes = "주문정보 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 500, message = "시스템 오류") })
	@RequestMapping(value = "/order-info", method = RequestMethod.GET)
	@ResponseBody
	private void orderInfo(HttpServletRequest request,
			@ApiParam(name = "paCode",		value = "제휴사코드",	  defaultValue = "") @RequestParam(value = "paCode",	   required = true) String paCode,
			@ApiParam(name = "orderId",		value = "카카오주문번호", defaultValue = "") @RequestParam(value = "orderId",required = true) String orderId,
			@ApiParam(name = "orderStatus",	value = "카카오주문번호", defaultValue = "") @RequestParam(value = "orderStatus",required = true) String orderStatus
			) throws Exception {
		
		Map<String, Object> map  	  = new HashMap<String, Object>();
		ParamMap apiInfoMap  = new ParamMap();
		ParamMap apiDataMap  = new ParamMap();
		ParamMap errorMap	 = null;
		String 	 prg_id 	 = "IF_PAKAKAOAPI_03_003";
		String   rtnMsg		 = "";
		
		try {
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();	
			apiInfoMap.put("paCode", paCode);
			
			apiInfoMap.put("url", url.replace("{order_id}", orderId));
			
			map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
			
			if(!"200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
				
				errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
				apiInfoMap.put("code", "500");
				rtnMsg += "orderId : " + orderId + " | "+ errorMap.get("errorMsg").toString() + " ";
				
			}else {
				PaKakaoOrderListVO vo = new PaKakaoOrderListVO();
				vo = orderListMapping(map,paCode, orderStatus);
				int	exits = 0;

				if(orderStatus.equals("ShippingWaiting") || orderStatus.equals("ShippingCancelRequestBuyer")) { //주문생성, 취소철회, 취소요청
					//원주문 있는지 체크
					exits = paKakaoDeliveryService.countOrderList(vo.getId());
					if(exits <= 0) {
						paKakaoDeliveryService.saveKakaoOrderListTx(vo);
					}
					
					if(map.containsKey("orderClaimCancel")) {
						vo.setPaOrderGb("20");
						vo.setProcFlag("00");
						vo.setOutBefClaimGb("0");
						
						if(orderStatus.equals("ShippingCancelRequestBuyer")) {
							//취소요청데이터 setting
							vo.setWithdrawYn("0");
							vo.setClaimStatus("20");
							
							paKakaoClaimService.saveKakaoCancelListTx(vo);
							
						} else {
							//철회데이터 setting
							vo.setWithdrawYn("1");
							vo.setWithdrawDate(vo.getInsertDate());
							
							paKakaoClaimService.saveKakaoWithdrawCancelListTx(vo);	
						}
					}
				}else if(orderStatus.equals("PayCancelComplete") || orderStatus.equals("ShippingCancelComplete") || orderStatus.equals("ShippingRefundComplete")){ //직권취소
					vo.setPaOrderGb("20");
					vo.setProcFlag("10");
					//vo.setOutBefClaimGb("1"); -> doFlag 확인 후 setting
					vo.setWithdrawYn("0");
					vo.setClaimStatus("21");
					
					paKakaoClaimService.saveKakaoCancelListTx(vo);
					
				}else if(orderStatus.equals("ShippingComplete")){//배송완료
					paKakaoDeliveryService.updatePaKakaoDeliveryCompleteTx(vo);
				}else if(orderStatus.equals("ExchangeShippingComplete")){//교환완료
					paKakaoClaimService.updateKakaoExchangeCompleteTx(vo);
				}else if(orderStatus.equals("BuyDecision")){//구매결정
					if(map.containsKey("orderClaimExchange")){
						vo.setPaOrderGb("40");
					}
					paKakaoDeliveryService.updateBuyDecisionTx(vo);
				}
				
				if(map.containsKey("orderClaimReturn")){
					if(orderStatus.equals("ShippingProgress") || orderStatus.equals("ShippingComplete") || orderStatus.equals("BuyDecision")){ //반품철회 , 배송완료 , 구매결정 
						vo.setPaClaimGb("31");
						vo.setPaOrderGb("31");
					}else if(orderStatus.equals("ReturnRequest") || orderStatus.equals("ReturnCancelComplete") || orderStatus.equals("BuyDecision")){ //반품요청, 반품완료, 구매결정
						vo.setPaClaimGb("30");
						vo.setPaOrderGb("30");
					}else {
						return;
					}
					
					paKakaoClaimService.saveKakaoClaimListTx(vo);
					
				} else if(map.containsKey("orderClaimExchange")){
					if(orderStatus.equals("ShippingProgress") || orderStatus.equals("ShippingComplete")){ //교환철회
						vo.setPaClaimGb("41");
						vo.setPaOrderGb("41");
					}else if(orderStatus.equals("ExchangeRequest")) { //교환요청
						vo.setPaClaimGb("40");
						vo.setPaOrderGb("40");
					}else {
						return;
					}
					
					paKakaoClaimService.saveKakaoClaimListTx(vo);
					
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
			
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
	}
		
	private void orderInputMain(HttpServletRequest request) throws Exception {
		String prg_id = "PAKAKAO_ORDER_INPUT";
		String duplicateCheck = "";
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			int limitCount = ConfigUtil.getInt("PA_ORDER_CREATE_CNT");
			
			List<Map<String, String>> orderInputTargetList = paKakaoDeliveryService.selectOrderInputTargetList(limitCount);
			
			for(Map<String, String> order : orderInputTargetList) {
				try {
					paKakaoAsyncController.orderInputAsync(order, request);
				} catch(Exception e) {
					log.error(prg_id + " - 주문 내역 생성 오류" + order.get("PA_ORDER_NO"), e);
				}
			}
		} catch(Exception e) {
			log.error("error msg : " + e.getMessage());
		} finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}
	
	@ApiOperation(value = "배송상품 발송처리", notes = "배송상품 발송처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), 
							@ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/slip-out-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> slipOutProc(HttpServletRequest request) throws Exception {

		int						  executedRtn						 = 0;
		String					  prg_id							 = "IF_PAKAKAOAPI_03_004";
		String					  rtnMsg							 = "";
		ParamMap				  apiInfoMap						 = new ParamMap();		
		ParamMap 				  errorMap							 = null;
		Map<String, Object>		  resultMap							 = new HashMap<String, Object>() ;
		Map<String,Object>		  deliveryInvoiceInfo				 = null;
		Map<String,Object>		  deliveryInvoiceRegisterRequest	 = null;
		List<Map<String, Object>> apiDataMap						 = null;
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id		, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			List<Map<String, Object>> orderShippingList =  paKakaoDeliveryService.selectSlipOutProcList();
			
			for(int i=0; i<orderShippingList.size(); i++) {
				try {
					
					String slipFlag = orderShippingList.get(i).get("SLIP_FLAG").toString();
					apiInfoMap.put("paCode",orderShippingList.get(i).get("PA_CODE").toString());
					apiInfoMap.put("url", url.replace("{method}", "SLIP_UPDATE".equals(slipFlag)?"shippingmethod":"invoices"));					
					
					apiDataMap = new ArrayList<Map<String,Object>>();
					deliveryInvoiceRegisterRequest = new HashMap<String,Object>();
					deliveryInvoiceRegisterRequest.put("orderId",orderShippingList.get(i).get("PA_ORDER_SEQ").toString());
					
					if("DIRECT".equals(orderShippingList.get(i).get("PA_DELY_GB").toString())) {
						deliveryInvoiceRegisterRequest.put("shippingMethod", "DIRECT"); 					
					} else {
						deliveryInvoiceRegisterRequest.put("shippingMethod", "SHIPPING");
						
						deliveryInvoiceInfo = new HashMap<String, Object>();
						deliveryInvoiceInfo.put("deliveryCompanyCode", orderShippingList.get(i).get("PA_DELY_GB").toString());
						deliveryInvoiceInfo.put("invoiceNumber", orderShippingList.get(i).get("SLIP_NO").toString());
						deliveryInvoiceRegisterRequest.put("deliveryInvoiceInfo", deliveryInvoiceInfo);
						
					}
					
					apiDataMap.add(deliveryInvoiceRegisterRequest);
					resultMap = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
					
					if(!"200".equals(ComUtil.objToStr(resultMap.get("statusCode")))) {
						
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(resultMap);
						
						if(errorMap.get("errorMsg").toString().indexOf("유효한 송장번호") == -1) {
							apiInfoMap.put("code", "500");
							rtnMsg += orderShippingList.get(i).get("PA_ORDER_SEQ").toString() + " " + errorMap.get("errorMsg").toString();
						} else {
							apiDataMap = new ArrayList<Map<String,Object>>();
							deliveryInvoiceInfo = new HashMap<String, Object>();
							
							deliveryInvoiceInfo.put("orderId", orderShippingList.get(i).get("PA_ORDER_SEQ").toString());
							deliveryInvoiceInfo.put("shippingMethod", "DIRECT");
							
							apiDataMap.add(deliveryInvoiceInfo);
							
							resultMap = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
							
							if(!"200".equals(ComUtil.objToStr(resultMap.get("statusCode")))) {
								
								errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(resultMap);
								apiInfoMap.put("code", "500");
								rtnMsg += orderShippingList.get(i).get("PA_ORDER_SEQ").toString() + " " + errorMap.get("errorMsg").toString();
								
							} else {
								if(!"SLIP_UPDATE".equals(slipFlag)) {
									executedRtn = updatePaOrderMDoFlag(orderShippingList.get(i), "40", "출고처리", "000000");
									if(executedRtn < 1){ //ERROR
										apiInfoMap.put("code", "500");
										rtnMsg += "ID : "+orderShippingList.get(i).get("PA_ORDER_SEQ").toString() + " TPAORDERM(pa_do_flag) UPDATE Fail/";
									}										
								}
							}
						}
					} else {// 정상적으로 송장 등록
						if(!"SLIP_UPDATE".equals(slipFlag)) {
							executedRtn = updatePaOrderMDoFlag(orderShippingList.get(i), "40", "출고처리", "000000");
							if(executedRtn < 1){ //ERROR
								apiInfoMap.put("code", "500");
								rtnMsg += "ID : " + orderShippingList.get(i).get("PA_ORDER_SEQ").toString() + " TPAORDERM(pa_do_flag) UPDATE Fail/";
							}									
						}
					}					
					
					if(!"200".equals(ComUtil.objToStr(resultMap.get("statusCode")))) {
						// 직접배송 일때의 GB 값을 알아서 세팅 해야함 
						orderShippingList.get(i).put("TRANS_PA_DELY_GB"	, "99");
						orderShippingList.get(i).put("TRANS_INVOICE_NO"	, "송장등록실패");
						orderShippingList.get(i).put("REMARK1_V"		, slipFlag + " | " + "운송장 연동 실패 후 재 연동");
						orderShippingList.get(i).put("INVOICE_NO"		, "송장등록실패");
						orderShippingList.get(i).put("TRANS_YN"			, "0");
					}else {
						orderShippingList.get(i).put("TRANS_PA_DELY_GB"	, orderShippingList.get(i).get("PA_DELY_GB").toString());
						orderShippingList.get(i).put("TRANS_INVOICE_NO"	, orderShippingList.get(i).get("SLIP_NO").toString());
						orderShippingList.get(i).put("REMARK1_V"		, slipFlag + " | " + "출고완료");
						orderShippingList.get(i).put("INVOICE_NO"		, orderShippingList.get(i).get("SLIP_NO").toString());
						orderShippingList.get(i).put("TRANS_YN"			, "1");
					}					
					orderShippingList.get(i).put("PA_GROUP_CODE", Constants.PA_KAKAO_GROUP_CODE);
					paOrderService.insertTpaSlipInfoTx(orderShippingList.get(i));
						
				}catch (Exception e) {
					apiInfoMap.put("code", "500");
					rtnMsg += "orderId :" + orderShippingList.get(i).get("PA_ORDER_SEQ").toString() +" "+PaKakaoComUtill.getErrorMessage(e)+"/";
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	private int updatePaOrderMDoFlag(Map<String, Object> map, String doFlag, String resultMessage, String resultCode)  throws Exception {
		int executedRtn = 0;
		
		map.put("PA_DO_FLAG", doFlag);
		map.put("API_RESULT_CODE", resultCode);
		map.put("API_RESULT_MESSAGE", resultMessage);
		
		executedRtn = paKakaoDeliveryService.updatePaOrderMDoFlag(map);
		
		return executedRtn;
	}
	
	@SuppressWarnings("unchecked")
	private PaKakaoOrderListVO orderListMapping(Map<String,Object> orderList, String paCode, String orderStatus) throws Exception{
		
		String dateTime = systemService.getSysdatetimeToString();
		
		PaKakaoOrderListVO vo = new PaKakaoOrderListVO();
		Map<String,Object> orderBase 			= (Map<String,Object>)(orderList.get("orderBase"));
		Map<String,Object> orderer 				= (Map<String,Object>)(orderList.get("orderer"));
		Map<String,Object> orderProduct 		= (Map<String,Object>)(orderList.get("orderProduct"));
		Map<String,Object> orderDeliveryRequest = (Map<String,Object>)(orderList.get("orderDeliveryRequest"));
		Map<String,Object> orderDelivery 		= (Map<String,Object>)(orderList.get("orderDelivery"));
		Map<String, Object> orderClaimReturn 	= (Map<String, Object>) orderList.get("orderClaimReturn");
		Map<String, Object> orderClaimExchange  = (Map<String, Object>) orderList.get("orderClaimExchange");
		
		vo.setPaCode(paCode);
		
		vo.setId(ComUtil.objToStr(orderList.get("id")));
		
		vo.setOrderBaseId(ComUtil.objToStr(orderBase.get("id")));
		vo.setPaymentId(ComUtil.objToStr(orderBase.get("paymentId")));
		vo.setGroupDiscountRoomId(ComUtil.objToStr(orderBase.get("groupDiscountRoomId")));
		vo.setStatus(ComUtil.objToStr(orderBase.get("status")));
		vo.setPaidAt(ComUtil.objToStr(orderBase.get("paidAt")));
		vo.setDecidedAt(ComUtil.objToStr(orderBase.get("decidedAt")));
		vo.setRefundedAt(ComUtil.objToStr(orderBase.get("refundedAt")));
		vo.setCanceledAt(ComUtil.objToStr(orderBase.get("canceledAt")));
		vo.setCreatedAt(ComUtil.objToStr(orderBase.get("createdAt")));
		vo.setModifiedAt(ComUtil.objToStr(orderBase.get("modifiedAt")));
		
		if(orderer.containsKey("phoneNumber")) {
			vo.setPhoneNumber(ComUtil.objToStr(orderer.get("phoneNumber")).replaceFirst("\\+82 ", "0"));
		}
		
		vo.setOrderProductId(ComUtil.objToStr(orderProduct.get("id")));
		vo.setName(ComUtil.objToStr(orderProduct.get("name")));
		vo.setSellerItemNo(ComUtil.objToStr(orderProduct.get("sellerItemNo")));
		vo.setSellerItemOptionCode(ComUtil.objToStr(orderProduct.get("sellerItemOptionCode")));
		vo.setOptionContent(ComUtil.objToStr(orderProduct.get("optionContent")));
		vo.setQuantity(ComUtil.objToLong(orderProduct.get("quantity")));
		vo.setProductPrice(ComUtil.objToDouble(orderProduct.get("productPrice")));
		vo.setOptionPrice(ComUtil.objToDouble(orderProduct.get("optionPrice")));
		vo.setSellerDiscountPrice(ComUtil.objToDouble(orderProduct.get("sellerDiscountPrice")));
		vo.setSellerCouponDiscountPrice(ComUtil.objToDouble(orderProduct.get("sellerCouponDiscountPrice")));
		vo.setSettlementBasicPrice(ComUtil.objToDouble(orderProduct.get("settlementBasicPrice")));
		vo.setBaseFee(ComUtil.objToDouble(orderProduct.get("baseFee")));
		vo.setDisplayFee(ComUtil.objToDouble(orderProduct.get("displayFee")));
		vo.setRefererCode(ComUtil.objToStr(orderProduct.get("refererCode")));
		vo.setBrandName(ComUtil.objToStr(orderProduct.get("brandName")));
		vo.setModelName(ComUtil.objToStr(orderProduct.get("modelName")));
		vo.setDeliveryAmountOriginId(ComUtil.objToStr(orderProduct.get("deliveryAmountOriginId")));
		vo.setDeliveryAmountPayPointTime(ComUtil.objToStr(orderProduct.get("deliveryAmountPayPointTime")));
		vo.setDeliveryAmountType(ComUtil.objToStr(orderProduct.get("deliveryAmountType")));
		vo.setDeliveryAmount(ComUtil.objToDouble(orderProduct.get("deliveryAmount")));
		vo.setAreaAdditionalDeliveryAmount(ComUtil.objToDouble(orderProduct.get("areaAdditionalDeliveryAmount")));
		
		vo.setConfirmedAt(ComUtil.objToStr(orderDelivery.get("confirmedAt")));
		vo.setDeliveredAt(ComUtil.objToStr(orderDelivery.get("deliveredAt")));
		vo.setDeliveryCompanyCode(ComUtil.objToStr(orderDelivery.get("deliveryCompanyCode")));
		vo.setDeliveryRequestAt(ComUtil.objToStr(orderDelivery.get("deliveryRequestAt")));
		vo.setInvoiceNumber(ComUtil.objToStr(orderDelivery.get("invoiceNumber")));
		vo.setInvoiceRegisteredAt(ComUtil.objToStr(orderDelivery.get("invoiceRegisteredAt")));
		vo.setShippingMethod(ComUtil.objToStr(orderDelivery.get("shippingMethod")));
		
		vo.setReceiverAddress(ComUtil.objToStr(orderDeliveryRequest.get("receiverAddress")));
		vo.setReceiverMobileNumber(ComUtil.objToStr(orderDeliveryRequest.get("receiverMobileNumber")));
		vo.setReceiverName(ComUtil.objToStr(orderDeliveryRequest.get("receiverName")));
		vo.setReceiverPhoneNumber(ComUtil.objToStr(orderDeliveryRequest.get("receiverPhoneNumber")));
		vo.setRequirement(ComUtil.objToStr(orderDeliveryRequest.get("requirement")));
		vo.setRoadZipCode(ComUtil.objToStr(orderDeliveryRequest.get("roadZipCode")));
		vo.setZipcode(ComUtil.objToStr(orderDeliveryRequest.get("zipcode")));
		
		if(orderList.containsKey("orderClaimCancel")) {
			Map<String, Object> orderClaimCancel = (Map<String, Object>)(orderList.get("orderClaimCancel"));
			vo.setClaimId(ComUtil.objToStr(orderClaimCancel.get("claimId")));
			vo.setClaimItemId(ComUtil.objToStr(orderClaimCancel.get("claimItemId")));
			vo.setReferType(ComUtil.objToStr(orderClaimCancel.get("referType")));
			vo.setOrderClaimCancelCreatedAt(ComUtil.objToStr(orderClaimCancel.get("createdAt")));
			vo.setOrderClaimCancelModifiedAt(ComUtil.objToStr(orderClaimCancel.get("modifiedAt")));
			vo.setReasonCodeName(ComUtil.objToStr(orderClaimCancel.get("reasonCodeName")).trim());
			vo.setReasonComment(ComUtil.objToStr(orderClaimCancel.get("reasonComment")));
		}else {
			if(orderStatus.equals("ShippingCancelComplete")) {
				vo.setClaimId("DeliveryDelay");
				vo.setClaimItemId(vo.getId());
				vo.setReferType("ETC");
				vo.setOrderClaimCancelCreatedAt(vo.getCanceledAt());
				vo.setOrderClaimCancelModifiedAt(vo.getCanceledAt());
				vo.setReasonCodeName("장기 미배송 자동 취소");
				vo.setReasonComment("배송지연으로 인한 자동 취소");
			}
		}
		
		if(orderList.containsKey("orderClaimReturn")) {
			vo.setClaimId(ComUtil.objToStr(orderClaimReturn.get("claimId")));
			vo.setClaimItemId(ComUtil.objToStr(orderClaimReturn.get("claimItemId")));
			vo.setReferType(ComUtil.objToStr(orderClaimReturn.get("referType")));
			vo.setReasonCodeName(ComUtil.objToStr(orderClaimReturn.get("reasonCodeName")).trim());
			vo.setReasonComment(ComUtil.objToStr(orderClaimReturn.get("reasonComment")));
			vo.setOrderClaimReturnCreatedAt(ComUtil.objToStr(orderClaimReturn.get("createdAt")));
			vo.setOrderClaimReturnModifiedAt(ComUtil.objToStr(orderClaimReturn.get("modifiedAt")));
			vo.setCompletedAt(ComUtil.objToStr(orderClaimReturn.get("completedAt")));
			vo.setReturnMethod(ComUtil.objToStr(orderClaimReturn.get("returnMethod")));
			vo.setReturnDeliveryCompanyId(ComUtil.objToStr(orderClaimReturn.get("returnDeliveryCompanyId")));
			vo.setReturnInvoiceNumber(ComUtil.objToStr(orderClaimReturn.get("returnInvoiceNumber")));
			vo.setReturnShippingFee(ComUtil.objToDouble(orderClaimReturn.get("returnShippingFee")));
			vo.setPendingModifiedAt(ComUtil.objToStr(orderClaimReturn.get("pendingModifiedAt")));
			vo.setPendingCodeName(ComUtil.objToStr(orderClaimReturn.get("pendingCodeName")));
			vo.setPendingComment(ComUtil.objToStr(orderClaimReturn.get("pendingComment")));
			vo.setPickupName(ComUtil.objToStr(orderClaimReturn.get("pickupName")));
			vo.setPickupZipCode(ComUtil.objToStr(orderClaimReturn.get("pickupZipCode")));
			vo.setPickupRoadZipCode(ComUtil.objToStr(orderClaimReturn.get("pickupRoadZipCode")));
			vo.setPickupAddress1(ComUtil.objToStr(orderClaimReturn.get("pickupAddress1")));
			vo.setPickupAddress2(ComUtil.objToStr(orderClaimReturn.get("pickupAddress2")));
			vo.setPickupMobileNumber(ComUtil.objToStr(orderClaimReturn.get("pickupMobileNumber")));
			vo.setPickupPhoneNumber(ComUtil.objToStr(orderClaimReturn.get("pickupPhoneNumber")));
			vo.setClaimShippingFeeMethod(ComUtil.objToStr(orderClaimReturn.get("returnShippingFeeMethod")));
			vo.setClaimShippingFeeMethodLabel(ComUtil.objToStr(orderClaimReturn.get("returnShippingFeeMethodLabel")));
		}
		
		if(orderList.containsKey("orderClaimExchange")){
			vo.setClaimId(ComUtil.objToStr(orderClaimExchange.get("claimId")));
			vo.setClaimItemId(ComUtil.objToStr(orderClaimExchange.get("claimItemId")));
			vo.setReferType(ComUtil.objToStr(orderClaimExchange.get("referType")));
			vo.setReasonCodeName(ComUtil.objToStr(orderClaimExchange.get("reasonCodeName")).trim());
			vo.setReasonComment(ComUtil.objToStr(orderClaimExchange.get("reasonComment")));
			vo.setOrderClaimExchCreatedAt(ComUtil.objToStr(orderClaimExchange.get("createdAt")));
			vo.setOrderClaimExchModifiedAt(ComUtil.objToStr(orderClaimExchange.get("modifiedAt")));
			vo.setCompletedAt(ComUtil.objToStr(orderClaimExchange.get("completedAt")));
			vo.setReturnMethod(ComUtil.objToStr(orderClaimExchange.get("returnMethod")));
			vo.setReturnDeliveryCompanyId(ComUtil.objToStr(orderClaimExchange.get("returnDeliveryCompanyId")));
			vo.setReturnInvoiceNumber(ComUtil.objToStr(orderClaimExchange.get("returnInvoiceNumber")));
			vo.setExchangeName(ComUtil.objToStr(orderClaimExchange.get("exchangeName")));
			vo.setExchangeZipCode(ComUtil.objToStr(orderClaimExchange.get("exchangeZipCode")));
			vo.setExchangeRoadZipCode(ComUtil.objToStr(orderClaimExchange.get("exchangeRoadZipCode")));
			vo.setExchangeAddress1(ComUtil.objToStr(orderClaimExchange.get("exchangeAddress1")));
			vo.setExchangeAddress2(ComUtil.objToStr(orderClaimExchange.get("exchangeAddress2")));
			vo.setExchangeMobileNumber(ComUtil.objToStr(orderClaimExchange.get("exchangeMobileNumber")));
			vo.setExchangePhoneNumber(ComUtil.objToStr(orderClaimExchange.get("exchangePhoneNumber")));
			vo.setExchangeMethod(ComUtil.objToStr(orderClaimExchange.get("exchangeMethod")));
			vo.setExchangeDeliveryCompanyId(ComUtil.objToStr(orderClaimExchange.get("exchangeDeliveryCompanyId")));
			vo.setExchangeInvoiceNumber(ComUtil.objToStr(orderClaimExchange.get("exchangeInvoiceNumber")));
			vo.setExchangeShippingFee(ComUtil.objToDouble(orderClaimExchange.get("exchangeShippingFee")));
			vo.setPendingModifiedAt(ComUtil.objToStr(orderClaimExchange.get("pendingModifiedAt")));
			vo.setPendingCodeName(ComUtil.objToStr(orderClaimExchange.get("pendingCodeName")));
			vo.setPendingComment(ComUtil.objToStr(orderClaimExchange.get("pendingComment")));
			vo.setPickupName(ComUtil.objToStr(orderClaimExchange.get("pickupName")));
			vo.setPickupZipCode(ComUtil.objToStr(orderClaimExchange.get("pickupZipCode")));
			vo.setPickupRoadZipCode(ComUtil.objToStr(orderClaimExchange.get("pickupRoadZipCode")));
			vo.setPickupAddress1(ComUtil.objToStr(orderClaimExchange.get("pickupAddress1")));
			vo.setPickupAddress2(ComUtil.objToStr(orderClaimExchange.get("pickupAddress2")));
			vo.setPickupMobileNumber(ComUtil.objToStr(orderClaimExchange.get("pickupMobileNumber")));
			vo.setPickupPhoneNumber(ComUtil.objToStr(orderClaimExchange.get("pickupPhoneNumber")));
			vo.setClaimShippingFeeMethod(ComUtil.objToStr(orderClaimExchange.get("exchangeShippingFeeMethod")));
			vo.setClaimShippingFeeMethodLabel(ComUtil.objToStr(orderClaimExchange.get("exchangeShippingFeeMethodLabel")));
		}
		
		vo.setInsertId(Constants.PA_KAKAO_PROC_ID);
		vo.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		vo.setModifyId(Constants.PA_KAKAO_PROC_ID);
		vo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		
		vo.setPaOrderGb("10");
		
		return vo;
	}
	
	/**
	 * 배송상품 발송지연 처리 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "배송상품 발송지연 처리 ", notes = "배송상품 발송지연 처리 ", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"), 
	@ApiResponse(code = 500, message = "시스템 오류") })
	@RequestMapping(value = "/shipping-delay", method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<?> shippingDelayProc(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap  = new ParamMap();
		ParamMap apiDataMap	 = null;
		ParamMap errorMap	 = null;
		String 	 prg_id 	 = "IF_PAKAKAOAPI_03_005";
		String   rtnMsg		 = "";
		int		 executedRtn = 0;
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			//패널티 방지를 위해 주문 접수 후 90시간 이후까지 출고되지 않을 경우 일반상품의 경우 15일, 설치배송/주문제작 상품의 경우 30일로 배송지연처리
			List<Map<String, Object>> delayOrderList = paKakaoDeliveryService.selectDelayOrderList();
			
			for(int i = 0; i < delayOrderList.size(); i++) {
				
				try {
					apiInfoMap.put("paCode", delayOrderList.get(i).get("PA_CODE").toString());
					apiDataMap = new ParamMap();
					
					apiDataMap.put("delayCausation", "배송지연"); 
					apiDataMap.put("delayCausationCode", delayOrderList.get(i).get("DELAY_CODE").toString());
					apiDataMap.put("deliveryExpectedAt",  delayOrderList.get(i).get("EXPECTED_DATE").toString());
					
					List<String> orderIdsList = new ArrayList<String>();
					orderIdsList.add(delayOrderList.get(i).get("PA_ORDER_SEQ").toString());
					apiDataMap.put("orderIds", orderIdsList);				
					
					resultMap = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
					
					if(!"200".equals(ComUtil.objToStr(resultMap.get("statusCode")))) {
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(resultMap);
						
						apiInfoMap.put("code", "500");
						rtnMsg += delayOrderList.get(i).get("PA_ORDER_SEQ").toString() + " : " + errorMap.get("errorMsg").toString();
						continue;
					} else {
						
						executedRtn = paKakaoDeliveryService.updatePaKakaoDeliveryExpectedAt(delayOrderList.get(i));
						if(executedRtn < 1) {
							apiInfoMap.put("code", "500");
							rtnMsg += delayOrderList.get(i).get("PA_ORDER_SEQ").toString() + " : TPAORDERM UPDATE Fail/";
							continue;
						}
					}
				} catch (Exception e) {
					apiInfoMap.put("code", "500");
					rtnMsg += delayOrderList.get(i).get("PA_ORDER_SEQ").toString() + PaKakaoComUtill.getErrorMessage(e)+"/";
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 카카오 변경 주문내역 조회 V2
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "변경 주문내역 조회 V2", notes = "변경 주문내역 조회 V2", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/order-list-v2", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public  ResponseEntity<ResponseMsg> modifyOrderListV2(HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "기간시작일[yyyyMMddHHmmss]", defaultValue = "") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate",   value = "기간종료일[yyyyMMddHHmmss]", defaultValue = "") @RequestParam(value = "toDate",   required = false) String toDate
			) throws Exception {
		ParamMap 			   	  apiInfoMap  = new ParamMap();
		String 				   	  paCode	  = "";
		String 					  prg_id 	  = "IF_PAKAKAOAPI_03_007";
		Map<String, Object>       map		  = new HashMap<String, Object>();
		String					  rtnMsg	  = "";
		ParamMap 				  errorMap	  = null;
		List<Map<String, Object>> orderList	  = null;
		
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
			
			long startTime = System.currentTimeMillis();
			log.info("카카오 변경 주문내역 조회 start :" + simpleDateFormat.format(startTime));
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			fromDate = ComUtil.NVL(fromDate).length() == 14 ? fromDate : DateUtil.addHour(DateUtil.toDateString(), -2, DateUtil.KAKAO_DATETIME_FORMAT); // 조회시작일
			toDate   = ComUtil.NVL(toDate).length()   == 14 ? toDate   : DateUtil.toDateString(); // 조회종료일
			
			String newUrl = apiInfoMap.get("url").toString();
			
			for(int i = 0; i < Constants.PA_KAKAO_CONTRACT_CNT; i++) {
				
				paCode = (i == 0) ? Constants.PA_KAKAO_BROAD_CODE : Constants.PA_KAKAO_ONLINE_CODE;
				
				apiInfoMap.put("paCode", paCode);
				String lastOrderId = "";
				String lastModifiedAt = "";
				
				boolean	lastYn = false;
				
				while(!lastYn) {
					String url = newUrl;
					
					if (!lastOrderId.isEmpty() && !lastModifiedAt.isEmpty()) {
						url += "&lastOrderId=" + lastOrderId + "&lastModifiedAt=" + lastModifiedAt;
				    }
					
					apiInfoMap.put("url", url.replace("{fromDate}", fromDate).replace("{toDate}", toDate));
					
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
					
					if(!"200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
						
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						apiInfoMap.put("code", "500");
						rtnMsg += errorMap.get("errorMsg").toString() + " ";
						
						break;
					}
					
					orderList = (List<Map<String, Object>>)(map.get("contents"));
					
					for(int j=0; j < orderList.size(); j++) {
						try {
							String orderStatus = orderList.get(j).get("orderStatus").toString();
							
							 if("ShippingWaiting".equals(orderStatus) || "ShippingCancelRequestBuyer".equals(orderStatus)
								|| "PayCancelComplete".equals(orderStatus) || "ShippingCancelComplete".equals(orderStatus) || "ShippingRefundComplete".equals(orderStatus)
								|| "ReturnRequest".equals(orderStatus)   || "ReturnCancelComplete".equals(orderStatus) 		|| "ShippingProgress".equals(orderStatus)  || "ExchangeRequest".equals(orderStatus)
								|| "ShippingComplete".equals(orderStatus) || "ExchangeShippingComplete".equals(orderStatus) 
								|| "BuyDecision".equals(orderStatus) ){
								//배송 준비 중, 구매자 배송 취소 요청
								//결제 취소 완료, 환불 완료 (직권취소)
								//반품 요청, 반품 완료, 반품 철회, 교환 요청, 교환 철회
								//배송완료
								//구매결정
								orderInfo(request, paCode, orderList.get(j).get("orderId").toString(), orderStatus);
							} else {
								continue;
							}
							
						}catch (Exception e) {
							apiInfoMap.put("code", "500");
							rtnMsg += "orderId :" + orderList.get(j).get("orderId").toString() +" "+PaKakaoComUtill.getErrorMessage(e)+"/";
						}					
					}
					
					if (orderList != null && !orderList.isEmpty()) {
				        Map<String, Object> lastItem = orderList.get(orderList.size() - 1);
				        lastOrderId = ComUtil.objToStr(lastItem.get("orderId"));
				        lastModifiedAt = ComUtil.objToStr(lastItem.get("modifiedAt"));
				    }
					lastYn = orderList.isEmpty() ? true : false;
				}		
			}
			
			long endTime = System.currentTimeMillis();
			log.info("카카오 변경 주문내역 조회 end :" + simpleDateFormat.format(endTime));

			double diffTime = (endTime - startTime)/1000.0;
			
			log.info("카카오 변경 주문내역 조회 실행시간 : " + diffTime + "(sec)");
			
			//주문
			orderInputMain(request);
			//취소
			paKakaoClaimController.cancelConfirmProc(request); // 취소승인 or 거절
			paKakaoClaimController.cancelInputMain(request);
			//반품
			//반품 상태에서 반품철회 후 바로 반품 시 반품 데이터 먼저 생성하면 반품 대상 수 부족으로 오류 후 2번째 요청 때 성공하기 때문에 "30", "31" 순서변경
			paKakaoClaimController.orderClaimMain(request, "31"); 
			paKakaoClaimController.orderClaimMain(request, "30");
			paKakaoClaimController.orderClaimMain(request, "40");
			paKakaoClaimController.orderClaimMain(request, "41");
			
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}	
}