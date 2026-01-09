package com.cware.partner.common.service;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.partner.common.domain.entity.DateItem;
import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.PaGoodsFilterLog;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import com.cware.partner.sync.repository.PaGoodsFilterLogRepository;

@Service
public class CommonService {

	@PersistenceContext
    EntityManager entityManager;

	@Autowired
	PaGoodsFilterLogRepository goodsFilterLogRepository;

    public Timestamp currentDate() {
        return  (Timestamp) entityManager.createNativeQuery("select sysdate from dual").getSingleResult();
    }

	public Timestamp currentTimestamp() {
		DateItem dateItem = (DateItem) entityManager
				.createNativeQuery("select systimestamp as date_value from dual", DateItem.class).getSingleResult();
		return Timestamp.from(dateItem.getDate());
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

}
