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
import com.cware.partner.common.domain.ProductTrans;
import com.cware.partner.common.domain.entity.PaTransBatch;
import com.cware.partner.common.repository.PaTransBatchRepository;
import com.cware.partner.common.service.CommonService;
import com.cware.partner.coupang.repository.PaCopnGoodsRepository;
import com.cware.partner.coupang.service.SaleProductService;
import com.cware.partner.coupang.service.StatusProductService;
import com.cware.partner.sync.domain.entity.PaCopnGoods;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 판매재개 배치
 */
@Slf4j
@Service
public class ResumeSaleProductBatch {

	@Autowired
	PaCopnGoodsRepository copnGoodsRepository;

	@Autowired
	SaleProductService saleProductService;

	@Autowired
	PaTransBatchRepository batchRepository;

	@Autowired
	StatusProductService statusProductService;

	@Autowired
	CommonService commonService;

	// 페이징 사이즈
	@Value("${partner.coupang.page-size}")
	int PAGE_SIZE;

	/**
	 * 판매재개
	 *
	 */
	public void resumeSaleProducts() {

		int targetCnt = 0;
		int procCnt = 0;
		int filterCnt = 0;
		int failCnt = 0;
		int successCnt = 0;

		Slice<PaCopnGoods> slice = null;

		Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.by("p.ranking").descending().and(Sort.by("paCode"))
				.and(Sort.by("lastSyncDate")).and(Sort.by("goodsCode")));

		Timestamp startDate = commonService.currentDate();

		PaTransBatch transBatch = new PaTransBatch();
		transBatch.setPaGroupCode(PaGroup.COUPANG.code());
		transBatch.setBatchName(Thread.currentThread().getStackTrace()[1].getMethodName());
		transBatch.setBatchNote("쿠팡-판매재개배치");
		transBatch.setStartDate(startDate);
		transBatch.setProcessId(Application.ID.code());
		batchRepository.save(transBatch);

		log.info("쿠팡 판매재개 타겟팅 배치번호: {}", transBatch.getTransBatchNo());

		while (true) {

			// 가격/프로모션 변경건
			slice = copnGoodsRepository.findResumeSaleTargetList(pageable);

			List<PaCopnGoods> targetList = slice.getContent();

			log.info("타겟팅 추출:{}", targetList.size());

			targetCnt = targetCnt + targetList.size();

			transBatch.setTargetCnt(targetCnt);
			batchRepository.save(transBatch);

			List<CompletableFuture<ProductTrans>> futures = new ArrayList<>();

			for (PaCopnGoods copnGoods : targetList) {
				log.info("No.{} {}", ++procCnt, copnGoods.getGoodsCode());
				// 상품수정/가격변경/판매재개
				futures.add(saleProductService.asyncResumeSaleProduct(copnGoods.getGoodsCode(),
						copnGoods.getTransTargetYn(), copnGoods.getPaCode(), Application.ID.code(),
						transBatch.getTransBatchNo()));
			}

			ProductTrans result;
			for(CompletableFuture<ProductTrans> future : futures) {
				try {
					result = future.get();

					if (result.getProcCnt() == 0) {
						filterCnt++;
					} else if(result.getProcCnt() > 0) {
						successCnt++;
//						if (result.isUpdated()) {
//							// 상품상태 업데이트
//							statusProductService
//									.asyncUpdateStatusProduct(result.getGoodsCode(), result.getPaCode(), Application.ID.code(), transBatch.getTransBatchNo());
//						}
					} else {
						failCnt++;
					}
				} catch (Exception e) {
					log.error("판매재개 오류: ", e);
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

		log.info("쿠팡 판매재개 완료({}) 타겟:{} 성공:{} 필터:{} 실패:{} End ==> {}s elapsed.", transBatch.getTransBatchNo(), targetCnt,
				successCnt, filterCnt, failCnt, (endDate.getTime() - startDate.getTime()) / 1000);

	}
}
