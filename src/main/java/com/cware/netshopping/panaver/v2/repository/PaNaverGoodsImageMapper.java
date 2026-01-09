package com.cware.netshopping.panaver.v2.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.cware.netshopping.domain.model.PaNaverGoodsImage;

@Mapper
public interface PaNaverGoodsImageMapper {
 
    /**
     * 네이버 이미지 전송 결과 반영 
     * @param image
     * @return
     */
    @Update(value = "merge into tpanavergoodsimage p "
    		+ " using dual on (p.goods_code = #{goodsCode}) "
    		+ " when matched then "
    		+ "      update "
    		+ "         set image_naver_p = #{imageNaverP} "
    		+ "           , image_naver_ap = #{imageNaverAp} "
    		+ "           , image_naver_bp = #{imageNaverBp} "
    		+ "           , image_naver_cp = #{imageNaverCp} "
    		+ "           , image_naver_dp = #{imageNaverDp} "
    		+ "           , last_sync_date = #{lastSyncDate} "
    		+ "           , modify_id = #{modifyId} "
    		+ "           , modify_date = sysdate "
    		+ " when not matched then "
    		+ "      insert ( goods_code, image_naver_p, image_naver_ap, image_naver_bp"
    		+ "             , image_naver_cp, image_naver_dp, last_sync_date "
    		+ "             , insert_id, insert_date, modify_id, modify_date ) "
    		+ "      values ( #{goodsCode}, #{imageNaverP}, #{imageNaverAp}, #{imageNaverBp}"
    		+ "             , #{imageNaverCp}, #{imageNaverDp}, #{lastSyncDate} "
    		+ "             , #{modifyId}, sysdate, #{modifyId}, sysdate ) "
    		)
	int mergeCompleteTrans(PaNaverGoodsImage image);

}