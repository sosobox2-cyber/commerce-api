package com.cware.api.paintp.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaIntpDelvSettlement;
import com.cware.netshopping.domain.model.PaIntpSettlement;
import com.cware.netshopping.paintp.common.service.PaIntpCommonService;
import com.cware.netshopping.paintp.util.PaIntpComUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@Api(value="/paintp/common", description="기초정보")
@Controller("com.cware.api.paintp.PaIntpCommonController")
@RequestMapping(value="/paintp/common")

public class PaIntpCommonController extends AbstractController {

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "paintp.common.paIntpCommonService")
	private PaIntpCommonService paIntpCommonService;
	
	@Resource(name = "com.cware.netshopping.paintp.util.PaIntpComUtil")
	private PaIntpComUtil paIntpComUtil;
	
	/**
	 * 기본전시 조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "기본전시 조회", notes = "기본전시 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 410, message = "제휴사코드를 확인하세요."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   
			   })
	@RequestMapping(value = "/goodsKinds-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsKindsList(HttpServletRequest request,
			@ApiParam(name="fromDate", required=false, value="FROM날짜", defaultValue = "") @RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,			
			@ApiParam(name="toDate",   required=false, value="TO날짜",   defaultValue = "") @RequestParam(value="toDate", required=false, defaultValue = "") String toDate ) throws Exception{
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		List<PaGoodsKinds> paGoodsKindsList = new ArrayList<PaGoodsKinds>();
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Document doc = null;
		
		String dateTime = "";
		String startDate = "";
		String endDate = "";
		String duplicateCheck = "";
		String paCode = "71"; //방송계정으로 고정처리
		
		log.info("===== 기본전시 조회 API Start =====");
		log.info("01.기본전시 조회 API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PAINTPAPI_00_001";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", dateTime);
		
		try{
			log.info("02.기본전시 조회 API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			paramMap.put("paCode", paCode);
			if(paCode.equals(Constants.PA_INTP_BROAD_CODE)){
				paramMap.put("paName", Constants.PA_INTP_BROAD);
			} else {
				paramMap.put("paName", Constants.PA_INTP_ONLINE);
			}
			apiInfo.put("paName", paramMap.getString("paName"));
			apiInfo.put("paCode", paCode);
			
			Map<String, String> apiParamMap = new HashMap<String, String>();

			//기간입력
			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				startDate = fromDate;
				endDate = toDate;
			} else{ //예외(조회기간 시작일(YYYYMMDD). 전시 정보 변경일을 기준으로 합니다.)
				startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), 0, DateUtil.GENERAL_DATE_FORMAT);
				endDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), 0, DateUtil.GENERAL_DATE_FORMAT);
			}
					
			apiParamMap.put("strDt", startDate);
			apiParamMap.put("endDt", endDate);
			apiParamMap.put("dispYn", "Y");
			
			HttpResponse response = ComUtil.paIntpConnectionSetting(apiInfo,apiParamMap);
			
			ParamMap resParam = ComUtil.parseIntpCommonResponse(response);
			
			if("200".equals(resParam.getString("code"))) {
				Map<String, String> map = new HashMap<String, String>();
				doc = (Document) resParam.get("data");
				String errorCheck = "";
				NodeList childeList = doc.getFirstChild().getChildNodes();
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				
				for(int j=0; j<childeList.getLength();j++){
					if("error".equals(childeList.item(j).getNodeName())){
						errorCheck = "error";
						map.put("code", childeList.item(j).getFirstChild().getNextSibling().getTextContent());
						map.put("explanation", childeList.item(j).getLastChild().getPreviousSibling().getTextContent());
					}
					ParamMap paIntpParamMap = new ParamMap();
					for(Node node = childeList.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){
						for(int i=0; i<node.getChildNodes().getLength(); i++){
	            			Node directionList = node.getChildNodes().item(i);
	            			paIntpParamMap.put(node.getNodeName().trim(), directionList.getTextContent().trim());
	        			}
					}
					if(!paIntpParamMap.get().isEmpty()) responseList.add(paIntpParamMap);
				}
				
				if("error".equals(errorCheck)) {
					String code = map.get("code").replace("|", "");
					paramMap.put("code", code.substring(1));
					paramMap.put("message", map.get("explanation"));
					rtnMsg = map.get("explanation");
				} else {
					log.info("03.기본전시 조회 API후 오픈마켓 표준카테고리(공통) 저장");
					String paLmsdKey = "";
					String dgroupName = "";
					String infoGroupNo = "";
					
					for(int k=0; k<responseList.size(); k++){
						
						paLmsdKey = responseList.get(k).getString("dispNo");
						dgroupName = responseList.get(k).getString("dispNm");
						infoGroupNo = responseList.get(k).getString("infoGroupNo");
						
						String[] groupName = dgroupName.split(">");
						
						PaGoodsKinds paGoodsKinds = new PaGoodsKinds();
						paGoodsKinds.setPaGroupCode(Constants.PA_INTP_GROUP_CODE);
						paGoodsKinds.setPaLmsdKey(paLmsdKey);
						paGoodsKinds.setPaLgroup(paLmsdKey.substring(0,6));
						paGoodsKinds.setPaLgroupName(groupName[0]);
						paGoodsKinds.setPaMgroup(paLmsdKey.substring(6,9));
						paGoodsKinds.setPaMgroupName(groupName[1]);
						if(paLmsdKey.length() > 9) {
							paGoodsKinds.setPaSgroup(paLmsdKey.substring(9,12));
							paGoodsKinds.setPaSgroupName(groupName[2]);
						}
						if(paLmsdKey.length() > 12){
							paGoodsKinds.setPaDgroup(paLmsdKey.substring(12,15));
							paGoodsKinds.setPaDgroupName(groupName[3]);
						}
						paGoodsKinds.setRemark(infoGroupNo);
						paGoodsKinds.setInsertId(Constants.PA_INTP_PROC_ID);
						paGoodsKinds.setModifyId(Constants.PA_INTP_PROC_ID);
						paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						paGoodsKinds.setLifeCertYn(responseList.get(k).getString("industrial"));
						paGoodsKinds.setElectricCertYn(responseList.get(k).getString("electric"));
						paGoodsKinds.setChildCertYn(responseList.get(k).getString("child"));
						paGoodsKinds.setMedicalYn(responseList.get(k).getString("medical"));
						paGoodsKinds.setHealthYn(responseList.get(k).getString("health"));
						paGoodsKinds.setFoodYn(responseList.get(k).getString("food"));
						
						paGoodsKindsList.add(paGoodsKinds);
						
						paLmsdKey = "";
						dgroupName = "";
						infoGroupNo = "";
					}
					
					log.info("04.기본전시카테고리 저장");
					
					rtnMsg = paIntpCommonService.savePaIntpGoodsKindsTx(paGoodsKindsList);
					
				}
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
					paramMap.put("code","404");
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else {
					paramMap.put("code","200");
					paramMap.put("message","OK");
				}
			} else{
				paramMap.put("code","500");
				paramMap.put("message",resParam.getString("message"));
			}
		} catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally{
			try{
				paramMap.put("message", paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);	
				systemService.insertApiTrackingTx(request,paramMap);
				log.info("===== 기본전시 조회 API End=====");
			} catch(Exception e){
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("05.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 브랜드조회 IF_PAWMKPAPI_01_007
	 * @return ResponseEntity
	 * @throws Exception
	 */
	
	@ApiOperation(value = "브랜드조회", notes = "브랜드조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") })
	@RequestMapping(value = "/brand-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> brandList(HttpServletRequest request,
			@ApiParam(name="fromDate", required=false, value="FROM날짜", defaultValue = "") @RequestParam(value="fromDate", required=false, defaultValue = "") String fromDate,
			@ApiParam(name="toDate",   required=false, value="TO날짜",   defaultValue = "") @RequestParam(value="toDate", required=false, defaultValue = "") String toDate,	
			@ApiParam(name="pageNumber",   required=false, value="페이지번호",   defaultValue = "") @RequestParam(value="pageNumber", required=false, defaultValue = "") String pageNumber ) throws Exception{
		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		ParamMap paramMap = new ParamMap();
		PaBrand paBrand = null;

		Document doc = null;
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		String dateTime = "";
		String startDate = "";
		String endDate = "";
		String duplicateCheck = "";
		int page = 1;
		String paCode = "71"; //방송계정으로 고정처리

		log.info("===== 브랜드 조회 API Start=====");
		log.info("01.브랜드 조회 API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PAINTPAPI_00_002";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", dateTime);

		try {
			log.info("02.API 중복 실행 검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated",new String[] { paramMap.getString("apiCode") });

			log.info("03.브랜드 조회 API 호출");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			paramMap.put("paCode", paCode);
			if(paCode.equals(Constants.PA_INTP_BROAD_CODE)){
				paramMap.put("paName", Constants.PA_INTP_BROAD);
			} else {
				paramMap.put("paName", Constants.PA_INTP_ONLINE);
			}
			apiInfo.put("paName", paramMap.getString("paName"));
			apiInfo.put("paCode", paCode);

			Map<String, String> apiParamMap = new HashMap<String, String>();

			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				startDate = fromDate;
				endDate = toDate;
			} else {
				startDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT);
				endDate = DateUtil.addDay(DateUtil.getCurrentDateAsString(), +1, DateUtil.GENERAL_DATE_FORMAT);
			}			
			
			while(rtnMsg.equals(Constants.SAVE_SUCCESS)) {
				List<PaBrand> paBrandList = new ArrayList<PaBrand>();
				List<ParamMap> responseList = new ArrayList<ParamMap>();
				
				apiParamMap.put("pageNum", Integer.toString(page));
					
				apiParamMap.put("strDts", startDate);
				apiParamMap.put("endDts", endDate);
				
				HttpResponse response = ComUtil.paIntpConnectionSetting(apiInfo, apiParamMap);
				
				ParamMap resParam = ComUtil.parseIntpCommonResponse(response);
				
				if("200".equals(resParam.getString("code"))) {
					Map<String, String> map = new HashMap<String, String>();
					String errorCheck = "";
					doc = (Document) resParam.get("data");
					NodeList childeList = doc.getFirstChild().getChildNodes();
					
					for(int j = 0; j < childeList.getLength(); j++){
						if("error".equals(childeList.item(j).getNodeName())) {
							errorCheck = "error";
							map.put("code", childeList.item(j).getFirstChild().getNextSibling().getTextContent());
							map.put("explanation", childeList.item(j).getLastChild().getPreviousSibling().getTextContent());
						}
						ParamMap paIntpParamMap = new ParamMap();
						for(Node node = childeList.item(j).getFirstChild(); node!=null; node=node.getNextSibling()){
							for(int n=0; n<node.getChildNodes().getLength(); n++){
		            			Node directionList = node.getChildNodes().item(n);
		            			
		            			System.out.println(node.getNodeName().trim() + " : " + directionList.getTextContent().trim());
		            			
		            			paIntpParamMap.put(node.getNodeName().trim(), directionList.getTextContent().trim());
		        			}
						}
						if(!paIntpParamMap.get().isEmpty()) responseList.add(paIntpParamMap);
					}
					
					if("error".equals(errorCheck)) {
						String code = map.get("code").replace("|", "");
						paramMap.put("code", code.substring(1));
						paramMap.put("message", map.get("explanation"));
						rtnMsg = map.get("explanation");
					} else {
						for(int k = 0; k < responseList.size(); k++) {
							paBrand = new PaBrand();
							paBrand.setPaGroupCode(Constants.PA_INTP_GROUP_CODE);
							paBrand.setPaBrandNo(responseList.get(k).getString("brandNo"));
							paBrand.setPaBrandName(responseList.get(k).getString("brandNm"));
							paBrand.setInsertId(Constants.PA_INTP_PROC_ID);
							paBrand.setModifyId(Constants.PA_INTP_PROC_ID);
							paBrand.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paBrand.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							paBrandList.add(paBrand);
						}
						log.info("03.제휴 브랜드 저장");
						rtnMsg = paIntpCommonService.savePaIntpBrandTx(paBrandList);
					}
						
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						paramMap.put("code","404");
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else {
						page++;
						paramMap.put("code","200");
						paramMap.put("message","OK");
					}
				} else {
					rtnMsg = resParam.getString("message");
					paramMap.put("code","500");
					paramMap.put("message",resParam.getString("message"));
				}
			}
		} catch (Exception e) {
			if(duplicateCheck.equals("1")){
				paramMap.put("code","490");
			} else {
				paramMap.put("code","500");
			}
			paramMap.put("message",getMessage("errors.api.system",new String[] { "saveIntpReturn" }));
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try{
				paramMap.put("message", paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);	
				systemService.insertApiTrackingTx(request,paramMap);
				log.info("===== 브랜드 조회 API End=====");
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}
		}	
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}
	

	/**
	 * 반품배송지등록
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "반품배송지등록", notes = "반품배송지등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 405, message = "XML 데이터 생성 오류."),
			   @ApiResponse(code = 410, message = "주소를 확인하세요."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.") 
			   
			   })
	@RequestMapping(value = "/returnslip-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnSlipInsert(HttpServletRequest request,
			@ApiParam(name="entpCode", 		 value="업체코드", 	defaultValue = "")  @RequestParam(value="entpCode", required=true) String entpCode,			
			@ApiParam(name="entpManSeq",	 value="업체담당자순번", defaultValue = "") @RequestParam(value="entpManSeq", required=true) String entpManSeq,
			@ApiParam(name="paCode", 		 value="제휴사코드", defaultValue = "")    @RequestParam(value="paCode", required=true) String paCode,
			@ApiParam(name="searchTearmGb",  value="중복체크", defaultValue = "")     @RequestParam(value="searchTearmGb", required=true) String searchTearmGb) throws Exception{

		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Document doc = null;
		String dataUrl = "";
		String dateTime = "";
		String duplicateCheck = "";
		String internalXmlUrl = "";
		
		String postAddr 	= ""; //주소
		String dtlsAddr 	= ""; //상세주소
		String gnrlTlphnNo  = ""; //일반전화번호
		String prtblTlphnNo = ""; //휴대전화번호
		String addrNm 	 	= ""; //주소명
		String zipCd 		= ""; //반품배송지우편번호
		
		log.info("===== 반품배송지등록 API Start =====");
		log.info("01.API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PAINTPAPI_00_004";
		String request_type = "POST";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("paCode", paCode);
		
		try{
			
			log.info("02.API 중복실행검사");
			//= 중복 실행 Check
			if(!"1".equals(searchTearmGb)) {
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			}

			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			if(paCode.equals(Constants.PA_INTP_BROAD_CODE)){
				paramMap.put("paName", Constants.PA_INTP_BROAD);
			} else {
				paramMap.put("paName", Constants.PA_INTP_ONLINE);
			}
			apiInfo.put("paName", paramMap.getString("paName"));
			apiInfo.put("paCode", paCode);
			
			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb("20");//회수
			paEntpSlip.setPaCode(paCode);	
			paEntpSlip.setPaAddrSeq("");
			
			internalXmlUrl = apiInfo.get("INTERNAL_XML_URL");
			
			Map<String,String> entpUserMap = new HashMap<String,String>();
		    entpUserMap = paIntpCommonService.selectEntpShipInsertList(paEntpSlip);
		    
		    if(entpUserMap == null) {
				paramMap.put("code","404");
				paramMap.put("message",getMessage("partner.no_change_data"));
		    } else {
			    postAddr 	 = entpUserMap.get("ADDRESS1").toString(); //주소
				dtlsAddr 	 = entpUserMap.get("ADDRESS2").toString(); //상세주소
				gnrlTlphnNo  = entpUserMap.get("ENTP_MAN_TEL").toString(); //일반전화번호
				prtblTlphnNo = entpUserMap.get("ENTP_MAN_HP").toString(); //휴대전화번호
				addrNm 		 = paCode + "-" + entpUserMap.get("ENTP_CODE").toString() + "-" + entpUserMap.get("ENTP_MAN_SEQ").toString(); //반품배송지명
				zipCd  		 = entpUserMap.get("POST_NO").toString(); //우편번호
				
				log.info("03.회수지 등록 API 파라미터 생성");

				Map<String, String> apiParamMap = new HashMap<String, String>();
		    
				if(!"".equals(ComUtil.isNull(internalXmlUrl, ""))) {
					dataUrl = internalXmlUrl + "?entpCode=" + entpCode + "&entpManSeq=" + entpManSeq + "&paCode=" + paCode;						
					apiParamMap.put("dataUrl", dataUrl);
				}
				String xmlStr = returnSlipMakeXmlFile(postAddr, dtlsAddr, gnrlTlphnNo, prtblTlphnNo, addrNm, zipCd, "");
				
				if("".equals(ComUtil.NVL(xmlStr, ""))) {
					paramMap.put("code", "405");
					paramMap.put("message", "XML 데이터 생성 오류");
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				} else {
					log.info("04.회수지 등록 API 호출");
					
					apiInfo.put("body", xmlStr);
					apiInfo.put("paCode", paCode);
					
					ParamMap resParam = paIntpComUtil.paIntpConnectionSetting(apiInfo, request_type, apiParamMap);
					
					if("200".equals(resParam.getString("code"))) {
						
						Map<String, String> map = new HashMap<String, String>();
						doc = (Document) resParam.get("data");
						String errorCheck = "";
						NodeList childeList = doc.getFirstChild().getChildNodes();
						for(int j=0; j<childeList.getLength(); j++) {
							if("error".equals(childeList.item(j).getNodeName())) {
								errorCheck = "error";
								map.put("code", childeList.item(j).getFirstChild().getNextSibling().getTextContent());
								map.put("explanation", childeList.item(j).getLastChild().getPreviousSibling().getTextContent());
							}
							for(Node node = childeList.item(j).getFirstChild(); node!=null; node=node.getNextSibling()) {
								for(int i=0; i<node.getChildNodes().getLength(); i++) {
			            			Node directionList = node.getChildNodes().item(i);
			            			map.put(node.getNodeName().trim(), directionList.getTextContent().trim());
			        			}
							}
						}
						
						if("error".equals(errorCheck)) {
							String code = map.get("code").replace("|", "");
							code = code.length() == 6 ? code.substring(2) : (code.length() == 5 ? code.substring(1) : code);
							paramMap.put("code", code);
							paramMap.put("message", map.get("explanation"));
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						} else {
							paEntpSlip.setPaAddrSeq(map.get("entrDelvInfoNo"));
							paEntpSlip.setTransTargetYn("0");
							paEntpSlip.setInsertId(Constants.PA_INTP_PROC_ID);
							paEntpSlip.setModifyId(Constants.PA_INTP_PROC_ID);
							paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							log.info("05.반품/교환처 저장");
							rtnMsg = paIntpCommonService.savePaIntpEntpSlipTx(paEntpSlip);
							
							if(!rtnMsg.equals(Constants.SAVE_SUCCESS)) {
								paramMap.put("code", "404");
								paramMap.put("message",getMessage("errors.no.select"));
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							} else {
								paramMap.put("code", "200");
								paramMap.put("message", "OK");
							}
						}
					} else {
						paramMap.put("code", "500");
						paramMap.put("message", resParam.getString("message"));
					}
			    }
		    }
		} catch (Exception e) {
			if(duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",e.getMessage().length() > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage());
		    log.error(paramMap.getString("message"), e);
		    return new ResponseEntity<ResponseMsg>(new ResponseMsg(HttpStatus.OK.value(), paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		} finally {
			try {
				paramMap.put("message", "entp_code" + ":" + entpCode + ">" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if(!"1".equals(searchTearmGb)) {
				if(duplicateCheck.equals("0")) {
					systemService.checkCloseHistoryTx("end", prg_id);
				}				
			}

			log.info("05.저장 완료 API END");
			log.info("===== 반품배송지등록 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);		
	}	

	/**
	 * 반품배송지 등록 XML RETURN
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/return_slip_to_xml", produces="text/xml;charset=EUC-KR" , method = RequestMethod.GET)
	@ResponseBody
	
	public String returnSlipToXml(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "entpCode"	, required = true, defaultValue = "") String entpCode,
			@RequestParam(value = "entpManSeq"	, required = true, defaultValue = "") String entpManSeq,
            @RequestParam(value = "paCode"	, 	  required = true, defaultValue = "") String paCode,
            @RequestParam(value = "paAddrSeq"	, required = false, defaultValue = "") String paAddrSeq) throws Exception {

		PaEntpSlip paEntpSlip = new PaEntpSlip();
		ParamMap paramMap = new ParamMap();		
		
		String postAddr 	= ""; //주소
		String dtlsAddr 	= ""; //상세주소
		String gnrlTlphnNo  = ""; //일반전화번호
		String prtblTlphnNo = ""; //휴대전화번호			
		String addrNm 		= ""; //주소명
		String zipCd 		= ""; //반품배송지우편번호		
		String rtnVal	 	= "";
		
		try {
			
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaAddrGb("20");//회수
			paEntpSlip.setPaCode(paCode);
			paEntpSlip.setPaAddrSeq(paAddrSeq);
			
			Map<String,String> entpUserMap = new HashMap<String,String>();
			
		    entpUserMap = paIntpCommonService.selectEntpShipInsertList(paEntpSlip);
			
		    if(entpUserMap == null){
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("partner.no_change_data"));
				throw processException("pa.fail_address_refine", new String[] { paramMap.getString("code"), paramMap.getString("message") });
		    } else {
		    	postAddr 	 = entpUserMap.get("ADDRESS1").toString(); //주소
				dtlsAddr 	 = entpUserMap.get("ADDRESS2").toString(); //상세주소
				gnrlTlphnNo  = entpUserMap.get("ENTP_MAN_TEL").toString(); //일반전화번호
				prtblTlphnNo = entpUserMap.get("ENTP_MAN_HP").toString(); //휴대전화번호
				addrNm 		 = paCode + "-" + entpUserMap.get("ENTP_CODE").toString() + "-" + entpUserMap.get("ENTP_MAN_SEQ").toString(); //반품배송지명
				zipCd  		 = entpUserMap.get("POST_NO").toString(); //우편번호
				
		    }
		    
			rtnVal = returnSlipMakeXmlFile(postAddr, dtlsAddr, gnrlTlphnNo, prtblTlphnNo, addrNm, zipCd, paAddrSeq);
		} catch (Exception e) {
		    log.error(e.getMessage(), e);
		}
		
		return rtnVal;
	}
	
	public String returnSlipMakeXmlFile(String postAddr,String dtlsAddr,String gnrlTlphnNo,String prtblTlphnNo,String addrNm, String zipCd,String PaAddrSeq) throws Exception{
		ParamMap paramMap = new ParamMap();		 
		
		Map<String, String> xmlMap = new HashMap<String, String>();
		String rtnVal = null;
		
		try{	
			xmlMap.put("delvNm",    addrNm);       //반품배송지관리명
			xmlMap.put("delvZipcd", zipCd);		   //반품배송지 우편번호
			xmlMap.put("delvAddr1", postAddr);	   //반품배송지 주소1
			xmlMap.put("delvAddr2", dtlsAddr);	   //반품배송지 주소2
			xmlMap.put("delvTelno", gnrlTlphnNo);  //반품배송지 전화번호
			xmlMap.put("delvHp",    prtblTlphnNo); //반품배송지 핸드폰번호				
			
			if(!PaAddrSeq.equals("")) {
				xmlMap.put("entrDelvInfoNo", PaAddrSeq);
				xmlMap.put("useYn", "Y");
			}
			
			rtnVal = paIntpComUtil.mapToXml(xmlMap);
			
		}catch (Exception e) {
		    log.error(paramMap.getString("message"), e);
		    rtnVal = null;
		}
		return rtnVal;
	}
	
	/**
	 * 반품배송지  수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품배송지 수정", notes = "반품배송지 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 410, message = "주소를 확인하세요."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/returnslip-update", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnShipUpdate(HttpServletRequest request) throws Exception {
		ParamMap paramMap = new ParamMap();
		HashMap<String, String>	apiInfo = new HashMap<String, String>();
		
		PaEntpSlip paEntpSlip = new PaEntpSlip();
		HashMap<String, String> entpUserMap = null;
		
		String duplicateCheck = "";
		String requestType = "POST";
		String prg_id = "IF_PAINTPAPI_00_005";
		Document doc = null;
		
		String rtnMsg = "";
		String dateTime = systemService.getSysdatetimeToString();
		String dataUrl = "";
		String internalXmlUrl = "";
				
		String entpCode		= "";
		String entpManSeq	= "";
		String paCode		= "";		
		String paAddrSeq	= "";
		String postAddr		= ""; //주소
		String dtlsAddr		= ""; //상세주소
		String gnrlTlphnNo	= ""; //일반전화번호
		String prtblTlphnNo	= ""; //휴대전화번호
		String addrNm		= ""; //주소명
		String zipCd		= ""; //반품배송지우편번호
		
		List<Object> entpShipUpdateList = null;
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
				
		try{
			log.info("===== 반품배송지 수정 API Start =====");
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			
			log.info("02.반품배송지 수정 API 정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			log.info("03.반품배송지수정 대상 리스트 조회");
			entpShipUpdateList = paIntpCommonService.selectEntpShipUpdateList("20");
			
			if(entpShipUpdateList.size() == 0){
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("partner.no_change_data"));
			}else{
				for(int i=0; i<entpShipUpdateList.size(); i++){
					entpUserMap = (HashMap<String, String>) entpShipUpdateList.get(i);
					
					paCode		 = entpUserMap.get("PA_CODE").toString();
					entpCode	 = entpUserMap.get("ENTP_CODE").toString();
					entpManSeq	 = entpUserMap.get("ENTP_MAN_SEQ").toString();
					paAddrSeq	 = entpUserMap.get("PA_ADDR_SEQ").toString(); 
					postAddr 	 = entpUserMap.get("ADDRESS1").toString(); //주소
					dtlsAddr 	 = entpUserMap.get("ADDRESS2").toString(); //상세주소
					gnrlTlphnNo  = entpUserMap.get("ENTP_MAN_TEL").toString(); //일반전화번호
					prtblTlphnNo = entpUserMap.get("ENTP_MAN_HP").toString(); //휴대전화번호
					addrNm 		 = paCode + "-" + entpUserMap.get("ENTP_CODE").toString() + "-" + entpUserMap.get("ENTP_MAN_SEQ").toString(); //반품배송지명
					zipCd  		 = entpUserMap.get("POST_NO").toString(); //우편번호
					
					paramMap.put("paCode", paCode);
					if(paCode.equals(Constants.PA_INTP_BROAD_CODE)){
						paramMap.put("paName", Constants.PA_INTP_BROAD);
					} else {
						paramMap.put("paName", Constants.PA_INTP_ONLINE);
					}
					apiInfo.put("paName", paramMap.getString("paName"));
					apiInfo.put("paCode", paCode);
					
					Map<String, String> apiParamMap = new HashMap<String, String>();
					
					internalXmlUrl = apiInfo.get("INTERNAL_XML_URL");
				    					
					if(!"".equals(ComUtil.isNull(internalXmlUrl, ""))) {
						dataUrl = internalXmlUrl + "?entpCode=" + entpCode + "&entpManSeq=" + entpManSeq + "&paCode=" + paCode + "&paAddrSeq=" + paAddrSeq;						
						apiParamMap.put("dataUrl", dataUrl);
					}
					
					log.info("04.반품 배송지 수정 API 파라미터 생성");
					String xmlStr = returnSlipMakeXmlFile(postAddr, dtlsAddr, gnrlTlphnNo, prtblTlphnNo, addrNm, zipCd, paAddrSeq);
					
					if("".equals(ComUtil.NVL(xmlStr, ""))) {
						paramMap.put("code", "405");
						paramMap.put("message", "XML 데이터 생성 오류");
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					} else {
						log.info("04.반품배송지 API 호출");
						
						apiInfo.put("body", xmlStr);
						apiInfo.put("paCode", paCode);	
						
						ParamMap resParam = paIntpComUtil.paIntpConnectionSetting(apiInfo, requestType, apiParamMap);
						
						if("200".equals(resParam.getString("code"))) {
							
							Map<String, String> map = new HashMap<String, String>();
							doc = (Document) resParam.get("data");
							String errorCheck = "";
							NodeList childeList = doc.getFirstChild().getChildNodes();
							for(int j=0; j<childeList.getLength(); j++) {
								if("error".equals(childeList.item(j).getNodeName())) {
									errorCheck = "error";
									map.put("code", childeList.item(j).getFirstChild().getNextSibling().getTextContent());
									map.put("explanation", childeList.item(j).getLastChild().getPreviousSibling().getTextContent());
								}
								for(Node node = childeList.item(j).getFirstChild(); node!=null; node=node.getNextSibling()) {
									for(int k=0; k<node.getChildNodes().getLength(); k++) {
										Node directionList = node.getChildNodes().item(k);
										map.put(node.getNodeName().trim(), directionList.getTextContent().trim());
									}
								}
							}
							
							if("error".equals(errorCheck)) {
								String code = map.get("code").replace("|", "");
								code = code.length() == 6 ? code.substring(2) : (code.length() == 5 ? code.substring(1) : code);
								paramMap.put("code", code);
								paramMap.put("message", map.get("explanation"));
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							} else {
								paEntpSlip.setPaCode(paCode);
								paEntpSlip.setEntpCode(entpCode);
								paEntpSlip.setEntpManSeq(entpManSeq);
								paEntpSlip.setPaAddrGb("20");
								paEntpSlip.setPaAddrSeq(paAddrSeq);
								paEntpSlip.setTransTargetYn("0");
								paEntpSlip.setModifyId(paramMap.getString("siteGb"));
								paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								
								log.info("05.반품/교환처 저장");
								rtnMsg = paIntpCommonService.savePaIntpEntpSlipUpdateTx(paEntpSlip);
								
								if(!rtnMsg.equals(Constants.SAVE_SUCCESS)) {
									paramMap.put("code", "404");
									paramMap.put("message",getMessage("errors.no.select"));
									return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
								} else {
									paramMap.put("code", "200");
									paramMap.put("message", "OK");
								}
							}
						} else {
							paramMap.put("code", "500");
							paramMap.put("message", resParam.getString("message"));
						}
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
				paramMap.put("message", "entp_code" + ":" + entpCode + ">" + paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 반품배송지 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 배송비 정책 등록
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "배송비 정책 등록", notes = "배송비 정책 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 410, message = "제휴사코드를 확인하세요."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   
			   })
	@RequestMapping(value = "/entpslip-cost-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpSlipCostInsert(HttpServletRequest request ,String searchTearmGb) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Document doc = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		String dataUrl = "";
		String internalXmlUrl = "";
		
		log.info("===== 배송비 정책 등록 API Start=====");
		log.info("01.배송비 정책 등록 API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PAINTPAPI_00_007";
		String request_type = "GET";
		String paCode = "";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", dateTime);
		paramMap.put("modCase", "INSERT");

		try{
			log.info("02.배송비 정책 등록 API 중복실행검사");
			//= 중복 실행 Check
			
			if(!"1".equals(searchTearmGb)) {
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
				if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			}
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			Map<String, String> apiParamMap = new HashMap<String, String>();
			
			internalXmlUrl = apiInfo.get("INTERNAL_XML_URL");
			
			for(int count=0 ; count<Constants.PA_INTP_CONTRACT_CNT; count++){
				if(count==0){
					paCode = Constants.PA_INTP_ONLINE_CODE;
				}else{
					paCode = Constants.PA_INTP_BROAD_CODE;
				}
				
				paramMap.put("paCode", paCode);
				if(paCode.equals(Constants.PA_INTP_BROAD_CODE)){
					paramMap.put("paName", Constants.PA_INTP_BROAD);
				} else {
					paramMap.put("paName", Constants.PA_INTP_ONLINE);
				}
				apiInfo.put("paName", paramMap.getString("paName"));
				apiInfo.put("paCode", paCode);
				
				List<HashMap<?,?>> entpSlipCostMap = paIntpCommonService.selectEntpSlipCost(paramMap);
				
				if(entpSlipCostMap.size() < 1){
					paramMap.put("code","404");
					paramMap.put("message",getMessage("partner.no_change_data"));
			    } else {
			    	for(HashMap<?,?> entpSlipCost : entpSlipCostMap){
			    		if(!"".equals(ComUtil.isNull(internalXmlUrl, ""))) {
			    			dataUrl = internalXmlUrl + "?entpCode="+ entpSlipCost.get("ENTP_CODE").toString()+"&shipCostCode="+entpSlipCost.get("SHIP_COST_CODE").toString() + "&paCode=" + paCode;				    
			    			apiParamMap.put("dataUrl", dataUrl);
			    		}
			    		
			    		String xmlStr = delvCostPlcMakeXmlFile(entpSlipCost);
			    		
			    		if("".equals(ComUtil.NVL(xmlStr, ""))) {
			    			paramMap.put("code","404");
			    			paramMap.put("message","XML 데이터 생성 오류");
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
			    					for(Node node = childeList.item(j).getFirstChild(); node!=null; node=node.getNextSibling()) {
			    						for(int i=0; i<node.getChildNodes().getLength(); i++) {
			    							Node directionList = node.getChildNodes().item(i);
			    							map.put(node.getNodeName().trim(), directionList.getTextContent().trim());
			    						}
			    					}
			    				}
			    				
			    				if("error".equals(errorCheck)) {
			    					String code = map.get("code").replace("|", "");
			    					code = code.length() == 6 ? code.substring(2) : (code.length() == 5?code.substring(1):code);
			    					paramMap.put("code", code);
			    					paramMap.put("message",map.get("explanation"));
			    				} else {
			    					log.info("04.배송비 정책 등록 API 오픈마켓 배송비(공통) 저장");
			    					//오픈마켓 배송비(공통) 저장
			    					ParamMap custShipCostMap = new ParamMap();
			    					custShipCostMap.put("paCode", paCode);
			    					custShipCostMap.put("entpCode", entpSlipCost.get("ENTP_CODE").toString());
			    					custShipCostMap.put("shipCostCode", entpSlipCost.get("SHIP_COST_CODE").toString());
			    					custShipCostMap.put("delvCostPlcNo", map.get("delvCostPlcNo"));
			    					custShipCostMap.put("modifyId", "BATCH");
			    					custShipCostMap.put("modifyDate", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
			    					
			    					rtnMsg = paIntpCommonService.savePaIntpCustShipCostTx(custShipCostMap);
			    					
			    					if(!rtnMsg.equals("000000")){
			    						paramMap.put("code","404");
			    						paramMap.put("message",getMessage("errors.no.select"));
			    					} else {
			    						paramMap.put("code","200");
			    						paramMap.put("message","OK");
			    					}
			    				}
			    			}else{
			    				paramMap.put("code","500");
			    				paramMap.put("message",resParam.getString("message"));
			    			}
			    		}
			    	}
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
				paramMap.put("message", paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);			
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(!"1".equals(searchTearmGb)) {
				if(duplicateCheck.equals("0")){
					systemService.checkCloseHistoryTx("end", prg_id);
				}
			}

			log.info("05.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 배송비 정책 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "배송비 정책 수정", notes = "배송비 정책 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "OK"), 
			   @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			   @ApiResponse(code = 410, message = "제휴사코드를 확인하세요."),
			   @ApiResponse(code = 490, message = "중복처리 오류 발생."),
			   @ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다") 
			   
			   })
	@RequestMapping(value = "/entpslip-cost-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpSlipCostModify(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		Document doc = null;
		
		String dateTime = "";
		String duplicateCheck = "";
		String dataUrl = "";
		String internalXmlUrl = "";
		
		log.info("===== 배송비 정책 수정 API Start=====");
		log.info("01.배송비 정책 등록 API 기본정보 세팅");
		
		//= connectionSetting 설정
		String prg_id = "IF_PAINTPAPI_00_008";
		String request_type = "GET";
		String paCode = "";
		dateTime = systemService.getSysdatetimeToString();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
    	paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		paramMap.put("modCase", "MODIFY");

		try{
			log.info("02.배송비 정책 수정 API 중복실행검사");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			Map<String, String> apiParamMap = new HashMap<String, String>();
			
			internalXmlUrl = apiInfo.get("INTERNAL_XML_URL");
			
			for(int count=0 ; count<Constants.PA_INTP_CONTRACT_CNT; count++){
				if(count==0){
					paCode = Constants.PA_INTP_ONLINE_CODE;
				}else{
					paCode = Constants.PA_INTP_BROAD_CODE;
				}
				
				paramMap.put("paCode", paCode);
				if(paCode.equals(Constants.PA_INTP_BROAD_CODE)){
					paramMap.put("paName", Constants.PA_INTP_BROAD);
				} else {
					paramMap.put("paName", Constants.PA_INTP_ONLINE);
				}
				apiInfo.put("paName", paramMap.getString("paName"));
				apiInfo.put("paCode", paramMap.getString("paCode"));
				
				List<HashMap<?,?>> entpSlipCostMap = paIntpCommonService.selectEntpSlipCost(paramMap);
				
				if(entpSlipCostMap.size() < 1){
					paramMap.put("code","404");
					paramMap.put("message",getMessage("partner.no_change_data"));
			    } else {
			    	for(HashMap<?,?> entpSlipCost : entpSlipCostMap){
			    		if(!"".equals(ComUtil.isNull(internalXmlUrl, ""))) {
			    			dataUrl = internalXmlUrl + "?entpCode="+ entpSlipCost.get("ENTP_CODE").toString()+"&shipCostCode="+entpSlipCost.get("SHIP_COST_CODE").toString() + "&paCode=" + paCode;				    
			    			apiParamMap.put("dataUrl", dataUrl);
			    		}
			    		
			    		String xmlStr = delvCostPlcMakeXmlFile(entpSlipCost);
			    		
			    		if("".equals(ComUtil.NVL(xmlStr, ""))) {
			    			paramMap.put("code","404");
			    			paramMap.put("message","XML 데이터 생성 오류");
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
			    					for(Node node = childeList.item(j).getFirstChild(); node!=null; node=node.getNextSibling()) {
			    						for(int i=0; i<node.getChildNodes().getLength(); i++) {
			    							Node directionList = node.getChildNodes().item(i);
			    							map.put(node.getNodeName().trim(), directionList.getTextContent().trim());
			    						}
			    					}
			    				}
			    				
			    				if("error".equals(errorCheck)) {
			    					String code = map.get("code").replace("|", "");
			    					code = code.length() == 6 ? code.substring(2) : (code.length() == 5?code.substring(1):code);
			    					paramMap.put("code", code);
			    					paramMap.put("message",map.get("explanation"));
			    				} else {
			    					log.info("04.배송비 정책 수정 API 오픈마켓 배송비(공통) 저장");
			    					//오픈마켓 배송비(공통) 저장
			    					ParamMap custShipCostMap = new ParamMap();
			    					
			    					custShipCostMap.put("paCode", paCode);
			    					custShipCostMap.put("entpCode", entpSlipCost.get("ENTP_CODE").toString());
			    					custShipCostMap.put("shipCostCode", entpSlipCost.get("SHIP_COST_CODE").toString());
			    					custShipCostMap.put("modifyId", "BATCH");
			    					custShipCostMap.put("modifyDate", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
			    					rtnMsg = paIntpCommonService.savePaIntpCustShipCostTx(custShipCostMap);
			    					
			    					if(!rtnMsg.equals("000000")){
			    						paramMap.put("code","404");
			    						paramMap.put("message",getMessage("errors.no.select"));
			    					} else {
			    						paramMap.put("code","200");
			    						paramMap.put("message","OK");
			    					}
			    				}
			    			}else{
			    				paramMap.put("code","500");
			    				paramMap.put("message",resParam.getString("message"));
			    			}
			    		}
			    	}
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
				paramMap.put("message", paramMap.getString("message"));
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);			
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("05.저장 완료 API END");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 배송비 정책 등록 XML RETURN
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/entpslip_cost_to_xml", produces="text/xml;charset=EUC-KR" , method = RequestMethod.GET)
	@ResponseBody
	public String entpSlipCostToXml(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="entpCode", required=true) String entpCode, 
			@RequestParam(value="shipCostCode", required=true) String shipCostCode,
            @RequestParam(value ="paCode"	, required = true, defaultValue = "") String paCode ) throws Exception{

		ParamMap paramMap	= new ParamMap();
		String rtnVal = "";
		
		try {
			paramMap.put("shipCostCode", shipCostCode);
			paramMap.put("entpCode", entpCode);
			paramMap.put("paCode", paCode);
			
			List<HashMap<?,?>> entpSlipCostMap = paIntpCommonService.selectEntpSlipCost(paramMap);
			
			for(HashMap<?,?> entpSlipCost : entpSlipCostMap){
				rtnVal = delvCostPlcMakeXmlFile(entpSlipCost);
			}
			
		} catch (Exception e) {
		    log.error(e.getMessage(), e);
		}
		
		return rtnVal;
	}

	public String delvCostPlcMakeXmlFile(Map<?, ?> dataMap) throws Exception {
		String rtnVal = null;
		
		Map<String, String> xmlMap = new HashMap<String, String>();
		
		try {
			String delvPlcNo = dataMap.get("GROUP_CODE").toString();
			
			if(!"".equals(ComUtil.isNull(delvPlcNo, ""))) {
				xmlMap.put("delvPlcNo", delvPlcNo);
			}
			
			//배송비 종류(00:무료, 98:판매자 조건부 무료, 99:판매자 정액)
			String shipCostCode = dataMap.get("SHIP_COST_CODE").toString();
            if("FR".equals(shipCostCode.substring(0, 2))) {
				xmlMap.put("distCostTp", "00");
			} else if ("CN".equals(shipCostCode.substring(0, 2)) || "PL".equals(shipCostCode.substring(0, 2))) {
				xmlMap.put("distCostTp", "98");
				xmlMap.put("maxbuyAmt", dataMap.get("SHIP_COST_BASE_AMT").toString());
			} else if ("ID".equals(shipCostCode.substring(0, 2))) {
				xmlMap.put("distCostTp", "99");
			}
			
			//결제방법 (01:착불, 02:선불(배송비 종류가 무료일 경우에도 선택), 03:선/착불)
            xmlMap.put("distCostCd", "02");
            
			//일부지역 유료여부 (Y:예, N:아니오(배송비 종류가 무료일 때만 입력))
			if(ComUtil.objToLong(dataMap.get("ISLAND_COST")) > 0 || ComUtil.objToLong(dataMap.get("JEJU_COST")) > 0) {
				xmlMap.put("localCostYn", "Y");				
			}else {
				xmlMap.put("localCostYn", "N");	
			}
			//배송비(배송비 종류가 무료일 경우 0)
			xmlMap.put("distCost", dataMap.get("ORD_COST").toString());
			
			rtnVal = paIntpComUtil.mapToXml(xmlMap);
			
		} catch (Exception e) {
			rtnVal = null;
		    log.error(e.getMessage(), e);
		}
		
		return rtnVal;
	}
	
	@RequestMapping(value = "/settlement-goods", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> settlementGoods(HttpServletRequest request,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "delYn", required=false) String delYn) throws Exception{		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, String> params = new HashMap<>();
		String prg_id = "IF_PAINTPAPI_00_009";
		String prg_id_delv = "IF_PAINTPAPI_00_010";
		String duplicateCheck = "";
		String startDate = "";
		String endDate = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		endDate   = ComUtil.NVL(toDate).length()   == 8 ? toDate   : DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT);
		startDate = ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT);
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("code", "200");
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("strDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("delYn", delYn);
		
		try {
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if ("1".equals(duplicateCheck))	throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			List<PaIntpSettlement> paIntpSettlementList = new ArrayList<PaIntpSettlement>();
			List<PaIntpDelvSettlement> paIntpSettleDelvSettlements = new ArrayList<PaIntpDelvSettlement>();
			
			log.info(prg_id + " - 02.파라미터 검증, [fromDate={}, toDate={}]", fromDate, toDate);
			
			for(int count = 0; count < Constants.PA_INTP_CONTRACT_CNT; count++) {
				if(count == 0) {
					params.put("supplyCtrtSeq", apiInfo.get("PA_BROAD").toString());
					apiInfo.put("paName", Constants.PA_INTP_BROAD);
				} else {
					params.put("supplyCtrtSeq", apiInfo.get("PA_ONLINE").toString());
					apiInfo.put("paName", Constants.PA_INTP_ONLINE);
				}

				params.put("entrId", ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
				params.put("entrNo", apiInfo.get("INTP_ENTP_CODE").toString());
				params.put("strDt", startDate);
				params.put("endDt", endDate);
				params.put("page","1");
			
				HttpResponse response = ComUtil.paIntpConnectionSetting(apiInfo,params);
				
				JSONArray resParam = ComUtil.parseIntpCommonResponseToJson(response);
				
				if(resParam != null) {
					for(int i = 0; i< resParam.length(); i++) {
						log.info(prg_id + " - 03. 상품별 정산 내역 조회");
						PaIntpSettlement paIntpSettlement = new PaIntpSettlement();
						JSONObject resObject = resParam.getJSONObject(i);

						paIntpSettlement.setRevLogNm("주문".equals(resObject.getString("rev_log_nm"))?"01":"02");
						paIntpSettlement.setCnclYn("정상".equals(resObject.getString("cncl_yn"))?"00":"01");
						
						if("일반".equals(resObject.getString("setl_mod_tp"))) {
							paIntpSettlement.setSetlModTp("01"); //일반
						}else if("정정".equals(resObject.getString("setl_mod_tp"))) {
							paIntpSettlement.setSetlModTp("02"); // 정정
						}else {
							paIntpSettlement.setSetlModTp("03"); // 이월
						}					
						paIntpSettlement.setRevDt(resObject.getString("rev_dt"));
						paIntpSettlement.setOrdclmNo(resObject.getString("ordclm_no"));
						paIntpSettlement.setOrdSeq(String.valueOf(resObject.getLong("ord_seq")));
						paIntpSettlement.setOriOrdclmNo(resObject.has("ori_ordclm_no")?resObject.getString("ori_ordclm_no"):"");
						paIntpSettlement.setClmSeq(resObject.has("clm_seq")?String.valueOf(resObject.getLong("clm_seq")):"");
						paIntpSettlement.setOrdNm(resObject.getString("ord_nm"));
						paIntpSettlement.setRcvrNm(resObject.getString("rcvr_nm"));
						paIntpSettlement.setParentPrdNo(resObject.getLong("parent_prd_no"));
						paIntpSettlement.setParentPrdNm(resObject.getString("parent_prd_nm"));
						paIntpSettlement.setPrdNo(resObject.getLong("prd_no"));
						//paIntpSettlement.setOptFnm(resObject.getString("opt_fnm"));
						paIntpSettlement.setOptFnm(resObject.has("opt_fnm")?resObject.getString("opt_fnm"):"");
						paIntpSettlement.setOldSaleUnitcost(resObject.getDouble("old_sale_unitcost"));
						paIntpSettlement.setRealSaleUnitcost(resObject.getDouble("real_sale_unitcost"));
						paIntpSettlement.setSaleFeeRt(resObject.getDouble("sale_fee_rt"));
						paIntpSettlement.setInDcCouponAmt(resObject.getDouble("in_dc_coupon_amt"));
						paIntpSettlement.setEntrDirectDcAmt(resObject.getDouble("entr_direct_dc_amt"));
						paIntpSettlement.setEntrDcCouponAmt(resObject.getDouble("entr_dc_coupon_amt"));
						paIntpSettlement.setSaleQty(resObject.getLong("sale_qty"));
						paIntpSettlement.setSaleAmt(resObject.getDouble("sale_amt"));
						paIntpSettlement.setSaleFee(resObject.getDouble("sale_fee"));
						paIntpSettlement.setDelvEndDt(resObject.has("delv_end_dt")?resObject.getString("delv_end_dt"):"");
						paIntpSettlement.setTransferDt(resObject.has("transfer_dt")?resObject.getString("transfer_dt"):"");
						paIntpSettlement.setPreUseUnitcost(resObject.getDouble("pre_use_unitcost"));
						paIntpSettlement.setIpointDcAmt(resObject.getDouble("ipoint_dc_amt"));
						paIntpSettlement.setInpkOnusAmt(resObject.getDouble("inpk_onus_amt"));
						paIntpSettlement.setDepositAmt(resObject.getDouble("deposit_amt"));

						paIntpSettlementList.add(paIntpSettlement);
					}
					
					rtnMsg = paIntpCommonService.savePaIntpSettlementTx(paIntpSettlementList, paramMap);
					
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)) {
						paramMap.put("code", "400");
						paramMap.put("message", rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
					}
				}
				
				/*
				log.info(prg_id_delv + " - 04. 배송비별 정산 내역 조회");
				paramMap.put("apiCode", prg_id_delv);
				HttpResponse response_delv = ComUtil.paIntpConnectionSetting(apiInfo,params);
				JSONArray resParam_delv = ComUtil.parseIntpCommonResponseToJson(response_delv);
				
				for(int j = 0; j < resParam_delv.length(); j++) {
					
					PaIntpDelvSettlement paIntpDelvSettlement = new PaIntpDelvSettlement();
					
					JSONObject resObject = resParam.getJSONObject(j);
					
					paIntpDelvSettlement.setRevLogNm("주문".equals(resObject.getString("rev_log_nm"))?"01":"02");
					paIntpDelvSettlement.setCnclYn("정상".equals(resObject.getString("cncl_yn"))?"00":"01");
					
					if("일반".equals(resObject.getString("setl_mod_tp"))) {
						paIntpDelvSettlement.setSetlModTp("01"); //일반
					}else if("정정".equals(resObject.getString("setl_mod_tp"))) {
						paIntpDelvSettlement.setSetlModTp("02"); // 정정
					}else {
						paIntpDelvSettlement.setSetlModTp("03"); // 이월
					}
					paIntpDelvSettlement.setRevDt(resObject.getString("rev_dt"));
					paIntpDelvSettlement.setOrdclmNo(resObject.getString("ordclm_no"));
					paIntpDelvSettlement.setOrdSeq(String.valueOf(resObject.getLong("ord_seq")));
					paIntpDelvSettlement.setOriOrdclmNo(resObject.has("ori_ordclm_no")?resObject.getString("ori_ordclm_no"):"");
					paIntpDelvSettlement.setClmSeq(resObject.has("clm_seq")?resObject.getString("clm_seq"):"");
					paIntpDelvSettlement.setOrdNm(resObject.getString("ord_nm"));
					paIntpDelvSettlement.setRcvrNm(resObject.getString("rcvr_nm"));
					paIntpDelvSettlement.setDelvAmt(resObject.getDouble("delv_amt"));
					paIntpDelvSettlement.setDelvRtnAmt(resObject.getDouble("delv_rtn_amt"));
					paIntpDelvSettlement.setFee24(resObject.getString("fee_24"));
					paIntpDelvSettlement.setFee22(resObject.getString("fee_22"));
					paIntpDelvSettlement.setTransferDt(resObject.has("transfer_dt")?resObject.getString("transfer_dt"):"");
					
					paIntpSettleDelvSettlements.add(paIntpDelvSettlement);
				}
				
				rtnMsg = paIntpCommonService.savePaIntpDelvSettlementTx(paIntpSettleDelvSettlements, paramMap);
				
				if(!rtnMsg.equals(Constants.SAVE_SUCCESS)) {
					paramMap.put("code", "400");
					paramMap.put("message", rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}
				*/
			}
		}
		catch ( Exception e ) {
			paramMap.put("code"   , ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(duplicateCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}
		finally {
			log.info(prg_id + " - 05.프로그램 중복 실행 검사 [end]");
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
			
			log.info(prg_id + " - 07.API trace information save 처리");
			systemService.insertApiTrackingTx(request, paramMap);
		}
		log.info(prg_id + "상품별 정산 리스트 호출 완료");
		
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
}