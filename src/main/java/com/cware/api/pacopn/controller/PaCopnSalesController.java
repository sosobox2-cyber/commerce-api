package com.cware.api.pacopn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Pacopnsettlement;
import com.cware.netshopping.pacopn.sales.service.PaCopnSalesService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Api(value="/pacopn/sales", description="공통")
@Controller("com.cware.api.pacopn.PaCopnSalesController")
@RequestMapping(value="/pacopn/sales")
public class PaCopnSalesController extends AbstractController{

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pacopn.sales.paCopnSalesService")
	private PaCopnSalesService paCopnSalesService;
	
	/**
	 * 쿠팡 매출내역 조회
	 *
	 * @param request
	 * @param compareDate
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "매출내역 조회", notes = "정산 API", httpMethod = "GET", produces = "application/xml")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), 
							@ApiResponse(code = 490, message = "중복처리 오류 발생."),
							@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") })
	@RequestMapping(value = "/daily-settle-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> settlementCompare(HttpServletRequest request,
			@RequestParam(value="fromDate", required=false, defaultValue="") String fromDate,
			@RequestParam(value="toDate", required=false, defaultValue="") String toDate,
			@RequestParam(value="delYn", required=false, defaultValue="") String delYn) throws Exception {		
		/**
	     *  fromDate / toDate ex) 20200201
	     *  기간입력
	     *  Default D-1 ~ D
	     * */		
		ParamMap paramMap = new ParamMap();
		String  duplicateCheck = "";
		int dtChk = 0;
		
		String startDate = "";
		String endDate = "";
		String accountDate = "";
		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String prg_id = "IF_PACOPNAPI_04_001";
		
		String dateTime = "";
		dateTime = systemService.getSysdatetimeToString();
		
		try{
			log.info("===== 쿠팡 매출내역 조회 API Start=====");
			log.info("01.API 기본정보 세팅");

			paramMap.put("apiCode", prg_id);
			paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate", dateTime);

			log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			//duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			//if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});

			paramMap.put("code", "200");
			paramMap.put("message", "OK");

			if (!(fromDate.isEmpty()||fromDate.equals(""))&&!(toDate.isEmpty()||toDate.equals(""))) {
				//Date validation check
				if (fromDate.length() != 8 || toDate.length() != 8) {
					throw new Exception("Parameter Validation Error : " + fromDate);
				}
				startDate = fromDate.substring(0, 4) + "-" + fromDate.substring(4, 6) + "-" + fromDate.substring(6, 8);
				endDate = toDate.substring(0, 4) + "-" + toDate.substring(4, 6) + "-" + toDate.substring(6, 8);
			} else {
				accountDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT);
				startDate = accountDate.substring(0, 4)   + "-" + accountDate.substring(4, 6)   + "-" + accountDate.substring(6, 8);
				endDate = startDate;
			}
			paramMap.put("startDateQ", startDate);
			paramMap.put("endDateQ"  , endDate);
			dtChk = 0;

			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

			if(delYn.equals("N")||delYn.equals("n")){
				log.info("4.정산일자 중복데이터 체크");
				dtChk = paCopnSalesService.selectChkPaCopnAccount(paramMap);
			}
			paramMap.put("delYn", delYn);
			if(dtChk == 0){
				for(int count=0 ; count<Constants.PA_COPN_CONTRACT_CNT; count++) {
					paramMap.put("nextToken" , "start");
					if(count == 0) {
						paramMap.put("paName", Constants.PA_BROAD);
						paramMap.put("paCode", Constants.PA_COPN_BROAD_CODE);
					} else {
						paramMap.put("paName", Constants.PA_ONLINE);
						paramMap.put("paCode", Constants.PA_COPN_ONLINE_CODE);
					}
					while(!"".equals(paramMap.getString("nextToken"))){
						procAccountListDay(request, paramMap, apiInfo);
						if(!"200".equals(paramMap.getString("code"))){
							break;
						}
					}
				}
			}else{
				paramMap.put("code","490");
				paramMap.put("message",getMessage("ssg.cannot_dup_data", new String[]{"날짜:"+startDate + "~" + endDate}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}

		}catch(Exception e){
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")),HttpStatus.OK);
		}finally{
			try {
				systemService.insertApiTrackingTx(request, paramMap);
				/*if (duplicateCheck.equals("0")) {
					systemService.checkCloseHistoryTx("end", prg_id);
				}*/
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			log.info("===== 쿠팡 매출내역 조회 API End =====");  
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}

	private ParamMap procAccountListDay(HttpServletRequest request, ParamMap paramMap, HashMap<String, String> apiInfo) throws Exception{
		
		JsonObject responseObj	= null;
		URIBuilder builder		= null;
		JsonArray contentList	= null;
		JsonObject content		= null;
		JsonArray itemsList		= null;
		JsonObject itemsContent	= null;
		
		Pacopnsettlement pacopnSettlement = null;
		List<Pacopnsettlement> pacopnSettlementList = new ArrayList<Pacopnsettlement>();
		
		String[] apiKeys = null;
		
		String nextToken = paramMap.getString("nextToken");
		
		try{
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";");
			
			builder = new URIBuilder(apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0]))
			.addParameter("vendorid"      , apiKeys[0])
			.addParameter("recognitionDateFrom", paramMap.getString("startDateQ"))
			.addParameter("recognitionDateTo"  , paramMap.getString("endDateQ"));
			
			if(nextToken != null && !"".equals(nextToken) && !"start".equals(nextToken)){
				builder.addParameter("token", nextToken);
			}else{
				builder.addParameter("token", "");
			}
			
			responseObj = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), builder);
			
			if("200".equals(responseObj.get("code").getAsString())){
				paramMap.put("code"     , "200");
				paramMap.put("message"  , "SUCCESS");
				
				contentList = responseObj.get("data").getAsJsonArray();
				
				if(contentList.size() > 0){
					for(int i=0; i<contentList.size(); i++){
						content = contentList.get(i).getAsJsonObject();
						itemsList = contentList.get(i).getAsJsonObject().get("items").getAsJsonArray();
						
						for(int j=0; j<itemsList.size(); j++){
							if(!"0".equals(content.get("orderId").getAsString())) {
								itemsContent = itemsList.get(j).getAsJsonObject();
								
								pacopnSettlement = new Pacopnsettlement();
								
								pacopnSettlement.setPaCode(paramMap.getString("paCode"));
								pacopnSettlement.setOrderId(content.get("orderId").getAsString());
								pacopnSettlement.setSaleType(content.get("saleType").getAsString());
								pacopnSettlement.setSaleDate(content.get("saleDate").getAsString());
								pacopnSettlement.setRecognitionDate(DateUtil.toTimestamp(content.get("recognitionDate").getAsString(), DateUtil.COPN_DATE_FORMAT));
								pacopnSettlement.setSettlementDate(content.get("settlementDate").getAsString());
								pacopnSettlement.setFinalSettlementDate(content.get("finalSettlementDate").getAsString());
								pacopnSettlement.setAmount(content.getAsJsonObject("deliveryFee").get("amount").getAsDouble());
								pacopnSettlement.setFee(content.getAsJsonObject("deliveryFee").get("fee").getAsDouble());
								pacopnSettlement.setFeeVat(content.getAsJsonObject("deliveryFee").get("feeVat").getAsDouble());
								pacopnSettlement.setFeeRatio(content.getAsJsonObject("deliveryFee").get("feeRatio").getAsDouble());
								pacopnSettlement.setSettlementAmountDeli(content.getAsJsonObject("deliveryFee").get("settlementAmount").getAsDouble());
								pacopnSettlement.setBaseAmount(content.getAsJsonObject("deliveryFee").get("baseAmount").getAsDouble());
								pacopnSettlement.setBaseFee(content.getAsJsonObject("deliveryFee").get("baseFee").getAsDouble());
								pacopnSettlement.setFeeVat(content.getAsJsonObject("deliveryFee").get("baseFeeVat").getAsDouble());
								pacopnSettlement.setRemoteAmount(content.getAsJsonObject("deliveryFee").get("remoteAmount").getAsDouble());
								pacopnSettlement.setRemoteFee(content.getAsJsonObject("deliveryFee").get("remoteFee").getAsDouble());
								pacopnSettlement.setRemoteFeeVat(content.getAsJsonObject("deliveryFee").get("remoteFeeVat").getAsDouble());
								pacopnSettlement.setTaxType(itemsContent.get("taxType").getAsString());
								pacopnSettlement.setProductId(itemsContent.get("productId").getAsString());
								pacopnSettlement.setProductName(itemsContent.get("productName").isJsonNull() ? "" : itemsContent.get("productName").getAsString());
								pacopnSettlement.setVendorItemId(itemsContent.get("vendorItemId").getAsString());
								pacopnSettlement.setVendorItemName(itemsContent.get("vendorItemName").getAsString());
								pacopnSettlement.setSalePrice(itemsContent.get("salePrice").getAsDouble());
								pacopnSettlement.setQuantity(itemsContent.get("quantity").getAsDouble());
								pacopnSettlement.setCoupangDiscountCoupon(itemsContent.get("coupangDiscountCoupon").getAsDouble());
								pacopnSettlement.setDiscountCouponpolicyagreement(itemsContent.get("discountCouponPolicyAgreement").getAsString());
								pacopnSettlement.setSaleAmount(itemsContent.get("saleAmount").getAsDouble());
								pacopnSettlement.setSellerDiscountCoupon(itemsContent.get("sellerDiscountCoupon").getAsDouble());
								pacopnSettlement.setDownloadableCoupon(itemsContent.get("downloadableCoupon").getAsDouble());
								pacopnSettlement.setServiceFee(itemsContent.get("serviceFee").getAsDouble());
								pacopnSettlement.setServiceFeeVat(itemsContent.get("serviceFeeVat").getAsDouble());
								pacopnSettlement.setServiceFeeRatio(itemsContent.get("serviceFeeRatio").getAsDouble());
								pacopnSettlement.setSettlementAmount(itemsContent.get("settlementAmount").getAsDouble());
								pacopnSettlement.setCouranteeFeeRatio(itemsContent.get("couranteeFeeRatio").getAsDouble());
								pacopnSettlement.setCouranteeFee(itemsContent.get("couranteeFee").getAsDouble());
								pacopnSettlement.setCouranteeFeeVat(itemsContent.get("couranteeFeeVat").getAsDouble());
								pacopnSettlement.setExternalSellerSkuCode(itemsContent.get("externalSellerSkuCode").getAsString());
								pacopnSettlement.setStoreFeeDiscount(itemsContent.get("storeFeeDiscount").getAsDouble());
								pacopnSettlement.setStoreFeeDiscountVat(itemsContent.get("storeFeeDiscountVat").getAsDouble());
								pacopnSettlement.setInsertId(Constants.PA_COPN_PROC_ID);
								pacopnSettlement.setInsertDate(paramMap.getTimestamp("startDate"));
								
								pacopnSettlementList.add(pacopnSettlement);
							}
						}
					}
					paCopnSalesService.savePaCopnSettlementTx(pacopnSettlementList, paramMap);
					
					paramMap.put("nextToken", responseObj.get("nextToken").getAsString());
				} else{
					paramMap.put("code"     , "404");
					paramMap.put("message"  , getMessage("pa.no_return_data"));
					paramMap.put("nextToken", "");
				}
			}else{
				paramMap.put("code"     , "500");
				paramMap.put("message"  , getMessage("errors.api.system"));
				paramMap.put("nextToken", "");
			}
		}catch(Exception e){
			paramMap.put("code"     , "500");
			paramMap.put("message"  , e.getMessage());
			paramMap.put("nextToken", "");
			return paramMap;
		}
		return paramMap;
	}
}