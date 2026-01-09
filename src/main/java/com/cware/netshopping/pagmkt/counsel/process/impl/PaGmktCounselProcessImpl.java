package com.cware.netshopping.pagmkt.counsel.process.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.pagmkt.counsel.process.PaGmktCounselProcess;
import com.cware.netshopping.pagmkt.counsel.repository.PaGmktCounselDAO;

@Service("pagmkt.counsel.paGmktCounselProcess")
public class PaGmktCounselProcessImpl extends AbstractService implements PaGmktCounselProcess {

    @Resource(name = "pagmkt.counsel.paGmktCounselDAO")
    private PaGmktCounselDAO paGmktCounselDAO;
    
    /**
	 * G마켓 상품Q&A답변처리 대상 조회
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaGmktAnsQna(ParamMap paramMap) throws Exception {
		return paGmktCounselDAO.selectPaGmktAnsQna(paramMap);
	}
	
	/**
	 * G마켓 상품Q&A답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktQnaTrans(PaqnamVO paQna) throws Exception{
		int executedRtn = 0;
		String paCounselDtSeq = "";
		
		executedRtn = paGmktCounselDAO.updatePaQnaTrans(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",
					new String[] { "TPAQNAM update" });
		}
		paCounselDtSeq = paGmktCounselDAO.selectPaQnaDtMaxSeq(paQna);
		
		paQna.setPaCounselDtSeq(paCounselDtSeq);
		executedRtn = paGmktCounselDAO.insertPaQnaDt(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",
					new String[] { "TPAQNADT insert" });
		}
		return Constants.SAVE_SUCCESS;
	}
}
