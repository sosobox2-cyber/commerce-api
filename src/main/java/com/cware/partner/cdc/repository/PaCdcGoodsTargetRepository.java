package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cware.partner.cdc.domain.entity.PaCdcGoods;
import com.cware.partner.cdc.domain.entity.PaCdcGoodsSnapshot;
import com.cware.partner.common.code.LowestModifyId;
import com.cware.partner.common.domain.GoodsTarget;
import com.cware.partner.common.domain.entity.PaCdcReason;
import com.cware.partner.common.repository.CommonRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class PaCdcGoodsTargetRepository {

	@Autowired
	private CommonRepository commonRepository;

	@Autowired
	private PaCdcGoodsRepository paCdcGoodsRepository;

	@Autowired
	private PaCdcGoodsSnapshotRepository paCdcGoodsSnapshotRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = ObjectOptimisticLockingFailureException.class)
	public boolean createCdcGoodsTarget(GoodsTarget target, PaCdcReason cdcReason, long cdcSnapshotNo) {

    	Timestamp currentDate = commonRepository.currentDate();

		PaCdcGoodsSnapshot snapshot = new PaCdcGoodsSnapshot();
		snapshot.setCdcSnapshotNo(cdcSnapshotNo);
		snapshot.setGoodsCode(target.getGoodsCode());
		snapshot.setInsertDate(currentDate);
		snapshot.setProcessId(target.getModifyId());
		paCdcGoodsSnapshotRepository.save(snapshot);

		int ranking = cdcReason.getCdcBoosting();

		if (StringUtils.hasText(target.getModifyId())) {
			if (Arrays.stream(LowestModifyId.values()).anyMatch(v -> v.name().equals(target.getModifyId()))) {
				ranking = 1; // 우선순위 낮은 업체사용자ID인 경우 부스팅 1 적용
			} else if (!target.getModifyId().equals("999999") && !target.getModifyId().startsWith("E")) {
				// 업체에서 등록한 데이터가 아니면 두배 부스팅 적용
				ranking *= 2;
			}
		}

		Optional<PaCdcGoods> optional = paCdcGoodsRepository.findById(target.getGoodsCode());
		PaCdcGoods paCdcGoods;

		if (optional.isPresent()) {
			paCdcGoods = optional.get();
			paCdcGoods.setRanking(paCdcGoods.getRanking() + ranking);
		} else {
			paCdcGoods = new PaCdcGoods();
			paCdcGoods.setGoodsCode(target.getGoodsCode());
			paCdcGoods.setCdcSnapshotNo(cdcSnapshotNo);
			paCdcGoods.setRanking(ranking);
			paCdcGoods.setInsertDate(currentDate);
		}

		paCdcGoods.setModifyDate(currentDate);
		try {
			paCdcGoodsRepository.saveAndFlush(paCdcGoods);
		} catch (ObjectOptimisticLockingFailureException ex) {
			log.info("상품동기화가 수행되어 이미 큐가 제거되었습니다. 상품:{} 스냅샷:{}", paCdcGoods.getGoodsCode(), cdcSnapshotNo);
		}

		// 큐 신규 생성여부
		return !optional.isPresent();

//		paCdcGoodsRepository.mergeCdcGoods(target.getGoodsCode(),
//				snapshot.getCdcSnapshotNo(), ranking);
	}

}
