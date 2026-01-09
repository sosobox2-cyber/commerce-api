package com.cware.netshopping.patdeal.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpMethod;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaCode;
import com.cware.netshopping.common.system.service.SystemService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * 티딜 API 연동 공통(인증/헤더)
 */
@Service
public class PaTdealApiRequest {
		
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE 	= "ERROR";
		
	// 티딜 API서버
	@Value("${partner.tdeal.api.gateway}")
	String TDEAL_GATEWAY;

	// Authorization Key
	@Value("${partner.tdeal.api.tv.AuthorizationKey}")
	String TDEAL_TV_AUTHORIZATION_KEY;
	
	// Authorization Key
	@Value("${partner.tdeal.api.online.AuthorizationKey}")
	String TDEAL_ONLINE_AUTHORIZATION_KEY;
				
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
	public HttpHeaders createHeader(HttpMethod method, String paCode, String apiName) throws URISyntaxException {
				
		HttpHeaders headers 	  = new HttpHeaders();

		// 토큰생성
		String authorization = ""; 
		
		if(paCode.equals(Constants.PA_TDEAL_BROAD_CODE)) {
			authorization = TDEAL_TV_AUTHORIZATION_KEY; // 방송
		}else {
			authorization = TDEAL_ONLINE_AUTHORIZATION_KEY; // 온라인 
		}
		
		switch (method) {
		case  GET :
		case PUT:	
		case POST:
		default:
			
			if("IF_PATDEALAPI_03_001".equals(apiName)) {
				headers.set("Version"	, "1.1");
			}else {
				headers.set("Version"	, "1.0");
			}
			
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			
			headers.set("Authorization"	, "Basic " + authorization);
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
			requestObejct = ((ParamMap)paramObject).get("body");			
		}else if(paramObject instanceof Properties) {
			//result = makeQuryString((Properties)paramObject);
			return "";
	    }else if(paramObject instanceof String) {
			//result = makeQuryString((Properties)paramObject);
			return paramObject.toString();
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
	    
	    if (tempMap == null) {
	    	 return "";
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
		apiInfoMap.put("broadCode"	, PaCode.TDEAL_TV.code());
		apiInfoMap.put("onlineCode"	, PaCode.TDEAL_ONLINE.code());
		apiInfoMap.put("siteGb"		, Constants.PA_TDEAL_PROC_ID);
		apiInfoMap.put("startDate"		, systemService.getSysdatetimeToString());
		
		apiInfo = systemService.selectPaApiInfo(apiInfoMap);
		
		apiInfoMap.put("method"		, apiInfo.get("REMARK1"));
		apiInfoMap.put("url"			, apiInfo.get("API_URL"));
		
		apiInfoMap.put("code"			, "");
		apiInfoMap.put("message"		, "");
		apiInfoMap.put("apiName"		,  apiInfo.get("API_NAME"));
		
		return apiInfoMap;
	}
	
}
