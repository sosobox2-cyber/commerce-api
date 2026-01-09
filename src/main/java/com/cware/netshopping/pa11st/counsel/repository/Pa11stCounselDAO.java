
package com.cware.netshopping.pa11st.counsel.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;


@Service("pa11st.counsel.pa11stCounselDAO")
public class Pa11stCounselDAO extends AbstractPaDAO{
	
	/**
     * 11번가 상품Q&A 조회 - 상담 등록 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPa11stQna() throws Exception{
    	return (List<PaqnamVO>) list("pa11st.counsel.selectPa11stQna", null);
    }
    
    /**
     * 11번가 긴급메세지 - 상담 등록 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPa11stUrgentQna() throws Exception{
    	return (List<PaqnamVO>) list("pa11st.counsel.selectPa11stUrgentQna", null);
    }

    /**
     * 11번가 상품Q&A답변처리 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPa11stAnsQna(ParamMap paramMap) throws Exception{
		return (List<PaqnamVO>) list("pa11st.counsel.selectPa11stAnsQna", paramMap.get());
	}
	
	/**
     * 11번가 상품Q&A답변처리 - 결과처리
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public int updatePaQnaTrans(PaqnamVO paQna) throws Exception{
		return update("pa11st.counsel.updatePaQnaTrans", paQna);
	}
	
	/**
     * 11번가 상품Q&A답변처리 - TPAQNADT MAX SEQ 조회(PA_COUNSEL_SEQ + 1)
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public String selectPaQnaDtMaxSeq(PaqnamVO paQna) throws Exception{
		return (String) selectByPk("pa11st.counsel.selectPaQnaDtMaxSeq", paQna);
	}
	
	/**
	 * 11번가 상품Q&A답변처리 - 상담완료 데이터 저장.
	 * @param PaqnamVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPaQnaDt(PaqnamVO paQna) throws Exception{
		return insert("pa11st.counsel.insertPaQnaDt", paQna);
	}
	public int insertPaMonitering(PaqnamVO paQna) throws Exception{
		return insert("pa11st.counsel.insertPaMonitering", paQna);
	}
	
	/**
	 * 11번가 긴급메세지 상담 존재 체크
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */	
	public String selectCheckPacounsel(ParamMap paramMap) throws Exception{
		return (String)selectByPk("pa11st.counsel.selectCheckPacounsel", paramMap.get());
	}
	
	/**
	 * 11번가 긴급메세지 상담 존재 체크
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public String selectGetMaxPacounselSeq(ParamMap paramMap) throws Exception{
		return (String)selectByPk("pa11st.counsel.selectGetMaxPacounselSeq", paramMap.get());
	}
}