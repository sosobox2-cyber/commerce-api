package com.cware.netshopping.pawemp.v2.service;

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
 * 위메프 API 연동 공통(인증/헤더)
 *
 */
@Service
public class PaWempApiRequest {

	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE = "ERROR";

	// 위메프 API서버
	@Value("${partner.wemp.api.gateway}")
	String WEMP_GATEWAY;

	// 방송상품 계정
	@Value("${partner.wemp.api.tv.access.key}")
	String TV_ACCESS_KEY;

	// 모바일상품 계정
	@Value("${partner.wemp.api.online.access.key}")
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
		uri = WEMP_GATEWAY + uri;
		String apiKey = PaCode.WEMP_TV.code().equals(paCode) ? TV_ACCESS_KEY : ONLINE_ACCESS_KEY;
		
		return RequestEntity.method(method, new URI(uri))
					.contentType(MediaType.APPLICATION_JSON)
			        .header("apiKey", apiKey)
			        .build();
	};

	public HttpHeaders createHttpHeaders(String paCode) {

		String apiKey = PaCode.WEMP_TV.code().equals(paCode) ? TV_ACCESS_KEY : ONLINE_ACCESS_KEY;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("apiKey", apiKey);
		return headers;
	}

}
