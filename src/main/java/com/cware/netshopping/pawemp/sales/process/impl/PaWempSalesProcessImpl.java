package com.cware.netshopping.pawemp.sales.process.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.pacommon.sales.repository.PaSalesDAO;
import com.cware.netshopping.pawemp.sales.model.SettleOptionSales;
import com.cware.netshopping.pawemp.sales.model.SettleShipSales;
import com.cware.netshopping.pawemp.sales.process.PaWempSalesProcess;
import com.cware.netshopping.pawemp.sales.repository.PaWempSalesDAO;

@Service("pawemp.sales.paWempSalesProcess")
public class PaWempSalesProcessImpl extends AbstractService implements PaWempSalesProcess{

	@Resource(name = "pawemp.sales.paWempSalesDAO")
	private PaWempSalesDAO paWempSalesDAO;

	@Resource(name = "pacommon.sales.paSalesDAO")
	private PaSalesDAO paSalesDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Override
	public String saveSettelmentOptList(List<SettleOptionSales> paWempSaleslist, String paCode) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.CWARE_JAVA_DATETIME_FORMAT);
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		String paWempSalesSeq = ""; 
		HashMap<String, Object> hashMap = null;
		int existsChk = 0;
		
		for(SettleOptionSales paWempSales : paWempSaleslist){
			
			paWempSales.setPaCode(paCode);
			existsChk = paWempSalesDAO.selectPaSettlementOptExists(paWempSales);
			if(existsChk > 0){
				continue;
			}
			
			hashMap = new HashMap<String, Object>();
    		hashMap.put("sequence_type", "PAWEMP_SALES_NO");
    		paWempSalesSeq = (String)systemDAO.selectSequenceNo(hashMap);
    		
    		if (paWempSalesSeq.equals("")) {
    		    throw processException("msg.cannot_create", new String[] { "PAWEMP_SALES_NO" });
    		}
    		//수수료율 퍼센트값 포맷 변경 (ex: 8.00 >> 8, 8.50 >> 8.5)
    		BigDecimal data = new BigDecimal(paWempSales.getFeeRate());
    		paWempSales.setFeeRate(data.stripTrailingZeros().toPlainString());
    		paWempSales.setPaWempSalesNo(paWempSalesSeq);
    		paWempSales.setInsertId(Constants.PA_WEMP_PROC_ID);
    		paWempSales.setInsertDate(new Timestamp(sdf.parse(systemDAO.getSysdatetime()).getTime()));
			executedRtn = paWempSalesDAO.insertPaWempSalesOpt(paWempSales);	
			if (executedRtn < 1) { //= 오류
				throw processException("msg.cannot_save", new String[] { "TPAWEMPSALES INSERT" });
			}
		}
		return rtnMsg;
	}
	
	@Override
	public String saveSettelmentShipList(List<SettleShipSales> paWempSaleslist, String paCode) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.CWARE_JAVA_DATETIME_FORMAT);
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		String paWempSalesSeq = ""; 
		HashMap<String, Object> hashMap = null;
		int existsChk = 0;
		
		for(SettleShipSales paWempSales : paWempSaleslist){
			
			paWempSales.setPaCode(paCode);
			existsChk = paWempSalesDAO.selectPaSettlementShipExists(paWempSales);
			if(existsChk > 0){
				continue;
			}
			
			hashMap = new HashMap<String, Object>();
    		hashMap.put("sequence_type", "PAWEMP_SALES_NO");
    		paWempSalesSeq = (String)systemDAO.selectSequenceNo(hashMap);
    		
    		if (paWempSalesSeq.equals("")) {
    		    throw processException("msg.cannot_create", new String[] { "PAWEMP_SALES_NO" });
    		}
			
    		paWempSales.setPaWempSalesNo(paWempSalesSeq);
    		paWempSales.setInsertId(Constants.PA_WEMP_PROC_ID);
    		paWempSales.setInsertDate(new Timestamp(sdf.parse(systemDAO.getSysdatetime()).getTime()));
			executedRtn = paWempSalesDAO.insertPaWempSalesShip(paWempSales);	
			if (executedRtn < 1) { //= 오류
				throw processException("msg.cannot_save", new String[] { "TPAWEMPSALES INSERT" });
			}
		}
		return rtnMsg;
	}
	
	@Override
	public int selectChkPaWempSalesOpt(ParamMap paramMap) throws Exception {
		return paWempSalesDAO.selectChkPaWempSalesOpt(paramMap);
	}
	
	@Override
	public int selectChkPaWempSalesShip(ParamMap paramMap) throws Exception {
		return paWempSalesDAO.selectChkPaWempSalesShip(paramMap);
	}

	@Override
	public int deletePaWempSales(ParamMap paramMap) throws Exception {
		int executedRtn = 0;
		
		executedRtn = paWempSalesDAO.deletePaWempSalesOpt(paramMap);
		if (executedRtn > 0){
			executedRtn = paWempSalesDAO.deletePaWempSalesShip(paramMap);
		}
		return executedRtn;
	}
}