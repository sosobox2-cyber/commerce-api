package com.cware.netshopping.paqeen.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaCode;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.log.domain.PaTransApi;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.repository.TransLogMapper;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.cware.netshopping.paqeen.repository.PaQeenCommonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

/**
 * 퀸잇 API 연동 공통(인증/헤더)
 */
@Service
public class PaQeenApiRequest extends AbstractService {
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Value("${partner.qeen.api.tv.id}")
	String QEEN_TV_ID; //방송 아이디
	
	@Value("${partner.qeen.api.tv.password}")
	String QEEN_TV_PWD; //방송 암호
	
	@Value("${partner.qeen.api.online.id}")
	String QEEN_ONLINE_ID; //온라인 미연동 
	
	@Value("${partner.qeen.api.online.password}")
	String QEEN_ONLINE_PWD; //온라인 미연동 
	
	@Value("${partner.qeen.api.gateway}")
	String QEEN_GATEWAY;
	
	@Autowired
	PaQeenCommonMapper paQeenCommonMapper;
	
	@Autowired
	private TransLogService transLogService;
	
	@Autowired
	private TransLogMapper logMapper;
		
	private static final int CONN_TIMEOUT = 100;
	private static final int CONN_TIMEOUT_120 = 120;
	
	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE 	= "ERROR";
	
	/* 퀸잇 API 토큰 생성 */
	public String createAccessToken(String paCode) throws Exception {
		HttpHeaders headers = null;
		HttpEntity<String> entity = null;
		HttpMethod method = null;
		RestTemplate restTemplate = null;
		ResponseEntity<String> response = null;
		StopWatch watch = null;
		String url = "";
		String connectResult = null;
		Map<String, Object> connectResultMap = new HashMap<String, Object>();
		Map<String, String> refreshToken = new HashMap<String, String>();
		Map<String, String> accessToken = new HashMap<String, String>();
		
		ParamMap apiBaseInfo = new ParamMap();
		HashMap<String, String> body = new HashMap<>();
		List<HashMap<String, String>> tokenResultMap = new ArrayList<HashMap<String, String>>();
		String result = null;
		String apiCode="";
		
		try {
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout(CONN_TIMEOUT * 1000);
			factory.setReadTimeout(CONN_TIMEOUT * 1000);
			
			headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			headers.set("Seller-Platform", "SKSTOA");
			
			tokenResultMap = paQeenCommonMapper.selectPaQeenToken(paCode);
			for(HashMap<String, String> token:tokenResultMap) {
				switch (token.get("TOKEN_TYPE").toString()) {
				case "F1ACT":
					accessToken = token;
					break;
				case "F1RFT":
					refreshToken = token;
					break;
				case "F2ACT":
					accessToken = token;
					break;
				case "F2RFT":
					refreshToken = token;
					break;
				}
			}
			
			if("N".equals(accessToken.get("FLAG"))) { //엑세스토큰 재발급 필요 여부
				return accessToken.get("TOKEN_VALUE"); // 재발급 필요 없으므로 기존 값 전달
			}else if("N".equals(refreshToken.get("FLAG"))) { //리프래시토큰 재발급 필요 여부
				body.put("refreshToken", refreshToken.get("TOKEN_VALUE")); //엑세스토큰 재발급 필요 리프래시토큰 재발급 필요없음 상태
				apiCode = "IF_PAQEENAPI_00_009";
			}else {
				return createRefreshToken(paCode);// 엑세스토큰 재발급 필요 리프래시토큰 재발급 필요 상태
			}
			
			apiBaseInfo.put("apiCode", apiCode);
			getApiInfo(apiBaseInfo);
			url = QEEN_GATEWAY + apiBaseInfo.getString("url");
			entity = new HttpEntity<>(getBody(body), headers);
			apiBaseInfo.put("reqHeader",entity.getHeaders().toString());
			
			method = HttpMethod.valueOf("POST");
			
			restTemplate = new RestTemplate(factory);
			
			watch = new StopWatch();
			watch.start(); // API 호출 및 응답 수행시간 측정 Start
			response = restTemplate.exchange(url, method, entity, String.class);
			watch.stop(); // API 호출 및 응답 수행시간 측정 End
			
			connectResultMap = StringUtil.jsonToMap(response.getBody());
			result = (String) connectResultMap.get("accessToken");
			
			if("IF_PAQEENAPI_00_009".equals(apiCode)) {
				if("F1".equals(paCode)){
					paQeenCommonMapper.updatePaQeenToken(connectResultMap.get("accessToken").toString(),"QEEN_BROAD_ACCESS_TOKEN");
				}else {
					paQeenCommonMapper.updatePaQeenToken(connectResultMap.get("accessToken").toString(),"QEEN_ONLINE_ACCESS_TOKEN");
				}
			}
		} catch (RestClientResponseException ex){
			connectResult = ex.getResponseBodyAsString();// finally에서 utf-8로 변환
			String stringResult = StringUtil.jsonToMap(connectResult).toString();
			if("Invalid refresh token".equals(StringUtil.jsonToMap(connectResult).get("msg"))) {
            	return createRefreshToken(paCode);
            }
			log.info("API RESPONSE: {}", stringResult);
            stringResult = "QEEN(API-RESPONSE) : " + stringResult;
            saveRequestMap(apiBaseInfo); // TPAREQUESTMAP_LOG 저장
            throw new TransApiException(stringResult, String.valueOf(ex.getRawStatusCode()));
		} catch (Exception e) {
			log.error("API ERROR: {}", getErrorMessage(e), e);
			connectResult = getErrorMessage(e);
			saveRequestMap(apiBaseInfo); // TPAREQUESTMAP_LOG 저장
			throw new TransApiException(getErrorMessage(e), "Exception");
		} finally {
			if(url.equals("")) {
				apiBaseInfo.put("result"		 , "");
				apiBaseInfo.put("responseTime", ""); //API 수행시간
			}else {
				apiBaseInfo.put("result"		 , StringUtil.jsonToMap(connectResult).toString());
				apiBaseInfo.put("responseTime", String.format("%.6f", watch.getTotalTimeSeconds())); //API 수행시간
			}
			if(!apiCode.equals("")) {
				saveRequestMap(apiBaseInfo); // TPAREQUESTMAP_LOG 저장	
			}
		}
		return result;
	}
	
	/* 퀸잇 API 토큰 생성(로그인) */
	public String createRefreshToken(String paCode) throws Exception {
		HttpHeaders headers = null;
		HttpEntity<String> entity = null;
		HttpMethod method = null;
		RestTemplate restTemplate = null;
		ResponseEntity<String> response = null;
		StopWatch watch = null;
		String url = "";
		String connectResult = null;
		Map<String, Object> connectResultMap = new HashMap<String, Object>();
		ParamMap apiBaseInfo = new ParamMap();
		HashMap<String, String> body = new HashMap<>();
		String result = null;
		String apiCode="";
		try {
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout(CONN_TIMEOUT * 1000);
			factory.setReadTimeout(CONN_TIMEOUT * 1000);
			
			headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			headers.set("Seller-Platform", "SKSTOA");
			
			apiCode = "IF_PAQEENAPI_00_008";
			if("F1".equals(paCode)){
				body.put("id", QEEN_TV_ID);
				body.put("password", QEEN_TV_PWD);
			}else {
				body.put("id", QEEN_ONLINE_ID);
				body.put("password", QEEN_ONLINE_PWD);
			}
			
			apiBaseInfo.put("apiCode", apiCode);
			getApiInfo(apiBaseInfo);
			url = QEEN_GATEWAY + apiBaseInfo.getString("url");
			entity = new HttpEntity<>(getBody(body), headers);
			apiBaseInfo.put("reqHeader",entity.getHeaders().toString());
			
			method = HttpMethod.valueOf("POST");
			
			restTemplate = new RestTemplate(factory);
			
			watch = new StopWatch();
			watch.start(); // API 호출 및 응답 수행시간 측정 Start
			response = restTemplate.exchange(url, method, entity, String.class);
			watch.stop(); // API 호출 및 응답 수행시간 측정 End
			
			connectResultMap = StringUtil.jsonToMap(response.getBody());
			if("F1".equals(paCode)){
				paQeenCommonMapper.updatePaQeenToken(connectResultMap.get("accessToken").toString(),"QEEN_BROAD_ACCESS_TOKEN");
				paQeenCommonMapper.updatePaQeenToken(connectResultMap.get("refreshToken").toString(),"QEEN_BROAD_REFRESH_TOKEN");
			}else {
				paQeenCommonMapper.updatePaQeenToken(connectResultMap.get("accessToken").toString(),"QEEN_ONLINE_ACCESS_TOKEN");
				paQeenCommonMapper.updatePaQeenToken(connectResultMap.get("refreshToken").toString(),"QEEN_ONLINE_REFRESH_TOKEN");
			}
			result = (String) connectResultMap.get("accessToken");
			
		} catch (RestClientResponseException ex){
			connectResult = ex.getResponseBodyAsString();// finally에서 utf-8로 변환
			String stringResult = StringUtil.jsonToMap(connectResult).toString();
			log.info("API RESPONSE: {}", stringResult);
            stringResult = "QEEN(API-RESPONSE) : " + stringResult;
            saveRequestMap(apiBaseInfo); // TPAREQUESTMAP_LOG 저장
            throw new TransApiException(stringResult, String.valueOf(ex.getRawStatusCode()));
		} catch (Exception e) {
			log.error("API ERROR: {}", getErrorMessage(e), e);
			connectResult = getErrorMessage(e);
			saveRequestMap(apiBaseInfo); // TPAREQUESTMAP_LOG 저장
			throw new TransApiException(getErrorMessage(e), "Exception");
		} finally {
			if(url.equals("")) {
				apiBaseInfo.put("result"		 , "");
				apiBaseInfo.put("responseTime", ""); //API 수행시간
			}else {
				apiBaseInfo.put("result"		 , StringUtil.jsonToMap(connectResult).toString());
				apiBaseInfo.put("responseTime", String.format("%.6f", watch.getTotalTimeSeconds())); //API 수행시간
			}
			if(!apiCode.equals("")) {
				saveRequestMap(apiBaseInfo); // TPAREQUESTMAP_LOG 저장	
			}
		}
		return result;
	}
	/* Body 생성 (Object(HashMap 또는 ArrayList) 데이터를  String로  전환) */
	public String getBody(Object apiRequestObject) throws Exception {
		ObjectMapper objectMapper = null;
		Object paramObejct = null;
		String body = null;
		objectMapper = new ObjectMapper();
		String packageName = apiRequestObject.getClass().getPackage().getName();
		try {
			if (apiRequestObject instanceof ArrayList<?>) {
				paramObejct = new ArrayList<Map<String, Object>>();
				paramObejct = ((ArrayList<?>) apiRequestObject).get(0);
			} else if (apiRequestObject instanceof ParamMap) {
				paramObejct = new HashMap<String, Object>();
				if(((ParamMap) apiRequestObject).get("body") == null) {
					paramObejct = ((ParamMap) apiRequestObject).get();	
				}else {
					paramObejct = ((ParamMap) apiRequestObject).get("body");
				}
			} else if (apiRequestObject instanceof HashMap) {
				paramObejct = new HashMap<String, Object>();
				paramObejct = apiRequestObject;

			} else if (packageName.startsWith("com.cware.netshopping.paqeen.domain")) {
				paramObejct = objectMapper.convertValue(apiRequestObject, Map.class);
			}
			else {
				log.error("apiRequestObject is not suitable.");
			}
			
			body = objectMapper.writeValueAsString(paramObejct);
						
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return body;
	}
	
	/* 퀸잇 통신 호출 및 결과 수신 */
	public String connectPaQeenAPILegacy(ParamMap apiBaseInfo) throws Exception {
		HttpHeaders headers = null;
		HttpEntity<String> entity = null;
		HttpMethod method = null;
		HttpComponentsClientHttpRequestFactory factory = null;
		RestTemplate restTemplate = null;
		ResponseEntity<String> response = null;
		StopWatch watch = null;
		String url = null;
		String connectResult = null;
		String authorization = null;
		try {
	          
			authorization = "Bearer " + createAccessToken(apiBaseInfo.get("paCode").toString());
			
			headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			headers.set("Authorization", authorization);
			headers.set("Seller-Platform", "SKSTOA");
			
			entity = new HttpEntity<>(apiBaseInfo.getString("body"), headers);
			apiBaseInfo.put("reqHeader",entity.getHeaders().toString());
			
			if(apiBaseInfo.get("getParameter") != null) {
				url = apiBaseInfo.getString("url") + apiBaseInfo.get("getParameter").toString();	
			}else {
				url = apiBaseInfo.getString("url");
			}
			if(apiBaseInfo.get("urlParameter") != null) {
				url = url.replace("{urlParameter}",apiBaseInfo.get("urlParameter").toString());
			} 
			
			apiBaseInfo.put("url", url);
			
			url = QEEN_GATEWAY + url;
			
			method = HttpMethod.valueOf(apiBaseInfo.getString("method"));
			
			factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout(CONN_TIMEOUT * 1000); //타임아웃 설정 100초
			factory.setReadTimeout(CONN_TIMEOUT_120 * 1000); //타임아웃 설정 120초
			
			restTemplate = new RestTemplate(factory);
			
			watch = new StopWatch();
			watch.start(); // API 호출 및 응답 수행시간 측정 Start
			response = restTemplate.exchange(url, method, entity, String.class);
			watch.stop(); // API 호출 및 응답 수행시간 측정 End
			apiBaseInfo.put("resultCode", response.getStatusCode());
			connectResult = response.getBody();
			
		} catch (RestClientResponseException ex){
			connectResult = ex.getResponseBodyAsString();
			String stringResult = StringUtil.jsonToMap(connectResult).toString();
			log.info("API RESPONSE: {}", stringResult);
            stringResult = "QEEN(API-RESPONSE) : " + stringResult;
            apiBaseInfo.put("resultCode", ex.getRawStatusCode());
            throw new TransApiException(stringResult, String.valueOf(ex.getRawStatusCode()));
		} catch (Exception e) {
			log.error("API ERROR: {}", getErrorMessage(e), e);
			connectResult = getErrorMessage(e);
			throw new TransApiException(getErrorMessage(e), "Exception");
		} finally {
			apiBaseInfo.put("result"		 , StringUtil.jsonToMap(connectResult).toString());
			apiBaseInfo.put("responseTime", String.format("%.6f", watch.getTotalTimeSeconds())); //API 수행시간
			saveRequestMap(apiBaseInfo); // TPAREQUESTMAP_LOG 저장
		}
		
		return connectResult;
	}

	/* 퀸잇 통신 호출 및 결과 수신 */
	public String connectPaQeenAPI(ParamMap apiBaseInfo, PaTransService serviceLog, ParamMap apiRequestObject) throws Exception {
		String url = "";
//		if(apiRequestObject.get("urlParameter") != null) {
//			//apiBaseInfo.getString("url").replace("{urlParameter}", apiRequestObject.get("urlParameter").toString());	
//			apiBaseInfo.put("url", apiBaseInfo.getString("url").replace("{urlParameter}", apiRequestObject.get("urlParameter").toString()));
//		} 
		
		if(apiRequestObject.get("getParameter") != null) {
			url = QEEN_GATEWAY + apiBaseInfo.getString("url") + apiRequestObject.get("getParameter").toString();	
		}else {
			url = QEEN_GATEWAY + apiBaseInfo.getString("url");
		}
		if(apiRequestObject.get("urlParameter") != null) {
			url = url.replace("{urlParameter}",apiRequestObject.get("urlParameter").toString());
		} 
		
		
		// URI 세팅 (Url + Params) and pathParameters replace 처리
		
		HttpMethod method = HttpMethod.valueOf(apiBaseInfo.getString("method"));
		String body = apiRequestObject.get("body").toString();
		String connectResult = null;
		String authorization = null;
		// API 로그 생성
		PaTransApi apiLog = new PaTransApi();		
		apiLog.setTransCode(serviceLog.getTransCode());
		apiLog.setTransType(serviceLog.getTransType());
		apiLog.setApiName(serviceLog.getServiceName());
		apiLog.setApiNote(serviceLog.getServiceNote());
		apiLog.setTransServiceNo(serviceLog.getTransServiceNo());
		apiLog.setPaGroupCode(serviceLog.getPaGroupCode());
		apiLog.setProcessId(PaGroup.QEEN.processId());	
		apiLog.setApiUrl(method.name()+ " " + url);
		apiLog.setTransApiNo(logMapper.createTransApiNo());
		
		try {
			authorization = "Bearer " + createAccessToken(apiBaseInfo.get("paCode").toString());
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			headers.set("Authorization", authorization);
			headers.set("Seller-Platform", "SKSTOA");
			
			HttpEntity<String> entity = new HttpEntity<>(body, headers);
			transLogService.logTransApiReqQeen(apiLog, entity);
			
			log.info("QEEN API URL : [" + String.valueOf(method) +"] " + url );
			log.info("QEEN header : " + entity.getHeaders());
			log.info("QEEN body : " + entity.getBody());
			
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout(CONN_TIMEOUT * 1000); //타임아웃 설정 100초
			factory.setReadTimeout(CONN_TIMEOUT_120 * 1000); //타임아웃 설정 120초
			
			RestTemplate restTemplate = new RestTemplate(factory);
			
			StopWatch watch = new StopWatch();
			watch.start(); // API 호출 및 응답 수행시간 측정 Start
			ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
			watch.stop(); // API 호출 및 응답 수행시간 측정 End
			
			apiLog.setResponsePayload(StringUtil.jsonToMap(response.getBody()).toString());
			apiLog.setResultCode(API_SUCCESS_CODE);
			apiLog.setSuccessYn("1");
			transLogService.logTransApiRes(apiLog); 
			
			log.info("QEEN API responseTime : " + String.format("%.6f", watch.getTotalTimeSeconds())); // API 호출 및 응답 수행시간
			
			connectResult = response.getBody();
			
		} catch (RestClientResponseException ex) {
			log.error(serviceLog.getServiceNote() + " ={} {}", serviceLog.getTransCode(), ex.getResponseBodyAsString());
			transLogService.logTransApiResErrorQeen(apiLog, ex);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}		
		catch (Exception e) {
			log.error(serviceLog.getServiceNote() +" ={}", serviceLog.getTransCode(), e);
			transLogService.logTransApiResError(apiLog, e);
			throw new TransApiException(apiLog.getResultMsg(), apiLog.getResultCode());
		}
		
		return connectResult; 
	}
	
	public void getApiInfo(ParamMap apiBaseInfo) throws Exception {
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		apiBaseInfo.put("broadCode"	    , PaCode.QEEN_TV.code());
		apiBaseInfo.put("onlineCode"	, PaCode.QEEN_ONLINE.code());
		apiBaseInfo.put("siteGb"		, Constants.PA_QEEN_PROC_ID);
		apiBaseInfo.put("startDate"		, systemService.getSysdatetimeToString());
		
		apiInfo = systemService.selectPaApiInfo(apiBaseInfo);
		
		apiBaseInfo.put("method"		, apiInfo.get("REMARK1"));
		apiBaseInfo.put("url"			, apiInfo.get("API_URL"));
		
		apiBaseInfo.put("code"			, "200");
		apiBaseInfo.put("message"		, "OK");
		apiBaseInfo.put("apiName"		,  apiInfo.get("API_NAME"));
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
	
	public void saveRequestMap(ParamMap apiInfoMap) {
		
		try {
			PaRequestMap paRequestMap = new PaRequestMap();
			
			String paCode	= apiInfoMap.getString("paCode");
			if("".equals(paCode) ) paCode = Constants.PA_QEEN_GROUP_CODE; 
			
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
	
	 public String convertMapToUrl(HashMap<String, String> map) {
	        return map.entrySet()
	                .stream()
	                .map(entry -> {
	                    try {
	                    	if(entry.getKey().equals("urlParameter")) {
	                    		return "";
	                    	}
	                        String key = URLEncoder.encode(entry.getKey(), "UTF-8");
	                        String value = URLEncoder.encode(entry.getValue(), "UTF-8").replace("%2C", ",");
	                        return key + "=" + value;
	                    } catch (UnsupportedEncodingException e) {
	                        throw new RuntimeException("UTF-8 encoding is not supported", e);
	                    }
	                })
	                .collect(Collectors.joining("&"));
	}
}
