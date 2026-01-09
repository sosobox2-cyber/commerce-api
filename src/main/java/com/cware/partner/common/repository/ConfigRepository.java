package com.cware.partner.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cware.partner.common.domain.entity.Config;

@Repository
public interface ConfigRepository extends JpaRepository<Config, String> {

}
