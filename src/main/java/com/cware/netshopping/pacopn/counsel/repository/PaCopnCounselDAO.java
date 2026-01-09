package com.cware.netshopping.pacopn.counsel.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;

@Service("pacopn.counsel.paCopnCounselDAO")
@SuppressWarnings("unchecked")
public class PaCopnCounselDAO extends AbstractPaDAO{
	
	/**
     * 상품별 고객문의 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
    public List<PaqnamVO> selectPaCopnQna() throws Exception{
    	return (List<PaqnamVO>) list("pacopn.counsel.selectPaCopnQna", null);
    }
    
    /**
     * 상품고객문의답변처리 결과 저장 - TPAQNAM UPDATE
     * @param qnaReply
     * @return int
     * @throws Exception
     */
    public int updatePaQnaMTrans(PaqnamVO qnaReply) throws Exception{
    	return update("pacopn.counsel.updatePaQnaMTrans", qnaReply);
    }
    
    /**
     * 상품고객문의답변처리 결과 저장 - TPAQNADT SEQ 조회
     * @param qnaReply
     * @return String
     * @throws Exception
     */
    public String selectPaQnaDtMaxSeq(PaqnamVO qnaReply) throws Exception{
    	return (String) selectByPk("pacopn.counsel.selectPaQnaDtMaxSeq", qnaReply);
    }
    
    /**
     * 상품고객문의답변처리 결과 저장 - TPAQNADT INSERT
     * @param qnaReply
     * @return int
     * @throws Exception
     */
    public int insertPaQnaDt(PaqnamVO qnaReply) throws Exception{
    	return insert("pacopn.counsel.insertPaQnaDt", qnaReply);
    }

    /**
     * 쿠팡 콜센터 고객문의 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
    public List<PaqnamVO> selectPaCopnCallQna() throws Exception{
    	return (List<PaqnamVO>) list("pacopn.counsel.selectPaCopnUrgentQna", null);
    }

    /**
     * 쿠팡콜센터문의답변처리 대상 조회
     * @return List<PaqnamVO>
     * @throws Exception
     */
    public List<PaqnamVO> selectPaCopnCounselReply(ParamMap paramMap) throws Exception{
    	return (List<PaqnamVO>) list("pacopn.counsel.selectPaCopnCounselReply", paramMap.get());
    }
    
	 /**
     * 쿠팡콜센터문의 저장(PAQNAMOMENT)
     * @param paqnamoment
     * @return
     * @throws Exception
     */
	public int insertPaQnaMoment(Paqnamoment paqnamoment) throws Exception{
		return insert("pacopn.counsel.insertPaQnaMoment", paqnamoment);
	}
	

}
