package com.cware.partner.coupang.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import com.cware.partner.common.code.PaCode;

@Service
public class ApiRequest {

	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE = "ERROR";

	// 쿠팡 API서버
	@Value("${partner.coupang.api.gateway}")
	String COUPANG_GATEWAY;

	// 방송상품 계정
	@Value("${partner.coupang.api.tv.access.key}")
	String TV_ACCESS_KEY;
	@Value("${partner.coupang.api.tv.secret.key}")
	String TV_SECRET_KEY;

	// 모바일상품 계정
	@Value("${partner.coupang.api.online.access.key}")
	String ONLINE_ACCESS_KEY;
	@Value("${partner.coupang.api.online.secret.key}")
	String ONLINE_SECRET_KEY;


	public RequestEntity<Void> createRequest(HttpMethod method, String uri, String authorization)  throws URISyntaxException {
		uri = COUPANG_GATEWAY + uri;
		return RequestEntity.method(method,  new URI(uri))
        .contentType(MediaType.APPLICATION_JSON)
        .headers(createHttpHeaders(authorization))
        .build();
	};

	public HttpHeaders createHttpHeaders(String authorization) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", authorization);
		headers.add("X-EXTENDED-TIMEOUT", "30000"); // 쿠팡 API gateway의 타임아웃은 기본 3초임. 30초로 설정
		return headers;
	}

	/**
	 * HMAC Signature 생성
	 *
	 * @param method    HTTP메서드
	 * @param uri       URI PATH
	 * @param paCode    제휴사코드
	 * @return
	 */
	public String generateSignature(String method, String uri, String paCode) {
		String secretKey, accessKey;

		if (PaCode.COUPANG_TV.code().equals(paCode)) {
			secretKey = TV_SECRET_KEY;
			accessKey = TV_ACCESS_KEY;
		} else {
			secretKey = ONLINE_SECRET_KEY;
			accessKey = ONLINE_ACCESS_KEY;
		}

		String signature, parts[] = uri.split("\\?");
		if (parts.length > 2)
			throw new RuntimeException("incorrect uri format");
		String path = parts[0];
		String query = "";
		if (parts.length == 2)
			query = parts[1];
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyMMdd'T'HHmmss'Z'");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		String datetime = dateFormatGmt.format(new Date());
		String message = datetime + method + path + query;
		try {
			SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(Charset.forName("UTF-8")), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(message.getBytes(Charset.forName("UTF-8")));
			signature = Hex.encodeHexString(rawHmac);
		} catch (GeneralSecurityException e) {
			throw new IllegalArgumentException("Unexpected error while creating hash: " + e.getMessage(), e);
		}
		String result = String.format("CEA algorithm=%s, access-key=%s, signed-date=%s, signature=%s",
				new Object[] { "HmacSHA256", accessKey, datetime, signature });
		return result;
	}

}
