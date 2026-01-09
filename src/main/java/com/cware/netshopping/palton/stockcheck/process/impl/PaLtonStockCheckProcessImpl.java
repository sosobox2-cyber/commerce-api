package com.cware.netshopping.palton.stockcheck.process.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.palton.stockcheck.process.PaLtonStockCheckProcess;
import com.cware.netshopping.palton.stockcheck.repository.PaLtonStockCheckDAO;

@Service("palton.stockcheck.PaLtonStockCheckProcess")
public class PaLtonStockCheckProcessImpl extends AbstractService implements PaLtonStockCheckProcess {

	@Resource(name = "palton.stockcheck.PaLtonStockCheckDAO")
	private PaLtonStockCheckDAO paLtonStockCheckDAO;
    
	
	@Override
	public PaLtonGoodsdtMappingVO selectStockCheck(PaLtonGoodsdtMappingVO paLtonGoodsdtMappingVO) throws Exception {
	    return paLtonStockCheckDAO.selectStockCheck(paLtonGoodsdtMappingVO);
	}
	
}
