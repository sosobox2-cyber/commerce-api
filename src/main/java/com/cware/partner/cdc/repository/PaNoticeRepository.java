package com.cware.partner.cdc.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.PaNoticeM;
import com.cware.partner.common.domain.GoodsTarget;

@Repository
public interface PaNoticeRepository extends JpaRepository<PaNoticeM, String> {

	// 공지사항적용
	@Query(value = "select g.goodsCode as goodsCode, max(g.modifyId) as modifyId "
			+ " from PaNoticeM n "
			+ " inner join PaNoticeTarget g on n.noticeNo = g.noticeNo "
			+ " inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
			+ " where n.useCode = '00' "
			+ " and current_date between n.noticeBdate and n.noticeEdate "
			+ " and not exists (select 1 from PaNoticeApply na "
			+ "           where g.noticeNo = na.noticeNo and g.goodsCode = na.goodsCode and gt.paGroupCode = na.paGroupCode "
			+ "             and rownum = 1) "
			+ " and (n.noticeBdate >= :lastCdcDate or g.modifyDate >= :lastCdcDate ) "
			+ " and q.goodsCode is null "
			+ " group by g.goodsCode "
			)
	Slice<GoodsTarget> findNoticeApplyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);


	// 공지사항종료
	@Query(value = "select na.goodsCode as goodsCode, max(n.modifyId) as modifyId "
			+ " from PaNoticeM n "
			+ " inner join PaNoticeApply na on n.noticeNo = na.noticeNo and na.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on na.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
			+ " where n.noticeEdate <= current_date and n.noticeEdate >= :lastCdcDate "
			+ " and q.goodsCode is null "
			+ " group by na.goodsCode ")
	Slice<GoodsTarget> findNoticeEndTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);


	// 공지사항변경
	@Query(value = "select na.goodsCode as goodsCode, max(n.modifyId) as modifyId "
			+ " from PaNoticeM n "
			+ " inner join PaNoticeApply na on n.noticeNo = na.noticeNo and na.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on na.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
			+ " where n.modifyDate >= :lastCdcDate "
			+ " and q.goodsCode is null "
			+ " group by na.goodsCode "
			)
	Slice<GoodsTarget> findNoticeModifyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

	// 공지사항대상삭제
	@Query(value = "select na.goodsCode as goodsCode, max(na.modifyId) as modifyId "
			+ " from PaNoticeM n "
			+ " inner join PaNoticeApply na on n.noticeNo = na.noticeNo and na.paGroupCode in :paGroupCodes "
			+ " left outer join PaCdcGoodsSnapshot q on na.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
			+ " where n.useCode = '00' "
			+ " and current_date between n.noticeBdate and n.noticeEdate "
			+ " and not exists (select 1 from PaNoticeTarget g "
			+ "           where g.noticeNo = na.noticeNo and g.goodsCode = na.goodsCode "
			+ "             and rownum = 1) "
			+ " and q.goodsCode is null "
			+ " group by na.goodsCode "
			)
	Slice<GoodsTarget> findNoticeExceptTargetList(long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

	//업체휴무일적용
	@Query(value = "select g.goodsCode as goodsCode, max(g.modifyId) as modifyId "
			+ "        from PaNoticeM n "
			+ "        inner join EntpHoliday e on n.entpHolidayNo = e.entpHolidayNo"
			+ "        inner join Goods g on e.entpCode = g.entpCode"
			+ "        inner join PaGoodsTarget gt on g.goodsCode = gt.goodsCode and gt.paSaleGb is not null and gt.paGroupCode in :paGroupCodes "
			+ "        left outer join PaCdcGoodsSnapshot q on g.goodsCode = q.goodsCode and q.cdcSnapshotNo = :cdcSnapshotNo  "
			+ "        where n.useCode = '00' "
			+ "        and e.holidayReasonCode != '20' "
			+ "        and current_date between n.noticeBdate and n.noticeEdate "
			+ "        and not exists (select 1 from PaNoticeTarget nt "
			+ "                         where n.noticeNo = nt.noticeNo and gt.goodsCode = nt.goodsCode "
			+ "                       and rownum = 1) "
			+ "        and n.noticeBdate >= :lastCdcDate  "
			+ "        and q.goodsCode is null "
			+ "        group by g.goodsCode"
			)
	Slice<GoodsTarget> findEntpHolidayApplyTargetList(Timestamp lastCdcDate, long cdcSnapshotNo, Pageable pageRequest, List<String> paGroupCodes);

}
