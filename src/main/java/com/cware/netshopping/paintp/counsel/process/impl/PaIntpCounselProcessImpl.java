package com.cware.netshopping.paintp.counsel.process.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.paintp.counsel.process.PaIntpCounselProcess;
import com.cware.netshopping.paintp.counsel.repository.PaIntpCounselDAO;

@Service("paintp.counsel.paIntpCounselProcess")
public class PaIntpCounselProcessImpl extends AbstractService implements PaIntpCounselProcess{

	@Resource(name = "paintp.counsel.paIntpCounselDAO")
	private PaIntpCounselDAO paIntpCounselDAO;

	/**
	 * 인터파크 상품Q&A답변처리 대상 조회
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaIntpAnsQna(ParamMap paramMap) throws Exception {
		return paIntpCounselDAO.selectPaIntpAnsQna(paramMap);
	}
	
	/**
	 * 인터파크 상품Q&A답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public String savePaIntpQnaTransTx(PaqnamVO paQna) throws Exception{
		int executedRtn = 0;
		String paCounselDtSeq = "";
		
		executedRtn = paIntpCounselDAO.updatePaQnaTrans(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",
					new String[] { "TPAQNAM update" });
		}
		paCounselDtSeq = paIntpCounselDAO.selectPaQnaDtMaxSeq(paQna);
		
		paQna.setPaCounselDtSeq(paCounselDtSeq);
		executedRtn = paIntpCounselDAO.insertPaQnaDt(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",
					new String[] { "TPAQNADT insert" });
		}
		
		return Constants.SAVE_SUCCESS;
	}
}
