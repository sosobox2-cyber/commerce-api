package com.cware.partner.common.service;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.PaGoodsFilterLog;
import com.cware.partner.sync.domain.entity.PaGoodsSyncLog;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import com.cware.partner.sync.domain.entity.PaPromoFilterLog;
import com.cware.partner.sync.domain.entity.PaPromoTarget;
import com.cware.partner.sync.repository.PaGoodsFilterLogRepository;
import com.cware.partner.sync.repository.PaGoodsSyncLogRepository;
import com.cware.partner.sync.repository.PaPromoFilterLogRepository;

@Service
public class CommonService {

	@PersistenceContext
    EntityManager entityManager;

	@Autowired
	PaGoodsFilterLogRepository goodsFilterLogRepository;

	@Autowired
	PaGoodsSyncLogRepository goodsSyncLogRepository;
	
	@Autowired
	PaPromoFilterLogRepository promoFilterLogRepository;
	
    public Timestamp currentDate() {
        return  (Timestamp) entityManager.createNativeQuery("select sysdate from dual").getSingleResult();
    }

	// 필터로그생성
	public void logFilter(String goodsCode, String filterType, String filterNote, String paGroupCode, long goodsSyncNo) {
		PaGoodsFilterLog filterLog = PaGoodsFilterLog.builder().goodsCode(goodsCode).filterType(filterType)
				.filterNote(filterNote).paGroupCode(paGroupCode)
				.goodsSyncNo(goodsSyncNo)
				.build();
		goodsFilterLogRepository.save(filterLog);
	}

	public void logFilter(String filterType, PaGoodsTarget target) {
		logFilter(target.getGoodsCode(), filterType, target.getExceptNote(), target.getPaGroupCode(), target.getGoodsSyncNo());
	}

	public void logFilter(String filterType, Goods goods) {
		logFilter(goods.getGoodsCode(), filterType, goods.getExceptNote(), null, goods.getGoodsSyncNo());
	}

	// 동기화로그생성
	protected void logSync(String goodsCode, String cdcReasonCode, String syncNote, String paGroupCode, long goodsSyncNo) {
		PaGoodsSyncLog syncLog = PaGoodsSyncLog.builder().goodsCode(goodsCode).cdcReasonCode(cdcReasonCode)
				.syncNote(syncNote)
				.goodsSyncNo(goodsSyncNo)
				.paGroupCode(paGroupCode).build();
		goodsSyncLogRepository.save(syncLog);
	}

	public void logSync(String cdcReasonCode, String syncNote, PaGoodsTarget target) {
		logSync(target.getGoodsCode(), cdcReasonCode, syncNote, target.getPaGroupCode(), target.getGoodsSyncNo());
	}

	public void logSync(String cdcReasonCode, String syncNote, Goods goods) {
		logSync(goods.getGoodsCode(), cdcReasonCode, syncNote, null, goods.getGoodsSyncNo());
	}

	// 프로모션로그생성
	public void logPromoFilter(String filterType, PaGoodsTarget target, PaPromoTarget promo)  {
		PaPromoFilterLog promoFilterlog = PaPromoFilterLog.builder().goodsCode(target.getGoodsCode()).filterType(filterType)
																	.filterNote(promo.getExceptNote()).paGroupCode(target.getPaGroupCode())
																	.promoNo(promo.getPromoNo())
																	.build();
		promoFilterLogRepository.save(promoFilterlog);
	}

	
}
