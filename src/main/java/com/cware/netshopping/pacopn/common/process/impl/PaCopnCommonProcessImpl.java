package com.cware.netshopping.pacopn.common.process.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.model.PaCopnCertification;
import com.cware.netshopping.domain.model.PaCopnDocment;
import com.cware.netshopping.domain.model.PaCopnOption;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.pacopn.common.process.PaCopnCommonProcess;
import com.cware.netshopping.pacopn.common.repository.PaCopnCommonDAO;

@Service("pacopn.common.paCopnCommonProcess")
public class PaCopnCommonProcessImpl extends AbstractService implements PaCopnCommonProcess{

	@Resource(name = "pacopn.common.paCopnCommonDAO")
	private PaCopnCommonDAO paCopnCommonDAO;

	@Override
	public String savePaCopnCategoryMetaInfo(List<PaCopnOption> attrList, List<PaCopnDocment> docsList, List<PaCopnCertification> certiList) throws Exception {
		
		String rtnMsg = "000000";
		int executedRtn = 0;
		
		for(int i=0; i<attrList.size(); i++){			
			executedRtn = paCopnCommonDAO.insertPaCopnOption(attrList.get(i));
			if(executedRtn < 1){
				throw processException("msg.cannot_save", new String[] { "TPACOPNCTGRUNITLIST INSERT" });
			}
		}
		for(int i=0; i<docsList.size(); i++){
			executedRtn = paCopnCommonDAO.insertPaCopnDocument(docsList.get(i));
			if(executedRtn < 1){
				throw processException("msg.cannot_save", new String[] { "TPACOPNCTGRDOCSLIST INSERT" });
			}
		}
		for(int i=0; i<certiList.size(); i++){
			executedRtn = paCopnCommonDAO.insertPaCopnCertification(certiList.get(i));
			if(executedRtn < 1){
				throw processException("msg.cannot_save", new String[] { "TPACOPNCTGRCERTILIST INSERT" });
			}
		}
		
		return rtnMsg;
	}

	@Override
	public Map<String, String> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		return paCopnCommonDAO.selectEntpShipInsertList(paEntpSlip);
	}

	@Override
	public String savePaCopnEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;

		try {
			executedRtn = paCopnCommonDAO.insertPaCopnEntpSlip(paEntpSlip);
			if (executedRtn < 0) {
				log.info("TPAENTPSLIP insert fail");
				throw processException("msg.cannot_save", new String[]{"TPAENTPSLIP INSERT"});
			}
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}

		return rtnMsg;
	}

	@Override
	public List<Object> selectEntpShipUpdateList(String paAddrGb) throws Exception {
		return paCopnCommonDAO.selectEntpShipUpdateList(paAddrGb);
	}

	@Override
	public String updatePaCopnEntpSlip(PaEntpSlip paEntpSlip) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;
		
		try{
			executedRtn = paCopnCommonDAO.updatePaCopnEntpSlip(paEntpSlip);
			if(executedRtn < 0){
				log.info("TPAENTPSLIP update fail");
				throw processException("msg.cannot_save", new String[]{"TPAENTPSLIP UPDATE"});
			}
		}catch(Exception e){
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}
	
}
