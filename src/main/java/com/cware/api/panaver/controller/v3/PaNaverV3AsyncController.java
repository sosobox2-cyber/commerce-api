package com.cware.api.panaver.controller.v3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.common.util.StringUtil;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.CancelInputVO;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.OrderInputVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.panaver.v3.domain.ClaimDeliveryFeePayMethodType;
import com.cware.netshopping.panaver.v3.service.PaNaverV3CancelService;
import com.cware.netshopping.panaver.v3.service.PaNaverV3ClaimService;
import com.cware.netshopping.panaver.v3.service.PaNaverV3DeliveryService;
import com.cware.netshopping.panaver.v3.service.PaNaverV3ExchangeService;

import io.swagger.annotations.ApiOperation;

@Controller("com.cware.api.panaver.v3.PaNaverV3AsycController")
@RequestMapping(value="/panaver/v3/async")
public class PaNaverV3AsyncController extends AbstractController {

	@Autowired
    private SystemService systemService;
	
	@Autowired
	private PaOrderService paOrderService;
	
	@Autowired
	private PaClaimService paClaimService;
	
	@Autowired
	@Qualifier("panaver.v3.cancel.paNaverV3CancelService")
	private PaNaverV3CancelService paNaverV3CancelService;
	
	@Autowired
	@Qualifier("panaver.v3.exchange.paNaverV3ExchangeService")
	private PaNaverV3ExchangeService paNaverV3ExchangeService;
	
	@Autowired
	@Qualifier("panaver.v3.delivery.paNaverV3DeliveryService")
	private PaNaverV3DeliveryService paNaverV3DeliveryService;
	
	@Autowired
	@Qualifier("panaver.v3.claim.paNaverV3ClaimService")
	private PaNaverV3ClaimService paNaverV3ClaimService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	/**
	 * 네이버 주문접수 데이터 생성 
	 * 
	 * @param request
	 * @param orderId
	 * @return
	 * @throws Exception
	 */	
	@ApiOperation(value = "네이버 주문접수 데이터 생성", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/createOrder", method = RequestMethod.GET)
	@Async
	public void orderCreateAsync(HttpServletRequest request, String orderId) throws Exception {
		ParamMap paramMap = null;
		
		int targetSize = 0;
		int executedRtn = 0;
		HashMap<String, Object>[] resultMap = null;
		HashMap<String, String> refusalMap = null;
		OrderInputVO[] orderInputs;
		
		String procId = "PANAVER";
		
		try {
			List<HashMap<String, Object>> createOrderTargetList = paNaverV3DeliveryService.selectOrderInputTargetDtList(orderId);
			targetSize = createOrderTargetList.size();
			orderInputs = new OrderInputVO[targetSize];
			
			if (targetSize > 0) {
				HashMap<String, Object> target;
				
				for (int i=0; i<targetSize; i++) {
					target = createOrderTargetList.get(i);
					
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
					
					if (target.get("DELIVERY_POLICY_TYPE").toString().equals("유료") || target.get("DELIVERY_POLICY_TYPE").toString().equals("조건부무료")
							|| target.get("DELIVERY_POLICY_TYPE").toString().equals("수량별") || target.get("DELIVERY_POLICY_TYPE").toString().equals("무료")) {
						if (i==targetSize-1) {
							orderInputs[i].setShpFeeCost(ComUtil.objToLong(target.get("SHP_FEE_COST")));
						} else {
							orderInputs[i].setShpFeeCost(0);
						}
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
					
					// PROMOTION DATA SETTING 
					double doAmt = ComUtil.objToDouble(target.get("OUT_PROMO_PRICE"));
		            if (doAmt > 0) {
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
				
				try {
					if (orderInputs[0].getDoFlag().equals("10")) {
						resultMap = paOrderService.newSaveOrderTx(orderInputs);
					} else {
						resultMap = paOrderService.saveOrderTx(orderInputs);
					}
				} catch (Exception e) {
					for (int j = 0; j<targetSize; j++) {
						paramMap = new ParamMap();
						paramMap.put("mappingSeq", orderInputs[j].getMappingSeq());
						paramMap.put("resultCode", "999999");
						paramMap.put("resultMessage", e.getMessage());
						paramMap.put("createYn", "0");
						
						executedRtn = paOrderService.updatePaOrdermTx(paramMap);
						
						if (executedRtn != 1){
							throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
						}
					}
					throw processMessageException(e.getMessage());
				}
				
				for (int j=0; j<targetSize; j++) {
					//= 취소처리 시작
					paramMap = new ParamMap();
					paramMap.setParamMap(resultMap[j]);
					paramMap.replaceCamel();
					
					// 재고부족 or 판매불가 상태
					if (paramMap.getString("resultCode").equals("100001")) {
						refusalMap = paNaverV3DeliveryService.selectOrderMappingInfoByMappingSeq(paramMap.getString("mappingSeq"));
						
						// 네이버 취소요청 호출
						paNaverV3CancelService.requestCancel(ComUtil.objToStr(refusalMap.get("PA_ORDER_SEQ")), ComUtil.objToStr(refusalMap.get("PA_ORDER_NO")), ComUtil.objToStr(refusalMap.get("PA_ORDER_SEQ")), orderInputs[j].getPaCode(), 2, procId, request);
					}
					//= 취소처리 종료
				} //= for
			} //= if
		} catch (Exception e) {
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "pa_order_no : " + orderId + " > " + e.getMessage());
		} finally {
			try {
				if (paramMap != null && !paramMap.getString("message").equals("")) {
					paramMap.put("apiCode", "PaNaver orderCreateAsync");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code", "500");
					paramMap.put("paCode", "41");
					
					systemService.insertApiTrackingTx(request, paramMap);
				}
			} catch(Exception ee) {
				log.error("ApiTracking Insert Error : "+ee.toString());
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
			List<HashMap<String, Object>> cancelInputTargetDtList = paNaverV3CancelService.selectCancelInputTargetDtList(cancelMap);
			
			if (cancelInputTargetDtList.size() > 0) {
				hmSheet = cancelInputTargetDtList.get(0);
			
				if (hmSheet.get("PRE_CANCEL_YN").toString().equals("0")) {
					//= 일반취소건 처리.
					CancelInputVO cancelInputVO = new CancelInputVO();
					cancelInputVO.setMappingSeq(hmSheet.get("MAPPING_SEQ").toString());
					cancelInputVO.setOrderNo(hmSheet.get("ORDER_NO").toString());
					cancelInputVO.setOrderGSeq(hmSheet.get("ORDER_G_SEQ").toString());
					cancelInputVO.setCancelQty(Integer.parseInt(String.valueOf(hmSheet.get("PA_PROC_QTY"))));
					
					cancelCode = hmSheet.get("CANCEL_CODE").toString();
					
					if (cancelCode == null || "".equals(cancelCode) || cancelCode.length() != 6) {
						cancelCode = "620499"; // 제휴 취소 (상담분류코드 > TCANCELDT.CS_LMS_CODE)
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
					
						if (paOrderService.updatePaOrdermTx(paramMap) != 1) {
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
					
					if (paNaverV3CancelService.updatePreCancelYnTx(preCancelMap) != 1) {
						throw processException("msg.cannot_save", new String[] {"TPAORDERM(pre_cancel_yn) UPDATE"});
					}
				}
			}
		} catch(Exception e) {
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "claimSeq : " + paramMap.get("claimSeq") + " > " + e.toString());
		} finally {
			try {
				if (paramMap != null && !paramMap.getString("message").equals("")) {
					paramMap.put("apiCode", "PaNaver cancelInputAsync");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code", "500");
					paramMap.put("paCode", "41");
					
					systemService.insertApiTrackingTx(request, paramMap);
				}
			} catch(Exception ee) {
				log.error("ApiTracking Insert Error : " + ee.toString());
			}
		}
	}
	
	@ApiOperation(value = "네이버 주문 교환 데이터 생성", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/createExchange", method = RequestMethod.GET)
	@Async
	public void exchangeInputAsync(HttpServletRequest request, String orderId, String productOrderId, String claimId) throws Exception {
		int targetSize = 0;
		HashMap<String, String> changeMap = null
							  , rejectMap = null;
		String claimCode;
		HashMap<String, Object>[] resultMap = null;
		ParamMap paramMap = new ParamMap();
		
		try {
			paramMap.put("orderId", orderId);
			paramMap.put("productOrderId", productOrderId);
			paramMap.put("claimId", claimId);
			paramMap.put("paCode", "41");
			
			List<HashMap<String, String>> exchangeTargetList = paNaverV3ExchangeService.selectChangeTargetDtList(paramMap); 
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
				
				if (claimCode != null && claimCode.length() < 6 ) {
					claimCode = "640499";
				}
				orderClaimVO[j].setClaimCode(claimCode);
				orderClaimVO[j].setClaimDesc(changeMap.get("CLAIM_DESC").toString());
				orderClaimVO[j].setShipcostChargeYn(changeMap.get("SHIPCOST_CHARGE_YN").toString());
				orderClaimVO[j].setAdminProcYn(changeMap.get("ADMIN_PROC_YN").toString());
				
				if (changeMap.get("CLAIM_GB").toString().equals("40")) {
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
					
					if (changeMap.get("CUST_DELY_YN").toString().equals("1")) {
						orderClaimVO[j].setReturnDelyGb(changeMap.get("RETURN_DELY_GB").toString()); //= 직접발송일 경우 11번가 고객입력 배송사코드.
						orderClaimVO[j].setReturnSlipNo(StringUtil.null2string(changeMap.get("RETURN_SLIP_NO").toString(), getMessage("pa.cust_dely_slip_no_empty"))); //= 직접발송일 경우 11번가 고객입력 운송장번호.
					} else {
						orderClaimVO[j].setReturnDelyGb("");
						orderClaimVO[j].setReturnSlipNo("");
					}
				}
				Long shpFeeAmt = Long.parseLong(String.valueOf(changeMap.get("CLM_LST_DLV_CST")));
				
				if (shpFeeAmt > 0) {
					orderClaimVO[j].setShpfeeYn("1");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
				} else {
					if (ClaimDeliveryFeePayMethodType.UNCLAIMED.codeName().equals((changeMap.get("DELIVERY_FEE_PAY_METHOD").toString())) && "1".equals(orderClaimVO[j].getShipcostChargeYn())) {
						orderClaimVO[j].setShpfeeYn("1"); // 반품안심케어 대상 && 고객 귀책사유 > 배송비 부과 여부 - 유상
					} else {
						orderClaimVO[j].setShpfeeYn("0");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2
					}
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
			
			if(targetSize > 0) {
				try {
					resultMap = paClaimService.saveOrderChangeTx(orderClaimVO);
				} catch (Exception e) {
					for (int j = 0; orderClaimVO.length > j; j++) {
						paramMap = new ParamMap();
						paramMap.put("mappingSeq", orderClaimVO[j].getMappingSeq());
						paramMap.put("resultCode", "999999");
						paramMap.put("resultMessage", e.getMessage());
						paramMap.put("createYn", "0");
						
						if (paOrderService.updatePaOrdermTx(paramMap) != 1) {
							throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
						}
					}
					throw processMessageException(e.getMessage());
				}
				
				for (int j = 0; resultMap.length > j; j++) {
					paramMap = new ParamMap();
					paramMap.setParamMap(resultMap[j]);
					paramMap.replaceCamel();
					
					if (paramMap.getString("resultCode").equals("100001")) {
						//= pa.out_of_stock_exchange_reject = 재고부족으로 인한 교환불가.
						ParamMap apiResult = new ParamMap();
						
						rejectMap = paNaverV3ExchangeService.selectExchangeRejectInfo(paramMap.getString("mappingSeq"));
						rejectMap.put("refsRsn", getMessage("pa.out_of_stock_exchange_reject"));
						//= 교환거부처리 호출.
						
						if (paNaverV3ExchangeService.reject(productOrderId, getMessage("pa.out_of_stock_exchange_reject"), "PANAVER", request).getStatus() != 200) {
							throw processException("msg.cannot_save", new String[] { "exchangeInputAsync fail - " +  apiResult.getString("EXCEPTION_MESSAGE")});
						}
						continue;
					}
				}
			}
			
		} catch (Exception e) {
			paramMap.put("message", "CLAIM_ID : " + paramMap.getString("claimID") + " > " + e.toString());
		} finally {
			try{
				if (paramMap != null && !paramMap.getString("message").equals("")) {
					paramMap.put("apiCode", "PaNaver exchangeInputAsync");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code", "500");
					
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
	public void exchangeCancelAsync(HttpServletRequest request, String orderId, String productOrderId, String claimId) throws Exception {
		String procId = "PANAVER";
		
		try {			
			if (paNaverV3ExchangeService.checkOrderChangeTargetList(orderId, claimId) > 0) {
				//= 교환접수 상담원 미처리건 교환철회
				Paorderm paorderm = new Paorderm();
				paorderm.setPreCancelReason("상담원이 교환처리 하기 전 교환 철회");
				paorderm.setPaOrderNo(orderId);
				paorderm.setPaClaimNo(claimId);
				paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmmss"));
			
				paNaverV3ExchangeService.updatePaOrdermPreChangeCancelTx(paorderm);
				return;
			} else if (paNaverV3ExchangeService.checkOrderChangeInputTargetList(orderId, claimId) > 0) {
				//= 교환접수 상담원 처리 후 교환 데이터 생성전, 교환철회 접수
				Paorderm paorderm = new Paorderm();
				paorderm.setPreCancelReason("상담원이 교환처리 후 교환 데이터 생성 전 교환 철회");
				paorderm.setPaOrderNo(orderId);
				paorderm.setPaClaimNo(claimId);
				paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmmss"));
			
				paNaverV3ExchangeService.updatePaOrdermPreChangeCancelTx(paorderm);
				return;
			}
			ParamMap paramMap = new ParamMap();
			paramMap.put("orderId", orderId);
			paramMap.put("productOrderId", productOrderId);
			paramMap.put("claimId", claimId);
			
			try {
				List<HashMap<String, Object>> exchangeTargetList = paNaverV3ExchangeService.selectChangeCancelTargetDtList(paramMap); // 교환접수 철회건 + 교환 기취소건
				
				if (exchangeTargetList.size() == 2) {
					OrderClaimVO[] orderClaimVO = new OrderClaimVO[2];
					
					for (int i=0; i<2; i++) {
						if (exchangeTargetList.get(i).get("PRE_CANCEL_YN").toString().equals("0")) {
							//= 교환접수 상담원 처리건 교환철회
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
							orderClaimVO[i].setInsertId(procId);							
						} else {
					        //= 기취소건 처리(교환생성 이전 취소건)
					        paramMap = new ParamMap();
					        paramMap.setParamMap(exchangeTargetList.get(i));
					        paramMap.replaceCamel();
					        
					        //=pa.before_claim_create_cancel = 교환생성 이전 취소건
					        paramMap.put("preCancelReason", getMessage("pa.before_change_create_cancel"));
					        
					        if (paNaverV3CancelService.updatePreCancelYnTx(paramMap) != 1) {
					        	throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
					        }
						}
					}
					if(orderClaimVO[0] != null  && orderClaimVO[1] != null) {
						try {
							paClaimService.saveChangeCancelTx(orderClaimVO);	
						} catch (Exception e) {
							for (int j=0; j<2; j++) {
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
						
				}
			} catch (Exception e) {
				paramMap.put("message", "claimID : " + claimId + " > " + e.toString());
			} finally {
				try{
					if (paramMap != null && !paramMap.getString("message").equals("")) {
						paramMap.put("apiCode", "PaNaver exchangeCancelAsync");
						paramMap.put("startDate", systemService.getSysdatetimeToString());
						paramMap.put("code", "500");
						paramMap.put("siteGb", procId);
						
						systemService.insertApiTrackingTx(request, paramMap);
					}
				} catch(Exception ee) {
					log.error("ApiTracking Insert Error : "+ee.toString());
				}
			}
		} catch (Exception e) {
			//do nothing
		}
	}
	
	
	@ApiOperation(value = "네이버 주문 반품 데이터 생성", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/createReturn", method = RequestMethod.GET)
	public void returnInputAsync(HttpServletRequest request, String orderId, String productOrderId, String claimId) throws Exception {
		ParamMap paramMap = new ParamMap();
		List<HashMap<String, String>> returnClaimTargetList = null;
		String paOrderGb = null;
		
		String procId = "PANAVER";
		
		try {
			paramMap = new ParamMap();
			paramMap.put("orderId", orderId);
			paramMap.put("productOrderId", productOrderId);
			paramMap.put("claimId", claimId);
			returnClaimTargetList = paNaverV3ClaimService.selectReturnClaimTargetList(paramMap);
			
			if(returnClaimTargetList.size() == 1) {
				paOrderGb = returnClaimTargetList.get(0).get("PA_ORDER_GB").toString();
				
				if("30".equals(paOrderGb)) {
					returnClaimTargetList = paNaverV3ClaimService.selectReturnClaimTargetDt30List(paramMap);
				} 
				else {
					returnClaimTargetList = paNaverV3ClaimService.selectReturnClaimTargetDt20List(paramMap);
				} 
				
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
						orderClaimVO.setReturnDelyGb(returnClaimTargetList.get(0).get("RETURN_DELY_GB").toString());  //= 직접발송일 경우  고객입력 배송사코드. 
						orderClaimVO.setReturnSlipNo(StringUtil.null2string(returnClaimTargetList.get(0).get("RETURN_SLIP_NO").toString(), getMessage("pa.cust_dely_slip_no_empty"))); //= 직접발송일 경우  고객입력 운송장번호.
					}
					else {
						orderClaimVO.setReturnDelyGb("");
						orderClaimVO.setReturnSlipNo("");
					}
					
					orderClaimVO.setOutBefClaimGb(returnClaimTargetList.get(0).get("OUT_BEF_CLAIM_GB").toString());
					
					if("30".equals(paOrderGb)){  
						
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
					//출하지시 이후 취소건은 반품인데, 이경우 배송비 부과여부를 무상으로 처리한다.
					if(("1").equals(orderClaimVO.getOutBefClaimGb()) && "20".equals(paOrderGb)) {
						orderClaimVO.setShpfeeYn("0"); //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
						orderClaimVO.setIs20Claim(true);
					}else {
						orderClaimVO.setIs20Claim(false);
						if (shpFee > 0 ){
							orderClaimVO.setShpfeeYn("1");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
						}else{
							/* 반품안심 케어 상품(반품비 3500원 이상 제외)인 경우, 고객이 구매내역에서 직접 반품 접수 하면 무슨 사유던지 "반품 안심케어"로 보내준다.
							  그러나, 판매자 센터를 통해서 반품 사유 변경 시 귀책사유 구매자 -> 판매자 로 바뀌면 "보류해제" 처리가 되면서 반품 안심케어 대상에서 제외된다. 
							  구매자 귀책 a > 구매자 귀책 b : 반품안심케어 유지
							  판매자 귀책 a > 판매자 귀책 b : 반품안심케어 유지
							  판매자 귀책 a > 구매자 귀책 a : 반품안심케어 유지
							  
							  구매자 귀책 a >  판매자 귀책 a : 반품안심케어 x
							  판매자 귀책 a > 구매자 귀책 a > 판매자 귀책 a  : 반품안심케어 x
							*/
							if("30".equals(paOrderGb) && ClaimDeliveryFeePayMethodType.UNCLAIMED.codeName().equals(returnClaimTargetList.get(0).get("DELIVERY_FEE_PAY_METHOD"))
									&& "1".equals(returnClaimTargetList.get(0).get("SHIPCOST_CHARGE_YN"))) {
								orderClaimVO.setShpfeeYn("1");// 반품안심케어 대상 && 고객 귀책사유 > 배송비 부과 여부 - 유상
							}else {
								orderClaimVO.setShpfeeYn("0");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2					
							}
						}
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
			paramMap.put("message", "CLAIM_ID : " + claimId + " > " + e.toString());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", 	"PaNaver returnInputAsync");
					paramMap.put("startDate", 	systemService.getSysdatetimeToString());
					paramMap.put("code",		"500");
					paramMap.put("siteGb", procId);
					
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
			}
		}
		
	}
	
	@ApiOperation(value = "반품 철회 데이터", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/returnCancel", method = RequestMethod.GET)
	public void returnCancelAsync(HttpServletRequest request, String orderId, String productOrderId, String claimId) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("orderId", orderId);
		paramMap.put("productOrderId", productOrderId);
		paramMap.put("claimId", claimId);
		paramMap.put("paCode", "41");
		
		try {
		
			List<HashMap<String, String>> returnCancelTargetList = paNaverV3ClaimService.selectReturnCancelTargetDtList(paramMap);
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
					if(paNaverV3CancelService.updatePreCancelYnTx(paramMap) != 1){
						throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
					}
				}
			}
		} catch (Exception e) {
			paramMap.put("message", "CLAIM_ID : " + claimId + " > " + e.toString());
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
	
}
