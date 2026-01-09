package com.cware.partner.coupang.batch;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.cware.partner.common.code.Application;
import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.domain.entity.PaTransBatch;
import com.cware.partner.common.repository.PaTransBatchRepository;
import com.cware.partner.common.service.CommonService;
import com.cware.partner.coupang.repository.PaEntpSlipRepository;
import com.cware.partner.coupang.service.PartnerSellerService;
import com.cware.partner.sync.domain.entity.PaEntpSlip;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 출고지등록 배치
 */
@Slf4j
@Service
public class RegisterOutboundBatch {

	@Autowired
	PaEntpSlipRepository entpSlipRepository;

	@Autowired
	PartnerSellerService partnerSellerService;

	@Autowired
	PaTransBatchRepository batchRepository;

	@Autowired
	CommonService commonService;

	// 페이징 사이즈
	@Value("${partner.coupang.page-size}")
	int PAGE_SIZE;

	public void registerOutbound() {

		int targetCnt = 0;
		int filterCnt = 0;
		int transCnt = 0;
		int failCnt = 0;
		int procCnt = 0;

		Slice<PaEntpSlip> slice = null;

        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

		Timestamp startDate = commonService.currentDate();

		PaTransBatch transBatch = new PaTransBatch();
		transBatch.setPaGroupCode(PaGroup.COUPANG.code());
		transBatch.setBatchName(Thread.currentThread().getStackTrace()[1].getMethodName());
		transBatch.setBatchNote("쿠팡-출고지등록요청배치");
		transBatch.setStartDate(startDate);
		transBatch.setProcessId(Application.ID.code());
		batchRepository.save(transBatch);

		log.info("쿠팡 출고지등록 타겟팅 배치번호: {}", transBatch.getTransBatchNo());

		while (true) {

			// 쿠팡 출고지 등록 대상
			slice = entpSlipRepository.findRegisterOutboundTargetList(pageable);

			List<PaEntpSlip> targetList = slice.getContent();

			log.info("타겟팅 추출:{}", targetList.size());

			targetCnt = targetCnt + targetList.size();

			transBatch.setTargetCnt(targetCnt);
			batchRepository.save(transBatch);

			for (PaEntpSlip entpSlip : targetList) {
				try {
					// 쿠팡 출고지등록 요청
					log.info("No.{} {}-{}", ++procCnt, entpSlip.getEntpCode(), entpSlip.getEntpManSeq());
					int result = partnerSellerService.registerOutbound(entpSlip.getEntpCode(), entpSlip.getEntpManSeq(),
							entpSlip.getPaCode(), transBatch.getTransBatchNo());
					if (result == 0) {
						filterCnt++;
					} else if (result > 0) {
						transCnt++;
					} else {
						failCnt++;
					}
				} catch (Exception e) {
					log.error("출고지등록 오류: ", e);
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

		log.info("쿠팡 출고지등록 완료({}) 타겟:{} 성공:{} 필터:{} 실패:{} End ==> {}s elapsed.", transBatch.getTransBatchNo(), targetCnt,
				transCnt, filterCnt, failCnt, (endDate.getTime() - startDate.getTime()) / 1000);

	}

}
