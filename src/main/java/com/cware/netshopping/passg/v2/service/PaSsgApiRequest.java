package com.cware.netshopping.passg.v2.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import com.cware.netshopping.common.code.PaCode;

/**
 * SSG API 연동 공통(인증/헤더)
 *
 */
@Service
public class PaSsgApiRequest {

	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE = "ERROR";

	// SSG API서버
	@Value("${partner.ssg.api.gateway}")
	String SSG_GATEWAY;

	// API인증키
	// 방송상품 계정
	@Value("${partner.ssg.api.tv.access.key}")
	String TV_ACCESS_KEY;

	// 모바일상품 계정
	@Value("${partner.ssg.api.online.access.key}")
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
		uri = SSG_GATEWAY + uri;
		String apiKey = PaCode.SSG_TV.code().equals(paCode) ? TV_ACCESS_KEY : ONLINE_ACCESS_KEY;
		
		return RequestEntity.method(method, new URI(uri))
					.accept(MediaType.APPLICATION_XML)
			        .header("Authorization", apiKey)
			        .build();
	};

	public HttpHeaders createHttpHeaders(String paCode) {

		String apiKey = PaCode.SSG_TV.code().equals(paCode) ? TV_ACCESS_KEY : ONLINE_ACCESS_KEY;

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
		headers.add("Authorization", apiKey);
		return headers;
	}

}
