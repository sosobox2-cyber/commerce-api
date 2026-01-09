package com.cware.partner.coupang.batch;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.cware.partner.coupang.service.StatusProductService;
import com.cware.partner.sync.domain.entity.PaCopnGoods;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 상태 업데이트
 */
@Slf4j
@Service
public class StatusProductBatch {

	@Autowired
	PaCopnGoodsRepository copnGoodsRepository;

	@Autowired
	StatusProductService productService;

	@Autowired
	PaTransBatchRepository batchRepository;

	@Autowired
	CommonService commonService;

	// 페이징 사이즈
//	@Value("${partner.coupang.page-size}")
	int PAGE_SIZE = 20000;

	/**
	 * 승인완료/반려 상태 업데이트
	 * 승인완료건 옵션코드 매핑
	 *
	 */
	public void updateStatusProducts() {

		int targetCnt = 0;
		int procCnt = 0;
		int filterCnt = 0;
		int failCnt = 0;
		int successCnt = 0;

		Slice<PaCopnGoods> slice = null;

		Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.by("insertDate").descending().and(Sort.by("paCode"))
				.and(Sort.by("transTargetYn")).descending().and(Sort.by("modifyDate")).and(Sort.by("goodsCode")));

		Timestamp startDate = commonService.currentDate();

		PaTransBatch transBatch = new PaTransBatch();
		transBatch.setPaGroupCode(PaGroup.COUPANG.code());
		transBatch.setBatchName(Thread.currentThread().getStackTrace()[1].getMethodName());
		transBatch.setBatchNote("쿠팡-상품상태업데이트배치");
		transBatch.setStartDate(startDate);
		transBatch.setProcessId(Application.ID.code());
		batchRepository.save(transBatch);

		log.info("쿠팡 상태 업데이트 타겟팅 배치번호: {}", transBatch.getTransBatchNo());

		while (true) {

			// 입점완료 상태중 제휴상품상태가 승인완료/반려가 안된건
			slice = copnGoodsRepository.findStatusTargetList(pageable);

			List<PaCopnGoods> targetList = slice.getContent();

			log.info("타겟팅 추출:{}", targetList.size());

			targetCnt = targetCnt + targetList.size();

			transBatch.setTargetCnt(targetCnt);
			batchRepository.save(transBatch);

			List<CompletableFuture<Integer>> futures = new ArrayList<>();

			for (PaCopnGoods copnGoods : targetList) {
				// 쿠팡 상태업데이트
				log.info("No.{} {}", ++procCnt, copnGoods.getGoodsCode());
				futures.add(productService.asyncUpdateStatusProduct(copnGoods.getGoodsCode(), copnGoods.getPaCode(),
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
					log.error("상태업데이트 오류: ", e);
					failCnt++;
				}
			}

			transBatch.setProcCnt(procCnt);
			transBatch.setFilterCnt(filterCnt);
			transBatch.setSuccessCnt(successCnt);
			transBatch.setFailCnt(failCnt);
			batchRepository.save(transBatch);

			// 상품승인상태가 업데이트가 지연되는 경우를 고려하여 한 배치 최대 1만개까지 처리
			if (targetCnt >= 10_000 || !slice.hasNext())
				break;

		}
		Timestamp endDate = commonService.currentDate();

		transBatch.setEndDate(endDate);
		batchRepository.save(transBatch);

		log.info("쿠팡 상태 업데이트 완료({}) 타겟:{} 성공:{} 필터:{} 실패:{} End ==> {}s elapsed.", transBatch.getTransBatchNo(), targetCnt,
				successCnt, filterCnt, failCnt, (endDate.getTime() - startDate.getTime()) / 1000);

	}

	public void updateOptionProducts() {

		int targetCnt = 0;
		int procCnt = 0;
		int filterCnt = 0;
		int failCnt = 0;
		int successCnt = 0;

		Slice<PaCopnGoods> slice = null;

        Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.by("lastSyncDate"));

		Timestamp startDate = commonService.currentDate();

		PaTransBatch transBatch = new PaTransBatch();
		transBatch.setPaGroupCode(PaGroup.COUPANG.code());
		transBatch.setBatchName(Thread.currentThread().getStackTrace()[1].getMethodName());
		transBatch.setBatchNote("쿠팡-옵션코드매핑배치");
		transBatch.setStartDate(startDate);
		transBatch.setProcessId(Application.ID.code());
		batchRepository.save(transBatch);

		log.info("쿠팡 옵션코드 업데이트 타겟팅 배치번호: {}", transBatch.getTransBatchNo());

		while (true) {

			// 입점완료/판매중 상품중 상품상태가 승인완료인데 옵션코드 매핑이 안된건
			slice = copnGoodsRepository.findOptionCodeTargetList(pageable);

			List<PaCopnGoods> targetList = slice.getContent();

			log.info("타겟팅 추출:{}", targetList.size());

			targetCnt = targetCnt + targetList.size();

			transBatch.setTargetCnt(targetCnt);
			batchRepository.save(transBatch);

			List<CompletableFuture<Integer>> futures = new ArrayList<>();

			for (PaCopnGoods copnGoods : targetList) {
				// 쿠팡 옵션코드업데이트
				log.info("No.{} {}", ++procCnt, copnGoods.getGoodsCode());
				futures.add(productService.asyncUpdateOptionProduct(copnGoods.getGoodsCode(),
						copnGoods.getSellerProductId(), copnGoods.getApprovalStatus(), copnGoods.getPaCode(),
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
					log.error("수정 오류: ", e);
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

		log.info("쿠팡 옵션코드 업데이트 완료({}) 타겟:{} 성공:{} 필터:{} 실패:{} End ==> {}s elapsed.", transBatch.getTransBatchNo(), targetCnt,
				successCnt, filterCnt, failCnt, (endDate.getTime() - startDate.getTime()) / 1000);

	}
}
