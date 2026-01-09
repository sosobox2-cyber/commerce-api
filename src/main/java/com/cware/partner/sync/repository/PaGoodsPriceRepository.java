package com.cware.partner.sync.repository;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaGoodsPrice;
import com.cware.partner.sync.domain.id.PaGoodsPriceId;

@Repository
public interface PaGoodsPriceRepository extends JpaRepository<PaGoodsPrice, PaGoodsPriceId> {

	@Query(value = "select gp.* "
			+ " from tPaGoodsPrice gp "
			+ "where gp.pa_Code = :paCode "
			+ " and gp.goods_Code = :goodsCode "
			+ " and (gp.apply_Date, gp.supply_seq) = (select /*+index_desc(sgp PK_TPAGOODSPRICE)*/"
			+ "		 sgp.apply_Date, sgp.supply_seq "
			+ " 	   from tPaGoodsPrice sgp "
			+ "		   where sgp.goods_Code = gp.goods_Code "
			+ " 	   and sgp.pa_Code = gp.pa_Code"
			+ "		   and sgp.apply_Date <= current_date"
			+ "		   and rownum = 1 ) "
			, nativeQuery = true
			) 
	Optional<PaGoodsPrice> findApplyGoodsPrice(String paCode, String goodsCode);


	@Query(value = "select gp "
			+ " from PaGoodsPrice gp "
			+ "where gp.paCode = :paCode "
			+ " and gp.goodsCode = :goodsCode "
			+ " and gp.applyDate = (select max(sgp.applyDate) "
			+ " from PaGoodsPrice sgp where sgp.goodsCode = gp.goodsCode and sgp.paCode = gp.paCode "
			+ " and sgp.applyDate <= current_date and sgp.transDate is not null ) "
			)
	Optional<PaGoodsPrice> findTransApplyGoodsPrice(String paCode, String goodsCode);

	@Query(value = " select ltrim(to_char(nvl(max(to_number(t.supply_seq)), 0) + 1, '0000')) "
			+ "   from tpagoodsprice t "
			+ "  where t.goods_code = :goodsCode "
			+ "    and t.apply_date = :applyDate "
			+ "    and t.pa_code = :paCode "
			, nativeQuery = true
			)
	String getNextSupplySeq(String goodsCode, String paCode,Timestamp applyDate );

}
