package com.cware.partner.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.common.domain.entity.PaWithoutHour;

@Repository
public interface PaWithoutHourRepository extends JpaRepository<PaWithoutHour, String> {

	@Query(value=" select count(1) "
			+ " from PaWithoutHour wh "
			+ " where wh.paGroupCode = :paGroupcode "
			+ " and sysdate between wh.withoutStartDate and wh.withoutEndDate "
			+ " and useYn = '1' ")
	int checkWithoutHour(String paGroupcode);
	

}
