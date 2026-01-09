package com.cware.api.passg.controller;

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
import com.cware.netshopping.domain.PaSsgGoodsVO;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaPromoGoodsPrice;
import com.cware.netshopping.domain.model.PaSsgGoodsdtMapping;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.passg.claim.service.PaSsgClaimService;
import com.cware.netshopping.passg.counsel.service.PaSsgCounselService;
import com.cware.netshopping.passg.delivery.service.PaSsgDeliveryService;
import com.cware.netshopping.passg.goods.service.PaSsgGoodsService;
import com.cware.netshopping.passg.util.PaSsgComUtill;
import com.cware.netshopping.passg.util.PaSsgConnectUtil;

import io.swagger.annotations.Api;

@Api(value="/passg/async")
@Controller("com.cware.api.passg.PaSsgAsycController")
@RequestMapping(value="/passg/async")
public class PaSsgAsyncController extends AbstractController{
	
	@Resource(name = "passg.claim.paSsgClaimService")
	private PaSsgClaimService paSsgClaimService;
	@Resource(name = "passg.delivery.paSsgDeliveryService")
	private PaSsgDeliveryService paSsgDeliveryService;
	@Resource(name = "passg.counsel.paSsgCounselService")
	private PaSsgCounselService PaSsgCounselService;
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paorderService;
	@Resource(name = "pacommon.claim.paclaimService")
	private PaClaimService paclaimService;
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	@Resource(name = "passg.goods.paSsgGoodsService")
	private PaSsgGoodsService paSsgGoodsService;
	@Resource(name = "passg.counsel.paSsgCounselService")
	private PaSsgCounselService paSsgCounselService;
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	@Autowired
	private PaSsgConnectUtil paSsgConnectUtil;
	
	public void cancelInputAsync(HashMap<String, Object> cancelTarget, HttpServletRequest request) {
		ParamMap paramMap = null;
		int executedRtn = 0;
		
		try {
			paramMap = new ParamMap();
			paramMap.setParamMap(cancelTarget);
			paramMap.replaceCamel();
			
			HashMap<String, Object> cancelDtInfo = paSsgClaimService.selectCancelInputTargetDtList(paramMap);
			
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
				cancelInputVO.setProcId	    (Constants.PA_SSG_PROC_ID);
				
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
				paSsgDeliveryService.updatePreCanYn(preCancelMap);
				
				if(executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
				}
				break;
			}
		} catch(Exception e) {
			paramMap.put("apiCode", "PASSG_CANCEL_INPUT_A");
			paramMap.put("message", "pa_order_no : " + paramMap.getString("paOrderNo") + " > " + PaSsgComUtill.getErrorMessage(e));
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
			
			String errMsg = PaSsgComUtill.getErrorMessage(e);
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
			paramMap.put("siteGb"		, Constants.PA_SSG_PROC_ID);
			systemService.insertApiTrackingTx(request, paramMap);
			
		}catch(Exception ee){
			log.error("ApiTracking Insert Error : "+ee.toString());
		}
	}
	
	/**
	 * SSG 교환 데이터 생성
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
			orderChangeTargetDtList = paSsgClaimService.selectOrderChangeTargetDtList(paramMap);
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
				// 교환거부 시 고객상담에 추가하여 처리
				if(paramMap.getString("resultCode").equals("100001")) {
					for(OrderClaimVO claimVO : orderClaimVOArray) {
						paSsgClaimService.updatePaOrdermChangeFlag("03", claimVO.getMappingSeq());
						
						if("40".equals(claimVO.getClaimGb())) refusalChange(claimVO);
					}
				}
			}
		} catch(Exception e) {
			//1) Error Log
			message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + PaSsgComUtill.getErrorMessage(e);
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PASSG_ORDER_CHANGE_A");
			//2) UPDATE TPAORDERM
			updatePaOrdermTxForRollback(orderClaimVOArray, e);
		} finally {
			saveApiTracking(paramMap, request);
		}
	}
	
	//재고 부족으로 인한 교환 처리(고객상담에 인입)
	private void refusalChange(OrderClaimVO claimVO) throws Exception {
		String dateTime = systemService.getSysdatetimeToString();
		
		try {
			
			HashMap<String, Object> changeCsDtMap = paSsgCounselService.selectPaSsgChangeCounselDt(claimVO);
			PaqnamVO paqnamVo = new PaqnamVO();
			paqnamVo.setPaCode(ComUtil.objToStr(changeCsDtMap.get("PA_CODE")));
			paqnamVo.setPaGroupCode("10");
			paqnamVo.setPaCounselDtSeq("1");
			paqnamVo.setPaCounselNo("00000000000");
			paqnamVo.setCounselDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paqnamVo.setTitle("SSG CS교환 문의");
			paqnamVo.setProcNote("재고부족, 판매불가로 인한 교환거부");
			paqnamVo.setProcId(Constants.PA_SSG_PROC_ID);
			paqnamVo.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paqnamVo.setPaGoodsCode(ComUtil.objToStr(changeCsDtMap.get("ITEM_ID")));
			paqnamVo.setPaGoodsDtCode(ComUtil.objToStr(changeCsDtMap.get("UITEM_ID")));
			paqnamVo.setPaOrderNo(ComUtil.objToStr(changeCsDtMap.get("PA_ORDER_NO")));
			paqnamVo.setCustTel(ComUtil.objToStr(changeCsDtMap.get("RCPTPE_HPNO")));
			paqnamVo.setMsgGb("10"); 
			paqnamVo.setInsertId(Constants.PA_SSG_PROC_ID);
			paqnamVo.setModifyId(Constants.PA_SSG_PROC_ID);
			paqnamVo.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paqnamVo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
			paqnamVo.setCounselGb("07"); 
			paqnamVo.setProcGb("10");
			paqnamVo.setTransYn("0");
			paqnamVo.setToken("");
			
			paSsgCounselService.savePaQnaTx(paqnamVo);
			
		}catch (Exception e) {
			throw processException(e.getMessage());
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
			String checkAddr = paSsgClaimService.compareAddress(paramMap);
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
		
		Long shpChargeYn  = Long.parseLong(String.valueOf(hmSheet.get("CLM_LST_DLV_CST")));  
		if (shpChargeYn > 0 ){
			orderClaimVO.setShpfeeYn		("1");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2	
			orderClaimVO.setShipcostChargeYn("1"); 
		}else{
			shpChargeYn	= 0L;
			orderClaimVO.setShpfeeYn		("0");  //// 배송비 부과여부 - 무상 : 0, 유상 :1, 협의 2
			orderClaimVO.setShipcostChargeYn("0"); 
		}
		//orderClaimVO.setShpFeeAmt			(shpFeeAmt);
		
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

	public void orderInputAsync(Map<String, String> order, HttpServletRequest request) throws Exception {
		List<Map<String, Object>> orderInputTargetDtList = null;
		HashMap<String, Object>[] resultMap = null;
		OrderInputVO[] orderInputVO 		= null;
		OrderInputVO   vo					= new OrderInputVO();
		ParamMap paramMap					= null;
		int		 index 						= 0;
		
		try {
			orderInputTargetDtList = paSsgDeliveryService.selectOrderInputTargetDtList(order);
			
			if(orderInputTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectOrderInputTargetDtList" });
			
			orderInputVO = new OrderInputVO[orderInputTargetDtList.size()];
			PaSsgComUtill.replaceCamelList(orderInputTargetDtList);
			for(Map<String, Object> map : orderInputTargetDtList) {
				//가격비교
				String paApplyDate = map.get("applyDate").toString(); // TPAGOODSPRICE 기준 APPLY_DATE
				String stoaApplyDate = map.get("stoaApplyDate").toString(); // TGOODSPRICE 기준 APPLY_DATE
				String paOrderDate = map.get("orderDate").toString(); // ssg 주문일시
				
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
				vo = (OrderInputVO) PaSsgComUtill.map2VO(map, OrderInputVO.class);
				
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
				
				// 주소처리
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

				// Local여부 처리
				if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
					vo.setIsLocalYn("Y");
				}else {
					vo.setIsLocalYn("N");					
				}
				vo.setProcUser    	(Constants.PA_SSG_PROC_ID);
				
				orderInputVO[index] = vo;
				index++;
			}
			// BO 데이터 생성
			resultMap = paorderService.saveOrderTx(orderInputVO);
			
		} catch(Exception e) {
			updatePaOrdermTxForRollback(orderInputVO, e);
			
			paramMap = new ParamMap();
			paramMap.put("apiCode", "PASSG_ORDER_INPUT_A");
			paramMap.put("message"	, "pa_order_no : " + order.get("PA_ORDER_NO") + "pa_ship_no : " + order.get("PA_SHIP_NO") + " > " + PaSsgComUtill.getErrorMessage(e));
		} finally {
			saveApiTracking(paramMap, request);
		}
		//재고 부족으로 인한 주문 거절(판매자 주문 취소)
		refusalOrder(resultMap);
	}

	@SuppressWarnings("unchecked")
	private void refusalOrder(HashMap<String, Object>[] resultMap) throws Exception {
		
		if(resultMap == null) return;
		
		List<Map<String, Object>> itemList  = new ArrayList<Map<String,Object>>();
		Map<String, Object>  connectResult	= new HashMap<String, Object>() ;
		ParamMap			 apiInfoMap		= null;
		String				 prg_id			= "IF_PASSGAPI_02_004";  //판매불가처리(결품처리)
		String 				 resultMsg		= "재고부족 판매자 취소처리";
		String 				 resultCode		= "0000";
		String 				 precanYn		= "0";
		
		try {
			for(HashMap<String ,Object> map : resultMap) {
				if(!("100001").equals(map.get("RESULT_CODE"))) continue; //100001 - 재고없음
				HashMap<String, Object> refusalMap = paSsgDeliveryService.selectRefusalInfo(map.get("MAPPING_SEQ").toString());
				itemList.add(refusalMap);
			}
			
			if(itemList.size() == 0) return;
			
			apiInfoMap = new ParamMap();
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", itemList.get(0).get("PA_CODE").toString());
			
			for(int i = 0; i < itemList.size(); i++) {
				Map<String, Object> requestNoSellRequestRegist = new HashMap<String, Object>();
				ParamMap apiDataMap	= new ParamMap();
				
				requestNoSellRequestRegist.put("shppNo", itemList.get(i).get("SHPP_NO").toString()); //배송번호
				requestNoSellRequestRegist.put("scEvnt", "I"); // 등록/삭제구분 : 품절일 경우 결품으로 '등록'해야 한다.
				requestNoSellRequestRegist.put("shppSeq", itemList.get(i).get("SHPP_SEQ").toString()); //배송순번
				requestNoSellRequestRegist.put("shortgRsnCd", "09"); // 등록/삭제구분 : 08[상품정보오류], 09[결품]
				requestNoSellRequestRegist.put("shortgProcDtlc", itemList.get(i).get("SHORTG_PROC_DTLC").toString()); //판매불가사유
				requestNoSellRequestRegist.put("itemId", itemList.get(i).get("ITEM_ID").toString()); //상품코드
				
				apiDataMap.put("requestNoSellRequestRegist", requestNoSellRequestRegist);
				
				connectResult = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				
				if(!"SUCCESS".equals(((Map<String, Object>)connectResult.get("result")).get("resultMessage").toString())) {
					itemList.get(i).put("preCanYn", "0");
					itemList.get(i).put("apiResultMessage" , String.valueOf(((Map<String, Object>)connectResult.get("result")).get("resultDesc").toString()));
					itemList.get(i).put("apiResultCode"	   , "500");
				} else {
					itemList.get(i).put("preCanYn"		   , "1");
					itemList.get(i).put("apiResultMessage" , resultMsg);
					itemList.get(i).put("apiResultCode"	   , resultCode);
				}
			}
		} catch(Exception e) {
			precanYn   = "0";
			resultMsg  = PaSsgComUtill.getErrorMessage(e);
			resultCode = "500";
			log.error("Ssg orderRefusal Fail :: " + resultMsg);
		} finally {
			for( Map<String, Object> map : itemList) {
				if(!map.containsKey("preCanYn")) {
					map.put("preCanYn"		   , precanYn);
					map.put("apiResultMessage" , resultMsg);
					map.put("apiResultCode"	   , resultCode);					
				}
				paSsgDeliveryService.updatePreCanYn(map);
			}
		}
	}
	
	/**
	 * SSG 반품 데이터 생성
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
				orderClaimTargetDtList = paSsgClaimService.selectOrderCalimTargetDt30List(paramMap); 
			} else {
				orderClaimTargetDtList = paSsgClaimService.selectOrderCalimTargetDt20List(paramMap); //출고전 반품
			}
			if(orderClaimTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectOrderCalimTargetDt30(20)List" });
			
			//2) 반품 VO 데이터 생성
			orderClaimVO = setOrderClaimVO((HashMap<String, Object>)orderClaimTargetDtList.get(0));
			paclaimService.saveOrderClaimTx(orderClaimVO);
		
		}catch(Exception e) {
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + PaSsgComUtill.getErrorMessage(e);
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PASSG_ORDER_CLAIM_A");
			
			//2) TPAORDERM Update
			OrderClaimVO[] orderClaimList = new OrderClaimVO[1];
			orderClaimList[0] 			  = orderClaimVO;
			updatePaOrdermTxForRollback(orderClaimList, e);
			
		}finally {
			saveApiTracking(paramMap, request);
		}
		
	}
	
	/**
	 * SSG 반품 취소 데이터 생성
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
			cancelInputTargetDtList = paSsgClaimService.selectClaimCancelTargetDtList(paramMap); 
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
					preCancelMap.put("MAPPING_SEQ"		, String.valueOf(hmSheet.get("MAPPING_SEQ")));
					preCancelMap.put("apiResultMessage"	, getMessage("pa.before_claim_create_cancel"));
					paSsgDeliveryService.updatePreCanYn(preCancelMap);

					break;	
			}
			
		}catch(Exception e){
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + PaSsgComUtill.getErrorMessage(e);
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("apiCode"  , "PASSG_CLAIM_CANCEL_A");
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
	 * SSG 교환 취소 데이터 생성
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
			changeCancelTargetDtList = paSsgClaimService.selectChangeCancelTargetDtList(paramMap);
			if(changeCancelTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectChangeCancelTargetDtList" });
			
			orderClaimVOArray = new OrderClaimVO[changeCancelTargetDtList.size()];
			
			for(Object hm : changeCancelTargetDtList) {
				hmSheet = (HashMap<String, Object>)hm;
				switch (hmSheet.get("PRE_CANCEL_YN").toString()) {
				case "0":
					orderClaimVO = new OrderClaimVO();
					orderClaimVO = setOrderClaimVO(hmSheet);
					orderClaimVOArray[index] = orderClaimVO;
					preCanYn = false;
					break;

				case "1":
					paSsgClaimService.preOrderChangeCancelTx(hmSheet);
					preCanYn = true;
					break;
				}
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
			paramMap.put("apiCode"  , "PASSG_CHANGE_CANCEL");
			updatePaOrdermTxForRollback(orderClaimVOArray, e);	
		} finally {
			saveApiTracking(paramMap, request);
		}
	}
	
	public void custCounselInputAsync(PaqnamVO paqnam, HttpServletRequest request) {
		ParamMap paramMap = new ParamMap();
		
		try {
			PaSsgCounselService.saveCustCounselTx(paqnam);
		} catch(Exception e) {
			paramMap.put("apiCode", "PASSG_CUSTCOUNSEL_INPUT_A");
			paramMap.put("message", "PA_COUNSEL_SEQ : " + paqnam.getPaCounselSeq() + " > " + PaSsgComUtill.getErrorMessage(e));
		} finally {
			saveApiTracking(paramMap, request);
		}
	}
	
	@Async
	public void spPagoodsSyncSsg(HttpServletRequest request, String goodsCode, String userId) throws Exception {
		ParamMap paramMap							 = new ParamMap();
		ParamMap stopSaleParam						 = new ParamMap();
		ParamMap stopShipParam 						 = new ParamMap();
		ParamMap stopOfferParam						 = new ParamMap();
		ParamMap ssgFoodDataParam				     = new ParamMap();
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
		int eTVLimitMargin = 0;
		
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paGroupCode", "10");
		paramMap.put("dateTime", dateTime);
		paramMap.put("conditionDay", conditionDay);		
		paramMap.put("comparePaCode", "A1");
		paramMap.put("feeCode", "O699");
		paramMap.put("minMarginCode", "86");		 
		paramMap.put("minPriceCode", "87");			
		
		paramMap.put("siteGb", userId);
		paramMap.put("apiCode", "IF_PACOMMON_00_002");
		
		// TODO Auto-generated method stub
		log.info("Step1. 신세계 상품이미지 동기화 START");
		curImageInfo = paCommonService.selectCurImageInfoList(paramMap);
		if(curImageInfo.size() > 0) {
			sb = new StringBuffer();
			for(PaGoodsImage curImageInfoTarget : curImageInfo) {
				//이미지 없는 케이스가 많아 for문안에 try catch 처리
				try {
					curImageInfoTarget.setModifyId(Constants.PA_SSG_PROC_ID);
					curImageInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					curImageInfoTarget.setRemark(dateTime);
					resultMsg = paCommonService.saveCurImageInfoTx(curImageInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step1 상품이미지 동기화 신세계 Fail > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
						sb.append(curImageInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step1  상품이미지 동기화 신세계 Sucess > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
				} catch(Exception e) {
					log.info("Step1. 신세계 상품이미지 동기화 : " + e.getMessage());
					paramMap.put("code", "500");
					paramMap.put("message", "Step1. 신세계 상품이미지 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}
		}
		log.info("Step1. 신세계 상품이미지 동기화 END");
		
		log.info("Step2. 신세계 상품가격 동기화 START");
		curPriceInfo = paCommonService.selectCurPriceInfoList(paramMap);
		eTVLimitMargin = paCommonService.selectEtvMarginCheck();
		
		if(curPriceInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaGoodsPriceVO curPriceInfoTarget : curPriceInfo) {
					
					paramMap.put("paCode", curPriceInfoTarget.getPaCode());
					paramMap.put("taxYn", curPriceInfoTarget.getTaxYn());
					minMarginPrice = paCommonService.selectMinMarginPrice(paramMap);
					
					if( (ComUtil.objToDouble(curPriceInfoTarget.getMarginRate()) < ComUtil.objToDouble(minMarginPrice.get("MIN_MARGIN").toString()) && "N".equals(curPriceInfoTarget.getEventYn()))
					 || (ComUtil.objToDouble(curPriceInfoTarget.getMarginRate()) < ComUtil.objToDouble(minMarginPrice.get("EVENT_MIN_MARGIN").toString()) && "Y".equals(curPriceInfoTarget.getEventYn()))
					 || (ComUtil.objToDouble(curPriceInfoTarget.getSalePrice()) < ComUtil.objToDouble(minMarginPrice.get("MIN_SALE_PRICE").toString()))
					 || ("61".equals(curPriceInfoTarget.getSourcingMedia()) && "1".equals(curPriceInfoTarget.getMobileEtvYn()) && ComUtil.objToDouble(curPriceInfoTarget.getMarginRate()) < eTVLimitMargin) ) {
						
						stopSaleParam.put("paGroupCode", "10");
						stopSaleParam.put("paCode", curPriceInfoTarget.getPaCode());
						stopSaleParam.put("goodsCode", curPriceInfoTarget.getGoodsCode());
						stopSaleParam.put("dateTime", dateTime);
						stopSaleParam.put("userId", Constants.PA_SSG_PROC_ID);
						stopSaleParam.put("paGoodsCode", curPriceInfoTarget.getPaGoodsCode());
						stopSaleParam.put("note", "마진10이하 혹은 판매가100원 미만/모바일eTV 마진");
						stopSaleParam.put("priceStopSale", "Y");
						
						//연동제외처리
						resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
					} else {
						curPriceInfoTarget.setModifyId(Constants.PA_SSG_PROC_ID);
						curPriceInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						curPriceInfoTarget.setDateTime(dateTime);
						resultMsg = paCommonService.saveCurPriceInfoTx(curPriceInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step2 상품가격 동기화 신세계 Fail > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
						sb.append(curPriceInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step2 상품가격 동기화 신세계 Sucess > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
				}
			} catch(Exception e) {
				log.info("Step2. 신세계 상품가격 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step2. 신세계 상품가격 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step2. 신세계 상품가격 동기화 END");
		
		log.info("Step3. 신세계 고객부담 배송비 동기화 START");
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
							
							stopSaleParam.put("paGroupCode", "10");
							stopSaleParam.put("paCode", curShipStopSaleTarget.get("PA_CODE").toString());
							stopSaleParam.put("goodsCode", curShipStopSaleTarget.get("GOODS_CODE").toString());
							stopSaleParam.put("dateTime", dateTime);
							stopSaleParam.put("userId", "PASSG");
							stopSaleParam.put("paGoodsCode", curShipStopSaleTarget.get("PA_GOODS_CODE").toString());
							stopSaleParam.put("note", "배송비유료변경");
							stopSaleParam.put("priceStopSale", "N");
							
							//연동제외처리
							resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
						}
						
					} else {
						curShipCostInfoTarget.setModifyId("PASSG");
						curShipCostInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						resultMsg = paCommonService.saveCurShipCostInfoTx(curShipCostInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step3 고객부담배송비 신세계 Fail");
						sb.append(curShipCostInfoTarget.getEntpCode() + "|" + curShipCostInfoTarget.getShipCostCode() + ", ");
						continue;
					}
					log.info("Step3 고객부담배송비 신세계 Sucess");
				}
			} catch(Exception e) {
				log.info("Step3. 신세계 고객부담배송비 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step3. 신세계 고객부담배송비 동기화 : " + e.getMessage() + "INFO : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step3. 신세계 고객부담 배송비 동기화 END");
		
		log.info("Step4. 신세계 출고지/회수지 동기화 START");
		curEntpSlipInfo = paCommonService.selectCurEntpSlipInfoList(paramMap);
		if(curEntpSlipInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaEntpSlip curEntpSlipInfoTarget : curEntpSlipInfo) {
					
					curEntpSlipInfoTarget.setModifyId("PASSG");
					curEntpSlipInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					resultMsg = paCommonService.saveCurEntpSlipInfoTx(curEntpSlipInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step4 출고/회수지 동기화 신세계 Fail");
						sb.append(curEntpSlipInfoTarget.getEntpCode() + ", ");
						continue;
					}
					log.info("Step4 출고/회수지 동기화 신세계 Sucess");
				}
			} catch(Exception e) {
				log.info("Step4. 신세계 출고/회수지 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step4. 신세계 출고/회수지 동기화 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step4. 신세계 출고지/회수지 동기화 END");
		
		log.info("Step5. 신세계 상품판매단계 동기화 START");
		curSaleStop = paCommonService.selectCurSaleStopList(paramMap);
		
		if(curSaleStop.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curSaleStopTarget : curSaleStop) {
					
					stopSaleParam.put("paGroupCode", "10");
					stopSaleParam.put("paCode", curSaleStopTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curSaleStopTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PASSG");
					stopSaleParam.put("paGoodsCode", curSaleStopTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "SK스토아상품판매중단");
					stopSaleParam.put("priceStopSale", "N");
					if("19".equals(curSaleStopTarget.get("PA_SALE_GB").toString())) {
						stopSaleParam.put("disposalYn", "Y"); // 폐기판매종료 상품
					} else {
						stopSaleParam.put("disposalYn", "N");
					}
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step5 상품판매단계 동기화 신세계 Fail > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
						sb.append(curSaleStopTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step5 상품판매단계 동기화 신세계 Sucess > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step5. 신세계 상품판매단계 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step5. 신세계 상품판매단계 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step5. 신세계 상품판매단계 동기화 END");
		
		log.info("Step6. 신세계 행사 종료 상품 마진 체크 동기화 START");
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
					stopSaleParam.put("userId", "PASSG");
					stopSaleParam.put("paGoodsCode", curEventMarginTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "행사종료");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step6 행사 종료 상품 마진 체크 신세계 Fail > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
						sb.append(curEventMarginTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step6 행사 종료 상품 마진 체크 신세계 Sucess > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
					//}					
				}
			} catch(Exception e) {
				log.info("Step6. 신세계 행사 종료 상품 마진 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step6. 신세계 행사 종료 상품 마진 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step6. 신세계 행사 종료 상품 마진 체크 동기화 END");
		/*
		log.info("Step7. 신세계 단품 개수 체크 START");
		curCheckDtCnt = paCommonService.selectCurCheckDtCntList(paramMap);
		if(curCheckDtCnt.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curCheckDtCntTarget : curCheckDtCnt) {					
					stopSaleParam.put("paGroupCode", "10");
					stopSaleParam.put("paCode", curCheckDtCntTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curCheckDtCntTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PASSG");
					stopSaleParam.put("paGoodsCode", curCheckDtCntTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "단품개수 100개 초과");
					stopSaleParam.put("paStatus", "90");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step7 단품 개수 체크 신세계 Fail > GOODS_CODE : " + curCheckDtCntTarget.get("GOODS_CODE").toString());
						sb.append(curCheckDtCntTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step7 단품 개수 체크 신세계 Sucess > GOODS_CODE : " + curCheckDtCntTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step7. 신세계 단품 개수 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step7. 신세계 단품 개수 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step7. 신세계 단품 개수 체크 END");
		*/
		log.info("Step8. 신세계 프로모션 START");
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
				log.error("Step8. SSG프로모션 에러 : " + paPromoGoodsPriceList.get(i).getGoodsCode());
			}
			compareGoodsCode = paPromoGoodsPriceList.get(i).getGoodsCode();
		}
		
		log.info("Step8. 신세계 프로모션 END");
		
		log.info("Step9. 신세계 농/수/축산물 정보고시 체크  START");
		List<PaSsgGoodsVO> paSsgGoodsOfferCodeList = paCommonService.selectPaSsgGoodsOfferList(paramMap);

		for(PaSsgGoodsVO paSsgGoodsVo : paSsgGoodsOfferCodeList) {
			
			if("N".equals(paSsgGoodsVo.getSsgFoodYn())) { // TGOODSSSGFOOD에 데이터 미존재
				stopOfferParam.put("paGroupCode", "10");
				stopOfferParam.put("paCode", paSsgGoodsVo.getPaCode());
				stopOfferParam.put("goodsCode", paSsgGoodsVo.getGoodsCode());
				stopOfferParam.put("dateTime", dateTime);
				stopOfferParam.put("userId", "PASSG");
				stopOfferParam.put("paGoodsCode", paSsgGoodsVo.getItemId());
				stopOfferParam.put("note", "농/수/축산물 필수값 없을 시 연동불가");
				stopOfferParam.put("ssgFoodYn", "Y");
				
				resultMsg = paCommonService.saveCurSaleStopInfoTx(stopOfferParam);
				
			} else { // TGOODSSSGFOOD에 데이터 존재, 동기화 대상
				ssgFoodDataParam.put("goodsCode", paSsgGoodsVo.getGoodsCode());
				ssgFoodDataParam.put("dateTime", dateTime);
				
				resultMsg = paCommonService.saveSsgFoodInfoTx(ssgFoodDataParam);
			}
			
			if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
				log.info("Step9 신세계 농/수/축산물 정보고시 체크 > GOODS_CODE : " + paSsgGoodsVo.getGoodsCode());
				continue;
			}
		}
		log.info("Step9. 신세계 농/수/축산물 정보고시 체크 END");
		
		log.info("Step10. 신세계 착불상품 체크  START");
		List<PaSsgGoodsVO> paSsgCollectGoodsList = paCommonService.selectPaSsgCollectGoodsList(paramMap);

		for(PaSsgGoodsVO paSsgGoodsVo : paSsgCollectGoodsList) {
			
			stopOfferParam.put("paGroupCode", "10");
			stopOfferParam.put("paCode", paSsgGoodsVo.getPaCode());
			stopOfferParam.put("goodsCode", paSsgGoodsVo.getGoodsCode());
			stopOfferParam.put("dateTime", dateTime);
			stopOfferParam.put("userId", "PASSG");
			stopOfferParam.put("paGoodsCode", paSsgGoodsVo.getItemId());
			stopOfferParam.put("note", "착불상품 연동불가");
			
			resultMsg = paCommonService.saveCurSaleStopInfoTx(stopOfferParam);
			
			if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
				log.info("Step10 신세계 착불상품 체크 > GOODS_CODE : " + paSsgGoodsVo.getGoodsCode());
				continue;
			}
		}
		log.info("Step9. 신세계 착불상품 체크 END");
	}

	@Async
	@SuppressWarnings("unchecked")
	public void asyncGoodsModify(HttpServletRequest request, ParamMap asyncMap, ParamMap bodyMap,
			PaSsgGoodsVO paSsgGoods, List<PaSsgGoodsdtMapping> goodsdtMapping) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> result		= new HashMap<String, Object>();
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		try {
			map = paSsgConnectUtil.apiGetObjectBySsg(asyncMap, bodyMap);
			
			result = (HashMap<String,Object>)map.get("result");
			
			result.put("modCase", "modify");
			
			if(result.get("resultCode").toString().equals("00")) {
				
				List<Map<String, Object>> uitems  = new ArrayList<Map<String,Object>>();
				uitems = (List<Map<String, Object>>)(result.get("uitems"));
				
				if(uitems != null) {
					List<Map<String, Object>> uitem  = new ArrayList<Map<String,Object>>();
					
					if(uitems.get(0).get("uitem") instanceof Map<?, ?>) {
						uitem.add((Map<String, Object>)(uitems.get(0).get("uitem")));
					}else {
						uitem = (List<Map<String, Object>>)(uitems.get(0).get("uitem"));
					}
					
					for(int i=0; i<uitem.size(); i++) {
						for(int j=0; j<goodsdtMapping.size(); j++) {
							String tempGoodsdtCode = goodsdtMapping.get(j).getPaCode() + goodsdtMapping.get(j).getGoodsCode() + goodsdtMapping.get(j).getGoodsdtCode() + goodsdtMapping.get(j).getGoodsdtSeq();
							
							if(tempGoodsdtCode.equals(uitem.get(i).get("tempUitemId").toString())) {
								goodsdtMapping.get(j).setPaOptionCode(uitem.get(i).get("uitemId").toString());
								goodsdtMapping.get(j).setTransOrderAbleQty(uitem.get(i).get("baseInvQty").toString());
								continue;
							}
						}
					}
				}
				
				rtnMsg = paSsgGoodsService.savePaSsgGoodsTx(paSsgGoods, goodsdtMapping);
				
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
					asyncMap.put("code", "500");
					asyncMap.put("message", paSsgGoods.getGoodsCode() + " : 통신성공 후 저장중 에러");
				} else {
					asyncMap.put("code", "200");
					asyncMap.put("message", "OK");
				}
			} else {
				asyncMap.put("code", "500");
				asyncMap.put("message", "[" + paSsgGoods.getGoodsCode() + "] " + result.get("resultDesc"));
				
				if(   String.valueOf(result.get("resultDesc")).indexOf("금칙어") != -1 
				   || String.valueOf(result.get("resultDesc")).indexOf("중복 된 옵션") != -1
				   || String.valueOf(result.get("resultDesc")).indexOf("성인상품 또는 주류상품") != -1
				   || String.valueOf(result.get("resultDesc")).indexOf("특수문자 제외하고 2byte 이상") != -1
				   || String.valueOf(result.get("resultDesc")).indexOf("판매가는 기존대비 80%") != -1
				   || String.valueOf(result.get("resultDesc")).indexOf("입력하신 판매가가") != -1
				   || String.valueOf(result.get("resultDesc")).indexOf("수정이 불가능합니다") != -1
				   || String.valueOf(result.get("resultDesc")).indexOf("해당 카테고리에 등록할 수 없는 과세구분") != -1) {
					
					String returnNote = result.get("resultDesc").toString();
					
					paSsgGoods.setPaSaleGb("30");
					paSsgGoods.setPaStatus("90");
					paSsgGoods.setReturnNote(returnNote);
					rtnMsg = paSsgGoodsService.savePaSsgGoodsErrorTx(paSsgGoods);
					
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						asyncMap.put("code", "500");
						asyncMap.put("message",  result.get("resultDesc") + " || " + rtnMsg);
					}else {
						asyncMap.put("code", "200");
						asyncMap.put("message",  result.get("resultDesc"));
					}
				}
			}
			
			insertPaGoodsTransLog(paSsgGoods, result);
			
		}catch (Exception e) {
			asyncMap.put("code", "500");
			asyncMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		} finally {
			if(!"200".equals(asyncMap.get("code").toString())) {
				systemService.insertApiTrackingTx(request, asyncMap);
			}
		}
	}
	
	public void insertPaGoodsTransLog(PaSsgGoodsVO paSsgGoods, Map<String, Object> map) throws Exception {
		
		String code = "";
		String message = "";
		String itemId = "null";
		if(!"00".equals(String.valueOf(map.get("resultCode")))){
			code = "500";
			message = String.valueOf(map.get("resultDesc"));
		} else {
			code = "200";
			message = "상품 연동 성공";
		}
		
		if("insert".equals(map.get("modCase"))) {
			if("00".equals(String.valueOf(map.get("resultCode")))) {
				itemId = map.get("itemId").toString();
			}
		}else {
			itemId = paSsgGoods.getItemId();
		}
		
		String dateTime = systemService.getSysdatetimeToString();
		
		PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
		paGoodsTransLog.setGoodsCode(paSsgGoods.getGoodsCode());
		paGoodsTransLog.setPaCode(paSsgGoods.getPaCode());
		paGoodsTransLog.setItemNo("null".equals(itemId) ? paSsgGoods.getGoodsCode() : itemId);
		paGoodsTransLog.setRtnCode(code);
		paGoodsTransLog.setRtnMsg(message);
		paGoodsTransLog.setSuccessYn(code.equals("200") ? "1" : "0");
		paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		paGoodsTransLog.setProcId(Constants.PA_SSG_PROC_ID);
		paCommonDAO.insertPaGoodsTransLog(paGoodsTransLog);		
	}
	
	public void noteCounselInputAsync(PaqnamVO panotem, HttpServletRequest request) {
		ParamMap paramMap = new ParamMap();
		
		try {
			PaSsgCounselService.saveNoteCounselTx(panotem);
		} catch(Exception e) {
			paramMap.put("apiCode", "PASSG_NOTECOUNSEL_INPUT_A");
			paramMap.put("message", "PA_COUNSEL_SEQ : " + panotem.getPaCounselSeq() + " > " + PaSsgComUtill.getErrorMessage(e));
		} finally {
			saveApiTracking(paramMap, request);
		}
	}
}
