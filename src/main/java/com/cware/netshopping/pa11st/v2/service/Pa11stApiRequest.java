package com.cware.netshopping.pa11st.v2.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import com.cware.netshopping.common.code.PaCode;

/**
 * 11번가API 연동 공통(인증/헤더)
 *
 */
@Service
public class Pa11stApiRequest {

	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE = "ERROR";

	// 11번가 API서버
	@Value("${partner.11st.api.gateway}")
	String SK11ST_GATEWAY;

	// 방송상품 계정
	@Value("${partner.11st.api.tv.access.key}")
	String TV_ACCESS_KEY;

	// 모바일상품 계정
	@Value("${partner.11st.api.online.access.key}")
	String ONLINE_ACCESS_KEY;
	
	/**
	 * API요청 공통 헤더 설정
	 * 
	 * @param method
	 * @param uri
	 * @param paCode
	 * @return
	 * @throws URISyntaxException
	 */
	public RequestEntity<Void> createRequest(HttpMethod method, String uri, String paCode) throws URISyntaxException {
		uri = SK11ST_GATEWAY + uri;
		return RequestEntity.method(method, new URI(uri))
					.contentType(MediaType.TEXT_XML)
			        .header("openapikey", PaCode.SK11ST_TV.code().equals(paCode) ? TV_ACCESS_KEY : ONLINE_ACCESS_KEY)
			        .build();
	};

	public HttpHeaders createHttpHeaders(String paCode) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_XML);
		headers.add("openapikey", PaCode.SK11ST_TV.code().equals(paCode) ? TV_ACCESS_KEY : ONLINE_ACCESS_KEY);
		return headers;
	}

}
