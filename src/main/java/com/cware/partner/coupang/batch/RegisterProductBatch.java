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
import com.cware.partner.coupang.service.PartnerProductService;
import com.cware.partner.coupang.service.StatusProductService;
import com.cware.partner.sync.domain.entity.PaCopnGoods;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 신규 제휴입점 대상 타겟팅하여 이관요청
 */
@Slf4j
@Service
public class RegisterProductBatch {

	@Autowired
	PaCopnGoodsRepository copnGoodsRepository;

	@Autowired
	PartnerProductService transProductService;

	@Autowired
	StatusProductService statusProductService;

	@Autowired
	PaTransBatchRepository batchRepository;

	@Autowired
	CommonService commonService;

	// 페이징 사이즈
	@Value("${partner.coupang.page-size}")
	int PAGE_SIZE;

	// 파일럿테스트
	@Value("${partner.coupang.pilot:false}")
	boolean isPilot;
	@Value("${partner.coupang.pilot.goods-codes}")
	List<String> GOODS_CODES;

	public void registerProducts() {

		int targetCnt = 0;
		int filterCnt = 0;
		int transCnt = 0;
		int failCnt = 0;
		int procCnt = 0;

		Slice<PaCopnGoods> slice = null;

		Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.by("p.ranking").descending()
				.and(Sort.by("paCode")).and(Sort.by("insertDate")).and(Sort.by("goodsCode")));

		Timestamp startDate = commonService.currentDate();

		PaTransBatch transBatch = new PaTransBatch();
		transBatch.setPaGroupCode(PaGroup.COUPANG.code());
		transBatch.setBatchName(Thread.currentThread().getStackTrace()[1].getMethodName());
		transBatch.setBatchNote("쿠팡-제휴입점요청배치");
		transBatch.setStartDate(startDate);
		transBatch.setProcessId(Application.ID.code());
		batchRepository.save(transBatch);

		log.info("쿠팡 제휴입점 타겟팅 배치번호: {}", transBatch.getTransBatchNo());
		while (true) {

			if (isPilot) {
				// 파일럿테스트
				slice = copnGoodsRepository.findTransTargetList(pageable, GOODS_CODES);
			} else {
				// 쿠팡 입점 상품 추출
				slice = copnGoodsRepository.findTransTargetList(pageable);
			}

			List<PaCopnGoods> targetList = slice.getContent();

			log.info("타겟팅 추출: {}", targetList.size());

			targetCnt = targetCnt + targetList.size();

			transBatch.setTargetCnt(targetCnt);
			batchRepository.save(transBatch);

			List<CompletableFuture<ProductTrans>> futures = new ArrayList<>();

			for (PaCopnGoods copnGoods : targetList) {
				// 쿠팡 입점 요청
				log.info("No.{} {}", ++procCnt, copnGoods.getGoodsCode());
				futures.add(transProductService.asyncRegisterProduct(copnGoods.getGoodsCode(), copnGoods.getPaCode(), transBatch.getTransBatchNo()));
			}

			ProductTrans result;
			for(CompletableFuture<ProductTrans> future : futures) {
				try {
					result = future.get();

					if (result.getProcCnt() == 0) {
						filterCnt++;
					} else if(result.getProcCnt() > 0) {
						transCnt++;
						// 상품상태 업데이트
//						statusProductService
//								.asyncUpdateStatusProduct(result.getGoodsCode(), result.getPaCode(), Application.ID.code(), transBatch.getTransBatchNo());
					} else {
						failCnt++;
					}
				} catch (Exception e) {
					log.error("입점 오류: ", e);
					failCnt++;
				}
			}

			transBatch.setProcCnt(procCnt);
			transBatch.setFilterCnt(filterCnt);
			transBatch.setSuccessCnt(transCnt);
			transBatch.setFailCnt(failCnt);
			batchRepository.save(transBatch);

			if (!slice.hasNext())
				break;

		}
		Timestamp endDate = commonService.currentDate();

		transBatch.setEndDate(endDate);
		batchRepository.save(transBatch);

		log.info("쿠팡 제휴입점 완료({}) 타겟:{} 입점:{} 필터:{} 실패:{} End ==> {}s elapsed.", transBatch.getTransBatchNo(), targetCnt,
				transCnt, filterCnt, failCnt, (endDate.getTime() - startDate.getTime()) / 1000);

	}

}
