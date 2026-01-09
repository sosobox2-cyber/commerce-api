package com.cware.partner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySources({
	@PropertySource(value = "classpath:partner-coupang.properties"),
    @PropertySource(value = "file:${app.bin}/partner-coupang.properties", ignoreResourceNotFound = true)
})
public class PartnerCoupangApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartnerCoupangApplication.class, args);
	}

}
