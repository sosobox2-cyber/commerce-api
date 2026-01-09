package com.cware.api.panaver.controller;

import io.swagger.annotations.ApiOperation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.common.util.StringUtil;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.CancelInputVO;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.OrderInputVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaCustShipCostVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.panaver.cancel.service.PaNaverCancelService;
import com.cware.netshopping.panaver.claim.service.PaNaverClaimService;
import com.cware.netshopping.panaver.delivery.service.PaNaverDeliveryService;
import com.cware.netshopping.panaver.exchange.service.PaNaverExchangeService;
import com.cware.netshopping.panaver.infocommon.service.PaNaverInfoCommonService;

@Controller("com.cware.api.panaver.PaNaverAsycController")
@RequestMapping(value="/panaver/async")
public class PaNaverAsyncController extends AbstractController {

	@Autowired
    private SystemService systemService;
	
	@Autowired
	private PaOrderService paOrderService;
	
	@Autowired
	private PaNaverInfoCommonService paNaverInfoCommonService;
	
	@Autowired
	private PaNaverDeliveryService paNaverDeliveryService;
	
	@Autowired
	private PaNaverCancelService paNaverCancelService;
	
	@Autowired
	private PaNaverClaimService paNaverClaimService;
	
	@Autowired
	private PaNaverExchangeService paNaverExchangeService;
	
	@Autowired
	private PaClaimService paClaimService;
	
	@Autowired
	private PaNaverCancelController paNaverCancelController;
	
	@Autowired
	private PaNaverExchangeController paNaverExchangeController;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	/**
	 * 네이버 주문접수 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */	
	@ApiOperation(value = "네이버 주문접수 데이터 생성", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/createOrder", method = RequestMethod.GET)
	@Async
	public void orderCreateAsync(HttpServletRequest request, String orderID) throws Exception {

		ParamMap paramMap = null;
		int targetSize = 0;
		int executedRtn = 0;
		HashMap<String, Object>[] resultMap = null;
		HashMap<String, String> refusalMap = null;
		OrderInputVO[] orderInputs;
		try {
			
			List<HashMap<String, Object>> createOrderTargetList = paNaverDeliveryService.selectOrderInputTargetDtList(orderID);
			targetSize = createOrderTargetList.size();
			orderInputs = new OrderInputVO[targetSize];
			
			if(targetSize > 0) {
				HashMap<String, Object> target;
				for(int i=0; i<targetSize; i++) {
					target = createOrderTargetList.get(i);
					paramMap = new ParamMap();
					paramMap.put("orderID", ComUtil.objToStr(target.get("ORDER_ID")));
					paramMap.put("productOrderID", ComUtil.objToStr(target.get("PRODUCT_ORDER_ID")));
					orderInputs[i] = new OrderInputVO();
					orderInputs[i].setPaOrderCode(ComUtil.objToStr(target.get("ORDER_ID")));
					orderInputs[i].setPaCode(ComUtil.objToStr(target.get("PA_CODE")));
					orderInputs[i].setMappingSeq(ComUtil.objToStr(target.get("MAPPING_SEQ")));
					orderInputs[i].setMediaCode(ComUtil.objToStr(target.get("MEDIA_CODE")));
					orderInputs[i].setOrderDate(ComUtil.objToStr(target.get("ORDER_DATE")));
					orderInputs[i].setGoodsCode(ComUtil.objToStr(target.get("GOODS_CODE")));
					orderInputs[i].setGoodsdtCode(ComUtil.objToStr(target.get("GOODSDT_CODE")));
					orderInputs[i].setOrderQty(ComUtil.objToInt(target.get("ORDER_QTY")));
					orderInputs[i].setApplyDate(ComUtil.objToStr(target.get("APPLY_DATE")));
					orderInputs[i].setRsaleAmt(ComUtil.objToDouble(target.get("RSALE_AMT")));
					orderInputs[i].setSupplyPrice(ComUtil.objToDouble(target.get("SUPPLY_PRICE")));
					orderInputs[i].setSellerDcAmt(ComUtil.objToLong(target.get("SELLER_DC_AMT")));
					orderInputs[i].setLumpSumDcAmt(ComUtil.objToLong(target.get("LUMP_SUM_DC_AMT")));			
					orderInputs[i].setLumpSumEntpDcAmt(ComUtil.objToLong(target.get("LUMP_SUM_ENTP_DC_AMT")));
					orderInputs[i].setLumpSumOwnDcAmt(ComUtil.objToLong(target.get("LUMP_SUM_OWN_DC_AMT")));
					orderInputs[i].setCustName(ComUtil.objToStr(target.get("CUST_NAME")));
					orderInputs[i].setCustChar("99");
					orderInputs[i].setCustTel1(ComUtil.objToStr(target.get("CUST_TEL1")));
					orderInputs[i].setCustTel2(ComUtil.objToStr(target.get("CUST_TEL2")));
					orderInputs[i].setReceiverName(ComUtil.objToStr(target.get("RECEIVER_NAME")));
					orderInputs[i].setReceiverTel(ComUtil.objToStr(target.get("RECEIVER_TEL")));
					orderInputs[i].setReceiverHp(ComUtil.objToStr(target.get("RECEIVER_HP")));
					orderInputs[i].setReceiverAddr(ComUtil.objToStr(target.get("RECEIVER_ADDR")));
					orderInputs[i].setMsg(ComUtil.objToStr(target.get("MSG")));
					orderInputs[i].setPaGoodsCode(ComUtil.objToStr(target.get("PRODUCT_ID")));
					orderInputs[i].setAddrGbn(ComUtil.objToStr(target.get("TYPE_ADD").toString()));
					orderInputs[i].setStdAddr(ComUtil.objToStr(target.get("RCVR_BASE_ADDR").toString()));
					orderInputs[i].setStdAddrDT(ComUtil.objToStr(target.get("RCVR_DTLS_ADDR").toString()));
					orderInputs[i].setPostNo(ComUtil.objToStr(target.get("POST_NO")));
					orderInputs[i].setPostNoSeq("001");
					if(target.get("DELIVERY_POLICY_TYPE").toString().equals("유료") || target.get("DELIVERY_POLICY_TYPE").toString().equals("조건부무료")
							|| target.get("DELIVERY_POLICY_TYPE").toString().equals("수량별") || target.get("DELIVERY_POLICY_TYPE").toString().equals("무료")) {
						if(i==targetSize-1) orderInputs[i].setShpFeeCost(ComUtil.objToLong(target.get("SHP_FEE_COST")));
						else orderInputs[i].setShpFeeCost(0);
					}		
					orderInputs[i].setReceiveMethod("61");
					orderInputs[i].setProcUser("PANAVER");
					orderInputs[i].setPriceSeq(target.get("PRICE_SEQ").toString());
					orderInputs[i].setDoFlag(target.get("DO_FLAG").toString());
					if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost())
							|| ("127.0.0.1").equals(request.getRemoteHost())) {
						orderInputs[i].setIsLocalYn("Y");
					} else {
						orderInputs[i].setIsLocalYn("N");
					}
					orderInputs[i].setInstantCouponDiscount(ComUtil.objToDouble(target.get("INSTANT_COUPON_DISCOUNT")));
					
					// 네이버 단품코드 중복 확인
					ParamMap goodsDtParam = new ParamMap();
					goodsDtParam.put("paOptionCode", target.get("PA_OPTION_CODE").toString());
					goodsDtParam.put("paCode", target.get("PA_CODE").toString());
					goodsDtParam.put("goodsCode", target.get("GOODS_CODE").toString());
					
					int goodsDtCount = paOrderService.selectOrderGoodsDtDupleCheck(goodsDtParam);
					if(goodsDtCount > 1) {
						goodsDtParam.put("mappingSeq", target.get("MAPPING_SEQ").toString());
						goodsDtParam.put("resultCode", "999999");
						goodsDtParam.put("resultMessage", "동일한 단품코드가 존재합니다.");
						goodsDtParam.put("createYn"		, "0");
						paOrderService.updatePaOrdermTx(goodsDtParam);
						throw processException("pa.fail_order_input", new String[]{ "동일한 단품코드가 존재합니다." });
					}
					
					// 가격 비교
					String paApplyDate = target.get("APPLY_DATE").toString(); // TPAGOODSPRICE 기준 APPLY_DATE
					String stoaApplyDate = target.get("STOA_APPLY_DATE").toString(); // TGOODSPRICE 기준 APPLY_DATE
					String paOrderDate = target.get("ORDER_DATE").toString(); // 네이버 주문일시
					
					if(!paApplyDate.equals(stoaApplyDate)) {
						SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
						Date orderDate = format.parse(paOrderDate.substring(0, 8));
				        Date stoaDate = format.parse(stoaApplyDate.substring(0, 8));
				        
				        // 스토아 최신 가격 적용일자와 제휴 주문일자 차이 계산
				        long diffDays = orderDate.getTime() - stoaDate.getTime();
				        diffDays = diffDays / (24 * 60 * 60 * 1000);
				        
						if(diffDays >= 3) { // 3일 이상 차이나는 경우
							ParamMap orderPriceParam = new ParamMap();
							orderPriceParam.put("mappingSeq", target.get("MAPPING_SEQ").toString());
							orderPriceParam.put("resultCode", "999999");
							orderPriceParam.put("resultMessage", "가격 정보가 잘못되었습니다.");
							orderPriceParam.put("createYn"		, "0");
							paOrderService.updatePaOrdermTx(orderPriceParam);
							throw processException("pa.fail_order_input", new String[]{ "가격 정보가 잘못되었습니다." });
						}
					}
					
					//PROMOTION DATA SETTING 
					double doAmt = ComUtil.objToDouble(target.get("OUT_PROMO_PRICE"));
		            if(doAmt > 0) {
		            	OrderpromoVO orderPaPromo = new OrderpromoVO();
			            
			            orderPaPromo.setPromoNo(ComUtil.objToStr(target.get("PROMO_NO"))); 
			            orderPaPromo.setDoType("30");
			            orderPaPromo.setDoAmt(doAmt); 
			            orderPaPromo.setProcCost(doAmt);
			            orderPaPromo.setOwnProcCost(ComUtil.objToDouble(target.get("OWN_COST")));
			            orderPaPromo.setEntpProcCost(ComUtil.objToDouble(target.get("ENTP_COST")));
			            orderPaPromo.setCouponPromoBdate(DateUtil.toTimestamp(target.get("COUPON_PROMO_BDATE").toString()));
			            orderPaPromo.setCouponPromoEdate(DateUtil.toTimestamp(target.get("COUPON_PROMO_EDATE").toString()));
			            orderPaPromo.setCouponYn("1"); 
			            orderPaPromo.setProcGb("I");
			            
			            orderInputs[i].setOrderPaPromo(orderPaPromo);
		            }
				}
				
				try{
					if(orderInputs[0].getDoFlag().equals("10")){
						resultMap = paOrderService.newSaveOrderTx(orderInputs);
					}
					else {
						resultMap = paOrderService.saveOrderTx(orderInputs);
					}
				}catch (Exception e) {
					for(int j = 0; j<targetSize; j++) {
						paramMap = new ParamMap();
						paramMap.put("mappingSeq", orderInputs[j].getMappingSeq());
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
				
				for(int j=0; j<targetSize; j++) {
					//취소처리 시작
					paramMap = new ParamMap();
					paramMap.setParamMap(resultMap[j]);
					paramMap.replaceCamel();
					
					refusalMap = paNaverDeliveryService.selectOrderMappingInfoByMappingSeq(paramMap.getString("mappingSeq"));
					refusalMap.put("ordCnRsnCd", 	"09");
					if(paramMap.getString("resultCode").equals("100001") ){
						refusalMap.put("ordCnDtlsRsn", 	getMessage("pa.out_of_stock_due_shortage_process"));
						refusalMap.put("paCode", 		orderInputs[j].getPaCode());
						paNaverCancelController.cancelSale(request, ComUtil.objToStr(refusalMap.get("PA_ORDER_NO")), ComUtil.objToStr(refusalMap.get("PA_ORDER_SEQ")), ComUtil.objToStr(refusalMap.get("PA_ORDER_SEQ")), 2, orderInputs[j].getPaCode());
						
					}else if(paramMap.getString("resultCode").equals("100002")){ 
						refusalMap.put("ordCnRsnCd", 	"07");
						refusalMap.put("ordCnDtlsRsn", 	getMessage("pa.differentBuyorSaleprice"));
						refusalMap.put("paCode", 		orderInputs[j].getPaCode());
						paNaverCancelController.cancelSale(request, ComUtil.objToStr(refusalMap.get("PA_ORDER_NO")), ComUtil.objToStr(refusalMap.get("PA_ORDER_SEQ")), ComUtil.objToStr(refusalMap.get("PA_ORDER_SEQ")), 0, orderInputs[j].getPaCode());
					}
					//취소처리 종료
				}//for
			}//if
			
		} catch (Exception e) {
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "pa_order_no : " + orderID + " > " + e.getMessage());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PaNaver orderCreateAsync");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
//				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}
	
	/**
	 * 네이버 주문 취소 데이터 생성
	 * @param cancelMap
	 * @param request
	 * @throws Exception
	 */
	@ApiOperation(value = "네이버 주문 취소 데이터 생성", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/createCancel", method = RequestMethod.GET)
	public void cancelInputAsync(HttpServletRequest request, ParamMap cancelMap) throws Exception {
		ParamMap paramMap = null;
		HashMap<String, Object> hmSheet = null;
		ParamMap preCancelMap = null;
		String cancelCode = null;
		
		try {
			List<HashMap<String, Object>> cancelInputTargetDtList = paNaverCancelService.selectCancelInputTargetDtList(cancelMap);
			if(cancelInputTargetDtList.size() > 0) {
	
				hmSheet = cancelInputTargetDtList.get(0);
			
				if(hmSheet.get("PRE_CANCEL_YN").toString().equals("0")) {
					//= 일반취소건 처리.
					CancelInputVO cancelInputVO = new CancelInputVO();
					cancelInputVO.setMappingSeq(hmSheet.get("MAPPING_SEQ").toString());
					cancelInputVO.setOrderNo(hmSheet.get("ORDER_NO").toString());
					cancelInputVO.setOrderGSeq(hmSheet.get("ORDER_G_SEQ").toString());
					cancelInputVO.setCancelQty(Integer.parseInt(String.valueOf(hmSheet.get("PA_PROC_QTY"))));
					
					cancelCode = hmSheet.get("CANCEL_CODE").toString();
					if(cancelCode == null || "".equals(cancelCode) || cancelCode.length() != 6) {
						cancelCode = "620499";	
					}
							
					cancelInputVO.setCancelCode(cancelCode);
					cancelInputVO.setProcId("PANAVER");
				
					try {
						paOrderService.saveCancelTx(cancelInputVO);
					} catch (Exception e) {
						paramMap = new ParamMap();
						paramMap.put("mappingSeq", cancelInputVO.getMappingSeq());
						paramMap.put("resultCode", "999999");
						paramMap.put("resultMessage", e.getMessage());
						paramMap.put("createYn", "0");
					
						if(paOrderService.updatePaOrdermTx(paramMap) != 1){
							throw processException("msg.cannot_save", new String[]{"TPAORDERM UPDATE - resultCode 999999"});
						}
				
						throw processMessageException(e.getMessage());
					}
				
				} else {
					//= 기취소건 처리(주문생성 이전 취소건)
					preCancelMap = new ParamMap();
					preCancelMap.setParamMap(hmSheet);
					preCancelMap.replaceCamel();
				
					preCancelMap.put("preCancelReason", getMessage("pa.before_order_create_cancel"));
					if(paNaverCancelService.updatePreCancelYnTx(preCancelMap) != 1){
						throw processException("msg.cannot_save", new String[] {"TPAORDERM(pre_cancel_yn) UPDATE"});
					}
				}
			}
		} catch(Exception e) {
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "claimSeq : " + paramMap.get("claimSeq") + " > " + e.toString());
		} finally {
			try {
				if(paramMap != null && !paramMap.getString("message").equals("")) {
					paramMap.put("apiCode", "PaNaver cancelInputAsync");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code", "500");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			} catch(Exception ee) {
				log.error("ApiTracking Insert Error : " + ee.toString());
			}
		}
	}
	
	@ApiOperation(value = "네이버 주문 반품 데이터 생성", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/createReturn", method = RequestMethod.GET)
	public void returnInputAsync(HttpServletRequest request, String orderID, String productOrderID, String claimID) throws Exception {
		ParamMap paramMap = new ParamMap();
		List<HashMap<String, String>> returnClaimTargetList = null;
		String paOrderGb = null;
		
		try {
			paramMap = new ParamMap();
			paramMap.put("orderID", orderID);
			paramMap.put("productOrderID", productOrderID);
			paramMap.put("claimID", claimID);
			returnClaimTargetList = paNaverClaimService.selectReturnClaimTargetList(paramMap);
			
			if(returnClaimTargetList.size() == 1) {
				paOrderGb = returnClaimTargetList.get(0).get("PA_ORDER_GB").toString();
				
				if(returnClaimTargetList.get(0).get("PA_ORDER_GB").equals("30")) returnClaimTargetList = paNaverClaimService.selectReturnClaimTargetDt30List(paramMap);
				else returnClaimTargetList = paNaverClaimService.selectReturnClaimTargetDt20List(paramMap);
				
				if(returnClaimTargetList.size() > 0) {
					OrderClaimVO orderClaimVO = new OrderClaimVO();
					orderClaimVO.setMappingSeq(returnClaimTargetList.get(0).get("MAPPING_SEQ").toString());
					orderClaimVO.setOrderNo(returnClaimTargetList.get(0).get("ORDER_NO").toString());
					orderClaimVO.setOrderGSeq(returnClaimTargetList.get(0).get("ORDER_G_SEQ").toString());
					orderClaimVO.setClaimQty(Integer.parseInt(String.valueOf(returnClaimTargetList.get(0).get("PA_PROC_QTY"))));
					orderClaimVO.setClaimCode("999");
					orderClaimVO.setClaimDesc(returnClaimTargetList.get(0).get("CLAIM_DESC").toString());
					orderClaimVO.setReturnName(returnClaimTargetList.get(0).get("RETURN_NAME").toString());
					orderClaimVO.setReturnTel(returnClaimTargetList.get(0).get("RETURN_TEL").toString().replace("-", ""));
					orderClaimVO.setReturnHp(returnClaimTargetList.get(0).get("RETURN_HP").toString().replace("-", ""));
					orderClaimVO.setReturnAddr(returnClaimTargetList.get(0).get("RETURN_ADDR").toString());
					orderClaimVO.setCustDelyYn(returnClaimTargetList.get(0).get("CUST_DELY_YN").toString()); //= 고객 직접발송일 경우 : 1, 아닐경우 : 0
					orderClaimVO.setInsertId("PANAVER");
					
					if(returnClaimTargetList.get(0).get("CUST_DELY_YN").toString().equals("1")){
						orderClaimVO.setReturnDelyGb(returnClaimTargetList.get(0).get("RETURN_DELY_GB").toString()); //= 직접발송일 경우 11번가 고객입력 배송사코드.
						orderClaimVO.setReturnSlipNo(StringUtil.null2string(returnClaimTargetList.get(0).get("RETURN_SLIP_NO").toString(), getMessage("pa.cust_dely_slip_no_empty"))); //= 직접발송일 경우 11번가 고객입력 운송장번호.
					}
					else {
						orderClaimVO.setReturnDelyGb("");
						orderClaimVO.setReturnSlipNo("");
					}
					
					orderClaimVO.setOutBefClaimGb(returnClaimTargetList.get(0).get("OUT_BEF_CLAIM_GB").toString());
					
					if(paramMap.getString("PA_ORDER_GB").equals("30")){
						
						String rcvrMailNoSeq;
						if(returnClaimTargetList.get(0).get("RCVR_MAIL_NO_SEQ") == null || "".equals(returnClaimTargetList.get(0).get("RCVR_MAIL_NO_SEQ"))){
							rcvrMailNoSeq = "001";
						}
						else {
							rcvrMailNoSeq = returnClaimTargetList.get(0).get("RCVR_MAIL_NO_SEQ").toString();
						}
						
						orderClaimVO.setRcvrMailNo		(returnClaimTargetList.get(0).get("RCVR_MAIL_NO").toString());
						orderClaimVO.setRcvrMailNoSeq   (rcvrMailNoSeq);
						orderClaimVO.setRcvrBaseAddr	(returnClaimTargetList.get(0).get("RCVR_BASE_ADDR").toString());
						orderClaimVO.setRcvrDtlsAddr	(returnClaimTargetList.get(0).get("RCVR_DTLS_ADDR").toString());
						orderClaimVO.setRcvrTypeAdd		(returnClaimTargetList.get(0).get("RCVR_TYPE_ADD").toString());
						
					}
					else {
						orderClaimVO.setRcvrMailNo		("");
						orderClaimVO.setRcvrMailNoSeq   ("");
						orderClaimVO.setRcvrBaseAddr	("");
						orderClaimVO.setRcvrDtlsAddr	("");
						orderClaimVO.setRcvrTypeAdd		("");
					}
					
					if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost())
							|| ("127.0.0.1").equals(request.getRemoteHost())) {
						orderClaimVO.setLocalYn("Y");
					} else {
						orderClaimVO.setLocalYn("N");
					}
					
					String claimCode = returnClaimTargetList.get(0).get("CLAIM_CODE").toString();
					if (claimCode == null || ("").equals(claimCode)  || claimCode.length() != 6){
						
						if(orderClaimVO.getOutBefClaimGb().equals("1")){  //출하지시전 반품, 취소에 걸려있음..
							claimCode = "630499" ; //Defalut;
							
						}else{
							claimCode = "620499" ; //Defalut;							
						}
					}
					
					orderClaimVO.setCsLgroup(claimCode.substring(0,2));
					orderClaimVO.setCsMgroup(claimCode.substring(2,4));
					orderClaimVO.setCsSgroup(claimCode.substring(4,6));
					orderClaimVO.setCsLmsCode(claimCode);
					orderClaimVO.setStandardType("0");     //// 기준내 :1 ,기준외 : 0
					
					Long shpFee = ComUtil.objToLong((returnClaimTargetList.get(0).get("CLM_LST_DLV_CST")));
					if (shpFee > 0 ){
						orderClaimVO.setShpfeeYn("1");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
					}else{
						orderClaimVO.setShpfeeYn("0");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2					
					}
					
					//출하지시 이후 취소건은 반품인데, 이경우 배송비 부과여부를 무상으로 처리한다.
					if(("1").equals(orderClaimVO.getOutBefClaimGb()) && "20".equals(paOrderGb)){
						orderClaimVO.setShpfeeYn("0");
						orderClaimVO.setIs20Claim(true);
					}else{
						orderClaimVO.setIs20Claim(false);
					}
					
					orderClaimVO.setShpFeeAmt(shpFee);
					
					try {
						paClaimService.saveOrderClaimTx(orderClaimVO);
					} catch (Exception e) {
						paramMap = new ParamMap();
						paramMap.put("mappingSeq", orderClaimVO.getMappingSeq());
						paramMap.put("resultCode", "999999");
						paramMap.put("resultMessage", e.getMessage());
						paramMap.put("createYn", "0");
						
						if(paOrderService.updatePaOrdermTx(paramMap) != 1){
							throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
						}
						
						throw processMessageException(e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			paramMap.put("message", "CLAIM_ID : " + claimID + " > " + e.toString());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PaNaver returnInputAsync");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
			}
		}
		
	}
	
	@ApiOperation(value = "반품 철회 데이터", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/returnCancel", method = RequestMethod.GET)
	public void returnCancelAsync(HttpServletRequest request, String orderID, String productOrderID, String claimID) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("orderID", orderID);
		paramMap.put("productOrderID", productOrderID);
		paramMap.put("claimID", claimID);
		paramMap.put("paCode", "41");
		
		try {
		
			List<HashMap<String, String>> returnCancelTargetList = paNaverClaimService.selectReturnCancelTargetDtList(paramMap);
			if(returnCancelTargetList != null && returnCancelTargetList.size() > 0) {
				HashMap<String, String> returnCancelTarget = returnCancelTargetList.get(0);
				if(returnCancelTarget.get("PRE_CANCEL_YN").equals("0")) {
					OrderClaimVO orderClaimVO = new OrderClaimVO();
					orderClaimVO.setMappingSeq(returnCancelTarget.get("MAPPING_SEQ").toString());
					orderClaimVO.setOrderNo(returnCancelTarget.get("ORDER_NO").toString());
					orderClaimVO.setOrderGSeq(returnCancelTarget.get("ORDER_G_SEQ").toString());
					orderClaimVO.setOrderWSeq(returnCancelTarget.get("ORDER_W_SEQ").toString());
					orderClaimVO.setClaimQty(Integer.parseInt(String.valueOf(returnCancelTarget.get("PA_PROC_QTY"))));
					orderClaimVO.setClaimCode(returnCancelTarget.get("CANCEL_CODE").toString());
					orderClaimVO.setInsertId("PANAVER");
					
					try {
						paClaimService.saveClaimCancelTx(orderClaimVO);
					} catch (Exception e) {
						paramMap = new ParamMap();
						paramMap.put("mappingSeq", orderClaimVO.getMappingSeq());
						paramMap.put("resultCode", "999999");
						paramMap.put("resultMessage", e.getMessage());
						paramMap.put("createYn", "0");
						
						if(paOrderService.updatePaOrdermTx(paramMap) != 1){
							throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
						}
						
						throw processMessageException(e.getMessage());
					}
				}
				else {
					//= 기취소건 처리(반품생성 이전 취소건)
					paramMap = new ParamMap();
					paramMap.put("preCancelYn", returnCancelTarget.get("PRE_CANCEL_YN"));
					paramMap.put("preCancelReason", returnCancelTarget.get("PRE_CANCEL_REASON"));
					paramMap.put("mappingSeq", returnCancelTarget.get("MAPPING_SEQ"));
					paramMap.put("preCancelReason", getMessage("pa.before_claim_create_cancel"));
					if(paNaverCancelService.updatePreCancelYnTx(paramMap) != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
					}
				}
			}
		} catch (Exception e) {
			paramMap.put("message", "claimID : " + claimID + " > " + e.toString());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PaNaver returnCancelAsync");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
			}
		}
	}
	
	@ApiOperation(value = "네이버 주문 교환 데이터 생성", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/createExchange", method = RequestMethod.GET)
	@Async
	public void exchangeInputAsync(HttpServletRequest request, String orderID, String productOrderID, String claimID) throws Exception {
		int targetSize = 0;
		HashMap<String, String> changeMap = null
							  , rejectMap = null;
		String claimCode;
		HashMap<String, Object>[] resultMap = null;
		ParamMap paramMap = new ParamMap();
		try {
			paramMap.put("orderID", orderID);
			paramMap.put("productOrderID", productOrderID);
			paramMap.put("claimID", claimID);
			List<HashMap<String, String>> exchangeTargetList = paNaverExchangeService.selectChangeTargetDtList(paramMap); 
			targetSize = exchangeTargetList.size();
			OrderClaimVO[] orderClaimVO = new OrderClaimVO[targetSize];
			for(int j = 0; targetSize > j; j++){
				changeMap = (HashMap<String, String>) exchangeTargetList.get(j);
				
				orderClaimVO[j] = new OrderClaimVO(); 
				orderClaimVO[j].setMappingSeq(changeMap.get("MAPPING_SEQ").toString());
				orderClaimVO[j].setClaimGb(changeMap.get("CLAIM_GB").toString());
				orderClaimVO[j].setOrderNo(changeMap.get("ORDER_NO").toString());
				orderClaimVO[j].setOrderGSeq(changeMap.get("ORDER_G_SEQ").toString());
				orderClaimVO[j].setExchGoodsdtCode(changeMap.get("EXCH_GOODSDT_CODE").toString());
				orderClaimVO[j].setClaimQty(Integer.parseInt(String.valueOf(changeMap.get("CLAIM_QTY"))));
				
				claimCode = changeMap.get("CLAIM_CODE").toString();
				if(claimCode != null && claimCode.length() < 6 ){
					claimCode = "640499";
				}
				orderClaimVO[j].setClaimCode(claimCode);
		
				orderClaimVO[j].setClaimDesc(changeMap.get("CLAIM_DESC").toString());
				orderClaimVO[j].setShipcostChargeYn(changeMap.get("SHIPCOST_CHARGE_YN").toString());
				orderClaimVO[j].setAdminProcYn(changeMap.get("ADMIN_PROC_YN").toString());
				
				if(changeMap.get("CLAIM_GB").toString().equals("40")) {
					orderClaimVO[j].setReturnName(changeMap.get("RECEIVER_NAME").toString());
					orderClaimVO[j].setReturnTel(changeMap.get("RECEIVER_TEL").toString().replace("-", ""));
					orderClaimVO[j].setReturnHp(changeMap.get("RECEIVER_HP").toString().replace("-", ""));
					orderClaimVO[j].setReturnAddr(changeMap.get("RECEIVER_ADDR").toString());
					orderClaimVO[j].setCustDelyYn("0"); //= 교환배송건은 0 고정.
					orderClaimVO[j].setReturnDelyGb(""); //= 교환배송건은 "" 고정.
					orderClaimVO[j].setReturnSlipNo(""); //= 교환배송건은 "" 고정.
				} else {
					orderClaimVO[j].setReturnName(changeMap.get("RETURN_NAME").toString());
					orderClaimVO[j].setReturnTel(changeMap.get("RETURN_TEL").toString().replace("-", ""));
					orderClaimVO[j].setReturnHp(changeMap.get("RETURN_HP").toString().replace("-", ""));
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
				orderClaimVO[j].setInsertId     ("PANAVER");
				if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost())
						|| ("127.0.0.1").equals(request.getRemoteHost())) {
					orderClaimVO[j].setLocalYn("Y");
				} else {
					orderClaimVO[j].setLocalYn("N");
				}
				
			}
			try {
				resultMap = paClaimService.saveOrderChangeTx(orderClaimVO);
			} catch (Exception e) {
				for(int j = 0; orderClaimVO.length > j; j++){
					paramMap = new ParamMap();
					paramMap.put("mappingSeq", orderClaimVO[j].getMappingSeq());
					paramMap.put("resultCode", "999999");
					paramMap.put("resultMessage", e.getMessage());
					paramMap.put("createYn", "0");
					
					if(paOrderService.updatePaOrdermTx(paramMap) != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
					}
				}
				
				throw processMessageException(e.getMessage());
			}
			
			for(int j = 0; resultMap.length > j; j++){
				paramMap = new ParamMap();
				paramMap.setParamMap(resultMap[j]);
				paramMap.replaceCamel();
				
				if(paramMap.getString("resultCode").equals("100001")){
					//pa.out_of_stock_exchange_reject = 재고부족으로 인한 교환불가.
					ParamMap apiResult = new ParamMap();
					
					rejectMap = paNaverClaimService.selectExchangeRejectInfo(paramMap.getString("mappingSeq"));
					rejectMap.put("refsRsn", 	getMessage("pa.out_of_stock_exchange_reject"));
					//= 교환거부처리 호출.
					
					if(!paNaverExchangeController.rejectExchange(request, productOrderID, getMessage("pa.out_of_stock_exchange_reject")).getStatusCode().equals(HttpStatus.OK)){
						throw processException("msg.cannot_save", new String[] { "exchangeInputAsync fail - " +  apiResult.getString("EXCEPTION_MESSAGE")});
					}
					continue;
				}
			}
		} catch (Exception e) {
			paramMap.put("message", "CLAIM_ID : " + paramMap.getString("claimID") + " > " + e.toString());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PaNaver exchangeInputAsync");
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
	
	@ApiOperation(value = "교환 철회 데이터", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "exchangeCancel", method = RequestMethod.GET)
	public void exchangeCancelAsync(HttpServletRequest request, String orderID, String productOrderID, String claimID) throws Exception {
		try {
			
			if(paNaverExchangeService.checkOrderChangeTargetList(orderID) > 0) {
				Paorderm paorderm = new Paorderm();
				paorderm.setPreCancelReason("상담원이 교환처리 하기 전 교환 철회");
				paorderm.setPaOrderNo(orderID);
				paorderm.setPaClaimNo(claimID);
				paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmmss"));
			
				paNaverExchangeService.updatePaOrdermPreChangeCancelTx(paorderm);
				return;
			}
			else {
				ParamMap paramMap = new ParamMap();
				paramMap.put("orderID", orderID);
				paramMap.put("productOrderID", productOrderID);
				paramMap.put("claimID", claimID);
				
				try {
					List<HashMap<String, Object>> exchangeTargetList = paNaverExchangeService.selectChangeCancelTargetDtList(paramMap);
					if(exchangeTargetList.size() == 2) {
						OrderClaimVO[] orderClaimVO = new OrderClaimVO[2];
						
						for(int i=0; i<2; i++){
							if(exchangeTargetList.get(i).get("PRE_CANCEL_YN").toString().equals("0")) {
								orderClaimVO[i] = new OrderClaimVO(); 
								orderClaimVO[i].setMappingSeq(exchangeTargetList.get(i).get("MAPPING_SEQ").toString());
								orderClaimVO[i].setClaimGb(exchangeTargetList.get(i).get("CLAIM_GB").toString());
								orderClaimVO[i].setOrderNo(exchangeTargetList.get(i).get("ORDER_NO").toString());
								orderClaimVO[i].setOrderGSeq(exchangeTargetList.get(i).get("ORDER_G_SEQ").toString());
								orderClaimVO[i].setOrderDSeq(exchangeTargetList.get(i).get("ORDER_D_SEQ").toString());
								orderClaimVO[i].setOrderWSeq(exchangeTargetList.get(i).get("ORDER_W_SEQ").toString());
								orderClaimVO[i].setClaimQty(Integer.parseInt(String.valueOf(exchangeTargetList.get(i).get("CLAIM_QTY"))));
								orderClaimVO[i].setClaimCode(exchangeTargetList.get(i).get("CLAIM_CODE").toString());
								orderClaimVO[i].setShipcostChargeYn(exchangeTargetList.get(i).get("SHIPCOST_CHARGE_YN").toString());
								orderClaimVO[i].setShpFeeAmt(Long.parseLong(String.valueOf(exchangeTargetList.get(i).get("SHIPFEE_COST"))));
								orderClaimVO[i].setInsertId("PANAVER");
								
							} else {
						        //= 기취소건 처리(반품생성 이전 취소건)
						        paramMap = new ParamMap();
						        paramMap.setParamMap(exchangeTargetList.get(i));
						        paramMap.replaceCamel();
						        
						        //=pa.before_claim_create_cancel = 교환생성 이전 취소건
						        paramMap.put("preCancelReason", getMessage("pa.before_change_create_cancel"));
						        if(paNaverCancelService.updatePreCancelYnTx(paramMap) != 1){
						        	throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
						        }
							}
						}
						try {
							
							paClaimService.saveChangeCancelTx(orderClaimVO);
							
						} catch (Exception e) {
							for(int j=0; j<2; j++){
								paramMap = new ParamMap();
								paramMap.put("mappingSeq", orderClaimVO[j].getMappingSeq());
								paramMap.put("resultCode", "999999");
								paramMap.put("resultMessage", e.getMessage());
								paramMap.put("createYn", "0");
								
								if(paOrderService.updatePaOrdermTx(paramMap) != 1){
									throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
								}
							}
							
							throw processMessageException(e.getMessage());
						}
					}
				} catch (Exception e) {
					paramMap.put("message", "claimID : " + claimID + " > " + e.toString());
				} finally {
					try{
						if(paramMap != null && !paramMap.getString("message").equals("")){
							paramMap.put("apiCode", 	"PaNaver exchangeCancelAsync");
							paramMap.put("startDate", 	systemService.getSysdatetimeToString());
							paramMap.put("code",		"500");
							systemService.insertApiTrackingTx(request, paramMap);
						}
					}catch(Exception ee){
						log.error("ApiTracking Insert Error : "+ee.toString());
					}
				}
			}
		} catch (Exception e) {
			//do nothing
		}
	}
	
	@Async
	public void preOrderUpdateAsync(HttpServletRequest request, HashMap<String, String> hmSheet) throws Exception {
	
		String orderNo   	 = hmSheet.get("ORDER_NO").toString();
		String paOrderNo	 = hmSheet.get("PA_ORDER_NO").toString();
		String siteGb 	 	 = hmSheet.get("SITE_GB").toString();
		String isLocalYn	 = hmSheet.get("isLocalYn").toString();
		String paGroupCode 	 = hmSheet.get("PA_GROUP_CODE").toString();
		double paShpAmt	 = 0;
		ParamMap paramMap = new ParamMap();
		HashMap<String,Object> addrMap = new HashMap<String, Object>(); 
		
		try{
			paramMap.put("apiCode"		, "preOrderInputAsync");
			paramMap.put("startDate"	, systemService.getSysdatetimeToString());
			paramMap.put("siteGb"		, siteGb);
			//preOrderUpdateTargetDtList = paGmktOrderService.selectPreOrderUpdateTargetDtList(paOrderNo); 
			paramMap.put("orderNo"   	, orderNo );
			paramMap.put("paOrderNo" 	, paOrderNo);
			paramMap.put("paGroupCode"	, paGroupCode);	
			paramMap.put("localYn"		, isLocalYn);


			paShpAmt = paNaverInfoCommonService.selectPaOrderShipCost(paOrderNo);
			addrMap  = paNaverInfoCommonService.selectPaAddrInfo(paOrderNo);
			paramMap.put("paShpAmt"  	, paShpAmt);
			setChangedAddress(paramMap, addrMap);
			
			paOrderService.upDateOrderTx(paramMap);
			
			paramMap.put("code","200");
			paramMap.put("message","UPDATE");
			
		}catch(Exception e){
			log.error(e.getMessage());
			paramMap.put("code", "500");
			paramMap.put("message", "선물 수락 승인 전처리 실패");
			paOrderService.updatePaOrderMFailConfrimPreOrder(hmSheet, e.getMessage());
		}finally{
			try{
				systemService.insertPaApiTrackingTx(request, paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
	}
	
	private void setChangedAddress(ParamMap paramMap, HashMap<String, Object> addrMap){
		if(addrMap == null) return;
		
		String type = "3";
		
		String zipCode = addrMap.get("ZIPCODE").toString().replace("-", "").trim();
			if(zipCode.length() == 6){
				type  = "01"; //1:지번주소, 2:도로명주소
			}else{
				type  = "02";
			}
	
		paramMap.put("receiverTel"  , addrMap.get("RECEIVER_TEL").toString().replace("-", ""));
		paramMap.put("receiverHp"	, addrMap.get("RECEIVER_HP").toString().replace("-", ""));

		paramMap.put("postNo"		, zipCode);
		//paramMap.put("postNoSeq"	, addrMap.get("POST_NO_SEQ").toString());
		paramMap.put("addrGbn"		, type);
		paramMap.put("addr"			, addrMap.get("RECEIVER_ADDR1").toString());
		paramMap.put("addrDt"		, addrMap.get("RECEIVER_ADDR2").toString());
		paramMap.put("receiverSeq"	, addrMap.get("RECEIVER_SEQ").toString());
		paramMap.put("receiverName" , addrMap.get("RECEIVER_NAME").toString());
	}
	
	@Async
	public void spPagoodsSyncNaver(HttpServletRequest request, String goodsCode, String userId) throws Exception{
		ParamMap paramMap = new ParamMap();
		ParamMap stopSaleParam = new ParamMap();
		ParamMap epNameParam = new ParamMap();
		ParamMap entpSlipParam = new ParamMap();
		ParamMap stopShipParam = new ParamMap();
		List<PaGoodsImage> curImageInfo = null;
		List<PaGoodsPriceVO> curPriceInfo = null;
		List<PaEntpSlip> curEntpSlipInfo = null;
		HashMap<String, String> minMarginPrice = new HashMap<String, String>();
		List<PaCustShipCostVO> curShipCostInfo = null;
		List<HashMap<String, String>> curShipStopSale = null;
		List<HashMap<String, String>> curSaleStop = null;
		List<HashMap<String, String>> curEventMargin = null;
		List<HashMap<String, String>> curStockCheck = null;
		List<HashMap<String, String>> curEpNameInfo = null;
		List<HashMap<String, String>> curEntpSlipChangeInfo = null;
		
		String resultMsg = "";
		String dateTime = systemService.getSysdatetimeToString();
		StringBuffer sb = null;
		int conditionDay = 2;		
		
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paGroupCode", "04");
		paramMap.put("dateTime", dateTime);
		paramMap.put("conditionDay", conditionDay);		
		paramMap.put("comparePaCode", "41");
		paramMap.put("feeCode", "O640");
		paramMap.put("minMarginCode", "50");
		paramMap.put("minPriceCode", "51");
		
		paramMap.put("siteGb", userId);
		paramMap.put("apiCode", "IF_PACOMMON_00_002");
		
		log.info("Step1. 네이버 상품이미지 동기화 START");
		curImageInfo = paCommonService.selectCurImageInfoList(paramMap);
		if(curImageInfo.size() > 0) {
			sb = new StringBuffer();
			for(PaGoodsImage curImageInfoTarget : curImageInfo) {
				//이미지 없는 케이스가 많아 for문안에 try catch 처리
				try {
					curImageInfoTarget.setModifyId("PANAVER");
					curImageInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					curImageInfoTarget.setRemark(dateTime);
					resultMsg = paCommonService.saveCurImageInfoTx(curImageInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step1 상품이미지 동기화 네이버 Fail > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
						sb.append(curImageInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step1 상품이미지 동기화 네이버 Sucess > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
				} catch(Exception e) {
					log.info("Step1. 네이버 상품이미지 동기화 : " + e.getMessage());
					paramMap.put("code", "500");
					paramMap.put("message", "Step1. 네이버 상품이미지 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}
		}
		log.info("Step1. 네이버 상품이미지 동기화 END");
		
		log.info("Step2. 네이버 상품가격 동기화 START");
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
					 || ("61".equals(curPriceInfoTarget.getSourcingMedia()) && "0032".equals(curPriceInfoTarget.getMdKind()) && ComUtil.objToDouble(curPriceInfoTarget.getMarginRate()) < 21) ) {
						
						stopSaleParam.put("paGroupCode", "04");
						stopSaleParam.put("paCode", curPriceInfoTarget.getPaCode());
						stopSaleParam.put("goodsCode", curPriceInfoTarget.getGoodsCode());
						stopSaleParam.put("dateTime", dateTime);
						stopSaleParam.put("userId", "PANAVER");
						stopSaleParam.put("paGoodsCode", curPriceInfoTarget.getPaGoodsCode());
						stopSaleParam.put("note", "마진10이하 혹은 판매가100원 미만");
						stopSaleParam.put("priceStopSale", "Y");
						
						//연동제외처리
						resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
						
					} else {
						curPriceInfoTarget.setModifyId("PANAVER");
						curPriceInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						curPriceInfoTarget.setDateTime(dateTime);
						resultMsg = paCommonService.saveCurPriceInfoTx(curPriceInfoTarget);
						
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step2 상품가격 동기화 네이버 Fail > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
						sb.append(curPriceInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step2 상품가격 동기화 네이버 Sucess > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
				}
			} catch(Exception e) {
				log.info("Step2. 네이버 상품가격 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step2. 네이버 상품가격 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step2. 네이버 상품가격 동기화 END");
		
		log.info("Step3. 네이버 고객부담배송비 동기화 START");
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
							
							stopSaleParam.put("paGroupCode", "04");
							stopSaleParam.put("paCode", curShipStopSaleTarget.get("PA_CODE").toString());
							stopSaleParam.put("goodsCode", curShipStopSaleTarget.get("GOODS_CODE").toString());
							stopSaleParam.put("dateTime", dateTime);
							stopSaleParam.put("userId", "PANAVER");
							stopSaleParam.put("paGoodsCode", curShipStopSaleTarget.get("PA_GOODS_CODE").toString());
							stopSaleParam.put("note", "배송비유료변경");
							stopSaleParam.put("priceStopSale", "N");
							
							//연동제외처리
							resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
						}
						
					} else {
						curShipCostInfoTarget.setModifyId("PANAVER");
						curShipCostInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						resultMsg = paCommonService.saveCurShipCostInfoTx(curShipCostInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step3 고객부담배송비 네이버 Fail");
						sb.append(curShipCostInfoTarget.getEntpCode() + "|" + curShipCostInfoTarget.getShipCostCode() + ", ");
						continue;
					}
					log.info("Step3 고객부담배송비 네이버 Sucess");
				}
			} catch(Exception e) {
				log.info("Step3. 네이버 고객부담배송비 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step3. 네이버 고객부담배송비 동기화 : " + e.getMessage() + "INFO : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step3. 네이버 고객부담배송비 동기화 END");
		
		log.info("Step4. 네이버 출고/회수지 동기화 START");
		curEntpSlipInfo = paCommonService.selectCurEntpSlipInfoList(paramMap);
		if(curEntpSlipInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaEntpSlip curEntpSlipInfoTarget : curEntpSlipInfo) {
					
					curEntpSlipInfoTarget.setModifyId("PANAVER");
					curEntpSlipInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					resultMsg = paCommonService.saveCurEntpSlipInfoTx(curEntpSlipInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step4 출고/회수지 동기화 네이버 Fail");
						sb.append(curEntpSlipInfoTarget.getEntpCode() + ", ");
						continue;
					}
					log.info("Step4 출고/회수지 동기화 네이버 Sucess");
				}
			} catch(Exception e) {
				log.info("Step4. 네이버 출고/회수지 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step4. 네이버 출고/회수지 동기화 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step4. 네이버 출고/회수지 회수지 동기화 END");
		
		log.info("Step5. 네이버 상품판매단계 동기화 START");
		curSaleStop = paCommonService.selectCurSaleStopList(paramMap);
		
		if(curSaleStop.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curSaleStopTarget : curSaleStop) {
					
					stopSaleParam.put("paGroupCode", "04");
					stopSaleParam.put("paCode", curSaleStopTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curSaleStopTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PANAVER");
					stopSaleParam.put("paGoodsCode", curSaleStopTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "SK스토아상품판매중단");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step5 상품판매단계 동기화 네이버 Fail > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
						sb.append(curSaleStopTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step5 상품판매단계 동기화 네이버 Sucess > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step5. 네이버 상품판매단계 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step5. 네이버 상품판매단계 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step5. 네이버 상품판매단계 동기화 END");
		
		log.info("Step6. 네이버 행사 종료 상품 마진 체크 START");
		curEventMargin = paCommonService.selectCurEventMarginList(paramMap);
		
		if(curEventMargin.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curEventMarginTarget : curEventMargin) {
					paramMap.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					//minMarginPrice = paCommonService.selectMinMarginPrice(paramMap);
					//selectCurEventMarginList 안에서 체크
					//if(ComUtil.objToDouble(curEventMarginTarget.get("MARGIN_RATE")) < ComUtil.objToDouble(minMarginPrice.get("MIN_MARGIN"))) {
					stopSaleParam.put("paGroupCode", "04");
					stopSaleParam.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curEventMarginTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PANAVER");
					stopSaleParam.put("paGoodsCode", curEventMarginTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "행사종료");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step6 행사 종료 상품 마진 체크 네이버 Fail > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
						sb.append(curEventMarginTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step6 행사 종료 상품 마진 체크 네이버 Sucess > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
					//}					
				}
			} catch(Exception e) {
				log.info("Step6. 네이버 행사 종료 상품 마진 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step6. 네이버 행사 종료 상품 마진 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step6. 네이버 행사 종료 상품 마진 체크 END");
		
		log.info("Step7. 네이버 단품재고 동기화 START");
		curStockCheck = paCommonService.selectCurStockCheckList(paramMap);
		if(curStockCheck.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curStockCheckTarget : curStockCheck) {					
					
					stopSaleParam.put("paGroupCode", "04");
					stopSaleParam.put("paCode", curStockCheckTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curStockCheckTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					
					resultMsg = paCommonService.saveCurStockCheckTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step7 단품재고 동기화 네이버 Fail > GOODS_CODE : " + curStockCheckTarget.get("GOODS_CODE").toString());
						sb.append(curStockCheckTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step7 단품재고 동기화 네이버 Sucess > GOODS_CODE : " + curStockCheckTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step7. 네이버 단품재고 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step7. 네이버 단품재고 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step7. 네이버 단품재고 동기화 END");
		
		log.info("Step8. 네이버 EP상품명 동기화 START");
		curEpNameInfo = paCommonService.selectCurEpNameInfoList(paramMap);
		if(curEpNameInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curEpNameInfoTarget : curEpNameInfo) {					
					
					epNameParam.put("paGroupCode", "04");
					epNameParam.put("paCode", curEpNameInfoTarget.get("PA_CODE").toString());
					epNameParam.put("goodsCode", curEpNameInfoTarget.get("GOODS_CODE").toString());
					epNameParam.put("goodsNameMc", curEpNameInfoTarget.get("GOODS_NAME_MC").toString());
					epNameParam.put("dateTime", dateTime);
					
					//커서만 다르고 처리하는건 위에랑 동일
					resultMsg = paCommonService.saveCurStockCheckTx(epNameParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step8  EP상품명 동기화 네이버 Fail > GOODS_CODE : " + curEpNameInfoTarget.get("GOODS_CODE").toString());
						sb.append(curEpNameInfoTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step8  EP상품명 동기화 네이버 Sucess > GOODS_CODE : " + curEpNameInfoTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step8.  EP상품명 동기화 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step8.  EP상품명 동기화 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step8. 네이버  EP상품명 동기화 END");
		
		//step8 출고/회수지 변경이력 생성 누락됨
		log.info("Step9. 네이버 출고/회수지 변경이력 생성 START");
		curEntpSlipChangeInfo = paCommonService.selectCurEntpSlipChangeInfoList(paramMap);
		if(curEntpSlipChangeInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curEntpSlipChangeInfoTarget : curEntpSlipChangeInfo) {					
					entpSlipParam.put("entpCode", curEntpSlipChangeInfoTarget.get("ENTP_CODE").toString());
					entpSlipParam.put("entpManSeq", curEntpSlipChangeInfoTarget.get("ENTP_MAN_SEQ").toString());
					entpSlipParam.put("paCode", "41");
					entpSlipParam.put("changeSeq", curEntpSlipChangeInfoTarget.get("CHANGE_SEQ").toString());
					entpSlipParam.put("changeFlag", curEntpSlipChangeInfoTarget.get("CHANGE_FLAG").toString());
					entpSlipParam.put("procFlag", "00");
					entpSlipParam.put("dateTime", dateTime);
					entpSlipParam.put("userId", "PANAVER");
					
					resultMsg = paCommonService.saveCurNaverEntpSlipTx(entpSlipParam);
		
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step9  출고/회수지 변경이력 생성 네이버 Fail > ENTP_CODE : " + curEntpSlipChangeInfoTarget.get("ENTP_CODE").toString());
						sb.append(curEntpSlipChangeInfoTarget.get("ENTP_CODE").toString() + ", ");
						continue;
					}
		
					log.info("Step9  출고/회수지 변경이력 생성 네이버 Sucess > ENTP_CODE : " + curEntpSlipChangeInfoTarget.get("ENTP_CODE").toString());					
				}
			} catch(Exception e) {
				log.info("Step9. 네이버 출고/회수지 변경이력 생성 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step9. 네이버 출고/회수지 변경이력 생성 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step9. 네이버  출고/회수지 변경이력 생성 END");
	}
}
