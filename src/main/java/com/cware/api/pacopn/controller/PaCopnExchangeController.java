package com.cware.api.pacopn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.common.exception.ProcessException;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Pacopnexchangeitemlist;
import com.cware.netshopping.domain.model.Pacopnexchangelist;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacopn.exchange.service.PaCopnExchangeService;
import com.cware.netshopping.pagmkt.util.PaGmktDateUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Api(value = "/pacopn/exchange", description = "공통")
@Controller("com.cware.api.pacopn.PaCopnExchangeController")
@RequestMapping(value = "/pacopn/exchange")
public class PaCopnExchangeController extends AbstractController {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	@Resource(name = "pacopn.exchange.paCopnExchangeService")
	private PaCopnExchangeService paCopnExchangeService;

	@Resource(name = "com.cware.api.pacopn.paCopnAsyncController")
	private PaCopnAsyncController asyncController;

	/**
	 * 쿠팡 - 교환신청/취소 목록조회
	 *
	 * @param request
	 * @param fromDate
	 * @param toDate
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "교환신청/취소 목록조회", notes = "교환신청/취소 목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."), @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") })
	@RequestMapping(value = "/exchange-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeList(HttpServletRequest request,
			@ApiParam(name = "fromDate", required = false, value = "FROM날짜[yyyyMMddHHmmss]", defaultValue = "") @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
			@ApiParam(name = "toDate", required = false, value = "TO날짜[yyyyMMddHHmmss]", defaultValue = "") @RequestParam(value = "toDate", required = false, defaultValue = "") String toDate,
			@ApiParam(name = "status", required = false, value = "접수상태", defaultValue = "RECEIPT") @RequestParam(value = "status", required = false, defaultValue = "RECEIPT") String status)
			throws Exception {

		ParamMap paramMap = null;
		HashMap<String, String> apiInfo = null;
		String duplicateCheck = "0";

		ArrayList<Pacopnexchangelist> exchangeListArr = null;
		ArrayList<Pacopnexchangeitemlist> exchangeItemListArr = null;
		Pacopnexchangelist exchangeList = null;
		Pacopnexchangeitemlist exchangeItemList = null;
		JsonObject responseObject = null;
		JsonObject tempObject = null;
		HashMap<String, String> exchangemMap = new HashMap<String, String>();

		log.info("[COPN-교환신청/취소 목록조회] start");
		log.info("[COPN-교환신청/취소 목록조회] satus : " + status);

		try {
			// API 기본정보 세팅
			paramMap = new ParamMap();
			paramMap.put("code", Constants.PA_COPN_SUCCESS_OK);
			paramMap.put("message", "OK");

			paramMap.put("apiCode", "RECEIPT".equals(status) ? "IF_PACOPNAPI_03_014" : "IF_PACOPNAPI_03_018");
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("maxPerPage", Constants.PA_COPN_COUNT_PER_PAGE);

			// 중복 실행 체크
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });

			// status: RECEIPT, CANCEL, REJECT 만 처리
			if (!status.equals("RECEIPT") && !status.equals("CANCEL") && !status.equals("REJECT")) {
				throw processException("errors.invalid", new String[] { "status : " + status });
			}

			// API 정보 조회
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

			// 조회 기간 기본 세팅: D-2 ~ D
			if (fromDate.isEmpty() || toDate.isEmpty()) {
				fromDate = DateUtil.formatConvertStringToGmarketDate(paramMap.getString("startDate"), -5);
				toDate = DateUtil.formatConvertStringToGmarketDate(paramMap.getString("startDate"), 1);
			} else {
				fromDate = DateUtil.timestampToString(
						DateUtil.toTimestamp(fromDate + "000000", DateUtil.DEFAULT_JAVA_DATE_FORMAT),
						DateUtil.GMKT_DATE_FORMAT);
				toDate = DateUtil.timestampToString(
						DateUtil.toTimestamp(toDate + "235959", DateUtil.DEFAULT_JAVA_DATE_FORMAT),
						DateUtil.GMKT_DATE_FORMAT);
			}
			
			for(int count=0 ; count<Constants.PA_COPN_CONTRACT_CNT; count++) {
				
				paramMap.put("nextToken", "init");
				
				if(count == 0) paramMap.put("paName", Constants.PA_BROAD);
				else paramMap.put("paName", Constants.PA_ONLINE);
				
				exchangeListArr = new ArrayList<Pacopnexchangelist>();
				while (!paramMap.getString("nextToken").isEmpty()) {
					
					log.info("[COPN-교환신청/취소 목록조회] API 호출 status : " + status);
					responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"),
							new URIBuilder(apiInfo.get("API_URL").toString().replaceAll("#vendorId#",
									apiInfo.get(paramMap.getString("paName")).split(";")[0])).addParameter("createdAtFrom", fromDate)
									.addParameter("createdAtTo", toDate).addParameter("status", status)
									.addParameter("nextToken",
											"init".equals(paramMap.getString("nextToken")) ? ""
													: paramMap.getString("nextToken"))
													.addParameter("maxPerPage", paramMap.getString("maxPerPage")));
					
					log.info("[COPN-교환신청/취소 목록조회] API 호출 결과처리");
					paramMap.put("code"   , "200".equals(responseObject.get("code").getAsString()) ? "200" : "500");
					paramMap.put("message", responseObject.get("message").getAsString());
					
					if (!Constants.PA_COPN_SUCCESS_OK.equals(responseObject.get("code").getAsString())) {
						break;
					}
					
					if (responseObject.get("data").getAsJsonArray().size() == 0) {
						paramMap.put("code", "404");
						paramMap.put("message", getMessage("errors.no.select"));
						break;
					}
					
					for (JsonElement element : responseObject.get("data").getAsJsonArray()) {
						exchangeList = new Pacopnexchangelist();
						exchangeList.setBlank();
						exchangeList.setPaOrderGb("RECEIPT".equals(status) ? "40" : "41");
						exchangeList.setExchangeId(NVL(element.getAsJsonObject().get("exchangeId")));
						exchangeList.setOrderId(NVL(element.getAsJsonObject().get("orderId")));
						exchangeList.setVendorId(NVL(element.getAsJsonObject().get("vendorId")));
						exchangeList.setOrderDeliveryStatusCode(NVL(element.getAsJsonObject().get("orderDeliveryStatusCode")));
						exchangeList.setExchangeStatus(NVL(element.getAsJsonObject().get("exchangeStatus")));
						exchangeList.setReferType(NVL(element.getAsJsonObject().get("referType")));
						exchangeList.setFaultType(NVL(element.getAsJsonObject().get("faultType")));
						exchangeList.setExchangeAmount(NVL(element.getAsJsonObject().get("exchangeAmount")));
						exchangeList.setReasonCode(NVL(element.getAsJsonObject().get("reasonCode")));
						exchangeList.setReasonCodeText(NVL(element.getAsJsonObject().get("reasonCodeText")));
						exchangeList.setReasonEtcDetail(NVL(element.getAsJsonObject().get("reasonEtcDetail")));
						exchangeList.setCancelReason(NVL(element.getAsJsonObject().get("cancelReason")));
						exchangeList.setCreatedByType(NVL(element.getAsJsonObject().get("createdByType")));
						exchangeList.setCreatedAt(PaGmktDateUtil.toTimestamp(NVL(element.getAsJsonObject().get("createdAt"))));
						exchangeList.setModifiedByType(NVL(element.getAsJsonObject().get("modifiedByType")));
						exchangeList.setModifiedAt(PaGmktDateUtil.toTimestamp(NVL(element.getAsJsonObject().get("modifiedAt"))));
						
						exchangeItemListArr = new ArrayList<Pacopnexchangeitemlist>();
						paramMap.put("orderId", exchangeList.getOrderId());
						
						for (JsonElement subElement : element.getAsJsonObject().get("exchangeItemDtoV1s")
								.getAsJsonArray()) {
							exchangeItemList = new Pacopnexchangeitemlist();
							exchangeItemList.setBlank();
							exchangeItemList.setPaOrderGb(exchangeList.getPaOrderGb());
							exchangeItemList.setExchangeId(exchangeList.getExchangeId());
							exchangeItemList.setOrderId(exchangeList.getOrderId());
							exchangeItemList.setExchangeItemId(NVL(subElement.getAsJsonObject().get("exchangeItemId")));
							exchangeItemList.setOrderItemId(NVL(subElement.getAsJsonObject().get("orderItemId")));
							exchangeItemList.setOrderItemUnitPrice(NVL(subElement.getAsJsonObject().get("orderItemUnitPrice")));
							exchangeItemList.setOrderItemName(NVL(subElement.getAsJsonObject().get("orderItemName")));
							exchangeItemList.setOrderPackageId(NVL(subElement.getAsJsonObject().get("orderPackageId")));
							exchangeItemList.setOrderPackageName(NVL(subElement.getAsJsonObject().get("orderPackageName")));
							exchangeItemList.setTargetItemId(NVL(subElement.getAsJsonObject().get("targetItemId")));
							exchangeItemList.setTargetItemUnitPrice(NVL(subElement.getAsJsonObject().get("targetItemUnitPrice")));
							exchangeItemList.setTargetItemName(NVL(subElement.getAsJsonObject().get("targetItemName")));
							exchangeItemList.setTargetPackageId(NVL(subElement.getAsJsonObject().get("targetPackageId")));
							exchangeItemList.setTargetPackageName(NVL(subElement.getAsJsonObject().get("targetPackageName")));
							exchangeItemList.setQuantity(subElement.getAsJsonObject().get("quantity").getAsLong());
							exchangeItemList.setOrderItemDeliveryComplete(NVL(subElement.getAsJsonObject().get("orderItemDeliveryComplete")));
							exchangeItemList.setOrderItemReturnComplete(NVL(subElement.getAsJsonObject().get("orderItemReturnComplete")));
							exchangeItemList.setTargetItemDeliveryComplete(NVL(subElement.getAsJsonObject().get("targetItemDeliveryComplete")));
							exchangeItemList.setCreatedAt(PaGmktDateUtil.toTimestamp(NVL(subElement.getAsJsonObject().get("createdAt"))));
							exchangeItemList.setModifiedAt(PaGmktDateUtil.toTimestamp(NVL(subElement.getAsJsonObject().get("modifiedAt"))));
							exchangeItemList.setOriginalShipmentBoxId(NVL(subElement.getAsJsonObject().get("originalShipmentBoxId")));
							
							paramMap.put("orderId", exchangeList.getOrderId());
							paramMap.put("vendorItemId", exchangeItemList.getOrderItemId());
							//paramMap.put("shipmentBoxId", exchangeItemList.getOriginalShipmentBoxId());
							exchangemMap = paCopnExchangeService.selectOrgShipmentBoxId(paramMap);
							exchangeItemList.setItemSeq(exchangemMap.get("ITEM_SEQ").toString());
							
							exchangeItemListArr.add(exchangeItemList);
						}
						exchangeList.setExchangeItemDtoV1s(exchangeItemListArr.toArray(new Pacopnexchangeitemlist[0]));
						
						tempObject = element.getAsJsonObject().get("exchangeAddressDtoV1").getAsJsonObject();
						exchangeList.setEadExchangeAddressId(NVL(tempObject.get("exchangeAddressId")));
						exchangeList.setEadReturnCustomerName(NVL(tempObject.get("returnCustomerName")));
						exchangeList.setEadReturnAddressZipCode(NVL(tempObject.get("returnAddressZipCode")));
						exchangeList.setEadReturnAddress(NVL(tempObject.get("returnAddress")));
						exchangeList.setEadReturnAddressDetail(NVL(tempObject.get("returnAddressDetail")));
						exchangeList.setEadReturnPhone(NVL(tempObject.get("returnPhone")));
						exchangeList.setEadReturnMobile(NVL(tempObject.get("returnMobile")));
						exchangeList.setEadReturnMemo(NVL(tempObject.get("returnMemo")));
						exchangeList.setEadDeliveryCustomerName(NVL(tempObject.get("deliveryCustomerName")));
						exchangeList.setEadDeliveryAddressZipCode(NVL(tempObject.get("deliveryAddressZipCode")));
						exchangeList.setEadDeliveryAddress(NVL(tempObject.get("deliveryAddress")));
						exchangeList.setEadDeliveryAddressDetail(NVL(tempObject.get("deliveryAddressDetail")));
						exchangeList.setEadDeliveryPhone(NVL(tempObject.get("deliveryPhone")));
						exchangeList.setEadDeliveryMobile(NVL(tempObject.get("deliveryMobile")));
						exchangeList.setEadDeliveryMemo(NVL(tempObject.get("deliveryMemo")));
						exchangeList.setEadCreatedAt(PaGmktDateUtil.toTimestamp(NVL(tempObject.get("createdAt"))));
						exchangeList.setEadModifiedAt(PaGmktDateUtil.toTimestamp(NVL(tempObject.get("modifiedAt"))));
						exchangeList.setEadExchangeId(NVL(tempObject.get("exchangeId")));
						exchangeList.setDeliveryInvoiceGroupDtos(element.getAsJsonObject().get("deliveryInvoiceGroupDtos").toString());
						exchangeList.setDeliveryStatus(NVL(element.getAsJsonObject().get("deliveryStatus")));
						exchangeList.setCollectStatus(NVL(element.getAsJsonObject().get("collectStatus")));
						exchangeList.setCollectCompleteDate(PaGmktDateUtil.toTimestamp(NVL(element.getAsJsonObject().get("collectCompleteDate"))));
						exchangeList.setCollectInformationsDto(element.getAsJsonObject().get("collectInformationsDto").toString());
						exchangeList.setReturnDeliveryDtos(element.getAsJsonObject().get("returnDeliveryDtos").toString());
						exchangeList.setOrderDeliveryStatusLabel(NVL(element.getAsJsonObject().get("orderDeliveryStatusLabel")));
						exchangeList.setExchangeStatusLabel(NVL(element.getAsJsonObject().get("exchangeStatusLabel")));
						exchangeList.setReferTypeLabel(NVL(element.getAsJsonObject().get("referTypeLabel")));
						exchangeList.setFaultTypeLabel(NVL(element.getAsJsonObject().get("faultTypeLabel")));
						exchangeList.setCreatedByTypeLabel(NVL(element.getAsJsonObject().get("createdByTypeLabel")));
						exchangeList.setRejectable(NVL(element.getAsJsonObject().get("rejectable")));
						exchangeList.setModifiedByTypeLabel(NVL(element.getAsJsonObject().get("modifiedByTypeLabel")));
						exchangeList.setDeliveryInvoiceModifiable(NVL(element.getAsJsonObject().get("deliveryInvoiceModifiable")));
						exchangeList.setSuccessable(NVL(element.getAsJsonObject().get("successable")));
						exchangeListArr.add(exchangeList);
					}
					// 다음 자료 존재 시 세팅
					paramMap.put("nextToken", responseObject.get("nextToken").getAsString());
				}
				
				// TPACOPNEXCHANGELIST, TPACOPNEXCHANGEITEMLIST, TPAORDERM 등록
				paCopnExchangeService.saveExchangeListTx(exchangeListArr);
			}


		} catch (Exception e) {
			paramMap.put("code", duplicateCheck.equals("1") ? "490" : "500");
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error("[COPN-교환신청/취소 목록조회] exception: " + e.getMessage(), e);
		} finally {
			try {
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0"))
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("[COPN-교환신청/취소 목록조회] end");

			if ("RECEIPT".equals(status)) {
				orderChangeMain(request); // 교환접수 데이터 생성
			} else if ("CANCEL".equals(status) || "REJECT".equals(status)) {
				orderChangeCancelMain(request); // 교환취소 데이터 생성
			}
		}
		return new ResponseEntity<ResponseMsg>(
				new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")),
				HttpStatus.OK);
	}

	/**
	 * 쿠팡 - 교환 접수
	 *
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "쿠팡 교환 접수", notes = "쿠팡 교환 접수", httpMethod = "GET")
	@RequestMapping(value = "/order-change", method = RequestMethod.GET)
	@ResponseBody
	public void orderChangeMain(HttpServletRequest request) throws Exception {
		String prg_id = "PACOPN_ORDER_CHANGE";
		String duplicateCheck = "0";

		log.info("[COPN-교환접수] start");

		try {
			// 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated", new String[] { prg_id });

			// 교환 접수 대상 조회 : D-5 ~ D
			List<Object> orderChangeTargetList = paCopnExchangeService.selectOrderChangeTargetList();

			for (Object targetObject : orderChangeTargetList) {
				try {
					asyncController.orderChangeAsync((HashMap<String, Object>) targetObject, request);
				} catch (Exception e) {
					continue;
				}
			}

		} catch (Exception e) {
			log.error("[COPN-교환신청/취소 목록조회] exception: " + e.getMessage(), e);
		} finally {
			if (duplicateCheck.equals("0"))
				systemService.checkCloseHistoryTx("end", prg_id);
			log.info("[COPN-교환접수] end");
		}
		return;
	}

	/**
	 * 쿠팡 - 교환 취소
	 *
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "쿠팡 교환취소", notes = "쿠팡 교환취소", httpMethod = "GET")
	@RequestMapping(value = "/order-change-cancel", method = RequestMethod.GET)
	@ResponseBody
	public void orderChangeCancelMain(HttpServletRequest request) throws Exception {
		String prg_id = "PACOPN_ORDER_CHANGE_CANCEL";
		String duplicateCheck = "0";

		log.info("[COPN-교환취소] start");

		try {
			// 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated", new String[] { prg_id });

			// 교환 취소 접수 대상 조회 : D-5 ~ D
			List<Object> changeCancelTargetList = paCopnExchangeService.selectChangeCancelTargetList();

			for (Object targetObject : changeCancelTargetList) {
				try {
					asyncController.orderChangeCancelAsync((HashMap<String, Object>) targetObject, request);
				} catch (Exception e) {
					continue;
				}
			}

		} catch (Exception e) {
			log.error("[COPN-교환취소] exception: " + e.getMessage(), e);
		} finally {
			if (duplicateCheck.equals("0"))
				systemService.checkCloseHistoryTx("end", prg_id);
			log.info("[COPN-교환취소] end");
		}
		return;
	}

	/**
	 * 쿠팡 - 교환요청 거부 처리
	 *
	 * @param exchangeId
	 * @param exchangeRejectCode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "쿠팡 교환요청 거부 처리", notes = "쿠팡 교환요청 거부 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 304, message = "데이터 처리에 실패 하였습니다."),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 406, message = "교환거부 처리전 교환취소건 존재"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") })
	@RequestMapping(value = "/exchange-reject-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeRejectProc(
			@ApiParam(name = "exchangeId", value = "교환ID", defaultValue = "") @RequestParam(value = "exchangeId", required = true) String exchangeId,
			@ApiParam(name = "exchangeRejectCode", value = "거절원인코드", defaultValue = "") @RequestParam(value = "exchangeRejectCode", required = true) String exchangeRejectCode,
			@ApiParam(name = "paCode", value = "paCode", defaultValue = "") @RequestParam(value = "paCode", required = true) String paCode,
			HttpServletRequest request) throws Exception {

		ParamMap paramMap = null;
		HashMap<String, String> apiInfo = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		JsonObject requestObject = null;
		JsonObject responseObject = null;
		String paName = null;

		log.info("[COPN-교환요청 거부 처리] start");

		try {
			// API 기본정보 세팅
			paramMap = new ParamMap();
			paramMap.put("apiCode", "IF_PACOPNAPI_03_016");
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("siteGb", "PACOPN");
			paramMap.put("code", Constants.PA_COPN_SUCCESS_OK);
			paramMap.put("message", "OK");

			// API 정보 조회
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			requestObject = new JsonObject();
			requestObject.addProperty("exchangeId", exchangeId);
			if(paCode.equals(Constants.PA_COPN_BROAD_CODE)) paName = Constants.PA_BROAD;
			else paName = Constants.PA_ONLINE;
			requestObject.addProperty("vendorId", apiInfo.get(paName).split(";")[0]);
			requestObject.addProperty("exchangeRejectCode", exchangeRejectCode);
			
			log.info("[COPN-교환요청 거부 처리] API 호출");
			responseObject = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(apiInfo.get("API_URL").toString().replaceAll("#vendorId#", apiInfo.get(paName).split(";")[0]).replaceAll("#exchangeId#", exchangeId)),
					"PATCH", new GsonBuilder().create().toJson(requestObject));
			
			paramMap.put("paClaimNo", exchangeId);
			paramMap.put("paCode", paCode);
			
			log.info("[COPN-교환요청 거부 처리] API 호출 결과처리");
			if (Constants.PA_COPN_SUCCESS_OK.equals(responseObject.get("code").getAsString())) {
				if ("SUCCESS".equals(responseObject.get("data").getAsJsonObject().get("resultCode").getAsString())) {
					paramMap.put("apiResultCode", Constants.SAVE_SUCCESS);
					paramMap.put("preCancelReason", "재고부족으로 인한 교환거부처리");
				} else {
					paramMap.put("apiResultCode", Constants.SAVE_FAIL);
				}
				paramMap.put("apiResultMessage",
						responseObject.get("data").getAsJsonObject().get("resultMessage").getAsString());
			} else {
				paramMap.put("apiResultCode", Constants.SAVE_FAIL);
				paramMap.put("apiResultMessage", responseObject.get("message").getAsString());
			}
			rtnMsg = paCopnExchangeService.saveExchangeRejectProcTx(paramMap);
			
			if (!rtnMsg.equals(Constants.SAVE_SUCCESS))
				throw processMessageException(rtnMsg);

		} catch (Exception e) {
			paramMap.put("code", "500");
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			log.error("[COPN-교환요청 거부 처리] exception: " + e.getMessage(), e);
			return new ResponseEntity<ResponseMsg>(
					new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			log.info("[COPN-교환요청 거부 처리] end");
		}
		return new ResponseEntity<ResponseMsg>(
				new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 교환 회수 입고 확인
	 *
	 * @return ResponseEntity
	 * @throws Exception
	 *
	 */
	@ApiOperation(value = "쿠팡 교환요청상품 입고 확인처리", notes = "쿠팡 교환요청상품 입고 확인처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 304, message = "데이터 처리에 실패 하였습니다."),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."), @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") })
	@RequestMapping(value = "/exchange-return-confirm", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeReturnConfirm(HttpServletRequest request) throws Exception {

		ParamMap paramMap = null;
		HashMap<String, String> apiInfo = null;
		List<Map<String, Object>> changeReturnConfirmList = null;
		JsonObject responseObject = null;
		JsonObject requestObject = null;
		String duplicateCheck = "";
		int totalCnt = 0, successCnt = 0, excutedRtn = 0;

		log.info("[COPN-교환요청상품 입고 확인처리] start");

		try {
			// API 기본정보 세팅
			paramMap = new ParamMap();
			paramMap.put("apiCode", "IF_PACOPNAPI_03_015");
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate", systemService.getSysdatetimeToString());

			// 중복 실행 체크
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });

			// API 정보 조회
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

			// 대상 조회
			for(int count=0 ; count<Constants.PA_COPN_CONTRACT_CNT; count++) {
				
				if(count == 0) {
					paramMap.put("paName", Constants.PA_BROAD);
					paramMap.put("paCode", Constants.PA_COPN_BROAD_CODE);
				}
				else {
					paramMap.put("paName", Constants.PA_ONLINE);
					paramMap.put("paCode", Constants.PA_COPN_ONLINE_CODE);
				}
				changeReturnConfirmList = paCopnExchangeService.selectExchangeReturn(paramMap.getString("paCode"));
				
				totalCnt += changeReturnConfirmList.size();
				
				for (Map<String, Object> targetData : changeReturnConfirmList) {
					try {
						
						requestObject = new JsonObject();
						requestObject.addProperty("vendorId", apiInfo.get(paramMap.getString("paName")).split(";")[0]);
						requestObject.addProperty("exchangeId", targetData.get("EXCHANGE_ID").toString());
						
						log.info("[COPN-교환요청상품 입고 확인처리] API 호출");
						responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder(apiInfo.get("API_URL").toString().replaceAll("#vendorId#", apiInfo.get(paramMap.getString("paName")).split(";")[0]).replaceAll("#exchangeId#", targetData.get("EXCHANGE_ID").toString())),
								"PATCH", new GsonBuilder().create().toJson(requestObject));
						
						paramMap.put("paCode", targetData.get("PA_CODE").toString());
						paramMap.put("paOrderGb", "45");
						paramMap.put("paOrderNo", targetData.get("ORDER_ID").toString());
						paramMap.put("paOrderSeq", targetData.get("PA_ORDER_SEQ").toString());
						paramMap.put("paClaimNO", targetData.get("EXCHANGE_ID").toString());
						
						if (Constants.PA_COPN_SUCCESS_OK.equals(responseObject.get("code").getAsString())) {
							paramMap.put("apiResultCode", Constants.SAVE_SUCCESS);
							paramMap.put("apiResultMessage", "교환요청상품 입고 확인처리 성공");
							successCnt++;
						} else {
							paramMap.put("apiResultCode", Constants.SAVE_FAIL);
							paramMap.put("apiResultMessage", responseObject.get("message").getAsString());
						}
						excutedRtn = paCopnExchangeService.updateExchangeReturnConfirmTx(paramMap);
						if (excutedRtn != 1) {
							log.error("[COPN-교환요청상품 입고 확인처리] updateExchangeReturnConfirmTx Fail");
							successCnt--;
						}
					} catch (Exception e) {
						log.error("[COPN-교환요청상품 입고 확인처리] exception: " + e.getMessage(), e);
						continue;
					}
				}
			}
		} catch (ProcessException se) {
			paramMap.put("code", "1".equals(duplicateCheck) ? "490" : "500");
			paramMap.put("message",
					se.getMessage().length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
			log.error("[COPN-교환요청상품 입고 확인처리] exception: " + se.getMessage(), se);
			return new ResponseEntity<ResponseMsg>(
					new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			paramMap.put("code", "1".equals(duplicateCheck) ? "490" : "500");
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error("[COPN-교환요청상품 입고 확인처리] exception: " + e.getMessage(), e);
			return new ResponseEntity<ResponseMsg>(
					new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (!"490".equals(paramMap.getString("code")) && !"404".equals(paramMap.getString("code"))) {
					paramMap.put("code", (totalCnt == successCnt) ? Constants.PA_COPN_SUCCESS_OK : "500");
					paramMap.put("code", (totalCnt == successCnt) ? Constants.PA_COPN_SUCCESS_OK : "500");
					paramMap.put("message",
							"전체: " + totalCnt + " | 성공: " + successCnt + " | 실패: " + (totalCnt - successCnt));
				}
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);

				if (duplicateCheck.equals("0"))
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));

			} catch (Exception e) {
				log.error("ApiTracking/CloseHistory Error : " + e.getMessage());
			}
			log.info("[COPN-교환요청상품 입고 확인처리] end");
		}

		return new ResponseEntity<ResponseMsg>(
				new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	@ApiOperation(value = "교환상품 송장 업로드 처리", notes = "교환상품 송장 업로드 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."), @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") })
	@RequestMapping(value = "/exchange-slip-out-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeSlipOutProc(HttpServletRequest request,
			@ApiParam(name = "procId", required = false, value = "처리자ID", defaultValue = "COP") @RequestParam(value = "procId", required = false, defaultValue = "COP") String procId)
			throws Exception {

		ParamMap paramMap = new ParamMap();
		ParamMap subMap = new ParamMap();
		JsonObject responseObject = null;
		JsonObject requestObject = null;
		HashMap<String, String> apiInfoRetrieve = new HashMap<String, String>();
		HashMap<String, String> apiInfoSave = new HashMap<String, String>();
		String duplicateCheck = "";
		String paName = null;
		String[] apiRetrieveKeys = null;
		String[] apiSaveKeys = null;
		HashMap<String, String> exchangeMap = new HashMap<String, String>();
		
		List<Map<String, Object>> exchangeSlipOutTargetList = null;

		int totalCnt = 0, successCnt = 0, excutedRtn = 0;

		log.info("[COPN-교환상품 송장 업로드 처리] start");
		try {

			paramMap.put("procId", procId.toUpperCase());
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("apiCode", "IF_PACOPNAPI_03_014");
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			
			apiInfoRetrieve = systemService.selectPaApiInfo(paramMap);
			apiInfoRetrieve.put("apiInfo", paramMap.getString("apiCode"));

			paramMap.put("apiCode", "IF_PACOPNAPI_03_017");
			apiInfoSave = systemService.selectPaApiInfo(paramMap);
			apiInfoSave.put("apiInfo", paramMap.getString("apiCode"));

			// = 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });

			exchangeSlipOutTargetList = paCopnExchangeService.selectExchangeSlipOutTargetList();

			if (exchangeSlipOutTargetList == null || exchangeSlipOutTargetList.isEmpty()) {
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(
						new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}

			totalCnt = exchangeSlipOutTargetList.size();

			for (Map<String, Object> targetData : exchangeSlipOutTargetList) {
				log.info("[COPN-교환상품 송장 업로드 처리] API 호출 - shipmentBoxId 조회");
				// 회수건 shipmentBoxId 조회
				
				if(targetData.get("PA_CODE").toString().equals(Constants.PA_COPN_BROAD_CODE)) {
					paName = Constants.PA_BROAD;
				} else {
					paName = Constants.PA_ONLINE;
				}
				apiRetrieveKeys = apiInfoRetrieve.get(paName).split(";");
				apiSaveKeys = apiInfoRetrieve.get(paName).split(";");
				
				responseObject = ComUtil.callPaCopnAPI(apiInfoRetrieve, paName,
						new URIBuilder(apiInfoRetrieve.get("API_URL").toString().replaceAll("#vendorId#",
								apiRetrieveKeys[0]))
										.addParameter("createdAtFrom",
												DateUtil.formatConvertStringToGmarketDate(
														targetData.get("INSERT_DATE").toString(), -2))
										.addParameter("createdAtTo",
												DateUtil.formatConvertStringToGmarketDate(
														targetData.get("INSERT_DATE").toString(), +1))
										.addParameter("orderId", targetData.get("PA_ORDER_NO").toString())
										.addParameter("maxPerPage", "50"));

				exchangeMap = paCopnExchangeService.selectExchangeDetail(targetData.get("PA_CLAIM_NO").toString());
				
				if (Constants.PA_COPN_SUCCESS_OK.equals(responseObject.get("code").getAsString())) {
					for (JsonElement element : responseObject.get("data").getAsJsonArray()) {
						if (targetData.get("PA_CLAIM_NO")
								.equals(element.getAsJsonObject().get("exchangeId").getAsString())) {
							if (!element.getAsJsonObject().get("deliveryInvoiceGroupDtos").getAsJsonArray()
									.isJsonNull()) {

								requestObject = new JsonObject();
								requestObject.addProperty("exchangeId", targetData.get("PA_CLAIM_NO").toString());
								requestObject.addProperty("vendorId",
										apiSaveKeys[0]);
								requestObject.addProperty("goodsDeliveryCode", exchangeMap.get("PA_DELY_GB").toString());
								if (exchangeMap.get("SLIP_NO") == null || "".equals(exchangeMap.get("SLIP_NO").toString())
										|| !ComUtil.isNumber(exchangeMap.get("SLIP_NO").toString().trim())) {
									requestObject.addProperty("invoiceNumber", exchangeMap.get("SLIP_I_NO").toString());
								} else {
									requestObject.addProperty("invoiceNumber", exchangeMap.get("SLIP_NO").toString().trim());
								}
								requestObject.addProperty("shipmentBoxId",
										element.getAsJsonObject().get("deliveryInvoiceGroupDtos").getAsJsonArray()
												.get(0).getAsJsonObject().get("shipmentBoxId").getAsString());

								JsonArray requestArray = new JsonArray();
								requestArray.add(requestObject);

								log.info("[COPN-교환상품 송장 업로드 처리] API 호출");
								// 교환상품 송장 업로드 처리
								responseObject = ComUtil.callPaCopnAPI(apiInfoSave, paName, new URIBuilder(apiInfoSave.get("API_URL").toString()
												.replaceAll("#vendorId#",
														apiSaveKeys[0])
												.replaceAll("#exchangeId#",
														element.getAsJsonObject().get("exchangeId").getAsString())),
										"POST", new GsonBuilder().create().toJson(requestArray));

								subMap.put("paClaimNo", targetData.get("PA_CLAIM_NO"));

								log.info("[COPN-교환상품 송장 업로드 처리] API 호출 결과처리");
								if (Constants.PA_COPN_SUCCESS_OK.equals(responseObject.get("code").getAsString())) {
									if ("SUCCESS".equals(responseObject.get("data").getAsJsonObject().get("resultCode")
											.getAsString())) {
										subMap.put("apiResultCode", Constants.SAVE_SUCCESS);
										subMap.put("paDoFlag", "40");
										successCnt++;
									} else {
										subMap.put("apiResultCode", Constants.SAVE_FAIL);
									}
									subMap.put("apiResultMessage", responseObject.get("data").getAsJsonObject()
											.get("resultMessage").getAsString());
								} else {
									subMap.put("apiResultCode", Constants.SAVE_FAIL);
									subMap.put("apiResultMessage", responseObject.get("message").getAsString());
								}
								excutedRtn = paCopnExchangeService.updateExchangeCompleteResultTx(subMap);
								if (excutedRtn < 1) {
									successCnt--;
								}
							}
							break;
						}
					}
				}
			}

			if (totalCnt != successCnt) {
				paramMap.put("code", "500");
				paramMap.put("message",
						"total(success) : " + totalCnt + "(" + successCnt + ")" + paramMap.getString("message"));
				return new ResponseEntity<ResponseMsg>(
						new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			} else {
				paramMap.put("code", Constants.PA_COPN_SUCCESS_OK);
				paramMap.put("message", "OK");
			}
		} catch (Exception e) {
			paramMap.put("code", duplicateCheck.equals("1") ? "490" : "500");
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(
					new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (!"490".equals(paramMap.getString("code")) && !"404".equals(paramMap.getString("code"))) {
				paramMap.put("code", (totalCnt == successCnt) ? Constants.PA_COPN_SUCCESS_OK : "500");
				paramMap.put("message",
						"전체: " + totalCnt + " | 성공: " + successCnt + " | 실패: " + (totalCnt - successCnt));
			}
			paramMap.put("siteGb", "PACOPN");
			systemService.insertApiTrackingTx(request, paramMap);
			if (duplicateCheck.equals("0"))
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("[COPN-교환상품 송장 업로드 처리] end");
		}
		return new ResponseEntity<ResponseMsg>(
				new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "교환배송완료 처리", notes = "교환배송완료 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/exchange-complete-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeCompleteList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = null;
		
		JsonObject responseObj  = null;
		JsonArray  exchangeList = null;
		JsonObject exchange     = null;
		
		List<HashMap<String, Object>> completeList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object>       complete     = null;
		Paorderm paOrderm = null;
		
		StringBuffer buffer = new StringBuffer();
		
		String dupCheck  = "";
		String rtnMsg    = Constants.SAVE_SUCCESS;
		String paName	 = null;
		
		int totalCnt   = 0;
		int successCnt = 0;
		
		try{
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_021");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());
			
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
				
			completeList = paCopnExchangeService.selectExchangeCompleteList();
			
			totalCnt = completeList.size();
			
			if(totalCnt >= 1){
				for(int i=0; i<completeList.size(); i++){
					complete = (HashMap<String, Object>)completeList.get(i);
					
					if(complete.get("PA_CODE").toString().equals(Constants.PA_COPN_BROAD_CODE)) {
						paName = Constants.PA_BROAD;
					} else {
						paName = Constants.PA_ONLINE;
					}
					
					paramMap.put("startDateQ", DateUtil.timestampToString(DateUtil.toTimestamp(complete.get("CREATED_AT").toString() + "000000", DateUtil.DEFAULT_JAVA_DATE_FORMAT), DateUtil.COPN_T_DATETIME_FORMAT));
					paramMap.put("endDateQ"  , DateUtil.timestampToString(DateUtil.toTimestamp(complete.get("CREATED_AT").toString() + "235959", DateUtil.DEFAULT_JAVA_DATE_FORMAT), DateUtil.COPN_T_DATETIME_FORMAT));
					
					responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(
							apiInfo.get("API_URL").replaceAll("#vendorId#", apiInfo.get(paName).split(";")[0]))
					.addParameter("createdAtFrom", paramMap.getString("startDateQ"))
					.addParameter("createdAtTo"  , paramMap.getString("endDateQ"))
					.addParameter("status"       , "SUCCESS")
					.addParameter("maxPerPage"   , Constants.PA_COPN_COUNT_PER_PAGE)
					.addParameter("orderId"      , complete.get("PA_ORDER_NO").toString()));
					
					if("200".equals(responseObj.get("code").getAsString())){
						exchangeList = responseObj.get("data").getAsJsonArray();
						
						if(exchangeList.size() > 0){
							exchange = exchangeList.get(0).getAsJsonObject();
							
							paOrderm = new Paorderm();
							
							paOrderm.setPaOrderNo       (NVL(exchange.get("orderId")));
							paOrderm.setPaClaimNo       (NVL(exchange.get("exchangeId")));
							paOrderm.setApiResultCode   (Constants.SAVE_SUCCESS);
							paOrderm.setApiResultMessage("교환배송완료");
							
							rtnMsg = paCopnExchangeService.updateExchangeDeliveryCompleteTx(paOrderm);
							
							if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
								paramMap.put("code"   , "200");
								paramMap.put("message", "OK");
							}
						}else{
							paramMap.put("code"   , "500");
							paramMap.put("message", responseObj.get("message").toString());
						}
					}else{
						paramMap.put("code"   , "500");
						paramMap.put("message", responseObj.get("message").toString());
						log.info(responseObj.get("message").toString());
					}
					
					if("200".equals(paramMap.getString("code"))){
						successCnt++;
					}else{
						buffer.append((buffer.length() == 0 ? "PA_CLAIM_NO: " : ", ") + complete.get("PA_CLAIM_NO").toString() + " | " + responseObj.get("message").toString());
					}
					
				}
			}else{
				paramMap.put("code"     , "404");
				paramMap.put("message"  , getMessage("pa.no_return_data"));
			}
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(dupCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(dupCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}finally{
			try{
				if(!"490".equals(paramMap.getString("code")) && !"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , (totalCnt == successCnt) ? "200" : "500");
					paramMap.put("message", "전체: " + totalCnt + " | 성공: " + successCnt + " | 실패: " + buffer.toString());
				}
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "요청일자별 교환철회데이터 조회", notes = "요청일자별 교환철회데이터 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/exchange-withdraw-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeWithdrawList(HttpServletRequest request) throws Exception{
		
		ParamMap paramMap = null;
		HashMap<String, String> apiInfo = null;
		String duplicateCheck = "0";

		ArrayList<Pacopnexchangelist> exchangeListArr = null;
		ArrayList<Pacopnexchangeitemlist> exchangeItemListArr = null;
		Pacopnexchangelist exchangeList = null;
		Pacopnexchangeitemlist exchangeItemList = null;
		JsonObject responseObject = null;
		JsonObject tempObject = null;
		HashMap<String, String> exchangemMap = new HashMap<String, String>();

		log.info("[COPN-요청일자별 교환철회데이터 조회] start");

		try {
			// API 기본정보 세팅
			paramMap = new ParamMap();
			paramMap.put("code", Constants.PA_COPN_SUCCESS_OK);
			paramMap.put("message", "OK");

			paramMap.put("apiCode", "IF_PACOPNAPI_03_024");
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("maxPerPage", Constants.PA_COPN_COUNT_PER_PAGE);

			// 중복 실행 체크
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });

			// API 정보 조회
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

			//대상 날짜 조회
			List<String> dateList = paCopnExchangeService.selectExchangeCreatedDate();
			
			for(String date : dateList) { //날짜별로 취소/거부 조회
				for(int i=0; i<2; i++) {					

					String fromDate = DateUtil.timestampToString(
							DateUtil.toTimestamp(date + "000000", DateUtil.DEFAULT_JAVA_DATE_FORMAT),
							DateUtil.GMKT_DATE_FORMAT);
					String toDate = DateUtil.timestampToString(
							DateUtil.toTimestamp(date + "235959", DateUtil.DEFAULT_JAVA_DATE_FORMAT),
							DateUtil.GMKT_DATE_FORMAT);
					
					String status = (i==0) ? "CANCEL" : "REJECT";
					
					for(int count=0 ; count<Constants.PA_COPN_CONTRACT_CNT; count++) {
						
						paramMap.put("nextToken", "init");
						
						if(count == 0) paramMap.put("paName", Constants.PA_BROAD);
						else paramMap.put("paName", Constants.PA_ONLINE);
						
						exchangeListArr = new ArrayList<Pacopnexchangelist>();
						while (!paramMap.getString("nextToken").isEmpty()) {
							
							log.info("[COPN-요청일자별 교환철회데이터 조회] API 호출 status : " + status);
							responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"),
									new URIBuilder(apiInfo.get("API_URL").toString().replaceAll("#vendorId#",
											apiInfo.get(paramMap.getString("paName")).split(";")[0])).addParameter("createdAtFrom", fromDate)
									.addParameter("createdAtTo", toDate).addParameter("status", status)
									.addParameter("nextToken",
											"init".equals(paramMap.getString("nextToken")) ? ""
													: paramMap.getString("nextToken"))
									.addParameter("maxPerPage", paramMap.getString("maxPerPage")));
							
							log.info("[COPN-요청일자별 교환철회데이터 조회] API 호출 결과처리");
							paramMap.put("code"   , "200".equals(responseObject.get("code").getAsString()) ? "200" : "500");
							paramMap.put("message", responseObject.get("message").getAsString());
							
							if (!Constants.PA_COPN_SUCCESS_OK.equals(responseObject.get("code").getAsString())) {
								break;
							}
							
							if (responseObject.get("data").getAsJsonArray().size() == 0) {
								paramMap.put("code", "404");
								paramMap.put("message", getMessage("errors.no.select"));
								break;
							}
							
							for (JsonElement element : responseObject.get("data").getAsJsonArray()) {
								exchangeList = new Pacopnexchangelist();
								exchangeList.setBlank();
								exchangeList.setPaOrderGb("41");
								exchangeList.setExchangeId(NVL(element.getAsJsonObject().get("exchangeId")));
								exchangeList.setOrderId(NVL(element.getAsJsonObject().get("orderId")));
								exchangeList.setVendorId(NVL(element.getAsJsonObject().get("vendorId")));
								exchangeList.setOrderDeliveryStatusCode(NVL(element.getAsJsonObject().get("orderDeliveryStatusCode")));
								exchangeList.setExchangeStatus(NVL(element.getAsJsonObject().get("exchangeStatus")));
								exchangeList.setReferType(NVL(element.getAsJsonObject().get("referType")));
								exchangeList.setFaultType(NVL(element.getAsJsonObject().get("faultType")));
								exchangeList.setExchangeAmount(NVL(element.getAsJsonObject().get("exchangeAmount")));
								exchangeList.setReasonCode(NVL(element.getAsJsonObject().get("reasonCode")));
								exchangeList.setReasonCodeText(NVL(element.getAsJsonObject().get("reasonCodeText")));
								exchangeList.setReasonEtcDetail(NVL(element.getAsJsonObject().get("reasonEtcDetail")));
								exchangeList.setCancelReason(NVL(element.getAsJsonObject().get("cancelReason")));
								exchangeList.setCreatedByType(NVL(element.getAsJsonObject().get("createdByType")));
								exchangeList.setCreatedAt(PaGmktDateUtil.toTimestamp(NVL(element.getAsJsonObject().get("createdAt"))));
								exchangeList.setModifiedByType(NVL(element.getAsJsonObject().get("modifiedByType")));
								exchangeList.setModifiedAt(PaGmktDateUtil.toTimestamp(NVL(element.getAsJsonObject().get("modifiedAt"))));
								
								exchangeItemListArr = new ArrayList<Pacopnexchangeitemlist>();
								paramMap.put("orderId", exchangeList.getOrderId());
								
								for (JsonElement subElement : element.getAsJsonObject().get("exchangeItemDtoV1s")
										.getAsJsonArray()) {
									exchangeItemList = new Pacopnexchangeitemlist();
									exchangeItemList.setBlank();
									exchangeItemList.setPaOrderGb(exchangeList.getPaOrderGb());
									exchangeItemList.setExchangeId(exchangeList.getExchangeId());
									exchangeItemList.setOrderId(exchangeList.getOrderId());
									exchangeItemList.setExchangeItemId(NVL(subElement.getAsJsonObject().get("exchangeItemId")));
									exchangeItemList.setOrderItemId(NVL(subElement.getAsJsonObject().get("orderItemId")));
									exchangeItemList.setOrderItemUnitPrice(NVL(subElement.getAsJsonObject().get("orderItemUnitPrice")));
									exchangeItemList.setOrderItemName(NVL(subElement.getAsJsonObject().get("orderItemName")));
									exchangeItemList.setOrderPackageId(NVL(subElement.getAsJsonObject().get("orderPackageId")));
									exchangeItemList.setOrderPackageName(NVL(subElement.getAsJsonObject().get("orderPackageName")));
									exchangeItemList.setTargetItemId(NVL(subElement.getAsJsonObject().get("targetItemId")));
									exchangeItemList.setTargetItemUnitPrice(NVL(subElement.getAsJsonObject().get("targetItemUnitPrice")));
									exchangeItemList.setTargetItemName(NVL(subElement.getAsJsonObject().get("targetItemName")));
									exchangeItemList.setTargetPackageId(NVL(subElement.getAsJsonObject().get("targetPackageId")));
									exchangeItemList.setTargetPackageName(NVL(subElement.getAsJsonObject().get("targetPackageName")));
									exchangeItemList.setQuantity(subElement.getAsJsonObject().get("quantity").getAsLong());
									exchangeItemList.setOrderItemDeliveryComplete(NVL(subElement.getAsJsonObject().get("orderItemDeliveryComplete")));
									exchangeItemList.setOrderItemReturnComplete(NVL(subElement.getAsJsonObject().get("orderItemReturnComplete")));
									exchangeItemList.setTargetItemDeliveryComplete(NVL(subElement.getAsJsonObject().get("targetItemDeliveryComplete")));
									exchangeItemList.setCreatedAt(PaGmktDateUtil.toTimestamp(NVL(subElement.getAsJsonObject().get("createdAt"))));
									exchangeItemList.setModifiedAt(PaGmktDateUtil.toTimestamp(NVL(subElement.getAsJsonObject().get("modifiedAt"))));
									exchangeItemList.setOriginalShipmentBoxId(NVL(subElement.getAsJsonObject().get("originalShipmentBoxId")));
									
									paramMap.put("orderId", exchangeList.getOrderId());
									paramMap.put("vendorItemId", exchangeItemList.getOrderItemId());
									//paramMap.put("shipmentBoxId", exchangeItemList.getOriginalShipmentBoxId());
									exchangemMap = paCopnExchangeService.selectOrgShipmentBoxId(paramMap);
									exchangeItemList.setItemSeq(exchangemMap.get("ITEM_SEQ").toString());
									
									exchangeItemListArr.add(exchangeItemList);
								}
								exchangeList.setExchangeItemDtoV1s(exchangeItemListArr.toArray(new Pacopnexchangeitemlist[0]));
								
								tempObject = element.getAsJsonObject().get("exchangeAddressDtoV1").getAsJsonObject();
								exchangeList.setEadExchangeAddressId(NVL(tempObject.get("exchangeAddressId")));
								exchangeList.setEadReturnCustomerName(NVL(tempObject.get("returnCustomerName")));
								exchangeList.setEadReturnAddressZipCode(NVL(tempObject.get("returnAddressZipCode")));
								exchangeList.setEadReturnAddress(NVL(tempObject.get("returnAddress")));
								exchangeList.setEadReturnAddressDetail(NVL(tempObject.get("returnAddressDetail")));
								exchangeList.setEadReturnPhone(NVL(tempObject.get("returnPhone")));
								exchangeList.setEadReturnMobile(NVL(tempObject.get("returnMobile")));
								exchangeList.setEadReturnMemo(NVL(tempObject.get("returnMemo")));
								exchangeList.setEadDeliveryCustomerName(NVL(tempObject.get("deliveryCustomerName")));
								exchangeList.setEadDeliveryAddressZipCode(NVL(tempObject.get("deliveryAddressZipCode")));
								exchangeList.setEadDeliveryAddress(NVL(tempObject.get("deliveryAddress")));
								exchangeList.setEadDeliveryAddressDetail(NVL(tempObject.get("deliveryAddressDetail")));
								exchangeList.setEadDeliveryPhone(NVL(tempObject.get("deliveryPhone")));
								exchangeList.setEadDeliveryMobile(NVL(tempObject.get("deliveryMobile")));
								exchangeList.setEadDeliveryMemo(NVL(tempObject.get("deliveryMemo")));
								exchangeList.setEadCreatedAt(PaGmktDateUtil.toTimestamp(NVL(tempObject.get("createdAt"))));
								exchangeList.setEadModifiedAt(PaGmktDateUtil.toTimestamp(NVL(tempObject.get("modifiedAt"))));
								exchangeList.setEadExchangeId(NVL(tempObject.get("exchangeId")));
								exchangeList.setDeliveryInvoiceGroupDtos(element.getAsJsonObject().get("deliveryInvoiceGroupDtos").toString());
								exchangeList.setDeliveryStatus(NVL(element.getAsJsonObject().get("deliveryStatus")));
								exchangeList.setCollectStatus(NVL(element.getAsJsonObject().get("collectStatus")));
								exchangeList.setCollectCompleteDate(PaGmktDateUtil.toTimestamp(NVL(element.getAsJsonObject().get("collectCompleteDate"))));
								exchangeList.setCollectInformationsDto(element.getAsJsonObject().get("collectInformationsDto").toString());
								exchangeList.setReturnDeliveryDtos(element.getAsJsonObject().get("returnDeliveryDtos").toString());
								exchangeList.setOrderDeliveryStatusLabel(NVL(element.getAsJsonObject().get("orderDeliveryStatusLabel")));
								exchangeList.setExchangeStatusLabel(NVL(element.getAsJsonObject().get("exchangeStatusLabel")));
								exchangeList.setReferTypeLabel(NVL(element.getAsJsonObject().get("referTypeLabel")));
								exchangeList.setFaultTypeLabel(NVL(element.getAsJsonObject().get("faultTypeLabel")));
								exchangeList.setCreatedByTypeLabel(NVL(element.getAsJsonObject().get("createdByTypeLabel")));
								exchangeList.setRejectable(NVL(element.getAsJsonObject().get("rejectable")));
								exchangeList.setModifiedByTypeLabel(NVL(element.getAsJsonObject().get("modifiedByTypeLabel")));
								exchangeList.setDeliveryInvoiceModifiable(NVL(element.getAsJsonObject().get("deliveryInvoiceModifiable")));
								exchangeList.setSuccessable(NVL(element.getAsJsonObject().get("successable")));
								exchangeListArr.add(exchangeList);
							}
							// 다음 자료 존재 시 세팅
							paramMap.put("nextToken", responseObject.get("nextToken").getAsString());
						}
						
						// TPACOPNEXCHANGELIST, TPACOPNEXCHANGEITEMLIST, TPAORDERM 등록
						paCopnExchangeService.saveExchangeListTx(exchangeListArr);
					}
				}
			}

		} catch (Exception e) {
			paramMap.put("code", duplicateCheck.equals("1") ? "490" : "500");
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error("[COPN-요청일자별 교환철회데이터 조회] exception: " + e.getMessage(), e);
		} finally {
			try {
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0"))
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("[COPN-요청일자별 교환철회데이터 조회] end");

		
			orderChangeCancelMain(request); // 교환취소 데이터 생성
		
		}
		return new ResponseEntity<ResponseMsg>(
				new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")),
				HttpStatus.OK);
	}
	
	private String NVL(JsonElement jsonElement) {
		return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
	}
}
