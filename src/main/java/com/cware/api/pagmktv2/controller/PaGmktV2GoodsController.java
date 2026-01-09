package com.cware.api.pagmktv2.controller;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaGmktGoodsVO;
import com.cware.netshopping.domain.model.PaGmktDelGoodsHis;
import com.cware.netshopping.domain.model.PaGmktGoods;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pagmkt.goods.service.PaGmktGoodsService;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;
import com.cware.netshopping.pagmkt.util.rest.PaGmktGoodsRest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pagmktv2/goods/legacy", description="상품레거시")
@Controller("com.cware.api.pagmktv2.PaGmktV2GoodsController")
@RequestMapping(value="/pagmktv2/goods/legacy")
public class PaGmktV2GoodsController extends AbstractController {


	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pagmkt.goods.paGmktGoodsService")
	private PaGmktGoodsService paGmktGoodsService;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;
	
	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2CommonController")
	private PaGmktV2CommonController paGmktV2CommonController;
	
	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2AsycController")
	private PaGmktV2AsycController paGmktAsycController;
	
	/**
	 * 상품 등록
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "상품 등록", notes = "상품 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-goods", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postGoods(HttpServletRequest request,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 		@RequestParam(value="paCode"			, required=true) String paCode,
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") 		@RequestParam(value="goodsCode"			, required=true) String goodsCode,
		@ApiParam(name = "searchTermGb", value = "중복실행체크여부", defaultValue = "") @RequestParam(value="searchTermGb"		, required=false, defaultValue = "") String searchTermGb) 
		throws Exception{
		
		ParamMap paramMapMsg = new ParamMap();
		ParamMap mapFor500 = new ParamMap();// 500에러 판별 위한 map
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_001";            
		String duplicateCheck = "";
		boolean putShippingPlaces500Flag = false;
		List<HashMap<String,String>> paGoodsTargetList = null;
		String exceptStr="";
		HashMap<String,Object> target = new HashMap<String,Object>();
				
		target.put("paCode"			, paCode);
		target.put("goodsCode"		, goodsCode);		
		
		try{
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});				
			}
			
			paGoodsTargetList = paGmktGoodsService.selectGmktGoodsInsertList(target);
			
			for(HashMap<String,String> paGoodsTarget:paGoodsTargetList){
				
				ParamMap paramMap = new ParamMap();
				paramMap.put("apiCode", apiCode);
				paramMap.put("startDate", systemService.getSysdatetimeToString());
				paramMap.put("siteGb", "afterset");
				paCode = paGoodsTarget.get("PA_CODE");
				goodsCode = paGoodsTarget.get("GOODS_CODE");
				paramMap.put("paCode", paCode);
				paramMap.put("goodsCode", goodsCode);
				
				try {
					log.info("상품코드 "+goodsCode);
					
					exceptStr =paExceptGoodsYn(paGoodsTarget.get("GOODS_CODE"));
		        	if(!exceptStr.equals("000000")){
		        		paGoodsTarget.put("message", exceptStr);
						savePaGoodsInsertReject(paramMap);
						continue;
					}
		        	
		        	//기존 상품등록 프로세스
		        	try{		        		              
	                    // 판매자주소 - 출하지 - 묶음배송 등록 API 별도의 배치로 분리 처리 20220509 LEEJY   
		        		//	paGmktV2CommonController.beforeGoodsInsert(request, mapFor500, goodsCode , searchTermGb); //구버전

		        		// 이슈로 인하여 출고회수지 재사용 처리 
//		        		paGmktV2CommonController.enrollEntpShipCost(request, "", "1"); //신버전
		        		
		        	}catch(Exception e){
		        		paramMap.put("code", "500");
		        		paramMap.put("message", e.getMessage());
		        		//paramMap.put("message", "판매자주소 - 출하지 - 묶음배송 등록 에러");
		        		
		        		if(mapFor500 != null){
		        			if("Y".equals(mapFor500.get("putShippingPlaces500Flag"))){
		        				putShippingPlaces500Flag = true;
		        			}
		        		}
		        		
		        		if(!putShippingPlaces500Flag){
		        			savePaGoodsInsertReject(paramMap);
		        		}
		        		continue;
		        		//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		        	}
		        	
		        	//= Step 1) select 테이블 후 urlParameter 셋팅
		        	paramMap = goodsInfoValidation(paramMap);
		        	if(!paramMap.getString("code").equals("200")){
		        		savePaGoodsInsertReject(paramMap);
		        		continue;
		        		//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		        	}
		        	
		        	//= Step 2) 통신
		        	String response = restUtil.getConnection(rest,  paramMap);
		        	Map<String,Object> resMap = ComUtil.splitJson(response);
		        	//= Step 3) 통신후 resMap 파싱 & setter
		        	Map<String,Object> siteDetail = (Map<String, Object>) resMap.get("siteDetail");
		        	Map<String,Object> gmktSiteDetail = (Map<String, Object>)siteDetail.get("gmkt");
		        	Map<String,Object> iacSiteDetail = (Map<String, Object>)siteDetail.get("iac");
		        	
		        	PaGmktGoodsVO paGmktGoods = new PaGmktGoodsVO();
		        	paGmktGoods.setEsmGoodsCode(resMap.get("goodsNo").toString());
		        	paGmktGoods.setGoodsCode(goodsCode);
		        	paGmktGoods.setPaCode(paCode);
		        	paGmktGoods.setPaSaleGb("20");
		        	paGmktGoods.setModifyId(paramMap.getString("siteGb"));
		        	
		        	switch(paramMap.getString("siteGb")){
		        	case "PAE"://2개 동시 등록
		        		if (gmktSiteDetail.get("SiteGoodsNo") == null) {
		        			paGmktGoods.setReturnNote("G마켓 상품코드의 값이 생성되지 않았습니다.");
		        		} else {
		        			paGmktGoods.setPaGroupCode("02");
		        			paGmktGoods.setItemNo(gmktSiteDetail.get("SiteGoodsNo").toString());
		        		}
		        		if (iacSiteDetail.get("SiteGoodsNo") == null) {
		        			paGmktGoods.setReturnNote("옥션 상품코드의 값이 생성되지 않았습니다.");
		        		} else {
		        			paGmktGoods.setPaGroupCode("03");
		        			paGmktGoods.setItemNoExtra(iacSiteDetail.get("SiteGoodsNo").toString());
		        		}
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
		        	
		        	//sk Stoa 재고는 옵션재고에서 관리
		        	//long orderAbleQty = Long.parseLong(requestGoodsMap.get("TRANS_ORDER_ABLE_QTY").toString());
		        	long orderAbleQty = 99999;
		        	if(orderAbleQty>99999){
		        		orderAbleQty=99999;
		        	}
		        	paGmktGoods.setTransOrderAbleQty(orderAbleQty);
		        	
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
		        		paPromoTarget.setGoodsCode(goodsCode);
		        		paPromoTarget.setPaCode(paCode);
		        		paPromoTarget.setPromoNo(priceMap.get("PROMO_NO").toString());
		        		paPromoTarget.setSeq(priceMap.get("SEQ").toString());
		        		paPromoTarget.setDoCost(Double.parseDouble(priceMap.get("DO_COST").toString()));
		        		paPromoTarget.setDoOwnCost(Double.parseDouble(priceMap.get("DO_OWN_COST").toString()));
		        		paPromoTarget.setDoEntpCost(Double.parseDouble(priceMap.get("DO_ENTP_COST").toString()));
		        		paPromoTarget.setPaGroupCode(priceMap.get("PA_GROUP_CODE").toString());
		        		
		        		paPromoTargetList.add(paPromoTarget);
		        	}
		        	//옥션
		        	for(HashMap<String,Object> priceMap : IacPriceMapList){
		        		
		        		if(priceMap.get("PROMO_NO").toString().equals("000000000000")){
		        			continue;
		        		}
		        		
		        		paPromoTarget = new PaPromoTarget();
		        		paPromoTarget.setGoodsCode(goodsCode);
		        		paPromoTarget.setPaCode(paCode);
		        		paPromoTarget.setPromoNo(priceMap.get("PROMO_NO").toString());
		        		paPromoTarget.setSeq(priceMap.get("SEQ").toString());
		        		paPromoTarget.setDoCost(Double.parseDouble(priceMap.get("DO_COST").toString()));
		        		paPromoTarget.setDoOwnCost(Double.parseDouble(priceMap.get("DO_OWN_COST").toString()));
		        		paPromoTarget.setDoEntpCost(Double.parseDouble(priceMap.get("DO_ENTP_COST").toString()));
		        		paPromoTarget.setPaGroupCode(priceMap.get("PA_GROUP_CODE").toString());
		        		
		        		paPromoTargetList.add(paPromoTarget);
		        	}
		        	
		        	//= Step 4) table insert
		        	paGmktGoodsService.savePaGmktGoodsInsertTx(paGmktGoods, paPromoTargetList);
		        	//paGmktGoodsService.savePaGmktGoodsInsertTx(paGmktGoods);
		        	
		        	//= Step 5)상품 site goods no 처리중이라고 리턴됨;
		        	//getGoodsStatus(request,paCode,goodsCode);
		        	//= Step 6) 상품 g마켓상품번호가 생성되지않았다고 리턴됨;
		        	//postGoodsOption(request,paramMap.getString("paCode"),paramMap.getString("goodsCode"));
		        	
		        	paramMap.put("code", "200");
		        	paramMap.put("message", "OK");					
				} catch (Exception se) {
					CommonUtil.dealException(se, paramMap);
					if(!duplicateCheck.equals("1")){
						//중복실행처리는 반려처리를 하지않기 위함
						savePaGoodsInsertReject(paramMap);
					}
					log.error(paramMap.getString("message"), se);

				}finally {
					try{
						paGmktAsycController.savePaGoodsTransLog(paramMap);
						CommonUtil.dealSuccess(paramMap, request);
					}catch(Exception e){
						log.error("ApiTracking Insert Error : "+e.getMessage());
					}
					paramMapMsg.put("code", paramMap.getString("code"));
		        	paramMapMsg.put("message", paramMap.getString("message"));
				}
			}		
		} catch (Exception se) {
			if(duplicateCheck.equals("1")){
				paramMapMsg.put("code","490");
			} else {
				paramMapMsg.put("code", "500");	
			}
        	paramMapMsg.put("message", se.getMessage());
			log.error(paramMapMsg.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMapMsg.getString("code"), paramMapMsg.getString("message")), HttpStatus.OK);

		}finally {			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
			log.info("===== 상품 등록 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMapMsg.getString("code"), paramMapMsg.getString("message")), HttpStatus.OK);
	}
	
	public String paExceptGoodsYn(String goodsCode) throws Exception{
		String rtnMsg = "연동제약사항 체크";
		boolean succeess = true;
		int executedRtn = 0;
		HashMap<String, Object> paExceptListMap = null;
		
		executedRtn = paGmktGoodsService.selectChkShipCost(goodsCode);
		
		if (executedRtn < 1) {
			rtnMsg += ", 배송비정책";
			succeess = false;
		}
		paExceptListMap = paGmktGoodsService.selectPaEbayCheckGoods(goodsCode);
		if (paExceptListMap.get("MARGIN_RATE_GMKT").equals("N")) {
			rtnMsg += ", 마진율";
			succeess = false;
		}
		if (paExceptListMap.get("STOCK_QTY").equals("N")) {
			rtnMsg += ", 재고";
			succeess = false;
		}
		if (paExceptListMap.get("OMBUDSMAN_YN").equals("N")) {
			rtnMsg += ", 옴부즈맨";
			succeess = false;
		}
		if (paExceptListMap.get("BROAD_SALE_YN").equals("N")) {
			rtnMsg += ", 방송중판매";
			succeess = false;
		}
		if (paExceptListMap.get("INVI_GOODS_TYPE_YN").equals("N")) {
			rtnMsg += ", 무형상품";
			succeess = false;
		}
		if (paExceptListMap.get("ORDER_MEDIA_YN").equals("N")) {
			rtnMsg += ", 주문매체";
			succeess = false;
		}
		if (paExceptListMap.get("SALE_PRICE_YN_GMKT").equals("N")) {
			rtnMsg += ", 판매가";
			succeess = false;
		}
		if (paExceptListMap.get("SALE_END_YN").equals("N")) {
			rtnMsg += ", 판매종료일";
			succeess = false;
		}
		if(!succeess){
			return rtnMsg;
		}else{
			return "000000";
		}
	}

	/**
	 * 상품등록 후처리
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "상품등록 후처리", notes = "상품등록 후처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-goods-status-option", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putGoodsStatusOption(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		/*
		String paCode[] = {"21","22"};
		for(String pa:paCode){
			postGoodsOptionList(request,pa);
		}
		*/
		//전체상품대상(trans_sale_yn=1 or trans_target_yn=1) 상품가격/판매상태/재고 업데이트 -> trans_target_yn=0 , trans_sale_yn=0, => 중지처리 로직으로 개선 2019.01.07 thjeon, 재고는 옵션수정에 포함(옵션상품 옵션재고수정)
		putGoodsPriceSaleYnList(request);
		putGoodsNameList(request);
		
		paramMap.put("code", "200");
		paramMap.put("message", "OK");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 * 전체 상품 site goods no
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	/*
	@RequestMapping(value = "/get-goods-status-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getGoodsStatusList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String,Object> target = new HashMap<String,Object>();
		int okCnt = 0;
		int totCnt = 0;
		List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectSiteGoodsNoTarget(target);
		totCnt = targetGoodsList.size();
		for(HashMap<String,Object> targetGoods : targetGoodsList){
			ResponseEntity<?> result = getGoodsStatus(request,targetGoods.get("PA_CODE").toString(),targetGoods.get("GOODS_CODE").toString());
			if(result.getStatusCode().toString().equals("200")){
				//okCnt++;
				okCnt++;
			}
		}
		paramMap.put("code", "200");
		paramMap.put("message", totCnt+" 중 "+okCnt+ " 적용완료 ");
		log.info("site 상품번호 조회 : "+paramMap.get("message"));
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	*/
	
	/**
	 * 상품 site goods no - 2019.1.16 이후 사용하지 않음 (상품등록에 포함된 신규API 전달받음)
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	/*
	@RequestMapping(value = "/get-goods-status", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getGoodsStatus(HttpServletRequest request,
			@RequestParam(value="paCode", required=true) String paCode,			
			@RequestParam(value="goodsCode", required=true) String goodsCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_002";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("goodsCode", goodsCode);
		
		
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			//단일타겟만 조회하도록 프로세싱 , 다중 처리는 상위에서 List돌리도록
			HashMap<String,Object> target = new HashMap<String,Object>();
			target.put("paCode", paCode);
			target.put("goodsCode", goodsCode);
			
			List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectSiteGoodsNoTarget(target);
			for(HashMap<String,Object> targetGoods : targetGoodsList){
				paramMap.put("urlParameter",targetGoods.get("ESM_GOODS_CODE"));
				
				//= Step 2) 통신
				String response = restUtil.getConnection(rest,  paramMap);
				Map<String,Object> resMap = ComUtil.splitJson(response);
				//= Step 3) 통신후 resMap 파싱 & setter
		    	Map<String,Object> siteDetail = (Map<String, Object>) resMap.get("siteDetail");
				Map<String,Object> gmktSiteDetail = (Map<String, Object>)siteDetail.get("gmkt");
				HashMap<String,Object> insertTarget = new HashMap<>();
				insertTarget.put("siteGoodsNo", gmktSiteGoodsNo);
				insertTarget.put("esmGoodsCode",targetGoods.get("ESM_GOODS_CODE"));
				
				
				
				paramMap.put("itemNo", gmktSiteDetail.get("SiteGoodsNo").toString());
				PaGmktGoods paGmktGoods = new PaGmktGoods();
		    	paGmktGoods.setEsmGoodsCode(targetGoods.get("ESM_GOODS_CODE").toString());
		    	paGmktGoods.setItemNo(gmktSiteDetail.get("SiteGoodsNo").toString());
		    	paGmktGoods.setGoodsCode(targetGoods.get("GOODS_CODE").toString());
		    	paGmktGoods.setPaCode(targetGoods.get("PA_CODE").toString());
		    	paGmktGoods.setPaSaleGb(targetGoods.get("PA_SALE_GB").toString());
		    	paGmktGoods.setModifyId("PAG");
		    	
		    	//= Step 4) table insert
		    	paGmktGoodsService.saveSiteGoodsNoTargetTx(paGmktGoods);
			}
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 상품 site goods No 조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	*/
	/**
	 * 전체상품 일괄수정 , 3분/33분 배치
	 */
	@ApiOperation(value = "전체상품 일괄수정 , 3분/33분 배치", notes = "전체상품 일괄수정 , 3분/33분 배치", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-goods-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putGoodsList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String,Object> target = new HashMap<String,Object>();
		int totCnt = 0;
		int procCnt = 0;
		String sinceTargetEntpCode = "";
		String sinceTargetEntpManSeq = "";
		String sinceTargetShipCostCode = "";
		String sinceTargetReturnManSeq = "";
		String targetEntpCode = "";
		String targetEntpManSeq = "";
		String targetShipCostCode = "";
		String targetReturnManSeq = "";
		boolean errorFlag = false; // true시 해당 업체 전체중지처리를 하기위함 
		ParamMap mapFor500 = new ParamMap();// 500에러 판별 위한 map
		
		paramMap.put("code", "200");
		paramMap.put("message","상품수정처리");
		
		String duplicateCheck = "";
		String apiCode = "IF_PAGMKTAPI_V2_01_003";
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("FROM_DATE",DateUtil.addDay(systemService.getSysdatetime(),  0 ));
		paramMap.put("isModify", "Y");
		paramMap.put("siteGb", "afterset");
		
		//최초 세팅.
		//paramMap.put("paCode", "21");
		//paramMap.put("siteGb", "PAG");
		
		try {
			log.info("02.API 중복실행검사");
            //= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
	    	if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});	
	    	
			// 타켓 건수 적용
			String targetCnt = paGmktGoodsService.selectCodeContentForTargetCnt("IF_PAGMKTAPI_V2_01_003");
			if(targetCnt != null && !"".equals(targetCnt)){
				target.put("targetCnt"		, Integer.parseInt(targetCnt));
				//target.put("massTargetYn"	,"0");
			}
			
			List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGmktGoodsModifyList(target);
			totCnt = targetGoodsList.size();
			//		System.out.println("적용CNT:"+totCnt);
			
			for(HashMap<String,Object> targetGoods : targetGoodsList){
				
				//https://stackoverflow.com/questions/28288546/how-to-copy-hashmap-not-shallow-copy-in-java/28288729
				//java 8로 업그레이드 이후 위 링크 들어가서 paramMap복사하는 법 참고해서 테스트해보기 AsyncMap을 마지막에 그냥 복사처리만 하면될듯
				//mapCopy = map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> List.copyOf(e.getValue())))
				ParamMap asyncMap = new ParamMap();
				asyncMap.put("apiCode", apiCode);
				asyncMap.put("startDate", systemService.getSysdatetimeToString());
				asyncMap.put("FROM_DATE",DateUtil.addDay(systemService.getSysdatetime(),  0 ));
				asyncMap.put("isModify", "Y");
				asyncMap.put("siteGb", "afterset");
				
				targetEntpCode = targetGoods.get("ENTP_CODE").toString();
				targetEntpManSeq = targetGoods.get("SHIP_MAN_SEQ").toString();
				targetShipCostCode = targetGoods.get("SHIP_COST_CODE").toString();
				targetReturnManSeq = targetGoods.get("RETURN_MAN_SEQ").toString();
				//(entpCode,entpManSeq,ShipCostCode)가 이전항목과 동일하다면 pass 로직 추가
				
				// 이베이 상품 유효기간 조회 후 만료 대상 유효기간 갱신 처리 2022.01.26 LEEJY
				HashMap<String,String>expiryGoodsList = paGmktGoodsService.selecGoodsExpiryDateYn(targetGoods.get("GOODS_CODE").toString());
				
				if(expiryGoodsList.get("EXPIRY_DATE").equals("Y")){
					putGoodsExpiryDate(request, targetGoods.get("PA_CODE").toString(), targetGoods.get("GOODS_CODE").toString());// 해당 메소드 에러발생 무시
				}
				
				if(sinceTargetEntpCode.equals(targetEntpCode) && sinceTargetEntpManSeq.equals(targetEntpManSeq) && sinceTargetShipCostCode.equals(targetShipCostCode)&& sinceTargetReturnManSeq.equals(targetReturnManSeq)){
					//업체가 변경되지 않은 케이스:::
					//이전 업체가 errorFlag = true인 경우
					if(errorFlag){
						asyncMap.put("goodsCode",targetGoods.get("GOODS_CODE"));
						asyncMap.put("paCode",targetGoods.get("PA_CODE"));
						asyncMap.put("message","판매자주소 - 출고지 수정시 에러, 업체코드 / 출고지 / 회수지 : "+targetEntpCode+" / "+targetEntpManSeq+" / "+targetReturnManSeq);
						paramMap.put("goodsCode",targetGoods.get("GOODS_CODE"));
						paramMap.put("paCode",targetGoods.get("PA_CODE"));
						paramMap.put("message","판매자주소 - 출고지 수정시 에러, 업체코드 / 출고지 / 회수지 : "+targetEntpCode+" / "+targetEntpManSeq+" / "+targetReturnManSeq);
						paGmktAsycController.savePaGoodsInsertFail(asyncMap);
					}else{
						//이전 업체가 error가 나지않은 경우	
						log.info("============동일 출고지/동일 배송비정책 중복 처리 프로세스=============");
					}
				} else{					
					// 판매자주소 - 출하지 - 묶음배송 등록 API 별도의 배치로 분리 처리 20220509 LEEJY					
					//업체가 변경되었을시에만 flag를 false로 변경
					errorFlag = false;
					//판매자주소 - 출하지 - 묶음배송 등록
					try{
//						paGmktV2CommonController.beforeGoodsInsert(request, mapFor500, targetGoods.get("GOODS_CODE").toString(), "0"); //구버전
						
						// 이슈로 인하여 출고회수지 재사용 처리 
//						paGmktV2CommonController.enrollEntpShipCost(request, "", "1"); // 신버전
					} catch (Exception e) {
						//에러시 errorFlag를 true로 셋팅, true인 경우 다음 업체가 같은업체이면 상품들을 중지처리한다	
						errorFlag= true;//판매자주소 - 출하지 - 묶음배송 등록 에러시 errorFlag = true로 바꿔주어, 해당 업체는 모두 중지처리하게 하기 위함..
						
						asyncMap.put("goodsCode",targetGoods.get("GOODS_CODE"));
						asyncMap.put("paCode",targetGoods.get("PA_CODE"));
						asyncMap.put("code", "500");
						asyncMap.put("message", e.getMessage());
						paramMap.put("goodsCode",targetGoods.get("GOODS_CODE"));
						paramMap.put("paCode",targetGoods.get("PA_CODE"));
						paramMap.put("code", "500");
						paramMap.put("message", e.getMessage());
						paGmktAsycController.savePaGoodsInsertFail(asyncMap);
					}
				}
				//현재 사용한 업체정보를 이전정보로 저장
				sinceTargetEntpCode = targetEntpCode;
				sinceTargetEntpManSeq = targetEntpManSeq;
				sinceTargetShipCostCode = targetShipCostCode;
				sinceTargetReturnManSeq = targetReturnManSeq;
				
				//상품 일괄 수정
				try{
					
					asyncMap.put("entpCode", targetEntpCode);
					asyncMap.put("entpManSeq", targetEntpManSeq);
					asyncMap.put("shipCostCode", targetShipCostCode);
					asyncMap.put("goodsCode", targetGoods.get("GOODS_CODE").toString());
					asyncMap.put("paCode", targetGoods.get("PA_CODE").toString());
					paramMap.put("entpCode", targetEntpCode);
					paramMap.put("entpManSeq", targetEntpManSeq);
					paramMap.put("shipCostCode", targetShipCostCode);
					paramMap.put("goodsCode", targetGoods.get("GOODS_CODE").toString());
					paramMap.put("paCode", targetGoods.get("PA_CODE").toString());
										
					int cnt = paGmktGoodsService.selectCheckDeliveryFee(asyncMap);
					
					if (cnt == 1) {
						asyncMap.put("message", "유료배송비의 금액이 0원");
						paGmktAsycController.savePaGoodsInsertFail(asyncMap);
						continue;
					}
					procCnt++;
					log.info("상품수정.."+totCnt+" 중 "+procCnt+ " 진행 중..");
					//result = putGoods(request,targetGoods.get("PA_CODE").toString(),targetGoods.get("GOODS_CODE").toString(),"API","");
					asyncMap = goodsInfoValidation(asyncMap);
					
					asyncMap.put("lastModifyDate", paramMap.get("startDate")); //상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와  수정대상조회 시작시간을 비교함
					//asyncMap = paramMap;
					if(!asyncMap.getString("code").equals("200")){
						paGmktAsycController.savePaGoodsInsertFail(asyncMap);
						continue;
					}
					
					paGmktAsycController.putGoods(request, asyncMap);
					Thread.sleep(50);

				}catch(Exception e){
					continue;
				}
			}
		}catch(Exception e){
    		if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")),HttpStatus.OK);
    	}finally{
    		try {
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
		   if(duplicateCheck.equals("0")){
			   systemService.checkCloseHistoryTx("end", apiCode);
		   }    			 

			log.info("===== 상품수정 API End=====");
    	}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	//상품 일괄 수정 (출하지등의 모든 정보(등록API와 같은 JSON))
	//BO에서 호출하는건 여기탐. 전체상품수정 호출하는건 AsycController탐. 후에 BO에서 호출하는 부분 수정처리 진행할 예정 by jchoi
	/**
	 * 상품 수정
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "상품 수정", notes = "상품 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-goods", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putGoods(HttpServletRequest request,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,			
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode,
			@ApiParam(name = "inComingUrl", value = "API여부", defaultValue = "") @RequestParam(value="inComingUrl", required=false, defaultValue="API") String inComingUrl,
			@ApiParam(name = "searchTermGb", value = "중복실행체크여부", defaultValue = "") @RequestParam(value="searchTermGb"	, required=false, defaultValue = "") String searchTermGb
			) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_003_S";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "afterset");
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("FROM_DATE",DateUtil.addDay(systemService.getSysdatetime(),  0 ));
		//상품수정 여부
		paramMap.put("isModify", "Y");
		ParamMap mapFor500 = new ParamMap();// 500에러 판별 위한 map
		boolean putShippingPlaces500Flag = false;
		log.info("상품코드 "+goodsCode);
				
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});				
			}
									
			// 이베이 상품 유효기간 조회 후 만료 대상 유효기간 갱신 처리 2022.01.26 LEEJY
			HashMap<String,String>expiryGoodsList = paGmktGoodsService.selecGoodsExpiryDateYn(goodsCode);
			
			if(expiryGoodsList.get("EXPIRY_DATE").equals("Y")){
				putGoodsExpiryDate(request, paCode, goodsCode);// 해당 메소드 에러발생 무시
			}
			
			// 이베이 단품을 재등록 한다 2022.02.08 LEEJY
			paGmktAsycController.postGoodsOption(request,paCode, goodsCode ,"BO");	
			
			paramMap.put("inComingUrl", inComingUrl);
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			paramMap = goodsInfoValidation(paramMap);
			if(!paramMap.getString("code").equals("200")){
				paGmktAsycController.savePaGoodsInsertFail(paramMap);
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			//= Step 2) 통신
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			Map<String,Object> siteDetail = (Map<String, Object>) resMap.get("siteDetail");
			Map<String,Object> gmktSiteDetail = (Map<String, Object>)siteDetail.get("gmkt");
			Map<String,Object> iacSiteDetail = (Map<String, Object>)siteDetail.get("iac");
			
			//= Step 3) 통신후 resMap 파싱 & setter
			PaGmktGoodsVO paGmktGoods = new PaGmktGoodsVO();
			paGmktGoods.setEsmGoodsCode(resMap.get("goodsNo").toString());
			paGmktGoods.setGoodsCode(goodsCode);
			paGmktGoods.setPaCode(paCode);
			//paGmktGoods.setPaSaleGb(requestGoodsMap.get("PA_SALE_GB").toString());
			paGmktGoods.setModifyId(paramMap.getString("siteGb"));
			//MODIFY_DATE <-> STARTDATE 비교용도
			paGmktGoods.setLastModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
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
	    	List<HashMap<String,Object>> gmktPriceMapList = (List<HashMap<String, Object>>) paramMap.get("requestGmktPriceMapList");;
        	List<HashMap<String,Object>> IacPriceMapList = (List<HashMap<String, Object>>) paramMap.get("requestIacPriceMapList");	;
        	
	    	PaPromoTarget paPromoTarget;
	    	List<PaPromoTarget> paPromoTargetList = new ArrayList<>();
	    	
	    	//G마켓
	    	for(HashMap<String,Object> priceMap : gmktPriceMapList){
	    		
	    		if(priceMap.get("PROMO_NO").toString().equals("000000000000")){
	    			continue;
	    		}
	    		
	    		paPromoTarget = new PaPromoTarget();
	    		paPromoTarget.setGoodsCode(goodsCode);
	    		paPromoTarget.setPaCode(paCode);
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
	    		paPromoTarget.setGoodsCode(goodsCode);
	    		paPromoTarget.setPaCode(paCode);
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
//	    	paGmktGoodsService.savePaGmktGoodsModifyTx(paGmktGoods);
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			if(!duplicateCheck.equals("1")){
				//중복실행처리는 중지처리를 하지않기 위함
				paGmktAsycController.savePaGoodsInsertFail(paramMap);
				log.error(paramMap.getString("message"), se);
			}else{
				log.info(paramMap.getString("message"), se);
			}
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);

		}finally {
			try{
				paGmktAsycController.savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(!searchTermGb.equals("1")){
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);				
			}
			log.info("===== 상품 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	
	
	/**
	 * 전체 상품 옵션 등록
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 *//*
	
	@RequestMapping(value = "/post-goods-option-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postGoodsOptionList(HttpServletRequest request,
			@RequestParam(value="paCode", required=true) String paCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String,Object> target = new HashMap<String,Object>();
		target.put("paCode", paCode);
		int okCnt = 0;
		int totCnt = 0;
		List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGoodsOptionList(target);
		totCnt = targetGoodsList.size();
		for(HashMap<String,Object> targetGoods : targetGoodsList){
			ResponseEntity<?> result = postGoodsOption(request,targetGoods.get("PA_CODE").toString(),targetGoods.get("GOODS_CODE").toString());
			if(result.getStatusCode().toString().equals("200")){
				okCnt++;
			}
		}
		paramMap.put("code", "200");
		paramMap.put("message", totCnt+" 중 "+okCnt+ " 적용완료 ");
		log.info("옵션 등록 : "+paramMap.get("message"));
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}*/
	
	/**
	 * 전체 상품 옵션 수정
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "전체 상품 옵션 수정", notes = "전체 상품 옵션 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-goods-option-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putGoodsOptionList(HttpServletRequest request,
			@RequestParam(value="paCode", required=true) String paCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String,Object> target = new HashMap<String,Object>();
		target.put("paCode", paCode);
		int okCnt = 0;
		int totCnt = 0;
		int procCnt = 0;
		ResponseEntity<?> result = null;
		
		String apiCode = "21".equals(paCode) ? "IF_PAGMKTAPI_V2_01_014_B" : "IF_PAGMKTAPI_V2_01_014_O";
		String duplicateCheck = "";
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("FROM_DATE",DateUtil.addDay(systemService.getSysdatetime(),  0 ));
		paramMap.put("siteGb", "PAG");
		paramMap.put("code", "200");
		paramMap.put("message", "옵션 수정 시작");
		
		try {
			log.info("02.API 중복실행검사");
            //= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
	    	if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
		
	    	// 타켓 건수 적용
	    	String targetCnt = paGmktGoodsService.selectCodeContentForTargetCnt(apiCode);
	    	if(targetCnt != null && !"".equals(targetCnt)){
	    		target.put("targetCnt", Integer.parseInt(targetCnt));
	    	}
	    	
	    	target.put("isModify", "Y");
	    	List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGoodsOptionList(target);
	    	totCnt = targetGoodsList.size();
	    	for(HashMap<String,Object> targetGoods : targetGoodsList){
	    		procCnt++;
	    		log.info("옵션수정.."+totCnt+" 중 "+procCnt+ " 진행 중 ");
	    		String goodsCode = targetGoods.get("GOODS_CODE").toString();
	    		if (paCode.equals("21")) {
	    			//result = putGoodsOptionBroad(request,targetGoods.get("PA_CODE").toString(),targetGoods.get("GOODS_CODE").toString());
	    			paGmktAsycController.putGoodsOptionBroadSub(request,paCode,goodsCode,0);
	    		} else {
	    			//result = putGoodsOptionOnline(request,targetGoods.get("PA_CODE").toString(),targetGoods.get("GOODS_CODE").toString());
	    			paGmktAsycController.putGoodsOptionOnlineSub(request,paCode,goodsCode,0);
	    		}
	    		okCnt++;
	    		Thread.sleep(50);
	    	}
	    	paramMap.put("message", totCnt+" 중 "+okCnt+ " 적용완료 ");
		} catch(Exception e){
    		if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")),HttpStatus.OK);
    	}finally{
    		try {
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
    		if(duplicateCheck.equals("0")){
    			systemService.checkCloseHistoryTx("end", apiCode);
    		}			 

    		log.info("옵션 수정 : "+paramMap.get("message"));
    	}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 * 상품 옵션 등록 LIST 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "전체 상품 옵션 수정", notes = "전체 상품 옵션 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-goods-option-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postGoodsOptionList(HttpServletRequest request,
			@ApiParam(name = "inComingUrl", value = "API여부", defaultValue = "") @RequestParam(value="inComingUrl", required=false, defaultValue="API") String inComingUrl
			) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String,Object> target = new HashMap<String,Object>();
		int okCnt = 0;
		int totCnt = 0;
		int procCnt = 0;
		ResponseEntity<?> result = null;
		
		String apiCode = "IF_PAGMKTAPI_V2_01_013_L";
		String duplicateCheck = "";
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("FROM_DATE",DateUtil.addDay(systemService.getSysdatetime(),  0 ));
		paramMap.put("siteGb", "PAG");
		paramMap.put("code", "200");
		paramMap.put("message", "옵션 등록 시작");
		
		try {
			
			log.info("02.API 중복실행검사");
            //= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
	    	if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			// 타켓 건수 적용
			String targetCnt = paGmktGoodsService.selectCodeContentForTargetCnt(apiCode);
			if(targetCnt != null && !"".equals(targetCnt)){
				target.put("targetCnt", Integer.parseInt(targetCnt));
			}
			
			List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGoodsOptionListForResister(target);
			totCnt = targetGoodsList.size();
			for(HashMap<String,Object> targetGoods : targetGoodsList){
				procCnt++;
				log.info("옵션등록 "+totCnt+" 중 "+procCnt+ " 진행 중 ");
				String goodsCode = targetGoods.get("GOODS_CODE").toString();
				String paCode = targetGoods.get("PA_CODE").toString();
				//result = postGoodsOption(request,targetGoods.get("PA_CODE").toString(),targetGoods.get("GOODS_CODE").toString());
				paGmktAsycController.postGoodsOption(request,paCode, goodsCode);
				Thread.sleep(50);
				//if(result.getStatusCode().toString().equals("200")){
				okCnt++;
				//}
			}
			
			paramMap.put("code", "200"); 
			paramMap.put("message", totCnt+" 중 "+okCnt+ " 적용완료 ");
			log.info("옵션 등록 : "+paramMap.get("message"));
		} catch(Exception e){
    		if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")),HttpStatus.OK);
    	}finally{
    		try {
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
    		if(duplicateCheck.equals("0")){
    			systemService.checkCloseHistoryTx("end", apiCode);
    		}    			 

    		log.info("옵션 수정 : "+paramMap.get("message"));
    	}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 * 옵션 등록 API LIMITED
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
/* 미사용처리
	@RequestMapping(value = "/post-goods-option", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postGoodsOption(HttpServletRequest request,
			@RequestParam(value="paCode", required=true) String paCode,			
			@RequestParam(value="goodsCode", required=true) String goodsCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_013";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("siteGb", selectGmktSellerId(paramMap));
		
		List<HashMap<String,Object>> targetOptionList = new ArrayList<>();
		try{			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			//단일타겟만 조회하도록 프로세싱 , 다중 처리는 상위에서 List돌리도록
			HashMap<String,Object> target = new HashMap<String,Object>();
			target.put("paCode", paCode);
			target.put("goodsCode", goodsCode);
			
			//전역화 (finally에서 사용할 수 있도록)
			targetOptionList = paGmktGoodsService.selectGoodsOption(target);
			paramMap.put("urlParameter",targetOptionList.get(0).get("ESM_GOODS_CODE"));
			paramMap.put("map", targetOptionList);
			
			//= Step 2) 통신
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			//= Step 3) 통신후 resMap 파싱 & setter
			
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			paGmktAsycController.savePaGoodsInsertFail(paramMap);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			try{
				paGmktAsycController.savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
				
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
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 상품 옵션등록 LIMITED API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
*/
	
	/**
	 * 옵션 수정 API LIMITED (방송)
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	/* Async로 이동처리
	public ResponseEntity<?> putGoodsOptionBroad(HttpServletRequest request, String paCode, String goodsCode) throws Exception{
		return putGoodsOptionBroadSub(request, paCode, goodsCode, 0); 
	}
	
	public ResponseEntity<?> putGoodsOptionBroadSub(HttpServletRequest request, String paCode, String goodsCode, int loopCnt) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_014_B";
		String duplicateCheck = "";
		boolean requestFlag = true;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("siteGb", selectGmktSellerId(paramMap));
		
		List<HashMap<String,Object>> targetOptionList = new ArrayList<>();
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
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
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			
			//= Step 3) 통신후 resMap 파싱 & setter
			
	    	//savePaGoodsInsertSuccess(paramMap);
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			if( !("400".equals(paramMap.getString("code")) && (paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_B500") > -1 
					|| paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_B502") > -1)  && loopCnt < 3) ){
				paGmktAsycController.savePaGoodsInsertFail(paramMap);
			}
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			try{
				paGmktAsycController.savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
				//상품 수정실패시 중지처리후 trans_target_yn을 0으로 업데이트하도록 finally에서 처리 2018.11.28 thjeon
				
				if("400".equals(paramMap.getString("code")) && (paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_B500") > -1 || paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_B502") > -1)){
					if(loopCnt < 3){
						
						if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
						log.info("===== 상품 옵션수정 putGoodsOptionBroadSub LIMITED API END =====");
						
						putGoodsOptionBroadSub(request, paCode, goodsCode, loopCnt + 1); 
						requestFlag = false;
						
					}else{
						requestFlag = true;
					}
				}else{
					requestFlag = true;
				}
				
				if(requestFlag){
					
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
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 상품 옵션수정 LIMITED API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	*/
	
	/**
	 * 옵션 수정 API LIMITED (온라인)
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	/* Async로 이동처리
	public ResponseEntity<?> putGoodsOptionOnline(HttpServletRequest request, String paCode, String goodsCode) throws Exception{
		return putGoodsOptionOnlineSub(request, paCode, goodsCode, 0); 
	}
	
	public ResponseEntity<?> putGoodsOptionOnlineSub(HttpServletRequest request, String paCode, String goodsCode, int loopCnt) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_014_O";
		String duplicateCheck = "";
		boolean requestFlag = true;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("siteGb", selectGmktSellerId(paramMap));
		
		List<HashMap<String,Object>> targetOptionList = new ArrayList<>();
		try{			
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
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
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			
			//= Step 3) 통신후 resMap 파싱 & setter
			
	    	//savePaGoodsInsertSuccess(paramMap);
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			if( !("400".equals(paramMap.getString("code")) && (paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_O500") > -1 
					|| paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_O502") > -1) && loopCnt < 3) ){
				paGmktAsycController.savePaGoodsInsertFail(paramMap);
			}
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			try{
				paGmktAsycController.savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
				
				if("400".equals(paramMap.getString("code")) && (paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_O500") > -1 || paramMap.getString("message").indexOf("IF_PAGMKTAPI_V2_01_014_O502") > -1)){
					if(loopCnt < 3){
						
						if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
						log.info("===== 상품 옵션수정 putGoodsOptionOnlineSub LIMITED API END =====");
						
						putGoodsOptionOnlineSub(request, paCode, goodsCode, loopCnt + 1); 
						requestFlag = false;
						
					}else{
						requestFlag = true;
					}
				}else{
					requestFlag = true;
				}
				
				if(requestFlag){
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
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 상품 옵션수정 LIMITED API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	*/
	
	/**
	 * 전체 상품 가격/재고/판매상태 수정 => 중지처리 로직으로 개선 2019.01.07 thjeon, 재고는 옵션수정에 포함(옵션상품 옵션재고수정)
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "전체 상품 가격/재고/판매상태 수정 => 중지처리 로직으로 개선", notes = "전체 상품 가격/재고/판매상태 수정 => 중지처리 로직으로 개선", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-goods-price-sale-yn-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putGoodsPriceSaleYnList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String,Object> target = new HashMap<String,Object>();
		
		String duplicateCheck = "";
		String apiCode = "IF_PAGMKTAPI_V2_01_004";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("FROM_DATE",DateUtil.addDay(systemService.getSysdatetime(),  0 ));
		paramMap.put("siteGb", "PAG");
		paramMap.put("code", "200");
		paramMap.put("message", "상품중지처리 시작");
		
		try {
			log.info("02.API 중복실행검사");
            //= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
	    	if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			// 타켓 건수 적용
			String targetCnt = paGmktGoodsService.selectCodeContentForTargetCnt("IF_PAGMKTAPI_V2_01_004");
			if(targetCnt != null && !"".equals(targetCnt)){
				target.put("targetCnt", Integer.parseInt(targetCnt));
			}
			
			int okCnt = 0;
			int totCnt = 0;
			List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGmktGoodsPriceSaleModifyList(target);
			totCnt = targetGoodsList.size();
			for(HashMap<String,Object> targetGoods : targetGoodsList){
				ResponseEntity<?> result = putGoodsPriceSaleYn(request,targetGoods.get("PA_CODE").toString(),targetGoods.get("GOODS_CODE").toString(),"0");
				if(result.getStatusCode().toString().equals("200")){
					okCnt++;
				}
			}
			
			paramMap.put("code", "200");
			paramMap.put("message", totCnt+" 중 "+okCnt+ " 적용완료 ");
			
		}catch(Exception e){
    		if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")),HttpStatus.OK);
    	}finally{
    		try {
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
    		if(duplicateCheck.equals("0")){
    			systemService.checkCloseHistoryTx("end", apiCode);
    		}    			 

    		log.info("판매 중지 처리 : "+paramMap.get("message"));
    	}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품 가격/재고/판매상태 수정
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "상품 가격/재고/판매상태 수정", notes = "상품 가격/재고/판매상태 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-goods-price-sale-yn", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putGoodsPriceSaleYn(HttpServletRequest request,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,			
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode,
			@ApiParam(name = "searchTermGb", value = "중복실행체크여부", defaultValue = "") @RequestParam(value="searchTermGb"	, required=false, defaultValue = "") String searchTermGb)
			throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_004";            
		 
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("goodsCode", goodsCode);
		
		/**
		 * TODO HSBAEK 옥션만 판매중지했을때 지마켓도 판매중지 되는 현상이 존재한다.
		 * 아래 주석을 처리하면 일단 판매중지에서는 정확하게 연동하지만, 그상태에서 상품수정에서 G마켓 상품 수정할때 옥션도 다시 판매중으로 바뀌어버린다.(씽크가 안맞는 이상한 데이터 만듬..)
		 * 상품 수정도 고치고 밑의 주석을 해제하도록 하자.
		 * **/ 
		//paramMap.put("paSaleGb", "30"); 
		paramMap.put("siteGb", selectGmktSellerId(paramMap));
		
		List<HashMap<String,Object>> targetGoodsList = new ArrayList<>();
		HashMap<String,Object> target = new HashMap<String,Object>();
		
		try{
			//= Step 1) select 테이블 후 urlParameter 셋팅
			//단일타겟만 조회하도록 프로세싱 , 다중 처리는 상위에서 List돌리도록
			target.put("paCode", paCode);
			target.put("goodsCode", goodsCode);
			
			// 타켓 건수 적용
			String targetCnt = paGmktGoodsService.selectCodeContentForTargetCnt("IF_PAGMKTAPI_V2_01_004");
			if(targetCnt != null && !"".equals(targetCnt)){
				target.put("targetCnt", Integer.parseInt(targetCnt));
			}
			
			targetGoodsList = paGmktGoodsService.selectGmktGoodsPriceSaleModifyList(target);
			target.put("esmGoodsCode", targetGoodsList.get(0).get("ESM_GOODS_CODE"));
			
			for(HashMap<String,Object> targetGoods : targetGoodsList){
				target.put("esmGoodsCode", targetGoods.get("ESM_GOODS_CODE"));
				paramMap.put("urlParameter",targetGoods.get("ESM_GOODS_CODE"));
				paramMap.put("map", targetGoods);
				paramMap.put("isSell", "false");
				try {
					//= Step 2) 통신
					String response = restUtil.getConnection(rest,  paramMap);
					Map<String,Object> resMap = ComUtil.splitJson(response);
					//= Step 3) 통신후 resMap 파싱 & setter
					PaGmktGoods paGmktGoods = new PaGmktGoods();
					paGmktGoods.setEsmGoodsCode(targetGoods.get("ESM_GOODS_CODE").toString());
					paGmktGoods.setPaCode(paCode);
					//= Step 4) table insert
					
					if("PAG".equals(paramMap.get("siteGb"))) {
						paGmktGoods.setPaGroupCode("02");
					}else if("PAA".equals(paramMap.get("siteGb"))) {
						paGmktGoods.setPaGroupCode("03");
					}
					
					paGmktGoodsService.saveGmktGoodsPriceSaleModifyTx(paGmktGoods);
				
					
				} catch (Exception se) {
					if (se.getMessage().indexOf("동일한 상태로의 변경") > 0 || se.getMessage().indexOf("동일한 판매상태로 변경") > 0) {
						PaGmktGoods paGmktGoods = new PaGmktGoods();
						paGmktGoods.setEsmGoodsCode(targetGoods.get("ESM_GOODS_CODE").toString());
						paGmktGoods.setPaCode(paCode);
						paGmktGoodsService.saveGmktGoodsPriceSaleModifyTx(paGmktGoods);
					}
					
					if (se.getMessage().indexOf("옥션") > 0) {
						paramMap.put("siteGb", "PAG");
						restUtil.getConnection(rest,  paramMap);
						//= Step 3) 통신후 resMap 파싱 & setter
						PaGmktGoods paGmktGoods = new PaGmktGoods();
						paGmktGoods.setEsmGoodsCode(targetGoods.get("ESM_GOODS_CODE").toString());
						paGmktGoods.setPaCode(paCode);
						//= Step 4) table insert
				    	paGmktGoodsService.saveGmktGoodsPriceSaleModifyTx(paGmktGoods);
					}
					
					if (se.getMessage().indexOf("G마켓") > 0) {
						paramMap.put("siteGb", "PAA");
						restUtil.getConnection(rest,  paramMap);
						//= Step 3) 통신후 resMap 파싱 & setter
						PaGmktGoods paGmktGoods = new PaGmktGoods();
						paGmktGoods.setEsmGoodsCode(targetGoods.get("ESM_GOODS_CODE").toString());
						paGmktGoods.setPaCode(paCode);
						//= Step 4) table insert
				    	paGmktGoodsService.saveGmktGoodsPriceSaleModifyTx(paGmktGoods);
					}
				}
			}
			
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			
			if (se.getMessage().indexOf("동일한 상태로의 변경") > 0 || se.getMessage().indexOf("동일한 판매상태로 변경") > 0) {
				PaGmktGoods paGmktGoods = new PaGmktGoods();
				paGmktGoods.setEsmGoodsCode(target.get("esmGoodsCode").toString());
				paGmktGoods.setPaCode(paCode);
				paGmktGoodsService.saveGmktGoodsPriceSaleModifyTx(paGmktGoods);
			}
			//상품수정 성공 후 1초이내에 가격/재고/판매상태 request시 실패 code:500 오류로 중지 처리하지않음 2018.12.18 thjeon
			//savePaGoodsInsertFail(paramMap);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				paGmktAsycController.savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			//log.info("===== 상품 가격/재고/판매상태 수정 API END =====");
			log.info("===== 상품 판매 중지 처리 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 전체 상품명 수정대상 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "전체 상품명 수정대상 조회", notes = "전체 상품명 수정대상 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-goods-name-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putGoodsNameList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String,Object> target = new HashMap<String,Object>();
		int okCnt = 0;
		int totCnt = 0;
		List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGmktGoodsNameModifyList(target);
		totCnt = targetGoodsList.size();
		for(HashMap<String,Object> targetGoods : targetGoodsList){
			ResponseEntity<?> result = putGoodsName(request,targetGoods.get("PA_CODE").toString(),targetGoods.get("GOODS_CODE").toString());
			if(result.getStatusCode().toString().equals("200")){
				okCnt++;
			}
		}
		paramMap.put("code", "200");
		paramMap.put("message", totCnt+" 중 "+okCnt+ " 적용완료 ");
		log.info("상품명 수정 : "+paramMap.get("message"));
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품명 수정
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "상품명 수정", notes = "상품명 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-goods-name", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putGoodsName(HttpServletRequest request,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,			
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode)
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_020";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("siteGb", selectGmktSellerId(paramMap));
		try{			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			//단일타겟만 조회하도록 프로세싱 , 다중 처리는 상위에서 List돌리도록
			HashMap<String,Object> target = new HashMap<String,Object>();
			target.put("paCode", paCode);
			target.put("goodsCode", goodsCode);
			
			List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGmktGoodsNameModifyList(target);
			for(HashMap<String,Object> targetGoods : targetGoodsList){
				paramMap.put("urlParameter",targetGoods.get("ESM_GOODS_CODE"));
				paramMap.put("map", targetGoods);
				//= Step 2) 통신
				//100바이트 이하인 항목에 대해서만 통신요청을 보낸다.
				if(targetGoods.get("GOODS_NAME").toString().getBytes("EUC-KR").length <= 100){
					String response = restUtil.getConnection(rest,  paramMap);
					Map<String,Object> resMap = ComUtil.splitJson(response);
				}
				//= Step 3) 통신후 resMap 파싱 & setter
				PaGmktGoods paGmktGoods = new PaGmktGoods();
				paGmktGoods.setEsmGoodsCode(targetGoods.get("ESM_GOODS_CODE").toString());
				paGmktGoods.setPaCode(paCode);
		    	//= Step 4) table insert
		    	paGmktGoodsService.saveGmktGoodsNameModifyTx(paGmktGoods);
			}
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				paGmktAsycController.savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 상품명 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품 판매자 부담할인 수정 대상 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	/*
	@RequestMapping(value = "/post-seller-discount-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> updateSellerDiscountList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		int okCnt = 0;
		int totCnt = 0;
		List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGmktGoodsPriceList();
		
		totCnt = targetGoodsList.size();
		for(HashMap<String,Object> targetGoods : targetGoodsList){
			if(targetGoods.get("DC_AMT").toString().equals("0")) {
				ResponseEntity<?> result = deleteSellerDiscount(request,targetGoods.get("PA_CODE").toString(),targetGoods.get("GOODS_CODE").toString());
				if(result.getStatusCode().toString().equals("200")){
					okCnt++;
				}
			} else {
				ResponseEntity<?> result = updateSellerDiscount(request,targetGoods.get("PA_CODE").toString(),targetGoods.get("GOODS_CODE").toString());
				if(result.getStatusCode().toString().equals("200")){
					okCnt++;
				}
			}
		}
		paramMap.put("code", "200");
		paramMap.put("message", totCnt+" 중 "+okCnt+ " 적용완료 ");
		log.info("부담할인 수정 : "+paramMap.get("message"));
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	*/
	/**
	 * 상품 판매자 부담할인 수정
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	/*
	@RequestMapping(value = "/post-seller-discount", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> updateSellerDiscount(HttpServletRequest request,
			@RequestParam(value="paCode", required=true) String paCode,			
			@RequestParam(value="goodsCode", required=true) String goodsCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_006";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paCode", paCode);
		paramMap.put("FROM_DATE",DateUtil.addDay(systemService.getSysdatetime(),  0 ));
		
		try{			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 상품번호 get
			
			String esmGoodsNo = paGmktGoodsService.selectEsmGoodsNo(paramMap);
			if(esmGoodsNo == null){
			    paramMap.put("code","404");
			    paramMap.put("message",getMessage("info.nodata.msg"));
			    return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			HashMap<String, Object> goodsPriceMap = paGmktGoodsService.selectGmktGoodsPrice(paramMap);
			if(goodsPriceMap==null){
			    paramMap.put("code","404");
			    paramMap.put("message",getMessage("info.nodata.msg"));
			    return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			if (Double.parseDouble(goodsPriceMap.get("DC_AMT").toString()) <= 0){
				paramMap.put("code","404");
			    paramMap.put("message",getMessage("info.nodata.msg"));
			    return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			paramMap.put("urlParameter", esmGoodsNo);
			paramMap.put("goodsPriceMap", goodsPriceMap);
			
			//= Step 2) 통신
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			
			//= Step 3) 통신후 resMap 파싱 & setter
	    	
	    	//= Step 4) table update
			savePaGmktTransDiscount(paramMap);
			
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 상품 판매자 부담할인 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	*/
	/**
	 * 상품 판매자 부담할인 해제
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	/*
	@RequestMapping(value = "/delete-seller-discount", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> deleteSellerDiscount(HttpServletRequest request,
			@RequestParam(value="paCode", required=true) String paCode,			
			@RequestParam(value="goodsCode", required=true) String goodsCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAGMKTAPI_V2_01_008";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paCode", paCode);
		
		try{			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 상품번호 get
			
			String esmGoodsNo = paGmktGoodsService.selectEsmGoodsNo(paramMap);
			if(esmGoodsNo == null){
			    paramMap.put("code","404");
			    paramMap.put("message",getMessage("info.nodata.msg"));
			    return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			paramMap.put("urlParameter", esmGoodsNo);
			
			//= Step 2) 통신
			
			//할인가격이 0보다 클 경우 체크
			HashMap<String, Object> goodsPriceMap = paGmktGoodsService.selectGmktGoodsPrice(paramMap);
			if(Double.parseDouble(goodsPriceMap.get("DC_AMT").toString()) > 0) {
				paramMap.put("code","404");
			    paramMap.put("message",getMessage("info.nodata.msg"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			String response = restUtil.getConnection(null,  paramMap);
			Map<String,Object> resMap = ComUtil.splitJson(response);
			
			//= Step 3) 통신후 resMap 파싱 & setter
	    	
	    	//= Step 4) table update
			savePaGmktTransDiscount(paramMap);
			
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 상품 판매자 부담할인 해제 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	*/
	
	/**
	 * 전체 이미지 수정대상 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	/*
	@RequestMapping(value = "/post-goods-image-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postGoodsImageList(HttpServletRequest request,
			@RequestParam(value="paCode", required=true) String paCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		int okCnt = 0;
		int totCnt = 0;
		List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGmktGoodsImageModify(paramMap);
		totCnt = targetGoodsList.size();
		for(HashMap<String,Object> targetGoods : targetGoodsList){
			ResponseEntity<?> result = postGoodsImage(request,paCode,targetGoods.get("GOODS_CODE").toString());
			if(result.getStatusCode().toString().equals("200")){
				okCnt++;
			}
		}
		paramMap.put("code", "200");
		paramMap.put("message", totCnt+" 중 "+okCnt+ " 적용완료 ");
		log.info("이미지수정 : "+paramMap.get("message"));
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	*/
	/**
	 * 상품 이미지 수정
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	/*
	@RequestMapping(value = "/post-goods-image", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postGoodsImage(HttpServletRequest request,
			@RequestParam(value="paCode", required=true) String paCode,			
			@RequestParam(value="goodsCode", required=true) String goodsCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_009";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paCode", paCode);
		
		String imageUrl = systemService.getConfig("FLEX_IMG_SERVER_URL").getVal();
		String http = "";
		//운영 FLEX_IMG_SERVER_URL : //1.255.85.245/
		//개발 FLEX_IMG_SERVER_URL : http://1.255.85.245/
		//config : PARTNER_API_IMAGE_PROTOCOL 운영 Y, 개발 N
		if(systemService.getConfig("PARTNER_API_IMAGE_PROTOCOL").getVal().equals("Y")){
			http ="http:";
			imageUrl = imageUrl+"/";
		}
		
		try{			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 상품번호 get
			List<HashMap<String,Object>> requestGoodsImageMapList = paGmktGoodsService.selectGmktGoodsImageModify(paramMap);
			
			for(HashMap<String,Object> requestGoodsImageMap : requestGoodsImageMapList){
				String esmGoodsNo = paGmktGoodsService.selectEsmGoodsNo(paramMap);
				if(esmGoodsNo == null){
				    paramMap.put("code","404");
				    paramMap.put("message",getMessage("info.nodata.msg"));
				    return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
				
				paramMap.put("urlParameter", esmGoodsNo);
				paramMap.put("requestGoodsImageMap", requestGoodsImageMap);
				paramMap.put("imageUrl",imageUrl);
				paramMap.put("http", http);
				
				//= Step 2) 통신
				String response = restUtil.getConnection(rest,  paramMap);
				Map<String,Object> resMap = ComUtil.splitJson(response);
				
				//= Step 3) 통신후 resMap 파싱 & setter
		    	PaGmktGoods paGmktGoods = new PaGmktGoods();
		    	paGmktGoods.setEsmGoodsCode(resMap.get("GoodsNo").toString());
		    	paGmktGoods.setGoodsCode(goodsCode);
		    	paGmktGoods.setPaCode(paCode);
		    	
		    	//= Step 4) table update
		    	paGmktGoodsService.updatePaGmktGoodsImageTx(paGmktGoods); //TPAGOODSIMAGE 테이블 타겟0으로 수정하도록 만들어야 함
			}
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 상품 이미지 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	*/
	/**
	 * 전체 상품 삭제대상 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "전체 상품 삭제대상 조회", notes = "전체 상품 삭제대상 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/delete-goods-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> deleteGoodsList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String,Object> target = new HashMap<String,Object>();
		int okCnt = 0;
		int totCnt = 0;
		List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGmktGoodsDeleteList(target);
		totCnt = targetGoodsList.size();
		for(HashMap<String,Object> targetGoods : targetGoodsList){
			deleteGoods(request,targetGoods.get("PA_CODE").toString(),targetGoods.get("GOODS_CODE").toString());
			okCnt++;
		}
		paramMap.put("code", "200");
		paramMap.put("message", totCnt+" 중 "+okCnt+ " 적용완료 ");
		log.info("상품삭제 : "+paramMap.get("message"));
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품 삭제
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "상품 삭제", notes = "상품 삭제", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/delete-goods", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> deleteGoods(HttpServletRequest request,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,			
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode)
			throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_029";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paCode", paCode);
		
		try{			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 상품번호 get
			
			String esmGoodsNo = paGmktGoodsService.selectEsmGoodsNo(paramMap);
			if(esmGoodsNo == null){
			    paramMap.put("code","404");
			    paramMap.put("message",getMessage("info.nodata.msg"));
			    return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			paramMap.put("urlParameter", esmGoodsNo);
			
			HashMap<String,Object> target = new HashMap<String,Object>();
			target.put("paCode", paCode);
			target.put("goodsCode", goodsCode);
			
			List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGmktGoodsDeleteList(target);
			for(HashMap<String,Object> targetGoods : targetGoodsList){
				//= Step 2) 통신
				String response = restUtil.getConnection(rest,  paramMap);
				Map<String,Object> resMap = ComUtil.splitJson(response);
				
				//= Step 3) 통신후 resMap 파싱 & setter
				PaGmktDelGoodsHis paGmktDelGoodsHis = new PaGmktDelGoodsHis();
				
				paGmktDelGoodsHis.setGoodsCode(targetGoods.get("GOODS_CODE").toString());
				paGmktDelGoodsHis.setPaCode(targetGoods.get("PA_CODE").toString());
				paGmktDelGoodsHis.setSeq(systemService.getMaxNo("TPAGMKTDELGOODSHIS", "SEQ", "GOODS_CODE = '" + targetGoods.get("GOODS_CODE").toString() + "' AND PA_CODE = '"+ targetGoods.get("PA_CODE").toString() +"'", 3));
				paGmktDelGoodsHis.setEsmGoodsCode(targetGoods.get("GOODS_CODE").toString());
				paGmktDelGoodsHis.setItemNo(targetGoods.get("ITEM_NO").toString());
				paGmktDelGoodsHis.setInsertDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
				paGmktDelGoodsHis.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
		    	
		    	//= Step 4) table insert
		    	paGmktGoodsService.savePaGmktGoodsStatusTx(paGmktDelGoodsHis);//TPAGMKTGOODS 테이블 ITEM_NO, ESM_GOODS_CODE NULL로 수정?
			}
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				paGmktAsycController.savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 상품 삭제 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 전체 상품 대상 판매상태 검증 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "전체 상품 대상 판매상태 검증 조회", notes = "전체 상품 대상 판매상태 검증 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/get-goods-price-sale-yn-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getGoodsPriceSaleYnList(HttpServletRequest request,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,			
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode)
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String,Object> target = new HashMap<String,Object>();
		target.put("paCode", paCode);
		target.put("goodsCode", goodsCode);
		int okCnt = 0;
		int totCnt = 0;
		List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGmktGoodsPriceSaleRealTimeList(target);
		totCnt = targetGoodsList.size();
		for(HashMap<String,Object> targetGoods : targetGoodsList){
			ResponseEntity<?> result = getGoodsPriceSaleYn(request,targetGoods.get("PA_CODE").toString(),targetGoods.get("GOODS_CODE").toString());
			if(result.getStatusCode().toString().equals("200")){
				okCnt++;
			}
		}
		paramMap.put("code", "200");
		paramMap.put("message", totCnt+" 중 "+okCnt+ " 적용완료 ");
		log.info("판매상태 검증 : "+paramMap.get("message"));
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 판매상태 조회 - 단일상품
	 * @return ResponseEntity 
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "판매상태 조회 - 단일상품", notes = "판매상태 조회 - 단일상품", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/get-goods-price-sale-yn", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getGoodsPriceSaleYn(HttpServletRequest request,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,			
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode)
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_005";            
		String duplicateCheck = "";
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paCode", paCode);
		
		try{			
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 상품번호 get
			HashMap<String,Object> target = new HashMap<String,Object>();
			target.put("paCode", paCode);
			target.put("goodsCode", goodsCode);
			
			List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGmktGoodsPriceSaleRealTimeList(target);
			for(HashMap<String,Object> targetGoods : targetGoodsList){
				paramMap.put("urlParameter", targetGoods.get("ESM_GOODS_CODE"));
				//= Step 2) 통신
				String response = restUtil.getConnection(rest,  paramMap);
				Map<String,Object> resMap = ComUtil.splitJson(response);
				//= Step 3) 통신후 resMap 파싱 & setter
				Map<String,Object> isSell = (Map<String,Object>)resMap.get("IsSell");
				String gPaSaleFlag = isSell.get("gmkt").toString(); // 지마켓의 현재 상태 ::gPaSaleGb
				String gPaSaleGb = "20"; //판매중상태 
				if(gPaSaleFlag.equals("false")){
					gPaSaleGb = "30"; //판매중지상태
				}
				if(!targetGoods.get("PA_SALE_GB").toString().equals(gPaSaleGb)){
					System.out.println("check point 현재 이상품의 판매상태가 다름");
					System.out.println(goodsCode + " 테이블 상태 :" +targetGoods.get("PA_SALE_GB").toString() + " / G마켓 상태 :"+gPaSaleGb);
					//우리테이블의 상태로 변경처리
					//update trans_sale_yn = 1 처리를 해주어야하나, 개발 중 취소된 프로세스 (TODO)
				}
			}
	    	//= Step 4) table insert
	    	
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				paGmktAsycController.savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 상품 판매상태 검증 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 제휴상품 입점QA (G마켓)
	 */
	@ApiOperation(value = "제휴상품 입점QA (G마켓)", notes = "제휴상품 입점QA (G마켓)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-auto-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsAutoInsert(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String,Object> resultMap = null; 

		try {
			log.info("/gmkt-goods-auto-insert = GMKTGOODS QA");
			resultMap = paGmktGoodsService.procPaGmktAutoInsert(paramMap);
		} catch (Exception e) {
			resultMap.put("out_code", "500");
		}
				
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(resultMap.get("out_code").toString(), resultMap.get("out_msg").toString()), HttpStatus.OK);
	}
	/** 등록실패 후 반려처리/message처리 */
	public void savePaGoodsInsertReject(ParamMap paramMap) throws Exception{
		PaGmktGoods paGmktGoods = new PaGmktGoods();
		paGmktGoods.setGoodsCode(paramMap.getString("goodsCode"));
		paGmktGoods.setPaCode(paramMap.getString("paCode"));
		paGmktGoods.setPaStatus("20");
		paGmktGoods.setReturnNote(paramMap.getString("message"));
		paGmktGoodsService.savePaGmktGoodsInsertRejectTx(paGmktGoods);
		//판매상태API호출은 일괄수정에서..
	}
	/** 판매성공처리/message처리 */
	public void savePaGoodsInsertSuccess(ParamMap paramMap) throws Exception{
		PaGmktGoodsVO paGmktGoods = new PaGmktGoodsVO();
		//paGmktGoods.setPaGroupCode(paramMap.getString("paGroupCode"));
		paGmktGoods.setGoodsCode(paramMap.getString("goodsCode"));
		paGmktGoods.setPaCode(paramMap.getString("paCode"));
		paGmktGoods.setPaSaleGb(paramMap.getString("paSaleGb"));
		paGmktGoods.setReturnNote("");
		paGmktGoods.setIsModifyYn( ComUtil.NVL(paramMap.getString("isModify"), "N"));
		paGmktGoodsService.savePaGmktGoodsInsertSuccessFailTx(paGmktGoods);
		//판매상태API호출은 일괄수정에서..
	}
	/** 판매중지처리/message처리 */
	/* 하나로 관리하기 위해 PaGmktV2AsyncController로 이동
	public void savePaGoodsInsertFail(ParamMap paramMap) throws Exception{
		PaGmktGoods paGmktGoods = new PaGmktGoods();
		//paGmktGoods.setPaGroupCode(paramMap.getString("paGroupCode"));
		paGmktGoods.setGoodsCode(paramMap.getString("goodsCode"));
		paGmktGoods.setPaCode(paramMap.getString("paCode"));
		paGmktGoods.setPaSaleGb("30");
		paGmktGoods.setReturnNote(paramMap.getString("message"));
		paGmktGoodsService.savePaGmktGoodsInsertSuccessFailTx(paGmktGoods);
		//판매상태API호출은 일괄수정에서..
	}
	*/
	/** DISCOUNT_YN 업데이트 */
	/*public void savePaGmktTransDiscount(ParamMap paramMap) throws Exception{
		PaGmktGoods paGmktGoods = new PaGmktGoods();
		paGmktGoods.setGoodsCode(paramMap.getString("goodsCode"));
		paGmktGoods.setPaCode(paramMap.getString("paCode"));
		paGmktGoodsService.savePaGmktTransDiscountTx(paGmktGoods);
	}*/
	/** TRANSLOG(상품) */
	/* 하나로 관리하기 위해 PaGmktV2AsyncController로 이동
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
	*/
	/** SellerId select
	 * 	pa_group_code에 따라
	 *  PAE : 02, 03 둘다 존재
	 *  PAG : 02만 존재
	 *  PAA : 03만 존재
	 * */
	public String selectGmktSellerId(ParamMap paramMap) throws Exception{
		return paGmktGoodsService.selectGmktSellerId(paramMap);
	}
	/** 상품 등록&수정 validation check - 2019.01.22 thjeon refactoring */
	public ParamMap goodsInfoValidation(ParamMap paramMap) throws Exception{
		List<HashMap<String,Object>> requestGoodsList = paGmktGoodsService.selectGmktGoodsInsertOne(paramMap);
		HashMap<String,Object> requestGoodsMap = null;
		
		/** 
		 case 0 : 입점 대상 상품 없음
		 case 1 : G마켓 or 옥션 입점 대상 상품 존재(1row)
		 	PAG일 경우 토큰 G:skstoa
		 	PAA일 경우 토큰 A:skstoa
		 case 2 : G마켓 and 옥션 입점 대상 상품 존재(2row)
		 	PAE일 경우 토큰 G:skstoa,A:skstoa
		 */
		
		if(requestGoodsList.size() == 0) {
			paramMap.put("code","407");
		    paramMap.put("message","해당상품재확인 필요");
		    return paramMap;
		}
		
		//착불이고 FR배송이면 착불 표기
		String shipCostCode = requestGoodsList.get(0).get("SHIP_COST_CODE").toString().substring(0, 2);
		String collectYn = requestGoodsList.get(0).get("COLLECT_YN").toString();

		if ("1".equals(collectYn) /* && "FR".equals(shipCostCode) */) {
			paramMap.put("COLLECT_YN", "1");
		} else {
			paramMap.put("COLLECT_YN", "0");
		}
		
		switch(requestGoodsList.size()){
			case 1:
				requestGoodsMap = requestGoodsList.get(0);
				if(requestGoodsMap.get("PA_GROUP_CODE").toString().equals("02")){
					paramMap.put("siteGb", "PAG");
				}else{
					paramMap.put("siteGb", "PAA");
				}
				break;
			case 2:
				requestGoodsMap = requestGoodsList.get(0); // get(0)에는 지마켓이 와야함 (쿼리로 order by해줌)
				
				if("".equals(requestGoodsList.get(0).get("ESM_GOODS_CODE"))){//옥션이 선입점된 경우.. 덮어씌워준다 2019.03.05
					requestGoodsMap.put("ESM_GOODS_CODE", requestGoodsList.get(1).get("ESM_GOODS_CODE"));
				}
				
				requestGoodsMap.put("SITE_CATEGORY_CODE_EXTRA", requestGoodsList.get(1).get("SITE_CATEGORY_CODE"));//site category code가 다르기에 추가컬럼으로 넘겨서 처리 2019.02.14 thjeon
				requestGoodsMap.put("PA_SALE_GB_EXTRA", requestGoodsList.get(1).get("PA_SALE_GB"));//site category code만 다르기에 추가컬럼으로 넘겨서 처리 2019.02.14 thjeon
				paramMap.put("siteGb", "PAE");
				
				/** G/A중 한쪽만 반려된 경우 자동수정처리에서 타지않게 하기위함 .. 2019.04.04 thjeon*/
				// BO or API --> api일시 반려인 상태값은 pa_sale_gb를 30으로 보낸다.
				if(paramMap.getString("inComingUrl").equals("API")){
					// 지마켓
					if(requestGoodsList.get(0).get("PA_STATUS").toString().equals("20")){
						requestGoodsMap.put("PA_SALE_GB", "30");
						paramMap.put("siteGb", "PAA");
					}
					if(requestGoodsList.get(1).get("PA_STATUS").toString().equals("20")){
						requestGoodsMap.put("PA_SALE_GB_EXTRA", "30");
						paramMap.put("siteGb", "PAG");
					}
				}
				
				/** 옥션 기입점 상태에서 지마켓 추가 입점할 경우 아래의 일괄 수정 API를 통해 등록해야함 */ 
				//지마켓  (입점요청 or 반려상태) and 옥션 입점완료 상태 일경우에 비교
				if( !requestGoodsList.get(0).get("PA_STATUS").toString().equals("30") && requestGoodsList.get(1).get("PA_STATUS").toString().equals("30") ) {
					paramMap.put("apiCode", "IF_PAGMKTAPI_V2_01_003_A");// APICODE 변경
					paGmktGoodsService.updateGmktGoodsdtMappingforGoodsModify(paramMap);// 위 API 이용할 경우 기존 옥션에 등록된 상품옵션들이 삭제되기때문에 옵션수정/등록 API 대상에 포함시켜야함
				}

				break;
		}
		
		if(requestGoodsMap.get("BUNDLE_NO")==null || requestGoodsMap.get("BUNDLE_NO")==""){
			paramMap.put("code","404");
		    paramMap.put("message",getMessage("pa.gmkt_no_bundle"));
		    return paramMap;
		}
		/*
		//2019.01.11 100바이트까지 잘라서 보내도록 처리
		if(requestGoodsMap.get("GOODS_NAME").toString().getBytes("EUC-KR").length > 99){
			paramMap.put("code","404");
			paramMap.put("message",getMessage("pa.over_goods_name_100byte"));
			return paramMap;
		}
		*/
		List<HashMap<String,Object>> requestOfferMapList = paGmktGoodsService.selectGmktGoodsOfferInsertOne(paramMap);
		if(requestOfferMapList.size() == 0){
			log.error("offer is null");
		    paramMap.put("code","404");
		    paramMap.put("message", getMessage("pa.no_found_goods_offer"));
		    return paramMap;
		}
		HashMap<String,Object> requestDescribeMap = paGmktGoodsService.selectGmktGoodsDescribeInsertOne(paramMap);
		if(requestDescribeMap==null){
			log.error("describe is null");
		    paramMap.put("code","404");
		    paramMap.put("message",getMessage("pa.no_found_goods_describe"));
		    return paramMap;
		}
		
		HashMap<String,Object> requestGmktPolicyMap = new HashMap<>();
		HashMap<String,Object> requestIacPolicyMap = new HashMap<>();
		
		switch(paramMap.getString("siteGb")){
			case "PAE":
				paramMap.put("paGroupCode", "02");
				requestGmktPolicyMap = paGmktGoodsService.selectGoodsForGmktPolicy(paramMap);
				if(requestGmktPolicyMap==null){
				    paramMap.put("code","404");
				    paramMap.put("message",getMessage("pa.no_found_policy"));
				    return paramMap;
				}
				paramMap.put("paGroupCode", "03");
				requestIacPolicyMap = paGmktGoodsService.selectGoodsForGmktPolicy(paramMap);
				if(requestIacPolicyMap==null){
				    paramMap.put("code","404");
				    paramMap.put("message",getMessage("pa.no_found_policy"));
				    return paramMap;
				}
				break;
			case "PAG":
				paramMap.put("paGroupCode", "02");
				requestGmktPolicyMap = paGmktGoodsService.selectGoodsForGmktPolicy(paramMap);
				if(requestGmktPolicyMap==null){
				    paramMap.put("code","404");
				    paramMap.put("message",getMessage("pa.no_found_policy"));
				    return paramMap;
				}
				break;
			case "PAA":
				paramMap.put("paGroupCode", "03");
				requestIacPolicyMap = paGmktGoodsService.selectGoodsForGmktPolicy(paramMap);
				if(requestIacPolicyMap==null){
				    paramMap.put("code","404");
				    paramMap.put("message",getMessage("pa.no_found_policy"));
				    return paramMap;
				}
				break;
		}
		//as-is
//		List<HashMap<String,Object>> requestPriceMapList = paGmktGoodsService.selectPaPromoTarget(paramMap);
//		paramMap.put("requestPriceMapList", requestPriceMapList);
		
		//to-be
		List<HashMap<String,Object>> requestGmktPriceMapList = paGmktGoodsService.selectPaGmktPromoTarget(paramMap);
		paramMap.put("requestGmktPriceMapList", requestGmktPriceMapList);
		
		List<HashMap<String,Object>> requestIacPriceMapList = paGmktGoodsService.selectPaIacPromoTarget(paramMap);
		paramMap.put("requestIacPriceMapList", requestIacPriceMapList);
		
		
		//이베이 상품명 제한 문자 조회
		paramMap.put("paGoodsLimitCharMapList", paGmktGoodsService.selectGoodsLimitCharList(paramMap));
		
		
		paramMap.put("requestGoodsMap", requestGoodsMap);
		paramMap.put("requestOfferMapList", requestOfferMapList);
		paramMap.put("requestDescribeMap", requestDescribeMap);
		paramMap.put("requestGmktPolicyMap", requestGmktPolicyMap);
		paramMap.put("requestIacPolicyMap", requestIacPolicyMap);
		//paramMap.put("requestPriceMapList", requestPriceMapList);
		String imageUrl = systemService.getConfig("FLEX_IMG_SERVER_URL").getVal();
		String http = "";
		//운영 FLEX_IMG_SERVER_URL : //1.255.85.245
		//개발 FLEX_IMG_SERVER_URL : http://1.255.85.245/
		//config : PARTNER_API_IMAGE_PROTOCOL 운영 Y, 개발 N
		if(systemService.getConfig("PARTNER_API_IMAGE_PROTOCOL").getVal().equals("Y")){
			http ="http:";
			imageUrl = imageUrl+"/";
		}
		paramMap.put("imageUrl",imageUrl);
		paramMap.put("http", http);
		
		if(paramMap.getString("isModify").equals("Y")){
			paramMap.put("urlParameter",requestGoodsMap.get("ESM_GOODS_CODE"));
		}
		
		paGmktGoodsService.checkRetentionGoodsModify(paramMap); //리텐션 API_CODE 처리
		
		paramMap.put("code","200");
	    paramMap.put("message","OK");
		return paramMap;
	}
	
	/**
	 * ESM 상품 판매종료 기한 업데이트
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "ESM 상품 판매종료 기한 업데이트", notes = "ESM 상품 판매종료 기한 업데이트", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/get-sales-day-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getSalesDayModify(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		List<HashMap<String,Object>> targetGoodsList = new ArrayList<>();
		String finishGoodsCodes = "";
		
		String apiCode = "IF_PAGMKTAPI_V2_05_008";            
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");

		try{			

			log.info("===== IF_PAGMKTAPI_V2_05_008 START =====");
			
			targetGoodsList = paGmktGoodsService.selectSalesDayModifyTarget();
			
			if(targetGoodsList.size() > 0){
				for(HashMap<String,Object> targetGoods : targetGoodsList){
					
					paramMap.put("goodsCode", targetGoods.get("GOODS_CODE"));
					paramMap.put("paCode", targetGoods.get("PA_CODE"));
					paramMap.put("paGroupCode", targetGoods.get("PA_GROUP_CODE"));
					paramMap.put("urlParameter", targetGoods.get("ESM_GOODS_CODE"));
					
					//통신
					String response = restUtil.getConnection(rest,  paramMap);
					Map<String,Object> resMap = ComUtil.splitJson(response);					
					
					//Response
					Map<String,Object> itemBasicInfo = (Map<String,Object>)resMap.get("itemBasicInfo");
					Map<String,Object> sellingPeriod = (Map<String,Object>)itemBasicInfo.get("SellingPeriod");
					
					String salesDay = sellingPeriod.get("gmkt").toString(); // 지마켓의 현재 상태 ::판매 기한
					
					if(salesDay != null && !"".equals(salesDay)){
						
						Date esmSaleDay = dateFormat.parse(salesDay);
						
						paramMap.put("esmSaleDay", salesDay);
						if ( esmSaleDay.compareTo(date) > 0) {
							
							long diffDay = (esmSaleDay.getTime() - date.getTime()) / (24*60*60*1000);
							
							if ( diffDay < 8 ) {
								paramMap.put("targetFlag", "1");
							} else {
								paramMap.put("targetFlag", "0");
							}
							
							if ( paGmktGoodsService.updateGmktGoodsforSalesDayModify(paramMap) < 0 ) {
								log.error("getSalesDayModify update Error goodsCode : " + targetGoods.get("GOODS_CODE"));
							} else {
								finishGoodsCodes += " || " + targetGoods.get("GOODS_CODE");
							}
							
						}
					}
				}
			}
			
			log.info("===== IF_PAGMKTAPI_V2_05_008 END ===== TARTGET SIZE : " + targetGoodsList.size());
			
		} catch (Exception se) {

			String tagerGoodsCodes = "";
			log.error("c == getSalesDayModify Error");
			
			if(targetGoodsList.size() > 0){
				for(HashMap<String,Object> targetGoods : targetGoodsList){
					tagerGoodsCodes += " || " + targetGoods.get("GOODS_CODE");
				}
			}
			
			log.error("== targetGoods : " + tagerGoodsCodes);
			log.error("== finishGoodsCodes : " + finishGoodsCodes);
				
		}finally {
			try{
			}catch(Exception e){
				String tagerGoodsCodes = "";
				log.error("f == getSalesDayModify Error");
				
				if(targetGoodsList.size() > 0){
					for(HashMap<String,Object> targetGoods : targetGoodsList){
						tagerGoodsCodes += " || " + targetGoods.get("GOODS_CODE");
					}
				}
				
				log.error("== targetGoods : " + tagerGoodsCodes);
				log.error("== finishGoodsCodes : " + finishGoodsCodes);
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * G마켓 팔자주문 처리(for 수수료율 변경)리스트 조회
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "G마켓 팔자주문 처리(for 수수료율 변경)리스트 조회", notes = "G마켓 팔자주문 처리(for 수수료율 변경)리스트 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-goods-selling-order-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putGoodsSellingOrderList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String,Object> target = new HashMap<String,Object>();
		
		// 타켓 건수 적용
		String targetCnt = paGmktGoodsService.selectCodeContentForTargetCnt("IF_PAGMKTAPI_V2_01_037_L");
		if(targetCnt != null && !"".equals(targetCnt)){
			target.put("targetCnt", Integer.parseInt(targetCnt));
		}
		
		int okCnt = 0;
		int totCnt = 0;
		List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGmktGoodsSellingOrderList(target);
	
		if(targetGoodsList.size() > 0){
			totCnt = targetGoodsList.size();
			for(HashMap<String,Object> targetGoods : targetGoodsList){
				ResponseEntity<?> result = putGoodsSellingOrder(request,targetGoods.get("PA_CODE").toString(),targetGoods.get("GOODS_CODE").toString());
				if(result.getStatusCode().toString().equals("200")){
					okCnt++;
				}
			}
		}
		
		paramMap.put("code", "200");
		paramMap.put("message", totCnt+" 중 "+okCnt+ " 적용완료 ");
		log.info("G마켓 팔자주문 처리 : "+paramMap.get("message"));
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * G마켓 팔자주문 처리(for 수수료율 변경)
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "G마켓 팔자주문 처리(for 수수료율 변경)", notes = "G마켓 팔자주문 처리(for 수수료율 변경)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-goods-selling-order", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putGoodsSellingOrder(HttpServletRequest request,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,			
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode)
		throws Exception{
		
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_037";            
		String duplicateCheck = "";
		HashMap<String,Object> target = new HashMap<String,Object>();
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("siteGb", selectGmktSellerId(paramMap));
		try{
			
			log.info("===== G마켓 팔자주문 처리 API START =====");
			
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) select 테이블 후 urlParameter 셋팅
			target.put("paCode", paCode);
			target.put("goodsCode", goodsCode);
			
			HashMap<String,Object> targetGoods = paGmktGoodsService.selectGmktGoodsSellingOrder(target);
			
			if(targetGoods != null){
				
				paramMap.put("urlParameter",targetGoods.get("ESM_GOODS_CODE"));
				paramMap.put("map", targetGoods);
				
				//= Step 2) 통신
				String response = restUtil.getConnection(rest, paramMap);
				Map<String,Object> resMap = ComUtil.splitJson(response);
				//= Step 3) 통신후 resMap 파싱 & setter
				PaGmktGoods paGmktGoods = new PaGmktGoods();
				paGmktGoods.setGoodsCode(goodsCode);
				paGmktGoods.setPaCode(paCode);
				//= Step 4) table insert
		    	paGmktGoodsService.saveGmktGoodsSellingOrderTx(paGmktGoods);
		    	
		    	paramMap.put("message", "OK");
			}else{
				paramMap.put("message", "판매중지");
				savePaGoodsSellingOrderInsertFail(paramMap);
			}
			
			paramMap.put("code", "200");
			
		} catch (Exception se) {
			
			CommonUtil.dealException(se, paramMap);
			if(!duplicateCheck.equals("1")){ //중복실행처리 여부 확인
				savePaGoodsSellingOrderInsertFail(paramMap);
				log.error(paramMap.getString("message"), se);
			}else{
				log.info(paramMap.getString("message"), se);
			}
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			
		}finally {
			try{
				paGmktAsycController.savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== G마켓 팔자주문 처리 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/** G마켓 팔자주문 처리 fail message처리 */
	public void savePaGoodsSellingOrderInsertFail(ParamMap paramMap) throws Exception{
		PaGmktGoods paGmktGoods = new PaGmktGoods();
		paGmktGoods.setGoodsCode(paramMap.getString("goodsCode"));
		paGmktGoods.setPaCode(paramMap.getString("paCode"));
		paGmktGoods.setReturnNote(paramMap.getString("message"));
		paGmktGoodsService.savePaGoodsSellingOrderInsertFailTx(paGmktGoods);
	}
	
	/**
	 * G9 전시여부 설정
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "G9 전시여부 설정", notes = "G9 전시여부 설정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-g9-display-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postG9DisplayList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();
		String apiCode = "IF_PAGMKTAPI_V2_01_021";            
		String duplicateCheck = "";
		
		HashMap<String,Object> target = new HashMap<String,Object>();
		//target.put("paCode", "21");
		int okCnt = 0;
		int totCnt = 0;
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectG9DisplayTransList(target);
			totCnt = targetGoodsList.size();
			for(HashMap<String,Object> targetGoods : targetGoodsList){
				
				paramMap.put("paCode", targetGoods.get("PA_CODE").toString());
				paramMap.put("goodsCode", targetGoods.get("GOODS_CODE").toString());
				paramMap.put("esmGoodsCode", targetGoods.get("ESM_GOODS_CODE").toString());
				paramMap.put("urlParameter",targetGoods.get("ESM_GOODS_CODE"));
				paramMap.put("map", targetGoods);
				try {
					String response = restUtil.getConnection(rest,  paramMap);
					Map<String,Object> resMap = ComUtil.splitJson(response);
					
					if(targetGoods.get("ESM_GOODS_CODE").toString().equals(resMap.get("goodsNo").toString())) {
						
						rtnMsg = paGmktGoodsService.saveGmktG9GoodsDisplay(paramMap);
						
						if(rtnMsg.equals(Constants.SAVE_SUCCESS)){
							okCnt++;
							paramMap.put("code","200");
							paramMap.put("message","전시처리완료");
						}else {
							paramMap.put("code","500");
							paramMap.put("message","전시처리실패");
						}
					} 
				} catch (Exception se) {
					//이미 등록되어 에러메세지주는 경우 g9_trans_yn 0 처리
					if(se.getMessage().replace(" ","").indexOf("이미G9사이트에등록된") >= 0) {
						paramMap.put("g9DisplayYn","1");
						rtnMsg = paGmktGoodsService.saveGmktG9GoodsDisplay(paramMap);
						
						if(rtnMsg.equals(Constants.SAVE_SUCCESS)){
							okCnt++;
							paramMap.put("code","200");
							paramMap.put("message","전시처리완료");
						}else {
							paramMap.put("code","500");
							paramMap.put("message","전시처리실패");
						}
					}else if(se.getMessage().replace(" ","").indexOf("무료배송비상품만") >= 0) {
						paramMap.put("g9DisplayYn","0");
						rtnMsg = paGmktGoodsService.saveGmktG9GoodsDisplay(paramMap);
						
						if(rtnMsg.equals(Constants.SAVE_SUCCESS)){
							okCnt++;
							paramMap.put("code","200");
							paramMap.put("message","전시처리완료");
						}else {
							paramMap.put("code","500");
							paramMap.put("message","전시처리실패");
						}
					}
					
					CommonUtil.dealException(se, paramMap);
					log.error(paramMap.getString("message"), se);
				} finally {
					paGmktAsycController.savePaGoodsTransLog(paramMap);
				}
			}
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				if(totCnt == okCnt){
					paramMap.put("code","200");
				} else {
					paramMap.put("code","500");
				}
				paramMap.put("message", "대상건수:" + totCnt + ", 성공건수:" + okCnt);
				
				CommonUtil.dealSuccess(paramMap, request);
				
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== G9전시 API END =====");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 *  리텐션 정책으로 인한 자동 상품 기간 연장
	 *  리텐션 정책 : 판매중지  1개월 후 판매 재개시 ESM에서 스토아 상품 정보 삭제됨
	 *  해당 문제를 해결하기 위해 지마켓에서 가이드로 1달 지나기 전에 잠깐 상품을 재개 하고 다시 중지하라고 함..;;
	 *  2022-01-18 : 기존에 판매중지일 (TPAGMKTGOODS.SALE_STOP_DATE) 를 관리하고 있지 않기 때문에 일단은 판매중지일 관리 후 1달 지나서 테스트 후 적용 필요 HSBAEK TODO
	 * **/
	@ApiOperation(value = "리텐션 정책으로 인한 자동 상품 기간 연장", notes = "리텐션 정책으로 인한 자동 상품 기간 연장", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/pagmktv2/goods/put-goods-extend-retention", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> extendRetention(HttpServletRequest request,
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=false) String paCode,			
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=false) String goodsCode)
		throws Exception{
		
		ParamMap paramMap 		= new ParamMap();
		String duplicateCheck 	= "";
		String apiCode 			= "IF_PAGMKTAPI_V2_01_038";
		PaGmktAbstractRest rest = new PaGmktGoodsRest();

		paramMap.put("apiCode"		, apiCode);
		paramMap.put("startDate"	, systemService.getSysdatetimeToString());
		paramMap.put("goodsCode"	, goodsCode);
		paramMap.put("siteGb"		, selectGmktSellerId(paramMap));
		paramMap.put("code"			, "200");
		paramMap.put("message"		, "OK");
			
		try{
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			List<HashMap<String,Object>> targetGoodsList = paGmktGoodsService.selectGmktGoodsRetentionExtendList(paramMap);
			
			for(HashMap<String ,Object> target : targetGoodsList) {
				
				try {
					paramMap.put("esmGoodsCode"	, target.get("ESM_GOODS_CODE"));
					paramMap.put("urlParameter"	, target.get("ESM_GOODS_CODE"));
					paramMap.put("map"			, target);
					
					paramMap.put("isSell", "true");
					restUtil.getConnection(rest,  paramMap); //판매재개
					paramMap.put("isSell", "false");
					restUtil.getConnection(rest,  paramMap); //판매중지 
					
					paGmktGoodsService.updateGmktSaleStopDate(paramMap); //UPDATE TPAGMKTGOODS.SALE_STOP_DATE = SYSDATE
					
				}catch (Exception e) {
					log.error(e.toString()); //TODO 어떤 케이스들로 인해 문제가 발생하는지 확인 해 봐야함.. 예를들어 "동일 상태로 변경할 수 없습니다." 등등
					continue;
				}
			}
			
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 *  이베이 상품 유효기간 만료된 건, 수정API 호출전에 해당 API 호출하여 유효기간 갱신 처리 
	 *  이베이 상품 유효기간 갱신 API 
	 *  에러가 나더라도 무시하고, 익셉션 던지지 않음
	 * **/
	@ApiOperation(value = "이베이 상품 유효기간 갱신 API", notes = "이베이 상품 유효기간 갱신 API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-goods-expiry-date", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putGoodsExpiryDate(HttpServletRequest request,		
		@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=false) String paCode,			
		@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=false) String goodsCode) {
			
		log.info("===== 이베이 상품 유효기간 갱신 API START =====");
		
		ParamMap paramMap = new ParamMap();
		PaGmktAbstractRest rest = new PaGmktGoodsRest();				
		String apiCode = "IF_PAGMKTAPI_V2_01_039";
		
		try{
			log.info("유효기간 갱신 GOODS_CODE : " + goodsCode);
			paramMap.put("apiCode", apiCode);
			paramMap.put("paCode", paCode);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("goodsCode", goodsCode);
//			paramMap.put("siteGb", selectGmktSellerId(paramMap));
			String siteGb = paGmktGoodsService.getGmktSiteGb(paCode, goodsCode, "SellingPeriod"); //selectGmktSellerId + 상품명 조회를 통해 
			if(siteGb.equals("999")) {
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			paramMap.put("siteGb", siteGb);
			
			HashMap<String,Object> targetGoods = new HashMap<String,Object>();	
			targetGoods.put("paCode", paCode);
			targetGoods.put("goodsCode", goodsCode);
			targetGoods = paGmktGoodsService.selecGoodsExpiry(targetGoods);
		
			paramMap.put("urlParameter",targetGoods.get("ESM_GOODS_CODE"));
			paramMap.put("map", targetGoods);
			paramMap.put("isSell", "true");
							
			restUtil.getConnection(rest,  paramMap);
			
			paGmktGoodsService.updateGoodsExpiryDateTx(paramMap); //유효기간 업데이트
			
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.info("유효기간 갱신 ERROR!! GOODS_CODE : " + goodsCode);
		} finally {
			try{
				paGmktAsycController.savePaGoodsTransLog(paramMap);
				CommonUtil.dealSuccess(paramMap, request);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			log.info("===== 이베이 상품 유효기간 갱신 API  END =====");
		}		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
}
