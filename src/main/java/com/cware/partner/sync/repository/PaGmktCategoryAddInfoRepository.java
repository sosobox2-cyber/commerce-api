package com.cware.partner.sync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaGmktCategoryAddInfo;
import com.cware.partner.sync.domain.id.PaGmktCategoryAddInfoId;

@Repository
public interface PaGmktCategoryAddInfoRepository extends JpaRepository<PaGmktCategoryAddInfo, PaGmktCategoryAddInfoId> {

	
	@Query(value = "select new PaGmktCategoryAddInfo (esm.lmsdCode, ca.minMarginRate, ca.commission, ca.paCode, ca.promoMinMarginRate)"
			+ " from PaGmktCategoryAddInfo ca "
			+ " inner join PaEsmGoodskindsMapping esm on ca.paLmsdKey = esm.paLmsdnKey"
			+ " where ca.paGroupCode = :paGroupCode "
			)
	List<PaGmktCategoryAddInfo> getGmktCategoryAddInfo(String paGroupCode);
	
}