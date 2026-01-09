package com.cware.api.pakakao.controller;


import java.awt.Image;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
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
import com.cware.framework.common.util.StringUtil;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaKakaoGoodsVO;
import com.cware.netshopping.domain.PaKakaoTalkDealVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaKakaoGoodsImage;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pakakao.goods.service.PaKakaoGoodsService;
import com.cware.netshopping.pakakao.util.PaKakaoComUtill;
import com.cware.netshopping.pakakao.util.PaKakaoConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/pakakao/goods", description="상품")
@Controller("com.cware.api.pakakao.PaKakaoGoodsController")
@RequestMapping(value="/pakakao/goods")
public class PaKakaoGoodsController extends AbstractController{
	
	private transient static Logger log = LoggerFactory.getLogger(PaKakaoCommonController.class);
	
	@Autowired
	private PaKakaoConnectUtil paKakaoConnectUtil;
	
	@Autowired
	private PaKakaoCommonController paKakaoCommonController;
	
	@Autowired
	private PaKakaoGoodsService paKakaoGoodsService;
	
	@Autowired
	private PaKakaoAsyncController paKakaoAsyncController;
	
	@Autowired
	private PaCommonService paCommonService;
	
	@Autowired
    private SystemService systemService;
	
	@ApiOperation(value = "상품 정보 등록", notes = "상품 정보 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 405, message = "카카오 반려케이스"),
			   @ApiResponse(code = 411, message = "판매자주소록 등록 실패."),
			   @ApiResponse(code = 414, message = "기술서 조회 실패."),
			   @ApiResponse(code = 415, message = "정보고시 조회 실패."),
			   @ApiResponse(code = 416, message = "단품정보 조회 실패."),
			   @ApiResponse(code = 417, message = "카카오이미지 조회 실패."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/legacy-goods-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsInsert(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = true) String goodsCode,
			@ApiParam(name = "paCode", 	  value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = true) String paCode,
			@ApiParam(name = "procId", 	  value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_KAKAO_PROC_ID) String procId) throws Exception{
		
		String	prg_id 		= "IF_PAKAKAOAPI_01_001";		
		ResponseEntity<?> responseMsg = null;
		ParamMap paramMap				= new ParamMap();
		ParamMap apiInfoMap				= new ParamMap();
		ParamMap apiDataMap				= new ParamMap();
		ParamMap errorMap	= null;
		PaKakaoGoodsVO paKakaoGoods = null;
		HashMap<String, Object> describeData	= null;
		PaKakaoTalkDealVO dealData	    		= null;
		List<PaGoodsOfferVO> paGoodsOffer       = null;
		List<PaGoodsdtMapping> goodsdtMapping   = null;
		List<PaKakaoGoodsImage> imageList       = null;
		Map<String, Object> map	= new HashMap<String, Object>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = "";
		
		try {
			
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paGroupCode", Constants.PA_KAKAO_GROUP_CODE);
			paramMap.put("paCode", paCode);
			paramMap.put("modCase", "INSERT");
			
			log.info(prg_id + "KAKAO 상품등록 API - 01.프로그램 중복 실행 검사 [start]");
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			log.info("KAKAO 상품등록 API - 03.판매자주소록 등록 호출");
			responseMsg = paKakaoCommonController.kakaoEntpslipInsert(request, goodsCode);
			if ( !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") ) {
				paramMap.put("code", "411");
				paramMap.put("message", "판매자주소록 등록 실패");
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			log.info("KAKAO 상품등록 API - 05-1. 상품정보 Select");
			paKakaoGoods = paKakaoGoodsService.selectPaKakaoGoodsInfo(paramMap);	
			if(paKakaoGoods == null) {
				apiInfoMap.put("code", "404");
				apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			
			log.info("KAKAO 상품등록 API - 05-2. 기술서 조회 ");
			describeData = paCommonService.selectDescData(paramMap);
			if(StringUtil.isEmpty(describeData.get("DESCRIBE_EXT").toString())){
				apiInfoMap.put("code","414");
				apiInfoMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			paKakaoGoods.setDescribeExt(ComUtil.getClobToString(describeData.get("DESCRIBE_EXT")));
						
			log.info("KAKAO 상품등록 API - 05-3. 정보고시 조회 ");
			paGoodsOffer = paCommonService.selectPaGoodsOfferList(paramMap);
			if(paGoodsOffer.size() < 1){
				apiInfoMap.put("code", "415");
				apiInfoMap.put("message", getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			
			log.info("KAKAO 상품등록 API - 05-4. 단품정보 조회 ");
			goodsdtMapping = paKakaoGoodsService.selectPaGoodsdtInfoList(paramMap);
			if(goodsdtMapping.size() < 1){
				apiInfoMap.put("code", "416");
				apiInfoMap.put("message", "단품정보 조회 실패");
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			
			log.info("KAKAO 상품등록 API - 05-5. 카카오 이미지 조회 ");
			imageList = paKakaoGoodsService.selectKakaoGoodsImage(paramMap);
			if(imageList.size() < 1){
				apiInfoMap.put("code", "417");
				apiInfoMap.put("message", "카카오 이미지 조회 실패");
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
			
			log.info("KAKAO 상품등록 API - 05-6. 톡딜 조회 ");
//			dealData = paKakaoGoodsService.selectPaKakaoDealInfo(paramMap);
			
			apiDataMap = makeGoodsInfo(paKakaoGoods, paGoodsOffer, goodsdtMapping, imageList, dealData, paramMap.get("modCase").toString());
			
			apiInfoMap.put("paCode", paKakaoGoods.getPaCode());
			
			log.info("KAKAO 상품등록 API - API 호출 ");
			map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
			
			if("200".equals(ComUtil.objToStr(map.get("statusCode")))) {
				
				paKakaoGoods.setProductId(map.get("productId").toString());
				paKakaoGoods.setPaSaleGb("20");
				paKakaoGoods.setPaStatus("30");
				paKakaoGoods.setModifyId(procId);
				paKakaoGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								
				rtnMsg = paKakaoGoodsService.savePaKakaoGoodsTx(paKakaoGoods, goodsdtMapping, dealData);
				
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
	    			apiInfoMap.put("code", "500");
					apiInfoMap.put("message", rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
				} else {
					apiInfoMap.put("code", "200");
					apiInfoMap.put("message", "OK");
				}				
			} else {
				errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
				
				apiInfoMap.put("code", "405");
				apiInfoMap.put("message", errorMap.get("errorMsg").toString());
				
				paKakaoGoods.setReturnNote(errorMap.get("errorMsg").toString());
				
				if(errorMap.get("errorMsg").toString().indexOf("제한된 단어")         		 != -1
				|| errorMap.get("errorMsg").toString().indexOf("50자 이내로 입력")	  	 	 != -1
				|| errorMap.get("errorMsg").toString().indexOf("입력이 불가능한 문자")   		 != -1
				|| errorMap.get("errorMsg").toString().indexOf("25자 이내로 입력")	   		 != -1
				|| errorMap.get("errorMsg").toString().indexOf("중복된 옵션")	       		 != -1
				|| errorMap.get("errorMsg").toString().indexOf("최대 100개")	       		 != -1
				|| errorMap.get("errorMsg").toString().indexOf("기본배송비 이상 금액")   		 != -1
				|| errorMap.get("errorMsg").toString().indexOf("교환배송비 최대 100만원")		 != -1
		        || errorMap.get("errorMsg").toString().indexOf("주문배송비 최대 100만원")      != -1
				|| errorMap.get("errorMsg").toString().indexOf("반품배송비 최대 50만원")       != -1
				|| errorMap.get("errorMsg").toString().indexOf("스토어에 유사한 상품이 이미 등록") != -1) {
					
					paKakaoGoods.setPaSaleGb("30");
					paKakaoGoods.setPaStatus("20");					
				} else {
					//paKakaoGoods.setPaSaleGb("20");
					paKakaoGoods.setPaStatus("10");
				}
				paKakaoGoodsService.savePaKakaoGoodsError(paKakaoGoods);
				map.put("message", (errorMap.get("errorMsg").toString()));
			}
			map.put("prgId", prg_id);
			map.put("modCase", "INSERT");
			paKakaoAsyncController.insertPaGoodsTransLog(paKakaoGoods, map);
			
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "상품 정보 수정", notes = "상품 정보 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 411, message = "판매자주소록 등록 실패."),
			   @ApiResponse(code = 414, message = "기술서 조회 실패."),
			   @ApiResponse(code = 415, message = "정보고시 조회 실패."),
			   @ApiResponse(code = 416, message = "단품정보 조회 실패."),
			   @ApiResponse(code = 417, message = "카카오이미지 조회 실패."),
			   @ApiResponse(code = 418, message = "상품 판매재개/중단 실패."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/legacy-goods-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode",		value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "paCode",			value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode",	   required = false) String paCode,
			@ApiParam(name = "procId",			value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_KAKAO_PROC_ID) String procId,
			@ApiParam(name = "searchTermGb",	value = "searchTermGb", defaultValue="") @RequestParam(value="searchTermGb", required=false, defaultValue="") String searchTermGb) throws Exception{

		String		prg_id = "IF_PAKAKAOAPI_01_002";
		ParamMap apiInfoMap						= new ParamMap();
		ParamMap paramMap 						= new ParamMap();
		List<PaKakaoGoodsVO> paKakaoGoodsList	= null;		
		ResponseEntity<?> responseMsg 			= null;
		
		try {
			
			log.info(prg_id + "KAKAO 상품수정 API - 01.프로그램 중복 실행 검사 [start]");
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			if ( !searchTermGb.equals("1") ) {
				paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);				
			}
			
			paramMap.put("paGroupCode", Constants.PA_KAKAO_GROUP_CODE);
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modCase", "MODIFY");
			
			log.info("KAKAO 상품수정 API - 03.판매자주소록 등록 호출");
			responseMsg = paKakaoCommonController.kakaoModifyEntpslipInsert(request, goodsCode);
			if ( !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("490") ) {
				apiInfoMap.put("code", "411");
				apiInfoMap.put("message", "판매자주소록 등록 실패");
				systemService.insertApiTrackingTx(request, apiInfoMap);
			}
			
			log.info("KAKAO 상품수정 API - 04.판매자주소록 수정 호출");
			responseMsg = paKakaoCommonController.kakaoEntpslipModify(request, goodsCode);
			if ( !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("490") ) {
				apiInfoMap.put("code", "411");
				apiInfoMap.put("message", "판매자주소록 수정 실패");
				systemService.insertApiTrackingTx(request, apiInfoMap);
			}
			
			log.info("KAKAO 상품수정 API - 06.상품 판매재개/중단 처리");
			responseMsg = sellStatusModify(request, goodsCode, paCode, procId);
			if ( !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("490") ) {
				apiInfoMap.put("code", "418");
				apiInfoMap.put("message", "상품 판매재개/중단 실패");
				systemService.insertApiTrackingTx(request, apiInfoMap);
			}
			
			log.info("KAKAO 상품수정 API - 07.옵션ID 조회");
			responseMsg = goodsdtInfoList(request, goodsCode, paCode, procId);
			if ( !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("490") ) {
				apiInfoMap.put("code", "419");
				apiInfoMap.put("message", "옵션ID 조회 실패");
				systemService.insertApiTrackingTx(request, apiInfoMap);
			}
			
			log.info("KAKAO 상품수정 API - 08. 톡딜 ID 조회 ");
//		    responseMsg = goodsInfoList(request, goodsCode, paCode, procId);
			
			log.info("KAKAO 상품수정 API - 09-1. 상품정보 Select");
			paKakaoGoodsList = paKakaoGoodsService.selectPaKakaoGoodsInfoList(paramMap);
			
			if ( paKakaoGoodsList.size() < 1 ) {
				apiInfoMap.put("code", "404");
				apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
				systemService.insertApiTrackingTx(request, apiInfoMap);
			}
			
			for (int i=0; i<paKakaoGoodsList.size(); i++) {
				try {
					//기술서 이미지 URL 유지되면서 이미지 변경된 경우 고민해야함 
					// 4.7 상세설명 이미지 업데이트 요청 (한 시간에 1회 요청가능)
					PaKakaoGoodsVO paKakaogoods = paKakaoGoodsList.get(i);
					HashMap<String, Object> describeData = null;
					HashMap<String, Object> paNoticeData = null;
					PaKakaoTalkDealVO dealData = null;
					List<PaGoodsOfferVO> paGoodsOffer = null;
					List<PaGoodsdtMapping> goodsdtMapping = null;
					List<PaKakaoGoodsImage> imageList = null;
					ParamMap asyncMap = new ParamMap();
					ParamMap bodyMap = new ParamMap();
					
					asyncMap.put("paBroad",  apiInfoMap.get("paBroad"));
					asyncMap.put("paOnline", apiInfoMap.get("paOnline"));
					asyncMap.put("url", apiInfoMap.get("url"));
					asyncMap.put("paCode", paKakaogoods.getPaCode());
					asyncMap.put("method", apiInfoMap.get("method"));
					asyncMap.put("siteGb", procId);
					asyncMap.put("apiCode", prg_id);
					
					asyncMap.put("goodsCode", paKakaogoods.getGoodsCode());
					asyncMap.put("paGroupCode", paKakaogoods.getPaGroupCode());
					asyncMap.put("paCode", paKakaogoods.getPaCode());
					asyncMap.put("modCase", "MODIFY");
					
					log.info("KAKAO 상품수정 API - 09-2. 기술서 조회 ");
					describeData = paCommonService.selectDescData(asyncMap);
					if( StringUtil.isEmpty(describeData.get("DESCRIBE_EXT").toString()) ) {
						apiInfoMap.put("code","414");
						apiInfoMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + paKakaogoods.getGoodsCode()}));
						systemService.insertApiTrackingTx(request, apiInfoMap);
					}
					paKakaogoods.setDescribeExt(ComUtil.getClobToString(describeData.get("DESCRIBE_EXT")));
					
					paNoticeData = paKakaoGoodsService.selectPaNoticeData(paKakaogoods);
					if(paNoticeData != null) {
						if(!StringUtil.isEmpty(paNoticeData.get("NOTICE_EXT").toString()))
						paKakaogoods.setNoticeExt(ComUtil.getClobToString(paNoticeData.get("NOTICE_EXT")));
					}
					
					log.info("KAKAO 상품수정 API - 09-3. 정보고시 조회 ");
					paGoodsOffer = paCommonService.selectPaGoodsOfferList(asyncMap);
					if ( paGoodsOffer.size() < 1 ) {
						apiInfoMap.put("code", "415");
						apiInfoMap.put("message", getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + paKakaogoods.getGoodsCode()}));
						systemService.insertApiTrackingTx(request, apiInfoMap);
					}
					
					log.info("KAKAO 상품수정 API - 09-4. 단품정보 조회 ");
					goodsdtMapping = paKakaoGoodsService.selectPaGoodsdtInfoList(asyncMap);
					if ( goodsdtMapping.size() < 1 ) {
						apiInfoMap.put("code", "416");
						apiInfoMap.put("message", paKakaogoods.getGoodsCode() + "단품정보 조회 실패");
						systemService.insertApiTrackingTx(request, apiInfoMap);
					}
					
					log.info("KAKAO 상품수정 API - 09-5. 카카오 이미지 조회 ");
					imageList = paKakaoGoodsService.selectKakaoGoodsImage(asyncMap);
					if ( imageList.size() < 1 ) {
						apiInfoMap.put("code", "417");
						apiInfoMap.put("message", paKakaogoods.getGoodsCode() + "카카오 이미지 조회 실패");
						systemService.insertApiTrackingTx(request, apiInfoMap);
						continue;
					}
					
					log.info("KAKAO 상품수정 API - 09-6. 톡딜 조회 ");
//					dealData = paKakaoGoodsService.selectPaKakaoDealInfo(asyncMap);
					
					bodyMap = makeGoodsInfo(paKakaogoods, paGoodsOffer, goodsdtMapping, imageList, dealData, asyncMap.get("modCase").toString());
					
					// 비동기처리
					paKakaoAsyncController.asyncGoodsModify(request, asyncMap, bodyMap, paKakaogoods, goodsdtMapping, dealData);
					Thread.sleep(50);
					
				} catch (Exception e) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", paKakaoGoodsList.get(i).getGoodsCode() + e.getMessage());
					systemService.insertApiTrackingTx(request, apiInfoMap);
				}
			}
			Thread.sleep(7000);
			
			//재고수정
			log.info("KAKAO 상품등록 API - 09.재고수정");
			responseMsg = setGoodsStockModify(request, goodsCode, paCode, procId);
			if ( !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("490") ) {
				apiInfoMap.put("code", "419");
				apiInfoMap.put("message", "단품 재고수정 실패");
				systemService.insertApiTrackingTx(request, apiInfoMap);
			}
			
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			if(!searchTermGb.equals("1")){
				paKakaoConnectUtil.closeApi(request, apiInfoMap);				
			}
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "상품 이미지 업로드", notes = "상품 이미지 업로드 확인요청", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다"),
			   })
	@RequestMapping(value = "/image-upload", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> imageUpload(HttpServletRequest request,
			@ApiParam(name = "goodsCode",		value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "procId",			value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_KAKAO_PROC_ID) String procId
			) throws Exception{

		String prg_id = "IF_PAKAKAOAPI_01_003";
		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		ParamMap errorMap	= null;
		ParamMap paramMap = new ParamMap();
		Map<String, Object> map	= new HashMap<String, Object>();
		Config imageServerUrl = systemService.getConfig("IMG_SERVER_1_URL");
		String image_address = "http:" + imageServerUrl.getVal().substring(imageServerUrl.getVal().indexOf("//"));
		String image_url = "";
		String dateTime = "";
		
		try {
			dateTime = systemService.getSysdatetimeToString();
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);				
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("modCase", "UPLOAD");
			
			//이미지 등록 후 10분 지나면 image 확인요청 불가. 한번에 5000개씩 처리되게 쿼리수정. (약 4분 정도 걸림)
			List<PaKakaoGoodsImage> imageList = paKakaoGoodsService.selectKakaoGoodsImage(paramMap);
						
			for( PaKakaoGoodsImage image : imageList ) {
				try {
					
					apiInfoMap.put("paCode", image.getPaCode());
					image_url = image_address + image.getImageUrl() + image.getStoaImage();
					
					apiDataMap.put("url", image_url);
					apiDataMap.put("ratio", "SQUARE");
					
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
					
					if("200".equals(ComUtil.objToStr(map.get("statusCode")))) {
						image.setKakaoImage(map.get("url").toString());
						image.setModifyId(procId);
						image.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						paKakaoGoodsService.updatePakakaoGoodsImage(image);					
					} else {
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", image.getGoodsCode() + errorMap.get("errorMsg").toString());
						systemService.insertApiTrackingTx(request, apiInfoMap);
					}	
					
				} catch (Exception e) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", image.getGoodsCode() + PaKakaoComUtill.getErrorMessage(e));
					systemService.insertApiTrackingTx(request, apiInfoMap);
				}						
			}
			
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);				
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "상품 이미지 업로드 확인요청", notes = "상품 이미지 업로드 확인요청", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/image-upload-check", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> imageUploadCheck(HttpServletRequest request,
			@ApiParam(name = "goodsCode",		value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "procId",			value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	   required = false, defaultValue = Constants.PA_KAKAO_PROC_ID) String procId) throws Exception{
		
		String		     prg_id  	= "IF_PAKAKAOAPI_01_004";
		ParamMap 	 apiInfoMap		= new ParamMap();
		ParamMap errorMap	= null;
		Map<String,Object> map	    = new HashMap<String,Object> ();
		ParamMap paramMap = new ParamMap();
		String dateTime = "";
		Config imageServerUrl = systemService.getConfig("IMG_SERVER_1_URL");
		String image_address = "http:" + imageServerUrl.getVal().substring(imageServerUrl.getVal().indexOf("//"));
		String image_url = "";
		
		try {
			
			dateTime = systemService.getSysdatetimeToString();
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);			
		    paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
		    
			String url = apiInfoMap.get("url").toString();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("modCase", "CHECK");
			
			List<PaKakaoGoodsImage> imageCheckList = paKakaoGoodsService.selectKakaoGoodsImage(paramMap);
			
			for( PaKakaoGoodsImage image : imageCheckList ) {
				try {
					
					apiInfoMap.put("paCode", image.getPaCode());
					apiInfoMap.put("url", url.replace("{url}", image.getKakaoImage()));
					//10분초과여부 확인 
					if(ComUtil.objToDouble(image.getRemark()) < 0) {
						
						//이미지 있는지 확인하고
						try {
							image_url = image_address + image.getImageUrl() + image.getStoaImage();
							
							Image imageTemp = ImageIO.read(new URL(image_url));
							if(imageTemp != null){
								//이미지 있으면 TPAKAKAOGOODSIMAGE imageurl null로 업데이트
								image.setUploadStatus("00");
								image.setModifyId(procId);
								image.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								
								paKakaoGoodsService.updatePakakaoGoodsImageCheckTx(image);
							}
						} catch (Exception e) {
							//이미지 없으면 TPAKAKAOGOODSIMAGE image_status = 20 업데이트
							image.setUploadStatus("20");
							image.setModifyId(procId);
							image.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							paKakaoGoodsService.updatePakakaoGoodsImageCheckTx(image);	
						}						
					} else {
						map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
						
						if("200".equals(ComUtil.objToStr(map.get("statusCode"))) && "SUCCESS".equals(ComUtil.objToStr(map.get("code")))) {
							image.setUploadStatus("30");
							image.setModifyId(procId);
							image.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							paKakaoGoodsService.updatePakakaoGoodsImageCheckTx(image);					
						} else if ("200".equals(ComUtil.objToStr(map.get("statusCode"))) && "ACCEPTED".equals(ComUtil.objToStr(map.get("code")))) {
							//이미지 등록중으로 대기
							continue;
						} else {
							errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
							apiInfoMap.put("code", "500");
							apiInfoMap.put("message", image.getGoodsCode() + errorMap.get("errorMsg").toString());
							systemService.insertApiTrackingTx(request, apiInfoMap);
						}
					}
					
				} catch (Exception e) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", image.getGoodsCode() + PaKakaoComUtill.getErrorMessage(e));
					systemService.insertApiTrackingTx(request, apiInfoMap);
				}						
			}
			
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "판매상태변경", notes = "상품 판매상태변경", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/sell-status-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> sellStatusModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode",	value = "상품코드",  defaultValue = "")	@RequestParam(value = "goodsCode",	required = false) String goodsCode,
			@ApiParam(name = "paCode",		value = "제휴사코드",  defaultValue = "")	@RequestParam(value = "paCode",		required = false) String paCode,
			@ApiParam(name = "procId",		value = "처리자ID",  defaultValue = "")	@RequestParam(value = "procId",		required = false, defaultValue = Constants.PA_KAKAO_PROC_ID) String procId) throws Exception {
		
		String		     prg_id  	= "IF_PAKAKAOAPI_01_005";
		String dateTime = systemService.getSysdatetimeToString();
		ParamMap 	 apiInfoMap		= new ParamMap();
		ParamMap errorMap	= null;
		Map<String,Object> map	    = new HashMap<String,Object> ();
		List<PaKakaoGoodsVO> paKakaoGoodsList = null;
		ParamMap paramMap = new ParamMap();
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);			
		    paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
		    
		    String url = apiInfoMap.get("url").toString();
		    
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			
			log.info("KAKAO 상품판매상태변경 API - 01.판매재개/중단 대상 조회");
			paKakaoGoodsList = paKakaoGoodsService.selectPaKakaoSellStatusList(paramMap);
			
			for ( PaKakaoGoodsVO paKakaoGoods : paKakaoGoodsList ) {
				try {
					
					apiInfoMap.put("paCode", paKakaoGoods.getPaCode());					
					apiInfoMap.put("url", url.replace("{productId}", paKakaoGoods.getProductId()).replace("{saleStatus}", "20".equals(paKakaoGoods.getPaSaleGb())? "on":"off"));
										
					log.info("KAKAO 상품판매상태변경 API - 02.API 호출");
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
					
					if ( "200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
						paKakaoGoods.setTransSaleYn("0");
						paKakaoGoods.setModifyId(procId);
						paKakaoGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paKakaoGoodsService.updatePakakaoGoodsSellStatus(paKakaoGoods);					
					} else {
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						//메세지 바뀔 수 있음. (코드값으로 파악할 수 없어 메세지 하드코딩 처리)
						if(errorMap.get("errorMsg").toString().contains("판매중지 처리 진행됩니다.") || errorMap.get("errorMsg").toString().contains("해제 처리 진행됩니다.")) {
							paKakaoGoods.setTransSaleYn("0");
							paKakaoGoods.setModifyId(procId);
							paKakaoGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paKakaoGoodsService.updatePakakaoGoodsSellStatus(paKakaoGoods);				
							
						} else {
							apiInfoMap.put("code", "500");
							apiInfoMap.put("message", paKakaoGoods.getGoodsCode() + errorMap.get("errorMsg").toString());
							systemService.insertApiTrackingTx(request, apiInfoMap);
						}
						map.put("message", (errorMap.get("errorMsg").toString()));
					}	
					
				} catch (Exception e) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", paKakaoGoods.getGoodsCode() + PaKakaoComUtill.getErrorMessage(e));
					systemService.insertApiTrackingTx(request, apiInfoMap);
				}
				map.put("prgId", prg_id);
				paKakaoAsyncController.insertPaGoodsTransLog(paKakaoGoods, map);
			}
			
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);				
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "재고변경", notes = "옵션수정/등록(재고등록)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goods-stock-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> setGoodsStockModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode",	value = "상품코드",  defaultValue = "")	@RequestParam(value = "goodsCode",	required = false) String goodsCode,
			@ApiParam(name = "paCode",		value = "제휴사코드",  defaultValue = "")	@RequestParam(value = "paCode",		required = false) String paCode,
			@ApiParam(name = "procId",		value = "처리자ID",  defaultValue = "")	@RequestParam(value = "procId",		required = false, defaultValue = Constants.PA_KAKAO_PROC_ID) String procId) throws Exception {
		
		String		     prg_id  	= "IF_PAKAKAOAPI_01_006";
		String dateTime = systemService.getSysdatetimeToString();
		ParamMap 	 apiInfoMap		= new ParamMap();
		ParamMap errorMap	= null;
		ParamMap apiDataMap			= null;
		Map<String,Object> map	    = new HashMap<String,Object> ();
		List<PaKakaoGoodsVO> goodsStockList = null;
		List<PaGoodsdtMapping> goodsDtStockList = null;
		ParamMap paramMap = new ParamMap();
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);			
		    paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
		    		    
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			
			String url = apiInfoMap.get("url").toString();
			
			log.info("KAKAO 재고변경 API - 01.재고수정 대상 조회");
			goodsStockList = paKakaoGoodsService.selectPaGoodsdtStockList(paramMap);
			
			for ( PaKakaoGoodsVO paKakaoGoods : goodsStockList ) {
				
				apiInfoMap.put("paCode", paKakaoGoods.getPaCode());
				apiInfoMap.put("url", url.replace("{productId}", paKakaoGoods.getProductId()));
				
				apiDataMap = new ParamMap();
				goodsDtStockList = paKakaoGoodsService.selectPaGoodsdtStockMappingList(paKakaoGoods);
				
				try {
					
					Map<String, Object> optionInfo = new HashMap<String, Object>();
					List<Map<String, Object>> combinationsList	= new ArrayList<Map<String,Object>>();
					Map<String, Object> combination = null;
					List<Map<String, Object>> combinationsNameList	= null;
					Map<String, Object> combinationName = null;
					List<Map<String, Object>> combinationAttributesList	= new ArrayList<Map<String,Object>>();
					Map<String, Object> combinationAttributes = null;
					
					optionInfo.put("type", "COMBINATION");
					
					for( PaGoodsdtMapping goodsdtMapping : goodsDtStockList) {
						combination = new HashMap<String, Object>();
						combinationAttributes = new HashMap<String, Object>();
						combinationsNameList = new ArrayList<Map<String,Object>>();
						combinationName = new HashMap<String, Object>();
						
						combinationName.put("key", "색상/크기/무늬/형태");
						combinationName.put("value", goodsdtMapping.getGoodsdtInfo());
						combinationsNameList.add(combinationName);
						
						combination.put("id", goodsdtMapping.getRemark1());
						combination.put("name", combinationsNameList);
						combination.put("price", 0);
						combination.put("stockQuantity", ComUtil.objToLong(goodsdtMapping.getTransOrderAbleQty()) > 9999 ? "9999" : goodsdtMapping.getTransOrderAbleQty());
						combination.put("managedCode", goodsdtMapping.getPaOptionCode());
						combination.put("usable", true);
						
						if ( "11".equals(goodsdtMapping.getSaleGb()) || "19".equals(goodsdtMapping.getSaleGb()) ) {
							combination.put("usable", false);	
						}
						
						combinationsList.add(combination);
						
						combinationAttributes.put("id", goodsdtMapping.getRemark2());
						combinationAttributes.put("name", "색상/크기/무늬/형태");
						combinationAttributes.put("value", goodsdtMapping.getGoodsdtInfo());
						combinationAttributesList.add(combinationAttributes);
						
						//modify id/date 세팅
						goodsdtMapping.setModifyId(procId);
						goodsdtMapping.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
					}
					optionInfo.put("combinations", combinationsList);
					optionInfo.put("combinationAttributes", combinationAttributesList);
					
					apiDataMap.put("option", optionInfo);
										
					log.info("KAKAO 옵션등록/수정 API - 02.API 호출");
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
					
					if ( "200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
						paKakaoGoodsService.updatePaGoodsdtMappingQtyTx(goodsDtStockList);					
					} else {
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", paKakaoGoods.getGoodsCode() + errorMap.get("errorMsg").toString());
						
						paKakaoGoods.setReturnNote(errorMap.get("errorMsg").toString());
						
						if(errorMap.get("errorMsg").toString().indexOf("제한된 단어")        != -1
						|| errorMap.get("errorMsg").toString().indexOf("50자 이내로 입력")	  != -1
						|| errorMap.get("errorMsg").toString().indexOf("입력이 불가능한 문자")  != -1
						|| errorMap.get("errorMsg").toString().indexOf("25자 이내로 입력")	  != -1
						|| errorMap.get("errorMsg").toString().indexOf("중복된 옵션")	      != -1
						|| errorMap.get("errorMsg").toString().indexOf("최대 100개")	      != -1) {
									
							paKakaoGoods.setPaSaleGb("30");
							paKakaoGoods.setPaStatus("90");	
							paKakaoGoods.setTransSaleYn("1");
						} else {
							//paKakaoGoods.setPaSaleGb("20");
							paKakaoGoods.setPaStatus("30");
						}
						
						paKakaoGoodsService.savePaKakaoGoodsError(paKakaoGoods);
						systemService.insertApiTrackingTx(request, apiInfoMap);
						map.put("message", (errorMap.get("errorMsg").toString()));
					}	
					
				} catch (Exception e) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", paKakaoGoods.getGoodsCode() + PaKakaoComUtill.getErrorMessage(e));
					systemService.insertApiTrackingTx(request, apiInfoMap);
				}
				map.put("prgId", prg_id);
				paKakaoAsyncController.insertPaGoodsTransLog(paKakaoGoods, map);
			}
			
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);				
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "옵션ID 조회", notes = "옵션ID 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goodsdt-info-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsdtInfoList(HttpServletRequest request,
			@ApiParam(name = "goodsCode",	value = "상품코드",  defaultValue = "")	@RequestParam(value = "goodsCode",	required = false) String goodsCode,
			@ApiParam(name = "paCode",		value = "제휴사코드",  defaultValue = "")	@RequestParam(value = "paCode",		required = false) String paCode,
			@ApiParam(name = "procId",		value = "처리자ID",  defaultValue = "")	@RequestParam(value = "procId",		required = false, defaultValue = Constants.PA_KAKAO_PROC_ID) String procId) throws Exception {
		
		String		     prg_id  	= "IF_PAKAKAOAPI_01_007";
		ParamMap 	 apiInfoMap		= new ParamMap();
		ParamMap errorMap	= null;
		Map<String,Object> map	    = new HashMap<String,Object> ();
		List<PaKakaoGoodsVO> paKakaoGoodsList = null;
		ParamMap paramMap = new ParamMap();
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);			
		    paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
		    
		    String dateTime = systemService.getSysdatetimeToString();
		    
		    String url = apiInfoMap.get("url").toString();
		    
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			
			log.info("KAKAO 상품판매상태변경 API - 01.옵션ID 없는대상 조회");
			paKakaoGoodsList = paKakaoGoodsService.selectPaGoodsdtMappingId(paramMap);
			
			for ( PaKakaoGoodsVO paKakaoGoods : paKakaoGoodsList ) {
				try {
					
					apiInfoMap.put("paCode", paKakaoGoods.getPaCode());					
					apiInfoMap.put("url", url.replace("{productId}", paKakaoGoods.getProductId()));
										
					log.info("KAKAO 옵션조회 API - 02.API 호출");
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
					
					if ( "200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
						
						List<Map<String, Object>> combinations  = new ArrayList<Map<String,Object>>();
						List<Map<String, Object>> combinationAttributes = new ArrayList<Map<String,Object>>();
						combinations = (List<Map<String, Object>>)(map.get("combinations"));
						combinationAttributes = (List<Map<String, Object>>)(map.get("combinationAttributes"));
						
						List<PaGoodsdtMapping> paGoodsDtMappingList = null;
						paGoodsDtMappingList = paKakaoGoodsService.selectPaGoodsdtMappingIdList(paKakaoGoods);
						
						for(PaGoodsdtMapping paGoodsdt : paGoodsDtMappingList) {
							
							List<Map<String, Object>> combinationName = null;
							String vaule = "";
							for ( int i=0; i < combinations.size(); i++ ) {
								if(paGoodsdt.getPaOptionCode().equals(combinations.get(i).get("managedCode").toString())) {
									
									paGoodsdt.setRemark1(combinations.get(i).get("id").toString());
									
									combinationName = (List<Map<String, Object>>)combinations.get(i).get("name");
									vaule = combinationName.get(0).get("value").toString();
									for ( int j=0; j < combinationAttributes.size(); j++ ) {
										if(vaule.equals(combinationAttributes.get(j).get("value").toString())) {
											paGoodsdt.setRemark2(combinationAttributes.get(j).get("id").toString());
										}
									}
								}
							}
							
							paGoodsdt.setModifyId(procId);
							paGoodsdt.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						}
												
						paKakaoGoodsService.updatePaKakaoGoodsdtMappingIdTx(paGoodsDtMappingList);	
									
					} else {
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", paKakaoGoods.getGoodsCode() + errorMap.get("errorMsg").toString());
						systemService.insertApiTrackingTx(request, apiInfoMap);
					}	
					
				} catch (Exception e) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", paKakaoGoods.getGoodsCode() + PaKakaoComUtill.getErrorMessage(e));
					systemService.insertApiTrackingTx(request, apiInfoMap);
				}						
			}
			
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);				
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/*
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "톡딜 ID 조회", notes = "톡딜 ID 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goods-info-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsInfoList(HttpServletRequest request,
			@ApiParam(name = "goodsCode",	value = "상품코드",  defaultValue = "")	@RequestParam(value = "goodsCode",	required = false) String goodsCode,
			@ApiParam(name = "paCode",		value = "제휴사코드",  defaultValue = "")	@RequestParam(value = "paCode",		required = false) String paCode,
			@ApiParam(name = "procId",		value = "처리자ID",  defaultValue = "")	@RequestParam(value = "procId",		required = false, defaultValue = Constants.PA_KAKAO_PROC_ID) String procId) throws Exception {
		
		String		     prg_id  	= "IF_PAKAKAOAPI_01_009";
		ParamMap 	 apiInfoMap		= new ParamMap();
		ParamMap errorMap	= null;
		Map<String,Object> map	    = new HashMap<String,Object> ();
		List<PaKakaoTalkDealVO>  paKakaoDealInfoList = null;
		PaKakaoTalkDealVO paKakaoDealInfo = null;
		ParamMap paramMap = new ParamMap();
		String modCase = "CHECK";
		String dateTime = "";
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);			
		    paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
		    
		    String url = apiInfoMap.get("url").toString();
		    dateTime = systemService.getSysdatetimeToString();
		    
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modCase", modCase);
			
			log.info("KAKAO 톡딜 ID 조회 API - 01.톡딜ID 없는대상 조회");
			paKakaoDealInfoList = paKakaoGoodsService.selectPaKakaoDealInfoList(paramMap);
			if ( paKakaoDealInfoList.size() > 0 ) {
				try {
					for ( int i = 0; i < paKakaoDealInfoList.size(); i ++) {
						paKakaoDealInfo = paKakaoDealInfoList.get(i);
						
						apiInfoMap.put("paCode", paKakaoDealInfo.getPaCode());
						apiInfoMap.put("url", url.replace("{productId}", paKakaoDealInfo.getProductId()));
											
						log.info("KAKAO 톡딜 ID 조회 API - 02.API 호출");
						map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
						if ( "200".equals(ComUtil.objToStr(map.get("statusCode")))) {
							Map<String ,Object> groupDiscount = new HashMap<String,Object> ();
							groupDiscount = (Map<String, Object>) map.get("groupDiscount");
							
							paKakaoDealInfo.setDealId(groupDiscount.get("id").toString());
							paKakaoDealInfo.setModifyId(procId);
							paKakaoDealInfo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							paKakaoGoodsService.updatePaKakaoTalkDealId(paKakaoDealInfo);
						} else {
							errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
							apiInfoMap.put("code", "500");
							apiInfoMap.put("message", paKakaoDealInfo.getGoodsCode() + errorMap.get("errorMsg").toString());
							systemService.insertApiTrackingTx(request, apiInfoMap);
						}
					}
					
				} catch (Exception e) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", paKakaoDealInfo.getGoodsCode() + PaKakaoComUtill.getErrorMessage(e));
					systemService.insertApiTrackingTx(request, apiInfoMap);
				}						
			}
			
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);				
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	*/

	private ParamMap makeGoodsInfo(PaKakaoGoodsVO paKakaoGoods, List<PaGoodsOfferVO> paGoodsOffer, List<PaGoodsdtMapping> goodsdtMapping, List<PaKakaoGoodsImage> imageList, PaKakaoTalkDealVO dealData, String modCase) throws Exception {
		ParamMap goodsMap = new ParamMap();
		String goodsDescData = "";
		String goodsCom = "";
		
		if ( "MODIFY".equals(modCase) ) {
			goodsMap.put("productId", paKakaoGoods.getProductId());
		}
		
		goodsMap.put("categoryId", paKakaoGoods.getCategoryId());
		goodsMap.put("name", paKakaoGoods.getGoodsName());
		goodsMap.put("brand", paKakaoGoods.getBrand());
		goodsMap.put("manufacturer", paKakaoGoods.getManufacture());
		
		goodsCom = (!ComUtil.NVL(paKakaoGoods.getGoodsCom()).equals("")) ? ("<div style=\"line-height: 2.0em; font-family: 'NanumBarunGothic'; font-size: 19px;\"><div><h4>&middot;&nbsp;상품구성<h4><pre>" + paKakaoGoods.getGoodsCom() + "</pre></div></div>") : "";
		
		if("".equals(paKakaoGoods.getCollectImage()) || paKakaoGoods.getCollectImage() == null) {
			goodsDescData = "<div align='center'><img alt='' src='" + paKakaoGoods.getTopImage() + "' /><br /><br /><br />"	//상단 이미지  
					+ goodsCom	//상품 구성
					+ paKakaoGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />"	//기술서 
					+ "<img alt='' src='" + paKakaoGoods.getBottomImage() + "' /></div>";	//하단 이미지
		} else {
			goodsDescData = "<div align='center'><img alt='' src='" + paKakaoGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + paKakaoGoods.getCollectImage() + "' /><br /><br /><br />"	//상단 이미지 + 착불 이미지
					+ goodsCom	//상품 구성
					+ paKakaoGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />"	//기술서 
					+ "<img alt='' src='" + paKakaoGoods.getBottomImage() + "' /></div>";	//하단 이미지
		}
		
		if(!ComUtil.NVL(paKakaoGoods.getNoticeExt()).equals("")) {
			goodsDescData = paKakaoGoods.getNoticeExt() + goodsDescData;
		}
		goodsMap.put("productDetailDescription", goodsDescData);
		
		goodsMap.put("taxType", "1".equals(paKakaoGoods.getTaxYn())?"TAX":"DUTYFREE");
		goodsMap.put("salePrice", (long)paKakaoGoods.getSalePrice());
		goodsMap.put("useSalePeriod", "false");
		goodsMap.put("productCondition", "NEW");
		goodsMap.put("plusFriendSubscriberExclusive", "false");
		
		// 카카오 최대 구매수량 1개 이상 1000개 이하
		if ( 1 <= paKakaoGoods.getOrderMaxQty() ) {			
			goodsMap.put("maxPurchaseQuantity", paKakaoGoods.getOrderMaxQty() <= 1000?paKakaoGoods.getOrderMaxQty():1000);
		}
		
		// 카카오 최소 구매수량 2개이상 1000개이하(기본 1개)
		if ( 2 <= paKakaoGoods.getOrderMinQty() ) {
			goodsMap.put("minPurchaseQuantity", paKakaoGoods.getOrderMinQty() <= 1000?paKakaoGoods.getOrderMinQty():1000);
		}
		
		goodsMap.put("storeManagementCode", paKakaoGoods.getPaCode() + paKakaoGoods.getGoodsCode());
		goodsMap.put("displayStatus", "OPEN");
		goodsMap.put("shoppingHowDisplayable", "true");
		
		Map<String, Object> originInfo = new HashMap<String, Object>();
		if("00".equals(paKakaoGoods.getProductOrginAreaInfo())) {
			if("60".equals(paKakaoGoods.getLgroup())) {	// 식품 카테고리 경우 혼합/기타 & 상세설명참조로 연동
				originInfo.put("originAreaType", "USER_INPUT");
				originInfo.put("originAreaContent", "상세설명참조");
			} else {
				originInfo.put("originAreaType", "LOCAL");
			}
		}else if("03".equals(paKakaoGoods.getProductOrginAreaInfo())) {
			originInfo.put("originAreaType", "USER_INPUT");
			originInfo.put("originAreaContent", "상세설명참조");
		}else {
			originInfo.put("originAreaType", "IMPORT");
			originInfo.put("originAreaCode", paKakaoGoods.getProductOrginAreaInfo());
		}
		goodsMap.put("productOriginAreaInfo", originInfo);
		
		Map<String, Object> imageInfo = new HashMap<String, Object>();
		Map<String, Object> pImage = new HashMap<String, Object>();
		
		List<Map<String, Object>> etcImageList	= new ArrayList<Map<String,Object>>();
		Map<String, Object> etcImage = null;
		
		imageInfo.put("imageRatio", "SQUARE");
		for(int i=0; i<imageList.size(); i++) {
			if("P".equals(imageList.get(i).getImageGb())) {
				pImage.put("url", imageList.get(i).getKakaoImage());
			}else {
				etcImage = new HashMap<String, Object>();
				etcImage.put("url", imageList.get(i).getKakaoImage());
				etcImageList.add(etcImage);
			}
		}
		imageInfo.put("representImage", pImage);
		imageInfo.put("optionalImages", etcImageList);
		goodsMap.put("productImage", imageInfo);
		
		Map<String, Object> offerInfo = new HashMap<String, Object>();
		Map<String, Object> announcementMap = new HashMap<String, Object>();
		offerInfo.put("announcementType", paGoodsOffer.get(0).getPaOfferType());
		for(int i=0; i<paGoodsOffer.size(); i++) {
			if("consumerServicePhoneNumber".equals(paGoodsOffer.get(i).getPaOfferCode())) {
				//전화번호 양식으로 넣어야 되서 SK스토아 고객센터 번호 입력처리
				announcementMap.put(paGoodsOffer.get(i).getPaOfferCode(), "1670-0694");
			} else {
				announcementMap.put(paGoodsOffer.get(i).getPaOfferCode(), paGoodsOffer.get(i).getPaOfferExt());
			}
		}
		offerInfo.put("announcement", announcementMap);		
		goodsMap.put("announcementInfo", offerInfo);
		
		double sumDcAmt = paKakaoGoods.getArsDcAmt() + paKakaoGoods.getLumpSumDcAmt() + paKakaoGoods.getCouponDcAmt();
		Map<String, Object> discountInfo = new HashMap<String, Object>();
		if(sumDcAmt > 0) {
			if ( dealData != null ) {
				Map<String, Object> groupDiscount = new HashMap<String, Object>();
				Map<String, Object> Period = new HashMap<String, Object>();
				SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				
						
				Period.put("from", transFormat.format(dealData.getDealBdate()));
				Period.put("to", transFormat.format(dealData.getDealEdate()));
				
				groupDiscount.put("useGroupDiscount", true);
				
				if( !"".equals(dealData.getDealId()) && dealData.getDealId() != null) {
					groupDiscount.put("id", dealData.getDealId());
				}
				
				groupDiscount.put("price", (long)sumDcAmt);
				groupDiscount.put("useStock", false);
				groupDiscount.put("period", Period);
				
				goodsMap.put("groupDiscount", groupDiscount);
				
			} else {
				discountInfo.put("type", "PRICE");
				discountInfo.put("value", (long)sumDcAmt);
				discountInfo.put("useDiscount", true);
			}
		} else {
			discountInfo.put("useDiscount", false);
		}
		goodsMap.put("discount", discountInfo);			
		
		Map<String, Object> deliveryInfo = new HashMap<String, Object>();
		String shipCode = paKakaoGoods.getShipCostCode().substring(0, 2);
		deliveryInfo.put("deliveryMethodType", "DELIVERY");
		deliveryInfo.put("bundleGroupAvailable", "ID".equals(shipCode)?false:true);
		if("FR".equals(shipCode)) {
			deliveryInfo.put("deliveryFeeType", "FREE");
		} else if("ID".equals(shipCode)) {
			deliveryInfo.put("deliveryFeeType", "PAID");
		} else {
			deliveryInfo.put("deliveryFeeType", "CONDITIONAL_FREE");
			deliveryInfo.put("freeConditionalAmount", (long)paKakaoGoods.getShipCostBaseAmt());
		}
		deliveryInfo.put("baseFee", (long)paKakaoGoods.getOrdCost());
		deliveryInfo.put("deliveryFeePaymentType", "PREPAID");
		
		//반품,교환 배송비 0원 처리. 카카오에서 결제금액에서 차감되게 개발 되면 금액 세팅 예쩡
//		카카오 측 반품비 결제 개발 완료로 인한 반품, 교환비 연동(22.11.30)
		deliveryInfo.put("returnDeliveryFee", (long)paKakaoGoods.getReturnCost());
		deliveryInfo.put("exchangeDeliveryFee", (long)paKakaoGoods.getChangeCost());
//		카카오 측 반품비 결제 개발 원복으로 인한 소스 원복(22.12.05)
//		deliveryInfo.put("returnDeliveryFee", 0);
//		deliveryInfo.put("exchangeDeliveryFee", 0);
		
		deliveryInfo.put("shippingAddressId", paKakaoGoods.getShippingAddressId());
		deliveryInfo.put("returnAddressId", paKakaoGoods.getReturnAddressId());
		deliveryInfo.put("usePickUpDelivery", false);
		deliveryInfo.put("useQuickDelivery", false);
		deliveryInfo.put("asPhoneNumber", paKakaoGoods.getAsTelNo());
		deliveryInfo.put("asGuideWords", "상세설명참조");
		//도서산간 관련해서 협의중
		deliveryInfo.put("useIsolatedAreaNotice", false);
//		deliveryInfo.put("isolatedAreaNotice", "여기에 도서산간 안내 문구");
		
		if("1".equals(paKakaoGoods.getNoIslandYn())) {
			deliveryInfo.put("availableIsolatedArea", false);//도서산간 배송 가능 여부
			deliveryInfo.put("useIsolatedAreaFee", false);
		} else {
			deliveryInfo.put("availableIsolatedArea", true);
			
			//2022 05 13 추가배송비 0원도 연동 가능하여 배송비 유무 상관없이 배송비 지정여부 true 세팅 
			deliveryInfo.put("useIsolatedAreaFee", true);//도서산간 추가 배송비 지정 여부
			deliveryInfo.put("jejuAreaAdditionalFee", (long)paKakaoGoods.getAddJejuCost());				
			deliveryInfo.put("isolatedAreaAdditionalFee", (long)paKakaoGoods.getAddIslandCost());
		}
		
		goodsMap.put("delivery", deliveryInfo);
		
		Map<String, Object> optionInfo = new HashMap<String, Object>();
		List<Map<String, Object>> combinationsList	= new ArrayList<Map<String,Object>>();
		Map<String, Object> combination = null;
		List<Map<String, Object>> combinationsNameList	= null;
		Map<String, Object> combinationName = null;
		
		List<Map<String, Object>> combinationAttributesList	= new ArrayList<Map<String,Object>>();
		Map<String, Object> combinationAttributes = null;
		
		optionInfo.put("type", "COMBINATION");
		for(int i=0; i<goodsdtMapping.size(); i++) {
			combination = new HashMap<String, Object>();
			combinationAttributes = new HashMap<String, Object>();
			combinationsNameList = new ArrayList<Map<String,Object>>();
			combinationName = new HashMap<String, Object>();
			
			combinationName.put("key", "색상/크기/무늬/형태");
			combinationName.put("value", goodsdtMapping.get(i).getGoodsdtInfo());
			combinationsNameList.add(combinationName);
			
			if ( !"".equals(goodsdtMapping.get(i).getRemark1()) && goodsdtMapping.get(i).getRemark1() != null) {
				combination.put("id", goodsdtMapping.get(i).getRemark1());
			}
			combination.put("name", combinationsNameList);
			combination.put("price", 0);
			combination.put("stockQuantity", ComUtil.objToLong(goodsdtMapping.get(i).getTransOrderAbleQty()) > 9999 ? "9999" :  goodsdtMapping.get(i).getTransOrderAbleQty());
			combination.put("managedCode", goodsdtMapping.get(i).getPaOptionCode());
			
			if (ComUtil.objToInt(goodsdtMapping.get(i).getTransOrderAbleQty()) < 1) {
				combination.put("usable", false);
			} else {
				combination.put("usable", true);
			}
			
			combinationsList.add(combination);
			
			if ( !"".equals(goodsdtMapping.get(i).getRemark2()) && goodsdtMapping.get(i).getRemark2() != null) {
				combinationAttributes.put("id", goodsdtMapping.get(i).getRemark2());
			}
			combinationAttributes.put("name", "색상/크기/무늬/형태");
			combinationAttributes.put("value", goodsdtMapping.get(i).getGoodsdtInfo());
			combinationAttributesList.add(combinationAttributes);
		}
		optionInfo.put("combinations", combinationsList);
		optionInfo.put("combinationAttributes", combinationAttributesList);
		
		goodsMap.put("option", optionInfo);
		
		//인증정보
		if("1".equals(paKakaoGoods.getCertYn())) {
			List<Map<String, Object>> certs	= new ArrayList<Map<String,Object>>();
			Map<String, Object> cert = new HashMap<String, Object>();
			cert.put("certType", "DETAIL_REF");
			certs.add(cert);
			
			goodsMap.put("certs", certs);
		}
		
		goodsMap.put("minorPurchasable", "1".equals(paKakaoGoods.getAdultYn())?false:true);
		
		return goodsMap;
	}
	
	@ApiOperation(value = "카카오 이미지처리", notes = "카카오 이미지처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 412, message = "이미지 등록 실패."),
			   @ApiResponse(code = 413, message = "이미지 확인요청 실패."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/legacy-kakao-image-process", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> kakaoImageProcess(HttpServletRequest request,
			@ApiParam(name = "goodsCode",		value = "상품코드",  defaultValue = "") @RequestParam(value = "goodsCode", required = false) String goodsCode,
			@ApiParam(name = "procId",			value = "처리자ID",  defaultValue = "") @RequestParam(value = "procId",	 required = false, defaultValue = Constants.PA_KAKAO_PROC_ID) String procId) throws Exception{

		String		prg_id = "IF_PAKAKAOAPI_01_008";
		ParamMap apiInfoMap						= new ParamMap();
		ResponseEntity<?> responseMsg 			= null;
		
		try {
			log.info(prg_id + "KAKAO 이미지처리 API - 01.프로그램 중복 실행 검사 [start]");
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			log.info("KAKAO 이미지처리 API - 01.상품 이미지 업로드");
			responseMsg = imageUpload(request, goodsCode, procId);
			if ( !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("490") ) {
				apiInfoMap.put("code", "412");
				apiInfoMap.put("message", "이미지 등록 실패");
				systemService.insertApiTrackingTx(request, apiInfoMap);
			}
			
			//이미지 생성까지 시간 좀 걸림. 2초 텀 두기.
			Thread.sleep(2000);
			
			log.info("KAKAO 이미지처리 API - 02.상품 이미지 업로드 요청 확인");
			responseMsg = imageUploadCheck(request, goodsCode, procId);
			if ( !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200") && !PropertyUtils.describe(responseMsg.getBody()).get("code").equals("490") ) {
				apiInfoMap.put("code", "413");
				apiInfoMap.put("message", "이미지 확인요청 실패");
				systemService.insertApiTrackingTx(request, apiInfoMap);
			}
			
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);				
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
}
