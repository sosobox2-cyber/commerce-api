package com.cware.netshopping.pahalf.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 하프클럽 API 연동 공통(인증/헤더)
 *
 */
@Service
public class PaHalfApiRequest {
	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE = "ERROR";

	// 하프클럽 API서버
	@Value("${partner.half.api.gateway}")
	String HALF_GATEWAY;
	
	// 방송상품 계정 ID
	@Value("${partner.half.api.tv.access.id}")
	String TV_ACCESS_ID;

	// 방송상품 계정 KEY
	@Value("${partner.half.api.tv.access.key}")
	String TV_ACCESS_KEY;
	
	// 모바일상품 계정 ID
	@Value("${partner.half.api.online.access.id}")
	String ONLINE_ACCESS_ID;

	// 모바일상품 계정 KEY
	@Value("${partner.half.api.online.access.key}")
	String ONLINE_ACCESS_KEY;

	/**
	 * API요청 공통 헤더 설정
	 * 
	 * @param method
	 * @param uri
	 * @param paCode
	 * @return
	 * @throws Exception
	 */
	public RequestEntity<Void> createRequest(HttpMethod method, String uri, ParamMap paramMap) throws Exception {
		uri = HALF_GATEWAY + uri;
		
		setSecKey(paramMap);
		
		if(uri.contains("postPrdCntsDtlInfo")||uri.contains("putPrdCntsDtlInfo")) { //상품 컨텐츠 상세 등록/수정
			return RequestEntity.method(method, new URI(uri))
					.header("Content-Type"	, "multipart/form-data; boundary=------")	
					.header("Cache-Control"	, "no-cache")
					.header("X-Timezone"	, TimeZone.getDefault().getID())
					.header("Accept-Language"	, TimeZone.getDefault().getID())
					.header("selAcntCd"	, paramMap.getString("idCode"))
					.header("pwd"	, paramMap.getString("secKey"))	
					.build();
		}else {
			return RequestEntity.method(method, new URI(uri))
					.contentType(new MediaType("application", "json", Charset.forName("UTF-8")))
					.header("Cache-Control"	, "no-cache")
					.header("X-Timezone"	, TimeZone.getDefault().getID())
					.header("Accept-Language"	, TimeZone.getDefault().getID())					
					.header("selAcntCd"	, paramMap.getString("idCode"))
					.header("pwd"	, paramMap.getString("secKey"))	
					.build();
		}
		
	};

	public HttpHeaders createHttpHeaders(String uri, Object apiDataMap) throws Exception {
			
		ParamMap apiDataParamMap = (ParamMap)apiDataMap;
		setSecKey(apiDataParamMap);
		HttpHeaders headers = new HttpHeaders();
		if(uri.contains("postPrdCntsDtlInfo")||uri.contains("putPrdCntsDtlInfo")) { //상품 컨텐츠 상세 등록/수정
			headers.add("Content-Type"	,"multipart/form-data; boundary=------");
		}else {
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		}
		headers.add("Cache-Control", "no-cache");
		headers.add("X-Timezone", TimeZone.getDefault().getID());
		headers.add("Accept-Language", TimeZone.getDefault().getID());
		headers.add("selAcntCd", apiDataParamMap.getString("idCode"));
		headers.add("pwd", apiDataParamMap.getString("secKey"));
		
		return headers;
	}
		
	/**
	 * API요청 공통 키값 설정
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	private void setSecKey(ParamMap paramMap) throws Exception {
		
		String idCode;
		String secKey;
		String paCode = paramMap.getString("paCode");
		
		if(Constants.PA_HALF_BROAD_CODE.equals(paCode)) {
			idCode = TV_ACCESS_ID;
			secKey = TV_ACCESS_KEY;			
		}else {
			idCode = ONLINE_ACCESS_ID;
			secKey = ONLINE_ACCESS_KEY;
		}
		
		paramMap.put("idCode", idCode);
		paramMap.put("secKey", secKey);
	}
	
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

}
