package com.cware.netshopping.patmon.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository("com.cware.netshopping.patmon.util.PaTmonConnectUtil")
public class PaTmonConnectUtil extends AbstractService{
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	private static final int    CONN_TIMEOUT = 100; 
	
	public void checkDuplication(String prg_id, ParamMap apiInfoMap) throws Exception {
		String duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
		apiInfoMap.put("duplicateCheck", duplicateCheck);
		if ("1".equals(duplicateCheck))	throw processException("msg.batch_process_duplicated", new String[] {prg_id});
	}
	
	public void checkException(ParamMap apiInfoMap , Exception e) {
		String duplicateCheck = apiInfoMap.getString("duplicateCheck");
		
		String resultCode = "1".equals(duplicateCheck) ? "490" : "500";
		String resultMsg  = "1".equals(duplicateCheck) ? getMessage("errors.duplicate") : PaTmonComUtill.getErrorMessage(e);
		log.error(apiInfoMap.getString("message") , e);
		
		apiInfoMap.put("code"		, resultCode);
		apiInfoMap.put("message"	, resultMsg);
	}
	
	public void closeApi(HttpServletRequest request, ParamMap apiInfoMap) {
		String duplicateCheck = apiInfoMap.getString("duplicateCheck");
		String prg_id		  = apiInfoMap.getString("apiCode");
		
		if("".equals(apiInfoMap.getString("code")))		  apiInfoMap.put("code"		, "200"); //성공
		if("".equals(apiInfoMap.getString("message"))) 	  apiInfoMap.put("message"	, "OK");
		
		try {
			systemService.insertApiTrackingTx(request, apiInfoMap);
			
			if("0".equals(duplicateCheck)) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}		
		}catch (Exception e) {
			log.error("ERROR - ApiTracking_CloseHistory" + e.toString());
		}
	}
	
	public void getApiInfo(String apiCode, ParamMap apiInfoMap) throws Exception {
		HashMap<String, String> apiInfo		= new HashMap<String, String>();
		
		apiInfoMap.put("apiCode"		, apiCode);
		apiInfoMap.put("broadCode"		, Constants.PA_TMON_BROAD_CODE);
		apiInfoMap.put("onlineCode"		, Constants.PA_TMON_ONLINE_CODE);
		apiInfoMap.put("siteGb"			, Constants.PA_TMON_PROC_ID);
		apiInfoMap.put("startDate"		, systemService.getSysdatetimeToString());
		apiInfo = systemService.selectPaApiInfo(apiInfoMap);
		apiInfoMap.put("method"			, apiInfo.get("REMARK1"));
		apiInfoMap.put("url"			, apiInfo.get("API_URL"));
		apiInfoMap.put("paBroad"		, apiInfo.get(Constants.PA_BROAD));
		apiInfoMap.put("paOnline"		, apiInfo.get(Constants.PA_ONLINE));
		apiInfoMap.put("code"			, "200");
		apiInfoMap.put("message"		, "OK");
	}
	
	/**
	 * 티몬 통신 모듈
	 * @param ParamMap/  apiInfoParam = 통신에 관련된 정보, apiDataObject =Body에 필요한 정보
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String,Object> apiGetObjectByTmon(ParamMap apiInfoParam, Object apiDataObject) throws Exception {
		
		//티몬 토큰 생성
		createTimonToken(apiInfoParam);
		String result 				 = "";
		String body					 = "";
		Map<String,Object> returnMap = null;
		//=1) Object(paramMap 또는 ArrayList)로 만들어진 데이터로 RequestBody를 생성
		//JSON 결과를 List로 만들고 싶으면 paramObject를 list로 Map으로 만들고 싶으면 ParmaMap으로 만들어서 보내면 됩니다.
		body = getBody(apiDataObject);
		apiInfoParam.put("body", body);
		
		//=2) 실제 통신을 통해 결과를 받아온다.
		result = connectTmonReal(apiInfoParam);  //result = connectLtonFake(paramMap);
		
		//=3) JSON을 MAP으로 만들어준다 
		returnMap = ComUtil.splitJson(result);
		
		return returnMap;
	}
	
	//티몬 토큰발급
	public void createTimonToken(ParamMap apiInfoParam) throws Exception{
		String   url 		 			= "";
		ParamMap urlResultParam 	= new ParamMap();
		HttpMethod method 			= null;
		RestTemplate restTemplate 	= new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		String tokenApi 			= "IF_PATMONAPI_00_001";
		//String responseTime			= "";
		Map<String,Object> resMap   = new HashMap<String, Object>();
		
		urlResultParam  			= getApiUrl(tokenApi);  //api_code
		method 						= getMethod(urlResultParam.getString("METHOD"));	
		url 						= urlResultParam.getString("URL");
		
		//TMON_AUTHORIZATION -> TMON_CLIENT_ID:TMON_CLIENT_SECRETKEY 를 BASE64인코딩한값
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("Authorization", "Basic " + ConfigUtil.getString("TMON_AUTHORIZATION"));
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
		bodyMap.add("grant_type", "client_credentials");
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(bodyMap, headers);
		//double timeS 		= 0;
		//double timeE 		= 0;
		ResponseEntity<String> response = null;
		String result 		= null;	
		try{
			//log.info("=====response=====================");
			//timeS 	 = System.nanoTime(); // 토큰생성 API 호출 시작 시간
			response = restTemplate.exchange(url, method, entity, String.class);
				
			//timeE	 = System.nanoTime(); // 토큰생성 API 호출 종료 시간
			result	 = checkResponseResult(tokenApi, response);
			resMap = ComUtil.splitJson(result);
			
			apiInfoParam.put("vendorId"	, resMap.get("vendorId").toString());
			apiInfoParam.put("token"	, resMap.get("access_token").toString());
		} catch (final HttpServerErrorException ee){
			//timeE 	= System.nanoTime(); // 토큰생성 API 호출 종료 시간
			log.error(ee.getResponseBodyAsString());
			result 	= tokenApi + "(토큰생성 에러) " + ee.getStatusCode() + " : " + ee.getResponseBodyAsString();
			checkErrorResponseResult(tokenApi , ee);
		} finally{
			//responseTime = Double.toString((timeE-timeS)/1000000000); //ex) 11초면  11.863670103
		}		
	}
	
	private ParamMap getApiUrl(String apiCode) throws Exception{
		String baseURL 		= ConfigUtil.getString("PATMON_COM_BASE_URL");
		ParamMap paramMap 	= new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		apiInfo 			= systemService.selectPaApiInfo4Url(apiCode);
		
		paramMap.put("URL" 			, baseURL + apiInfo.get("API_URL"));
		paramMap.put("METHOD" 		, apiInfo.get("REMARK1"));
		paramMap.put("REQUEST_TYPE" , apiInfo.get("REQUEST_TYPE"));
	
		return paramMap;
	}
	
	private HttpMethod getMethod(String method){
		switch (method) {
		case "POST":
			return HttpMethod.POST;
		case "GET":
			return HttpMethod.GET;
		case "PUT":
			return HttpMethod.PUT;
		case "DELETE":
			return HttpMethod.DELETE;
		case "GP":  
		default:
			return HttpMethod.GET;
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	private String checkResponseResult(String apiCode , ResponseEntity<String> result) throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String passErrCode = null;
		String message 		= "";

		//Step 1) Null Check - 1
		if(result ==null || "".equals(result)){
			throw processException("pa.connect_error" ,  new String[] { "Response is Null" }); //호출 함수에서 실패여부 따로 체크
		}

		//Step 2) Null Check - 2
		resultMap = ComUtil.splitJson(result.getBody());

		if (resultMap.get("ResultCode") ==null ){
			return result.getBody(); //호출 함수에서 실패여부 따로 체크
		}

		//Step 3) Setting API Result-Code And Message
		passErrCode	 = String.valueOf(resultMap.get("ResultCode").toString());

		if (resultMap.get("Message") != null && !resultMap.get("Message").equals("")){
			message = resultMap.get("Message").toString();
		}else{
			message =  result.getBody();
		}
		
		//Step 4) Check Management logic what Each api set And Return Exception if API Don't have Right Result-Code
		errorCodeManaging(apiCode, passErrCode, message);
		
		return result.getBody();
		
	}
	
	private void checkErrorResponseResult(String apiCode , HttpServerErrorException ee) throws Exception{
		
		String ResponseBody = ee.getResponseBodyAsString();
		String errorCode 	= ee.getStatusCode().toString();
		errorCodeManaging(apiCode, errorCode ,ResponseBody);
	}
	
	private void errorCodeManaging(String apiCode, String passErrCode, String message) throws Exception{
		/*
		//= 1) 공통 Pass
		if( passErrCode.equals("0") ) return;
		
		switch(apiCode){
		case "IF_PATMONAPI_03_015": //교환 재발송 처리 완료			
			if(message.contains("이미 교환 완료되었습니다")) return;
			break;
		case "IF_PATMONAPI_05_004" :	//고객 긴급메시지 조회
			if(message.contains("조회된 기간에 조회 대상이 없습니다")) return;
			break;
		case "IF_PATMONAPI_00_007" : //판매자주소조회
			if(message.contains("정보가 조회되지 않습니다.")){
				return;
			}
		}
		*/
		throw processException("pa.connect_error" ,  new String[] { apiCode + " " + passErrCode + " : " + message} );		
	}
	
	/**
	 * 티몬 통신 모듈 
	 * @param apiCode, method, url, secKey 
	 * 		  paCode ,body 필수
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	private String connectTmonReal(ParamMap paramMap) throws Exception {
		
		String openAPIUrl 	 = ConfigUtil.getString("PATMON_COM_BASE_URL");
		//String stringResult  = "";
		String token		= paramMap.get("token").toString();
		String partnerId	= "";
		String partnerToken	= "";
		String body			= paramMap.get("body").toString();
		StopWatch watch		= new StopWatch();
		HttpMethod method	= getMethod(paramMap.get("method").toString());
		String url			= openAPIUrl + paramMap.get("url").toString();
		url					= url.replace("{vendorId}", paramMap.get("vendorId").toString());
		String stringResult	= "";
		
		if(Constants.PA_TMON_BROAD_CODE.equals(paramMap.get("paCode").toString())) {
			partnerId = ConfigUtil.getString("TMON_BROAD_PARTNER_ID");
			partnerToken = paramMap.get("paBroad").toString();
		} else {
			partnerId = ConfigUtil.getString("TMON_ONLINE_PARTNER_ID");
			partnerToken = paramMap.get("paOnline").toString();
		}
		
		try {
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout	(CONN_TIMEOUT * 1000); //타임아웃 설정 100초
			factory.setReadTimeout		(CONN_TIMEOUT * 1000); //타임아웃 설정 100초
			RestTemplate restTemplate = new RestTemplate(factory);		
			HttpHeaders headers 	  = new HttpHeaders();
			
			headers.set ("Authorization"	, "Bearer " + token);
			headers.set ("X-Partner-Id"		, partnerId);
			headers.set ("X-Partner-Token"	, partnerToken);			
			
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			HttpEntity<String> entity = new HttpEntity<>(body, headers);
				
			watch.start();
			ResponseEntity<String> response = restTemplate.exchange(url, method, entity , String.class);
			watch.stop();
			
			if(response.getBody()!=null 
				&& !"IF_PATMONAPI_04_007".equals(paramMap.get("apiCode").toString()) 
				&& !"IF_PATMONAPI_02_002".equals(paramMap.get("apiCode").toString())) {
				//즉시취소처리, 상품문의 답변 response형식이 좀 다름
				stringResult = response.getBody();
			} else {
				stringResult = "{ \"code\" : \"" + String.valueOf(response.getStatusCodeValue()) + "\"}";
			}
			
		}catch (final HttpClientErrorException ee){
			log.error(ee.toString());
			throw processException("pa.tmon_connect_error" , new String[] { PaTmonComUtill.getErrorMessage(ee)} );
		}catch (final HttpServerErrorException e2){
			log.error(e2.getResponseBodyAsString());
			stringResult = e2.getResponseBodyAsString();
			//checkErrorResponseResult(paramMap.get("apiCode").toString(), e2);
		}catch (Exception e) {
			log.error(e.toString());
			throw processException("pa.tmon_connect_error" , new String[] { PaTmonComUtill.getErrorMessage(e) });
		}finally { 		
			paramMap.put("result"		, stringResult);
			paramMap.put("body"			, body);
			paramMap.put("responseTime"	, String.format("%.6f", watch.getTotalTimeSeconds())); //API 수행시간
			saveRequestMap(paramMap);
		}
		
		return stringResult;
	}
	
	private String getBody(Object paramObject) throws Exception {
		ObjectMapper objectMapper   	= new ObjectMapper();
		String 	     result				= "";
		Object		 requestObejct		= null;
		
		if(paramObject == null) return "";
		
		if(paramObject instanceof ArrayList<?> ) {
			requestObejct = new ArrayList<Map<String , Object>>();
			requestObejct = paramObject;
		}else if(paramObject instanceof ParamMap){
			requestObejct = new  HashMap<String , Object>();
			requestObejct = ((ParamMap)paramObject).get();
		}else {
			return "";
		}
		
		try {
			result = objectMapper.writeValueAsString(requestObejct);
		} catch (JsonGenerationException e) {
			result = "";
			e.printStackTrace();
		} catch (JsonMappingException e) {
			result = "";
			e.printStackTrace();
		} catch (IOException e) {
			result = "";
			e.printStackTrace();
		}
			
		return result;
	}
	
	private void saveRequestMap(ParamMap param) {
		
		try {
			PaRequestMap paRequestMap = new PaRequestMap();
			
			String paCode	= param.getString("paCode");
			if("".equals(paCode) ) paCode = Constants.PA_TMON_BROAD_CODE; 
			
			paRequestMap.setPaCode			(paCode);
			paRequestMap.setReqApiCode		(param.getString("apiCode"));
			paRequestMap.setReqUrl			("["+param.getString("method")+"]"+param.getString("url"));
			paRequestMap.setReqHeader		(""); 
			paRequestMap.setRequestMap		(param.getString("body"));
			paRequestMap.setResponseMap		(param.getString("result"));
			paRequestMap.setRemark			(param.getString("responseTime"));
			
			systemService.insertPaRequestMapTx(paRequestMap);
		}
		catch ( Exception e ) {
			//ignore
			log.error(e.toString());
		}
	}
	
	//flag 1 : 주문 (30분 이전 시간 조회)
	//flag 2 : 상담
	public ParamMap getRetrieveDate(String fromDate, String toDate, String flag) throws Exception {
		
		ParamMap paramMap 			  = new ParamMap();
		SimpleDateFormat date_format  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat counsel_format  = new SimpleDateFormat("yyyy-MM-dd");
		
		if(ComUtil.NVL(toDate).length() == 14 && ComUtil.NVL(fromDate).length() == 14) {
			fromDate = fromDate.substring(0,4) + "-" + fromDate.substring(4,6) + "-" + fromDate.substring(6,8)+ "T" + 
					   fromDate.substring(8,10) + ":" + fromDate.substring(10,12) + ":" + fromDate.substring(12,14);
			toDate   = toDate.substring(0,4) + "-" + toDate.substring(4,6) +  "-" + toDate.substring(6,8) + "T" + 
					   toDate.substring(8,10) + ":" + toDate.substring(10,12) + ":" + toDate.substring(12,14); 
			
			paramMap.put("FROM_DATE", fromDate);
			paramMap.put("TO_DATE", toDate);
		} else if (ComUtil.NVL(toDate).length() == 8 && ComUtil.NVL(fromDate).length() == 8) {
			//상담
			fromDate = fromDate.substring(0,4) + "-" + fromDate.substring(4,6) + "-" + fromDate.substring(6,8);
			toDate   = toDate.substring(0,4) + "-" + toDate.substring(4,6) +  "-" + toDate.substring(6,8);
			
			paramMap.put("FROM_DATE", fromDate);
			paramMap.put("TO_DATE", toDate);
		} else if ("1".equals(flag)) {
			paramMap.put("FROM_DATE", date_format.format(DateUtil.addDay(systemService.getSysdate(), -3)));
			paramMap.put("TO_DATE"	, date_format.format(DateUtil.addMinute(systemService.getSysdatetime(), -30)));
		} else if ("2".equals(flag)) {
			paramMap.put("FROM_DATE", counsel_format.format(DateUtil.addDay(systemService.getSysdate(), -3)));
			paramMap.put("TO_DATE"	, counsel_format.format(systemService.getSysdate()));
		}else {
			paramMap.put("FROM_DATE", date_format.format(DateUtil.addDay(systemService.getSysdate(), -3)));
			paramMap.put("TO_DATE"	, date_format.format(DateUtil.toTimestamp(systemService.getSysdatetimeToString())));
		}
		return paramMap;
	}
}
