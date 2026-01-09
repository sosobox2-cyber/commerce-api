package com.cware.netshopping.patmon.v2.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cware.netshopping.common.code.PaCode;
import com.cware.netshopping.patmon.v2.domain.Authorization;

/**
 * 티몬 API 연동 공통(인증/헤더)
 *
 */
@Service
public class PaTmonApiRequest {

	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE = "ERROR";

	// 티몬 API서버
	@Value("${partner.tmon.api.gateway}")
	String TMON_GATEWAY;

	@Value("${partner.tmon.api.secret.key}")
	String SECRET_KEY;

	// 방송상품 계정
	@Value("${partner.tmon.api.tv.partner.id}")
	String TV_PARTNER_ID;
	@Value("${partner.tmon.api.tv.partner.token}")
	String TV_PARTNER_TOKEN;

	// 모바일상품 계정
	@Value("${partner.tmon.api.online.partner.id}")
	String ONLINE_PARTNER_ID;
	@Value("${partner.tmon.api.online.partner.token}")
	String ONLINE_PARTNER_TOKEN;

	@Autowired
	RestTemplate restTemplate;
	
	/**
	 * API요청 공통 헤더 설정
	 * 
	 * @param method
	 * @param uri
	 * @param paCode
	 * @param authorization
	 * @return
	 * @throws URISyntaxException
	 */
	public RequestEntity<Void> createRequest(HttpMethod method, String uri, String paCode, String authorization) throws URISyntaxException {

		String partnerId;
		String partnerToken;

		if (PaCode.TMON_TV.code().equals(paCode)) {
			partnerId = TV_PARTNER_ID;
			partnerToken = TV_PARTNER_TOKEN;
		} else {
			partnerId = ONLINE_PARTNER_ID;
			partnerToken = ONLINE_PARTNER_TOKEN;
		}
		
		uri = TMON_GATEWAY + uri;
		return RequestEntity.method(method, new URI(uri))
					.contentType(MediaType.APPLICATION_JSON)
			        .header("Authorization", "Bearer " + authorization)
			        .header("X-Partner-Id", partnerId)
			        .header("X-Partner-Token", partnerToken)
			        .build();
	};

	public HttpHeaders createHttpHeaders(String paCode, String authorization) {
		String partnerId;
		String partnerToken;

		if (PaCode.TMON_TV.code().equals(paCode)) {
			partnerId = TV_PARTNER_ID;
			partnerToken = TV_PARTNER_TOKEN;
		} else {
			partnerId = ONLINE_PARTNER_ID;
			partnerToken = ONLINE_PARTNER_TOKEN;
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + authorization);
		headers.add ("X-Partner-Id", partnerId);
		headers.add ("X-Partner-Token", partnerToken);
		return headers;
	}

	public Authorization generateToken() throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Basic " + SECRET_KEY);

		String uri = TMON_GATEWAY + "/oauth/token?grant_type=client_credentials";

		RequestEntity<Void> requestEntity = RequestEntity.method(HttpMethod.POST, new URI(uri))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic " + SECRET_KEY)
				.build();

		ResponseEntity<Authorization> response = restTemplate.exchange(requestEntity, Authorization.class);
		return response.getBody();
	}


}
