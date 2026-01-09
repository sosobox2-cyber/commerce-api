package com.cware.partner.coupang.batch;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cware.partner.common.code.Application;
import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.domain.entity.PaTransBatch;
import com.cware.partner.common.repository.PaTransBatchRepository;
import com.cware.partner.common.service.CommonService;
import com.cware.partner.coupang.repository.PaCopnGoodsRepository;
import com.cware.partner.coupang.service.StockProductService;
import com.cware.partner.sync.domain.entity.PaCopnGoods;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 재고업데이트 배치
 */
@Slf4j
@Service
public class StockProductBatch {

	@Autowired
	PaCopnGoodsRepository copnGoodsRepository;

	@Autowired
	StockProductService stockProductService;

	@Autowired
	PaTransBatchRepository batchRepository;

	@Autowired
	CommonService commonService;

	// 페이징 사이즈
	@Value("${partner.coupang.page-size}")
	int PAGE_SIZE;

	/**
	 * 재고변경
	 *
	 */
	public void updateStockProducts() {

		int targetCnt = 0;
		int procCnt = 0;
		int filterCnt = 0;
		int failCnt = 0;
		int successCnt = 0;

		Slice<PaCopnGoods> slice = null;

		Pageable pageable = PageRequest.of(0, PAGE_SIZE,
				Sort.by("paCode").and(Sort.by("lastSyncDate")).and(Sort.by("goodsCode")));

		Timestamp startDate = commonService.currentDate();

		PaTransBatch transBatch = new PaTransBatch();
		transBatch.setPaGroupCode(PaGroup.COUPANG.code());
		transBatch.setBatchName(Thread.currentThread().getStackTrace()[1].getMethodName());
		transBatch.setBatchNote("쿠팡-재고변경배치");
		transBatch.setStartDate(startDate);
		transBatch.setProcessId(Application.ID.code());
		batchRepository.save(transBatch);

		log.info("쿠팡 재고변경 타겟팅 배치번호: {}", transBatch.getTransBatchNo());

		while (true) {

			// 재고변경건
			slice = copnGoodsRepository.findUpdateStockTargetList(pageable);

			List<PaCopnGoods> targetList = slice.getContent();

			log.info("타겟팅 추출:{}", targetList.size());

			targetCnt = targetCnt + targetList.size();

			transBatch.setTargetCnt(targetCnt);
			batchRepository.save(transBatch);

			List<CompletableFuture<Integer>> futures = new ArrayList<>();

			for (PaCopnGoods copnGoods : targetList) {
				log.info("No.{} {}", ++procCnt, copnGoods.getGoodsCode());
				//  재고변경
				futures.add(stockProductService.asyncUpdateStockProduct(copnGoods.getGoodsCode(), copnGoods.getPaCode(),
						Application.ID.code(), transBatch.getTransBatchNo()));
			}

			Integer result;
			for(CompletableFuture<Integer> future : futures) {
				try {
					result = future.get();

					if (result == 0) {
						filterCnt++;
					} else if(result > 0) {
						successCnt++;
					} else {
						failCnt++;
					}
				} catch (Exception e) {
					log.error("재고변경 오류: ", e);
					failCnt++;
				}
			}

			transBatch.setProcCnt(procCnt);
			transBatch.setFilterCnt(filterCnt);
			transBatch.setSuccessCnt(successCnt);
			transBatch.setFailCnt(failCnt);
			batchRepository.save(transBatch);

			if (!slice.hasNext())
				break;

		}
		Timestamp endDate = commonService.currentDate();

		transBatch.setEndDate(endDate);
		batchRepository.save(transBatch);

		log.info("쿠팡 재고변경 완료({}) 타겟:{} 성공:{} 필터:{} 실패:{} End ==> {}s elapsed.", transBatch.getTransBatchNo(), targetCnt,
				successCnt, filterCnt, failCnt, (endDate.getTime() - startDate.getTime()) / 1000);

	}
}
