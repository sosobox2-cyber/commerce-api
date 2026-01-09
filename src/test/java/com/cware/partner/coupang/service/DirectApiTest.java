package com.cware.partner.coupang.service;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.cware.partner.common.util.StringUtil;
import com.cware.partner.coupang.domain.ApiResponseMsg;

@SpringBootTest
class DirectApiTest {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApiRequest apiRequest;

	@Test
	void testGoodsInfo() throws RestClientException, URISyntaxException {
		String PATH = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/{sellerProductId}";

		UriTemplate template = new UriTemplate(PATH);
		Map<String, String> params = new HashMap<String, String>();
		params.put("sellerProductId", "11401524305");

		String uri = template.expand(params).toString();
		System.out.println(uri);

		HttpMethod method = HttpMethod.GET;
		String authorization = apiRequest.generateSignature(method.name(), uri, "52");
		System.out.println(authorization);

		ResponseEntity<ApiResponseMsg> response = restTemplate.exchange(apiRequest.createRequest(method, uri, authorization),
				ApiResponseMsg.class);

		ApiResponseMsg result = response.getBody();
		System.out.println(result.getCode());
		System.out.println(result.getMessage());

		System.out.println(result.getData().get("statusName"));

	}

	@Test
	void testGoodsHistory() throws RestClientException, URISyntaxException {
		String PATH = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/{sellerProductId}/histories";

		UriTemplate template = new UriTemplate(PATH);
		Map<String, String> params = new HashMap<String, String>();
		params.put("sellerProductId", "13170607453");

		String uri = template.expand(params).toString() + "?maxPerPage=1";
		System.out.println(uri);

		HttpMethod method = HttpMethod.GET;
		String authorization = apiRequest.generateSignature(method.name(), uri, "52");
		System.out.println(authorization);

		try {
			ResponseEntity<String> response = restTemplate
					.exchange(apiRequest.createRequest(method, uri, authorization), String.class);

			System.out.println(response.getBody());
//			ProductStatusHistory result = response.getBody();
//			System.out.println(result.getCode());
//			System.out.println(result.getMessage());
//
//			ProductStatus data =  result.getData()[0];
//			String comment = data.getComment();
//
//			System.out.println(comment.split(",")[0]);
//			System.out.println(comment.split(",")[0].split(":")[1]);
		} catch (HttpClientErrorException ex) {

			System.out.println(ex.getMessage());
			System.out.println(ex.getResponseBodyAsString());
			System.out.println(StringUtil.jsonToMap(ex.getResponseBodyAsString()));

		}

	}
}
