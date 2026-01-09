package com.cware.netshopping.pagmkt.stockcheck.process.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.PaGmktGoodsdtMappingVO;
import com.cware.netshopping.pagmkt.stockcheck.process.PaGmktStockCheckProcess;
import com.cware.netshopping.pagmkt.stockcheck.repository.PaGmktStockCheckDAO;

@Service("pagmkt.stockcheck.PaGmktStockCheckProcess")
public class PaGmktStockCheckProcessImpl extends AbstractService implements PaGmktStockCheckProcess {

	@Resource(name = "pagmkt.stockcheck.PaGmktStockCheckDAO")
	private PaGmktStockCheckDAO paGmktStockCheckDAO;
    
	
	@Override
	public PaGmktGoodsdtMappingVO selectStockCheck(PaGmktGoodsdtMappingVO paGmktGoodsdtMappingVO) throws Exception {
	    return paGmktStockCheckDAO.selectStockCheck(paGmktGoodsdtMappingVO);
	}
	
}
