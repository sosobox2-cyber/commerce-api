package com.cware.netshopping.pacopn.sales.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pacopnsettlement;
import com.cware.netshopping.pacopn.sales.process.PaCopnSalesProcess;
import com.cware.netshopping.pacopn.sales.service.PaCopnSalesService;

@Service("pacopn.sales.paCopnSalesService")
public class PaCopnSalesServiceImpl extends AbstractService implements PaCopnSalesService{

	@Resource(name = "pacopn.sales.paCopnSalesProcess")
    private PaCopnSalesProcess paCopnSalesProcess;

	
//	@Override
//	public ParamMap selectPaCopnCompareProcessTx(ParamMap paramMap) throws Exception {
//		return paCopnSalesProcess.selectPaCopnCompareProcess(paramMap);
//	}
//
//	@Override
//	public ParamMap selectPaCopnCompareProcessMonthTx(ParamMap paramMap) throws Exception {
//		return paCopnSalesProcess.selectPaCopnCompareProcessMonth(paramMap);
//	}

	@Override
	public String savePaCopnSettlementTx(List<Pacopnsettlement> pacopnSettlementList, ParamMap paramMap) throws Exception {
		return paCopnSalesProcess.savePaCopnSettlement(pacopnSettlementList, paramMap);
	}

	@Override
	public int selectChkPaCopnAccount(ParamMap paramMap) throws Exception {
		return paCopnSalesProcess.selectChkPaCopnAccount(paramMap);
	}
}
