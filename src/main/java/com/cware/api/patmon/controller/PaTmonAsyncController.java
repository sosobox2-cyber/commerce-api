package com.cware.api.patmon.controller;

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
import com.cware.netshopping.domain.PaTmonGoodsVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.patmon.claim.service.PaTmonClaimService;
import com.cware.netshopping.patmon.delivery.service.PaTmonDeliveryService;
import com.cware.netshopping.patmon.goods.service.PaTmonGoodsService;
import com.cware.netshopping.patmon.util.PaTmonComUtill;
import com.cware.netshopping.patmon.util.PaTmonConnectUtil;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@Api(value="/patmon/claim")
@Controller("com.cware.api.patmon.PaTmonAsycController")
@RequestMapping(value="/patmon/claim")
public class PaTmonAsyncController extends AbstractController{
	
	@Autowired 
	private SystemService systemService;
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	@Resource(name = "patmon.goods.paTmonGoodsService")
	private PaTmonGoodsService paTmonGoodsService;
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paorderService;
	@Resource(name = "paTmon.delivery.paTmonDeliveryService")
	private PaTmonDeliveryService paTmonDeliveryService;
	@Resource(name = "pacommon.claim.paclaimService")
	private PaClaimService paclaimService;	
	@Autowired
	private PaTmonConnectUtil paTmonConnectUtil;
	@Autowired
	private PaTmonClaimService paTmonClaimService;
	
	@SuppressWarnings("unchecked")
	public void insertPaGoodsTransLog(PaTmonGoodsVO paTmonGoods, Map<String, Object> map) throws Exception {
		
		String code = "";
		String message = "";
		String vendorDealNo = "null";
		if(!"null".equals(String.valueOf(map.get("error")))){
			code = "500";
			message = String.valueOf(ComUtil.subStringBytes(map.get("error").toString(), 900));
		} else {
			code = "200";
			message = "상품 연동 성공";
			HashMap<String, Object> dealNo = (HashMap<String, Object>) map.get("dealNo");
			vendorDealNo = String.valueOf(dealNo.get(paTmonGoods.getPaCode() + "-" + paTmonGoods.getGoodsCode()).toString());
		}
		String dateTime = systemService.getSysdatetimeToString();
		
		PaGoodsTransLog paGoodsTransLog = new PaGoodsTransLog();
		paGoodsTransLog.setGoodsCode(paTmonGoods.getGoodsCode());
		paGoodsTransLog.setPaCode(paTmonGoods.getPaCode());
		paGoodsTransLog.setItemNo("null".equals(vendorDealNo) ? paTmonGoods.getGoodsCode() : vendorDealNo);
		paGoodsTransLog.setRtnCode(code);
		paGoodsTransLog.setRtnMsg(message);
		paGoodsTransLog.setSuccessYn(code.equals("200") ? "1" : "0");
		paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		paGoodsTransLog.setProcId(Constants.PA_TMON_PROC_ID);
		paTmonGoodsService.insertPaTmonGoodsTransLogTx(paGoodsTransLog);		
	}
	
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
			
			orderInputTargetDtList = paTmonDeliveryService.selectOrderInputTargetDtList(paramMap);  
			
			if(orderInputTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectOrderInputTargetDtList" });
			
			//=2) 1)의 데이터를 가지고 BO 데이터를 생성하기위해 VO SETTING
			orderInputVO = new OrderInputVO[orderInputTargetDtList.size()];
			PaTmonComUtill.replaceCamelList(orderInputTargetDtList);
			for(Map<String, Object> map : orderInputTargetDtList) {
				
				// = 가격비교
				String paApplyDate   = map.get("applyDate").toString(); // TPAGOODSPRICE 기준 APPLY_DATE
				String stoaApplyDate = map.get("stoaApplyDate").toString(); // TGOODSPRICE 기준 APPLY_DATE
				String paOrderDate   = map.get("orderDate").toString(); // 티몬 주문일시
				
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
				
				//=DB에서 가져온 값은 자동으로 VO SETTING
				vo = (OrderInputVO)PaTmonComUtill.map2VO(map, OrderInputVO.class); 

                //= 프로모션(TORDERPROMO) 용 객체 생성         
				if(Double.parseDouble(String.valueOf(map.get("outPromoPrice"))) > 0) {  
					
	                OrderpromoVO orderPaPromo = new OrderpromoVO();
	               // String promoNo = vo.getPromoNo();
	                double doAmt = Long.parseLong(String.valueOf(map.get("outPromoPrice"))); 
	                double ownCost = Long.parseLong(String.valueOf(map.get("ownCost")));
	                double entpCost = Long.parseLong(String.valueOf(map.get("entpCost")));
	                
	                orderPaPromo.setPromoNo(vo.getPromoNo()); 
	                orderPaPromo.setDoType("30");
	                orderPaPromo.setDoAmt(doAmt); 
	                orderPaPromo.setProcCost(doAmt);
	                orderPaPromo.setOwnProcCost(ownCost);
	                orderPaPromo.setEntpProcCost(entpCost);
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
				vo.setProcUser    	(Constants.PA_TMON_PROC_ID);
				
				orderInputVO[index] = vo;
				index++;
			}
			
			//=3) BO 데이터 생성
			resultMap = paorderService.saveOrderTx(orderInputVO);
			
		}catch (Exception e) {
			updatePaOrdermTxForRollback(orderInputVO, e);
			
			paramMap = new ParamMap();
			paramMap.put("apiCode"	, "PATMON_ORDER_INPUT_A");
			paramMap.put("message"	, "pa_order_no : " + paOrderNo + " > " + PaTmonComUtill.getErrorMessage(e));
		
		}finally {
			saveApiTracking(paramMap, request);
		}
		
		//재고 부족으로 인한 주문 거절(판매자 주문 취소)
		refusalOrder(resultMap, paOrderNo);
		
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
			
			String errMsg = PaTmonComUtill.getErrorMessage(e);
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
			paramMap.put("siteGb"		, Constants.PA_TMON_PROC_ID);
			systemService.insertApiTrackingTx(request, paramMap);
			
		}catch(Exception ee){
			log.error("ApiTracking Insert Error : "+ee.toString());
		}
	}

	@Async
	public void spPagoodsSyncTmon(HttpServletRequest request, String goodsCode, String userId) throws Exception {

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
		List<HashMap<String, String>> curGoodsName = null;
		
		String resultMsg = "";
		String dateTime = systemService.getSysdatetimeToString();
		StringBuffer sb = null;
		int conditionDay = 2;		
		int eTVLimitMargin = 0;
		
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paGroupCode", "09");
		paramMap.put("dateTime", dateTime);
		paramMap.put("conditionDay", conditionDay);		
		paramMap.put("comparePaCode", "91");
		paramMap.put("feeCode", "O698");
		paramMap.put("minMarginCode", "84");		 
		paramMap.put("minPriceCode", "85");			
		
		paramMap.put("siteGb", userId);
		paramMap.put("apiCode", "IF_PACOMMON_00_002");
		
		log.info("Step1. 티몬 상품이미지 동기화 START");
		curImageInfo = paCommonService.selectCurImageInfoList(paramMap);
		if(curImageInfo.size() > 0) {
			sb = new StringBuffer();
			for(PaGoodsImage curImageInfoTarget : curImageInfo) {
				//이미지 없는 케이스가 많아 for문안에 try catch 처리
				try {
					curImageInfoTarget.setModifyId("PATMON");
					curImageInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					curImageInfoTarget.setRemark(dateTime);
					resultMsg = paCommonService.saveCurImageInfoTx(curImageInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step1 상품이미지 동기화 티몬 Fail > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
						sb.append(curImageInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step1  상품이미지 동기화 티몬 Sucess > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
				} catch(Exception e) {
					log.info("Step1. 티몬 상품이미지 동기화 : " + e.getMessage());
					paramMap.put("code", "500");
					paramMap.put("message", "Step1. 티몬 상품이미지 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}
		}
		log.info("Step1. 티몬 상품이미지 동기화 END");
		

		log.info("Step2. 티몬 상품가격 동기화 START");
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
						
						stopSaleParam.put("paGroupCode", "09");
						stopSaleParam.put("paCode", curPriceInfoTarget.getPaCode());
						stopSaleParam.put("goodsCode", curPriceInfoTarget.getGoodsCode());
						stopSaleParam.put("dateTime", dateTime);
						stopSaleParam.put("userId", "PATMON");
						stopSaleParam.put("paGoodsCode", curPriceInfoTarget.getPaGoodsCode());
						stopSaleParam.put("note", "마진10이하 혹은 판매가100원 미만/모바일eTV 마진");
						stopSaleParam.put("priceStopSale", "Y");
						
						//연동제외처리
						resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
					} else {
						curPriceInfoTarget.setModifyId("PATMON");
						curPriceInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						curPriceInfoTarget.setDateTime(dateTime);
						resultMsg = paCommonService.saveCurPriceInfoTx(curPriceInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step2 상품가격 동기화 티몬 Fail > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
						sb.append(curPriceInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step2 상품가격 동기화 티몬 Sucess > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
				}
			} catch(Exception e) {
				log.info("Step2. 티몬 상품가격 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step2. 티몬 상품가격 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step2. 티몬 상품가격 동기화 END");	

		log.info("Step3. 티몬 고객부담배송비 동기화 START");
		curShipCostInfo = paCommonService.selectCurShipCostInfoList(paramMap);
		if(curShipCostInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaCustShipCostVO curShipCostInfoTarget : curShipCostInfo) {
					
					if(((curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("CN") || curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("PL"))
														&& curShipCostInfoTarget.getShipCostBaseAmt() < 100 ) 
					  || curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("QN")
					  || (!curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("FR") && curShipCostInfoTarget.getOrdCost() < 100)) {  
						//배송정책이 CN, PL이면서 기준금액이 100원 이하인경우, 배송비정책이 QN인 경우, 무료배송이 아니면서 배송비 금액이 0원인 경우 연동 제외처리
						
						stopShipParam.put("paCode", curShipCostInfoTarget.getPaCode());
						stopShipParam.put("entpCode", curShipCostInfoTarget.getEntpCode());
						stopShipParam.put("shipCostCode", curShipCostInfoTarget.getShipCostCode());
						
						curShipStopSale = paCommonService.selectCurShipStopSaleList(stopShipParam);
						for(HashMap<String, String> curShipStopSaleTarget : curShipStopSale) {
							
							stopSaleParam.put("paGroupCode", "09");
							stopSaleParam.put("paCode", curShipStopSaleTarget.get("PA_CODE").toString());
							stopSaleParam.put("goodsCode", curShipStopSaleTarget.get("GOODS_CODE").toString());
							stopSaleParam.put("dateTime", dateTime);
							stopSaleParam.put("userId", "PATMON");
							stopSaleParam.put("paGoodsCode", curShipStopSaleTarget.get("PA_GOODS_CODE").toString());
							stopSaleParam.put("note", "배송비유료변경");
							stopSaleParam.put("priceStopSale", "N");
							
							//연동제외처리
							resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
						}
						
					} else {
						curShipCostInfoTarget.setModifyId("PATMON");
						curShipCostInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						resultMsg = paCommonService.saveCurShipCostInfoTx(curShipCostInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step3 고객부담배송비 티몬 Fail");
						sb.append(curShipCostInfoTarget.getEntpCode() + "|" + curShipCostInfoTarget.getShipCostCode() + ", ");
						continue;
					}
					log.info("Step3 고객부담배송비 티몬 Sucess");
				}
			} catch(Exception e) {
				log.info("Step3. 티몬 고객부담배송비 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step3. 티몬 고객부담배송비 동기화 : " + e.getMessage() + "INFO : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step3. 티몬 고객부담배송비 동기화 END");
		
		log.info("Step4. 티몬 회수지 동기화 START");
		curEntpSlipInfo = paCommonService.selectCurEntpSlipInfoList(paramMap);
		if(curEntpSlipInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaEntpSlip curEntpSlipInfoTarget : curEntpSlipInfo) {
					
					curEntpSlipInfoTarget.setModifyId("PATMON");
					curEntpSlipInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					resultMsg = paCommonService.saveCurEntpSlipInfoTx(curEntpSlipInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step4 회수지 동기화 티몬 Fail");
						sb.append(curEntpSlipInfoTarget.getEntpCode() + ", ");
						continue;
					}
					log.info("Step4 회수지 동기화 티몬 Sucess");
				}
			} catch(Exception e) {
				log.info("Step4. 티몬 회수지 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step4. 티몬 회수지 동기화 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step4. 티몬 회수지 동기화 END");

		log.info("Step5. 티몬 상품판매단계 동기화 START");
		curSaleStop = paCommonService.selectCurSaleStopList(paramMap);
		
		if(curSaleStop.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curSaleStopTarget : curSaleStop) {
					
					stopSaleParam.put("paGroupCode", "09");
					stopSaleParam.put("paCode", curSaleStopTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curSaleStopTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PATMON");
					stopSaleParam.put("paGoodsCode", curSaleStopTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "SK스토아상품판매중단");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step5 상품판매단계 동기화 티몬 Fail > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
						sb.append(curSaleStopTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step5 상품판매단계 동기화 티몬 Sucess > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step5. 티몬 상품판매단계 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step5. 티몬 상품판매단계 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step5. 티몬 상품판매단계 동기화 END");
		

		log.info("Step6. 티몬 행사 종료 상품 마진 체크 START");
		curEventMargin = paCommonService.selectCurEventMarginList(paramMap);
		
		if(curEventMargin.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curEventMarginTarget : curEventMargin) {
					paramMap.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					stopSaleParam.put("paGroupCode", "09");
					stopSaleParam.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curEventMarginTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PATMON");
					stopSaleParam.put("paGoodsCode", curEventMarginTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "행사종료");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step6 행사 종료 상품 마진 체크 티몬 Fail > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
						sb.append(curEventMarginTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step6 행사 종료 상품 마진 체크 티몬 Sucess > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
					//}					
				}
			} catch(Exception e) {
				log.info("Step6. 티몬 행사 종료 상품 마진 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step6. 티몬 행사 종료 상품 마진 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step6. 티몬 행사 종료 상품 마진 체크 END");
		
		log.info("Step7. 티몬 단품 개수 체크 START");
		curCheckDtCnt = paCommonService.selectTmonCurCheckDtCntList(paramMap);
		if(curCheckDtCnt.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curCheckDtCntTarget : curCheckDtCnt) {					
					stopSaleParam.put("paGroupCode", "09");
					stopSaleParam.put("paCode", curCheckDtCntTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curCheckDtCntTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PATMON");
					stopSaleParam.put("paGoodsCode", curCheckDtCntTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "단품개수 200개 초과");
					stopSaleParam.put("paStatus", "90");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step7 단품 개수 체크 티몬 Fail > GOODS_CODE : " + curCheckDtCntTarget.get("GOODS_CODE").toString());
						sb.append(curCheckDtCntTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step7 단품 개수 체크 티몬 Sucess > GOODS_CODE : " + curCheckDtCntTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step7. 티몬 단품 개수 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step7. 티몬 단품 개수 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step7. 티몬 단품 개수 체크 END");
		
		/* 2022-09-16 티몬 60자 제한 ...으로 연동하기 때문에 해당 로직 제거 */
//		log.info("Step8. 티몬 상품명 글자 수 체크 START");
//		curGoodsName = paCommonService.selectTmonCurGoodsNameLengthCheckList(paramMap);
//		if(curGoodsName.size() > 0) {
//			sb = new StringBuffer();
//			try {
//				for(HashMap<String, String> curGoodsNameTarget : curGoodsName) {					
//					stopSaleParam.put("paGroupCode", "09");
//					stopSaleParam.put("paCode", curGoodsNameTarget.get("PA_CODE").toString());
//					stopSaleParam.put("goodsCode", curGoodsNameTarget.get("GOODS_CODE").toString());
//					stopSaleParam.put("dateTime", dateTime);
//					stopSaleParam.put("userId", "PATMON");
//					stopSaleParam.put("paGoodsCode", curGoodsNameTarget.get("PA_GOODS_CODE").toString());
//					stopSaleParam.put("note", "상품명 60자 이상");
//					stopSaleParam.put("paStatus", "90");
//					stopSaleParam.put("priceStopSale", "N");
//					
//					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
//					
//					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
//						log.info("Step8 티몬 상품명 글자 수 체크 티몬 Fail > GOODS_CODE : " + curGoodsNameTarget.get("GOODS_CODE").toString());
//						sb.append(curGoodsNameTarget.get("GOODS_CODE").toString() + ", ");
//						continue;
//					}
//					log.info("Step8 티몬 상품명 글자 수 체크 티몬 Sucess > GOODS_CODE : " + curGoodsNameTarget.get("GOODS_CODE").toString());
//				}
//			} catch(Exception e) {
//				log.info("Step8. 티몬 상품명 글자 수 체크: " + e.getMessage());
//				paramMap.put("code", "500");
//				paramMap.put("message", "Step8. 티몬 상품명 글자 수 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
//				systemService.insertApiTrackingTx(request, paramMap);
//			}
//		}
//		log.info("Step8. 티몬 상품명 글자 수 체크 END");
		
	}

	@Async
	@SuppressWarnings("unchecked")
	public void asyncGoodsModify(HttpServletRequest request,  ParamMap asyncMap, ParamMap bodyMap,
			PaTmonGoodsVO paTmonGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> deal = new HashMap<String, Object>();
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		try {
			map = paTmonConnectUtil.apiGetObjectByTmon(asyncMap, bodyMap); // 통신
			
			if(map.containsKey("deal")) {
				deal = (HashMap<String,Object>)map.get("deal");
				
				if(!"true".equals(String.valueOf(deal.get("success")))){
					asyncMap.put("message", deal.get("message"));
					
					if(String.valueOf(deal.get("message")).indexOf("금칙어") != -1){
						paTmonGoods.setReturnNote(String.valueOf(deal.get("message")));
						paTmonGoods.setPaSaleGb("30");
						paTmonGoods.setPaStatus("90");
						rtnMsg = paTmonGoodsService.savePaTmonGoodsErrorTx(paTmonGoods);
						
						if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
							asyncMap.put("code", "500");
							asyncMap.put("message",  map.get("error")+ " || " + rtnMsg);
						}else {
							asyncMap.put("code", "200");
							asyncMap.put("message",  map.get("error")+ " || " + deal.get("message"));
						}
					} else {
						asyncMap.put("code", "500");
						asyncMap.put("message",  map.get("error") + " || " + deal.get("message"));
					}
					
				} else {
					deal = (HashMap<String, Object>) map.get("deal");
					HashMap<String, Object> dealOptions = (HashMap<String, Object>) map.get("dealOptions");
					
					if("true".equals(String.valueOf(deal.get("success"))) && "true".equals(String.valueOf(dealOptions.get("success")))) {
						
						rtnMsg = paTmonGoodsService.savePaTmonGoodsTx(paTmonGoods, goodsdtMapping, paPromoTargetList);
						
						if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
							asyncMap.put("code", "500");
							asyncMap.put("message", paTmonGoods.getGoodsCode() + " : 통신성공 후 저장중 에러");
						} else {
							asyncMap.put("code", "200");
							asyncMap.put("message", "OK");
						}
					}else {
						asyncMap.put("code", "500");
						//메세지처리
						if(!"null".equals(String.valueOf(deal.get("message")))) {
							asyncMap.put("message", deal.get("message"));
						}else if(!"null".equals(String.valueOf(dealOptions.get("message")))) {
							asyncMap.put("message", dealOptions.get("message"));
						} else {
							asyncMap.put("message", "error!!!");
						}
					}
				}
			} else {
				deal.put("message", map.get("error").toString());
				asyncMap.put("message", deal.get("message"));
				
				if(String.valueOf(deal.get("message")).indexOf("배송템플릿") != -1){
					paTmonGoods.setReturnNote(String.valueOf(deal.get("message")));
					paTmonGoods.setPaSaleGb("30");
					paTmonGoods.setPaStatus("90");
					rtnMsg = paTmonGoodsService.savePaTmonGoodsErrorTx(paTmonGoods);
					
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						asyncMap.put("code", "500");
						asyncMap.put("message",  map.get("error")+ " || " + rtnMsg);
					}else {
						asyncMap.put("code", "200");
						asyncMap.put("message",  map.get("error")+ " || " + deal.get("message"));
					}
				} else {
					asyncMap.put("code", "500");
					asyncMap.put("message",  map.get("error") + " || " + deal.get("message"));
				}
			}
		}catch (Exception e) {
			asyncMap.put("code", "500");
			asyncMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		} finally {
			if(!"200".equals(asyncMap.get("code").toString())) {
				systemService.insertApiTrackingTx(request, asyncMap);
			}
			
			//예외케이스 발생 시 판매 중단처리
			/*
			if(asyncMap.get("message").toString().indexOf("관리명은") > -1) {
				//연동 종료 처리. PA_STATUS = 90, TRANS_TARGET_YN = 1
			}
			*/
		}		
	}
	
	private void refusalOrder(HashMap<String, Object>[] resultMap, String paOrderNo) throws Exception {
		
		if(resultMap == null) return;
		
		ParamMap				apiInfoMap	= null;
		ParamMap				apiDataMap	= null;
		String					prg_id		= "IF_PATMONAPI_04_007";  //판매자 직접취소
		String 					resultMsg	= "재고부족 판매자 취소처리";
		String 					resultCode	= "0000";
		List<Map<String, Object>> itemList  = new ArrayList<Map<String,Object>>();
		Map<String, Object> connectResult	= new HashMap<String, Object>() ;
		String precanYn						= "0";
		
		List<Object> dealList = null;
		Map<String,Object> deal = null;
		List<Object> dealOptionsList = null;
		Map<String,Object> dealOptions = null;
		
		try {
			//=1) 판매자 주문 취소할 LIST를 생성해준다
			for(HashMap<String ,Object> map :  resultMap) {
				if( !("100001").equals(map.get("RESULT_CODE"))) continue; //100001 - 재고없음
				HashMap<String, Object> refusalMap = paTmonDeliveryService.selectRefusalInfo(map.get("MAPPING_SEQ").toString());
				itemList.add(refusalMap);
			}
			
			if(itemList.size() == 0) return;
			
			apiInfoMap = new ParamMap();
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", itemList.get(0).get("PA_CODE").toString());
			
			for(int i=0; i < itemList.size(); i++) {
				dealOptionsList = new ArrayList<Object>();
				dealOptions = new HashMap<String, Object>();
				dealOptions.put("tmonDealOptionNo", itemList.get(i).get("TMON_DEAL_OPTION_NO").toString());
				dealOptions.put("qty", itemList.get(i).get("QTY").toString());
				dealOptionsList.add(dealOptions);
				
				dealList = new ArrayList<Object>();
				deal = new HashMap<String, Object>();
				deal.put("tmonDealNo", itemList.get(i).get("TMON_DEAL_NO").toString());
				deal.put("reason", itemList.get(i).get("REASON").toString());
				deal.put("reasonDetail", itemList.get(i).get("REASON_DETAIL").toString());
				deal.put("tmonDealOptions", dealOptionsList);
				dealList.add(deal);
				
				apiDataMap = new ParamMap();
				apiDataMap.put("tmonOrderNo", itemList.get(i).get("TMON_ORDER_NO").toString());
				apiDataMap.put("deliveryNo", itemList.get(i).get("DELIVERY_NO").toString());
				apiDataMap.put("tmonDeals", dealList);
				
				connectResult = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
				
				if(!"null".equals(String.valueOf(connectResult.get("error")))){
					itemList.get(i).put("preCanYn"			, "0");
					itemList.get(i).put("apiResultMessage"	, String.valueOf(connectResult.get("error")));
					itemList.get(i).put("apiResultCode"		, "500");
					
				} else {
					itemList.get(i).put("preCanYn"			, "1");
					itemList.get(i).put("apiResultMessage"	, resultMsg);
					itemList.get(i).put("apiResultCode"		, resultCode);
				}
			}
			
		}catch (Exception e) {
			precanYn  = "0";
			resultMsg = PaTmonComUtill.getErrorMessage(e);
			resultCode = "500";
			log.error("Tmon orderRefusal Fail :: " + resultMsg);
		}finally {
			
			//UPDATE TPAORDERM.PRE_CANCEL_YN
			for( Map<String, Object> map : itemList) {
				if(!map.containsKey("preCanYn")) {
					map.put("preCanYn"			, precanYn);
					map.put("apiResultMessage"	, resultMsg);
					map.put("apiResultCode"		, resultCode);					
				}
				paTmonDeliveryService.updatePreCanYn(map);
			}
		}
	}
	
	public void cancelInputAsync(HashMap<String, Object> cancelTarget, HttpServletRequest request) {
		ParamMap paramMap = null;
		int executedRtn = 0;
		
		try {
			paramMap = new ParamMap();
			paramMap.setParamMap(cancelTarget);
			paramMap.replaceCamel();
			
			HashMap<String, Object> cancelDtInfo = paTmonClaimService.selectCancelInputTargetDtList(paramMap);
			
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
				cancelInputVO.setProcId	    (Constants.PA_TMON_PROC_ID);
				
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
				paTmonDeliveryService.updatePreCanYn(preCancelMap);
				
				if(executedRtn != 1) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
				}
				break;
			}
		} catch(Exception e) {
			paramMap.put("apiCode", "PATMON_CANCEL_INPUT_A");
			paramMap.put("message", "pa_order_no : " + paramMap.getString("paOrderNo") + " > " + PaTmonComUtill.getErrorMessage(e));
		} finally {
			saveApiTracking(paramMap, request);
		}
	}

	/**
	 * 티몬 반품 데이터 생성
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
				orderClaimTargetDtList = paTmonClaimService.selectOrderCalimTargetDt30List(paramMap); 
			} else {
				orderClaimTargetDtList = paTmonClaimService.selectOrderCalimTargetDt20List(paramMap); //출고전 반품
			}
			if(orderClaimTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectOrderCalimTargetDt30(20)List" });
			
			//2) 반품 VO 데이터 생성
			orderClaimVO = setOrderClaimVO((HashMap<String, Object>)orderClaimTargetDtList.get(0));
			paclaimService.saveOrderClaimTx(orderClaimVO);
		
		}catch(Exception e) {
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + PaTmonComUtill.getErrorMessage(e);
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PATMON_ORDER_CLAIM_A");
			
			//2) TPAORDERM Update
			OrderClaimVO[] orderClaimList = new OrderClaimVO[1];
			orderClaimList[0] 			  = orderClaimVO;
			updatePaOrdermTxForRollback(orderClaimList, e);
			
		}finally {
			saveApiTracking(paramMap, request);
		}
		
	}

	/**
	 * 티몬 반품 취소 데이터 생성
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
			cancelInputTargetDtList = paTmonClaimService.selectClaimCancelTargetDtList(paramMap); 
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
					paTmonDeliveryService.updatePreCanYn(preCancelMap);

					break;	
			}
			
		}catch(Exception e){
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + PaTmonComUtill.getErrorMessage(e);
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("apiCode"  , "PATMON_CLAIM_CANCEL_A");
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
	 * 티몬 교환 데이터 생성
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
		int 		   index = 0;
		int 		   targetSize = 0;
		paramMap.setParamMap(claim);
		paramMap.replaceCamel();
		
		try {
			orderChangeTargetDtList = paTmonClaimService.selectOrderChangeTargetDtList(paramMap);
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
					for(OrderClaimVO claimVO : orderClaimVOArray) {
						paTmonClaimService.updatePaOrdermChangeFlag("03", claimVO.getMappingSeq());
					}
				}
			}
		} catch(Exception e) {
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + PaTmonComUtill.getErrorMessage(e);
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			paramMap.put("apiCode"  , "PATMON_ORDER_CHANGE_A");
			//2) UPDATE TPAORDERM
			updatePaOrdermTxForRollback(orderClaimVOArray, e);
		} finally {
			saveApiTracking(paramMap, request);
		}
	}


	/**
	 * 티몬 교환 취소 데이터 생성
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
			changeCancelTargetDtList = paTmonClaimService.selectChangeCancelTargetDtList(paramMap);
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
					paTmonClaimService.preOrderChangeCancelTx(hmSheet);
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
			paramMap.put("apiCode"  , "PATMON_CHANGE_CANCEL");
			updatePaOrdermTxForRollback(orderClaimVOArray, e);	
		} finally {
			saveApiTracking(paramMap, request);
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
			String checkAddr = paTmonClaimService.compareAddress(paramMap);
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
		if (shpFeeAmt > 0 ){
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
	
}
