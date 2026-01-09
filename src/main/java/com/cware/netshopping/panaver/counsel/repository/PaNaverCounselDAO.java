package com.cware.netshopping.panaver.counsel.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;

@Service("panaver.counsel.paNaverCounselDAO")
public class PaNaverCounselDAO extends AbstractPaDAO{

	/**
	 * 네이버 상담 등록 대상 조회
	 * @param siteGb
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaNaverQna() throws Exception{
		return (List<PaqnamVO>) list("panaver.counsel.selectPaNaverQna", null);
	}

	 /**
     * 네이버 상품Q&A답변처리 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaNaverAnsQna() throws Exception{
		return (List<PaqnamVO>) list("panaver.counsel.selectPaNaverAnsQna", null);
	}

	/**
     * 네이버 판매자 문의 - 상담 등록 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaNaverCustQna() throws Exception{
    	return (List<PaqnamVO>) list("panaver.counsel.selectPaNaverCustQna", null);
    }

	 /**
     * TPAQNAMOMENT INSERT
     * @param Paqnamoment
     * @return Integer
     * @throws Exception
     */
    public int insertPaQnaMoment(Paqnamoment paqnamoment) throws Exception{
	return insert("panaver.counsel.insertPaQnaMoment", paqnamoment);
    }
   
    /**
     * 네이버 판매자 문의 답변처리 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaNaverCustAnsQna() throws Exception{
		return (List<PaqnamVO>) list("panaver.counsel.selectPaNaverCustAnsQna", null);
	}
}