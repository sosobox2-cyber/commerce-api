package com.cware.partner.sync.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaFapleGoodsDtMapping;
import com.cware.partner.sync.domain.id.PaGoodsDtId;

@Repository
public interface PaFapleGoodsDtMappingRepository extends JpaRepository<PaFapleGoodsDtMapping, PaGoodsDtId> {

	@Query(value = "select dt "
			+ " from PaFapleGoodsDtMapping dt "
			+ "where dt.goodsCode = :goodsCode "
			+ " and dt.goodsdtCode = :goodsdtCode "
			+ " and dt.paCode = :paCode "
			+ " and dt.goodsdtSeq = (select max(sdt.goodsdtSeq) "
			+ " 					   from PaFapleGoodsDtMapping sdt "
			+ " 					  where sdt.goodsCode = dt.goodsCode "
			+ "   						and sdt.goodsdtCode = dt.goodsdtCode  "
			+ "   						and sdt.paCode = dt.paCode  "
			+ "   						and sdt.useYn = '1' ) "
			)
	Optional<PaFapleGoodsDtMapping> findGoodsDtMapping(String paCode, String goodsCode, String goodsdtCode);

	@Query(value = " select ltrim(to_char(nvl(max(to_number(dt.goodsdtSeq)), 0) + 1, '000')) "
			+ "   from PaFapleGoodsDtMapping dt "
			+ "  where dt.goodsCode = :goodsCode "
			+ "    and dt.goodsdtCode = :goodsdtCode "
			+ "    and dt.paCode = :paCode "
			)
	String getNextSeq(String paCode, String goodsCode, String goodsdtCode);
	
	int countByGoodsCodeAndGoodsdtCodeAndPaCodeAndGoodsdtInfo(String goodsCode, String goodsdtCode, String paCode, String goodsdtInfo);
	
	Optional<PaFapleGoodsDtMapping> findTopByGoodsCodeAndGoodsdtCodeAndPaCodeAndGoodsdtInfoOrderByGoodsdtSeqAsc(String goodsCode, String goodsdtCode, String paCode, String goodsdtInfo);
	
  
}

