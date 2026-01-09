package com.cware.netshopping.panaver.v2.service;

import java.security.Security;
import java.security.SignatureException;
import java.util.UUID;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cware.api.panaver.product.type.AccessCredentialsType;
import com.cware.api.panaver.product.type.BaseProductRequestType;
import com.nhncorp.psinfra.toolkit.SimpleCryptLib;

/**
 * 네이버 API 연동 공통(인증/헤더)
 *
 */
@Service
public class PaNaverApiRequest {

	public static final String API_SUCCESS_CODE = "SUCCESS";
	public static final String API_ERROR_CODE = "ERROR";

	// API인증정보
	@Value("${partner.naver.api.access.license}")
	String ACCESS_LICENSE;
	@Value("${partner.naver.api.secret.key}")
	String SECRET_KEY;
	@Value("${partner.naver.api.product.version}")
	String PRODUCT_VERSION;

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * API 요청 공통 전문
	 *
	 * @param request
	 * @param serviceName
	 * @param operationName
	 * @return
	 */
	public BaseProductRequestType createRequest(BaseProductRequestType request, String serviceName,
			String operationName) {
		request.setRequestID(UUID.randomUUID().toString());
		request.setVersion(PRODUCT_VERSION);
		request.setAccessCredentials(generateCredentials(serviceName, operationName));
		return request;
	};

	/**
	 * API 인증정보 생성
	 *
	 * @param serviceName
	 * @param operationName
	 * @return
	 */
	public AccessCredentialsType generateCredentials(String serviceName, String operationName) {
		String timestamp = SimpleCryptLib.getTimestamp();
		String signature = null;
		try {
			signature = SimpleCryptLib.generateSign(timestamp + serviceName + operationName, SECRET_KEY);
		} catch (SignatureException e) {
			throw new IllegalArgumentException("Unexpected error while creating signature: " + e.getMessage(), e);
		}
		AccessCredentialsType accessCredentials = new AccessCredentialsType();
		accessCredentials.setAccessLicense(ACCESS_LICENSE);
		accessCredentials.setTimestamp(timestamp);
		accessCredentials.setSignature(signature);

		return accessCredentials;
	}

}
