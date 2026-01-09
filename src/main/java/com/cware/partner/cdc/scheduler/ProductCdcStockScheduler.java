package com.cware.partner.cdc.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cware.partner.cdc.service.ProductCdcService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "partner.cdc.schedule.stock", name = "schedule", havingValue = "on")
public class ProductCdcStockScheduler {

	@Autowired
	private ProductCdcService productCdcService;
	
	// 재고 체크 CDC 캡쳐 별도 처리
	@Scheduled(fixedDelayString = "${partner.cdc.schedule.stock.delay}", initialDelayString = "${partner.cdc.schedule.stock.initial}")
	public void excecuteStock() {
		log.info("ProductCdcScheduler(Stock) Start=====");
		productCdcService.executeStockCdc();
		log.info("ProductCdcScheduler(Stock) End=====");
	}


}
