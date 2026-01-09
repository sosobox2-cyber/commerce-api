package com.cware.partner.sync.service;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import com.cware.partner.common.code.Application;
import com.cware.partner.common.code.CdcReason;
import com.cware.partner.common.service.CommonService;
import com.cware.partner.sync.domain.entity.PaFapleShipCost;
import com.cware.partner.sync.domain.entity.PaGoodsSync;
import com.cware.partner.sync.repository.PaFapleShipCostRepository;
import com.cware.partner.sync.repository.PaGoodsSyncLogRepository;
import com.cware.partner.sync.repository.PaGoodsSyncRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 패션플러스 업체주소 동기화
 */
@Slf4j
@Service
public class EntpAddrSyncFapleService {
	@Autowired
	PaFapleShipCostRepository paFapleShipCostRepository;
	
	@Autowired
	CommonService commonService;

	@Autowired
	PaGoodsSyncRepository paGoodsSyncRepository;

	@Autowired
	PaGoodsSyncLogRepository goodsSyncLogRepository;

	// 페이징 사이즈
	@Value("${partner.sync.page-size}")
	int PAGE_SIZE;

	CdcReason reason = CdcReason.ENTPUSER_MODIFY;

	public void executeEntpAddressSync() {

		log.info("*******Partner Faple Enterprise Address Sync service start*******");
		// 패션플러스 업체주소 동기화
		entpAddressSync();
		log.info("*******Partner Faple Enterprise Address Sync service end*******");
	}

	private void entpAddressSync() {

		int targetCnt = 0;
		int procCnt = 0;
		int syncCnt = 0;

		Slice<PaFapleShipCost> slice = null;

		Pageable pageable = PageRequest.of(0, PAGE_SIZE);

		Timestamp currentDate = commonService.currentDate();
		PaGoodsSync goodsSync = new PaGoodsSync();
		goodsSync.setStartDate(currentDate);
		paGoodsSyncRepository.save(goodsSync);

		log.info("패션플러스 업체주소 타겟팅 [{}({})] 동기화시작일시:{} Start....", reason.name(), goodsSync.getGoodsSyncNo(), currentDate);

		while (true) {

			// 업체주소 변경리스트 추출
			slice = paFapleShipCostRepository.findModifyTargetList(pageable);
			List<PaFapleShipCost> targetList = slice.getContent();
			targetCnt = targetCnt + targetList.size();

			log.info("타겟팅 추출[{}({})] 타겟:{}", reason.name(), goodsSync.getGoodsSyncNo(), targetList.size());

			goodsSync.setTargetCnt(targetCnt);
			paGoodsSyncRepository.save(goodsSync);


			for (PaFapleShipCost target : targetList) {
				// 패션플러스 업체주소 동기화
				syncCnt += syncEntpSlip(target, goodsSync.getGoodsSyncNo());
				procCnt++;
			}

			goodsSync.setProcCnt(procCnt);
			goodsSync.setSyncCnt(syncCnt);
			paGoodsSyncRepository.save(goodsSync);

			if (!slice.hasNext())
				break;

		}

		goodsSync.setEndDate(commonService.currentDate());
		paGoodsSyncRepository.save(goodsSync);
	}

	private int syncEntpSlip(PaFapleShipCost target, long goodsSyncNo) {
		int syncCnt = 0;
		Timestamp currentDate = commonService.currentDate();
		target.setTransTargetYn		("1");
		target.setModifyDate		(currentDate);
		target.setLastEntpSyncDate	(currentDate);
		target.setModifyId			(Application.ID.code());
		paFapleShipCostRepository.save(target);

		return syncCnt;
	}
}
