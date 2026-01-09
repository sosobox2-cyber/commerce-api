package com.cware.partner.common.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Value("${partner.coupang.api.read-timeout}")
	int READ_TIMEOUT;

	@Value("${partner.coupang.api.connect-timeout}")
	int CONNECT_TIMEOUT;

	@Value("${partner.coupang.api.max-conn-total}")
	int MAX_CONN_TOTAL;

	@Value("${partner.coupang.api.max-conn-route}")
	int MAX_CONN_ROUTE;

	@Bean
	public RestTemplate restTemplate() {

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(READ_TIMEOUT);
		factory.setConnectTimeout(CONNECT_TIMEOUT);

		HttpClient httpClient = HttpClientBuilder.create()
				.setMaxConnTotal(MAX_CONN_TOTAL)
				.setMaxConnPerRoute(MAX_CONN_ROUTE).build(); // per host

		factory.setHttpClient(httpClient);

		RestTemplate restTemplate = new RestTemplate(factory);

		return restTemplate;

	}
}
