package com.cware.netshopping.patdeal.repository;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.model.PaTdealSlackGoods;

@Repository("patdeal.slack.paTdealSlackDAO")
public class PaTdealSlackDAO extends AbstractPaDAO {

	@SuppressWarnings("unchecked")
	public List<PaTdealSlackGoods> slackTransferTdealList(HttpServletRequest request) throws Exception {
		return list("patdeal.slack.slackTransferTdealList", null);
	}

}