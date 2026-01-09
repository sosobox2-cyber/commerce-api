package com.cware.partner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PartnerCdcApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartnerCdcApplication.class, args);
	}

}
