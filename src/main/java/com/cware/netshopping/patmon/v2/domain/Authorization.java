package com.cware.netshopping.patmon.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Authorization {

	private String accessToken; // 접근 토큰 값

	private String tokenType; // 접근 토큰 타입

	private Integer expiresIn; // 토큰 유효시간 (초)

	private String scope; // 스코프 타입
	private String vendorId;

	@JsonProperty("access_token")
	public String getAccessToken() {
		return accessToken;
	}

	@JsonProperty("access_token")
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@JsonProperty("token_type")
	public String getTokenType() {
		return tokenType;
	}

	@JsonProperty("token_type")
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@JsonProperty("expires_in")
	public Integer getExpiresIn() {
		return expiresIn;
	}

	@JsonProperty("expires_in")
	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	@Override
	public String toString() {
		return "Authorization [accessToken=" + accessToken + ", tokenType=" + tokenType + ", expiresIn=" + expiresIn
				+ ", scope=" + scope + ", vendorId=" + vendorId + "]";
	}

}
