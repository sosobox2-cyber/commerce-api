package com.cware.partner.sync.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaCopnGoods;
import com.cware.partner.sync.domain.id.PaGoodsId;

@Repository
public interface PaCopnGoodsRepository extends JpaRepository<PaCopnGoods, PaGoodsId> {

    // 판매중지처리
    @Modifying
    @Query(value = "update PaCopnGoods p "
    		+ " set p.transSaleYn = '1' "
    		+ " , p.paSaleGb = '30' "
    		+ " , p.modifyId = :modifyId "
    		+ " , p.modifyDate = sysdate "
    		+ " where p.goodsCode = :goodsCode "
    		+ "   and p.paCode like :paCode "
    		+ "   and p.paSaleGb = '20' "
    		+ "   and p.paStatus = '30' ")
    int stopSale(String goodsCode, String paCode, String modifyId);

    // 전송대상여부 활성화
    @Modifying
    @Query(value = "update PaCopnGoods p "
    		+ "  set p.transTargetYn = '1'"
    		+ " , p.modifyId = :modifyId "
    		+ " , p.modifyDate = sysdate "
    		+ " where p.goodsCode = :goodsCode "
    		+ "   and p.paCode like :paCode "
    		+ "   and p.transTargetYn = '0' ")
    int enableTransTarget(String goodsCode, String paCode, String modifyId);

	@Query(value = "select g "
			+ " from PaCopnGoods g "
			+ " inner join PaGoods p on g.goodsCode = p.goodsCode "
			+ " inner join PaGoodsPrice pp on g.goodsCode = pp.goodsCode and g.paCode = pp.paCode "
			+ "   and pp.applyDate = ( select max(gp.applyDate) from GoodsPrice gp where gp.goodsCode = pp.goodsCode and gp.applyDate <= current_date) "
			+ " where g.paStatus = '10' "
			+ " and g.paSaleGb = '20' "
			+ " and p.insertDate < current_date - 1/24 "
			+ " and p.insertDate > current_date - 7 "
			)
	Slice<PaCopnGoods> findTransTargetList(Pageable pageable);

	// 제휴입점 반려사유
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query(value = "update PaCopnGoods p "
    		+ "  set p.paStatus = '20'"
    		+ " , p.returnNote = :returnNote "
    		+ " , p.modifyId = :modifyId "
    		+ " , p.modifyDate = sysdate "
    		+ " where p.goodsCode = :goodsCode "
    		+ "   and p.paCode = :paCode"
    		+ "   and p.paStatus != '20' ")
    int rejectTransTarget(String goodsCode, String paCode, String modifyId, String returnNote);
    
    //쿠팡 필수 구매옵션 입력 여부
	@Query(value = "select case "
            + "         when bbb = aaa then "
            + "          '등록' "
            + "         else "
            + "          '미등록' "
            + "       end as status "
            + "  from (select (select count(1) "
            + "				   from tpacopngoodsuserattri b, tgoodsdt c "
            + "                 where c.goods_code = z.goods_code "
            + "                   and b.goods_code(+) = c.goods_code "
            + "                   and b.goodsdt_code(+) = c.goodsdt_code) as aaa, "
		    + "               (select count(1) "
		    + "                  from tpacopngoodsuserattri b, tgoodsdt t1 "
		    + "                 where b.goods_code = z.goods_code "
		    + "                   and b.goods_code = t1.goods_code "
		    + "                   and b.goodsdt_code = t1.goodsdt_code) as bbb "
		    + "          from tgoods z "
		    + "         where z.goods_code = :goodsCode) ",
            nativeQuery = true
            )
	String findCopnPurchaseOptionYn(String goodsCode);
	
	@Query(value = "select case "
			+ "         when exists ( "
			+ "				select 1"
			+ "				  from tpacopngoodsexceptopt ce"
			+ " 			 where ce.goods_code = :goodsCode "
			+ "             ) then 'Y' "
			+ "         else 'N' "
			+ "       end as stauts"
			+ "      from dual ",
			nativeQuery = true
			)
	String findCopnOptExceptYn(String goodsCode);
	
	@Query(value = "select case "
			+ "			when count(1) > 0 then 'Y' "
		    + "   		else 'N' "
		    + "   	   end as requiredYn "
		    + "		  from tpacopnctgrunitlist t, tpagoodskindsmapping a, tgoods b "
		    + " 	 where t.required_yn = 'Y' "
		    + " 	   and a.pa_group_code = '05' "
		    + " 	   and a.lmsd_code = b.lmsd_code "
		    + " 	   and t.pa_lmsd_key = a.pa_lmsd_key "
		    + " 	   and b.goods_code = :goodsCode " ,
			nativeQuery = true
			)
	String findCopnOptRequiredYn(String goodsCode);
	
	boolean existsByGoodsCode(String goodsCode);	

}
