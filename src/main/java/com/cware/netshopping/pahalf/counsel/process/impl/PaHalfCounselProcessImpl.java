package com.cware.netshopping.pahalf.counsel.process.impl;


import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.pahalf.counsel.process.PaHalfCounselProcess;
import com.cware.netshopping.pahalf.counsel.repository.PaHalfCounselDAO;

@Service("pahalf.counsel.paHalfCounselProcess")
public class PaHalfCounselProcessImpl extends AbstractService implements PaHalfCounselProcess{

	@Resource(name = "pahalf.counsel.paHalfCounselDAO")
	private PaHalfCounselDAO paHalfCounselDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;

	@Override
	public List<PaqnamVO> selectPaHalfAnsQna(ParamMap paramMap) throws Exception {
		return paHalfCounselDAO.selectPaHalfAnsQna(paramMap);
	}

	@Override
	public void savePaHalfQnaTransTx(PaqnamVO paQna) throws Exception {
		int executedRtn = 0;
		String paCounselDtSeq = "";
		
		executedRtn = paHalfCounselDAO.updatePaQnaTrans(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAQNAM update" });
		}
		
		paCounselDtSeq = paHalfCounselDAO.selectPaQnaDtMaxSeq(paQna);
		
		paQna.setPaCounselDtSeq(paCounselDtSeq);
		executedRtn = paHalfCounselDAO.insertPaQnaDt(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAQNADT insert" });
		}
		
	}
}