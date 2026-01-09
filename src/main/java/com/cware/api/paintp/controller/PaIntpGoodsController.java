package com.cware.api.paintp.controller;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.common.util.StringUtil;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaIntpGoodsVO;
import com.cware.netshopping.domain.PaIntpGoodsdtMappingVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.paintp.goods.service.PaIntpGoodsService;
import com.cware.netshopping.paintp.util.PaIntpComUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@Api(value="/paintp/goods", description="공통")
@Controller("com.cware.api.paintp.PaIntpGoodsController")
@RequestMapping(value="/paintp/goods")
public class PaIntpGoodsController extends AbstractController{

	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "paintp.goods.paIntpGoodsService")
	private PaIntpGoodsService paIntpGoodsService;
	
	@Resource(name = "com.cware.api.paintp.PaIntpCommonController")
	private PaIntpCommonController paIntpCommonController;
	
	@Resource(name = "com.cware.netshopping.paintp.util.PaIntpComUtil")
	private PaIntpComUtil paIntpComUtil;
	
	@Resource(name = "com.cware.api.paintp.PaIntpAsycController")
	private PaIntpAsyncController paIntpAsyncController;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@ApiOperation(value = "상품등록", notes = "상품등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 410, message = "이미 등록된 상품입니다."),
			   @ApiResponse(code = 420, message = "기술서를 입력하세요."),
			   @ApiResponse(code = 430, message = "정보고시를 입력하세요."),
			   @ApiResponse(code = 440, message = "상품의 출고지 담당을 확인하세요"),
			   @ApiResponse(code = 441, message = "상품의 회수지 담당을 확인하세요"),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/legacy-goods-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsInsert(HttpServletRequest request,
			@ApiParam(name = "goodsCode", 		value = "상품코드", 	 defaultValue = "") @RequestParam(value = "goodsCode",		required = false) String goodsCode,
			@ApiParam(name = "paCode", 	  		value = "제휴사코드",   defaultValue = "") @RequestParam(value = "paCode",			required = false) String paCode,
			@ApiParam(name = "procId", 	  		value = "처리자ID",    defaultValue = "") @RequestParam(value = "procId",			required = false, defaultValue = "PAINTP") String procId,
			@ApiParam(name = "searchTermGb", 	value = "중복체크여부",  defaultValue = "") @RequestParam(value = "searchTermGb",	required = false, defaultValue = "") String searchTermGb,
			@ApiParam(name = "massTargetYn", 	value = "대량입점여부",  defaultValue = "") @RequestParam(value = "massTargetYn",	required = false, defaultValue = "") String massTargetYn,
			@ApiParam(name = "inComingUrl", 	value = "BO호출구분",   defaultValue = "") @RequestParam(value = "inComingUrl", 	required = false, defaultValue = "") String inComingUrl) throws Exception {

		ParamMap paramMap 				= new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime 				= "";
		String duplicateCheck 			= "";
		int	resultCnt					= 0;
		List<HashMap<String, String>> paGoodsTargetList = null;
				
		//= connectionSetting 설정
		String prg_id = "IF_PAINTPAPI_01_001";
		try{
			
			log.info("=====인터파크 상품등록 API Start=====");
			log.info("01.인터파크 상품등록 API 기본정보 세팅");
			dateTime = systemService.getSysdatetimeToString();
			
			paramMap.put("apiCode"		, prg_id);
			paramMap.put("broadCode"	, Constants.PA_INTP_BROAD_CODE);
        	paramMap.put("onlineCode"	, Constants.PA_INTP_ONLINE_CODE);
        	paramMap.put("siteGb"		, Constants.PA_INTP_PROC_ID);
			paramMap.put("startDate"	, dateTime);
			paramMap.put("modCase"		, "INSERT");
			
			paramMap.put("massTargetYn" , massTargetYn);
			
			
			log.info("02.인터파크 상품등록 API 중복실행검사");
			//= 중복 실행 Check
			if(!"1".equals(searchTermGb)) {
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			}
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
				
			
			paramMap.put("goodsCode"	, goodsCode);
			paramMap.put("paCode"		, paCode);
			paramMap.put("modifyId"		, procId);
			paramMap.put("inComingUrl"	, inComingUrl);
			paGoodsTargetList = paIntpGoodsService.selectPaIntpGoodsTrans(paramMap);		
			
			for(HashMap<String, String> paGoodsTarget : paGoodsTargetList){
				resultCnt += goodsInsert(paGoodsTarget, paramMap, apiInfo, request, searchTermGb);
			}
			
			//입점 결과 처리
			if(paGoodsTargetList.size() != resultCnt) {
				paramMap.put("code"		,"490");
				paramMap.put("message"	, paGoodsTargetList.size() + "건 중"  + resultCnt + "성공" );
			}else {
				paramMap.put("code"		,"200");
				paramMap.put("message"	, paGoodsTargetList.size() + "건 입점 완료" );
			}
			
		}catch (Exception e) {
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
				paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(!"1".equals(searchTermGb)) {
				if(duplicateCheck.equals("0")){
					systemService.checkCloseHistoryTx("end", prg_id);
				}				
			}
			
			log.info("09.인터파크 상품등록 저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	
	public int goodsInsert(HashMap<String, String> paGoodsTarget, ParamMap paramMap
						  , HashMap<String, String> apiInfo
					      , HttpServletRequest request
						  , String searchTearmGb ) {
		
		try {
			String method 							= "GET";
			String dateTime 						= paramMap.getString("startDate");
			String goodsCode  						= paGoodsTarget.get("GOODS_CODE");
			String paCode	 						= paGoodsTarget.get("PA_CODE");
			String procId	  						= paramMap.getString("modifyId");
			String exceptStr  						= "";
			String rtnMsg 	    					= Constants.SAVE_SUCCESS;
			PaEntpSlip entpSlip 					= null;
			ResponseEntity<?> responseMsg 			= null;
			PaGoodsTransLog paGoodsTransLog 		= null;
			PaIntpGoodsVO paIntpGoods 				= null;
			List<PaGoodsOffer> paIntpGoodsOffer 	= null;
			List<PaGoodsdtMapping> goodsdtMapping 	= null;
		    List<PaPromoTarget> paPromoTargetList 	= null;
		    HashMap<String, Object> describeData	= null;
		    
			log.info("인터파크 상품코드 : " +  goodsCode);
			log.info("인터파크 제휴사코드: "+  paCode);
					
			paramMap.put("goodsCode"	, goodsCode);
			paramMap.put("paCode"		, paCode);
			paramMap.put("paGroupCode"	, "07");
			paramMap.put("marginCode"	, "80");
			paramMap.put("minpriceCode"	, "81");				
			
			exceptStr = paCommonService.paExceptGoodsYn(paramMap);
			if(!exceptStr.equals("000000")){
				paGoodsTarget.put("RETURN_NOTE"	, exceptStr);
				paGoodsTarget.put("PA_STATUS"	, "20");
				paGoodsTarget.put("MODIFY_ID"	, procId);
				paIntpGoodsService.updatePaIntpGoodsFail(paGoodsTarget);
				return 0;
			}
			
			if(paCode.equals(Constants.PA_INTP_BROAD_CODE)){
				paramMap.put("paName", Constants.PA_INTP_BROAD);
			} else {
				paramMap.put("paName", Constants.PA_INTP_ONLINE);
			}
			apiInfo.put("paName", paramMap.getString("paName"));
			apiInfo.put("paCode", paCode);
			
			log.info("03.인터파크 상품등록 API 반품배송지 등록 반품배송지 담당자 주소 체크");
			//상품 회수지 담당자 주소 체크
			entpSlip = paIntpGoodsService.selectPaIntpEntpSlip(paramMap);
			if(null != entpSlip) {
				//회수지 등록
				responseMsg= paIntpCommonController.returnSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaCode(), searchTearmGb);
				log.info("03-1.인터파크 상품등록 API 반품배송지 등록 CODE CHECK :"+PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
				
				if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
					paramMap.put("code","440");
					paramMap.put("message", getMessage("pa.check_entp_slip_man_seq", new String[] {" 상품코드 : " + goodsCode}));
					return 0;
				}
			}
			
			log.info("04.인터파크 상품등록 API 배송비 정책 등록");
			paIntpCommonController.entpSlipCostInsert(request , searchTearmGb);
			
			//상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와  수정대상조회 시작시간을 비교함
			String goodsListTime = systemService.getSysdatetimeToString();
			
			log.info("05.상품정보 Select");
			paIntpGoods = paIntpGoodsService.selectPaIntpGoodsInfo(paramMap);
			
			describeData = paCommonService.selectDescData(paramMap);
			paIntpGoods.setDescribeExt(ComUtil.getClobToString(describeData.get("DESCRIBE_EXT")));
			
			//인터파크 기등록 상품일때 처리
			if(paIntpGoods.getPrdNo() != null){
				paramMap.put("code","410");
				paramMap.put("message",getMessage("pa.already_insert_goods", new String[] {"인터파크 상품코드 : " + paIntpGoods.getPrdNo()}));
				return 0;
			}
			//상품 상세 설명이 존재하지 않을때 처리
			if(paIntpGoods.getDescribeExt() == null){
				paramMap.put("code","420");
				paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
				return 0;
			}
			
			paramMap.put("paGroupCode", paIntpGoods.getPaGroupCode());
			//정보고시 조회
			paIntpGoodsOffer = paIntpGoodsService.selectPaIntpGoodsOfferList(paramMap);
			//정보고시 존재하지 않을때 처리
			if(paIntpGoodsOffer.size() < 1){
				paramMap.put("code","430");
				paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
				return 0;
			}
						
			//옵션정보
			goodsdtMapping = paIntpGoodsService.selectPaIntpGoodsdtInfoList(paramMap);
			//프로모션정보
			paPromoTargetList = paIntpGoodsService.selectPaPromoTarget(paramMap);
			
			log.info("06.인터파크 인터파크 상품등록 API 호출");
			
			Map<String, String> apiParamMap = new HashMap<String, String>();
			String dataUrl = "";
			String internalXmlUrl = apiInfo.get("INTERNAL_XML_URL");
			
			if(!"".equals(ComUtil.isNull(internalXmlUrl, ""))) {
				dataUrl = internalXmlUrl + "?goodsCode=" + goodsCode + "&paCode=" + paCode;
				apiParamMap.put("dataUrl", dataUrl);
			}
			
			String xmlStr = goodsInsertToXmlFile(paIntpGoods, paIntpGoodsOffer, goodsdtMapping, paPromoTargetList, dataUrl);			
			
			if("".equals(ComUtil.NVL(xmlStr, ""))) {
				paramMap.put("code","404");
				paramMap.put("message","인터파크 XML 데이터 생성 오류");
				return 0;
				
			} else {
				apiInfo.put("body", xmlStr);
				
				ParamMap resParam = paIntpComUtil.paIntpConnectionSetting(apiInfo,method,apiParamMap);
				
				if("200".equals(resParam.getString("code"))) {
					ParamMap map = new ParamMap();
					map = ComUtil.paIntpXmlToMap(resParam);
					if("ERR".equals(map.getString("ERROR"))) {
						String code = map.getString("code").replace("|", "");
						code = code.length() == 6 ? code.substring(2) : (code.length() == 5?code.substring(1):code);
						paramMap.put("code", code);
						paramMap.put("message",map.get("explanation"));
						paIntpGoods.setPaSaleGb("30");
						paIntpGoods.setPaStatus("20");
						paIntpGoods.setReturnNote(map.get("explanation").toString());
						paIntpGoods.setModifyId(procId);
						paIntpGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						rtnMsg = paIntpGoodsService.savePaIntpFailGoodsTx(paIntpGoods);
						return 0;
						
					} else {
						paramMap.put("code", "200");
			    		paramMap.put("message", "OK");
						String producNo = map.getString("prdNo");
						
						//전송관리 테이블 저장
						paGoodsTransLog = new PaGoodsTransLog();
						paGoodsTransLog.setGoodsCode(paIntpGoods.getGoodsCode());
						paGoodsTransLog.setPaCode(paIntpGoods.getPaCode());
						paGoodsTransLog.setItemNo(producNo);
						paGoodsTransLog.setRtnCode(paramMap.getString("code"));
						paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
						paGoodsTransLog.setSuccessYn(paramMap.getString("code").equals("200")==true?"1":"0");
						paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsTransLog.setProcId(procId);
						paIntpGoodsService.insertPaIntpGoodsTransLogTx(paGoodsTransLog);
						
						paIntpGoods.setPrdNo(producNo);
						paIntpGoods.setPaStatus("30");//입점완료
						paIntpGoods.setModifyId(procId);
						paIntpGoods.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paIntpGoods.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paIntpGoods.setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));
						
						log.info("07.인터파크 상품등록 제휴사 상품정보 저장");
						rtnMsg = paIntpGoodsService.savePaIntpGoodsTx(paIntpGoods, goodsdtMapping, paPromoTargetList);
						
						if(!rtnMsg.equals("000000")){
							paramMap.put("code","404");
							paramMap.put("message",getMessage("pa.not_exists_process_list"));
							return 0;
						} else {
							paramMap.put("code","200");
							paramMap.put("message","OK");
							return 1; //success
						}
					}
				}else{
					paramMap.put("code","500");
					paramMap.put("message",resParam.getString("message"));
					return 0;
				}
			}		
		}catch (Exception e) {
			paramMap.put("code"		, "500");
			paramMap.put("message"	, e.toString());
			return 0;
		}finally {
			if(!paramMap.getString("code").equals("200")) {
				log.info(paramMap.getString("goodsCode") + "(" + paramMap.getString("paCode") + ") : " + paramMap.getString("code") + " " + paramMap.getString("message"));
			}
		}
	}
		
	
	@ApiOperation(value = "상품수정", notes = "상품수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 411, message = "존재하지 않는 상품입니다."),
			   @ApiResponse(code = 420, message = "기술서를 입력하세요."),
			   @ApiResponse(code = 430, message = "정보고시를 입력하세요."),
			   @ApiResponse(code = 440, message = "단품저장에 실패했습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/legacy-goods-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsModify(HttpServletRequest request,
			@ApiParam(name="goodsCode",	value="상품코드",	defaultValue = "") @RequestParam(value="goodsCode", required=false) String goodsCode,
			@ApiParam(name="paCode",	value="제휴사코드",defaultValue = "") @RequestParam(value="paCode", required=false) String paCode,
			@ApiParam(name="procId",	value="처리자ID",	defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PAINTP") String procId,
			@RequestParam(value="searchTermGb"	, required=false, defaultValue = "") String searchTermGb) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		//HashMap<String, String> apiInfo = new HashMap<String, String>();
		String dateTime = "";
		String duplicateCheck = "";
		
		List<PaIntpGoodsVO> paIntpGoodsList = null;
		List<PaEntpSlip> entpSlipList = null;		
		PaEntpSlip entpSlip = null;
		HashMap<String, Object> describeData	= null;
		
		ResponseEntity<?> responseMsg = null;
		//PaGoodsTransLog paGoodsTransLog = null;
		log.info("===== 인터파크 상품수정 API Start=====");
		log.info("01.인터파크 상품수정 API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PAINTPAPI_01_002";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
    	paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", dateTime);
		paramMap.put("modCase", "MODIFY");
				
		try{
			log.info("인터파크 상품수정 상품코드 : "+goodsCode);
			log.info("인터파크 상품수정 제휴사코드: "+paCode);
			log.info("인터파크 상품수정 처리자ID : "+procId.toUpperCase());
			
			log.info("02.인터파크 상품수정 API 중복실행검사");
			//= 중복 실행 Check
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
		    	if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});		    	
		    }
						
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			
			//반품배송지 주소 체크(등록 할 반품배송지 존재시 처리)
			log.info("03.인터파크 상품수정 API 반품배송지 등록");
			entpSlipList = paIntpGoodsService.selectPaIntpEntpSlipList(paramMap);
			if(entpSlipList.size() > 0){
				//반품배송지 등록
				for(int i=0; i<entpSlipList.size(); i++){
					try{
						entpSlip = entpSlipList.get(i);
						responseMsg = paIntpCommonController.returnSlipInsert(request, entpSlip.getEntpCode(), entpSlip.getEntpManSeq(), entpSlip.getPaCode(), searchTermGb);
						log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
						if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
							paramMap.put("code","440");
							paramMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 인터파크 상품수정 반품배송지 등록에러 : " + entpSlip.getEntpCode()}));
							log.info("/entp-slip-insert");
							log.info("code : "+paramMap.get("code"));
							log.info("Message : "+paramMap.get("message"));
						}
					}catch(Exception e){
						log.info("반품배송지등록처리 Exception : "+entpSlip.getEntpCode()+"|"+entpSlip.getEntpManSeq());
					}
				}
			}
			
			//반품배송지 주소 UPDATE
			log.info("04.인터파크 상품수정 API 반품배송지 수정");
			responseMsg = paIntpCommonController.returnShipUpdate(request);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
				paramMap.put("code","440");
				paramMap.put("message",getMessage("pa.check_entp_slip_man_seq", new String[] {" 상품코드 : " + goodsCode}));
				log.info("/return-ship-update");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			//배송비정책 INSERT
			log.info("05.인터파크 상품수정 API 배송비정책 등록");
			responseMsg = paIntpCommonController.entpSlipCostInsert(request, searchTermGb);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
				paramMap.put("code","440");
				paramMap.put("message",getMessage("pa.error_ship_cost", new String[] {" 상품코드 : " + goodsCode}));
				log.info("/return-ship-update");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			
			//배송비정책 MODIFY
			log.info("06.인터파크 상품수정 API 배송비정책 수정");
			responseMsg = paIntpCommonController.entpSlipCostModify(request);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
				paramMap.put("code","440");
				paramMap.put("message",getMessage("pa.error_ship_cost", new String[] {" 상품코드 : " + goodsCode}));
				log.info("/return-ship-update");
				log.info("code : "+paramMap.get("code"));
				log.info("Message : "+paramMap.get("message"));
			}
			//상품 수정 중 동기화배치가 돌아 상품정보 수정될 경우 다시 수정대상에 포함 하기위해 MODIFY_DATE와  수정대상조회 시작시간을 비교함
			String goodsListTime = systemService.getSysdatetimeToString();
			paramMap.put("goodsListTime", goodsListTime);
			
			//수정할 상품 검색
			log.info("07.인터파크 상품수정 API 상품 수정 대상 조회");
			paIntpGoodsList = paIntpGoodsService.selectPaIntpGoodsInfoList(paramMap);
			if(paIntpGoodsList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list", new String[] {" 상품코드 : " + goodsCode}));
				paramMap.put("resultMessage",paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}		
			
			
			HashMap<String, String> apiInfo = new HashMap<String, String>();
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			for(PaIntpGoodsVO paIntpGoods : paIntpGoodsList) {
				try {
					goodsModifyIntp(request, apiInfo, paramMap, paIntpGoods);
				}catch (Exception e) {
					log.error(e.toString());
					continue;
				}
			}
			/*
			for(int i=0; i<paIntpGoodsList.size(); i++) {
				try{
					HashMap<String, String> apiInfo = new HashMap<String, String>();
					apiInfo = systemService.selectPaApiInfo(paramMap);
					apiInfo.put("apiInfo", paramMap.getString("apiCode"));
					apiInfo.put("contentType", "text/xml;charset=utf-8");
					
					ParamMap asyncMap = new ParamMap();					
					PaIntpGoodsVO paIntpGoods = paIntpGoodsList.get(i);
					
					paIntpGoods.setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));
					asyncMap.put("url", apiInfo.get("API_URL"));
					asyncMap.put("paCode",paIntpGoods.getPaCode());
					asyncMap.put("siteGb", Constants.PA_INTP_PROC_ID);
					asyncMap.put("apiCode", prg_id);	
					
					if(paIntpGoods.getPaCode().equals(Constants.PA_INTP_BROAD_CODE)){
						asyncMap.put("paName", Constants.PA_INTP_BROAD);
					} else {
						asyncMap.put("paName", Constants.PA_INTP_ONLINE);
					}
					apiInfo.put("paName", asyncMap.getString("paName"));
					apiInfo.put("paCode", paIntpGoods.getPaCode());
					
					log.info("GOODS_CODE : " + paIntpGoods.getGoodsCode());
					//수정할 상품 인터파크 상품 코드 존재하지 않을시 처리
					if(StringUtil.isEmpty(paIntpGoods.getPrdNo())){
						paramMap.put("code","411");
						paramMap.put("message",getMessage("pa.not_exists_goods", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}

					paramMap.put("paGroupCode", paIntpGoods.getPaGroupCode());
					paramMap.put("goodsCode", paIntpGoods.getGoodsCode());
					paramMap.put("paCode", paIntpGoods.getPaCode());
					
					describeData = paCommonService.selectDescData(paramMap);
					paIntpGoods.setDescribeExt(ComUtil.getClobToString(describeData.get("DESCRIBE_EXT")));
					
					//수정할 상품 상세설명 존재하지 않을시 처리
					if(StringUtil.isEmpty(paIntpGoods.getDescribeExt())){
						paramMap.put("code","420");
						paramMap.put("message",getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}

					//상품 정보고시 조회
					paIntpGoodsOffer = paIntpGoodsService.selectPaIntpGoodsOfferList(paramMap);
					if(paIntpGoodsOffer.size() < 1){
						paramMap.put("code","430");
						paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
						continue;
					}
					
					//옵션정보
					List<PaGoodsdtMapping> goodsdtMapping = paIntpGoodsService.selectPaIntpGoodsdtInfoList(paramMap);
					//프로모션정보
					List<PaPromoTarget> paPromoTargetList = paIntpGoodsService.selectPaPromoTarget(paramMap);

					log.info("인터파크 상품수정 상품수정 API 호출 :" + paIntpGoods.getGoodsCode());
					
					Map<String, String> apiParamMap = new HashMap<String, String>();
					String dataUrl = "";
					String internalXmlUrl = apiInfo.get("INTERNAL_XML_URL");

					if(!"".equals(ComUtil.isNull(internalXmlUrl, ""))) {
						dataUrl = internalXmlUrl + "?goodsCode=" + paIntpGoods.getGoodsCode() + "&paCode=" + paIntpGoods.getPaCode();
						apiParamMap.put("dataUrl", dataUrl);
					}

					String xmlStr = goodsInsertToXmlFile(paIntpGoods, paIntpGoodsOffer, goodsdtMapping, paPromoTargetList, dataUrl);

					if("".equals(ComUtil.NVL(xmlStr, ""))) {
						paramMap.put("code","404");
						paramMap.put("message","인터파크 XML 데이터 생성 오류");
						continue;
					} else {
						
						apiInfo.put("body", xmlStr);						
						//비동기처리
						paIntpAsyncController.asyncGoodsModify(request, apiInfo, apiParamMap, asyncMap, paIntpGoods, goodsdtMapping, paPromoTargetList);
						Thread.sleep(50);
					}
				} catch (Exception e) {
					log.info("상품수정 Exception : "+paIntpGoodsList.get(i).getGoodsCode());
					log.info(e.getMessage());
				}
			}
			*/
			
			log.info("08.SK스토아 상품재고 수정대상 조회");
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
			
			log.info("09.SK스토아 상품재고코드 조회");
			responseMsg = goodsStockList(request, goodsCode, paCode, procId);
			log.info(PropertyUtils.describe(responseMsg.getBody()).get("code").toString());
			if(!PropertyUtils.describe(responseMsg.getBody()).get("code").equals("200")){
				if(PropertyUtils.describe(responseMsg.getBody()).get("code").equals("404")){
					paramMap.put("code","404");
					paramMap.put("message",PropertyUtils.describe(responseMsg.getBody()).get("message"));
				} else {
					paramMap.put("code","500");
					paramMap.put("message",getMessage("errors.api.system")+" : goodsStockList" + "("+PropertyUtils.describe(responseMsg.getBody()).get("message")+")");
				}
			}
			
		}catch (Exception e) {
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
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(!searchTermGb.equals("1")){
				if(duplicateCheck.equals("0")){
					systemService.checkCloseHistoryTx("end", prg_id);
				}				
			}
			
			log.info("9.인터파크 상품수정 저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 상품재고 조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품재고 조회", notes = "상품재고 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goods-stock-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsStockList(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=false) String goodsCode,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=false) String paCode,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PAINTP") String procId) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String dateTime = "";
		String duplicateCheck = "";
		//String request_type = "GET";
		//String rtnMsg = Constants.SAVE_SUCCESS;
		//Document doc = null;
		//PaGoodsTransLog paGoodsTransLog = null;
		List<PaIntpGoodsdtMappingVO> paGoodsdtMappingList = null;
		//List<PaIntpGoodsdtMappingVO> paGoodsdtList = new ArrayList<PaIntpGoodsdtMappingVO>();
		//PaIntpGoodsdtMappingVO paGoodsdtMapping = null;
	
		log.info("===== 인터파크 상품재고 조회 API Start=====");
		log.info("01.인터파크 상품재고 조회 API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PAINTPAPI_01_004";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
    	paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", dateTime);
		paramMap.put("code","200");
		paramMap.put("message","OK");
		
		try{
			log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			
			log.info("03.상품재고조회 대상 조회");
			paGoodsdtMappingList = paIntpGoodsService.selectEmptyPaOptionCodeList(paramMap);
			
			if(paGoodsdtMappingList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			for(int i=0; i<paGoodsdtMappingList.size(); i++){

				PaIntpGoodsdtMappingVO paGoodsdtMapping = paGoodsdtMappingList.get(i);
				
				Thread.sleep(100);
				
				paIntpAsyncController.goodsStock(paGoodsdtMapping, apiInfo);
				/*
				
				if(paGoodsdtMapping.getPaCode().equals(Constants.PA_INTP_BROAD_CODE)){
					paramMap.put("paName", Constants.PA_INTP_BROAD);
				} else {
					paramMap.put("paName", Constants.PA_INTP_ONLINE);
				}
				apiInfo.put("paName", paramMap.getString("paName"));
				apiInfo.put("paCode", paGoodsdtMapping.getPaCode());
				
				
				Map<String, String> apiParamMap = new HashMap<String, String>();
				apiParamMap.put("prdNo", paGoodsdtMapping.getPrdNo());
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
								paGoodsDt.setPaCode(paGoodsdtMapping.getPaCode());
								paGoodsDt.setGoodsCode(paGoodsdtMapping.getGoodsCode());
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
						continue;
					} else {
						log.info("04.인터파크  상품 재고 조회 완료 저장");
						
						rtnMsg = paIntpGoodsService.savePaIntpGoodsStockTx(paGoodsdtList);
						
						if(!rtnMsg.equals("000000")){
							paramMap.put("code","404");
							paramMap.put("message",getMessage("pa.not_exists_process_list"));
							continue;
						} else {
							paramMap.put("code","200");
							paramMap.put("message","OK");
						}
					}
				}

				if("200".equals(paramMap.getString("code"))) {
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paGoodsdtMapping.getGoodsCode());
					paGoodsTransLog.setPaCode(paGoodsdtMapping.getPaCode());
					paGoodsTransLog.setItemNo(paGoodsdtMapping.getPrdNo());
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn("1");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					paIntpGoodsService.insertPaIntpGoodsTransLogTx(paGoodsTransLog);
				}
				*/
			}
		}catch (Exception e) {
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
				paramMap.put("resultCode", "ERROR".equals(paramMap.getString("code")) ?"500" :  paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("05.상품재고 조회 저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품재고 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "상품재고 수정", notes = "상품재고 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/goods-stock-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsStockModify(HttpServletRequest request,
			@ApiParam(name = "goodsCode", value = "상품코드", defaultValue = "") @RequestParam(value="goodsCode", required=false) String goodsCode,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value="paCode", required=false) String paCode,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") @RequestParam(value="procId", required=false, defaultValue="PAINTP") String procId) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String dateTime = "";
		String duplicateCheck = "";
		String internalXmlUrl = "";
		String dataUrl = "";
		String request_type = "GET";
		String rtnMsg = Constants.SAVE_SUCCESS;
		Document doc = null;
		PaGoodsTransLog paGoodsTransLog = null;
		List<PaIntpGoodsdtMappingVO> paGoodsdtMappingList = null;
		List<PaIntpGoodsdtMappingVO> paGoodsdtList = null;
		PaIntpGoodsdtMappingVO paGoodsdtMapping = null;
	
		log.info("===== 인터파크 상품재고 수정 API Start=====");
		log.info("01.인터파크 상품재고 수정 API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PAINTPAPI_01_005";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
    	paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", dateTime);
		
		try{
			log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiInfo.put("contentType", "text/xml;charset=utf-8");
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			
			log.info("03.상품재고 수정대상 조회");
			paGoodsdtMappingList = paIntpGoodsService.selectPaIntpGoodsdtMappingStock(paramMap);
			
			if(paGoodsdtMappingList.size()==0){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("pa.not_exists_process_list"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			for(int i=0; i<paGoodsdtMappingList.size(); i++) {
				
				paGoodsdtMapping = paGoodsdtMappingList.get(i);
					
				paramMap.put("goodsCode", paGoodsdtMapping.getGoodsCode());
				paramMap.put("paCode", paGoodsdtMapping.getPaCode());
				paGoodsdtList = paIntpGoodsService.selectPaIntpGoodsdtMappingStockList(paramMap);
				
				if(paGoodsdtMapping.getPaCode().equals(Constants.PA_INTP_BROAD_CODE)){
					paramMap.put("paName", Constants.PA_INTP_BROAD);
				} else {
					paramMap.put("paName", Constants.PA_INTP_ONLINE);
				}
				apiInfo.put("paName", paramMap.getString("paName"));
				apiInfo.put("paCode", paGoodsdtMapping.getPaCode());
				
				Map<String, String> apiParamMap = new HashMap<String, String>();
				internalXmlUrl = apiInfo.get("INTERNAL_XML_URL");
				
				if(!"".equals(ComUtil.isNull(internalXmlUrl, ""))) {
					dataUrl = internalXmlUrl + "?goodsCode=" + paGoodsdtMapping.getGoodsCode() + "&paCode=" + paGoodsdtMapping.getPaCode();
					apiParamMap.put("dataUrl", dataUrl);
				}
				
				String xmlStr = productQtyMakeXmlFile(paGoodsdtList, dataUrl);
				
				if("".equals(ComUtil.NVL(xmlStr, ""))) {
					paramMap.put("code","404");
					paramMap.put("message","인터파크 XML 데이터 생성 오류");
					continue;
				} else {
					apiInfo.put("body", xmlStr);
					
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
							for(Node node = childeList.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){
								for(int k=0; k<node.getChildNodes().getLength(); k++){
			            			Node directionList = node.getChildNodes().item(k);
			            			map.put(node.getNodeName().trim(), directionList.getTextContent().trim());
			        			}
							}
						}
						
						if("error".equals(errorCheck)) {
							String code = map.get("code").replace("|", "");
							
							code = code.length() == 6 ? code.substring(2) : (code.length() == 5?code.substring(1):code);
							paramMap.put("code", code);
							paramMap.put("message",map.get("explanation"));
							continue;
						} else {
							log.info("04.인터파크  상품 재고 수정 동기화 완료 저장");
							
							rtnMsg = paIntpGoodsService.savePaIntpGoodsStockTx(paGoodsdtList);
							
							if(!rtnMsg.equals("000000")){
								paramMap.put("code","404");
								paramMap.put("message",getMessage("pa.not_exists_process_list"));
								continue;
							} else {
								paramMap.put("code","200");
								paramMap.put("message","OK");
							}
						}
					}
				}
				
				if("200".equals(paramMap.getString("code"))) {
					//전송관리 테이블 저장
					paGoodsTransLog = new PaGoodsTransLog();
					paGoodsTransLog.setGoodsCode(paGoodsdtMapping.getGoodsCode());
					paGoodsTransLog.setPaCode(paGoodsdtMapping.getPaCode());
					paGoodsTransLog.setItemNo(paGoodsdtMapping.getPrdNo());
					paGoodsTransLog.setRtnCode(paramMap.getString("code"));
					paGoodsTransLog.setRtnMsg(paramMap.getString("message"));
					paGoodsTransLog.setSuccessYn("1");
					paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsTransLog.setProcId(procId);
					paIntpGoodsService.insertPaIntpGoodsTransLogTx(paGoodsTransLog);
				}
			}
		}catch (Exception e) {
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
				paramMap.put("resultCode", "ERROR".equals(paramMap.getString("code")) ?"500" :  paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("06.상품재고 수정 저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 상품등록 수정 XML RETURN
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/goods-insert-xml", produces="text/xml;charset=EUC-KR" , method = RequestMethod.GET)
	@ResponseBody
	public String goodsInsertToXml(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "goodsCode"	, required = true, defaultValue = "") String goodsCode,
            @RequestParam(value = "paCode"	, required = true, defaultValue = "") String paCode) throws Exception {
		
		ParamMap paramMap	= new ParamMap();
		PaIntpGoodsVO paIntpGoods = null;
		List<PaGoodsOffer> paIntpGoodsOfferList = null;
		List<PaGoodsdtMapping> goodsdtMapping = null;
		List<PaPromoTarget> paPromoTargetList = null;
		HashMap<String, Object> describeData = null;
		String rtnVal = "";
	    
		//xml 파일을 떨구기 위한 경로와 파일 이름 지정해 주기
		try {
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			
			//상품정보 조회
			paIntpGoods = paIntpGoodsService.selectPaIntpGoodsInfo(paramMap);
			
			describeData = paCommonService.selectDescData(paramMap);
			paIntpGoods.setDescribeExt(ComUtil.getClobToString(describeData.get("DESCRIBE_EXT")));
			
			paramMap.put("paGroupCode", paIntpGoods.getPaGroupCode());
			//정보고시 조회
			paIntpGoodsOfferList = paIntpGoodsService.selectPaIntpGoodsOfferList(paramMap);
			
			goodsdtMapping = paIntpGoodsService.selectPaIntpGoodsdtInfoList(paramMap);
			paPromoTargetList = paIntpGoodsService.selectPaPromoTarget(paramMap);
			
			rtnVal = goodsInsertToXmlFile(paIntpGoods, paIntpGoodsOfferList, goodsdtMapping, paPromoTargetList, "");
			
		} catch (Exception e) {
		    log.error(e.getMessage(), e);
		}
		
		return rtnVal;
	}
	/**
	 * 상품 등록, 수정 XML 생성
	 * @return boolean
	 * @throws Exception
	 */
	public String goodsInsertToXmlFile(PaIntpGoodsVO paIntpGoods, List<PaGoodsOffer> paIntpGoodsOfferList, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> promoTargetList, String saveUrl) throws Exception {
		String rtnVal = null;
		
		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		String image_address = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
		String goodsName = paIntpGoods.getGoodsName();
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder;
	    docBuilder = docFactory.newDocumentBuilder();
	    //List<HashMap<String,Object>> paGoodsLimitCharMapList = paIntpGoodsService.selectGoodsLimitCharList(paIntpGoods.getPaCode());
	    
		try {
			// 루트 엘리먼트
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("result");
			doc.appendChild(rootElement);
			
			// item 엘리먼트
			Element item = doc.createElement("item");
			rootElement.appendChild(item);
			
			//상품 수정시 인터파크 상품 번호 추가
		    if(!"".equals(ComUtil.isNull(paIntpGoods.getPrdNo(), ""))) {
		    	ComUtil.makeCdataNode(doc, item, "prdNo", paIntpGoods.getPrdNo());
		    }
		    
		    //상품상태 - 새상품:01, 중고상품:02, 반품상품:03  
		    ComUtil.makeNode(doc, item, "prdStat", "01"); //새상품 고정
		    
		    //인터파크 상점번호 (default - 0000100000) 몰 넣어야되지??
		    ComUtil.makeNode(doc, item, "shopNo", "0000100000");
		    //인터파크 전시코드 
		    ComUtil.makeNode(doc, item, "omDispNo", paIntpGoods.getPaLmsdKey());

		    if("1".equals(paIntpGoods.getCollectYn())) {
                ComUtil.makeNode(doc, item, "proddelvCostUseYn", "Y");
                ComUtil.makeNode(doc, item, "delvCost", String.valueOf((long)paIntpGoods.getOrdCost()));
                ComUtil.makeNode(doc, item, "delvAmtPayTpCom", "01"); //배송비결제방식 착불:01, 선불:02, 선불전환착불가능:03
                ComUtil.makeNode(doc, item, "delvCostApplyTp", "02"); //배송비 적용 방식 - 개당:01, 무조건:02, n개당:03
                ComUtil.makeNode(doc, item, "freedelvStdCnt", "0"); //무료배송기준 수량 - 기준수량 입력 사용하지 않을 경우 0
                ComUtil.makeNode(doc, item, "stdQty", "0"); //n개당 배송 주문 수량 - 기준수량 입력 사용하지 않을 경우 0
		    	
		    	goodsName = "(착불)" + goodsName; //착불배송일경우 상품명앞에 착불표기 추가 21.11.05
		    }else {
		    	//상품배송비사용여부 - 상품배송비사용:Y, 업체배송비정책사용:N 
		    	ComUtil.makeNode(doc, item, "proddelvCostUseYn", "N");
		    	//묶음배송비번호
		    	ComUtil.makeNode(doc, item, "delvPlcNo", paIntpGoods.getGroupCode());
		    }
		    
		    
		    //특수문자 제한
		    /* 상품 수정 속도 저하 예상되어 주석처리. 필요시 주석 제거하면됨 2021.04.26 by ybsong
	    	if(paGoodsLimitCharMapList != null) {
	    		for(HashMap<String, Object> paGoodsLimitCharMap : paGoodsLimitCharMapList){
	    			String replaceChar = paGoodsLimitCharMap.get("REPLACE_CHAR").toString() == null ? "" : paGoodsLimitCharMap.get("REPLACE_CHAR").toString();
	    			goodsName = goodsName.replaceAll(paGoodsLimitCharMap.get("LIMIT_CHAR").toString(), replaceChar);
	    		}
	    		goodsName = goodsName.trim();
	    		goodsName = goodsName.replaceAll("    ", " ");
	    		goodsName = goodsName.replaceAll("   ", " ");
	    		goodsName = goodsName.replaceAll("  ",  "");
	    		goodsName = goodsName.replaceAll(" ",   " ");
	    	}
	    	*/
	    	//상품명 길이 제한		:: 2022/02/04 인터파크 상품명 길이 제한이 풀림 :: 
			/*
			 * if(goodsName.length() > 40) { goodsName = goodsName.substring(0,38) + "...";
			 * }
			 */ 
		    
		    //상품명 - 한글 60자 (영문/숫자 120자)
		    ComUtil.makeCdataNode(doc, item, "prdNm", goodsName);
		    //제조업체명 
		    ComUtil.makeCdataNode(doc, item, "hdelvMafcEntrNm", paIntpGoods.getMakecoName());
		    //원산지 
		    ComUtil.makeCdataNode(doc, item, "prdOriginTp", paIntpGoods.getOriginName());
		    //부가면세상품 - 과세상품:01, 면세상품:02, 영세상품:03
		    if("1".equals(paIntpGoods.getTaxSmallYn())) {
		    	ComUtil.makeNode(doc, item, "taxTp", "03");
		    }else if ("1".equals(paIntpGoods.getTaxYn())) {
		    	ComUtil.makeNode(doc, item, "taxTp", "01");
		    }else {
		    	ComUtil.makeNode(doc, item, "taxTp", "02");
		    }
		    //성인용품여부 - 성인용품:Y, 일반용품:N 
		    ComUtil.makeNode(doc, item, "ordAgeRstrYn", "1".equals(paIntpGoods.getAdultYn())? "Y" : "N");
		    //판매중:01, 품절:02, 판매중지:03, 일시품절:05, 예약판매:09, 상품삭제:98
		    ComUtil.makeNode(doc, item, "saleStatTp", "20".equals(paIntpGoods.getPaSaleGb())? "01" : "03");		    	
		    //판매가 
		    ComUtil.makeNode(doc, item, "saleUnitcost", String.valueOf((long)paIntpGoods.getSalePrice()));
		    //판매수량 - 99999 개 이하로 입력 
		    ComUtil.makeNode(doc, item, "saleLmtQty", paIntpGoods.getTransOrderAbleQty());
		    //판매시작일 - yyyyMMdd
		    ComUtil.makeNode(doc, item, "saleStrDts", paIntpGoods.getIntpSaleStartDate());
		    //판매종료일 - yyyyMMdd
		    ComUtil.makeNode(doc, item, "saleEndDts", paIntpGoods.getIntpSaleEndDate());
		    
		    /* 신세계쇼핑소스
		      installYn = paIntpGoods.getInstallYn();
		      
		      if("0".equals(mixpackYn) || "1".equals(installYn))
		      {
		      	 //택배:01, 우편(소포/등기):02, 화물배달(가구직배송):03,배송필요없음:00 (미입력시 디폴트로 '01' 설정됨)
		    	  String delvMthd = "1".equals(installYn) ?  "03" : "01"; 
		    	  ComUtil.makeNode(doc, item, "proddelvCostUseYn", "Y");
		    	  ComUtil.makeNode(doc, item, "delvCost", ""+paIntpGoods.getDelvCost());
		    	  ComUtil.makeNode(doc, item, "delvAmtPayTpCom", "02");
		    	  ComUtil.makeNode(doc, item, "delvMthd", delvMthd);
		    	  ComUtil.makeNode(doc, item, "freedelvStdCnt", String.valueOf(paIntpGoods.getFreeDelvStdCnt()));
		    	  ComUtil.makeNode(doc, item, "stdQty", "0");
		      }else{
		    	  //상품배송비사용여부 - 상품배송비사용:Y, 업체배송비정책사용:N 
		    	  ComUtil.makeNode(doc, item, "proddelvCostUseYn", paIntpGoods.getProddelvCostUseYn());
		    	  //묶음배송비번호
		    	  ComUtil.makeNode(doc, item, "delvPlcNo", paIntpGoods.getDelvPlcNo());
		      }
		      */
		    
		    //상품 반품택배비 사용여부
		    ComUtil.makeNode(doc, item, "prdrtnCostUseYn", "Y");
		    //상품 반품배송지번호. 업체 반품정책 사용하지 않을때 지정 
		    ComUtil.makeNode(doc, item, "rtndelvNo", paIntpGoods.getPaAddrSeq());
		    ComUtil.makeNode(doc, item, "rtndelvCost", String.valueOf((long)paIntpGoods.getReturnCost()));
		    
		    //상품설명
		    String goodsCom = "";
		    
		    goodsCom = (!ComUtil.NVL(paIntpGoods.getGoodsCom()).equals("")) ? ("<div style=\"line-height: 2.0em; font-family: 'NanumBarunGothic'; font-size: 19px;\"><div><h4>&middot;&nbsp;상품구성<h4><pre>" + paIntpGoods.getGoodsCom() + "</pre></div></div>") : "";
		    
		    if("".equals(paIntpGoods.getCollectImage()) || paIntpGoods.getCollectImage() == null) {
		    	ComUtil.makeCdataNode(doc, item, "prdBasisExplanEd",
		    			"<div align='center'><img alt='' src='" + paIntpGoods.getTopImage() + "' /><br /><br /><br />"	//상단 이미지
					  + goodsCom	//상품 구성
					  + paIntpGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='"	//기술서 본문 
					  + paIntpGoods.getBottomImage() + "' /></div>");	//하단 이미지
		    } else {
		    	ComUtil.makeCdataNode(doc, item, "prdBasisExplanEd",
		    			"<div align='center'><img alt='' src='" + paIntpGoods.getTopImage() + "' /><br /><br /><br /><img alt='' src='" + paIntpGoods.getCollectImage() + "' /><br /><br /><br />"	//상단 이미지 + 착불 이미지
					  + goodsCom	//상품 구성
					  + paIntpGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='"	//기술서 본문 
					  + paIntpGoods.getBottomImage() + "' /></div>");	//하단 이미지
		    }
		    
		    //대표이미지 - 대표이미지 URL, 영문/숫자 조합, JPG와 PNG만 가능 
		    ComUtil.makeCdataNode(doc, item, "zoomImg", image_address + paIntpGoods.getImageUrl() + paIntpGoods.getImageP());
		  		    
		    //쇼핑태그 - 최대 4개까지, 콤마로 구분	
		    ComUtil.makeCdataNode(doc, item, "prdKeywd", "sk스토아,skstoa,에스케이스토아"); //100 byte 이상 오뷰 발생(EUC-KR 2byte)
		    
		    //브랜드번호 
		    ComUtil.makeNode(doc, item, "brandNo", paIntpGoods.getBrandNo());
		    //브랜드명 
		    ComUtil.makeCdataNode(doc, item, "prdModelNo", paIntpGoods.getBrandName());
		    //1회당 주문 제한 수량
		    ComUtil.makeNode(doc, item, "perordRstrQty", String.valueOf(paIntpGoods.getOrderMaxQty()));
		    
		    //선택형 옵션 이름을 지정할때 사용 선택1을 타입, 선택2를 색상으로 하는 경우 타입,사이즈 으로 태그 설정. 지정 하지 않은 경우 선택1,선택2로 기본 지정됨
		    StringBuilder sbOpt = new StringBuilder();
		    for(int i=0; i<goodsdtMapping.size(); i++) {
		    	if(i == 0) {
		    		ComUtil.makeNode(doc, item, "selOptName", goodsdtMapping.get(i).getGoodsdtInfoKind());
		    		//선택형 옵션노출 정렬 유형. 옵션이 있을 경우 필수. 01-등록순, 02-가나다순. 선택형 옵션만 적용됨.
		    		ComUtil.makeNode(doc, item, "optPrirTp", "01");
		    	}
		    	sbOpt.append("{");
		    	sbOpt.append(goodsdtMapping.get(i).getGoodsdtInfo());
		    	sbOpt.append("수량<");
		    	sbOpt.append(goodsdtMapping.get(i).getTransOrderAbleQty());
		    	sbOpt.append(">옵션코드<");
		    	sbOpt.append(goodsdtMapping.get(i).getGoodsdtCode());
		    	sbOpt.append(">사용여부<");
		    	if(ComUtil.objToInt(goodsdtMapping.get(i).getTransOrderAbleQty()) > 0) {
		    		sbOpt.append("Y");
		    	}else {
		    		sbOpt.append("N");
		    	}
		    	sbOpt.append(">}");		    	
		    }
		    //선택형 옵션(상품옵션)
		    ComUtil.makeCdataNode(doc, item, "prdOption", sbOpt.toString());
		    
		    //ComUtil.makeCdataNode(doc, item, "delvCostApplyTp", "02");
		    //제주도서산간배송비사용여부 - Y : 등록/수정, N : 사용안함
            if(paIntpGoods.getJejuCost() > 0 || paIntpGoods.getIslandCost() > 0) {
		    	ComUtil.makeNode(doc, item, "jejuetcDelvCostUseYn", "Y");
		    	//제주배송비 - jejuetcDelvCostUseYn가 Y일때 제주배송비와 도서산간비 둘 중 하나는 필수, 0이면 제주배송비 0원, null이면 사용안함 
		    	ComUtil.makeNode(doc, item, "jejuDelvCost", String.valueOf((long)paIntpGoods.getJejuCost()));
		    	//도서산간배송비 - jejuetcDelvCostUseYn가 Y일때 제주배송비와 도서산간비 둘 중 하나는 필수, 0이면 도서산간배송비 0원, null이면 사용안함
		    	ComUtil.makeNode(doc, item, "etcDelvCost", String.valueOf((long)paIntpGoods.getIslandCost()));
		    }else {
		    	ComUtil.makeNode(doc, item, "jejuetcDelvCostUseYn", "N");
		    }
		    
		    //원상품번호(제휴업체상품코드)
		    ComUtil.makeNode(doc, item, "originPrdNo", paIntpGoods.getGoodsCode());
		    //A/S정보
		    ComUtil.makeCdataNode(doc, item, "asInfo", "상세설명참조");
		      
		    StringBuilder sbImg = new StringBuilder();
		    if(paIntpGoods.getImageAP()!=null) {
		    	sbImg.append(image_address + paIntpGoods.getImageUrl() + paIntpGoods.getImageAP()).append(",");
		    }
		    if(paIntpGoods.getImageBP()!=null) {
		    	sbImg.append(image_address + paIntpGoods.getImageUrl() + paIntpGoods.getImageBP()).append(",");
		    }
		    if(paIntpGoods.getImageCP()!=null) {
		    	sbImg.append(image_address + paIntpGoods.getImageUrl() + paIntpGoods.getImageCP()).append(",");
		    }
		    
		    if(sbImg.length() > 0) {
		    	sbImg.deleteCharAt(sbImg.length() - 1);
		    	//상세이미지 - 상세이미지 URL, 영문/숫자 조합, JPG와 PNG만 가능 최대 3개의 이미지까지, 콤마(,)로 구분하여 등록.
		    	ComUtil.makeCdataNode(doc, item, "detailImg", sbImg.toString());
		    }
		    
		    //이미지 변경여부
		    if("1".equals(paIntpGoods.getImageTransYn())) {
		    	ComUtil.makeNode(doc, item, "imgUpdateYn", "Y");
		    }else {
		    	ComUtil.makeNode(doc, item, "imgUpdateYn", "N");
		    }
		    
		    //해외구매대행 또는 해외항공배송 상품
		    ComUtil.makeNode(doc, item, "abroadBsYn", "N");
		    
		    //상품인증여부 생활용품/전기용품/방송통신용품/어린이제품에 대한 인증 여부 (의료기기, 건강기능식품, 제조/가공식품 인증 대상 제외)
		    if("1".equals(paIntpGoods.getLifeCertYn()) || "1".equals(paIntpGoods.getElectricCertYn()) || "1".equals(paIntpGoods.getChildCertYn())) {
		    	ComUtil.makeNode(doc, item, "prdCertStatus", "S");
		    }    		
		    
		    if("1".equals(paIntpGoods.getMedicalYn())) {
		    	ComUtil.makeNode(doc, item, "medicalCertTp", "0303");
		    }
		    
		    if("1".equals(paIntpGoods.getHealthYn())) {
		    	ComUtil.makeNode(doc, item, "healthCertTp", "0403");
		    }
		    
		    if("1".equals(paIntpGoods.getFoodYn())) {
		    	ComUtil.makeNode(doc, item, "foodCertTp", "0503");
		    }
		    
		    Element prdinfoNoti = doc.createElement("prdinfoNoti");
		    
		    for(int i=0; i<paIntpGoodsOfferList.size(); i++) {		    	
		    	Element info = doc.createElement("info");
		    	item.appendChild(prdinfoNoti);
		    	prdinfoNoti.appendChild(info);
		        		
		    	String paOfferExt = paIntpGoodsOfferList.get(i).getPaOfferExt();
		    	ComUtil.makeCdataNode(doc, info, "infoSubNo", paIntpGoodsOfferList.get(i).getPaOfferCode());
		    	if("N".equals(paIntpGoodsOfferList.get(i).getRemark1())) {
		    		ComUtil.makeNode(doc, info, "infoCd", "N");
		    	} else {
		    		ComUtil.makeNode(doc, info, "infoCd", "I");
		    	}
		    	ComUtil.makeNode(doc, info, "infoTx", paOfferExt);
		    }
		    
		    //판매자부담 즉시할인 설정여부
		    double couponPrice = 0;
		    if(promoTargetList != null && promoTargetList.size() > 0) {
				for(PaPromoTarget paPromoTarget : promoTargetList) {
					if(paPromoTarget != null) {
						if(!paPromoTarget.getProcGb().equals("D")) {
							log.info("### couponPrice : "+paPromoTarget.getDoCost());
							couponPrice += paPromoTarget.getDoCost();	//할인금액(자동적용쿠폰 + 제휴OUT)
						}
					}
				}
			}
		    
		    double dcAllPrict = paIntpGoods.getDcAmt() + paIntpGoods.getLumpSumDcAmt() + couponPrice;
		    if(dcAllPrict > 0) {
		    	ComUtil.makeNode(doc, item, "entrDcUseYn", "Y");		
		    	//판매자부담 즉시할인 할인구분
		    	ComUtil.makeNode(doc, item, "entrDcTp", "2");
		    	//판매자부담 즉시할인 할인률/할인금액
		    	ComUtil.makeNode(doc, item, "entrDcNum", String.valueOf((long)dcAllPrict));
		    	//판매자부담 즉시할인 할인시작일 - yyyyMMdd 미입력시 상품 판매기간동안 할인 적용
		    	//ComUtil.makeNode(doc, item, "entrDcStrDt", ""+paIntpGoods.getEntrDcStrDt());
		    	//판매자부담 즉시할인 할인종료일 - yyyyMMdd 미입력시 상품 판매기간동안 할인 적용
		    	//ComUtil.makeNode(doc, item, "entrDcEndDt", ""+paIntpGoods.getEntrDcEndDt());
		    } else {
		    	ComUtil.makeNode(doc, item, "entrDcUseYn", "N");
		    }
		      
		    //XML 파일로 쓰기
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
	
			transformer.setOutputProperty(OutputKeys.ENCODING, "EUC_KR");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");	
			DOMSource source = new DOMSource(doc);
			
			// XML 문자열로 변환하기!
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(out);
			// 출력 시 문자코드: EUC-KR
			transformer.setOutputProperty(OutputKeys.ENCODING, "EUC-KR");
			// 들여 쓰기 있음
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			
			rtnVal =  new String(out.toByteArray(), Charset.forName("EUC-KR"));
		      
		}catch (Exception e) {
			rtnVal = null;
			log.error(e.getMessage());
		}
		
		return rtnVal;
	}
	
	/**
	 * 상품 재고 수정 XML RETURN
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/product_qty_update_xml", produces="text/xml;charset=EUC-KR" , method = RequestMethod.GET)
	@ResponseBody
	public String productQtyUpdateXml(HttpServletRequest request,
			@ApiParam(name="paCode", 	 required=true, value="제휴사코드") @RequestParam(value="paCode", required=true) String paCode,
			@ApiParam(name="goodsCode",  required=true, value="상품코드")   @RequestParam(value="goodsCode", required=true) String goodsCode) throws Exception{

		ParamMap paramMap	= new ParamMap();
		String rtnVal = "";
		List<PaIntpGoodsdtMappingVO> paGoodsdtList = null;

		try {
			
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			
			paGoodsdtList = paIntpGoodsService.selectPaIntpGoodsdtMappingStockList(paramMap);
			
			rtnVal = productQtyMakeXmlFile(paGoodsdtList,"");
			
		} catch (Exception e) {
		    log.error(e.getMessage(), e);
		}
		
		return rtnVal;
	}
	
	public String productQtyMakeXmlFile(List<PaIntpGoodsdtMappingVO> goodsdtMapping, String saveUrl) throws Exception
	{
		String rtnVal = null;
		
		Map<String, String> xmlMap = new HashMap<String, String>();
		
		try {
			// 인터파크 상품번호 
			xmlMap.put("prdNo", goodsdtMapping.get(0).getPrdNo());
			// 판매중:01, 품절:02, 판매중지:03, 일시품절:05, 상품삭제:98
			xmlMap.put("saleStatTp", "01");
			
			StringBuilder sbOpt = new StringBuilder();
			for(int i=0; i<goodsdtMapping.size(); i++) {
		    	sbOpt.append("{");
		    	sbOpt.append(goodsdtMapping.get(i).getGoodsdtInfo());
		    	sbOpt.append("수량<");
		    	sbOpt.append(goodsdtMapping.get(i).getTransOrderAbleQty());
		    	sbOpt.append(">}");		    	
		    }
			
			// 선택형 옵션(상품옵션), 옵션이 있을 경우 필수로 선택해야 하는 사항들
			xmlMap.put("prdOption", sbOpt.toString());
			
			rtnVal = paIntpComUtil.mapToXml(xmlMap);
			
		} catch (Exception e) {
			rtnVal = null;
		    log.error(e.getMessage(), e);
		}
		
		return rtnVal;
	}
	
	public String subStringEuckr(String strData, int iStartPos, int iByteLength) {
		byte[] bytTemp = null;
		int iRealStart = 0;
		int iRealEnd = 0;
		int iLength = 0;
		int iChar = 0;

		try {
			// UTF-8로 변환하는경우 한글 2Byte, 기타 1Byte로 떨어짐
			bytTemp = strData.getBytes("EUC-KR");
			iLength = bytTemp.length;

			for(int iIndex = 0; iIndex < iLength; iIndex++) {
				if(iStartPos <= iIndex) {
					break;
				}
				iChar = (int)bytTemp[iIndex];
				if((iChar > 127)|| (iChar < 0)) {
					// 한글의 경우(2byte 통과처리)
					// 한글은 2Byte이기 때문에 다음 글자는 볼것도 없이 스킵한다
					iRealStart++;
					iIndex++;
				} else {
					// 기타 글씨(1Byte 통과처리)
					iRealStart++;
				}
			}

			iRealEnd = iRealStart;
			int iEndLength = iRealStart + iByteLength;
			for(int iIndex = iRealStart; iIndex < iEndLength; iIndex++)
			{
				iChar = (int)bytTemp[iIndex];
				if((iChar > 127)|| (iChar < 0)) {
					// 한글의 경우(2byte 통과처리)
					// 한글은 2Byte이기 때문에 다음 글자는 볼것도 없이 스킵한다
					iRealEnd++;
					iIndex++;
				} else {
					// 기타 글씨(1Byte 통과처리)
					iRealEnd++;
				}
			}
		} catch(Exception e) {
			//
			log.info("DEBUG",e.getMessage());
		}

		return strData.substring(iRealStart, iRealEnd);
	}
	
	public void goodsModifyIntp(HttpServletRequest request, HashMap<String, String> apiInfo, ParamMap paramMap, PaIntpGoodsVO paIntpGoods) {

		String prg_id 						 = paramMap.getString("apiCode");
		String goodsListTime				 = paramMap.getString("goodsListTime");
		String goodsCode					 = paramMap.getString("goodsCode");
		HashMap<String, Object>	describeData = null;
		HashMap<String, Object>	paNoticeData = null;
		try{
			
			ParamMap asyncMap = new ParamMap();					
			paIntpGoods.setLastModifyDate(DateUtil.toTimestamp(goodsListTime, "yyyy/MM/dd HH:mm:ss"));
			asyncMap.put("url"		, apiInfo.get("API_URL"));
			asyncMap.put("paCode"	, paIntpGoods.getPaCode());
			asyncMap.put("siteGb"	, Constants.PA_INTP_PROC_ID);
			asyncMap.put("apiCode"	, prg_id);	
			
			if(paIntpGoods.getPaCode().equals(Constants.PA_INTP_BROAD_CODE)){
				asyncMap.put("paName", Constants.PA_INTP_BROAD);
			} else {
				asyncMap.put("paName", Constants.PA_INTP_ONLINE);
			}
			apiInfo.put("paName", asyncMap.getString("paName"));
			apiInfo.put("paCode", paIntpGoods.getPaCode());
			
			log.info("GOODS_CODE : " + paIntpGoods.getGoodsCode());
			//수정할 상품 인터파크 상품 코드 존재하지 않을시 처리
			if(StringUtil.isEmpty(paIntpGoods.getPrdNo())){
				paramMap.put("code","411");
				paramMap.put("message",	getMessage("pa.not_exists_goods", new String[] {"상품코드 : " + goodsCode}));
				return;
			}

			paramMap.put("paGroupCode"	, paIntpGoods.getPaGroupCode());
			paramMap.put("goodsCode"	, paIntpGoods.getGoodsCode());
			paramMap.put("paCode"		, paIntpGoods.getPaCode());
			
			describeData = paCommonService.selectDescData(paramMap);
			paIntpGoods.setDescribeExt(ComUtil.getClobToString(describeData.get("DESCRIBE_EXT")));
			
			//수정할 상품 상세설명 존재하지 않을시 처리
			if(StringUtil.isEmpty(paIntpGoods.getDescribeExt())){
				paramMap.put("code","420");
				paramMap.put("message",	getMessage("pa.insert_goods_describe", new String[] {"상품코드 : " + goodsCode}));
				return;
			}
			
			paNoticeData = paIntpGoodsService.selectPaNoticeData(paramMap);
			if(paNoticeData != null) {
				paIntpGoods.setNoticeExt(ComUtil.getClobToString(paNoticeData.get("NOTICE_EXT")));
				
				if(!ComUtil.NVL(paIntpGoods.getNoticeExt()).equals("")) {
					paIntpGoods.setDescribeExt(paIntpGoods.getNoticeExt() + paIntpGoods.getDescribeExt());
				}
			}

			//상품 정보고시 조회
			 List<PaGoodsOffer> paIntpGoodsOffer = paIntpGoodsService.selectPaIntpGoodsOfferList(paramMap);
			if(paIntpGoodsOffer.size() < 1){
				paramMap.put("code","430");
				paramMap.put("message",getMessage("pa.insert_goods_offer", new String[] {"상품코드 : " + goodsCode}));
				return;
			}
			
			//옵션정보
			List<PaGoodsdtMapping> goodsdtMapping = paIntpGoodsService.selectPaIntpGoodsdtInfoList(paramMap);
			//프로모션정보
			List<PaPromoTarget> paPromoTargetList = paIntpGoodsService.selectPaPromoTarget(paramMap);

			log.info("인터파크 상품수정 상품수정 API 호출 :" + paIntpGoods.getGoodsCode());
			
			Map<String, String> apiParamMap = new HashMap<String, String>();
			String dataUrl 					= "";
			String internalXmlUrl 			= apiInfo.get("INTERNAL_XML_URL");

			if(!"".equals(ComUtil.isNull(internalXmlUrl, ""))) {
				dataUrl = internalXmlUrl + "?goodsCode=" + paIntpGoods.getGoodsCode() + "&paCode=" + paIntpGoods.getPaCode();
				apiParamMap.put("dataUrl", dataUrl);
			}

			String xmlStr = goodsInsertToXmlFile(paIntpGoods, paIntpGoodsOffer, goodsdtMapping, paPromoTargetList, dataUrl);

			if("".equals(ComUtil.NVL(xmlStr, ""))) {
				paramMap.put("code","404");
				paramMap.put("message","인터파크 XML 데이터 생성 오류");
				return;
			} else {
				apiInfo.put("body", xmlStr);						
				//비동기처리
				paIntpAsyncController.asyncGoodsModify(request, apiInfo, apiParamMap, asyncMap, paIntpGoods, goodsdtMapping, paPromoTargetList);
				Thread.sleep(50);
			}
		} catch (Exception e) {
			log.info("상품수정 Exception : "+paIntpGoods.getGoodsCode());
			log.info(e.getMessage());
		}
	}
	
}