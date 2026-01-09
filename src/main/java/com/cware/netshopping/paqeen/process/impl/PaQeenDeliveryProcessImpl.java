package com.cware.netshopping.paqeen.process.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.paqeen.controller.PaQeenAsyncController;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.paqeen.domain.Address;
import com.cware.netshopping.paqeen.domain.CostElement;
import com.cware.netshopping.paqeen.domain.DeliveryRequest;
import com.cware.netshopping.paqeen.domain.Item;
import com.cware.netshopping.paqeen.domain.ItemGroupForSeller;
import com.cware.netshopping.paqeen.domain.OrderList;
import com.cware.netshopping.paqeen.domain.OrderListResponse;
import com.cware.netshopping.paqeen.domain.OrderSummary;
import com.cware.netshopping.paqeen.domain.ProductGoods;
import com.cware.netshopping.paqeen.domain.ProductItem;
import com.cware.netshopping.paqeen.domain.ProductPriceDetail;
import com.cware.netshopping.paqeen.domain.RefundAccount;
import com.cware.netshopping.paqeen.process.PaQeenDeliveryProcess;
import com.cware.netshopping.paqeen.repository.PaQeenDeliveryDAO;
import com.cware.netshopping.paqeen.service.PaQeenDeliveryService;
import com.cware.netshopping.paqeen.util.PaQeenApiRequest;
import com.cware.netshopping.paqeen.util.PaQeenConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("paqeen.delivery.paQeenDeliveryProcess")
public class PaQeenDeliveryProcessImpl extends AbstractProcess implements PaQeenDeliveryProcess {
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	private PaQeenConnectUtil paQeenConnectUtil;
	
	@Autowired
	private PaQeenApiRequest paQeenApiRequest;
	
	@Autowired
	private PaQeenDeliveryDAO paQeenDeliveryDAO;
	
	@Autowired
	private PaQeenAsyncController paQeenAsyncController;
	
	@Autowired
	@Qualifier("paqeen.delivery.paQeenDeliveryService")
	private PaQeenDeliveryService paQeenDeliveryService;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getOrderList(String fromDate, String toDate, HttpServletRequest request) throws Exception {
		
		String apiCode = "IF_PAQEENAPI_03_001";
		String duplicateCheck = "";
		String paCode  = "";
		int page = 1;
		ParamMap paramMap = new ParamMap();
		ParamMap retrieveDate = null;
		
		boolean isNext = true;
		
		try {
			
			log.info("======= 퀸잇 주문목록조회 API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paQeenContractCnt = Constants.PA_QEEN_CONTRACT_CNT;
			
			for(int i = 0; i< paQeenContractCnt; i ++) {
				Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				isNext = true;
				page = 1;
				log.info("03.API Request Setting");
				retrieveDate = paQeenConnectUtil.setRetrieveDateFormat(fromDate, toDate, "1");
				fromDate = retrieveDate.getString("startAt");
				toDate = retrieveDate.getString("endAt");
				
				HashMap<String, String> apiUrlParameter  = new HashMap<String, String>();
				HashMap<String, String> apiRequestObject  = new HashMap<String, String>();
				
				while(isNext) {
					apiUrlParameter.put("page", Integer.toString(page)); // 페이지 더 돌려야함 
					apiUrlParameter.put("size", "30");
					apiUrlParameter.put("startAt", fromDate);
					apiUrlParameter.put("endAt", toDate);
					apiUrlParameter.put("deliveryState", "WAIT");
					
					log.info("04.API Call");
					responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiUrlParameter);
					ObjectMapper objectMapper = new ObjectMapper();
					OrderListResponse orderListResponse = new OrderListResponse();
					orderListResponse = objectMapper.convertValue(responseMap, OrderListResponse.class);
					
					if(((List<Map<String, Object>>)(responseMap.get("list"))).size() < 1 || orderListResponse.getTotalPageCount() == page) {
						isNext = false;
					}
					page ++;
					
					
					if(orderListResponse.getList() != null) {
						log.debug("getOrderList succeed");
						paramMap.put("code", "200");
						if(orderListResponse.getList().size() > 0) {
							for(OrderList order : orderListResponse.getList()) {
								try {
									paQeenDeliveryService.savePaQeenOrderTx(order, paCode);
								} catch (Exception e) {
									log.error(e.getMessage()); 
									continue;
								}
							}
						}
					}
				}
			}
		}catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 주문목록조회 API End - {} =======");
		}
		
		putOrderState("PREPARING", request);
		orderInputMain(request);
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	@Override
	public String savePaQeenOrderTx(OrderList order, String paCode) throws Exception {
		String rtnMsg		 = Constants.SAVE_SUCCESS;
		String dateTime 	 = systemService.getSysdatetimeToString();
		Timestamp systemTime = DateUtil.toTimestamp(dateTime);
		int executedRtn = 0;
		ParamMap param = new ParamMap();
		List<Item> items ;
		ProductGoods product ;
		ProductItem productItem;
		ProductPriceDetail prdocutDetail;
		List<CostElement> costElementList;
		DeliveryRequest deliveryRequest = order.getDeliveryRequest();
		RefundAccount refundAccount = order.getRefundAccount();
		Address address = deliveryRequest.getAddress();
		OrderSummary orderSummary = order.getOrderSummary();
		List<ItemGroupForSeller> itemGroupForSellerList = orderSummary.getItemGroupsForSeller();
		
		param.put("ordererId", order.getOrdererId());
		param.put("orderId", orderSummary.getOrderId());
		param.put("confirmedAtMillis", new Date(orderSummary.getConfirmedAtMillis()));
		param.put("createdAtMillis", new Date(orderSummary.getCreatedAtMillis()));
		param.put("recipientName", address.getRecipientName());
		param.put("recipientPhoneNumber", address.getPhoneNumber());
		param.put("zipCode", address.getZipCode());
		param.put("address", address.getAddress());
		param.put("detailedAddress", address.getDetailedAddress());
		if(refundAccount != null) {
			param.put("holderName", refundAccount.getHolderName());
		}
		param.put("request", deliveryRequest.getRequest());
		
		for(ItemGroupForSeller itemGroupForSeller : itemGroupForSellerList) {
			param.put("groupId", itemGroupForSeller.getGroupId());
			costElementList = itemGroupForSeller.getCostElements();
			
			for(CostElement costElement : costElementList) {
				switch(costElement.getType()) {
				case "SHIPPING_COST_JEJU" :
					param.put("shippingCostJeju", costElement.getAmount());
					break;
				case "SHIPPING_COST_NORMAL" : 
					param.put("shippingCostNormal", costElement.getAmount());
					break;
				case "SHIPPING_COST_BACK_COUNTRY" : 
					param.put("shippingCostBackCountry", costElement.getAmount());
					break;
				}
			}
			
			items = itemGroupForSeller.getItems();
			for(Item item : items) {
				product = item.getProductGoods();
				productItem = item.getProductItem();
				prdocutDetail = product.getProductPriceDetail();
				
				param.put("orderLineId", item.getId());
				param.put("productId", product.getProductId());
				param.put("state", item.getState());
				param.put("productItemId", productItem.getId());
				param.put("productItemtitle", productItem.getTitle());
				param.put("prodcutName", product.getName());
				param.put("quantity", item.getQuantity());
				param.put("directCouponAppliedFinalPrice", prdocutDetail.getDirectCouponAppliedFinalPrice());
				param.put("finalPrice", product.getFinalPrice());
				param.put("originalPrice", prdocutDetail.getOriginalPrice());
				param.put("insertDate", systemTime);
				param.put("modifyDate", systemTime);
				param.put("sellerAmount", item.getDiscountDutyAmount().getSellerAmount());
				param.put("queenitAmount", item.getTotalProductPrice() - item.getFinalPurchaseAmount()); // 퀸잇 총 할인가 = 판매가 - 퀸잇 실 결제가
				
				// 기존주문 체크 
				int exists = paQeenDeliveryDAO.selectCountOrderList(param);
				if(exists > 0) continue;
				
				// 주문 체크 
				executedRtn = paQeenDeliveryDAO.insertPaQeenOrderList(param);
				if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAQEENORDERLIST INSERT" });
				
				//INSERT PAORDERM
				Paorderm paorderm = new Paorderm();
				
				paorderm.setPaOrderGb("10");
				paorderm.setPaCode(paCode);
				paorderm.setPaOrderNo(param.getString("orderId"));
				paorderm.setPaOrderSeq(param.getString("groupId").substring(20, 23));
				paorderm.setPaShipNo(param.getString("orderLineId").substring(20, 26));
				paorderm.setPaProcQty(ComUtil.objToStr(param.getString("quantity"), null));
				paorderm.setPaDoFlag("10");
				paorderm.setOutBefClaimGb("0");
				paorderm.setInsertDate(systemTime);
				paorderm.setModifyDate(systemTime);
				paorderm.setModifyId	(Constants.PA_QEEN_PROC_ID);
				paorderm.setPaGroupCode (Constants.PA_QEEN_GROUP_CODE);
				
				if("REFUNDED".equals(item.getState())) {
					paorderm.setPreCancelYn("1");
				}
				
				executedRtn = paQeenDeliveryDAO.insertPaOrderM(paorderm); 
				if (executedRtn < 0)  throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				
			}
		}
		
		return rtnMsg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg putOrderState(String deliveryStateEnum, HttpServletRequest request) throws Exception {
		Map<String, Object> responseMap = new HashMap<String, Object>() ;
		ParamMap paramMap = new ParamMap();
		String apiCode = "";
		if("PREPARING".equals(deliveryStateEnum)) {
			apiCode = "IF_PAQEENAPI_03_003";
		}else {
			apiCode = "IF_PAQEENAPI_03_021";
		}
		String duplicateCheck = "";
		String paCode  = "";
		int executedRtn = 0;
		
		try {
			
			log.info("======= 퀸잇 배송 상태 변경 API Start - {} =======");
			
			log.info("01.API BaseInfo Setting");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paQeenContractCnt = Constants.PA_QEEN_CONTRACT_CNT;
			
			for(int i = 0; i< paQeenContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				List<Map<String, Object>> stateList = new ArrayList<Map<String,Object>>();
				
				switch(deliveryStateEnum) {
				/* 송장 입력시 자동으로 배송중으로 변경 됌 
				case "WAIT" :
					stateList = paQeenDeliveryDAO.selectOrderConfirmTargetList();
					break;
				case "IN_DELIVERY" : 
					stateList = paQeenDeliveryDAO.selectDeliveryComplete(paramMap); 
					break;
				*/
				case "PREPARING" : 
					stateList = paQeenDeliveryDAO.selectOrderConfirmTargetList(paramMap);
					break;
				case "COMPLETED" : 
					stateList = paQeenDeliveryDAO.selectDeliveryComplete(paramMap); 
					break;
				case "CANCELED" : 
					
					break;
				}
				
				log.info("03.API Request Setting");
				
				
				if (stateList.size() > 0) {
					
					for(Map<String, Object> stateMap : stateList) {
						HashMap<String, Object> apiRequestObject  = new HashMap<String, Object>();
						HashMap<String, String> apiParameters  = new HashMap<String, String>();
						List<HashMap<String, Object>> requestParam = new ArrayList<HashMap<String,Object>>();
						HashMap<String, Object> requestMap = new HashMap<String, Object>();
						requestMap.put("orderId", stateMap.get("PA_ORDER_SEQ").toString());
						requestMap.put("orderLineId", stateMap.get("PA_SHIP_NO").toString());
						requestMap.put("deliveryStateEnum", deliveryStateEnum );
						
						requestParam.add(requestMap);
						apiRequestObject.put("requests", requestParam);
						
						log.info("04.API Call");
						responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiParameters);
						
						if(((List<Map<String, Object>>)(responseMap.get("responses"))).size() < 1) {
							continue;
						}
						
						List<HashMap<String, Object>> responses = (List<HashMap<String, Object>>) responseMap.get("responses");
						
						
						Map<String, Object> stateReturnMap = new HashMap<String, Object>();
						if("PREPARING".equals(deliveryStateEnum) && "true".equals(responses.get(0).get("success").toString())) {
							stateReturnMap.put("paDoFlag", "30");
							stateReturnMap.put("resultMessage"	, "주문 승인 완료");
							stateReturnMap.put("resultCode"		, responses.get(0).get("success").toString());
							stateReturnMap.put("mappingSeq"		, stateMap.get("MAPPING_SEQ"));
						} else if("COMPLETED".equals(deliveryStateEnum) && "true".equals(responses.get(0).get("success").toString())) {
							stateReturnMap.put("paDoFlag", "80");
							stateReturnMap.put("resultMessage"	, "배송 완료");
							stateReturnMap.put("resultCode"		, "200");
							stateReturnMap.put("mappingSeq"		, stateMap.get("MAPPING_SEQ"));
						}
						
						try {
							if("true".equals(responses.get(0).get("success").toString())) {
								executedRtn = paQeenDeliveryDAO.updatePaorderm(stateReturnMap);
								if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
							}
						} catch (Exception e) {
							log.error(e.getMessage()); 
							continue;
						}
					}
				}
			}
		}catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 배송 상태 변경 API End - {} =======");
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}
	
	/* 주문생성 */
	@RequestMapping(value = "/order-input-main", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderInputMain(HttpServletRequest request) throws Exception {
		String apiCode = "PAQEEN_ORDER_INPUT";
		ParamMap paramMap = new ParamMap();
		int paOrderCreateCnt = ConfigUtil.getInt("PA_ORDER_CREATE_CNT");
		String duplicateCheck = "";
		
		try {
			paramMap.put("apiCode", apiCode);
			
			log.info("01.API BaseInfo Setting");
			paQeenApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			List<Map<String, String>> orderInputTargetList = paQeenDeliveryDAO.selectOrderInputTargetList(paOrderCreateCnt);
			
			for (Map<String, String> orderInputTarget : orderInputTargetList) {
				try {
					paQeenAsyncController.orderInputAsync(orderInputTarget, request);
				} catch (Exception e) {
					log.error(apiCode + " - 주문 내역 생성 오류" + orderInputTarget.get("PA_ORDER_NO"), e);
				}
			}
		} catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("===== " + apiCode + " End =====");
		}
		
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return paQeenDeliveryDAO.selectOrderInputTargetDtList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception {
		return paQeenDeliveryDAO.selectRefusalInfo(mappingSeq);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg slipOutProc(HttpServletRequest request) throws Exception {
		Map<String, Object> responseMap = new HashMap<String, Object>() ;
		Map<String,Object> slipProcTargetMap = null;
		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAQEENAPI_03_002";
		String duplicateCheck = "";
		String apiResultStatus = null;
		String apiResultMesage = null;
		String paDoFlag = null;
		String mappingSeq = null;
		String paCode  = "";
		String errMsg = "";
		
		int executedRtn = 0;
		
		try {
			log.info("======= 퀸잇 송장번호 및 배송사 수정 API Start - {} =======");
			paramMap.put("apiCode", apiCode);
			paQeenApiRequest.getApiInfo(paramMap);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paQeenContractCnt = Constants.PA_QEEN_CONTRACT_CNT;
			
			for(int i = 0; i< paQeenContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_QEEN_BROAD_CODE : Constants.PA_QEEN_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				
				List<Map<String,Object>> slipProcTargetList= paQeenDeliveryDAO.selectSlipProcList(paramMap);
				HashMap<String, Object> apiRequestObject  = new HashMap<String, Object>();
				HashMap<String, String> apiParameters  = new HashMap<String, String>();
				
				List<Map<String,Object>> slipOutProcList = new ArrayList<Map<String,Object>>();
				
				for (Map<String, Object> slipOutTarget : slipProcTargetList) {
					
					Map<String,Object> slipOutProc = new HashMap<String, Object>();
					
					slipOutProc.put("orderId", slipOutTarget.get("ORDER_ID").toString());
					slipOutProc.put("orderLineId", slipOutTarget.get("ORDER_LINE_ID").toString());
					slipOutProc.put("deliveryVendor", slipOutTarget.get("PA_DELY_GB").toString());
					slipOutProc.put("trackingNumber", slipOutTarget.get("SLIP_NO").toString());
					
					slipOutProcList.add(slipOutProc);
				}
				
				apiRequestObject.put("list", slipOutProcList);
				
				if(slipOutProcList.size()>0) {
					log.info("04.API Call");
					responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, apiParameters);
					
					List<HashMap<String, Object>> responsesList = (List<HashMap<String, Object>>) responseMap.get("responses");
					
					for (Map<String, Object> responses : responsesList) {
						apiResultStatus = String.valueOf(responses.get("success"));
						
						mappingSeq = slipProcTargetList.stream()
								.filter(slipOutProc -> String.valueOf(slipOutProc.get("ORDER_ID"))
										.equals(String.valueOf(responses.get("orderId")))
										&& String.valueOf(slipOutProc.get("ORDER_LINE_ID"))
										.equals(String.valueOf(responses.get("orderLineId"))))
								.map(slipOutProc -> String.valueOf(slipOutProc.get("MAPPING_SEQ"))).findFirst()
								.orElse(null);
						
						if ("true".equals(apiResultStatus)
								//|| "Err-Inv-005".equals(apiResultStatus)
								) { // Err-Inv-005 : 이미 출고처리되었습니다.
							paDoFlag = "40";
							apiResultMesage = "운송장등록 성공";
						} else {
							paDoFlag = null;
							apiResultMesage = String.valueOf(responses.get("failedReason"));
							errMsg += "QEEN_MAPPING_SEQ : " + mappingSeq + " | 송장 등록 실패 :: (" + apiResultMesage
									+ ") / ";
						}
						
						slipProcTargetMap = new HashMap<String, Object>();
						slipProcTargetMap.put("mappingSeq", mappingSeq);
						
						//UPDATE TPAORDERM.DO_FLAG
						executedRtn = updatePaOrderm(slipProcTargetMap, paDoFlag , apiResultMesage, Constants.SAVE_SUCCESS);
						if(executedRtn>0) {
							paramMap.put("code", "200");
							paramMap.put("message", "발송처리 성공 및 저장 완료");
						}else {
							paramMap.put("code", "400");
							paramMap.put("message", "저장 실패");
						}
						
						if (StringUtils.isNotBlank(errMsg)) {
							paramMap.put("code", "500");
							paramMap.put("message", errMsg);
							log.error(errMsg);
						}
					}
				}else {
					paramMap.put("code", "200");
					paramMap.put("message", "발송 처리 대상 없음");
				}
			}
		}catch (Exception e) {
			paQeenConnectUtil.checkException(paramMap, e);
		} finally {
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 송장번호 및 배송사 수정 API End - {} =======");
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	private int updatePaOrderm(Map<String, Object> slipProcTargetMap, String paDoFlag, String apiResultMesage,
			String resultCode) {
		int executedRtn = 0;
		
		try {
			
			Map<String, Object> order = new HashMap<String,Object>();
			
			order.put("paDoFlag"		, paDoFlag);
			order.put("resultMessage"	, apiResultMesage);
			order.put("resultCode"		, resultCode);
			order.put("mappingSeq"		, slipProcTargetMap.get("mappingSeq"));
			
			if(slipProcTargetMap.containsKey("preCancelYn") && !"".equals(slipProcTargetMap.get("preCancelYn"))) {
				order.put("preCancelYn"		, slipProcTargetMap.get("preCancelYn"));
			}
			executedRtn = paQeenDeliveryDAO.updatePaorderm(order);
		} catch (Exception e) {
			log.info("{} : {}", "퀸잇 제휴 주문 테이블 업데이트 오류",  e.getMessage() );
		}
		
		return executedRtn;
	}

	@Override
	public int updatePreCanYn(Map<String, Object> preCancelMap) throws Exception {
		return paQeenDeliveryDAO.updatePreCanYn(preCancelMap);
	}
	
}
