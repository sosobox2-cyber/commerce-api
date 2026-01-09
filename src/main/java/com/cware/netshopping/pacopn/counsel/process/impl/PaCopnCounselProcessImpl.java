package com.cware.netshopping.pacopn.counsel.process.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.pacopn.counsel.process.PaCopnCounselProcess;
import com.cware.netshopping.pacopn.counsel.repository.PaCopnCounselDAO;

@Service("pacopn.counsel.paCopnCounselProcess")
public class PaCopnCounselProcessImpl extends AbstractService implements PaCopnCounselProcess{
	
	@Resource(name = "pacopn.counsel.paCopnCounselDAO")
	private PaCopnCounselDAO paCopnCounselDAO;
	
	
	@Override
	public List<PaqnamVO> selectPaCopnCounselReply(ParamMap paramMap) throws Exception {
		return paCopnCounselDAO.selectPaCopnCounselReply(paramMap);
	}
	
	@Override
	public String savePaCopnCounselReply(PaqnamVO qnaReply) throws Exception{
		String paCounselDtSeq = "";
		
		int executedRtn = 0;
		
		executedRtn = paCopnCounselDAO.updatePaQnaMTrans(qnaReply);
		if(executedRtn != 1){
			throw processException("msg.cannot_save", new String[]{"TPAQNAM UPDATE"});
		}
		
		paCounselDtSeq = paCopnCounselDAO.selectPaQnaDtMaxSeq(qnaReply);
		qnaReply.setPaCounselDtSeq(paCounselDtSeq);
		
		executedRtn = paCopnCounselDAO.insertPaQnaDt(qnaReply);
		if(executedRtn != 1){
			throw processException("msg.cannot_save", new String[]{"TPAQNAM INSERT"});
		}
		
		return Constants.SAVE_SUCCESS;
	}
}
