package com.cware.netshopping.pa11st.stockcheck.process.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.StockCheckVO;
import com.cware.netshopping.pa11st.stockcheck.process.Pa11stStockCheckProcess;
import com.cware.netshopping.pa11st.stockcheck.repository.Pa11stStockCheckDAO;

@Service("pa11st.stock.pa11stStockCheckProcess")
public class Pa11stStockCheckProcessImpl extends AbstractService implements Pa11stStockCheckProcess {

	@Resource(name = "pa11st.stock.pa11stStockCheckDAO")
	private Pa11stStockCheckDAO pa11stStckCheckDAO;

	@Override
	public List<StockCheckVO> selectStockCheck(List<HashMap<String, String>> paramMap) throws Exception{
	    
	    List<StockCheckVO> stockCheckList = new ArrayList<StockCheckVO>();	   
	    for(int i=0; i < paramMap.size(); i++){
		HashMap<String, String>  param = new HashMap<String, String>();		
		param = paramMap.get(i);			
		StockCheckVO vo = new StockCheckVO();
		vo = pa11stStckCheckDAO.selectStockCheck(param);		
		stockCheckList.add(vo);		
	    }
	    return stockCheckList;
	}
	
	@Override
	public String selectCheckOpenApiCode() throws Exception{

	    return pa11stStckCheckDAO.selectCheckOpenApiCode();
	}
}
