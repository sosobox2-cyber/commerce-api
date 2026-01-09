package com.cware.netshopping.pacopn.sales.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pacopnsettlement;

public interface PaCopnSalesService {

//	public ParamMap selectPaCopnCompareProcessTx(ParamMap paramMap) throws Exception;

//	public ParamMap selectPaCopnCompareProcessMonthTx(ParamMap paramMap) throws Exception;

	public String savePaCopnSettlementTx(List<Pacopnsettlement> pacopnSettlementList, ParamMap paramMap) throws Exception;

	public int selectChkPaCopnAccount(ParamMap paramMap) throws Exception;

}
