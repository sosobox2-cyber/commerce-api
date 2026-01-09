package com.cware.netshopping.paintp.counsel.repository;

import java.util.List;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

@Service("paintp.counsel.paIntpCounselDAO")
public class PaIntpCounselDAO extends AbstractPaDAO{	
	
	/**
     * 인터파크 상품Q&A답변처리 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaIntpAnsQna(ParamMap paramMap) throws Exception{
		return (List<PaqnamVO>) list("paintp.counsel.selectPaIntpAnsQna", paramMap.get());
	}
	
	/**
     * 인터파크 상품Q&A답변처리 - 결과처리
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public int updatePaQnaTrans(PaqnamVO paQna) throws Exception{
		return update("paintp.counsel.updatePaQnaTrans", paQna);
	}
	
	/**
     * 인터파크 상품Q&A답변처리 - TPAQNADT MAX SEQ 조회(PA_COUNSEL_SEQ + 1)
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public String selectPaQnaDtMaxSeq(PaqnamVO paQna) throws Exception{
		return (String) selectByPk("paintp.counsel.selectPaQnaDtMaxSeq", paQna);
	}
	
	/**
	 * 인터파크 상품Q&A답변처리 - 상담완료 데이터 저장.
	 * @param PaqnamVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPaQnaDt(PaqnamVO paQna) throws Exception{
		return insert("paintp.counsel.insertPaQnaDt", paQna);
	}
	
	  /** 인터파크 상품Q&A 조회 - 상담 등록 대상 조회
	     * @param 
	     * @return Integer
	     * @throws Exception
	     */
	    @SuppressWarnings("unchecked")
		public List<PaqnamVO> selectPaIntpQna(String msgGb) throws Exception{
	    	return (List<PaqnamVO>) list("paintp.counsel.selectPaIntpQna", msgGb);
	    }
	    
	    /**
	     * 인터파크 긴급메세지 - 상담 등록 대상 조회
	     * @param 
	     * @return Integer
	     * @throws Exception
	     */
	    @SuppressWarnings("unchecked")
		public List<PaqnamVO> selectPaIntpUrgentQna(String msgGb) throws Exception{
	    	return (List<PaqnamVO>) list("paintp.counsel.selectPaIntpUrgentQna", msgGb);
	    }
	    
	
	
}