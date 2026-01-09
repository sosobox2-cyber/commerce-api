package com.cware.api.pacopn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.cware.netshopping.domain.model.PaCopnCertification;
import com.cware.netshopping.domain.model.PaCopnDocment;
import com.cware.netshopping.domain.model.PaCopnLmsdKeyOption;
import com.cware.netshopping.domain.model.PaCopnOption;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.pacopn.common.repository.PaCopnCommonDAO;
import com.cware.netshopping.pacopn.common.service.PaCopnCommonService;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Api(value = "/pacopn/common", description="상품 기초 정보 / 출고지 등록 및 수정")
@Controller("com.cware.api.pacopn.paCopnCommonController")
@RequestMapping(value = "/pacopn/common")
public class PaCopnCommonController extends AbstractController{

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pacopn.common.paCopnCommonService")
	private PaCopnCommonService paCopnCommonService;
	
	@Resource(name = "pacopn.common.paCopnCommonDAO")
	private PaCopnCommonDAO paCopnCommonDAO;
	
	/**
	 * 카테고리별 메타정보 조회
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "카테고리별 메타정보 조회", notes = "카테고리별 메타정보 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/goods-metainfo-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsKindsMetaInfoList(HttpServletRequest request, 
			@ApiParam(name = "categoryCode", value = "카테고리코드", defaultValue = "") String categoryCode) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		List<PaCopnLmsdKeyOption> categoryList = null;
		
		String rtnMsg = Constants.SAVE_SUCCESS;
        JsonObject resultJson = new JsonObject();
        String dateTime = "";
        String duplicateCheck = "";
        PaCopnOption opt = new PaCopnOption();
        PaCopnDocment doc = new PaCopnDocment();
        PaCopnCertification certi = new PaCopnCertification();
        String apiKeys[]=null;
        	
        try{
        	log.info("===== 카테고리 메타 정보 조회  API Start=====");
        	
        	log.info("01.API 기본정보 세팅");
    		dateTime = systemService.getSysdatetimeToString();

            paramMap.put("apiCode", "IF_PACOPNAPI_00_002");
            paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
            paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
            paramMap.put("startDate", dateTime);
            paramMap.put("paName", Constants.PA_ONLINE);

            log.info("02.API 중복실행검사");
		    duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
		    if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});

		    log.info("03.API정보 조회");

		    apiInfo = systemService.selectPaApiInfo(paramMap);
		    apiInfo.put("apiInfo", paramMap.getString("apiCode"));
		    apiKeys = apiInfo.get(paramMap.getString("paName")).split(";");
		    categoryList = paCopnCommonDAO.selectcategoryList();
		    String optionKey = "";
		    for(int i=0; i<categoryList.size(); i++){
		    	PaCopnLmsdKeyOption key = categoryList.get(i);
		    	log.info("04.카테고리 메타 정보 조회" + key.getPaLmsdKey());
	            optionKey = key.getPaLmsdKey();

				String urlPath = apiInfo.get("API_URL").toString();
				apiInfo.put("apiUrl", urlPath );
				log.info("key " + optionKey);

				Thread.sleep(200);

				resultJson = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")+optionKey).replaceAll("#vendorId#", apiKeys[0])));
				JsonArray attrArry =  resultJson.getAsJsonObject("data").getAsJsonArray("attributes");
				JsonArray notiArry = resultJson.getAsJsonObject("data").getAsJsonArray("noticeCategories");
				JsonArray docsArry = resultJson.getAsJsonObject("data").getAsJsonArray("requiredDocumentNames");
				JsonArray certiArry = resultJson.getAsJsonObject("data").getAsJsonArray("certifications");
				StringBuilder sb = new StringBuilder();
				JsonObject attr = null;
				JsonObject docs = null;
				JsonObject certis = null;
				int insertSeq = 0;
				
				List<PaCopnOption> attrList = new ArrayList<PaCopnOption>();  
				List<PaCopnDocment> docsList = new ArrayList<PaCopnDocment>();  
				List<PaCopnCertification> certiList = new ArrayList<PaCopnCertification>();  
				
				log.info("categoryCode : " + optionKey);
				
				for(int k=0; k<attrArry.size(); k++){//카테고리 옵션 목록 조회
					attr = attrArry.get(k).getAsJsonObject();
					
					JsonArray usableUnitsArray = attr.getAsJsonArray("usableUnits");
					
					if (usableUnitsArray == null || usableUnitsArray.size() == 0) {
						opt = new PaCopnOption();
						opt.setPaLmsdKey(optionKey);
						opt.setAttrSeq(""+k);
						opt.setDataType(attr.get("dataType").getAsString());
						opt.setAttrTypeNm(attr.get("attributeTypeName").getAsString());
						opt.setBasicUnit(attr.get("basicUnit").getAsString());
						opt.setRequiredYn(attr.get("required").getAsString().equals("MANDATORY")  ? "Y" : "N" );
						opt.setGroupNum(attr.get("groupNumber").getAsString().equals("NONE") ? "0" : attr.get("groupNumber").getAsString() );
						opt.setExposedType(attr.get("exposed").getAsString().equals("EXPOSED") ? "0" : "1" );
						opt.setUsableUnits("");
						opt.setInsertSeq("" + insertSeq);
						sb = new StringBuilder();
						
						for(int j = 0 ; j < notiArry.size() ; j++) {
							notiArry.get(j).getAsJsonObject();
							sb.append(notiArry.get(j).getAsJsonObject().get("noticeCategoryName")) ;
							if(notiArry.size()-1 ==j ) {
							} else {
								sb.append(",") ;
							}
						}
						opt.setNoticeCategoryNm(sb.toString());
						attrList.add(opt);
						insertSeq++;
					} else {
						for (int s=0; s<usableUnitsArray.size(); s++) {
							String usableUnit = "";
							usableUnit = usableUnitsArray.get(s).getAsString();
							
							opt = new PaCopnOption();
							opt.setPaLmsdKey(optionKey);
							opt.setAttrSeq(""+k);
							opt.setDataType(attr.get("dataType").getAsString());
							opt.setAttrTypeNm(attr.get("attributeTypeName").getAsString());
							opt.setBasicUnit(attr.get("basicUnit").getAsString());
							opt.setRequiredYn(attr.get("required").getAsString().equals("MANDATORY")  ? "Y" : "N" );
							opt.setGroupNum(attr.get("groupNumber").getAsString().equals("NONE") ? "0" : attr.get("groupNumber").getAsString() );
							opt.setExposedType(attr.get("exposed").getAsString().equals("EXPOSED") ? "0" : "1" );
							opt.setUsableUnits(usableUnit);
							opt.setInsertSeq("" + insertSeq);
							sb = new StringBuilder();
							
							for(int j = 0 ; j < notiArry.size() ; j++) {
								notiArry.get(j).getAsJsonObject();
								sb.append(notiArry.get(j).getAsJsonObject().get("noticeCategoryName")) ;
								if(notiArry.size()-1 ==j ) {
								} else {
									sb.append(",") ;
								}
							}
							opt.setNoticeCategoryNm(sb.toString());
							attrList.add(opt);
							insertSeq++;
						}
					}
				}
				
				for(int k=0; k<docsArry.size(); k++){//구비서류 목록 조회
					doc = new PaCopnDocment();
					doc.setPaLmsdKey(optionKey);
					doc.setDocsSeq(""+k);
					
					docs = docsArry.get(k).getAsJsonObject();
					
					doc.setTempleteNm(docs.get("templateName").getAsString());
					doc.setRequiredYn(docs.get("required").getAsString().equals("MANDATORY") ? "Y" : "N");
					
					docsList.add(doc);
				}
				
				for(int k=0; k<certiArry.size(); k++){//상품 인증 정보 조회
					certi = new PaCopnCertification();
					certi.setPaLmsdKey(optionKey);
					certi.setSertiSeq(""+k);
					
					certis = certiArry.get(k).getAsJsonObject();
					
					certi.setCertiType(certis.get("certificationType").getAsString());
					certi.setNames(certis.get("name").getAsString());
					if(certis.get("required").getAsString().equals("MANDATORY")){
						certi.setRequiredYn("0");
					}else if(certis.get("required").getAsString().equals("RECOMMEND")){
						certi.setRequiredYn("1");
					}else{
						certi.setRequiredYn("2");
					}
					certi.setDataType(certis.get("dataType").getAsString().equals("CODE") ? "0" : "1");
					
					certiList.add(certi);
				}
				paCopnCommonService.savePaCopnCategoryMetaInfoTx(attrList,docsList,certiList);
		    }
		    
		    if(!rtnMsg.equals("000000")){
				paramMap.put("code","404");
				paramMap.put("message",getMessage("errors.no.select"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			} else {
				paramMap.put("code","200");
				paramMap.put("message","OK");
			}
        }catch (Exception e){
        	paramMap.put("code","500");
        	paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
        	return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
        }finally{
        	try{
				paramMap.put("resultMessage", resultJson.get("message"));
				paramMap.put("resultCode", resultJson.get("code"));
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}

			log.info("===== 카테고리 메타 정보 조회  API End=====");
        }
		return new ResponseEntity<>(resultJson.toString(), HttpStatus.OK);
	}
	
	/**
	 * 출고지 생성
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "출고지 생성", notes = "출고지 생성", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/entpslip-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpSlipInsert(HttpServletRequest request,
			@ApiParam(name = "entpCode", value = "업체코드", defaultValue = "") @RequestParam(value = "", required = true) String entpCode,
			@ApiParam(name = "entpManSeq", value = "업체순번", defaultValue = "") @RequestParam(value = "", required = true) String entpManSeq,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") @RequestParam(value = "", required = true) String paCode,
			@ApiParam(name = "searchTermGb", value = "API중복체크", defaultValue = "") @RequestParam(value = "", required = true) String searchTermGb
			) throws Exception {
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		PaEntpSlip paEntpSlip = new PaEntpSlip();
		Map<String, String> entpUserMap = null;
		
		String duplicateCheck = "";
		
		String respMsg = "";
		String rtnMsg = "";
		String dateTime = systemService.getSysdatetimeToString();
		
		String apiKeys[] = null;
		String requestType = "POST";
		String prg_id = "IF_PACOPNAPI_00_003";
		
		JsonObject responseObject = null;
		JsonObject jRoot = null;
		JsonArray jAddressDetail = null;
		JsonObject jAddress = null;
		
		String shippingPlaceName = "";
		String companyContactNumber = "";
		String phoneNumber2 = "";
		String returnZipCode = "";
		String returnAddress = "";
		String returnAddressDetail;
		
		paramMap.put("apiCode",prg_id);
		paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate", dateTime);
		paramMap.put("modCase", "INSERT");
		if(paCode.equals(Constants.PA_COPN_BROAD_CODE)){
			paramMap.put("paName", Constants.PA_BROAD);
			paramMap.put("paCopnLoginId", ConfigUtil.getString("PA_COPN_BROAD_LOGIN_ID"));
		} else {
			paramMap.put("paName", Constants.PA_ONLINE);
			paramMap.put("paCopnLoginId", ConfigUtil.getString("PA_COPN_ONLINE_LOGIN_ID"));
		}
		
		try{
			log.info("업체코드 : " + entpCode);
			log.info("업체담당자순번 : " + entpManSeq);
			log.info("제휴사코드 : " + paCode);
			
			paramMap.put("entpCode", entpCode);
			paramMap.put("entpManSeq", entpManSeq);
			paramMap.put("paCode",paCode);
			
			if(!"1".equals(searchTermGb)) {
				duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
				if(duplicateCheck.equals("1"))
					throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode")});
			}
			
			log.info("02.출고지 생성 API 정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";");
			
			log.info("03.출고지 조회");
			paEntpSlip.setEntpCode(paramMap.getString("entpCode"));
			paEntpSlip.setEntpManSeq(paramMap.getString("entpManSeq"));
			paEntpSlip.setPaAddrGb("30");//출고
			paEntpSlip.setPaCode(paCode);
			
			entpUserMap = paCopnCommonService.selectEntpShipInsertList(paEntpSlip);
			
			if(entpUserMap == null){
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("partner.no_change_data"));
			}else{
				returnZipCode = entpUserMap.get("POST_NO").toString();
				shippingPlaceName = entpUserMap.get("ENTP_CODE").toString() + "-" + entpUserMap.get("ENTP_NAME").toString() + "-" + entpUserMap.get("ENTP_MAN_SEQ").toString();//주소명
				companyContactNumber = entpUserMap.get("ENTP_MAN_TEL").toString();
				phoneNumber2 = entpUserMap.get("ENTP_MAN_HP").toString();
				returnAddress = entpUserMap.get("ADDRESS1").toString();
				returnAddressDetail = entpUserMap.get("ADDRESS2").toString();
				
				jRoot = new JsonObject();
				jAddressDetail = new JsonArray();
				jAddress = new JsonObject();
				
				jRoot.addProperty("vendorId", apiKeys[0]);
				jRoot.addProperty("userId", paramMap.getString("paCopnLoginId"));
				jRoot.addProperty("shippingPlaceName", shippingPlaceName);
				jRoot.addProperty("global", "false");
				
				jAddress.addProperty("addressType", "JIBUN");
				jAddress.addProperty("countryCode", "KR");
				jAddress.addProperty("companyContactNumber", companyContactNumber);
				jAddress.addProperty("phoneNumber2", phoneNumber2);
				jAddress.addProperty("returnZipCode", returnZipCode);
				jAddress.addProperty("returnAddress", returnAddress);
				jAddress.addProperty("returnAddressDetail", returnAddressDetail);
				
				jAddressDetail.add(jAddress);
				
				jAddress = new JsonObject();
				
				jAddress.addProperty("addressType","ROADNAME");
				jAddress.addProperty("countryCode","KR");
				jAddress.addProperty("companyContactNumber",companyContactNumber);
				jAddress.addProperty("phoneNumber2",phoneNumber2);
				jAddress.addProperty("returnZipCode",returnZipCode);
				jAddress.addProperty("returnAddress",returnAddress);
				jAddress.addProperty("returnAddressDetail",returnAddressDetail);
				
				jAddressDetail.add(jAddress);

				jRoot.add("placeAddresses", jAddressDetail);
				
				log.info("04.출고지 생성 API 호출");
				responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0])), requestType, new GsonBuilder().create().toJson(jRoot));
				
				respMsg = responseObject.get("message").getAsString();
				if("200".equals(responseObject.get("code").getAsString())) {
					if("SUCCESS".equals(responseObject.get("data").getAsJsonObject().get("resultCode").getAsString())) { // 출고지 생성 성공, success constants파일에 있다 
						paEntpSlip.setPaAddrSeq(responseObject.get("data").getAsJsonObject().get("resultMessage").getAsString());
						paEntpSlip.setTransTargetYn("0");
						paEntpSlip.setInsertId(Constants.PA_COPN_PROC_ID);
						paEntpSlip.setModifyId(Constants.PA_COPN_PROC_ID);
						paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						log.info("05.출고지 저장");
						rtnMsg = paCopnCommonService.savePaCopnEntpSlipTx(paEntpSlip);
						
						if(!rtnMsg.equals("000000")){
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
				} else {
					paramMap.put("code","500");
					paramMap.put("message",respMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
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
				paramMap.put("siteGb", Constants.PA_COPN_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			} catch (Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			
			if(!"1".equals(searchTermGb)) {
				if (duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info("===== 출고지 생성 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 출고지 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "출고지 수정", notes = "출고지 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/entpslip-update", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpSlipUpdate(HttpServletRequest request) throws Exception {
		ParamMap paramMap = new ParamMap();
		HashMap<String, String>	apiInfo = new HashMap<String, String>();
		
		PaEntpSlip paEntpSlip = null;
		HashMap<String, String> entpUserMap = null;
		
		String duplicateCheck = "";
		String apiKeys[] = null;
		String requestType = "PUT";
		String prg_id = "IF_PACOPNAPI_00_004";
		
		String respMsg = "";
		String rtnMsg = "";
		String dateTime = systemService.getSysdatetimeToString();
		
		JsonObject responseObject = null;
		JsonObject jRoot = null;
		JsonArray jAddressDetail = null;
		JsonObject jAddress = null;
		
		String shippingPlaceName = "";
		String companyContactNumber = "";
		String phoneNumber2 = "";
		String returnZipCode = "";
		String returnAddress = "";
		String returnAddressDetail = "";
		String outboundShippingPlaceCode = "";
		
		List<Object> entpShipUpdateList = null;
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("modCase", Constants.PA_COPN_MOD_CASE_MODIFY);
		paramMap.put("startDate", dateTime);
		
		try{
			log.info("===== 출고지 수정 API Start =====");
			
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			
			log.info("02.출고지 수정 API 정보 조회");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			log.info("03.출고지 수정 대상 리스트 조회");
			entpShipUpdateList = paCopnCommonService.selectEntpShipUpdateList("30");
			
			if(entpShipUpdateList.size() == 0){
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("partner.no_change_data"));
			}else{
				for(int i=0; i<entpShipUpdateList.size(); i++){
					entpUserMap = (HashMap<String, String>) entpShipUpdateList.get(i);
					
					if(entpUserMap.get("PA_CODE").toString().equals(Constants.PA_COPN_BROAD_CODE)){
						paramMap.put("paName", Constants.PA_BROAD);
						paramMap.put("paCopnLoginId", ConfigUtil.getString("PA_COPN_BROAD_LOGIN_ID"));
					} else {
						paramMap.put("paName", Constants.PA_ONLINE);
						paramMap.put("paCopnLoginId", ConfigUtil.getString("PA_COPN_ONLINE_LOGIN_ID"));
					}
					
					apiKeys = apiInfo.get(paramMap.getString("paName")).split(";");
					
					returnZipCode = entpUserMap.get("POST_NO").toString();
					shippingPlaceName = entpUserMap.get("ENTP_CODE").toString() + "-" + entpUserMap.get("ENTP_NAME").toString() + "-" + entpUserMap.get("ENTP_MAN_SEQ").toString();
					companyContactNumber = entpUserMap.get("ENTP_MAN_TEL").toString();
					phoneNumber2 = entpUserMap.get("ENTP_MAN_HP").toString();
					returnAddress = entpUserMap.get("ADDRESS1").toString();
					returnAddressDetail = entpUserMap.get("ADDRESS2").toString();
					outboundShippingPlaceCode = entpUserMap.get("PA_ADDR_SEQ");
					
					jRoot = new JsonObject();
					jAddressDetail = new JsonArray();
					
					jRoot.addProperty("vendorId", apiKeys[0]);
					jRoot.addProperty("userId", paramMap.getString("paCopnLoginId"));
					jRoot.addProperty("outboundShippingPlaceCode", outboundShippingPlaceCode);
					jRoot.addProperty("shippingPlaceName", shippingPlaceName);
					jRoot.addProperty("global", "false");
					
					jAddress = new JsonObject();
					jAddress.addProperty("addressType", "JIBUN");
					jAddress.addProperty("countryCode", "KR");
					jAddress.addProperty("companyContactNumber", companyContactNumber);
					jAddress.addProperty("phoneNumber2",phoneNumber2);
					jAddress.addProperty("returnZipCode",returnZipCode);
					jAddress.addProperty("returnAddress",returnAddress);
					jAddress.addProperty("returnAddressDetail",returnAddressDetail);
					
					jAddressDetail.add(jAddress);
					
					jAddress = new JsonObject();
					
					jAddress.addProperty("addressType","ROADNAME");
					jAddress.addProperty("countryCode","KR");
					jAddress.addProperty("companyContactNumber",companyContactNumber);
					jAddress.addProperty("phoneNumber2",phoneNumber2);
					jAddress.addProperty("returnZipCode",returnZipCode);
					jAddress.addProperty("returnAddress",returnAddress);
					jAddress.addProperty("returnAddressDetail",returnAddressDetail);
					
					jAddressDetail.add(jAddress);

					jRoot.add("placeAddresses", jAddressDetail);
					
					log.info("04.출고지 수정 API 호출");
					responseObject = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), new URIBuilder((apiInfo.get("API_URL")).replaceAll("#vendorId#", apiKeys[0]).replaceAll("#outboundShippingPlaceCode#", outboundShippingPlaceCode))
					, requestType, new GsonBuilder().create().toJson(jRoot));
					
					if(!"".equals(responseObject.get("data").getAsJsonObject().get("resultMessage").getAsString())) {
						respMsg = responseObject.get("data").getAsJsonObject().get("resultMessage").getAsString();
					} else {
						respMsg = responseObject.get("message").getAsString();  responseObject.get("data").getAsJsonObject().get("resultCode");
					}
					
					if("200".equals(responseObject.get("code").getAsString())){
						if("SUCCESS".equals(responseObject.get("message").getAsString())){
							paEntpSlip = new PaEntpSlip();
							
							paEntpSlip.setEntpCode(entpUserMap.get("ENTP_CODE"));
							paEntpSlip.setEntpManSeq(entpUserMap.get("ENTP_MAN_SEQ"));
							paEntpSlip.setPaCode(entpUserMap.get("PA_CODE"));
							paEntpSlip.setTransTargetYn("0");
							paEntpSlip.setModifyId(Constants.PA_COPN_PROC_ID);
							paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							log.info("05.출고지 저장");
							rtnMsg = paCopnCommonService.updatePaCopnEntpSlipTx(paEntpSlip);
							
							if(!rtnMsg.equals("000000")){
								paramMap.put("code", "404");
								paramMap.put("message", rtnMsg);
								return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
							}else{
								paramMap.put("code", "200");
								paramMap.put("message", "OK");
							}
						}else{
							paramMap.put("code", "404");
							paramMap.put("message", getMessage("partner.no_change_data"));
							return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
						}
					}else{
						paramMap.put("code", "500");
						paramMap.put("message", respMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
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
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("===== 출고지 수정 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
}