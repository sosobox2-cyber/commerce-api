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
import com.cware.partner.coupang.batch.PriceProductBatch;
import com.cware.partner.coupang.batch.ResumeSaleProductBatch;
import com.cware.partner.coupang.batch.StatusProductBatch;
import com.cware.partner.coupang.batch.StockProductBatch;
import com.cware.partner.coupang.batch.StopSaleProductBatch;
import com.cware.partner.coupang.batch.UpdateProductBatch;

import lombok.extern.slf4j.Slf4j;


/**
 *  쿠팡 상품 변경 스케줄러
 *
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "partner.coupang.product", name = "schedule", havingValue = "on")
public class ProductTransScheduler {

	@Autowired
	private UpdateProductBatch updateProductBatch;

	@Autowired
	private StatusProductBatch statusProductBatch;

	@Autowired
	private PriceProductBatch priceProductBatch;

	@Autowired
	private StopSaleProductBatch stopSaleProductBatch;

	@Autowired
	private ResumeSaleProductBatch resumeSaleProductBatch;

	@Autowired
	private StockProductBatch stockProductBatch;
	
	@Autowired
	private PaWithoutHourRepository paWithoutHourRepository;

	// 배치실행 제외 시간
	@Value("${partner.coupang.product.without.schedule.hour}")
	List<Integer> WITHOUT_HOURS;

	@Scheduled(fixedDelayString = "${partner.coupang.product.update.schedule.delay}", initialDelayString = "${partner.coupang.product.update.schedule.initial}")
	public void excecuteUpdateProduct() {
		
		if (exceptSchedule()) return;
		
		log.info("=====UpdateProduct Schedule Start=====");

		// 상품수정 배치
		updateProductBatch.updateProducts();

		log.info("=====UpdateProduct Schedule End=====");
	}

	@Scheduled(fixedDelayString = "${partner.coupang.product.status.schedule.delay}", initialDelayString = "${partner.coupang.product.status.schedule.initial}")
	public void excecuteStatusProduct() {
		
		if (exceptSchedule()) return;
		
		log.info("=====StatusProduct Schedule Start=====");

		// 상품상태업데이트 배치
		statusProductBatch.updateStatusProducts();
		// 옵션매핑 배치
		statusProductBatch.updateOptionProducts();

		log.info("=====StatusProduct Schedule End=====");
	}

	@Scheduled(fixedDelayString = "${partner.coupang.product.price.schedule.delay}", initialDelayString = "${partner.coupang.product.price.schedule.initial}")
	public void excecutePriceProduct() {
		
		if (exceptSchedule()) return;
		
		log.info("=====PriceProduct Schedule Start=====");

		// 가격변경 배치
		priceProductBatch.updatePriceProducts();

		log.info("=====PriceProduct Schedule End=====");
	}

	@Scheduled(fixedDelayString = "${partner.coupang.product.stop.schedule.delay}", initialDelayString = "${partner.coupang.product.stop.schedule.initial}")
	public void excecuteStopProduct() {
		
		if (exceptSchedule()) return;
		
		log.info("=====StopProduct Schedule Start=====");

		// 판매중지 배치
		stopSaleProductBatch.stopSaleProducts();

		log.info("=====StopProduct Schedule End=====");
	}

	@Scheduled(fixedDelayString = "${partner.coupang.product.resume.schedule.delay}", initialDelayString = "${partner.coupang.product.resume.schedule.initial}")
	public void excecuteResumeProduct() {
		
		if (exceptSchedule()) return;
		
		log.info("=====ResumeProduct Schedule Start=====");

		// 판매재개 배치
		resumeSaleProductBatch.resumeSaleProducts();

		log.info("=====ResumeProduct Schedule End=====");
	}

	@Scheduled(fixedDelayString = "${partner.coupang.product.stock.schedule.delay}", initialDelayString = "${partner.coupang.product.stock.schedule.initial}")
	public void excecuteStockProduct() {
		
		if (exceptSchedule()) return;
		
		log.info("=====StockProduct Schedule Start=====");

		// 재고변경 배치
		stockProductBatch.updateStockProducts();

		log.info("=====StockProduct Schedule End=====");
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
