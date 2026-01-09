package com.cware.partner.sync.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.QeenGoodsOffer;
import com.cware.partner.sync.domain.FapleGoodsOffer;
import com.cware.partner.sync.domain.entity.PaGoodsOffer;
import com.cware.partner.sync.domain.id.PaGoodsOfferId;

@Repository
public interface PaGoodsOfferRepository extends JpaRepository<PaGoodsOffer, PaGoodsOfferId> {

	Optional<PaGoodsOffer> findOneByGoodsCodeAndPaGroupCode (String paGroupCode, String goodsCode);

	@Query(value = "select new PaGoodsOffer(o.goodsCode, p.paGroupCode, p.paOfferType, p.paOfferCode, nvl(o.offerContents, '상세설명 참조'))"
			+ " from PaOfferCode p "
			+ " left outer join PaOfferCodeMapping m on p.paGroupCode = m.paGroupCode "
			+ "  and p.paOfferType = m.paOfferType "
			+ "  and p.paOfferCode = m.paOfferCode "
			+ " left outer join Offer o on m.offerType = o.offerType "
			+ "  and m.offerCode = o.offerCode "
			+ "  and o.useYn = '1' "
			+ "  and o.goodsCode = :goodsCode "
			+ " where p.paGroupCode = :paGroupCode "
			+ "   and p.useYn = '1' "
			+ "   and p.paOfferType = (select c.paOfferType from PaOfferCodeMapping c where c.paGroupCode = p.paGroupCode and c.offerType = :offerType and rownum = 1) "
			)
	List<PaGoodsOffer> selectGoodsOffer(String paGroupCode, String goodsCode, String offerType);
	
	@Query(value = "select new PaGoodsOffer(o.goodsCode, p.paGroupCode, p.paOfferType, p.paOfferCode, nvl(o.offerContents, '상세설명 참조'), o.useYn)"
			+ " from PaGoodsOffer p "
			+ " inner join PaOfferCodeMapping m on p.paGroupCode = m.paGroupCode "
			+ "  and p.paOfferType = m.paOfferType "
			+ "  and p.paOfferCode = m.paOfferCode "
			+ "  and p.paGroupCode = :paGroupCode "
			+ "  and p.goodsCode = :goodsCode "
			+ "  inner join Offer o on m.offerType = o.offerType "
			+ "  and m.offerCode = o.offerCode "
			+ "  and p.goodsCode = o.goodsCode "
			+ "  and p.lastSyncDate < o.modifyDate "
			)
	List<PaGoodsOffer> selectGoodsOfferModify(String paGroupCode, String goodsCode);

	boolean existsByPaGroupCodeAndGoodsCodeAndUseYn(String paGroupCode, String goodsCode, String useYn );


	@Query(value = "select new PaGoodsOffer(p.goodsCode, m.paGroupCode, m.paOfferType, m.paOfferCode, '상세설명 참조', '1')"
			+ " from PaOfferCode m "
			+ " left outer join PaGoodsOffer p on p.paGroupCode = m.paGroupCode "
			+ "  and p.paOfferType = m.paOfferType "
			+ "  and p.paOfferCode = m.paOfferCode "
			+ "  and p.goodsCode = :goodsCode "
			+ " where m.paGroupCode = :paGroupCode "
			+ "  and m.useYn = '1' "
			+ "  and m.paOfferType = (select g.paOfferType from PaGoodsOffer g where g.paGroupCode = :paGroupCode and g.goodsCode = :goodsCode and g.useYn = '1' and rownum = 1) "
			+ "  and p.goodsCode is null "
			)
	List<PaGoodsOffer> selectGoodsOfferNew(String paGroupCode, String goodsCode);

	@Query(value = "select new PaGoodsOffer(p.paGroupCode, p.paOfferType, p.paOfferCode)"
			+ " from PaOfferCode p "
			+ " where p.paOfferType = :paOfferType "
			+ "  and p.paGroupCode = :paGroupCode "
			+ "  and p.useYn = '1' "
			)
	List<PaGoodsOffer> selectGoodsOfferByPaOfferType(String paGroupCode, String paOfferType);

	@Query(value = "select p.paOfferTypeName "
			+ " from PaOfferCode p "
			+ " inner join PaOfferCodeMapping m on p.paGroupCode = m.paGroupCode "
			+ "  and p.paOfferType = m.paOfferType "
			+ "  and p.paOfferCode = m.paOfferCode "
			+ "  and p.paGroupCode = :paGroupCode "
			+ "  and p.useYn = '1' "
			+ " inner join Offer o on m.offerType = o.offerType "
			+ "  and m.offerCode = o.offerCode "
			+ "  and o.goodsCode = :goodsCode "
			+ "  and o.useYn = '1' "
			+ " where rownum = 1 "
			)
	String getPaOfferTypeName(String paGroupCode, String goodsCode);

	@Query(value = "select m.offerType "
			+ " from PaGoodsOffer p"
			+ " inner join PaOfferCodeMapping m on p.paGroupCode = m.paGroupCode "
			+ "  and p.paOfferType = m.paOfferType "
			+ "  and p.paOfferCode = m.paOfferCode "
			+ "  and p.useYn = '1' "
			+ " where p.goodsCode = :goodsCode "
			+ "  and p.paGroupCode = :paGroupCode "
			+ "  and p.useYn = '1' "
			+ "  and rownum = 1 "
			)
	String getOfferType(String paGroupCode, String goodsCode);

	@Query(value = "select c.notice_category_name "
			+ " from tpacopnctgrattrlist c "
			+ " where c.pa_lmsd_key = :paLmsdKey "
			+ "   and c.notice_category_name like '%'||:paOfferTypeName||'%'  "
			+ "   and rownum = 1 ", nativeQuery = true
			)
	String getCoupangNoticeCategoryName(String paLmsdKey, String paOfferTypeName);

	@Query(value = "select c.notice_category_name "
			+ " from tpacopnctgrattrlist c "
			+ " where c.pa_lmsd_key = :paLmsdKey "
			+ "   and replace(c.notice_category_name, '기타 재화(유통기간)', '') like '%기타 재화%'  "
			+ "   and rownum = 1 ", nativeQuery = true
			)
	String getCoupangNoticeCategoryEtcName(String paLmsdKey);

	@Query(value = "select c.notice_category_name "
			+ " from tpacopnctgrattrlist c "
			+ " where c.pa_lmsd_key = :paLmsdKey "
			+ "   and rownum = 1 ", nativeQuery = true
			)
	String getCoupangNoticeCategoryDefaultName(String paLmsdKey);


	@Query(value = "select c.pa_offer_type "
			+ " from tpaoffercode c "
			+ " where c.pa_group_code = :paGroupCode "
			+ "   and replace(c.pa_offer_type_name, ' ', '') = :paOfferTypeName "
			+ "   and rownum = 1 ", nativeQuery = true
			)
	String getPaOfferType(String paGroupCode, String paOfferTypeName);


    @Modifying
    @Query(value = "update PaGoodsOffer p "
    		+ " set p.useYn = '0' "
    		+ " , p.transTargetYn = '1' "
    		+ " , p.lastSyncDate = sysdate "
    		+ " , p.modifyId = :modifyId "
    		+ " , p.modifyDate = sysdate "
    		+ " where p.goodsCode = :goodsCode "
    		+ "   and p.paGroupCode = :paGroupCode "
    		+ "   and p.useYn = '1' ")
    int disableGooodsOffer(String paGroupCode, String goodsCode, String modifyId);
    
	@Query(value = "select "
			+ " d.goods_code as goodsCode, "
			+ " p.pa_group_code as paGroupCode, "
			+ " p.pa_offer_type as paOfferType, "
			+ " p.pa_offer_code as paOfferCode, "
			+ " nvl(d.offer_contents, '상세설명 참조') as paOfferExt "
			+ " from tpaoffercode p "
			+ " left outer join ( "
			+ "    select m.pa_group_code, m.pa_offer_type, m.pa_offer_code, o.goods_code, o.offer_contents "
			+ "      from tpaoffercodemapping m "
			+ "     inner join toffer o "
			+ "        on m.offer_type = o.offer_type "
			+ "       and m.offer_code = o.offer_code "
			+ "     where m.pa_group_code = :paGroupCode "
			+ "       and o.goods_code = :goodsCode "
			+ " ) d on d.pa_group_code = p.pa_group_code and d.pa_offer_type = p.pa_offer_type and d.pa_offer_code = p.pa_offer_code "
			+ " where p.pa_group_code = :paGroupCode "
			+ " and p.use_yn = '1' "
			+ " and p.pa_offer_type = (select c.pa_offer_type from tpaoffercodemapping c where c.pa_group_code = p.pa_group_code and c.offer_type = :offerType and rownum = 1 ) "
			, nativeQuery = true )
	List<QeenGoodsOffer> selectQeenGoodsOffer(String paGroupCode, String goodsCode, String offerType);
	
	@Query(" select p.remark1 "
			+ " from PaGoodsOffer p "
			+ " where p.paGroupCode = :paGroupCode "
			+ "   and p.goodsCode = :goodsCode"
			+ "    and rownum = 1 ")
    String getRemark1(String paGroupCode, String goodsCode);
	
	@Query(value = "select m.paOfferType "
			+ " from PaGoodsOffer p"
			+ " inner join PaOfferCodeMapping m on p.paGroupCode = m.paGroupCode "
			+ "  and p.paOfferType = m.paOfferType "
			+ "  and p.paOfferCode = m.paOfferCode "
			+ "  and p.useYn = '1' "
			+ " where p.goodsCode = :goodsCode "
			+ "  and p.paGroupCode = :paGroupCode "
			+ "  and p.useYn = '1' "
			+ "  and rownum = 1 "
			)
	String getFaplePaOfferType(String paGroupCode, String goodsCode);
	
	@Query(value = "select :goodsCode as goodsCode , p.pa_Group_Code as paGroupCode, :offerType as paOfferType, p.pa_Offer_Code as paOfferCode, '상세설명 참조' as paOfferExt "
			+ " from bmtpartner.tPaOfferCode p "
			+ " left outer join bmtpartner.tPaOfferCodeMapping m on p.pa_Group_Code = m.pa_Group_Code "
			+ "  and p.pa_Offer_Type = m.pa_Offer_Type "
			+ "  and p.pa_Offer_Code = m.pa_Offer_Code "
			+ " left outer join bmtcom.tOfferCode o on m.offer_Type = o.offer_Type "
			+ "  and m.offer_Code = o.offer_Code "
			+ "  and o.use_Yn = '1' "
			+ " where p.pa_Group_Code = :paGroupCode "
			+ "   and p.use_Yn = '1' "
			+ "   and p.pa_Offer_Type = :offerType "
			, nativeQuery = true )
	List<FapleGoodsOffer> selectGoodsFakeOffer(String paGroupCode, String goodsCode, String offerType);
    
}
