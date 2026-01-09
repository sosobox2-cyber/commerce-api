package com.cware.api.pawemp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import com.cware.netshopping.domain.PaWempDeliveryVO;
import com.cware.netshopping.domain.PaWempTargetVO;
import com.cware.netshopping.domain.PawemporderlistVO;
import com.cware.netshopping.domain.model.PaWempClaimItemList;
import com.cware.netshopping.domain.model.PaWempClaimList;
import com.cware.netshopping.domain.model.PaWempOrderItemList;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pawemp.claim.model.Claim;
import com.cware.netshopping.pawemp.claim.model.GetClaimListRequest;
import com.cware.netshopping.pawemp.claim.model.GetClaimListResponse;
import com.cware.netshopping.pawemp.claim.service.PaWempClaimService;
import com.cware.netshopping.pawemp.common.enums.WempCode;
import com.cware.netshopping.pawemp.common.model.OrderOption;
import com.cware.netshopping.pawemp.common.model.ReturnData;
import com.cware.netshopping.pawemp.common.model.SetParcelDelivery;
import com.cware.netshopping.pawemp.common.model.ShipAddress;
import com.cware.netshopping.pawemp.common.service.PaWempApiService;
import com.cware.netshopping.pawemp.order.model.Bundle;
import com.cware.netshopping.pawemp.order.model.GetOrderInfoRequest;
import com.cware.netshopping.pawemp.order.model.GetOrderInfoResponse;
import com.cware.netshopping.pawemp.order.model.GetOrderListRequest;
import com.cware.netshopping.pawemp.order.model.GetOrderListResponse;
import com.cware.netshopping.pawemp.order.model.OrderDelivery;
import com.cware.netshopping.pawemp.order.model.OrderProduct;
import com.cware.netshopping.pawemp.order.model.SetOrderConfirmRequest;
import com.cware.netshopping.pawemp.order.service.PaWempOrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@Api(value="/pawemp/order", description="주문 배송 취소")
@Controller("com.cware.api.pawemp.PaWempOrderController")
@RequestMapping(value="/pawemp/order")
public class PaWempOrderController extends AbstractController{

	@Resource(name = "pawemp.order.paWempOrderService")
	private PaWempOrderService paWempOrderService;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pawemp.common.paWempApiService")
	private PaWempApiService paWempApiService;
	
	@Resource(name = "pawemp.claim.paWempClaimService")
	private PaWempClaimService paWempClaimService;
	
	@Resource(name = "com.cware.api.pawemp.PaWempAsyncController")
	private PaWempAsyncController asyncController;

	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paOrderService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	/**
	 * 위메프 주문내역 조회 및 SK스토아 주문생성 메소드 호출 IF_PAWEMPAPI_03_001
	 * @param request
	 * @throws Exception
	 */
	@ApiOperation(value = "위메프 주문접수", notes = "위메프 주문접수", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 405, message = "처리건수와 성공건수가 서로 상이합니다. 처리건수:{0} / 성공건수:{1}"),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/order-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderList(HttpServletRequest request,
			@ApiParam(name = "fromDate", required = false, value = "시작일자") @RequestParam(value = "fromDate", required = false) String fromDate) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_001";
		
		log.info("===== 위메프 order-list (주문요청조회) Start =====");
		String dateTime = systemService.getSysdatetimeToString();

		String startDateTime = paWempApiService.makeDateTimeStart(fromDate);
		String endDateTime   = paWempApiService.makeDateTimeEnd(fromDate);
		
		log.info("01.API 기본정보 세팅");
		paramMap.put("apiCode"   , apiCode);
		paramMap.put("procId"    , Constants.PA_WEMP_PROC_ID);
		paramMap.put("broadCode" , Constants.PA_WEMP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
		paramMap.put("startDate" , dateTime);
		paramMap.put("startDateQ", startDateTime);
		paramMap.put("endDateQ"  , endDateTime);
		
		try {
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			log.info("03.API정보 조회");
			paramMap.put("code"   , "200");
			paramMap.put("message", "OK");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			for(int i=0; i<2; i++){
				paramMap.put("status", (i == 0) ? "NEW" : "CONFIRM"); // NEW: 결제완료, CONFIRM: 상품준비중
				for(int count=0 ; count<Constants.PA_WEMP_CONTRACT_CNT; count++) {
					if(count == 0) {
						paramMap.put("paName", Constants.PA_BROAD);
						paramMap.put("paCode", Constants.PA_WEMP_BROAD_CODE);
					} else {
						paramMap.put("paName", Constants.PA_ONLINE);
						paramMap.put("paCode", Constants.PA_WEMP_ONLINE_CODE);
					}
					procOrderListDay(request, paramMap, apiInfo);
				}
			}
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(duplicateCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		} finally {
			try {
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(duplicateCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			
			log.info("===== 위메프 order-list (주문요청조회) End =====");
			orderInputMain(request);
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 주문 조회 및 저장
	 * 목록 조회 > 상품준비중 처리
	 * @param request
	 * @param paramMap
	 * @param apiInfo
	 * @return
	 * @throws Exception
	 */
	private ParamMap procOrderListDay(HttpServletRequest request, ParamMap paramMap, HashMap<String, String> apiInfo) throws Exception{
		ResponseEntity<?> responseEntity = null;
		
		try{
			GetOrderListRequest requestObject = new GetOrderListRequest();
			requestObject.setFromDate(paramMap.getString("startDateQ"));
			requestObject.setToDate(paramMap.getString("endDateQ"));
			requestObject.setType(paramMap.getString("status"));
			requestObject.setSearchDateType(paramMap.getString("status"));
			GetOrderListResponse bundleObject = (GetOrderListResponse)paWempApiService.callWApiObejct(apiInfo, "POST", requestObject, GetOrderListResponse.class, paramMap.getString("paName"));
			
			if(bundleObject != null && CollectionUtils.isNotEmpty(bundleObject.getBundle())) {
				for(Bundle bundle: bundleObject.getBundle()) {
					if("CONFIRM".equals(paramMap.getString("status"))){
						savePaOrderList(paramMap, bundle);
						
						paramMap.put("code"   , "200");
						paramMap.put("message", "OK");
					} else {
						log.info("03. 주문확인 전송");
						responseEntity = shippingReadyProc(request, bundle.getBundleNo(), paramMap.getString("paName"));
						
						if("200".equals(PropertyUtils.describe(responseEntity.getBody()).get("code"))){
							savePaOrderList(paramMap, bundle);
							
							paramMap.put("code"   , "200");
							paramMap.put("message", "OK");
						}else{
							paramMap.put("code"     , "500");
							paramMap.put("message"  , "상품준비중처리 실패");
							paramMap.put("nextToken", "");
						}
					}
				}
			} else {
				paramMap.put("code"   , "404");
				paramMap.put("message", getMessage("pa.no_return_data"));
			}
		
		}catch(Exception e){
			paramMap.put("code"   , "500");
			paramMap.put("message", e.getMessage());
			return paramMap;
		}
		
		return paramMap;
	}
	
	@ApiOperation(value = "상품준비중처리", notes="상품준비중처리")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/shipping-ready-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> shippingReadyProc(HttpServletRequest request,
			@ApiParam(name = "bundleNo", required = true, value = "배송번호") @RequestParam(value = "bundleNo", required = true) long bundleNo,
			@ApiParam(name = "paName"  , required = true, value = "paName") @RequestParam(value = "paName"  , required = true) String paName) throws Exception{
		HashMap<String, String> apiInfo = null;
		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAWEMPAPI_03_005";
		String dupCheck = null;
		
		log.info("01.API 기본정보 세팅");
		String dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode"   , apiCode);
		paramMap.put("procId"    , Constants.PA_WEMP_PROC_ID);
		paramMap.put("broadCode" , Constants.PA_WEMP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
		paramMap.put("startDate" , dateTime);

		try{
			log.info("[상품준비중처리] 02.API 중복실행 검사");
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			SetOrderConfirmRequest confirmReq = new SetOrderConfirmRequest();
			confirmReq.setBundleNo(bundleNo);
			ReturnData returnData = (ReturnData)paWempApiService.callWApiObejct(apiInfo, "POST", confirmReq, ReturnData.class, paName);
			
			if(returnData.getReturnKey() == 1) { //성공
				log.info("주문확인 API 전송성공 결과 returnKey:1, returnMsg:"+returnData.getReturnMsg());
				paramMap.put("code"   , "200");
				paramMap.put("message", "OK");
			}else{
				log.info("주문확인 API 전송실패 결과 returnKey:"+returnData.getReturnKey()+", returnMsg:"+returnData.getReturnMsg());
				paramMap.put("code"   , "500");
				paramMap.put("message", returnData.getReturnMsg());
			}
			
		}catch(Exception e){
			paramMap.put("code"   , "500");
			paramMap.put("message", e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try{
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
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
	
	/**
	 * 주문 저장(중계 테이블)
	 * @param paramMap
	 * @param order
	 * @return
	 */
	private String savePaOrderList(ParamMap paramMap, Bundle bundle){
		PawemporderlistVO orderList = null;
		ShipAddress shipAddress = null;
		List<PaWempOrderItemList> itemList = null;
		long paOrderSeq = 1;
		OrderProduct orderProduct = null;
		PaWempOrderItemList orderItem = null;
		List<OrderProduct> productList = null;
		List<OrderOption> orderOptionList = null;
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		try {
			orderList = new PawemporderlistVO();
			
			orderList.setPaCode(paramMap.getString("paCode"));
			orderList.setPaOrderNo(Long.toString(bundle.getPurchaseNo()));
			orderList.setPaShipNo(Long.toString(bundle.getBundleNo()));
			orderList.setOrderDate(DateUtil.toTimestamp(bundle.getOrderDate(),DateUtil.WEMP_DATETIME_FORMAT));
			orderList.setPayDate(DateUtil.toTimestamp(bundle.getPayDate(),DateUtil.WEMP_DATETIME_FORMAT));
			orderList.setOriginShipDate(DateUtil.toTimestamp(bundle.getOriginShipDate(),DateUtil.WEMP_DATETIME_FORMAT));
			orderList.setOrderConfirmDate(DateUtil.toTimestamp(bundle.getOrderConfirmDate(),DateUtil.WEMP_DATETIME_FORMAT));
			orderList.setOrderShipDate(DateUtil.toTimestamp(bundle.getOrderShippingDate(),DateUtil.WEMP_DATETIME_FORMAT));
			orderList.setShipCompleteDate(DateUtil.toTimestamp(bundle.getShipCompleteDate(),DateUtil.WEMP_DATETIME_FORMAT));
			orderList.setBuyerName(bundle.getBuyerName());
			orderList.setBuyerPhone(bundle.getBuyerPhone());
			orderList.setShipPrice(bundle.getShipPrice());
			if(bundle.getPrepayment().equals("선결제")){
				orderList.setPrepayType("선불");
			}else{
				orderList.setPrepayType(bundle.getPrepayment());
			}
			orderList.setShipType(bundle.getShipType());

			OrderDelivery delivery = bundle.getDelivery();
			orderList.setDeliveryStatus(delivery.getShipStatus());
			if(delivery.getShipMethod().equals("택배배송") || delivery.getShipMethod().equals("직접배송")){
				orderList.setDeliveryMethod("일반"+delivery.getShipMethod());
			}else{
				orderList.setDeliveryMethod(delivery.getShipMethod().replace("-", ""));
			}
			
			if(StringUtils.isBlank(delivery.getShipMethodMessage())) {
				orderList.setDeliveryMethodMessage("");
			} else {
				orderList.setDeliveryMethodMessage(ComUtil.subStringBytes(delivery.getShipMethodMessage(), 198));
			}
			if(StringUtils.isNotBlank(delivery.getScheduleShipDate())) {
				orderList.setScheduleShipDate(DateUtil.toTimestamp(delivery.getScheduleShipDate(),DateUtil.WEMP_DATE_FORMAT));
			}
	
			//Code가 아닌 이름으로 넘어올 경우 코드로 입력필요
			//확인필요
			//if(!StringUtils.isBlank(delivery.getParcelCompany())) {
			//	HashMap<String, String> delyGbMap = paWempCommonDAO.selectDelyGb(StringUtils.trim(delivery.getParcelCompany()));
			//	if(delyGbMap == null || StringUtils.isBlank(delyGbMap.get("PA_DELY_GB"))) {
					orderList.setDelyComp(ComUtil.subStringBytes(delivery.getParcelCompany(), 5));  //못찾았으면 혹시 코드로 온걸수도 있으므로..
			//	} else {
			//		orderList.setDelyComp(delyGbMap.get("PA_DELY_GB").toString());
			//	}
			//}
			
			orderList.setInvoiceNo(delivery.getInvoiceNo());
			orderList.setReceiverName(delivery.getName());
			orderList.setReceiverPhone(delivery.getPhone());
			orderList.setCustomsPin(delivery.getCustomsPin());
			
			shipAddress = delivery.getShipAddress();
			orderList.setZipcode(shipAddress.getZipcode());
			orderList.setBaseAddr(ComUtil.subStringBytes(shipAddress.getAddrFixed(), 498));
			orderList.setDetailAddr(ComUtil.subStringBytes(shipAddress.getAddrDetail(), 498));
			orderList.setDeliveryMessage(ComUtil.subStringBytes(shipAddress.getMessage(), 98));
			orderList.setInsertId(Constants.PA_WEMP_PROC_ID);
			orderList.setModifyId(Constants.PA_WEMP_PROC_ID);
			
			itemList = new ArrayList<PaWempOrderItemList>();
			
			productList = bundle.getOrderProduct();
			if(CollectionUtils.isEmpty(productList)) {
				return null;
			}
			
			for(int i=0; i < productList.size(); i++ ){
				orderProduct = productList.get(i); // 상품준비중(confirm) 주문목록
				
				orderOptionList = orderProduct.getOrderOption();
				//옵션 갯수만큼 DB 테이블 정보 생성
				int cnt = 0;
				for(OrderOption optionItem : orderOptionList) {
					orderItem = new PaWempOrderItemList();
					
					orderItem.setPaOrderNo(Long.toString(bundle.getPurchaseNo()));
					orderItem.setPaShipNo(Long.toString(bundle.getBundleNo()));
					orderItem.setPaOrderSeq(Long.toString(paOrderSeq));
					paOrderSeq++;
					orderItem.setOrderProductNo(Long.toString(orderProduct.getOrderNo()));
					orderItem.setProductNo(Long.toString(orderProduct.getProductNo()));
					orderItem.setProductName(orderProduct.getProductName());
					orderItem.setProductPrice(orderProduct.getProductPrice());
					orderItem.setProductQty(orderProduct.getProductQty());
					orderItem.setProductOriginPrice(orderProduct.getProductOriginPrice()); 
					orderItem.setProductCommissionPrice(orderProduct.getProductCommissionPrice()); 
					
					//동일 상품의 001 단품에 프로모션 금액 적용
					if(cnt == 0) {
						orderItem.setWmpChargeDiscount(orderProduct.getWmpChargeDiscountPrice()); 						
						orderItem.setSellerChargeDiscount(orderProduct.getSellerChargeDiscountPrice()); 
						orderItem.setCardChargeDiscount(orderProduct.getCardChargeDiscountPrice()); 
					}else {
						orderItem.setWmpChargeDiscount(0);
						orderItem.setSellerChargeDiscount(0); 
						orderItem.setCardChargeDiscount(0);
					}
					
					orderItem.setGoodsCode(StringUtils.trim(orderProduct.getSellerProductCode()));		
					orderItem.setInsertId(Constants.PA_WEMP_PROC_ID);
					orderItem.setModifyId(Constants.PA_WEMP_PROC_ID);
					orderItem.setOrderOptionNo(Long.toString(optionItem.getOrderOptionNo()));
					orderItem.setOptionNo(Long.toString(optionItem.getOptionNo()));
					orderItem.setOptionName(optionItem.getOptionName());
					orderItem.setOptionQty(optionItem.getOptionQty());
					if(StringUtils.isBlank(optionItem.getSellerOptionCode())) {
						orderItem.setGoodsdtCode("001");
					} else {
						orderItem.setGoodsdtCode(StringUtils.trim(optionItem.getSellerOptionCode()));
					}
					itemList.add(orderItem);
					
					cnt++;
				}
			}
				
			rtnMsg = paWempOrderService.savePaWempOrderTx(orderList, itemList);
			
		}catch(Exception e){
			paramMap.put("code"   , "500");
			paramMap.put("message", e.getMessage());
			e.printStackTrace();
			return rtnMsg;
		}
		return rtnMsg;
	}
	
	//한 주문에 동일 상품의 단품 001 / 002 주문 시 묶음배송처리되며 부분 발송처리 불가.
	//첫 번째 상품 출고 시 발송처리 되며 위메프 상태는 두 상품 모두 발송처리 됨.
	//만약 두번째 상품이 발송 불가할 경우 위메프 관리자 페이지에서 처리 필요. TSDCHECK를 통해 나가고 있지 않은 상품이 있는지 체크 예정.
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "발송처리", notes = "발송처리 (배송중 처리)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/slip-out-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> slipOutProc(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		List<Object> targetList = null;
		HashMap<String, Object> targetVo = null;
		List<Object> targetItem = null;
		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_004";
		
		HashMap<String, Object> deliveryVo = null;
		int totalCnt = 0;
		int procCount = 0;
		StringBuffer sb = new StringBuffer();
		String msg = "";
		int excuteCnt = 0;
		String deliveryStatus = "03"; // [O692] 03:배송중, 04:배송완료
		String representSlipNo = "";
		String representDelyGb = "";
		
		
		log.info("===== 위메프 slip-out-proc (배송중 처리) Start=====");
		
		log.info("01.API 기본정보 세팅");
		String dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode"   , apiCode);
		paramMap.put("procId"    , Constants.PA_WEMP_PROC_ID);
	    paramMap.put("broadCode" , Constants.PA_WEMP_BROAD_CODE);
	    paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
	    paramMap.put("startDate" , dateTime);
		
		try {
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

			log.info("04.발송처리 대상 조회");
			targetList = paWempOrderService.selectSlipOutProcList();
			totalCnt = targetList.size();
			
			if(totalCnt < 1) {
				log.info("slipOutProc no data found. skip");
				paramMap.put("code","404");
				paramMap.put("message", getMessage("errors.no.select"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			for(int i=0; i<targetList.size(); i++) {
				targetVo = (HashMap<String, Object>)targetList.get(i);
				
				try {
					targetItem = paWempOrderService.selectSlipOutProcDtList(targetVo.get("PA_SHIP_NO").toString());
					if(targetItem == null || targetItem.size() < 1) {
						log.info("slipOutProc not found list paShipNo:"+targetVo.get("PA_SHIP_NO").toString());
						throw processException(targetVo.get("PA_SHIP_NO").toString() + " 출고 대상 조회 실패");
					}
					for(int k=0; k<targetItem.size(); k++) {
						deliveryVo = (HashMap<String, Object>) targetItem.get(k);
						//분리배송 관련된 답변 들은 후 다시 체크 필요. 성공 업데이트 처리 시 mapping_seq로 할지 pa_ship_no 기준으로 할지
						ParamMap procResultMap = paWempOrderService.slipOutProc(deliveryVo, apiInfo);
						
						//성공으로 update 처리
						if(StringUtils.equals("1", procResultMap.getString("result_code"))) {
							
							excuteCnt = paWempOrderService.updateSlipOutProcTx(deliveryVo);
	    					if(excuteCnt < 1) throw processException("errors.process", new String[] { "TPAORDERM SUCCESS-UPDATE 오류 발생" });
	    					
	    					if(StringUtils.equals("OK", procResultMap.getString("result_text"))) {
	    						procCount++;
	    						//송장변경 체크를 하기 위해 TPAWEMPORDERLIST에 송장정보 반영
	    						paWempOrderService.updateInvoiceInfoTx(deliveryVo, deliveryStatus);
	    						
								representDelyGb = deliveryVo.get("PA_DELY_GB").toString(); //API를 구지 ORDER_SEQ 단위인 DT리스트로 분리할 필요 있었나...
								representSlipNo = deliveryVo.get("INVOICE_NO").toString();
	    					}
	    					
	    					//INSERT TPASLIPINFO - 연동 송장번호 이력관리..
	    					deliveryVo.put("TRANS_PA_DELY_GB", representDelyGb);
	    					deliveryVo.put("TRANS_INVOICE_NO", representSlipNo);
	    					deliveryVo.put("REMARK1_V"		 , "발송완료");
	    					deliveryVo.put("TRANS_YN"		 , "1");
	    					deliveryVo.put("PA_GROUP_CODE"	 , "06"); 
	    					paOrderService.insertTpaSlipInfoTx(deliveryVo);
	    					 
						} else {
							//실패로 update 처리
							excuteCnt = paWempOrderService.updateSlipOutProcFailTx(deliveryVo, procResultMap.getString("result_text"));
	    					if(excuteCnt < 1) {
	    						throw processException("errors.process", new String[] { "TPAORDERM FAIL-UPDATE 오류 발생" });
	    					}
						}
					}
					representDelyGb = "";
					representSlipNo = "";
					
				} catch (Exception e) {
					log.info(targetVo.get("PA_SHIP_NO").toString() + ": 발송처리 fail - " +e.getMessage() + "|");
					sb.append(targetVo.get("PA_SHIP_NO").toString() + ": 발송처리 fail - " +e.getMessage() + "|");
				}
			}
		}catch(Exception e){
			msg = "대상건수:" + totalCnt + ", 성공건수:" + procCount + "|";
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message", e.getMessage().length() > 3950 ? msg+e.getMessage().substring(0, 3950) : msg+e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if(!paramMap.getString("code").equals("500")){
					msg = "대상건수:" + totalCnt + ", 성공건수:" + procCount + " | ";
					
					//대상건수 모두 성공하였을 경우
					if(totalCnt == procCount){
						paramMap.put("code","200");
					} else {
						paramMap.put("code","500");
					}
					paramMap.put("message", msg + sb.toString());
				}
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 slip-out-proc (배송중 처리) End =====");
		}
		
		invoiceModify(request);//운송장 수정 (추후에 스프링 배치로 분리하던 현상태 유지하던 결정해야함)
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);		
	}
	
	@ApiOperation(value = "송장 수정", notes = "배송 송장번호 수정전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/invoice-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> invoiceModify(HttpServletRequest request) throws Exception{
		
		ParamMap paramMap 						 = new ParamMap();	
		HashMap<String, String> apiInfo 	 	 = new HashMap<String, String>();
		String duplicateCheck  				 	 = "";
		String apiCode 						 	 = "IF_PAWEMPAPI_03_007";
		String dateTime 					 	 = systemService.getSysdatetimeToString();
		List<Map<String, Object>> slipUpdateList = null;
		ReturnData returnData				 	 = null;
		
		String representSlipNo 				 	 = "";
		String representDelyGb 				 	 = "";
		int successCnt							 = 0;
		int failCnt 							 = 0;
		
		log.info("===== 위메프 invoice-modify (배송송장번호 수정) Start=====");
		log.info("01.API 기본정보 세팅");
		paramMap.put("apiCode"   	, apiCode);
	    paramMap.put("broadCode" 	, Constants.PA_WEMP_BROAD_CODE);
	    paramMap.put("onlineCode"	, Constants.PA_WEMP_ONLINE_CODE);
		paramMap.put("startDate" 	, dateTime);
						
		try {
			//1)API BASIC INFO Setting
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

			//2)SELECT SLIP Information for Changing Shipping
			slipUpdateList = paWempOrderService.selectSlipUpdateProcList();
			
			String paTempShipNo = "";
			for(Map<String, Object> slipUpdate : slipUpdateList) {
				slipUpdate.put("PA_GROUP_CODE"		, "06");
				if(paTempShipNo.equals(slipUpdate.get("PA_SHIP_NO").toString())) {
					slipUpdate.put("TRANS_INVOICE_NO"	, representSlipNo);
					slipUpdate.put("TRANS_PA_DELY_GB"	, representDelyGb);
					
					if(returnData.getReturnKey() == 1) {
						slipUpdate.put("REMARK1_V"			, "운송장 수정 완료");
						slipUpdate.put("TRANS_YN"			, "1");
						
					}else {
						slipUpdate.put("REMARK1_V"			, "운송장 수정 실패");
						slipUpdate.put("TRANS_YN"			, "0");
					}
					paOrderService.insertTpaSlipInfoTx(slipUpdate);
					continue;
				} 
		
				representSlipNo = slipUpdate.get("INVOICE_NO").toString();	
				representDelyGb = slipUpdate.get("PA_DELY_GB").toString();
				
				String paCode = slipUpdate.get("PA_CODE").toString();
				String paName = paCode.equals(Constants.PA_WEMP_BROAD_CODE) ? Constants.PA_BROAD : Constants.PA_ONLINE;
				
				SetParcelDelivery deliveryReq = new SetParcelDelivery();
				deliveryReq.setBundleNo			(Long.parseLong( slipUpdate.get("PA_SHIP_NO").toString()));
				deliveryReq.setParcelCompanyCode(representDelyGb);
				deliveryReq.setInvoiceNo		(representSlipNo);
				returnData = (ReturnData)paWempApiService.callWApiObejct(apiInfo, "POST", deliveryReq, ReturnData.class, paName);

				if(returnData.getReturnKey() == 1) {
					slipUpdate.put("REMARK1_V"	, "운송장 수정 완료");
					slipUpdate.put("TRANS_YN"	, "1");
					successCnt ++;
				}else {
					slipUpdate.put("REMARK1_V"	, "운송장 수정 실패");
					slipUpdate.put("TRANS_YN"	, "0");
					failCnt++;
				}
				slipUpdate.put("TRANS_INVOICE_NO"	, representSlipNo);
				slipUpdate.put("TRANS_PA_DELY_GB"	, representDelyGb);
				//5)INSERT TPASLIMINFO
				paOrderService.insertTpaSlipInfoTx(slipUpdate);
				
				paTempShipNo 	= slipUpdate.get("PA_SHIP_NO").toString();
			}
			
			
			if(failCnt > 0) {
				paramMap.put("code"		, "500");
			}else {
				paramMap.put("code"		, "200");
			}
			paramMap.put("message"	, "운송장 수정 -  성공 :" + successCnt  + " 실패 :" + failCnt );
			
		}catch(Exception e){
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 invoice-modify (배송송장번호 수정) End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "직송 배송완료처리", notes = "배송 완료(직접배송 전용)", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/direct-complete-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> directCompleteProc(HttpServletRequest request,
			@ApiParam(name = "paShipNo", required = true, value = "paShipNo") @RequestParam(value = "paShipNo", required = true) String paShipNo,
			@ApiParam(name = "paCode"  , required = true, value = "paCode")   @RequestParam(value = "paCode"  , required = true) String paCode) throws Exception{
		ParamMap paramMap = new ParamMap();	
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_008";
		String dateTime = systemService.getSysdatetimeToString();
		String paName = "";
		int excuteCnt = 0;
		String deliveryStatus = "04"; // [O692] 03:배송중, 04:배송완료
		
		log.info("===== 위메프 direct-complete-proc (직송 배송완료처리) Start=====");
		log.info("01.API 기본정보 세팅");
		paramMap.put("apiCode"   , apiCode);
	    paramMap.put("broadCode" , Constants.PA_WEMP_BROAD_CODE);
	    paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
		paramMap.put("startDate" , dateTime);
		
		try {
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			paName = paCode.equals(Constants.PA_WEMP_BROAD_CODE) ? Constants.PA_BROAD : Constants.PA_ONLINE;
			GetOrderInfoRequest orderInfoReq = new GetOrderInfoRequest();
			orderInfoReq.setBundleNo(Integer.parseInt(paShipNo));
			
			ReturnData returnData = (ReturnData)paWempApiService.callWApiObejct(apiInfo, "POST", orderInfoReq, ReturnData.class, paName);
			if(returnData.getReturnKey() == 1) { //성공
				log.info("송장수정 API 전송성공 결과 returnKey:1, returnMsg:"+returnData.getReturnMsg());
				excuteCnt = paWempOrderService.updateDeliveryStatusTx(paShipNo, deliveryStatus);
				if(excuteCnt < 1) {
					throw processException("errors.process", new String[] { "TPAWEMPORDERLIST SUCCESS-UPDATE 오류 발생" });
				}
				paramMap.put("code"   , "200");
				paramMap.put("message", "OK");
			} else {
				log.info("송장수정 API 전송실패 결과 returnKey:"+returnData.getReturnKey()+", returnMsg:"+returnData.getReturnMsg());
				paramMap.put("code"   , "500");
				paramMap.put("message", returnData.getReturnMsg());
			}
		}catch(Exception e){
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 direct-complete-proc (직송 배송완료처리) End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	@ApiOperation(value = "배송완료목록 조회", notes = "배송완료목록 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/slip-complete-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> slipCompleteList(HttpServletRequest request,
			@ApiParam(name = "fromDate", required = false, value = "시작일자")   @RequestParam(value = "fromDate", required = false) String fromDate) throws Exception{
		ParamMap paramMap = new ParamMap();	
		HashMap<String, String> apiInfo = new HashMap<String, String>();	
		List<HashMap<String, Object>> completeList = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> complete = null;
		ResponseEntity<?> responseMsg = null;
		Paorderm paOrderm = null;
		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_003";
		String paName = "";
		String shipCompleteDate = "";
		String shipMethod = "";
		StringBuffer buffer = new StringBuffer();
		int totalCnt   = 0;
		int successCnt = 0;
		int waitCnt    = 0;
		
		String rtnMsg    = Constants.SAVE_SUCCESS;
		int excuteCnt = 0;
		String deliveryStatus = "04"; // [O692] 03:배송중, 04:배송완료
		
		log.info("===== 위메프 slip-complete-list (배송완료목록 조회) Start=====");
		log.info("01.API 기본정보 세팅");
		String dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode"   , apiCode);
		paramMap.put("procId"    , Constants.PA_WEMP_PROC_ID);
	    paramMap.put("broadCode" , Constants.PA_WEMP_BROAD_CODE);
	    paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
	    paramMap.put("startDate" , dateTime);
	    
		try {
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

			log.info("04.주문정보 조회-배송완료");
			completeList = paWempOrderService.selectDeliveryCompleteList();
			
			totalCnt = completeList.size();
			
			if(totalCnt >= 1){
				for(int i=0; i<completeList.size(); i++){
					shipCompleteDate = "";
					shipMethod = "";
					complete = (HashMap<String, Object>) completeList.get(i);
					paName = complete.get("PA_CODE").toString().equals(Constants.PA_WEMP_BROAD_CODE) ? Constants.PA_BROAD : Constants.PA_ONLINE;
					
					try{			
						GetOrderInfoRequest orderInfoReq = new GetOrderInfoRequest();
						orderInfoReq.setBundleNo(Integer.parseInt(complete.get("PA_SHIP_NO").toString()));
						GetOrderInfoResponse bundleObject = (GetOrderInfoResponse)paWempApiService.callWApiObejct(apiInfo, "POST", orderInfoReq, GetOrderInfoResponse.class, paName);
						
						if(bundleObject != null && CollectionUtils.isNotEmpty(bundleObject.getBundle())) {
							Bundle bundle = bundleObject.getBundle().get(0);
							
							log.info("05.배송완료 주문정보 세팅");
							shipCompleteDate = bundle.getShipCompleteDate();
							shipMethod = bundle.getDelivery().getShipMethod();
							
							log.info("06.배송완료정보 반영"); //TPAWEMPORDERLIST, TPAORDERM update
							if(!"".equals(shipCompleteDate) && shipCompleteDate != null) { //송장 트래킹 가능한 경우.
								excuteCnt = paWempOrderService.updateDeliveryStatusTx(complete.get("PA_SHIP_NO").toString(), deliveryStatus);
								if(excuteCnt < 1) {
									throw processException("errors.process", new String[] { "TPAWEMPORDERLIST SUCCESS-UPDATE 오류 발생" });
								}
								
								paOrderm = new Paorderm();
								paOrderm.setPaCode(complete.get("PA_CODE").toString());
								paOrderm.setPaOrderNo(complete.get("PA_ORDER_NO").toString());
								paOrderm.setPaShipNo(complete.get("PA_SHIP_NO").toString());
								paOrderm.setProcDate(DateUtil.toTimestamp(shipCompleteDate, DateUtil.WEMP_DATETIME_FORMAT));
								paOrderm.setApiResultCode(Constants.SAVE_SUCCESS);
								paOrderm.setApiResultMessage("배송완료 처리 성공");
								paOrderm.setModifyId(Constants.PA_WEMP_PROC_ID);
								
								rtnMsg = paWempOrderService.updateDeliveryCompleteTx(paOrderm);
								
								if(StringUtils.equals(Constants.SAVE_SUCCESS, rtnMsg)) {
									paramMap.put("code"   , "200");
									paramMap.put("message", "OK");
									successCnt++;
								} else {
									paramMap.put("code"   , "500");
									paramMap.put("message", "updatePaWempOrderTx fail");
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								}
							} else if ("직접배송".equals(shipMethod)){ //직접배송 처리 한 경우
								responseMsg = directCompleteProc(request, complete.get("PA_SHIP_NO").toString(), complete.get("PA_CODE").toString());
								if(PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
									
									paOrderm = new Paorderm();
									paOrderm.setPaCode(complete.get("PA_CODE").toString());
									paOrderm.setPaOrderNo(complete.get("PA_ORDER_NO").toString());
									paOrderm.setPaShipNo(complete.get("PA_SHIP_NO").toString());
									paOrderm.setProcDate(DateUtil.toTimestamp(shipCompleteDate, DateUtil.WEMP_DATETIME_FORMAT));
									paOrderm.setApiResultCode(Constants.SAVE_SUCCESS);
									paOrderm.setApiResultMessage("배송완료 처리 성공");
									paOrderm.setModifyId(Constants.PA_WEMP_PROC_ID);
									
									rtnMsg = paWempOrderService.updateDeliveryCompleteTx(paOrderm);
									
									if(StringUtils.equals(Constants.SAVE_SUCCESS, rtnMsg)) {
										paramMap.put("code"   , "200");
										paramMap.put("message", "OK");
										successCnt++;
									} else {
										paramMap.put("code"   , "500");
										paramMap.put("message", "updatePaWempOrderTx fail");
										return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
									}
								}else {
									paramMap.put("code"   , "200");
									paramMap.put("message", "OK");
									waitCnt++; // 직접배송 완료처리 실패 (대기)
								}
							} else if ("기타배송".equals(shipMethod)){ //송장 오류로 인해 관리자 페이지에서 기타배송 처리한 경우
								paOrderm = new Paorderm();
								paOrderm.setPaCode(complete.get("PA_CODE").toString());
								paOrderm.setPaOrderNo(complete.get("PA_ORDER_NO").toString());
								paOrderm.setPaShipNo(complete.get("PA_SHIP_NO").toString());
								paOrderm.setProcDate(DateUtil.toTimestamp(shipCompleteDate, DateUtil.WEMP_DATETIME_FORMAT));
								paOrderm.setApiResultCode(Constants.SAVE_SUCCESS);
								paOrderm.setApiResultMessage("송장오류로 인한 기타배송 처리");
								paOrderm.setModifyId(Constants.PA_WEMP_PROC_ID);
								
								rtnMsg = paWempOrderService.updateDeliveryCompleteTx(paOrderm);
								
								if(StringUtils.equals(Constants.SAVE_SUCCESS, rtnMsg)) {
									paramMap.put("code"   , "200");
									paramMap.put("message", "OK");
									successCnt++;
								} else {
									paramMap.put("code"   , "500");
									paramMap.put("message", "updatePaWempOrderTx fail");
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								}								
							} else {
								paramMap.put("code"   , "200");
								paramMap.put("message", "OK");
								waitCnt++; // 배송완료 안됨. 대기
							}
						}else{
							paOrderm = new Paorderm();
							paOrderm.setPaCode(complete.get("PA_CODE").toString());
							paOrderm.setPaOrderNo(complete.get("PA_ORDER_NO").toString());
							paOrderm.setPaShipNo(complete.get("PA_SHIP_NO").toString());
							paOrderm.setProcDate(DateUtil.toTimestamp(shipCompleteDate, DateUtil.WEMP_DATETIME_FORMAT));
							paOrderm.setApiResultCode(Constants.SAVE_SUCCESS);
							paOrderm.setApiResultMessage("주문상세 조회 불가: 교환/반품으로 인해");
							paOrderm.setModifyId(Constants.PA_WEMP_PROC_ID);
							
							rtnMsg = paWempOrderService.updateDeliveryCompleteTx(paOrderm);
							
							if(StringUtils.equals(Constants.SAVE_SUCCESS, rtnMsg)) {
								paramMap.put("code"   , "200");
								paramMap.put("message", "OK");
								successCnt++;
							} else {
								paramMap.put("code"   , "500");
								paramMap.put("message", "updatePaWempOrderTx fail");
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							}
						}
					}catch(Exception e){
						paramMap.put("code"   , "500");
						paramMap.put("message", e.getMessage());
					}
				}
			}
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try{
				if(!"500".equals(paramMap.getString("code"))){
					paramMap.put("code"   , (totalCnt == successCnt + waitCnt) ? "200" : "500");
					paramMap.put("message", "전체: " + totalCnt + " | 성공: " + successCnt + " | 대기: "+ waitCnt + " | 실패: " + buffer.toString());
				}
				
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			}catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 slip-complete-list (배송완료목록 조회) End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}
	
	@ApiOperation(value = "판매취소 처리", notes = "판매취소 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/soldOut-cancel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderSoldOut(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_006_BO";
		String pa_group_code = "06";
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
		paramMap.put("paGroupCode", pa_group_code);
		
		log.info("===== 위메프 soldOut-cancel (품절취소반품) Start=====");
		
		try {
			log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { apiCode });
			}

			log.info("02. 판매취소 조회");
			List<Object> cancelList = paCommonService.selectPaSoldOutordList(paramMap);
			
			HashMap<String, String> map = new HashMap<String, String>();
			
			if(cancelList.size() > 0) {
				for(int i = 0; i < cancelList.size(); i++) {
					try {
						HashMap<String, String> cancelMap = (HashMap<String, String>)cancelList.get(i);
						PaWempTargetVO targetVo = paWempOrderService.selectRefusalInfo(cancelMap.get("MAPPING_SEQ"));

						map.put("PA_GROUP_CODE", pa_group_code);
						map.put("PA_CODE", cancelMap.get("PA_CODE"));
						map.put("SITE_GB", cancelMap.get("SITE_GB"));
						map.put("PA_ORDER_NO", cancelMap.get("PA_ORDER_NO"));
						map.put("PA_ORDER_SEQ", cancelMap.get("PA_ORDER_SEQ"));
						map.put("ORDER_NO", cancelMap.get("ORDER_NO"));
						map.put("ORG_ORD_CAN_YN", cancelMap.get("ORDER_CANCEL_YN"));
						
						if(targetVo == null || StringUtils.isBlank(targetVo.getOrderOptionNo())) {
							log.info("orderInputAsync not found orderOptionNo. mappingSeq:"+paramMap.getString("mappingSeq"));
							continue;
						}
						//주문취소 호출
						ParamMap procResultMap = paWempOrderService.orderRefusalProc(targetVo);
						
						if(!StringUtils.equals("1", (String)procResultMap.get("result_code"))) {
							map.put("ORDER_CANCEL_YN", "90");
							map.put("RSLT_MESSAGE", "판매취소 실패(api 연동 실패)");
						} else {
							map.put("ORDER_CANCEL_YN", "10");
							map.put("RSLT_MESSAGE", "판매취소 성공");
						}
					}catch(Exception e) {
						log.error("11st 판매불가처리 실패." + e.getMessage());
						map.put("ORDER_CANCEL_YN", "90");
						map.put("RSLT_MESSAGE", "판매취소 실패(api 연동 실패)");
					}finally {
						paOrderService.updateOrderCancelYnTx(map);

						//상담생성 & 문자발송
						paCommonService.saveOrderCancelCounselTx(map);
					}
				}
			}
		}catch(Exception e){
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode"  , "IF_PAWEMPAPI_03_006_BO");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code"     , "500");
					paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
					systemService.insertApiTrackingTx(request, paramMap);
				}
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : "+e.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 soldOut-cancel (품절취소반품) End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "취소신청목록 조회", notes = "취소신청목록 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/cancel-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelList(HttpServletRequest request,
			@ApiParam(name = "fromDate", required=false, value = "시작일자", defaultValue = "")   @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate) throws Exception{
		ParamMap paramMap = new ParamMap();		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_021";
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
		
		log.info("===== 위메프 cancel-list (취소신청목록 조회) Start=====");
		String startDateTime = paWempApiService.makeDateTimeStart(fromDate);
		String endDateTime   = paWempApiService.makeDateTimeEnd(fromDate);
		
		try {
			log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { apiCode });
			}
			

			log.info("02. 취소정보 조회");
			GetClaimListRequest getClaimListRequest = new GetClaimListRequest();
			getClaimListRequest.setFromDate(startDateTime);
			getClaimListRequest.setToDate(endDateTime);
			getClaimListRequest.setClaimType("CANCEL");
			getClaimListRequest.setClaimStatus("REQUEST");
			getClaimListRequest.setSearchDateType("REQUEST");
			
			for(int i=0; i<Constants.PA_WEMP_CONTRACT_CNT; i++) {
				if(i==0) {
					paramMap.put("paCode", Constants.PA_WEMP_BROAD_CODE);
					paramMap.put("paName", Constants.PA_BROAD);
				}
				else {
					paramMap.put("paCode", Constants.PA_WEMP_ONLINE_CODE);
					paramMap.put("paName", Constants.PA_ONLINE);
				}
				HashMap<String, String> apiInfo = systemService.selectPaApiInfo(paramMap);
				apiInfo.put("apiInfo", paramMap.getString("apiCode"));
				
				paramMap.put("code","200");
				paramMap.put("message","OK");
				
				try {
					GetClaimListResponse claimObject = (GetClaimListResponse) paWempApiService.callWApiObejct(apiInfo, "POST",getClaimListRequest, GetClaimListResponse.class, paramMap.getString("paName"));
					
					if(claimObject != null && CollectionUtils.isNotEmpty(claimObject.getClaim())) {
						for(Claim claim: claimObject.getClaim()) {
							log.info("03. 취소요청 정보 DB저장");
							PaWempClaimList claimList = paWempClaimService.makePaWempClaimList(claim, WempCode.ORDER_GB.CANCEL.getCode(), paramMap.getString("paCode")); //PA_ORDER_GB 20 취소
							if(claimList == null) {
								log.info("Not found or duplicate PaWempOrderList SKIP");
								continue;
							}
							
							//TPAWEMPCLAIMLIST, TPAWEMPCLAIMITEMLIST insert
							String rtnMsg = paWempClaimService.savePaWempCancelTx(claimList);
							if(StringUtils.equals(Constants.SAVE_SUCCESS, rtnMsg)) {
								paramMap.put("code","200");
								paramMap.put("message","OK");
							} else {
								paramMap.put("code","500");
								paramMap.put("message","savePaWempCancelTx fail");
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							}
						}
					}
				} catch (Exception ex) {
					paramMap.put("code", "404");
					paramMap.put("message", ex.getMessage());
				}
			}
			
		}catch(Exception e){
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 cancel-list (취소신청목록 조회) End =====");
			// 취소 철회정보 조회 TPAWEMPCLAIMLIST.CANCEL_WITHDRAW_YN 업데이트
			//cancelWithdrawList(request, fromDate); -> 취소는 철회로 조회되지 않는다. 승인/거절시 에러코드로 확인
			// 주문처리상태에 따라 취소승인,거절
			cancelApprovalProc(request);
			// SK스토아 취소정보 생성
			cancelInputMain(request);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "취소승인거절전송", notes = "취소승인거절 API전송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/cancel-approval-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelApprovalProc(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_024";
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("code","200");
		paramMap.put("message","OK");
		
		int targetCnt = 0;
		int successCnt = 0;
		PaWempClaimList paWempClaim = new PaWempClaimList();
		PaWempDeliveryVO deliveryVo = new PaWempDeliveryVO();
		String msg = "";
		StringBuffer sb = new StringBuffer();
		boolean isApprove = false;
		boolean isReject = false;
		String outBefClaimGb = "0"; //= 원주문건의 do_flag가  30미만인것만 처리하므로 정상 취소처리
		int businessHour = 0;
		
		log.info("===== 위메프 cancel-approval-proc (취소승인 취소거절) Start=====");
		try {
			log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { apiCode });
			}

			log.info("02.취소처리 대상 조회");
			List<PaWempClaimList> claimList = paWempOrderService.selectCancelRequestList();
			if(claimList == null || claimList.size() < 1) {
				log.info("cancelApprovalProc no data found. skip");
				paramMap.put("code","404");
				paramMap.put("message", getMessage("errors.no.select"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			targetCnt = claimList.size();
			
			for(int i=0; i<targetCnt; i++) {
				paWempClaim = claimList.get(i);
				paWempClaim.setModifyId(Constants.PA_WEMP_PROC_ID);
				// ITEM목록 및 Invoice 정보 조회
				ParamMap selectParam = new ParamMap();
				selectParam.put("paClaimNo"  ,paWempClaim.getPaClaimNo());
				selectParam.put("paOrderNo"  ,paWempClaim.getPaOrderNo());
				selectParam.put("paShipNo"   ,paWempClaim.getPaShipNo());
				selectParam.put("paGroupCode",Constants.PA_WEMP_GROUP_CODE);
				selectParam.put("modifyId"   ,paWempClaim.getModifyId());
				List<PaWempDeliveryVO> deliveryVoList = paWempOrderService.selectCancelRequestDtList(selectParam);
				if(deliveryVoList == null || deliveryVoList.size() < 1) {
					log.info("selectCancelRequestDtList not found, PA_CLAIM_NO:"+paWempClaim.getPaClaimNo());
					sb.append("selectCancelRequestDtList not found, PA_CLAIM_NO:"+paWempClaim.getPaClaimNo());
					continue;
				}

				// 승인,거부 여부 체크
				isApprove = false;
				isReject = false;
				businessHour = 0;
				outBefClaimGb = "0";
				for(int k=0; k < deliveryVoList.size(); k++) {
					deliveryVo = deliveryVoList.get(k);
					
					int doFlag = Integer.parseInt(deliveryVo.getDoFlag());
					if(doFlag < 30){ // DO_FLAG(20)승인 상태면 취소승인처리 API 호출
						isApprove = true;
					} else if(doFlag >= 30 && !StringUtils.isBlank(deliveryVo.getInvoiceNo())){ // DO_FLAG(30)이상이고 송장번호 기입되어 클레임거부 대상
						isReject = true;
					} else {
						// 위메프는 2영업일이 초과하면 자동승인되므로 그전에 승인처리, 11번가는 65시간, G마켓도 65시간 초과시 승인
						if(businessHour == 0) {
							businessHour = paWempOrderService.selectBusinessDayAccount(deliveryVo.getPaClaimNo(), deliveryVo.getDelyGb());
							if(businessHour >= ConfigUtil.getInt("WEMP_CANCEL_BUSINESS_DAY")){
								log.info("취소승인대상 인터페이스 전송");
								isApprove = true;
								outBefClaimGb = "1";
							}
							log.info("cancelApprovalProc no invoiceNo skip. DO_FLAG:"+doFlag+", PA_CLAIM_NO:"+deliveryVo.getPaClaimNo());
						}
					}
				}
				
				deliveryVo = deliveryVoList.get(0); //첫번째 order정보를 이용해서 API처리 진행
				// 승인,거부 API 전송
				if(isReject) { //거부가 한건이라도 있으면 해당 claimBundleNo는 전부 거부로 처리
					ParamMap procResultMap = paWempOrderService.cancelRejectProc(deliveryVo);
					if(StringUtils.equals("1", procResultMap.getString("result_code"))) {
						//TPAORDERM.PA_DO_FLAG '40'출고로 update, TPAWEMPCLAIMITEMLIST.PROC_FLAG '20'취소거부로 update
						ParamMap saveResultMap = paWempOrderService.saveCancelRejectTx(deliveryVo);
						insertPaSlipInfo4CancelReject(deliveryVo);
						
						if(StringUtils.equals(Constants.SAVE_SUCCESS, saveResultMap.getString("result_code"))) {
							successCnt++;
						} else {
							log.info("saveCancelInfo insert TPAORDERM fail, PA_CLAIM_NO:"+deliveryVo.getPaClaimNo());
							sb.append("saveCancelInfo insert TPAORDERM fail, PA_CLAIM_NO:"+deliveryVo.getPaClaimNo());
						}
					} else {
						if(StringUtils.contains(procResultMap.getString("result_text"), "철회")) {
							//TPAWEMPCLAIMLIST.CANCEL_WITHDRAW_YN = '1' update
							paWempClaim.setCancelWithdrawYn("1"); //철회
							if (paWempOrderService.updateCancelWithdrawYnTx(paWempClaim) < 1) {
								log.info("updatePaWempCancelWithdrawTx fail");
							} else {
								log.info("updatePaWempCancelWithdrawTx success");
							}
						} else {
							//TPAWEMPCLAIMITEMLIST.PROC_FLAG '90'처리실패로 update
							paWempOrderService.saveCancelFailTx(deliveryVo);
						}
					}
				} else if(isApprove) {
					ParamMap procResultMap = paWempOrderService.cancelApproveProc(Long.parseLong(deliveryVo.getPaClaimNo()), deliveryVo.getPaCode());
					if(StringUtils.equals("1", procResultMap.getString("result_code"))) {
						//TPAORDERM에 취소승인으로 insert, TPAWEMPCLAIMITEMLIST.PROC_FLAG '10'승인으로 update
						ParamMap saveResultMap = paWempOrderService.saveCancelApproveTx(deliveryVoList, outBefClaimGb, true);
						if(StringUtils.equals(Constants.SAVE_SUCCESS, saveResultMap.getString("result_code"))) {
							successCnt++;
						} else {
							log.info("saveCancelInfo insert TPAORDERM fail, PA_CLAIM_NO:"+deliveryVo.getPaClaimNo());
							sb.append("saveCancelInfo insert TPAORDERM fail, PA_CLAIM_NO:"+deliveryVo.getPaClaimNo());
						}
					} else {
						// {"resultCode":200,"data":{"returnKey":0,"returnMsg":"철회된 클레임입니다."}} 처리
						if(StringUtils.contains(procResultMap.getString("result_text"), "철회")) {
							//TPAWEMPCLAIMLIST.CANCEL_WITHDRAW_YN = '1' update
							paWempClaim.setCancelWithdrawYn("1"); //철회
							if (paWempOrderService.updateCancelWithdrawYnTx(paWempClaim) < 1) {
								log.info("updatePaWempCancelWithdrawTx fail");
							} else {
								log.info("updatePaWempCancelWithdrawTx success");
							}
						} else {
							//TPAWEMPCLAIMITEMLIST.PROC_FLAG '90'처리실패로 update
							paWempOrderService.saveCancelFailTx(deliveryVo);
						}
					}
				}
			}
		}catch(Exception e){
			msg = "대상건수:" + targetCnt + ", 성공건수:" + successCnt + "|";
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",
					e.getMessage().length() > 3950 ? msg+e.getMessage().substring(0, 3950) : msg+e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if(!paramMap.getString("code").equals("500")){
					msg = "대상건수:" + targetCnt + ", 성공건수:" + successCnt + " | ";
					paramMap.put("message", msg + sb.toString());
				}
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 cancel-approval-proc (취소승인 취소거절) End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	@ApiOperation(value = "취소승인목록 조회", notes = "취소승인목록 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/cancel-complete-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelCompleteList(HttpServletRequest request,
			@ApiParam(name = "fromDate", required=false, value = "시작일자", defaultValue = "")   @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate) throws Exception{
		ParamMap paramMap = new ParamMap();		
		String duplicateCheck  = "";
		String apiCode = "IF_PAWEMPAPI_03_023";
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("broadCode" , Constants.PA_WEMP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
		paramMap.put("procId"    , Constants.PA_WEMP_PROC_ID);
		
		boolean isDuplicate = false;
		int existsCnt   = 0;
		PawemporderlistVO paWempOrderList = null;
		
		log.info("===== 위메프 cancel-complete-list (취소승인목록 조회) Start=====");
		String startDateTime = paWempApiService.makeDateTimeStart(fromDate);
		String endDateTime   = paWempApiService.makeDateTimeEnd(fromDate);
		
		try {
			log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { apiCode });
			}
			
			//조회해와서 TPAWEMPCLAIMITEMLIST.PROC_FLAG = '10'으로 된것, 즉 취소생성한 건은 skip
			//그렇지 않은 것들은 취소생성을 위한 TPAWEMPCLAIMLIST&TPAWEMPCLAIMITEMLIST insert or update, TPAORDERM insert처리
			log.info("02. 취소정보 조회");
			GetClaimListRequest getClaimListRequest = new GetClaimListRequest();
			getClaimListRequest.setFromDate(startDateTime);
			getClaimListRequest.setToDate(endDateTime);
			getClaimListRequest.setClaimType("CANCEL");
			getClaimListRequest.setClaimStatus("APPROVE");
			getClaimListRequest.setSearchDateType("APPROVE");
			
			for(int i=0; i<Constants.PA_WEMP_CONTRACT_CNT; i++) {
				if(i==0) {
					paramMap.put("paCode", Constants.PA_WEMP_BROAD_CODE);
					paramMap.put("paName", Constants.PA_BROAD);
				}
				else {
					paramMap.put("paCode", Constants.PA_WEMP_ONLINE_CODE);
					paramMap.put("paName", Constants.PA_ONLINE);
				}
				HashMap<String, String> apiInfo = systemService.selectPaApiInfo(paramMap);
				apiInfo.put("apiInfo", paramMap.getString("apiCode"));
				
				try {
					GetClaimListResponse claimObject = (GetClaimListResponse) paWempApiService.callWApiObejct(apiInfo, "POST",getClaimListRequest, GetClaimListResponse.class, paramMap.getString("paName"));
					
					if(claimObject != null && CollectionUtils.isNotEmpty(claimObject.getClaim())) {
						for(Claim claim: claimObject.getClaim()) {
							log.info("03. 이미 처리가 된 건인지 DB조회");
							isDuplicate = false;
							List<PaWempClaimItemList> itemList = paWempClaimService.selectPaWempClaimItemList(Long.toString(claim.getClaimBundleNo()), Long.toString(claim.getPurchaseNo()), Long.toString(claim.getBundleNo()), WempCode.ORDER_GB.CANCEL.getCode(), "");
							if(itemList != null && itemList.size() > 0) {
								for(int k=0; k<itemList.size(); k++) {
									if(StringUtils.equals("10", itemList.get(k).getProcFlag())) {
										isDuplicate = true;
										break;
									}
								}
							}
							
							//생성된 주문이 있는지 조회
							existsCnt   = 0;
							paWempOrderList = new PawemporderlistVO();
							paWempOrderList.setPaOrderNo(Long.toString(claim.getPurchaseNo()));
							paWempOrderList.setPaShipNo(Long.toString(claim.getBundleNo()));
							existsCnt = paWempOrderService.selectPaWempOrderListExists(paWempOrderList);
							if(existsCnt == 0) {
								isDuplicate = true;
							}							
							
							if(isDuplicate) {
								log.info("이미처리된 건 skip");
								continue;
							}
							else {
								log.info("04. 취소 승인 중간 데이터 생성");
								PaWempClaimList claimList = paWempClaimService.makePaWempClaimList(claim, WempCode.ORDER_GB.CANCEL.getCode(), paramMap.getString("paCode")); //PA_ORDER_GB 20 취소
								if(claimList == null) {
									log.info("Not found or duplicate PaWempOrderList SKIP");
									continue;
								}
								
								//TPAWEMPCLAIMLIST, TPAWEMPCLAIMITEMLIST insert
								String rtnMsg = paWempClaimService.savePaWempCancelTx(claimList);
								if(StringUtils.equals(Constants.SAVE_SUCCESS, rtnMsg)) {
									paramMap.put("code","200");
									paramMap.put("message","OK");
								} else {
									paramMap.put("code","500");
									paramMap.put("message","savePaWempCancelTx fail");
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								}
								//TPAWEMPCLAIMITEMLIST PROC_FLAG "10"승인으로 업데이트
								paWempOrderService.updateClaimItemProcFlag(Long.toString(claim.getClaimBundleNo()), Long.toString(claim.getPurchaseNo()), Long.toString(claim.getBundleNo()), WempCode.ORDER_GB.CANCEL.getCode(), "10");
								
								//PAORDERM에 데이터 삽입.
								String outBefClaimGb = "0";
								ParamMap selectParam = new ParamMap();
								selectParam.put("paClaimNo"  ,Long.toString(claim.getClaimBundleNo()));
								selectParam.put("paOrderNo"  ,Long.toString(claim.getPurchaseNo()));
								selectParam.put("paShipNo"   ,Long.toString(claim.getBundleNo()));
								selectParam.put("paGroupCode",Constants.PA_WEMP_GROUP_CODE);
								selectParam.put("modifyId"   ,Constants.PA_WEMP_PROC_ID);
								List<PaWempDeliveryVO> deliveryVoList = paWempOrderService.selectCancelRequestDtList(selectParam);
								if(deliveryVoList == null || deliveryVoList.size() < 1) {
									log.info("selectCancelRequestDtList not found, PA_CLAIM_NO:"+Long.toString(claim.getClaimBundleNo()));
									continue;
								}
								//출하지시 이후건 하나라도 존재 시 출고전 반품처리.
								for(int j=0; j<deliveryVoList.size(); j++) {
									if(Integer.parseInt(deliveryVoList.get(j).getDoFlag()) >= 30) {
										outBefClaimGb = "1";
									}
								}
								ParamMap saveResultMap = paWempOrderService.saveCancelApproveTx(deliveryVoList, outBefClaimGb, false);
								if(!StringUtils.equals(Constants.SAVE_SUCCESS, saveResultMap.getString("result_code"))) {
									log.info("saveCancelInfo insert TPAORDERM fail, PA_CLAIM_NO:"+deliveryVoList.get(0).getPaClaimNo());
								}
							}
						}
					}
				}
				catch (Exception ex) {
					log.info(ex.getMessage());
				}
			}
			
			paramMap.put("code","200");
			paramMap.put("message","OK");
		}catch(Exception e){
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("===== 위메프 cancel-complete-list (취소승인목록조회) End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "취소승인전송(BO호출용)", notes = "취소승인 API전송(BO호출용)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"),
			   @ApiResponse(code = 304, message = "데이터 처리에 실패 하였습니다."), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/cancel-approval-proc-bo", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelApprovalProcBo(HttpServletRequest request,
			@ApiParam(name="paClaimNo", required=true , value="제휴사 클레임번호") @RequestParam(value="paClaimNo", required=true) String paClaimNo,
			@ApiParam(name="paCode",    required=false, value="제휴사 코드") @RequestParam(value="paCode", required=true) String paCode) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		int executedRtn = 0;
		String apiCode = "IF_PAWEMPAPI_03_026";
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		List<PaWempDeliveryVO> deliveryVoList = null;
		PaWempDeliveryVO deliveryVo = new PaWempDeliveryVO();
		log.info("===== 위메프 cancel-approval-proc-bo (취소승인 BO용) Start=====");
		try {
			ParamMap procResultMap = paWempOrderService.cancelApproveProc(Long.parseLong(paClaimNo), paCode);			
			if(StringUtils.equals("1", procResultMap.getString("result_code"))) {
				
				deliveryVoList = paWempOrderService.selectOrgItemInfoByCancelInfo(paClaimNo);
				//TPAORDERM에 취소승인으로 insert
				//isUpdate false. TPAWEMPCLAIMITEMLIST.PROC_FLAG '10'승인으로 update는 BO에서 진행하므로 제외
				ParamMap saveResultMap = paWempOrderService.saveCancelApproveTx(deliveryVoList, "1", false);
				if(!StringUtils.equals(Constants.SAVE_SUCCESS, saveResultMap.getString("result_code"))) {
					log.info("saveCancelInfo insert TPAORDERM fail, PA_CLAIM_NO:"+deliveryVo.getPaClaimNo());
					paramMap.put("code","304");
					paramMap.put("message", paramMap.getString("result_text") + ":" + "paClaimNo : " + paClaimNo);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			} else {
				log.info("cancelApproveProc fail, PA_CLAIM_NO:"+paClaimNo+", result_text:"+procResultMap.getString("result_text"));
				if(StringUtils.contains(procResultMap.getString("result_text"), "철회")) {
					//TPAWEMPCLAIMLIST.CANCEL_WITHDRAW_YN = '1' update
					PaWempClaimList paWempClaim = new PaWempClaimList();
					paWempClaim.setPaClaimNo(paClaimNo);
					paWempClaim.setCancelWithdrawYn("1"); //철회
					paWempClaim.setModifyId(Constants.PA_WEMP_PROC_ID);
					
					executedRtn = paWempOrderService.updateCancelWithdrawYnTx(paWempClaim);
					if (executedRtn < 0) {
						throw processException("msg.cannot_save", new String[] { "TPAWEMPCLAIMLIST UPDATE" });
					}
				} else {
					paramMap.put("code","304");
					paramMap.put("message", paramMap.getString("result_text") + ":" + "paClaimNo : " + paClaimNo);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}
			paramMap.put("code","200");
			paramMap.put("message","OK");
		}catch(Exception e){
			paramMap.put("code", "500");
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			log.info("===== 위메프 cancel-approval-proc-bo (취소승인 BO용) End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * SK스토아 주문생성
	 * @param request
	 * @throws Exception
	 */
	@ApiOperation(value = "SK스토아 주문생성", notes = "SK스토아 주문생성", httpMethod = "GET")
	@RequestMapping(value = "/order-input", method = RequestMethod.GET)
	@ResponseBody
	public void orderInputMain(HttpServletRequest request) throws Exception{
		String duplicateCheck  = "";
		String prgId = "PAWEMP_ORDER_INPUT";
		
		log.info("===== 위메프 order-input (SK스토아 주문생성) Start =====");
		try {
			log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prgId);
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { prgId });
			}
			
			//대상조회
			int paOrderCreateCnt = ConfigUtil.getInt("PA_ORDER_CREATE_CNT"); //= 제휴사 1회 주문생성 건수
			List<PaWempTargetVO> orderInputTargetList = paWempOrderService.selectOrderInputTargetList(paOrderCreateCnt);
			
			String paOrderNo = "";
			int targetCnt = 0;
			
			int procCnt = orderInputTargetList.size();
			for(int i=0; i < procCnt; i++) {
				try{
					PaWempTargetVO targetVo = orderInputTargetList.get(i);
					paOrderNo = targetVo.getPaOrderNo();
					targetCnt = (int)targetVo.getTargetCnt();
					
					asyncController.orderInputAsync(paOrderNo, targetCnt, request);
				} catch (Exception e) {
					continue;
				}
			}
		}catch(Exception e){
			log.info("error msg : " + e.getMessage());
		} finally {
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prgId);
			}
			log.info("===== 위메프 order-input (SK스토아 주문생성) End =====");
		}
		return;
	}
	
	/**
	 * SK스토아 취소생성
	 * @param request
	 * @throws Exception
	 */
	@ApiOperation(value = "SK스토아 취소생성", notes = "SK스토아 취소생성", httpMethod = "GET")
	@RequestMapping(value = "/cancel-input", method = RequestMethod.GET)
	@ResponseBody
	public void cancelInputMain(HttpServletRequest request) throws Exception{
		String duplicateCheck  = "";
		String prgId = "PAWEMP_CANCEL_INPUT";
		
		log.info("===== 위메프 cancel-input (SK스토아 취소생성) Start =====");
		try {
			log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prgId);
			if("1".equals(duplicateCheck)) {
				throw processException("msg.batch_process_duplicated", new String[] { prgId });
			}
			
			//대상조회
			List<PaWempTargetVO> cancelInputTargetList = paWempOrderService.selectCancelInputTargetList();
			int procCnt = cancelInputTargetList.size();
			for(int i = 0; procCnt > i; i++){
				try{
					PaWempTargetVO targetVo = cancelInputTargetList.get(i);
					
					asyncController.cancelInputAsync(targetVo.getPaCode(), targetVo.getPaClaimNo(), targetVo.getPaOrderNo(), targetVo.getPaOrderSeq(), targetVo.getPaShipNo(), request);
				} catch (Exception e) {
					continue;
				}
			}
		}catch(Exception e){
			log.info("error msg : " + e.getMessage());
		} finally {
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prgId);
			}
			log.info("===== 위메프 cancel-input (SK스토아 취소생성) End =====");
		}
		return;
	}
	
	private int insertPaSlipInfo4CancelReject(PaWempDeliveryVO deliveryVo) {
		Map<String, Object> slipInfo = new HashMap<String, Object>();
		try {
			String invoiceNo = "";
		
			slipInfo.put("PA_GROUP_CODE" 	, "O6");
			slipInfo.put("MAPPING_SEQ"	 	, deliveryVo.getMappingSeq());
			slipInfo.put("PA_DELY_GB"	 	, deliveryVo.getParcelCompanyCode());
			
			if(StringUtils.equals("DIRECT", deliveryVo.getParcelCompanyCode())) {
				invoiceNo = deliveryVo.getScheduleShipDate();
			}else {
				invoiceNo = deliveryVo.getInvoiceNo();
			}
			slipInfo.put("INVOICE_NO"		, deliveryVo.getInvoiceNo());
			slipInfo.put("TRANS_PA_DELY_GB"	, deliveryVo.getParcelCompanyCode());
			slipInfo.put("TRANS_INVOICE_NO"	, invoiceNo);
			slipInfo.put("REMARK1_V"		, "취소거부 출고처리");
			slipInfo.put("TRANS_YN"			, "1");
			paOrderService.insertTpaSlipInfoTx(slipInfo);	
		}catch (Exception e) {
			log.error(e.toString());
			return 0;
		}
		return 1;
	}
	
}