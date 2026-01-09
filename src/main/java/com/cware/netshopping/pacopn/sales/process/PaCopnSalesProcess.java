package com.cware.netshopping.pacopn.sales.process;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pacopnsettlement;

public interface PaCopnSalesProcess {

//	public ParamMap selectPaCopnCompareProcess(ParamMap paramMap)throws Exception;

//	public ParamMap selectPaCopnCompareProcessMonth(ParamMap paramMap) throws Exception;

	public String savePaCopnSettlement(List<Pacopnsettlement> pacopnSettlementList, ParamMap paramMap) throws Exception;

	public int selectChkPaCopnAccount(ParamMap paramMap) throws Exception;

}
