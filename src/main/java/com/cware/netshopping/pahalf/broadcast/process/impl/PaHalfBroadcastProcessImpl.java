package com.cware.netshopping.pahalf.broadcast.process.impl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pahalf.broadcast.process.PaHalfBroadcastProcess;
import com.cware.netshopping.pahalf.broadcast.repository.PaHalfBroadcastDAO;

@Service("pahalf.broadcast.paHalfBroadcastProcess")
public class PaHalfBroadcastProcessImpl extends AbstractService implements PaHalfBroadcastProcess {

	@Resource(name = "pahalf.broadcast.paHalfBroadcastDAO")
	private PaHalfBroadcastDAO paHalfBroadcastDAO;

	@Override
	public void refreshBroadReplaceGoods(ParamMap paramMap) throws Exception {
		int checkReplace = 0;
		try {
			checkReplace = paHalfBroadcastDAO.selectCheckReplaceGoodsYn(paramMap);
			if(checkReplace > 0) {
				paHalfBroadcastDAO.deletePaBroadReplace(paramMap);
				paHalfBroadcastDAO.insertPaBroadReplace(paramMap);				
			}
		}catch(Exception e){
			throw processException("msg.cannot_save", new String[] { "TPABROADREPLACE REFRESH FAIL" });
		}
		
	}
	
	@Override
	public List<MultiframescheVO> selectHalfBroadcastScheList(ParamMap paramMap) throws Exception {
		return paHalfBroadcastDAO.selectHalfBroadcastScheList(paramMap);
	}
	
	@Override
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception {
		return paHalfBroadcastDAO.selectAlternativeBroadcastLink(paGroupCode);
	}
	
	@Override
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception {
		return paHalfBroadcastDAO.selectBroadcastScheList(paramMap);
	}

	@Override
	public String saveHalfBroadSche(Pabroadsche pabroadshce) throws Exception {
		String rtnMsg   = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			if(pabroadshce.getModifyFlag().equals("I")){
				executedRtn = paHalfBroadcastDAO.insertBroadSche(pabroadshce);
				if(executedRtn == 1){
					log.info("TPABROADSCHE Insert Success");
				} else {
					log.info("TPABROADSCHE Insert Fail");
					log.info("SEQ_FRAME_NO : " + pabroadshce.getSeqFrameNo());
					throw processException("msg.cannot_save", new String[] { "TPABROADSCHE INSERT" });
				}
			} else if(pabroadshce.getModifyFlag().equals("U")){
				executedRtn = paHalfBroadcastDAO.updateBroadSche(pabroadshce);
				if(executedRtn == 1){
					log.info("TPABROADSCHE Update Success");
				} else {
					log.info("TPABROADSCHE Update Fail");
					log.info("SEQ_FRAME_NO : " + pabroadshce.getSeqFrameNo());
					throw processException("msg.cannot_save", new String[] { "TPABROADSCHE UPDATE" });
				}
			} else {
				executedRtn = paHalfBroadcastDAO.deleteBroadSche(pabroadshce);
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
			paHalfBroadcastDAO.updateCycleSeqUseEnd(pabroadreplace);
			paHalfBroadcastDAO.updateCycleSeqUseStart(pabroadreplace);
		}catch(Exception e){
			throw processException("msg.cannot_save", new String[] { "TPABROADREPLACE UPDATE" });
		}
		
	}


}
