package com.cware.netshopping.pa11st.stockcheck.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.StockCheckVO;
import com.cware.netshopping.pa11st.stockcheck.process.Pa11stStockCheckProcess;
import com.cware.netshopping.pa11st.stockcheck.service.Pa11stStockCheckService;

@Service("pa11st.stock.pa11stStockCheckService")
public class Pa11stStockCheckServiceImpl  extends AbstractService implements Pa11stStockCheckService {

    @Resource(name = "pa11st.stock.pa11stStockCheckProcess")
    private Pa11stStockCheckProcess pa11stStockCheckProcess;

    @Override
    public List<StockCheckVO> selectStockCheck(List<HashMap<String, String>> paramMap) throws Exception {
	return pa11stStockCheckProcess.selectStockCheck(paramMap);
    }
    
    @Override
    public String selectCheckOpenApiCode() throws Exception{
	return pa11stStockCheckProcess.selectCheckOpenApiCode();
    }

}
