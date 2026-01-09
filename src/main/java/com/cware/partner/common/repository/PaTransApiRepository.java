package com.cware.partner.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.common.domain.entity.PaTransApi;
import com.cware.partner.common.domain.id.PaTransApiId;

@Repository
public interface PaTransApiRepository extends JpaRepository<PaTransApi, PaTransApiId> {

	@Modifying
	@Query(value = " update PaTransApi p "
			+ "   set responseDate = :#{#apiLog.responseDate} "
			+ "     , successYn = :#{#apiLog.successYn} "
			+ "     , resultCode = :#{#apiLog.resultCode} "
			+ "     , resultMsg = :#{#apiLog.resultMsg} "
			+ "     , responsePayload = :#{#apiLog.responsePayload} "
			+ "  where p.transCode = :#{#apiLog.transCode} "
			+ "    and p.transType = :#{#apiLog.transType} "
			+ "    and p.transApiNo = :#{#apiLog.transApiNo} "
			)
	int updateResponse(PaTransApi apiLog);
}
