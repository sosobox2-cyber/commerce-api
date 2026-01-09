package com.cware.netshopping.pacommon.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cware.netshopping.pacommon.v2.domain.GoodsVod;

@Mapper
public interface GoodsVodMapper {
            
    @Select(value = "select gv.* "
            + "from tgoodsvod gv "
            + " where gv.display_media = '62' "
            + "    and gv.goods_vod_seq = "
            + "                 (select max(gvs.goods_vod_seq) "
            + "                       from tgoodsvod gvs "
            + "                where gvs.goods_code = gv.goods_code "
            + "                      and gvs.display_media = '62') "
            + "    and gv.goods_code = #{goodsCode} ")
    
    GoodsVod getGoodsVod(@Param("goodsCode") String goodsCode);
}