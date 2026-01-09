package com.cware.netshopping.patmon.common.process.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaTmonSettlement;
import com.cware.netshopping.patmon.common.process.PaTmonCommonProcess;
import com.cware.netshopping.patmon.common.repository.PaTmonCommonDAO;

@Service("patmon.common.paTmonCommonProcess")
public class PaTmonCommonProcessImpl extends AbstractService implements PaTmonCommonProcess{
	
	@Resource(name = "patmon.common.paTmonCommonDAO")
	PaTmonCommonDAO paTmonCommonDAO;
	
	@Override
	public String savePaGoodsKinds(List<PaGoodsKinds> paGoodsKindsList) throws Exception {
        String rtnMsg 	= Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	
    	try {
			executedRtn = paTmonCommonDAO.deletePaGoodsKindsMomentList(paGoodsKindsList.get(0));
			if(executedRtn < 0){
				log.info("tpagoodskindsmoment delete fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDSMOMENT TMON DELETE" });
			}
    		
    		for(PaGoodsKinds paGoodsKinds:paGoodsKindsList){
    			executedRtn = paTmonCommonDAO.insertPaGoodsKindMomentsList(paGoodsKinds);
    			
    			if (executedRtn < 0) {
					log.info("tpagoodskindsmoment insert fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDSMOMENT TMON INSERT" });
    			}
    		}
    		
    		if(executedRtn > 0){
	    		executedRtn = paTmonCommonDAO.insertPaGoodsKindsList(paGoodsKindsList.get(0));
	    		if (executedRtn < 0) {
					log.info("tpagoodskinds insert fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS TMON INSERT" });
    			}
    		}
    	} catch (Exception e) {
    		rtnMsg = e.getMessage();
    		throw e;
    	}
    	return rtnMsg;
	}

	@Override
	public HashMap<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		return paTmonCommonDAO.selectSlipInsertList(paEntpSlip);
	}

	@Override
	public String savePaTmonEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg 	= Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = paTmonCommonDAO.insertPaTmonEntpSlip(paEntpSlip);
			
			if(executedRtn < 0){
				log.info("TPAENTPSLIP insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAENTPSLIP INSERT" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public List<HashMap<String, Object>> selectEntpSlipUpdateList() throws Exception {
		return paTmonCommonDAO.selectEntpSlipUpdateList();
	}

	@Override
	public String updatePaTmonEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg	= Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			executedRtn = paTmonCommonDAO.updatePaTmonEntpSlip(paEntpSlip);
			
			if(executedRtn < 0){
				log.info("TPAENTPSLIP update fail");
				throw processException("msg.cannot_save", new String[] { "TPAENTPSLIP UPDATE" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public List<HashMap<String, Object>> selectEntpSlipCost(ParamMap apiInfoMap) throws Exception {
		return paTmonCommonDAO.selectEntpSlipCost(apiInfoMap);
	}

	@Override
	public String updatePaTmonShipCost(ParamMap tmonShipCostMap) throws Exception {
		String rtnMsg 	= Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			executedRtn = paTmonCommonDAO.updatePaTmonShipCost(tmonShipCostMap);
			
			if(executedRtn < 0){
				log.info("TPATMONSIPCOST update fail");
				throw processException("msg.cannot_save", new String[] { "TPATMONSIPCOST UPDATE" });
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public String savePaTmonSettlement(List<PaTmonSettlement> paTmonSettlementList, ParamMap paramMap) throws Exception {
		String rtnMsg 	= Constants.SAVE_SUCCESS;
		PaTmonSettlement paTmonSettlement = null;
		int executedRtn = 0;
		int existsChk	= 0;
		Date salesDate  = DateUtil.toDate(paTmonSettlementList.get(0).getSalesDateTime(),"yyyy-MM-dd");	// 시간 포맷을 맞춰주기 위해서 
		
		String salesDateToString = DateUtil.toString(salesDate, "yyyy-MM-dd");
		paramMap.put("salesDateToString", salesDateToString);
		
		try {
			
			if("y".equals(paramMap.getString("delYn")) || "Y".equals(paramMap.getString("delYn"))) {
				executedRtn = paTmonCommonDAO.deletePaTmonSettlement(paramMap);
			}
			
			for(int i = 0 ; i < paTmonSettlementList.size(); i++) {
				paTmonSettlement = paTmonSettlementList.get(i);
				
				existsChk = paTmonCommonDAO.selectPaTmonSettlementExists(paTmonSettlement);
				
				if(existsChk > 0){
					continue;
				}
				
				executedRtn = paTmonCommonDAO.insertPaTmonSettlement(paTmonSettlementList.get(i));
				
				if(executedRtn < 0){
					log.info("TPATMONSETTLEMENT insert fail");
					throw processException("msg.cannot_save", new String[] { "TPATMONSETTLEMENT INSERT" });
				}
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}
}
