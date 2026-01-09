package com.cware.partner.sync.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaTdealEvent;

@Repository
public interface PaTdealEventRepository extends JpaRepository<PaTdealEvent, String> {
	
	
	@Query(value = "select new PaTdealEvent(te.goodsCode, te.eventNo, te.paLmsdKey)"
			+ " from PaTdealEvent te "
			+ " where te.useYn = '1' "
			+ "   and (current_date between te.eventBdate and te.eventEdate) "
			+ "   and (te.eventBdate >= te.lastSyncDate or te.lastSyncDate is null) "
			+ "   and te.goodsCode = :goodsCode "
			)
	List<PaTdealEvent> selectTdealEventApply(String goodsCode);
	
	@Modifying
	@Query(value = "update PaTdealEvent te "
			+ " set te.lastSyncDate  = current_date "
			+ " where te.eventNo = :eventNo "
			+ "   and te.goodsCode   = :goodsCode ")
	int updateLastSyncDate(String eventNo, String goodsCode);
	
	@Query(value = "select new PaTdealEvent(te.goodsCode, te.eventNo, te.paLmsdKey)"
			+ " from PaTdealEvent te "
			+ " where ((te.eventEdate <= current_date and te.eventEdate >= te.lastSyncDate) or (te.useYn = '0' and te.modifyDate >= te.lastSyncDate)) "
			+ "   and te.goodsCode = :goodsCode "
			)
	List<PaTdealEvent> selectTdealEventEnd(String goodsCode);
	
	@Query(value = "select new PaTdealEvent(te.goodsCode, te.eventNo, te.paLmsdKey)"
			+ " from PaTdealEvent te "
			+ " where te.useYn = '1' "
			+ "   and (current_date between te.eventBdate and te.eventEdate) "
			+ "   and te.goodsCode = :goodsCode "
			)
	List<PaTdealEvent> selectTdealEvent(String goodsCode);
	
	@Query(value = "select te "
			+ " from PaTdealEvent te "
			+ " where te.goodsCode = :goodsCode "
			+ "   and te.eventNo   = (select max(eventNo) from PaTdealEvent te2"
			+ "						   where te2.useYn = '1' "
			+ "						     and (current_date between te.saleStartDate and te.saleEndDate) "
			+ "						     and te2.goodsCode = te.goodsCode)"
			)
    Optional<PaTdealEvent> selectTdealSaleEvent(String goodsCode);
	
}
