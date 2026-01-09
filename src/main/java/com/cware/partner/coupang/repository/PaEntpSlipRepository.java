package com.cware.partner.coupang.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaEntpSlip;
import com.cware.partner.sync.domain.id.PaEntpUserId;

@Repository
public interface PaEntpSlipRepository extends JpaRepository<PaEntpSlip, PaEntpUserId> {

	// 출고지등록 대상
	@Query(value = "select distinct new PaEntpSlip(eu.entpCode, eu.entpManSeq, g.paCode) "
			+ " from PaCopnGoods g "
			+ " inner join Goods t on g.goodsCode = t.goodsCode and t.saleGb = '00' "
			+ " inner join EntpUser eu on decode(t.delyType, '10', '100001', t.entpCode) = eu.entpCode "
			+ "         and decode(t.delyType, '10', '003', t.shipManSeq) = eu.entpManSeq "
			+ " where not exists (select 1 from PaEntpSlip e "
			+ "             where e.paCode = g.paCode and e.entpCode = eu.entpCode and e.entpManSeq = eu.entpManSeq )"
			)
	Slice<PaEntpSlip> findRegisterOutboundTargetList(Pageable pageable);

	// 출고지수정 대상
	@Query(value = "select e "
			+ " from PaEntpSlip e "
			+ " where e.transTargetYn = '1' "
			+ "   and e.paCode in ('51', '52') "
			)
	Slice<PaEntpSlip> findUpdateOutboundTargetList(Pageable pageable);
}

