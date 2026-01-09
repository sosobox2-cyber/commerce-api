package com.cware.api.palton.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
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
import com.cware.netshopping.domain.PaLtonGoodsVO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.palton.claim.service.PaLtonClaimService;
import com.cware.netshopping.palton.delivery.service.PaLtonDeliveryService;
import com.cware.netshopping.palton.goods.service.PaLtonGoodsService;
import com.cware.netshopping.palton.util.PaLtonComUtill;
import com.cware.netshopping.palton.util.PaLtonConnectUtil;

import io.swagger.annotations.Api;

@Api(value="/palton/claim")
@Controller("com.cware.api.palton.PaLtonAsycController")
@RequestMapping(value="/palton/claim")
public class PaLtonAsyncController extends AbstractController{
	
	@Autowired
	private PaLtonDeliveryService paLtonDeliveryService;
	@Autowired
	private PaLtonClaimService paLtonClaimService;
	@Autowired
	private PaLtonConnectUtil paLtonConnectUtil;
	@Autowired 
	private SystemService systemService;
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paorderService;
	@Resource(name = "pacommon.claim.paclaimService")
	private PaClaimService paclaimService;	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	@Resource(name = "palton.goods.paLtonGoodsService")
	private PaLtonGoodsService paLtonGoodsService;

	
	public void orderInputAsync(Map<String, String> order, HttpServletRequest request) throws Exception {
		
		List<Map<String, Object>>  orderInputTargetDtList = null;
		String paOrderNo  					= order.get("PA_ORDER_NO");
		OrderInputVO[] orderInputVO 		= null;
		HashMap<String, Object>[] resultMap = null;
		OrderInputVO			  vo		= new OrderInputVO();
		int	index 							= 0;
		ParamMap paramMap					= null;
		String   papromoAllowTerm			= order.get("PAPROMO_ALLOW_TERM");
		
		try {
			//=1) 한 주문에 대한 DT Information 
			paramMap = new ParamMap();
			paramMap.put("paOrderNo"		, paOrderNo);
			paramMap.put("papromoAllowTerm"	, papromoAllowTerm);
			
			orderInputTargetDtList = paLtonDeliveryService.selectOrderInputTargetDtList(paramMap);  
			
			if(orderInputTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectOrderInputTargetDtList" });
			
			//=2) 1)의 데이터를 가지고 BO 데이터를 생성하기위해 VO SETTING
			orderInputVO = new OrderInputVO[orderInputTargetDtList.size()];
			PaLtonComUtill.replaceCamelList(orderInputTargetDtList);
			for(Map<String, Object> map : orderInputTargetDtList) {
				
				// 2-1) 가격비교
				String paApplyDate   = map.get("applyDate").toString(); // TPAGOODSPRICE 기준 APPLY_DATE
				String stoaApplyDate = map.get("stoaApplyDate").toString(); // TGOODSPRICE 기준 APPLY_DATE
				String paOrderDate   = map.get("orderDate").toString(); // 롯데온 주문일시
				
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
				
				// 2-2) 프로모션 가격비교
				if(!checkOrderPrice(map)) throw processException("pa.fail_order_input", new String[] { "가격 및 프로모션 연동이 잘못되었습니다 " }); 
				
				//=DB에서 가져온 값은 자동으로 VO SETTING
                if (map.get("receiverTel").toString().length() < 4)  map.put("receiverTel", "");
				vo = (OrderInputVO)PaLtonComUtill.map2VO(map, OrderInputVO.class); 
	            double doAmt = Long.parseLong(String.valueOf(map.get("outPromoPrice"))); 

				//PROMOTION DATA SETTING 
	            if(doAmt > 0) {
	            	OrderpromoVO orderPaPromo = new OrderpromoVO();
		            
		            orderPaPromo.setPromoNo(vo.getPromoNo()); 
		            orderPaPromo.setDoType("30");
		            orderPaPromo.setDoAmt(doAmt); 
		            orderPaPromo.setProcCost(doAmt);
		            orderPaPromo.setOwnProcCost(vo.getOwnCost());
		            orderPaPromo.setEntpProcCost(vo.getEntpCost());
		            orderPaPromo.setCouponPromoBdate(vo.getCouponPromoBdate());
		            orderPaPromo.setCouponPromoEdate(vo.getCouponPromoEdate());
		            orderPaPromo.setCouponYn("1"); 
		            orderPaPromo.setProcGb("I");
		            
		            vo.setOrderPaPromo(orderPaPromo);
	            }
	            
				//=주소처리
				String zipCode = ComUtil.NVL(map.get("ZIPCODE")).toString().replace("-", "").trim();
				String type    = "";
				if(zipCode.length() == 6){
					type  = "01"; //1:지번주소, 2:도로명주소
				}else{
					type  = "02";
				}
				vo.setAddrGbn     	(type);
				vo.setStdAddr     	(ComUtil.NVL(map.get("receiverAddr1")).toString());
				vo.setStdAddrDT   	(ComUtil.NVL(map.get("receiverAddr2")).toString());
				vo.setPostNo      	(zipCode);
				vo.setPostNoSeq   	("001");

				//=Local여부 처리
				if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
					vo.setIsLocalYn("Y");
				}else {
					vo.setIsLocalYn("N");					
				}
				vo.setProcUser    	(Constants.PA_LTON_PROC_ID);
				
				orderInputVO[index] = vo;
				index++;
			}
			
			//=3) BO 데이터 생성
			resultMap = paorderService.saveOrderTx(orderInputVO);
			
		}catch (Exception e) {
			updatePaOrdermTxForRollback(orderInputVO, e);
			
			paramMap = new ParamMap();
			paramMap.put("apiCode"	, "PALTON_ORDER_INPUT_A");
			paramMap.put("message"	, "pa_order_no : " + paOrderNo + " > " + PaLtonComUtill.getErrorMessage(e));
		
		}finally {
			saveApiTracking(paramMap, request);
		}
		
		//재고 부족으로 인한 주문 거절(판매자 주문 취소)
		refusalOrder(resultMap, paOrderNo);
		
	}
	
	private boolean checkOrderPrice(Map<String, Object> map) {
		boolean chkResult = true;
		
		try {
			double salePrice 		= Long.parseLong(String.valueOf(map.get("salePrice")));  	
			double dcAmt 			= Long.parseLong(String.valueOf(map.get("dcAmt"))); 		
			double doAmt 			= Long.parseLong(String.valueOf(map.get("outPromoPrice")));
			double orderQty			= Long.parseLong(String.valueOf(map.get("orderQty")));
			double rSaleAmt			= Long.parseLong(String.valueOf(map.get("rsaleAmt"))) ;	//결제가 : 배송비 포함하지 않은채 롯데온에서 줌
			double lumpSumDcAmt 	= Long.parseLong(String.valueOf(map.get("lumpSumDcAmt"))) / orderQty; 
			double instantCouponAmt = Long.parseLong(String.valueOf(map.get("instantCouponDiscount"))) / orderQty; 
			double paDcAmt			= Long.parseLong(String.valueOf(map.get("paDcAmt"))) ; // 롯데온 자체 할인
			
			if(rSaleAmt < 1) throw processException("msg.no.select", new String[] { "RSALE_AMT ERROR" });	
			if(salePrice != ((rSaleAmt + paDcAmt) / orderQty) + dcAmt + lumpSumDcAmt + doAmt + instantCouponAmt  ) chkResult = false;
			
		}catch (Exception e) {
			chkResult = false;
		}
		
		return chkResult;
	}

	public void cancelInputAsync(HashMap<String,Object> cancelTarge, HttpServletRequest request) {
		ParamMap paramMap = null;
		int executedRtn = 0;
		
		try {
			paramMap = new ParamMap();
			paramMap.setParamMap(cancelTarge);
			paramMap.replaceCamel();
			
			HashMap<String, Object> cancelDtInfo = paLtonClaimService.selectCancelInputTargetDtList(paramMap);
			
			if(cancelDtInfo == null) throw processException("msg.no.select", new String[] { "selectCancelInputTargetDtList" });		
			
			String preCancelYn = cancelDtInfo.get("PRE_CANCEL_YN").toString();
			
			switch (preCancelYn) {
			case "0": //취소
				
				CancelInputVO cancelInputVO = new CancelInputVO();
				cancelInputVO.setMappingSeq		(String.valueOf(cancelDtInfo.get("MAPPING_SEQ")));
				cancelInputVO.setOrderNo		(String.valueOf(cancelDtInfo.get("ORDER_NO")));
				cancelInputVO.setOrderGSeq		(String.valueOf(cancelDtInfo.get("ORDER_G_SEQ")));
				cancelInputVO.setCancelQty		(Integer.parseInt(String.valueOf(cancelDtInfo.get("PA_PROC_QTY"))));
				cancelInputVO.setCancelCode		(String.valueOf(cancelDtInfo.get("CANCEL_CODE")));
				cancelInputVO.setProcId			(Constants.PA_LTON_PROC_ID);
				
				try {
					paorderService.saveCancelTx(cancelInputVO);
					
				} catch (Exception e) {
					CancelInputVO[] cancelInputArray = new CancelInputVO[1];
					cancelInputArray[0] = cancelInputVO;
					updatePaOrdermTxForRollback(cancelInputArray , e); // TPAORDERM.RESULT_CODE 99999 처리
				}
				break;

			default: //기취소
				
				Map<String , Object> preCancelMap = new HashMap<String, Object>();
				preCancelMap.put("preCanYn"			, "1");
				preCancelMap.put("mappingSeq"		, String.valueOf(cancelDtInfo.get("MAPPING_SEQ")));
				preCancelMap.put("apiResultMessage"	, getMessage("pa.before_order_create_cancel"));
				paLtonDeliveryService.updatePreCanYn(preCancelMap);
				
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
				}
				break;
			}

		}
		catch ( Exception e ) {
			paramMap.put("apiCode"	, "PALTON_CANCEL_INPUT_A");
			paramMap.put("message"	, "pa_order_no : " + paramMap.getString("paOrderNo") + " > " + PaLtonComUtill.getErrorMessage(e));
		}
		finally {
			saveApiTracking(paramMap, request);
		}
	}
	
	private void refusalOrder(HashMap<String, Object>[] resultMap, String paOrderNo) throws Exception {
		
		if(resultMap == null) return;
		
		ParamMap				apiInfoMap	= null;
		ParamMap				apiDataMap	= null;
		String					prg_id		= "IF_PALTONAPI_04_018";  //판매자 직접취소
		String 					resultMsg	= "재고부족 판매자 취소처리";
		String 					resultCode	= "0000";
		List<Map<String, Object>> itemList  = new ArrayList<Map<String,Object>>();
		Map<String, Object> connectResult	= new HashMap<String, Object>() ;
		String precanYn						= "0";
		
		try {
			//=1) 판매자 주문 취소할 LIST를 생성해준다
			for(HashMap<String ,Object> map :  resultMap) {
				if( !("100001").equals(map.get("RESULT_CODE"))) continue; //100001 - 재고없음
				HashMap<String, Object> refusalMap = paLtonDeliveryService.selectRefusalInfo(map.get("MAPPING_SEQ").toString());
				itemList.add(refusalMap);
			}
			
			if(itemList.size() == 0) return;
			
			//=2) 롯데온 판매자 취소 API를 호출할 인자를 SETTING 한다.
			PaLtonComUtill.replaceCamelList(itemList);
			apiInfoMap = new ParamMap();
			apiDataMap = new ParamMap();
			
			paLtonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			
			apiDataMap.put("odNo"		, paOrderNo);
			apiDataMap.put("itemList"	, itemList);
			
			//=3) 판매자취소 API 호출과 통신후 결과를 TPAORDERM에 적용한다.
			connectResult = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
			if(!Constants.PA_LTON_SUCCESS_CODE.equals(connectResult.get("returnCode").toString())) 
				throw new Exception("판매자 주문 취소 실패 + " + connectResult.get("returnCode").toString());
			
			precanYn = "1";
			
		}catch (Exception e) {
			precanYn  = "0";
			resultMsg = PaLtonComUtill.getErrorMessage(e);
			resultCode = connectResult.get("returnCode").toString();
			log.error("Lton orderRefusal Fail :: " + resultMsg);
		}finally {
			
			//UPDATE TPAORDERM.PRE_CANCEL_YN
			for( Map<String, Object> map : itemList) {
				map.put("preCanYn"			, precanYn);
				map.put("apiResultMessage"	, resultMsg);
				map.put("apiResultCode"		, resultCode);
				paLtonDeliveryService.updatePreCanYn(map);
			}
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
			
			String errMsg = PaLtonComUtill.getErrorMessage(e);
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
			paramMap.put("siteGb"		, Constants.PA_LTON_PROC_ID);
			systemService.insertApiTrackingTx(request, paramMap);
			
		}catch(Exception ee){
			log.error("ApiTracking Insert Error : "+ee.toString());
		}
	}
	
	
	/**
	 * 롯데온 반품 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void orderClaimAsync(HashMap<String, Object> claimMap, HttpServletRequest request) throws Exception {

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
				orderClaimTargetDtList = paLtonClaimService.selectOrderCalimTargetDt30List(paramMap); 
			} else {
				orderClaimTargetDtList = paLtonClaimService.selectOrderCalimTargetDt20List(paramMap); //출고전 반품
			}
			if(orderClaimTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectOrderCalimTargetDt30(20)List" });
			
			//2) 반품 VO 데이터 생성
			orderClaimVO = setOrderClaimVO((HashMap<String, Object>)orderClaimTargetDtList.get(0));
			paclaimService.saveOrderClaimTx(orderClaimVO);
		
		}catch(Exception e) {
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + PaLtonComUtill.getErrorMessage(e);
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PALTON_ORDER_CLAIM_A");
			
			//2) TPAORDERM Update
			OrderClaimVO[] orderClaimList = new OrderClaimVO[1];
			orderClaimList[0] 			  = orderClaimVO;
			updatePaOrdermTxForRollback(orderClaimList, e);
			
		}finally {
			saveApiTracking(paramMap, request);
		}
	}

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
			cancelInputTargetDtList = paLtonClaimService.selectClaimCancelTargetDtList(paramMap); 
			if(cancelInputTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectClaimCancelTargetDtList" });
			
			hmSheet = (HashMap<String, Object>) cancelInputTargetDtList.get(0);
			switch(hmSheet.get("PRE_CANCEL_YN").toString()){
			
				case "0": //일반적인 반품 취소
					orderClaimVO = setOrderClaimVO(hmSheet);
					paclaimService.saveClaimCancelTx(orderClaimVO);	
					break;
				
				case "1": //기취소
					Map<String , Object> preCancelMap = new HashMap<String, Object>();
					preCancelMap.put("preCanYn"			, "1");
					preCancelMap.put("mappingSeq"		, String.valueOf(hmSheet.get("MAPPING_SEQ")));
					preCancelMap.put("apiResultMessage"	, getMessage("pa.before_claim_create_cancel"));
					paLtonDeliveryService.updatePreCanYn(preCancelMap);

					break;	
			}
			
		}catch(Exception e){
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + PaLtonComUtill.getErrorMessage(e);
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("apiCode"  , "PALTON_CLAIM_CANCEL_A");
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
	public void orderChangeAsync(HashMap<String, Object> exchangeMap, HttpServletRequest request) {

		HashMap<String, Object>[] resultMap 		= null;
		List<Object> orderChangeTargetDtList		= null;
		OrderClaimVO orderClaimVO 					= null;
		OrderClaimVO[] orderClaimVOArray 			= null;
		int index 									= 0;
		int targetSize 								= 0;
		String paOrderNo					 		= exchangeMap.get("PA_ORDER_NO").toString();
		ParamMap paramMap 							= new ParamMap();
		paramMap.setParamMap(exchangeMap);
		paramMap.replaceCamel();

		try {
			orderChangeTargetDtList = paLtonClaimService.selectOrderChangeTargetDtList(paramMap); 
			targetSize = orderChangeTargetDtList.size();
			if(targetSize < 1) throw processException("msg.no.select", new String[] { "selectOrderChangeTargetDtList" });
			
			orderClaimVOArray = new OrderClaimVO[orderChangeTargetDtList.size()];

			for(Object hm : orderChangeTargetDtList) {
				orderClaimVO = new OrderClaimVO();
				orderClaimVO = setOrderClaimVO((HashMap<String, Object>) hm);
				orderClaimVOArray[index] = orderClaimVO;
				index ++;
			}
			
			resultMap = paclaimService.saveOrderChangeTx(orderClaimVOArray);
						
			for(int j = 0; targetSize > j; j++) {
				paramMap = new ParamMap();
				paramMap.setParamMap(resultMap[j]);
				paramMap.replaceCamel();
				
				//재고부족으로 인한 교환불가
				if(paramMap.getString("resultCode").equals("100001")) {
					for(OrderClaimVO claimVo : orderClaimVOArray) {
						paLtonClaimService.updatePaOrdermChangeFlag("06", claimVo.getMappingSeq());						
					}
				}
			}
		}catch(Exception e){			
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + PaLtonComUtill.getErrorMessage(e);
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PALTON_ORDER_CHANGE_A");
			//2) UPDATE TPAORDERM
			updatePaOrdermTxForRollback(orderClaimVOArray, e);
		}finally{
			saveApiTracking(paramMap, request);			
		}
	}

	@SuppressWarnings("unchecked")
	public void changeCancelAsync(HashMap<String, Object> cancelMap, HttpServletRequest request) {

		List<Object> changeCancelTargetDtList		= null;
		OrderClaimVO orderClaimVO 					= null;
		HashMap<String, Object> hmSheet 			= null;
		int index 									= 0;
		OrderClaimVO[] orderClaimVOArray 			= null;
		boolean	preCanYn							= false;
		String paOrderNo					 		= cancelMap.get("PA_ORDER_NO").toString();
		ParamMap paramMap 							= new ParamMap();
		paramMap.setParamMap(cancelMap);
		paramMap.replaceCamel();
		
		try {
			changeCancelTargetDtList = paLtonClaimService.selectChangeCancelTargetDtList(paramMap);
			if(changeCancelTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectChangeCancelTargetDtList" });
			
			orderClaimVOArray = new OrderClaimVO[changeCancelTargetDtList.size()];
			
			for(Object hm : changeCancelTargetDtList) {	
				hmSheet = (HashMap<String, Object>)hm;
				switch(hmSheet.get("PRE_CANCEL_YN").toString()){
					case "0":
						orderClaimVO = new OrderClaimVO();
						orderClaimVO = setOrderClaimVO(hmSheet);
						orderClaimVOArray[index] = orderClaimVO;
						preCanYn = false;
						break;
						
					case "1": //기취소
						paLtonClaimService.preOrderChangeCancelTx(hmSheet);
						preCanYn = true;
						break;
				}
				index++;
			}
			
			if(!preCanYn){
				paclaimService.saveChangeCancelTx(orderClaimVOArray);
			}
			
		}catch(Exception e){			
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + e.getMessage();
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PALTON_CHANGE_CANCEL");
			//2) UPDATE TPAORDERM
			updatePaOrdermTxForRollback(orderClaimVOArray, e);	
		}finally{
			saveApiTracking(paramMap, request);
		}		
	}	
	
	
	private OrderClaimVO setOrderClaimVO(HashMap<String, Object> hmSheet) throws Exception{
		
		OrderClaimVO orderClaimVO = new OrderClaimVO();
		orderClaimVO.setMappingSeq			(hmSheet.get("MAPPING_SEQ").toString());
		orderClaimVO.setOrderNo				(hmSheet.get("ORDER_NO").toString());
		orderClaimVO.setOrderGSeq			(hmSheet.get("ORDER_G_SEQ").toString());
		orderClaimVO.setOrderDSeq			(ComUtil.NVL(hmSheet.get("ORDER_D_SEQ")).toString());
		orderClaimVO.setOrderWSeq			(ComUtil.NVL(hmSheet.get("ORDER_W_SEQ")).toString());		
		orderClaimVO.setClaimQty			(Integer.parseInt(String.valueOf(hmSheet.get("PA_PROC_QTY")))); 
		orderClaimVO.setClaimGb				(hmSheet.get("PA_ORDER_GB").toString());
		orderClaimVO.setClaimDesc			(ComUtil.NVL(hmSheet.get("CLAIM_DESC")).toString());
		orderClaimVO.setCustDelyYn			("0"); //= 고객 직접발송일 경우 : 1, 아닐경우 : 0
		orderClaimVO.setReturnDelyGb		("");//직접배송 못하게 막아야함..
		orderClaimVO.setReturnSlipNo		("");		
		orderClaimVO.setOutBefClaimGb		(ComUtil.NVL(hmSheet.get("OUT_BEF_CLAIM_GB")).toString());
		orderClaimVO.setInsertId			(hmSheet.get("SITE_GB").toString());

		
		switch(hmSheet.get("PA_ORDER_GB").toString()){
		
		case "30": case "40": case "45": //교환 발송
			orderClaimVO.setExchGoodsdtCode	(hmSheet.get("EXCH_GOODSDT_CODE").toString());
			orderClaimVO.setAdminProcYn		(hmSheet.get("ADMIN_PROC_YN").toString());

			//TODO 배송지, 회수지 체크 .. 이거 해결해야함
			ParamMap paramMap = new ParamMap();
			orderClaimVO.setReturnName		(hmSheet.get("RETURN_NAME").toString());
			orderClaimVO.setReturnTel		(hmSheet.get("RETURN_TEL").toString().replace("-", ""));
			
			String returnHp = hmSheet.get("RETURN_HP").toString().replace("-", "");
			orderClaimVO.setReturnHp		((returnHp.length() < 4) ? "" : returnHp);
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
			paramMap.put("PA_ORDER_SEQ"	, hmSheet.get("PA_ORDER_SEQ").toString());
			paramMap.put("PA_CLAIM_NO"	, hmSheet.get("PA_CLAIM_NO").toString());
			paramMap.put("PA_ORDER_GB"	, hmSheet.get("PA_ORDER_GB").toString());
			String checkAddr = paLtonClaimService.compareAddress(paramMap);
			orderClaimVO.setSameAddr(checkAddr);
			
			break;

		case "31": case "41": case"46":
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
				
		String cLaimCode = hmSheet.get("CLAIM_CODE").toString();
		
		if (cLaimCode.length() ==6){
			orderClaimVO.setCsLgroup		(cLaimCode.substring(0,2));
			orderClaimVO.setCsMgroup		(cLaimCode.substring(2,4));
			orderClaimVO.setCsSgroup		(cLaimCode.substring(4,6));
			orderClaimVO.setCsLmsCode		(cLaimCode);		
			orderClaimVO.setStandardType	("0");     //// 기준내 :1 ,기준외 : 0
		}
		orderClaimVO.setClaimCode(cLaimCode);
		
		//TODO 배송비 0원보다 높을시 처리하는것을 배송주체 코드로 관리할지 고민해봐야함..
		//RT_HDELV_AMT(반품택배비) , RTN_RESP_TPNM(수거책임구분), COST_RESP_TP(비용책임구분)
		String shpChargeYn  = String.valueOf(hmSheet.get("CLM_LST_DLV_CST"));  
		if (shpChargeYn.equals("1")) {
			orderClaimVO.setShpfeeYn		("1");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
			orderClaimVO.setShipcostChargeYn("1"); 
		} else {
			orderClaimVO.setShpfeeYn		("0");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2
			orderClaimVO.setShipcostChargeYn("0"); 
		}
		
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
	@SuppressWarnings("unchecked")
	public void asyncGoodsModify(HttpServletRequest request, ParamMap asyncMap, ParamMap bodyMap, PaLtonGoodsVO paLtonGoods, List<PaLtonGoodsdtMappingVO> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		ParamMap paramMap = new ParamMap();
		try {
			
			map = paLtonConnectUtil.apiGetObjectByLtn(asyncMap, bodyMap); // 통신
			
			ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) map.get("data");
			if(data.size() > 0 ) {
				if(Constants.PA_LTON_SUCCESS_CODE.equals(data.get(0).get("resultCode"))) {
					
					Map<String, Object> resultData = data.get(0);
					
					insertPaGoodsTransLog(paLtonGoods, resultData);
					
					String paGoodsCode = String.valueOf(resultData.get("spdNo"));
					
					if(!"null".equals(paGoodsCode)){
						paLtonGoods.setSpdNo(paGoodsCode);
						
						rtnMsg = paLtonGoodsService.savePaLtonGoodsTx(paLtonGoods, goodsdtMapping, paPromoTargetList);
					}
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						asyncMap.put("code", "500");
						asyncMap.put("message", paLtonGoods.getGoodsCode() + " : 통신성공 후 저장중 에러");
					} else {
						asyncMap.put("code", "200");
						asyncMap.put("message", "OK");
					}
				} else {
					asyncMap.put("code", "500");
					asyncMap.put("message", paLtonGoods.getGoodsCode() + data.get(0).get("resultMessage"));
					if(data.get(0).get("resultMessage").toString().indexOf("해외배송 사용 셀러만") > -1) {
						paramMap.put("goodsCode", paLtonGoods.getGoodsCode());
						paramMap.put("paSaleGb", "30");
						paramMap.put("paStatus", "90");
						paramMap.put("spdNo", data.get(0).get("spdNo"));
						paramMap.put("returnNote", data.get(0).get("resultMessage"));
						paramMap.put("paCode", paLtonGoods.getPaCode());
						executedRtn = paLtonGoodsService.updatePaLtonPaStatus(paramMap);
						
						if(executedRtn != 1) {
							asyncMap.put("code", "500");
							asyncMap.put("message", paramMap.get("goodsCode") + " : UPDATE TPALTONGOODS FAIL");
						}
					}
				}
			} else {
				asyncMap.put("code", "500");
				asyncMap.put("message", paLtonGoods.getGoodsCode() + map.get("message").toString());
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
	
	public void insertPaGoodsTransLog(PaLtonGoodsVO paLtonGoods, Map<String, Object> resultData) throws Exception {
		String paGoodsCode = String.valueOf(resultData.get("spdNo"));
		String dateTime = systemService.getSysdatetimeToString();;
		
		PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
		paGoodsTransLog.setGoodsCode(paLtonGoods.getEpdNo());
		paGoodsTransLog.setPaCode(paLtonGoods.getPaCode());
		paGoodsTransLog.setItemNo(paGoodsCode.equals("null") ? paLtonGoods.getEpdNo() : paGoodsCode);
		paGoodsTransLog.setRtnCode(String.valueOf(resultData.get("resultCode")));
		paGoodsTransLog.setRtnMsg(String.valueOf(resultData.get("resultMessage")));
		paGoodsTransLog.setSuccessYn(paGoodsTransLog.getRtnCode().equals(Constants.PA_LTON_SUCCESS_CODE) ? "1" : "0");
		paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		paGoodsTransLog.setProcId(Constants.PA_LTON_PROC_ID);
		paLtonGoodsService.insertPaLtonGoodsTransLogTx(paGoodsTransLog);
	}
	
	@Async
	public void spPagoodsSyncLton(HttpServletRequest request, String goodsCode, String userId) throws Exception {
		
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
		List<HashMap<String, String>> curCheckDtCnt = null;
		List<PaLtonGoodsVO> curGoodsTransQty = null;
		List<PaLtonGoodsdtMappingVO> curGoodsDtTransQty = null;
		
		String resultMsg = "";
		String dateTime = systemService.getSysdatetimeToString();
		StringBuffer sb = null;
		int conditionDay = 2;
		int eTVLimitMargin = 0;
		
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paGroupCode", "08");
		paramMap.put("dateTime", dateTime);
		paramMap.put("conditionDay", conditionDay);		
		paramMap.put("comparePaCode", "81");
		paramMap.put("feeCode", "O697");
		paramMap.put("minMarginCode", "82");
		paramMap.put("minPriceCode", "83");
		
		paramMap.put("siteGb", userId);
		paramMap.put("apiCode", "IF_PACOMMON_00_002");
		
		log.info("Step1. 롯데ON 상품이미지 동기화 START");
		curImageInfo = paCommonService.selectCurImageInfoList(paramMap);
		if(curImageInfo.size() > 0) {
			sb = new StringBuffer();
			for(PaGoodsImage curImageInfoTarget : curImageInfo) {
				try {
					curImageInfoTarget.setModifyId("PALTON");
					curImageInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					curImageInfoTarget.setRemark(dateTime);
					resultMsg = paCommonService.saveCurImageInfoTx(curImageInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step1 상품이미지 동기화 롯데ON Fail > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
						sb.append(curImageInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step1 상품이미지 동기화 롯데ON Sucess > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
				} catch(Exception e) {
					log.info("Step1. 롯데ON 상품이미지 동기화 : " + e.getMessage());
					paramMap.put("code", "500");
					paramMap.put("message", "Step1. 롯데ON 상품이미지 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}
		}
		log.info("Step1. 롯데ON 상품이미지 동기화 END");
		
		
		log.info("Step2. 롯데ON 상품가격 동기화 START");
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
						
						stopSaleParam.put("paGroupCode", "08");
						stopSaleParam.put("paCode", curPriceInfoTarget.getPaCode());
						stopSaleParam.put("goodsCode", curPriceInfoTarget.getGoodsCode());
						stopSaleParam.put("dateTime", dateTime);
						stopSaleParam.put("userId", "PALTON");
						stopSaleParam.put("paGoodsCode", curPriceInfoTarget.getPaGoodsCode());
						stopSaleParam.put("note", "마진10이하 혹은 판매가100원 미만/모바일eTV 마진");
						stopSaleParam.put("priceStopSale", "Y");
						
						//연동제외처리
						resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
					} else {
						curPriceInfoTarget.setModifyId("PALTON");
						curPriceInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						curPriceInfoTarget.setDateTime(dateTime);
						resultMsg = paCommonService.saveCurPriceInfoTx(curPriceInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step2 상품가격 동기화 롯데ON Fail > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
						sb.append(curPriceInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step2 상품가격 동기화 롯데ON Sucess > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
				}
			} catch(Exception e) {
				log.info("Step2. 롯데ON 상품가격 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step2. 롯데ON 상품가격 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step2. 롯데ON 상품가격 동기화 END");
		
		
		log.info("Step3. 롯데ON 고객부담배송비 동기화 START");
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
							
							stopSaleParam.put("paGroupCode", "08");
							stopSaleParam.put("paCode", curShipStopSaleTarget.get("PA_CODE").toString());
							stopSaleParam.put("goodsCode", curShipStopSaleTarget.get("GOODS_CODE").toString());
							stopSaleParam.put("dateTime", dateTime);
							stopSaleParam.put("userId", "PALTON");
							stopSaleParam.put("paGoodsCode", curShipStopSaleTarget.get("PA_GOODS_CODE").toString());
							stopSaleParam.put("note", "배송비유료변경");
							stopSaleParam.put("priceStopSale", "N");
							
							//연동제외처리
							resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
						}
						
					} else {
						curShipCostInfoTarget.setModifyId("PALTON");
						curShipCostInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						resultMsg = paCommonService.saveCurShipCostInfoTx(curShipCostInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step3 고객부담배송비 롯데ON Fail");
						sb.append(curShipCostInfoTarget.getEntpCode() + "|" + curShipCostInfoTarget.getShipCostCode() + ", ");
						continue;
					}
					log.info("Step3 고객부담배송비 롯데ON Sucess");
				}
			} catch(Exception e) {
				log.info("Step3. 롯데ON 고객부담배송비 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step3. 롯데ON 고객부담배송비 동기화 : " + e.getMessage() + "INFO : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step3. 롯데ON 고객부담배송비 동기화 END");
		
		
		log.info("Step4. 롯데ON 출고/회수지 동기화 START");
		curEntpSlipInfo = paCommonService.selectCurEntpSlipInfoList(paramMap);
		if(curEntpSlipInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaEntpSlip curEntpSlipInfoTarget : curEntpSlipInfo) {
					
					curEntpSlipInfoTarget.setModifyId("PALTON");
					curEntpSlipInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					resultMsg = paCommonService.saveCurEntpSlipInfoTx(curEntpSlipInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step4 출고/회수지 동기화 롯데ON Fail");
						sb.append(curEntpSlipInfoTarget.getEntpCode() + ", ");
						continue;
					}
					log.info("Step4 출고/회수지 동기화 롯데ON Sucess");
				}
			} catch(Exception e) {
				log.info("Step4. 롯데ON 출고/회수지 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step4. 롯데ON 출고/회수지 동기화 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step4. 롯데ON 출고/회수지 동기화 END");
		
		
		log.info("Step5. 롯데ON 상품판매단계 동기화 START");
		curSaleStop = paCommonService.selectCurSaleStopList(paramMap);
		if(curSaleStop.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curSaleStopTarget : curSaleStop) {
					
					stopSaleParam.put("paGroupCode", "08");
					stopSaleParam.put("paCode", curSaleStopTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curSaleStopTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PALTON");
					stopSaleParam.put("paGoodsCode", curSaleStopTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "SK스토아상품판매중단");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step5 상품판매단계 동기화 롯데ON Fail > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
						sb.append(curSaleStopTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step5 상품판매단계 동기화 롯데ON Sucess > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step5. 롯데ON 상품판매단계 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step5. 롯데ON 상품판매단계 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step5. 롯데ON 상품판매단계 동기화 END");
		
		
		log.info("Step6. 롯데ON 행사 종료 상품 마진 체크 START");
		curEventMargin = paCommonService.selectCurEventMarginList(paramMap);
		if(curEventMargin.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curEventMarginTarget : curEventMargin) {
					paramMap.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					//minMarginPrice = paCommonService.selectMinMarginPrice(paramMap);
					//selectCurEventMarginList 안에서 체크
					//if(ComUtil.objToDouble(curEventMarginTarget.get("MARGIN_RATE")) < ComUtil.objToDouble(minMarginPrice.get("MIN_MARGIN"))) {
					stopSaleParam.put("paGroupCode", "08");
					stopSaleParam.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curEventMarginTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PALTON");
					stopSaleParam.put("paGoodsCode", curEventMarginTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "행사종료");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step6 행사 종료 상품 마진 체크 롯데ON Fail > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
						sb.append(curEventMarginTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step6 행사 종료 상품 마진 체크 롯데ON Sucess > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
					//}					
				}
			} catch(Exception e) {
				log.info("Step6. 롯데ON 행사 종료 상품 마진 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step6. 롯데ON 행사 종료 상품 마진 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step6. 롯데ON 행사 종료 상품 마진 체크 END");
		
		log.info("Step7. 롯데ON 단품 개수 체크 START");
		curCheckDtCnt = paCommonService.selectCurCheckDtCntList(paramMap);
		if(curCheckDtCnt.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curCheckDtCntTarget : curCheckDtCnt) {					
					stopSaleParam.put("paGroupCode", "08");
					stopSaleParam.put("paCode", curCheckDtCntTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curCheckDtCntTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PALTON");
					stopSaleParam.put("paGoodsCode", curCheckDtCntTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "단품개수 500개 초과");
					stopSaleParam.put("paStatus", "90");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step7 단품 개수 체크 롯데ON Fail > GOODS_CODE : " + curCheckDtCntTarget.get("GOODS_CODE").toString());
						sb.append(curCheckDtCntTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step7 단품 개수 체크 롯데ON Sucess > GOODS_CODE : " + curCheckDtCntTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step7. 롯데ON 단품 개수 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step6. 롯데ON 단품 개수 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step7. 롯데ON 단품 개수 체크 END");
		
		log.info("Step8. 롯데ON 상품 판매수량 체크 START");
		curGoodsTransQty = paCommonService.selectCurGoodsTransQtyList(paramMap);
		if(curGoodsTransQty.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaLtonGoodsVO curGoodsTransQtyTarget : curGoodsTransQty) {
					if("0".equals(curGoodsTransQtyTarget.getTransOrderAbleQty())) {
						curGoodsTransQtyTarget.setTransSaleYn("1");
					}
					curGoodsTransQtyTarget.setModifyId("PALTON");
					curGoodsTransQtyTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					
					resultMsg = paCommonService.saveCurGoodsTransQtyTx(curGoodsTransQtyTarget);
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step8 상품 판매수량 체크 롯데ON Fail > GOODS_CODE : " + curGoodsTransQtyTarget.getGoodsCode());
						sb.append(curGoodsTransQtyTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step8 상품 판매수량 체크 롯데ON Sucess > GOODS_CODE : " + curGoodsTransQtyTarget.getGoodsCode());
				}
			} catch(Exception e) {
				log.info("Step8. 롯데ON 상품 판매수량 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step8. 롯데ON 상품 판매수량 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step8. 롯데ON 상품 판매수량 체크 END");
		
		log.info("Step9. 롯데ON 단품 판매수량 체크 START");
		curGoodsDtTransQty = paCommonService.selectCurGoodsDtTransQtyList(paramMap);
		if(curGoodsDtTransQty.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaLtonGoodsdtMappingVO curGoodsDtTransQtyTarget : curGoodsDtTransQty) {
					if("0".equals(curGoodsDtTransQtyTarget.getTransOrderAbleQty()) ||
					   ("0".equals(curGoodsDtTransQtyTarget.getTransOrderAbleQty()) && Integer.parseInt(curGoodsDtTransQtyTarget.getTransOrderAbleQty()) < Integer.parseInt(curGoodsDtTransQtyTarget.getNewTransQty()))) {
						curGoodsDtTransQtyTarget.setTransSaleYn("1");
					}
					
					curGoodsDtTransQtyTarget.setTransStockYn("1");
					curGoodsDtTransQtyTarget.setModifyId("PALTON");
					curGoodsDtTransQtyTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					
					resultMsg = paCommonService.saveCurGoodsDtTransQtyTx(curGoodsDtTransQtyTarget);
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step9 단품 판매수량 체크 롯데ON Fail > GOODS_CODE : " + curGoodsDtTransQtyTarget.getGoodsCode());
						sb.append(curGoodsDtTransQtyTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step9 단품 판매수량 체크 롯데ON Sucess > GOODS_CODE : " + curGoodsDtTransQtyTarget.getGoodsCode());
				}
			} catch(Exception e) {
				log.info("Step9. 롯데ON 단품 판매수량 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step9. 롯데ON 단품 판매수량 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step9. 롯데ON 단품 판매수량 체크 END");
		paCommonService.checkMassModifyGoods("08");
		return;
	}

}
