package com.cware.netshopping.pacopn.sales.repository;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pacopnsettlement;

@Service("pacopn.sales.paCopnSalesDAO")
public class PaCopnSalesDAO extends AbstractPaDAO{

	/**
	 * 쿠팡 매출 조회 내역 insert
	 * @param settlementList
	 * @return
	 * @throws Exception
	 */
	public int insertPaCopnSettlement(Pacopnsettlement settlementList) throws Exception{
		return insert("pacopn.sales.insertPaCopnSettlement", settlementList);
	}

	/**
	 * 쿠팡 매출 내역 존재여부 확인
	 * @param settlementList
	 * @return
	 * @throws Exception
	 */
	public int selectPaSettlementExists(Pacopnsettlement settlementList) throws Exception{
		return (Integer) selectByPk("pacopn.sales.selectPaSettlementExists", settlementList);
	}

	public int selectChkPaCopnAccount(ParamMap paramMap) throws Exception{
		return (Integer) selectByPk("pacopn.sales.selectChkPaCopnAccount", paramMap.get());
	}

	public int deletePaCopnAccount(ParamMap paramMap) throws Exception{
		return delete("pacopn.sales.deletePaCopnAccount", paramMap.get());
	}

}
