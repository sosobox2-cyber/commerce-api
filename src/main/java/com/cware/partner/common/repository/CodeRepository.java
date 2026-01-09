package com.cware.partner.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.common.domain.entity.Code;
import com.cware.partner.common.domain.id.CodeId;

@Repository
public interface CodeRepository extends JpaRepository<Code, CodeId> {

	List<Code> findByCodeLgroup(String codeLgroup);

	@Query(value = "select c "
			+ " from Code c "
			+ "where c.codeLgroup = :codeLgroup "
			+ " and c.codeMgroup = (select max(cc.codeMgroup) from Code cc where cc.codeLgroup = c.codeLgroup and cc.codeMgroup <= to_char(current_date, 'yyyymmdd')) ")
	Code findCommisionByCodeLgroup(String codeLgroup); 
}
