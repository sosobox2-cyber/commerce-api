package com.cware.netshopping.palton.stockcheck.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.palton.stockcheck.process.PaLtonStockCheckProcess;
import com.cware.netshopping.palton.stockcheck.service.PaLtonStockCheckService;

@Service("palton.stockcheck.PaLtonStockCheckService")
public class PaLtonStockCheckServiceImpl  extends AbstractService implements PaLtonStockCheckService {

    @Resource(name = "palton.stockcheck.PaLtonStockCheckProcess")
    private PaLtonStockCheckProcess paLtonStockCheckProcess;
    
    @Override
    public PaLtonGoodsdtMappingVO selectStockCheck(PaLtonGoodsdtMappingVO paLtonGoodsdtMappingVO) throws Exception {
        return paLtonStockCheckProcess.selectStockCheck(paLtonGoodsdtMappingVO);
    }
    
}
