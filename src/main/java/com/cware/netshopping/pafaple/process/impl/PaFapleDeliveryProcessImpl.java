package com.cware.netshopping.pafaple.process.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.pafaple.controller.PaFapleAsyncController;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pafaple.domain.PaFapleOrderListVO;
import com.cware.netshopping.pafaple.process.PaFapleDeliveryProcess;
import com.cware.netshopping.pafaple.repository.PaFapleDeliveryDAO;
import com.cware.netshopping.pafaple.util.PaFapleApiRequest;
import com.cware.netshopping.pafaple.util.PaFapleConnectUtil;

@Service("pafaple.delivery.paFapleDeliveryProcess")
public class PaFapleDeliveryProcessImpl extends AbstractProcess implements PaFapleDeliveryProcess {
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	private PaFapleConnectUtil paFapleConnectUtil;
	
	@Autowired
	private PaFapleApiRequest paFapleApiRequest;
	
	@Autowired
	private PaFapleDeliveryDAO paFapleDeliveryDAO;
	
	@Autowired
	private PaFapleAsyncController paFapleAsyncController;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg getOrderList(String fromDate, String toDate, HttpServletRequest request) throws Exception {
		
		String apiCode = "IF_PAFAPLEAPI_03_001";
		String duplicateCheck = "";
		String apiResultStatus = null;
		String statusCd = null;
		String dateTime = null;
		String paCode  = "";
		String ship_to_mobile = "";
		String ship_to_phone = "";
		
		List<Map<String, Object>> orderList = null;
		ParamMap paramMap = new ParamMap();
		ParamMap retrieveDate = null;
		PaFapleOrderListVO paFapleOrderListVO = null;
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		try {
			
			log.info("======= 패션플러스 주문목록조회 API Start - {} =======");
			
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
			
			for(int i = 0; i< paFapleContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				
				log.info("03.API Request Setting");
				retrieveDate = paFapleConnectUtil.setRetrieveDateFormat(fromDate, toDate, "1");
				fromDate = retrieveDate.getString("startDate");
				toDate = retrieveDate.getString("endDate");
				
				Map<String, String> apiRequestMap  = new HashMap<String, String>();
				apiRequestMap.put("DlvProc", "X");
				apiRequestMap.put("StartDate", fromDate);
				apiRequestMap.put("EndDate", toDate);
				
				log.info("04.API Call");
				responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiRequestMap);
			
				if (responseMap != null) {
					apiResultStatus = String.valueOf(responseMap.get("Status"));
					
					if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(apiResultStatus)) {
						log.info("05.Processing");
						orderList = (List<Map<String, Object>>)(responseMap.get("OrderList"));
						
						for(Map<String, Object> order : orderList) {
							try {
								statusCd = ComUtil.objToStr(order.get("status_cd"));
								
								if ("지불완료".equals(statusCd)) {
									paFapleOrderListVO = new PaFapleOrderListVO();
									dateTime = systemService.getSysdatetimeToString();
									ship_to_mobile = ComUtil.objToStr(order.get("ship_to_mobile"));
									ship_to_phone = ComUtil.objToStr(order.get("ship_to_phone"));
									ship_to_phone = ship_to_phone.trim().equals("") ? "--" : ship_to_phone;
									
									paFapleOrderListVO.setPaCode(ComUtil.objToStr(paramMap.get("paCode")));
									paFapleOrderListVO.setOrderId(ComUtil.objToStr(order.get("order_id")));
									paFapleOrderListVO.setOrderDate(DateUtil.toTimestamp(ComUtil.objToStr(order.get("order_date")),DateUtil.FAPLE_DATE_FORMAT));
									paFapleOrderListVO.setCreateDate(DateUtil.toTimestamp(ComUtil.objToStr(order.get("created_date")),DateUtil.FAPLE_DATE_FORMAT));
									paFapleOrderListVO.setItemNo(ComUtil.objToStr(order.get("item_no")));
									paFapleOrderListVO.setItemId(ComUtil.objToStr(order.get("item_id")));
									paFapleOrderListVO.setStatusCd(ComUtil.objToStr(order.get("status_cd")));
									paFapleOrderListVO.setPayMethod(ComUtil.objToStr(order.get("pay_method")));
									paFapleOrderListVO.setShopperName(ComUtil.objToStr(order.get("shopper_name")));
									paFapleOrderListVO.setOptId(ComUtil.objToStr(order.get("OptID")));
									paFapleOrderListVO.setGoodsNo(ComUtil.objToStr(order.get("goods_no")));
									paFapleOrderListVO.setGoodsName(ComUtil.objToStr(order.get("goods_name")));
									paFapleOrderListVO.setGoodsAttr(ComUtil.objToStr(order.get("goods_attr")));
									paFapleOrderListVO.setColor(ComUtil.objToStr(order.get("color")));
									paFapleOrderListVO.setSize(ComUtil.objToStr(order.get("size")));
									paFapleOrderListVO.setQuantity(ComUtil.objToLong(order.get("quantity")));
									paFapleOrderListVO.setIadjustCurrentprice(ComUtil.objToDouble(order.get("iadjust_currentprice")));
									paFapleOrderListVO.setOadjustAdjustedprice(ComUtil.objToDouble(order.get("oadjust_adjustedprice")));
									paFapleOrderListVO.setProductPurchasePrice(ComUtil.objToDouble(order.get("product_purchase_price")));
									paFapleOrderListVO.setShopperReceiptName(ComUtil.objToStr(order.get("shopper_receipt_name")));
									paFapleOrderListVO.setShopperReceiptStreet(ComUtil.objToStr(order.get("shopper_receipt_street")));
									paFapleOrderListVO.setShipToStreet(ComUtil.objToStr(order.get("ship_to_street")));
									paFapleOrderListVO.setShipToZip(ComUtil.objToStr(order.get("ship_to_zip")));
									paFapleOrderListVO.setShipToName(ComUtil.objToStr(order.get("ship_to_name")));
									paFapleOrderListVO.setShipToPhone((!ship_to_mobile.equals("") && ship_to_phone.equals("--")) ? ship_to_mobile : ship_to_phone);
									paFapleOrderListVO.setShipToMobile(ship_to_mobile);
									paFapleOrderListVO.setGiftMessage(ComUtil.objToStr(order.get("gift_message")));
									paFapleOrderListVO.setGiftName(ComUtil.objToStr(order.get("gift_name")));
									paFapleOrderListVO.setDeliveryDate(ComUtil.objToStr(order.get("delivery_date")));
									paFapleOrderListVO.setGoalDeliDate(ComUtil.objToStr(order.get("goalDeli_date")));
									paFapleOrderListVO.setEtcInfo1(ComUtil.objToStr(order.get("etcInfo1")));
									paFapleOrderListVO.setEtcInfo2(ComUtil.objToStr(order.get("etcInfo2")));
									paFapleOrderListVO.setCouponUseCust(ComUtil.objToDouble(order.get("coupon_use_cust")));
									paFapleOrderListVO.setOrderDateDetail(DateUtil.toTimestamp(ComUtil.objToStr(order.get("order_date_detail")),DateUtil.FAPLE_DATETIME_FORMAT));
									paFapleOrderListVO.setSenderFee(ComUtil.objToDouble(order.get("sender_fee")));
									paFapleOrderListVO.setUitemId(ComUtil.objToStr(order.get("UitemID")));
									paFapleOrderListVO.setInsertId(Constants.PA_FAPLE_PROC_ID);
									paFapleOrderListVO.setModifyId(Constants.PA_FAPLE_PROC_ID);
									paFapleOrderListVO.setModifyDate(DateUtil.toTimestamp(dateTime));
									paFapleOrderListVO.setInsertDate(DateUtil.toTimestamp(dateTime));
									
									saveFapleOrderList(paFapleOrderListVO);
								}
								
							} catch (Exception e) {
								log.error("Exception occurs : " + e.getMessage());
							}
						}
					} else if ("Err-Dat-902".equals(apiResultStatus)) { // Err-Dat-902 : 발주확인할 주문이 없습니다.
						paramMap.put("code", "200");
						paramMap.put("message", getMessage("errors.no.select"));
					} else {
						throw new Exception(String.valueOf(paramMap));
					}
				}
			}
		}catch (Exception e) {
			paFapleConnectUtil.checkException(paramMap, e);
		} finally {
			paFapleConnectUtil.closeApi(request, paramMap);
			log.info("======= 패션플러스 주문목록조회 API End - {} =======");
		}
		
		orderConfirm(fromDate, toDate, request);
		orderInputMain(request);
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	private String saveFapleOrderList(PaFapleOrderListVO paFapleOrderListVO) throws Exception {
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		int exists = 0;
		
		exists = paFapleDeliveryDAO.checkOrderExistYn(paFapleOrderListVO);
		
		if (exists > 0) {
			return "";
		}
		
		executedRtn = paFapleDeliveryDAO.insertPaFapleOrderList(paFapleOrderListVO);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAFAPLEORDERLIST INSERT" });
		}
		
		//=INSERT TPAORDERM 
		Paorderm paorderm = new Paorderm();
		paorderm.setPaCode(paFapleOrderListVO.getPaCode());
		paorderm.setPaOrderGb("10");
		paorderm.setPaGroupCode("14");
		paorderm.setPaOrderNo(paFapleOrderListVO.getOrderId());
		paorderm.setPaOrderSeq(paFapleOrderListVO.getItemId());
		paorderm.setPaProcQty(ComUtil.objToStr(paFapleOrderListVO.getQuantity(), null));
		paorderm.setPaDoFlag("10");
		paorderm.setOutBefClaimGb("0");
		paorderm.setPreCancelYn("0");
		paorderm.setInsertDate(paFapleOrderListVO.getInsertDate());
		paorderm.setModifyDate(paFapleOrderListVO.getModifyDate());
		paorderm.setModifyId(paFapleOrderListVO.getModifyId());
		
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		
		return rtnMsg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseMsg slipOutProc(String order_id, String item_id, HttpServletRequest request) throws Exception {
		ParamMap paramMap = new ParamMap();
		Map<String,Object> slipProcTargetMap = null;
		
		String apiCode = "IF_PAFAPLEAPI_03_002";
		String duplicateCheck = "";
		String paDoFlag = null;
		String mappingSeq = null;
		String paCode  = "";
		String apiResultStatus = null;
		String apiResultMesage = null;
		String errMsg = "";
		List<Map<String, Object>> slipOutList = null;
		
		int executedRtn = 0;
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		
		log.info("======= 패션플러스 발송처리 API Start - {} =======");
		try {
			paramMap.put("apiCode", apiCode);
			paramMap.put("order_id", order_id);
			paramMap.put("item_id", item_id);
			paFapleApiRequest.getApiInfo(paramMap);
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			int paFapleContractCnt = Constants.PA_FAPLE_CONTRACT_CNT;
			//로컬 세팅
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				paFapleContractCnt = 1;
			}
			
			for(int i = 0; i< paFapleContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				
				// 수정 필요한 쿼리문
				List<Map<String,Object>> slipProcTargetList= paFapleDeliveryDAO.selectSlipProcList(paramMap);
				ParamMap apiRequestMap = null; 
				
				List<Map<String,Object>> slipOutProcList = new ArrayList<Map<String,Object>>();
				
				if (slipProcTargetList.size() > 0) {
					
					for (Map<String, Object> slipOutTarget : slipProcTargetList) {
						
						Map<String,Object> slipOutProc = new HashMap<String, Object>();
						
						slipOutProc.put("order_id", Integer.parseInt((String) slipOutTarget.get("ORDER_ID"))); // 발주확인 API에서 수집한 패션플러스 주문ID
						slipOutProc.put("item_id", Integer.parseInt((String) slipOutTarget.get("ITEM_ID"))); // 발주확인 API에서 수집한 패션플러스 주문순번
						slipOutProc.put("invoice_no", slipOutTarget.get("SLIP_NO")); // 송장번호
						slipOutProc.put("delivery_co", slipOutTarget.get("PA_DELY_GB")); // 택배사코드(미입력시 등록된 배송처 정보를 기준으로 자동 적용)
						
						slipOutProcList.add(slipOutProc);
					}
					
					apiRequestMap = new ParamMap();
					apiRequestMap.put("OrderList", slipOutProcList);
					
					if(slipOutProcList.size()>0) {
						
						responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiRequestMap);
						
						if (responseMap != null) {
							slipOutList = (List<Map<String, Object>>) responseMap.get("OderList");
							
							if (slipOutList != null) {
								for (Map<String, Object> slipOut : slipOutList) {
									apiResultStatus = String.valueOf(slipOut.get("Status"));
									
									mappingSeq = slipProcTargetList.stream()
											.filter(slipOutProc -> String.valueOf(slipOutProc.get("ORDER_ID"))
													.equals(String.valueOf(slipOut.get("order_id")))
													&& String.valueOf(slipOutProc.get("ITEM_ID"))
															.equals(String.valueOf(slipOut.get("item_id"))))
											.map(slipOutProc -> String.valueOf(slipOutProc.get("MAPPING_SEQ"))).findFirst()
											.orElse(null);
									
									if (Constants.PA_FAPLE_SUCCESS_STATUS.equals(apiResultStatus)
											|| "Err-Inv-005".equals(apiResultStatus)) { // Err-Inv-005 : 이미 출고처리되었습니다.
										paDoFlag = "40";
										apiResultMesage = "운송장등록 성공";
									} else {
										paDoFlag = null;
										apiResultMesage = String.valueOf(slipOut.get("Message"));
										errMsg += "FAPLE_MAPPING_SEQ : " + mappingSeq + " | 송장 등록 실패 :: (" + apiResultMesage
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
								}
							} else {
								errMsg = responseMap.toString();
							}
							
							if (StringUtils.isNotBlank(errMsg)) {
								paramMap.put("code", "500");
								paramMap.put("message", errMsg);
								log.error(errMsg);
							}
						}
					}
				} else {
					paramMap.put("code", "200");
					paramMap.put("message", getMessage("발송 처리 대상 없음"));
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			paFapleConnectUtil.checkException(paramMap ,  e);
		}finally {
			if(paramMap.get("code") == null || "".equals(paramMap.get("code"))) {
				paramMap.put("code", "200");
				paramMap.put("message", "발송 처리 대상 없음");
			}
			else {
				paramMap.put("message", paramMap.get("message"));
			}
			paFapleConnectUtil.closeApi(request, paramMap);
			log.info("======= 패션플러스 발송처리 API End - {} =======");
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
	}

	private int updatePaOrderm(Map<String, Object> slipProcTargetMap,String paDoFlag, String message, String resultCode) {
		int executedRtn = 0;
		
		try {
			
			Map<String, Object> order = new HashMap<String,Object>();
			
			order.put("paDoFlag"		, paDoFlag);
			order.put("resultMessage"	, message);
			order.put("resultCode"		, resultCode);
			order.put("mappingSeq"		, slipProcTargetMap.get("mappingSeq"));
			
			if(slipProcTargetMap.containsKey("preCancelYn") && !"".equals(slipProcTargetMap.get("preCancelYn"))) { 
				order.put("preCancelYn"		, slipProcTargetMap.get("preCancelYn"));
			}
			executedRtn = paFapleDeliveryDAO.updatePaorderm(order);
		} catch (Exception e) {
			log.info("{} : {}", "패션플러스 제휴 주문 테이블 업데이트 오류",  e.getMessage() );
		}
		
		return executedRtn;
	}

	@Override
	public ResponseMsg orderConfirm(String fromDate, String toDate, HttpServletRequest request) throws Exception {

		String apiCode = "IF_PAFAPLEAPI_03_001";
		String duplicateCheck = "";
		String apiResultStatus = null;
		String paCode = "";
		
		List<Map<String, Object>> orderConfirmTargetList = null;
		Map<String, Object> orderConfirm = null;
		ParamMap paramMap = new ParamMap();
		ParamMap retrieveDate = null;
		
		Map<String, Object> responseMap 	= new HashMap<String, Object>() ;
		try {
			
			log.info("======= 패션플러스 배송접수 API Start - {} =======");
			
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
			
			for(int i = 0; i< paFapleContractCnt; i ++) {
				paCode = (i==0 )? Constants.PA_FAPLE_BROAD_CODE : Constants.PA_FAPLE_ONLINE_CODE;
				paramMap.put("paCode", paCode);
				
				log.info("03.API Request Setting");
				retrieveDate = paFapleConnectUtil.setRetrieveDateFormat(fromDate, toDate, "1");
				fromDate = retrieveDate.getString("startDate");
				toDate = retrieveDate.getString("endDate");
				
				Map<String, String> apiRequestMap  = new HashMap<String, String>();
				apiRequestMap.put("DlvProc", "O");
				apiRequestMap.put("StartDate", fromDate);
				apiRequestMap.put("EndDate", toDate);
				
				log.info("04.API Call");
				responseMap = paFapleConnectUtil.callPaFapleAPILegacy(paramMap, apiRequestMap);
				
				if (responseMap != null) {
					apiResultStatus = String.valueOf(responseMap.get("Status"));
					
					// 패션플러스의 배송 접수 API 호출 여부와 상관없이 TPAORDERM 주문(승인) 여부로 판단하여 처리 
					orderConfirmTargetList = paFapleDeliveryDAO.selectOrderConfirmTargetList();
					
					if (orderConfirmTargetList.size() > 0) {
						for (Map<String, Object> orderConfirmTarget : orderConfirmTargetList) {
							try {
								orderConfirm = new HashMap<String, Object>();
								orderConfirm.put("mappingSeq", orderConfirmTarget.get("MAPPING_SEQ"));
		
								// UPDATE TPAORDERM.DO_FLAG(10 -> 30)
								updatePaOrderm(orderConfirm, "30" , "주문 승인 성공",Constants.PA_FAPLE_SUCCESS_STATUS);
							} catch (Exception e) {
								log.error("Exception occurs : " + e.getMessage() + " / PA_ORDER_NO: " + String.valueOf(orderConfirmTarget.get("PA_ORDER_NO")));
							}
						}
					} else if (!Constants.PA_FAPLE_SUCCESS_STATUS.equals(apiResultStatus) && !"Err-Dat-902".equals(apiResultStatus)) { // Err-Dat-902 : 발주확인할 주문이 없습니다.
						throw new Exception(String.valueOf(responseMap));
					} else {
						paramMap.put("code", "200");
						paramMap.put("message", getMessage("errors.no.select"));
					}
				}
			}
		}catch (Exception e) {
			paFapleConnectUtil.checkException(paramMap, e);
		} finally {
			paFapleConnectUtil.closeApi(request, paramMap);
			log.info("======= 패션플러스 배송접수 API End - {} =======");
		}
		
		return new ResponseMsg(paramMap.getString("code"), paramMap.getString("message"));
		
	}
	
	/* 주문생성 */
	@RequestMapping(value = "/order-input-main", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderInputMain(HttpServletRequest request) throws Exception {
		String apiCode = "PAFAPLE_ORDER_INPUT";
		ParamMap paramMap = new ParamMap();
		int paOrderCreateCnt = ConfigUtil.getInt("PA_ORDER_CREATE_CNT");
		String duplicateCheck = "";
		
		try {
			paramMap.put("apiCode", apiCode);
			
			log.info("01.API BaseInfo Setting");
			paFapleApiRequest.getApiInfo(paramMap);
			
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			
			List<Map<String, String>> orderInputTargetList = paFapleDeliveryDAO.selectOrderInputTargetList(paOrderCreateCnt);
			
			for (Map<String, String> orderInputTarget : orderInputTargetList) {
				try {
					paFapleAsyncController.orderInputAsync(orderInputTarget, request);
				} catch (Exception e) {
					log.error(apiCode + " - 주문 내역 생성 오류" + orderInputTarget.get("PA_ORDER_NO"), e);
				}
			}
		} catch (Exception e) {
			paFapleConnectUtil.checkException(paramMap, e);
		} finally {
			paFapleConnectUtil.closeApi(request, paramMap);
			log.info("===== " + apiCode + " End =====");
		}
		
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return paFapleDeliveryDAO.selectOrderInputTargetDtList(paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectRefusalInfo(List<HashMap<String, Object>> itemList) throws Exception {
		return paFapleDeliveryDAO.selectRefusalInfo(itemList);
	}

	@Override
	public int updatePreCanYn(Map<String, Object> preCancelMap) throws Exception {
		return paFapleDeliveryDAO.updatePreCanYn(preCancelMap);
	}
}
