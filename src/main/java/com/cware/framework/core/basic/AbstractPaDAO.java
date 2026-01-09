package com.cware.framework.core.basic;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;

/**
 * @Class Name : AbstractSSGDAO.java
 * @Description : Spring 의 iBatis 연동 지원을 Annotation 형식으로 쉽게 처리하기 위한 공통 parent DAO 클래스이다.
 *  Spring 에서 iBatis 연동을 지원하는 org.springframework.orm.ibatis3.support.SqlSessionDaoSupport 을 extends 하고 있으며
 *  CRUD 와 관련한 대표적인 method 를 간단하게 호출할 수 있도록 Wrapping 하고 있어 사용자 DAO 에서 iBatis SqlSessionDaoSupport 호출을 쉽게 하며
 *  Bean 생성 시 Annotation 기반으로 sqlMapClient 을 쉽게 injection 할 수 있는 공통 로직을 포함하고 있다.
 *  
 *  한진택배 DB 연결을 위한 DAO
 * @Modification Information
 *
 * @author 황인철
 * @since 2015.04.21
 * @version 1.0
 *
 * Copyright (C) 2010 by Commerceware All right reserved.
 */
@SuppressWarnings({"rawtypes" })
public class AbstractPaDAO extends DaoSupport {

	/**
	 * source : org.mybatis.spring.support.SqlSessionDaoSupport
	 */
    private SqlSession sqlSession;

    /**
     * source : org.mybatis.spring.support.SqlSessionDaoSupport
     */
    private boolean externalSqlSession;

    /**
     * source : org.mybatis.spring.support.SqlSessionDaoSupport
     * @param sqlSessionFactory
     */
    @Autowired(required = false)
    @Resource(name = "sqlSessionFactory")
    public final void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        if (!this.externalSqlSession) {
            this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
        }
    }

    /**
     * source : org.mybatis.spring.support.SqlSessionDaoSupport
     * @param sqlSessionTemplate
     */
    @Autowired(required = false)
    public final void setSqlSessionTemplateSSG(SqlSessionTemplate sqlSessionTemplateSSG) {
        this.sqlSession = sqlSessionTemplateSSG;
        this.externalSqlSession = true;
    }

    /**
     * Users should use this method to get a SqlSession to call its statement methods
     * This is SqlSession is managed by spring. Users should not commit/rollback/close it
     * because it will be automatically done.
     *
     * source : org.mybatis.spring.support.SqlSessionDaoSupport
     *
     * @return Spring managed thread safe SqlSession
     */
    public final SqlSession getSqlSession() {
        return this.sqlSession;
    }

    /**
     * {@inheritDoc}
     *
     * source : org.mybatis.spring.support.SqlSessionDaoSupport
     */
    protected void checkDaoConfig() {
        Assert.notNull(this.sqlSession, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
    }
	
    /**
     * AbstractSSGDAO 는 base class 로만 사용되며 해당 인스턴스를 직접 생성할 수 없도록 
     * protected constructor 로 선언하였음.
     */
    protected AbstractPaDAO() {
        // PMD abstract Rule - If the class is intended to be used as a base class only (not to be instantiated
        // directly)
        // a protected constructor can be provided prevent direct instantiation
    }

    /**
     * 입력 처리 SQL mapping 을 실행한다.
     * @param queryId
     *        - 입력 처리 SQL mapping 쿼리 ID
     * @param parameterObject
     *        - 입력 처리 SQL mapping 입력 데이터를 세팅한 파라메터 객체(보통 VO 또는 Map)
     * @return Database 에 갱신된 rows
     *        - Mybatis 의 경우 selectKey 를 사용하여 key 를 딴 경우 해당 key값은 parameterObject 에 값이 자동 세팅된다.
     */
    public int insert(String queryId, Object parameterObject) {
        return getSqlSession().insert(queryId, parameterObject);
    }

    /**
     * 수정 처리 SQL mapping 을 실행한다.
     * @param queryId
     *        - 수정 처리 SQL mapping 쿼리 ID
     * @param parameterObject
     *        - 수정 처리 SQL mapping 입력 데이터(key 조건 및 변경 데이터)를 세팅한 파라메터 객체(보통 VO 또는 Map)
     * @return DBMS가 지원하는 경우 update 적용 결과 count
     */
    public int update(String queryId, Object parameterObject) {
        return getSqlSession().update(queryId, parameterObject);
    }

    /**
     * 삭제 처리 SQL mapping 을 실행한다.
     * @param queryId
     *        - 삭제 처리 SQL mapping 쿼리 ID
     * @param parameterObject
     *        - 삭제 처리 SQL mapping 입력 데이터(일반적으로 key 조건)를 세팅한 파라메터 객체(보통 VO 또는 Map)
     * @return DBMS가 지원하는 경우 delete 적용 결과 count
     */
    public int delete(String queryId, Object parameterObject) {
        return getSqlSession().delete(queryId, parameterObject);
    }

    /**
     * pk 를 조건으로 한 단건조회 처리 SQL mapping 을 실행한다.
     * @param queryId
     *        - 단건 조회 처리 SQL mapping 쿼리 ID
     * @param parameterObject
     *        - 단건 조회 처리 SQL mapping 입력 데이터(key)를 세팅한 파라메터 객체(보통 VO 또는 Map)
     * @return 결과 객체 - SQL mapping 파일에서 지정한 resultClass/resultMap 에 의한 단일 결과 객체(보통 VO 또는 Map)
     */
    public Object selectByPk(String queryId, Object parameterObject) {
    	getSqlSession().clearCache();
        return getSqlSession().selectOne(queryId,
            parameterObject);
    }

    /**
     * 리스트 조회 처리 SQL mapping 을 실행한다.
     * @param queryId
     *        - 리스트 조회 처리 SQL mapping 쿼리 ID
     * @param parameterObject
     *        - 리스트 조회 처리 SQL mapping 입력 데이터(조회 조건)를 세팅한 파라메터 객체(보통 VO 또는 Map)
     * @return 결과 List 객체 - SQL mapping 파일에서 지정한 resultClass/resultMap 에 의한 결과 객체(보통 VO 또는 Map)의 List
     */
    public List list(String queryId, Object parameterObject) {
    	getSqlSession().clearCache();
        return getSqlSession().selectList(queryId, parameterObject);
    }

}
