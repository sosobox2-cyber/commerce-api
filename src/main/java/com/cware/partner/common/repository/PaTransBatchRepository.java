package com.cware.partner.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cware.partner.common.domain.entity.PaTransBatch;

@Repository
public interface PaTransBatchRepository extends JpaRepository<PaTransBatch, String> {

}
