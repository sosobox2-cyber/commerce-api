package com.cware.netshopping.patdeal.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.repository.TransLogMapper;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository("com.cware.netshopping.patdeal.util.paTdealConnectUtil")
public class PaTdealConnectUtil  extends AbstractService{
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaTdealApiRequest apiRequest;

	@Autowired
	private TransLogService transLogService;
	
	@Autowired
	private TransLogMapper logMapper;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired 
	MessageSource ms;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final int    CONN_TIMEOUT = 100;
	
	/**
	 * 티딜 Common 통신 API
	 * 
	 * @param PaTransService
	 * @param String
	 * @param Map<String, String>
	 * @param Object
	 * @return Map<String,Object>
	 * @throws Exception
	 */	
	public Map<String,Object> getCommon(PaTransService serviceLog, String pathParameters, Map<String, String> queryParameters, Object apiDataObject) throws Exception {
						
		// API 정보조회
		ParamMap apiInfoMap = apiRequest.getApiInfo(serviceLog.getServiceName());
		// HTTP METHOD
		HttpMethod method = apiRequest.getMethod(apiInfoMap.getString("method").toUpperCase());
				
		// URI 세팅 (Url + Params) and pathParameters replace 처리
		String uri = apiInfoMap.getString("url");
		uri = uri.replace("{urlParameter}", pathParameters);		
        uri = apiRequest.TDEAL_GATEWAY + uri + PaTdealApiRequest.makeQuryString(queryParameters);
				
		// BODY 세팅
		String body = apiRequest.getBody(apiDataObject);
		
		// API 로그 생성
		PaTransApi apiLog = new PaTransApi();		
		apiLog.setTransCode(serviceLog.getTransCode());
		apiLog.setTransType(serviceLog.getTransType());
		apiLog.setApiName(serviceLog.getServiceName());
		apiLog.setApiNote(serviceLog.getServiceNote());
		apiLog.setTransServiceNo(serviceLog.getTransServiceNo());
		apiLog.setPaGroupCode(serviceLog.getPaGroupCode());
		apiLog.setProcessId(PaGroup.TDEAL.processId());	
		apiLog.setApiUrl(method.name() + " " + uri);
		apiLog.setTransApiNo(logMapper.createTransApiNo());
		
		try {			
			// API명 추출(헤더 공통 전송용)
			String apiName = apiLog.getApiName();
			
			// API요청 공통 헤더 설정
			HttpHeaders headers 	  = apiRequest.createHeader(method, serviceLog.getPaCode(), apiName);
			HttpEntity<?> entity = new HttpEntity<>(body, headers);
						
			transLogService.logTransApiReqTdeal(apiLog, entity);

			// API 통신
			HttpEntity<String> response = restTemplate.exchange(uri ,method, entity, String.class);
			
			apiLog.setResponsePayload(response.getBody());
			apiLog.setResultCode(PaTdealApiRequest.API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog); 
							
			return ComUtil.splitJson(response.getBody()); 
			
		} catch (RestClientResponseException ex) {
			log.error(serviceLog.getServiceNote() + " ={} {}", serviceLog.getTransCode(), ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorTdeal(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error(serviceLog.getServiceNote() + " ={} {}", serviceLog.getTransCode(), apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error(serviceLog.getServiceNote() +" ={}", serviceLog.getTransCode(), e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}	
	
	/**
	 * 티딜 상품 통신 API
	 * 
	 * @param PaTransService
	 * @param String
	 * @param Map<String, String>
	 * @param Object
	 * @return Map<String,Object>
	 * @throws Exception
	 */	
	public Map<String,Object> getProduct(PaTransService serviceLog, String pathParameters, Map<String, String> queryParameters, Object apiDataObject) throws Exception {
						
		// API 정보조회
		ParamMap apiInfoMap = apiRequest.getApiInfo(serviceLog.getServiceName());
		// HTTP METHOD
		HttpMethod method = apiRequest.getMethod(apiInfoMap.getString("method").toUpperCase());
				
		// URI 세팅 (Url + Params) and pathParameters replace 처리
		String uri = apiInfoMap.getString("url");
		uri = uri.replace("{urlParameter}", pathParameters);		
        uri = apiRequest.TDEAL_GATEWAY + uri + PaTdealApiRequest.makeQuryString(queryParameters);
				
		// BODY 세팅
		String body = apiRequest.getBody(apiDataObject);
		
		// API 로그 생성
		PaTransApi apiLog = new PaTransApi();		
		apiLog.setTransCode(serviceLog.getTransCode());
		apiLog.setTransType(serviceLog.getTransType());
		apiLog.setApiName(serviceLog.getServiceName());
		apiLog.setApiNote(serviceLog.getServiceNote());
		apiLog.setTransServiceNo(serviceLog.getTransServiceNo());
		apiLog.setPaGroupCode(serviceLog.getPaGroupCode());
		apiLog.setProcessId(PaGroup.TDEAL.processId());	
		apiLog.setApiUrl(method.name() + " " + uri);
		apiLog.setTransApiNo(logMapper.createTransApiNo());
		
		try {			
			// API명 추출(헤더 공통 전송용)
			String apiName = apiLog.getApiName();
			
			// API요청 공통 헤더 설정
			HttpHeaders headers 	  = apiRequest.createHeader(method, serviceLog.getPaCode(), apiName);
			HttpEntity<?> entity = new HttpEntity<>(body, headers);
						
			transLogService.logTransApiReqTdeal(apiLog, entity);

			// API 통신
			HttpEntity<String> response = restTemplate.exchange(uri ,method, entity, String.class);
			
			apiLog.setResponsePayload(response.getBody());
			apiLog.setResultCode(PaTdealApiRequest.API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog); 
							
			return ComUtil.splitJson(response.getBody()); 
			
		} catch (RestClientResponseException ex) {
			log.error(serviceLog.getServiceNote() + " ={} {}", serviceLog.getTransCode(), ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorTdeal(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error(serviceLog.getServiceNote() + " ={} {}", serviceLog.getTransCode(), apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error(serviceLog.getServiceNote() +" ={}", serviceLog.getTransCode(), e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}	
	
	/**
	 * 티딜 상품 승인 통신 api
	 * @param serviceLog
	 * @param apiDataObject
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String confirmProduct(PaTransService serviceLog, ParamMap apiDataObject) throws Exception {
		// API 정보조회
		ParamMap apiInfoMap = apiRequest.getApiInfo("IF_PATDEALAPI_01_003");
		// HTTP METHOD
		HttpMethod method = apiRequest.getMethod(apiInfoMap.getString("method").toUpperCase());

		// URI 세팅 (Url + Params) and pathParameters replace 처리
		String uri = apiInfoMap.getString("url");
		uri = apiRequest.TDEAL_GATEWAY + uri;

		// BODY 세팅
		String body = apiRequest.getBody(apiDataObject);

		// API 로그 생성
		PaTransApi apiLog = new PaTransApi();
		apiLog.setTransCode(serviceLog.getTransCode());
		apiLog.setTransType(serviceLog.getTransType());
		apiLog.setApiName(serviceLog.getServiceName());
		apiLog.setApiNote(serviceLog.getServiceNote());
		apiLog.setTransServiceNo(serviceLog.getTransServiceNo());
		apiLog.setPaGroupCode(serviceLog.getPaGroupCode());
		apiLog.setProcessId(PaGroup.TDEAL.processId());
		apiLog.setApiUrl(method.name() + " " + uri);
		apiLog.setTransApiNo(logMapper.createTransApiNo());

		try {
			// API명 추출(헤더 공통 전송용)
			String apiName = apiLog.getApiName();

			// API요청 공통 헤더 설정
			HttpHeaders headers = apiRequest.createHeader(method, serviceLog.getPaCode(), apiName);
			HttpEntity<?> entity = new HttpEntity<>(body, headers);

			transLogService.logTransApiReqTdeal(apiLog, entity);

			// API 통신
			HttpEntity<String> response = restTemplate.exchange(uri, method, entity, String.class);

			String result = response.getBody();
			
			Map<String, Object> resultMap = StringUtil.jsonToMap(result);

            List<String> successNos = (List<String>) resultMap.get("successNos");
            String successNo = (successNos.size() > 0 ? String.valueOf( successNos.get(0)) : "");
           
			apiLog.setResponsePayload(result);
			
			if(!"".equals(successNo) || String.valueOf(resultMap.get("failures")).contains("상품심사진행 상태가 아닙니다")) {
				apiLog.setSuccessYn("1");
			}else {
				apiLog.setSuccessYn("0");
			}
			
			if ("1".equals(apiLog.getSuccessYn())) {
				apiLog.setResultCode(PaTdealApiRequest.API_SUCCESS_CODE);
			} else {
				apiLog.setResultCode(PaTdealApiRequest.API_ERROR_CODE);
				apiLog.setResultMsg(String.valueOf(resultMap.get("failures")));
			}
			
			transLogService.logTransApiRes(apiLog);

			if (apiLog.getSuccessYn().equals("0")) throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());

			
			return apiLog.getSuccessYn();

		} catch (RestClientResponseException ex) {
			log.error(serviceLog.getServiceNote() + " ={} {}", serviceLog.getTransCode(), ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorTdeal(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (TransApiException ex) {
			log.error(serviceLog.getServiceNote() + " ={} {}", serviceLog.getTransCode(), apiLog.getResultMsg());
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		} catch (Exception e) {
			log.error(serviceLog.getServiceNote() + " ={}", serviceLog.getTransCode(), e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
	}
	
	/**
	 * 티딜 Common 통신 API (레거시)
	 * 
	 * @param String
	 * @param String
	 * @param Map<String, String>
	 * @param Object
	 * @return Map<String,Object>
	 * @throws Exception
	 */	
	public Map<String,Object> getCommonLegacy(String paCode, String apiCode, String pathParameters, Map<String, String> queryParameters, Object apiDataObject) throws Exception {
						
		// API 정보조회
		ParamMap apiInfoMap = apiRequest.getApiInfo(apiCode);
		// HTTP METHOD
		HttpMethod method = apiRequest.getMethod(apiInfoMap.getString("method").toUpperCase());
		
        apiInfoMap.put("paCode" , paCode);
				
		// URI 세팅 (Url + Params) and pathParameters replace 처리
		String uri = apiInfoMap.getString("url");
		uri = uri.replace("{urlParameter}", pathParameters);
        apiInfoMap.put("url", uri + PaTdealApiRequest.makeQuryString(queryParameters));
        uri = apiRequest.TDEAL_GATEWAY + uri + PaTdealApiRequest.makeQuryString(queryParameters);
				
		// BODY 세팅
		String body = apiRequest.getBody(apiDataObject);
		apiInfoMap.put("body", body);
		
		StopWatch watch = new StopWatch();
		ResponseEntity<String> response = null;
		String stringResult = "";
		Map<String, Object> errMap = null;
		
		try {			
			// API요청 공통 헤더 설정
			HttpHeaders headers  = apiRequest.createHeader(method, paCode, apiCode);
			HttpEntity<?> entity = new HttpEntity<>(body, headers);
			apiInfoMap.put("reqHeader", entity.getHeaders().toString());

			// API 통신
			watch.start();
			response = restTemplate.exchange(uri ,method, entity, String.class);
			watch.stop();
			
			stringResult = response.getBody();
			Map<String,Object> responseMap = new HashMap<String,Object>();
			
			if(ComUtil.splitJson(stringResult)!= null) {
				 responseMap = ComUtil.splitJson(stringResult);
				
			}
			
			responseMap.put("status", response.getStatusCode().toString());
			
			return responseMap;
			
		} catch (RestClientResponseException ex){
			stringResult = getErrorMessageUtf8(ex);
			errMap = StringUtil.jsonToMap(stringResult);
			if(errMap.containsKey("status")&&errMap.containsKey("code")) { // code가 존재 할 시 정상적인 에러(주문상태가 변경되었거나 철회된 클레임을 승인 할 시 발생) 
				Map<String,Object> responseMap = new HashMap<String,Object>();
				responseMap.put("status", errMap.get("status"));
				responseMap.put("code", errMap.get("code"));
				responseMap.put("message", errMap.get("message")); 
				responseMap.put("path", errMap.get("path"));
				return responseMap;	
			}
			log.info("API RESPONSE: {}", ex.getResponseBodyAsString());
            stringResult = "TDEAL(API-RESPONSE) : " + ex.getResponseBodyAsString();
            throw new TransApiException(stringResult, String.valueOf(ex.getRawStatusCode()));
		} catch (Exception e) {
			log.error("API ERROR: {}", getErrorMessage(e), e);
            throw new TransApiException(getErrorMessage(e), "Exception");
		} finally {
			apiInfoMap.put("result"		 , stringResult);
			apiInfoMap.put("responseTime", String.format("%.6f", watch.getTotalTimeSeconds())); //API 수행시간
			saveRequestMap(apiInfoMap); // TPAREQUESTMAP_LOG 저장
		}
	}
	
	public void saveRequestMap(ParamMap apiInfoMap) {
		
		try {
			PaRequestMap paRequestMap = new PaRequestMap();
			
			String paCode	= apiInfoMap.getString("paCode");
			if("".equals(paCode) ) paCode = Constants.PA_TDEAL_BROAD_CODE; 
			
			paRequestMap.setPaCode			(paCode);
			paRequestMap.setReqApiCode		(apiInfoMap.getString("apiCode"));
			paRequestMap.setReqUrl			("["+apiInfoMap.getString("method")+"]"+apiInfoMap.getString("url"));
			paRequestMap.setReqHeader		(apiInfoMap.getString("reqHeader")); 
			paRequestMap.setRequestMap		(apiInfoMap.getString("body"));
			paRequestMap.setResponseMap		(apiInfoMap.getString("result"));
			paRequestMap.setRemark			(apiInfoMap.getString("responseTime"));
			
			systemService.insertPaRequestMapTx(paRequestMap);
		}
		catch ( Exception e ) {
			//ignore
			log.error(e.toString());
		}
	}
	
	public static String getErrorMessage(Exception e) {
		String errMsg = "";
		
		if(e.getMessage() != null) {
			errMsg = e.getMessage();
		}else {
			errMsg = e.toString();
		}
		errMsg = errMsg.length() > 3950 ? errMsg.substring(0, 3950) : errMsg;
		return errMsg;
	}
	
	public String getErrorMessageUtf8(RestClientResponseException ex) throws UnsupportedEncodingException {
		String responseBodyAsString = "";
		
		// 4XX에러 OR GW 발생시 Content-type의 인코딩 셋이 null로 RestClientResponseException 발생
		// RestClientResponseException의 기본 인코딩셋 'ISO-8859-1'을 'UTF-8'로 변환 처리
		if(ex.getResponseHeaders().getContentType().getCharset() == null) {
			responseBodyAsString = URLDecoder.decode(ex.getResponseBodyAsString(), "ISO-8859-1");
			responseBodyAsString = new String(responseBodyAsString.getBytes("ISO-8859-1"), "UTF-8");
		} else {
			responseBodyAsString = ex.getResponseBodyAsString();
		}
		
		return responseBodyAsString;
	}



	
	
// 해당 소스 필요 없으면 삭제 필요...	
//	/** paramMap 필수 값 : apiCode startDate siteGb    
//	    선택 값 : message, code  **/
//	public void dealSuccess(ParamMap paramMap, HttpServletRequest request) throws Exception {
//		String rtnCode = paramMap.getString("code");
//		
//		if(!rtnCode.equals("200") &&  rtnCode.equals("")){
//			paramMap.put("code","304");
//			paramMap.put("message","에러 내역이 존재하지 않는 기타 에러건");
//		}
//		
//		systemService.insertPaApiTrackingTx(request,paramMap);	
//	}
//	
//	public void dealException(Exception se, ParamMap paramMap){
//		String message = null;
//		String duplicatrionMessage = ms.getMessage("msg.batch_process_duplicated",new String[] { "" }, null);
//		String connectionMessage = ms.getMessage("msg.jwt_connect_error", new String[] { "" }, null);
//		String jsonSplitMessage    = ms.getMessage("msg.error_jsonsplit",new String[] { "" }, null);
//		
//		if(se.getMessage() ==null){
//			message = se.toString();
//		}else{
//			message = se.getMessage();
//		}
//		
//		//= 중복체크
//		if(message.contains(duplicatrionMessage)){
//			paramMap.put("code","490");
//			paramMap.put("message",message.length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
//		}
//			
//		//= Json 가공 에러
//		if(message.contains(jsonSplitMessage)){
//			paramMap.put("code","480");
//			paramMap.put("message",message.length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
//		}
//		
//		//= JWT 통신 에러
//		if(message.contains(connectionMessage)){
//			paramMap.put("code","400");
//			paramMap.put("message",message.length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
//		}
//
//		//= 기타 프로세스 에러
//		if(paramMap.getString("code").equals("")){
//			paramMap.put("code","500");
//			paramMap.put("message",message.length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
//		}
//	
//		//= 추적이 불가능한 에러
//		if(message == null ||message.equals("")){
//			paramMap.put("code","304");
//			paramMap.put("message","에러 내역이 존재하지 않는 기타 에러건");
//		}
//		
//	}
//	
	
	public void checkException(ParamMap apiInfoMap , Exception e) {
		String duplicateCheck = apiInfoMap.getString("duplicateCheck");
		
		String resultCode = "1".equals(duplicateCheck) ? "490" : "500";
		String resultMsg  = "1".equals(duplicateCheck) ? getMessage("errors.duplicate") : PaTdealComUtil.getErrorMessage(e);
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
			
			if(!"1".equals(duplicateCheck) && !"".equals(prg_id)) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}		
		}catch (Exception e) {
			log.error("ERROR - ApiTracking_CloseHistory" + e.toString());
		}
	}
	
	public String connectSlackTransfer(ParamMap paramMap) throws Exception {
		String url = paramMap.get("url").toString();
		String body = paramMap.get("body").toString();
		String result = "";
		StopWatch watch = new StopWatch();
		HttpMethod method = HttpMethod.POST;
		
		try {
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout(CONN_TIMEOUT * 1000);
			factory.setReadTimeout(CONN_TIMEOUT * 1000);
			RestTemplate restTemplate = new RestTemplate(factory);
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			HttpEntity<String> entity = new HttpEntity<>(body, headers);
			
			watch.start();
			ResponseEntity<String> response = restTemplate.exchange(url, method, entity , String.class);
			watch.stop();
			
			if (response.getBody() != null) {
				result = response.getBody();
			} else {
				result = "{ \"code\" : \"" + String.valueOf(response.getStatusCodeValue()) + "\"}";
			}
		} catch (final HttpClientErrorException ee) {
			log.error(ee.toString());
			throw processException("pa.tdeal_slack_connect_error" , new String[] { getErrorMessage(ee)} );
		} catch (final HttpServerErrorException e2) {
			log.error(e2.getResponseBodyAsString());
			result = e2.getResponseBodyAsString();
		} catch (Exception e) {
			log.error(e.toString());
			throw processException("pa.tdeal_slack_connect_error" , new String[] { getErrorMessage(e) });
		} finally {
			paramMap.put("result", result);
			paramMap.put("body", body);
			paramMap.put("responseTime", String.format("%.6f", watch.getTotalTimeSeconds()));
			saveRequestMap(paramMap);
		}
		return result;
	}
	
	public String getBody(Object paramObject) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Object requestObject = null;
		String result = "";
		
		if (paramObject == null) return "";
		
		if (paramObject instanceof ArrayList<?>) {
			requestObject = new ArrayList<Map<String, Object>>();
			requestObject = paramObject;
		} else if (paramObject instanceof ParamMap) {
			requestObject = new HashMap<String, Object>();
			requestObject = ((ParamMap)paramObject).get();
		} else {
			return "";
		}
		
		try {
			result = objectMapper.writeValueAsString(requestObject);
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
}
