
package com.cware.netshopping.pawemp.sales.repository;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pawemp.sales.model.SettleOptionSales;
import com.cware.netshopping.pawemp.sales.model.SettleShipSales;


@Service("pawemp.sales.paWempSalesDAO")
public class PaWempSalesDAO extends AbstractPaDAO{
	
	public Integer selectChkPaWempSalesOpt(ParamMap paramMap) throws Exception {
		return (Integer) selectByPk("pawemp.sales.selectChkPaWempSalesOpt", paramMap.get());
	}
	
	public Integer selectChkPaWempSalesShip(ParamMap paramMap) throws Exception {
		return (Integer) selectByPk("pawemp.sales.selectChkPaWempSalesShip", paramMap.get());
	}
	
	public int insertPaWempSalesOpt(SettleOptionSales paWempsales) throws Exception{
		return insert("pawemp.sales.insertPaWempSalesOpt", paWempsales);
	}
	
	public int insertPaWempSalesShip(SettleShipSales paWempsales) throws Exception{
		return insert("pawemp.sales.insertPaWempSalesShip", paWempsales);
	}
	
	/**
	 * 정산매출(옵션)정보 삭제
	 * @param ParamMap
	 * @return int 
	 * @throws Exception
	 */
	public int deletePaWempSalesOpt(ParamMap paramMap) throws Exception{
		return delete("pawemp.sales.deletePaWempSalesOpt", paramMap.get());
	}
	
	/**
	 * 정산매출(배송비)정보 삭제
	 * @param ParamMap
	 * @return int 
	 * @throws Exception
	 */
	public int deletePaWempSalesShip(ParamMap paramMap) throws Exception{
		return delete("pawemp.sales.deletePaWempSalesShip", paramMap.get());
	}

	/**
	 * 옵션 매출 내역 존재여부 확인
	 * @param SettleOptionSales
	 * @return
	 * @throws Exception
	 */
	public int selectPaSettlementOptExists(SettleOptionSales paWempSales) throws Exception{
		return (Integer) selectByPk("pawemp.sales.selectPaSettlementOptExists", paWempSales);
	}

	/**
	 * 배송비 매출 내역 존재여부 확인
	 * @param SettleShipSales
	 * @return
	 * @throws Exception
	 */
	public int selectPaSettlementShipExists(SettleShipSales paWempSales) throws Exception{
		return (Integer) selectByPk("pawemp.sales.selectPaSettlementShipExists", paWempSales);
	}
}