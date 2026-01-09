package com.cware.partner.sync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.GoodsInfoImage;
import com.cware.partner.sync.domain.id.GoodsInfoImageId;

@Repository
public interface GoodsInfoImageRepository extends JpaRepository<GoodsInfoImage, GoodsInfoImageId> {

	@Query(value = "select /*+ INDEX_DESC (I PK_TGOODSINFOIMAGE)*/ "
			+ " i.* "
			+ " from tGoodsInfoImage i "
			+ " where i.info_Image_Type = '106' "
			+ "   and i.info_Image_File is not null "
			+ "   and i.goods_Code = :goodsCode "
			+ "   and rownum = 1 "
			, nativeQuery = true
			)
	GoodsInfoImage getCoupangGoodsImage(String goodsCode);
	
	@Query(value = "select /*+ INDEX_DESC (I PK_TGOODSINFOIMAGE)*/ "
			+ " i.* "
			+ " from tGoodsInfoImage i "
			+ " where i.info_Image_Type = '40' "
			+ "   and i.info_Image_File is not null "	
			+ "   and i.goods_Code = :goodsCode "
			+ "   and rownum = 1 "
			, nativeQuery = true
			)
	GoodsInfoImage getTdealGoodsMcImage(String goodsCode);
	
	@Query(value = "select /*+ INDEX_DESC (I PK_TGOODSINFOIMAGE)*/ "
			+ " i.* "
			+ " from tGoodsInfoImage i "
			+ " where i.info_Image_Type = '201' "
			+ "   and i.info_Image_File is not null "
			+ "   and i.goods_Code = :goodsCode "
			+ "   and rownum = 1 "
			, nativeQuery = true
			)
	GoodsInfoImage getTdealGoodsListImage(String goodsCode);
	
	@Query(value = "select /*+ INDEX_DESC (I PK_TGOODSINFOIMAGE)*/ "
			+ " i.* "
			+ " from tGoodsInfoImage i "
			+ " where i.info_Image_Type = '202' "
			+ "   and i.info_Image_File is not null "
			+ "   and i.goods_Code = :goodsCode "
			+ "   and rownum = 1 "
			, nativeQuery = true
			)
	GoodsInfoImage getTdealGoodsInfoImage(String goodsCode);
	
	@Query(value = "select /*+ INDEX_DESC (I PK_TGOODSINFOIMAGE)*/ "
			+ " i.* "
			+ " from tGoodsInfoImage i "
			+ " where i.info_Image_Type = '203' "
			+ "   and i.info_Image_File is not null "
			+ "   and i.goods_Code = :goodsCode "
			+ "   and rownum = 1 "
			, nativeQuery = true
			)
	GoodsInfoImage getCoupangGoodsInfoImage(String goodsCode);
}