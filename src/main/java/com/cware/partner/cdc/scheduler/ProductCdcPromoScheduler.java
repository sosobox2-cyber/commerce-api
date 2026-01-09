package com.cware.partner.cdc.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cware.partner.cdc.service.ProductCdcService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "partner.cdc.schedule.promo", name = "schedule", havingValue = "on")
public class ProductCdcPromoScheduler {

	@Autowired
	private ProductCdcService productCdcService;
	
	// 프로모션 CDC 캡쳐 별도 처리
	@Scheduled(fixedDelayString = "${partner.cdc.schedule.promo.delay}", initialDelayString = "${partner.cdc.schedule.promo.initial}")
	public void excecutePromo() {
		log.info("ProductCdcScheduler(Promo) Start=====");
		productCdcService.executePromoCdc();
		log.info("ProductCdcScheduler(Promo) End=====");
	}


}
