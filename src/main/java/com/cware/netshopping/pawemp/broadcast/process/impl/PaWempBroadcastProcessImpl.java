package com.cware.netshopping.pawemp.broadcast.process.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pawemp.broadcast.process.PaWempBroadcastProcess;
import com.cware.netshopping.pawemp.broadcast.repository.PaWempBroadcastDAO;

@Service("pawemp.broadcast.paWempBroadcastProcess")
public class PaWempBroadcastProcessImpl extends AbstractService implements PaWempBroadcastProcess {

	@Resource(name = "pawemp.broadcast.paWempBroadcastDAO")
	private PaWempBroadcastDAO paWempBroadcastDAO;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;

	@Override
	public void refreshBroadReplaceGoods(ParamMap paramMap) throws Exception {
		int checkReplace = 0;
		try {
			checkReplace = paWempBroadcastDAO.selectCheckReplaceGoodsYn(paramMap);
			if(checkReplace > 0) {
				paWempBroadcastDAO.deletePaBroadReplace(paramMap);
				paWempBroadcastDAO.insertPaBroadReplace(paramMap);				
			}
		}catch(Exception e){
			throw processException("msg.cannot_save", new String[] { "TPABROADREPLACE REFRESH FAIL" });
		}
	}
	
	@Override
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception {
		return paWempBroadcastDAO.selectBroadcastScheList(paramMap);
	}

	@Override
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception {
		return paWempBroadcastDAO.selectAlternativeBroadcastLink(paGroupCode);
	}
	
	@Override
	public String saveWempBroadSche(Pabroadsche pabroadshce) throws Exception {
		String rtnMsg   = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			if(pabroadshce.getModifyFlag().equals("I")){
				executedRtn = paWempBroadcastDAO.insertWempBroadSche(pabroadshce);
				if(executedRtn == 1){
					log.info("TPABROADSCHE Insert Success");
				} else {
					log.info("TPABROADSCHE Insert Fail");
					log.info("SEQ_FRAME_NO : " + pabroadshce.getSeqFrameNo());
					throw processException("msg.cannot_save", new String[] { "TPABROADSCHE INSERT" });
				}
			}else{
				executedRtn = paWempBroadcastDAO.updateWempBroadSche(pabroadshce);
				if(executedRtn == 1){
					log.info("TPABROADSCHE Update Success");
				} else {
					log.info("TPABROADSCHE Update Fail");
					log.info("SEQ_FRAME_NO : " + pabroadshce.getSeqFrameNo());
					throw processException("msg.cannot_save", new String[] { "TPABROADSCHE UPDATE" });
				}
			}	
		}catch(Exception e){
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}
	
	@Override
	public void updatePaBroadReplace(Pabroadreplace pabroadreplace) throws Exception {
		try {
			paWempBroadcastDAO.updateCycleSeqUseEnd(pabroadreplace);
			paWempBroadcastDAO.updateCycleSeqUseStart(pabroadreplace);
		}catch(Exception e){
			throw processException("msg.cannot_save", new String[] { "TPABROADREPLACE UPDATE" });
		}
	}
}
