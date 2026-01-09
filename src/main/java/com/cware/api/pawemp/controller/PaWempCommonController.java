package com.cware.api.pawemp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Brand;
import com.cware.netshopping.domain.model.Makecomp;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaWempBrand;
import com.cware.netshopping.domain.model.PaWempEntpSlip;
import com.cware.netshopping.domain.model.PaWempMaker;
import com.cware.netshopping.pawemp.common.model.GetCategoryResponse;
import com.cware.netshopping.pawemp.common.model.GetNoticeResponse;
import com.cware.netshopping.pawemp.common.model.NoticeList;
import com.cware.netshopping.pawemp.common.model.ReturnData;
import com.cware.netshopping.pawemp.common.service.PaWempApiService;
import com.cware.netshopping.pawemp.common.service.PaWempCommonService;
import com.cware.netshopping.pawemp.goods.model.GetBrandResponse;
import com.cware.netshopping.pawemp.goods.model.GetMakerResponse;
import com.cware.netshopping.pawemp.system.exception.WmpApiException;
import com.google.gson.JsonObject;
@ApiIgnore
@Api(value = "/pawemp/common", description = "위메프 공통")
@Controller("com.cware.api.pawemp.PaWempCommonController")
@RequestMapping(value = "/pawemp/common")
public class PaWempCommonController extends AbstractController{

	@Resource(name = "common.system.systemService")
	public SystemService systemService;
	
	@Resource(name = "pawemp.common.paWempApiService")
	public PaWempApiService paWempApiService;
	
	@Resource(name = "pawemp.common.paWempCommonService")
	private PaWempCommonService paWempCommonService;
	
	@ApiOperation(value = "전체카테고리조회", notes = "전체카테고리조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."), @ApiResponse(code = 490, message = "중복처리 오류 발생."),
	@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") })
	@RequestMapping(value = "/goodskinds-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsKindsList(HttpServletRequest request) throws Exception {

		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		List<PaGoodsKinds> paGoodsKindsList = new ArrayList<PaGoodsKinds>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		String apiCode = "IF_PAWEMPAPI_00_001";
		String duplicateCheck = "";

		try {
			log.info("===== 전체카테고리조회 API Start =====");

			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", apiCode);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
	        paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
	        paramMap.put("startDate", dateTime);
	        paramMap.put("paName", Constants.PA_BROAD);
			
			log.info("02.API 중복실행검사");
			// = 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start",paramMap.getString("apiCode"));
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });

			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
						
			log.info("04.위메프 API 조회");
			List<GetCategoryResponse> list = paWempApiService.callWApiList(apiInfo, "GET", GetCategoryResponse[].class, paramMap.getString("paName"));
			
			for(GetCategoryResponse object : list){
				object.getLcateName();
				PaGoodsKinds paGoodsKinds = new PaGoodsKinds();
			
				paGoodsKinds.setPaGroupCode(Constants.PA_WEMP_GROUP_CODE);
				paGoodsKinds.setPaLgroupName(object.getLcateName());
				paGoodsKinds.setPaMgroupName(object.getMcateName());
				paGoodsKinds.setPaSgroupName(object.getScateName());
				paGoodsKinds.setPaDgroupName(object.getDcateName());
				paGoodsKinds.setPaLmsdKey(Long.toString(object.getDcateCode()));
				paGoodsKinds.setPaLgroup(Long.toString(object.getDcateCode()-3000000));
				paGoodsKinds.setPaMgroup(Long.toString(object.getDcateCode()-2000000));
				paGoodsKinds.setPaSgroup(Long.toString(object.getDcateCode()-1000000));
				paGoodsKinds.setPaDgroup(Long.toString(object.getDcateCode()));
				paGoodsKinds.setInsertId(Constants.PA_WEMP_PROC_ID);
				paGoodsKinds.setModifyId(Constants.PA_WEMP_PROC_ID);
				paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paGoodsKindsList.add(paGoodsKinds);
			}

			log.info("05.전체카테고리 저장");
			rtnMsg = paWempCommonService.savePaWempGoodsKindsTx(paGoodsKindsList);

			if (Constants.SAVE_SUCCESS.equals(rtnMsg)) {
				paramMap.put("code", "200");
				paramMap.put("message", "OK");
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", rtnMsg);
			}

		} catch (Exception e) {
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}

			log.info("06.저장 완료 API END");
		}

		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품고시조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품고시조회", notes = "상품고시조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goods-offer-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsOfferList(HttpServletRequest request) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		String apiCode = "IF_PAWEMPAPI_00_002";
        String dateTime = "";
        String duplicateCheck = "";
        
        List<PaOfferCode> paWempOfferCodeList = new ArrayList<PaOfferCode>();
        PaOfferCode paWempOfferCode = null;

        try{
            log.info("===== 상품고시조회 API Start =====");

            log.info("01.API 기본정보 세팅");
    		dateTime = systemService.getSysdatetimeToString();
    		
    		paramMap.put("apiCode", apiCode);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
	        paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
	        paramMap.put("startDate", dateTime);
	        paramMap.put("paName", Constants.PA_BROAD);
            
            log.info("02.API 중복실행검사");
            //= 중복 실행 Check
		    duplicateCheck = systemService.checkCloseHistoryTx("start",paramMap.getString("apiCode"));
		    if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
		    
		    log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
		    
		    log.info("04.위메프 API 조회");
		    List<GetNoticeResponse> list = paWempApiService.callWApiList(apiInfo, "GET", GetNoticeResponse[].class, paramMap.getString("paName"));
		    
			for(GetNoticeResponse groupNotice : list){
				int sortSeq = 0;
				for(NoticeList notice : groupNotice.getNoticeList()){
					paWempOfferCode = new PaOfferCode();

					paWempOfferCode.setPaGroupCode("06");
					paWempOfferCode.setPaOfferType(Long.toString(groupNotice.getGroupNoticeNo()));//유형코드
					paWempOfferCode.setPaOfferTypeName(groupNotice.getGroupNoticeNoName());//유형명
					paWempOfferCode.setPaOfferCode(Long.toString(notice.getNoticeNo()));//항목코드
					paWempOfferCode.setPaOfferCodeName(notice.getNoticeName());//항목명
					paWempOfferCode.setRemark(notice.getDefaultValue());//상세입력방법
					paWempOfferCode.setRequiredYn("1");
					paWempOfferCode.setUseYn("1");
					paWempOfferCode.setSortSeq(Integer.toString(sortSeq));
					paWempOfferCode.setInsertId(Constants.PA_WEMP_PROC_ID);
					paWempOfferCode.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
					paWempOfferCode.setModifyId(Constants.PA_WEMP_PROC_ID);
					paWempOfferCode.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
					sortSeq++;
					paWempOfferCodeList.add(paWempOfferCode);
				}
			}
	    	
		    log.info("05.정보고시 저장");
 			rtnMsg = paWempCommonService.savePaWempOfferCodeTx(paWempOfferCodeList);
			
			if(!rtnMsg.equals("000000")){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("errors.no.select"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			} else {
				paramMap.put("code","200");
				paramMap.put("message","OK");
			}
        }catch (Exception e) {
			paramMap.put("code","500");
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR );
		}finally{
		    	try{
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", apiCode);
			}
			log.info("06.저장 완료 API END");
		}
        
        return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 브랜드조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "브랜드조회", notes = "브랜드조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/brand-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> brandList(HttpServletRequest request) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		String apiCode = "IF_PAWEMPAPI_00_005";
        String dateTime = "";
        String duplicateCheck = "";
        
        String queryParams = "";

		List<PaWempBrand> paWempBrandList = new ArrayList<PaWempBrand>();

        try{
            log.info("===== 브랜드 조회 API Start =====");

            log.info("01.API 기본정보 세팅");
    		dateTime = systemService.getSysdatetimeToString();
    		
    		paramMap.put("apiCode", apiCode);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
	        paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
	        paramMap.put("startDate", dateTime);
	        paramMap.put("paName", Constants.PA_BROAD);
    		
            log.info("02.API 중복실행검사");
            //= 중복 실행 Check
		    duplicateCheck = systemService.checkCloseHistoryTx("start",paramMap.getString("apiCode"));
		    if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
		    
		    log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

			log.info("04.브랜드 조회");
			List<Brand> brandList = paWempCommonService.selectBrandList();
			
		    log.info("05.위메프 API 조회");
		    for(Brand brand : brandList){
		    	
		    	queryParams = "brandName=" + brand.getBrandName();
		    	
		    	List<GetBrandResponse> wempBrand = paWempApiService.callWApiList(apiInfo, "GET", GetBrandResponse[].class, queryParams, paramMap.getString("paName"));
		    
		    	if(wempBrand.size() > 0) {
		    		PaWempBrand paWempBrand= new PaWempBrand();
		    		//paBrand.setPaGroupCode(Constants.PA_WEMP_GROUP_CODE);
		    		paWempBrand.setBrandCode(brand.getBrandCode());
		    		paWempBrand.setBrandNo(wempBrand.get(0).getBrandNo());
		    		paWempBrand.setBrandName(wempBrand.get(0).getBrandName());
		    		paWempBrand.setRemark(wempBrand.get(0).getBrandNameEnglish());
		    		paWempBrand.setInsertId(Constants.PA_WEMP_PROC_ID);
		    		paWempBrand.setModifyId(Constants.PA_WEMP_PROC_ID);
		    		
		    		paWempBrandList.add(paWempBrand);		    		
		    	}
			}

			log.info("06.브랜드 저장");
			rtnMsg = paWempCommonService.savePaWempBrandTx(paWempBrandList);

			if (Constants.SAVE_SUCCESS.equals(rtnMsg)) {
				paramMap.put("code"   , "200");
				paramMap.put("message", "OK");
			} else {
				paramMap.put("code"   , "500");
				paramMap.put("message", rtnMsg);
			}

		} catch (Exception e) {
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}
            log.info("===== 브랜드 조회 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 제조사조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "제조사조회", notes = "제조사조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/maker-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> makerList(HttpServletRequest request) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		String apiCode = "IF_PAWEMPAPI_00_006";
        String dateTime = "";
        String duplicateCheck = "";
        
        String queryParams = "";

		List<PaWempMaker> paWempMakerList = new ArrayList<PaWempMaker>();

        try{
            log.info("===== 제조사 조회 API Start =====");

            log.info("01.API 기본정보 세팅");
    		dateTime = systemService.getSysdatetimeToString();
    		
    		paramMap.put("apiCode", apiCode);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
	        paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
	        paramMap.put("startDate", dateTime);
	        paramMap.put("paName", Constants.PA_BROAD);
    		
            log.info("02.API 중복실행검사");
            //= 중복 실행 Check
		    duplicateCheck = systemService.checkCloseHistoryTx("start",paramMap.getString("apiCode"));
		    if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
		    
		    log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

			log.info("04.제조사 조회");
			List<Makecomp> makerList = paWempCommonService.selectMakerList();
			
		    log.info("05.위메프 API 조회");
		    for(Makecomp wempMaker : makerList){
		    	
		    	queryParams = "makerName=" + wempMaker.getMakecoName();
		    	
		    	List<GetMakerResponse> makers = paWempApiService.callWApiList(apiInfo, "GET", GetMakerResponse[].class, queryParams, paramMap.getString("paName"));
		    
		    	if(makers.size() > 0) {
		    		PaWempMaker paWempMaker= new PaWempMaker();
		    		//paWempMaker.setPaGroupCode(Constants.PA_WEMP_GROUP_CODE);
		    		paWempMaker.setMakerCode(wempMaker.getMakecoCode());
		    		paWempMaker.setMakerNo(makers.get(0).getMakerNo());
		    		paWempMaker.setMakerName(makers.get(0).getMakerName());
		    		paWempMaker.setRemark(makers.get(0).getMakerNameEnglish());
		    		paWempMaker.setInsertId(Constants.PA_WEMP_PROC_ID);
		    		paWempMaker.setModifyId(Constants.PA_WEMP_PROC_ID);
		    		
		    		paWempMakerList.add(paWempMaker);
		    	}
			}

			log.info("06.제조사 저장");
			rtnMsg = paWempCommonService.savePaWempMakerTx(paWempMakerList);

			if (Constants.SAVE_SUCCESS.equals(rtnMsg)) {
				paramMap.put("code"   , "200");
				paramMap.put("message", "OK");
			} else {
				paramMap.put("code"   , "500");
				paramMap.put("message", rtnMsg);
			}

		} catch (Exception e) {
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",
					e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				paramMap.put("resultMessage", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}
            log.info("===== 제조사 조회 API End =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 배송정책 생성
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "배송정책 생성", notes = "배송정책 생성", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."), 
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다"),
			@ApiResponse(code = 510, message = "반품비와 교환비 금액이 다릅니다.") })
	@RequestMapping(value = "/entpslip-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpSlipInsert(HttpServletRequest request,
			@ApiParam(name = "paCode", required = true, value = "제휴사코드", defaultValue = "") @RequestParam(value = "paCode", required = true) String paCode,
			@ApiParam(name = "entpCode", required = true, value = "업체코드", defaultValue = "") @RequestParam(value = "entpCode", required = true) String entpCode,
			@ApiParam(name = "entpManSeq", required = true, value = "업체담당자순번", defaultValue = "") @RequestParam(value = "entpManSeq", required = true) String entpManSeq,
			@ApiParam(name = "shipCostCode", required = true, value = "배송비정책코드", defaultValue = "") @RequestParam(value = "shipCostCode", required = true) String shipCostCode,
			@ApiParam(name = "noShipIsland", required = true, value = "도서산간배송불가", defaultValue = "") @RequestParam(value = "noShipIsland", required = true) String noShipIsland,
			@ApiParam(name = "installYn", required = true, value = "설치상품여부", defaultValue = "") @RequestParam(value = "installYn", required = true) String installYn,
			@ApiParam(name = "searchTermGb", required = false, value = "searchTermGb", defaultValue = "") @RequestParam(value = "searchTermGb", required = false) String searchTermGb
			)
			throws Exception {
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();

		PaWempEntpSlip paEntpSlip = new PaWempEntpSlip();
		HashMap<String, Object> entpUserMap = null;

		String rtnMsg = "Constants.SAVE_SUCCESS";
		String apiCode = "IF_PAWEMPAPI_00_003";
		String duplicateCheck = "";
		
		ReturnData returnData = null; // 결과
		
		try {
			
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", apiCode);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
	        paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
	        paramMap.put("startDate", dateTime);
	        paramMap.put("modCase", "INSERT");
	        if(paCode.equals(Constants.PA_WEMP_BROAD_CODE)){
	        	paramMap.put("paName", Constants.PA_BROAD);
	        } else {
	        	paramMap.put("paName", Constants.PA_ONLINE);
	        }
			
			log.info("===== 배송정책 생성 API Start =====");
			log.info("제휴사코드 : " + paCode);
			log.info("업체코드 : " + entpCode);
			log.info("업체담당자순번: " + entpManSeq);
			log.info("배송비코드 : " + shipCostCode);
			log.info("도서산간배송불가여부: " + noShipIsland);
			log.info("설치상품여부 : " + installYn);

			log.info("02.API 중복실행검사");
			if(!"1".equals(searchTermGb)) {
				duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
				if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

			paEntpSlip.setPaCode(paCode);
		    paEntpSlip.setEntpCode(entpCode);
		    paEntpSlip.setEntpManSeq(entpManSeq);
		    paEntpSlip.setShipCostCode(shipCostCode);
		    paEntpSlip.setNoShipIsland(noShipIsland);
		    paEntpSlip.setInstallYn(installYn);
		    paEntpSlip.setPaAddrGb("20");// 회수지
		    
		    log.info("04.출고지 조회");
		    entpUserMap = paWempCommonService.selectEntpShipInsertList(paEntpSlip);
		    
		    if (entpUserMap == null) {
		    	paramMap.put("code", "404");
		    	paramMap.put("message", getMessage("partner.no_change_data"));
		    } else { 
				// 정의된 배송정책과 다를 경우 반려(error 처리)
				if(isReturnShipPolicy(shipCostCode, entpUserMap, paramMap)){ // 배송정책 위배
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
				try {
					log.info("05.배송정책 생성 API 호출");
					returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST", getShipPolicyObject(false, entpUserMap, paEntpSlip), ReturnData.class, paramMap.getString("paName"));
					
					if(returnData != null){
						if(StringUtils.isNotEmpty(returnData.getReturnKey()+"")) { // 배송정책 api 등록 성공
							// 출고지 INSERT
							log.info("05-1.배송정책 생성 API 성공 : "+returnData.getReturnKey());
							paEntpSlip.setPaShipPolicyNo(returnData.getReturnKey()+"");
							paEntpSlip.setTransTargetYn("0");
							paEntpSlip.setInsertId(Constants.PA_WEMP_PROC_ID);
							paEntpSlip.setModifyId(Constants.PA_WEMP_PROC_ID);
							paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							rtnMsg = paWempCommonService.savePaWempEntpSlipTx(paEntpSlip);
							log.info("05-2.배송정책 저장 : "+rtnMsg);
							if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
								paramMap.put("code","404");
								paramMap.put("message",rtnMsg);
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							} else {
								paramMap.put("code","200");
								paramMap.put("message","OK");
							}
						} else {
							paramMap.put("code","404");
							paramMap.put("message",getMessage("partner.no_change_data"));
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						}
					}else{
						paramMap.put("code","404");
						paramMap.put("message",getMessage("partner.no_change_data"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
				} catch(WmpApiException e){ // API 오류
					String errMsg = e.getMessage();
					String[] msg = errMsg.split("error:");
					if(msg.length > 1){
						errMsg = msg[1].replaceAll("\"", "");
					}
					paramMap.put("code","500");
					paramMap.put("message", errMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
		    }
		} 
		catch (Exception e) {
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")),HttpStatus.OK);
		} finally {
			try {
				paramMap.put("message", "entp_code" + ":" + entpCode + ">" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(!"1".equals(searchTermGb)) {
				if (duplicateCheck.equals("0")) {
					systemService.checkCloseHistoryTx("end", apiCode);
				}	
			}
			log.info("===== 배송정책 생성 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 배송정책 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "배송정책 수정", notes = "배송정책 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."), 
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다"),
			@ApiResponse(code = 510, message = "반품비와 교환비 금액이 다릅니다.") })
	@RequestMapping(value = "/entpslip-update", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpSlipUpdate(HttpServletRequest request) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		HashMap<String, Object> entpUserMap = null;
				
		String duplicateCheck = "";
		String apiCode = "IF_PAWEMPAPI_00_004";
		String rtnMsg = "";
		
		List<Object> entpShipUpdateList = null;
		ReturnData returnData = null; // 결과
		int targetCount = 0;
		int procCount = 0;
		
		try {
			log.info("===== 배송정책 수정 API Start =====");
			
			log.info("01.API 기본정보 세팅");
			String dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode", apiCode);
			paramMap.put("broadCode", Constants.PA_WEMP_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
			paramMap.put("startDate", dateTime);
			paramMap.put("modCase", "MODIFY");
			
			log.info("02.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			
			log.info("03.API정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));

		    log.info("04.출고지 수정 대상 리스트 조회");
		    paramMap.put("paAddrGb", "20");
		    entpShipUpdateList = paWempCommonService.selectEntpShipUpdateList(paramMap); // 수정 목록 대상 조회
		    targetCount = entpShipUpdateList.size();
		    if (targetCount == 0) {
		    	paramMap.put("code", "404");
		    	paramMap.put("message", getMessage("partner.no_change_data"));
		    } else {
		    	for(int i = 0; i < targetCount; i++) {
		    		entpUserMap = (HashMap<String, Object>) entpShipUpdateList.get(i);
		    		
		    		if(entpUserMap.get("PA_CODE").toString().equals(Constants.PA_WEMP_BROAD_CODE)){
		    			paramMap.put("paName", Constants.PA_BROAD);
		    		} else {
		    			paramMap.put("paName", Constants.PA_ONLINE);
		    		}
		    		
		    		PaWempEntpSlip paEntpSlip = new PaWempEntpSlip();
		    		paEntpSlip.setPaCode(entpUserMap.get("PA_CODE").toString());
					paEntpSlip.setEntpCode(entpUserMap.get("ENTP_CODE").toString());
					paEntpSlip.setEntpManSeq(entpUserMap.get("ENTP_MAN_SEQ").toString());
		    		paEntpSlip.setShipCostCode(entpUserMap.get("SHIP_COST_CODE").toString());
		    		paEntpSlip.setNoShipIsland(entpUserMap.get("NO_SHIP_ISLAND").toString());
		    		paEntpSlip.setInstallYn(entpUserMap.get("INSTALL_YN").toString());
		    		// 정의된 배송정책과 다를 경우 반려(error 처리)
					if(!isReturnShipPolicy(entpUserMap.get("SHIP_COST_CODE").toString(), entpUserMap, paramMap)){ // 배송정책 일치
						try {
							log.info("05.위메프 배송정책 수정 API 호출");
							returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST", getShipPolicyObject(true, entpUserMap, paEntpSlip), ReturnData.class, paramMap.getString("paName"));
						
							if(StringUtils.isNotEmpty(returnData.getReturnKey()+"")) { // 배송정책 api 수정 성공
								// 출고지 UPDATE
								log.info("05-1.위메프 배송정책 수정 API 성공 : "+returnData.getReturnKey());
								paEntpSlip.setTransTargetYn("0");
								paEntpSlip.setModifyId(Constants.PA_WEMP_PROC_ID);
								paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								
								rtnMsg = paWempCommonService.updatePaWempEntpSlipTx(paEntpSlip);
								log.info("05-2.배송정책 수정 동기화 저장 : "+rtnMsg);
								if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
									paramMap.put("code","404");
									paramMap.put("message",rtnMsg);
								} else {
									paramMap.put("code","200");
									paramMap.put("message","OK");
									procCount++;
								}
							} else {
								paramMap.put("code","404");
								paramMap.put("message",getMessage("partner.no_change_data"));
							}
						} catch(WmpApiException e){ // API 오류
							String errMsg = e.getMessage();
							String[] msg = errMsg.split("error:");
							if(msg.length > 1){
								errMsg = msg[1].replaceAll("\"", "");
							}
							paramMap.put("code","500");
							paramMap.put("message", errMsg);
						}
					}
		    	}
		    }
		} catch (Exception e) {
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")),HttpStatus.OK);
		} finally {
			try {
				if(paramMap.getString("code").equals("200") && (targetCount != procCount)){
					paramMap.put("code","500");
					paramMap.put("message", "대상건수:" + targetCount + ", 성공건수:" + procCount);
				}
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", apiCode);
			}
			log.info("===== 배송정책 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 정의된 배송정책과 일치 체크
	 * @param shipCostCode
	 * @param entpUserMap
	 * @param paramMap
	 * @return 배송정책 위배(true), 배송정책 일치(false)
	 */
	private boolean isReturnShipPolicy(String shipCostCode, HashMap<String, Object> entpUserMap, ParamMap paramMap){
		int baseAmt = Integer.parseInt(entpUserMap.get("SHIP_COST_BASE_AMT").toString());
		String tempShipCode = StringUtils.substring(shipCostCode, 0, 2);
		// 배송비가 없거나 무료배송 정책일경우
		// 조건부 배송비 무료일 경우 100원으로 입력 처리.
		//if(tempShipCode.equals("CN") && baseAmt == 0){
		//	paramMap.put("code", "501");
		//	paramMap.put("message", "조건부무료 기준 금액을 입력해주세요.");
		//	return true;
		//}
		
		return false;
	}
	
	/**
	 * 배송정책 등록/수정 JSON OBJECT 처리 
	 * @param isUpdate( true :수정, false : 등록)
	 * @param entpUserMap
	 * @param paEntpSlip
	 * @return
	 */
	private JsonObject getShipPolicyObject(boolean isUpdate, HashMap<String, Object> entpUserMap, PaWempEntpSlip paEntpSlip){
		
		String returnZipCode = entpUserMap.get("POST_NO").toString(); // 우편번호
		String returnRoadAddress  = entpUserMap.get("ROAD_ADDR1").toString(); // 도로명 주소
		String returnRoadAddressDetail = entpUserMap.get("ROAD_ADDR2").toString(); // 도로명 상세주소
		String returnAddress = entpUserMap.get("POST_ADDR1").toString(); // 지번 주소
		String returnAddressDetail = entpUserMap.get("POST_ADDR2").toString(); // 지번 상세주소
		String shipCostCode = paEntpSlip.getShipCostCode();
		String freeCondition = "";
		
		int shipFee = Integer.parseInt(entpUserMap.get("ORD_COST").toString());
		int returnCost = Integer.parseInt(entpUserMap.get("RETURN_COST").toString()); // 반품비
		
		Integer jejuCost = Integer.parseInt(entpUserMap.get("JEJU_COST").toString()) - shipFee;
		Integer islandCost = Integer.parseInt(entpUserMap.get("ISLAND_COST").toString()) - shipFee;
		
		//배송정보명 = entpcode + entpmanseq + shipcostcode + noShipIsland + installYn
		String shippingPlaceName = entpUserMap.get("ENTP_CODE").toString()+"-"+entpUserMap.get("ENTP_MAN_SEQ").toString()+"-"+shipCostCode+"-"+paEntpSlip.getNoShipIsland()+"-"+paEntpSlip.getInstallYn(); //배송정보명
		
		JsonObject jShipPolicy = new JsonObject();
		if(isUpdate){
			jShipPolicy.addProperty("shipPolicyNo", Integer.parseInt(entpUserMap.get("PA_SHIP_POLICY_NO").toString())); // 배송정책번호
			paEntpSlip.setPaShipPolicyNo(entpUserMap.get("PA_SHIP_POLICY_NO").toString());
		}
		jShipPolicy.addProperty("shipPolicyName", shippingPlaceName); // 배송정보명               
		jShipPolicy.addProperty("shipMethod","KP"); // 배송방법, 일반-택배배송
		
		// 도서산간 배송 여부
		if(paEntpSlip.getNoShipIsland().equals("0")){
			jShipPolicy.addProperty("shipArea", "WHOLE"); // 배송 가능 지역
			jShipPolicy.addProperty("jejuShipFee", jejuCost > 0 ? jejuCost.toString() : "0");
			jShipPolicy.addProperty("islandMountainShipFee", islandCost > 0 ? islandCost.toString() : "0");
		} else {
			jShipPolicy.addProperty("shipArea", "NO_MOUNTAIN_ISLAND");
		}
		
		//주문제작 여부
		if(paEntpSlip.getInstallYn().equals("1")){
			jShipPolicy.addProperty("releaseDay", "10"); // 출고기한
		} else {
			jShipPolicy.addProperty("releaseDay", "3");	// 출고기한
		}
		
		if(shipFee > 0){ //개별/조건부
			switch(StringUtils.substring(shipCostCode, 0, 2)){
			case "FR" :
				jShipPolicy.addProperty("bundleKind", "PRD");
				jShipPolicy.addProperty("shipType", "FREE");
				jShipPolicy.addProperty("claimShipFee", returnCost/2);
				break;
			case "CN" : case "PL" :
				//기준금액 0원일 경우 100원으로 강제세팅처리.
				if(Integer.parseInt(entpUserMap.get("SHIP_COST_BASE_AMT").toString()) == 0) {
					freeCondition = "100";
				}else {
					freeCondition = entpUserMap.get("SHIP_COST_BASE_AMT").toString();
				}
				jShipPolicy.addProperty("bundleKind", "PRD");
				jShipPolicy.addProperty("shipType", "COND");
				jShipPolicy.addProperty("shipFee", shipFee);
				jShipPolicy.addProperty("claimShipFee", returnCost);
				jShipPolicy.addProperty("freeCondition", freeCondition);
				break;
			default : //상품별
				jShipPolicy.addProperty("bundleKind", "PRD");
				jShipPolicy.addProperty("shipType", "FIXED");
				jShipPolicy.addProperty("shipFee", shipFee);
				jShipPolicy.addProperty("claimShipFee", returnCost);
				break;
			}
		} else { // 무료배송
			jShipPolicy.addProperty("bundleKind", "PRD");
			jShipPolicy.addProperty("shipType", "FREE");
			jShipPolicy.addProperty("claimShipFee", returnCost/2);
		}
		
		// 출고지 주소(회수지 주소 사용)
		jShipPolicy.addProperty("releaseZipCode", returnZipCode); // 
		jShipPolicy.addProperty("releaseRoadAddress1", returnRoadAddress);
		jShipPolicy.addProperty("releaseRoadAddress2", returnRoadAddressDetail);
		jShipPolicy.addProperty("releaseAddress1", returnAddress);
		jShipPolicy.addProperty("releaseAddress2", returnAddressDetail);
		// 회수지 주소
		jShipPolicy.addProperty("returnZipCode", returnZipCode);
		jShipPolicy.addProperty("returnRoadAddress1", returnRoadAddress);
		jShipPolicy.addProperty("returnRoadAddress2", returnRoadAddressDetail);
		jShipPolicy.addProperty("returnAddress1", returnAddress);
		jShipPolicy.addProperty("returnAddress2", returnAddressDetail);
		
		if("wapi-stg.wemakeprice.com".equals(ConfigUtil.getString("WEMP_HOST"))) {
			jShipPolicy.addProperty("safetyNoDisplayYn","N"); //안심번호 서비스 사용 노출여부(Y:노출, N:비노출), 개발 때는 비노출로 설정
		} else  {
			jShipPolicy.addProperty("safetyNoDisplayYn","Y"); //운영
		}
		
		return jShipPolicy;
	}
}
