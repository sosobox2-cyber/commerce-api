package com.cware.partner.sync.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.TargetExcept;
import com.cware.partner.sync.domain.entity.PaTargetExcept;

@Repository
public interface PaTargetExceptRepository extends JpaRepository<PaTargetExcept, String> {

	Optional<PaTargetExcept> findByTargetGbAndTargetCode(String targetCode, String targetGb);

	@Query(value = "select pa_group_code_all_yn as paGroupCodeAllYn, pa_group_code as paGroupCode "
			+ " from ( "
			+ " select pa_group_code_all_yn, pa_group_code from tpatargetexcept "
			+ " where target_gb = '00' and target_code = :goodsCode and use_yn = '1' "
			+ " union all  "
			+ " select entp.pa_group_code_all_yn, entp.pa_group_code "
			+ "  from tpaexceptentp entp "
			+ "     , tpaexceptbrand brand "
			+ " where entp.entp_code = :entpCode "
			+ "   and entp.use_yn = '1' "
			+ "   and entp.except_seq = brand.except_seq(+)"
			+ "   and entp.entp_code = brand.entp_code(+)"
			+ "   and brand.use_yn(+) = '1' "
			+ "   and ( entp.all_brand_yn = '1' "
			+ "    or  brand.brand_code = :brandCode) "
			+ "   and (entp.sourcing_media = '00' "
			+ "    or instr(entp.sourcing_media, :sourcingMedia) > 0)"
			+ ")", nativeQuery = true)
	List<TargetExcept> findTargetExcept(String goodsCode, String entpCode, String brandCode, String sourcingMedia);


	// 네이버 제외코드
	@Query(value="select ki.except_code "
			+ " from tpanavergoodskindsinfo ki "
			+ " inner join tpagoodskindsmapping km on km.pa_lmsd_key = ki.category_id "
			+ " where km.lmsd_code = :lmsdCode", nativeQuery = true)
	String getNaverExceptCode(String lmsdCode);
}
