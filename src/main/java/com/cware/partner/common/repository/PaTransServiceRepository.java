package com.cware.partner.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.common.domain.entity.PaTransService;
import com.cware.partner.common.domain.id.PaTransServiceId;

@Repository
public interface PaTransServiceRepository extends JpaRepository<PaTransService, PaTransServiceId> {

	@Modifying
	@Query(value = " update PaTransService p "
			+ "   set endDate = :#{#serviceLog.endDate} "
			+ "     , successYn = :#{#serviceLog.successYn} "
			+ "     , resultCode = :#{#serviceLog.resultCode} "
			+ "     , resultMsg = :#{#serviceLog.resultMsg} "
			+ "  where p.transCode = :#{#serviceLog.transCode} "
			+ "    and p.transType = :#{#serviceLog.transType} "
			+ "    and p.transServiceNo = :#{#serviceLog.transServiceNo} "
			)
	int updateResult(PaTransService serviceLog);
}
