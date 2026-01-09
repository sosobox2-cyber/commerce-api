package com.cware.netshopping.pawemp.sales.process;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pawemp.sales.model.SettleOptionSales;
import com.cware.netshopping.pawemp.sales.model.SettleShipSales;

public interface PaWempSalesProcess {

	/**
	 * 정산매출(옵션)정보 저장 IF_PAWEMPAPI_05_003
	 * @param List<SettleProdSales>
	 * @return String
	 * @throws Exception
	 */
	public String saveSettelmentOptList(List<SettleOptionSales> paWempSaleslist, String paCode) throws Exception;
	
	/**
	 * 정산매출(배송비)정보 저장 IF_PAWEMPAPI_05_002
	 * @param List<SettleShipSales>
	 * @return String
	 * @throws Exception
	 */
	public String saveSettelmentShipList(List<SettleShipSales> paWempSaleslist, String paCode) throws Exception;
	
	/**
	 * 정산매출 정보 삭제
	 * @param ParamMap
	 * @return int 
	 * @throws Exception
	 */
	public int deletePaWempSales(ParamMap paramMap) throws Exception;
	
	/**
	 * 정산 옵션 매출 저장 - 정산일자 중복데이터 체크
	 * @param ParamMap
	 * @return int 
	 * @throws Exception
	 */
	public int selectChkPaWempSalesOpt(ParamMap paramMap) throws Exception;
	
	/**
	 * 정산 배송비 매출 저장 - 정산일자 중복데이터 체크
	 * @param ParamMap
	 * @return int 
	 * @throws Exception
	 */
	public int selectChkPaWempSalesShip(ParamMap paramMap) throws Exception;
}