package com.cware.netshopping.pagmkt.v2.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cware.netshopping.common.code.PaCode;

/**
 * 이베이API 연동 공통(인증/헤더)
 *
 */
@Service
public class PaEbayApiRequest {

	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE = "ERROR";

	// 이베이 API서버
	@Value("${partner.ebay.api.gateway}")
	String EBAY_GATEWAY;

	// 방송상품 계정ID
	@Value("${partner.ebay.api.tv.vendor.user.id.gmarket}")
	String TV_GMARKET_ID;
	@Value("${partner.ebay.api.tv.vendor.user.id.auction}")
	String TV_AUCTION_ID;

	// 모바일상품 계정ID
	@Value("${partner.ebay.api.online.vendor.user.id.gmarket}")
	String ONLINE_GMARKET_ID;
	@Value("${partner.ebay.api.online.vendor.user.id.auction}")
	String ONLINE_AUCTION_ID;

	// 이베이 JWT Secret Key
	@Value("${partner.ebay.api.secret.key}")
	String SECRET_KEY;
	
	/**
	 * API요청 공통 헤더 설정
	 * 
	 * @param method
	 * @param uri
	 * @param authorization
	 * @return
	 * @throws URISyntaxException
	 */
	public RequestEntity<Void> createRequest(HttpMethod method, String uri, String authorization) throws URISyntaxException {
		uri = EBAY_GATEWAY + uri;
		return RequestEntity.method(method, new URI(uri))
					.contentType(MediaType.APPLICATION_JSON)
			        .header("Authorization", "Bearer " + authorization)
			        .build();
	};

	public HttpHeaders createHttpHeaders(String authorization) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + authorization);
		return headers;
	}
	
	/**
	 * HMAC JWT 생성
	 *
	 * @param method    HTTP메서드
	 * @param uri       URI PATH
	 * @param paCode    제휴사코드
	 * @param paGroupCode   제휴사그룹코드
	 * @return
	 */
	public String generateToken(String method, String uri, String paCode, String paGroupCode) {

		String siteId = "";

		switch (paGroupCode) {
		case "02":
			siteId = PaCode.EBAY_TV.code().equals(paCode) ? TV_GMARKET_ID : ONLINE_GMARKET_ID;
			break;
		case "03":
			siteId = PaCode.EBAY_TV.code().equals(paCode) ? TV_AUCTION_ID : ONLINE_AUCTION_ID;
			break;
		default:
			siteId = PaCode.EBAY_TV.code().equals(paCode) ? TV_GMARKET_ID + "," + TV_AUCTION_ID
					: ONLINE_GMARKET_ID + "," + ONLINE_AUCTION_ID;
			break;
		}

		HashMap<String, Object> header = new HashMap<>();
		header.put("alg", "HS256");
		header.put("typ", "JWT");
		header.put("kid", "skstoa");

		String token = JWT.create()
				.withHeader(header)
				.withIssuer("www.skstoa.com")
				.withIssuedAt(Date.from(Instant.now()))
				.withSubject("sell") //sell만 사용, ad는 광고 계정
				.withAudience("sa.esmplus.com")
				.withClaim("ssi", siteId)
				.sign(Algorithm.HMAC256(SECRET_KEY));

		return token;

	}

}
