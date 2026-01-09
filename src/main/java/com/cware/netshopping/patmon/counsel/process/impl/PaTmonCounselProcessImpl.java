package com.cware.netshopping.patmon.counsel.process.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.patmon.counsel.process.PaTmonCounselProcess;
import com.cware.netshopping.patmon.counsel.repository.PaTmonCounselDAO;

@Service("patmon.counsel.paTmonCounselProcess")
public class PaTmonCounselProcessImpl extends AbstractService implements PaTmonCounselProcess{
		
	
	@Resource(name = "patmon.counsel.paTmonCounselDAO")
	PaTmonCounselDAO paTmonCounselDAO;

	@Override
	public List<PaqnamVO> selectPaTmonAnsQna(ParamMap paramMap) throws Exception {
		return paTmonCounselDAO.selectPaTmonAnsQna(paramMap);
	}

	@Override
	public String savePaTmonQnaTrans(PaqnamVO paQna) throws Exception {
		int executedRtn = 0;
		String paCounselDtSeq = "";
		
		executedRtn = paTmonCounselDAO.updatePaQnaTrans(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",
					new String[] { "TPAQNAM update" });
		}
		paCounselDtSeq = paTmonCounselDAO.selectPaQnaDtMaxSeq(paQna);
		
		paQna.setPaCounselDtSeq(paCounselDtSeq);
		executedRtn = paTmonCounselDAO.insertPaQnaDt(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",
					new String[] { "TPAQNADT insert" });
		}
		
		return Constants.SAVE_SUCCESS;
	}

	@Override
	public int selectPaCounselNoCheck(String pa_counsel_no) throws Exception {
		return paTmonCounselDAO.selectPaCounselNoCheck(pa_counsel_no);
	}
}
