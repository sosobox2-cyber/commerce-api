package com.cware.netshopping.pacommon.v2.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cware.netshopping.domain.model.PaGoodsLimitChar;

@Mapper
public interface PaGoodsLimitCharMapper {

	@Select(value = "select lc.seq, lc.pa_group_code "
			+ "    , decode(lc.add_char, '1', '\\'||lc.limit_char, '2', '['||lc.limit_char||']', lc.limit_char) as limit_char "
			+ "    , lc.replace_char "
			+ " from tpagoodslimitchar lc "
			+ "  where lc.pa_group_code = #{paGroupCode} "
			+ "    and lc.use_yn = '1' "
			+ " order by lc.seq "
			)
	List<PaGoodsLimitChar> getLimitCharList(@Param("paGroupCode") String paGroupCode);

}
