package com.cware.api.pacopn.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
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
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.CopnGoodsDeleteVO;
import com.cware.netshopping.domain.PaCopnGoodsVO;
import com.cware.netshopping.domain.PaCopnGoodsdtMappingVO;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaCopnGoodsAttri;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacopn.goods.service.PaCopnGoodsService;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "/pacopn/goods", description="상품 등록/수정")
@Controller("com.cware.api.pacopn.PaCopnGoodsController")
@RequestMapping(value = "/pacopn/goods")
public class PaCopnGoodsController extends AbstractController{
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pacopn.goods.paCopnGoodsService")
	private PaCopnGoodsService paCopnGoodsService;
	
	@Resource(name = "com.cware.api.pacopn.paCopnCommonController")
	private PaCopnCommonController paCopnCommonController;
	
	@Resource(name = "com.cware.api.pacopn.paCopnAsyncController")
	private PaCopnAsyncController paCopnAsyncController;
	
	
	@ApiIgnore
	@ApiOperation(value = "테스트", notes = "테스트", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/testtesttest123", method = RequestMethod.GET)
	@ResponseBody	
	public void testFunction(HttpServletRequest request,
			@ApiParam(name = "goodsCode"		, value = "상품코드"		, defaultValue = "") @RequestParam(value="goodsCode", required = false)  String goodsCode,
			@ApiParam(name = "paCode"			, value = "제휴사코드"		, defaultValue = "") @RequestParam(value = "paCode"	, required = true)   String paCode
			) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		
		paramMap.put("goodsCode"	, goodsCode);
		paramMap.put("paCode"		, paCode);
		
		List<PaPromoTarget> asyncPromoTargetList = paCopnGoodsService.selectPaPromoTarget(paramMap);
		
		for(PaPromoTarget p : asyncPromoTargetList) {
			System.out.println(p.getPaCode() +"  " + p.getGoodsCode() +"  " +  p.getPromoNo() + "  " + p.getDoAmt());
		}
		
	}
	
	
	@ApiOperation(value = "상품등록", notes = "상품등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	
	@RequestMapping(value = "/legacy-goods-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsInsert(HttpServletRequest request,
			@ApiParam(name = "goodsCode"		, value = "상품코드"		, defaultValue = "") @RequestParam(value="goodsCode", required = false)  String goodsCode,
			@ApiParam(name = "paCode"			, value = "제휴사코드"		, defaultValue = "") @RequestParam(value = "paCode"	, required = true)   String paCode,
			@ApiParam(name = "procId"			, value = "처리자ID"		, defaultValue = "") @RequestParam(value = "procId"	, required = false,  defaultValue = "PACOPN") String procId,
			@ApiParam(name = "searchTermGb"		, value = "API중복체크"	, defaultValue = "") @RequestParam(value = ""		, required = false)  String searchTermGb	,
			@ApiParam(name = "massTargetYn"		, value = "대량입점"	, defaultValue = "") 	 @RequestParam(value = ""		, required = false)  String massTargetYn
			) throws Exception{

		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		String  duplicateCheck = "";
		JsonObject responseObject = null;
		
		String apiKeys[] = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		String respMsg = "";
		String sellerProductId = "";
		
		ResponseEntity<?> responseMsg = null;
		
		PaCopnGoodsVO paCopnGoods = null;
        List<PaCopnGoodsAttri> copnGoodsAttri = null;
        List<PaGoodsOfferVO> goodsOffer = null;
        List<PaGoodsdtMapping> goodsdtMapping = null;
        List<PaPromoTarget> paPromoTargetList = null;//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
		List<HashMap<String, Object>> paGoodsLimitCharMapList = null;  //검색어 특수문자 치환
        
        PaGoodsTransLog paGoodsTransLog = null;
        
        PaEntpSlip entpSlip = null;
        
        HashMap<String, String> apiInfo = new HashMap<String, String>();
        
        String prg_id = "IF_PACOPNAPI_01_001";
        String request_type = "POST";
        
        if("".equals(massTargetYn) || massTargetYn == null) {
        	massTargetYn = "0";
        }
        
        try{
        	log.info("===== 상품등록 API Start=====");
        	log.info("01.API 기본정보 세팅");
        	
        	dateTime = systemService.getSysdatetimeToString();
        	
        	paramMap.put("apiCode", prg_id);
        	paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
        	paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
        	paramMap.put("startDate", dateTime);
        	paramMap.put("modCase", Constants.PA_COPN_MOD_CASE_INSERT);
        	if(paCode.equals(Constants.PA_COPN_BROAD_CODE)){
    			paramMap.put("paName", Constants.PA_BROAD);
    			paramMap.put("paCopnLoginId", ConfigUtil.getString("PA_COPN_BROAD_LOGIN_ID"));
    		} else {
    			paramMap.put("paName", Constants.PA_ONLINE);
    			paramMap.put("paCopnLoginId", ConfigUtil.getString("PA_COPN_ONLINE_LOGIN_ID"));
    		}
        	
        	log.info("02.API 중복실행검사");
            //= 중복 실행 Check
        	if(!"1".equals(searchTermGb)) {
    		    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
    			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
        	}
        	
		    log.info("03.SK stoa 상품정보 조회");
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			
			//상품 출고지, 회수지 담당자 주소 체크
			entpSlip = paCopnGoodsService.selectPaCopnEntpSlip(paramMap);
			if(entpSlip != null){
				//출고지 등록
				responseMsg = paCopnCommonController.entpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaCode(), searchTermGb);
				log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
					paramMap.put("code", "440");
					paramMap.put("message", getMessage("pa.check_entp_slip_man_seq", new String[]{" 상품코드 : " + goodsCode}));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			}
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";");
			
			//상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와  수정대상조회 시작시간을 비교함
			String goodsListTime = systemService.getSysdatetimeToString();
			paramMap.put("dateTime"			, dateTime);
			paramMap.put("goodsListTime"	, goodsListTime);
			paramMap.put("massTargetYn"		, massTargetYn);
			
			//상품정보 조회
			paCopnGoods = paCopnGoodsService.selectPaCopnGoodsInfo(paramMap);
						
			if(paCopnGoods == null){
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			} else {
				paGoodsLimitCharMapList = paCopnGoodsService.selectGoodsLimitCharList(paramMap);

				//검색어 특수문자 치환
				if(paCopnGoods.getKeyWord() != null) {
					String keyWord = paCopnGoods.getKeyWord();
					if(paGoodsLimitCharMapList != null){
						for(HashMap<String, Object> paGoodsLimitCharMap : paGoodsLimitCharMapList){
							String replaceChar = paGoodsLimitCharMap.get("REPLACE_CHAR").toString() == null ? "" : paGoodsLimitCharMap.get("REPLACE_CHAR").toString();
							keyWord = keyWord.replaceAll(paGoodsLimitCharMap.get("LIMIT_CHAR").toString(), replaceChar);
						}
					}
					keyWord = keyWord.trim();
					keyWord = keyWord.replaceAll("   ", " ");
					keyWord = keyWord.replaceAll("  ",  " ");
					keyWord = keyWord.replaceAll(" ",   " ");
					paCopnGoods.setKeyWord(keyWord);
				}
			}
			
			//기술서
			
			String goodsCom = "";
			
			goodsCom = (!ComUtil.NVL(paCopnGoods.getGoodsCom()).equals("")) ? ("<div style=\"line-height: 2.0em; font-family: 'NanumBarunGothic'; font-size: 19px;\"><div><h4>&middot;&nbsp;상품구성<h4><h4>" + paCopnGoods.getGoodsCom() + "<h4></div></div>") : "";
			
			if("".equals(paCopnGoods.getCollectImage()) || paCopnGoods.getCollectImage() == null) {
				paCopnGoods.setDescribeExt("<div align='center'><img alt='' src='" + paCopnGoods.getTopImage() + "' /><br /><br /><br />"	//상단 이미지
			  + goodsCom	//상품 구성
			  + paCopnGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + paCopnGoods.getBottomImage() + "' /></div>");	//기술서 + 하단 이미지				
			}else {
				paCopnGoods.setDescribeExt("<div align='center'><img alt='' src='" + paCopnGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + paCopnGoods.getCollectImage() + "' /><br /><br /><br />"	//상단 이미지 + 착불 이미지 
			  + goodsCom	//상품 구성
			  + paCopnGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + paCopnGoods.getBottomImage() + "' /></div>");	//기술서 + 하단 이미지
			}
			
			paramMap.put("paGroupCode", paCopnGoods.getPaGroupCode());
			
			if(paCopnGoods.getSellerProductId() != null){
				paramMap.put("code", "410");
				paramMap.put("message", getMessage("pa.already_insert_goods", new String[] {"쿠팡 상품코드 : " + paCopnGoods.getSellerProductId()}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			if(paCopnGoods.getDescribeExt() == null){
				paramMap.put("code", "420");
				paramMap.put("message", getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			//정보고시 조회
			goodsOffer = paCopnGoodsService.selectPaCopnGoodsOfferList(paramMap);
			if(goodsOffer.size() < 1){
				paramMap.put("code", "430");
				paramMap.put("message", getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			//쿠팡 매핑 옵션 조회
			copnGoodsAttri = paCopnGoodsService.selectPaCopnGoodsAttriList(paramMap);
			//SK스토아 단품 옵션 조회
			goodsdtMapping = paCopnGoodsService.selectPaCopnGoodsdtInfoList(paramMap);
			//프로모션 조회 
			paPromoTargetList = paCopnGoodsService.selectPaPromoTarget(paramMap);//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
			
			//쿠팡 상품 등록 시작
			
			JsonObject root = new JsonObject();
			root.add("insert", createProductJson(paCopnGoods, goodsdtMapping, copnGoodsAttri, goodsOffer, paPromoTargetList, apiKeys[0], paramMap.getString("paCopnLoginId"), paramMap));
			
			log.info("04.상품등록 API 호출");
			//log.info("입력 DATA : " + new GsonBuilder().create().toJson(root.get("insert")));
			responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0])), request_type, new GsonBuilder().create().toJson(root.get("insert")));
		    
		    if(responseObject != null){
		    	respMsg = responseObject.get("code").getAsString();
		    	log.info("connect respMsg  : "+respMsg);
		    	
		    	//쿠팡상품 생성 결과
		    	if("SUCCESS".equals(respMsg)){
		    		paramMap.put("code", "200");
		    		paramMap.put("message", respMsg);
		    		
		    		sellerProductId = responseObject.get("data").getAsString();
		    		paramMap.put("sellerProductID", sellerProductId);
		    		
		    		//전송관리 테이블 저장
		    		paGoodsTransLog = new PaGoodsTransLog();
		    		paGoodsTransLog.setGoodsCode(paramMap.getString("goodsCode"));
		    		paGoodsTransLog.setPaCode(paramMap.getString("paCode"));
		    		paGoodsTransLog.setItemNo(paramMap.getString("sellerProductID").isEmpty()?paramMap.getString("goodsCode"):paramMap.getString("sellerProductID"));
		    		paGoodsTransLog.setRtnCode(paramMap.getString("code"));
		    		paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
		    		paGoodsTransLog.setSuccessYn(paramMap.getString("code").equals("200")==true?"1":"0");
		    		paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		    		paGoodsTransLog.setProcId(procId);
		    		
		    		paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
		    		
		    		if(copnGoodsAttri.size() != 0){
		    			copnGoodsAttri.get(0).setModifyId(procId);
		    			copnGoodsAttri.get(0).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		    			paCopnGoodsService.savePaCopnGoodsAttriTx(copnGoodsAttri);
		    			
		    			if(!rtnMsg.equals("000000")){
		    				paramMap.put("code", "404");
		    				paramMap.put("message", rtnMsg);
		    				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		    			} else {
		    				paramMap.put("code", paramMap.getString("code"));
		    				paramMap.put("message", paramMap.getString("message"));
		    			}
		    		}
		    		if(!sellerProductId.equals("")){
		    			//TPACOPNGOODS에 결과 UPDATE
		    			paCopnGoods.setSellerProductId(responseObject.get("data").getAsString());
		    			paCopnGoods.setPaStatus("30"); //입점완료
		    			paCopnGoods.setInsertId(procId);
		    			paCopnGoods.setModifyId(procId);
		    			paCopnGoods.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paCopnGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paCopnGoods.setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));
						
						log.info("05.제휴사 상품정보 저장");
						rtnMsg = paCopnGoodsService.savePaCopnGoodsTx(paCopnGoods, goodsdtMapping, paPromoTargetList);
						
						if(!rtnMsg.equals("000000")){
							paramMap.put("code", "404");
							paramMap.put("message", rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code",paramMap.getString("code"));
							paramMap.put("message", paramMap.getString("message"));
						}
		    		} else {
		    			paramMap.put("code", "500");
		    			paramMap.put("message", respMsg);
		    		}
		    	} else {
		    		paramMap.put("code", "500");
		    		paramMap.put("message", responseObject.get("message").getAsString());
		    		
		    		//정보고시 에러
		    		if(responseObject.get("message").getAsString().indexOf("고시정보") > -1) {
		    			paCopnGoods.setPaSaleGb("30"); //판매중지
		    			paCopnGoods.setPaStatus("20"); //반려
		    			paCopnGoods.setReturnNote("해당 카테고리에 사용할 수 없는 정보고시 입니다.");
		    			paCopnGoods.setModifyId(procId);
						paCopnGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						log.info("05.제휴사 상품정보 저장");
						rtnMsg = paCopnGoodsService.savePaCopnGoodsFailTx(paCopnGoods);
		    		}
		    	}
		    } else {
		    	paramMap.put("code", "500");
		    	paramMap.put("message", "no data found");
		    	return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		    }
        }catch(Exception e){
        	if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
        }finally{
        	try{
				paramMap.put("resultCode", ComUtil.NVL(paramMap.getString("code"),"500"));
				paramMap.put("resultMsg", ComUtil.NVL(paramMap.getString("message")," "));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
        	if(!"1".equals(searchTermGb)) {
        		if(duplicateCheck.equals("0")){
    				systemService.checkCloseHistoryTx("end", prg_id);
    			}	
        	}
			log.info("===== 상품등록 API End=====");
        }

        return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	private JsonObject createProductJson(PaCopnGoodsVO paCopnGoods, List<PaGoodsdtMapping> paGoodsDtMappingList,
			List<PaCopnGoodsAttri> paCopnGoodsAttriList, List<PaGoodsOfferVO> paGoodsOfferList, List<PaPromoTarget> paPromoTargetList, String vendorId, String loginId,
			ParamMap infoParam) throws Exception{
		
		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		
		String image_address = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
		
		JsonArray jItems = new JsonArray();
		JsonArray retrieveItems = new JsonArray();
		JsonObject jProduct = null;
		JsonObject oItems = null;
		JsonArray jCertifications = null;
		JsonObject oCertifications = null;
		JsonArray jImages = null;
		JsonObject oPImages = null;
		JsonObject oApImages = new JsonObject();
		JsonArray jSearchTags = null;
		JsonArray jNotices = null;
		JsonObject oNotices = null;
		JsonArray jAttributes = null;
		JsonObject oAttributes = null;
		JsonArray jContents = null;
		JsonObject oContents = null;
		JsonArray jContentDetails = null;
		JsonObject oContentDetails = null;
		int imageCount = 1;
		String mappingValue [] = null;
		String optionValue = "";
		String brand = "";
		String keyWord = "";
		String keyAry [] = null;
		PaGoodsdtMapping paGoodsdtMapping = null;
		PaGoodsOfferVO paCopnGoodsOffer = null;
		PaCopnGoodsAttri paCopnGoodsAttri = null;
		
		String optionName = "";
		
		brand = paCopnGoods.getBrandName();
		
		if(paCopnGoods.getSellerProductId() != null){//상품 수정시 조회 API실행 후 변경되는 부분만 수정한다.
			
			String prg_id = "IF_PACOPNAPI_01_002";
			String dateTime = systemService.getSysdatetimeToString();
			ParamMap paramMap = new ParamMap();
			paramMap.put("apiCode", prg_id);
	    	paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
	    	paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
	    	paramMap.put("startDate", dateTime);
	    	if(paCopnGoods.getPaCode().equals(Constants.PA_COPN_BROAD_CODE)){
				paramMap.put("paName", Constants.PA_BROAD);
			} else {
				paramMap.put("paName", Constants.PA_ONLINE);
			}
	    	HashMap<String, String> apiInfo = new HashMap<String, String>();
	    	apiInfo = systemService.selectPaApiInfo(paramMap);
	    	apiInfo.put("apiInfo", paramMap.getString("apiCode"));
	    	JsonObject responseObject = null;
	    	//상품조회 API
	    	responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#sellerProductId#", paCopnGoods.getSellerProductId())));
	    	if(responseObject != null){
	    		if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())){
	    			jProduct = responseObject.get("data").getAsJsonObject();
	    			retrieveItems = responseObject.get("data").getAsJsonObject().get("items").getAsJsonArray();
	    		} else {
					paramMap.put("code","500");
					paramMap.put("message",responseObject.get("message"));
					return jProduct; //에러.
				}
	    	} else {
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return jProduct; //에러.
			}
			//jProduct.addProperty("sellerProductId", paCopnGoods.getSellerProductId());
		} else {
			jProduct = new JsonObject();			
		}
		jProduct.addProperty("displayCategoryCode", paCopnGoods.getDisplayCategoryCode());
		jProduct.addProperty("sellerProductName", paCopnGoods.getDisplayProductName());
		jProduct.addProperty("vendorId", vendorId);
		jProduct.addProperty("saleStartedAt", DateUtil.timestampToString(paCopnGoods.getSaleStartDate(), DateUtil.COPN_T_DATETIME_FORMAT));
		jProduct.addProperty("saleEndedAt",DateUtil.timestampToString(paCopnGoods.getSaleEndDate(), DateUtil.COPN_T_DATETIME_FORMAT));
		jProduct.addProperty("displayProductName", "");//노출상품명 ※ 검토사항 : 빈값 or tbrand.brand_name + tpagoods.goods_name
		brand = StringUtils.remove(brand, "㈜");
		brand = StringUtils.remove(brand, "(주)");
		jProduct.addProperty("brand", brand);
		jProduct.addProperty("generalProductName", paCopnGoods.getGeneralProductName());

		

		if("1".equals(paCopnGoods.getCopnFreshYn())) {
			jProduct.addProperty("deliveryMethod", "COLD_FRESH");
		}else if("1".equals(paCopnGoods.getInstallYn())){
			jProduct.addProperty("deliveryMethod", "VENDOR_DIRECT");
		} else if("1".equals(paCopnGoods.getCreateYn())){
			jProduct.addProperty("deliveryMethod", "MAKE_ORDER");
		} else {
			jProduct.addProperty("deliveryMethod", "SEQUENCIAL");
		}
		
		jProduct.addProperty("deliveryCompanyCode", "DIRECT");
		
		if(paCopnGoods.getOrdCost() > 0){ // 개별/조건부 
			switch(StringUtils.substring(paCopnGoods.getShipCostCode(), 0, 2)){
			case "FR" : //SK스토아 무료 배송비 정책
				//착불 확인
				if("1".equals(paCopnGoods.getCollectYn())) {
					jProduct.addProperty("deliveryChargeType", "CHARGE_RECEIVED");
					jProduct.addProperty("unionDeliveryType", "NOT_UNION_DELIVERY");
				} else {
					jProduct.addProperty("deliveryChargeType", "FREE");
					jProduct.addProperty("unionDeliveryType", "UNION_DELIVERY"); //묶음 배송 여부
				}
				jProduct.addProperty("deliveryCharge", "0");
				jProduct.addProperty("freeShipOverAmount", "0");
				jProduct.addProperty("deliveryChargeOnReturn", "0");
				jProduct.addProperty("returnCharge", paCopnGoods.getReturnCost());
				
				break;
			case "CN" : case "PL" : //조건부
				if("1".equals(paCopnGoods.getCollectYn())) {
					jProduct.addProperty("deliveryChargeType", "CHARGE_RECEIVED");
					jProduct.addProperty("unionDeliveryType", "NOT_UNION_DELIVERY");
				} else {
					jProduct.addProperty("deliveryChargeType", "CONDITIONAL_FREE");
					jProduct.addProperty("unionDeliveryType", "UNION_DELIVERY"); //묶음 배송 여부		
				}
				jProduct.addProperty("deliveryCharge", paCopnGoods.getOrdCost());
				jProduct.addProperty("freeShipOverAmount", paCopnGoods.getShipCostBaseAmt());
				jProduct.addProperty("deliveryChargeOnReturn", "0");
				jProduct.addProperty("returnCharge", paCopnGoods.getReturnCost());
				break;
			default : //상품별
				if("1".equals(paCopnGoods.getCollectYn())) {
					jProduct.addProperty("deliveryChargeType", "CHARGE_RECEIVED");
					jProduct.addProperty("unionDeliveryType", "NOT_UNION_DELIVERY");
				} else {
					jProduct.addProperty("deliveryChargeType", "NOT_FREE");
					jProduct.addProperty("unionDeliveryType", "NOT_UNION_DELIVERY"); //묶음 배송 여부
				}
				jProduct.addProperty("deliveryCharge", paCopnGoods.getOrdCost());
				jProduct.addProperty("freeShipOverAmount", "0");
				jProduct.addProperty("deliveryChargeOnReturn", "0");
				jProduct.addProperty("returnCharge", paCopnGoods.getReturnCost());
				
				break;
			}
		} else { //무료배송
			if("1".equals(paCopnGoods.getCollectYn())) {
				jProduct.addProperty("deliveryChargeType", "CHARGE_RECEIVED");
				jProduct.addProperty("unionDeliveryType", "NOT_UNION_DELIVERY");
			} else {
				jProduct.addProperty("deliveryChargeType", "FREE");
				jProduct.addProperty("unionDeliveryType", "UNION_DELIVERY"); //묶음 배송 여부
			}
			jProduct.addProperty("deliveryCharge", "0");
			jProduct.addProperty("freeShipOverAmount", "0");
			jProduct.addProperty("deliveryChargeOnReturn", "0");
			jProduct.addProperty("returnCharge", paCopnGoods.getReturnCost());
		}
		
		jProduct.addProperty("remoteAreaDeliverable", "N"); //도서산간 배송여부
		jProduct.addProperty("returnCenterCode", "NO_RETURN_CENTERCODE");
		jProduct.addProperty("returnChargeName", paCopnGoods.getReturnChargeName());
		jProduct.addProperty("companyContactNumber", paCopnGoods.getCompanyContactNumber());
		jProduct.addProperty("returnZipCode", paCopnGoods.getReturnZipCode());
		jProduct.addProperty("returnAddress", paCopnGoods.getReturnAddress());
		if(paCopnGoods.getReturnAddressDetail() == null){
			jProduct.addProperty("returnAddressDetail", " "); //반품지 주소 상세
		} else {
			jProduct.addProperty("returnAddressDetail", paCopnGoods.getReturnAddressDetail());
		}
		jProduct.addProperty("returnChargeVendor", "N");//착불여부 : 더 이상 동작 안함
		jProduct.addProperty("afterServiceInformation", paCopnGoods.getRemark()); // A/S 안내
		jProduct.addProperty("afterServiceContactNumber", paCopnGoods.getCsTel());// A/S 전화번호
		jProduct.addProperty("outboundShippingPlaceCode", paCopnGoods.getOutBoundShippingPlaceCode());
		jProduct.addProperty("vendorUserId", loginId);//수정필요
		jProduct.addProperty("requested", "true"); //자동승인 요청 여부
		
		for(int i=0; i<paGoodsDtMappingList.size(); i++){
			paGoodsdtMapping = paGoodsDtMappingList.get(i);
			if(Constants.PA_COPN_MOD_CASE_MODIFY.equals(infoParam.getString("modCase"))){
				if(paGoodsdtMapping.getPaOptionCode() != null){
					//oItems.addProperty("sellerProductItemId", paGoodsdtMapping.getRemark1());
					//oItems.addProperty("vendorItemId", paGoodsdtMapping.getPaOptionCode());
					for(int k=0; k<retrieveItems.size(); k++) {
						if (!retrieveItems.get(k).getAsJsonObject().get("vendorItemId").isJsonNull()) {
							if (paGoodsdtMapping.getPaOptionCode().equals(retrieveItems.get(k).getAsJsonObject().get("vendorItemId").getAsString())) {
								oItems = retrieveItems.get(k).getAsJsonObject();
							}
						} else {
							continue;
						}
					}
				}else {
					oItems = new JsonObject();
				}
			}else {
				oItems = new JsonObject();
				
			}
			oItems.addProperty("originalPrice", paCopnGoods.getSalePrice());
			//oItems.addProperty("salePrice", paCopnGoods.getSalePrice() - paCopnGoods.getDcAmt());// 판매 가격
			
			//프로모션 적용 2020.06.15 by jchoi
			double couponPrice = 0;
			
			//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
			if(paPromoTargetList != null && paPromoTargetList.size() > 0) {
				for(PaPromoTarget paPromoTarget : paPromoTargetList) {
					if(paPromoTarget != null) {
						if(!paPromoTarget.getProcGb().equals("D")) {
							log.info("### couponPrice : "+paPromoTarget.getDoCost());
							couponPrice += paPromoTarget.getDoCost();	//할인금액(자동적용쿠폰 + 제휴OUT)
						}
					}
				}
			}
			//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
			
			//판매가 - ARS할인 - 일시불할인 - 프로모션할인
//			log.info("### originalPrice : "+paCopnGoods.getSalePrice());
//			log.info("### DcAmt : "+paCopnGoods.getDcAmt());
//			log.info("### LumpSumDcAmt : "+paCopnGoods.getLumpSumDcAmt());
//			log.info("### couponPrice : "+couponPrice);
			oItems.addProperty("salePrice", paCopnGoods.getSalePrice() - paCopnGoods.getDcAmt() - paCopnGoods.getLumpSumDcAmt() - couponPrice);
			
			oItems.addProperty("maximumBuyCount", paGoodsdtMapping.getTransOrderAbleQty());// 판매가능 수량
			
			//기간제한
			if(paCopnGoods.getDirectShipYn().equals("1")){ //직택배일 경우
				oItems.addProperty("maximumBuyForPerson", "1"); //인당최대구매수량
				if(paCopnGoods.getCustOrdQtyCheckYn()==1){
					if(paCopnGoods.getCustOrdQtyCheckTerm() != 0){
						oItems.addProperty("maximumBuyForPersonPeriod", paCopnGoods.getCustOrdQtyCheckTerm());
					} else {
						oItems.addProperty("maximumBuyForPersonPeriod", "0");
					}
				} else { //기간당 살 수 있는 수량 체크가 없다면
					oItems.addProperty("maximumBuyForPersonPeriod", "1");
				}
			} else if(paCopnGoods.getOrderMaxQty() > 0){
				oItems.addProperty("maximumBuyForPerson", paCopnGoods.getOrderMaxQty());
				oItems.addProperty("maximumBuyForPersonPeriod", "1");
			} else if(paCopnGoods.getCustOrdQtyCheckYn()==1){
				oItems.addProperty("maximumBuyForPerson", paCopnGoods.getTermOrderQty());
				oItems.addProperty("maximumBuyForPersonPeriod", paCopnGoods.getCustOrdQtyCheckTerm());
			} else {
				oItems.addProperty("maximumBuyForPerson", "0");
				oItems.addProperty("maximumBuyForPersonPeriod", "1");
			}
			
//			상품 발송 예정일 템플릿 조회
			oItems.addProperty("outboundShippingTimeDay", paCopnGoodsService.selectGoodsForCopnPolicy(paCopnGoods).get("DURATION"));
			
			oItems.addProperty("unitCount", 0); //단위 수량, 개당 가격 필요하지 않을 경우 '0'
			
			if("1".equals(paCopnGoods.getAdultYn())){
				oItems.addProperty("adultOnly", "ADULT_ONLY");
			} else {
				oItems.addProperty("adultOnly", "EVERYONE");
			}
			if("1".equals(paCopnGoods.getTaxYn())){
				oItems.addProperty("taxType", "TAX");
			} else {
				oItems.addProperty("taxType", "FREE");
			}
			oItems.addProperty("parallelImported", "NOT_PARALLEL_IMPORTED"); // 병행수입여부
			oItems.addProperty("overseasPurchased", "NOT_OVERSEAS_PURCHASED"); // 해외 구매대행 여부
			oItems.addProperty("pccNeeded", "false"); // 개인통관번호
			oItems.addProperty("externalVendorSku", paGoodsdtMapping.getGoodsCode()+paGoodsdtMapping.getGoodsdtCode()); // 업체상품코드(SK스토아 단품코드)
			oItems.addProperty("barcode", ""); // 바코드, 상품에 부착된 유효한 표준상품 코드
			oItems.addProperty("emptyBarcode", true);
			oItems.addProperty("emptyBarcodeReason", systemService.getSysdatetimeToString());
			oItems.addProperty("modelNo", "");
			
			jCertifications = new JsonArray();
			oCertifications = new JsonObject();
			
//			oCertifications.addProperty("certificationType", "PRESENTED_IN_DETAIL_PAGE"); //인증정보 Type, 매핑 완료 시 수정 필요
//			oCertifications.addProperty("certificationCode", ""); //상품인증 정보 코드
			oCertifications.addProperty("certificationType", paCopnGoods.getCertificationType()); //인증정보 Type, 매핑 완료 시 수정 필요
			oCertifications.addProperty("certificationCode", paCopnGoods.getCertificationCode()); //상품인증 정보 코드
			
			jCertifications.add(oCertifications);
			

			if(paCopnGoods.getKeyWord() != null){
				//쿠팡 검색어 연동 2021.03.09
				jSearchTags  = new JsonArray();
				keyWord = paCopnGoods.getKeyWord();
				keyWord.replace(", ", ",");
				keyAry = keyWord.split(",");
				
				for(int j=0; j<keyAry.length; j++) {
					//검색어 최대 20개
					if(jSearchTags.size() == 20) {
						break;
					}
					if(keyAry[j].length() <= 20) {
						jSearchTags.add(keyAry[j]);
					}
					
				}
				oItems.add("searchTags", jSearchTags);
			}
			
			jImages = new JsonArray();
			imageCount = 0;
			if(paCopnGoods.getImageP() != null){
				oPImages = new JsonObject();
				oPImages.addProperty("imageOrder", imageCount);
				oPImages.addProperty("imageType", "REPRESENTATION");
				oPImages.addProperty("vendorPath", image_address + paCopnGoods.getImageUrl() + paCopnGoods.getImageP());
				
				jImages.add(oPImages);
				++imageCount;
			}
			if(paCopnGoods.getImageAP() != null){
				oApImages = new JsonObject();
				oApImages.addProperty("imageOrder", imageCount);
				oApImages.addProperty("imageType", "DETAIL");
				oApImages.addProperty("vendorPath", image_address + paCopnGoods.getImageUrl() + paCopnGoods.getImageAP());
				
				jImages.add(oApImages);
				imageCount += 1;
			}
			if(paCopnGoods.getImageBP() != null){
				oApImages = new JsonObject();
				oApImages.addProperty("imageOrder", imageCount);
				oApImages.addProperty("imageType", "DETAIL");
				oApImages.addProperty("vendorPath", image_address + paCopnGoods.getImageUrl() + paCopnGoods.getImageBP());
				
				jImages.add(oApImages);
				imageCount += 1;
			}
			if(paCopnGoods.getImageCP() != null){
				oApImages  = new JsonObject();
				oApImages.addProperty("imageOrder", imageCount);
				oApImages.addProperty("imageType", "DETAIL");
				oApImages.addProperty("vendorPath", image_address + paCopnGoods.getImageUrl() + paCopnGoods.getImageCP());
				
				jImages.add(oApImages);
				imageCount += 1;
			}
			if(paCopnGoods.getImageDP() != null){
				oApImages  = new JsonObject();
				oApImages.addProperty("imageOrder", imageCount);
				oApImages.addProperty("imageType", "DETAIL");
				oApImages.addProperty("vendorPath", image_address + paCopnGoods.getImageUrl() + paCopnGoods.getImageDP());
				jImages.add(oApImages);
			}
			
			//정보고시
			jNotices = new JsonArray();
			for(int k=0; k<paGoodsOfferList.size(); k++){
				paCopnGoodsOffer = paGoodsOfferList.get(k);
						
				oNotices = new JsonObject();
				oNotices.addProperty("noticeCategoryDetailName", paCopnGoodsOffer.getPaOfferCodeName());//상품고시정보카테고리상세명
				oNotices.addProperty("noticeCategoryName", paCopnGoodsOffer.getPaOfferTypeName());
				oNotices.addProperty("content", paCopnGoodsOffer.getPaOfferExt());
				jNotices.add(oNotices);
			}
			
			//옵션, 속성 개수 만큼 반복문 돌리기
			jAttributes = new JsonArray();
			optionName = "";
			
			if(paCopnGoodsAttriList.size() == 0){
				oAttributes = new JsonObject();
				oAttributes.addProperty("attributeValueName", "");
				jAttributes.add(oAttributes);
			} else {
				for(int k=0; k<paCopnGoodsAttriList.size(); k++){
					optionValue = "";
					paCopnGoodsAttri = paCopnGoodsAttriList.get(k);
					
					if(paCopnGoodsAttri.getAttributeTypeMapping()!=null || paCopnGoodsAttri.getAttributeValueName()!=null) {
						if(paCopnGoodsAttri.getAttributeTypeMapping() != null){
							if("색상/크기/무늬/형태".equals(paCopnGoodsAttri.getAttributeTypeName())) {
								optionValue = paGoodsdtMapping.getGoodsdtInfo()+"/";
							}else {
								mappingValue = paCopnGoodsAttri.getAttributeTypeMapping().split(",");
								
								for(int l=0; l<mappingValue.length; l++){
									switch(mappingValue[l]) {
									case "색상":
										optionValue = optionValue.concat(paGoodsdtMapping.getColorName()+"/");
										break;
									case "사이즈":
										optionValue = optionValue.concat(paGoodsdtMapping.getSizeName()+"/");
										break;
									case "무늬": //미사용
										optionValue = optionValue.concat(paGoodsdtMapping.getPatternName()+"/");
										break;
									case "형태": //미사용
										optionValue = optionValue.concat(paGoodsdtMapping.getFormName()+"/");
										break;
									case "기타":
										optionValue = optionValue.concat(paGoodsdtMapping.getOtherText()+"/");
										break;
									}
								}
							}
							if(!"".equals(optionValue)){
								optionValue = optionValue.substring(0, optionValue.length()-1); //마지막 문자 자르기
							}
						}
						oAttributes = new JsonObject();
						oAttributes.addProperty("attributeTypeName", paCopnGoodsAttri.getAttributeTypeName());
						if(paCopnGoodsAttri.getAttributeValueName()==null) {
							oAttributes.addProperty("attributeValueName", optionValue);		
							optionName = optionName+" "+optionValue;
						} else {
							oAttributes.addProperty("attributeValueName", paCopnGoodsAttri.getAttributeValueName());
							optionName = optionName+" "+paCopnGoodsAttri.getAttributeValueName();
						}
						oAttributes.addProperty("exposed", "EXPOSED");
						oAttributes.addProperty("editable", "true");
						jAttributes.add(oAttributes);
					}
				}
			}
			if(optionName.equals("")) {
				oItems.addProperty("itemName", "단일상품"); // 업체상품옵션명, 각각의 아이템에 중복되지 않도록 기입 (사이트에 노출되는 옵션명X, 구매옵션에 따라 변경될 수 있다)
			} else {
				oItems.addProperty("itemName", optionName.substring(1, optionName.length())); // 업체상품옵션명, 각각의 아이템에 중복되지 않도록 기입 (사이트에 노출되는 옵션명X, 구매옵션에 따라 변경될 수 있다)
			}
			// 기술서
			
			JsonArray jcon = new JsonArray();
			jContents = new JsonArray();
			
			oContents = new JsonObject();
			oContents.addProperty("contentsType", "TEXT");
			
			jContentDetails = new JsonArray();
			
			oContentDetails = new JsonObject();
			
			//기술서 style 제거
			if(!"".equals(ComUtil.NVL(paCopnGoods.getDescribeExt()))) {
				String attributesToRemove = "width|height|style";
				String orgString = paCopnGoods.getDescribeExt();
				String tmpString = ComUtil.cleanHtmlFragment(orgString, attributesToRemove);
				String tmp2String = ComUtil.parse(tmpString);
				paCopnGoods.setDescribeExt(tmp2String);
			}			
			
			oContentDetails.addProperty("content", paCopnGoods.getDescribeExt()); // 기술서 경로
			oContentDetails.addProperty("detailType", "TEXT");
			
			jContentDetails.add(oContentDetails);

			jContents.add(jContentDetails);
			oContents.add("contentDetails", jContentDetails);
			jcon.add(oContents);

			oItems.add("certifications", jCertifications);
			oItems.add("images", jImages);
			oItems.add("notices", jNotices);
			oItems.add("attributes", jAttributes);
			oItems.add("contents", jcon);
			
			jProduct.addProperty("offerCondition", "NEW");
			
			jItems.add(oItems);
		}
		jProduct.add("items", jItems);
		
		jProduct.addProperty("manufacture", paCopnGoods.getMakecoName()); // 제조사

		return jProduct;
	}
	
	@ApiOperation(value = "상품수정", notes = "상품수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/legacy-goods-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode", required = false) String paCode,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value = "procId", required = false, defaultValue = "PACOPN") String procId,
			@RequestParam(value="searchTermGb"	, required=false, defaultValue = "") String searchTermGb)throws Exception {
		ParamMap paramMap 		= new ParamMap();
		String dateTime	 		= "";
		String duplicateCheck 	= "";
		String prg_id 			= "";
		
		ResponseEntity<?> responseMsg 						= null;
		List<PaEntpSlip> entpSlipList 						= null;
		PaEntpSlip entpSlip 								= null;
		List<PaCopnGoodsVO> paCopnGoodsList 				= null;
		PaCopnGoodsVO paCopnGoods 							= null;
        List<PaGoodsPriceVO> goodsPriceVoList 				= null;
        List<PaCopnGoodsdtMappingVO> vendorIdSelectList 	= null;
        List<PaCopnGoodsdtMappingVO> productIdSelectList 	= null;
        PaCopnGoodsdtMappingVO vendorIdSelect 				= null;
        PaCopnGoodsdtMappingVO productIdSelect 				= null;
        
       /*
		String rtnMsg 											= Constants.SAVE_SUCCESS;
        List<PaCopnGoodsAttri> copnGoodsAttri 					= null;
        List<PaGoodsOfferVO> goodsOffer 						= null;
        List<PaPromoTarget> paPromoTargetList 					= null;
        List<HashMap<String, Object>> paGoodsLimitCharMapList 	= null;  //검색어 특수문자 치환
        PaGoodsPriceVO goodsPriceVo 							= null;
        PaGoodsTransLog paGoodsTransLog 						= null;
        */
		log.info("===== 상품수정 API Start=====");
		log.info("01.API 기본정보 세팅");

		prg_id = "IF_PACOPNAPI_01_005";
		//request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		HashMap<String, String> apiInfo	= new HashMap<String, String>();
		
		paramMap.put("apiCode", prg_id);
    	paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
    	paramMap.put("startDate", dateTime);
    	paramMap.put("modCase", Constants.PA_COPN_MOD_CASE_MODIFY);
    	
    	try{
    		log.info("상품코드 : " + goodsCode);
			log.info("제휴사코드: " + paCode);
			log.info("처리자ID : " + procId.toUpperCase());

			log.info("02.API 중복실행검사");
            //= 중복 실행 Check
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
		    	if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});		    	
		    }
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			
			log.info("03.출고지 생성");
			//상품 출고지, 회수지 담당자 주소 체크
			paramMap.put("paAddrGb", "30");
			entpSlipList = paCopnGoodsService.selectPaCopnEntpSlipList(paramMap);
			if(entpSlipList.size() != 0){
				//출고지 등록
				for(int i = 0; i < entpSlipList.size(); i++) {
					entpSlip = entpSlipList.get(i);
					responseMsg= paCopnCommonController.entpSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaCode(), searchTermGb);
					log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
					if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
						paramMap.put("code","440");
						paramMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 업체코드 : " + entpSlip.getEntpCode()}));
						log.info("/entp-slip-insert");
						log.info("code : "+paramMap.get("code"));
						log.info("Message : "+paramMap.get("message"));
					}
				}
			}
			log.info("04.출고지 수정");
			responseMsg = paCopnCommonController.entpSlipUpdate(request);
			if (PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list", new String[] {" 상품코드 : " + goodsCode}));
				log.info("/entp-ship-update");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}else if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")) { 
				paramMap.put("code","440");
				paramMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 상품코드 : " + goodsCode}));
				log.info("/entp-ship-update");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			log.info("05.SK스토아 등록상품 승인 완료대상 조회");
			paCopnGoodsList = paCopnGoodsService.selectRegisterEmpty(paramMap);
			for(int i = 0; i < paCopnGoodsList.size(); i++) {
				paCopnGoods = paCopnGoodsList.get(i);
				responseMsg = paOptionCodeSave(request,paCopnGoods.getGoodsCode(),paCopnGoods.getSellerProductId(),"approvalStatus",procId, paCopnGoods.getPaCode());
				paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				log.info("/goods-list");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
						
			log.info("05_1.SK스토아 벤더아이디가 없는 상품 조회");
			vendorIdSelectList = paCopnGoodsService.selectEmptyVendorId(paramMap);
			for(int i = 0; i < vendorIdSelectList.size(); i++) {
				vendorIdSelect = vendorIdSelectList.get(i);
				responseMsg = paOptionCodeSave(request,vendorIdSelect.getGoodsCode(),vendorIdSelect.getSellerProductId(),"vendorItemId",procId, vendorIdSelect.getPaCode());
				paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				log.info("/goods-list");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			log.info("05_2.노출상품아이디 저장");
			productIdSelectList = paCopnGoodsService.selectEmptyProductId(paramMap);
			for(int i = 0; i < productIdSelectList.size(); i++) {
				productIdSelect = productIdSelectList.get(i);
				responseMsg = paOptionCodeSave(request,productIdSelect.getGoodsCode(),productIdSelect.getSellerProductId(),"productId",procId, productIdSelect.getPaCode());
				paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				log.info("/goods-list");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			/* 제휴OUT딜 상품 정보 조회, 저장 */
			//딜 주석처리 나중에 이슈 해결되면 다시 살리기
			//responseMsg = alcoutDealGoodsList(request, "PACOPNDEAL");
			
			log.info("07. 옵션별 판매중지");
			paCopnGoodsList = paCopnGoodsService.selectPaCopnGoodsSaleStopList(paramMap);
			for(int i = 0; i < paCopnGoodsList.size(); i++) {
				paCopnGoods = paCopnGoodsList.get(i);
				responseMsg = goodsSellStop(request, paCopnGoods.getGoodsCode(), paCopnGoods.getPaCode(), procId);
				paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				log.info("/goods-sell-stop");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			log.info("08. 옵션별 판매재개");
			paCopnGoodsList = paCopnGoodsService.selectPaCopnGoodsSaleRestartList(paramMap);
			for(int i = 0; i < paCopnGoodsList.size(); i++) {
				paCopnGoods = paCopnGoodsList.get(i);
				responseMsg = goodsSellRestart(request, paCopnGoods.getGoodsCode(), paCopnGoods.getPaCode(), procId);
				paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				log.info("/goods-sell-restart");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			//상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와  수정대상조회 시작시간을 비교함
			String goodsListTime = systemService.getSysdatetimeToString(); 
			
			log.info("09. SK스토아 상품 기본정보 수정 대상조회");
			paCopnGoodsList = paCopnGoodsService.selectPaCopnGoodsInfoList(paramMap);
			if(paCopnGoodsList.size()==0){
				paramMap.put("code"				,	"404");
				paramMap.put("message"			,	getMessage("pa.not_exists_process_list", new String[] {" 상품코드 : " + goodsCode}));
				paramMap.put("resultMessage"	,	paramMap.getString("message"));
				systemService.insertApiTrackingTx(request, paramMap);
			}

			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			paramMap.put("dateTime"			,	dateTime);
			paramMap.put("goodsListTime"	,	goodsListTime);
			
			for(PaCopnGoodsVO asyncCopnGoods :  paCopnGoodsList) {
				try {
					goodsModifyCopn(request, apiInfo, paramMap, asyncCopnGoods);		
				}catch (Exception e) {
					log.error(e.toString()); //운영상 문제점있으면 찾기
					continue;
				}
			}
			
			
			/*
			paCopnGoodsList = paCopnGoodsService.selectPaCopnGoodsInfoList(paramMap);
			if(paCopnGoodsList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list", new String[] {" 상품코드 : " + goodsCode}));
				paramMap.put("resultMessage",paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}
			
			HashMap<String, String> apiInfo = new HashMap<String, String>();
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			for(int i = 0; i < paCopnGoodsList.size(); i++) {
				
				ParamMap asyncMap = new ParamMap();
				String apiKeys[]=null;

				PaCopnGoodsVO asyncCopnGoods = paCopnGoodsList.get(i);
				List<PaGoodsdtMapping> goodsdtMapping = null;
				List<PaPromoTarget> asyncPromoTargetList = null;
				
				asyncMap.put("url", apiInfo.get("API_URL"));
				asyncMap.put("paCode",asyncCopnGoods.getPaCode());
				asyncMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				asyncMap.put("apiCode", prg_id);				
				
				if(asyncCopnGoods.getPaCode().equals(Constants.PA_COPN_BROAD_CODE)){
					asyncMap.put("paName", Constants.PA_BROAD);
					asyncMap.put("paCopnLoginId", ConfigUtil.getString("PA_COPN_BROAD_LOGIN_ID"));
				} else {
					asyncMap.put("paName", Constants.PA_ONLINE);
					asyncMap.put("paCopnLoginId", ConfigUtil.getString("PA_COPN_ONLINE_LOGIN_ID"));
				}
				
				apiKeys = apiInfo.get(asyncMap.getString("paName")).split(";"); 
				
				if(asyncCopnGoods.getSellerProductId()==null){
					paramMap.put("code","411");
					paramMap.put("message",getMessage("pa.not_exists_goods", new String[] {"상품코드 : " + goodsCode}));
					continue;
				}
				if(asyncCopnGoods.getDescribeExt()==null){
					paramMap.put("code","420");
					paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
					continue;
				}
				if(asyncCopnGoods.getApprovalStatus().equals("05") || asyncCopnGoods.getApprovalStatus().equals("10") || asyncCopnGoods.getApprovalStatus().equals("15")|| asyncCopnGoods.getApprovalStatus().equals("25")) { // 승인 반려상태
					paramMap.put("code","480");
					paramMap.put("message",getMessage("pa.copn_not_approval_status", new String[] {"상품코드 : " + goodsCode}));
					log.info("code : "+paramMap.get("code"));
					log.info("Message : "+paramMap.get("message"));
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(asyncCopnGoods.getGoodsCode());
					paGoodsTransLog.setPaCode(asyncCopnGoods.getPaCode());
					paGoodsTransLog.setItemNo(asyncCopnGoods.getSellerProductId()); // 상품코드
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn("0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
				} else {
					if("".equals(asyncCopnGoods.getCollectImage()) || asyncCopnGoods.getCollectImage() == null) {
						asyncCopnGoods.setDescribeExt("<div align='center'><img alt='' src='" + asyncCopnGoods.getTopImage() + "' /><br /><br /><br />" + asyncCopnGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + asyncCopnGoods.getBottomImage() + "' /></div>");				
					}else {
						asyncCopnGoods.setDescribeExt("<div align='center'><img alt='' src='" + asyncCopnGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + asyncCopnGoods.getCollectImage() + "' /><br /><br /><br />" + asyncCopnGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + asyncCopnGoods.getBottomImage() + "' /></div>");
					}
					
					paramMap.put("paGroupCode", asyncCopnGoods.getPaGroupCode());
					paramMap.put("goodsCode", asyncCopnGoods.getGoodsCode());
					paramMap.put("paCode", asyncCopnGoods.getPaCode());
					
					// 정보고시 조회
					goodsOffer = paCopnGoodsService.selectPaCopnGoodsOfferList(paramMap);
					if(goodsOffer.size() < 1){
						paramMap.put("code","430");
						paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
						//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						continue;
					}
					
					// 쿠팡 매핑 옵션 조회
					copnGoodsAttri = paCopnGoodsService.selectPaCopnGoodsAttriList(paramMap);
					// SK스토아 단품 옵션 조회
					goodsdtMapping = paCopnGoodsService.selectPaCopnGoodsdtInfoList(paramMap);
					// 프로모션 조회
					asyncPromoTargetList = paCopnGoodsService.selectPaPromoTarget(paramMap);
					
					//구매옵션 한개로 통일 by 2020.12.02 이후로
					if(!"색상/크기/무늬/형태".equals(copnGoodsAttri.get(0).getAttributeTypeName())) {
						// 쿠팡 옵션이 추가 되었을 경우를 대비해 체크
						if(goodsdtMapping.get(0).getTransTargetYn().equals("1")) {
							String nowOption = "";  // 현재 TPACOPNGOODSATTRI에 매핑된 단품이 모두 저장되는 변수
							for(int k = 0; k < copnGoodsAttri.size(); k++) {
								if(copnGoodsAttri.get(k).getAttributeTypeMapping()!= null) 
									nowOption = nowOption.concat(copnGoodsAttri.get(k).getAttributeTypeMapping()+",");
							}
							if(!"".equals(nowOption)) {
								nowOption = nowOption.substring(0,nowOption.length()-1);
							}
							
							String [] dt_info_kind= {"색상", "사이즈", "무늬","형태","기타"};
							String [] dt_info_kind_num = {"0", "0", "0", "0", "0"};
							String goods_dt_kinds[];
							String kind="";
							for(int l = 0; l < goodsdtMapping.size(); l++) {
								kind = goodsdtMapping.get(l).getGoodsdtInfoKind();
								goods_dt_kinds = kind.split("/");
								for(int k = 0; k < goods_dt_kinds.length; k++) {
									switch(goods_dt_kinds[k]) {
									case "색상":
										dt_info_kind_num[0] ="1";
										break;
									case "사이즈":
										dt_info_kind_num[1] ="1";
										break;
										
									case "무늬":
									case "형태":
									case "기타":
										dt_info_kind_num[4] ="1";
										break;
									}
								}
							}
							
							for(int k = 0; k < dt_info_kind.length; k++) {
								if(dt_info_kind_num[k].equals("1")) {
									if(!nowOption.contains(dt_info_kind[k]) && k<4) { //기타는 제외
										// 판매중지 api 호출
										responseMsg = goodsSellStop(request, asyncCopnGoods.getGoodsCode(), asyncCopnGoods.getPaCode(), procId);
										paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
										paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
										log.info("/goods-sell-stop");
										log.info("code : "+paramMap.get("code"));
										log.info("Message : "+paramMap.get("message"));
										
										// 테이블 update
										asyncCopnGoods.setPaSaleGb("10");
										asyncCopnGoods.setPaStatus("40");
										asyncCopnGoods.setModifyId(procId);
										asyncCopnGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
										
										rtnMsg = paCopnGoodsService.savePaChangeOptionStatus(asyncCopnGoods);
										if(!rtnMsg.equals("000000")){
											paramMap.put("code","404");
											paramMap.put("message",rtnMsg);
											return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
										} else {
											paramMap.put("code","200");
											paramMap.put("message",getMessage("pa.copn_change_option"));
										}
										
									}
								}
							}
						}
					}
					
					paGoodsLimitCharMapList = paCopnGoodsService.selectGoodsLimitCharList(paramMap);
					//검색어 특수문자 치환
					if(asyncCopnGoods.getKeyWord() != null) {
						String keyWord = asyncCopnGoods.getKeyWord();
						if(paGoodsLimitCharMapList != null){
							for(HashMap<String, Object> paGoodsLimitCharMap : paGoodsLimitCharMapList){
								String replaceChar = paGoodsLimitCharMap.get("REPLACE_CHAR").toString() == null ? "" : paGoodsLimitCharMap.get("REPLACE_CHAR").toString();
								keyWord = keyWord.replaceAll(paGoodsLimitCharMap.get("LIMIT_CHAR").toString(), replaceChar);
							}
						}
						keyWord = keyWord.trim();
						keyWord = keyWord.replaceAll("   ", " ");
						keyWord = keyWord.replaceAll("  ",  " ");
						keyWord = keyWord.replaceAll(" ",   " ");
						asyncCopnGoods.setKeyWord(keyWord);
					}
					
					JsonObject root = new JsonObject();
					root.add("insert", createProductJson(asyncCopnGoods, goodsdtMapping, copnGoodsAttri, goodsOffer, asyncPromoTargetList, apiKeys[0], asyncMap.getString("paCopnLoginId"), paramMap));
					
					asyncCopnGoods.setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));
					//비동기처리
					paCopnAsyncController.asyncGoodsModify(request, root, apiInfo, apiKeys, asyncMap, asyncCopnGoods, goodsdtMapping, asyncPromoTargetList);
					//앞에서 너무 오래 걸려서 sleep안줌. 나중에 빨라지고 디비 커넥션 관련 문제있으면 주석 해제 처리
					//Thread.sleep(50);
				}
				
				//REQ_PRM_041 쿠팡 제휴OUT 딜 상품 수정 호출//
				//딜 주석처리
		    	//alcoutDealGoodsModify(request, paCopnGoods.getGoodsCode(),paCopnGoods.getPaCode(), "PACOPNDEAL", "", "", "1");
			}
			*/
			
			log.info("10.SK스토아 상품재고 수정대상 조회");
			responseMsg = goodsStockModify(request, goodsCode, paCode, procId);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
				if(PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
					paramMap.put("code","404");
					paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message"));
				} else {
					paramMap.put("code","500");
					paramMap.put("message",getMessage("errors.api.system")+" : goodsStockModify" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
				}
			} else {
				paramMap.put("code","200");
				paramMap.put("message","OK");
			}
			
			paramMap.put("goodsCode", goodsCode);//단일수정시필요
			paramMap.put("paCode", paCode);
			
			log.info("11.SK스토아 상품 가격변경 대상 조회");
			goodsPriceVoList = paCopnGoodsService.selectCopnPriceModify(paramMap);
			
			for(PaGoodsPriceVO goodsPrice : goodsPriceVoList) {
				try {
					goodsPriceModifyCopn(request, paramMap, goodsPrice,  searchTermGb);
				}catch (Exception e) {
					log.error(e.toString()); //운영상 문제점있으면 찾기
					continue;
				}
			}			
			/*
			for(int i = 0; i < goodsPriceVoList.size(); i++) {

				goodsPriceVo = goodsPriceVoList.get(i);
				
				//프로모션조회
				paramMap.put("goodsCode", goodsPriceVo.getGoodsCode());
				paramMap.put("paCode", goodsPriceVo.getPaCode());
				paPromoTargetList = paCopnGoodsService.selectPaPromoTarget(paramMap);
				
				int dcPrice = Integer.parseInt(goodsPriceVo.getDcAmt()) + Integer.parseInt(goodsPriceVo.getLumpSumDcAmt());
				
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
				if(paPromoTargetList != null && paPromoTargetList.size() > 0) {
					for(PaPromoTarget paPromoTarget : paPromoTargetList) {
						if(paPromoTarget != null) {
							if(!paPromoTarget.getProcGb().equals("D")) {
								dcPrice += (int)paPromoTarget.getDoCost();
							}
						}
					}
				}
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
				
				responseMsg = goodsPriceModify(request, goodsPriceVo.getGoodsCode(), goodsPriceVo.getPaCode(), goodsPriceVo.getSalePrice(), dcPrice, procId);
				if(PropertyUtils.describe(responseMsg.getBody()).get("code").toString().equals("200")) {
					log.info("code : "+PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
					log.info("message : "+PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
					log.info("/goods-price-modify");
					responseMsg = goodsPriceDiscountModify(request, goodsPriceVo.getGoodsCode(), goodsPriceVo.getPaCode(), goodsPriceVo.getSalePrice(), goodsPriceVo.getApplyDate(), paPromoTargetList, procId);
					paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
					paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
					log.info("/goods-price-discount-modify");
					log.info("code : "+paramMap.get("code"));
					log.info("Message : "+paramMap.get("message"));
				} else {
					paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
					paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
					log.info("/goods-price-modify");
					log.info("code : "+paramMap.get("code"));
					log.info("Message : "+paramMap.get("message"));
				}
			}
			*/
			
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
				paramMap.put("resultCode", ComUtil.NVL(paramMap.getString("code"),"500"));
				paramMap.put("resultMsg", ComUtil.NVL(paramMap.getString("message")," "));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
    		if(!searchTermGb.equals("1")){
    		   if(duplicateCheck.equals("0")){
    			 systemService.checkCloseHistoryTx("end", prg_id);
    		   }    			 
    		}

			log.info("===== 상품수정 API End=====");
    	}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 상품 정보 조회
	 * @param request
	 * @param goodsCode
	 * @param sellerProductId
	 * @param status
	 * @param procId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품정보조회", notes = "상품정보조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> paOptionCodeSave(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value = "goodsCode", required = true) String goodsCode,//true
			@ApiParam(name = "sellerProductId", value = "등록상품ID", defaultValue = "") @RequestParam(value = "sellerProductId", required = true) String sellerProductId,//true
			@ApiParam(name = "status", value = "상태", defaultValue = "") @RequestParam(value = "status", required = true) String status,//true
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value = "procId", required = true, defaultValue = "PACOPN") String procId,//true
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode", required = true) String paCode) throws Exception{//true
			
		String rtnMsg = Constants.SAVE_SUCCESS;
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		JsonObject responseObject = null;
		ParamMap paramMap = new ParamMap();
		PaCopnGoodsdtMappingVO dtMap = new PaCopnGoodsdtMappingVO();
		PaCopnGoodsVO goodsMap = new PaCopnGoodsVO();
		PaGoodsTransLog paGoodsTransLog = null;
		String dateTime = "";
		JsonArray jsonPaOption = new JsonArray(); // 상품의 옵션 코드를 저장하기 위해
		int itemSize = 0;
		String paOptionCode = "";
		String remark = "";
		String dtCode = "";
		String approvalStatus = "";
		String duplicateCheck = "";
		
		String prg_id = "IF_PACOPNAPI_01_002";
		
		try{
			log.info("===== 상품정보조회 API START=====");
//			log.info("/goods-list input param ##########");
//			log.info("goodsCode : "+goodsCode);
//			log.info("sellerProductId : "+sellerProductId);
//			log.info("status : "+status);
//			log.info("procId : "+procId);
//			log.info("paCode : "+paCode);
//			log.info("/goods-list input param ##########");
			dateTime = systemService.getSysdatetimeToString();
			
			log.info("01.API 기본정보 세팅");
			paramMap.put("apiCode", prg_id);
        	paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
        	paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
        	paramMap.put("startDate", dateTime);
        	
        	if(paCode.equals(Constants.PA_COPN_BROAD_CODE)){
    			paramMap.put("paName", Constants.PA_BROAD);
    		} else {
    			paramMap.put("paName", Constants.PA_ONLINE);
    		}
        	
        	apiInfo = systemService.selectPaApiInfo(paramMap);
        	apiInfo.put("apiInfo", paramMap.getString("apiCode"));
        	
        	log.info("02.API 중복실행검사");
		    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			log.info("03.상품정보 조회 API START");
			responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#sellerProductId#", sellerProductId)));
			
			if(responseObject != null){
				if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())){
					paramMap.put("code", "200");
					jsonPaOption = responseObject.get("data").getAsJsonObject().get("items").getAsJsonArray();
					itemSize = jsonPaOption.size();
					paramMap.put("successCode", "200");
					paramMap.put("transSuccessMsg", responseObject.get("code").getAsString());
					
					if(status.equals("vendorItemId")){
						log.info("04.상품 옵션코드 UPDATE");
						for(int i=0; i<itemSize; i++){
							if(!jsonPaOption.get(i).getAsJsonObject().get("vendorItemId").toString().equals("null")) {
								paOptionCode = jsonPaOption.get(i).getAsJsonObject().get("vendorItemId").getAsString();
								remark = jsonPaOption.get(i).getAsJsonObject().get("sellerProductItemId").getAsString();
								dtCode = jsonPaOption.get(i).getAsJsonObject().get("externalVendorSku").getAsString();
								dtCode = dtCode.substring(8, 11);
								dtMap.setModifyDate(systemService.getSysdatetime());
								dtMap.setModifyId(procId);
								dtMap.setGoodsCode(goodsCode);
								dtMap.setPaOptionCode(paOptionCode);
								dtMap.setGoodsdtCode(dtCode);
								dtMap.setGoodsCode(goodsCode);
								dtMap.setRemark1(remark);
								dtMap.setPaCode(paCode);
								
								rtnMsg = paCopnGoodsService.savePaCopnGoodsDtOptionTx(dtMap);
								
								if(!rtnMsg.equals("000000")){
									paramMap.put("code","404");
									paramMap.put("message",rtnMsg);
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								} else {
									paramMap.put("code","200");
									paramMap.put("message", "OK");
								}
							} else {
								paramMap.put("successCode", "480");
								paramMap.put("transSuccessMsg", getMessage("pa.copn_not_approval_status"));
							}
						}
						//전송관리 테이블 저장
						paGoodsTransLog = new PaGoodsTransLog();
						paGoodsTransLog.setGoodsCode(goodsCode);
						paGoodsTransLog.setPaCode(paCode);
						paGoodsTransLog.setItemNo(sellerProductId);
						paGoodsTransLog.setRtnCode(paramMap.getString("successCode"));
						paGoodsTransLog.setRtnMsg(paramMap.getString("transSuccessMsg"));
						paGoodsTransLog.setSuccessYn(paramMap.getString("successCode").equals("200")==true?"1":"0");
						paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsTransLog.setProcId(procId);
						paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
					} else if(status.equals("productId")){
						for(int i=0; i<itemSize; i++){
							if(!jsonPaOption.get(i).getAsJsonObject().get("vendorItemId").toString().equals("null")) {
								paOptionCode = jsonPaOption.get(i).getAsJsonObject().get("vendorItemId").getAsString();
								remark = responseObject.get("data").getAsJsonObject().get("productId").getAsString();
								dtCode = jsonPaOption.get(i).getAsJsonObject().get("externalVendorSku").getAsString();
								dtCode = dtCode.substring(8, 11);
								dtMap.setModifyDate(systemService.getSysdatetime());
								dtMap.setModifyId(procId);
								dtMap.setGoodsCode(goodsCode);
								dtMap.setPaOptionCode(paOptionCode);
								dtMap.setGoodsdtCode(dtCode);
								dtMap.setGoodsCode(goodsCode);
								dtMap.setRemark2(remark);
								dtMap.setPaCode(paCode);
								
								rtnMsg = paCopnGoodsService.savePaCopnProductId(dtMap);
								
								if(!rtnMsg.equals("000000")){
									paramMap.put("code","404");
									paramMap.put("message",rtnMsg);
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								} else {
									paramMap.put("code","200");
									paramMap.put("message", "OK");
								}
							} else {
								paramMap.put("successCode", "480");
								paramMap.put("transSuccessMsg", getMessage("pa.copn_not_approval_status"));
							}
						}
						//전송관리 테이블 저장
						paGoodsTransLog = new PaGoodsTransLog();
						paGoodsTransLog.setGoodsCode(goodsCode);
						paGoodsTransLog.setPaCode(paCode);
						paGoodsTransLog.setItemNo(sellerProductId);
						paGoodsTransLog.setRtnCode(paramMap.getString("successCode"));
						paGoodsTransLog.setRtnMsg(paramMap.getString("transSuccessMsg"));
						paGoodsTransLog.setSuccessYn(paramMap.getString("successCode").equals("200")==true?"1":"0");
						paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsTransLog.setProcId(procId);
						paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
					} else {
						//전송관리 테이블 저장
						paGoodsTransLog = new PaGoodsTransLog();
						paGoodsTransLog.setGoodsCode(goodsCode);
						paGoodsTransLog.setPaCode(paCode);
						paGoodsTransLog.setItemNo(sellerProductId);
						paGoodsTransLog.setRtnCode(paramMap.getString("code"));
						paGoodsTransLog.setRtnMsg(responseObject.get("code").getAsString());
						paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
						paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsTransLog.setProcId(procId);
						paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
						
						log.info("04.상품 상태 UPDATE");
						approvalStatus = responseObject.get("data").getAsJsonObject().get("statusName").getAsString();
						
						goodsMap.setModifyDate(systemService.getSysdatetime());
						goodsMap.setModifyId(procId);
						goodsMap.setGoodsCode(goodsCode);
						goodsMap.setPaCode(paCode);
						goodsMap.setSellerProductId(sellerProductId);
						goodsMap.setApprovalStatus(approvalStatus);
						
						rtnMsg = paCopnGoodsService.savePaCopnApprovalStatus(goodsMap);
						
						if(!rtnMsg.equals("000000")){
							paramMap.put("code","404");
							paramMap.put("message",rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code","200");
							paramMap.put("message", "OK");
						}
					}
				} else {
					paramMap.put("code","500");
					paramMap.put("message",responseObject.get("message"));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			} else {
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			log.info("05.상품 기본정보 조회 API END");
		}catch(Exception e){
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			System.out.println(e.getMessage());
		}finally{
			try {
				paramMap.put("message", goodsCode + "|" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 상품정보조회 API End=====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 판매중지처리
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "판매중지처리", notes = "판매중지처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-sell-stop", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsSellStop(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode,//true
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,//true
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PACOPN") String procId) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		JsonObject responseObject = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		PaCopnGoodsdtMappingVO paCopnGoodsMapping = null;
		List<PaCopnGoodsdtMappingVO> paCopnGoodsMappingList = null;
		String vendorItemId = "";
		String apiKeys[]=null;
		
		log.info("===== 판매중지처리 API Start=====");
		log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PACOPNAPI_01_008";
		String request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		if(paCode.equals(Constants.PA_COPN_BROAD_CODE)){
			paramMap.put("paName", Constants.PA_BROAD);
		} else {
			paramMap.put("paName", Constants.PA_ONLINE);
		}
		
		try{
			responseObject = new JsonObject();
			
			log.info("02.API 중복실행검사");
		    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";"); 
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.SK스토아 상품 판매중지대상 조회");
			paramMap.put("paSaleGb", "30");
			paCopnGoodsMappingList = paCopnGoodsService.selectPaCopnGoodsProductNo(paramMap);
			
			log.info("04.쿠팡 api호출");
			for(int i = 0 ; i < paCopnGoodsMappingList.size(); i++) {
				paCopnGoodsMapping = paCopnGoodsMappingList.get(i);
				
				vendorItemId = paCopnGoodsMapping.getPaOptionCode();
				
				if(vendorItemId != null) {
					responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0]).replaceAll("#vendorItemId#", vendorItemId)), request_type);
					
					if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())) {
						paramMap.put("code","200");
						paramMap.put("message",responseObject.get("message").getAsString());
					} else {
						paramMap.put("code","500");
						paramMap.put("message",responseObject.get("message").getAsString());
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paCopnGoodsMapping.getGoodsCode());
					paGoodsTransLog.setPaCode(paCode);
					paGoodsTransLog.setItemNo(paCopnGoodsMapping.getSellerProductId()); // 단품코드
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					
					paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
				} else {
					paramMap.put("code","404");
					paramMap.put("message","vendorItemId is null");
				}
			}
			
			if(paCopnGoodsMappingList.size() == 0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			if(paramMap.getString("code").equals("200")) {
				paCopnGoodsMapping.setGoodsCode(goodsCode);
				paCopnGoodsMapping.setModifyId(procId);
				paCopnGoodsMapping.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paCopnGoodsMapping.setTransSaleYn("0");
				
				
				log.info("05.동기화 완료 저장");
				rtnMsg = paCopnGoodsService.savePaCopnGoodsSellTx(paCopnGoodsMapping);
				
				if(!rtnMsg.equals("000000")){
					paramMap.put("code","404");
					paramMap.put("message",rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else {
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}
			}
		}catch(Exception e){
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try {
				paramMap.put("message", goodsCode + "|" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 판매중지처리 API End=====");
		}
		/* 제휴OUT딜 상품 판매중지 호출*/
		//딜 주석처리
		//alcoutDealGoodsSellStop(request, goodsCode, paCode,"PACOPNDEAL");
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 판매재개처리
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "판매재개처리", notes = "판매재개처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-sell-restart", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsSellRestart(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode,//true
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,//true
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PACOPN") String procId) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		JsonObject responseObject = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		PaCopnGoodsdtMappingVO paCopnGoodsMapping = null;
		List<PaCopnGoodsdtMappingVO> paCopnGoodsMappingList = null;
		String vendorItemId = "";
		String apiKeys[]=null;
		
		log.info("===== 판매재개처리 API Start=====");
		log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PACOPNAPI_01_009";
		String request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		if(paCode.equals(Constants.PA_COPN_BROAD_CODE)){
			paramMap.put("paName", Constants.PA_BROAD);
		} else {
			paramMap.put("paName", Constants.PA_ONLINE);
		}
		
		try{
			responseObject = new JsonObject();
			
			log.info("02.API 중복실행검사");
		    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";"); 
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.SK스토아 상품 판매재개대상 조회");
			paramMap.put("paSaleGb", "20");
			paCopnGoodsMappingList = paCopnGoodsService.selectPaCopnGoodsProductNo(paramMap);
			
			log.info("04.쿠팡 api호출");
			for(int i = 0 ; i < paCopnGoodsMappingList.size(); i++) {
				paCopnGoodsMapping = paCopnGoodsMappingList.get(i);
				
				vendorItemId = paCopnGoodsMapping.getPaOptionCode();
				
				if(vendorItemId != null) {
					
					responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0]).replaceAll("#vendorItemId#", vendorItemId)), request_type);
					
					if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())) {
						paramMap.put("code","200");
						paramMap.put("message",responseObject.get("message").getAsString());
					} else {
						paramMap.put("code","500");
						paramMap.put("message",responseObject.get("message").getAsString());
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paCopnGoodsMapping.getGoodsCode());
					paGoodsTransLog.setPaCode(paCode);
					paGoodsTransLog.setItemNo(vendorItemId); // 단품코드
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					
					paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
				}
			}
			
			if(paCopnGoodsMappingList.size() == 0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			if(paramMap.getString("code").equals("200")) {
				paCopnGoodsMapping.setGoodsCode(goodsCode);
				paCopnGoodsMapping.setModifyId(procId);
				paCopnGoodsMapping.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paCopnGoodsMapping.setTransSaleYn("0");
				
				log.info("05.동기화 완료 저장");
				rtnMsg = paCopnGoodsService.savePaCopnGoodsSellTx(paCopnGoodsMapping);
				
				if(!rtnMsg.equals("000000")){
					paramMap.put("code","404");
					paramMap.put("message",rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else {
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}
			}
		}catch(Exception e){
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try {
				paramMap.put("message", goodsCode + "|" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 판매재개처리 API End=====");
		}
		
		/* 제휴OUT 딜 판매재개처리 */
		//딜 주석처리
		//alcoutDealGoodsSellRestart(request, goodsCode, paCode, "PACOPNDEAL");
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 
	   상품 아이템별 수량 변경
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 아이템별 수량 변경", notes = "상품 아이템별 수량 변경", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-stock-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsStockModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode, //true
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode, //true
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PACOPN") String procId) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		JsonObject responseObject = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		List<PaCopnGoodsdtMappingVO> paCopnGoodsdtMappingList = null;
		PaCopnGoodsdtMappingVO paCopnGoodsMapping = null;
		
		String vendorItemId = "";
		String transOrderAbleQty = "";
		String apiBroadKeys[]=null;
		String apiOnlineKeys[]=null;
		
		log.info("===== 상품아이템별 수량 변경 API Start=====");
		log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PACOPNAPI_01_006";
		String request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		paramMap.put("paNameBroad", Constants.PA_BROAD);
		paramMap.put("paNameOnline", Constants.PA_ONLINE);
		
		try{
			responseObject = new JsonObject();
			
			log.info("02.API 중복실행검사");
		    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiBroadKeys = apiInfo.get(paramMap.getString("paNameBroad")).split(";");
			apiOnlineKeys = apiInfo.get(paramMap.getString("paNameOnline")).split(";");
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.SK스토아 상품아이템별 수량 변경 대상 조회");
			paCopnGoodsdtMappingList = paCopnGoodsService.selectPaCopnGoodsdtMappingStockList(paramMap);
			
			log.info("04.쿠팡 api호출");
			for(int i = 0 ; i < paCopnGoodsdtMappingList.size(); i++) {
				paCopnGoodsMapping = paCopnGoodsdtMappingList.get(i);
				
				vendorItemId = paCopnGoodsMapping.getPaOptionCode();
				transOrderAbleQty = paCopnGoodsMapping.getTransOrderAbleQty();
				
				if(vendorItemId != null) {
					if(paCopnGoodsMapping.getPaCode().equals(Constants.PA_COPN_BROAD_CODE)){
						responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paNameBroad"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiBroadKeys[0]).replaceAll("#vendorItemId#", vendorItemId).replaceAll("#quantity#", transOrderAbleQty)), request_type);
					} else {
						responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paNameOnline"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiOnlineKeys[0]).replaceAll("#vendorItemId#", vendorItemId).replaceAll("#quantity#", transOrderAbleQty)), request_type);
					}
					
					if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())) {
						paramMap.put("code","200");
						paramMap.put("message",responseObject.get("message").getAsString());
						
						// DB update
						paCopnGoodsMapping.setModifyId(procId);
						paCopnGoodsMapping.setModifyDate(systemService.getSysdatetime());
						rtnMsg = paCopnGoodsService.updatePaCopnGoodsdtOrderTx(paCopnGoodsMapping);
						
						if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
							paramMap.put("code","404");
							paramMap.put("message",rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code","200");
							paramMap.put("message", "OK");
						}
						
					} else {
						paramMap.put("code","500");
						paramMap.put("message",responseObject.get("message").getAsString());
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paCopnGoodsMapping.getGoodsCode());
					paGoodsTransLog.setPaCode(paCopnGoodsMapping.getPaCode());
					paGoodsTransLog.setItemNo(vendorItemId); // 단품코드
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(responseObject.get("message").getAsString());
					paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					
					paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
				} else {
					paramMap.put("code","410");
					paramMap.put("message",getMessage("pa.copn_not_exists_sellerid"));
				}
			}
			if(paCopnGoodsdtMappingList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
		}catch(Exception e){
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try {
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("===== 상품아이템별 수량 변경 API End=====");
		}
		
		/* 제휴OUT 딜 상품아이템별 수량 변경 */
		//딜 주석처리
		//alcoutDealGoodsStockModify(request, goodsCode, paCode, "PACOPNDEAL");
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 
	   상품 아이템별 가격 변경
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 아이템별 가격 변경", notes = "상품 아이템별 가격 변경", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-price-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsPriceModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode, //true
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode, //true
			@ApiParam(name = "goodsPrice", value = "상품가격", defaultValue = "") @RequestParam(value="goodsPrice", required=true) String goodsPrice, //true
			@ApiParam(name = "goodsDiscountPrice", value = "할인율 기준가", defaultValue = "") @RequestParam(value="goodsDiscountPrice", required=true) int goodsDiscountPrice, //true
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PACOPN") String procId,
			@ApiParam(name = "searchTermGb", value = "API중복체크", defaultValue = "") @RequestParam(value = "", required = true) String searchTermGb
			) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		JsonObject responseObject = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		List<PaCopnGoodsdtMappingVO> paCopnGoodsdtMappingList = null;
		PaCopnGoodsdtMappingVO paCopnGoodsMapping = null;
		PaCopnGoodsVO paCopnGoodsData = null;
		
		String vendorItemId = "";
		String apiKeys[]=null;
		String rtnMsg = "";
		
		int price = 0;
		
		log.info("===== 상품 아이템별 가격 변경 API Start=====");
		log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PACOPNAPI_01_004";
		String request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		if(paCode.equals(Constants.PA_COPN_BROAD_CODE)){
			paramMap.put("paName", Constants.PA_BROAD);
		} else {
			paramMap.put("paName", Constants.PA_ONLINE);
		}
		
		try{
			responseObject = new JsonObject();
			
			log.info("02.API 중복실행검사");
			
			if(!searchTermGb.equals("1")){
			    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			}
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";"); 
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			price = Integer.parseInt(goodsPrice) - goodsDiscountPrice;
			
			log.info("03.SK스토아 상품아이템별 가격 변경 대상 조회");
			paCopnGoodsdtMappingList = paCopnGoodsService.selectCopnPriceModifyVendorIdSearch(paramMap);
			
			log.info("04.쿠팡 api호출");
			for(int i = 0 ; i < paCopnGoodsdtMappingList.size(); i++) {
				String dtMessage = "";
				paCopnGoodsData = new PaCopnGoodsVO();
				
				paCopnGoodsMapping = paCopnGoodsdtMappingList.get(i);
				
				vendorItemId = paCopnGoodsMapping.getPaOptionCode();
				
				if(vendorItemId != null) {
					responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0]).replaceAll("#vendorItemId#", vendorItemId).replaceAll("#price#", String.valueOf(price))), request_type);
					
					if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())) {
						paramMap.put("code","200");
						paramMap.put("message",responseObject.get("message").getAsString());
					} else {
						if(responseObject.get("message").getAsString().contains("변경전 판매가의 최대 50% 인하/최대 100%인상까지") || responseObject.get("message").getAsString().contains("배송비")) {
							paCopnGoodsData.setPaCode(paCopnGoodsMapping.getPaCode());
							paCopnGoodsData.setGoodsCode(paCopnGoodsMapping.getGoodsCode());
							paCopnGoodsData.setPaSaleGb("30");
							paCopnGoodsData.setPaStatus("90");
							paCopnGoodsData.setReturnNote(responseObject.get("message").getAsString());
							paCopnGoodsData.setTransSaleYn("1");
							paCopnGoodsData.setModifyId(Constants.PA_COPN_PROC_ID);
							paCopnGoodsData.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							rtnMsg = paCopnGoodsService.savePaCopnGoodsFailTx(paCopnGoodsData);
							if(!rtnMsg.equals(Constants.SAVE_SUCCESS)) {
								paramMap.put("code", "500");
								paramMap.put("message", rtnMsg);
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							} else {
								dtMessage = responseObject.get("message").getAsString().contains("배송비") ? " | 쿠팡 배송비 설정 기준 불만족[30,90]" : " | 판매가 변동 폭으로 인해 상태 변경 되었습니다.[30,90]";
								paramMap.put("code", "000");
								paramMap.put("message", "GOODS_CODE : " + paCopnGoodsMapping.getGoodsCode() + dtMessage);
							}
						} else {
							paramMap.put("code","500");
							paramMap.put("message",responseObject.get("message").getAsString());
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						}
					}
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paCopnGoodsMapping.getGoodsCode());
					paGoodsTransLog.setPaCode(paCopnGoodsMapping.getPaCode());
					paGoodsTransLog.setItemNo(paCopnGoodsMapping.getSellerProductId());
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					
					paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
				}
			}
			if(paCopnGoodsdtMappingList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
		}catch(Exception e){
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try {
				paramMap.put("message", goodsCode + "|" + (paramMap.getString("code").equals("200") ? "OK" : paramMap.getString("message")));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(!searchTermGb.equals("1")){
				if (duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);	
			}
			
			log.info("===== 상품 아이템별 가격 변경 API End=====");
		}
		
		/* 제휴OUT 딜 상품 아이템별 가격 변경 */
		//딜 주석처리
		//alcoutDealGoodsPriceModify(request, goodsCode, paCode, goodsPrice, goodsDiscountPrice, "PACOPNDEAL");
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 
	   상품 아이템별 할인율 기준가 변경
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품 아이템별 할인율 기준가 변경", notes = "상품 아이템별 할인율 기준가 변경", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-original-prices-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsPriceDiscountModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode, //true
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode, //true
			@ApiParam(name = "goodsPrice", value = "상품가격", defaultValue = "") @RequestParam(value="goodsPrice", required=true) String goodsPrice, //true
			@ApiParam(name = "applyDate", value = "적용일시", defaultValue = "") @RequestParam(value="applyDate", required=true) Timestamp applyDate, //true
			@ApiParam(name = "paPromoTargetList", value = "프로모션", defaultValue = "") @RequestParam(value="paPromoTargetList", required=false) List<PaPromoTarget> paPromoTargetList,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PACOPN") String procId,
			@ApiParam(name = "searchTermGb", value = "API중복체크", defaultValue = "") @RequestParam(value = "", required = true) String searchTermGb
			) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		JsonObject responseObject = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		List<PaCopnGoodsdtMappingVO> paCopnGoodsdtMappingList = null;
		PaCopnGoodsdtMappingVO paCopnGoodsMapping = null;
		
		String vendorItemId = "";
		String apiKeys[]=null;
		
		log.info("===== 상품 아이템별 할인율 기준가 변경 API Start=====");
		log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PACOPNAPI_01_010";
		String request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		if(paCode.equals(Constants.PA_COPN_BROAD_CODE)){
			paramMap.put("paName", Constants.PA_BROAD);
		} else {
			paramMap.put("paName", Constants.PA_ONLINE);
		}
		
		try{
			responseObject = new JsonObject();
			
			log.info("02.API 중복실행검사");
			
			if(!"1".equals(searchTermGb)) {
			    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			}
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";"); 
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.SK스토아 상품아이템별 가격 변경 대상 조회");
			paCopnGoodsdtMappingList = paCopnGoodsService.selectCopnPriceModifyVendorIdSearch(paramMap);
			
			log.info("04.쿠팡 api호출");
			for(int i = 0 ; i < paCopnGoodsdtMappingList.size(); i++) {
				paCopnGoodsMapping = paCopnGoodsdtMappingList.get(i);
				
				vendorItemId = paCopnGoodsMapping.getPaOptionCode();
				
				if(vendorItemId != null) {
						responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0]).replaceAll("#vendorItemId#", vendorItemId).replaceAll("#originalPrice#", String.valueOf(goodsPrice))), request_type);
					
					if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())) {
						paramMap.put("code","200");
						paramMap.put("message",responseObject.get("message").getAsString());
					} else {
						paramMap.put("code","500");
						paramMap.put("message",responseObject.get("message").getAsString());
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paCopnGoodsMapping.getGoodsCode());
					paGoodsTransLog.setPaCode(paCopnGoodsMapping.getPaCode());
					paGoodsTransLog.setItemNo(paCopnGoodsMapping.getSellerProductId()); 
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn(Constants.PA_COPN_SUCCESS_OK.equals(paramMap.getString("code"))==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					
					paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
				}
			}
			
			if("200".equals(paramMap.getString("code"))) { 
				// DB update
				paCopnGoodsMapping.setModifyId(procId);
				paCopnGoodsMapping.setModifyDate(systemService.getSysdatetime());
				paCopnGoodsMapping.setTransDate(systemService.getSysdatetime());
				paCopnGoodsMapping.setTransId(procId);
				paCopnGoodsMapping.setApplyDate(applyDate);
				rtnMsg = paCopnGoodsService.updatePaCopnGoodsPriceDiscountTx(paCopnGoodsMapping, paPromoTargetList);
				
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
					paramMap.put("code","404");
					paramMap.put("message",rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else {
					paramMap.put("code","200");
					paramMap.put("message", "OK");
				}
			} else { // vendorId가 하나도 없는 상품
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			if(paCopnGoodsdtMappingList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
		}catch(Exception e){
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try {
				paramMap.put("message", goodsCode + "|" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(!"1".equals(searchTermGb)) {
				if (duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);				
			}
			log.info("===== 상품 아이템별 할인율 기준가 변경 API End=====");
		}
		/* 제휴OUT 딜 상품 아이템별 할인율 기준가 변경 */
		//딜 주석처리
		//alcoutDealGoodsPriceDiscountModify(request, goodsCode, paCode, goodsPrice, applyDate, paPromoTargetList, "PACOPNDEAL");
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "제휴OUT 딜 상품등록", notes = "제휴OUT 딜 상품등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	
	@RequestMapping(value = "/alcoutDeal-goods-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealgoodsInsert(HttpServletRequest request,
			@ApiParam(name = "alcoutDealCode", value = "제휴OUT딜코드", defaultValue = "") @RequestParam(value="alcoutDealCode", required = false) String alcoutDealCode,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode", required = true) String paCode,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value = "procId", required = false, defaultValue = "PACOPNDEAL") String procId) throws Exception{

		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		String  duplicateCheck = "";
		JsonObject responseObject = null;
		
		String apiKeys[] = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		String respMsg = "";
		String sellerProductId = "";
		
		PaCopnGoodsVO paCopnGoods = null;
        List<PaPromoTarget> paPromoTargetList = null;//프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
        
        PaGoodsTransLog paGoodsTransLog = null;
        
        HashMap<String, String> apiInfo = new HashMap<String, String>();
        
        String prg_id = "IF_PACOPNAPI_01_011";
        String request_type = "POST";
        
        HashMap<String, Object> paCopnAlcoutDealInfo = new HashMap<String, Object>();
		List<HashMap<String, Object>> paCopnAlcoutDealGoodsdtList = new ArrayList<HashMap<String, Object>>();
		
		List<PaCopnGoodsVO> psCopnGoodsDescribeList = null; // 기술서 목록
        
        try{
        	log.info("===== 제휴OUT딜 상품등록 API Start =====");
        	log.info("01.API 기본정보 세팅");
        	
        	dateTime = systemService.getSysdatetimeToString();
        	
        	paramMap.put("apiCode", prg_id);
        	paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
        	paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
        	paramMap.put("startDate", dateTime);
        	paramMap.put("modCase", Constants.PA_COPN_MOD_CASE_INSERT);
        	if(paCode.equals(Constants.PA_COPN_BROAD_CODE)){
    			paramMap.put("paName", Constants.PA_BROAD);
    			paramMap.put("paCopnLoginId", ConfigUtil.getString("PA_COPN_BROAD_LOGIN_ID"));
    		} else {
    			paramMap.put("paName", Constants.PA_ONLINE);
    			paramMap.put("paCopnLoginId", ConfigUtil.getString("PA_COPN_ONLINE_LOGIN_ID"));
    		}
        	
        	log.info("02.API 중복실행검사");
            //= 중복 실행 Check
		    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
		    
		    log.info("03.SK stoa 상품정보 조회");
		    paramMap.put("alcoutDealCode", 	alcoutDealCode);
			paramMap.put("goodsCode", 		goodsCode);
			paramMap.put("paCode", 			paCode);
			paramMap.put("modifyId", 		procId);
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";");
			
			//제휴OUT 딜정보 조회
			paCopnAlcoutDealInfo = paCopnGoodsService.selectPaCopnAlcoutDealInfo(paramMap);
			
			if(paCopnAlcoutDealInfo == null){
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			//제휴OUT 딜 대표 상품정보 조회
			paCopnGoods = paCopnGoodsService.selectPaCopnAlcoutDealGoodsInfo(paramMap);
			
			//기술서 조회
			psCopnGoodsDescribeList = paCopnGoodsService.selectPaCopnAlcoutDealGoodsDescribe(paramMap); 
			
			if(paCopnGoods == null){
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			String strDescribeExt = "";
			
			/* 기술서 통합 */
			for(PaCopnGoodsVO psCopnGoodsDescribe : psCopnGoodsDescribeList) {
				if(!("").equals(psCopnGoodsDescribe.getDescribeExt()) && psCopnGoodsDescribe.getDescribeExt() != null) {
					strDescribeExt +=  psCopnGoodsDescribe.getGoodsName() + "<br />" + psCopnGoodsDescribe.getDescribeExt() + "<br />";
				}
			}
			
			//기술서
			if("".equals(paCopnGoods.getCollectImage()) || paCopnGoods.getCollectImage() == null) {
				paCopnGoods.setDescribeExt("<div align='center'><img alt='' src='" + paCopnGoods.getTopImage() + "' /><br /><br /><br />" + strDescribeExt.replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + paCopnGoods.getBottomImage() + "' /></div>");				
			}else {
				paCopnGoods.setDescribeExt("<div align='center'><img alt='' src='" + paCopnGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + paCopnGoods.getCollectImage() + "' /><br /><br /><br />" + strDescribeExt.replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + paCopnGoods.getBottomImage() + "' /></div>");
			}
			
			paramMap.put("paGroupCode", paCopnGoods.getPaGroupCode());
			
			if(paCopnGoods.getSellerProductId() != null){
				paramMap.put("code", "410");
				paramMap.put("message", getMessage("pa.already_insert_goods", new String[] {"쿠팡 상품코드 : " + paCopnGoods.getSellerProductId()}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			if(paCopnGoods.getDescribeExt() == null){
				paramMap.put("code", "420");
				paramMap.put("message", getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			//제휴OUT 딜 상품/단품조회
			paCopnAlcoutDealGoodsdtList = paCopnGoodsService.selectPaCopnAlcoutDealGoodsdtInfoList(paramMap);

			//제휴OUT 딜 상품 즉시할인쿠폰 프로모션
			paPromoTargetList = paPromoTargetAlcoutDealInfoSetting(paramMap); //프로모션 별 운영관리 기능 효율화(REQ_PRM_040)  
			
			//쿠팡 상품 등록 시작
			JsonObject root = new JsonObject();
			root.add("insert", createAlcoutDealProductJson(paCopnAlcoutDealInfo, paCopnGoods, paCopnAlcoutDealGoodsdtList, paPromoTargetList, apiKeys[0], paramMap.getString("paCopnLoginId"), paramMap));
			
			log.info("04.상품등록 API 호출");
			//log.info("입력 DATA : " + new GsonBuilder().create().toJson(root.get("insert")));
			responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0])), request_type, new GsonBuilder().create().toJson(root.get("insert")));

			if(responseObject != null){
		    	respMsg = responseObject.get("code").getAsString();
		    	log.info("connect respMsg  : "+respMsg);
		    	
		    	//쿠팡상품 생성 결과
		    	if("SUCCESS".equals(respMsg)){
		    		paramMap.put("code", "200");
		    		paramMap.put("message", respMsg);
		    		
		    		sellerProductId = responseObject.get("data").getAsString();
		    		paramMap.put("sellerProductID", sellerProductId);
		    		
		    		//전송관리 테이블 저장
		    		paGoodsTransLog = new PaGoodsTransLog();
		    		paGoodsTransLog.setGoodsCode(paramMap.getString("goodsCode"));
		    		paGoodsTransLog.setPaCode(paramMap.getString("paCode"));
		    		paGoodsTransLog.setItemNo(paramMap.getString("sellerProductID").isEmpty()?paramMap.getString("goodsCode"):paramMap.getString("sellerProductID"));
		    		paGoodsTransLog.setRtnCode(paramMap.getString("code"));
		    		paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
		    		paGoodsTransLog.setSuccessYn(paramMap.getString("code").equals("200")==true?"1":"0");
		    		paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
		    		paGoodsTransLog.setProcId(procId);
		    		
		    		paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
		    		
		    		if(!sellerProductId.equals("")){
						paCopnAlcoutDealInfo.put("alcoutDealCode", 	paramMap.getString("alcoutDealCode"));
						paCopnAlcoutDealInfo.put("paGoodsCode", 	responseObject.get("data").getAsString());
						paCopnAlcoutDealInfo.put("paCode", 			paramMap.getString("paCode"));
						paCopnAlcoutDealInfo.put("paStatus"	, 		"30");// 입점완료
						paCopnAlcoutDealInfo.put("approvalStatus", 	"00");// 등록
						paCopnAlcoutDealInfo.put("modifyId"	, 	 	"COPNDEAL");
						paCopnAlcoutDealInfo.put("modifyDate",		DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						log.info("05.제휴사 제휴OUT딜 상품정보 저장");
						rtnMsg = paCopnGoodsService.savePaCopnAlcoutDealGoodsTx(paCopnAlcoutDealInfo);
						
						if(!rtnMsg.equals("000000")){
							paramMap.put("code", "404");
							paramMap.put("message", rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code",paramMap.getString("code"));
							paramMap.put("message", paramMap.getString("message"));
						}
		    		} else {
		    			paramMap.put("code", "500");
		    			paramMap.put("message", respMsg);
		    		}
		    	} else {
		    		paramMap.put("code", "500");
		    		paramMap.put("message", responseObject.get("message").getAsString());
		    	}
		    } 
        }catch(Exception e){
        	if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
        }finally{
        	try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("===== 제휴OUT 딜 상품등록 API End=====");
        }

        return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/* REQ_PRM_041 제휴OUT 딜 상품 프로모션 정보 세팅 */
	private List<PaPromoTarget> paPromoTargetAlcoutDealInfoSetting(ParamMap paramMap) throws Exception{
		//제휴OUT 딜 상품 조회
		List<HashMap<String,Object>> requestAlcoutDealGoodsList = paCopnGoodsService.selectPaCopnAlcoutDealGoodsList(paramMap);

		List<PaPromoTarget> paPromoTargetList = new ArrayList<>();
		for(HashMap<String,Object> AlcoutDealGoodsMap : requestAlcoutDealGoodsList) {
			//제휴OUT 딜 상품 프로모션 조회
			ParamMap goodsParamMap = new ParamMap();
			goodsParamMap.put("goodsCode", AlcoutDealGoodsMap.get("GOODS_CODE"));
			goodsParamMap.put("paCode", AlcoutDealGoodsMap.get("PA_CODE"));
			List<PaPromoTarget> requestPriceMapList = paCopnGoodsService.selectPaPromoTarget(goodsParamMap);
			
			PaPromoTarget paPromoTarget;
	    	
	    	for(PaPromoTarget priceMap : requestPriceMapList){
	    		paPromoTarget = new PaPromoTarget();
	    		paPromoTarget.setGoodsCode(goodsParamMap.getString("goodsCode"));
	    		paPromoTarget.setPaCode(goodsParamMap.getString("paCode"));
	    		paPromoTarget.setPromoNo(priceMap.getPromoNo());
	    		paPromoTarget.setSeq(priceMap.getSeq());
	    		paPromoTarget.setProcGb(priceMap.getProcGb());
	    		paPromoTarget.setDoCost(priceMap.getDoCost());
	    		paPromoTarget.setDoOwnCost(priceMap.getDoOwnCost());
	    		paPromoTarget.setDoEntpCost(priceMap.getDoEntpCost());
	    		
	    		if( !("").equals(priceMap.getTransDate())){
	    			paPromoTarget.setTransDate(priceMap.getTransDate());	
	    		}else{
	    			paPromoTarget.setTransDate(null);	
	    		}
	    		paPromoTargetList.add(paPromoTarget);
	    	}
		}
		return paPromoTargetList;
	}
	
	/* 제휴OUT딜 상품 Json*/
	private JsonObject createAlcoutDealProductJson(HashMap<String, Object> paCopnAlcoutDealInfo, PaCopnGoodsVO paCopnGoods, List<HashMap<String, Object>> paCopnAlcoutDealGoodsdtList,
			List<PaPromoTarget> paPromoTargetList, String vendorId, String loginId,
			ParamMap infoParam) throws Exception{
		
		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		
		String image_address = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
		
		JsonArray jItems = new JsonArray();
		JsonObject jProduct = null;
		JsonObject oItems = null;
		JsonArray jCertifications = null;
		JsonObject oCertifications = null;
		JsonArray jImages = null;
		JsonObject oPImages = null;
		JsonObject oApImages = new JsonObject();
		JsonArray jNotices = null;
		JsonObject oNotices = null;
		JsonArray jAttributes = null;
		JsonObject oAttributes = null;
		JsonArray jContents = null;
		JsonObject oContents = null;
		JsonArray jContentDetails = null;
		JsonObject oContentDetails = null;
		int imageCount = 1;
		String mappingValue [] = null;
		String optionValue = "";
		String brand = "";
		
        List<PaCopnGoodsAttri> paCopnGoodsAttriList = null;
        PaCopnGoodsAttri paCopnGoodsAttri = null;
        
        List<PaGoodsOfferVO> paGoodsOfferList = null;
        PaGoodsOfferVO paCopnGoodsOffer = null;
		
        String optionName = "";
		
		brand = paCopnGoods.getBrandName();
		
		if(paCopnGoods.getSellerProductId() != null){//상품 수정시 조회 API실행 후 변경되는 부분만 수정한다.
			
			String prg_id = "IF_PACOPNAPI_01_002";
			String dateTime = systemService.getSysdatetimeToString();
			ParamMap paramMap = new ParamMap();
			paramMap.put("apiCode", prg_id);
	    	paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
	    	paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
	    	paramMap.put("startDate", dateTime);
	    	if(paCopnGoods.getPaCode().equals(Constants.PA_COPN_BROAD_CODE)){
				paramMap.put("paName", Constants.PA_BROAD);
			} else {
				paramMap.put("paName", Constants.PA_ONLINE);
			}
	    	HashMap<String, String> apiInfo = new HashMap<String, String>();
	    	apiInfo = systemService.selectPaApiInfo(paramMap);
	    	apiInfo.put("apiInfo", paramMap.getString("apiCode"));
	    	JsonObject responseObject = null;
	    	//상품조회 API
	    	responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#sellerProductId#", paCopnGoods.getSellerProductId())));
	    	if(responseObject != null){
	    		if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())){
	    			jProduct = responseObject.get("data").getAsJsonObject();
	    		} else {
					paramMap.put("code","500");
					paramMap.put("message",responseObject.get("message"));
					return jProduct; //에러.
				}
	    	} else {
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return jProduct; //에러.
			}
			//jProduct.addProperty("sellerProductId", paCopnGoods.getSellerProductId());
		} else {
			jProduct = new JsonObject();			
		}
		
		jProduct.addProperty("displayCategoryCode", paCopnGoods.getDisplayCategoryCode());
		jProduct.addProperty("sellerProductName", paCopnGoods.getDisplayProductName());
		jProduct.addProperty("vendorId", vendorId);
		jProduct.addProperty("saleStartedAt", DateUtil.timestampToString(paCopnGoods.getSaleStartDate(), DateUtil.COPN_T_DATETIME_FORMAT));
		jProduct.addProperty("saleEndedAt",DateUtil.timestampToString(paCopnGoods.getSaleEndDate(), DateUtil.COPN_T_DATETIME_FORMAT));
		jProduct.addProperty("displayProductName", "");//노출상품명 ※ 검토사항 : 빈값 or tbrand.brand_name + tpagoods.goods_name
		brand = StringUtils.remove(brand, "㈜");
		brand = StringUtils.remove(brand, "(주)");
		jProduct.addProperty("brand", brand);
		jProduct.addProperty("generalProductName", paCopnGoods.getGeneralProductName());
		
		if("1".equals(paCopnGoods.getCopnFreshYn())) {
			jProduct.addProperty("deliveryMethod", "COLD_FRESH");
		}else if("1".equals(paCopnGoods.getInstallYn())){
			jProduct.addProperty("deliveryMethod", "VENDOR_DIRECT");
		} else if("1".equals(paCopnGoods.getCreateYn())){
			jProduct.addProperty("deliveryMethod", "MAKE_ORDER");
		} else {
			jProduct.addProperty("deliveryMethod", "SEQUENCIAL");
		}
		
		jProduct.addProperty("deliveryCompanyCode", "DIRECT");
		
		if(paCopnGoods.getOrdCost() > 0){ // 개별/조건부 
			switch(StringUtils.substring(paCopnGoods.getShipCostCode(), 0, 2)){
			case "FR" : //SK스토아 무료 배송비 정책
				jProduct.addProperty("deliveryChargeType", "FREE");
				jProduct.addProperty("deliveryCharge", "0");
				jProduct.addProperty("freeShipOverAmount", "0");
				jProduct.addProperty("deliveryChargeOnReturn", "0");
				jProduct.addProperty("returnCharge", paCopnGoods.getReturnCost());
				jProduct.addProperty("unionDeliveryType", "UNION_DELIVERY"); //묶음 배송 여부
				break;
			case "CN" : //조건부
				jProduct.addProperty("deliveryChargeType", "CONDITIONAL_FREE");
				jProduct.addProperty("deliveryCharge", paCopnGoods.getOrdCost());
				jProduct.addProperty("freeShipOverAmount", paCopnGoods.getShipCostBaseAmt());
				jProduct.addProperty("deliveryChargeOnReturn", "0");
				jProduct.addProperty("returnCharge", paCopnGoods.getReturnCost());
				jProduct.addProperty("unionDeliveryType", "UNION_DELIVERY"); //묶음 배송 여부
				break;
			default : //상품별
				jProduct.addProperty("deliveryChargeType", "NOT_FREE");
				jProduct.addProperty("deliveryCharge", paCopnGoods.getOrdCost());
				jProduct.addProperty("freeShipOverAmount", "0");
				jProduct.addProperty("deliveryChargeOnReturn", "0");
				jProduct.addProperty("returnCharge", paCopnGoods.getReturnCost());
				jProduct.addProperty("unionDeliveryType", "NOT_UNION_DELIVERY"); //묶음 배송 여부
				break;
			}
		} else { //무료배송
			jProduct.addProperty("deliveryChargeType", "FREE");
			jProduct.addProperty("deliveryCharge", "0");
			jProduct.addProperty("freeShipOverAmount", "0");
			jProduct.addProperty("deliveryChargeOnReturn", "0");
			jProduct.addProperty("returnCharge", paCopnGoods.getReturnCost());
			jProduct.addProperty("unionDeliveryType", "UNION_DELIVERY"); //묶음 배송 여부
		}
		
		jProduct.addProperty("remoteAreaDeliverable", "N"); //도서산간 배송여부
		jProduct.addProperty("returnCenterCode", "NO_RETURN_CENTERCODE");
		jProduct.addProperty("returnChargeName", paCopnGoods.getReturnChargeName());
		jProduct.addProperty("companyContactNumber", paCopnGoods.getCompanyContactNumber());
		jProduct.addProperty("returnZipCode", paCopnGoods.getReturnZipCode());
		jProduct.addProperty("returnAddress", paCopnGoods.getReturnAddress());
		if(paCopnGoods.getReturnAddressDetail() == null){
			jProduct.addProperty("returnAddressDetail", " "); //반품지 주소 상세
		} else {
			jProduct.addProperty("returnAddressDetail", paCopnGoods.getReturnAddressDetail());
		}
		jProduct.addProperty("returnChargeVendor", "N");//착불여부 : 더 이상 동작 안함
		jProduct.addProperty("afterServiceInformation", paCopnGoods.getRemark()); // A/S 안내
		jProduct.addProperty("afterServiceContactNumber", paCopnGoods.getCsTel());// A/S 전화번호
		jProduct.addProperty("outboundShippingPlaceCode", paCopnGoods.getOutBoundShippingPlaceCode());
		jProduct.addProperty("vendorUserId", loginId);//수정필요
		jProduct.addProperty("requested", "true"); //자동승인 요청 여부
		
		for(int i=0; i<paCopnAlcoutDealGoodsdtList.size(); i++){
			HashMap<String, Object> paCopnAlcoutDealGoodsdt = paCopnAlcoutDealGoodsdtList.get(i);
			
			oItems = new JsonObject();
			
			if(paCopnAlcoutDealGoodsdt.get("PA_OPTION_CODE") != null && paCopnAlcoutDealGoodsdt.get("REMARK1") != null){
				oItems.addProperty("vendorItemId", paCopnAlcoutDealGoodsdt.get("PA_OPTION_CODE").toString());
				oItems.addProperty("sellerProductItemId", paCopnAlcoutDealGoodsdt.get("REMARK1").toString());
			}
			
			ParamMap paramMap = new ParamMap();
			
			paramMap.put("goodsCode", paCopnAlcoutDealGoodsdt.get("GOODS_CODE"));
			paramMap.put("paGroupCode", "05"); // 쿠팡
			
			//정보고시 조회
			paGoodsOfferList = paCopnGoodsService.selectPaCopnGoodsOfferList(paramMap);
			//쿠팡 매핑 옵션 조회
			paCopnGoodsAttriList = paCopnGoodsService.selectPaCopnGoodsAttriList(paramMap);
			
			oItems.addProperty("originalPrice", paCopnAlcoutDealGoodsdt.get("SALE_PRICE").toString());
			//oItems.addProperty("salePrice", paCopnGoods.getSalePrice() - paCopnGoods.getDcAmt());// 판매 가격
			
			//프로모션 적용 2020.06.15 by jchoi
			double couponPrice = 0;
			
			//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
			if(paPromoTargetList != null && paPromoTargetList.size() > 0) {
				for(PaPromoTarget paPromoTarget : paPromoTargetList) {
					if(paPromoTarget != null) {
						if(!paPromoTarget.getProcGb().equals("D") && paPromoTarget.getGoodsCode().equals(paCopnAlcoutDealGoodsdt.get("GOODS_CODE"))) {
							log.info("### couponPrice : "+paPromoTarget.getDoCost());
							couponPrice += paPromoTarget.getDoCost();	//할인금액(자동적용쿠폰 + 제휴OUT)
						}
					}
				}
			}
			//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
			
			//판매가 - ARS할인 - 일시불할인 - 프로모션할인
//			log.info("### originalPrice : "+paCopnGoods.getSalePrice());
//			log.info("### DcAmt : "+paCopnGoods.getDcAmt());
//			log.info("### LumpSumDcAmt : "+paCopnGoods.getLumpSumDcAmt());
//			log.info("### couponPrice : "+couponPrice);
			oItems.addProperty("salePrice", Integer.parseInt(paCopnAlcoutDealGoodsdt.get("SALE_PRICE").toString()) - Integer.parseInt(paCopnAlcoutDealGoodsdt.get("DC_AMT").toString()) 
								- Integer.parseInt(paCopnAlcoutDealGoodsdt.get("LUMP_SUM_DC_AMT").toString()) - couponPrice);
			
			oItems.addProperty("maximumBuyCount", paCopnAlcoutDealGoodsdt.get("TRANS_ORDER_ABLE_QTY").toString());// 판매가능 수량
			
			//기간제한
			if(paCopnAlcoutDealGoodsdt.get("DIRECT_SHIP_YN").equals("1")){ //직택배일 경우
				oItems.addProperty("maximumBuyForPerson", "1"); //인당최대구매수량
				if(paCopnAlcoutDealGoodsdt.get("CUST_ORD_QTY_CHECK_YN").equals("1")){
					if(!paCopnAlcoutDealGoodsdt.get("CUST_ORD_QTY_CHECK__ERM").equals(0)){
						oItems.addProperty("maximumBuyForPersonPeriod", paCopnAlcoutDealGoodsdt.get("CUST_ORD_QTY_CHECK_TERM").toString());
					} else {
						oItems.addProperty("maximumBuyForPersonPeriod", "0");
					}
				} else { //기간당 살 수 있는 수량 체크가 없다면
					oItems.addProperty("maximumBuyForPersonPeriod", "1");
				}
			} else if(Integer.parseInt(paCopnAlcoutDealGoodsdt.get("ORDER_MAX_QTY").toString()) > 0){
				oItems.addProperty("maximumBuyForPerson", paCopnAlcoutDealGoodsdt.get("ORDER_MAX_QTY").toString());
				oItems.addProperty("maximumBuyForPersonPeriod", "1");
			} else if(paCopnAlcoutDealGoodsdt.get("CUST_ORD_QTY_CHECK_YN").equals("1")){
				oItems.addProperty("maximumBuyForPerson", paCopnAlcoutDealGoodsdt.get("TERM_ORDER_QTY").toString());
				oItems.addProperty("maximumBuyForPersonPeriod", paCopnAlcoutDealGoodsdt.get("CUST_ORD_QTY_CHECK_TERM").toString());
			} else {
				oItems.addProperty("maximumBuyForPerson", "0");
				oItems.addProperty("maximumBuyForPersonPeriod", "1");
			}
			if("1".equals(paCopnAlcoutDealGoodsdt.get("INSTALL_YN"))||"1".equals(paCopnAlcoutDealGoodsdt.get("CREATE_YN"))){
				oItems.addProperty("outboundShippingTimeDay", 10); //설치 상품이거나 주문 제작 상품은 기준 출고일 10일
			} else if(paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("100981") || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("101192") || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("101329") || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("101577")
				   || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("100251") || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("100531") || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("100551") || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("100558")
				   || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("100593") || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("100646") || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("100664") || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("101278")
				   || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("101189") || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("103481") || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("104494") || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("104224")
				   || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("104214") || paCopnAlcoutDealGoodsdt.get("ENTP_CODE").equals("100044")) {
				oItems.addProperty("outboundShippingTimeDay", 5); //특정 업체의 경우 5일로 세팅 긴급배포 2020.09.01
			} else {
				oItems.addProperty("outboundShippingTimeDay", 2); //일반 상품들 기존 출고일 3일 -> 08/05부로 2일로 수정
			}
			oItems.addProperty("unitCount", 0); //단위 수량, 개당 가격 필요하지 않을 경우 '0'
			
			if("1".equals(paCopnAlcoutDealGoodsdt.get("ADULT_YN"))){
				oItems.addProperty("adultOnly", "ADULT_ONLY");
			} else {
				oItems.addProperty("adultOnly", "EVERYONE");
			}
			if("1".equals(paCopnAlcoutDealGoodsdt.get("TAX_YN"))){
				oItems.addProperty("taxType", "TAX");
			} else {
				oItems.addProperty("taxType", "FREE");
			}
			oItems.addProperty("parallelImported", "NOT_PARALLEL_IMPORTED"); // 병행수입여부
			oItems.addProperty("overseasPurchased", "NOT_OVERSEAS_PURCHASED"); // 해외 구매대행 여부
			oItems.addProperty("pccNeeded", "false"); // 개인통관번호
			oItems.addProperty("externalVendorSku", paCopnAlcoutDealGoodsdt.get("GOODS_CODE").toString() + paCopnAlcoutDealGoodsdt.get("GOODSDT_CODE").toString()); // 업체상품코드(SK스토아 단품코드)
			oItems.addProperty("barcode", ""); // 바코드, 상품에 부착된 유효한 표준상품 코드
			oItems.addProperty("emptyBarcode", true);
			oItems.addProperty("emptyBarcodeReason", systemService.getSysdatetimeToString());
			oItems.addProperty("modelNo", "");
			
			jCertifications = new JsonArray();
			oCertifications = new JsonObject();
			
//			oCertifications.addProperty("certificationType", "PRESENTED_IN_DETAIL_PAGE"); //인증정보 Type, 매핑 완료 시 수정 필요
//			oCertifications.addProperty("certificationCode", ""); //상품인증 정보 코드
			oCertifications.addProperty("certificationType", paCopnAlcoutDealGoodsdt.get("CERTIFICATION_TYPE").toString()); //인증정보 Type, 매핑 완료 시 수정 필요
			oCertifications.addProperty("certificationCode", paCopnAlcoutDealGoodsdt.get("CERTIFICATION_CODE").toString()); //상품인증 정보 코드
			
			jCertifications.add(oCertifications);
			
			jImages = new JsonArray();
			imageCount = 0;
			if(!"".equals(paCopnAlcoutDealGoodsdt.get("IMAGE_P"))){
				oPImages = new JsonObject();
				oPImages.addProperty("imageOrder", imageCount);
				oPImages.addProperty("imageType", "REPRESENTATION");
				oPImages.addProperty("vendorPath", image_address + paCopnAlcoutDealGoodsdt.get("IMAGE_URL") + paCopnAlcoutDealGoodsdt.get("IMAGE_P"));
				
				jImages.add(oPImages);
				++imageCount;
			}
			if(!"".equals(paCopnAlcoutDealGoodsdt.get("IMAGE_AP"))){
				oApImages = new JsonObject();
				oApImages.addProperty("imageOrder", imageCount);
				oApImages.addProperty("imageType", "DETAIL");
				oApImages.addProperty("vendorPath", image_address + paCopnAlcoutDealGoodsdt.get("IMAGE_URL") + paCopnAlcoutDealGoodsdt.get("IMAGE_AP"));
				
				jImages.add(oApImages);
				imageCount += 1;
			}
			if(!"".equals(paCopnAlcoutDealGoodsdt.get("IMAGE_BP"))){
				oApImages = new JsonObject();
				oApImages.addProperty("imageOrder", imageCount);
				oApImages.addProperty("imageType", "DETAIL");
				oApImages.addProperty("vendorPath", image_address + paCopnAlcoutDealGoodsdt.get("IMAGE_URL") + paCopnAlcoutDealGoodsdt.get("IMAGE_BP"));
				
				jImages.add(oApImages);
				imageCount += 1;
			}
			if(!"".equals(paCopnAlcoutDealGoodsdt.get("IMAGE_CP"))){
				oApImages  = new JsonObject();
				oApImages.addProperty("imageOrder", imageCount);
				oApImages.addProperty("imageType", "DETAIL");
				oApImages.addProperty("vendorPath", image_address + paCopnAlcoutDealGoodsdt.get("IMAGE_URL") + paCopnAlcoutDealGoodsdt.get("IMAGE_CP"));
				
				jImages.add(oApImages);
				imageCount += 1;
			}
			if(!"".equals(paCopnAlcoutDealGoodsdt.get("IMAGE_DP"))){
				oApImages  = new JsonObject();
				oApImages.addProperty("imageOrder", imageCount);
				oApImages.addProperty("imageType", "DETAIL");
				oApImages.addProperty("vendorPath", image_address + paCopnAlcoutDealGoodsdt.get("IMAGE_URL") + paCopnAlcoutDealGoodsdt.get("IMAGE_DP"));
				jImages.add(oApImages);
			}
			
			//정보고시
			jNotices = new JsonArray();
			for(int k=0; k<paGoodsOfferList.size(); k++){
				paCopnGoodsOffer = paGoodsOfferList.get(k);
						
				oNotices = new JsonObject();
				oNotices.addProperty("noticeCategoryDetailName", paCopnGoodsOffer.getPaOfferCodeName());//상품고시정보카테고리상세명
				oNotices.addProperty("noticeCategoryName", paCopnGoodsOffer.getPaOfferTypeName());
				oNotices.addProperty("content", paCopnGoodsOffer.getPaOfferExt());
				jNotices.add(oNotices);
			}
			
			//옵션, 속성 개수 만큼 반복문 돌리기
			jAttributes = new JsonArray();
			optionName = "";
			
			if(paCopnGoodsAttriList.size() == 0){
				oAttributes = new JsonObject();
				oAttributes.addProperty("attributeValueName", "");
				jAttributes.add(oAttributes);
			} else {
				for(int k=0; k<paCopnGoodsAttriList.size(); k++){
					optionValue = "";
					paCopnGoodsAttri = paCopnGoodsAttriList.get(k);
					
					if(paCopnGoodsAttri.getAttributeTypeMapping()!=null || paCopnGoodsAttri.getAttributeValueName()!=null) {
						if(paCopnGoodsAttri.getAttributeTypeMapping() != null){
							if("색상/크기/무늬/형태".equals(paCopnGoodsAttri.getAttributeTypeName())) {
								optionValue = paCopnAlcoutDealGoodsdt.get("GOODSDT_INFO")+"/";
							}else {
								mappingValue = paCopnGoodsAttri.getAttributeTypeMapping().split(",");
								
								for(int l=0; l<mappingValue.length; l++){
									switch(mappingValue[l]) {
									case "색상":
										optionValue = optionValue.concat(paCopnAlcoutDealGoodsdt.get("COLOR_NAME")+"/");
										break;
									case "사이즈":
										optionValue = optionValue.concat(paCopnAlcoutDealGoodsdt.get("SIZE_NAME")+"/");
										break;
									case "무늬": //미사용
										optionValue = optionValue.concat(paCopnAlcoutDealGoodsdt.get("PATTERN_NAME")+"/");
										break;
									case "형태": //미사용
										optionValue = optionValue.concat(paCopnAlcoutDealGoodsdt.get("FROM_NAME")+"/");
										break;
									case "기타":
										optionValue = optionValue.concat(paCopnAlcoutDealGoodsdt.get("OTHER_TEXT")+"/");
										break;
									}
								}
							}
							if(!"".equals(optionValue)){
								if("".equals(paCopnAlcoutDealGoodsdt.get("EXPOSURE_GOODS_NM").toString()) || paCopnAlcoutDealGoodsdt.get("EXPOSURE_GOODS_NM").toString() == null ) {
									optionValue = ComUtil.subStringBytes(paCopnAlcoutDealGoodsdt.get("GOODS_NAME").toString(), 60) + " " + ComUtil.subStringBytes(optionValue.substring(0, optionValue.length()-1).toString(), 28); //마지막 문자 자르기
								} else {
									optionValue =ComUtil.subStringBytes(paCopnAlcoutDealGoodsdt.get("EXPOSURE_GOODS_NM").toString(), 60) + " " +ComUtil.subStringBytes(optionValue.substring(0, optionValue.length()-1).toString(), 28); //마지막 문자 자르기
								}
							}
						}
						oAttributes = new JsonObject();
						oAttributes.addProperty("attributeTypeName", paCopnGoodsAttri.getAttributeTypeName());
						if(paCopnGoodsAttri.getAttributeValueName()==null) {
							oAttributes.addProperty("attributeValueName", optionValue);		
							optionName = optionName+" "+optionValue;
						} else {
							oAttributes.addProperty("attributeValueName", paCopnGoodsAttri.getAttributeValueName());
							optionName = optionName+" "+paCopnGoodsAttri.getAttributeValueName();
						}
						oAttributes.addProperty("exposed", "EXPOSED");
						oAttributes.addProperty("editable", "true");
						jAttributes.add(oAttributes);
					}
				}
			}
			if(optionName.equals("")) {
				oItems.addProperty("itemName", "단일상품"); // 업체상품옵션명, 각각의 아이템에 중복되지 않도록 기입 (사이트에 노출되는 옵션명X, 구매옵션에 따라 변경될 수 있다)
			} else {
				oItems.addProperty("itemName", optionName.substring(1, optionName.length())); // 업체상품옵션명, 각각의 아이템에 중복되지 않도록 기입 (사이트에 노출되는 옵션명X, 구매옵션에 따라 변경될 수 있다)
			}
			// 기술서
			
			JsonArray jcon = new JsonArray();
			jContents = new JsonArray();
			
			oContents = new JsonObject();
			oContents.addProperty("contentsType", "TEXT");
			
			jContentDetails = new JsonArray();
			
			oContentDetails = new JsonObject();
			
			//기술서 style 제거
			if(!"".equals(ComUtil.NVL(paCopnGoods.getDescribeExt()))) {
				String attributesToRemove = "width|height|style";
				String orgString = paCopnGoods.getDescribeExt();
				String tmpString = ComUtil.cleanHtmlFragment(orgString, attributesToRemove);
				String tmp2String = ComUtil.parse(tmpString);
				paCopnAlcoutDealGoodsdt.put("DESCRIBE_EXT", tmp2String);
			}			
			
			oContentDetails.addProperty("content", paCopnGoods.getDescribeExt()); // 기술서 경로
			oContentDetails.addProperty("detailType", "TEXT");
			
			jContentDetails.add(oContentDetails);

			jContents.add(jContentDetails);
			oContents.add("contentDetails", jContentDetails);
			jcon.add(oContents);

			oItems.add("certifications", jCertifications);
			oItems.add("images", jImages);
			oItems.add("notices", jNotices);
			oItems.add("attributes", jAttributes);
			oItems.add("contents", jcon);
			
			jProduct.addProperty("offerCondition", "NEW");
			
			jItems.add(oItems);
		}
		jProduct.add("items", jItems);
		
		jProduct.addProperty("manufacture", paCopnGoods.getMakecoName()); // 제조사

		return jProduct;
	}
	
	@ApiOperation(value = "제휴OUT 딜 상품 정보 조회", notes = "제휴OUT 딜 상품 정보 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealGoodsList(HttpServletRequest request,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value = "procId", required = false, defaultValue = "PACOPN") String procId
			)throws Exception {
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime = "";
		String duplicateCheck = "";
		String prg_id = "";
		String request_type = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		ResponseEntity<?> responseMsg = null;
		
		List<HashMap<String, Object>> vendorIdSelectList = new ArrayList<>();
		List<HashMap<String, Object>> productIdSelectList = new ArrayList<>();
		List<HashMap<String, Object>> approvalSelectList = new ArrayList<>();
		
		HashMap<String, Object> vendorIdSelect = new HashMap<String, Object>();
		HashMap<String, Object> productIdSelect = new HashMap<String, Object>();
		HashMap<String, Object> approvalSelect = new HashMap<String, Object>();
		
		log.info("===== 제휴OUT 딜 상품 정보 조회  API Start=====");
		log.info("01.API 기본정보 세팅");

		prg_id = "IF_PACOPNAPI_01_002";
		request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
    	paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
    	paramMap.put("startDate", dateTime);
    	paramMap.put("modCase", Constants.PA_COPN_MOD_CASE_MODIFY);
    	
    	try{
			log.info("처리자ID : " + procId.toUpperCase());

			paramMap.put("modifyId", procId);
			
			log.info("03_1.제휴OUT 딜 옵션코드, 벤더아이디 저장 ");
			vendorIdSelectList = paCopnGoodsService.selectAlcoutDealEmptyVendorId();
			for(int i = 0; i < vendorIdSelectList.size(); i++) {
				vendorIdSelect = vendorIdSelectList.get(i);
				responseMsg = alcoutDealPaOptionCodeSave(request,vendorIdSelect.get("ALCOUT_DEAL_CODE").toString(), vendorIdSelect.get("SELLER_PRODUCT_ID").toString(), "vendorItemId", procId, vendorIdSelect.get("PA_CODE").toString());
				paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				log.info("/goods-list");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			log.info("03_2.제휴OUT 딜 노출상품아이디 저장");
			productIdSelectList = paCopnGoodsService.selectAlcoutDealEmptyProductId();
			for(int i = 0; i < productIdSelectList.size(); i++) {
				productIdSelect = productIdSelectList.get(i);
				responseMsg = alcoutDealPaOptionCodeSave(request,productIdSelect.get("ALCOUT_DEAL_CODE").toString(), productIdSelect.get("SELLER_PRODUCT_ID").toString(), "productId", procId, productIdSelect.get("PA_CODE").toString());
				paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				log.info("/goods-list");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			log.info("04.제휴OUT 딜 등록상품 승인 완료대상 조회");
			approvalSelectList = paCopnGoodsService.selectRegisterAlcoutDealEmpty();
			for(int i = 0; i < approvalSelectList.size(); i++) {
				approvalSelect = approvalSelectList.get(i);
				responseMsg = alcoutDealPaOptionCodeSave(request,approvalSelect.get("ALCOUT_DEAL_CODE").toString(), approvalSelect.get("SELLER_PRODUCT_ID").toString(), "approvalStatus",procId, approvalSelect.get("PA_CODE").toString());
				paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				log.info("/goods-list");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
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
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			log.info("===== 제휴OUT 딜 상품 정보 조회 API End=====");
    	}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 제휴OUT 딜 상품 정보 저장
	 * @param request
	 * @param goodsCode
	 * @param sellerProductId
	 * @param status
	 * @param procId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "제휴OUT 딜 상품 정보 저장", notes = "제휴OUT 딜 상품 정보 저장", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-save", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealPaOptionCodeSave(HttpServletRequest request,
			@ApiParam(name = "alcoutDealCode", value = "제휴OUT딜코드", defaultValue = "") @RequestParam(value = "alcoutDealCode", required = true) String alcoutDealCode,//true
			@ApiParam(name = "sellerProductId", value = "등록상품ID", defaultValue = "") @RequestParam(value = "sellerProductId", required = true) String sellerProductId,//true
			@ApiParam(name = "status", value = "상태", defaultValue = "") @RequestParam(value = "status", required = true) String status,//true
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value = "procId", required = true, defaultValue = "PACOPNDEAL") String procId,//true
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode", required = true) String paCode) throws Exception{//true
			
		String rtnMsg = Constants.SAVE_SUCCESS;
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		JsonObject responseObject = null;
		ParamMap paramMap = new ParamMap();
		HashMap<String, Object> dtMap = new HashMap<String, Object>();
		PaGoodsTransLog paGoodsTransLog = null;
		String dateTime = "";
		JsonArray jsonPaOption = new JsonArray(); // 상품의 옵션 코드를 저장하기 위해
		int itemSize = 0;
		String paOptionCode = "";
		String remark = "";
		String tempCode = "";
		String goodsCode = "";
		String goodsdtCode = "";
		String approvalStatus = "";
		String duplicateCheck = "";
		
		String prg_id = "IF_PACOPNAPI_01_002";
		
		try{
			log.info("===== 제휴OUT딜 상품정보조회 API START=====");
			dateTime = systemService.getSysdatetimeToString();
			
			log.info("01.API 기본정보 세팅");
			paramMap.put("apiCode", prg_id);
        	paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
        	paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
        	paramMap.put("startDate", dateTime);
        	
        	if(paCode.equals(Constants.PA_COPN_BROAD_CODE)){
    			paramMap.put("paName", Constants.PA_BROAD);
    		} else {
    			paramMap.put("paName", Constants.PA_ONLINE);
    		}
        	
        	apiInfo = systemService.selectPaApiInfo(paramMap);
        	apiInfo.put("apiInfo", paramMap.getString("apiCode"));
        	
        	log.info("02.API 중복실행검사");
		    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			log.info("03.제휴OUT 딜 상품 정보 조회 API START : " + status);
			responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#sellerProductId#", sellerProductId)));
			
			if(responseObject != null){
				if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())){
					paramMap.put("code", "200");
					jsonPaOption = responseObject.get("data").getAsJsonObject().get("items").getAsJsonArray();
					itemSize = jsonPaOption.size();
					paramMap.put("successCode", "200");
					paramMap.put("transSuccessMsg", responseObject.get("code").getAsString());
					
					if(status.equals("vendorItemId")){
						log.info("04-1.제휴OUT 딜 상품 옵션코드 UPDATE");
						for(int i=0; i<itemSize; i++){
							
							tempCode = jsonPaOption.get(i).getAsJsonObject().get("externalVendorSku").getAsString();
							goodsCode = tempCode.substring(0, 8);
							goodsdtCode = tempCode.substring(8, 11);
							
							if(!jsonPaOption.get(i).getAsJsonObject().get("vendorItemId").toString().equals("null")) {
								paOptionCode = jsonPaOption.get(i).getAsJsonObject().get("vendorItemId").getAsString();
								remark = jsonPaOption.get(i).getAsJsonObject().get("sellerProductItemId").getAsString();
								
								
								dtMap.put("modifyDate", 	DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								dtMap.put("modifyId", 		procId);
								dtMap.put("alcoutDealCode",	alcoutDealCode);
								dtMap.put("goodsCode",		goodsCode);
								dtMap.put("paGoodsCode",	sellerProductId);
								dtMap.put("paOptionCode",	paOptionCode);
								dtMap.put("goodsdtCode",	goodsdtCode);
								dtMap.put("remark1",		remark);
								dtMap.put("paCode",			paCode);
								
								rtnMsg = paCopnGoodsService.saveAlcoutDealGoodsdtMappingPaOptionCodeTx(dtMap);
								
								if(!rtnMsg.equals("000000")){
									paramMap.put("code","404");
									paramMap.put("message",rtnMsg);
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								} else {
									paramMap.put("code","200");
									paramMap.put("message", "OK");
								}
							} else {
								paramMap.put("successCode", "480");
								paramMap.put("transSuccessMsg", getMessage("pa.copn_not_approval_status"));
							}
						}
						//전송관리 테이블 저장
						paGoodsTransLog = new PaGoodsTransLog();
						paGoodsTransLog.setGoodsCode(goodsCode);
						paGoodsTransLog.setPaCode(paCode);
						paGoodsTransLog.setItemNo(sellerProductId);
						paGoodsTransLog.setRtnCode(paramMap.getString("successCode"));
						paGoodsTransLog.setRtnMsg(paramMap.getString("transSuccessMsg"));
						paGoodsTransLog.setSuccessYn(paramMap.getString("successCode").equals("200")==true?"1":"0");
						paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsTransLog.setProcId(procId);
						paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
					} else if(status.equals("productId")){
						log.info("04-2.제휴OUT 딜 노출상품 id UPDATE");
						for(int i=0; i<itemSize; i++){
							
							tempCode = jsonPaOption.get(i).getAsJsonObject().get("externalVendorSku").getAsString();
							goodsCode = tempCode.substring(0, 8);
							goodsdtCode = tempCode.substring(8, 11);
							
							if(!jsonPaOption.get(i).getAsJsonObject().get("vendorItemId").toString().equals("null")) {
								paOptionCode = jsonPaOption.get(i).getAsJsonObject().get("vendorItemId").getAsString();
								remark = responseObject.get("data").getAsJsonObject().get("productId").getAsString();
								
								
								dtMap.put("modifyDate", 	DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								dtMap.put("modifyId", 		procId);
								dtMap.put("alcoutDealCode",	alcoutDealCode);
								dtMap.put("goodsCode",		goodsCode);
								dtMap.put("paGoodsCode",	sellerProductId);
								dtMap.put("paOptionCode",	paOptionCode);
								dtMap.put("goodsdtCode",	goodsdtCode);
								dtMap.put("remark2",		remark);
								dtMap.put("paCode",			paCode);
								rtnMsg = paCopnGoodsService.saveAlcoutDealGoodsdtMappingPaOptionCodeTx(dtMap);
								
								if(!rtnMsg.equals("000000")){
									paramMap.put("code","404");
									paramMap.put("message",rtnMsg);
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								} else {
									paramMap.put("code","200");
									paramMap.put("message", "OK");
								}
							} else {
								paramMap.put("successCode", "480");
								paramMap.put("transSuccessMsg", getMessage("pa.copn_not_approval_status"));
							}
						}
						//전송관리 테이블 저장
						paGoodsTransLog = new PaGoodsTransLog();
						paGoodsTransLog.setGoodsCode(goodsCode);
						paGoodsTransLog.setPaCode(paCode);
						paGoodsTransLog.setItemNo(sellerProductId);
						paGoodsTransLog.setRtnCode(paramMap.getString("successCode"));
						paGoodsTransLog.setRtnMsg(paramMap.getString("transSuccessMsg"));
						paGoodsTransLog.setSuccessYn(paramMap.getString("successCode").equals("200")==true?"1":"0");
						paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsTransLog.setProcId(procId);
						paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
					} else {
						log.info("04-3.제휴OUT 딜 상품 상태 UPDATE");
						for(int i=0; i<itemSize; i++){
							
							tempCode = jsonPaOption.get(i).getAsJsonObject().get("externalVendorSku").getAsString();
							goodsCode = tempCode.substring(0, 8);
							goodsdtCode = tempCode.substring(8, 11);
							
							if(!jsonPaOption.get(i).getAsJsonObject().get("vendorItemId").toString().equals("null")) {
								paOptionCode = jsonPaOption.get(i).getAsJsonObject().get("vendorItemId").getAsString();
								approvalStatus = responseObject.get("data").getAsJsonObject().get("statusName").getAsString();
								
								dtMap.put("modifyDate", 	DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								dtMap.put("modifyId", 		procId);
								dtMap.put("alcoutDealCode",	alcoutDealCode);
								dtMap.put("goodsCode",		goodsCode);
								dtMap.put("paGoodsCode",	sellerProductId);
								dtMap.put("paOptionCode",	paOptionCode);
								dtMap.put("goodsdtCode",	goodsdtCode);
								dtMap.put("approvalStatus",	approvalStatus);
								dtMap.put("paCode",			paCode);
								
								rtnMsg = paCopnGoodsService.saveAlcoutDealGoodsdtMappingPaOptionCodeTx(dtMap);
								
								if(!rtnMsg.equals("000000")){
									paramMap.put("code","404");
									paramMap.put("message",rtnMsg);
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								} else {
									paramMap.put("code","200");
									paramMap.put("message", "OK");
								}
							} else {
								paramMap.put("successCode", "480");
								paramMap.put("transSuccessMsg", getMessage("pa.copn_not_approval_status"));
							}
						}
						//전송관리 테이블 저장
						paGoodsTransLog = new PaGoodsTransLog();
						paGoodsTransLog.setGoodsCode(goodsCode);
						paGoodsTransLog.setPaCode(paCode);
						paGoodsTransLog.setItemNo(sellerProductId);
						paGoodsTransLog.setRtnCode(paramMap.getString("code"));
						paGoodsTransLog.setRtnMsg(responseObject.get("code").getAsString());
						paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
						paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsTransLog.setProcId(procId);
						paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
					}
				} else {
					paramMap.put("code","500");
					paramMap.put("message",responseObject.get("message"));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
			} else {
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			log.info("05.제휴OUT 딜 상품 기본 정보 저장 API END");
		}catch(Exception e){
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			System.out.println(e.getMessage());
		}finally{
			try {
				paramMap.put("message", goodsCode + "|" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 제휴OUT 딜 상품 정보 저장 API End=====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "제휴OUT 딜 상품수정", notes = "제휴OUT 딜 상품수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealGoodsModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode", required = false) String paCode,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value = "procId", required = false, defaultValue = "PACOPN") String procId,
			@RequestParam(value="searchTermGb"	, required=false, defaultValue = "") String searchTermGb,
			@ApiParam(name = "alcoutDealCode", value = "제휴OUT딜 코드", defaultValue = "") @RequestParam(value = "alcoutDealCode", required = false) String alcoutDealCode,
			@ApiParam(name = "modifyYn", value = "수정여부", defaultValue = "0") @RequestParam(value = "modifyYn", required = false) String modifyYn
			)throws Exception {
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime = "";
		String duplicateCheck = "";
		String prg_id = "";
		String request_type = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		ResponseEntity<?> responseMsg = null;
		
		List<HashMap<String, Object>> modifyAlcoutDealList = new ArrayList<>();  //수정대상 딜 목록
		List<HashMap<String, Object>> paCopnAlcoutDealGoodsdtList = new ArrayList<HashMap<String, Object>>();
		
		List<HashMap<String, Object>> paCopnNotExistsGoodsdtList = new ArrayList<>(); // 수정대상 추가 단품목록
		List<PaCopnGoodsVO> psCopnGoodsDescribeList = null; // 기술서 목록
		
		List<PaCopnGoodsVO> paCopnGoodsList = null;
		PaCopnGoodsVO paCopnGoods = null;
		
        List<PaPromoTarget> paPromoTargetList = null;
        
        PaGoodsTransLog paGoodsTransLog = null;
        String apiKeys[]=null;
        
        JsonObject responseObject = null;
		
		log.info("===== 제휴OUT 딜 상품수정 API Start=====");
		log.info("01.API 기본정보 세팅");

		prg_id = "IF_PACOPNAPI_01_012";
		request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
    	paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
    	paramMap.put("startDate", dateTime);
    	paramMap.put("modCase", Constants.PA_COPN_MOD_CASE_MODIFY);
    	
    	try{
    		log.info("상품코드 : " + goodsCode);
			log.info("제휴사코드: " + paCode);
			log.info("처리자ID : " + procId.toUpperCase());

			log.info("02.API 중복실행검사");
            //= 중복 실행 Check
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
		    	if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});		    	
		    }
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			
			if(!"".equals(alcoutDealCode)){
				paramMap.put("alcoutDealCode", alcoutDealCode);	
			}
			
			paramMap.put("modify", "1"); /*상품 옵션 수정시에는 딜 대표상품정보를 가져와야 하므로 플래그 설정*/
			
			/* 제휴OUT딜 상품 정보 조회, 저장 */
			responseMsg = alcoutDealGoodsList(request, "PACOPNDEAL");
			
			log.info("03.제휴OUT 딜 옵션별 판매중지");
			paCopnGoodsList = paCopnGoodsService.selectPaCopnAlcoutDealGoodsSaleStopList(paramMap);
			for(int i = 0; i < paCopnGoodsList.size(); i++) {
				paCopnGoods = paCopnGoodsList.get(i);
				responseMsg = alcoutDealGoodsSellStop(request, paCopnGoods.getGoodsCode(), paCopnGoods.getPaCode(), procId);
				paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				log.info("/goods-sell-stop");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}

			log.info("04.제휴OUT 딜 옵션별 판매재개");
			paCopnGoodsList = paCopnGoodsService.selectPaCopnAlcoutDealGoodsSaleRestartList(paramMap);
			for(int i = 0; i < paCopnGoodsList.size(); i++) {
				paCopnGoods = paCopnGoodsList.get(i);
				responseMsg = alcoutDealGoodsSellRestart(request, paCopnGoods.getGoodsCode(), paCopnGoods.getPaCode(), procId);
				paramMap.put("code",PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				log.info("/goods-sell-restart");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			log.info("05.제휴OUT딜 상품옵션 수정대상 조회");
			modifyAlcoutDealList = paCopnGoodsService.selectPaCopnModifyAlcoutDealList(paramMap); //수정대상 딜 목록
			if(modifyAlcoutDealList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			if(modifyAlcoutDealList.size() > 0 && modifyYn.equals("1")){
				for(HashMap<String, Object> paCopnAlcoutDealInfo : modifyAlcoutDealList){
					
					paramMap.put("alcoutDealCode", 			paCopnAlcoutDealInfo.get("ALCOUT_DEAL_CODE"));
					
					log.info("05-1.추가 단품 제휴OUT 딜 상품매핑 테이블에 추가");
					paCopnNotExistsGoodsdtList = paCopnGoodsService.selectPaCopnNotExistsGoodsdtList(paramMap);
					for(HashMap<String, Object> paCopnNotExistsGoodsdt : paCopnNotExistsGoodsdtList) {
						paCopnNotExistsGoodsdt.put("alcoutDealCode", paramMap.get("alcoutDealCode"));
						paCopnNotExistsGoodsdt.put("paGoodsCode",	 paCopnAlcoutDealInfo.get("PA_GOODS_CODE"));
						paCopnNotExistsGoodsdt.put("paCode", 		 paramMap.get("paCode"));
						paCopnGoodsService.insertPaCopnNotExistsGoodsdtTx(paCopnNotExistsGoodsdt);
					}
					
					//수정대상 제휴OUT 딜 대표 상품정보 조회
					paCopnGoods = paCopnGoodsService.selectPaCopnAlcoutDealGoodsInfo(paramMap);
					if(paCopnGoods == null){
						paramMap.put("code", "404");
						paramMap.put("message", getMessage("pa.not_exists_process_list"));
						continue;
					}
					
					//수정대상 기술서 조회
					psCopnGoodsDescribeList = paCopnGoodsService.selectPaCopnAlcoutDealGoodsDescribe(paramMap);
					
					if(paCopnGoods.getPaCode().equals(Constants.PA_COPN_BROAD_CODE)){
						paramMap.put("paName", Constants.PA_BROAD);
						paramMap.put("paCopnLoginId", ConfigUtil.getString("PA_COPN_BROAD_LOGIN_ID"));
					} else {
						paramMap.put("paName", Constants.PA_ONLINE);
						paramMap.put("paCopnLoginId", ConfigUtil.getString("PA_COPN_ONLINE_LOGIN_ID"));
					}
					
					String strDescribeExt = "";
					
					/* 기술서 통합 */
					for(PaCopnGoodsVO psCopnGoodsDescribe : psCopnGoodsDescribeList) {
						if(!("").equals(psCopnGoodsDescribe.getDescribeExt()) && psCopnGoodsDescribe.getDescribeExt() != null) {
							strDescribeExt +=  psCopnGoodsDescribe.getGoodsName() + "<br />" + psCopnGoodsDescribe.getDescribeExt() + "<br />";
						}
					}
					
					//기술서
					if("".equals(paCopnGoods.getCollectImage()) || paCopnGoods.getCollectImage() == null) {
						paCopnGoods.setDescribeExt("<div align='center'><img alt='' src='" + paCopnGoods.getTopImage() + "' /><br /><br /><br />" + strDescribeExt.replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + paCopnGoods.getBottomImage() + "' /></div>");				
					}else {
						paCopnGoods.setDescribeExt("<div align='center'><img alt='' src='" + paCopnGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + paCopnGoods.getCollectImage() + "' /><br /><br /><br />" + strDescribeExt.replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + paCopnGoods.getBottomImage() + "' /></div>");
					}
					paramMap.put("paGroupCode", paCopnGoods.getPaGroupCode());

					if(paCopnGoods.getSellerProductId()==null){
						paramMap.put("code","411");
						paramMap.put("message",getMessage("pa.not_exists_goods", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}
					if(paCopnGoods.getDescribeExt() == null){
						paramMap.put("code", "420");
						paramMap.put("message", getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}
					//수정대상 제휴OUT 딜 상품/단품조회
					paCopnAlcoutDealGoodsdtList = paCopnGoodsService.selectPaCopnAlcoutDealGoodsdtInfoList(paramMap);

					//수정대상 제휴OUT 딜 상품 즉시할인쿠폰 프로모션
					paPromoTargetList = paPromoTargetAlcoutDealInfoSetting(paramMap); //프로모션 별 운영관리 기능 효율화(REQ_PRM_040) 
					
					apiInfo = systemService.selectPaApiInfo(paramMap);
					apiInfo.put("apiInfo", paramMap.getString("apiCode"));
					apiKeys = apiInfo.get(paramMap.getString("paName")).split(";"); 
					
					if(paCopnGoods.getApprovalStatus().equals("05") || paCopnGoods.getApprovalStatus().equals("10") || paCopnGoods.getApprovalStatus().equals("15")|| paCopnGoods.getApprovalStatus().equals("25")) { // 승인 반려상태
						paramMap.put("code","480");
						paramMap.put("message",getMessage("pa.copn_not_approval_status", new String[] {"상품코드 : " + goodsCode}));
						log.info("code : "+paramMap.get("code"));
						log.info("Message : "+paramMap.get("message"));
						//전송관리 테이블 저장
						paGoodsTransLog = new PaGoodsTransLog();
						paGoodsTransLog.setGoodsCode(paCopnGoods.getGoodsCode());
						paGoodsTransLog.setPaCode(paCopnGoods.getPaCode());
						paGoodsTransLog.setItemNo(paCopnGoods.getSellerProductId()); // 상품코드
						paGoodsTransLog.setRtnCode(paramMap.getString("code"));
						paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
						paGoodsTransLog.setSuccessYn("0");
						paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsTransLog.setProcId(procId);
						paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
					} else {

						if(!paCopnGoods.getPaSaleGb().equals("10")) {
							JsonObject root = new JsonObject();
							root.add("insert", createAlcoutDealProductJson(paCopnAlcoutDealInfo, paCopnGoods, paCopnAlcoutDealGoodsdtList, paPromoTargetList, apiKeys[0], paramMap.getString("paCopnLoginId"), paramMap));
							responseObject = new JsonObject();
							
							log.info("06-1. 쿠팡 제휴OUT 딜 상품정보수정 API 호출");
								responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0]))
								, request_type, new GsonBuilder().create().toJson(root.get("insert")));
								
							if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())) {
								paramMap.put("code", "200");
								paramMap.put("message", responseObject.get("code").getAsString());
								paramMap.put("resultCode", "200");
								paramMap.put("resultMsg","OK");
								
								//전송관리 테이블 저장
								paGoodsTransLog = new PaGoodsTransLog();
								paGoodsTransLog.setGoodsCode(paCopnGoods.getGoodsCode());
								paGoodsTransLog.setPaCode(paCopnGoods.getPaCode());
								paGoodsTransLog.setItemNo(paCopnGoods.getSellerProductId()); // 상품코드
								paGoodsTransLog.setRtnCode(paramMap.getString("code"));
								paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
								paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
								paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paGoodsTransLog.setProcId(procId);
								paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
								
								log.info("06-2.제휴사 제휴OUT 딜 상품정보 저장");
								// TPACOPNGOODS에 결과 UPDATE
								paCopnGoods.setSellerProductId(responseObject.get("data").getAsString()); // 쿠팡 상품번호 
								paCopnAlcoutDealInfo.put("alcoutDealCode", 	paramMap.getString("alcoutDealCode"));
								paCopnAlcoutDealInfo.put("paGoodsCode", 	responseObject.get("data").getAsString());
								paCopnAlcoutDealInfo.put("paStatus", 		"30");// 입점완료
								paCopnAlcoutDealInfo.put("paCode", 			paramMap.getString("paCode"));
								paCopnAlcoutDealInfo.put("modifyId"	, 	 	"COPNDEAL");
								paCopnAlcoutDealInfo.put("modifyDate",		DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								
								rtnMsg = paCopnGoodsService.savePaCopnAlcoutDealGoodsTx(paCopnAlcoutDealInfo);
								
								if(!rtnMsg.equals("000000")){
									paramMap.put("code","404");
									paramMap.put("message",rtnMsg);
									continue;
								} else {
									paramMap.put("code",paramMap.getString("code"));
									paramMap.put("message","OK");
								}
								
								// 상품 승인상태 정보조회
								//responseMsg = alcoutDealPaOptionCodeSave(request, paramMap.getString("alcoutDealCode"), paCopnGoods.getSellerProductId(), "approvalStatus", procId, paCopnGoods.getPaCode());
								
							} else {
								paramMap.put("code", "500");
								paramMap.put("message", responseObject.get("message").getAsString());
								
								//전송관리 테이블 저장
								paGoodsTransLog = new PaGoodsTransLog();
								paGoodsTransLog.setGoodsCode(paCopnGoods.getGoodsCode());
								paGoodsTransLog.setPaCode(paCopnGoods.getPaCode());
								paGoodsTransLog.setItemNo(paCopnGoods.getSellerProductId()); // 상품코드
								paGoodsTransLog.setRtnCode(paramMap.getString("code"));
								//paGoodsTransLog.setRtnMsg(paramMap.getString("message").substring(0, 2000));
								paGoodsTransLog.setRtnMsg(paramMap.getString("message").length() > 900 ? paramMap.getString("message").substring(0, 900) : paramMap.getString("message"));
								paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
								paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paGoodsTransLog.setProcId(procId);
								
								paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
							}
						}
						paramMap.put("message", paCopnGoods.getGoodsCode() + "|" + paramMap.getString("message"));
						paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
						systemService.insertApiTrackingTx(request,paramMap);
					}
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
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
    		if(!searchTermGb.equals("1")){
    		   if(duplicateCheck.equals("0")){
    			 systemService.checkCloseHistoryTx("end", prg_id);
    		   }    			 
    		}

			log.info("===== 제휴OUT 딜 상품수정 API End=====");
    	}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 제휴OUT딜 상품 판매중지처리
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "제휴OUT 딜 판매중지처리", notes = "제휴OUT딜판매중지처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-sell-stop", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealGoodsSellStop(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode,//true
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,//true
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PACOPNDEAL") String procId) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		JsonObject responseObject = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		PaCopnGoodsdtMappingVO paCopnGoodsMapping = null;
		List<PaCopnGoodsdtMappingVO> paCopnGoodsMappingList = null;
		String vendorItemId = "";
		String apiKeys[]=null;
		
		log.info("===== 제휴OUT 딜 판매중지처리 API Start=====");
		log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PACOPNAPI_01_008";
		String request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		if(paCode.equals(Constants.PA_COPN_BROAD_CODE)){
			paramMap.put("paName", Constants.PA_BROAD);
		} else {
			paramMap.put("paName", Constants.PA_ONLINE);
		}
		
		try{
			responseObject = new JsonObject();
			
			log.info("02.API 중복실행검사");
		    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";"); 
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.제휴OUT 딜  판매중지대상 조회");
			paCopnGoodsMappingList = paCopnGoodsService.selectPaCopnAlcoutDealGoodsProductNo(paramMap);
			
			log.info("04.쿠팡 api호출");
			for(int i = 0 ; i < paCopnGoodsMappingList.size(); i++) {
				paCopnGoodsMapping = paCopnGoodsMappingList.get(i);
				
				vendorItemId = paCopnGoodsMapping.getPaOptionCode();
				
				if(vendorItemId != null) {
					responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0]).replaceAll("#vendorItemId#", vendorItemId)), request_type);
					
					if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())) {
						paramMap.put("code","200");
						paramMap.put("message",responseObject.get("message").getAsString());
					} else {
						paramMap.put("code","500");
						paramMap.put("message",responseObject.get("message").getAsString());
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paCopnGoodsMapping.getGoodsCode());
					paGoodsTransLog.setPaCode(paCode);
					paGoodsTransLog.setItemNo(paCopnGoodsMapping.getSellerProductId()); // 단품코드
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					
					paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
					
					if(paramMap.getString("code").equals("200")) {
						paCopnGoodsMapping.setPaSaleGb("50");
						paCopnGoodsMapping.setModifyId(procId);
						paCopnGoodsMapping.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paCopnGoodsMapping.setTransSaleYn("0");
						
						log.info("05.제휴OUT 딜 옵션별 판매중단 동기화 완료 저장");
						rtnMsg = paCopnGoodsService.savePaCopnAlcoutDealGoodsSellTx(paCopnGoodsMapping);
						
						if(!rtnMsg.equals("000000")){
							paramMap.put("code","404");
							paramMap.put("message",rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code","200");
							paramMap.put("message","OK");
						}
					}
				} else {
					paramMap.put("code","404");
					paramMap.put("message","vendorItemId is null");
				}
			}
			
			if(paCopnGoodsMappingList.size() == 0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
		}catch(Exception e){
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try {
				paramMap.put("message", goodsCode + "|" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 판매중지처리 API End=====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 제휴OUT 딜 판매재개처리
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "제휴OUT 딜 판매재개처리", notes = "제휴OUT 딜 판매재개처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-sell-restart", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealGoodsSellRestart(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode,//true
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode,//true
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PACOPN") String procId) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		JsonObject responseObject = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		PaCopnGoodsdtMappingVO paCopnGoodsMapping = null;
		List<PaCopnGoodsdtMappingVO> paCopnGoodsMappingList = null;
		String vendorItemId = "";
		String apiKeys[]=null;
		
		log.info("===== 제휴OUT 딜 판매재개처리 API Start=====");
		log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PACOPNAPI_01_009";
		String request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		if(paCode.equals(Constants.PA_COPN_BROAD_CODE)){
			paramMap.put("paName", Constants.PA_BROAD);
		} else {
			paramMap.put("paName", Constants.PA_ONLINE);
		}
		
		try{
			responseObject = new JsonObject();
			
			log.info("02.API 중복실행검사");
		    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";"); 
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.제휴OUT 딜 상품 판매재개대상 조회");
			paCopnGoodsMappingList = paCopnGoodsService.selectPaCopnAlcoutDealGoodsProductNo(paramMap);
			
			log.info("04.쿠팡 api호출");
			for(int i = 0 ; i < paCopnGoodsMappingList.size(); i++) {
				paCopnGoodsMapping = paCopnGoodsMappingList.get(i);
				
				vendorItemId = paCopnGoodsMapping.getPaOptionCode();
				
				if(vendorItemId != null) {
					
					responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0]).replaceAll("#vendorItemId#", vendorItemId)), request_type);
					
					if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())) {
						paramMap.put("code","200");
						paramMap.put("message",responseObject.get("message").getAsString());
					} else {
						paramMap.put("code","500");
						paramMap.put("message",responseObject.get("message").getAsString());
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paCopnGoodsMapping.getGoodsCode());
					paGoodsTransLog.setPaCode(paCode);
					paGoodsTransLog.setItemNo(vendorItemId); // 단품코드
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					
					paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
					
					if(paramMap.getString("code").equals("200")) {
						paCopnGoodsMapping.setPaSaleGb("30");
						paCopnGoodsMapping.setModifyId(procId);
						paCopnGoodsMapping.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paCopnGoodsMapping.setTransSaleYn("0");
						
						log.info("05.제휴OUT 딜 옵션별 판매재개 동기화 완료 저장");
						rtnMsg = paCopnGoodsService.savePaCopnAlcoutDealGoodsSellTx(paCopnGoodsMapping);
						
						if(!rtnMsg.equals("000000")){
							paramMap.put("code","404");
							paramMap.put("message",rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code","200");
							paramMap.put("message","OK");
						}
					}
				}
			}
			
			if(paCopnGoodsMappingList.size() == 0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
		}catch(Exception e){
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try {
				paramMap.put("message", goodsCode + "|" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 판매재개처리 API End=====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 제휴OUT 딜 상품 아이템별 수량 변경
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "제휴OUT 딜 상품 아이템별 수량 변경", notes = "제휴OUT 딜 상품 아이템별 수량 변경", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-stock-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealGoodsStockModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode, //true
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode, //true
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PACOPNDEAL") String procId) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		JsonObject responseObject = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		List<PaCopnGoodsdtMappingVO> paCopnGoodsdtMappingList = null;
		PaCopnGoodsdtMappingVO paCopnGoodsMapping = null;
		
		String vendorItemId = "";
		String transOrderAbleQty = "";
		String apiBroadKeys[]=null;
		String apiOnlineKeys[]=null;
		
		log.info("===== 제휴OUT 딜 상품아이템별 수량 변경 API Start=====");
		log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PACOPNAPI_01_006";
		String request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		
		paramMap.put("paNameBroad", Constants.PA_BROAD);
		paramMap.put("paNameOnline", Constants.PA_ONLINE);
		
		try{
			responseObject = new JsonObject();
			
			log.info("02.API 중복실행검사");
		    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiBroadKeys = apiInfo.get(paramMap.getString("paNameBroad")).split(";");
			apiOnlineKeys = apiInfo.get(paramMap.getString("paNameOnline")).split(";");
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.제휴OUT 딜 상품아이템별 수량 변경 대상 조회");
			paCopnGoodsdtMappingList = paCopnGoodsService.selectPaCopnAlcoutDealGoodsdtMappingStockList(paramMap);
			
			log.info("04.쿠팡 api호출");
			for(int i = 0 ; i < paCopnGoodsdtMappingList.size(); i++) {
				paCopnGoodsMapping = paCopnGoodsdtMappingList.get(i);
				
				vendorItemId = paCopnGoodsMapping.getPaOptionCode();
				transOrderAbleQty = paCopnGoodsMapping.getTransOrderAbleQty();
				
				if(vendorItemId != null) {
					if(paCopnGoodsMapping.getPaCode().equals(Constants.PA_COPN_BROAD_CODE)){
						responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paNameBroad"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiBroadKeys[0]).replaceAll("#vendorItemId#", vendorItemId).replaceAll("#quantity#", transOrderAbleQty)), request_type);
					} else {
						responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paNameOnline"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiOnlineKeys[0]).replaceAll("#vendorItemId#", vendorItemId).replaceAll("#quantity#", transOrderAbleQty)), request_type);
					}
					
					if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())) {
						paramMap.put("code","200");
						paramMap.put("message",responseObject.get("message").getAsString());
						
						if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
							paramMap.put("code","404");
							paramMap.put("message",rtnMsg);
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paramMap.put("code","200");
							paramMap.put("message", "OK");
						}
						
					} else {
						paramMap.put("code","500");
						paramMap.put("message",responseObject.get("message").getAsString());
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paCopnGoodsMapping.getGoodsCode());
					paGoodsTransLog.setPaCode(paCopnGoodsMapping.getPaCode());
					paGoodsTransLog.setItemNo(vendorItemId); // 단품코드
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(responseObject.get("message").getAsString());
					paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					
					paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
				} else {
					paramMap.put("code","410");
					paramMap.put("message",getMessage("pa.copn_not_exists_sellerid"));
				}
			}
			if(paCopnGoodsdtMappingList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
		}catch(Exception e){
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try {
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("===== 제휴OUT 딜 상품아이템별 수량 변경 API End=====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 제휴OUT 딜  상품 아이템별 가격 변경
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "제휴OUT 딜  상품 아이템별 가격 변경", notes = "제휴OUT 딜  상품 아이템별 가격 변경", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-price-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealGoodsPriceModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode, //true
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode, //true
			@ApiParam(name = "goodsPrice", value = "상품가격", defaultValue = "") @RequestParam(value="goodsPrice", required=true) String goodsPrice, //true
			@ApiParam(name = "goodsDiscountPrice", value = "할인율 기준가", defaultValue = "") @RequestParam(value="goodsDiscountPrice", required=true) int goodsDiscountPrice, //true
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PACOPN") String procId) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		JsonObject responseObject = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		List<PaCopnGoodsdtMappingVO> paCopnGoodsdtMappingList = null;
		PaCopnGoodsdtMappingVO paCopnGoodsMapping = null;
		
		String vendorItemId = "";
		String apiKeys[]=null;
		
		int price = 0;
		
		log.info("===== 제휴OUT 딜 상품 아이템별 가격 변경 API Start=====");
		log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PACOPNAPI_01_004";
		String request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		if(paCode.equals(Constants.PA_COPN_BROAD_CODE)){
			paramMap.put("paName", Constants.PA_BROAD);
		} else {
			paramMap.put("paName", Constants.PA_ONLINE);
		}
		
		try{
			responseObject = new JsonObject();
			
			log.info("02.API 중복실행검사");
		    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";"); 
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			price = Integer.parseInt(goodsPrice) - goodsDiscountPrice;
			
			log.info("03.제휴OUT 딜 상품아이템별 가격 변경 대상 조회");
			paCopnGoodsdtMappingList = paCopnGoodsService.selectPaCopnAlcoutDealGoodsProductNo(paramMap);
			
			log.info("04.쿠팡 api호출");
			for(int i = 0 ; i < paCopnGoodsdtMappingList.size(); i++) {
				paCopnGoodsMapping = paCopnGoodsdtMappingList.get(i);
				
				vendorItemId = paCopnGoodsMapping.getPaOptionCode();
				
				if(vendorItemId != null) {
					responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0]).replaceAll("#vendorItemId#", vendorItemId).replaceAll("#price#", String.valueOf(price))), request_type);
					
					if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())) {
						paramMap.put("code","200");
						paramMap.put("message",responseObject.get("message").getAsString());
					} else {
						paramMap.put("code","500");
						paramMap.put("message",responseObject.get("message").getAsString());
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paCopnGoodsMapping.getGoodsCode());
					paGoodsTransLog.setPaCode(paCopnGoodsMapping.getPaCode());
					paGoodsTransLog.setItemNo(paCopnGoodsMapping.getSellerProductId());
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					
					paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
				}
			}
			if(paCopnGoodsdtMappingList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
		}catch(Exception e){
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try {
				paramMap.put("message", goodsCode + "|" + (paramMap.getString("code").equals("200") ? "OK" : paramMap.getString("message")));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 제휴OUT 딜 상품 아이템별 가격 변경 API End=====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 제휴OUT 딜 상품 아이템별 할인율 기준가 변경
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "제휴OUT 딜 상품 아이템별 할인율 기준가 변경", notes = "상품 아이템별 할인율 기준가 변경", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/alcoutDeal-goods-original-prices-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alcoutDealGoodsPriceDiscountModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=true) String goodsCode, //true
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=true) String paCode, //true
			@ApiParam(name = "goodsPrice", value = "상품가격", defaultValue = "") @RequestParam(value="goodsPrice", required=true) String goodsPrice, //true
			@ApiParam(name = "applyDate", value = "적용일시", defaultValue = "") @RequestParam(value="applyDate", required=true) Timestamp applyDate, //true
			@ApiParam(name = "paPromoTargetList", value = "프로모션", defaultValue = "") @RequestParam(value="paPromoTargetList", required=false) List<PaPromoTarget> paPromoTargetList,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PACOPN") String procId) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		JsonObject responseObject = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		PaGoodsTransLog paGoodsTransLog = null;
		List<PaCopnGoodsdtMappingVO> paCopnGoodsdtMappingList = null;
		PaCopnGoodsdtMappingVO paCopnGoodsMapping = null;
		
		String vendorItemId = "";
		String apiKeys[]=null;
		
		log.info("===== 제휴OUT 딜 상품 아이템별 할인율 기준가 변경 API Start=====");
		log.info("01.API 기본정보 세팅");
		
		String prg_id = "IF_PACOPNAPI_01_010";
		String request_type = "PUT";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		if(paCode.equals(Constants.PA_COPN_BROAD_CODE)){
			paramMap.put("paName", Constants.PA_BROAD);
		} else {
			paramMap.put("paName", Constants.PA_ONLINE);
		}
		
		try{
			responseObject = new JsonObject();
			
			log.info("02.API 중복실행검사");
		    duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";"); 
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("sysDateTime", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			log.info("03.제휴OUT 딜 상품 아이템별 할인율 기준가 변경 대상 조회");
			paCopnGoodsdtMappingList = paCopnGoodsService.selectPaCopnAlcoutDealGoodsProductNo(paramMap);
			
			log.info("04.쿠팡 api호출");
			for(int i = 0 ; i < paCopnGoodsdtMappingList.size(); i++) {
				paCopnGoodsMapping = paCopnGoodsdtMappingList.get(i);
				
				vendorItemId = paCopnGoodsMapping.getPaOptionCode();
				if(vendorItemId != null) {
						responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0]).replaceAll("#vendorItemId#", vendorItemId).replaceAll("#originalPrice#", String.valueOf(goodsPrice))), request_type);
					
					if(Constants.PA_COPN_RESULT_SUCCESS.equals(responseObject.get("code").getAsString())) {
						paramMap.put("code","200");
						paramMap.put("message",responseObject.get("message").getAsString());
					} else {
						paramMap.put("code","500");
						paramMap.put("message",responseObject.get("message").getAsString());
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paCopnGoodsMapping.getGoodsCode());
					paGoodsTransLog.setPaCode(paCopnGoodsMapping.getPaCode());
					paGoodsTransLog.setItemNo(paCopnGoodsMapping.getSellerProductId()); 
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn(Constants.PA_COPN_SUCCESS_OK.equals(paramMap.getString("code"))==true?"1":"0");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					
					paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
				}
			}
			
			if(!"200".equals(paramMap.getString("code"))) { 
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			if(paCopnGoodsdtMappingList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
		}catch(Exception e){
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try {
				paramMap.put("message", goodsCode + "|" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 제휴OUT 딜 상품 아이템별 할인율 기준가 변경 API End=====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 쿠팡 상품 삭제(판매중단 상태만 가능)
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "쿠팡 상품 삭제(판매중단 상태만 가능)", notes = "상품코드 미임력시 TPACOPNGOODSDELETELIST데이터 기준으로 삭제", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/delete-goods", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> deleteGoods(HttpServletRequest request,
			@ApiParam(name = "goodsCode", required = false, value = "상품코드")   @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "deleteCount", required = false, value = "삭제갯수(미입력시 1만개처리)", defaultValue = "10000")   @RequestParam(value = "deleteCount", required = false, defaultValue="10000") Long deleteCount) throws Exception{
		ParamMap paramMap = new ParamMap();
		List<HashMap<String, String>> cancelList = new ArrayList<>();
		List<HashMap<String, String>> saleStatusList = new ArrayList<>();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HashMap<String, Object> goodsStatusMap = new HashMap<String, Object>();
		List<CompletableFuture<CopnGoodsDeleteVO>> futures = new ArrayList<>();
		
		String dupCheck = "";
		String paGroupCode = "05";
		String resultMsg = "";
		int sellStopCount = 0;
		int totalSuccess = 0;
		int totalError = 0;
		int batchSize = 1000;
		long startTime = System.currentTimeMillis();
		
		try{
			paramMap.put("apiCode"   , "IF_PACOPNAPI_01_013");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());
			paramMap.put("paGroupCode", paGroupCode);
			log.info("쿠팡 상품삭제 API START");
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			if(null == goodsCode || goodsCode.equals("")) {
				goodsStatusMap.put("deleteCount", deleteCount);
				cancelList = paCopnGoodsService.selectDeleteGoodsList(goodsStatusMap);
				
				// 20 30 조회
				saleStatusList = paCopnGoodsService.selectDeleteGoodsSaleStatusList();
				// 판매중단 api 호출
				for(HashMap<String, String> saleStatusMap : saleStatusList) {
					goodsStatusMap.put("goodsCode", saleStatusMap.get("GOODS_CODE"));
					// 20 30 판매중단 상태로 update
					paCopnGoodsService.updateDeleteGoodsSaleStatus(goodsStatusMap);
					// 판매중지 api 호출
					ResponseEntity<?> responseMsg = goodsSellStop(request, saleStatusMap.get("GOODS_CODE"), saleStatusMap.get("PA_CODE"), "PACOPN");

					log.info("/delete-goods");
					log.info("code : "+PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
					log.info("Message : "+PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				}
			} else {
				goodsStatusMap.put("goodsCode", goodsCode);
				cancelList = paCopnGoodsService.selectDeleteGoodsList(goodsStatusMap);
				
				sellStopCount = paCopnGoodsService.updateDeleteGoodsSaleStatus(goodsStatusMap);
				if(sellStopCount > 0) {
					ResponseEntity<?> responseMsg = goodsSellStop(request, goodsCode, ComUtil.objToStr(cancelList.get(0).get("PA_CODE")), "PACOPN");
					
					log.info("/delete-goods");
					log.info("code : "+PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
					log.info("Message : "+PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
				}
			}
			
			// 쿠팡 상품 삭제 로직
			if(cancelList.size() > 0){
				for (int i = 0; i < cancelList.size(); i += batchSize) {
		            List<HashMap<String, String>> asyncList = cancelList.subList(i, Math.min(i + batchSize, cancelList.size()));
		            CompletableFuture<CopnGoodsDeleteVO> future = paCopnGoodsService.asyncGoodsDelete(asyncList, paramMap, apiInfo);
		            futures.add(future);
		        }
				// 모든 Future 완료 대기
			    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
			    for (CompletableFuture<CopnGoodsDeleteVO> f : futures) {
			    	CopnGoodsDeleteVO result = f.join();
			        totalSuccess += result.getSuccessCount();
			        totalError += result.getErrorCount();
			    }
			 }
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(dupCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(dupCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}finally{
			try{
				if(!"490".equals(paramMap.getString("code")) && !"500".equals(paramMap.getString("code"))) {
					paramMap.put("code"   , Constants.SAVE_SUCCESS);
					paramMap.put("message", "[성공:"+totalSuccess+"건] [실패:"+totalError+"건] 수행시간 : " + getExecutionTime(startTime) + " " + resultMsg);
				}
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
			log.info("쿠팡 상품삭제 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
		
	public void goodsModifyCopn(HttpServletRequest request, HashMap<String, String> apiInfo, ParamMap paramMap, PaCopnGoodsVO asyncCopnGoods) throws Exception{
		
		String goodsCode				= paramMap.getString("goodsCode");
		String prg_id					= paramMap.getString("apiCode");
		String dateTime					= paramMap.getString("dateTime");
		String procId					= paramMap.getString("modifyId");
		String goodsListTime			= paramMap.getString("goodsListTime");
		String rtnMsg					= "";
				
		ParamMap asyncMap = new ParamMap();
		String apiKeys[]  = null;

		List<PaGoodsdtMapping> goodsdtMapping    = null;
		List<PaPromoTarget> asyncPromoTargetList = null;
		PaGoodsTransLog paGoodsTransLog			 = null;
		List<PaGoodsOfferVO> goodsOffer 		 = null;
		List<PaCopnGoodsAttri> copnGoodsAttri 	 = null;
			
			
		asyncMap.put("url"		, apiInfo.get("API_URL"));
		asyncMap.put("paCode"	, asyncCopnGoods.getPaCode());
		asyncMap.put("siteGb"	, Constants.PA_COPN_PROC_ID);
		asyncMap.put("apiCode"	, prg_id);				
			
		if(asyncCopnGoods.getPaCode().equals(Constants.PA_COPN_BROAD_CODE)){
			asyncMap.put("paName"			, Constants.PA_BROAD);
			asyncMap.put("paCopnLoginId"	, ConfigUtil.getString("PA_COPN_BROAD_LOGIN_ID"));
		} else {
			asyncMap.put("paName"			, Constants.PA_ONLINE);
			asyncMap.put("paCopnLoginId"	, ConfigUtil.getString("PA_COPN_ONLINE_LOGIN_ID"));
		}
			
		apiKeys = apiInfo.get(asyncMap.getString("paName")).split(";"); 
			
		if(asyncCopnGoods.getSellerProductId()==null){
			paramMap.put("code","411");
			paramMap.put("message",getMessage("pa.not_exists_goods", new String[] {"상품코드 : " + goodsCode}));
			return;
		}
		if(asyncCopnGoods.getDescribeExt()==null){
			paramMap.put("code","420");
			paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
			return;
		}
			
		if(asyncCopnGoods.getApprovalStatus().equals("05") || asyncCopnGoods.getApprovalStatus().equals("10") || asyncCopnGoods.getApprovalStatus().equals("15")|| asyncCopnGoods.getApprovalStatus().equals("25")) { // 승인 반려상태
			paramMap.put("code","480");
			paramMap.put("message",getMessage("pa.copn_not_approval_status", new String[] {"상품코드 : " + goodsCode}));
			log.info("code : "+paramMap.get("code"));
			log.info("Message : "+paramMap.get("message"));
			//전송관리 테이블 저장
			paGoodsTransLog = new PaGoodsTransLog();
			paGoodsTransLog.setGoodsCode	(asyncCopnGoods.getGoodsCode());
			paGoodsTransLog.setPaCode		(asyncCopnGoods.getPaCode());
			paGoodsTransLog.setItemNo		(asyncCopnGoods.getSellerProductId()); // 상품코드
			paGoodsTransLog.setRtnCode		(paramMap.getString("code"));
			paGoodsTransLog.setRtnMsg		(paramMap.getString("message"));
			paGoodsTransLog.setSuccessYn	("0");
			paGoodsTransLog.setProcDate		(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			paGoodsTransLog.setProcId		(procId);
			paCopnGoodsService.insertPaCopnGoodsTransLogTx(paGoodsTransLog);
			
		} else {
			String goodsCom = "";
			
			goodsCom = (!ComUtil.NVL(asyncCopnGoods.getGoodsCom()).equals("")) ? ("<div style=\"line-height: 2.0em; font-family: 'NanumBarunGothic'; font-size: 19px;\"><div><h4>&middot;&nbsp;상품구성<h4><h4>" + asyncCopnGoods.getGoodsCom() + "<h4></div></div>") : "";
			
			if("".equals(asyncCopnGoods.getCollectImage()) || asyncCopnGoods.getCollectImage() == null) {
					asyncCopnGoods.setDescribeExt("<div align='center'><img alt='' src='" + asyncCopnGoods.getTopImage() + "' /><br /><br /><br />"	//상단 이미지
				  + goodsCom	//상품 구성
				  + asyncCopnGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + asyncCopnGoods.getBottomImage() + "' /></div>");	//기술서 + 하단 이미지				
			}else {
					asyncCopnGoods.setDescribeExt("<div align='center'><img alt='' src='" + asyncCopnGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + asyncCopnGoods.getCollectImage() + "' /><br /><br /><br />"	//상단 이미지 + 착불 이미지
				  + goodsCom	//상품 구성
				  + asyncCopnGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='" + asyncCopnGoods.getBottomImage() + "' /></div>");	//기술서 + 하단 이미지
			}
			
			if(!ComUtil.NVL(asyncCopnGoods.getNoticeExt()).equals("")) {
				asyncCopnGoods.setDescribeExt(asyncCopnGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + asyncCopnGoods.getDescribeExt());
			}
			
			paramMap.put("paGroupCode", asyncCopnGoods.getPaGroupCode());
			paramMap.put("goodsCode", asyncCopnGoods.getGoodsCode());
			paramMap.put("paCode", asyncCopnGoods.getPaCode());
			
			// 정보고시 조회
			goodsOffer = paCopnGoodsService.selectPaCopnGoodsOfferList(paramMap);
			if(goodsOffer.size() < 1){
				paramMap.put("code","430");
				paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
				//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				return;
			}
			
			// 쿠팡 매핑 옵션 조회
			copnGoodsAttri = paCopnGoodsService.selectPaCopnGoodsAttriList(paramMap);
			// SK스토아 단품 옵션 조회
			goodsdtMapping = paCopnGoodsService.selectPaCopnGoodsdtInfoList(paramMap);
			// 프로모션 조회
			asyncPromoTargetList = paCopnGoodsService.selectPaPromoTarget(paramMap);
				
			//구매옵션 한개로 통일 by 2020.12.02 이후로
			if(!"색상/크기/무늬/형태".equals(copnGoodsAttri.get(0).getAttributeTypeName())) {
				// 쿠팡 옵션이 추가 되었을 경우를 대비해 체크
				if(goodsdtMapping.get(0).getTransTargetYn().equals("1")) {
					String nowOption = "";  // 현재 TPACOPNGOODSATTRI에 매핑된 단품이 모두 저장되는 변수
					for(int k = 0; k < copnGoodsAttri.size(); k++) {
						if(copnGoodsAttri.get(k).getAttributeTypeMapping()!= null) 
							nowOption = nowOption.concat(copnGoodsAttri.get(k).getAttributeTypeMapping()+",");
					}
					if(!"".equals(nowOption)) {
						nowOption = nowOption.substring(0,nowOption.length()-1);
					}
					
					String [] dt_info_kind= {"색상", "사이즈", "무늬","형태","기타"};
					String [] dt_info_kind_num = {"0", "0", "0", "0", "0"};
					String goods_dt_kinds[];
					String kind="";
					for(PaGoodsdtMapping goodsd : goodsdtMapping) {
						kind = goodsd.getGoodsdtInfoKind();
						goods_dt_kinds = kind.split("/");
						for(int k = 0; k < goods_dt_kinds.length; k++) {
							switch(goods_dt_kinds[k]) {
							case "색상":
								dt_info_kind_num[0] ="1";
								break;
							case "사이즈":
								dt_info_kind_num[1] ="1";
								break;
							case "무늬":
							case "형태":
							case "기타":
								dt_info_kind_num[4] ="1";
								break;
							}
						}
					}
						
					for(int k = 0; k < dt_info_kind.length; k++) {
						if(dt_info_kind_num[k].equals("1")) {
							if(!nowOption.contains(dt_info_kind[k]) && k<4) { //기타는 제외
								// 판매중지 api 호출
								ResponseEntity<?> responseMsg = goodsSellStop(request, asyncCopnGoods.getGoodsCode(), asyncCopnGoods.getPaCode(), procId);		
								paramMap.put("code"		,	PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
								paramMap.put("message"	,	PropertyUtils.describe(responseMsg.getBody()).get("message").toString());

								log.info("/goods-sell-stop");
								log.info("code : "+paramMap.get("code"));
								log.info("Message : "+paramMap.get("message"));
									
								// 테이블 update
								asyncCopnGoods.setPaSaleGb	("10");
								asyncCopnGoods.setPaStatus	("40");
								asyncCopnGoods.setModifyId	(procId);
								asyncCopnGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								
								rtnMsg = paCopnGoodsService.savePaChangeOptionStatus(asyncCopnGoods);
								if(!rtnMsg.equals("000000")){
									paramMap.put("code"		,	"404");
									paramMap.put("message"	,	rtnMsg);
									return;
									//return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK); //TODO CHECK
								} else {
									paramMap.put("code"		,	"200");
									paramMap.put("message"	,	getMessage("pa.copn_change_option"));
								}
									
							}
						}
					}
				}
			}
				
			List<HashMap<String, Object>> paGoodsLimitCharMapList = paCopnGoodsService.selectGoodsLimitCharList(paramMap);
			//검색어 특수문자 치환
			if(asyncCopnGoods.getKeyWord() != null) {
				String keyWord = asyncCopnGoods.getKeyWord();
				
				for(HashMap<String, Object> paGoodsLimitCharMap : paGoodsLimitCharMapList){
					String replaceChar = paGoodsLimitCharMap.get("REPLACE_CHAR").toString() == null ? "" : paGoodsLimitCharMap.get("REPLACE_CHAR").toString();
					keyWord = keyWord.replaceAll(paGoodsLimitCharMap.get("LIMIT_CHAR").toString(), replaceChar);
				}
				
				keyWord = keyWord.trim();
				keyWord = keyWord.replaceAll("   ", " ");
				keyWord = keyWord.replaceAll("  ",  " ");
				keyWord = keyWord.replaceAll(" ",   " ");
				asyncCopnGoods.setKeyWord(keyWord);
			}
			
			JsonObject root = new JsonObject();
			root.add("insert", createProductJson(asyncCopnGoods, goodsdtMapping, copnGoodsAttri, goodsOffer, asyncPromoTargetList, apiKeys[0], asyncMap.getString("paCopnLoginId"), paramMap));
			
			asyncCopnGoods.setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));
			//비동기처리
			paCopnAsyncController.asyncGoodsModify(request, root, apiInfo, apiKeys, asyncMap, asyncCopnGoods, goodsdtMapping, asyncPromoTargetList);
			//앞에서 너무 오래 걸려서 sleep안줌. 나중에 빨라지고 디비 커넥션 관련 문제있으면 주석 해제 처리
			//Thread.sleep(50);
		}
	}
	
	public void goodsPriceModifyCopn(HttpServletRequest request, ParamMap paramMap, PaGoodsPriceVO goodsPriceVo, String searchTermGb) throws Exception {
		
		String procId  = paramMap.getString("modifyId");
		
		//프로모션조회
		paramMap.put("goodsCode", goodsPriceVo.getGoodsCode());
		paramMap.put("paCode"	, goodsPriceVo.getPaCode());
		List<PaPromoTarget>  paPromoTargetList = paCopnGoodsService.selectPaPromoTarget(paramMap);
		
		int dcPrice = Integer.parseInt(goodsPriceVo.getDcAmt()) + Integer.parseInt(goodsPriceVo.getLumpSumDcAmt());
		
		//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
		if(paPromoTargetList != null && paPromoTargetList.size() > 0) {
			for(PaPromoTarget paPromoTarget : paPromoTargetList) {
				if(paPromoTarget != null) {
					if(!paPromoTarget.getProcGb().equals("D")) {
						dcPrice += (int)paPromoTarget.getDoCost();
					}
				}
			}
		}
		//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
		
		ResponseEntity<?> responseMsg = goodsPriceModify(request, goodsPriceVo.getGoodsCode(), goodsPriceVo.getPaCode(), goodsPriceVo.getSalePrice(), dcPrice, procId , searchTermGb);
		
		if(PropertyUtils.describe(responseMsg.getBody()).get("code").toString().equals("200")) {
			log.info("code : "	 +PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			log.info("message : "+PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
			log.info("/goods-price-modify");
			
			responseMsg = goodsPriceDiscountModify(request, goodsPriceVo.getGoodsCode(), goodsPriceVo.getPaCode(), goodsPriceVo.getSalePrice(), goodsPriceVo.getApplyDate(), paPromoTargetList, procId, "1");
			paramMap.put("code"		,	PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			paramMap.put("message"	,	PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
			log.info("/goods-price-discount-modify");
			log.info("code : "+paramMap.get("code"));
			log.info("Message : "+paramMap.get("message"));
		} else {
			paramMap.put("code"		, PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			paramMap.put("message"	, PropertyUtils.describe(responseMsg.getBody()).get("message").toString());
			log.info("/goods-price-modify");
			log.info("code : "   +paramMap.get("code"));
			log.info("Message : "+paramMap.get("message"));
		}
	}
	
	// 실행시간 구하는 메소드
	public String getExecutionTime(long startTime) {

		long execTime = System.currentTimeMillis() - startTime;
        long millis = execTime % 1000;	// 밀리초
        long seconds = (execTime / 1000) % 60; 	// 초
        long minutes = (execTime / 1000) / 60;	// 분
        
        String formattedTime = String.format("%d분 %d초 %d밀리초", minutes, seconds, millis);
        return formattedTime;
    }
}			
