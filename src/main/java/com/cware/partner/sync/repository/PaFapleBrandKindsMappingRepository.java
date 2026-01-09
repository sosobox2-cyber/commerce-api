package com.cware.partner.sync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cware.partner.sync.domain.entity.PaFapleBrandKindsMapping;

@Repository
public interface PaFapleBrandKindsMappingRepository extends JpaRepository<PaFapleBrandKindsMapping, String> {

	  
	@Query(value = " select bm.brandKindsName "
			+ "   from PaFapleBrandKindsMapping bm "
			+ "  where bm.paLmsdKey = :paLmsdKey "
			)
	String getBrandKindsName(String paLmsdKey);
}
