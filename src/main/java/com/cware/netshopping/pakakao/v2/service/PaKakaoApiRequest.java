package com.cware.netshopping.pakakao.v2.service;

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
 * 카카오 API 연동 공통(인증/헤더)
 *
 */
@Service
public class PaKakaoApiRequest {
	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE = "ERROR";

	// 카카오 API서버
	@Value("${partner.kakao.api.gateway}")
	String KAKAO_GATEWAY;

	// 어드민
	@Value("${partner.kakao.api.admin.access.key}")
	String ADMIN_ACCESS_KEY;

	// 방송상품 계정
	@Value("${partner.kakao.api.tv.access.key}")
	String TV_ACCESS_KEY;

	// 모바일상품 계정
	@Value("${partner.kakao.api.online.access.key}")
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
		uri = KAKAO_GATEWAY + uri;
		String apiKey = PaCode.KAKAO_TV.code().equals(paCode) ? TV_ACCESS_KEY : ONLINE_ACCESS_KEY;
		
		return RequestEntity.method(method, new URI(uri))
					.contentType(MediaType.APPLICATION_JSON)
			        .header("Authorization", "KakaoAK " + ADMIN_ACCESS_KEY)
			        .header("Target-Authorization", "KakaoAK " + apiKey)
			        .header("channel-ids", "101")
			        .build();
	};

	public HttpHeaders createHttpHeaders(String paCode) {

		String apiKey = PaCode.KAKAO_TV.code().equals(paCode) ? TV_ACCESS_KEY : ONLINE_ACCESS_KEY;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "KakaoAK " + ADMIN_ACCESS_KEY);
		headers.add("Target-Authorization", "KakaoAK " + apiKey);
		headers.add("channel-ids", "101"); //톡스토어 : 101, 선물하기 : 1, 톡스토어, 선물하기 동시 호출 : 1,101
		return headers;
	}

}
