package com.cware.netshopping.common.config;

import java.nio.charset.StandardCharsets;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource({
	"classpath:/com/cware/properties/resource.properties", 
	"classpath:/com/cware/properties/service-api.properties"
})
public class RestTemplateConfig {

	@Value("${partner.api.read-timeout}")
	int READ_TIMEOUT;
	
	@Value("${partner.api.connect-timeout}")
	int CONNECT_TIMEOUT;
	
	@Value("${partner.api.max-conn-total}")
	int MAX_CONN_TOTAL;
	
	@Value("${partner.api.max-conn-route}")
	int MAX_CONN_ROUTE;
	
	@Bean
	public RestTemplate restTemplate() {
		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
		 
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(READ_TIMEOUT);
		factory.setConnectTimeout(CONNECT_TIMEOUT);

		HttpClient httpClient = HttpClientBuilder.create()
				.setMaxConnTotal(MAX_CONN_TOTAL)
				.setMaxConnPerRoute(MAX_CONN_ROUTE)
                .setDefaultRequestConfig(requestConfig)
				.build(); 

		factory.setHttpClient(httpClient);

		RestTemplate restTemplate = new RestTemplate(factory);
		
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

		return restTemplate;

	}
}
