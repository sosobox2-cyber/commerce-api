package com.cware.partner.common.repository;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class CommonRepository {

	@PersistenceContext
    EntityManager entityManager;


    public Timestamp currentDate() {
        return  (Timestamp) entityManager.createNativeQuery("select sysdate from dual").getSingleResult();
    }

}
