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
import com.cware.partner.coupang.batch.RegisterOutboundBatch;
import com.cware.partner.coupang.batch.UpdateOutboundBatch;

import lombok.extern.slf4j.Slf4j;


/**
 *  쿠팡 판매자정보 전송 스케줄러
 *
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "partner.coupang.seller", name = "schedule", havingValue = "on")
public class SellerTransScheduler {


	@Autowired
	private RegisterOutboundBatch registerOutboundBatch;

	@Autowired
	private UpdateOutboundBatch updateOutboundBatch;
	
	@Autowired
	private PaWithoutHourRepository paWithoutHourRepository;

	// 배치실행 제외 시간
	@Value("${partner.coupang.product.without.schedule.hour}")
	List<Integer> WITHOUT_HOURS;

	@Scheduled(fixedDelayString = "${partner.coupang.seller.outbound.register.schedule.delay}", initialDelayString = "${partner.coupang.seller.outbound.register.schedule.initial}")
	public void excecuteRegisterOutbound() {
		
		if (exceptSchedule()) return;
		
		log.info("=====RegisterOutbound Schedule Start=====");

		// 출고지등록 배치
		registerOutboundBatch.registerOutbound();

		log.info("=====RegisterOutbound Schedule End=====");
	}

	@Scheduled(fixedDelayString = "${partner.coupang.seller.outbound.update.schedule.delay}", initialDelayString = "${partner.coupang.seller.outbound.update.schedule.initial}")
	public void excecuteUpdateOutbound() {
		
		if (exceptSchedule()) return;
		
		log.info("=====UpdateOutbound Schedule Start=====");

		// 출고지수정 배치
		updateOutboundBatch.updateOutbound();

		log.info("=====UpdateOutbound Schedule End=====");
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
