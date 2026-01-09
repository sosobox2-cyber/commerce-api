package com.cware.partner.sync.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cware.partner.sync.service.CdcGoodsSyncService;
import com.cware.partner.sync.service.EntpAddrSyncHalfService;
import com.cware.partner.sync.service.EntpAddrSyncQeenService;
import com.cware.partner.sync.service.EntpAddrSyncService;
import com.cware.partner.sync.service.EntpAddrSyncWempService;

import lombok.extern.slf4j.Slf4j;


/**
 * 상품동기화 스케줄러
 *
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "partner.sync", name = "schedule", havingValue = "on")
public class ProductSyncScheduler {


	@Autowired
	private EntpAddrSyncService entpAddrSyncService;

	@Autowired
	private EntpAddrSyncHalfService entpAddrSyncHalfService;

	@Autowired
	private EntpAddrSyncWempService entpAddrWempSyncService;
	
//	@Autowired
//	private EntpAddrSyncFapleService entpAddrSyncFapleService;
	
	@Autowired
	private EntpAddrSyncQeenService entpAddrSyncQeenService;

	@Autowired
	private CdcGoodsSyncService cdcGoodsSyncService;

	@Scheduled(fixedDelayString = "${partner.sync.schedule.delay}", initialDelayString = "${partner.sync.schedule.initial}")
	public void excecute() {
		log.info("ProductSyncScheduler Start=====");

		// 업체주소동기화
		entpAddrSyncService.executeEntpAddressSync();
		entpAddrSyncHalfService.executeEntpAddressSync();
		entpAddrWempSyncService.executeEntpAddressSync();
		//entpAddrSyncFapleService.executeEntpAddressSync();
		entpAddrSyncQeenService.executeEntpAddressSync();

		// 상품동기화
		cdcGoodsSyncService.executeProductSync();

		log.info("ProductSyncScheduler End=====");
	}

}
