package com.cware.netshopping.pa11st.broadcast.process.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pa11st.broadcast.process.Pa11stBroadcastProcess;
import com.cware.netshopping.pa11st.broadcast.repository.Pa11stBroadcastDAO;

@Service("pa11st.broadcast.pa11stBroadcastProcess")
public class Pa11stBroadcastProcessImpl extends AbstractService implements Pa11stBroadcastProcess {

	@Resource(name = "pa11st.broadcast.pa11stBroadcastDAO")
	private Pa11stBroadcastDAO pa11stBroadcastDAO;

	@Override
	public List<MultiframescheVO> selectBroadcastScheListOld(ParamMap paramMap) throws Exception {
		return pa11stBroadcastDAO.selectBroadcastScheListOld(paramMap);
	}
	
	
	
	@Override
	public void refreshBroadReplaceGoods(ParamMap paramMap) throws Exception {
		int checkReplace = 0;
		try {
			checkReplace = pa11stBroadcastDAO.selectCheckReplaceGoodsYn(paramMap);
			if(checkReplace > 0) {
				pa11stBroadcastDAO.deletePaBroadReplace(paramMap);
				pa11stBroadcastDAO.insertPaBroadReplace(paramMap);				
			}
		}catch(Exception e){
			throw processException("msg.cannot_save", new String[] { "TPABROADREPLACE REFRESH FAIL" });
		}
	}
	
	@Override
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception {
		return pa11stBroadcastDAO.selectBroadcastScheList(paramMap);
	}
	
	@Override
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception {
		return pa11stBroadcastDAO.selectAlternativeBroadcastLink(paGroupCode);
	}
	
	@Override
	public String save11stBroadSche(Pabroadsche pabroadshce) throws Exception {
		String rtnMsg   = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			if(pabroadshce.getModifyFlag().equals("I")){
				executedRtn = pa11stBroadcastDAO.insertBroadSche(pabroadshce);
				if(executedRtn == 1){
					log.info("TPABROADSCHE Insert Success");
				} else {
					log.info("TPABROADSCHE Insert Fail");
					log.info("SEQ_FRAME_NO : " + pabroadshce.getSeqFrameNo());
					throw processException("msg.cannot_save", new String[] { "TPABROADSCHE INSERT" });
				}
			} else if(pabroadshce.getModifyFlag().equals("U")){
				executedRtn = pa11stBroadcastDAO.updateBroadSche(pabroadshce);
				if(executedRtn == 1){
					log.info("TPABROADSCHE Update Success");
				} else {
					log.info("TPABROADSCHE Update Fail");
					log.info("SEQ_FRAME_NO : " + pabroadshce.getSeqFrameNo());
					throw processException("msg.cannot_save", new String[] { "TPABROADSCHE UPDATE" });
				}
			} else {
				executedRtn = pa11stBroadcastDAO.deleteBroadSche(pabroadshce);
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
			pa11stBroadcastDAO.updateCycleSeqUseEnd(pabroadreplace);
			pa11stBroadcastDAO.updateCycleSeqUseStart(pabroadreplace);
		}catch(Exception e){
			throw processException("msg.cannot_save", new String[] { "TPABROADREPLACE UPDATE" });
		}
	}
	
	@Override
	public List<MultiframescheVO> select11stBroadcastScheList(ParamMap paramMap) throws Exception {
		return pa11stBroadcastDAO.select11stBroadcastScheList(paramMap);
	}
}
