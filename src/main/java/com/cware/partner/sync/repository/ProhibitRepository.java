package com.cware.partner.sync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.cware.partner.sync.domain.entity.Prohibitdt;


@Repository
public interface ProhibitRepository extends JpaRepository<Prohibitdt, String> {


	
}

