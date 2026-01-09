package com.cware.netshopping.palton.common.process.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaLtonAttrList;
import com.cware.netshopping.domain.model.PaLtonDispCtgr;
import com.cware.netshopping.domain.model.PaLtonDispList;
import com.cware.netshopping.domain.model.PaLtonPdItmsList;
import com.cware.netshopping.domain.model.PaLtonRetrieveCode;
import com.cware.netshopping.domain.model.PaLtonSettlement;
import com.cware.netshopping.domain.model.PaLtonStdCtgr;
import com.cware.netshopping.palton.common.process.PaLtonCommonProcess;
import com.cware.netshopping.palton.common.repository.PaLtonCommonDAO;

@Service("palton.common.paLtonCommonProcessImpl")
public class PaLtonCommonProcessImpl extends AbstractService implements PaLtonCommonProcess{
	
	@Autowired
	PaLtonCommonDAO paLtonCommonDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Override
	public String insertPaDisplayCategory(PaLtonDispCtgr displayData) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			executedRtn = paLtonCommonDAO.insertPaDisplayCategory(displayData);
			if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPALTONDISPLAYCATEGORY INSERT" });
		} catch(Exception e) {
			rtnMsg = e.getMessage();
		}
		
		return rtnMsg;
	}
	
	@Override
	public String saveLtonStdCategoryInfo(List<PaLtonDispList> disInfoList, List<PaLtonAttrList> attrInfoList, List<PaLtonPdItmsList> pdItmsInfoList, PaLtonStdCtgr stdCtgrData) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			for(int i = 0; i < disInfoList.size(); i++) {
				executedRtn = paLtonCommonDAO.insertDisInfoList(disInfoList.get(i));
				if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPALTONSTANDARDDISPLIST INSERT" });
			}
			/*
			for(int i = 0; i < attrInfoList.size(); i++) {
				executedRtn = paLtonCommonDAO.insertAttrInfoList(attrInfoList.get(i));
				if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPALTONSTANDARDATTRLIST INSERT" });
			}
			for(int i = 0; i < pdItmsInfoList.size(); i++) {
				executedRtn = paLtonCommonDAO.insertPdItmsInfoList(pdItmsInfoList.get(i));
				if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPALTONSTANDARDPDITEMSLIST INSERT" });
			}
			*/
			executedRtn = paLtonCommonDAO.insertStdCtgrInfoList(stdCtgrData);
			if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPALTONSTANDARDCATEGORY INSERT" });
		} catch(Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public String saveLtonBrand(List<PaBrand> paBrandList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		PaBrand paBrand = null;
		
		try {
			for(int i = 0; i < paBrandList.size(); i++) {
				paBrand = paBrandList.get(i);
				executedRtn = paLtonCommonDAO.insertPaLtonBrand(paBrand);
				if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPABRAND INSERT" });
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		
		return rtnMsg;
	}

	@Override
	public HashMap<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		return paLtonCommonDAO.selectSlipInsertList(paEntpSlip);
	}

	@Override
	public String savePaLtonEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = paLtonCommonDAO.insertPaLtonEntpSlip(paEntpSlip);
			
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
	public List<HashMap<String,Object>> selectEntpSlipUpdateList() throws Exception {
		return paLtonCommonDAO.selectEntpSlipUpdateList();
	}

	@Override
	public String updatePaLtonEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			executedRtn = paLtonCommonDAO.updatePaLtonEntpSlip(paEntpSlip);
			
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
	public List<HashMap<String,Object>> selectEntpSlipCost(ParamMap paramMap)  throws Exception{
		return paLtonCommonDAO.selectEntpSlipCost(paramMap);
	}
	
	@Override
	public List<HashMap<String,Object>> selectAddShipCost(ParamMap paramMap)  throws Exception{
		return paLtonCommonDAO.selectAddShipCost(paramMap);
	}

	@Override
	public String savePaLtonCustShipCost(ParamMap custShipCostMap) {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try{
			executedRtn = paLtonCommonDAO.updatePaLtonCustShipCost(custShipCostMap);
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPACUSTSHIPCOST UPDATE" });
			}
		}catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}
	
	@Override
	public String savePaLtonAddCustShipCost(ParamMap addShipCostMap) {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try{
			executedRtn = paLtonCommonDAO.updatePaLtonAddShipCost(addShipCostMap);
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPALTONADDSHIPCOST UPDATE" });
			}
		}catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public String insertPaLtonOrigin(List<PaLtonRetrieveCode> paLtonOrigin) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			for(int i = 0; i < paLtonOrigin.size(); i++) {
				executedRtn = paLtonCommonDAO.insertPaLtonOrigin(paLtonOrigin.get(i));
				if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPAORIGIN INSERT" });
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
		}
		
		return rtnMsg;
	}

	@Override
	public void saveLtonSettlement(PaLtonSettlement settlementData, ParamMap apiDataMap) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		String paLtonSettlementNo = "";
		int executedRtn = 0;
		
		if(settlementData.getDelYn().equals("y") || settlementData.getDelYn().equals("Y")) {
			executedRtn = paLtonCommonDAO.deletePaLtonSettlement(apiDataMap);
		}
		
		hashMap.put("sequence_type", "PALTON_SETTLEMENT_NO");
		paLtonSettlementNo = (String)systemDAO.selectSequenceNo(hashMap);
		
		settlementData.setPaltonSettlementNo(paLtonSettlementNo);
		
		executedRtn = paLtonCommonDAO.insertPaLtonSettlement(settlementData);
		if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPALTONSETTLEMNET INSERT" });
	}

}
