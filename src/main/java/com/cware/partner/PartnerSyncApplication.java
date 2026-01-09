package com.cware.partner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PartnerSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartnerSyncApplication.class, args);
	}

}
