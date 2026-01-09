package com.cware.api.pakakao.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaKakaoSettlement;
import com.cware.netshopping.pakakao.settlement.service.PaKakaoSettlementService;
import com.cware.netshopping.pakakao.util.PaKakaoComUtill;
import com.cware.netshopping.pakakao.util.PaKakaoConnectUtil;
import com.cware.netshopping.pakakao.v2.code.KakaoPayMainMethod;
import com.cware.netshopping.pakakao.v2.code.KakaoSettlementTermType;
import com.cware.netshopping.pakakao.v2.code.KakaoSettlementType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/pakakao/settlement", description="카카오 정산")
@Controller("com.cware.api.pakakao.PaKakaoSettlementController")
@RequestMapping(value = "/pakakao/settlement")
public class PaKakaoSettlementController extends AbstractController  {
	
	private transient static Logger log = LoggerFactory.getLogger(PaKakaoCommonController.class);
	
	@Autowired
	private PaKakaoConnectUtil paKakaoConnectUtil;
	@Autowired
	private PaKakaoSettlementService paKakaoSettlementService;
	
	/**
	 * 주문단위 정산 조회 API
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "주문단위 정산 조회 API", notes = "구매결정된 주문 건들에 대해 주문번호 단위의 기간별 상세 정보를 제공하며, 구매결정일+1일 오전 9시부터 조회 가능", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/settlement-list", method = RequestMethod.GET)
	@ResponseBody
	public  ResponseEntity<ResponseMsg> settlementList(HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "기간시작일 [yyyy-MM-dd]", defaultValue = "") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate",   value = "기간종료일 [yyyy-MM-dd]", defaultValue = "") @RequestParam(value = "toDate",   required = false) String toDate
			) throws Exception {
		ParamMap apiInfoMap = new ParamMap();
		ParamMap errorMap = null;
		String paCode = "";
		String prg_id = "IF_PAKAKAOAPI_05_001"; 
		String url = "";
		Map<String, Object> map = new HashMap<String, Object>();
		PaKakaoSettlement paKakaoSettlement = null;
		List<PaKakaoSettlement> paKakaoSettlementList = new ArrayList<PaKakaoSettlement>(); 
		List<Map<String, Object>> settlementList = null;
		
		int page = 1; 									// 요청 페이지 번호. 기본값 1
		int size = 500; 								// 요청 페이지당 데이터 수. 기본값 500 최대 1000
		String periodType = "SETTLEMENT_CONFIRM_DATE";	// SETTLEMENT_BASE_DATE(정산기준일) / SETTLEMENT_CONFIRM_DATE(정산확정일)
		boolean lastYn = false; 						// 마지막 페이지 여부
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
		long startTime = System.currentTimeMillis();
		log.info("카카오 주문단위 정산 조회 start :" + simpleDateFormat.format(startTime));

		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			url = apiInfoMap.get("url").toString();
			fromDate = ComUtil.NVL(fromDate).length() == 10 ? fromDate : DateUtil.addDay(DateUtil.getCurrentDateWithHyphen(fromDate), -1, DateUtil.KAKAO_DATE_FORMAT); // 조회시작일
			toDate   = ComUtil.NVL(toDate).length()   == 10 ? toDate   : DateUtil.getCurrentDateWithHyphen(toDate); // 조회종료일
			
			for(int i = 0; i < Constants.PA_KAKAO_CONTRACT_CNT; i++) {
				
				paCode = (i == 0) ? Constants.PA_KAKAO_BROAD_CODE : Constants.PA_KAKAO_ONLINE_CODE;
				
				apiInfoMap.put("paCode", paCode);
				apiInfoMap.put("code", "200");
				apiInfoMap.put("message", "OK");
				apiInfoMap.put("paCode", paCode);
				page = 1;
				lastYn = false;
				
				while(!lastYn) {
					// 주문단위 정산 조회 파라미터 세팅
					apiInfoMap.put("url", url.replace("{page}", Integer.toString(page))
											 .replace("{size}", Integer.toString(size))
											 .replace("{periodType}", periodType)
											 .replace("{startDate}", fromDate)
											 .replace("{endDate}", toDate));
					
					// 카카오 주문단위 정산 조회 API 호출
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
					
					// 정상적으로 통신하지 못 하였을 때
					if(!"200".equals(ComUtil.objToStr(map.get("statusCode"))) || !"".equals(ComUtil.objToStr(map.get("code")))) {
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", errorMap.get("errorMsg").toString() + " ");
						break;
					}
					
					settlementList = (List<Map<String, Object>>)(map.get("contents"));
					
					if(null != settlementList) {
						for(Map<String, Object> settlementMap : settlementList) {
							try {
								paKakaoSettlement = new PaKakaoSettlement();
								
								// KakaoSettlementType, KakaoSettlementTermType, KakaoPayMainMethod enum파일 참고
								paKakaoSettlement.setPaCode(paCode);                                                                                            // 제휴사코드[B1, B2]
								paKakaoSettlement.setPaymentId(ComUtil.objToStr(settlementMap.get("paymentId").toString()));                                    // 결제번호
								paKakaoSettlement.setDeliveryAmountOriginId(ComUtil.objToStr(settlementMap.get("originDeliveryAmountId")));                     // 최초배송비번호
								paKakaoSettlement.setId(ComUtil.objToStr(settlementMap.get("orderId")));                                                        // 주문번호
								paKakaoSettlement.setSettleType( (ComUtil.objToStr(settlementMap.get("settlementType"))).contains("Item") ? "일반상품" : "배송비"); // 정산구분 - 전달주지 않아 settlementType으로 판단/카카오 직매입상품은 없다고 판단
								paKakaoSettlement.setStatus(KakaoSettlementType.getDescriptionByCode(ComUtil.objToStr(settlementMap.get("settlementType"))));   // 주문상태
								//paKakaoSettlement.setSettleState(ComUtil.objToStr(settlementList.get(i).get("settlementType")));                              // 정산확정상태 - 쿼리에서 settlementBaseDate로 판단 후 저장
								paKakaoSettlement.setSettlementCycle(KakaoSettlementTermType.getDescriptionByCode(ComUtil.objToStr(settlementMap.get("settlementTermType")))); // 정산주기
								paKakaoSettlement.setSettlementDate(ComUtil.objToStr(settlementMap.get("settlementBaseDate")));                                 // 정산기준일
								paKakaoSettlement.setSettleConfirmDate(ComUtil.objToStr(settlementMap.get("settlementConfirmDate")));                           // 정산확정일
								paKakaoSettlement.setChannel(ComUtil.objToStr(settlementMap.get("channelName")));                                               // 채널 [선물하기/톡스토어]
								paKakaoSettlement.setProductId(ComUtil.objToStr(settlementMap.get("productItemId")));                                           // 상품번호
								paKakaoSettlement.setGoodsName(ComUtil.objToStr(settlementMap.get("productName")));                                             // 상품명
								paKakaoSettlement.setSellerNo(ComUtil.objToStr(settlementMap.get("sellerId")));                                                 // 판매자번호
								paKakaoSettlement.setSellerName(ComUtil.objToStr(settlementMap.get("sellerName")));                                             // 판매자명
								paKakaoSettlement.setPaymentType(KakaoPayMainMethod.getDescriptionByCode(ComUtil.objToStr(settlementMap.get("payMainMethod"))));// 결제수단
								paKakaoSettlement.setProductPrice(ComUtil.objToDouble(settlementMap.get("productStandardAmount")));                             // 상품주문금액
								paKakaoSettlement.setTotalSellerDiscount(ComUtil.objToDouble(settlementMap.get("sellerTotalDiscountAmount")));                  // 판매자할인금액합계
								paKakaoSettlement.setSellerDiscount(ComUtil.objToDouble(settlementMap.get("productSellerDiscountAmount")));                     // 판매자즉시할인
								paKakaoSettlement.setShareDiscount(ComUtil.objToDouble(settlementMap.get("sellerDiscountSpreadAmount")));                       // 소문내기판매자할인
								paKakaoSettlement.setTalkdealDiscount(ComUtil.objToDouble(settlementMap.get("groupDiscountAmount")));                           // 톡딜할인
								paKakaoSettlement.setSellerDiscountCoupon(ComUtil.objToDouble(settlementMap.get("sellerCouponDiscountAmount")));                // 판매자할인쿠폰
								paKakaoSettlement.setAuthDiscount(ComUtil.objToDouble(settlementMap.get("digitalCardDiscountAmount")));                         // 인증할인
								paKakaoSettlement.setSellerCartDiscountCoupon(ComUtil.objToDouble(settlementMap.get("sellerCartCouponAmount")));                // 판매자장바구니할인쿠폰
								paKakaoSettlement.setSettlementFee(ComUtil.objToDouble(settlementMap.get("settlementBaseAmount")));                             // 정산기준금액
								paKakaoSettlement.setPrepaidFee(ComUtil.objToDouble(settlementMap.get("deliveryAmount")));                                      // 선불배송비
								paKakaoSettlement.setReturnPrepaidFee(ComUtil.objToDouble(settlementMap.get("returnDeliveryAmount")));                          // 반품배송비
								paKakaoSettlement.setQuantity(ComUtil.objToDouble(settlementMap.get("orderQuantity")));                                         // 수량
								paKakaoSettlement.setTotalFeePer(ComUtil.objToDouble(settlementMap.get("commissionTotalRate")));                                // 수수료율합계
								paKakaoSettlement.setTotalFee(ComUtil.objToDouble(settlementMap.get("commissionTotalAmount")));                                 // 수수료합계
								paKakaoSettlement.setSalesSettleAmount(ComUtil.objToDouble(settlementMap.get("settlementConfirmAmount")));                      // 판매정산금액
								paKakaoSettlement.setTotalKakaoDiscount(ComUtil.objToDouble(settlementMap.get("orderAdminTotalDiscountAmount")));               // 카카오할인금액합계

								paKakaoSettlementList.add(paKakaoSettlement); // 저장 List에 담기
							}catch (Exception e) {
								apiInfoMap.put("code", "500");
								apiInfoMap.put("message", "orderId :" + settlementMap.get("orderId").toString() +" "+PaKakaoComUtill.getErrorMessage(e)+"/");
								break;
							}
						}
						if(!apiInfoMap.get("code").equals("200")) break;
						
						lastYn = "true".equals(map.get("last").toString()) ? true : false;
						page++;
					}
				}		
			}
			
			if(apiInfoMap.get("code").equals("200") && paKakaoSettlementList.size() > 0) {
				log.info("TPAKAKAOSETTLEMENT 저장 START");
				paKakaoSettlementService.savePaKakaoSettlementTx(paKakaoSettlementList);
				log.info("TPAKAKAOSETTLEMENT 저장 END");
			} else {
				if(apiInfoMap.get("message").equals("OK")) {
					log.info("IF_PAKAKAOAPI_05_001-TPAKAKAOSETTLEMENT 저장할 데이터가 없음");
					apiInfoMap.put("message", "저장할 데이터가 없음");
				}
			}
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			long endTime = System.currentTimeMillis();
			double diffTime = (endTime - startTime)/1000.0;
			log.info("카카오 주문단위 정산 조회 실행시간 : " + diffTime + "(sec)");
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
}
