package com.cware.partner.coupang.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.repository.PaWithoutHourRepository;
import com.cware.partner.coupang.batch.RegisterProductBatch;

import lombok.extern.slf4j.Slf4j;


/**
 *  쿠팡 상품 입점 스케줄러
 *
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "partner.coupang.product.register", name = "schedule", havingValue = "on")
public class ProductRegisterScheduler {


	@Autowired
	private RegisterProductBatch registerProductBatch;
	
	@Autowired
	private PaWithoutHourRepository paWithoutHourRepository;

	// 배치실행 제외 시간
	@Value("${partner.coupang.product.without.schedule.hour}")
	List<Integer> WITHOUT_HOURS;

	@Scheduled(fixedDelayString = "${partner.coupang.product.register.schedule.delay}", initialDelayString = "${partner.coupang.product.register.schedule.initial}")
	public void excecuteRegisterProduct() {
		
		if (exceptSchedule()) return;
		
		log.info("=====RegisterProduct Schedule Start=====");

		// 제휴입점 배치
		registerProductBatch.registerProducts();

		log.info("=====RegisterProduct Schedule End=====");
	}
	
	/**
	 * 특정시간대 스케줄 제외
	 * @return
	 */
	private boolean exceptSchedule() {
		int hour = LocalDateTime.now().getHour();

		if( paWithoutHourRepository.checkWithoutHour(PaGroup.COUPANG.code()) > 0 ) {
			return true;
		}else {			
			return WITHOUT_HOURS.contains(hour) ;
		}
		
	}
}
