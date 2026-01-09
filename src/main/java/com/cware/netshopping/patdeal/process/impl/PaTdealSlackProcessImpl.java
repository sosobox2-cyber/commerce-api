package com.cware.netshopping.patdeal.process.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.model.PaTdealSlackGoods;
import com.cware.netshopping.patdeal.process.PaTdealSlackProcess;
import com.cware.netshopping.patdeal.repository.PaTdealSlackDAO;


@Service("patdeal.slack.paTdealSlackProcess")
public class PaTdealSlackProcessImpl extends AbstractService implements PaTdealSlackProcess {

	@Resource(name = "patdeal.slack.paTdealSlackDAO")
    private PaTdealSlackDAO paTdealSlackDAO;
	
	@Override
	public List<PaTdealSlackGoods> slackTransferTdealList(HttpServletRequest request) throws Exception {
		return paTdealSlackDAO.slackTransferTdealList(request);
	}

}	
