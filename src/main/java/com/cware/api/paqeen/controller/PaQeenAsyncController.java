package com.cware.api.paqeen.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.AbstractVO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.CancelInputVO;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.OrderInputVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.paqeen.repository.PaQeenDeliveryDAO;
import com.cware.netshopping.paqeen.service.PaQeenCancelService;
import com.cware.netshopping.paqeen.service.PaQeenClaimService;
import com.cware.netshopping.paqeen.service.PaQeenDeliveryService;
import com.cware.netshopping.paqeen.util.PaQeenApiRequest;
import com.cware.netshopping.paqeen.util.PaQeenComUtil;
import com.cware.netshopping.paqeen.util.PaQeenConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;

@Api(value="/paqeen/claim")
@Controller("com.cware.api.paqeen.PaQeenAsycController")
@RequestMapping(value="/paqeen/claim")
public class PaQeenAsyncController extends AbstractController{

	@Autowired
    private SystemService systemService;
	
	@Autowired
	private PaOrderService paOrderService;
	
	@Autowired
	private PaClaimService paClaimService;
	
	@Autowired
	private PaQeenApiRequest paQeenApiRequest;
	
	@Autowired
	private PaQeenConnectUtil paQeenConnectUtil;
	
	@Autowired
	private PaQeenDeliveryDAO paQeenDeliveryDAO;
	
	@Autowired
	@Qualifier("paqeen.claim.paQeenClaimService")
	private PaQeenClaimService paQeenClaimService;
	
	@Autowired
	@Qualifier("paqeen.delivery.paQeenDeliveryService")
	private PaQeenDeliveryService paQeenDeliveryService;
	
	@Autowired
	@Qualifier("paqeen.cancel.paQeenCancelService")
	private PaQeenCancelService paQeenCancelService;
	
	public void orderInputAsync(Map<String, String> order, HttpServletRequest request) throws Exception {
		
		List<Map<String, Object>>  orderInputTargetDtList = null;
		String paOrderNo  					= order.get("PA_ORDER_NO");
		OrderInputVO[] orderInputVO 		= null;
		HashMap<String, Object>[] resultMap = null;
		OrderInputVO vo						= new OrderInputVO();
		int	index 							= 0;
		ParamMap paramMap					= null;
		String isLocalYn					= "N";
		
		//로컬 세팅
		if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
			isLocalYn = "Y";
		}
		
		try {
			//=1) SELECT ORDER_DETAIL_INFOMATION
			paramMap = new ParamMap();
			paramMap.put("paOrderNo"		, paOrderNo);
			
			orderInputTargetDtList = paQeenDeliveryService.selectOrderInputTargetDtList(paramMap);
			if(orderInputTargetDtList == null || orderInputTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectOrderInputTargetDtList size 0" });
			
			//=2) ORDER_DATA SETTING
			orderInputVO = new OrderInputVO[orderInputTargetDtList.size()];
			PaQeenComUtil.replaceCamelList(orderInputTargetDtList);
			for(Map<String, Object> map : orderInputTargetDtList) {
				
				ObjectMapper objectMapper = new ObjectMapper();
				vo = objectMapper.convertValue(map, OrderInputVO.class);  
				
				//전화번호 
				String receiverTel = map.get("receiverTel").toString().replace("-", "");
				if(receiverTel.length() < 8) {
					orderInputVO[index] = vo;
					throw processException("pa.fail_order_input", new String[]{ "전화번호가 8자 미만입니다." });
				}
				
				//가격비교
				String paApplyDate   = map.get("applyDate").toString(); // TPAGOODSPRICEAPPLY 기준 APPLY_DATE
				String stoaApplyDate = map.get("stoaApplyDate").toString(); // TGOODSPRICE 기준 APPLY_DATE
				String paOrderDate   = map.get("orderDate").toString(); // 퀸잇 주문일시
				
				if(!paApplyDate.equals(stoaApplyDate)) {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					Date orderDate = format.parse(paOrderDate.substring(0, 8));
			        Date stoaDate = format.parse(stoaApplyDate.substring(0, 8));
			        
			        // 스토아 최신 가격 적용일자와 제휴 주문일자 차이 계산
			        long diffDays = orderDate.getTime() - stoaDate.getTime();
			        diffDays = diffDays / (24 * 60 * 60 * 1000);
			        
					if(diffDays >= 3) { // 3일 이상 차이나는 경우
						orderInputVO[index] = vo;
						throw processException("pa.fail_order_input", new String[]{ "가격 정보가 잘못되었습니다." });
					}
				}
				
				if(vo.getDoAmt() > 0) {
					OrderpromoVO orderPaPromo = new OrderpromoVO();
					orderPaPromo.setPromoNo(vo.getPromoNo());
					orderPaPromo.setDoType("30");
					orderPaPromo.setDoAmt(vo.getDoAmt());
					orderPaPromo.setProcCost(vo.getDoAmt());
					orderPaPromo.setOwnProcCost(vo.getOwnCost());
					orderPaPromo.setEntpProcCost(vo.getEntpCost());
					orderPaPromo.setCouponPromoBdate(vo.getCouponPromoBdate());
					orderPaPromo.setCouponPromoEdate(vo.getCouponPromoEdate());
					orderPaPromo.setCouponYn("1");
					orderPaPromo.setProcGb("I");
					
					vo.setOrderPaPromo(orderPaPromo);
				}
				
				vo.setIsLocalYn	(isLocalYn);
				vo.setProcUser  (Constants.PA_QEEN_PROC_ID);
				
				//=ADDR SETTING
				vo.setStdAddr     	(map.get("receiverAddr").toString());
				vo.setStdAddrDT   	("");
				orderInputVO[index] = vo;
				index++;
			}
			
			//=3) BO 데이터 생성
			resultMap = paOrderService.saveOrderTx(orderInputVO);
			
		}catch (Exception e) {
			updatePaOrdermTxForRollback(orderInputVO, e);
		
			paramMap = new ParamMap();
			paramMap.put("apiCode"	, "PAQEEN_ORDER_INPUT_ASYNC");
			paramMap.put("message"	, "pa_order_no : " + paOrderNo + " > " + e.getMessage());
		
		}finally {
			saveApiTracking(paramMap, request);
		}
		//재고 부족으로 인한 주문 거절(판매자 주문 취소)
		refusalOrder(request, resultMap);
	}
	
	private void updatePaOrdermTxForRollback(AbstractVO[] aVO, Exception e){
		
		if(aVO == null || aVO.length < 1 || aVO[0] == null)  return;
		
		ParamMap paramMap 	= null;
		int excuteCnt 		= 0;
		
		for(int j = 0; aVO.length > j; j++){
			paramMap = new ParamMap();
			
			if(aVO instanceof OrderInputVO[]){
				OrderInputVO[] orderInput = (OrderInputVO[]) aVO;
				paramMap.put("mappingSeq", orderInput[j].getMappingSeq());
			}else if(aVO instanceof OrderClaimVO[]){
				OrderClaimVO[] orderClaim = (OrderClaimVO[]) aVO;
				paramMap.put("mappingSeq", orderClaim[j].getMappingSeq());
			}else if(aVO instanceof CancelInputVO[]){
				CancelInputVO[] orderCancel = (CancelInputVO[]) aVO;
				paramMap.put("mappingSeq", orderCancel[j].getMappingSeq());
			}
			
			String errMsg = e.getMessage();
			paramMap.put("resultCode"		, "999999");
			paramMap.put("resultMessage"	, errMsg.length() > 1950 ? errMsg.substring(0,1950) : errMsg);
			paramMap.put("createYn"			, "0");
			
			try {
				excuteCnt = paOrderService.updatePaOrdermTx(paramMap);
				if(excuteCnt != 1){
					log.error(paramMap.getString("mappingSeq") + " Update ERROR - Result Code 999999 in TPAORDERM");
				}
			} catch (Exception e1) {
				log.error(paramMap.getString("mappingSeq") + " Update ERROR - Result Code 999999 in TPAORDERM");
				continue;
			}
		}//end of for
	}
	
	private void saveApiTracking(ParamMap paramMap , HttpServletRequest request) {
		if(paramMap == null) return;
		if(paramMap.getString("apiCode").equals("")) return;
		
		try{
			paramMap.put("startDate"	, systemService.getSysdatetimeToString());
			paramMap.put("code"			, "500");
			paramMap.put("siteGb"		, Constants.PA_QEEN_PROC_ID);
			systemService.insertApiTrackingTx(request, paramMap);
			
		}catch(Exception ee){
			log.error("ApiTracking Insert Error : "+ee.toString());
		}
	}
	
	// 품절취소 주문 등록 API 
	private void refusalOrder(HttpServletRequest request, HashMap<String, Object>[] resultMap) throws Exception {
		if(resultMap == null) return;
		
		String apiCode = "IF_PAQEENAPI_03_007";
		String duplicateCheck = "";
		String preCancelYn = "0";
		String resultMsg = null;
		String resultCode = null;
		
		ParamMap paramMap = new ParamMap();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> refusalInfoList = new ArrayList<HashMap<String,Object>>();
		List<HashMap<String, Object>> itemList = null;
		
		try {
			
			itemList = new ArrayList<HashMap<String,Object>>();
			itemList = Arrays.stream(resultMap).filter(result -> "100001".equals(result.get("RESULT_CODE")))
					.collect(Collectors.toList());
			
			if (itemList.size() == 0) {
				return;
			}
			log.info("======= 퀸잇 품절주문 등록  API Start - {} =======");
			log.info("01.API BaseInfo Setting");
			
			paramMap.put("apiCode", apiCode);
			log.info("02.API Duplicate Check");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			
			paramMap.put("duplicateCheck", duplicateCheck);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			for(HashMap<String ,Object> map :  resultMap) {
				if( !("100001").equals(map.get("RESULT_CODE"))) continue; //100001 - 재고없음
				HashMap<String, Object> refusalMap = paQeenDeliveryService.selectRefusalInfo(map.get("MAPPING_SEQ").toString());
				refusalInfoList.add(refusalMap);
			}
			
			for(Map<String, Object> refusalInfo : refusalInfoList) {

				HashMap<String, Object> apiRequestObject = new HashMap<String, Object>();
				List<HashMap<String, Object>> partials = new ArrayList<HashMap<String,Object>>();
				HashMap<String, Object> partial = new HashMap<String, Object>();
			
				paramMap.put("paCode", refusalInfo.get("PA_CODE"));
				paQeenApiRequest.getApiInfo(paramMap);
				
				log.info("03.API Request Setting");
				partial.put("orderItemId",  refusalInfo.get("ORDER_LINE_ID"));
				partial.put("quantity", refusalInfo.get("PA_PROC_QTY"));
				
				partials.add(partial);
				
				apiRequestObject.put("channel", "etc");
				apiRequestObject.put("reason", refusalInfo.get("REASON_DETAIL").toString());
				apiRequestObject.put("isCustomerNegligence", 0); 
				apiRequestObject.put("partials", partials);
				
				paramMap.put("urlParameter", refusalInfo.get("ORDER_ID").toString());
				
				log.info("04.API Call");
				try {
					responseMap = paQeenConnectUtil.callPaQeenAPILegacy(paramMap, apiRequestObject, null);	
				}catch (Exception e) {
					if(!"200".equals(paramMap.get("resultCode"))) {
					preCancelYn = "0";
					resultMsg = e.getMessage();
					log.info("API returnCode : " + paramMap.get("resultCode") + " returnMsg : " + resultMsg);
					}
				}	
				
				
				if (responseMap != null) {
					resultCode = paramMap.getString("resultCode");
					log.info("04.Processing");
						if("200".equals(resultCode)) {
							preCancelYn = "1";
							resultMsg = "재고부족 or 판매불가 상태 따른 판매자 취소처리";
						}
						
						HashMap<String, Object> m = new HashMap<String,Object>();
						
						m.put("resultMessage"	, resultMsg);
						m.put("resultCode"		, resultCode);
						m.put("preCancelYn"		, preCancelYn);
						m.put("mappingSeq"		, refusalInfo.get("MAPPING_SEQ"));
						
						try {
							paQeenDeliveryDAO.updatePaorderm(m); 
						} catch (Exception e2) {
							log.error("Exception occurs : " + e2.getMessage());
						}
				}
			}
		}catch (Exception e) {
			log.error(e.getMessage());
		}finally {
			paQeenConnectUtil.closeApi(request, paramMap);
			log.info("======= 퀸잇 품절주문 등록  API End - {} =======");
		}
	}

	public void cancelInputAsync(HashMap<String, Object> cancelInputTarget, HttpServletRequest request) {
		ParamMap paramMap = null;
		int executedRtn = 0;
		
		try {
			paramMap = new ParamMap();
			paramMap.setParamMap(cancelInputTarget);
			paramMap.replaceCamel();
			
			HashMap<String, Object> cancelDtInfo = paQeenCancelService.selectCancelInputTargetDtList(paramMap);
			
			if(cancelDtInfo == null) throw processException("msg.no.select", new String[] { "selectCancelInputTargetDtList" });
			
			String preCancelYn = cancelDtInfo.get("PRE_CANCEL_YN").toString();
			
			switch (preCancelYn) {
			case "0" :
				CancelInputVO cancelInputVO = new CancelInputVO();
				cancelInputVO.setMappingSeq (String.valueOf(cancelDtInfo.get("MAPPING_SEQ")));
				cancelInputVO.setOrderNo    (String.valueOf(cancelDtInfo.get("ORDER_NO")));
				cancelInputVO.setOrderGSeq  (String.valueOf(cancelDtInfo.get("ORDER_G_SEQ")));
				cancelInputVO.setCancelQty  (Integer.parseInt(String.valueOf(cancelDtInfo.get("PA_PROC_QTY"))));
				cancelInputVO.setCancelCode (String.valueOf(cancelDtInfo.get("CANCEL_CODE")));
				cancelInputVO.setProcId	    (Constants.PA_QEEN_PROC_ID);
				
				try {
					paOrderService.saveCancelTx(cancelInputVO);
				} catch (Exception e) {
					CancelInputVO[] cancelInputArray = new CancelInputVO[1];
					cancelInputArray[0] = cancelInputVO;
					updatePaOrdermTxForRollback(cancelInputArray, e);
				}
				break;
				
			default : 
				
				Map<String, Object> preCancelMap = new HashMap<String, Object>();
				preCancelMap.put("preCanYn"			, "1");
				preCancelMap.put("MAPPING_SEQ"		, String.valueOf(cancelDtInfo.get("MAPPING_SEQ")));
				preCancelMap.put("apiResultMessage"	, getMessage("pa.before_order_create_cancel"));
				paQeenDeliveryService.updatePreCanYn(preCancelMap);
				
				if(executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
				}
				break;
			}
		} catch(Exception e) {
			paramMap.put("apiCode", "PAQEEN_CANCEL_INPUT_A");
			paramMap.put("message", "pa_order_no : " + paramMap.getString("paOrderNo") + " > " + e);
		} finally {
			saveApiTracking(paramMap, request);
		}
	}

	public void changeClaimAsync(HashMap<String, Object> claim, HttpServletRequest request) {
		
		
		HashMap<String, Object>[] resultMap    = null;
		List<Object>   orderChangeTargetDtList = null;
		OrderClaimVO[] orderClaimVOArray 	   = null;
		OrderClaimVO   orderClaimVO 		   = null;
		ParamMap 	   paramMap 			   = new ParamMap();
		String 		   paOrderNo = claim.get("PA_ORDER_NO").toString();
		int 		   index = 0;
		int 		   targetSize = 0;
		paramMap.setParamMap(claim);
		paramMap.replaceCamel();
		String isLocalYn = "N";
		
		try {
			orderChangeTargetDtList = paQeenClaimService.selectOrderChangeTargetDtList(paramMap);
			targetSize = orderChangeTargetDtList.size();
			if(targetSize < 1) throw processException("msg.no.select", new String[] { "selectOrderChangeTargetDtList" });
			
			//로컬확인
			if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				isLocalYn = "Y";
			} 

			orderClaimVOArray = new OrderClaimVO[orderChangeTargetDtList.size()];
			
			for(Object hm : orderChangeTargetDtList) {
				orderClaimVO = new OrderClaimVO();
				orderClaimVO = setOrderClaimVO((HashMap<String, Object>) hm,isLocalYn);
				orderClaimVOArray[index] = orderClaimVO;
				index++;
			}
							
			resultMap = paClaimService.saveOrderChangeTx(orderClaimVOArray);
			paramMap.put("code", "200");
			paramMap.put("message", "SUCCESS");
			
			for(int j = 0; targetSize > j; j++) {
				paramMap = new ParamMap();
				paramMap.setParamMap(resultMap[j]);
				paramMap.replaceCamel();
				
				// 재고부족으로 인한 교환 불가
				if(paramMap.getString("resultCode").equals("100001")) {
					for(OrderClaimVO claimVO : orderClaimVOArray) {
						paQeenClaimService.updatePaOrdermChangeFlag("06", claimVO.getMappingSeq());
						//퀸잇은 아웃바운드 처리로 교환 취소 할 수 밖에 없다.
					}
				}
			}
		} catch(Exception e) {
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + e;
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PAQEEN_ORDER_CHANGE_A");
			//2) UPDATE TPAORDERM
			updatePaOrdermTxForRollback(orderClaimVOArray, e);
		} finally {
			saveApiTracking(paramMap, request);
		}
	}

	private OrderClaimVO setOrderClaimVO(HashMap<String, Object> hmSheet, String isLocalYn)  throws Exception {
		OrderClaimVO orderClaimVO = new OrderClaimVO();
		
		orderClaimVO.setMappingSeq			(hmSheet.get("MAPPING_SEQ").toString());
		orderClaimVO.setOrderNo				(hmSheet.get("ORDER_NO").toString());
		orderClaimVO.setOrderGSeq			(hmSheet.get("ORDER_G_SEQ").toString());
		orderClaimVO.setOrderDSeq			(ComUtil.NVL(hmSheet.get("ORDER_D_SEQ")).toString());
		orderClaimVO.setOrderWSeq			(ComUtil.NVL(hmSheet.get("ORDER_W_SEQ")).toString());
		orderClaimVO.setClaimQty			(Integer.parseInt(String.valueOf(hmSheet.get("PA_PROC_QTY"))));
		orderClaimVO.setClaimDesc			(ComUtil.NVL(hmSheet.get("CLAIM_DESC")).toString());
		orderClaimVO.setClaimGb				(hmSheet.get("PA_ORDER_GB").toString());
		orderClaimVO.setCustDelyYn			("0"); //= 고객 직접발송일 경우 : 1, 아닐경우 : 0
		orderClaimVO.setReturnDelyGb		("");//직접배송 못하게 막아야함..
		orderClaimVO.setReturnSlipNo		("");
		orderClaimVO.setOutBefClaimGb		(ComUtil.NVL(hmSheet.get("OUT_BEF_CLAIM_GB")).toString());
		orderClaimVO.setLocalYn				(isLocalYn);
		orderClaimVO.setInsertId			(hmSheet.get("SITE_GB").toString());
		

		ParamMap paramMap = new ParamMap();
		String checkAddr = "";
		
		switch(hmSheet.get("PA_ORDER_GB").toString()){
		
		case "30":
			orderClaimVO.setReturnName		(hmSheet.get("RETURN_NAME").toString());
			orderClaimVO.setReturnTel		(hmSheet.get("RETURN_TEL").toString().replace("-", ""));
			orderClaimVO.setReturnHp		(hmSheet.get("RETURN_HP").toString().replace("-", ""));
			orderClaimVO.setReturnAddr		(hmSheet.get("RETURN_ADDR").toString());
			orderClaimVO.setRcvrMailNoSeq	("001");
			orderClaimVO.setRcvrMailNo		(hmSheet.get("RCVR_MAIL_NO").toString());
			orderClaimVO.setRcvrBaseAddr	(hmSheet.get("RCVR_BASE_ADDR").toString());
			orderClaimVO.setRcvrDtlsAddr	(hmSheet.get("RCVR_DTLS_ADDR").toString());
			if(hmSheet.get("RCVR_MAIL_NO").toString().replace("-", "").trim().length() == 6){
				orderClaimVO.setRcvrTypeAdd	("01"); //지번
			}else{
				orderClaimVO.setRcvrTypeAdd	("02"); //도로명
			}
			//주소지 변경 체크
			paramMap.put("PA_ORDER_NO", hmSheet.get("PA_ORDER_NO").toString());
			paramMap.put("PA_ORDER_SEQ", hmSheet.get("PA_ORDER_SEQ").toString());
			paramMap.put("PA_CLAIM_NO", hmSheet.get("PA_CLAIM_NO").toString());
			paramMap.put("PA_ORDER_GB", "30");
			paramMap.put("claimGb", hmSheet.get("PA_ORDER_GB").toString());
			checkAddr = paQeenClaimService.compareAddress(paramMap);
			orderClaimVO.setSameAddr(checkAddr);
			 
			break;
		
		case "31": case "41": case"46":
			orderClaimVO.setReturnName		(hmSheet.get("RETURN_NAME").toString());
			orderClaimVO.setReturnTel		(hmSheet.get("RETURN_TEL").toString().replace("-", ""));
			orderClaimVO.setReturnHp		(hmSheet.get("RETURN_HP").toString().replace("-", ""));
			orderClaimVO.setReturnAddr		(hmSheet.get("RETURN_ADDR").toString());
			orderClaimVO.setRcvrMailNo		("");
			orderClaimVO.setRcvrMailNoSeq   ("");
			orderClaimVO.setRcvrBaseAddr	("");
			orderClaimVO.setRcvrDtlsAddr	("");
			orderClaimVO.setRcvrTypeAdd		("");
			break;
		
		case "40": //교환 발송
			orderClaimVO.setExchGoodsdtCode	(hmSheet.get("EXCH_GOODSDT_CODE").toString());
			orderClaimVO.setAdminProcYn		(hmSheet.get("ADMIN_PROC_YN").toString());
			orderClaimVO.setReturnName		(hmSheet.get("RECEIVER_NAME").toString());
			orderClaimVO.setReturnTel		(hmSheet.get("RECEIVER_TEL").toString().replace("-", ""));
			orderClaimVO.setReturnHp		(hmSheet.get("RECEIVER_HP").toString().replace("-", ""));
			orderClaimVO.setReturnAddr		(hmSheet.get("RECEIVER_ADDR").toString());
			orderClaimVO.setRcvrMailNoSeq	("001");
			orderClaimVO.setRcvrMailNo		(hmSheet.get("RECEIVER_ZIPCODE").toString());
			orderClaimVO.setRcvrBaseAddr	(hmSheet.get("RECEIVER_ADDR1").toString());
			orderClaimVO.setRcvrDtlsAddr	(hmSheet.get("RECEIVER_ADDR2").toString());
			if(hmSheet.get("RECEIVER_ZIPCODE").toString().replace("-", "").trim().length() == 6){
				orderClaimVO.setRcvrTypeAdd	("01"); //지번
			}else{
				orderClaimVO.setRcvrTypeAdd	("02"); //도로명
			}
			//주소지 변경 체크
			paramMap.put("PA_ORDER_NO", hmSheet.get("PA_ORDER_NO").toString());
			paramMap.put("PA_ORDER_SEQ", hmSheet.get("PA_ORDER_SEQ").toString());
			paramMap.put("PA_CLAIM_NO", hmSheet.get("PA_CLAIM_NO").toString());
			paramMap.put("PA_ORDER_GB", "40");
			paramMap.put("claimGb", hmSheet.get("PA_ORDER_GB").toString());
			checkAddr = paQeenClaimService.compareAddress(paramMap);
			orderClaimVO.setSameAddr(checkAddr);
			
			break;

		case "45": //교환 회수
			orderClaimVO.setExchGoodsdtCode	(hmSheet.get("EXCH_GOODSDT_CODE").toString());
			orderClaimVO.setAdminProcYn		(hmSheet.get("ADMIN_PROC_YN").toString());
			orderClaimVO.setReturnName		(hmSheet.get("RETURN_NAME").toString());
			orderClaimVO.setReturnTel		(hmSheet.get("RETURN_TEL").toString().replace("-", ""));
			orderClaimVO.setReturnHp		(hmSheet.get("RETURN_HP").toString().replace("-", ""));
			orderClaimVO.setReturnAddr		(hmSheet.get("RETURN_ADDR").toString());
			orderClaimVO.setRcvrMailNoSeq	("001");
			orderClaimVO.setRcvrMailNo		(hmSheet.get("RETURN_ZIPCODE").toString());
			orderClaimVO.setRcvrBaseAddr	(hmSheet.get("RETURN_ADDR1").toString());
			orderClaimVO.setRcvrDtlsAddr	(hmSheet.get("RETURN_ADDR2").toString());
			if(hmSheet.get("RETURN_ZIPCODE").toString().replace("-", "").trim().length() == 6){
				orderClaimVO.setRcvrTypeAdd	("01"); //지번
			}else{
				orderClaimVO.setRcvrTypeAdd	("02"); //도로명
			}
			//주소지 변경 체크
			paramMap.put("PA_ORDER_NO", hmSheet.get("PA_ORDER_NO").toString());
			paramMap.put("PA_ORDER_SEQ", hmSheet.get("PA_ORDER_SEQ").toString());
			paramMap.put("PA_CLAIM_NO", hmSheet.get("PA_CLAIM_NO").toString());
			paramMap.put("PA_ORDER_GB", "40");
			paramMap.put("claimGb", hmSheet.get("PA_ORDER_GB").toString());
			checkAddr = paQeenClaimService.compareAddress(paramMap);
			orderClaimVO.setSameAddr(checkAddr);
			
			break;
		}
				
		String claimCode = hmSheet.get("CLAIM_CODE").toString();
		
		if (claimCode.length() ==6){
			orderClaimVO.setCsLgroup		(claimCode.substring(0,2));
			orderClaimVO.setCsMgroup		(claimCode.substring(2,4));
			orderClaimVO.setCsSgroup		(claimCode.substring(4,6));
			orderClaimVO.setCsLmsCode		(claimCode);		
			orderClaimVO.setStandardType	("0");     //// 기준내 :1 ,기준외 : 0
		}
		orderClaimVO.setClaimCode(claimCode);
		
		
		Long shpFeeAmt  = Math.abs(Long.parseLong(String.valueOf(hmSheet.get("CLM_LST_DLV_CST"))));
		orderClaimVO.setShpFeeAmt			(shpFeeAmt);
		
		//귀책자에 따른 shpfeeYn 결정
		String reason = hmSheet.get("CUSTOMER_NEGLIGENCE") != null  ? String.valueOf(hmSheet.get("CUSTOMER_NEGLIGENCE")) : String.valueOf(hmSheet.get("IS_CUSTOMER_NEGLIGENCE"));
		
		if("1".equals(reason)) {
			orderClaimVO.setShpfeeYn		("1"); //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
			orderClaimVO.setShipcostChargeYn("1");
		} else {
			orderClaimVO.setShpfeeYn		("0");	
			orderClaimVO.setShipcostChargeYn("0");
		}
		
		//출하지시 이후 취소건은 반품인데, 이경우 배송비 부과여부를 무상으로 처리한다.
        //if(("1").equals(orderClaimVO.getOutBefClaimGb()) && "20".equals(hmSheet.get("PA_ORDER_GB").toString())){ //** 티딜측에서 취소 철회만들어주면 주석 빼고 바로 아랫줄 삭제 
		if(("1").equals(orderClaimVO.getOutBefClaimGb())){
			orderClaimVO.setShpfeeYn		("0");
			orderClaimVO.setIs20Claim		(true);
		}else{
			orderClaimVO.setIs20Claim		(false);
		}
		
		return orderClaimVO;
	}

	public void claimCancelAsync(HashMap<String, Object> cancelInputTarget, HttpServletRequest request) {
		HashMap<String, Object> hmSheet 	 = null;
		List<Object> cancelInputTargetDtList = null;
		ParamMap paramMap 					 = new ParamMap();
		OrderClaimVO orderClaimVO 			 = null;
		String paOrderNo					 = cancelInputTarget.get("PA_ORDER_NO").toString();
		paramMap.setParamMap(cancelInputTarget);
		paramMap.replaceCamel();		
		OrderClaimVO[] orderClaimVOArray = null;
		int index = 0;
		
		//로컬 세팅
		String isLocalYn = "N";
		if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
				isLocalYn = "Y";
		}
				
		try {
			
			if(cancelInputTarget.get("PA_ORDER_GB").equals("31")) { // 반품철회
				cancelInputTargetDtList = paQeenClaimService.selectClaimCancelTargetDtList(paramMap); 
				if(cancelInputTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectRetrunCancelTargetDtList" });
				hmSheet = (HashMap<String, Object>) cancelInputTargetDtList.get(0);
				
				orderClaimVO = setOrderClaimVO(hmSheet, isLocalYn);
				paClaimService.saveClaimCancelTx(orderClaimVO);	
			}else { // 교환 철회
				cancelInputTargetDtList = paQeenClaimService.selectChangeCancelTargetDtList(paramMap);
				if(cancelInputTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectChangeCancelTargetDtList" });
				orderClaimVOArray = new OrderClaimVO[cancelInputTargetDtList.size()];
				
				for(Object hm : cancelInputTargetDtList) {
					hmSheet = (HashMap<String, Object>)hm;
					orderClaimVO = new OrderClaimVO();
					orderClaimVO = setOrderClaimVO(hmSheet, isLocalYn);
					orderClaimVOArray[index] = orderClaimVO;
					index++;
				}
				paClaimService.saveChangeCancelTx(orderClaimVOArray);
			}
			
		}catch(Exception e){
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " +e;
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("apiCode"  , "PAQEEN_CLAIM_CANCEL_A");
			paramMap.put("message" 	, message);
			
			//2) TPAORDERM Update
			OrderClaimVO[] orderClaimList = new OrderClaimVO[1];
			orderClaimList[0] = orderClaimVO;
			updatePaOrdermTxForRollback(orderClaimList, e);
		}finally{
			saveApiTracking(paramMap, request);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void returnClaimAsync(HashMap<String, Object> claimMap, HttpServletRequest request) {
		List<Object> orderClaimTargetDtList = null;
		ParamMap paramMap 					= new ParamMap();
		OrderClaimVO orderClaimVO 			= null;
		orderClaimVO 						= new OrderClaimVO();
		String paOrderNo					= claimMap.get("PA_ORDER_NO").toString();
		paramMap.setParamMap(claimMap);
		paramMap.replaceCamel();
		
		//로컬 세팅
		String isLocalYn					= "N";
		if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
			isLocalYn = "Y";
		}
		
		try {
			//1) 반품 타겟  추출
			if(paramMap.getString("paOrderGb").equals("30")) {
				orderClaimTargetDtList = paQeenClaimService.selectOrderCalimTargetDt30List(paramMap); 
			} else {
				orderClaimTargetDtList = paQeenClaimService.selectOrderCalimTargetDt20List(paramMap);
			}
			if(orderClaimTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectOrderCalimTargetDt30(20)List" });
			
			//2) 반품 VO 데이터 생성
			orderClaimVO = setOrderClaimVO((HashMap<String, Object>)orderClaimTargetDtList.get(0), isLocalYn);
			paClaimService.saveOrderClaimTx(orderClaimVO);
		
		}catch(Exception e) {
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + e;
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PAQEEN_ORDER_CLAIM_A");
			
			//2) TPAORDERM Update
			OrderClaimVO[] orderClaimList = new OrderClaimVO[1];
			orderClaimList[0] 			  = orderClaimVO;
			updatePaOrdermTxForRollback(orderClaimList, e);
			
		}finally {
			saveApiTracking(paramMap, request);
		}
		
	}
}
