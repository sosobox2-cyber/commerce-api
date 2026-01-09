package com.cware.netshopping.pafaple.process.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pafaple.process.PaFapleBroadcastProcess;
import com.cware.netshopping.pafaple.repository.PaFapleBroadcastDAO;


@Service("pafaple.broadcast.paFapleBroadcastProcess")
public class PaFapleBroadcastProcessImpl extends AbstractProcess implements PaFapleBroadcastProcess {

	@Resource(name = "pafaple.broadcast.paFapleBroadcastDAO")
	private PaFapleBroadcastDAO paFapleBroadcastDAO;
	
	@Override
	public void refreshBroadReplaceGoods(ParamMap paramMap) throws Exception {
		int checkReplace = 0;
		try {
			checkReplace = paFapleBroadcastDAO.selectCheckReplaceGoodsYn(paramMap);
			if(checkReplace > 0) {
				paFapleBroadcastDAO.deletePaBroadReplace(paramMap);
				paFapleBroadcastDAO.insertPaBroadReplace(paramMap);				
			}
		}catch(Exception e){
			throw processException("msg.cannot_save", new String[] { "TPABROADREPLACE REFRESH FAIL" });
		}
	}
	
	@Override
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception {
		return paFapleBroadcastDAO.selectBroadcastScheList(paramMap);
	}
	
	@Override
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception {
		return paFapleBroadcastDAO.selectAlternativeBroadcastLink(paGroupCode);
	}
	
	@Override
	public String saveFapleBroadSche(Pabroadsche pabroadshce) throws Exception {
		String rtnMsg   = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			if(pabroadshce.getModifyFlag().equals("I")){
				executedRtn = paFapleBroadcastDAO.insertBroadSche(pabroadshce);
				if(executedRtn == 1){
					log.info("TPABROADSCHE Insert Success");
				} else {
					log.info("TPABROADSCHE Insert Fail");
					log.info("SEQ_FRAME_NO : " + pabroadshce.getSeqFrameNo());
					throw processException("msg.cannot_save", new String[] { "TPABROADSCHE INSERT" });
				}
			} else if(pabroadshce.getModifyFlag().equals("U")){
				executedRtn = paFapleBroadcastDAO.updateBroadSche(pabroadshce);
				if(executedRtn == 1){
					log.info("TPABROADSCHE Update Success");
				} else {
					log.info("TPABROADSCHE Update Fail");
					log.info("SEQ_FRAME_NO : " + pabroadshce.getSeqFrameNo());
					throw processException("msg.cannot_save", new String[] { "TPABROADSCHE UPDATE" });
				}
			} else {
				executedRtn = paFapleBroadcastDAO.deleteBroadSche(pabroadshce);
				if(executedRtn == 1){
					log.info("TPABROADSCHE Delete Success");
				} else {
					log.info("TPABROADSCHE Delete Fail");
					log.info("SEQ_FRAME_NO : " + pabroadshce.getSeqFrameNo());
					throw processException("msg.cannot_save", new String[] { "TPABROADSCHE DELETE" });
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
			paFapleBroadcastDAO.updateCycleSeqUseEnd(pabroadreplace);
			paFapleBroadcastDAO.updateCycleSeqUseStart(pabroadreplace);
		}catch(Exception e){
			throw processException("msg.cannot_save", new String[] { "TPABROADREPLACE UPDATE" });
		}
	}
	
	@Override
	public List<MultiframescheVO> selectFapleBroadcastScheList(ParamMap paramMap) throws Exception {
		return paFapleBroadcastDAO.selectFapleBroadcastScheList(paramMap);
	}

}
