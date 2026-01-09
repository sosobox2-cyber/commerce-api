package com.cware.api.pakakao.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.AbstractVO;
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
import com.cware.netshopping.domain.PaKakaoGoodsVO;
import com.cware.netshopping.domain.PaKakaoTalkDealVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoGoodsPrice;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pakakao.claim.service.PaKakaoClaimService;
import com.cware.netshopping.pakakao.delivery.service.PaKakaoDeliveryService;
import com.cware.netshopping.pakakao.goods.service.PaKakaoGoodsService;
import com.cware.netshopping.pakakao.util.PaKakaoComUtill;
import com.cware.netshopping.pakakao.util.PaKakaoConnectUtil;

import io.swagger.annotations.Api;

@Api(value="/pakakao/async")
@Controller("com.cware.api.pakakao.PaKakaoAsycController")
@RequestMapping(value="/pakakao/async")
public class PaKakaoAsyncController extends AbstractController{
	@Autowired
	PaKakaoClaimService paKakaoClaimService;
	@Autowired
	PaOrderService paorderService;
	@Autowired
	PaKakaoDeliveryService paKakaoDeliveryService;
	@Autowired
	SystemService systemService;
	@Autowired
	PaClaimService paclaimService;
	@Autowired
	PaKakaoConnectUtil paKakaoConnectUtil;
	@Autowired
	private PaKakaoGoodsService paKakaoGoodsService;
	@Autowired
	PaCommonService paCommonService;
	@Autowired
	PaCommonDAO paCommonDAO;
	
	public void cancelInputAsync(HashMap<String, Object> cancelTarget, HttpServletRequest request) {
		ParamMap paramMap = null;
		int executedRtn = 0;
		
		try {
			paramMap = new ParamMap();
			paramMap.setParamMap(cancelTarget);
			paramMap.replaceCamel();
			
			HashMap<String, Object> cancelDtInfo = paKakaoClaimService.selectCancelInputTargetDtList(paramMap);
			
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
				cancelInputVO.setProcId	    (Constants.PA_KAKAO_PROC_ID);
				
				try {
					paorderService.saveCancelTx(cancelInputVO);
				} catch (Exception e) {
					CancelInputVO[] cancelInputArray = new CancelInputVO[1];
					cancelInputArray[0] = cancelInputVO;
					updatePaOrdermTxForRollback(cancelInputArray, e);
				}
				break;
				
			default : //기취소
				
				Map<String, Object> preCancelMap = new HashMap<String, Object>();
				preCancelMap.put("preCanYn"			, "1");
				preCancelMap.put("MAPPING_SEQ"		, String.valueOf(cancelDtInfo.get("MAPPING_SEQ")));
				preCancelMap.put("apiResultMessage"	, getMessage("pa.before_order_create_cancel"));
				paKakaoDeliveryService.updatePreCanYn(preCancelMap);
				
				if(executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
				}
				break;
			}
		} catch(Exception e) {
			paramMap.put("apiCode", "PAKAKAO_CANCEL_INPUT_A");
			paramMap.put("message", "pa_order_no : " + paramMap.getString("paOrderNo") + " > " + PaKakaoComUtill.getErrorMessage(e));
		} finally {
			saveApiTracking(paramMap, request);
		}
	}
	
	/**
	 * 카카오 반품 데이터 생성
	 * @param claim
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public void orderClaimAsync(HashMap<String, Object> claimMap, HttpServletRequest request) {
		List<Object> orderClaimTargetDtList = null;
		ParamMap paramMap 					= new ParamMap();
		OrderClaimVO orderClaimVO 			= null;
		orderClaimVO 						= new OrderClaimVO();
		String paOrderNo					= claimMap.get("PA_ORDER_NO").toString();
		paramMap.setParamMap(claimMap);
		paramMap.replaceCamel();
		
		try {
			//1) 반품 타겟  추출
			if(paramMap.getString("paOrderGb").equals("30")) {
				orderClaimTargetDtList = paKakaoClaimService.selectOrderCalimTargetDt30List(paramMap); 
			} else {
				orderClaimTargetDtList = paKakaoClaimService.selectOrderCalimTargetDt20List(paramMap); //출고전 반품
			}
			if(orderClaimTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectOrderCalimTargetDt30(20)List" });
			
			//2) 반품 VO 데이터 생성
			orderClaimVO = setOrderClaimVO((HashMap<String, Object>)orderClaimTargetDtList.get(0));
			paclaimService.saveOrderClaimTx(orderClaimVO);
		
		}catch(Exception e) {
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + PaKakaoComUtill.getErrorMessage(e);
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PAKAKAO_ORDER_CLAIM_A");
			
			//2) TPAORDERM Update
			OrderClaimVO[] orderClaimList = new OrderClaimVO[1];
			orderClaimList[0] 			  = orderClaimVO;
			updatePaOrdermTxForRollback(orderClaimList, e);
			
		}finally {
			saveApiTracking(paramMap, request);
		}
		
	}
	
	/**
	 * 카카오 반품 취소 데이터 생성
	 * @param claim
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public void claimCancelAsync(HashMap<String, Object> claimMap, HttpServletRequest request) {
		HashMap<String, Object> hmSheet 	 = null;
		List<Object> cancelInputTargetDtList = null;
		ParamMap paramMap 					 = new ParamMap();
		OrderClaimVO orderClaimVO 			 = null;
		String paOrderNo					 = claimMap.get("PA_ORDER_NO").toString();
		paramMap.setParamMap(claimMap);
		paramMap.replaceCamel();		
		
		try {
			
			cancelInputTargetDtList = paKakaoClaimService.selectClaimCancelTargetDtList(paramMap); 
			if(cancelInputTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectClaimCancelTargetDtList" });
			
			hmSheet = (HashMap<String, Object>) cancelInputTargetDtList.get(0);
			
			orderClaimVO = setOrderClaimVO(hmSheet);
			paclaimService.saveClaimCancelTx(orderClaimVO);
			
		}catch(Exception e){
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + PaKakaoComUtill.getErrorMessage(e);
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("apiCode"  , "PAKAKAO_CLAIM_CANCEL_A");
			paramMap.put("message" 	, message);
			
			//2) TPAORDERM Update
			OrderClaimVO[] orderClaimList = new OrderClaimVO[1];
			orderClaimList[0] = orderClaimVO;
			updatePaOrdermTxForRollback(orderClaimList, e);
		}finally{
			saveApiTracking(paramMap, request);
		}
	}
	
	/**
	 * 카카오 교환 데이터 생성
	 * @param claim
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public void orderChangeAsync(HashMap<String, Object> claim, HttpServletRequest request) {
		HashMap<String, Object>[] resultMap    = null;
		List<Object>   orderChangeTargetDtList = null;
		OrderClaimVO[] orderClaimVOArray 	   = null;
		OrderClaimVO   orderClaimVO 		   = null;
		ParamMap 	   paramMap 			   = new ParamMap();
		String 		   paOrderNo = claim.get("PA_ORDER_NO").toString();
		String		   message				   = "";
		int 		   index 				   = 0;
		int 		   targetSize 			   = 0;
		paramMap.setParamMap(claim);
		paramMap.replaceCamel();
		
		try {
			orderChangeTargetDtList = paKakaoClaimService.selectOrderChangeTargetDtList(paramMap);
			targetSize = orderChangeTargetDtList.size();
			if(targetSize < 1) throw processException("msg.no.select", new String[] { "selectOrderChangeTargetDtList" });
			
			orderClaimVOArray = new OrderClaimVO[orderChangeTargetDtList.size()];
			
			for(Object hm : orderChangeTargetDtList) {
				orderClaimVO = new OrderClaimVO();
				orderClaimVO = setOrderClaimVO((HashMap<String, Object>) hm);
				orderClaimVOArray[index] = orderClaimVO;
				index++;
			}
			
			resultMap = paclaimService.saveOrderChangeTx(orderClaimVOArray);
			paramMap.put("code", "200");
			paramMap.put("message", "SUCCESS");
			
			for(int j = 0; targetSize > j; j++) {
				paramMap = new ParamMap();
				paramMap.setParamMap(resultMap[j]);
				paramMap.replaceCamel();
				
				// 재고부족으로 인한 교환 불가
				if(paramMap.getString("resultCode").equals("100001")) {
					OrderClaimVO claimVO = orderClaimVOArray[j];
					paKakaoClaimService.updatePaOrdermChangeFlag("03", claimVO.getMappingSeq());
					
					if("40".equals(claimVO.getClaimGb())) refusalChange(claimVO, request);
				}
			}
		} catch(Exception e) {
			//1) Error Log
			message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + PaKakaoComUtill.getErrorMessage(e);
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PAKAKAO_ORDER_CHANGE_A");
			//2) UPDATE TPAORDERM
			updatePaOrdermTxForRollback(orderClaimVOArray, e);
		} finally {
			saveApiTracking(paramMap, request);
		}
	}
	
	/**
	 * 카카오 교환 철회(거부)
	 * @param claimVO
	 * @param request
	 * @throws Exception
	 */
	private void refusalChange(OrderClaimVO claimVO, HttpServletRequest request) throws Exception {
		Map<String, Object> map    = new HashMap<String, Object>();
		ArrayList<String> orderIds = new ArrayList<String>();
		ParamMap apiInfoMap	= new ParamMap();
		ParamMap apiDataMap	= new ParamMap();
		ParamMap errorMap	= null;
		ParamMap paramMap	= new ParamMap();
		String   prg_id     = "";
		String   rtnMsg	    = "";
		int 	 excuteCnt  = 0; 
		
		try {
			
			prg_id = "IF_PAKAKAOAPI_04_003";
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			paramMap.put("mappingSeq", claimVO.getMappingSeq());
			HashMap<String, Object> refusalMap = paKakaoClaimService.selectRefusalInfo(paramMap);
			
			orderIds.add(refusalMap.get("PA_ORDER_SEQ").toString());
			
			apiInfoMap.put("paCode", refusalMap.get("PA_CODE").toString());
			apiDataMap.put("claimId", refusalMap.get("PA_CLAIM_NO").toString());
			apiDataMap.put("orderIds", orderIds);

			map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap); 

			if( !"200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
				
				errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
				apiInfoMap.put("code", "500");
				rtnMsg += "paClaimNo" + ":" + refusalMap.get("PA_CLAIM_NO").toString() + " " + errorMap.get("errorMsg").toString() + " ";
				
			}else {
				paramMap.put("paCode", refusalMap.get("PA_CODE").toString());
				paramMap.put("claimId", refusalMap.get("PA_CLAIM_NO").toString());
				paramMap.put("orderIds", refusalMap.get("PA_ORDER_SEQ").toString());
				paramMap.put("changeFlag", "03");
				paramMap.put("preCanYn", "1");
				paramMap.put("apiResultMessage", "재고부족 or 판매불가로 인한 교환거절 처리");
				paramMap.put("apiResultCode", "0000");
				
				excuteCnt = paKakaoClaimService.updatePreCanYn(paramMap);
				if(excuteCnt < 1) {
					throw processException("msg.cannot_save", new String[] { paramMap.get("claimId")+":TPAORDERM UPDATE(preCancelYn)" });
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
	}
	
	/**
	 * 카카오 교환 취소 데이터 생성
	 * @param claim
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public void changeCancelAsync(HashMap<String, Object> claimMap, HttpServletRequest request) {
		List<Object> changeCancelTargetDtList = null;
		HashMap<String, Object> hmSheet = null;
		int index = 0;
		ParamMap paramMap = new ParamMap();
		String paOrderNo = claimMap.get("PA_ORDER_NO").toString();
		OrderClaimVO[] orderClaimVOArray = null;
		OrderClaimVO orderClaimVO = null;
		boolean preCanYn = false;
		
		paramMap.setParamMap(claimMap);
		paramMap.replaceCamel();
		try {
			changeCancelTargetDtList = paKakaoClaimService.selectChangeCancelTargetDtList(paramMap);
			if(changeCancelTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectChangeCancelTargetDtList" });
			
			orderClaimVOArray = new OrderClaimVO[changeCancelTargetDtList.size()];
			
			for(Object hm : changeCancelTargetDtList) {
				hmSheet = (HashMap<String, Object>)hm;
				
				orderClaimVO = new OrderClaimVO();
				orderClaimVO = setOrderClaimVO(hmSheet);
				orderClaimVOArray[index] = orderClaimVO;
				preCanYn = false;
				
				index++;
			}
			
			if(!preCanYn) {
				paclaimService.saveChangeCancelTx(orderClaimVOArray);
			}
			paramMap.put("code", "200");
			paramMap.put("message", "SUCCESS");
		} catch(Exception e) {
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + e.getMessage();
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PAKAKAO_CHANGE_CANCEL");
			updatePaOrdermTxForRollback(orderClaimVOArray, e);	
		} finally {
			saveApiTracking(paramMap, request);
		}
	}
	
	private void updatePaOrdermTxForRollback(AbstractVO[] aVO, Exception e){
		
		if(aVO == null || aVO.length < 1 || aVO[0] == null)  return;
		
		ParamMap paramMap = null;
		int excuteCnt = 0;
		
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
			
			String errMsg = PaKakaoComUtill.getErrorMessage(e);
			paramMap.put("resultCode"		, "999999");
			paramMap.put("resultMessage"	, errMsg.length() > 1950 ? errMsg.substring(0,1950) : errMsg);
			paramMap.put("createYn"			, "0");
			
			try {
				excuteCnt = paorderService.updatePaOrdermTx(paramMap);
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
			paramMap.put("siteGb"		, Constants.PA_KAKAO_PROC_ID);
			systemService.insertApiTrackingTx(request, paramMap);
			
		}catch(Exception ee){
			log.error("ApiTracking Insert Error : "+ee.toString());
		}
	}

	public void orderInputAsync(Map<String, String> order, HttpServletRequest request) throws Exception {
		int		 index									 = 0;
		OrderInputVO[] orderInputVO						 = null;
		OrderInputVO   vo								 = new OrderInputVO();
		HashMap<String, Object>[] resultMap				 = null;
		ParamMap paramMap								 = null;
		List<Map<String, Object>> orderInputTargetDtList = null;
		
		int targetCnt   = Integer.parseInt(String.valueOf(order.get("TARGET_CNT")));
		int targetSize  = 0;
		
		try {
			orderInputTargetDtList = paKakaoDeliveryService.selectOrderInputTargetDtList(order);
			
			targetSize = orderInputTargetDtList.size();
			
			if(targetSize < 1){
				throw processException("msg.no.select", new String[]{"selectOrderInputTargetDtList"});
			}else if(targetSize != targetCnt){
				throw processException("pa.cannot_dup_data", new String[]{"selectOrderInputTargetDtList"});
			}
			
			orderInputVO = new OrderInputVO[orderInputTargetDtList.size()];
			PaKakaoComUtill.replaceCamelList(orderInputTargetDtList);
			for(Map<String, Object> map : orderInputTargetDtList) {
				
				// 가격비교
				String paApplyDate   = map.get("applyDate").toString(); // TPAGOODSPRICEAPPLY 기준 APPLY_DATE
				String stoaApplyDate = map.get("stoaApplyDate").toString(); // TGOODSPRICE 기준 APPLY_DATE
				String paOrderDate   = map.get("orderDate").toString(); // 카카오 주문일시
				
				if(!paApplyDate.equals(stoaApplyDate)) {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					Date orderDate = format.parse(paOrderDate.substring(0, 8));
			        Date stoaDate = format.parse(stoaApplyDate.substring(0, 8));
			        
			        // 스토아 최신 가격 적용일자와 제휴 주문일자 차이 계산
			        long diffDays = orderDate.getTime() - stoaDate.getTime();
			        diffDays = diffDays / (24 * 60 * 60 * 1000);
			        
					if(diffDays >= 3) { // 3일 이상 차이나는 경우
						throw processException("pa.fail_order_input", new String[]{ "가격 정보가 잘못되었습니다." });
					}
				}
				
				vo = (OrderInputVO) PaKakaoComUtill.map2VO(map, OrderInputVO.class);
				
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
				
				// 단품명 불일치 확인
				String orderGoodsDtName = map.get("optionContent").toString().substring(12).replaceAll(" ", ""); // 카카오는 "색상/크기/무늬/형태/" 를 하드코딩으로 연동 
				String goodsDtName = paKakaoGoodsService.selectKakaoOrderGoodsDtName(map);
				
				if(!goodsDtName.equals(orderGoodsDtName)) {
					updatePaorderM(vo, "단품명이 일치하지 않습니다.");
					throw processException("pa.fail_order_input", new String[]{ "단품명이 일치하지 않습니다." });
				}
				
				// 주소처리
				/*
				String zipCode = ComUtil.NVL(map.get("ZIPCODE")).toString().replace("-", "").trim();
				String type    = "";
				if(zipCode.length() == 6){
					type  = "01"; //1:지번주소, 2:도로명주소
				}else{
					type  = "02";
				}
				*/
				vo.setAddrGbn     	("02"); //도로명 강제 세팅. 지번으로 넘어와도 별로 상관없는듯????
				vo.setStdAddr     	(ComUtil.NVL(map.get("receiverAddr1")).toString());
				vo.setStdAddrDT   	("");
				vo.setPostNo      	(map.get("zipcode").toString());
				vo.setPostNoSeq   	("001");

				// Local여부 처리
				if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
					vo.setIsLocalYn("Y");
				}else {
					vo.setIsLocalYn("N");					
				}
				vo.setProcUser    	(Constants.PA_KAKAO_PROC_ID);
				
				orderInputVO[index] = vo;
				index++;
			}
			// BO 데이터 생성
			resultMap = paorderService.saveOrderTx(orderInputVO);
			
		} catch(Exception e) {
			updatePaOrdermTxForRollback(orderInputVO, e);
			
			paramMap = new ParamMap();
			paramMap.put("apiCode", "PAKAKAO_ORDER_INPUT_A");
			paramMap.put("message"	, "pa_order_no : " + order.get("PA_ORDER_NO") + "pa_ship_no : " + order.get("PA_SHIP_NO") + " > " + PaKakaoComUtill.getErrorMessage(e));
										// 제휴 주문번호 + 배송번호 
		} finally {
			saveApiTracking(paramMap, request);
		}
		//재고 부족으로 인한 주문 거절(판매자 주문 취소)
		refusalOrder(resultMap, request);
		
	}
	
	private void refusalOrder(HashMap<String, Object>[] resultMap, HttpServletRequest request) throws Exception {

		if(resultMap == null) return;
		
		ParamMap				apiInfoMap	= null;
		ParamMap 				errorMap	= null;
		String					prg_id		= "IF_PAKAKAOAPI_04_010";  //판매자 직접취소
		String 					resultMsg	= "재고부족 판매자 취소처리";
		String 					resultCode	= "0000";
		List<Map<String, Object>> refusalList  = new ArrayList<Map<String,Object>>();
		Map<String, Object> connectResult	= new HashMap<String, Object>() ;
		
		try {
			//=1) 판매자 주문 취소할 LIST를 생성해준다
			for(HashMap<String ,Object> map :  resultMap) {
				if( !("100001").equals(map.get("RESULT_CODE"))) continue; //100001 - 재고없음
				HashMap<String, Object> refusalMap = paKakaoDeliveryService.selectRefusalInfo(map.get("MAPPING_SEQ").toString());
				refusalList.add(refusalMap);
			}
			
			if(refusalList.size() == 0) return;
			
			apiInfoMap = new ParamMap();
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			apiInfoMap.put("paCode", refusalList.get(0).get("PA_CODE").toString());
			
			for(int i=0; i < refusalList.size(); i++) {
				try {
					apiInfoMap.put("url", url.replace("{orderId}", refusalList.get(i).get("ID").toString()));
					connectResult = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
					
					if(!"200".equals(ComUtil.objToStr(connectResult.get("statusCode"))) ) {
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(connectResult);
						
						refusalList.get(i).put("preCanYn"			, "0");
						refusalList.get(i).put("apiResultMessage"	, errorMap.get("errorMsg").toString());
						refusalList.get(i).put("apiResultCode"		, "500");
						
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", errorMap.get("errorMsg").toString());
					} else {
						refusalList.get(i).put("preCanYn"			, "1");
						refusalList.get(i).put("apiResultMessage"	, resultMsg);
						refusalList.get(i).put("apiResultCode"		, resultCode);
					}				
					
				} catch(Exception e) {
					log.error(prg_id + " - 주문 내역 생성 오류" + refusalList.get(i).get("ID").toString(), e);
					refusalList.get(i).put("preCanYn"			, "0");
					refusalList.get(i).put("apiResultMessage"	, PaKakaoComUtill.getErrorMessage(e));
					refusalList.get(i).put("apiResultCode"		, "500");
					
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", refusalList.get(i).get("ID").toString() + " 품절취소처리에러" );
				}
			}
			
		}catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		}finally {
			for( Map<String, Object> map : refusalList) {
				paKakaoDeliveryService.updatePreCanYn(map);
			}
		}
	}

	private OrderClaimVO setOrderClaimVO(HashMap<String, Object> hmSheet) throws Exception {
		OrderClaimVO orderClaimVO = new OrderClaimVO();
		orderClaimVO.setMappingSeq	  (hmSheet.get("MAPPING_SEQ").toString());
		orderClaimVO.setOrderNo	  	  (hmSheet.get("ORDER_NO").toString());
		orderClaimVO.setOrderGSeq	  (hmSheet.get("ORDER_G_SEQ").toString());
		orderClaimVO.setOrderDSeq	  (ComUtil.NVL(hmSheet.get("ORDER_D_SEQ")).toString());
		orderClaimVO.setOrderWSeq	  (ComUtil.NVL(hmSheet.get("ORDER_W_SEQ")).toString());
		orderClaimVO.setClaimQty	  (Integer.parseInt(String.valueOf(hmSheet.get("PA_PROC_QTY")))); 
		orderClaimVO.setClaimGb		  (hmSheet.get("PA_ORDER_GB").toString());
		orderClaimVO.setClaimDesc	  (ComUtil.NVL(hmSheet.get("CLAIM_DESC")).toString());
		orderClaimVO.setCustDelyYn    ("0");
		orderClaimVO.setReturnDelyGb  ("");
		orderClaimVO.setReturnSlipNo  ("");
		orderClaimVO.setOutBefClaimGb (ComUtil.NVL(hmSheet.get("OUT_BEF_CLAIM_GB")).toString());
		orderClaimVO.setInsertId	  (hmSheet.get("SITE_GB").toString());
		
		switch(hmSheet.get("PA_ORDER_GB").toString()) {
		case "30": case "40": case "45":
			orderClaimVO.setExchGoodsdtCode	(hmSheet.get("EXCH_GOODSDT_CODE").toString());
			orderClaimVO.setAdminProcYn		(hmSheet.get("ADMIN_PROC_YN").toString());
			
			ParamMap paramMap = new ParamMap();
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
			paramMap.put("PA_ORDER_NO"	, hmSheet.get("PA_ORDER_NO").toString());
			paramMap.put("PA_CLAIM_NO"	, hmSheet.get("PA_CLAIM_NO").toString());
			paramMap.put("PA_ORDER_GB"	, hmSheet.get("PA_ORDER_GB").toString());
			String checkAddr = paKakaoClaimService.compareAddress(paramMap);
			orderClaimVO.setSameAddr(checkAddr);
			
			break;
			
		case "31": case "41": case "46":
			orderClaimVO.setReturnName		("");
			orderClaimVO.setReturnTel		("");
			orderClaimVO.setReturnHp		("");
			orderClaimVO.setReturnAddr		("");
			orderClaimVO.setRcvrMailNo		("");
			orderClaimVO.setRcvrMailNoSeq   ("");
			orderClaimVO.setRcvrBaseAddr	("");
			orderClaimVO.setRcvrDtlsAddr	("");
			orderClaimVO.setRcvrTypeAdd		("");
			break;	
		}
		
		String claimCode = hmSheet.get("CLAIM_CODE").toString();
			
		if(claimCode.length() == 6) {
			orderClaimVO.setCsLgroup		(claimCode.substring(0,2));
			orderClaimVO.setCsMgroup		(claimCode.substring(2,4));
			orderClaimVO.setCsSgroup		(claimCode.substring(4,6));
			orderClaimVO.setCsLmsCode		(claimCode);		
			orderClaimVO.setStandardType	("0");     //// 기준내 :1 ,기준외 : 0
		}
		orderClaimVO.setClaimCode(claimCode);
		
		Long shpFeeAmt  = Long.parseLong(String.valueOf(hmSheet.get("CLM_LST_DLV_CST")));  
		String shipcostChargeYn = String.valueOf(hmSheet.get("SHIPCOST_CHARGE_YN"));
		if ("1".equals(shipcostChargeYn)){
			orderClaimVO.setShpfeeYn		("1");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
			orderClaimVO.setShipcostChargeYn("1"); 
		}else{
			shpFeeAmt	= 0L;
			orderClaimVO.setShpfeeYn		("0");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2
			orderClaimVO.setShipcostChargeYn("0"); 
		}
		orderClaimVO.setShpFeeAmt			(shpFeeAmt);
		
		//출하지시 이후 취소건은 반품인데, 이경우 배송비 부과여부를 무상으로 처리한다.
		if(("1").equals(orderClaimVO.getOutBefClaimGb()) && "20".equals(hmSheet.get("PA_ORDER_GB").toString())){
			orderClaimVO.setShpfeeYn		("0");
			orderClaimVO.setShipcostChargeYn("0"); 
			orderClaimVO.setShpFeeAmt		(0L);
			orderClaimVO.setIs20Claim		(true);
		}else{
			orderClaimVO.setIs20Claim		(false);
		}
		return orderClaimVO;
	}

	@Async
	public void spPagoodsSyncKakao(HttpServletRequest request, String goodsCode, String userId) throws Exception {
		ParamMap paramMap							 = new ParamMap();
		ParamMap stopSaleParam						 = new ParamMap();
		ParamMap stopShipParam 						 = new ParamMap();
		//ParamMap stopOfferParam						 = new ParamMap();
		HashMap<String, String> minMarginPrice		 = new HashMap<String, String>();
		List<PaGoodsImage> curImageInfo				 = null;
		List<PaEntpSlip> curEntpSlipInfo			 = null;
		List<PaGoodsPriceVO> curPriceInfo			 = null;
		List<HashMap<String, String>> curSaleStop	 = null;
		List<HashMap<String, String>> curEventMargin = null;
		List<PaCustShipCostVO> curShipCostInfo 		 = null;
		List<HashMap<String, String>> curShipStopSale = null;
		//List<HashMap<String, String>> curCheckDtCnt = null;
		
		String resultMsg = "";
		String dateTime  = systemService.getSysdatetimeToString();
		StringBuffer sb  = null;
		int conditionDay = 2;
		
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paGroupCode", "11");
		paramMap.put("dateTime", dateTime);
		paramMap.put("conditionDay", conditionDay);		
		paramMap.put("comparePaCode", "B1");
		paramMap.put("feeCode", "O699");
		paramMap.put("minMarginCode", "86");		 
		paramMap.put("minPriceCode", "87");			
		
		paramMap.put("siteGb", userId);
		paramMap.put("apiCode", "IF_PACOMMON_00_002");
		
		log.info("Step1. 카카오 상품이미지 동기화 START");
		curImageInfo = paCommonService.selectCurImageInfoList(paramMap);
		if(curImageInfo.size() > 0) {
			sb = new StringBuffer();
			for(PaGoodsImage curImageInfoTarget : curImageInfo) {
				//이미지 없는 케이스가 많아 for문안에 try catch 처리
				try {
					curImageInfoTarget.setModifyId(Constants.PA_KAKAO_PROC_ID);
					curImageInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					curImageInfoTarget.setRemark(dateTime);
					resultMsg = paCommonService.saveCurImageInfoTx(curImageInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step1 상품이미지 동기화 카카오 Fail > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
						sb.append(curImageInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step1  상품이미지 동기화 카카오 Sucess > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
				} catch(Exception e) {
					log.info("Step1. 카카오 상품이미지 동기화 : " + e.getMessage());
					paramMap.put("code", "500");
					paramMap.put("message", "Step1. 카카오 상품이미지 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}
		}
		log.info("Step1. 카카오 상품이미지 동기화 END");
		
		log.info("Step2. 카카오 상품가격 동기화 START");
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
						
						stopSaleParam.put("paGroupCode", "11");
						stopSaleParam.put("paCode", curPriceInfoTarget.getPaCode());
						stopSaleParam.put("goodsCode", curPriceInfoTarget.getGoodsCode());
						stopSaleParam.put("dateTime", dateTime);
						stopSaleParam.put("userId", Constants.PA_KAKAO_PROC_ID);
						stopSaleParam.put("paGoodsCode", curPriceInfoTarget.getPaGoodsCode());
						stopSaleParam.put("note", "마진10이하 혹은 판매가100원 미만");
						stopSaleParam.put("priceStopSale", "Y");
						
						//연동제외처리
						resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
					} else {
						curPriceInfoTarget.setModifyId(Constants.PA_KAKAO_PROC_ID);
						curPriceInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						curPriceInfoTarget.setDateTime(dateTime);
						resultMsg = paCommonService.saveCurPriceInfoTx(curPriceInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step2 상품가격 동기화 카카오 Fail > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
						sb.append(curPriceInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step2 상품가격 동기화 카카오 Sucess > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
				}
			} catch(Exception e) {
				log.info("Step2. 카카오 상품가격 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step2. 카카오 상품가격 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step2. 카카오 상품가격 동기화 END");
		
		log.info("Step3. 카카오 고객부담 배송비 동기화 START");
		curShipCostInfo = paCommonService.selectCurShipCostInfoList(paramMap);
		if(curShipCostInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaCustShipCostVO curShipCostInfoTarget : curShipCostInfo) {
					
					if(curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("QN")) {  //2021-05-07  : 배송비정책이 QN인 경우 연동 제외처리 / 향후 정책 확정되면 조건 추가될수도
						
						stopShipParam.put("paCode", curShipCostInfoTarget.getPaCode());
						stopShipParam.put("entpCode", curShipCostInfoTarget.getEntpCode());
						stopShipParam.put("shipCostCode", curShipCostInfoTarget.getShipCostCode());
						
						curShipStopSale = paCommonService.selectCurShipStopSaleList(stopShipParam);
						for(HashMap<String, String> curShipStopSaleTarget : curShipStopSale) {
							
							stopSaleParam.put("paGroupCode", "11");
							stopSaleParam.put("paCode", curShipStopSaleTarget.get("PA_CODE").toString());
							stopSaleParam.put("goodsCode", curShipStopSaleTarget.get("GOODS_CODE").toString());
							stopSaleParam.put("dateTime", dateTime);
							stopSaleParam.put("userId", "PAKAKAO");
							stopSaleParam.put("paGoodsCode", curShipStopSaleTarget.get("PA_GOODS_CODE").toString());
							stopSaleParam.put("note", "배송비유료변경");
							stopSaleParam.put("priceStopSale", "N");
							
							//연동제외처리
							resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
						}
						
					} else {
						curShipCostInfoTarget.setModifyId("PAKAKAO");
						curShipCostInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						resultMsg = paCommonService.saveCurShipCostInfoTx(curShipCostInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step3 고객부담배송비 카카오 Fail");
						sb.append(curShipCostInfoTarget.getEntpCode() + "|" + curShipCostInfoTarget.getShipCostCode() + ", ");
						continue;
					}
					log.info("Step3 고객부담배송비 카카오 Sucess");
				}
			} catch(Exception e) {
				log.info("Step3. 카카오 고객부담배송비 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step3. 카카오 고객부담배송비 동기화 : " + e.getMessage() + "INFO : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step3. 카카오 고객부담 배송비 동기화 END");
		
		log.info("Step4. 카카오 출고지/회수지 동기화 START");
		curEntpSlipInfo = paCommonService.selectCurEntpSlipInfoList(paramMap);
		if(curEntpSlipInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaEntpSlip curEntpSlipInfoTarget : curEntpSlipInfo) {
					
					curEntpSlipInfoTarget.setModifyId("PAKAKAO");
					curEntpSlipInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					resultMsg = paCommonService.saveCurEntpSlipInfoTx(curEntpSlipInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step4 출고/회수지 동기화 카카오 Fail");
						sb.append(curEntpSlipInfoTarget.getEntpCode() + ", ");
						continue;
					}
					log.info("Step4 출고/회수지 동기화 카카오 Sucess");
				}
			} catch(Exception e) {
				log.info("Step4. 카카오 출고/회수지 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step4. 카카오 출고/회수지 동기화 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step4. 카카오 출고지/회수지 동기화 END");
		
		log.info("Step5. 카카오 상품판매단계 동기화 START");
		curSaleStop = paCommonService.selectCurSaleStopList(paramMap);
		
		if(curSaleStop.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curSaleStopTarget : curSaleStop) {
					
					stopSaleParam.put("paGroupCode", "11");
					stopSaleParam.put("paCode", curSaleStopTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curSaleStopTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PAKAKAO");
					stopSaleParam.put("paGoodsCode", curSaleStopTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "SK스토아상품판매중단");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step5 상품판매단계 동기화 카카오 Fail > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
						sb.append(curSaleStopTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step5 상품판매단계 동기화 카카오 Sucess > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step5. 카카오 상품판매단계 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step5. 카카오 상품판매단계 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step5. 카카오 상품판매단계 동기화 END");
		
		log.info("Step6. 카카오 행사 종료 상품 마진 체크 동기화 START");
		curEventMargin = paCommonService.selectCurEventMarginList(paramMap);
		
		if(curEventMargin.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curEventMarginTarget : curEventMargin) {
					paramMap.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					//minMarginPrice = paCommonService.selectMinMarginPrice(paramMap);
					//selectCurEventMarginList 안에서 체크
					//if(ComUtil.objToDouble(curEventMarginTarget.get("MARGIN_RATE")) < ComUtil.objToDouble(minMarginPrice.get("MIN_MARGIN"))) {
					stopSaleParam.put("paGroupCode", "10");
					stopSaleParam.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curEventMarginTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PAKAKAO");
					stopSaleParam.put("paGoodsCode", curEventMarginTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "행사종료");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step6 행사 종료 상품 마진 체크 카카오 Fail > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
						sb.append(curEventMarginTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step6 행사 종료 상품 마진 체크 카카오 Sucess > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
					//}					
				}
			} catch(Exception e) {
				log.info("Step6. 카카오 행사 종료 상품 마진 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step6. 카카오 행사 종료 상품 마진 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step6. 카카오 행사 종료 상품 마진 체크 동기화 END");
	
		log.info("Step7. 카카오 프로모션 START");
		paCommonService.savePaPromoTargetHistory(paramMap);
		
		String compareGoodsCode = "";
		String rtnMsg;
		List<PaPromoGoodsPrice> paPromoGoodsPriceList = paCommonService.selectPaPromoGoodsPriceList(paramMap);

		for(int i=0; i<paPromoGoodsPriceList.size(); i++) {
			if(compareGoodsCode.equals(paPromoGoodsPriceList.get(i).getGoodsCode())) {
				continue;
			}

			rtnMsg = paCommonService.savePaPromoGoodsPriceTx(paPromoGoodsPriceList.get(i));
			if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
				log.error("Step8. 카카오 프로모션 에러 : " + paPromoGoodsPriceList.get(i).getGoodsCode());
			}
			compareGoodsCode = paPromoGoodsPriceList.get(i).getGoodsCode();
		}
		log.info("Step7. 카카오 프로모션 END");
		
	}
	
	@Async
	//@SuppressWarnings("unchecked")
	public void asyncGoodsModify(HttpServletRequest request, ParamMap asyncMap, ParamMap bodyMap,
			PaKakaoGoodsVO paKakaoGoods, List<PaGoodsdtMapping> goodsdtMapping, PaKakaoTalkDealVO dealData) throws Exception {
		
		ParamMap errorMap	= null;
		Map<String, Object> map = new HashMap<String, Object>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		try {
			String dateTime = systemService.getSysdatetimeToString();
			
			log.info("KAKAO 상품수정 API - API 호출 ");
			map = paKakaoConnectUtil.apiGetObjectByKakao(asyncMap, bodyMap);
			
			if( "200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
				
				paKakaoGoods.setModifyId(asyncMap.get("siteGb").toString());
				paKakaoGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				rtnMsg = paKakaoGoodsService.savePaKakaoGoodsTx(paKakaoGoods, goodsdtMapping, dealData);
				
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
					asyncMap.put("code", "500");
					asyncMap.put("message", paKakaoGoods.getGoodsCode() + " : 통신성공 후 저장중 에러");
				} else {
					asyncMap.put("code", "200");
					asyncMap.put("message", "OK");
				}		
				
			} else {
				errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
				
				asyncMap.put("code", "500");
				asyncMap.put("message", errorMap.get("errorMsg").toString());
				paKakaoGoods.setReturnNote(errorMap.get("errorMsg").toString());
				
				if(errorMap.get("errorMsg").toString().indexOf("제한된 단어")         != -1
				|| errorMap.get("errorMsg").toString().indexOf("50자 이내로 입력")	  != -1
				|| errorMap.get("errorMsg").toString().indexOf("입력이 불가능한 문자")   != -1
				|| errorMap.get("errorMsg").toString().indexOf("25자 이내로 입력")	  != -1
				|| errorMap.get("errorMsg").toString().indexOf("중복된 옵션")	      != -1
				|| errorMap.get("errorMsg").toString().indexOf("최대 100개")	      != -1
				|| errorMap.get("errorMsg").toString().indexOf("기본배송비 이상 금액")  != -1
				|| errorMap.get("errorMsg").toString().indexOf("교환배송비 최대 100만원")!= -1
				|| errorMap.get("errorMsg").toString().indexOf("주문배송비 최대 100만원")!= -1
				|| errorMap.get("errorMsg").toString().indexOf("반품배송비 최대 50만원") != -1
				|| errorMap.get("errorMsg").toString().indexOf("스토어에 유사한 상품이 이미 등록") != -1) {
							
					paKakaoGoods.setPaSaleGb("30");
					paKakaoGoods.setPaStatus("90");
					paKakaoGoods.setTransSaleYn("1");
				} else {
					//paKakaoGoods.setPaSaleGb("20");
					paKakaoGoods.setPaStatus("30");
				}
				
				paKakaoGoodsService.savePaKakaoGoodsError(paKakaoGoods);
				map.put("message", (errorMap.get("errorMsg").toString()));
			}
			map.put("prgId", asyncMap.get("apiCode"));
			insertPaGoodsTransLog(paKakaoGoods, map);
			
		}catch (Exception e) {
			asyncMap.put("code", "500");
			asyncMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		} finally {
			if(!"200".equals(asyncMap.get("code").toString())) {
				systemService.insertApiTrackingTx(request, asyncMap);
			}
		}
	}
	
	public void insertPaGoodsTransLog(PaKakaoGoodsVO paKakaoGoods, Map<String, Object> map) throws Exception {
		
		String code = "";
		String message = "";
		String productId = "null";
		if(!"200".equals(String.valueOf(map.get("statusCode")))){
			code = "500";
			message = String.valueOf(map.get("message"));
		} else {
			code = "200";
			message = "연동 성공";
		}
		
		if ("INSERT".equals(map.get("modCase"))) {
			if("200".equals(String.valueOf(map.get("statusCode")))) {
				productId = map.get("productId").toString();
			}
		}else {
			productId = paKakaoGoods.getProductId();
		}
		
		String dateTime = systemService.getSysdatetimeToString();
		
		PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
		paGoodsTransLog.setGoodsCode(paKakaoGoods.getGoodsCode());
		paGoodsTransLog.setPaCode(paKakaoGoods.getPaCode());
		paGoodsTransLog.setItemNo("null".equals(productId) ? paKakaoGoods.getGoodsCode() : productId);
		paGoodsTransLog.setRtnCode(code);
		paGoodsTransLog.setRtnMsg(map.get("prgId") + "|" + message);
		paGoodsTransLog.setSuccessYn(code.equals("200") ? "1" : "0");
		paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		paGoodsTransLog.setProcId(Constants.PA_KAKAO_PROC_ID);
		paCommonDAO.insertPaGoodsTransLog(paGoodsTransLog);		
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
