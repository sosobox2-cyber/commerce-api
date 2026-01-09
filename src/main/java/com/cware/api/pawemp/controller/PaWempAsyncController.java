package com.cware.api.pawemp.controller;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cware.framework.common.util.StringUtil;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.CancelInputVO;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.OrderInputVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaCustShipCostVO;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.PaWempGoodsVO;
import com.cware.netshopping.domain.PaWempTargetVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pawemp.claim.service.PaWempClaimService;
import com.cware.netshopping.pawemp.common.service.PaWempApiService;
import com.cware.netshopping.pawemp.exchange.service.PaWempExchangeService;
import com.cware.netshopping.pawemp.goods.model.ReturnData;
import com.cware.netshopping.pawemp.goods.service.PaWempGoodsService;
import com.cware.netshopping.pawemp.order.service.PaWempOrderService;
import com.cware.netshopping.pawemp.system.exception.WmpApiException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
@ApiIgnore
@Api(value = "/pawemp/async", description="공통")
@Controller("com.cware.api.pawemp.PaWempAsyncController")
@RequestMapping(value = "/pawemp/async")
public class PaWempAsyncController extends AbstractController{

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pacommon.claim.paclaimService")
	private PaClaimService paClaimService;
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paorderService;
	
	@Resource(name = "pawemp.order.paWempOrderService")
	private PaWempOrderService paWempOrderService;
	
	@Resource(name = "pawemp.common.paWempApiService")
	private PaWempApiService paWempApiService;

	@Resource(name = "pawemp.claim.paWempClaimService")
	private PaWempClaimService paWempClaimService;
	
	@Resource(name = "pawemp.exchange.paWempExchangeService")
	private PaWempExchangeService paWempExchangeService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@Resource(name = "pawemp.goods.paWempGoodsService")
	private PaWempGoodsService paWempGoodsService;

	@SuppressWarnings("unchecked")
	public void orderInputAsync(String paOrderNo, int targetCnt, HttpServletRequest request) throws Exception{
		ParamMap paramMap = null;
		int executedRtn = 0;
		HashMap<String, Object>[] resultMap = null;
		HashMap<String, String> orderMap = null;
		String promoAllowTerm = ComUtil.NVL(systemService.getValRealTime("PAPROMO_ALLOW_TERM") , "0.1" );	// 프로모션 연동 종료 건 조회 허용 시간 
		
		try{
			
			paramMap = new ParamMap();
			paramMap.put("paOrderNo"		, paOrderNo);
			paramMap.put("papromoAllowTerm"	, promoAllowTerm);
					
			// 생성대상조회
			List<Object> orderInputTargetDtList = paWempOrderService.selectOrderInputTargetDtList(paramMap);
			int targetSize = orderInputTargetDtList.size();
			if(targetSize < 1){
				throw processException("msg.no.select", new String[] { "selectOrderInputTargetDtList" });
			} else if(targetSize != targetCnt){
				throw processException("pa.cannot_dup_data", new String[] { "selectOrderInputTargetDtList" });	
			}
			
			OrderInputVO[] orderInputVO = new OrderInputVO[targetSize];
			for(int j = 0; targetSize > j; j++){
				orderMap = (HashMap<String, String>) orderInputTargetDtList.get(j);
				
				orderInputVO[j] = new OrderInputVO(); 
				orderInputVO[j].setPaCode       (orderMap.get("PA_CODE").toString());
				orderInputVO[j].setMappingSeq   (orderMap.get("MAPPING_SEQ").toString());
				orderInputVO[j].setMediaCode    (orderMap.get("MEDIA_CODE").toString());
				orderInputVO[j].setOrderDate    (orderMap.get("ORDER_DATE").toString());
				orderInputVO[j].setGoodsCode    (orderMap.get("GOODS_CODE").toString());
				orderInputVO[j].setGoodsdtCode  (orderMap.get("GOODSDT_CODE").toString());
				orderInputVO[j].setOrderQty     (Integer.parseInt(String.valueOf(orderMap.get("ORDER_QTY"))));
				orderInputVO[j].setApplyDate    (orderMap.get("APPLY_DATE").toString());
				orderInputVO[j].setRsaleAmt     (Double.parseDouble(String.valueOf(orderMap.get("RSALE_AMT"))));
				orderInputVO[j].setSupplyPrice  (Double.parseDouble(String.valueOf(orderMap.get("SUPPLY_PRICE"))));
				orderInputVO[j].setSellerDcAmt  (Long.parseLong(String.valueOf(orderMap.get("SELLER_DC_AMT"))));
				orderInputVO[j].setCustName     (orderMap.get("CUST_NAME").toString());
				orderInputVO[j].setCustChar     ("99");
				orderInputVO[j].setCustTel1     (orderMap.get("CUST_TEL1").toString().replace("-", ""));
				orderInputVO[j].setCustTel2     (orderMap.get("CUST_TEL2").toString().replace("-", ""));
				orderInputVO[j].setReceiverName (orderMap.get("RECEIVER_NAME").toString());
				orderInputVO[j].setReceiverTel  (orderMap.get("RECEIVER_TEL").toString().replace("-", ""));
				orderInputVO[j].setReceiverHp   (orderMap.get("RECEIVER_HP").toString().replace("-", ""));
				orderInputVO[j].setAddrGbn      ("02");
				orderInputVO[j].setReceiverAddr (orderMap.get("RECEIVER_ADDR").toString());
				orderInputVO[j].setPostNo       (orderMap.get("RECEIVER_ZIPCODE").toString());
				orderInputVO[j].setStdAddr      (orderMap.get("RECEIVER_ADDR1").toString());
				orderInputVO[j].setStdAddrDT    (orderMap.get("RECEIVER_ADDR2").toString());
				orderInputVO[j].setPostNoSeq    ("001");
				orderInputVO[j].setReceiveMethod("63");
				orderInputVO[j].setMsg          (orderMap.get("MSG").toString());
				orderInputVO[j].setPaGoodsCode  (orderMap.get("PA_GOODS_CODE").toString());
				orderInputVO[j].setPaOrderCode  (orderMap.get("PA_ORDER_NO").toString());
				orderInputVO[j].setShpFeeCost   (Long.parseLong(String.valueOf(orderMap.get("PA_SHPFEE_COST"))));
				orderInputVO[j].setProcUser     (Constants.PA_WEMP_PROC_ID);
				orderInputVO[j].setPriceSeq	    (orderMap.get("PRICE_SEQ").toString());
				orderInputVO[j].setDoFlag	    (orderMap.get("DO_FLAG").toString());
				orderInputVO[j].setLumpSumDcAmt(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_DC_AMT"))));
				orderInputVO[j].setLumpSumOwnDcAmt(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_OWN_DC_AMT"))));
				orderInputVO[j].setLumpSumEntpDcAmt(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_ENTP_DC_AMT"))));
				orderInputVO[j].setCouponPromoBdate(DateUtil.toTimestamp(orderMap.get("COUPON_PROMO_BDATE")));
				orderInputVO[j].setCouponPromoEdate(DateUtil.toTimestamp(orderMap.get("COUPON_PROMO_EDATE")));
				
				if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost())
						|| ("127.0.0.1").equals(request.getRemoteHost())) {
					orderInputVO[j].setIsLocalYn("Y");
				} else {
					orderInputVO[j].setIsLocalYn("N");
				}
				
				// 가격 비교
				String paApplyDate = orderMap.get("APPLY_DATE").toString(); // TPAGOODSPRICE 기준 APPLY_DATE
				String stoaApplyDate = orderMap.get("STOA_APPLY_DATE").toString(); // TGOODSPRICE 기준 APPLY_DATE
				String paOrderDate = orderMap.get("ORDER_DATE").toString(); // 위메프 주문일시
				
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
						paorderService.updatePaOrdermTx(paramMap);
						throw processException("pa.fail_order_input", new String[]{ "가격 정보가 잘못되었습니다." });
					}
				}
				
				//PROMOTION DATA SETTING 
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
	            orderPaPromo.setCouponPromoBdate(orderInputVO[j].getCouponPromoBdate());
	            orderPaPromo.setCouponPromoEdate(orderInputVO[j].getCouponPromoEdate());
	            orderPaPromo.setCouponYn("1"); 
	            orderPaPromo.setProcGb("I");
				
	            orderInputVO[j].setOrderPaPromo(orderPaPromo);
				//orderInputVO[j].setOrderPromo	(paWempOrderService.selectOrderPromo(orderMap));
				//orderInputVO[j].setOrderPaPromo	(paWempOrderService.selectOrderPaPromo(orderMap));//제휴OUT프로모션 : 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
			}
			
			// 생성
			try {
				resultMap = paorderService.saveOrderTx(orderInputVO);
			} catch (Exception e) {
				for(int j = 0; targetSize > j; j++){
					paramMap = new ParamMap();
					paramMap.put("mappingSeq", orderInputVO[j].getMappingSeq());
					paramMap.put("resultCode", "999999");
					paramMap.put("resultMessage", e.getMessage());
					paramMap.put("createYn", "0");
					
					executedRtn = paorderService.updatePaOrdermTx(paramMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
					}
				}
				
				throw processMessageException(e.getMessage());
			}
			
			// 재고부족 or 판매불가 호출
			ParamMap preCancelMap = new ParamMap();
			for(int j = 0; targetSize > j; j++){
				paramMap = new ParamMap();
				paramMap.setParamMap(resultMap[j]);
				paramMap.replaceCamel();
				
				if(paramMap.getString("resultCode").equals("100001")){
					//재고부족 or 판매불가 상태.
					PaWempTargetVO targetVo = paWempOrderService.selectRefusalInfo(paramMap.getString("mappingSeq"));
					if(targetVo == null || StringUtils.isBlank(targetVo.getOrderOptionNo())) {
						log.info("orderInputAsync not found orderOptionNo. mappingSeq:"+paramMap.getString("mappingSeq"));
						continue;
					}
					//주문취소 호출
					ParamMap procResultMap = paWempOrderService.orderRefusalProc(targetVo);
					if(!StringUtils.equals("1", (String)procResultMap.get("result_code"))) {
						preCancelMap.put("mappingSeq", paramMap.getString("mappingSeq"));
						preCancelMap.put("preCancelYn", "0");
						preCancelMap.put("preCancelReason", "");
						preCancelMap.put("apiResultCode", "999999");
						preCancelMap.put("apiResultMessage", "FAIL - 배송불가정보등록 : " + procResultMap.getString("result_text"));
					} else {
						preCancelMap.put("mappingSeq", paramMap.getString("mappingSeq"));
						preCancelMap.put("preCancelYn", "1");
						preCancelMap.put("preCancelReason", getMessage("pa.out_of_stock_due_shortage_process"));
						preCancelMap.put("apiResultCode", Constants.SAVE_SUCCESS);
						preCancelMap.put("apiResultMessage", "SUCCESS : 배송불가정보등록");
					}
					executedRtn = paWempOrderService.updatePreCancelYnTx(preCancelMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
					}
				}
			}
		}catch(Exception e){
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "PA_ORDER_NO: " + paOrderNo + " | " + e.getMessage());
		}finally{
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode"  , "PAWEMP_ORDER_INPUT");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code"     , "500");
					paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	/**
	 * 위메프 주문취소 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void cancelInputAsync(String paCode, String paClaimNo, String paOrderNo, String paOrderSeq, String paShipNo, HttpServletRequest request) throws Exception{
		HashMap<String, Object> hmSheet = null;
		ParamMap paramMap = null;
		int targetSize = 0;
		int executedRtn = 0;
		
		try {
			paramMap = new ParamMap();
			paramMap.put("paClaimNo", paClaimNo);
			paramMap.put("paOrderNo", paOrderNo);
			paramMap.put("paOrderSeq", paOrderSeq);
			paramMap.put("paShipNo", paShipNo);
			paramMap.put("paGroupCode", Constants.PA_WEMP_GROUP_CODE);
			paramMap.put("paCode", paCode);
			
			List<Object> cancelInputTargetDtList = paWempOrderService.selectCancelInputTargetDtList(paramMap); 
			targetSize = cancelInputTargetDtList.size();
			if(targetSize != 1){
				throw processException("msg.no.select", new String[] { "selectCancelInputTargetDtList" });
			}
			hmSheet = (HashMap<String, Object>) cancelInputTargetDtList.get(0);
			
			if(hmSheet.get("PRE_CANCEL_YN").toString().equals("0")){
				//= 일반취소건 처리.
				CancelInputVO cancelInputVO = new CancelInputVO();
				cancelInputVO.setMappingSeq(hmSheet.get("MAPPING_SEQ").toString());
				cancelInputVO.setOrderNo(hmSheet.get("ORDER_NO").toString());
				cancelInputVO.setOrderGSeq(hmSheet.get("ORDER_G_SEQ").toString());
				cancelInputVO.setCancelQty(Integer.parseInt(String.valueOf(hmSheet.get("PA_PROC_QTY"))));
				cancelInputVO.setCancelCode(hmSheet.get("CANCEL_CODE").toString());
				cancelInputVO.setProcId("PAWEMP");
				try {
					paorderService.saveCancelTx(cancelInputVO);
				} catch (Exception e) {
					paramMap = new ParamMap();
					paramMap.put("mappingSeq", cancelInputVO.getMappingSeq());
					paramMap.put("resultCode", "999999");
					paramMap.put("resultMessage", e.getMessage());
					paramMap.put("createYn", "0");
					
					executedRtn = paorderService.updatePaOrdermTx(paramMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
					}
				
					throw processMessageException(e.getMessage());
				}
				
			} else {
				//= 기취소건 처리(주문생성 이전 취소건)
				//=pa.before_order_create_cancel = 주문생성 이전 취소건
				ParamMap preCancelMap = new ParamMap();
				preCancelMap.put("mappingSeq", hmSheet.get("MAPPING_SEQ").toString());
				preCancelMap.put("preCancelYn", "1");
				preCancelMap.put("preCancelReason", getMessage("pa.before_order_create_cancel"));
				
				executedRtn = paWempOrderService.updatePreCancelYnTx(preCancelMap);
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
				}
			}
		} catch (Exception e) {
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "pa_claim_no : " + paClaimNo + " > " + e.toString());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PAWEMP_CANCEL_INPUT");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					paramMap.put("siteGb", 		"PAWEMP");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	/**
	 * 위메프 반품 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	//@Async
	public void returnInputAsync(HashMap<String, Object> claimMap, HttpServletRequest request) throws Exception{
		HashMap<String, Object> hmSheet = null;
		ParamMap paramMap = null;
		List<Object> orderClaimTargetDtList = null;
		int targetSize = 0;
		int executedRtn = 0;
		
		try {
			paramMap = new ParamMap();
			paramMap.setParamMap(claimMap);
			paramMap.replaceCamel();
			if(paramMap.getString("paOrderGb").equals("30")){
				orderClaimTargetDtList = paWempClaimService.selectOrderReturnTargetDt30List(paramMap);
			}else{
				orderClaimTargetDtList = paWempClaimService.selectOrderReturnTargetDt20List(paramMap);
			}
			
			targetSize = orderClaimTargetDtList.size();
			if(targetSize == 0){
				throw processException("msg.no.select", new String[] { "selectOrderCalimTargetDtList" });
			}
			hmSheet = (HashMap<String, Object>) orderClaimTargetDtList.get(0);
			
			//= 반품 공통화 호출.
			OrderClaimVO orderClaimVO = new OrderClaimVO();
			orderClaimVO.setMappingSeq(hmSheet.get("MAPPING_SEQ").toString());
			orderClaimVO.setOrderNo   (hmSheet.get("ORDER_NO").toString());
			orderClaimVO.setOrderGSeq (hmSheet.get("ORDER_G_SEQ").toString());
			orderClaimVO.setClaimQty  (Integer.parseInt(String.valueOf(hmSheet.get("PA_PROC_QTY"))));
			orderClaimVO.setClaimGb   (hmSheet.get("CLAIM_TYPE").toString());
			orderClaimVO.setClaimCode("999");
			orderClaimVO.setClaimDesc (hmSheet.get("CLAIM_DESC").toString());
			orderClaimVO.setReturnName(hmSheet.get("PICKUP_NAME").toString());
			orderClaimVO.setReturnTel (hmSheet.get("PICKUP_PHONE").toString().replace("-", ""));
			orderClaimVO.setReturnHp  (hmSheet.get("PICKUP_PHONE").toString().replace("-", ""));
			orderClaimVO.setReturnAddr(hmSheet.get("PICKUP_ADDR").toString());
			orderClaimVO.setCustDelyYn(hmSheet.get("CUST_DELY_YN").toString()); //= 고객 직접발송일 경우 : 1, 아닐경우 : 0
			
			if(hmSheet.get("CUST_DELY_YN").toString().equals("1")){
				orderClaimVO.setReturnDelyGb(hmSheet.get("RETURN_DELY_GB").toString()); //= 직접발송일 경우 위메프 고객입력 배송사코드.
				orderClaimVO.setReturnSlipNo(StringUtil.null2string(hmSheet.get("RETURN_SLIP_NO").toString(), getMessage("pa.cust_dely_slip_no_empty"))); //= 직접발송일 경우 위메프 고객입력 운송장번호.
			}else{
				orderClaimVO.setReturnDelyGb("");
				orderClaimVO.setReturnSlipNo("");
			}
			
			orderClaimVO.setOutBefClaimGb(hmSheet.get("OUT_BEF_CLAIM_GB").toString());
			
			orderClaimVO.setRcvrBaseAddr  (hmSheet.get("PICKUP_BASE_ADDR").toString());
			orderClaimVO.setRcvrDtlsAddr(hmSheet.get("PICKUP_DETAIL_ADDR").toString());
			orderClaimVO.setRcvrMailNo    (hmSheet.get("PICKUP_ZIPCODE").toString());
			orderClaimVO.setRcvrMailNoSeq("001");
			orderClaimVO.setRcvrTypeAdd("");
			
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost())
					|| ("127.0.0.1").equals(request.getRemoteHost())) {
				orderClaimVO.setLocalYn("Y");
			} else {
				orderClaimVO.setLocalYn("N");
			}			
			
			
			String claimCode = hmSheet.get("CLAIM_CODE").toString();
			if (claimCode == null || ("").equals(claimCode)  || claimCode.length() != 6){
				if(orderClaimVO.getOutBefClaimGb().equals("1")){  //출하지시전 반품, 취소에 걸려있음..
					orderClaimVO.setClaimCode("620698"); //Defalut;
				}else{
					orderClaimVO.setClaimCode("630697"); //Defalut;							
				}
			}
			orderClaimVO.setCsLgroup(claimCode.substring(0,2));
			orderClaimVO.setCsMgroup(claimCode.substring(2,4));
			orderClaimVO.setCsSgroup(claimCode.substring(4,6));
			orderClaimVO.setCsLmsCode(claimCode);
			
			orderClaimVO.setInsertId("PAWEMP");
			
			Long shpFee = Long.parseLong(String.valueOf(hmSheet.get("CLAIM_FEE")));
			if (shpFee > 0 ){
				orderClaimVO.setShpfeeYn("1");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
			}else{
				orderClaimVO.setShpfeeYn("0");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2					
			}
			
			String checkAddr = "";
			//출고전 반품처리 배송비 무상처리
			if(("1").equals(orderClaimVO.getOutBefClaimGb())){
				orderClaimVO.setShpfeeYn("0");
				orderClaimVO.setIs20Claim(true);
				checkAddr = "1";
			}else{
				orderClaimVO.setIs20Claim(false);
				//기존주소와 같은지 확인 (출고전반품은 주문배송지 사용)
				paramMap.put("claimGb", paramMap.getString("paOrderGb"));
				checkAddr = paWempClaimService.compareAddress(paramMap); //PA_ORDER_NO, PA_ORDER_SEQ, PA_CLAIM_NO, PA_ORDER_GB
				orderClaimVO.setSameAddr(checkAddr);
			}
			
			try {
				paClaimService.saveOrderClaimTx(orderClaimVO);
			} catch (Exception e) {
				paramMap = new ParamMap();
				paramMap.put("mappingSeq", orderClaimVO.getMappingSeq());
				paramMap.put("resultCode", "999999");
				paramMap.put("resultMessage", e.getMessage());
				paramMap.put("createYn", "0");
				
				executedRtn = paorderService.updatePaOrdermTx(paramMap);
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
				}
			
				throw processMessageException(e.getMessage());
			}
			
		} catch (Exception e) {
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "pa_claim_no : " + claimMap.get("PA_CLAIM_NO") + " > " + e.toString());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PAWEMP_ORDER_CLAIM");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					paramMap.put("siteGb", 		"PAWEMP");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	/**
	 * 위메프 반품철회 데이터 생성
	 * @param claimMap
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void returnCancelAsync(HashMap<String, Object> returnMap, HttpServletRequest request) throws Exception{
		
		List<Object> returnCancelTargetDtList = null;
		HashMap<String, Object> returnCancelMap = null;
		ParamMap paramMap = null;
		
		OrderClaimVO orderClaimVO = null;
		
		int executedRtn = 0;
		
		try{
			
			paramMap = new ParamMap();
			
			paramMap.setParamMap(returnMap);
			paramMap.replaceCamel();
					
			returnCancelTargetDtList = paWempClaimService.selectReturnCancelTargetDtList(paramMap);
			if(returnCancelTargetDtList.size() != 1){
				throw processException("msg.no.select", new String[] { "selectReturnCancelTargetDtList" });
			}
			
			returnCancelMap = (HashMap<String, Object>) returnCancelTargetDtList.get(0);
			
			orderClaimVO = new OrderClaimVO();
			
			orderClaimVO.setMappingSeq  (returnCancelMap.get("MAPPING_SEQ").toString());
			orderClaimVO.setOrderNo     (returnCancelMap.get("ORDER_NO").toString());
			orderClaimVO.setOrderGSeq   (returnCancelMap.get("ORDER_G_SEQ").toString());
			orderClaimVO.setOrderWSeq   (returnCancelMap.get("ORDER_W_SEQ").toString());
			orderClaimVO.setClaimQty    (Integer.parseInt(String.valueOf(returnCancelMap.get("PA_PROC_QTY"))));
			orderClaimVO.setClaimCode   (returnCancelMap.get("CANCEL_CODE").toString());
			orderClaimVO.setInsertId	("PAWEMP");
			
			try{
				paClaimService.saveClaimCancelTx(orderClaimVO);
			}catch(Exception e){
				paramMap = new ParamMap();
				paramMap.put("mappingSeq", orderClaimVO.getMappingSeq());
				paramMap.put("resultCode", "999999");
				paramMap.put("resultMessage", e.getMessage());
				paramMap.put("createYn", "0");
				
				executedRtn = paorderService.updatePaOrdermTx(paramMap);
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
				}
				
				throw processMessageException(e.getMessage());
			}
		}catch(Exception e){
			paramMap.put("message", "PA_CLAIM_NO: " + returnMap.get("PA_CLAIM_NO") + " ,MSG: " + e.getMessage());
		}finally{
			try{
				if(paramMap != null && !"".equals(paramMap.getString("message"))){
					paramMap.put("apiCode"  , "PAWEMP_CLAIM_CANCEL");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code"     , "500");
					paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception e){
				log.error("[ApiTrackingTx Error] " + e.getMessage());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	/**
	 * 위메프 교환 데이터 생성 
	 * @param Paorderm, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void orderChangeAsync(Paorderm targetPaOrderm, HttpServletRequest request) throws Exception{
		HashMap<String, Object>[] resultMap = null;
		HashMap<String, String> changeMap = null;
		ParamMap paramMap = null;
		int targetSize = 0;
		int executedRtn = 0;
		
		paramMap = new ParamMap();
		paramMap.put("paOrderNo", targetPaOrderm.getPaOrderNo());
		paramMap.put("paOrderSeq", targetPaOrderm.getPaOrderSeq());
		paramMap.put("paClaimNo", targetPaOrderm.getPaClaimNo());
		paramMap.put("paShipNo", targetPaOrderm.getPaShipNo());
		
		try {
			List<Object> orderChangeTargetDtList = paWempExchangeService.selectOrderChangeTargetDtList(paramMap);
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
				orderClaimVO[j].setShipcostChargeYn(changeMap.get("SHIPCOST_CHARGE_YN").toString()); //박스동봉이라 0원처리
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
				orderClaimVO[j].setInsertId     ("PAWEMP");
				
				if(changeMap.get("CLAIM_GB").toString().equals("40")) { //교환배송
					orderClaimVO[j].setReturnName(changeMap.get("EXCH_NAME").toString());
					orderClaimVO[j].setReturnHp(changeMap.get("EXCH_HP").toString().replace("-", ""));
					orderClaimVO[j].setReturnAddr(changeMap.get("EXCH_ADDR").toString());
					orderClaimVO[j].setCustDelyYn(changeMap.get("EXCH_CUST_DELY_YN").toString()); //= 고객 직접발송일 경우 : 1, 아닐경우 : 0
					orderClaimVO[j].setReturnDelyGb(changeMap.get("EXCH_DELY_GB").toString()); //= 직접발송일 경우 위메프 고객입력 배송사코드.
					orderClaimVO[j].setReturnSlipNo(StringUtil.null2string(changeMap.get("EXCH_SLIP_NO").toString(), getMessage("pa.cust_dely_slip_no_empty"))); //= 직접발송일 경우 위메프 고객입력 운송장번호.
					orderClaimVO[j].setRcvrMailNo(changeMap.get("EXCH_ZIPCODE").toString()); //이거 문제
					orderClaimVO[j].setRcvrMailNoSeq("001");
					orderClaimVO[j].setRcvrBaseAddr	(changeMap.get("EXCH_BASE_ADDR").toString());
					orderClaimVO[j].setRcvrDtlsAddr	(changeMap.get("EXCH_DETAIL_ADDR").toString());
					orderClaimVO[j].setRcvrTypeAdd	("01");
				} else {//교환회수
					orderClaimVO[j].setReturnName(changeMap.get("RETURN_NAME").toString());
					orderClaimVO[j].setReturnHp(changeMap.get("RETURN_HP").toString().replace("-", ""));
					orderClaimVO[j].setReturnAddr(changeMap.get("RETURN_ADDR").toString());
					orderClaimVO[j].setCustDelyYn(changeMap.get("RETURN_CUST_DELY_YN").toString()); //= 고객 직접발송일 경우 : 1, 아닐경우 : 0
					orderClaimVO[j].setReturnDelyGb(changeMap.get("RETURN_DELY_GB").toString()); //= 직접발송일 경우 위메프 고객입력 배송사코드.
					orderClaimVO[j].setReturnSlipNo(StringUtil.null2string(changeMap.get("RETURN_SLIP_NO").toString(), getMessage("pa.cust_dely_slip_no_empty"))); //= 직접발송일 경우 위메프 고객입력 운송장번호.
					orderClaimVO[j].setRcvrMailNo(changeMap.get("RETURN_ZIPCODE").toString());
					orderClaimVO[j].setRcvrMailNoSeq("001");
					orderClaimVO[j].setRcvrBaseAddr	(changeMap.get("RETURN_BASE_ADDR").toString());
					orderClaimVO[j].setRcvrDtlsAddr	(changeMap.get("RETURN_DETAIL_ADDR").toString());
					orderClaimVO[j].setRcvrTypeAdd	("01");
				}

				//제휴 인입 주소 확인
				String checkAddr = "";
				paramMap.put("paOrderGb", "40");
				paramMap.put("claimGb", paramMap.getString("paOrderGb"));
				checkAddr = paWempClaimService.compareAddress(paramMap);
				orderClaimVO[j].setSameAddr(checkAddr);
				
			}
			
			try {
				resultMap = paClaimService.saveOrderChangeTx(orderClaimVO);
			} catch (Exception e) {
				for(int j = 0; targetSize > j; j++){
					paramMap = new ParamMap();
					paramMap.put("mappingSeq", orderClaimVO[j].getMappingSeq());
					paramMap.put("resultCode", "999999");
					paramMap.put("resultMessage", e.getMessage());
					paramMap.put("createYn", "0");
					
					executedRtn = paorderService.updatePaOrdermTx(paramMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
					}
				}
				
				throw processMessageException(e.getMessage());
			}
			
			for(int j = 0; targetSize > j; j++){
				paramMap = new ParamMap();
				paramMap.setParamMap(resultMap[j]);
				paramMap.replaceCamel();
				
				if(paramMap.getString("resultCode").equals("100001")){
					//재고부족으로 인한 교환불가.
					Paorderm paorder = paWempExchangeService.selectPaOrdermInfo(paramMap.getString("mappingSeq"));
					if(paorder == null || StringUtils.isBlank(paorder.getMappingSeq())) {
						log.info("Not found TPAORDERM. mappingSeq:"+paramMap.getString("mappingSeq"));
						continue;
					}
					
					//API전송이 실패여도 TPAORDERM.CHANGE_FLAG에 '06 반영
					paWempExchangeService.updatePaOrdermChangeFlag("06", paorder.getMappingSeq()); //[O0512 06:위메프재고부족 교환보류
				}
			}
		} catch (Exception e) {
			paramMap.put("message", "pa_claim_no : " + targetPaOrderm.getPaClaimNo() + " > " + e.toString());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PAWEMP_ORDER_CHANGE");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					paramMap.put("siteGb", 		"PAWEMP");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	/**
	 * 위메프 교환 취소 데이터 생성 
	 * @param Paorderm, HttpServletRequest
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void changeCancelAsync(Paorderm targetPaOrderm, HttpServletRequest request) throws Exception {
		HashMap<String, Object> hmSheet = null;
		ParamMap paramMap = null;
		int targetSize = 0;
		int executedRtn = 0;
		boolean isMake = false;
		
		try {
			paramMap = new ParamMap();
			paramMap.put("paOrderNo", targetPaOrderm.getPaOrderNo());
			paramMap.put("paOrderSeq", targetPaOrderm.getPaOrderSeq());
			paramMap.put("paClaimNo", targetPaOrderm.getPaClaimNo());
			paramMap.put("paShipNo", targetPaOrderm.getPaShipNo());
			
			List<Object> orderChangeTargetDtList = paWempExchangeService.selectChangeCancelTargetDtList(paramMap); 
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
					orderClaimVO[i].setInsertId("PAWEMP");
					
					isMake = true;
				} else {
			        //= 기취소건 처리(교환생성 이전 취소건)
			        //=pa.before_claim_create_cancel = 교환생성 이전 취소건
					ParamMap preCancelMap = new ParamMap();
					preCancelMap.put("mappingSeq", hmSheet.get("MAPPING_SEQ").toString());
					preCancelMap.put("preCancelYn", "1");
					preCancelMap.put("preCancelReason", getMessage("pa.before_change_create_cancel"));
					
					executedRtn = paWempOrderService.updatePreCancelYnTx(preCancelMap);
			        if(executedRtn != 1){
			        	throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
			        }
				}
			}
			
			try {
				if(isMake) {
					paClaimService.saveChangeCancelTx(orderClaimVO);
				}
			} catch (Exception e) {
				for(int j = 0; targetSize > j; j++){
					paramMap = new ParamMap();
					paramMap.put("mappingSeq", orderClaimVO[j].getMappingSeq());
					paramMap.put("resultCode", "999999");
					paramMap.put("resultMessage", e.getMessage());
					paramMap.put("createYn", "0");
					
					executedRtn = paorderService.updatePaOrdermTx(paramMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
					}
				}
				
				throw processMessageException(e.getMessage());
			}
			
		} catch (Exception e) {
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "pa_claim_no : " + targetPaOrderm.getPaClaimNo() + " > " + e.toString());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PAWEMP_CHANGE_CANCEL");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					paramMap.put("siteGb", 		"PAWEMP");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	@Async
	public void asyncGoodsModify(HttpServletRequest request, ParamMap asyncMap, HashMap<String, String> apiInfo, PaWempGoodsVO paWempGoods, List<PaGoodsdtMapping> goodsdtMapping, 
			List<PaGoodsOfferVO> goodsOffer, List<PaPromoTarget> paPromoTargetList) throws Exception{
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		String productNo = "";
		asyncMap.put("goodsCode", paWempGoods.getGoodsCode());
		asyncMap.put("paCode", paWempGoods.getPaCode());
		
		try {
			
			log.info("09-3.위메프 상픔수정 API 호출");
			if(paWempGoods.getPaCode().equals(Constants.PA_WEMP_BROAD_CODE)){
				asyncMap.put("paName", Constants.PA_BROAD);
			} else {
				asyncMap.put("paName", Constants.PA_ONLINE);
			}
			
			ReturnData returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST", getProductObject(true, paWempGoods , goodsdtMapping, goodsOffer, paPromoTargetList), ReturnData.class, asyncMap.getString("paName"));
			if(StringUtils.isNotBlank(Long.toString(returnData.getProductNo()))){
				productNo = Long.toString(returnData.getProductNo());
				log.info("09-4.위메프 상픔수정 API 성공 : "+paWempGoods.getGoodsCode());
				asyncMap.put("code", "200");
				asyncMap.put("message", "[" + paWempGoods.getProductNo() + "]" +"상품수정이 정상적으로 처리되었습니다.");
				
				rtnMsg = paWempGoodsService.savePaWempGoodsTx(paWempGoods, goodsdtMapping, paPromoTargetList);//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
				log.info("09-5.제휴사 상품수정 상품정보 저장 : "+rtnMsg);
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
					asyncMap.put("code","500");
					asyncMap.put("message",paWempGoods.getGoodsCode() + rtnMsg);
				}
			} else {
				asyncMap.put("code","500");
				asyncMap.put("message",paWempGoods.getProductNo() + getMessage("partner.no_change_data"));
			}
			
			insertPaGoodsTransLog(asyncMap, productNo);
			
		} catch(WmpApiException e){ // API 오류
			String errMsg = e.getMessage();
			String[] msg = errMsg.split("error:");
			if(msg.length > 1){
				errMsg = msg[1].replaceAll("\"", "");
			}
			asyncMap.put("code","500");
			asyncMap.put("message", "[" + paWempGoods.getProductNo() + "]" + errMsg);
		} finally{
			if(!"200".equals(asyncMap.get("code").toString())) {
				systemService.insertApiTrackingTx(request, asyncMap);			
			}
		}
	}
	
	/**
	 * 위메프 상품 등록/수정 API JSON OBJECT 처리
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : 프로모션 정보 List로 변경
	 * @param isUpdate( true :수정, false : 등록)
	 * @param paWempGoods
	 * @param paGoodsOption
	 * @param goodsOffer
	 * @return
	 * @throws Exception
	 */
	public JsonObject getProductObject(boolean isUpdate, PaWempGoodsVO paWempGoods, List<PaGoodsdtMapping> paGoodsOption, List<PaGoodsOfferVO> goodsOffer, List<PaPromoTarget> paPromoTargetList) throws Exception{
		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		
		String imageURL = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
		JsonObject root = new JsonObject();
		JsonObject jBasic = new JsonObject();
		JsonObject jSale = new JsonObject();
		JsonObject jDetail = new JsonObject();
		JsonArray jImgUrlList = new JsonArray();
		JsonObject jOption = new JsonObject();
		JsonArray jOptionValueList = new JsonArray();
		JsonArray jNoticeList = new JsonArray();
		JsonObject jEtc = new JsonObject();
		StringBuilder sb = new StringBuilder();

		// 기본정보 
		
		String collectYn = ComUtil.NVL(paWempGoods.getCollectYn(), "0");
		//String shipCostCode = paWempGoods.getShipCostCode().substring(0,2);
		
		if ("1".equals(collectYn) /* && "FR".equals(shipCostCode) */) {
			jBasic.addProperty("productName", "(착불)"+paWempGoods.getGoodsName());
		} else {
			jBasic.addProperty("productName", paWempGoods.getGoodsName());
		}
		
		jBasic.addProperty("productType", "N");  //상품유형 (N:새상품, U:중고, R:리퍼, B:반품(리세일), O:주문제작)
		jBasic.addProperty("dcateCode", Integer.parseInt(paWempGoods.getPaLmsdKey()));
		jBasic.addProperty("shipPolicyNo", Integer.parseInt(paWempGoods.getShipPolicyNo()));
		jBasic.addProperty("adultLimitYn", paWempGoods.getAdultYn().equals("1") ? "Y" : "N");
		jBasic.addProperty("displayYn", "Y"); //20201021 이후로 항상 검색되게 변경처리
		
		if(StringUtils.isNotBlank(paWempGoods.getBrandNo())){ // 브랜드 번호
			jBasic.addProperty("brandNo", Long.parseLong(paWempGoods.getBrandNo()));
		}
		/*if(StringUtils.isNotBlank(paWempGoods.getMakerNo())){ // 제조사 번호
			jBasic.addProperty("makerNo", Long.parseLong(paWempGoods.getMakerNo()));
		}*/
		
		jOption.addProperty("selectOptionUseYn", "Y"); // 선택형 옵션 사용여부
		jOption.addProperty("selectOptionDepth", 1); 
		if(paGoodsOption.get(0).getGoodsdtInfoKind().length() > 10) {
			jOption.addProperty("selectOptionTitle1", paGoodsOption.get(0).getGoodsdtInfoKind().substring(0,10));			
		}else {
			jOption.addProperty("selectOptionTitle1", paGoodsOption.get(0).getGoodsdtInfoKind());	
		}
		
		for(PaGoodsdtMapping optionItem : paGoodsOption){
			JsonObject oItem = new JsonObject();
			oItem.addProperty("optionValue1", optionItem.getGoodsdtInfo()); // 옵션값1 (최대 70자)
			oItem.addProperty("stockCount", getOrderAbleQty(optionItem.getTransOrderAbleQty())); // 옵션 재고수량 (0 ~ 99999)
			jSale.addProperty("stockCount", getOrderAbleQty(optionItem.getTransOrderAbleQty())); // 옵션 재고수량 (0 ~ 99999)
			oItem.addProperty("displayYn", "Y"); // 옵션 노춡여부
			oItem.addProperty("sellerOptionCode", optionItem.getGoodsdtCode()); // 업체 옵션코드 최대(50자)
			jOptionValueList.add(oItem);
		}
		jOption.add("selectOptionValueList", jOptionValueList);

		jOption.addProperty("textOptionUseYn", "N"); // 텍스트 옵션 사용여부
		
		jSale.addProperty("saleStartDate", paWempGoods.getPaSaleStartDate()); // 판매기간 시작일 yyyy-MM-dd HH:00
		// 판매기간 종료일 변경여부 체크
		String add5YearEndDate = DateUtil.addMonth(paWempGoods.getPaSaleStartDate(), 60, "yyyy-MM-dd HH:00"); //판매종료일은 시작일+5년까지 설정할 수 있습니다. 판매시작일 +5년 날짜
		int check = DateUtil.compareTo(paWempGoods.getSaleEndDate(), add5YearEndDate, "yyyy-MM-dd HH:00"); // 판매기간 종료일 데이터 변경됐는지 체크
		
		if(check < 0) {// DB(TGOODS)조회 상품 판매기간 종료일이 시작일+5년 보다 작은경우
			jSale.addProperty("saleEndDate", paWempGoods.getSaleEndDate());  // TGOODS에서 조회 판매기간 종료일 yyyy-MM-dd HH:00
			paWempGoods.setPaSaleEndDate(paWempGoods.getSaleEndDate());      // TPAWEMPGOODS에도 동기화
		} else { // 크거나 같을 경우에는 기존 판매기간 종료일로 셋팅, 클경우에는 수정오류가 발생함. 판매종료일은 시작일+5년이므로..)
			jSale.addProperty("saleEndDate", add5YearEndDate); // 판매기간 종료일 yyyy-MM-dd HH:00
			paWempGoods.setPaSaleEndDate(add5YearEndDate);
		}
		
		jSale.addProperty("originPrice", paWempGoods.getSalePrice());  // 정상가격
		
		//프로모션 적용 2020.06.15 by jchoi
		double couponPrice = 0;
		//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
		if(paPromoTargetList != null && paPromoTargetList.size() > 0) {
			for(PaPromoTarget paPromoTarget : paPromoTargetList) {
				if(paPromoTarget != null) {
					if(!paPromoTarget.getProcGb().equals("D")) {
						couponPrice += paPromoTarget.getDoCost();//(자동적용쿠폰+제휴OUT)
					}
				}
			}
		}
		//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
		
		jSale.addProperty("salePrice", paWempGoods.getSalePrice() - paWempGoods.getDcAmt() - paWempGoods.getLumpSumDcAmt() - couponPrice);    // 판매가격 = 정상가격 - ARS할인 - 일시불할인 - (자동적용쿠폰+제휴OUT)
		jSale.addProperty("taxYn", paWempGoods.getTaxYn().equals("1") ? "Y" : "N");
		jSale.addProperty("purchaseMinCount", paWempGoods.getOrderMinQty());
		
		if(paWempGoods.getOrderMaxQty() > 0) {
			jSale.addProperty("purchaseLimitYn", "Y");
			jSale.addProperty("purchaseLimitDuration", "O"); //구매제한 타입 (O:1회, P:기간제한-구매제한일자 필수입력)
			jSale.addProperty("purchaseLimitCount", paWempGoods.getOrderMaxQty()); // 구매제한 개수 - 구매제한 일자에 대한 구매제한 개수 (0 ~ 9999)
		} else if(paWempGoods.getCustOrdQtyCheckYn() == 1) {  // 고객주문수량검사여부
			jSale.addProperty("purchaseLimitYn", "Y");
			jSale.addProperty("purchaseLimitCount", paWempGoods.getTermOrderQty());
			
			if(paWempGoods.getCustOrdQtyCheckTerm() > 0 ) {  // 구매제한 일자가 0보다 클경우 
				jSale.addProperty("purchaseLimitDuration", "P"); //구매제한 타입 (O:1회, P:기간제한-구매제한일자 필수입력)
				jSale.addProperty("purchaseLimitDay", paWempGoods.getCustOrdQtyCheckTerm()); //구매제한 일자 (1 ~ 30) 고객주문수량검사기간
			} else {
				jSale.addProperty("purchaseLimitDuration", "O"); 
			}
		} else { // 구매제한수량 없음
			jSale.addProperty("purchaseLimitYn", "N");
		}
		jSale.addProperty("referencePriceType", "WMP");  // 위메프가-미등록/정상가격 미입력
		
		//싱픔 상세 정보
		if("wapi-stg.wemakeprice.com".equals(ConfigUtil.getString("WEMP_HOST"))) {
			//테스트이미지
			jDetail.addProperty("basicImgUrl", "http://devimage.skstoa.com/goods/029/20020029_c.jpg");
			jImgUrlList.add("http://devimage.skstoa.com/goods/029/20020029_c.jpg");
			jImgUrlList.add("http://devimage.skstoa.com/goods/029/20020029_c.jpg");
			jDetail.add("addImgUrlList", jImgUrlList); //상품 추가 이미지 URL 최대 2개
			jDetail.addProperty("listImgUrl", "http://devimage.skstoa.com/goods/029/20020029_c.jpg");
		} else {
			//운영
			jDetail.addProperty("basicImgUrl", imageURL + paWempGoods.getImageUrl() + paWempGoods.getImageP()); // 상품 대표 이미지 URL (최대 200자) - [최적화 가이드] 사이즈: 460*460 / 최소:	200*200 / 용량: 2MB 이하 / 파일 : JPG, JPEG, PNG
			if (StringUtils.isNotBlank(paWempGoods.getImageAP())) { // 상품 추가 이미지1
				jImgUrlList.add(imageURL + paWempGoods.getImageUrl() + paWempGoods.getImageAP());
			}
			if (StringUtils.isNotBlank(paWempGoods.getImageBP())) { // 상품 추가 이미지2
				jImgUrlList.add(imageURL + paWempGoods.getImageUrl() + paWempGoods.getImageBP());
			}
			if(jImgUrlList.size() > 0) {  // 상품추가 이미지가 있으면 등록
				jDetail.add("addImgUrlList", jImgUrlList); //상품 추가 이미지 URL 최대 2개
			}
			jDetail.addProperty("listImgUrl", imageURL + paWempGoods.getImageUrl() + paWempGoods.getImageP()); // 리스팅 이미지 URL (최대 200자) - [최적화 가이드] 사이즈: 580*320 / 최소:200*100 / 용량: 2MB 이하 / 파일 : JPG, JPEG, PNG
		}
		
		jDetail.addProperty("descType", "HTML"); //상세정보 타입 (IMG:이미지 - 아래 descImgUrlList 항목에 입력, HTML:html -	아래 descHtml 항목에 입력)
		jDetail.addProperty("descHtml", paWempGoods.getDescribeExt()); // 상품 상세설명 HTML (최대 65535byte)
		
		//상품 정보고시
		JsonObject noticeItem = new JsonObject();
		JsonArray groupNoticeList = new JsonArray();
		//if(isUpdate){ // 업데이트일경우 제휴상품정보고시 번호 추가
		if(!"".equals(paWempGoods.getPaProductGroupNoticeNo()) && paWempGoods.getPaProductGroupNoticeNo() != null){	
			noticeItem.addProperty("productGroupNoticeNo", Long.parseLong(paWempGoods.getPaProductGroupNoticeNo())); // 상품에 저장된 상품정보고시 번호 (입력안하면 등록, 입력하면 수정 - 상품조회
		}
		noticeItem.addProperty("groupNoticeNo", Integer.parseInt(goodsOffer.get(0).getPaOfferType()));  // 정책번호
		// 고시항목
		for(PaGoodsOfferVO offerItem : goodsOffer){
			JsonObject groupNoticeItem = new JsonObject();
			groupNoticeItem.addProperty("noticeNo", Integer.parseInt(offerItem.getPaOfferCode()));
			groupNoticeItem.addProperty("description", offerItem.getPaOfferExt());
			groupNoticeList.add(groupNoticeItem);
		}
		noticeItem.add("noticeList", groupNoticeList);
		jNoticeList.add(noticeItem);
		
		String keyWord[] = paWempGoods.getGoodsName().replaceAll(" ", "").split(",");
		if(keyWord.length > 5){
			for(int i=0; i<5; i++){
				sb.append(keyWord[i] + ",");
			}
			jEtc.addProperty("keywordWemakeprice", sb.toString() + "sk스토아,SK스토아,SKstoa,skstoa,에스케이스토아"); // 검색키워드 (위메프) (최대 10개까지 등록 가능 / 구분값은 ,(쉼표) 사용)
		} else {
			jEtc.addProperty("keywordWemakeprice", paWempGoods.getGoodsName() + ",sk스토아,SK스토아,SKstoa,skstoa,에스케이스토아");
		}
		// 기타
		jEtc.addProperty("parallelImportYn", "N");  // 병행수입여부
		jEtc.addProperty("sellerProdCode", paWempGoods.getGoodsCode()); // 업체상품코드
		jEtc.addProperty("kcKidIsCertification", "D");
		jEtc.addProperty("kcLifeIsCertification", "D");
		jEtc.addProperty("kcElectricIsCertification", "D");
		jEtc.addProperty("kcReportIsCertification", "D");
		jEtc.addProperty("kcLifeChemistryIsCertification", "D");
		jEtc.addProperty("priceComparisonSiteYn", "Y");
		
		if(isUpdate){ // 업데이트일경우 제휴상품번호 추가
			root.addProperty("productNo", Long.parseLong(paWempGoods.getProductNo()));
		}
		root.add("basic", jBasic);
		root.add("sale", jSale);
		root.add("detail", jDetail);
		root.add("option", jOption);
		root.add("noticeList", jNoticeList);
		root.add("etc", jEtc);
		
		return root;
	}
	
	public long getOrderAbleQty(String transOrderAbleQty){
		long tempQty = 0;
		long maxOrderAbleQty = 99999;
		if(StringUtils.isNotBlank(transOrderAbleQty)){
			tempQty = Long.parseLong(transOrderAbleQty);
			if(tempQty > maxOrderAbleQty){
				tempQty = maxOrderAbleQty;
			}
		}
		return tempQty;
	}
	
	public void insertPaGoodsTransLog(ParamMap paramMap, String prodNo) throws Exception {
		String successYn = "0";
		String productNo = paramMap.getString("goodsCode");

		if (StringUtils.isBlank(paramMap.getString("startDate"))) {
			paramMap.put("startDate", systemService.getSysdatetimeToString());
		}
		if(prodNo != null && StringUtils.isNotBlank(prodNo)){
			productNo = prodNo;
			successYn = "1";
		}
		PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
		paGoodsTransLog.setGoodsCode(paramMap.getString("goodsCode"));
		paGoodsTransLog.setPaCode(paramMap.getString("paCode"));
		paGoodsTransLog.setItemNo(productNo); // 단품코드
		paGoodsTransLog.setRtnCode(paramMap.getString("code"));
		paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
		paGoodsTransLog.setSuccessYn(successYn);
		paGoodsTransLog.setProcDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
		paGoodsTransLog.setProcId(Constants.PA_WEMP_PROC_ID);
		paWempGoodsService.insertPaWempGoodsTransLogTx(paGoodsTransLog);
	}
	
	
	@Async
	public void spPagoodsSyncWemp(HttpServletRequest request, String goodsCode, String userId) throws Exception{
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
		int eTVLimitMargin = 0; 
		
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paGroupCode", "06");
		paramMap.put("dateTime", dateTime);
		paramMap.put("conditionDay", conditionDay);		
		paramMap.put("comparePaCode", "61");
		paramMap.put("feeCode", "O680");
		paramMap.put("minMarginCode", "70");
		paramMap.put("minPriceCode", "71");
		
		paramMap.put("siteGb", userId);
		paramMap.put("apiCode", "IF_PACOMMON_00_002");
		
		log.info("Step1. 위메프 상품이미지 동기화 START");
		curImageInfo = paCommonService.selectCurImageInfoList(paramMap);
		if(curImageInfo.size() > 0) {
			sb = new StringBuffer();
			for(PaGoodsImage curImageInfoTarget : curImageInfo) {
				//이미지 없는 케이스가 많아 for문안에 try catch 처리
				try {					
					curImageInfoTarget.setModifyId("PAWEMP");
					curImageInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					curImageInfoTarget.setRemark(dateTime);
					resultMsg = paCommonService.saveCurImageInfoTx(curImageInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step1 상품이미지 동기화 위메프 Fail > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
						sb.append(curImageInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step1 상품이미지 동기화 위메프 Sucess > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
				} catch(Exception e) {
					log.info("Step1. 위메프 상품이미지 동기화 : " + e.getMessage());
					paramMap.put("code", "500");
					paramMap.put("message", "Step1. 위메프 상품이미지 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}
		}
		log.info("Step1. 위메프 상품이미지 동기화 END");
		
		log.info("Step2. 위메프 상품가격 동기화 START");
		curPriceInfo = paCommonService.selectCurPriceInfoList(paramMap);
		eTVLimitMargin = paCommonService.selectEtvMarginCheck();
		
		if(curPriceInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaGoodsPriceVO curPriceInfoTarget : curPriceInfo) {
					
					paramMap.put("paCode", curPriceInfoTarget.getPaCode());
					minMarginPrice = paCommonService.selectMinMarginPrice(paramMap);
					
					if( (ComUtil.objToDouble(curPriceInfoTarget.getMarginRate()) < ComUtil.objToDouble(minMarginPrice.get("MIN_MARGIN").toString()) && "N".equals(curPriceInfoTarget.getEventYn()))
					 || (ComUtil.objToDouble(curPriceInfoTarget.getMarginRate()) < ComUtil.objToDouble(minMarginPrice.get("EVENT_MIN_MARGIN").toString()) && "Y".equals(curPriceInfoTarget.getEventYn()))							
					 || (ComUtil.objToDouble(curPriceInfoTarget.getSalePrice()) < ComUtil.objToDouble(minMarginPrice.get("MIN_SALE_PRICE").toString()))
					 || ("61".equals(curPriceInfoTarget.getSourcingMedia()) && "1".equals(curPriceInfoTarget.getMobileEtvYn()) && ComUtil.objToDouble(curPriceInfoTarget.getMarginRate()) < eTVLimitMargin) ) {
						
						stopSaleParam.put("paGroupCode", "06");
						stopSaleParam.put("paCode", curPriceInfoTarget.getPaCode());
						stopSaleParam.put("goodsCode", curPriceInfoTarget.getGoodsCode());
						stopSaleParam.put("dateTime", dateTime);
						stopSaleParam.put("userId", "PAWEMP");
						stopSaleParam.put("paGoodsCode", curPriceInfoTarget.getPaGoodsCode());
						stopSaleParam.put("note", "마진10이하 혹은 판매가100원 미만/모바일eTV 마진");
						stopSaleParam.put("priceStopSale", "Y");
						
						//연동제외처리
						resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
					} else {
						curPriceInfoTarget.setModifyId("PAWEMP");
						curPriceInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						curPriceInfoTarget.setDateTime(dateTime);
						resultMsg = paCommonService.saveCurPriceInfoTx(curPriceInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step2 상품가격 동기화 위메프 Fail > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
						sb.append(curPriceInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step2 상품가격 동기화 위메프 Sucess > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
				}
			} catch(Exception e) {
				log.info("Step2. 위메프 상품가격 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step2. 위메프 상품가격 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step2. 위메프 상품가격 동기화 END");	
		
		log.info("Step3. 위메프 고객부담배송비 동기화 START");
		curShipCostInfo = paCommonService.selectCurShipCostInfoList(paramMap);
		if(curShipCostInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaCustShipCostVO curShipCostInfoTarget : curShipCostInfo) {
					
					if(curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("QN")) {  //배송비정책이 QN인 경우 연동 제외처리
						
						stopShipParam.put("paCode", curShipCostInfoTarget.getPaCode());
						stopShipParam.put("entpCode", curShipCostInfoTarget.getEntpCode());
						stopShipParam.put("shipCostCode", curShipCostInfoTarget.getShipCostCode());
						
						curShipStopSale = paCommonService.selectCurShipStopSaleList(stopShipParam);
						for(HashMap<String, String> curShipStopSaleTarget : curShipStopSale) {
							
							stopSaleParam.put("paGroupCode", "06");
							stopSaleParam.put("paCode", curShipStopSaleTarget.get("PA_CODE").toString());
							stopSaleParam.put("goodsCode", curShipStopSaleTarget.get("GOODS_CODE").toString());
							stopSaleParam.put("dateTime", dateTime);
							stopSaleParam.put("userId", "PAWEMP");
							stopSaleParam.put("paGoodsCode", curShipStopSaleTarget.get("PA_GOODS_CODE").toString());
							stopSaleParam.put("note", "배송비유료변경");
							stopSaleParam.put("priceStopSale", "N");
							
							//연동제외처리
							resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
						}
						
					} else {
						curShipCostInfoTarget.setModifyId("PAWEMP");
						curShipCostInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						resultMsg = paCommonService.saveCurShipCostInfoTx(curShipCostInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step3 고객부담배송비 위메프 Fail");
						sb.append(curShipCostInfoTarget.getEntpCode() + "|" + curShipCostInfoTarget.getShipCostCode() + ", ");
						continue;
					}
					log.info("Step3 고객부담배송비 위메프 Sucess");
				}
			} catch(Exception e) {
				log.info("Step3. 위메프 고객부담배송비 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step3. 위메프 고객부담배송비 동기화 : " + e.getMessage() + "INFO : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step3. 위메프 고객부담배송비 동기화 END");
		
		log.info("Step4. 위메프 출고/회수지 동기화 START");
		curEntpSlipInfo = paCommonService.selectCurEntpSlipInfoList(paramMap);
		if(curEntpSlipInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaEntpSlip curEntpSlipInfoTarget : curEntpSlipInfo) {
					
					curEntpSlipInfoTarget.setModifyId("PAWEMP");
					curEntpSlipInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					resultMsg = paCommonService.saveCurEntpSlipInfoTx(curEntpSlipInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step4 출고/회수지 동기화 위메프 Fail");
						sb.append(curEntpSlipInfoTarget.getEntpCode() + ", ");
						continue;
					}
					log.info("Step4 출고/회수지 동기화 위메프 Sucess");
				}
			} catch(Exception e) {
				log.info("Step4. 위메프 출고/회수지 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step4. 위메프 출고/회수지 동기화 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step4. 위메프 출고/회수지 동기화 END");
		
		log.info("Step5. 위메프 상품판매단계 동기화 START");
		curSaleStop = paCommonService.selectCurSaleStopList(paramMap);
		
		if(curSaleStop.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curSaleStopTarget : curSaleStop) {
					
					stopSaleParam.put("paGroupCode", "06");
					stopSaleParam.put("paCode", curSaleStopTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curSaleStopTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PAWEMP");
					stopSaleParam.put("paGoodsCode", curSaleStopTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "SK스토아상품판매중단");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step5 상품판매단계 동기화 위메프 Fail > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
						sb.append(curSaleStopTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step5 상품판매단계 동기화 위메프 Sucess > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step5. 위메프 상품판매단계 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step5. 위메프 상품판매단계 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step5. 위메프 상품판매단계 동기화 END");
		
		log.info("Step6. 위메프 행사 종료 상품 마진 체크 START");
		curEventMargin = paCommonService.selectCurEventMarginList(paramMap);
		
		if(curEventMargin.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curEventMarginTarget : curEventMargin) {
					paramMap.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					//minMarginPrice = paCommonService.selectMinMarginPrice(paramMap);
					//selectCurEventMarginList 안에서 체크
					//if(ComUtil.objToDouble(curEventMarginTarget.get("MARGIN_RATE")) < ComUtil.objToDouble(minMarginPrice.get("MIN_MARGIN"))) {
					stopSaleParam.put("paGroupCode", "06");
					stopSaleParam.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curEventMarginTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PAWEMP");
					stopSaleParam.put("paGoodsCode", curEventMarginTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "행사종료");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step6 행사 종료 상품 마진 체크 위메프 Fail > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
						sb.append(curEventMarginTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step6 행사 종료 상품 마진 체크 위메프 Sucess > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
					//}					
				}
			} catch(Exception e) {
				log.info("Step6. 위메프 행사 종료 상품 마진 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step6. 위메프 행사 종료 상품 마진 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step6. 위메프 행사 종료 상품 마진 체크 END");		
		
		paCommonService.checkMassModifyGoods("06");
	}
}
