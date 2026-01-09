package com.cware.netshopping.pa11st.counsel.process.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.pa11st.counsel.process.Pa11stCounselProcess;
import com.cware.netshopping.pa11st.counsel.repository.Pa11stCounselDAO;

@Service("pa11st.counsel.pa11stCounselProcess")
public class Pa11stCounselProcessImpl extends AbstractService implements Pa11stCounselProcess{

	@Resource(name = "pa11st.counsel.pa11stCounselDAO")
	private Pa11stCounselDAO pa11stCounselDAO;
	

	/**
	 * 11번가 상품Q&A답변처리 대상 조회
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPa11stAnsQna(ParamMap paramMap) throws Exception {
		return pa11stCounselDAO.selectPa11stAnsQna(paramMap);
	}
	
	/**
	 * 11번가 상품Q&A답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public String savePa11stQnaTrans(PaqnamVO paQna) throws Exception{
		int executedRtn = 0;
		String paCounselDtSeq = "";
		
		executedRtn = pa11stCounselDAO.updatePaQnaTrans(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",
					new String[] { "TPAQNAM update" });
		}
		paCounselDtSeq = pa11stCounselDAO.selectPaQnaDtMaxSeq(paQna);
		
		paQna.setPaCounselDtSeq(paCounselDtSeq);
		executedRtn = pa11stCounselDAO.insertPaQnaDt(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",
					new String[] { "TPAQNADT insert" });
		}
		return Constants.SAVE_SUCCESS;
	}
	
	public String saveQnaMonitering(PaqnamVO paQna) throws Exception{
		int executedRtn = 0;
		executedRtn = pa11stCounselDAO.insertPaMonitering(paQna);
		if (executedRtn != 1) {
			log.error("QNA TPAMONITERING INSERT ERROR");
		}
		return Constants.SAVE_SUCCESS;
	}
	
	/**
	 * 11번가 긴급메세지 상담 존재 체크
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public String selectCheckPacounsel(ParamMap paramMap) throws Exception{
	    
	    return pa11stCounselDAO.selectCheckPacounsel(paramMap);
	    
	}
	
	/**
	 * 11번가 긴급메세지 seq 채번
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String selectGetMaxPacounselSeq(ParamMap paramMap) throws Exception{
	    return pa11stCounselDAO.selectGetMaxPacounselSeq(paramMap);
	}
}