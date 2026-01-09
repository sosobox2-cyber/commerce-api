package com.cware.partner.sync.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaQeenGoodsDtMapping;
import com.cware.partner.sync.domain.id.PaGoodsDtId;

@Repository
public interface PaQeenGoodsDtMappingRepository extends JpaRepository<PaQeenGoodsDtMapping, PaGoodsDtId> {

	@Query(value = "select dt "
			+ " from PaQeenGoodsDtMapping dt "
			+ "where dt.goodsCode = :goodsCode "
			+ " and dt.goodsdtCode = :goodsdtCode "
			+ " and dt.paCode = :paCode "
			+ " and dt.goodsdtSeq = (select max(sdt.goodsdtSeq) "
			+ " 					   from PaQeenGoodsDtMapping sdt "
			+ " 					  where sdt.goodsCode = dt.goodsCode "
			+ "   						and sdt.goodsdtCode = dt.goodsdtCode  "
			+ "   						and sdt.paCode = dt.paCode  "
			+ "   						and sdt.useYn = '1' ) "
			)
	Optional<PaQeenGoodsDtMapping> findGoodsDtMapping(String paCode, String goodsCode, String goodsdtCode);

	@Query(value = " select ltrim(to_char(nvl(max(to_number(dt.goodsdtSeq)), 0) + 1, '000')) "
			+ "   from PaQeenGoodsDtMapping dt "
			+ "  where dt.goodsCode = :goodsCode "
			+ "    and dt.goodsdtCode = :goodsdtCode "
			+ "    and dt.paCode = :paCode "
			)
	String getNextSeq(String paCode, String goodsCode, String goodsdtCode);
	
	Optional<PaQeenGoodsDtMapping> findTopByGoodsCodeAndGoodsdtCodeAndPaCodeAndGoodsdtInfoOrderByGoodsdtSeqAsc(String goodsCode, String goodsdtCode, String paCode, String goodsdtInfo);
	
  
}

