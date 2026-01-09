package com.cware.partner.cdc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.cdc.domain.entity.PaCdcGoods;

@Repository
public interface PaCdcGoodsRepository extends JpaRepository<PaCdcGoods, String> {

	@Modifying
	@Query(value = " merge into tpacdcgoods "
			+ " using dual on (goods_code = :goodsCode ) "
			+ " when matched then "
			+ "  update set ranking = ranking + :ranking, modify_date = sysdate "
			+ " when not matched then "
			+ "   insert (goods_code, cdc_snapshot_no, ranking, insert_date, modify_date) "
			+ "   values (:goodsCode, :cdcSnapshotNo, :ranking, sysdate, sysdate) "
			, nativeQuery = true)
	int mergeCdcGoods(String goodsCode, int cdcSnapshotNo, int ranking);

}
