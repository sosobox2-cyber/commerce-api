package com.cware.api.pacopn.controller;

import io.swagger.annotations.Api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.common.util.HttpUtil;
import com.cware.netshopping.domain.CancelInputVO;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.OrderInputVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaCopnGoodsVO;
import com.cware.netshopping.domain.PaCustShipCostVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pacopn.claim.service.PaCopnClaimService;
import com.cware.netshopping.pacopn.exchange.service.PaCopnExchangeService;
import com.cware.netshopping.pacopn.goods.service.PaCopnGoodsService;
import com.cware.netshopping.pacopn.order.service.PaCopnOrderService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Api(value = "/pacopn/async", description="공통")
@Controller("com.cware.api.pacopn.paCopnAsyncController")
@RequestMapping(value = "/pacopn/async")
public class PaCopnAsyncController extends AbstractController{

	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	@Resource(name = "pacopn.order.paCopnOrderService")
	private PaCopnOrderService paCopnOrderService;

	@Resource(name = "pacopn.claim.paCopnClaimService")
	private PaCopnClaimService paCopnClaimService;
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paOrderService;

	@Resource(name = "pacommon.claim.paclaimService")
	private PaClaimService paclaimService;

	@Resource(name = "pacopn.exchange.paCopnExchangeService")
	private PaCopnExchangeService paCopnExchangeService;

	@Resource(name = "pacommon.claim.paclaimService")
	private PaClaimService paClaimService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@Resource(name = "pacopn.goods.paCopnGoodsService")
	private PaCopnGoodsService paCopnGoodsService;
	
	/**
	 * 쿠팡 - 주문 데이터 생성
	 * @param hmSheet
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void orderInputAsync(HashMap<String, String> orderInputTarget, HttpServletRequest request) throws Exception{
		ParamMap paramMap = null;

		List<Object> orderInputTargetDtList = null;
		OrderInputVO[] orderInputVO         = null;
		HashMap<String, Object>[] resultMap = null;
		HashMap<String, String> orderMap    = null;
		HashMap<String, String> refusalMap  = null;

		String paOrderNo = orderInputTarget.get("PA_ORDER_NO").toString();

		int targetCnt   = Integer.parseInt(String.valueOf(orderInputTarget.get("TARGET_CNT")));
		int targetSize  = 0;
		int executedRtn = 0;

		try{
			orderInputTargetDtList = paCopnOrderService.selectOrderInputTargetDtList(orderInputTarget);
			targetSize = orderInputTargetDtList.size();
			
			if(targetSize < 1){
				throw processException("msg.no.select", new String[]{"selectOrderInputTargetDtList"});
			}else if(targetSize != targetCnt){
				throw processException("pa.cannot_dup_data", new String[]{"selectOrderInputTargetDtList"});
			}
			
			
			orderInputVO = new OrderInputVO[targetSize];
			
			for(int i=0; i<targetSize; i++){
				orderMap = (HashMap<String, String>) orderInputTargetDtList.get(i);
				
				orderInputVO[i] = new OrderInputVO();
				orderInputVO[i].setPaCode      (orderMap.get("PA_CODE").toString());
				orderInputVO[i].setMappingSeq  (orderMap.get("MAPPING_SEQ").toString());
				orderInputVO[i].setMediaCode   (orderMap.get("MEDIA_CODE").toString());
				orderInputVO[i].setOrderDate   (orderMap.get("ORDER_DATE").toString());
				orderInputVO[i].setGoodsCode   (orderMap.get("GOODS_CODE").toString());
				orderInputVO[i].setGoodsdtCode (orderMap.get("GOODSDT_CODE").toString());
				orderInputVO[i].setOrderQty    (Integer.parseInt(String.valueOf(orderMap.get("ORDER_QTY"))));
				orderInputVO[i].setApplyDate   (orderMap.get("APPLY_DATE").toString());
				orderInputVO[i].setRsaleAmt    (Double.parseDouble(String.valueOf(orderMap.get("RSALE_AMT"))));
				orderInputVO[i].setSupplyPrice (Double.parseDouble(String.valueOf(orderMap.get("SUPPLY_PRICE"))));
				orderInputVO[i].setSellerDcAmt (Long.parseLong(String.valueOf(orderMap.get("SELLER_DC_AMT")))); //상품가격 * 수량 - 실결제가 = ARS할인 + 일시불할인 + 제휴프로모션할인 + 즉시할인쿠폰
				orderInputVO[i].setCustName    (orderMap.get("CUST_NAME").toString());
				orderInputVO[i].setCustChar    ("99");
				orderInputVO[i].setCustTel1    (orderMap.get("CUST_TEL1").toString().replace("-", ""));
				orderInputVO[i].setCustTel2    (orderMap.get("CUST_TEL2").toString().replace("-", ""));
				orderInputVO[i].setReceiverName(orderMap.get("RECEIVER_NAME").toString());
				orderInputVO[i].setReceiverTel (orderMap.get("RECEIVER_TEL").toString().replace("-", ""));
				orderInputVO[i].setReceiverHp  (orderMap.get("RECEIVER_HP").toString().replace("-", ""));
				orderInputVO[i].setReceiverAddr(orderMap.get("RECEIVER_ADDR").toString());
				orderInputVO[i].setMsg         (orderMap.get("MSG").toString());
				orderInputVO[i].setPaGoodsCode (orderMap.get("PA_GOODS_CODE").toString());
				orderInputVO[i].setAddrGbn     ("02");
				orderInputVO[i].setStdAddr     (orderMap.get("RECEIVER_ADDR1").toString());
				orderInputVO[i].setStdAddrDT   (orderMap.get("RECEIVER_ADDR2").toString());
				orderInputVO[i].setPostNo      (orderMap.get("RECEIVER_ZIPCODE").toString());
				orderInputVO[i].setPostNoSeq   ("001");
				orderInputVO[i].setPaOrderCode (orderMap.get("PA_ORDER_NO").toString());
				orderInputVO[i].setShpFeeCost  (Long.parseLong(String.valueOf(orderMap.get("PA_SHPFEE_COST"))));
				orderInputVO[i].setReceiveMethod("62");
				orderInputVO[i].setProcUser    ("PACOPN");
				orderInputVO[i].setPriceSeq	   (orderMap.get("PRICE_SEQ").toString());
				orderInputVO[i].setDoFlag	   (orderMap.get("DO_FLAG").toString());
				orderInputVO[i].setLumpSumDcAmt(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_DC_AMT"))));
				orderInputVO[i].setLumpSumOwnDcAmt(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_OWN_DC_AMT"))));
				orderInputVO[i].setLumpSumEntpDcAmt(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_ENTP_DC_AMT"))));
				orderInputVO[i].setInstantCouponDiscount(Long.parseLong(String.valueOf(orderMap.get("INSTANT_COUPON_DISCOUNT"))));
				orderInputVO[i].setCouponPromoBdate(DateUtil.toTimestamp(orderMap.get("COUPON_PROMO_BDATE")));
				orderInputVO[i].setCouponPromoEdate(DateUtil.toTimestamp(orderMap.get("COUPON_PROMO_EDATE")));
				
				if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost())
						|| ("127.0.0.1").equals(request.getRemoteHost())) {
					orderInputVO[i].setIsLocalYn("Y");
				} else {
					orderInputVO[i].setIsLocalYn("N");
				}
				
				//전화번호 NULL 체크
				if("".equals( orderInputVO[i].getReceiverTel() ) || orderInputVO[i].getReceiverTel() == null ) { 
					paramMap = new ParamMap();
					paramMap.put("mappingSeq", orderMap.get("MAPPING_SEQ").toString());
					paramMap.put("resultCode", "999999");
					paramMap.put("resultMessage", "전화번호 NULL.");
					paramMap.put("createYn", "0");
					paOrderService.updatePaOrdermTx(paramMap);
					throw processException("pa.fail_order_input", new String[]{ "전화번호 NULL." });
				}
				
				// 가격 비교
				String paApplyDate = orderMap.get("APPLY_DATE").toString(); // TPAGOODSPRICE 기준 APPLY_DATE
				String stoaApplyDate = orderMap.get("STOA_APPLY_DATE").toString(); // TGOODSPRICE 기준 APPLY_DATE
				String paOrderDate = orderMap.get("ORDER_DATE").toString(); // 쿠팡 주문일시
				
				if(!paApplyDate.equals(stoaApplyDate)) {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					Date orderDate = format.parse(paOrderDate.substring(0, 8));
			        Date stoaDate = format.parse(stoaApplyDate.substring(0, 8));
			        
			        // 스토아 최신 가격 적용일자와 제휴 주문일자 차이 계산
			        long diffDays = orderDate.getTime() - stoaDate.getTime();
			        diffDays = diffDays / (24 * 60 * 60 * 1000);
			        
					if(diffDays >= 3) { // 3일 이상 차이나는 경우
						paramMap = new ParamMap();
						paramMap.put("mappingSeq", orderMap.get("MAPPING_SEQ").toString());
						paramMap.put("resultCode", "999999");
						paramMap.put("resultMessage", "가격 정보가 잘못되었습니다.");
						paramMap.put("createYn", "0");
						paOrderService.updatePaOrdermTx(paramMap);
						throw processException("pa.fail_order_input", new String[]{ "가격 정보가 잘못되었습니다." });
					}
				}
								
				//PROMOTION DATA SETTING 
				ParamMap promoParam = new ParamMap();
				promoParam.put("goodsCode"		  , orderMap.get("GOODS_CODE").toString());
				promoParam.put("paCode"			  , orderMap.get("PA_CODE").toString());
				promoParam.put("orderDate"		  , orderMap.get("ORDER_DATE").toString());
				promoParam.put("remark4N"		  , String.valueOf(orderMap.get("REMARK4_N")));
				promoParam.put("papromoAllowTerm" , orderInputTarget.get("PAPROMO_ALLOW_TERM"));
				promoParam.put("alcoutPromoYn"	  , "0");
				
				if(Double.parseDouble(String.valueOf(orderMap.get("OUT_PROMO_PRICE"))) > 0) {
					
				  OrderpromoVO orderPaPromo = new OrderpromoVO();
				  String promoNo = orderMap.get("PROMO_NO").toString();
				  double doAmt = Long.parseLong(String.valueOf(orderMap.get("OUT_PROMO_PRICE"))); 
				  double ownCost = Long.parseLong(String.valueOf(orderMap.get("OWN_COST")));
				  double entpCost = Long.parseLong(String.valueOf(orderMap.get("ENTP_COST")));
				  
				  orderPaPromo.setPromoNo(promoNo); 
				  orderPaPromo.setDoType("30");
				  orderPaPromo.setDoAmt(doAmt); 
				  orderPaPromo.setProcCost(doAmt);
				  orderPaPromo.setOwnProcCost(ownCost);
				  orderPaPromo.setEntpProcCost(entpCost);
				  orderPaPromo.setCouponPromoBdate(orderInputVO[i].getCouponPromoBdate());
				  orderPaPromo.setCouponPromoEdate(orderInputVO[i].getCouponPromoEdate());
				  orderPaPromo.setCouponYn("1"); 
				  orderPaPromo.setProcGb("I");
				  
				  orderInputVO[i].setOrderPaPromo(orderPaPromo);
			    }
			}

			try{
				resultMap = paOrderService.saveOrderTx(orderInputVO);
			}catch(Exception e){
				for(int j=0; j<targetSize; j++){
					paramMap = new ParamMap();
					paramMap.put("mappingSeq", orderInputVO[j].getMappingSeq());
					paramMap.put("resultCode", "999999");
					paramMap.put("resultMessage", e.getMessage());
					paramMap.put("createYn", "0");

					executedRtn = paOrderService.updatePaOrdermTx(paramMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
					}
				}

				throw processMessageException(e.getMessage());
			}
			
			for(int k=0; k<targetSize; k++){
				paramMap = new ParamMap();
				paramMap.setParamMap(resultMap[k]);
				paramMap.replaceCamel();

				//재고부족 or 판매불가 상태.
				if(paramMap.getString("resultCode").equals("100001")){
					ParamMap apiResult = new ParamMap();
					
					refusalMap = paCopnOrderService.selectRefusalInfo(paramMap.getString("mappingSeq"));
					//= 판매불가처리 호출.
					apiResult = reqSaleRefusalProc(refusalMap);

					if(apiResult.getInt("HTTP_RESPONSE_CODE") != 200){
						throw processException("msg.cannot_save", new String[] { "IF_PACOPNAPI_03_004 fail - " +  apiResult.getString("EXCEPTION_MESSAGE")});
					}else {
						//품절처리 성공 : 품절 상품 PAORDERM.PRE_CANCEL_YN = 1
						paOrderService.updatePreCancelOrder(paramMap.getString("mappingSeq"));
					}
					continue;
				}
			}
			
		}catch(Exception e){
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "PA_ORDER_NO: " + paOrderNo + " | " + e.getMessage());
		}finally{
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode"  , "PACOPN_ORDER_INPUT");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code"     , "500");
					paramMap.put("siteGb", "PACOPN");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}

	/**
	 * 상품준비중취소 API 호출 - IF_PACOPNAPI_03_003
	 * @param refusalMap
	 * @return
	 * @throws Exception
	 */
	public ParamMap reqSaleRefusalProc(HashMap<String, String> refusalMap) throws Exception {
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		ParamMap configMap = new ParamMap();
		Properties params = null;
		

		configMap.put("apiCode", "IF_PACOPNAPI_03_007");
		apiInfo = systemService.selectPaApiInfo(configMap);
		apiInfo.put("apiInfo", configMap.getString("apiCode"));
		
		params = new Properties();
		params.setProperty("paShipNo"     , refusalMap.get("PA_SHIP_NO").toString());
		params.setProperty("paOrderNo"    , refusalMap.get("PA_ORDER_NO").toString());
		params.setProperty("paOrderSeq"   , refusalMap.get("PA_ORDER_SEQ").toString());
		params.setProperty("vendorItemId" , refusalMap.get("VENDOR_ITEM_ID").toString());
		params.setProperty("shippingCount", String.valueOf(refusalMap.get("SHIPPING_COUNT")));
		params.setProperty("paCode"   	  , refusalMap.get("PA_CODE").toString());
		
		configMap.put("HTTP_CONFIG_ADDRESS", ConfigUtil.getString("OPEN_API_BASE_ADDRESS"));
		configMap.put("HTTP_CONFIG_PROTOCOL", ConfigUtil.getString("OPEN_API_BASE_PROTOCOL"));
		configMap.put("HTTP_CONFIG_PORT", ConfigUtil.getString("OPEN_API_BASE_PORT"));
		configMap.put("HTTP_CONFIG_TIME_OUT", 5000);
		configMap.put("HTTP_CONFIG_ADDRESS_DETAIL", apiInfo.get("INTERNAL_URL"));
		configMap.put("HTTP_CONFIG_CONTENT_TYPE", "application/x-www-form-urlencoded;charset=utf-8");

		configMap = HttpUtil.getGetHttpClient(configMap, params);

		return configMap;
	}
	
	/**
	 * 쿠팡 - 교환 데이터 생성
	 * @param hmSheet
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void orderChangeAsync(HashMap<String, Object> hmSheet, HttpServletRequest request) throws Exception {
		HashMap<String, Object>[] resultMap = null;
		HashMap<String, String> changeMap = null;
		HashMap<String, String> rejectMap = null;
		ParamMap apiResult = null;
		ParamMap paramMap = null;
		int targetSize = 0;
		int executedRtn = 0;

		paramMap = new ParamMap();
		paramMap.setParamMap(hmSheet);
		paramMap.replaceCamel();
		
		try {
			List<Object> orderChangeTargetDtList = paCopnExchangeService.selectOrderChangeTargetDtList(paramMap);
			targetSize = orderChangeTargetDtList.size();
			if(targetSize != 2){
				throw processException("msg.no.select", new String[] { "selectOrderChangeTargetDtList" });
			}

			OrderClaimVO[] orderClaimVO = new OrderClaimVO[targetSize];
			for(int j = 0; targetSize > j; j++){
				changeMap = (HashMap<String, String>) orderChangeTargetDtList.get(j);
				orderClaimVO[j] = new OrderClaimVO();
				orderClaimVO[j].setMappingSeq(changeMap.get("MAPPING_SEQ").toString());
				orderClaimVO[j].setClaimGb(changeMap.get("CLAIM_GB").toString());
				orderClaimVO[j].setOrderNo(changeMap.get("ORDER_NO").toString());
				orderClaimVO[j].setOrderGSeq(changeMap.get("ORDER_G_SEQ").toString());
				orderClaimVO[j].setExchGoodsdtCode(changeMap.get("EXCH_GOODSDT_CODE").toString());
				orderClaimVO[j].setClaimQty(Integer.parseInt(String.valueOf(changeMap.get("CLAIM_QTY"))));
				orderClaimVO[j].setClaimCode(changeMap.get("CLAIM_CODE").toString());
				orderClaimVO[j].setClaimType(changeMap.get("CLAIM_TYPE").toString());
				orderClaimVO[j].setClaimDesc(changeMap.get("CLAIM_DESC").toString());
				orderClaimVO[j].setShipcostChargeYn(changeMap.get("SHIPCOST_CHARGE_YN").toString());
				orderClaimVO[j].setAdminProcYn(changeMap.get("ADMIN_PROC_YN").toString());
				
				Long shpFeeAmt = Long.parseLong(String.valueOf(changeMap.get("PA_SHPFEE_COST")));
				if (shpFeeAmt > 0 ){
					orderClaimVO[j].setShpfeeYn("1");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
				}else{
					orderClaimVO[j].setShpfeeYn("0");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2
				}
				orderClaimVO[j].setShpFeeAmt(shpFeeAmt);
				
				if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) ||("127.0.0.1").equals(request.getRemoteHost())){
					orderClaimVO[j].setLocalYn("Y");
				}else{
					orderClaimVO[j].setLocalYn("N");
				}
				orderClaimVO[j].setInsertId     ("PACOPN");

				if(changeMap.get("CLAIM_GB").toString().equals("40")) {
					orderClaimVO[j].setReturnName(changeMap.get("RECEIVER_NAME").toString());
					orderClaimVO[j].setReturnTel(changeMap.get("RECEIVER_TEL").toString().replace("-", ""));
					orderClaimVO[j].setReturnHp(changeMap.get("RECEIVER_HP").toString().replace("-", ""));
					orderClaimVO[j].setReturnAddr(changeMap.get("RECEIVER_ADDR").toString());
					orderClaimVO[j].setCustDelyYn("0"); //= 교환배송건은 0 고정.
					orderClaimVO[j].setReturnDelyGb(""); //= 교환배송건은 "" 고정.
					orderClaimVO[j].setReturnSlipNo(""); //= 교환배송건은 "" 고정.
					orderClaimVO[j].setRcvrMailNo(changeMap.get("RCVR_MAIL_NO").toString());
					orderClaimVO[j].setRcvrMailNoSeq("001");
					orderClaimVO[j].setRcvrBaseAddr	(changeMap.get("RCVR_BASE_ADDR").toString());
					orderClaimVO[j].setRcvrDtlsAddr	(changeMap.get("RCVR_DTLS_ADDR").toString());
					orderClaimVO[j].setRcvrTypeAdd	("01");
				} else {
					orderClaimVO[j].setReturnName(changeMap.get("RETURN_NAME").toString());
					orderClaimVO[j].setReturnTel(changeMap.get("RETURN_TEL").toString().replace("-", ""));
					orderClaimVO[j].setReturnHp(changeMap.get("RETURN_HP").toString().replace("-", ""));
					orderClaimVO[j].setReturnAddr(changeMap.get("RETURN_ADDR").toString());
					orderClaimVO[j].setCustDelyYn("0"); //= 고객 직접발송일 경우 : 1, 아닐경우 : 0
					orderClaimVO[j].setReturnDelyGb(""); //= 직접발송일 경우 G마켓 고객입력 배송사코드.
					orderClaimVO[j].setReturnSlipNo(""); //= 직접발송일 경우 G마켓 고객입력 운송장번호.
					orderClaimVO[j].setRcvrMailNo(changeMap.get("EXCH_MAIL_NO").toString());
					orderClaimVO[j].setRcvrMailNoSeq("001");
					orderClaimVO[j].setRcvrBaseAddr	(changeMap.get("EXCH_BASE_ADDR").toString());
					orderClaimVO[j].setRcvrDtlsAddr	(changeMap.get("EXCH_DTLS_ADDR").toString());
					orderClaimVO[j].setRcvrTypeAdd	("01");
				}
				
	            // 전화번호 null인 경우 교환데이터 미생성 처리
				if(!StringUtils.hasText(orderClaimVO[j].getReturnTel()) && !StringUtils.hasText(orderClaimVO[j].getReturnHp())){
					throw processException("msg.cannot_save", new String[] { "전화번호 NULL" });
				}
				
				//제휴 인입 주소 확인
				String checkAddr = "";
				paramMap.put("exchangeId", changeMap.get("EXCHANGE_ID").toString());
				paramMap.put("claimGb",changeMap.get("CLAIM_GB").toString());
				checkAddr = paCopnClaimService.compareExAddress(paramMap);
				orderClaimVO[j].setSameAddr(checkAddr);
				
			}

			try {
				resultMap = paclaimService.saveOrderChangeTx(orderClaimVO);
			} catch (Exception e) {
				for(int j = 0; targetSize > j; j++){
					paramMap = new ParamMap();
					paramMap.put("mappingSeq", orderClaimVO[j].getMappingSeq());
					paramMap.put("resultCode", "999999");
					paramMap.put("resultMessage", e.getMessage());
					paramMap.put("createYn", "0");

					executedRtn = paOrderService.updatePaOrdermTx(paramMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
					}
				}
				throw processMessageException(e.getMessage());
			}

			// 재고부족으로 인한 교환거부 처리
			paramMap = new ParamMap();
			paramMap.setParamMap(resultMap[0]);
			paramMap.replaceCamel();

			if(paramMap.getString("resultCode").equals("100001")){
				rejectMap = new HashMap<String, String>();
				rejectMap.put("exchangeId", changeMap.get("EXCHANGE_ID").toString());
				rejectMap.put("exchangeRejectCode", "SOLDOUT");
				rejectMap.put("paCode", changeMap.get("PA_CODE").toString());
				// 교환요청 거부 처리
				apiResult = reqExchangeRejectProc(rejectMap);

				if(apiResult.getInt("HTTP_RESPONSE_CODE") != 200){
					throw processException("msg.cannot_save", new String[] { "IF_PACOPNAPI_03_016 fail - " +  apiResult.getString("EXCEPTION_MESSAGE")});
				}
			}

		} catch (Exception e) {
			paramMap.put("message", "pa_claim_no : " + hmSheet.get("PA_CLAIM_NO") + " > " + e.toString());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", "PACOPN_ORDER_CHANGE");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code", "500");
					paramMap.put("siteGb", "PACOPN");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}

	/**
	 * 교환요청 거부 처리 API 호출
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public ParamMap reqExchangeRejectProc(HashMap<String, String> reqMap) throws Exception {
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		ParamMap configMap = new ParamMap();
		Properties params = null;

		configMap.put("apiCode", "IF_PACOPNAPI_03_016");
		apiInfo = systemService.selectPaApiInfo(configMap);
		apiInfo.put("apiInfo", configMap.getString("apiCode"));

		params = new Properties();
		params.setProperty("exchangeId", reqMap.get("exchangeId").toString());
		params.setProperty("exchangeRejectCode", reqMap.get("exchangeRejectCode").toString());
		params.setProperty("paCode", reqMap.get("paCode"));

		configMap.put("HTTP_CONFIG_ADDRESS", ConfigUtil.getString("OPEN_API_BASE_ADDRESS"));
		configMap.put("HTTP_CONFIG_PROTOCOL", ConfigUtil.getString("OPEN_API_BASE_PROTOCOL"));
		configMap.put("HTTP_CONFIG_PORT", ConfigUtil.getString("OPEN_API_BASE_PORT"));
		configMap.put("HTTP_CONFIG_TIME_OUT", 5000);
		configMap.put("HTTP_CONFIG_ADDRESS_DETAIL", apiInfo.get("INTERNAL_URL"));
		configMap.put("HTTP_CONFIG_CONTENT_TYPE", "application/x-www-form-urlencoded;charset=utf-8");

		configMap = HttpUtil.getGetHttpClient(configMap, params);

		return configMap;
	}

	/**
	 * 쿠팡 - 교환취소 데이터 생성
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void orderChangeCancelAsync(HashMap<String, Object> cancelMap, HttpServletRequest request) throws Exception {
		HashMap<String, Object> hmSheet = null;
		ParamMap paramMap = null;
		ParamMap preCancelMap = null;
		int targetSize = 0;
		int executedRtn = 0;

		try {
			paramMap = new ParamMap();
			paramMap.setParamMap(cancelMap);
			paramMap.replaceCamel();

			List<Object> orderChangeTargetDtList = paCopnExchangeService.selectOrderChangeCancelTargetDtList(paramMap);
			targetSize = orderChangeTargetDtList.size();
			if(targetSize != 2){
				throw processException("msg.no.select", new String[] { "selectChangeCancelTargetDtList" });
			}

			OrderClaimVO[] orderClaimVO = new OrderClaimVO[targetSize];
			for(int i = 0; targetSize > i; i++){
				hmSheet = (HashMap<String, Object>) orderChangeTargetDtList.get(i);

				if(hmSheet.get("PRE_CANCEL_YN").toString().equals("0")) {
					orderClaimVO[i] = new OrderClaimVO();
					orderClaimVO[i].setMappingSeq(hmSheet.get("MAPPING_SEQ").toString());
					orderClaimVO[i].setClaimGb(hmSheet.get("CLAIM_GB").toString());
					orderClaimVO[i].setOrderNo(hmSheet.get("ORDER_NO").toString());
					orderClaimVO[i].setOrderGSeq(hmSheet.get("ORDER_G_SEQ").toString());
					orderClaimVO[i].setOrderDSeq(hmSheet.get("ORDER_D_SEQ").toString());
					orderClaimVO[i].setOrderWSeq(hmSheet.get("ORDER_W_SEQ").toString());
					orderClaimVO[i].setClaimQty(Integer.parseInt(String.valueOf(hmSheet.get("CLAIM_QTY"))));
					orderClaimVO[i].setClaimCode(hmSheet.get("CLAIM_CODE").toString());
					orderClaimVO[i].setShipcostChargeYn(hmSheet.get("SHIPCOST_CHARGE_YN").toString());
					orderClaimVO[i].setInsertId("PACOPN");
				} else {
			        //= 기취소건 처리(반품생성 이전 취소건)
			        preCancelMap = new ParamMap();
			        preCancelMap.setParamMap(hmSheet);
			        preCancelMap.replaceCamel();

			        //=pa.before_claim_create_cancel = 교환생성 이전 취소건
			        preCancelMap.put("preCancelReason", getMessage("pa.before_change_create_cancel"));
			        executedRtn = paCopnExchangeService.updatePreCancelYnTx(preCancelMap);
			        if(executedRtn != 1){
			        	throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
			        }
				}
			}

			try {
				paclaimService.saveChangeCancelTx(orderClaimVO);
			} catch (Exception e) {
				for(int j = 0; targetSize > j; j++){
					paramMap = new ParamMap();
					paramMap.put("mappingSeq", orderClaimVO[j].getMappingSeq());
					paramMap.put("resultCode", "999999");
					paramMap.put("resultMessage", e.getMessage());
					paramMap.put("createYn", "0");

					executedRtn = paOrderService.updatePaOrdermTx(paramMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
					}
				}

				throw processMessageException(e.getMessage());
			}

		} catch (Exception e) {
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "pa_claim_no : " + cancelMap.get("PA_CLAIM_NO") + " > " + e.toString());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PACOPN_CHANGE_CANCEL");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					paramMap.put("siteGb", "PACOPN");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	/**
	 * 쿠팡 - 취소 데이터 생성
	 * @param cancelMap
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void cancelInputAsync(HashMap<String, Object> cancelMap, HttpServletRequest request) throws Exception{
		List<Object> cancelInputTargetDtList        = null;
		HashMap<String, Object> cancelInputTargetDt = null;
		ParamMap paramMap     = null;
		CancelInputVO cancelInputVO = null;
		
		int executedRtn = 0;
		
		try{
			paramMap = new ParamMap();
			paramMap.setParamMap(cancelMap);
			paramMap.replaceCamel();
			
			cancelInputTargetDtList = paCopnClaimService.selectCancelInputTargetDtList(paramMap);
			if(cancelInputTargetDtList.size() != 1){
				throw processException("errors.api.system", new String[] { "selectCancelInputTargetDtList" });
			}
			
			cancelInputTargetDt = (HashMap<String, Object>) cancelInputTargetDtList.get(0);
			
			cancelInputVO = new CancelInputVO();
			
			cancelInputVO.setMappingSeq  (cancelInputTargetDt.get("MAPPING_SEQ").toString());
			cancelInputVO.setOrderNo     (cancelInputTargetDt.get("ORDER_NO").toString());
			cancelInputVO.setOrderGSeq   (cancelInputTargetDt.get("ORDER_G_SEQ").toString());
			cancelInputVO.setCancelQty   (Integer.parseInt(String.valueOf(cancelInputTargetDt.get("PA_PROC_QTY"))));
			cancelInputVO.setCancelCode  (cancelInputTargetDt.get("CANCEL_CODE").toString());
			cancelInputVO.setProcId("PACOPN");
			try{
				paOrderService.saveCancelTx(cancelInputVO);
			}catch(Exception e){
				paramMap = new ParamMap();
				paramMap.put("mappingSeq", cancelInputVO.getMappingSeq());
				paramMap.put("resultCode", "999999");
				paramMap.put("resultMessage", e.getMessage());
				paramMap.put("createYn", "0");
				
				executedRtn = paOrderService.updatePaOrdermTx(paramMap);
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
				}
			
				throw processMessageException(e.getMessage());
			}
		}catch(Exception e){
			paramMap.put("message", "PA_CLAIM_NO: " + cancelMap.get("PA_CLAIM_NO") + " ,MSG: " + e.getMessage());
		}finally{
			try{
				if(paramMap != null && !"".equals(paramMap.getString("message"))){
					paramMap.put("apiCode"  , "PACOPN_CANCEL_INPUT");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code"     , "500");
					paramMap.put("siteGb", "PACOPN");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception e){
				log.error("[ApiTrackingTx Error] " + e.getMessage());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	/**
	 * 쿠팡 - 반품 데이터 생성
	 * @param claimMap
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "serial" })
	public void orderClaimAsync(HashMap<String, Object> claimMap, HttpServletRequest request) throws Exception{
		List<Object> delivery                       = null;
		List<Object> orderClaimTargetDtList         = null;
		List<HashMap<String, Object>> mailAlertList = null;
		HashMap<String, Object> orderClaimTargetDt  = null;
		HashMap<String, Object> resultMap           = null;
		ParamMap paramMap  = null;
		OrderClaimVO orderClaimVO = null;
		
		Gson gson = new Gson();
		
		String delyGb = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		int executedRtn = 0;
		
		try{
			paramMap = new ParamMap();
			
			paramMap.setParamMap(claimMap);
			paramMap.replaceCamel();
			
			orderClaimTargetDtList = paCopnClaimService.selectOrderClaimTargetDtList(paramMap);
			
			if(orderClaimTargetDtList.size() != 1){
				throw processException("errors.api.system", new String[] { "selectOrderCalimTargetDtList" });
			}
			
			orderClaimTargetDt = (HashMap<String, Object>) orderClaimTargetDtList.get(0);
			
			orderClaimVO = new OrderClaimVO();
			
			orderClaimVO.setMappingSeq(orderClaimTargetDt.get("MAPPING_SEQ").toString());
			orderClaimVO.setOrderNo   (orderClaimTargetDt.get("ORDER_NO").toString());
			orderClaimVO.setOrderGSeq (orderClaimTargetDt.get("ORDER_G_SEQ").toString());
			orderClaimVO.setClaimQty  (Integer.parseInt(String.valueOf(orderClaimTargetDt.get("PA_PROC_QTY"))));
			
			//기존주소와 같은지 확인
			String checkAddr = "";
			paramMap.put("receiptId", orderClaimTargetDt.get("CLAIM_ID").toString());
			checkAddr = paCopnClaimService.compareAddress(paramMap);
			orderClaimVO.setSameAddr(checkAddr);
			
			String claimCode = orderClaimTargetDt.get("CLAIM_CODE").toString();
			if (claimCode == null || ("").equals(claimCode)  || claimCode.length() != 6){
				
				if(orderClaimVO.getOutBefClaimGb().equals("1")){  //출하지시전 반품, 취소에 걸려있음..
					claimCode = "630599" ; //Defalut;
					
				}else{
					claimCode = "620599" ; //Defalut;							
				}
			}
			
			paramMap.put("orderNo", orderClaimTargetDt.get("ORDER_NO").toString());
			
			orderClaimVO.setCsLgroup(claimCode.substring(0,2));
			orderClaimVO.setCsMgroup(claimCode.substring(2,4));
			orderClaimVO.setCsSgroup(claimCode.substring(4,6));
			orderClaimVO.setCsLmsCode(claimCode);
			orderClaimVO.setClaimCode ("999");
			
			orderClaimVO.setClaimGb   (orderClaimTargetDt.get("CLAIM_TYPE").toString());
			orderClaimVO.setClaimDesc (orderClaimTargetDt.get("CLAIM_DESC").toString());
			orderClaimVO.setReturnName(orderClaimTargetDt.get("RETURN_NAME").toString());
			orderClaimVO.setReturnTel (orderClaimTargetDt.get("RETURN_HP").toString().replace("-", ""));
			orderClaimVO.setReturnHp  (orderClaimTargetDt.get("RETURN_HP").toString().replace("-", ""));
			orderClaimVO.setReturnAddr(orderClaimTargetDt.get("RETURN_ADDR").toString());
			orderClaimVO.setCustDelyYn(orderClaimTargetDt.get("CUST_DELY_YN").toString());
			
			orderClaimVO.setRcvrBaseAddr  (orderClaimTargetDt.get("REQUESTER_ADDRESS").toString());
			orderClaimVO.setRcvrDtlsAddr(orderClaimTargetDt.get("REQUESTER_ADDRESS_DETAIL").toString());
			orderClaimVO.setRcvrMailNo    (orderClaimTargetDt.get("REQUESTER_ZIP_CODE").toString());
			
			String rcvrMailNoSeq;
			if(orderClaimTargetDt.get("RCVR_MAIL_NO_SEQ") == null || "".equals(orderClaimTargetDt.get("RCVR_MAIL_NO_SEQ").toString())){
				rcvrMailNoSeq = "001";
			}
			else {
				rcvrMailNoSeq = orderClaimTargetDt.get("RCVR_MAIL_NO_SEQ").toString();
			}
			
			orderClaimVO.setRcvrMailNoSeq(rcvrMailNoSeq);
			orderClaimVO.setRcvrTypeAdd("");
			
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost())
					|| ("127.0.0.1").equals(request.getRemoteHost())) {
				orderClaimVO.setLocalYn("Y");
			} else {
				orderClaimVO.setLocalYn("N");
			}
			
			orderClaimVO.setOutBefClaimGb(orderClaimTargetDt.get("OUT_BEF_CLAIM_GB").toString());
			orderClaimVO.setInsertId("PACOPN");
			
			if("1".equals(orderClaimVO.getCustDelyYn())){
				delivery = gson.fromJson(orderClaimTargetDt.get("RETURN_DELIVERY_DTOS").toString(), new TypeToken<ArrayList<JsonObject>>(){}.getType());
				
				try {
					delyGb = paCopnClaimService.selectClaimDelyGb(((JsonObject)delivery.get(0)).get("deliveryCompanyCode").isJsonNull() ? "" : ((JsonObject)delivery.get(0)).get("deliveryCompanyCode").getAsString());
				} catch (Exception ex) {
					delyGb = "99";
				}
				
				if(delyGb == null || delyGb.isEmpty()){
					delyGb = "99";
				}
				
				orderClaimVO.setReturnDelyGb(delyGb);
				orderClaimVO.setReturnSlipNo(((JsonObject)delivery.get(0)).get("deliveryInvoiceNo").isJsonNull() ? "" : ((JsonObject)delivery.get(0)).get("deliveryInvoiceNo").getAsString());
			}else{
				orderClaimVO.setReturnDelyGb("");
				orderClaimVO.setReturnSlipNo("");
			}
			
			Long shpFee = ComUtil.objToLong((orderClaimTargetDt.get("SHIP_FEE")));
			if (shpFee > 0 ){
				orderClaimVO.setShpfeeYn("1");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
			}else{
				orderClaimVO.setShpfeeYn("0");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2					
			}
			
			/* 출고중지요청 건은 모두 반품 배송비 0원처리 */
			if("03".equals(orderClaimTargetDt.get("FAULT_BY_TYPE").toString())){
				orderClaimVO.setShipcostChargeYn(orderClaimTargetDt.get("SHIPCOST_CHARGE_YN").toString());
			}else{
				orderClaimVO.setShipcostChargeYn("0");
			}
			
			//출고전 반품처리 배송비 무상처리
			if(("1").equals(orderClaimVO.getOutBefClaimGb())){
				orderClaimVO.setShpfeeYn("0");
				orderClaimVO.setIs20Claim(true);
			}else{
				orderClaimVO.setIs20Claim(false);
			}
			
			// 전화번호 null인 경우 반품데이터 미생성 처리
			if(!StringUtils.hasText(orderClaimVO.getReturnTel()) && !StringUtils.hasText(orderClaimVO.getReturnHp())){
				throw processException("msg.cannot_save", new String[] { "전화번호 NULL" });
			}
						
			try{				
				resultMap = paClaimService.saveOrderClaimTx(orderClaimVO);
				
				/* 미사용
				if(Constants.SAVE_SUCCESS.equals(resultMap.get("RESULT_CODE").toString())){
					paramMap = new ParamMap();
					
					paramMap.put("mappingSeq", orderClaimVO.getMappingSeq());
					paramMap.put("paDoFlag", "20");
					
					executedRtn = paCopnClaimService.updatePaDoFlag(paramMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - PaDoFlag" });
					}
				}
				*/
				
				/* 업체 회수 담당자에게 메일 전송 */
				if (Constants.SAVE_SUCCESS.equals(resultMap.get("RESULT_CODE").toString())) {
					mailAlertList = paCopnClaimService.selectMailAlertEntpList(paramMap);
					
					if (mailAlertList.size() > 0) {
						for (HashMap<String, Object> mailAlert : mailAlertList) {
							ParamMap mailMap = new ParamMap();
							
							mailMap.put("orderNo"	   , paramMap.get("orderNo").toString());
							mailMap.put("paOrderNo"	   , paramMap.get("paOrderNo").toString());
							mailMap.put("paClaimNo"	   , paramMap.get("paClaimNo").toString());
							mailMap.put("paGroupCode"  , mailAlert.get("PA_GROUP_CODE").toString());
							mailMap.put("entpManName"  , mailAlert.get("ENTP_MAN_NAME").toString());
							mailMap.put("goodsCode"	   , mailAlert.get("GOODS_CODE").toString());
							mailMap.put("emailAddr"    , mailAlert.get("EMAIL_ADDR").toString());
							mailMap.put("returnDueDate", mailAlert.get("RETURN_DUE_DATE").toString());
							
							rtnMsg = paCopnClaimService.saveMailAlertTx(mailMap);
						}
					}
				}
			}catch(Exception e){
				paramMap = new ParamMap();
				
				paramMap.put("mappingSeq", orderClaimVO.getMappingSeq());
				paramMap.put("resultCode", "999999");
				paramMap.put("resultMessage", e.getMessage());
				paramMap.put("createYn", "0");
				
				executedRtn = paOrderService.updatePaOrdermTx(paramMap);
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
				}
			
				throw processMessageException(e.getMessage());
			}
		}catch(Exception e){
			paramMap.put("message", "PA_CLAIM_NO: " + claimMap.get("PA_CLAIM_NO") + " ,MSG: " + e.getMessage());
		}finally{
			try{
				if(paramMap != null && !"".equals(paramMap.getString("message"))){
					paramMap.put("apiCode"  , "PACOPN_CLAIM_INPUT");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code"     , "500");
					paramMap.put("siteGb", "PACOPN");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception e){
				log.error("[ApiTrackingTx Error] " + e.getMessage());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	/**
	 * 쿠팡 - 반품철회 데이터 생성
	 * @param claimMap
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void claimCancelAsync(HashMap<String, Object> claimMap, HttpServletRequest request) throws Exception{
		List<Object> claimCancelTargetDtList   = null;
		HashMap<String, Object> claimCancelMap = null;
		ParamMap paramMap = null;
		
		OrderClaimVO orderClaimVO = null;
		
		int executedRtn = 0;
		
		try{
			paramMap = new ParamMap();
			
			paramMap.setParamMap(claimMap);
			paramMap.replaceCamel();
			
			claimCancelTargetDtList = paCopnClaimService.selectClaimCancelTargetDtList(paramMap);
			if(claimCancelTargetDtList.size() != 1){
				throw processException("msg.no.select", new String[] { "selectClaimCancelTargetDtList" });
			}
			
			claimCancelMap = (HashMap<String, Object>) claimCancelTargetDtList.get(0);
			
			orderClaimVO = new OrderClaimVO();
			
			orderClaimVO.setMappingSeq  (claimCancelMap.get("MAPPING_SEQ").toString());
			orderClaimVO.setOrderNo     (claimCancelMap.get("ORDER_NO").toString());
			orderClaimVO.setOrderGSeq   (claimCancelMap.get("ORDER_G_SEQ").toString());
			orderClaimVO.setOrderWSeq   (claimCancelMap.get("ORDER_W_SEQ").toString());
			orderClaimVO.setClaimQty    (Integer.parseInt(String.valueOf(claimCancelMap.get("PA_PROC_QTY"))));
			orderClaimVO.setClaimCode   (claimCancelMap.get("CANCEL_CODE").toString());
			orderClaimVO.setInsertId("PACOPN");
			
			try{
				paClaimService.saveClaimCancelTx(orderClaimVO);
			}catch(Exception e){
				paramMap = new ParamMap();
				paramMap.put("mappingSeq", orderClaimVO.getMappingSeq());
				paramMap.put("resultCode", "999999");
				paramMap.put("resultMessage", e.getMessage());
				paramMap.put("createYn", "0");
				
				executedRtn = paOrderService.updatePaOrdermTx(paramMap);
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
				}
				
				throw processMessageException(e.getMessage());
			}
		}catch(Exception e){
			paramMap.put("message", "PA_CLAIM_NO: " + claimMap.get("PA_CLAIM_NO") + " ,MSG: " + e.getMessage());
		}finally{
			try{
				if(paramMap != null && !"".equals(paramMap.getString("message"))){
					paramMap.put("apiCode"  , "PACOPN_CLAIM_CANCEL");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code"     , "500");
					paramMap.put("siteGb", "PACOPN");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception e){
				log.error("[ApiTrackingTx Error] " + e.getMessage());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	@Async
	public void asyncGoodsModify(HttpServletRequest request, JsonObject root, HashMap<String, String> apiInfo, String apiKeys[], ParamMap asyncMap,
			PaCopnGoodsVO paCopnGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception{
		
		JsonObject responseObject = new JsonObject();
		String request_type = "PUT";
		String dateTime = systemService.getSysdatetimeToString();
		String procId = "PACOPN";
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dtMessage = "";
		PaGoodsTransLog paGoodsTransLog = null;
		
		try {
			
			log.info("09-1. 쿠팡 상품정보수정 API 호출");
			responseObject = ComUtil.callPaCopnAPI(apiInfo, asyncMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0]))
					, request_type, new GsonBuilder().create().toJson(root.get("insert")));
			
			if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())) {
				asyncMap.put("code", "200");
				asyncMap.put("message", responseObject.get("code").getAsString());
				asyncMap.put("resultCode", "200");
				asyncMap.put("resultMsg","OK");
				
				//전송관리 테이블 저장
				paGoodsTransLog = new PaGoodsTransLog();
				paGoodsTransLog.setGoodsCode(paCopnGoods.getGoodsCode());
				paGoodsTransLog.setPaCode(paCopnGoods.getPaCode());
				paGoodsTransLog.setItemNo(paCopnGoods.getSellerProductId()); // 상품코드
				paGoodsTransLog.setRtnCode(asyncMap.getString("code"));
				paGoodsTransLog.setRtnMsg(asyncMap.getString("message"));
				paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
				paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paGoodsTransLog.setProcId(procId);
				paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
				
				log.info("09-2.제휴사 상품정보 저장");
				// TPACOPNGOODS에 결과 UPDATE
				paCopnGoods.setSellerProductId(responseObject.get("data").getAsString()); // 쿠팡 상품번호 
				paCopnGoods.setApprovalStatus("15");
				paCopnGoods.setModifyId(procId);
				paCopnGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				
				// 수정 : 제휴사 TPACOPNGOODS 테이블만 UPDATE 하도록
				rtnMsg = paCopnGoodsService.savePaCopnGoodsTx(paCopnGoods, goodsdtMapping, paPromoTargetList);
				
				if(!rtnMsg.equals("000000")){
					asyncMap.put("code","500");
					asyncMap.put("message",paCopnGoods.getGoodsCode() + rtnMsg);
				} else {
					asyncMap.put("code","200");
					asyncMap.put("message","OK");
				}
				// 상품정보조회
				//다음 상품 수정때 조회되게 처리
				//responseMsg = paOptionCodeSave(request, paCopnGoods.getGoodsCode(), paCopnGoods.getSellerProductId(), "approvalStatus", procId, paCopnGoods.getPaCode());
				
			} else {
				asyncMap.put("code", "500");
				asyncMap.put("message", paCopnGoods.getGoodsCode() + responseObject.get("message").getAsString());
				
				//전송관리 테이블 저장
				paGoodsTransLog = new PaGoodsTransLog();
				paGoodsTransLog.setGoodsCode(paCopnGoods.getGoodsCode());
				paGoodsTransLog.setPaCode(paCopnGoods.getPaCode());
				paGoodsTransLog.setItemNo(paCopnGoods.getSellerProductId()); // 상품코드
				paGoodsTransLog.setRtnCode(asyncMap.getString("code"));
				paGoodsTransLog.setRtnMsg(asyncMap.getString("message").length() > 900 ? asyncMap.getString("message").substring(0, 900) : asyncMap.getString("message"));
				paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
				paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paGoodsTransLog.setProcId(procId);
				
				paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
				
				if((responseObject.get("message").getAsString().indexOf("고시정보") > -1) || (responseObject.get("message").getAsString().indexOf("배송비") > -1)) {
					dtMessage = responseObject.get("message").getAsString().indexOf("고시정보") > -1 ? "해당 카테고리에 사용할 수 없는 정보고시 입니다." : "쿠팡 배송비 설정 기준 불만족[30,90]";
	    			paCopnGoods.setPaSaleGb("30"); //판매중지
	    			paCopnGoods.setPaStatus("90"); //연동제외
	    			paCopnGoods.setTransSaleYn("1");
	    			paCopnGoods.setReturnNote(dtMessage);
	    			paCopnGoods.setModifyId(procId);
					paCopnGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					
					log.info("05.제휴사 상품정보 저장");
					rtnMsg = paCopnGoodsService.savePaCopnGoodsFailTx(paCopnGoods);
	    		} else if(responseObject.get("message").getAsString().indexOf("판매중인 상품은 삭제할 수 없습니다") > -1) {
	    			paCopnGoods.setApprovalStatus("15");
	    			paCopnGoods.setReturnNote("판매중인 상품은 삭제할 수 없습니다");
	    			paCopnGoods.setModifyId(procId);
	    			paCopnGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
	    			
	    			log.info("05.제휴사 상품정보 저장");
	    			rtnMsg = paCopnGoodsService.savePaCopnGoodsFailTx(paCopnGoods);
	    		}
			}
		} catch (Exception e) {
			asyncMap.put("code", "500");
			asyncMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		} finally{
			if(!"200".equals(asyncMap.get("code").toString())) {
				systemService.insertApiTrackingTx(request, asyncMap);			
			}
		}	
	}
	
	@Async
	public void spPagoodsSyncCopn(HttpServletRequest request, String goodsCode, String userId) throws Exception{
		ParamMap paramMap = new ParamMap();
		ParamMap stopSaleParam = new ParamMap();
		ParamMap stopShipParam = new ParamMap();
		List<PaGoodsImage> curImageInfo = null;
		List<PaGoodsPriceVO> curPriceInfo = null;
		List<PaEntpSlip> curEntpSlipInfo = null;
		HashMap<String, String> minMarginPrice = new HashMap<String, String>();
		List<PaCustShipCostVO> curShipCostInfo = null;
		List<HashMap<String, String>> curShipStopSale = null;
		List<HashMap<String, String>> curSaleStop = null;
		List<HashMap<String, String>> curEventMargin = null;
		
		String resultMsg = "";
		String dateTime = systemService.getSysdatetimeToString();
		StringBuffer sb = null;
		int conditionDay = 2;		
		
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paGroupCode", "05");
		paramMap.put("dateTime", dateTime);
		paramMap.put("conditionDay", conditionDay);		
		paramMap.put("comparePaCode", "51");
		paramMap.put("feeCode", "O669");
		paramMap.put("minMarginCode", "60");
		paramMap.put("minPriceCode", "61");
		
		paramMap.put("siteGb", userId);
		paramMap.put("apiCode", "IF_PACOMMON_00_002");
		
		log.info("Step1. 쿠팡 상품이미지 동기화 START");
		curImageInfo = paCommonService.selectCurImageInfoList(paramMap);
		if(curImageInfo.size() > 0) {
			sb = new StringBuffer();
			for(PaGoodsImage curImageInfoTarget : curImageInfo) {
				//이미지 없는 케이스가 많아 for문안에 try catch 처리
				try {
					curImageInfoTarget.setModifyId("PACOPN");
					curImageInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					curImageInfoTarget.setRemark(dateTime);
					resultMsg = paCommonService.saveCurImageInfoTx(curImageInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step1 상품이미지 동기화 쿠팡 Fail > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
						sb.append(curImageInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step1 상품이미지 동기화 쿠팡 Sucess > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
				} catch(Exception e) {
					log.info("Step1. 쿠팡 상품이미지 동기화 : " + e.getMessage());
					paramMap.put("code", "500");
					paramMap.put("message", "Step1. 쿠팡 상품이미지 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}
		}
		log.info("Step1. 쿠팡 상품이미지 동기화 END");
		
		log.info("Step2. 쿠팡 상품가격 동기화 START");
		curPriceInfo = paCommonService.selectCurPriceInfoList(paramMap);
		if(curPriceInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaGoodsPriceVO curPriceInfoTarget : curPriceInfo) {
					
					paramMap.put("paCode", curPriceInfoTarget.getPaCode());
					minMarginPrice = paCommonService.selectMinMarginPrice(paramMap);
					
					if( (ComUtil.objToDouble(curPriceInfoTarget.getMarginRate()) < ComUtil.objToDouble(minMarginPrice.get("MIN_MARGIN").toString()) && "N".equals(curPriceInfoTarget.getEventYn()))
					 || (ComUtil.objToDouble(curPriceInfoTarget.getMarginRate()) < ComUtil.objToDouble(minMarginPrice.get("EVENT_MIN_MARGIN").toString()) && "Y".equals(curPriceInfoTarget.getEventYn()))
					 || (ComUtil.objToDouble(curPriceInfoTarget.getSalePrice()) < ComUtil.objToDouble(minMarginPrice.get("MIN_SALE_PRICE").toString()))
					 || (ComUtil.objToDouble(curPriceInfoTarget.getBeSalePrice()) * 0.5 >= ComUtil.objToDouble(curPriceInfoTarget.getSalePrice()))
					 || (ComUtil.objToDouble(curPriceInfoTarget.getBeSalePrice()) * 2 <= ComUtil.objToDouble(curPriceInfoTarget.getSalePrice()))
					 || ("61".equals(curPriceInfoTarget.getSourcingMedia()) && "0032".equals(curPriceInfoTarget.getMdKind()) && ComUtil.objToDouble(curPriceInfoTarget.getMarginRate()) < 21) ) {
						
						stopSaleParam.put("paGroupCode", "05");
						stopSaleParam.put("paCode", curPriceInfoTarget.getPaCode());
						stopSaleParam.put("goodsCode", curPriceInfoTarget.getGoodsCode());
						stopSaleParam.put("dateTime", dateTime);
						stopSaleParam.put("userId", "PACOPN");
						stopSaleParam.put("paGoodsCode", curPriceInfoTarget.getPaGoodsCode());
						stopSaleParam.put("note", "마진10이하 혹은 판매가100원 미만");
						stopSaleParam.put("priceStopSale", "Y");
						
						//연동제외처리
						resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
					} else {
						curPriceInfoTarget.setModifyId("PACOPN");
						curPriceInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						curPriceInfoTarget.setDateTime(dateTime);
						resultMsg = paCommonService.saveCurPriceInfoTx(curPriceInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step2 상품가격 동기화 쿠팡 Fail > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
						sb.append(curPriceInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step2 상품가격 동기화 쿠팡 Sucess > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
				}
			} catch(Exception e) {
				log.info("Step2. 쿠팡 상품가격 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step2. 쿠팡 상품가격 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step2. 쿠팡 상품가격 동기화 END");	
		
		log.info("Step3. 쿠팡 고객부담배송비 동기화 START");
		curShipCostInfo = paCommonService.selectCurShipCostInfoList(paramMap);
		if(curShipCostInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaCustShipCostVO curShipCostInfoTarget : curShipCostInfo) {
					
					if(((curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("CN") || curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("PL"))
						&& (curShipCostInfoTarget.getShipCostBaseAmt() < 100 || curShipCostInfoTarget.getShipCostBaseAmt() % 100 > 0) ) //쿠팡 조건부 배송비의 경우 기준금액은 100원단위 & 100원이상 필수
						|| curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("QN")) {  //배송정책이 CN, PL이면서 기준금액이 100원 이하인경우, 배송비정책이 QN인 경우 연동 제외처리
						
						stopShipParam.put("paCode", curShipCostInfoTarget.getPaCode());
						stopShipParam.put("entpCode", curShipCostInfoTarget.getEntpCode());
						stopShipParam.put("shipCostCode", curShipCostInfoTarget.getShipCostCode());
						
						curShipStopSale = paCommonService.selectCurShipStopSaleList(stopShipParam);
						for(HashMap<String, String> curShipStopSaleTarget : curShipStopSale) {
							
							stopSaleParam.put("paGroupCode", "05");
							stopSaleParam.put("paCode", curShipStopSaleTarget.get("PA_CODE").toString());
							stopSaleParam.put("goodsCode", curShipStopSaleTarget.get("GOODS_CODE").toString());
							stopSaleParam.put("dateTime", dateTime);
							stopSaleParam.put("userId", "PACOPN");
							stopSaleParam.put("paGoodsCode", curShipStopSaleTarget.get("PA_GOODS_CODE").toString());
							stopSaleParam.put("note", "배송비유료변경");
							stopSaleParam.put("priceStopSale", "N");
							
							//연동제외처리
							resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
						}
						
					} else {
						
						curShipCostInfoTarget.setModifyId("PACOPN");
						curShipCostInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						resultMsg = paCommonService.saveCurShipCostInfoTx(curShipCostInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step3 고객부담배송비 쿠팡 Fail");
						sb.append(curShipCostInfoTarget.getEntpCode() + "|" + curShipCostInfoTarget.getShipCostCode() + ", ");
						continue;
					}
					log.info("Step3 고객부담배송비 쿠팡 Sucess");
				}
			} catch(Exception e) {
				log.info("Step3. 쿠팡 고객부담배송비 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step3. 쿠팡 고객부담배송비 동기화 : " + e.getMessage() + "INFO : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step3. 쿠팡 고객부담배송비 동기화 END");
		
		log.info("Step4. 쿠팡 출고/회수지 동기화 START");
		curEntpSlipInfo = paCommonService.selectCurEntpSlipInfoList(paramMap);
		if(curEntpSlipInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaEntpSlip curEntpSlipInfoTarget : curEntpSlipInfo) {
					
					curEntpSlipInfoTarget.setModifyId("PACOPN");
					curEntpSlipInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					resultMsg = paCommonService.saveCurEntpSlipInfoTx(curEntpSlipInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step4 출고/회수지 동기화 쿠팡 Fail");
						sb.append(curEntpSlipInfoTarget.getEntpCode() + ", ");
						continue;
					}
					log.info("Step4 출고/회수지 동기화 쿠팡 Sucess");
				}
			} catch(Exception e) {
				log.info("Step4. 쿠팡 출고/회수지 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step4. 쿠팡 출고/회수지 동기화 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step4. 쿠팡 출고/회수지 회수지 동기화 END");
		
		log.info("Step5. 쿠팡 상품판매단계 동기화 START");
		curSaleStop = paCommonService.selectCurSaleStopList(paramMap);
		
		if(curSaleStop.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curSaleStopTarget : curSaleStop) {
					
					stopSaleParam.put("paGroupCode", "05");
					stopSaleParam.put("paCode", curSaleStopTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curSaleStopTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PACOPN");
					stopSaleParam.put("paGoodsCode", curSaleStopTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "SK스토아상품판매중단");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step5 상품판매단계 동기화 쿠팡 Fail > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
						sb.append(curSaleStopTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step5 상품판매단계 동기화 쿠팡 Sucess > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step5. 쿠팡 상품판매단계 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step5. 쿠팡 상품판매단계 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step5. 쿠팡 상품판매단계 동기화 END");
		
		log.info("Step6. 쿠팡 행사 종료 상품 마진 체크 START");
		curEventMargin = paCommonService.selectCurEventMarginList(paramMap);
		
		if(curEventMargin.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curEventMarginTarget : curEventMargin) {
					paramMap.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					//minMarginPrice = paCommonService.selectMinMarginPrice(paramMap);
					//selectCurEventMarginList 안에서 체크
					//if(ComUtil.objToDouble(curEventMarginTarget.get("MARGIN_RATE")) < ComUtil.objToDouble(minMarginPrice.get("MIN_MARGIN"))) {
					stopSaleParam.put("paGroupCode", "05");
					stopSaleParam.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curEventMarginTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PACOPN");
					stopSaleParam.put("paGoodsCode", curEventMarginTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "행사종료");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step6 행사 종료 상품 마진 체크 쿠팡 Fail > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
						sb.append(curEventMarginTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step6 행사 종료 상품 마진 체크 쿠팡 Sucess > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
					//}					
				}
			} catch(Exception e) {
				log.info("Step6. 쿠팡 행사 종료 상품 마진 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step6. 쿠팡 행사 종료 상품 마진 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step6. 쿠팡 행사 종료 상품 마진 체크 END");
		paCommonService.checkMassModifyGoods("05");
	}
}
