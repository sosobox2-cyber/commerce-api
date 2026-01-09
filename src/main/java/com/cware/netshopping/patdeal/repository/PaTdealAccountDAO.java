package com.cware.netshopping.patdeal.repository;

import org.springframework.stereotype.Repository;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.model.PaTdealSettlement;

@Repository("patdeal.account.paTdealAccountDAO")
public class PaTdealAccountDAO extends AbstractPaDAO {

	public int selectSettlementSeq(PaTdealSettlement settlement) {
		return (Integer) selectByPk("patdeal.account.selectSettlementSeq",settlement);
	}

	public int insertPaTdealSettlement(PaTdealSettlement settlement) {
		return insert("patdeal.account.insertPaTdealSettlement", settlement);
	}


}
