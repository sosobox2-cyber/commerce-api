package com.cware.partner.cdc.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cware.partner.cdc.service.ProductCdcService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "partner.cdc.schedule", name = "schedule", havingValue = "on")
public class ProductCdcScheduler {

	@Autowired
	private ProductCdcService productCdcService;

	@Scheduled(fixedDelayString = "${partner.cdc.schedule.delay}", initialDelayString = "${partner.cdc.schedule.initial}")
	public void excecute() {
		log.info("ProductCdcScheduler Start=====");
		productCdcService.executeProductCdc();
		log.info("ProductCdcScheduler End=====");
	}

}
