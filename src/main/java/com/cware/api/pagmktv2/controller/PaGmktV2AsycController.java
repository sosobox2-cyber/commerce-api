package com.cware.api.pagmktv2.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cware.framework.common.exception.ProcessException;
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
import com.cware.netshopping.domain.PaGmktGoodsVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGmkCancel;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pagmkt.cancel.service.PaGmktCancelService;
import com.cware.netshopping.pagmkt.claim.service.PaGmktClaimService;
import com.cware.netshopping.pagmkt.exchange.service.PaGmktExchangeService;
import com.cware.netshopping.pagmkt.goods.service.PaGmktGoodsService;
import com.cware.netshopping.pagmkt.order.service.PaGmktOrderService;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;
import com.cware.netshopping.pagmkt.util.rest.PaGmktCancelRest;
import com.cware.netshopping.pagmkt.util.rest.PaGmktGoodsRest;

@Controller("com.cware.api.pagmktv2.PaGmktV2AsycController")
@RequestMapping(value="/pagmktv2/claim")
public class PaGmktV2AsycController extends AbstractController {

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pagmkt.claim.PaGmktClaimService")
	private PaGmktClaimService paGmktClaimService;
	
	@Resource(name = "pacommon.claim.paclaimService")
	private PaClaimService paclaimService;
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paorderService;
	
	@Resource(name = "pagmkt.order.PaGmktOrderService")
	private PaGmktOrderService paGmktOrderService;
	
	@Resource(name = "pagmkt.exchange.PaGmktExchangeService")
	private PaGmktExchangeService paGmktExchangeService;
		
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Resource(name = "pagmkt.goods.paGmktGoodsService")
	private PaGmktGoodsService paGmktGoodsService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@Resource(name = "pacommon.common.paCommonDAO")
    private PaCommonDAO pacommonDAO;
	
	@Resource(name = "pagmkt.cancel.PaGmktCancelService")
	private PaGmktCancelService paGmktCancelService;
	
	/**
	 * G마켓 무통장 주문접수 입금 데이터 처리 
	 * @return Map
	 * @throws Exception
	 */	
	//@Async
	public void preOrderUpdateAsync(HashMap<String, String> hmSheet) throws Exception {
	
		String orderNo   	 = hmSheet.get("ORDER_NO").toString();  	 //PAY_NO
		String paOrderNo	 = hmSheet.get("PA_ORDER_NO").toString();    //PAY_NO
		String siteGb 	 	 = hmSheet.get("SITE_GB").toString();    //PAY_NO
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


			paShpAmt = paGmktOrderService.selectPaOrderShipCost(paramMap);
			addrMap  = paGmktOrderService.selectPaAddrInfo(paramMap);
			paramMap.put("paShpAmt"  	, paShpAmt);
			setChangedAddress(paramMap, addrMap);
			
			paorderService.upDateOrderTx(paramMap);
			
			paramMap.put("code","200");
			paramMap.put("message","UPDATE");
			
		}catch(Exception e){
			CommonUtil.dealException(e, paramMap);
			log.error(e.getMessage());
			paorderService.updatePaOrderMFailConfrimPreOrder(hmSheet, e.getMessage());
		}finally{
			try{
				CommonUtil.dealSuccess(paramMap);
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
	/**
	 * G마켓 주문접수 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */	
	//@Async
	public void orderInputAsync(HashMap<String, String> hmSheet, String localYn) throws Exception {
	
		List<Object> orderInputTargetDtList = new ArrayList<>();
		HashMap<String, Object>[] resultMap = null;
		int targetSize = 0;
		OrderInputVO[] orderInputVO = null;
		ParamMap paramMap = new ParamMap();
		
		try{
			paramMap.put("apiCode"	, "orderInputAsync");
			paramMap.put("siteGb"	, hmSheet.get("SITE_GB").toString());  //apiTracking 용
			
			orderInputTargetDtList = paGmktOrderService.selectOrderInputTargetDtList(hmSheet); 
			targetSize = orderInputTargetDtList.size();
			
			checkTargetSize(targetSize,"selectPreOrderInputTargetDtList");
			
			orderInputVO = new OrderInputVO[targetSize];
			setOrderInputVO(orderInputTargetDtList,orderInputVO, localYn);
			
			/**2019.03.12 백호선 *
			 * 안정화 되면 newSaveOrderTx 만 사용하게 수정해야함.
			 * */
			String useNewPaSaveYN = paorderService.getConfig("USE_NEW_PA_SAVE_YN");
			
			switch(useNewPaSaveYN){
			
			case "Y" :
				resultMap = paorderService.newSaveOrderTx(orderInputVO);
				break;
				
			default :
				if(orderInputVO[0].getDoFlag().equals("10")){
					resultMap = paorderService.newSaveOrderTx(orderInputVO);
				}else{
					resultMap = paorderService.saveOrderTx(orderInputVO);
				}
			}

			paramMap.put("code","200");
			paramMap.put("message","OK");
			
			
		}catch(Exception e){
			CommonUtil.dealException(e, paramMap);
			log.error(e.getMessage());
			updatePaOrdermTxForRollback(orderInputVO, e);	
		
		}finally{
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
		
		//=재고 부족 또는 판매가 불일치 문제로 주문 거부 API 호출  TODO LIST 취소때 구현
		refuseOrder(resultMap);	
	}	
	
	/**
	 * G마켓 주문 취소 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void cancelInputAsync(HashMap<String, Object> cancelMap) throws Exception{
		
		HashMap<String, Object> hmSheet = null;

		ParamMap paramMap = new ParamMap();
		paramMap.setParamMap(cancelMap);
		paramMap.replaceCamel();
		
		
		List<Object> cancelInputTargetDtList = paGmktOrderService.selectCancelInputTargetDtList(paramMap); 
		checkTargetSize(cancelInputTargetDtList.size(),"selectCancelInputTargetDtList");
		hmSheet = (HashMap<String, Object>) cancelInputTargetDtList.get(0);
		
	
		switch(hmSheet.get("PRE_CANCEL_YN").toString()){
		
			case "0": //=일반 취소
				saveCancel(hmSheet);
				break;
			
			default : //= 기취소	
				savePreCancel(hmSheet);
				break;
		}
		
				
	}
	
	private void saveCancel(HashMap<String, Object> hmSheet) throws Exception{
		
		CancelInputVO cancelInputVO  = null;
		String cancelCode = null;
	
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"	, "cancelInputAsync");
		paramMap.put("siteGb"	, hmSheet.get("SITE_GB").toString());

		try {
			cancelInputVO = new CancelInputVO();

			cancelInputVO.setMappingSeq		(hmSheet.get("MAPPING_SEQ").toString());
			cancelInputVO.setOrderNo		(hmSheet.get("ORDER_NO").toString());
			cancelInputVO.setOrderGSeq		(hmSheet.get("ORDER_G_SEQ").toString());
			cancelInputVO.setCancelQty		(Integer.parseInt(String.valueOf(hmSheet.get("PA_PROC_QTY"))));
			cancelInputVO.setProcId			(hmSheet.get("SITE_GB").toString());

			cancelCode = hmSheet.get("CANCEL_CODE").toString();
			if (cancelCode == null || "".equals(cancelCode) || cancelCode.length() != 6 ) cancelCode = "620298";
			cancelInputVO.setCancelCode(cancelCode);

			String useNewPaSaveYN = paorderService.getConfig("USE_NEW_PA_SAVE_YN");
			
			switch(useNewPaSaveYN){
	
			case "Y" :
				paorderService.newSaveCancelTx(cancelInputVO);
				break;
			default : 
				if(hmSheet.get("PA_DO_FLAG").toString().equals("10")){
					paorderService.newSaveCancelTx(cancelInputVO);
				}else{
					paorderService.saveCancelTx(cancelInputVO);
				}
			}	
			
			paramMap.put("code","200");
			paramMap.put("message","SAVED");
		
		}catch(Exception e){
			CommonUtil.dealException(e, paramMap);
			CancelInputVO[] cancelInputArray = new CancelInputVO[1];
			cancelInputArray[0] = cancelInputVO;
			updatePaOrdermTxForRollback(cancelInputArray , e);
		}finally{
			try{
			CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
	}
	
	private void savePreCancel(HashMap<String, Object> hmSheet) throws Exception{
		
		ParamMap preCancelMap = new ParamMap();
		int executedRtn =  0;
		
		preCancelMap.setParamMap(hmSheet);
		preCancelMap.replaceCamel();
		
		//=pa.before_order_create_cancel = 주문생성 이전 취소건
		preCancelMap.put("preCancelReason", getMessage("pa.before_order_create_cancel"));
		
		executedRtn = paGmktOrderService.updatePreCancelYnTx(preCancelMap);
		
		if(executedRtn != 1){
			try{
				ParamMap resultParam = new ParamMap();
				resultParam.put("apiCode"		, "cancelInputAsync");
				resultParam.put("siteGb"		, preCancelMap.get("siteGb").toString());
				resultParam.put("code"			, "500");
				resultParam.put("message"		, "TPAORDERM(pre_cancel_yn) UPDATE");
				CommonUtil.dealSuccess(resultParam);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
	}
	
		
	/**
	 * G마켓 주문 반품 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void orderClaimAsync(HashMap<String, Object> claimMap, String isLocalYn) throws Exception{
		
		HashMap<String, Object> hmSheet = null;
		List<Object> orderClaimTargetDtList = null;
		OrderClaimVO orderClaimVO = null;
		ParamMap paramMap = new ParamMap();
		paramMap.setParamMap(claimMap);
		paramMap.replaceCamel();
		paramMap.put("apiCode", "orderClaimAsync");
		CommonUtil.setParams(paramMap);

		try {
			
			
			if(paramMap.getString("paOrderGb").equals("30")){
				orderClaimTargetDtList = paGmktClaimService.selectOrderCalimTargetDt30List(paramMap); 
			}else{
				orderClaimTargetDtList = paGmktClaimService.selectOrderCalimTargetDt20List(paramMap);
			}
			
			checkTargetSize(orderClaimTargetDtList.size(),"selectOrderCalimTargetDtList");
			hmSheet = (HashMap<String, Object>) orderClaimTargetDtList.get(0);
			
			orderClaimVO = new OrderClaimVO();
			orderClaimVO = setOrderClaimVO(hmSheet,isLocalYn);
			paclaimService.saveOrderClaimTx(orderClaimVO);
			
			paramMap.put("code", "200");;
			paramMap.put("message","SAVED");
			
			
		}catch(Exception e){			
			log.error(e.getMessage());
			CommonUtil.dealException(e, paramMap);
			OrderClaimVO[] orderClaimList = new OrderClaimVO[1];
			orderClaimList[0] = orderClaimVO;
			updatePaOrdermTxForRollback(orderClaimList, e);		
		}finally{
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
	}
	
	/**
	 * G마켓 반품취소 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	//@Async
	public void claimCancelAsync(HashMap<String, Object> claimMap) throws Exception{
		HashMap<String, Object> hmSheet = null;
		List<Object> cancelInputTargetDtList = null;
		OrderClaimVO orderClaimVO = null;
		ParamMap paramMap = new ParamMap();
		paramMap.setParamMap(claimMap);
		paramMap.replaceCamel();
		paramMap.put("apiCode", "claimCancelAsync");
		
		try {
			cancelInputTargetDtList = paGmktClaimService.selectClaimCancelTargetDtList(paramMap); 
			checkTargetSize(cancelInputTargetDtList.size(),"selectClaimCancelTargetDtList");
			hmSheet = (HashMap<String, Object>) cancelInputTargetDtList.get(0);

			switch(hmSheet.get("PRE_CANCEL_YN").toString()){
			
				case "0": //일반적인 반품 취소
					orderClaimVO = setOrderClaimVO(hmSheet, "N");
					paclaimService.saveClaimCancelTx(orderClaimVO);	
					break;
				
				case "1": //기취소
					preCancel(hmSheet, getMessage("pa.before_claim_create_cancel"));
					break;	
			}
			paramMap.put("code", "200");;
			paramMap.put("message","SAVED");
			
			
		}catch(Exception e){
			log.error(e.getMessage());
			CommonUtil.dealException(e, paramMap);
			OrderClaimVO[] orderClaimList = new OrderClaimVO[1];
			orderClaimList[0] = orderClaimVO;
			updatePaOrdermTxForRollback(orderClaimList, e);
		}finally{
			try{
			CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
	}
	
	/**
	 * G마켓 주문 교환 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	public void orderExchangeAsync(HashMap<String, Object> exchangeMap, String isLocalYn) throws Exception{
		HashMap<String, Object>[] resultMap 		= null;
		HashMap<String, Object> hmSheet 			= null;
		List<Object> orderChangeTargetDtList		= null;
		OrderClaimVO orderClaimVO 					= null;
		OrderClaimVO[] orderClaimVOArray 			= null;
		boolean preCanYn						    = false;
		int targetSize 								= 0;
		ParamMap paramMap 							= new ParamMap();
		paramMap.setParamMap(exchangeMap);
		paramMap.replaceCamel();
		paramMap.put("apiCode", "orderExchangeAsync");

		try {
			orderChangeTargetDtList = paGmktExchangeService.selectOrderChangeTargetDtList(paramMap); 
			targetSize=  orderChangeTargetDtList.size();
			checkTargetSize(targetSize,"orderChangeTargetDtList"); //TODO need to change Method
			orderClaimVOArray = new OrderClaimVO[targetSize];

			for(int j = 0; targetSize > j; j++){

				hmSheet = (HashMap<String, Object>) orderChangeTargetDtList.get(j);

				switch(hmSheet.get("PRE_CANCEL_YN").toString()){
				
				case "0": //교환
					orderClaimVO = new OrderClaimVO();
					orderClaimVO = setOrderClaimVO(hmSheet, isLocalYn);
					orderClaimVOArray[j] = orderClaimVO;	
					preCanYn = false;
					break;
					
				case "1": // Hold 중 교환 취소 요청이 들어온 상태..
					preCancel(hmSheet, getMessage("pa.before_exchange_create_cancel"));
					preCanYn = true;
					break;
				}
			}
			
			if(!preCanYn){
				resultMap = paclaimService.saveOrderChangeTx(orderClaimVOArray);
				checkStockValidation(resultMap);
			}
			
			paramMap.put("code", "200");;
			paramMap.put("message","SAVED");
					
		}catch(Exception e){			
			log.error(e.getMessage());
			CommonUtil.dealException(e, paramMap);
			updatePaOrdermTxForRollback(orderClaimVOArray, e);	
		}finally{
			try{
			CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
		
		//2018-11-30 hsbaek 이 기능은 판매취소(주문거부) + 상품 품절이니 일단 재고체크는 BO 화면에서 막고 CS처리 하도록 협의
		//refuseOrder(resultMap, request); //교환 수량 부족시 품절처리.. 
		
	}
	
	private void checkStockValidation(HashMap<String, Object>[] resultMap)  throws Exception {
		for(int i = 0; i < resultMap.length; i++ ) {
			if(resultMap[i].get("RESULT_CODE").toString().equals("100001")){
				paGmktExchangeService.updateExchangeStatue(resultMap[i].get("MAPPING_SEQ").toString());	
			}
		}
	}

	/**
	 * G마켓 교환 취소 데이터 생성 
	 * @return Map
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	public void exchangeCancelAsync(HashMap<String, Object> exchangeMap, String isLocalYn) throws Exception{
		HashMap<String, Object> hmSheet = null;
		List<Object> changeCancelTargetDtList = null;
		OrderClaimVO orderClaimVO = null;
		OrderClaimVO[] orderClaimVOArray = null;
		ParamMap paramMap = new ParamMap();
		paramMap.setParamMap(exchangeMap);
		paramMap.replaceCamel();
		paramMap.put("apiCode", "exchangeCancelAsync");

		int targetSize  = 0;
		boolean preCanYn = false;

		try {
					
			changeCancelTargetDtList = paGmktExchangeService.selectChangeCancelTargetDtList(paramMap); 
			targetSize=  changeCancelTargetDtList.size();
			checkTargetSize(targetSize,"changeCancelTargetDtList"); //TODO need to change Method
			orderClaimVOArray = new OrderClaimVO[targetSize];

			for(int j = 0; targetSize > j; j++){
				hmSheet = (HashMap<String, Object>)changeCancelTargetDtList.get(j);
				
				switch(hmSheet.get("PRE_CANCEL_YN").toString()){
				
				case "0":
					orderClaimVO = new OrderClaimVO();
					orderClaimVO = setOrderClaimVO(hmSheet, "N");
					orderClaimVOArray[j] = orderClaimVO;
					preCanYn = false;
					break;
					
				case "1": //기취소
					preCancelOriginalExchange(hmSheet.get("MAPPING_SEQ").toString());
					preCancel(hmSheet, getMessage("pa.before_change_create_cancel"));
					preCanYn = true;
					break;
				}//end of switch

			}
			
			if(!preCanYn){
				paclaimService.saveChangeCancelTx(orderClaimVOArray);
			}

			paramMap.put("code", "200");;
			paramMap.put("message","SAVED");
					
		}catch(Exception e){
			CommonUtil.dealException(e, paramMap);
			log.error(e.getMessage());
			updatePaOrdermTxForRollback(orderClaimVOArray, e);
		}finally{
			try{
			CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
	}
	
	
		
	private OrderClaimVO setOrderClaimVO(HashMap<String, Object> hmSheet, String isLocalYn) throws Exception{
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
			checkAddr = paGmktClaimService.compareAddress(paramMap);
			orderClaimVO.setSameAddr(checkAddr);
			
			break;
		
		case "31":
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
			checkAddr = paGmktClaimService.compareAddress(paramMap);
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
			checkAddr = paGmktClaimService.compareAddress(paramMap);
			orderClaimVO.setSameAddr(checkAddr);
			
			break;
		}
				
		String cLaimCode = hmSheet.get("CLAIM_CODE").toString();
		
        if(hmSheet.get("REASON_CODE") !=null && "99".equals(hmSheet.get("REASON_CODE").toString())) { //당사상품 직권취소건
            cLaimCode = hmSheet.get("CANCEL_CLAIM_CODE").toString();
        }
        
		if (cLaimCode.length() ==6){
			orderClaimVO.setCsLgroup		(cLaimCode.substring(0,2));
			orderClaimVO.setCsMgroup		(cLaimCode.substring(2,4));
			orderClaimVO.setCsSgroup		(cLaimCode.substring(4,6));
			orderClaimVO.setCsLmsCode		(cLaimCode);		
			orderClaimVO.setStandardType	("0");     //// 기준내 :1 ,기준외 : 0
		}
		orderClaimVO.setClaimCode(cLaimCode);
		
		
		Long shpFeeAmt  = Long.parseLong(String.valueOf(hmSheet.get("CLM_LST_DLV_CST")));
		orderClaimVO.setShpFeeAmt			(shpFeeAmt);
		
		//귀책자에 따른 shpfeeYn 결정
		String reason = String.valueOf(hmSheet.get("REASON"));
		if("1".equals(reason)) {
			orderClaimVO.setShpfeeYn		("1"); //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
			orderClaimVO.setShipcostChargeYn("1"); //나중에 걷어내야함..
		} else {
			orderClaimVO.setShpfeeYn		("0");	
			orderClaimVO.setShipcostChargeYn("0");
		}
		
		//출하지시 이후 취소건은 반품인데, 이경우 배송비 부과여부를 무상으로 처리한다.
		if(("1").equals(orderClaimVO.getOutBefClaimGb()) && "20".equals(hmSheet.get("PA_ORDER_GB").toString())){
			orderClaimVO.setShpfeeYn		("0");
			orderClaimVO.setIs20Claim		(true);
		}else{
			orderClaimVO.setIs20Claim		(false);
		}
		
		if (hmSheet.get("REASON_CODE") !=null && "99".equals(hmSheet.get("REASON_CODE").toString())) { // 당사상품 직권취소건
			orderClaimVO.setShpfeeYn		("0");	
			orderClaimVO.setShipcostChargeYn("0");
		}
		
		return orderClaimVO;
	}
		
	@SuppressWarnings("unchecked")
	private void setOrderInputVO(List<Object> orderInputTargetDtList, OrderInputVO[] orderInputVO , String isLocalYn) throws Exception{
		HashMap<String, String> orderMap = null;
		String zipCode = null;
		String type	   = null;

		int targetSize = orderInputTargetDtList.size();
		for(int j = 0; targetSize > j; j++){
			orderMap = (HashMap<String, String>) orderInputTargetDtList.get(j);
			
			orderInputVO[j] = new OrderInputVO(); 		
			orderInputVO[j].setPaOrderCode				(orderMap.get("PA_ORDER_NO").toString());
			orderInputVO[j].setApplyDate				(orderMap.get("APPLY_DATE").toString());
			orderInputVO[j].setPaGoodsCode				(String.valueOf(orderMap.get("PA_GOODS_CODE")));
			orderInputVO[j].setShpFeeCost				(Long.parseLong(String.valueOf(orderMap.get("SHPFEE_COST"))));
			orderInputVO[j].setPaCode					(orderMap.get("PA_CODE").toString());
			orderInputVO[j].setMappingSeq				(orderMap.get("MAPPING_SEQ").toString());
			orderInputVO[j].setMediaCode				(orderMap.get("MEDIA_CODE").toString());
			orderInputVO[j].setOrderDate				(orderMap.get("ORDER_DATE").toString());
			orderInputVO[j].setGoodsCode				(orderMap.get("GOODS_CODE").toString());
			orderInputVO[j].setGoodsdtCode				(orderMap.get("GOODSDT_CODE").toString());
			orderInputVO[j].setOrderQty					(Integer.parseInt(String.valueOf(orderMap.get("ORDER_QTY"))));
			orderInputVO[j].setRsaleAmt					(Double.parseDouble(String.valueOf(orderMap.get("RSALE_PRICE"))));
			orderInputVO[j].setSupplyPrice				(Double.parseDouble(String.valueOf(orderMap.get("SUPPLY_PRICE"))));
			orderInputVO[j].setCustName					(orderMap.get("CUST_NAME").toString());
			orderInputVO[j].setCustChar					("99");
			orderInputVO[j].setCustTel1					(orderMap.get("CUST_TEL1").toString().replace("-", ""));
			orderInputVO[j].setCustTel2					(orderMap.get("CUST_TEL2").toString().replace("-", ""));
			orderInputVO[j].setApplyDate				(orderMap.get("APPLY_DATE").toString());
			orderInputVO[j].setSellerDcAmt				(Long.parseLong(String.valueOf(orderMap.get("SELLER_DC_AMT"))));
			orderInputVO[j].setLumpSumDcAmt				(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_DC_AMT"))));			
			orderInputVO[j].setLumpSumEntpDcAmt			(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_ENTP_DC_AMT"))));
			orderInputVO[j].setLumpSumOwnDcAmt			(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_OWN_DC_AMT"))));			
			orderInputVO[j].setMsg						(orderMap.get("MSG").toString());
			orderInputVO[j].setProcUser					(orderMap.get("SITE_GB").toString());
			orderInputVO[j].setReceiverName				(orderMap.get("RECEIVER_NAME").toString());
			orderInputVO[j].setReceiverTel				(orderMap.get("RECEIVER_TEL").toString().replace("-", ""));
			orderInputVO[j].setReceiverHp				(orderMap.get("RECEIVER_HP").toString().replace("-", ""));
			orderInputVO[j].setReceiverAddr				(orderMap.get("RECEIVER_ADDR").toString());	
			orderInputVO[j].setReceiveMethod			(orderMap.get("RECEIVE_METHOD").toString()); //C014
			orderInputVO[j].setPriceSeq					(orderMap.get("PRICE_SEQ").toString());
			orderInputVO[j].setDoFlag					(orderMap.get("DO_FLAG").toString());			
			orderInputVO[j].setIsLocalYn				(isLocalYn); //Reason == 개발은 주소정제 프로그램인 수지원넷이 작동안해서 이게 없으면 무조건 주소정제실패 미지정에러 나온다.
			orderInputVO[j].setPaGroupCode				(orderMap.get("PA_GROUP_CODE"));
			orderInputVO[j].setCouponPromoBdate(DateUtil.toTimestamp(orderMap.get("COUPON_PROMO_BDATE")));
			orderInputVO[j].setCouponPromoEdate(DateUtil.toTimestamp(orderMap.get("COUPON_PROMO_EDATE")));
			
			zipCode = orderMap.get("ZIPCODE").toString().replace("-", "").trim();
			if(zipCode.length() == 6){
				type  = "01"; //1:지번주소, 2:도로명주소
			}else{
				type  = "02";
			}
			
			orderInputVO[j].setAddrGbn					(type);           //지번, 도로명 여부
			orderInputVO[j].setStdAddr					(orderMap.get("RECEIVER_ADDR1").toString());     //RCVR_BASE_ADDR
			orderInputVO[j].setStdAddrDT				(orderMap.get("RECEIVER_ADDR2").toString());   //RCVR_DTLS_ADDR
			orderInputVO[j].setPostNo					(zipCode);    
			
			// 1) 가격 비교
			String paApplyDate = orderMap.get("APPLY_DATE").toString(); // TPAGOODSPRICE 기준 APPLY_DATE
			String stoaApplyDate = orderMap.get("STOA_APPLY_DATE").toString(); // TGOODSPRICE 기준 APPLY_DATE
			String paOrderDate = orderMap.get("ORDER_DATE_ORG").toString(); // 이베이 주문일시
			
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
			
			// 2) 프로모션 가격 계산 
			double sellerDcAmt   = Long.parseLong(String.valueOf(orderMap.get("SELLER_DISCOUNT_PRICE")))  / Integer.parseInt( String.valueOf( orderMap.get("ORDER_QTY") ));
			double arsDcAmt		 = Long.parseLong(String.valueOf(orderMap.get("SELLER_DC_AMT")))   		  / Integer.parseInt( String.valueOf( orderMap.get("ORDER_QTY") ));
			double lumpSumDcAmt	 = Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_DC_AMT"))) 		  / Integer.parseInt( String.valueOf( orderMap.get("ORDER_QTY") ));
			double outPromoPrice = Long.parseLong(String.valueOf(orderMap.get("OUT_PROMO_PRICE")));		
			double sumDcAmt		 = 0;
			
			if(sellerDcAmt < 1 ) {
				orderInputVO[j].setSellerDcAmtExists("N");
				continue;  // 11번가 비회원 및 판매자 할인 금액이 없는 경우 가격 비교 및 프로모션 데이터 세팅에 대한 수행을 하지 않는다.
			}
				
			sumDcAmt = arsDcAmt + lumpSumDcAmt + outPromoPrice;
			
			if(sumDcAmt != sellerDcAmt) {
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
			//orderInputVO[j].setOrderPaPromo			(selectOrderPaPromo(orderMap));//제휴OUT프로모션 : 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
		
		}//End of for
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
		
		return paGmktOrderService.selectOrderPromo(orderMap);
	}
	
	/**
	 * 제휴OUT프로모션 조회
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
	 * @param orderMap
	 * @return
	 * @throws Exception
	 */
	private OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception{
		return paGmktOrderService.selectOrderPaPromo(orderMap);
	}
	
	private void refuseOrder(HashMap<String, Object>[] resultMap){
		if(resultMap == null || resultMap.length < 1 ) return;
		
		HashMap<String, String> refusalMap = null;
		ParamMap paramMap = null;
		int targetSize = resultMap.length;
		boolean isSoldOut = false;
		boolean sendOk    = false;
		int excuteCnt	  = 0;
		
		try{
			
			for(int j = 0; targetSize > j; j++){
				
				paramMap = new ParamMap();
				paramMap.setParamMap(resultMap[j]);
				paramMap.replaceCamel();
				
				paramMap.put("apiCode"		, "CallRefusalOrder");

				refusalMap = paGmktOrderService.selectRefusalInfo(paramMap.getString("mappingSeq"));					

				
				if(paramMap.getString("resultCode").equals("100001")){ //=재고부족 or 판매불가 상태.
					isSoldOut = true;
				}else if (paramMap.getString("resultCode").equals("100002")){
					isSoldOut = false;
				}else continue;

				// = PaGmktOrderController 의 refusalOrderForSoldOut 또는 refusalOrderNotSoldOut 호출
				reqSaleRefusalProc(refusalMap, isSoldOut);
				sendOk = true;
				excuteCnt = paorderService.updatePreCancelOrder(paramMap.getString("mappingSeq"));
				if(excuteCnt < 1) log.error("Fail Update - updatePaOrdermTx By refuseOrder" + paramMap.getString("mappingSeq")); 

			}//End Of for	
			
			paramMap.put("code","200");
			paramMap.put("message", "refuseOrder - SEND");
			
		}catch(Exception e){			
			CommonUtil.dealException(e, paramMap);			
		}finally{
			
			if(!sendOk) return;
				
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
	}
		
	// = PaGmktOrderController 의 refusalOrderForSoldOut 또는 refusalOrderNotSoldOut 호출
	public void reqSaleRefusalProc(HashMap<String, String> reqMap, boolean isSoldOut) throws Exception {
		ParamMap configMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCancelRest();
		String ordCantMsg = "";
		
		if(isSoldOut){
			configMap.put("apiCode"		, "IF_PAGMKTAPI_V2_03_003");
			ordCantMsg = "품절취소반품";
		}else{
			configMap.put("apiCode"		, "IF_PAGMKTAPI_V2_03_004");
			ordCantMsg = "일괄취소반품";
		}
		configMap.put("paGroupCode"     , reqMap.get("PA_GROUP_CODE").toString());	
		configMap.put("paCode"			, reqMap.get("PA_CODE").toString());
		configMap.put("siteGb"			, reqMap.get("SITE_GB").toString());
		CommonUtil.setParams(configMap);
		configMap.put("urlParameter"	, reqMap.get("PA_ORDER_SEQ").toString());
		Map<String, Object> resMap = null;
		String response = "";
		String resMsg = "";

		reqMap.put("ORG_ORD_CAN_YN", ComUtil.objToStr(reqMap.get("ORDER_CANCEL_YN")));
		
		try {
			response = restUtil.getConnection(rest,  configMap);
			resMap = ComUtil.splitJson(response);
			resMsg = ComUtil.objToStr(resMap.get("Message").toString());
			
		}catch(Exception e) {
			log.error(e.getMessage());
			resMsg = e.getMessage();
		} finally {
			//일괄판매취소로 처리된 주문들만 update                                 
			if(!"".equals(ComUtil.objToStr(reqMap.get("ORDER_CANCEL_YN"))) && !"10".equals(ComUtil.objToStr(reqMap.get("ORDER_CANCEL_YN")))) {      
				if("Success".equals(resMsg)) {                        
					reqMap.put("ORDER_CANCEL_YN", "10");
					reqMap.put("RSLT_MESSAGE", ordCantMsg + " 성공");            
				} else {                                              
					reqMap.put("ORDER_CANCEL_YN", "90");          
					reqMap.put("RSLT_MESSAGE",  ordCantMsg + " 실패 " + resMsg);  
				}
				paorderService.updateOrderCancelYnTx(reqMap);     
				                                                      
				//상담생성 & 문자발송                                         
				paCommonService.saveOrderCancelCounselTx(reqMap);     
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
					
			paramMap.put("resultCode", "999999");
			paramMap.put("resultMessage", e.getMessage().length() > 1950 ? e.getMessage().substring(0,1950) : e.getMessage());
			paramMap.put("createYn", "0");
			
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
	
	private void checkTargetSize(int targetSize, String message) throws ProcessException, Exception{
		
		//TODO 추후에 TpaMonnioring에 Insert 하는 부분을 심자.
		if(targetSize < 1) throw processException("msg.no.select", new String[] { message });
		
	}
	
	
	private void preCancel(HashMap<String, Object> hmSheet, String message) throws Exception{
		
		int executedRtn= 0;
		ParamMap preCancelMap = new ParamMap();
		preCancelMap.setParamMap(hmSheet);
		preCancelMap.replaceCamel();
		//=pa.before_claim_create_cancel = 반품생성 이전 취소건
		preCancelMap.put("preCancelReason", message);

		executedRtn = paGmktOrderService.updatePreCancelYnTx(preCancelMap);
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
		
	}
	
	private void preCancelOriginalExchange(String mappingSeq) throws Exception{
		int executedRtn= 0;
		String originalExchangeMappingSeq = paGmktExchangeService.selectOriginalExchangeMappingSeq(mappingSeq);

		if(originalExchangeMappingSeq == null || originalExchangeMappingSeq.equals("")) return;
		
		ParamMap preCancelMap = new ParamMap();
		preCancelMap.put("preCancelYn"	, "1");
		preCancelMap.put("mappingSeq"	, originalExchangeMappingSeq);
		preCancelMap.put("preCancelReason","교환 취소로 인한 원 교환건 취소");
		
		executedRtn = paGmktOrderService.updatePreCancelYnTx(preCancelMap);
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
		
	}
	
	@Async
	@SuppressWarnings("unchecked")
	public void putGoods(HttpServletRequest request, ParamMap paramMap) throws Exception{
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		
		//paramMap.put("siteGb", "afterset");		
		log.info("상품코드 "+paramMap.get("goodsCode").toString());		
		try{						
			//= Step 1) select 테이블 후 urlParameter 셋팅
			//paramMap = goodsInfoValidation(paramMap);
			//if(!paramMap.getString("code").equals("200")){
			//	savePaGoodsInsertFail(paramMap);
			//	return;
			//}
			
			//= Step 2) 통신
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			Map<String,Object> siteDetail = (Map<String, Object>) resMap.get("siteDetail");
			Map<String,Object> gmktSiteDetail = (Map<String, Object>)siteDetail.get("gmkt");
			Map<String,Object> iacSiteDetail = (Map<String, Object>)siteDetail.get("iac");
			
			//= Step 3) 통신후 resMap 파싱 & setter
			PaGmktGoodsVO paGmktGoods = new PaGmktGoodsVO();
			paGmktGoods.setEsmGoodsCode(resMap.get("goodsNo").toString());
			paGmktGoods.setGoodsCode(paramMap.get("goodsCode").toString());
			paGmktGoods.setPaCode(paramMap.get("paCode").toString());
			//paGmktGoods.setPaSaleGb(requestGoodsMap.get("PA_SALE_GB").toString());
			paGmktGoods.setModifyId(paramMap.getString("siteGb"));
			paGmktGoods.setLastModifyDate(DateUtil.toTimestamp(paramMap.getString("lastModifyDate"), "yyyy/MM/dd HH:mm:ss"));
			
	    	switch(paramMap.getString("siteGb")){
				case "PAE"://2개 동시 등록
					paGmktGoods.setItemNo(gmktSiteDetail.get("SiteGoodsNo").toString());
					paGmktGoods.setItemNoExtra(iacSiteDetail.get("SiteGoodsNo").toString());
					break;
				case "PAG"://지마켓 등록
					paGmktGoods.setItemNo(gmktSiteDetail.get("SiteGoodsNo").toString());
					paGmktGoods.setPaGroupCode("02");
					break;
				case "PAA"://옥션 등록
			    	paGmktGoods.setItemNo(iacSiteDetail.get("SiteGoodsNo").toString());
			    	paGmktGoods.setPaGroupCode("03");
					break;
			}

	    	// SD1 프로모션 적용 ( ARS+일시불+즉시할인쿠폰 )
	    	//List<HashMap<String,Object>> priceMapList = (List<HashMap<String, Object>>) paramMap.get("requestPriceMapList");
	    	List<HashMap<String,Object>> gmktPriceMapList = (List<HashMap<String, Object>>) paramMap.get("requestGmktPriceMapList");
        	List<HashMap<String,Object>> IacPriceMapList = (List<HashMap<String, Object>>) paramMap.get("requestIacPriceMapList");
        	
	    	PaPromoTarget paPromoTarget;
	    	List<PaPromoTarget> paPromoTargetList = new ArrayList<>();
	    	//G마켓
	    	for(HashMap<String,Object> priceMap : gmktPriceMapList){
	    		
	    		if(priceMap.get("PROMO_NO").toString().equals("000000000000")){
	    			continue;
	    		}
	    		
	    		paPromoTarget = new PaPromoTarget();
	    		paPromoTarget.setGoodsCode(paramMap.get("goodsCode").toString());
	    		paPromoTarget.setPaCode(paramMap.get("paCode").toString());
	    		paPromoTarget.setPromoNo(priceMap.get("PROMO_NO").toString());
	    		paPromoTarget.setSeq(priceMap.get("SEQ").toString());
	    		paPromoTarget.setDoCost(Double.parseDouble(priceMap.get("DO_COST").toString()));
	    		paPromoTarget.setDoOwnCost(Double.parseDouble(priceMap.get("DO_OWN_COST").toString()));
	    		paPromoTarget.setDoEntpCost(Double.parseDouble(priceMap.get("DO_ENTP_COST").toString()));
	    		
	    		if( !priceMap.get("TRANS_DATE").toString().equals("") ){
	    			paPromoTarget.setTransDate(DateUtil.toTimestamp(priceMap.get("TRANS_DATE").toString(), "yyyy-MM-dd HH:mm:ss"));	
	    		}else{
	    			paPromoTarget.setTransDate(null);	
	    		}
	    		
	    		paPromoTargetList.add(paPromoTarget);
	    	}
	    	//옥션
	    	for(HashMap<String,Object> priceMap : IacPriceMapList){
	    		
	    		if(priceMap.get("PROMO_NO").toString().equals("000000000000")){
	    			continue;
	    		}
	    		
	    		paPromoTarget = new PaPromoTarget();
	    		paPromoTarget.setGoodsCode(paramMap.get("goodsCode").toString());
	    		paPromoTarget.setPaCode(paramMap.get("paCode").toString());
	    		paPromoTarget.setPromoNo(priceMap.get("PROMO_NO").toString());
	    		paPromoTarget.setSeq(priceMap.get("SEQ").toString());
	    		paPromoTarget.setDoCost(Double.parseDouble(priceMap.get("DO_COST").toString()));
	    		paPromoTarget.setDoOwnCost(Double.parseDouble(priceMap.get("DO_OWN_COST").toString()));
	    		paPromoTarget.setDoEntpCost(Double.parseDouble(priceMap.get("DO_ENTP_COST").toString()));
	    		
	    		if( !priceMap.get("TRANS_DATE").toString().equals("") ){
	    			paPromoTarget.setTransDate(DateUtil.toTimestamp(priceMap.get("TRANS_DATE").toString(), "yyyy-MM-dd HH:mm:ss"));	
	    		}else{
	    			paPromoTarget.setTransDate(null);	
	    		}
	    		
	    		paPromoTargetList.add(paPromoTarget);
	    	}
	    	
	    	//= Step 4) table insert  ESM코드가 있는값에 where조건문으로 update 처리
	    	paGmktGoodsService.savePaGmktGoodsModifyTx(paGmktGoods, paPromoTargetList);
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			//특정 메세지일 경우에만 판매 중단 처리.
			 if(paramMap.getString("message").indexOf("500") > -1) {
				savePaGoodsInsert500Fail(paramMap);				
			}else {
				savePaGoodsInsertFail(paramMap);
			}
			
			/*상품 판매 중단처리 제외
			if(!duplicateCheck.equals("1")){
				//중복실행처리는 중지처리를 하지않기 위함
				log.error(paramMap.getString("message"), se);
			}else{
				log.info(paramMap.getString("message"), se);
			}
			*/
			log.error(paramMap.getString("message"), se);
			
			return;

		}finally {
			try{
				savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			log.info("===== 상품 수정 API END =====");
		}
		return;
	}
	
	@Async
	public void putGoodsOptionOnlineSub(HttpServletRequest request, 
										String paCode, 
										String goodsCode, 
										int loopCnt) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_014_O";
		String response = "";
		boolean requestFlag = true;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("goodsCode", goodsCode);
		//paramMap.put("siteGb", selectGmktSellerId(paramMap));
		
		String siteGb = paGmktGoodsService.getGmktSiteGb(paCode, goodsCode, "isSell"); //selectGmktSellerId + 상품명 조회를 통해 
		if("999".equals(siteGb)) return;
		paramMap.put("siteGb", siteGb);
		
		
		List<HashMap<String,Object>> targetOptionList = new ArrayList<>();
		try{			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			//단일타겟만 조회하도록 프로세싱 , 다중 처리는 상위에서 List돌리도록
			HashMap<String,Object> target = new HashMap<String,Object>();
			target.put("paCode", paCode);
			target.put("goodsCode", goodsCode);
			target.put("isModify", "Y");
			targetOptionList = paGmktGoodsService.selectGoodsOption(target);
			paramMap.put("urlParameter",targetOptionList.get(0).get("ESM_GOODS_CODE"));
			paramMap.put("map", targetOptionList);
			paramMap.put("paSaleGb", "20");
			
			//= Step 2) 통신
			try {
				response = restUtil.getConnection(rest,  paramMap);
				//= Step 3) 통신후 resMap 파싱 & setter
				Map<String,Object> resMap = ComUtil.splitJson(response);				
				//savePaGoodsInsertSuccess(paramMap);
				paramMap.put("code", "200");
				paramMap.put("message", "OK");
			} catch (Exception se) {
				
				if (se.getMessage().indexOf("옥션") > 0 && se.getMessage().indexOf("상품번호가") > 0) {
					paramMap.put("siteGb", "PAG");
					response = restUtil.getConnection(rest,  paramMap);
					Map<String,Object> resMap = ComUtil.splitJson(response);				
					//savePaGoodsInsertSuccess(paramMap);
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
				}
				
				if (se.getMessage().indexOf("G마켓") > 0 && se.getMessage().indexOf("상품번호가") > 0) {
					paramMap.put("siteGb", "PAA");
					response = restUtil.getConnection(rest,  paramMap);
					Map<String,Object> resMap = ComUtil.splitJson(response);				
					//savePaGoodsInsertSuccess(paramMap);
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
				}
				
				CommonUtil.dealException(se, paramMap);
				//특정 메세지일 경우에만 판매 중단 처리.
				if(paramMap.getString("message").indexOf("선택형(Independent) 항목은 20개까지") > -1 || paramMap.getString("message").indexOf("상품번호가 생성 되지") > -1
				|| paramMap.getString("message").indexOf("주문옵션에는 특수문자") > -1 || paramMap.getString("message").indexOf("시퀀스에 둘 이상의") > -1
				|| paramMap.getString("message").indexOf("G마켓에 문의 바랍니다") > -1 || paramMap.getString("message").indexOf("최대 50byte까지") > -1) {
					savePaGoodsInsertFail(paramMap);				
				}
			}
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			/*
			if( !("400".equals(paramMap.getString("code")) && (paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_O500") > -1 
					|| paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_O502") > -1) && loopCnt < 3) ){
				savePaGoodsInsertFail(paramMap);
			}
			*/
			savePaGoodsInsertFail(paramMap);
			return;
		}finally {
			try{
				savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
				
				if("400".equals(paramMap.getString("code")) && (paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_O500") > -1 || paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_O502") > -1)){
					if(loopCnt < 3){
						
						//if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
						log.info("===== 상품 옵션수정 putGoodsOptionOnlineSub LIMITED API END =====");
						
						putGoodsOptionOnlineSub(request, paCode, goodsCode, loopCnt + 1); 
						requestFlag = false;
						
					}else{
						requestFlag = true;
					}
				}else{
					requestFlag = true;
				}
				
				if("200".equals(paramMap.getString("code")) && requestFlag){
					//상품 수정실패시 중지처리후 trans_target_yn을 0으로 업데이트하도록 finally에서 처리 2018.11.28 thjeon
					List<PaGoodsdtMapping> paGoodsdtMappingList = new ArrayList<>();
					for(HashMap<String,Object> targetOption : targetOptionList){
						PaGoodsdtMapping paGoodsdtMapping = new PaGoodsdtMapping();
						long orderAbleQty = Long.parseLong(targetOption.get("TRANS_ORDER_ABLE_QTY").toString());
						if(orderAbleQty>99999){
							orderAbleQty=99999;
						}
						paGoodsdtMapping.setTransOrderAbleQty(Long.toString(orderAbleQty));
						paGoodsdtMapping.setGoodsCode(targetOption.get("GOODS_CODE").toString());
						paGoodsdtMapping.setGoodsdtCode(targetOption.get("GOODSDT_CODE").toString());
						paGoodsdtMapping.setPaCode(paCode);
						paGoodsdtMappingList.add(paGoodsdtMapping);
					}
					
			    	//= Step 4) table insert
					paGmktGoodsService.saveGoodsOptionTx(paGoodsdtMappingList);
				}
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			log.info("===== 상품 옵션수정 LIMITED API END =====");
		}
		return;
	}
	
	@Async
	public void putGoodsOptionBroadSub(HttpServletRequest request, 
									   String paCode, 
									   String goodsCode, 
									   int loopCnt) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_014_B";
		String response = "";
		boolean requestFlag = true;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("goodsCode", goodsCode);
		//paramMap.put("siteGb", selectGmktSellerId(paramMap));
		
		String siteGb = paGmktGoodsService.getGmktSiteGb(paCode, goodsCode, "isSell"); //selectGmktSellerId + 상품명 조회를 통해 
		if("999".equals(siteGb)) return;
		paramMap.put("siteGb", siteGb);
		
		List<HashMap<String,Object>> targetOptionList = new ArrayList<>();
		try{			
						
			//= Step 1) select 테이블 후 urlParameter 셋팅
			//단일타겟만 조회하도록 프로세싱 , 다중 처리는 상위에서 List돌리도록
			HashMap<String,Object> target = new HashMap<String,Object>();
			target.put("paCode", paCode);
			target.put("goodsCode", goodsCode);
			target.put("isModify", "Y");
			targetOptionList = paGmktGoodsService.selectGoodsOption(target);
			paramMap.put("urlParameter",targetOptionList.get(0).get("ESM_GOODS_CODE"));
			paramMap.put("map", targetOptionList);
			paramMap.put("paSaleGb", "20");

			//= Step 2) 통신
			try {
				response = restUtil.getConnection(rest,  paramMap);
				//= Step 3) 통신후 resMap 파싱 & setter
				Map<String,Object> resMap = ComUtil.splitJson(response);				
				//savePaGoodsInsertSuccess(paramMap);
				paramMap.put("code", "200");
				paramMap.put("message", "OK");
			} catch (Exception se) {
				
				if (se.getMessage().indexOf("옥션") > 0 && se.getMessage().indexOf("상품번호") > 0) {
					paramMap.put("siteGb", "PAG");
					response = restUtil.getConnection(rest,  paramMap);
					Map<String,Object> resMap = ComUtil.splitJson(response);				
					//savePaGoodsInsertSuccess(paramMap);
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
				}
				
				if (se.getMessage().indexOf("G마켓") > 0 && se.getMessage().indexOf("상품번호") > 0) {
					paramMap.put("siteGb", "PAA");
					response = restUtil.getConnection(rest,  paramMap);
					Map<String,Object> resMap = ComUtil.splitJson(response);				
					//savePaGoodsInsertSuccess(paramMap);
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
				}
				
				CommonUtil.dealException(se, paramMap);
				//특정 메세지일 경우에만 판매 중단 처리.
				if(paramMap.getString("message").indexOf("선택형(Independent) 항목은 20개까지") > -1 || paramMap.getString("message").indexOf("상품번호가 생성 되지") > -1
				|| paramMap.getString("message").indexOf("주문옵션에는 특수문자") > -1 || paramMap.getString("message").indexOf("시퀀스에 둘 이상의") > -1
				|| paramMap.getString("message").indexOf("G마켓에 문의 바랍니다") > -1 || paramMap.getString("message").indexOf("최대 50byte까지") > -1) {
					savePaGoodsInsertFail(paramMap);				
				}
			}
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			/*
			if( !("400".equals(paramMap.getString("code")) && (paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_B500") > -1 
					|| paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_B502") > -1)  && loopCnt < 3) ){
				savePaGoodsInsertFail(paramMap);
			}
			*/
			savePaGoodsInsertFail(paramMap);
			return;
		}finally {
			try{
				savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
				//상품 수정실패시 중지처리후 trans_target_yn을 0으로 업데이트하도록 finally에서 처리 2018.11.28 thjeon
				
				if("400".equals(paramMap.getString("code")) && (paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_B500") > -1 || paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_B502") > -1)){
					if(loopCnt < 3){
						
						//if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
						log.info("===== 상품 옵션수정 putGoodsOptionBroadSub LIMITED API END =====");
						
						putGoodsOptionBroadSub(request, paCode, goodsCode, loopCnt + 1); 
						requestFlag = false;
						
					}else{
						requestFlag = true;
					}
				}else{
					requestFlag = true;
				}
				
				if("200".equals(paramMap.getString("code")) && requestFlag){
					
					List<PaGoodsdtMapping> paGoodsdtMappingList = new ArrayList<>();
					for(HashMap<String,Object> targetOption : targetOptionList){
						PaGoodsdtMapping paGoodsdtMapping = new PaGoodsdtMapping();
						long orderAbleQty = Long.parseLong(targetOption.get("TRANS_ORDER_ABLE_QTY").toString());
						if(orderAbleQty>99999){
							orderAbleQty=99999;
						}
						paGoodsdtMapping.setTransOrderAbleQty(Long.toString(orderAbleQty));
						paGoodsdtMapping.setGoodsCode(targetOption.get("GOODS_CODE").toString());
						paGoodsdtMapping.setGoodsdtCode(targetOption.get("GOODSDT_CODE").toString());
						paGoodsdtMapping.setPaCode(paCode);
						paGoodsdtMappingList.add(paGoodsdtMapping);
					}
					
			    	//= Step 4) table insert
					paGmktGoodsService.saveGoodsOptionTx(paGoodsdtMappingList);
				}
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			log.info("===== 상품 옵션수정 LIMITED API END =====");
		}
		return;
	}
	
	@Async
	public void postGoodsOption(HttpServletRequest request, 
			   					String paCode, 
			   					String goodsCode
			) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_013";            
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("siteGb", paGmktGoodsService.getGmktSiteGb(paCode, goodsCode, "SellingPeriod")); //selectGmktSellerId + 상품명 조회를 통해 );
		paramMap.put("dateTime", systemService.getSysdatetimeToString());
	
		List<HashMap<String,Object>> targetOptionList = new ArrayList<>();
		try{
			//= Step 1) select 테이블 후 urlParameter 셋팅
			//단일타겟만 조회하도록 프로세싱 , 다중 처리는 상위에서 List돌리도록
			HashMap<String,Object> target = new HashMap<String,Object>();
			target.put("paCode", paCode);
			target.put("goodsCode", goodsCode);
						
			targetOptionList = paGmktGoodsService.selectGoodsOption(target);
			paramMap.put("urlParameter",targetOptionList.get(0).get("ESM_GOODS_CODE"));
			paramMap.put("map", targetOptionList);
			
			//= Step 2) 통신
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			//= Step 3) 통신후 resMap 파싱 & setter			
						
			List<PaGoodsdtMapping> paGoodsdtMappingList = new ArrayList<>();
			for(HashMap<String,Object> targetOption : targetOptionList){
				PaGoodsdtMapping paGoodsdtMapping = new PaGoodsdtMapping();
				long orderAbleQty = Long.parseLong(targetOption.get("TRANS_ORDER_ABLE_QTY").toString());
				if(orderAbleQty>99999){
					orderAbleQty=99999;
				}
				paGoodsdtMapping.setTransOrderAbleQty(Long.toString(orderAbleQty));
				paGoodsdtMapping.setGoodsCode(targetOption.get("GOODS_CODE").toString());
				paGoodsdtMapping.setGoodsdtCode(targetOption.get("GOODSDT_CODE").toString());
				paGoodsdtMapping.setPaCode(paCode);
				paGoodsdtMappingList.add(paGoodsdtMapping);
			}
			
	    	//= Step 4) table insert
			paGmktGoodsService.saveGoodsOptionTx(paGoodsdtMappingList);
			
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error("ApiTracking Insert Error : "+se.getMessage());
			
			// 단품등록 실패 시 오류메세지 구분하여 중단
			paramMap.put("message", apiCode+ "/" + paramMap.get("message"));
			int errorChk = paGmktGoodsService.selectOptionErrorChk(paramMap);
			if(errorChk == 0) {
				savePaGoodsInsertFail(paramMap);
			}
		} finally {
				savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);		
				log.info("===== 상품 옵션등록 LIMITED API END =====");
		}
		return;
	}
	
	// 제휴상품관리 호출용(옵션등록)
	public void postGoodsOption(HttpServletRequest request, 
			   					String paCode, 
			   					String goodsCode,
			   					String inComingUrl
			) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_013";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("siteGb", paGmktGoodsService.getGmktSiteGb(paCode, goodsCode, "SellingPeriod")); //selectGmktSellerId + 상품명 조회를 통해 );
		paramMap.put("dateTime", systemService.getSysdatetimeToString());
	
		List<HashMap<String,Object>> targetOptionList = new ArrayList<>();
		try{
			//= Step 1) select 테이블 후 urlParameter 셋팅
			//단일타겟만 조회하도록 프로세싱 , 다중 처리는 상위에서 List돌리도록
			HashMap<String,Object> target = new HashMap<String,Object>();
			target.put("paCode", paCode);
			target.put("goodsCode", goodsCode);
			
			if(inComingUrl.equals("BO")) {
				target.put("isModify", "true");
				paramMap.put("isModify", "Y");
				pacommonDAO.updatePaGoodsDtMappingTrans(paramMap);
			}
			
			targetOptionList = paGmktGoodsService.selectGoodsOptionBO(target);
			paramMap.put("urlParameter",targetOptionList.get(0).get("ESM_GOODS_CODE"));
			paramMap.put("map", targetOptionList);
			
			//= Step 2) 통신
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			//= Step 3) 통신후 resMap 파싱 & setter			
						
			List<PaGoodsdtMapping> paGoodsdtMappingList = new ArrayList<>();
			for(HashMap<String,Object> targetOption : targetOptionList){
				PaGoodsdtMapping paGoodsdtMapping = new PaGoodsdtMapping();
				long orderAbleQty = Long.parseLong(targetOption.get("TRANS_ORDER_ABLE_QTY").toString());
				if(orderAbleQty>99999){
					orderAbleQty=99999;
				}
				paGoodsdtMapping.setTransOrderAbleQty(Long.toString(orderAbleQty));
				paGoodsdtMapping.setGoodsCode(targetOption.get("GOODS_CODE").toString());
				paGoodsdtMapping.setGoodsdtCode(targetOption.get("GOODSDT_CODE").toString());
				paGoodsdtMapping.setPaCode(paCode);
				paGoodsdtMappingList.add(paGoodsdtMapping);
			}
			
	    	//= Step 4) table insert
			paGmktGoodsService.saveGoodsOptionTx(paGoodsdtMappingList);
			
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error("ApiTracking Insert Error : "+se.getMessage());
			
//			//특정 메세지일 경우에만 판매 중단 처리.
//			if(paramMap.getString("message").indexOf("선택형(Independent) 항목은 20개까지") > -1 || paramMap.getString("message").indexOf("상품번호가 생성 되지") > -1
//			|| paramMap.getString("message").indexOf("주문옵션에는 특수문자") > -1 || paramMap.getString("message").indexOf("시퀀스에 둘 이상의") > -1
//			|| paramMap.getString("message").indexOf("G마켓에 문의 바랍니다") > -1 || paramMap.getString("message").indexOf("최대 50byte까지") > -1) {
//				savePaGoodsInsertFail(paramMap);				
//			}
			// 단품등록 실패시 모두 판매 중단 처리
			savePaGoodsInsertFail(paramMap);
			//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
				savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);		
				log.info("===== 상품 옵션등록 LIMITED API END =====");
		}
		return;
	}
	
	/** TRANSLOG(상품) */
	public void savePaGoodsTransLog(ParamMap paramMap) throws Exception{
		PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
		paGoodsTransLog.setGoodsCode(paramMap.getString("goodsCode"));
		paGoodsTransLog.setPaCode(paramMap.getString("paCode"));
		//itemNo가 없을시, goodsCode로 insert되도록
		paGoodsTransLog.setItemNo(paramMap.getString("urlParameter").equals("")==true?paramMap.getString("goodsCode"):paramMap.getString("urlParameter"));
		
		//성공시 code와 message는 200,OK 실패시 dealException에서 넣어줌
		paGoodsTransLog.setRtnCode(paramMap.getString("code"));
		paGoodsTransLog.setSuccessYn(paramMap.getString("code").equals("200")==true?"1":"0");
		paGoodsTransLog.setRtnMsg(paramMap.getString("apiCode") + " || " + paramMap.getString("message"));
		//초기 API 실행시 기본데이터 
		paGoodsTransLog.setProcDate(paramMap.getTimestamp("startDate"));
		paGoodsTransLog.setProcId(paramMap.getString("siteGb"));
		paGmktGoodsService.insertPaGoodsTransLogTx(paGoodsTransLog);
	}
	
	/** 판매중지처리/message처리 */
	public void savePaGoodsInsertFail(ParamMap paramMap) throws Exception{
		PaGmktGoodsVO paGmktGoods = new PaGmktGoodsVO();
		//paGmktGoods.setPaGroupCode(paramMap.getString("paGroupCode"));
		paGmktGoods.setGoodsCode(paramMap.getString("goodsCode"));
		paGmktGoods.setPaCode(paramMap.getString("paCode"));
		paGmktGoods.setPaSaleGb("30");
		
		HashMap<String, String > pagoodsInfo = paGmktGoodsService.selectEmsCodeItemNo(paGmktGoods);
				
	    String orgAucItemNo    = pagoodsInfo.get("AUCTION_ITEM_NO");
	    String orgGmkItemNo    = pagoodsInfo.get("GMKT_ITEM_NO");
	    
		if( !("".equals(orgAucItemNo)) || !("".equals(orgGmkItemNo))) {
			paGmktGoods.setPaSaleGb("30");
			paramMap.put("isModify","Y");
		} else {
		    paGmktGoods.setPaSaleGb("20");
		}
		
		paGmktGoods.setIsModifyYn( ComUtil.NVL(paramMap.getString("isModify"), "N"));
		paGmktGoods.setReturnNote(paramMap.getString("message"));
		
		paGmktGoodsService.savePaGmktGoodsInsertSuccessFailTx(paGmktGoods);
		//판매상태API호출은 일괄수정에서..
	}
	
	/** 이베이 500에러 발생시 처리 로직 */
	public void savePaGoodsInsert500Fail(ParamMap paramMap) throws Exception{
		PaGmktGoodsVO paGmktGoods = new PaGmktGoodsVO();
		//paGmktGoods.setPaGroupCode(paramMap.getString("paGroupCode"));
		paGmktGoods.setGoodsCode(paramMap.getString("goodsCode"));
		paGmktGoods.setPaCode(paramMap.getString("paCode"));
		paGmktGoods.setPaSaleGb("20");
		paGmktGoods.setReturnNote("500");
		paGmktGoods.setIsModifyYn( "Y");
		paGmktGoodsService.savePaGmktGoodsInsertSuccessFailTx(paGmktGoods);
	}
	
	public String selectGmktSellerId(ParamMap paramMap) throws Exception{
		return paGmktGoodsService.selectGmktSellerId(paramMap);
	}
	
	@Async
	public void spPagoodsSyncEbay(HttpServletRequest request, String goodsCode, String userId) throws Exception{
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
		List<HashMap<String, String>> curStockCheck = null;
		
		String resultMsg = "";
		String dateTime = systemService.getSysdatetimeToString();
		StringBuffer sb = null;
		int conditionDay = 2;		
		
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paGroupCode", "02");
		paramMap.put("dateTime", dateTime);
		paramMap.put("conditionDay", conditionDay);		
		paramMap.put("comparePaCode", "21");
		paramMap.put("feeCode", "O579");
		paramMap.put("minMarginCode", "30");
		paramMap.put("minPriceCode", "40");
		
		paramMap.put("siteGb", userId);
		paramMap.put("apiCode", "IF_PACOMMON_00_002");
		
		log.info("Step1. 이베이 상품이미지 동기화 START");
		
		curImageInfo = paCommonService.selectCurImageInfoList(paramMap);
		if(curImageInfo.size() > 0) {
			sb = new StringBuffer();
			for(PaGoodsImage curImageInfoTarget : curImageInfo) {
				//이미지 없는 케이스가 많아 for문안에 try catch 처리
				try {
					curImageInfoTarget.setModifyId("PAGMKT");
					curImageInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					curImageInfoTarget.setRemark(dateTime);
					resultMsg = paCommonService.saveCurImageInfoTx(curImageInfoTarget);
		
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step1 상품이미지 동기화 이베이 Fail > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
						sb.append(curImageInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step1 상품이미지 동기화 이베이 Sucess > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
				} catch(Exception e) {
					log.info("Step1. 이베이 상품이미지 동기화 : " + e.getMessage());
					paramMap.put("code", "500");
					paramMap.put("message", "Step1. 이베이 상품이미지 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}
		}
		log.info("Step1. 이베이 상품이미지 동기화 END");
		
		log.info("Step2. 이베이 상품가격 동기화 START");
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
						
						stopSaleParam.put("paGroupCode", "02");
						stopSaleParam.put("paCode", curPriceInfoTarget.getPaCode());
						stopSaleParam.put("goodsCode", curPriceInfoTarget.getGoodsCode());
						stopSaleParam.put("dateTime", dateTime);
						stopSaleParam.put("userId", "PAGMKT");
						stopSaleParam.put("paGoodsCode", curPriceInfoTarget.getPaGoodsCode());
						stopSaleParam.put("note", "마진10이하 혹은 판매가100원 미만");
						stopSaleParam.put("priceStopSale", "Y");
						
						//연동제외처리
						resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
					} else {
						curPriceInfoTarget.setModifyId("PAGMKT");
						curPriceInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						curPriceInfoTarget.setDateTime(dateTime);
						resultMsg = paCommonService.saveCurPriceInfoTx(curPriceInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step2 상품가격 동기화 이베이 Fail > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
						sb.append(curPriceInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step2 상품가격 동기화 이베이 Sucess > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
				}
			} catch(Exception e) {
				log.info("Step2. 이베이 상품가격 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step2. 이베이 상품가격 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step2. 이베이 상품가격 동기화 END");	
		
		log.info("Step3. 이베이 고객부담배송비 동기화 START");
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
							
							stopSaleParam.put("paGroupCode", "02");
							stopSaleParam.put("paCode", curShipStopSaleTarget.get("PA_CODE").toString());
							stopSaleParam.put("goodsCode", curShipStopSaleTarget.get("GOODS_CODE").toString());
							stopSaleParam.put("dateTime", dateTime);
							stopSaleParam.put("userId", "PAGMKT");
							stopSaleParam.put("paGoodsCode", curShipStopSaleTarget.get("PA_GOODS_CODE").toString());
							stopSaleParam.put("note", "배송비유료변경");
							stopSaleParam.put("priceStopSale", "N");
							
							//연동제외처리
							resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
						}
					} else {
						curShipCostInfoTarget.setModifyId("PAGMKT");
						curShipCostInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						resultMsg = paCommonService.saveCurShipCostInfoTx(curShipCostInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step3 고객부담배송비 이베이 Fail");
						sb.append(curShipCostInfoTarget.getEntpCode() + "|" + curShipCostInfoTarget.getShipCostCode() + ", ");
						continue;
					}
					log.info("Step3 고객부담배송비 이베이 Sucess");
				}
			} catch(Exception e) {
				log.info("Step3. 이베이 고객부담배송비 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step3. 이베이 고객부담배송비 동기화 : " + e.getMessage() + "INFO : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step3. 이베이 고객부담배송비 동기화 END");
		
		log.info("Step4. 이베이 출고/회수지 동기화 START");
		curEntpSlipInfo = paCommonService.selectCurEntpSlipInfoList(paramMap);
		if(curEntpSlipInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaEntpSlip curEntpSlipInfoTarget : curEntpSlipInfo) {
					
					curEntpSlipInfoTarget.setModifyId("PAGMKT");
					curEntpSlipInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					resultMsg = paCommonService.saveCurEntpSlipInfoTx(curEntpSlipInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step4 출고/회수지 동기화 이베이 Fail");
						sb.append(curEntpSlipInfoTarget.getEntpCode() + ", ");
						continue;
					}
					log.info("Step4 출고/회수지 동기화 이베이 Sucess");
				}
			} catch(Exception e) {
				log.info("Step4. 이베이 출고/회수지 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step4. 이베이 출고/회수지 동기화 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step4. 이베이 출고/회수지 회수지 동기화 END");
		
		log.info("Step5. 이베이 상품판매단계 동기화 START");
		curSaleStop = paCommonService.selectCurSaleStopList(paramMap);
		
		if(curSaleStop.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curSaleStopTarget : curSaleStop) {
					
					stopSaleParam.put("paGroupCode", "02");
					stopSaleParam.put("paCode", curSaleStopTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curSaleStopTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PAGMKT");
					stopSaleParam.put("paGoodsCode", curSaleStopTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "SK스토아상품판매중단");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step5 상품판매단계 동기화 이베이 Fail > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
						sb.append(curSaleStopTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step5 상품판매단계 동기화 이베이 Sucess > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step5. 이베이 상품판매단계 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step5. 이베이 상품판매단계 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step5. 이베이 상품판매단계 동기화 END");
		
		
		log.info("Step6. 이베이 행사 종료 상품 마진 체크 START");
		curEventMargin = paCommonService.selectCurEventMarginList(paramMap);
		if(curEventMargin.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curEventMarginTarget : curEventMargin) {
					paramMap.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					//minMarginPrice = paCommonService.selectMinMarginPrice(paramMap);
					//selectCurEventMarginList 안에서 체크
					//if(ComUtil.objToDouble(curEventMarginTarget.get("MARGIN_RATE")) < ComUtil.objToDouble(minMarginPrice.get("MIN_MARGIN"))) {
					stopSaleParam.put("paGroupCode", "02");
					stopSaleParam.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curEventMarginTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PAGMKT");
					stopSaleParam.put("paGoodsCode", curEventMarginTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "행사종료");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step6 행사 종료 상품 마진 체크 이베이 Fail > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
						sb.append(curEventMarginTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step6 행사 종료 상품 마진 체크 이베이 Sucess > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
					//}					
				}
			} catch(Exception e) {
				log.info("Step6. 이베이 행사 종료 상품 마진 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step6. 이베이 행사 종료 상품 마진 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step6. 이베이 행사 종료 상품 마진 체크 END");
		
		log.info("Step7. 이베이 단품재고 동기화 START");
		curStockCheck = paCommonService.selectCurStockCheckList(paramMap);
		if(curStockCheck.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curStockCheckTarget : curStockCheck) {					
					
					stopSaleParam.put("paGroupCode", "02");
					stopSaleParam.put("paCode", curStockCheckTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curStockCheckTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					
					resultMsg = paCommonService.saveCurStockCheckTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step7 단품재고 동기화 이베이 Fail > GOODS_CODE : " + curStockCheckTarget.get("GOODS_CODE").toString());
						sb.append(curStockCheckTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step7 단품재고 동기화 이베이 Sucess > GOODS_CODE : " + curStockCheckTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step7. 이베이 단품재고 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step7. 이베이 단품재고 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step7. 이베이 단품재고 동기화 END");
		
		paCommonService.checkMassModifyGoods("02");
	}

	// 모바일 자동취소 (품절취소반품)
	public String moblieReqSaleRefusalProc(HashMap<String, String> cancelMap) throws Exception {
		ParamMap configMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktCancelRest(); 
		Map<String, Object> resMap = null;
		String response = "";
		String resMsg = "";
		HashMap<String, String> map = new HashMap<String, String>();
		int successCount  = 0;
		int failCount 	  = 0;
		
		try {
			configMap.put("apiCode"			, "IF_PAGMKTAPI_V2_03_021");
			configMap.put("paGroupCode"     , cancelMap.get("PA_GROUP_CODE").toString());	
			configMap.put("paCode"			, cancelMap.get("PA_CODE").toString());	
			CommonUtil.setParams(configMap);
			configMap.put("urlParameter"	, cancelMap.get("PA_ORDER_SEQ").toString());
	
			map.put("PA_GROUP_CODE", cancelMap.get("PA_GROUP_CODE").toString());
			map.put("PA_CODE", cancelMap.get("PA_CODE").toString());
			map.put("PA_ORDER_NO", cancelMap.get("PA_ORDER_NO").toString());
			map.put("PA_ORDER_SEQ", cancelMap.get("PA_ORDER_SEQ").toString());
			map.put("ORDER_NO", cancelMap.get("ORDER_NO"));
			map.put("ORDER_G_SEQ", cancelMap.get("ORDER_G_SEQ"));
			map.put("PROC_ID", "PAGMKT");
		
			response = restUtil.getConnection(rest,  configMap);
			resMap = ComUtil.splitJson(response);
			resMsg = ComUtil.objToStr(resMap.get("Message").toString());
			
		}catch(Exception e) {
			log.error(e.getMessage());
			resMsg = e.getMessage();
		} finally {
			if("Success".equals(resMsg)) {                        
				log.info("모바일자동취소 성공");
				map.put("REMARK3_N", "10");
				map.put("RSLT_MESSAGE", "모바일자동취소 성공");
				paorderService.updateRemark3NTx(map);
				
				//상담생성 & 문자발송 & 상품품절처리
				paCommonService.savePaMobileOrderCancelTx(map);
				saveMobileCancelList(cancelMap);
				successCount++;
			} else {
				log.info("모바일자동취소 실패");
				map.put("REMARK3_N", "90");
				map.put("RSLT_MESSAGE", "모바일자동취소 실패 " + resMsg);
				paorderService.updateRemark3NTx(map);
				failCount++;
			}
		}
		return CommonUtil.setResultMessage("모바일 자동취소", cancelMap.get("PA_CODE").toString(), successCount, failCount);	
	}

	private void saveMobileCancelList(HashMap<String, String> cancelMap) throws Exception {
		PaGmkCancel cancel = null;
		String sysdate = systemService.getSysdatetimeToString();
		
		cancel = new PaGmkCancel();
		cancel.setPaCode				(cancelMap.get("PA_CODE").toString());
		cancel.setPaGroupCode			(cancelMap.get("PA_GROUP_CODE").toString());
		
		cancel.setPayNo					(cancelMap.get("PA_ORDER_NO").toString());
		//cancel.setGroupNo				(ComUtil.NVL(cancelMap.get("GROUP_NO")).toString());
		cancel.setContrNo				(cancelMap.get("PA_ORDER_SEQ").toString());
		cancel.setContrNoSeq			(CommonUtil.getMaxPaOrderWSeq(cancel.getPayNo(),cancel.getContrNo())); 
		cancel.setProcNo				("00");
		cancel.setGoodsNo				("0");
		//cancel.setSiteGoodsNo			(ComUtil.NVL(cancelMap.get("SITE_GOODS_NO")).toString());
		cancel.setRequestUser			("2");
		cancel.setApproveUser			("2");
		cancel.setCancelStatus			("3");  
		cancel.setReason				("0");
		cancel.setReasonDetail			("재고부족(품절)");
		cancel.setAddShippingFee		(0);
		
		//cancel.setOrderDate				(Timestamp.valueOf(cancelMap.get("ORDER_DATE").toString()));
		cancel.setRequestDate			(DateUtil.toTimestamp("2999/12/31 23:59:59",  "yyyy/MM/dd HH:mm:ss"));
		cancel.setApprovalDate			(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		cancel.setCompleteDate			(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		
		cancel.setWithDrawYn		("0");
		
		cancel.setModifyDate(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		cancel.setInsertDate(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		
		paGmktCancelService.saveCancelCompleteListTx(cancel);
		
	}
}
