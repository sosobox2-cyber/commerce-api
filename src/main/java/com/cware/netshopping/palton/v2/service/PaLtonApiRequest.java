package com.cware.netshopping.palton.v2.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

/**
 * 롯데온 API 연동 공통(인증/헤더)
 *
 */
@Service
public class PaLtonApiRequest {

	public static final String API_SUCCESS_CODE = "0000";
	public static final String API_ERROR_CODE = "9999";
	
	public static final String API_SUCCESS_MSG = "정상 처리되었습니다.";

	// 롯데온 API서버
	@Value("${partner.lotteon.api.gateway}")
	String LOTTEON_GATEWAY;

	// API인증키
	@Value("${partner.lotteon.api.access.key}")
	String ACCESS_KEY;
	
	/**
	 * API요청 공통 헤더 설정
	 * 
	 * @param method
	 * @param uri
	 * @return
	 * @throws URISyntaxException
	 */
	public RequestEntity<Void> createRequest(HttpMethod method, String uri) throws URISyntaxException {
		uri = LOTTEON_GATEWAY + uri;
		return RequestEntity.method(method, new URI(uri))
					.contentType(MediaType.APPLICATION_JSON)
			        .header("Authorization", "Bearer " + ACCESS_KEY)
			        .header("Accept-Language", Locale.KOREA.toString())
			        .header("X-Timezone", TimeZone.getDefault().getID())
			        .header("Cache-Control", "no-cache")
			        .build();
	};

	public HttpHeaders createHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + ACCESS_KEY);
		headers.add("Accept-Language", Locale.KOREA.toString());
		headers.add("X-Timezone", TimeZone.getDefault().getID());
		headers.setCacheControl("no-cache");
		return headers;
	}

}
