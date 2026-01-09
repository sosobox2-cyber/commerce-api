package com.cware.netshopping.pawemp.sales.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pawemp.sales.model.SettleOptionSales;
import com.cware.netshopping.pawemp.sales.model.SettleShipSales;
import com.cware.netshopping.pawemp.sales.process.PaWempSalesProcess;
import com.cware.netshopping.pawemp.sales.service.PaWempSalesService;


@Service("pawemp.sales.paWempSalesService")
public class PaWempSalesServiceImpl  extends AbstractService implements PaWempSalesService {

	@Resource(name = "pawemp.sales.paWempSalesProcess")
    private PaWempSalesProcess paWempSalesProcess;
	
	@Override
	public String saveSettelmentOptListTx(List<SettleOptionSales> paWempSaleslist, String paCode) throws Exception{
		return paWempSalesProcess.saveSettelmentOptList(paWempSaleslist, paCode);
	}
	
	@Override
	public String saveSettelmentShipListTx(List<SettleShipSales> paWempSaleslist, String paCode) throws Exception{
		return paWempSalesProcess.saveSettelmentShipList(paWempSaleslist, paCode);
	}
	
	@Override
	public int selectChkPaWempSalesOpt(ParamMap paramMap) throws Exception {
		return paWempSalesProcess.selectChkPaWempSalesOpt(paramMap);
	}
	
	@Override
	public int selectChkPaWempSalesShip(ParamMap paramMap) throws Exception {
		return paWempSalesProcess.selectChkPaWempSalesShip(paramMap);
	}

	@Override
	public int deletePaWempSales(ParamMap paramMap) throws Exception {
		return paWempSalesProcess.deletePaWempSales(paramMap);
	}
}