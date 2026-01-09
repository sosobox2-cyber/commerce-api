package com.cware.netshopping.paqeen.repository;

import org.springframework.stereotype.Repository;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.model.PaQeenSettlement;

@Repository("paqeen.account.paQeenAccountDAO")
public class PaQeenAccountDAO extends AbstractPaDAO {
	
	public int selectSettlementSeq(PaQeenSettlement paQeenSettlement) {
		return (Integer) selectByPk("paqeen.account.selectSettlementSeq",paQeenSettlement);
	}

	public int insertPaQeenSettlement(PaQeenSettlement settlement) {
		return insert("paqeen.account.insertPaQeenSettlement", settlement);
	}
}
