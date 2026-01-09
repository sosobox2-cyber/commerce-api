package com.cware.partner.sync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaTdealCategoryAddInfo;
import com.cware.partner.sync.domain.id.PaTdealCategoryAddInfoId;

@Repository
public interface PaTdealCategoryAddInfoRepository extends JpaRepository<PaTdealCategoryAddInfo, PaTdealCategoryAddInfoId> {

	
	@Query(value = "select new PaTdealCategoryAddInfo (gm.lmsdCode, ca.minMarginRate, ca.commission, ca.promoMinMarginRate, ca.paLmsdKey, ca.paCode)"
			+ " from PaTdealCategoryAddInfo ca "
			+ " inner join PaGoodsKindsMapping gm on ca.paGroupCode = gm.paGroupCode and ca.paLmsdKey = gm.paLmsdKey"
			+ " where ca.paGroupCode = :paGroupCode "
			)
	List<PaTdealCategoryAddInfo> getTdealCategoryAddInfo(String paGroupCode);
	
	@Query(value = "select new PaTdealCategoryAddInfo (ca.minMarginRate, ca.commission, ca.promoMinMarginRate, ca.paLmsdKey, ca.paCode)"
			+ " from PaTdealCategoryAddInfo ca "
			+ " where ca.paGroupCode = :paGroupCode "
			)
	List<PaTdealCategoryAddInfo> getTdealCategoryAddInfoAll(String paGroupCode);
}