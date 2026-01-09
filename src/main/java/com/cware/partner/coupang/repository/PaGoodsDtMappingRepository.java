package com.cware.partner.coupang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cware.partner.sync.domain.entity.PaGoodsDtMapping;
import com.cware.partner.sync.domain.id.PaGoodsDtId;

@Repository
public interface PaGoodsDtMappingRepository extends JpaRepository<PaGoodsDtMapping, PaGoodsDtId> {

	List<PaGoodsDtMapping> findByGoodsCodeAndPaCodeAndPaOptionCodeIsNotNull(String goodsCode, String paCode);

    // 옵션별 재고
	@Query(value = "select new PaGoodsDtMapping(g.paCode, g.goodsCode, g.goodsdtCode, g.paOptionCode, g.transStockYn, "
			+ " dt.orderAbleQty ) "
			+ " from PaGoodsDtMapping g "
			+ " inner join GoodsDt dt on dt.goodsCode = g.goodsCode and dt.goodsdtCode = g.goodsdtCode "
			+ " where g.transStockYn = '1' "
			+ "  and g.paOptionCode is not null "
			+ "  and g.goodsCode = :goodsCode "
			+ "  and g.paCode = :paCode "
			)
	List<PaGoodsDtMapping> findUpdateStock(String goodsCode, String paCode);

	// 옵션코드/노출아이디 업데이트
	@Transactional
    @Modifying
    @Query(value = "update PaGoodsDtMapping p "
    		+ "  set p.paOptionCode = :optionCode "
    		+ " , p.remark1 = :itemId "
    		+ " , p.remark2 = :productId "
    		+ " , p.modifyId = :modifyId "
    		+ " , p.modifyDate = sysdate "
    		+ " where p.goodsCode = :goodsCode "
    		+ "   and p.goodsdtCode = :goodsdtCode"
    		+ "   and p.paCode = :paCode "
    		)
    int updateOptionCode(String goodsCode, String goodsdtCode, String paCode, String modifyId, String optionCode, String itemId, String productId);

	// 재고 전송 완료
	@Transactional
    @Modifying
    @Query(value = "update PaGoodsDtMapping p "
    		+ " set p.transStockYn = '0' "
    		+ " , p.transOrderAbleQty = :transOrderAbleQty "
    		+ " , p.modifyId = :modifyId "
    		+ " , p.modifyDate = sysdate "
    		+ " where p.goodsCode = :goodsCode "
    		+ "   and p.goodsdtCode = :goodsdtCode "
    		+ "   and p.paCode = :paCode ")
	int updateTransStock(String goodsCode, String goodsdtCode, String paCode, int transOrderAbleQty, String modifyId);

}
