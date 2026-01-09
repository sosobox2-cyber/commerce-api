package com.cware.netshopping.pacommon.v2.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cware.netshopping.pacommon.v2.domain.PaRetentionGoods;

@Mapper
public interface PaRetentionGoodsMapper {

   
    /**
     * 삭제 상품 이력 생성
     * 
     * @param retentionGoods
     * @return
     */
    @Insert(value = "insert into tparetentiongoods ( "
    		+ " pa_code, pa_group_code, goods_code, pa_goods_code "
    		+ " , seq"
    		+ " , status, memo, target_yn"
    		+ " , insert_id, insert_date"
    		+ " ) values ( "
    		+ "  #{paCode}, #{paGroupCode}, #{goodsCode}, #{paGoodsCode} "
    		+ " , ( select ltrim(to_char(nvl(max(to_number(p.seq)), 0) + 1, '00000')) "
    		+ "       from tparetentiongoods p "
    		+ "      where p.pa_code = #{paCode} "
    		+ "        and p.pa_group_code = #{paGroupCode} "
    		+ "        and p.goods_code = #{goodsCode} ) "
    		+ " , #{status}, #{memo}, #{targetYn} "
    		+ " , #{insertId}, sysdate "
    		+ " ) ")
	int insertRetentionGoods(PaRetentionGoods retentionGoods);

	@Select(value = "select max(t.seq) "
			+ " from tparetentiongoods t "
			+ "  where t.goods_code = #{goodsCode} "
			+ "    and t.pa_code = #{paCode} "
			+ "    and t.pa_group_code = #{paGroupCode} "
			)
	String selectMaxSeq(@Param("goodsCode") String goodsCode, @Param("paCode") String paCode,
			@Param("paGroupCode") String paGroupCode);
}
