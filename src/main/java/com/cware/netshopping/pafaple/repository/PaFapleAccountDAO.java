package com.cware.netshopping.pafaple.repository;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.model.PaFapleOrderSettlement;
import com.cware.netshopping.domain.model.PaFapleShipSettlement;

@Repository("pafaple.account.PaFapleAccountDAO")
public class PaFapleAccountDAO extends AbstractPaDAO {
	
	/**
	 * 패션플러스 : 주문내역(TPAFAPLE_ORDERLIST) INSERT 
	 * @param PaFapleOrderListVO
	 * @return int 
	 * @throws Exception
	 */

	public int insertPaFapleOrderSettlementList(PaFapleOrderSettlement paFapleOrderSettlement) {
		return insert("pafaple.account.insertPaFapleOrderSettlementList", paFapleOrderSettlement);
	}

	public int selectPaFapleOrderSettlementList(PaFapleOrderSettlement paFapleOrderSettlement) {
		return (Integer) selectByPk("pafaple.account.selectPaFapleOrderSettlementList",paFapleOrderSettlement);	
	}

	public int selectPaFapleShipSettlementList(PaFapleShipSettlement paFapleShipSettlement) {
		return (Integer) selectByPk("pafaple.account.selectPaFapleShipSettlementList",paFapleShipSettlement);	
	}

	public int insertPaFapleShipSettlementList(PaFapleShipSettlement paFapleShipSettlement) {
		return insert("pafaple.account.insertPaFapleShipSettlementList", paFapleShipSettlement);
	}

}
