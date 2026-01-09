package com.cware.netshopping.paintp.v2.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

/**
 * 인터파크API 연동 공통(인증/헤더)
 *
 */
@Service
public class PaIntpApiRequest {

	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE = "ERROR";

	// 인터파크 API서버
	@Value("${partner.interpark.api.gateway}")
	String INTERPARK_GATEWAY;

	@Value("${partner.interpark.api.payload.host}")
	String INTERPARK_PAYLOAD_HOST;

	@Value("${partner.interpark.api.payload.path}")
	String INTERPARK_PAYLOAD_PATH;
	
	/**
	 * API요청 공통 헤더 설정
	 * 
	 * @param method
	 * @param uri
	 * @return
	 * @throws URISyntaxException
	 */
	public RequestEntity<Void> createRequest(HttpMethod method, String uri) throws URISyntaxException {
		uri = INTERPARK_GATEWAY + uri;
		return RequestEntity.method(method, new URI(uri))
					.contentType(MediaType.TEXT_XML)
			        .build();
	};

	public HttpHeaders createHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_XML);
		return headers;
	}

}
