package com.cware.api.pa11st.controller;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cware.framework.common.util.StringUtil;
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
import com.cware.netshopping.domain.Pa11stGoodsVO;
import com.cware.netshopping.domain.PaCustShipCostVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pa11st.claim.service.Pa11stClaimService;
import com.cware.netshopping.pa11st.exchange.service.Pa11stExchangeService;
import com.cware.netshopping.pa11st.goods.service.Pa11stGoodsService;
import com.cware.netshopping.pa11st.order.service.Pa11stOrderService;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;

@Controller("com.cware.api.pa11st.Pa11stAsycController")
@RequestMapping(value="/pa11st/claim")
public class Pa11stAsycController extends AbstractController {

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pa11st.claim.pa11stClaimService")
	private Pa11stClaimService pa11stClaimService;
	
	@Resource(name = "pacommon.claim.paclaimService")
	private PaClaimService paclaimService;
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paorderService;
	
	@Resource(name = "pa11st.order.pa11stOrderService")
	private Pa11stOrderService pa11stOrderService;
	
	@Resource(name = "pa11st.exchange.pa11stExchangeService")
	private Pa11stExchangeService pa11stExchangeService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@Resource(name = "pa11st.goods.pa11stGoodsService")
	private Pa11stGoodsService pa11stGoodsService;
	
	/**
	 * 11번가 주문접수 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	@Async
	public void orderInputAsync(HashMap<String, String> hmSheet, HttpServletRequest request, String isLocalYN) throws Exception{
		

		String paOrderNo = hmSheet.get("PA_ORDER_NO").toString();
		ParamMap paramMap = null;
		int targetSize = 0;
		int executedRtn = 0;
		HashMap<String, Object>[] resultMap = null;
		HashMap<String, String> orderMap = null;
		HashMap<String, String> refusalMap = null;
	
		try {
			List<Object> orderInputTargetDtList = pa11stOrderService.selectOrderInputTargetDtList(hmSheet); 
			targetSize = orderInputTargetDtList.size();
			
			if(targetSize < 1){
				throw processException("msg.no.select", new String[] { "selectOrderInputTargetDtList" });
			}
			
			OrderInputVO[] orderInputVO = new OrderInputVO[targetSize];
			for(int j = 0; targetSize > j; j++){
				orderMap = (HashMap<String, String>) orderInputTargetDtList.get(j);
				
				orderInputVO[j] = new OrderInputVO(); 
				orderInputVO[j].setPaOrderCode(orderMap.get("PA_ORDER_NO").toString());
				orderInputVO[j].setPaCode(orderMap.get("PA_CODE").toString());
				orderInputVO[j].setMappingSeq(orderMap.get("MAPPING_SEQ").toString());
				orderInputVO[j].setMediaCode(orderMap.get("MEDIA_CODE").toString());
				//orderInputVO[j].setOrderDate(DateUtil.toTimestamp(orderMap.get("ORDER_DATE"), "yyyyMMddHHmmss"));
				orderInputVO[j].setOrderDate(orderMap.get("ORDER_DATE").toString());
				orderInputVO[j].setGoodsCode(orderMap.get("GOODS_CODE").toString());
				orderInputVO[j].setGoodsdtCode(orderMap.get("GOODSDT_CODE").toString());
				orderInputVO[j].setOrderQty(Integer.parseInt(String.valueOf(orderMap.get("ORDER_QTY"))));
				orderInputVO[j].setApplyDate(orderMap.get("APPLY_DATE").toString());
				orderInputVO[j].setRsaleAmt(Double.parseDouble(String.valueOf(orderMap.get("RSALE_AMT"))));
				orderInputVO[j].setSupplyPrice(Double.parseDouble(String.valueOf(orderMap.get("SUPPLY_PRICE"))));
				orderInputVO[j].setSellerDcAmt(Long.parseLong(String.valueOf(orderMap.get("SELLER_DC_AMT"))));
				orderInputVO[j].setCustName(orderMap.get("CUST_NAME").toString());
				orderInputVO[j].setCustChar("99");
				orderInputVO[j].setCustTel1(orderMap.get("CUST_TEL1").toString().replace("-", ""));
				orderInputVO[j].setCustTel2(orderMap.get("CUST_TEL2").toString().replace("-", ""));
				orderInputVO[j].setReceiverName(orderMap.get("RECEIVER_NAME").toString());
				
				String receiverTel = orderMap.get("RECEIVER_TEL").toString().replace("-", "");
				orderInputVO[j].setReceiverTel((receiverTel.length() < 4) ? "" : receiverTel);// 비정상적인 전화번호일 경우 빈공백으로 치환
				orderInputVO[j].setReceiverHp(orderMap.get("RECEIVER_HP").toString().replace("-", ""));
				orderInputVO[j].setReceiverAddr(orderMap.get("RECEIVER_ADDR").toString());
				orderInputVO[j].setMsg(orderMap.get("MSG").toString());
				orderInputVO[j].setPaGoodsCode(String.valueOf(orderMap.get("PA_GOODS_CODE")));
				orderInputVO[j].setAddrGbn(orderMap.get("TYPE_ADD").toString());   //RCVR_MAIL_NO
				orderInputVO[j].setStdAddr(orderMap.get("RCVR_BASE_ADDR").toString());   //RCVR_MAIL_NO_SEQ
				orderInputVO[j].setStdAddrDT(orderMap.get("RCVR_DTLS_ADDR").toString());  //RCVR_BASE_ADDR
				orderInputVO[j].setPostNo(orderMap.get("RCVR_MAIL_NO").toString());     //RCVR_DTLS_ADDR
				orderInputVO[j].setPostNoSeq(orderMap.get("RCVR_MAIL_NO_SEQ").toString());  //TYPE_ADD
				orderInputVO[j].setShpFeeCost(Long.parseLong(String.valueOf(orderMap.get("SHPFEE_COST"))));
				orderInputVO[j].setReceiveMethod("50"); //'C014'
				orderInputVO[j].setProcUser("PA11");
				orderInputVO[j].setPriceSeq(orderMap.get("PRICE_SEQ").toString());
				orderInputVO[j].setLumpSumDcAmt(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_DC_AMT"))));
				orderInputVO[j].setLumpSumOwnDcAmt(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_OWN_DC_AMT"))));
				orderInputVO[j].setLumpSumEntpDcAmt(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_ENTP_DC_AMT"))));
				orderInputVO[j].setCouponPromoBdate(DateUtil.toTimestamp(orderMap.get("COUPON_PROMO_BDATE")));
				orderInputVO[j].setCouponPromoEdate(DateUtil.toTimestamp(orderMap.get("COUPON_PROMO_EDATE")));
								
				if("Y".equals(isLocalYN)){
					orderInputVO[j].setIsLocalYn("Y");
				} else {
					orderInputVO[j].setIsLocalYn("N");
				}
				
				
				////////////////////PROMOTION DATA SETTING /////////////////////////////////
				// 1) DEAL ORDER PROMOTION SETTING
				if("DEAL".equals(String.valueOf( orderMap.get("ORDER_TYPE")))) {
					//딜 상품 가격, 연동 등등 검증
					String rtnMsg = checkDealOrderPrice(orderMap);
					
					if("".equals(rtnMsg)) {
						
						double sdPromoPrice  = Double.parseDouble(String.valueOf(orderMap.get("SD_PROMO_PRICE")));
						double outPromoPrice = Double.parseDouble(String.valueOf(orderMap.get("OUT_PROMO_PRICE")));	
						
						ParamMap promoParam = new ParamMap();
						promoParam.put("goodsCode"		  , orderMap.get("GOODS_CODE").toString());
						promoParam.put("paCode"			  , orderMap.get("PA_CODE").toString());
						promoParam.put("orderDate"		  , orderMap.get("ORDER_DATE").toString());
						promoParam.put("remark4N"		  , String.valueOf(orderMap.get("REMARK4_N")));
						promoParam.put("papromoAllowTerm" , hmSheet.get("PAPROMO_ALLOW_TERM"));
						promoParam.put("alcoutPromoYn"	  , "0");
						OrderpromoVO sdPromo  = paorderService.selectPaOrderPromo		(promoParam);
						promoParam.put("alcoutPromoYn"	  , "1");
						OrderpromoVO outPromo = paorderService.selectPaOrderPromo		(promoParam);
						
						if(sdPromo != null) {
							if(sdPromo.getDoAmt() != sdPromoPrice ) {
								updatePaorderM(orderInputVO[j] , "프로모션을 제대로 가져오지 못했습니다 .(SD-selectPaOrderPromo)");
								throw processException("msg.no.select", new String[]{"selectPaOrderPromo"});
							}
							orderInputVO[j].setOrderPromo(sdPromo);
						}
						
						if(outPromo != null) {
							if(outPromo.getDoAmt() != outPromoPrice) {
								updatePaorderM(orderInputVO[j] , "프로모션을 제대로 가져오지 못했습니다 .(OUT-selectPaOrderPromo)");
								throw processException("msg.no.select", new String[]{"selectPaOrderPromo"});
							}
							orderInputVO[j].setOrderPaPromo(outPromo);
						}
						continue;
					}else {
						updatePaorderM(orderInputVO[j] , rtnMsg); //ERROR-UPDATE TPAORDERM.RESULT_MESSAGE
						throw processException("pa.fail_order_input", new String[]{ rtnMsg });
					}
				}//End of DEAL ORDER PROMOTION CHECK
				
				
				// 단품명 불일치 확인
				ParamMap goodsDtParam = new ParamMap();
				goodsDtParam.put("goodsCode", orderMap.get("GOODS_CODE").toString());
				goodsDtParam.put("goodsDtCode", orderMap.get("GOODSDT_CODE").toString());
				
				if(orderMap.get("SLCT_PRD_OPT_NM").toString().length() > 4) {
					String orderGoodsDtName = orderMap.get("SLCT_PRD_OPT_NM").toString().substring(4).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
					int lastIdx = orderGoodsDtName.lastIndexOf("-"); // "-n개" 제거 위함 (마지막에 있는 "-" 자리를 찾음)
					orderGoodsDtName = orderGoodsDtName.substring(0, lastIdx); // "-n개" 앞의 문자열만 추출
					
					String goodsDtName = paorderService.selectOrderGoodsDtName(goodsDtParam);
					goodsDtName = goodsDtName.replaceAll("\\[", "").replaceAll("\\]", "");
					
					if(!goodsDtName.equals(orderGoodsDtName)) {
						updatePaorderM(orderInputVO[j] , "단품명이 일치하지 않습니다.");
						throw processException("pa.fail_order_input", new String[]{ "단품명이 일치하지 않습니다." });
					}
				} else {
					updatePaorderM(orderInputVO[j] , "단품명이 불명확합니다.");
					throw processException("pa.fail_order_input", new String[]{ "단품명이 불명확합니다." });
				}
				
				// 11번가 단품코드 중복 확인
				goodsDtParam.put("paOptionCode", orderMap.get("PA_OPTION_CODE").toString());
				goodsDtParam.put("paCode", orderMap.get("PA_CODE").toString());
				
				int goodsDtCount = paorderService.selectOrderGoodsDtDupleCheck(goodsDtParam);
				if(goodsDtCount > 1) {
					updatePaorderM(orderInputVO[j] , "동일한 단품코드가 존재합니다.");
					throw processException("pa.fail_order_input", new String[]{ "동일한 단품코드가 존재합니다." });
				}
				
				// 가격 비교
				String paApplyDate = orderMap.get("APPLY_DATE").toString(); // TPAGOODSPRICE 기준 APPLY_DATE
				String stoaApplyDate = orderMap.get("STOA_APPLY_DATE").toString(); // TGOODSPRICE 기준 APPLY_DATE
				String paOrderDate = orderMap.get("ORDER_DATE").toString(); // 11번가 주문일시
				
				if(!paApplyDate.equals(stoaApplyDate)) {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					Date orderDate = format.parse(paOrderDate.substring(0, 8));
			        Date stoaDate = format.parse(stoaApplyDate.substring(0, 8));
			        
			        // 스토아 최신 가격 적용일자와 제휴 주문일자 차이 계산
			        long diffDays = orderDate.getTime() - stoaDate.getTime();
			        diffDays = diffDays / (24 * 60 * 60 * 1000);
			        
					if(diffDays >= 3) { // 3일 이상 차이나는 경우
						updatePaorderM(orderInputVO[j] , "가격 정보가 잘못되었습니다.");
						throw processException("pa.fail_order_input", new String[]{ "가격 정보가 잘못되었습니다." });
					}
				}
				
				// 11번가 주문 결제가(RSALE_AMT) 확인
				if(orderInputVO[j].getRsaleAmt() < 0) { // rsaleAmt : 11번가 결제금액 - 배송비
					updatePaorderM(orderInputVO[j] , "결제 금액이 0원 미만입니다.");
					throw processException("pa.fail_order_input", new String[]{ "결제 금액이 0원 미만입니다." });
				}
				
				// 2) 일반 상품 주문 프로모션 가격 계산 
				double sellerDcAmt   = Double.parseDouble(String.valueOf(orderMap.get("SELLER_DISCOUNT_PRICE"))) / Integer.parseInt(String.valueOf( orderMap.get("ORDER_QTY") ));
				double arsDcAmt		 = Double.parseDouble(String.valueOf(orderMap.get("DC_AMT")));
				double lumpSumDcAmt	 = Double.parseDouble(String.valueOf(orderMap.get("LUMP_SUM_DC_AMT"))) / Integer.parseInt(String.valueOf( orderMap.get("ORDER_QTY") ));
				double outPromoPrice = Double.parseDouble(String.valueOf(orderMap.get("OUT_PROMO_PRICE")));		
				double sumDcAmt		 = 0;
				
				if(sellerDcAmt < 1 ) {
					orderInputVO[j].setSellerDcAmtExists("N");
					continue;  // 11번가 비회원 및 판매자 할인 금액이 없는 경우 가격 비교 및 프로모션 데이터 세팅에 대한 수행을 하지 않는다.
				}

				sumDcAmt = arsDcAmt + lumpSumDcAmt +  outPromoPrice;
				
				if(sumDcAmt != sellerDcAmt) {
					updatePaorderM(orderInputVO[j] , "상품 가격이 일치하지 않습니다.(selectOrderInputTargetDtList_Dismatch_Price)"); //ERROR-UPDATE TPAORDERM.RESULT_MESSAGE
					throw processException("msg.no.select", new String[]{"selectOrderInputTargetDtList_Dismatch_Price"});
				}
				
	            // 3) 프로모션(TORDERPROMO) 용 객체 생성            
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
				//orderInputVO[j].setOrderPromo				(selectOrderPromo(orderMap));
				//orderInputVO[j].setOrderPaPromo			(selectOrderPaPromo(orderMap));	//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)		
				
			}
			
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
			
			for(int j = 0; targetSize > j; j++){
				paramMap = new ParamMap();
				paramMap.setParamMap(resultMap[j]);
				paramMap.replaceCamel();
				
				if(paramMap.getString("resultCode").equals("100001") ){
					//재고부족 or 판매불가 상태.
					ParamMap apiResult = new ParamMap();
					refusalMap = pa11stOrderService.selectRefusalInfo(paramMap.getString("mappingSeq"));
					refusalMap.put("ordCnRsnCd", 	"09");
					refusalMap.put("ordCnDtlsRsn", 	getMessage("pa.out_of_stock_due_shortage_process"));
					refusalMap.put("paCode", 		orderMap.get("PA_CODE").toString());
					//= 판매불가처리 호출.
					apiResult = reqSaleRefusalProc(refusalMap);
					
					if(apiResult.getInt("HTTP_RESPONSE_CODE") != 200){
						throw processException("msg.cannot_save", new String[] { "IF_PA11STAPI_03_004 fail - " +  apiResult.getString("EXCEPTION_MESSAGE")});
					}
					continue;
			
				}else if(paramMap.getString("resultCode").equals("100002")){
					//판매가 또는 매입가 제휴 연동 실패로 인한 판매취소(거부)
					ParamMap apiResult = new ParamMap();
					refusalMap = pa11stOrderService.selectRefusalInfo(paramMap.getString("mappingSeq"));
					refusalMap.put("ordCnRsnCd", 	"07");
					refusalMap.put("ordCnDtlsRsn", 	getMessage("pa.differentBuyorSaleprice"));
					refusalMap.put("paCode", 		orderMap.get("PA_CODE").toString());
					//= 판매불가처리 호출.
					apiResult = reqSaleRefusalProc(refusalMap);
					
					if(apiResult.getInt("HTTP_RESPONSE_CODE") != 200){
						throw processException("msg.cannot_save", new String[] { "IF_PA11STAPI_03_004 fail - " +  apiResult.getString("EXCEPTION_MESSAGE")});
					}
					continue;
				}
					
					
			}
		} catch (Exception e) {
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "pa_order_no : " + paOrderNo + " > " + e.getMessage());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PA11ST_ORDER_INPUT");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	
	private OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception{
		/*
		orderPromo.setPromoNo			("");  //프로모션 넘버
		orderPromo.setDoType			("");  //혜택구분 B007
		orderPromo.setProcAmt			(0);   //금액 
		orderPromo.setProcCost			(0);   //금액 (개당)
		orderPromo.setOwnProcCost		(0);   //당사부담금액
		orderPromo.setEntpProcCost		(0);   //업체부담금액
		orderPromo.setEntpCode			("");  //업체코드
		orderPromo.setCouponYn			("");  //쿠폰유무
		orderPromo.setCouponPromoBdate	(null); //쿠폰적용일
		orderPromo.setCouponPromoEdate	(null); //쿠폰만료일
		*/
		
		return pa11stOrderService.selectOrderPromo(orderMap);
	}
	
	private OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception{
		/*
		orderPromo.setPromoNo			("");  //프로모션 넘버
		orderPromo.setDoType			("");  //혜택구분 B007
		orderPromo.setProcAmt			(0);   //금액 
		orderPromo.setProcCost			(0);   //금액 (개당)
		orderPromo.setOwnProcCost		(0);   //당사부담금액
		orderPromo.setEntpProcCost		(0);   //업체부담금액
		orderPromo.setEntpCode			("");  //업체코드
		orderPromo.setCouponYn			("");  //쿠폰유무
		orderPromo.setCouponPromoBdate	(null); //쿠폰적용일
		orderPromo.setCouponPromoEdate	(null); //쿠폰만료일
		 */
		
		return pa11stOrderService.selectOrderPaPromo(orderMap);
	}
		
	
	/**
	 * 11번가 주문취소 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void cancelInputAsync(HashMap<String, Object> cancelMap, HttpServletRequest request) throws Exception{
		HashMap<String, Object> hmSheet = null;
		ParamMap paramMap = null;
		ParamMap preCancelMap = null;
		String cancelCode = null;
		
		int targetSize = 0;
		int executedRtn = 0;
	
		
		
		try {
			paramMap = new ParamMap();
			paramMap.setParamMap(cancelMap);
			paramMap.replaceCamel();
			
			List<Object> cancelInputTargetDtList = pa11stOrderService.selectCancelInputTargetDtList(paramMap); 
			targetSize = cancelInputTargetDtList.size();
	
			
			if(targetSize != 1){
				
				checkErrorCase(paramMap);
				
				
			}else {
				hmSheet = (HashMap<String, Object>) cancelInputTargetDtList.get(0);
			
				if(hmSheet.get("PRE_CANCEL_YN").toString().equals("0")){
					//= 일반취소건 처리.
					CancelInputVO cancelInputVO = new CancelInputVO();
					cancelInputVO.setMappingSeq(hmSheet.get("MAPPING_SEQ").toString());
					cancelInputVO.setOrderNo(hmSheet.get("ORDER_NO").toString());
					cancelInputVO.setOrderGSeq(hmSheet.get("ORDER_G_SEQ").toString());
					cancelInputVO.setCancelQty(Integer.parseInt(String.valueOf(hmSheet.get("PA_PROC_QTY"))));
					
					cancelCode = hmSheet.get("CANCEL_CODE").toString();
					if (cancelCode == null || "".equals(cancelCode) || cancelCode.length() != 6 ){
						cancelCode = "620198";	
					}
							
					cancelInputVO.setCancelCode(cancelCode);
					cancelInputVO.setProcId("PA11");
				
				
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
					preCancelMap = new ParamMap();
					preCancelMap.setParamMap(hmSheet);
					preCancelMap.replaceCamel();
				
					//=pa.before_order_create_cancel = 주문생성 이전 취소건
					preCancelMap.put("preCancelReason", getMessage("pa.before_order_create_cancel"));
					executedRtn = pa11stOrderService.updatePreCancelYnTx(preCancelMap);
					if(executedRtn != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
					}
				}
			}
		} catch (Exception e) {
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "pa_claim_no : " + cancelMap.get("PA_CLAIM_NO") + " > " + e.toString());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PA11ST_CANCEL_INPUT");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	/**
	 * 11번가 반품 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void orderClaimAsync(HashMap<String, Object> claimMap, HttpServletRequest request,  String isLocalYn) throws Exception{
		HashMap<String, Object> hmSheet = null;
		ParamMap paramMap = null;
		List<Object> orderClaimTargetDtList = null;
		int targetSize 	= 0;
		int executedRtn = 0;
	
		
		try {
			paramMap = new ParamMap();
			paramMap.setParamMap(claimMap);
			paramMap.replaceCamel();
			if(paramMap.getString("paOrderGb").equals("30")){
				orderClaimTargetDtList = pa11stClaimService.selectOrderCalimTargetDt30List(paramMap); 
			} else {
				//출고전 반품 처리(출하지시 후 , 운송장x인 상태에서 취소를 하는경우)
				orderClaimTargetDtList = pa11stClaimService.selectOrderCalimTargetDt20List(paramMap);
			}
			targetSize = orderClaimTargetDtList.size();
			if(targetSize != 1){
				throw processException("msg.no.select", new String[] { "selectOrderCalimTargetDtList" });
			}
			hmSheet = (HashMap<String, Object>) orderClaimTargetDtList.get(0);
			
			//= 반품 공통화 호출.
			OrderClaimVO orderClaimVO = new OrderClaimVO();
			orderClaimVO.setMappingSeq(hmSheet.get("MAPPING_SEQ").toString());
			orderClaimVO.setOrderNo(hmSheet.get("ORDER_NO").toString());
			orderClaimVO.setOrderGSeq(hmSheet.get("ORDER_G_SEQ").toString());
			orderClaimVO.setClaimQty(Integer.parseInt(String.valueOf(hmSheet.get("PA_PROC_QTY"))));
			orderClaimVO.setClaimCode("999");
			orderClaimVO.setClaimDesc(hmSheet.get("CLAIM_DESC").toString());
			orderClaimVO.setReturnName(hmSheet.get("RETURN_NAME").toString());
			
            String returnTel = hmSheet.get("RETURN_TEL").toString().replace("-", "");
            orderClaimVO.setReturnTel((returnTel.length() < 4) ? "" : returnTel);
			orderClaimVO.setReturnHp(hmSheet.get("RETURN_HP").toString().replace("-", ""));
			orderClaimVO.setReturnAddr(hmSheet.get("RETURN_ADDR").toString());
			orderClaimVO.setCustDelyYn(hmSheet.get("CUST_DELY_YN").toString()); //= 고객 직접발송일 경우 : 1, 아닐경우 : 0
			orderClaimVO.setInsertId("PA11");
			
			if(hmSheet.get("CUST_DELY_YN").toString().equals("1")){
				orderClaimVO.setReturnDelyGb(hmSheet.get("RETURN_DELY_GB").toString()); //= 직접발송일 경우 11번가 고객입력 배송사코드.
				orderClaimVO.setReturnSlipNo(StringUtil.null2string(hmSheet.get("RETURN_SLIP_NO").toString(), getMessage("pa.cust_dely_slip_no_empty"))); //= 직접발송일 경우 11번가 고객입력 운송장번호.
			} else {
				orderClaimVO.setReturnDelyGb("");
				orderClaimVO.setReturnSlipNo("");
			}
			
			
			orderClaimVO.setOutBefClaimGb(hmSheet.get("OUT_BEF_CLAIM_GB").toString());
			
		
			//20180502 PARKSEONJUN 11번가에서 반품접수받은배송지정보 세팅 및 IP정보로 로컬여부 추가 로컬에서 주소정제안됨 
			if(paramMap.getString("paOrderGb").equals("30")){
			
				String rcvrMailNoSeq;
				if (hmSheet.get("RCVR_MAIL_NO_SEQ") == null || "".equals(hmSheet.get("RCVR_MAIL_NO_SEQ"))){
					rcvrMailNoSeq = "001";
				}else{
					rcvrMailNoSeq = hmSheet.get("RCVR_MAIL_NO_SEQ").toString();
				}
				
				orderClaimVO.setRcvrMailNo		(hmSheet.get("RCVR_MAIL_NO").toString());
				orderClaimVO.setRcvrMailNoSeq   (rcvrMailNoSeq);
				orderClaimVO.setRcvrBaseAddr	(hmSheet.get("RCVR_BASE_ADDR").toString());
				orderClaimVO.setRcvrDtlsAddr	(hmSheet.get("RCVR_DTLS_ADDR").toString());
				orderClaimVO.setRcvrTypeAdd		(hmSheet.get("RCVR_TYPE_ADD").toString());
				
			}else{
				orderClaimVO.setRcvrMailNo		("");
				orderClaimVO.setRcvrMailNoSeq   ("");
				orderClaimVO.setRcvrBaseAddr	("");
				orderClaimVO.setRcvrDtlsAddr	("");
				orderClaimVO.setRcvrTypeAdd		("");
			}
			
			
			if( "Y".equals(isLocalYn)){
				orderClaimVO.setLocalYn("Y");
			}else{
				orderClaimVO.setLocalYn("N");
			}
			
			
			String cLaimCode = hmSheet.get("CLAIM_CODE").toString();
			
			if("11번가 취소(당사)".equals(orderClaimVO.getClaimDesc())) {
				cLaimCode = hmSheet.get("CANCEL_CLAIM_CODE").toString();
			}
			
			if (cLaimCode == null || ("").equals(cLaimCode)  || cLaimCode.length() != 6){
			
				if(orderClaimVO.getOutBefClaimGb().equals("1") || "11번가 취소(당사)".equals(orderClaimVO.getClaimDesc())){  //출하지시전 반품, 취소에 걸려있음..
					cLaimCode = "620198" ; //Defalut;
					
				}else{
					cLaimCode = "630197" ; //Defalut;							
				}
			}
					
			orderClaimVO.setCsLgroup(cLaimCode.substring(0,2));
			orderClaimVO.setCsMgroup(cLaimCode.substring(2,4));
			orderClaimVO.setCsSgroup(cLaimCode.substring(4,6));
			orderClaimVO.setCsLmsCode(cLaimCode);		
			orderClaimVO.setStandardType("0");     //// 기준내 :1 ,기준외 : 0
			
			
			//11번가에 고객이 배송비를 지불 했느냐 안했느냐 여부..
			Long shpFeeYn = Long.parseLong(String.valueOf(hmSheet.get("CLM_LST_DLV_CST")));

			String dlvCstRespnClf; //반품완료API의 '부담여부'
			if(hmSheet.get("DLV_CST_RESPN_CLF") == null) {
				dlvCstRespnClf = "0";
			} else {
				dlvCstRespnClf = hmSheet.get("DLV_CST_RESPN_CLF").toString();
			}
			
			if (shpFeeYn > 0){
				if (!"02".equals(dlvCstRespnClf)) {
					orderClaimVO.setShpfeeYn("1");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
				} else {
					orderClaimVO.setShpfeeYn("0");
				}
			}else{
				orderClaimVO.setShpfeeYn("0");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2					
			}
			
			//출하지시 이후 취소건은 반품인데, 이경우 배송비 부과여부를 무상으로 처리한다.
			if(("1").equals(orderClaimVO.getOutBefClaimGb()) && "20".equals(paramMap.getString("paOrderGb"))){
				orderClaimVO.setShpfeeYn("0");
				orderClaimVO.setIs20Claim(true);
			}else{
				orderClaimVO.setIs20Claim(false);
			}
			
			/* --> 11번가는 공통에서 주문배송지를 바라보게 해놓음
			if(!orderClaimVO.getIsIs20Claim()) { //일반반품일 경우 (출고전반품 제외)
				//기존주소와 같은지 확인
				String checkAddr = "";
				paramMap.put("claimGb", paramMap.getString("paOrderGb"));
				checkAddr = pa11stClaimService.compareAddress(paramMap);// PM.PA_ORDER_NO, PM.PA_ORDER_SEQ, PM.PA_CLAIM_NO, PM.PA_ORDER_GB
				orderClaimVO.setSameAddr(checkAddr);
			}*/

			try {
				paclaimService.saveOrderClaimTx(orderClaimVO);
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
					paramMap.put("apiCode", 	"PA11ST_ORDER_CLAIM");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	
	
	/**
	 * 11번가 반품 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	public void orderClaimAsync4Test(HttpServletRequest request) throws Exception{
	
		
		OrderClaimVO orderClaimVO = new OrderClaimVO();
		orderClaimVO.setMappingSeq("1234");
		orderClaimVO.setOrderNo("20180509047438");
		orderClaimVO.setOrderGSeq("001");
		orderClaimVO.setClaimQty(1);//부분반품 불가
		
		orderClaimVO.setClaimCode("");
		orderClaimVO.setClaimType("");
		orderClaimVO.setClaimDesc("");
		
		orderClaimVO.setReturnName("백호선");
		orderClaimVO.setReturnTel("01062987099");
		orderClaimVO.setReturnHp("01062987099");
		orderClaimVO.setReturnAddr("서울특별시 강북구 솔샘로");
		
		
		orderClaimVO.setCustDelyYn("0"); //= 고객 직접발송일 경우 : 1, 아닐경우 : 0
		orderClaimVO.setReturnDelyGb(""); //= 직접발송일 경우 11번가 고객입력 배송사코드.
		orderClaimVO.setReturnSlipNo(""); //= 직접발송일 경우 11번가 고객입력 운송장번호.
		//StringUtil.null2string(hmSheet.get("RETURN_SLIP_NO").toString(), getMessage("pa.cust_dely_slip_no_empty"))	
		
			
		orderClaimVO.setOutBefClaimGb("0");
		orderClaimVO.setShipcostChargeYn("0");
		orderClaimVO.setLocalYn("Y");
		
		orderClaimVO.setRcvrMailNo		("01192"); //우편번호
		orderClaimVO.setRcvrBaseAddr	("서울특별시 강북구 솔샘로"); //기본주소
		orderClaimVO.setRcvrDtlsAddr	("174, (미아동, SK북한산시티아파트)"); //디테일주소
		orderClaimVO.setRcvrTypeAdd		("01"); //도로명여부
		
		
		orderClaimVO.setCsLgroup("86");
		orderClaimVO.setCsMgroup("06");
		orderClaimVO.setCsSgroup("01");
		orderClaimVO.setCsLmsCode("860601");
		
		orderClaimVO.setStandardType("0");
		orderClaimVO.setShpfeeYn("3");
		
		
		/*
		if(orderClaimVO.getOutBefClaimGb().equals("1")){
			//= 출하지시 이후 취소건은 배송비 생성 X
			orderClaimVO.setShipcostChargeYn("0");
		} else {
			orderClaimVO.setShipcostChargeYn(hmSheet.get("SHIPCOST_CHARGE_YN").toString());
		}
		*/

		paclaimService.saveOrderClaimTx(orderClaimVO);
	}

	/**
	 * 11번가 반품취소 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void claimCancelAsync(HashMap<String, Object> claimMap, HttpServletRequest request) throws Exception{
		HashMap<String, Object> hmSheet = null;
		ParamMap paramMap = null;
		ParamMap preCancelMap = null;
		int targetSize = 0;
		int executedRtn = 0;
		
		try {
			paramMap = new ParamMap();
			paramMap.setParamMap(claimMap);
			paramMap.replaceCamel();
			
			List<Object> cancelInputTargetDtList = pa11stClaimService.selectClaimCancelTargetDtList(paramMap); 
			targetSize = cancelInputTargetDtList.size();
			if(targetSize != 1){
				//error Log 수집 - 특수케이스
				throw processException("msg.no.select", new String[] { "selectOrderInputTargetDtList" });
			}
			hmSheet = (HashMap<String, Object>) cancelInputTargetDtList.get(0);
			
			if(hmSheet.get("PRE_CANCEL_YN").toString().equals("0")){
				//= 일반 반품취소건 처리.
				OrderClaimVO orderClaimVO = new OrderClaimVO();
				orderClaimVO.setMappingSeq(hmSheet.get("MAPPING_SEQ").toString());
				orderClaimVO.setOrderNo(hmSheet.get("ORDER_NO").toString());
				orderClaimVO.setOrderGSeq(hmSheet.get("ORDER_G_SEQ").toString());
				orderClaimVO.setOrderWSeq(hmSheet.get("ORDER_W_SEQ").toString());
				orderClaimVO.setClaimQty(Integer.parseInt(String.valueOf(hmSheet.get("PA_PROC_QTY"))));
				orderClaimVO.setClaimCode(hmSheet.get("CANCEL_CODE").toString());
				orderClaimVO.setInsertId("PA11");
				
				try {
					paclaimService.saveClaimCancelTx(orderClaimVO);
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
			} else {
				//= 기취소건 처리(반품생성 이전 취소건)
				preCancelMap = new ParamMap();
				preCancelMap.setParamMap(hmSheet);
				preCancelMap.replaceCamel();
				
				//=pa.before_claim_create_cancel = 반품생성 이전 취소건
				preCancelMap.put("preCancelReason", getMessage("pa.before_claim_create_cancel"));
				executedRtn = pa11stOrderService.updatePreCancelYnTx(preCancelMap);
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
				}
			}
		} catch (Exception e) {
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "pa_claim_no : " + claimMap.get("PA_CLAIM_NO") + " > " + e.toString());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PA11ST_CLAIM_CANCEL");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	/**
	 * 11번가 교환 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void orderChangeAsync(HashMap<String, Object> hmSheet, HttpServletRequest request, String isLocalYN) throws Exception {
		HashMap<String, Object>[] resultMap = null;
		HashMap<String, String> changeMap = null;
		HashMap<String, String> rejectMap = null;
		ParamMap paramMap = null;
		int targetSize = 0;
		int executedRtn = 0;
		String claimCode = null;
		
		paramMap = new ParamMap();
		paramMap.setParamMap(hmSheet);
		paramMap.replaceCamel();
		
		try {
			List<Object> orderChangeTargetDtList = pa11stExchangeService.selectOrderChangeTargetDtList(paramMap); 
			targetSize = orderChangeTargetDtList.size();
			if(targetSize != 2){
				//error Log 수집 - 특수케이스 , targetSize가 없을 때 모니터링
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
			
				
				claimCode = changeMap.get("CLAIM_CODE").toString();
				if(claimCode.length() < 6 ){
					claimCode = "640199";
				}
				orderClaimVO[j].setClaimCode(claimCode);
				
		
				orderClaimVO[j].setClaimDesc(changeMap.get("CLAIM_DESC").toString());
				orderClaimVO[j].setShipcostChargeYn(changeMap.get("SHIPCOST_CHARGE_YN").toString());
				orderClaimVO[j].setAdminProcYn(changeMap.get("ADMIN_PROC_YN").toString());
				
				if(changeMap.get("CLAIM_GB").toString().equals("40")) {
					orderClaimVO[j].setReturnName(changeMap.get("RECEIVER_NAME").toString());
					String returnTel = changeMap.get("RECEIVER_TEL").toString().replace("-", "");
					orderClaimVO[j].setReturnTel((returnTel.length() < 4) ? "" : returnTel);
					orderClaimVO[j].setReturnHp(changeMap.get("RECEIVER_HP").toString().replace("-", ""));
					orderClaimVO[j].setReturnAddr(changeMap.get("RECEIVER_ADDR").toString());
					orderClaimVO[j].setCustDelyYn("0"); //= 교환배송건은 0 고정.
					orderClaimVO[j].setReturnDelyGb(""); //= 교환배송건은 "" 고정.
					orderClaimVO[j].setReturnSlipNo(""); //= 교환배송건은 "" 고정.
				} else {
					orderClaimVO[j].setReturnName(changeMap.get("RETURN_NAME").toString());
					String returnTel = changeMap.get("RETURN_TEL").toString().replace("-", "");
					orderClaimVO[j].setReturnTel((returnTel.length() < 4) ? "" : returnTel);
					String returnHp = changeMap.get("RETURN_HP").toString().replace("-", "");
					orderClaimVO[j].setReturnHp((returnHp.length() < 4) ? "" : returnHp);
					orderClaimVO[j].setReturnAddr(changeMap.get("RETURN_ADDR").toString());
					orderClaimVO[j].setCustDelyYn(changeMap.get("CUST_DELY_YN").toString()); //= 고객 직접발송일 경우 : 1, 아닐경우 : 0
					if(changeMap.get("CUST_DELY_YN").toString().equals("1")){
						orderClaimVO[j].setReturnDelyGb(changeMap.get("RETURN_DELY_GB").toString()); //= 직접발송일 경우 11번가 고객입력 배송사코드.
						orderClaimVO[j].setReturnSlipNo(StringUtil.null2string(changeMap.get("RETURN_SLIP_NO").toString(), getMessage("pa.cust_dely_slip_no_empty"))); //= 직접발송일 경우 11번가 고객입력 운송장번호.
					} else {
						orderClaimVO[j].setReturnDelyGb("");
						orderClaimVO[j].setReturnSlipNo("");
					}
				}
				

				Long shpFeeAmt = Long.parseLong(String.valueOf(changeMap.get("CLM_LST_DLV_CST")));
				if (shpFeeAmt > 0 ){
					orderClaimVO[j].setShpfeeYn("1");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
				}else{
					orderClaimVO[j].setShpfeeYn("0");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2
				}
				orderClaimVO[j].setShpFeeAmt(shpFeeAmt);
						
	
				String rcvrMailNoSeq = "001";
				
				if (changeMap.get("RCVR_MAIL_NO_SEQ") != null && "".equals(changeMap.get("RCVR_MAIL_NO_SEQ").toString())){
					rcvrMailNoSeq = changeMap.get("RCVR_MAIL_NO_SEQ").toString();
				}

				orderClaimVO[j].setRcvrMailNo	(changeMap.get("RCVR_MAIL_NO").toString());
				orderClaimVO[j].setRcvrMailNoSeq(rcvrMailNoSeq);
				orderClaimVO[j].setRcvrBaseAddr	(changeMap.get("RCVR_BASE_ADDR").toString());
				orderClaimVO[j].setRcvrDtlsAddr	(changeMap.get("RCVR_DTLS_ADDR").toString());
				orderClaimVO[j].setRcvrTypeAdd	(changeMap.get("RCVR_TYPE_ADD").toString());
				orderClaimVO[j].setLocalYn      (isLocalYN);
				orderClaimVO[j].setInsertId     ("PA11");

				//제휴 인입 주소 확인
				String checkAddr = "";
				paramMap.put("claimGb", changeMap.get("CLAIM_GB").toString());
				paramMap.put("paOrderGb", "40");
				checkAddr = pa11stClaimService.compareAddress(paramMap);
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
					//pa.out_of_stock_exchange_reject = 재고부족으로 인한 교환불가.
					ParamMap apiResult = new ParamMap();
					
					rejectMap = pa11stExchangeService.selectRejectInfo(paramMap.getString("mappingSeq"));
					rejectMap.put("refsRsnCd", 	"204");
					rejectMap.put("refsRsn", 	getMessage("pa.out_of_stock_exchange_reject"));
					//= 판매불가처리 호출.
					apiResult = reqExchangeRejectProc(rejectMap);
					
					if(apiResult.getInt("HTTP_RESPONSE_CODE") != 200){
						throw processException("msg.cannot_save", new String[] { "IF_PA11STAPI_03_016 fail - " +  apiResult.getString("EXCEPTION_MESSAGE")});
					}
					continue;
				}
			}
		} catch (Exception e) {
			paramMap.put("message", "pa_claim_no : " + hmSheet.get("PA_CLAIM_NO") + " > " + e.toString());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PA11ST_ORDER_CHANGE");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	
	
	
	/**
	 * 11번가 교환취소 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void changeCancelAsync(HashMap<String, Object> cancelMap, HttpServletRequest request) throws Exception {
		HashMap<String, Object> hmSheet = null;
		ParamMap paramMap = null;
		ParamMap preCancelMap = null;
		int targetSize = 0;
		int executedRtn = 0;
		
		try {
			paramMap = new ParamMap();
			paramMap.setParamMap(cancelMap);
			paramMap.replaceCamel();
			
			List<Object> orderChangeTargetDtList = pa11stExchangeService.selectChangeCancelTargetDtList(paramMap); 
			targetSize = orderChangeTargetDtList.size();
			if(targetSize != 2){
				//error Log 수집 - 특수케이스, targetSize가 없을 때 모니터링
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
					orderClaimVO[i].setShpFeeAmt(Long.parseLong(String.valueOf(hmSheet.get("SHIPFEE_COST"))));
					orderClaimVO[i].setInsertId("PA11");
					
					
				} else {
			        //= 기취소건 처리(반품생성 이전 취소건)
			        preCancelMap = new ParamMap();
			        preCancelMap.setParamMap(hmSheet);
			        preCancelMap.replaceCamel();
			        
			        //=pa.before_claim_create_cancel = 교환생성 이전 취소건
			        preCancelMap.put("preCancelReason", getMessage("pa.before_change_create_cancel"));
			        executedRtn = pa11stOrderService.updatePreCancelYnTx(preCancelMap);
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
					
					executedRtn = paorderService.updatePaOrdermTx(paramMap);
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
					paramMap.put("apiCode", 	"PA11ST_CHANGE_CANCEL");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	
	/**
	 * 판매불가처리 API 호출 - IF_PA11STAPI_03_004
	 * 
	 * @param ParamMap
	 * @return ParamMap
	 * @throws Exception
	 */
	public ParamMap reqSaleRefusalProc(HashMap<String, String> reqMap) throws Exception {
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		ParamMap configMap = null;
		Properties params = null;
		
		configMap = new ParamMap();
		configMap.put("apiCode", "IF_PA11STAPI_03_004");
		
		apiInfo = systemService.selectPaApiInfo(configMap);
		
		params = new Properties();
		params.setProperty("ordNo",			reqMap.get("PA_ORDER_NO").toString());
		params.setProperty("ordPrdSeq",		reqMap.get("PA_ORDER_SEQ").toString());
		params.setProperty("ordCnRsnCd", 	reqMap.get("ordCnRsnCd").toString());
		params.setProperty("ordCnDtlsRsn", 	URLEncoder.encode(reqMap.get("ordCnDtlsRsn").toString(), "UTF-8"));
		params.setProperty("paCode", 		reqMap.get("paCode").toString());
		
		configMap.put("HTTP_CONFIG_ADDRESS",		ConfigUtil.getString("OPEN_API_BASE_ADDRESS"));
		configMap.put("HTTP_CONFIG_PROTOCOL",		ConfigUtil.getString("OPEN_API_BASE_PROTOCOL"));
		configMap.put("HTTP_CONFIG_PORT",			ConfigUtil.getString("OPEN_API_BASE_PORT"));
		configMap.put("HTTP_CONFIG_TIME_OUT", 		5000);
		configMap.put("HTTP_CONFIG_ADDRESS_DETAIL", apiInfo.get("INTERNAL_URL"));
		configMap.put("HTTP_CONFIG_CONTENT_TYPE",	"application/x-www-form-urlencoded;charset=utf-8");
		
		configMap = HttpUtil.getGetHttpClient(configMap, params);		

		return configMap;
	}
	
	/**
	 * 교환거부처리 API 호출 - IF_PA11STAPI_03_016
	 * 
	 * @param ParamMap
	 * @return ParamMap
	 * @throws Exception
	 */
	public ParamMap reqExchangeRejectProc(HashMap<String, String> reqMap) throws Exception {
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		ParamMap configMap = null;
		Properties params = null;
		
		configMap = new ParamMap();
		configMap.put("apiCode", "IF_PA11STAPI_03_016");
		
		apiInfo = systemService.selectPaApiInfo(configMap);
		
		params = new Properties();
		params.setProperty("ordNo",			reqMap.get("PA_ORDER_NO").toString());
		params.setProperty("ordPrdSeq",		reqMap.get("PA_ORDER_SEQ").toString());
		params.setProperty("clmReqSeq", 	reqMap.get("PA_CLAIM_NO").toString());
		params.setProperty("refsRsnCd", 	reqMap.get("refsRsnCd").toString());
		params.setProperty("refsRsn", 		URLEncoder.encode(reqMap.get("refsRsn").toString(), "UTF-8"));
		params.setProperty("paCode", 		reqMap.get("PA_CODE").toString());
		
		configMap.put("HTTP_CONFIG_ADDRESS",		ConfigUtil.getString("OPEN_API_BASE_ADDRESS"));
		configMap.put("HTTP_CONFIG_PROTOCOL",		ConfigUtil.getString("OPEN_API_BASE_PROTOCOL"));
		configMap.put("HTTP_CONFIG_PORT",			ConfigUtil.getString("OPEN_API_BASE_PORT"));
		configMap.put("HTTP_CONFIG_TIME_OUT", 		5000);
		configMap.put("HTTP_CONFIG_ADDRESS_DETAIL", apiInfo.get("INTERNAL_URL"));
		configMap.put("HTTP_CONFIG_CONTENT_TYPE",	"application/x-www-form-urlencoded;charset=utf-8");
		
		configMap = HttpUtil.getGetHttpClient(configMap, params);

		return configMap;
	}

	
	
	@SuppressWarnings("unchecked")
	private void checkErrorCase(ParamMap paramMap) throws Exception{
		
		
		int target2Size = 0;
		
		HashMap<String, String> cancelMap = null;
		
		List<Object> cancelWithoutOrderList = pa11stOrderService.selectCancelWithoutOrderList(paramMap);
		target2Size = cancelWithoutOrderList.size();
	
		//특수 케이스, 11번가에서 기취소시 주문 xml을 보내줘서 pa11stOrderList에 10번인 데이터 존재, but Tpaorderm에 데이터가 안쓰이는 경우
		//프로세스 적으로는 문제없음..
		if(target2Size > 0) {
		
			for(int i = 0 ; i < target2Size; i++){
				
				
				cancelMap = (HashMap<String, String>) cancelWithoutOrderList.get(i);
				Paorderm paorderm = new Paorderm();
				
				paorderm.setPaOrderNo(cancelMap.get("PA_ORDER_NO"));
				paorderm.setPaOrderSeq(cancelMap.get("PA_ORDER_SEQ"));
				paorderm.setPaShipNo(cancelMap.get("PA_SHIP_NO"));
				paorderm.setPaClaimNo(cancelMap.get("PA_CLAIM_NO")) ;
				paorderm.setPreCancelReason("주문생성 이전 취소건");
				
				pa11stOrderService.updatePreCancelReason(paorderm);
			}
			
		}else {
			//error Log 수집 - 특수케이스
			//targetSize가 없을 때 모니터링
			throw processException("주문취소", new String[] { "target2Size Error " });

		}
		
	}
	
	@Async
	public void asyncGoodsModify(HttpServletRequest request, ParamMap asyncMap, HashMap<String, String> apiInfo, Pa11stGoodsVO pa11stGoods, 
			List<PaGoodsdtMapping> pa11stGoodsdt, List<PaPromoTarget> paPromoTargetList, StringBuilder reqXml, String prgId) throws Exception{
		
		String request_type = "PUT";
		double timeS = 0;
		double timeE = 0;
		OutputStreamWriter out = null;
		HttpURLConnection conn = null;
		int respCode = 0;
		String respMsg = null;
		String responseTime = null;
		Document doc = null;
		NodeList descNodes = null;
		PaGoodsTransLog paGoodsTransLog = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = systemService.getSysdatetimeToString();
		
		try {
			
			if(pa11stGoods.getPaCode().equals(Constants.PA_11ST_BROAD_CODE)){
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_BROAD,request_type,"/"+pa11stGoods.getProductNo());
			} else {
				conn = ComUtil.pa11stConnectionSetting(apiInfo,Constants.PA_11ST_ONLINE,request_type,"/"+pa11stGoods.getProductNo());
			}
			
			//log.info("05.11번가 상품수정 API 호출");
			timeS = System.nanoTime();
			out = new OutputStreamWriter(conn.getOutputStream());
			out.write(String.valueOf(reqXml));
			out.flush();

			respCode = conn.getResponseCode();
			respMsg  = conn.getResponseMessage();
			
			timeE = System.nanoTime();
			responseTime = Double.toString((timeE-timeS)/1000000000);
			asyncMap.put("responseTime", responseTime);
			asyncMap.put("result", respMsg);
			
			insertPaRequestMap(asyncMap);
			
			if(respCode == 200){
				// RESPONSE XML 			
				doc = ComUtil.parseXML(conn.getInputStream());
				descNodes = doc.getElementsByTagName("ClientMessage");
				conn.disconnect();

				for(int j=0; j<descNodes.getLength();j++){
			        for(Node node = descNodes.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){ 
			        	asyncMap.put(node.getNodeName().trim(), node.getTextContent().trim());
			        }
			    }
				
				//전송관리 테이블 저장
				paGoodsTransLog = new PaGoodsTransLog();
				paGoodsTransLog.setGoodsCode(pa11stGoods.getGoodsCode());
				paGoodsTransLog.setPaCode(asyncMap.getString("paCode").equals("")==true?pa11stGoods.getPaCode():asyncMap.getString("paCode"));
				//아래로 변경, 실패시 로그테이블에 이전 코드로 쌓임, NULL체크는 상단에서 해줌
				paGoodsTransLog.setItemNo(pa11stGoods.getProductNo());
				paGoodsTransLog.setRtnCode(asyncMap.getString("resultCode"));
				paGoodsTransLog.setRtnMsg(asyncMap.getString("message"));
				paGoodsTransLog.setSuccessYn(asyncMap.getString("resultCode").equals("200")==true?"1":"0");
				paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paGoodsTransLog.setProcId("PA11");
				pa11stGoodsService.insertPa11stGoodsTransLogTx(paGoodsTransLog);
				
				if(asyncMap.getString("resultCode").equals("200")){
					pa11stGoods.setProductNo(asyncMap.getString("productNo"));
					pa11stGoods.setInsertId("PA11");
					pa11stGoods.setModifyId("PA11");
					pa11stGoods.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					pa11stGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					
					//log.info("05.제휴사 상품정보 저장");
					rtnMsg = pa11stGoodsService.savePa11stGoodsTx(pa11stGoods, pa11stGoodsdt, paPromoTargetList, prgId);
					
					if(!rtnMsg.equals("000000")){
						asyncMap.put("code","500");
						asyncMap.put("message",pa11stGoods.getGoodsCode() + rtnMsg);
					} else {
						asyncMap.put("code","200");
						asyncMap.put("message","OK");
					}
				} else {
					asyncMap.put("code",asyncMap.getString("resultCode"));
					asyncMap.put("message", pa11stGoods.getGoodsCode() + asyncMap.getString("message"));
					savePaGoodsModifyFail(asyncMap);
				}
			} else {
				asyncMap.put("code",respCode);
				asyncMap.put("message",respMsg);
			}
			
		} catch (Exception e) {
			asyncMap.put("code", "500");
			asyncMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		} finally{
			if (conn != null) conn.disconnect();
			
			if(!"200".equals(asyncMap.get("code").toString())) {
				systemService.insertApiTrackingTx(request, asyncMap);			
			}
		}
	}
	
	public void insertPaRequestMap(ParamMap param) throws Exception{
		PaRequestMap paRequestMap = new PaRequestMap();
		paRequestMap.setPaCode(param.getString("paCode"));
		paRequestMap.setReqApiCode(param.getString("apiCode"));
		paRequestMap.setReqUrl(param.getString("url"));
		paRequestMap.setReqHeader(param.getString("header")+"");
		paRequestMap.setRequestMap(param.getString("body"));
		paRequestMap.setResponseMap(param.getString("result"));
		paRequestMap.setRemark(param.getString("responseTime"));
		systemService.insertPaRequestMapTx(paRequestMap);
	}
	
	public void savePaGoodsModifyFail(ParamMap paramMap) throws Exception{
		Pa11stGoodsVO pa11stGoods = new Pa11stGoodsVO();
		pa11stGoods.setPaGroupCode("01");
		pa11stGoods.setGoodsCode(paramMap.getString("goodsCode"));
		pa11stGoods.setPaCode(paramMap.getString("paCode"));
		pa11stGoods.setPaSaleGb("30");
		pa11stGoods.setPaStatus("90");
		pa11stGoods.setReturnNote(paramMap.getString("message"));
		pa11stGoods.setProductNo(paramMap.getString("productNo"));
		pa11stGoodsService.savePaGoodsModifyFailTx(pa11stGoods);
	}
	
	@Async
	public void spPagoodsSync11st(HttpServletRequest request, String goodsCode, String userId) throws Exception{
		ParamMap paramMap = new ParamMap();
		ParamMap stopSaleParam = new ParamMap();
		ParamMap stopShipParam = new ParamMap();
		ParamMap cnShipCostParam = new ParamMap();
		ParamMap cnCostTransParam = new ParamMap();
		List<PaGoodsImage> curImageInfo = null;
		List<PaGoodsPriceVO> curPriceInfo = null;
		List<PaEntpSlip> curEntpSlipInfo = null;
		HashMap<String, String> minMarginPrice = new HashMap<String, String>();
		HashMap<String, String> applyCnCostSeq = new HashMap<String, String>();
		List<PaCustShipCostVO> curShipCostInfo = null;
		List<HashMap<String, String>> curShipStopSale = null;
		List<HashMap<String, String>> curSaleStop = null;
		List<HashMap<String, String>> curEventMargin = null;
		List<HashMap<String, String>> curCnShipCostInfo = null;
		List<HashMap<String, String>> curCnShipCostDtInfo = null;
		List<HashMap<String, String>> curCnShipCostTransSingle = null;
		List<HashMap<String, String>> curCnShipCostTransMulti = null;
		
		String resultMsg = "";
		String dateTime = systemService.getSysdatetimeToString();
		StringBuffer sb = null;
		int conditionDay = 2;		
		
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paGroupCode", "01");
		paramMap.put("dateTime", dateTime);
		paramMap.put("conditionDay", conditionDay);		
		paramMap.put("comparePaCode", "11");
		paramMap.put("feeCode", "O531");
		paramMap.put("minMarginCode", "10");
		paramMap.put("minPriceCode", "20");
		
		paramMap.put("siteGb", userId);
		paramMap.put("apiCode", "IF_PACOMMON_00_002");
		
		log.info("Step1. 11ST 상품이미지 동기화 START");
		
		curImageInfo = paCommonService.selectCurImageInfoList(paramMap);
		if(curImageInfo.size() > 0) {
			sb = new StringBuffer();
			for(PaGoodsImage curImageInfoTarget : curImageInfo) {
				//이미지 없는 케이스가 많아 for문안에 try catch 처리
				try {
					curImageInfoTarget.setModifyId("PA11ST");
					curImageInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					curImageInfoTarget.setRemark(dateTime);
					resultMsg = paCommonService.saveCurImageInfoTx(curImageInfoTarget);
		
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step1 상품이미지 동기화 11ST Fail > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
						sb.append(curImageInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step1 상품이미지 동기화 11ST Sucess > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
				} catch(Exception e) {
					log.info("Step1. 11ST 상품이미지 동기화 : " + e.getMessage());
					paramMap.put("code", "500");
					paramMap.put("message", "Step1. 11ST 상품이미지 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}
		}
		log.info("Step1. 11ST 상품이미지 동기화 END");
		
		log.info("Step2. 11ST 상품가격 동기화 START");
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
					 || (ComUtil.objToDouble(curPriceInfoTarget.getBeSalePrice()) * 0.2 >= ComUtil.objToDouble(curPriceInfoTarget.getSalePrice()))
					 || ("61".equals(curPriceInfoTarget.getSourcingMedia()) && "1".equals(curPriceInfoTarget.getMobileEtvYn()) && ComUtil.objToDouble(curPriceInfoTarget.getMarginRate()) < 21) ) {
						
						stopSaleParam.put("paGroupCode", "01");
						stopSaleParam.put("paCode", curPriceInfoTarget.getPaCode());
						stopSaleParam.put("goodsCode", curPriceInfoTarget.getGoodsCode());
						stopSaleParam.put("dateTime", dateTime);
						stopSaleParam.put("userId", "PA11ST");
						stopSaleParam.put("paGoodsCode", curPriceInfoTarget.getPaGoodsCode());
						stopSaleParam.put("note", "마진10이하 혹은 판매가100원 미만/모바일eTV 마진");
						stopSaleParam.put("priceStopSale", "Y");
						
						//연동제외처리
						resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
						
					} else {
						curPriceInfoTarget.setModifyId("PA11ST");
						curPriceInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						curPriceInfoTarget.setDateTime(dateTime);
						resultMsg = paCommonService.saveCurPriceInfoTx(curPriceInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step2 상품가격 동기화 11ST Fail > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
						sb.append(curPriceInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step2 상품가격 동기화 11ST Sucess > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
				}
			} catch(Exception e) {
				log.info("Step2. 11ST 상품가격 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step2. 11ST 상품가격 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step2. 11ST 상품가격 동기화 END");	
		
		log.info("Step3. 11ST 고객부담배송비 동기화 START");
		curShipCostInfo = paCommonService.selectCurShipCostInfoList(paramMap);
		if(curShipCostInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaCustShipCostVO curShipCostInfoTarget : curShipCostInfo) {
					if(curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("QN")) { //배송비정책이 QN인 경우 연동 제외처리
						
						stopShipParam.put("paCode", curShipCostInfoTarget.getPaCode());
						stopShipParam.put("entpCode", curShipCostInfoTarget.getEntpCode());
						stopShipParam.put("shipCostCode", curShipCostInfoTarget.getShipCostCode());
						
						curShipStopSale = paCommonService.selectCurShipStopSaleList(stopShipParam);
						for(HashMap<String, String> curShipStopSaleTarget : curShipStopSale) {
							
							stopSaleParam.put("paGroupCode", "01");
							stopSaleParam.put("paCode", curShipStopSaleTarget.get("PA_CODE").toString());
							stopSaleParam.put("goodsCode", curShipStopSaleTarget.get("GOODS_CODE").toString());
							stopSaleParam.put("dateTime", dateTime);
							stopSaleParam.put("userId", "PA11ST");
							stopSaleParam.put("paGoodsCode", curShipStopSaleTarget.get("PA_GOODS_CODE").toString());
							stopSaleParam.put("note", "배송비유료변경");
							stopSaleParam.put("priceStopSale", "N");
							
							//연동제외처리
							resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
						}
					} else {
						curShipCostInfoTarget.setModifyId("PA11ST");
						curShipCostInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						resultMsg = paCommonService.saveCurShipCostInfoTx(curShipCostInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step3 고객부담배송비 11ST Fail");
						sb.append(curShipCostInfoTarget.getEntpCode() + "|" + curShipCostInfoTarget.getShipCostCode() + ", ");
						continue;
					}
					log.info("Step3 고객부담배송비 11ST Sucess");
				}
			} catch(Exception e) {
				log.info("Step3. 11ST 고객부담배송비 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step3. 11ST 고객부담배송비 동기화 : " + e.getMessage() + "INFO : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step3. 11ST 고객부담배송비 동기화 END");
		
		log.info("Step4. 11ST 출고/회수지 동기화 START");
		curEntpSlipInfo = paCommonService.selectCurEntpSlipInfoList(paramMap);
		if(curEntpSlipInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaEntpSlip curEntpSlipInfoTarget : curEntpSlipInfo) {
					
					curEntpSlipInfoTarget.setModifyId("PA11ST");
					curEntpSlipInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					resultMsg = paCommonService.saveCurEntpSlipInfoTx(curEntpSlipInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step4 출고/회수지 동기화 11ST Fail");
						sb.append(curEntpSlipInfoTarget.getEntpCode() + ", ");
						continue;
					}
					log.info("Step4 출고/회수지 동기화 11ST Sucess");
				}
			} catch(Exception e) {
				log.info("Step4. 11ST 출고/회수지 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step4. 11ST 출고/회수지 동기화 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step4. 11ST 출고/회수지 회수지 동기화 END");
		
		log.info("Step5. 11ST 상품판매단계 동기화 START");
		curSaleStop = paCommonService.selectCurSaleStopList(paramMap);
		
		if(curSaleStop.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curSaleStopTarget : curSaleStop) {
					
					stopSaleParam.put("paGroupCode", "01");
					stopSaleParam.put("paCode", curSaleStopTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curSaleStopTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PA11ST");
					stopSaleParam.put("paGoodsCode", curSaleStopTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "SK스토아상품판매중단");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step5 상품판매단계 동기화 11ST Fail > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
						sb.append(curSaleStopTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step5 상품판매단계 동기화 11ST Sucess > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step5. 11ST 상품판매단계 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step5. 11ST 상품판매단계 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step5. 11ST 상품판매단계 동기화 END");
		
		log.info("Step6. 11ST 행사 종료 상품 마진 체크 START");
		curEventMargin = paCommonService.selectCurEventMarginList(paramMap);
		if(curEventMargin.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curEventMarginTarget : curEventMargin) {
					paramMap.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					//minMarginPrice = paCommonService.selectMinMarginPrice(paramMap);
					//selectCurEventMarginList 안에서 체크
					//if(ComUtil.objToDouble(curEventMarginTarget.get("MARGIN_RATE")) < ComUtil.objToDouble(minMarginPrice.get("MIN_MARGIN"))) {
					stopSaleParam.put("paGroupCode", "01");
					stopSaleParam.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curEventMarginTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PA11ST");
					stopSaleParam.put("paGoodsCode", curEventMarginTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "행사종료");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step6 행사 종료 상품 마진 체크 11ST Fail > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
						sb.append(curEventMarginTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step6 행사 종료 상품 마진 체크 11ST Sucess > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
					//}					
				}
			} catch(Exception e) {
				log.info("Step6. 11ST 행사 종료 상품 마진 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step6. 11ST 행사 종료 상품 마진 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step6. 11ST 행사 종료 상품 마진 체크 END");
		
		//Step6-1. 출고지 조건부 배송비 동기화
		log.info("Step7-1. 11ST 출고지 조건부 배송비 동기화 START");
		curCnShipCostInfo = paCommonService.selectCurCnShipCostInfoList(paramMap);
		if(curCnShipCostInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curCnShipCostInfoTarget : curCnShipCostInfo) {
					
					cnShipCostParam.put("entpCode", curCnShipCostInfoTarget.get("ENTP_CODE").toString());
					cnShipCostParam.put("paCode", curCnShipCostInfoTarget.get("PA_CODE").toString());
					
					curCnShipCostDtInfo = paCommonService.selectCurCnShipCostDtInfoList(cnShipCostParam);
					
					for(HashMap<String, String> curCnShipCostDtInfoTarget : curCnShipCostDtInfo) {
						
						cnShipCostParam.put("entpCode", curCnShipCostDtInfoTarget.get("ENTP_CODE").toString());
						cnShipCostParam.put("entpManSeq", curCnShipCostDtInfoTarget.get("SHIP_MAN_SEQ").toString());
						cnShipCostParam.put("shipCostCode", curCnShipCostDtInfoTarget.get("SHIP_COST_CODE").toString());
						cnShipCostParam.put("shipCostBaseAmt", ComUtil.objToDouble(curCnShipCostDtInfoTarget.get("SHIP_COST_BASE_AMT")));
						cnShipCostParam.put("ordCostAmt", ComUtil.objToDouble(curCnShipCostDtInfoTarget.get("ORD_COST_AMT")));
						cnShipCostParam.put("dateTime", dateTime);
						cnShipCostParam.put("userId", "PA11ST");
						
						resultMsg = paCommonService.savePaCustCnShipCostTx(cnShipCostParam);
						if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
							log.info("Step7-1. 11ST 출고지 조건부 배송비 동기화 Fail > ENTP_CODE : " + cnShipCostParam.get("ENTP_CODE").toString());
							sb.append(cnShipCostParam.get("ENTP_CODE").toString() + ", ");
							continue;
						}
					}
					
					resultMsg = paCommonService.saveCnCostYnTx(cnShipCostParam);
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step7-1. 11ST 출고지 조건부 배송비 동기화 Fail > ENTP_CODE : " + cnShipCostParam.get("ENTP_CODE").toString());
						sb.append(cnShipCostParam.get("ENTP_CODE").toString() + ", ");
						continue;
					}
				}
			} catch(Exception e) {
				log.info("Step7-1. 11ST 출고지 조건부 배송비 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step7-1. 11ST 출고지 조건부 배송비 동기화 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step7-1. 11ST 출고지 조건부 배송비 동기화 END");
		
		log.info("Step7-2. 11ST 출고지 조건부 배송비 동기화 START");
		curCnShipCostTransSingle = paCommonService.selectCurCnShipCostTransSingle(paramMap);
		if(curCnShipCostTransSingle.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curCnShipCostTransSingleTarget : curCnShipCostTransSingle) {
					
					paramMap.put("paCode", curCnShipCostTransSingleTarget.get("PA_CODE").toString());
					paramMap.put("entpCode", curCnShipCostTransSingleTarget.get("ENTP_CODE").toString());
					paramMap.put("entpManSeq", curCnShipCostTransSingleTarget.get("ENTP_MAN_SEQ").toString());
					
					applyCnCostSeq = paCommonService.selectApplyCnCostSeq(paramMap);
					
					if(!"X".equals(applyCnCostSeq.get("APPLY_CN_COST_SEQ").toString())) {
						
						cnCostTransParam.put("applyCnCostSeq", applyCnCostSeq.get("APPLY_CN_COST_SEQ").toString());
						cnCostTransParam.put("entpCode", curCnShipCostTransSingleTarget.get("ENTP_CODE").toString());
						cnCostTransParam.put("entpManSeq", curCnShipCostTransSingleTarget.get("ENTP_MAN_SEQ").toString());
						cnCostTransParam.put("paCode", curCnShipCostTransSingleTarget.get("PA_CODE").toString());
						
						resultMsg = paCommonService.saveCnCostTransTx(cnCostTransParam);
						if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
							log.info("Step7-2. 11ST 출고지 조건부 배송비 동기화 Fail > ENTP_CODE : " + cnCostTransParam.get("ENTP_CODE").toString());
							sb.append(cnCostTransParam.get("ENTP_CODE").toString() + ", ");
							continue;
						}
					}
				}
			} catch(Exception e) {
				log.info("Step7-2. 11ST 출고지 조건부 배송비 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step7-2. 11ST 출고지 조건부 배송비 동기화 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step7-2. 11ST 출고지 조건부 배송비 동기화 END");
		
		log.info("Step7-3. 11ST 출고지 조건부 배송비 동기화 START");
		curCnShipCostTransMulti = paCommonService.selectCurCnShipCostTransMulti(paramMap);
		if(curCnShipCostTransMulti.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curCnShipCostTransMultiTarget : curCnShipCostTransMulti) {
					
					paramMap.put("paCode", curCnShipCostTransMultiTarget.get("PA_CODE").toString());
					paramMap.put("entpCode", curCnShipCostTransMultiTarget.get("ENTP_CODE").toString());
					paramMap.put("entpManSeq", curCnShipCostTransMultiTarget.get("ENTP_MAN_SEQ").toString());
					
					applyCnCostSeq = paCommonService.selectMaxOrdCost(paramMap);
					
					if(!"X".equals(applyCnCostSeq.get("APPLY_SHIP_COST").toString())) {
						
						cnCostTransParam.put("applyShipCost", applyCnCostSeq.get("APPLY_SHIP_COST").toString());
						cnCostTransParam.put("entpCode", curCnShipCostTransMultiTarget.get("ENTP_CODE").toString());
						cnCostTransParam.put("entpManSeq", curCnShipCostTransMultiTarget.get("ENTP_MAN_SEQ").toString());
						cnCostTransParam.put("paCode", curCnShipCostTransMultiTarget.get("PA_CODE").toString());
						
						resultMsg = paCommonService.saveCnCostTrans2Tx(cnCostTransParam);
						
						if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
							log.info("Step7-3. 11ST 출고지 조건부 배송비 동기화 Fail > ENTP_CODE : " + cnCostTransParam.get("ENTP_CODE").toString());
							sb.append(cnCostTransParam.get("ENTP_CODE").toString() + ", ");
							continue;
						}
					}
				}
			} catch(Exception e) {
				log.info("Step7-2. 11ST 출고지 조건부 배송비 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step7-2. 11ST 출고지 조건부 배송비 동기화 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step7-3. 11ST 출고지 조건부 배송비 동기화 END");
		
		
		paCommonService.checkMassModifyGoods("01");
		
	}
	
	private String checkDealOrderPrice(HashMap<String, String> orderMap) throws Exception {
		String rtnMsg = "";
		double outPromoPrice 	= Double.parseDouble( String.valueOf(orderMap.get("OUT_PROMO_PRICE")));
		double sdPromoPrice  	= Double.parseDouble( String.valueOf(orderMap.get("SD_PROMO_PRICE")));
		double sellerDcAmt   	= Double.parseDouble( String.valueOf(orderMap.get("SELLER_DC_AMT"))) / Integer.parseInt(String.valueOf( orderMap.get("ORDER_QTY") ));
		double arsDcAmt		 	= Double.parseDouble(String.valueOf(orderMap.get("DC_AMT")));
		double lumpSumDcAmt	 	= Double.parseDouble(String.valueOf(orderMap.get("LUMP_SUM_DC_AMT"))) / Integer.parseInt(String.valueOf( orderMap.get("ORDER_QTY") ));				
		double p11stDiscountAmt = Double.parseDouble(String.valueOf(orderMap.get("PA_DC_AMT")));
		double salePrice		= Double.parseDouble( String.valueOf(orderMap.get("SALE_PRICE")));
		double rsaleAmt			= Double.parseDouble( String.valueOf(orderMap.get("RSALE_AMT")))  / Integer.parseInt(String.valueOf( orderMap.get("ORDER_QTY") ));		
		double masterDcAmt		= Double.parseDouble( String.valueOf(orderMap.get("SELLER_DISCOUNT_PRICE"))) / Integer.parseInt(String.valueOf( orderMap.get("ORDER_QTY") ));
		double masterSalePrice  = Double.parseDouble( String.valueOf(orderMap.get("SEL_PRC"))); 
		double optionAmt		= Double.parseDouble( String.valueOf(orderMap.get("ORD_OPT_WON_STL"))) / Integer.parseInt(String.valueOf( orderMap.get("ORDER_QTY") ));
		double sumDcAmt 		= outPromoPrice + sdPromoPrice + arsDcAmt + lumpSumDcAmt;
		
		//마스터 상품가격 +  옵션가 + 해당 상품의  할인가 - 마스터 할인가 == 상품 판매가
		if(masterSalePrice + ( sellerDcAmt + optionAmt - masterDcAmt) != salePrice) {
			rtnMsg = "딜 상품의 가격 연동이 잘 못되었습니다.(연동시차오류)";
			return rtnMsg;
		}
		//판매자 할인가 검증
		if(sumDcAmt != sellerDcAmt) {
			rtnMsg = "해당 주문의 판매자할인이 잘못되었습니다..";
			return rtnMsg;
		}
		//제휴사 할인(TORDERPROMO.DO_TYPE ='92') 검증
		if(salePrice - rsaleAmt - sumDcAmt   != p11stDiscountAmt) {
			rtnMsg = "해당 주문의 제휴사 할인이 잘못 계산 될것입니다.";
			return rtnMsg;
		}			
		return rtnMsg;
	}
	
	private void updatePaorderM(OrderInputVO orderInputVO, String message) {
		try {
			ParamMap paramMap = new ParamMap();
			paramMap.put("mappingSeq"	, orderInputVO.getMappingSeq());
			paramMap.put("resultCode"	, "999999");
			paramMap.put("resultMessage", message);
			paramMap.put("createYn"		, "0");
			paorderService.updatePaOrdermTx(paramMap);	
		}catch (Exception e) {
			log.error(e.toString());
		}
	}
	
}
