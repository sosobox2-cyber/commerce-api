package com.cware.netshopping.pagmkt.stockcheck.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.PaGmktGoodsdtMappingVO;
import com.cware.netshopping.pagmkt.stockcheck.process.PaGmktStockCheckProcess;
import com.cware.netshopping.pagmkt.stockcheck.service.PaGmktStockCheckService;

@Service("pagmkt.stockcheck.PaGmktStockCheckService")
public class PaGmktStockCheckServiceImpl  extends AbstractService implements PaGmktStockCheckService {

    @Resource(name = "pagmkt.stockcheck.PaGmktStockCheckProcess")
    private PaGmktStockCheckProcess paGmktStockCheckProcess;
    
    @Override
    public PaGmktGoodsdtMappingVO selectStockCheck(PaGmktGoodsdtMappingVO paGmktGoodsdtMappingVO) throws Exception {
        return paGmktStockCheckProcess.selectStockCheck(paGmktGoodsdtMappingVO);
    }
    
}
