package com.cware.netshopping.panaver.v3.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaCode;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.BCrypt;
import com.cware.netshopping.common.util.StringUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 네이버 API 연동 공통(인증/헤더)
 */
@Service
public class PaNaverV3ApiRequest {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE 	= "ERROR";
		
	// 네이버 API서버
	@Value("${partner.naver.api.gateway}")
	String NAVER_GATEWAY;

	// 애플리케이션 ID
	@Value("${partner.naver.api.clientId}")
	String NAVER_CLIENT_ID;
	
	// 애플리케이션 시크릿
	@Value("${partner.naver.api.clientSecret}")
	String NAVER_CLIENT_SECRET;
			
	/**
	 * API요청 공통 Method 세팅
	 * 
	 * @param String
	 * @return 
	 * @throws 
	 */
	public HttpMethod getMethod(String method) {
		HttpMethod returnMethod = null;
		
		switch (method) {
		case "GET":
			returnMethod = HttpMethod.GET;
			break;			
		case "PUT":
			returnMethod = HttpMethod.PUT;
			break;
		case "POST":
			returnMethod = HttpMethod.POST;
			break;
		case "DELETE":
			returnMethod = HttpMethod.DELETE;
			break;
		default:
			break;
		}
		return returnMethod;
	}
	
	/**
	 * API요청 공통 헤더 설정
	 * 
	 * @param method
	 * @param uri
	 * @param authorization
	 * @return
	 * @throws URISyntaxException
	 */
	public HttpHeaders createHeader(HttpMethod method, String authorization, String apiName) throws URISyntaxException {
				
		HttpHeaders headers 	  = new HttpHeaders();
		
		switch (method) {
		case  GET :
		case PUT:
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));		
		case POST:
			// 상품 이미지 등록 API는 Content-Type을 multipart/form-data 로 세팅
			if("IF_PANAVERAPI_V3_01_005".equals(apiName)) {
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);	
			}else {
				headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			}
		default:
			headers.set ("Authorization"	, "Bearer " + authorization);
		}
		return headers;
	};
		
	/**
	 * API요청 공통 body 세팅
	 * 
	 * @param Object
	 * @return String
	 * @throws Exception
	 */
	public String getBody(Object paramObject) throws Exception {
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
		}else if(paramObject instanceof Properties) {
			//result = makeQuryString((Properties)paramObject);
			return "";
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
		
	/**
	 * API Query Parameters 세팅
	 * 
	 * @param Object
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	public static String makeQuryString(Object params) throws Exception{
		Map<String, Object> tempMap = null;
		
		Iterator<Map.Entry<String, Object>> itertator 	= null;    
	    if(params instanceof ParamMap){
	    	tempMap = ((ParamMap) params).get();
	    }else if(params instanceof HashMap<?, ?>) {
	    	tempMap = (Map<String, Object>)params;
	    	//itertator = ((HashMap<?,?>)params).keySet().iterator();
	    }
	    
	    itertator = tempMap.entrySet().iterator();
		StringBuffer sb    = new StringBuffer();

		if(itertator.hasNext()) sb.append("?");

		while (itertator.hasNext() ) {
			Map.Entry<String,Object> entry = (Map.Entry<String,Object>) itertator.next();
			sb.append(entry.getKey() + "=" + entry.getValue());			
			if (itertator.hasNext()) sb.append("&");
		}
		return sb.toString();
	}
		
	/**
	 * API 정보조회
	 * 
	 * @param PaTransService
	 * @return
	 * @throws Exception 
	 */	
	public ParamMap getApiInfo(String apiCode) throws Exception {
		
		ParamMap apiInfoMap = new ParamMap();		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		apiInfoMap.put("apiCode"		, apiCode);
		apiInfoMap.put("broadCode"	, PaCode.NAVER.code());
		apiInfoMap.put("onlineCode"	, PaCode.NAVER.code());
		apiInfoMap.put("siteGb"		, Constants.PA_CM_NAVER_PROC_ID);
		apiInfoMap.put("startDate"		, systemService.getSysdatetimeToString());
		
		apiInfo = systemService.selectPaApiInfo(apiInfoMap);
		
		apiInfoMap.put("method"		, apiInfo.get("REMARK1"));
		apiInfoMap.put("url"			, apiInfo.get("API_URL"));
		apiInfoMap.put("paCode"		, PaCode.NAVER.code());
		apiInfoMap.put("code"			, "");
		apiInfoMap.put("message"		, "");
		apiInfoMap.put("apiName"		,  apiInfo.get("API_NAME"));
		
		return apiInfoMap;
	}
		
	/**
	 * API 토큰생성 
	 * 	
	 */
	public String generateToken() throws URISyntaxException{
		
		String token = "";
		
		try {			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -1); // 네이버 기준 시간과 맞지않아 -1초 세팅하여 통신
			Long timestamp = cal.getTimeInMillis();
			String uri = "https://api.commerce.naver.com" + "/external/v1/oauth2/token";
			
			// HEADER 세팅
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			
			// BODY 세팅(APPLICATION_FORM_URLENCODED 형태)
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("client_id", NAVER_CLIENT_ID);
			body.add("timestamp", Long.toString(timestamp));
			body.add("client_secret_sign", generateSignature(NAVER_CLIENT_ID, NAVER_CLIENT_SECRET, timestamp));
			body.add("type", "SELF");
			
			HttpEntity<?> httpEntity = new HttpEntity<>(body, httpHeaders);
			
			// 통신
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
			
			// 결과값 Map<String, Object> 변환
			Map<String, Object> resultMap = StringUtil.jsonToMap(response.getBody());
			
			token = (String) resultMap.get("access_token");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());		
		}
		
		return token;		
	}

	
	/**
	 * 전자서명 생성(토큰생성용)
	 * 	
	 */
	public String generateSignature(String clientId, String clientSecret, Long timestamp) {
		// 밑줄로 연결하여 password 생성
		String password =clientId +"_"+ timestamp;
		// bcrypt 해싱
		String hashedPw = BCrypt.hashpw(password, clientSecret);
		// base64 인코딩
		return Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
	}

}
