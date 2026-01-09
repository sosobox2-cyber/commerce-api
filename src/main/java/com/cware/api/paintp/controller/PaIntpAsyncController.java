package com.cware.api.paintp.controller;

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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.AbstractVO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.CancelInputVO;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.OrderInputVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaCustShipCostVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.PaIntpGoodsVO;
import com.cware.netshopping.domain.PaIntpGoodsdtMappingVO;
import com.cware.netshopping.domain.PaIntpTargetVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.paintp.xml.PaIntpOrderListVO;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.paintp.claim.service.PaIntpClaimService;
import com.cware.netshopping.paintp.goods.service.PaIntpGoodsService;
import com.cware.netshopping.paintp.order.service.PaIntpOrderService;
import com.cware.netshopping.paintp.util.PaIntpComUtil;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@Api(value="/paintp/claim", description="공통")
@Controller("com.cware.api.paintp.PaIntpAsycController")
@RequestMapping(value="/paintp/claim")
public class PaIntpAsyncController extends AbstractController {

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "paintp.claim.paIntpClaimService")
	private PaIntpClaimService paIntpClaimService;
	
	@Resource(name = "pacommon.claim.paclaimService")
	private PaClaimService paclaimService;
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paorderService;
	
	@Resource(name = "paintp.order.paIntpOrderService")
	private PaIntpOrderService paIntpOrderService;
	
	@Resource(name = "paintp.goods.paIntpGoodsService")
	private PaIntpGoodsService paIntpGoodsService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@Autowired
	private PaIntpComUtil paIntpComUtil;
	
	/**
	 * 인터파크 주문 연동 내역에 의한 TV쇼핑 주문 내역 생성
	 * @param paOrderNo		인터파크 주문번호
	 * @param orderRowCount	주문 데이터 ROW 수
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void orderInputAsync(String paOrderNo, int orderRowCount, HttpServletRequest request) throws Exception {
		HashMap<String, Object>[] resultMap = null;
		HashMap<String, String> orderMap = null;
		ParamMap paramMap = null;
		int executedRtn = 0;
		int targetSize = 0;
		List<Object> orderInputTargetDtList = null;
		String promoAllowTerm = ComUtil.NVL(systemService.getValRealTime("PAPROMO_ALLOW_TERM") , "0.1" );	// 프로모션 연동 종료 건 조회 허용 시간 
		
		try {
			
			paramMap = new ParamMap();
			paramMap.put("paOrderNo"		, paOrderNo);
			paramMap.put("papromoAllowTerm"	, promoAllowTerm);
					
			
			orderInputTargetDtList = paIntpOrderService.selectOrderInputTargetDtList(paramMap);
			targetSize = orderInputTargetDtList.size();
			if(targetSize < 1) {
				throw processException("msg.no.select", new String[] { "selectOrderInputTargetDtList" });
			} else if (targetSize != orderRowCount) {
				throw processException("pa.cannot_dup_data", new String[] { "selectOrderInputTargetDtList" });
			}
			
			OrderInputVO[] orderInputVO = new OrderInputVO[targetSize];
			for(int i = 0;  i < targetSize ; i++) {
				String zipCode = "";
				String type = "";
				
				orderMap = (HashMap<String, String>) orderInputTargetDtList.get(i);
				
				orderInputVO[i] = new OrderInputVO();
				orderInputVO[i].setPaCode      (orderMap.get("PA_CODE").toString());
				orderInputVO[i].setMappingSeq  (orderMap.get("MAPPING_SEQ").toString());
				orderInputVO[i].setMediaCode   (orderMap.get("MEDIA_CODE").toString());
				orderInputVO[i].setOrderDate   (orderMap.get("ORDER_DATE").toString());
				orderInputVO[i].setGoodsCode   (orderMap.get("GOODS_CODE").toString());
				orderInputVO[i].setGoodsdtCode (orderMap.get("GOODSDT_CODE").toString());
				orderInputVO[i].setOrderQty    (Integer.parseInt(String.valueOf(orderMap.get("ORDER_QTY"))));
				orderInputVO[i].setApplyDate   (orderMap.get("APPLY_DATE").toString());
				orderInputVO[i].setRsaleAmt    (Double.parseDouble(String.valueOf(orderMap.get("RSALE_AMT"))));
				orderInputVO[i].setSupplyPrice (Double.parseDouble(String.valueOf(orderMap.get("SUPPLY_PRICE"))));
				orderInputVO[i].setSellerDcAmt (Long.parseLong(String.valueOf(orderMap.get("SELLER_DC_AMT")))); //상품가격 * 수량 - 실결제가 = ARS할인 + 일시불할인 + 제휴프로모션할인
				orderInputVO[i].setCustName    (orderMap.get("CUST_NAME").toString());
				orderInputVO[i].setCustChar    ("99");
				orderInputVO[i].setCustTel1    (orderMap.get("CUST_TEL1").toString().replace("-", ""));
				orderInputVO[i].setCustTel2    (orderMap.get("CUST_TEL2").toString().replace("-", ""));
				orderInputVO[i].setReceiverName(orderMap.get("RECEIVER_NAME").toString());
				orderInputVO[i].setReceiverTel (orderMap.get("RECEIVER_TEL").toString().replace("-", ""));
				orderInputVO[i].setReceiverHp  (orderMap.get("RECEIVER_HP").toString().replace("-", ""));
				orderInputVO[i].setReceiverAddr(orderMap.get("RECEIVER_ADDR").toString());
				orderInputVO[i].setMsg         (orderMap.get("MSG").toString());
				orderInputVO[i].setPaGoodsCode (orderMap.get("PA_GOODS_CODE").toString());
				orderInputVO[i].setStdAddr     (orderMap.get("RECEIVER_ADDR1").toString());
				orderInputVO[i].setStdAddrDT   (orderMap.get("RECEIVER_ADDR2").toString());
				orderInputVO[i].setPostNo      (orderMap.get("RECEIVER_ZIPCODE").toString());
				orderInputVO[i].setPostNoSeq   ("001");
				orderInputVO[i].setPaOrderCode (orderMap.get("PA_ORDER_NO").toString());
				orderInputVO[i].setShpFeeCost  (Long.parseLong(String.valueOf(orderMap.get("PA_SHPFEE_COST"))));
				orderInputVO[i].setReceiveMethod("64");
				orderInputVO[i].setProcUser    (Constants.PA_INTP_PROC_ID);
				orderInputVO[i].setPriceSeq	   (orderMap.get("PRICE_SEQ").toString());
				orderInputVO[i].setDoFlag	   (orderMap.get("DO_FLAG").toString());

				orderInputVO[i].setLumpSumDcAmt(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_DC_AMT"))));
				orderInputVO[i].setLumpSumOwnDcAmt(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_OWN_DC_AMT"))));
				orderInputVO[i].setLumpSumEntpDcAmt(Long.parseLong(String.valueOf(orderMap.get("LUMP_SUM_ENTP_DC_AMT"))));
				orderInputVO[i].setCouponPromoBdate(DateUtil.toTimestamp(orderMap.get("COUPON_PROMO_BDATE")));
				orderInputVO[i].setCouponPromoEdate(DateUtil.toTimestamp(orderMap.get("COUPON_PROMO_EDATE")));
				
				zipCode = orderMap.get("RECEIVER_ZIPCODE").toString().trim();
				if(zipCode.length() == 6){
					type  = "01"; //1:지번주소, 2:도로명주소
				}else{
					type  = "02";
				}
				orderInputVO[i].setAddrGbn     (type);
				
				if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost()) || ("127.0.0.1").equals(request.getRemoteHost())) {
					orderInputVO[i].setIsLocalYn("Y");
				} else {
					orderInputVO[i].setIsLocalYn("N");
				}
				
				// 1) 가격 비교
				String paApplyDate = orderMap.get("APPLY_DATE").toString(); // TPAGOODSPRICE 기준 APPLY_DATE
				String stoaApplyDate = orderMap.get("STOA_APPLY_DATE").toString(); // TGOODSPRICE 기준 APPLY_DATE
				String paOrderDate = orderMap.get("ORDER_DATE").toString(); // 인터파크 주문일시
				
				if(!paApplyDate.equals(stoaApplyDate)) {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					Date orderDate = format.parse(paOrderDate.substring(0, 8));
			        Date stoaDate = format.parse(stoaApplyDate.substring(0, 8));
			        
			        // 스토아 최신 가격 적용일자와 제휴 주문일자 차이 계산
			        long diffDays = orderDate.getTime() - stoaDate.getTime();
			        diffDays = diffDays / (24 * 60 * 60 * 1000);
			        
					if(diffDays >= 3) { // 3일 이상 차이나는 경우
						ParamMap orderPriceParam = new ParamMap();
						orderPriceParam.put("mappingSeq", orderMap.get("MAPPING_SEQ").toString());
						orderPriceParam.put("resultCode", "999999");
						orderPriceParam.put("resultMessage", "가격 정보가 잘못되었습니다.");
						orderPriceParam.put("createYn"	   , "0");
						paorderService.updatePaOrdermTx(orderPriceParam);
						throw processException("pa.fail_order_input", new String[]{ "가격 정보가 잘못되었습니다." });
					}
				}
				
				// 2) 프로모션 가격 계산 
				double sellerDcAmt   = Double.parseDouble(String.valueOf(orderMap.get("SELLER_DC_AMT")))  / Integer.parseInt( String.valueOf( orderMap.get("ORDER_QTY")) );
				double arsDcAmt		 = Double.parseDouble(String.valueOf(orderMap.get("ARS_DC_AMT")));
				double lumpSumDcAmt	 = Double.parseDouble(String.valueOf(orderMap.get("LUMP_SUM_DC_AMT"))) /  Integer.parseInt( String.valueOf( orderMap.get("ORDER_QTY")) );						
				double outPromoPrice = Double.parseDouble(String.valueOf(orderMap.get("OUT_PROMO_PRICE")));		
				double sumDcAmt		 = 0;

				if(sellerDcAmt < 1) {
					orderInputVO[i].setSellerDcAmtExists("N");
					continue; // 인터파크 비회원 및 판매자 할인 금액이 없는 경우 가격 비교 및 프로모션 데이터 세팅에 대한 수행을 하지 않는다.
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
                orderPaPromo.setCouponPromoBdate(orderInputVO[i].getCouponPromoBdate());
                orderPaPromo.setCouponPromoEdate(orderInputVO[i].getCouponPromoEdate());
                orderPaPromo.setCouponYn("1"); 
                orderPaPromo.setProcGb("I");
                
                orderInputVO[i].setOrderPaPromo(orderPaPromo);
				//프로모션 관련 셋팅
				//orderInputVO[i].setOrderPromo(paIntpOrderService.selectOrderPromo(orderMap));
				//orderInputVO[i].setOrderPaPromo(paIntpOrderService.selectOrderPaPromo(orderMap));//제휴OUT프로모션
			}
			
			try {
				resultMap = paorderService.saveOrderTx(orderInputVO);
			} catch (Exception e) {
				for(int j = 0; targetSize > j; j++) {
					paramMap = new ParamMap();
					paramMap.put("mappingSeq", orderInputVO[j].getMappingSeq());
					paramMap.put("resultCode", Constants.SAVE_FAIL);
					paramMap.put("resultMessage", e.getMessage());
					paramMap.put("createYn", "0");
					
					executedRtn = paorderService.updatePaOrdermTx(paramMap);
					if(executedRtn != 1) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE - resultCode 999999" });
					}
				}
				throw processMessageException(e.getMessage());
			}
			
			//재고부족 or 판매불가 호출
			for(int k = 0; k < targetSize; k++) {
				paramMap = new ParamMap();
				paramMap.setParamMap(resultMap[k]);
				paramMap.replaceCamel();
				
				if(paramMap.getString("resultCode").equals("100001")) {
					HashMap<String, String> apiInfo = new HashMap<String, String>();
					String prg_id = "IF_PAINTPAPI_03_004";
					paramMap.put("apiCode", prg_id);
					paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
					paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
					paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					
					apiInfo = systemService.selectPaApiInfo(paramMap);
					apiInfo.put("apiInfo", paramMap.getString("apiCode"));
					
					Map<String, String> params = new HashMap<>();
					Map<String, String> refusalMap = paIntpOrderService.selectRefusalInfo(paramMap.getString("mappingSeq"));
					if(refusalMap.isEmpty() || refusalMap == null) {
						log.info("orderInputAsync not found orderInfo. mappingSeq:"+paramMap.getString("mappingSeq"));
						continue;
					}
					String ordNo = refusalMap.get("PA_ORDER_NO").toString();
					Integer ordSeq = Integer.valueOf(refusalMap.get("PA_ORDER_SEQ"));
					String optPrdTp = "01"; 
					String updateCode = "";
					
					log.info("외부 API 호출 : 판매불가 또는 품절로 인한 주문취소요청 처리");
					params.put("sc.entrId", ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
					params.put("sc.ordclmNo", ordNo);
					params.put("sc.ordSeq", ComUtil.objToStr(ordSeq));
					params.put("sc.optPrdTp", optPrdTp);
					params.put("sc.optOrdSeqList", ComUtil.objToStr(ordSeq));
					String paCode = refusalMap.get("PA_CODE");
					
					PaIntpOrderListVO orderList = paIntpComUtil.apiGetObjectByProgramId(apiInfo, params, PaIntpOrderListVO.class, paCode);
					if(orderList != null && orderList.getResult() != null) {
						updateCode = paIntpOrderService.saveOrderRejectProcTx(ordNo, ordSeq, true, "000", "판매자주문취소성공", "재고부족 or 판매불가", null, paCode);
					} else {
						updateCode = paIntpOrderService.saveOrderRejectProcTx(ordNo, ordSeq, true, "999", "판매자주문취소실패", "재고부족 or 판매불가", null, paCode);
					}
					
					if(!Constants.SAVE_SUCCESS.equals(updateCode)) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
					}
				}
			}
		} catch (Exception e) {
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "PA_ORDER_NO: " + paOrderNo + " | " + e.getMessage());
		} finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode"  , "PAINTP_ORDER_INPUT");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code"     , "500");
					paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		
			
		}
	}
	
	/**
	 * 인터파크 주문취소 데이터 생성
	 * @param cancelTargetList
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void cancelInputAsync(PaIntpTargetVO cancelTargetList, HttpServletRequest request) throws Exception {
		HashMap<String, Object> cancelDtInfo = null;
		ParamMap paramMap = null;
		int targetSize = 0;
		int executedRtn = 0;
		
		try {
			paramMap = new ParamMap();
			paramMap.put("paCode", cancelTargetList.getPaCode());
			paramMap.put("paOrderNo", cancelTargetList.getPaOrderNo());
			paramMap.put("paOrderSeq", cancelTargetList.getPaOrderSeq());
			paramMap.put("paShipNo", cancelTargetList.getPaShipNo());
			paramMap.put("paClaimNo", cancelTargetList.getPaClaimNo());
			
			List<Object> cancelList = paIntpOrderService.selectCancelInputTargetDtList(paramMap);
			targetSize = cancelList.size();
			if(targetSize != 1){
				throw processException("msg.no.select", new String[] { "selectCancelInputTargetDtList" });
			}
			cancelDtInfo = (HashMap<String, Object>) cancelList.get(0);
			
			if (cancelDtInfo.get("PRE_CANCEL_YN").toString().equals("0")) {
				//= 일반취소건 처리.
				CancelInputVO cancelInputVO = new CancelInputVO();
				cancelInputVO.setMappingSeq(String.valueOf(cancelDtInfo.get("MAPPING_SEQ")));
				cancelInputVO.setOrderNo(String.valueOf(cancelDtInfo.get("ORDER_NO")));
				cancelInputVO.setOrderGSeq(String.valueOf(cancelDtInfo.get("ORDER_G_SEQ")));
				cancelInputVO.setCancelQty(Integer.parseInt(String.valueOf(cancelDtInfo.get("PA_PROC_QTY"))));
				cancelInputVO.setCancelCode(String.valueOf(cancelDtInfo.get("CANCEL_CODE")));
				cancelInputVO.setProcId(Constants.PA_INTP_PROC_ID);
				try {
					paorderService.saveCancelTx(cancelInputVO);
				} catch (Exception e) {
					paramMap = new ParamMap();
					paramMap.put("mappingSeq", cancelInputVO.getMappingSeq());
					paramMap.put("resultCode", Constants.SAVE_FAIL);
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
				//=pa.before_order_create_cancel = 주문생성 이전 취소건
				ParamMap preCancelMap = new ParamMap();
				preCancelMap.put("mappingSeq", String.valueOf(cancelDtInfo.get("MAPPING_SEQ")));
				preCancelMap.put("preCancelYn", "1");
				preCancelMap.put("preCancelReason", getMessage("pa.before_order_create_cancel"));
				
				executedRtn = paIntpOrderService.updatePreCancelYnTx(preCancelMap);
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPAORDERM(pre_cancel_yn) UPDATE" });
				}
			}
		}
		catch ( Exception e ) {
			if(paramMap == null) paramMap = new ParamMap();
			paramMap.put("message", "pa_order_no : " + cancelTargetList.getPaOrderNo() + " > " + e.toString());
		}
		finally {
			try{
				if(paramMap != null && !paramMap.getString("message").equals("")){
					paramMap.put("apiCode", "PAINTP_CANCEL_INPUT");
					paramMap.put("startDate", systemService.getSysdatetimeToString());
					paramMap.put("code", "500");
					paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}catch(Exception ee){
				log.error("ApiTracking Insert Error : "+ee.toString());
				throw processException("msg.cannot_save", new String[] { "ApiTracking insert" });
			}
		}
	}

	
	/**
	 * 인터파크 반품 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	///@Async
	public void orderClaimAsync(HashMap<String, Object> claimMap, HttpServletRequest request) throws Exception{
		List<Object> orderClaimTargetDtList = null;
		ParamMap paramMap 					= new ParamMap();
		OrderClaimVO orderClaimVO 			= null;
		orderClaimVO 						= new OrderClaimVO();
		String paOrderNo					= claimMap.get("PA_ORDER_NO").toString();
		paramMap.setParamMap(claimMap);
		paramMap.replaceCamel();
		
		try {
			//1) 반품 타겟  추출
			if(paramMap.getString("paOrderGb").equals("30")){
				orderClaimTargetDtList = paIntpClaimService.selectOrderCalimTargetDt30List(paramMap); 
			}else {
				orderClaimTargetDtList = paIntpClaimService.selectOrderCalimTargetDt20List(paramMap); //출고전 반품
			}
			if(orderClaimTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectOrderCalimTargetDt30(20)List" });
			
			//2) 반품 VO 데이터 생성
			orderClaimVO = setOrderClaimVO((HashMap<String, Object>)orderClaimTargetDtList.get(0));
			paclaimService.saveOrderClaimTx(orderClaimVO);
			
			//3) 결과값 세팅
			paramMap.put("code"		, 	"200");
			paramMap.put("message"	,	"SAVED");

			
		}catch(Exception e) {
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + e.getMessage();
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			
			//2) TPAORDERM Update
			OrderClaimVO[] orderClaimList = new OrderClaimVO[1];
			orderClaimList[0] 			  = orderClaimVO;
			updatePaOrdermTxForRollback(orderClaimList, e);
			
		}finally {
			try{
				if(!"200".equals(paramMap.getString("code"))) {
					paramMap.put("apiCode"  	, "PAINTP_ORDER_CLAIM");
					paramMap.put("startDate"	, systemService.getSysdatetimeToString());
					systemService.insertPaApiTrackingTx(paramMap);
				}
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
	}

	/**
	 * 인터파크 반품취소 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	///@Async
	public void claimCancelAsync(HashMap<String, Object> claimMap, HttpServletRequest request) throws Exception{
		HashMap<String, Object> hmSheet 	 = null;
		List<Object> cancelInputTargetDtList = null;
		ParamMap paramMap 					 = new ParamMap();
		OrderClaimVO orderClaimVO 			 = null;
		String paOrderNo					 = claimMap.get("PA_ORDER_NO").toString();
		paramMap.setParamMap(claimMap);
		paramMap.replaceCamel();		
		
		try {
			cancelInputTargetDtList = paIntpClaimService.selectClaimCancelTargetDtList(paramMap); 
			if(cancelInputTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectClaimCancelTargetDtList" });
			
			hmSheet = (HashMap<String, Object>) cancelInputTargetDtList.get(0);
			switch(hmSheet.get("PRE_CANCEL_YN").toString()){
			
				case "0": //일반적인 반품 취소
					orderClaimVO = setOrderClaimVO(hmSheet);
					paclaimService.saveClaimCancelTx(orderClaimVO);	
					break;
				
				case "1": //기취소
					paIntpOrderService.updatePreCancelYnTx(hmSheet.get("MAPPING_SEQ").toString(), hmSheet.get("PRE_CANCEL_YN").toString(), getMessage("pa.before_claim_create_cancel"));
					break;	
			}
			paramMap.put("code"		, "200");;
			paramMap.put("message"	, "SAVED");
			
			
		}catch(Exception e){
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + e.getMessage();
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			
			//2) TPAORDERM Update
			OrderClaimVO[] orderClaimList = new OrderClaimVO[1];
			orderClaimList[0] = orderClaimVO;
			updatePaOrdermTxForRollback(orderClaimList, e);
		}finally{
			try{
				if(!"200".equals(paramMap.getString("code"))) {
					paramMap.put("apiCode"  	, "PAINTP_CLAIM_CANCEL");
					paramMap.put("startDate"	, systemService.getSysdatetimeToString());
					systemService.insertPaApiTrackingTx(paramMap);
				}
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}	
	}
	
	/**
	 * 인터파크 교환 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void orderChangeAsync(HashMap<String, Object> exchangeMap, HttpServletRequest request) throws Exception {
		List<Object> orderChangeTargetDtList		= null;
		OrderClaimVO orderClaimVO 					= null;
		OrderClaimVO[] orderClaimVOArray 			= null;
		int index 									= 0;
		String paOrderNo					 		= exchangeMap.get("PA_ORDER_NO").toString();
		ParamMap paramMap 							= new ParamMap();
		paramMap.setParamMap(exchangeMap);
		paramMap.replaceCamel();

		try {
			orderChangeTargetDtList = paIntpClaimService.selectOrderChangeTargetDtList(paramMap); 
			if(orderChangeTargetDtList.size() < 1) throw processException("msg.no.select", new String[] { "selectOrderChangeTargetDtList" });
			
			orderClaimVOArray = new OrderClaimVO[orderChangeTargetDtList.size()];

			for(Object hm : orderChangeTargetDtList) {
				orderClaimVO = new OrderClaimVO();
				orderClaimVO = setOrderClaimVO((HashMap<String, Object>) hm);
				orderClaimVOArray[index] = orderClaimVO;
				index ++;
			}
			
			paclaimService.saveOrderChangeTx(orderClaimVOArray);
						
			paramMap.put("code"		, 	"200");;
			paramMap.put("message"	,	"SAVED");
					
		}catch(Exception e){			
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + e.getMessage();
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			//2) UPDATE TPAORDERM
			updatePaOrdermTxForRollback(orderClaimVOArray, e);	
		}finally{
			try{
				if(!"200".equals(paramMap.getString("code"))) {
					paramMap.put("apiCode"  	, "PAINTP_ORDER_CHANGE");
					paramMap.put("startDate"	, systemService.getSysdatetimeToString());
					systemService.insertPaApiTrackingTx(paramMap);
				}
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
	}
	
	/**
	 * 인터파크 교환취소 데이터 생성 
	 * @return HashMap<String, Object>, HttpServletRequest
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void changeCancelAsync(HashMap<String, Object> cancelMap, HttpServletRequest request) throws Exception {
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
			changeCancelTargetDtList = paIntpClaimService.selectChangeCancelTargetDtList(paramMap);
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
						paIntpOrderService.updatePreCancelYnTx(hmSheet.get("MAPPING_SEQ").toString(), hmSheet.get("PRE_CANCEL_YN").toString(), getMessage("pa.before_change_create_cancel"));
						preCanYn = true;
						break;
				}
				index++;
			}
			
			if(!preCanYn){
				paclaimService.saveChangeCancelTx(orderClaimVOArray);
			}
			
			paramMap.put("code"		, 	"200");;
			paramMap.put("message"	,	"SAVED");
					
		}catch(Exception e){			
			//1) Error Log
			String message = "BO 데이터 생성 실패 : PA_ORDER_NO: " + paOrderNo + " | " + e.getMessage();
			log.error(message);
			paramMap.put("code"		, "500");
			paramMap.put("message" 	, message);
			//2) UPDATE TPAORDERM
			updatePaOrdermTxForRollback(orderClaimVOArray, e);	
		}finally{
			try{
				if(!"200".equals(paramMap.getString("code"))) {
					paramMap.put("apiCode"  	, "PAINTP_CHANGE_CANCEL");
					paramMap.put("startDate"	, systemService.getSysdatetimeToString());
					systemService.insertPaApiTrackingTx(paramMap);
				}
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
	}
	
	private void updatePaOrdermTxForRollback(AbstractVO[] aVO, Exception e){
		
		if(aVO == null || aVO.length < 1 || aVO[0] == null)  return;
		
		ParamMap paramMap = null;
		int excuteCnt 	  = 0;
		
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
					
			paramMap.put("resultCode"		, "999999");
			//paramMap.put("resultMessage"	, e.toString().length() > 1950 ? e.toString().substring(0,1950) : e.toString());
			paramMap.put("resultMessage"	, e.getMessage().length() > 1950 ? e.getMessage().substring(0,1950) : e.getMessage());
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

			//배송지, 회수지 체크 - 인터파크는 따로 배송지와 회수지를 나누지 않음, 나눌경우 아래 Switch 문에서 해결할것!
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
			paramMap.put("PA_ORDER_SEQ"	, hmSheet.get("PA_ORDER_SEQ").toString());
			paramMap.put("PA_CLAIM_NO"	, hmSheet.get("PA_CLAIM_NO").toString());
			paramMap.put("PA_ORDER_GB"	, hmSheet.get("PA_ORDER_GB").toString());
			String checkAddr = paIntpClaimService.compareAddress(paramMap);
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
	
	@Async
	public void goodsStock(PaIntpGoodsdtMappingVO pagoodsdtMapping, HashMap<String, String> apiInfo) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		String request_type = "GET";
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = systemService.getSysdatetimeToString();
		Document doc = null;
		List<PaIntpGoodsdtMappingVO> paGoodsdtList = new ArrayList<PaIntpGoodsdtMappingVO>();
		PaGoodsTransLog paGoodsTransLog = null;
		
		if(pagoodsdtMapping.getPaCode().equals(Constants.PA_INTP_BROAD_CODE)){
			apiInfo.put("paName", Constants.PA_INTP_BROAD);
		} else {
			apiInfo.put("paName", Constants.PA_INTP_ONLINE);
		}
		apiInfo.put("paCode", pagoodsdtMapping.getPaCode());
		
		Map<String, String> apiParamMap = new HashMap<String, String>();
		apiParamMap.put("prdNo", pagoodsdtMapping.getPrdNo());
		apiInfo.put("body", "");
		
		ParamMap resParam = paIntpComUtil.paIntpConnectionSetting(apiInfo,request_type,apiParamMap);
		if("200".equals(resParam.getString("code"))) {
			Map<String, String> map = new HashMap<String, String>();
			doc = (Document) resParam.get("data");
			String errorCheck = "";
			NodeList childeList = doc.getFirstChild().getChildNodes();
			for(int j=0; j<childeList.getLength();j++){
				if("error".equals(childeList.item(j).getNodeName())) {
					errorCheck = "error";
				}
				//PA_OPTION_CODE UPDATE를 위한 Map 생성
				Map<String, String> dtMap = new HashMap<String, String>();
				for(Node node = childeList.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){
					for(int k=0; k<node.getChildNodes().getLength(); k++){
            			Node directionList = node.getChildNodes().item(k);
            			map.put(node.getNodeName().trim(), directionList.getTextContent().trim());
            			dtMap.put(node.getNodeName().trim(), directionList.getTextContent().trim());
        			}
				}
				
				if(dtMap.get("externalPrdNo") != null) {
					if(dtMap.get("externalPrdNo").length() < 5) {
						PaIntpGoodsdtMappingVO paGoodsDt = new PaIntpGoodsdtMappingVO();
						paGoodsDt.setPaCode(pagoodsdtMapping.getPaCode());
						paGoodsDt.setGoodsCode(pagoodsdtMapping.getGoodsCode());
						paGoodsDt.setGoodsdtCode(dtMap.get("externalPrdNo"));
						paGoodsDt.setPaOptionCode(dtMap.get("prdNo"));
						paGoodsdtList.add(paGoodsDt);								
					}
				}
			}
			
			if("error".equals(errorCheck)) {
				String code = map.get("code").replace("|", "");
				
				code = code.length() == 6 ? code.substring(2) : (code.length() == 5?code.substring(1):code);
				paramMap.put("code", code);
				paramMap.put("message",map.get("explanation"));
			} else {
				log.info("04.인터파크  상품 재고 조회 완료 저장");
				
				rtnMsg = paIntpGoodsService.savePaIntpGoodsStockTx(paGoodsdtList);
				
				if(!rtnMsg.equals("000000")){
					paramMap.put("code","404");
					paramMap.put("message",getMessage("pa.not_exists_process_list"));
				} else {
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}
			}
		}

		if("200".equals(paramMap.getString("code"))) {
			//전송관리 테이블 저장
			paGoodsTransLog = new PaGoodsTransLog();
			paGoodsTransLog.setGoodsCode(pagoodsdtMapping.getGoodsCode());
			paGoodsTransLog.setPaCode(pagoodsdtMapping.getPaCode());
			paGoodsTransLog.setItemNo(pagoodsdtMapping.getPrdNo());
			paGoodsTransLog.setRtnCode(paramMap.getString("code"));
			paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
			paGoodsTransLog.setSuccessYn("1");
			paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paGoodsTransLog.setProcId("PAINTP");
			paIntpGoodsService.insertPaIntpGoodsTransLogTx(paGoodsTransLog);
		}
				
		return;
	}
	
	@Async
	public void asyncGoodsModify(HttpServletRequest request, HashMap<String, String> apiInfo, Map<String, String> apiParamMap, ParamMap asyncMap,
			PaIntpGoodsVO paIntpGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception{
		
		PaGoodsTransLog paGoodsTransLog = null;
		String method = "GET";
		String dateTime = systemService.getSysdatetimeToString();
		String procId = "PAINTP";
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		try {
			
			ParamMap resParam = paIntpComUtil.paIntpConnectionSetting(apiInfo,method,apiParamMap);

			if("200".equals(resParam.getString("code"))) {
				ParamMap map = new ParamMap();
				map = ComUtil.paIntpXmlToMap(resParam);
				if("ERR".equals(map.getString("ERROR"))) {
					String code = map.getString("code").replace("|", "");
					code = code.length() == 6 ? code.substring(2) : (code.length() == 5?code.substring(1):code);
					asyncMap.put("code", code);
					asyncMap.put("message",paIntpGoods.getGoodsCode() + map.get("explanation"));
				} else {
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paIntpGoods.getGoodsCode());
					paGoodsTransLog.setPaCode(paIntpGoods.getPaCode());
					paGoodsTransLog.setItemNo(map.getString("prdNo"));					
					paGoodsTransLog.setRtnCode("200");
					paGoodsTransLog.setRtnMsg("OK");					
					paGoodsTransLog.setSuccessYn("1");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					paIntpGoodsService.insertPaIntpGoodsTransLogTx(paGoodsTransLog);

					paIntpGoods.setPrdNo(map.getString("prdNo"));
					paIntpGoods.setModifyId(procId);
					paIntpGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

					log.info("08.인터파크 상품수정 제휴사 상품정보 저장");
					rtnMsg = paIntpGoodsService.savePaIntpGoodsTx(paIntpGoods, goodsdtMapping, paPromoTargetList);

					if(!rtnMsg.equals("000000")){
						asyncMap.put("code","500");
						asyncMap.put("message",paIntpGoods.getGoodsCode() + rtnMsg);
					} else {
						asyncMap.put("code","200");
						asyncMap.put("message","OK");
					}
				}
			}else{
				asyncMap.put("code","500");
				asyncMap.put("message",paIntpGoods.getGoodsCode() + resParam.getString("message"));
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
	
	@Async
	public void spPagoodsSyncIntp(HttpServletRequest request, String goodsCode, String userId) throws Exception{
		
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
		
		String resultMsg = "";
		String dateTime = systemService.getSysdatetimeToString();
		StringBuffer sb = null;
		int conditionDay = 2;		
		int eTVLimitMargin = 0;
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paGroupCode", "07");
		paramMap.put("dateTime", dateTime);
		paramMap.put("conditionDay", conditionDay);		
		paramMap.put("comparePaCode", "71");
		paramMap.put("feeCode", "O696");
		paramMap.put("minMarginCode", "80");
		paramMap.put("minPriceCode", "81");
		
		paramMap.put("siteGb", userId);
		paramMap.put("apiCode", "IF_PACOMMON_00_002");
		
		log.info("Step1. 인터파크 상품이미지 동기화 START");
		curImageInfo = paCommonService.selectCurImageInfoList(paramMap);
		if(curImageInfo.size() > 0) {
			sb = new StringBuffer();
			for(PaGoodsImage curImageInfoTarget : curImageInfo) {
				//이미지 없는 케이스가 많아 for문안에 try catch 처리
				try {
					curImageInfoTarget.setModifyId("PAINTP");
					curImageInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					curImageInfoTarget.setRemark(dateTime);
					resultMsg = paCommonService.saveCurImageInfoTx(curImageInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step1 상품이미지 동기화 인터파크 Fail > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
						sb.append(curImageInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step1 상품이미지 동기화 인터파크 Sucess > GOODS_CODE : " + curImageInfoTarget.getGoodsCode());
				} catch(Exception e) {
					log.info("Step1. 인터파크 상품이미지 동기화 : " + e.getMessage());
					paramMap.put("code", "500");
					paramMap.put("message", "Step1. 인터파크 상품이미지 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
					systemService.insertApiTrackingTx(request, paramMap);
				}
			}
		}
		log.info("Step1. 인터파크 상품이미지 동기화 END");
		
		log.info("Step2. 인터파크 상품가격 동기화 START");
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
						
						stopSaleParam.put("paGroupCode", "07");
						stopSaleParam.put("paCode", curPriceInfoTarget.getPaCode());
						stopSaleParam.put("goodsCode", curPriceInfoTarget.getGoodsCode());
						stopSaleParam.put("dateTime", dateTime);
						stopSaleParam.put("userId", "PAINTP");
						stopSaleParam.put("paGoodsCode", curPriceInfoTarget.getPaGoodsCode());
						stopSaleParam.put("note", "마진10이하 혹은 판매가100원 미만/모바일eTV 마진");
						stopSaleParam.put("priceStopSale", "Y");
						
						//연동제외처리
						resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
					} else {
						curPriceInfoTarget.setModifyId("PAINTP");
						curPriceInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						curPriceInfoTarget.setDateTime(dateTime);
						resultMsg = paCommonService.saveCurPriceInfoTx(curPriceInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step2 상품가격 동기화 인터파크 Fail > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
						sb.append(curPriceInfoTarget.getGoodsCode() + ", ");
						continue;
					}
					log.info("Step2 상품가격 동기화 인터파크 Sucess > GOODS_CODE : " + curPriceInfoTarget.getGoodsCode());
				}
			} catch(Exception e) {
				log.info("Step2. 인터파크 상품가격 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step2. 인터파크 상품가격 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step2. 인터파크 상품가격 동기화 END");		
		
		log.info("Step3. 인터파크 고객부담배송비 동기화 START");
		curShipCostInfo = paCommonService.selectCurShipCostInfoList(paramMap);
		if(curShipCostInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaCustShipCostVO curShipCostInfoTarget : curShipCostInfo) {
					
					if(((curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("CN") || curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("PL"))
														&& curShipCostInfoTarget.getShipCostBaseAmt() < 100) 
					  || curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("QN")
					  || (!curShipCostInfoTarget.getShipCostCode().substring(0, 2).equals("FR") && curShipCostInfoTarget.getOrdCost() < 100)) {  
						//배송정책이 CN, PL이면서 기준금액이 100원 이하인경우, 배송비정책이 QN인 경우, 무료배송이 아니면서 배송비가 100원 이하인 경우 연동 제외처리
						
						stopShipParam.put("paCode", curShipCostInfoTarget.getPaCode());
						stopShipParam.put("entpCode", curShipCostInfoTarget.getEntpCode());
						stopShipParam.put("shipCostCode", curShipCostInfoTarget.getShipCostCode());
						
						curShipStopSale = paCommonService.selectCurShipStopSaleList(stopShipParam);
						for(HashMap<String, String> curShipStopSaleTarget : curShipStopSale) {
							
							stopSaleParam.put("paGroupCode", "07");
							stopSaleParam.put("paCode", curShipStopSaleTarget.get("PA_CODE").toString());
							stopSaleParam.put("goodsCode", curShipStopSaleTarget.get("GOODS_CODE").toString());
							stopSaleParam.put("dateTime", dateTime);
							stopSaleParam.put("userId", "PAINTP");
							stopSaleParam.put("paGoodsCode", curShipStopSaleTarget.get("PA_GOODS_CODE").toString());
							stopSaleParam.put("note", "배송비유료변경");
							stopSaleParam.put("priceStopSale", "N");
							
							//연동제외처리
							resultMsg = paCommonService.saveStopSaleTx(stopSaleParam);
						}
						
					} else {
						curShipCostInfoTarget.setModifyId("PAINTP");
						curShipCostInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						resultMsg = paCommonService.saveCurShipCostInfoTx(curShipCostInfoTarget);
					}
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step3 고객부담배송비 인터파크 Fail");
						sb.append(curShipCostInfoTarget.getEntpCode() + "|" + curShipCostInfoTarget.getShipCostCode() + ", ");
						continue;
					}
					log.info("Step3 고객부담배송비 인터파크 Sucess");
				}
			} catch(Exception e) {
				log.info("Step3. 인터파크 고객부담배송비 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step3. 인터파크 고객부담배송비 동기화 : " + e.getMessage() + "INFO : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step3. 인터파크 고객부담배송비 동기화 END");
		
		log.info("Step4. 인터파크 회수지 동기화 START");
		curEntpSlipInfo = paCommonService.selectCurEntpSlipInfoList(paramMap);
		if(curEntpSlipInfo.size() > 0) {
			sb = new StringBuffer();
			try {
				for(PaEntpSlip curEntpSlipInfoTarget : curEntpSlipInfo) {
					
					curEntpSlipInfoTarget.setModifyId("PAINTP");
					curEntpSlipInfoTarget.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					resultMsg = paCommonService.saveCurEntpSlipInfoTx(curEntpSlipInfoTarget);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step4 회수지 동기화 인터파크 Fail");
						sb.append(curEntpSlipInfoTarget.getEntpCode() + ", ");
						continue;
					}
					log.info("Step4 회수지 동기화 인터파크 Sucess");
				}
			} catch(Exception e) {
				log.info("Step4. 인터파크 회수지 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step4. 인터파크 회수지 동기화 : " + e.getMessage() + "ENTP_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step4. 인터파크 회수지 동기화 END");
		
		log.info("Step5. 인터파크 상품판매단계 동기화 START");
		curSaleStop = paCommonService.selectCurSaleStopList(paramMap);
		
		if(curSaleStop.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curSaleStopTarget : curSaleStop) {
					
					stopSaleParam.put("paGroupCode", "07");
					stopSaleParam.put("paCode", curSaleStopTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curSaleStopTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PAINTP");
					stopSaleParam.put("paGoodsCode", curSaleStopTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "SK스토아상품판매중단");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step5 상품판매단계 동기화 인터파크 Fail > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
						sb.append(curSaleStopTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step5 상품판매단계 동기화 인터파크 Sucess > GOODS_CODE : " + curSaleStopTarget.get("GOODS_CODE").toString());
				}
			} catch(Exception e) {
				log.info("Step5. 인터파크 상품판매단계 동기화 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step5. 인터파크 상품판매단계 동기화 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step5. 인터파크 상품판매단계 동기화 END");
		
		log.info("Step6. 인터파크 행사 종료 상품 마진 체크 START");
		curEventMargin = paCommonService.selectCurEventMarginList(paramMap);
		
		if(curEventMargin.size() > 0) {
			sb = new StringBuffer();
			try {
				for(HashMap<String, String> curEventMarginTarget : curEventMargin) {
					paramMap.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					//minMarginPrice = paCommonService.selectMinMarginPrice(paramMap);
					//selectCurEventMarginList 안에서 체크
					//if(ComUtil.objToDouble(curEventMarginTarget.get("MARGIN_RATE")) < ComUtil.objToDouble(minMarginPrice.get("MIN_MARGIN"))) {
					stopSaleParam.put("paGroupCode", "07");
					stopSaleParam.put("paCode", curEventMarginTarget.get("PA_CODE").toString());
					stopSaleParam.put("goodsCode", curEventMarginTarget.get("GOODS_CODE").toString());
					stopSaleParam.put("dateTime", dateTime);
					stopSaleParam.put("userId", "PAINTP");
					stopSaleParam.put("paGoodsCode", curEventMarginTarget.get("PA_GOODS_CODE").toString());
					stopSaleParam.put("note", "행사종료");
					stopSaleParam.put("priceStopSale", "N");
					
					resultMsg = paCommonService.saveCurSaleStopInfoTx(stopSaleParam);
					
					if(!resultMsg.equals(Constants.SAVE_SUCCESS)) {
						log.info("Step6 행사 종료 상품 마진 체크 인터파크 Fail > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
						sb.append(curEventMarginTarget.get("GOODS_CODE").toString() + ", ");
						continue;
					}
					log.info("Step6 행사 종료 상품 마진 체크 인터파크 Sucess > GOODS_CODE : " + curEventMarginTarget.get("GOODS_CODE").toString());
					//}					
				}
			} catch(Exception e) {
				log.info("Step6. 인터파크 행사 종료 상품 마진 체크 : " + e.getMessage());
				paramMap.put("code", "500");
				paramMap.put("message", "Step6. 인터파크 행사 종료 상품 마진 체크 : " + e.getMessage() + "GOODS_CODE : " + sb.toString());
				systemService.insertApiTrackingTx(request, paramMap);
			}
		}
		log.info("Step6. 인터파크 행사 종료 상품 마진 체크 END");
		
		paCommonService.checkMassModifyGoods("07");
		
		return;		
	}
}
