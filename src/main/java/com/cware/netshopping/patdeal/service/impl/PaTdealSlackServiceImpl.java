package com.cware.netshopping.patdeal.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.model.PaTdealSlackGoods;
import com.cware.netshopping.patdeal.process.PaTdealSlackProcess;
import com.cware.netshopping.patdeal.service.PaTdealSlackService;

@Service("patdeal.slack.paTdealSlackService")
public class PaTdealSlackServiceImpl extends AbstractService implements PaTdealSlackService {
	
	@Autowired
	@Qualifier("patdeal.slack.paTdealSlackProcess")
	PaTdealSlackProcess paTdealSlackProcess;

	@Override
	public List<PaTdealSlackGoods> slackTransferTdealList(HttpServletRequest request) throws Exception {
		return paTdealSlackProcess.slackTransferTdealList(request);
	}

}
