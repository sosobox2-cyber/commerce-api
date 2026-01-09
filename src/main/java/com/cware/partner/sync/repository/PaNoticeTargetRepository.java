package com.cware.partner.sync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaNoticeTarget;
import com.cware.partner.sync.domain.id.PaNoticeSeq;

@Repository
public interface PaNoticeTargetRepository extends JpaRepository<PaNoticeTarget, PaNoticeSeq> {

	@Query(value = " select nvl(lpad(max(to_number(t.notice_seq)) + 1, 7, '0'), '0000001') "
			+ "		   from tpanoticetarget t "
			+ "		  where t.notice_no = :noticeNo "
			, nativeQuery = true )
	String getNextSupplySeq(String noticeNo);


}
