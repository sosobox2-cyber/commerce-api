package com.cware.netshopping.pagmkt.counsel.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

@Service("pagmkt.counsel.paGmktCounselDAO")
@SuppressWarnings("unchecked")
public class PaGmktCounselDAO extends AbstractPaDAO {


    /**
     * 제휴사 : G마켓 상담 등록 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
    public List<PaqnamVO> selectPaGmktQna(String siteGb) throws Exception{
    	return (List<PaqnamVO>) list("pagmkt.counsel.selectPaGmktQna", siteGb);
    }
    
	/**
     * G마켓 상품Q&A답변처리 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
	public List<PaqnamVO> selectPaGmktAnsQna(ParamMap paramMap) throws Exception{
		return (List<PaqnamVO>) list("pagmkt.counsel.selectPaGmktAnsQna", paramMap.get());
	}
	
	/**
     * G마켓 상품Q&A답변처리 - 결과처리
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public int updatePaQnaTrans(PaqnamVO paQna) throws Exception{
		return update("pagmkt.counsel.updatePaQnaTrans", paQna);
	}
	
	/**
     * G마켓 상품Q&A답변처리 - TPAQNADT MAX SEQ 조회(PA_COUNSEL_SEQ + 1)
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public String selectPaQnaDtMaxSeq(PaqnamVO paQna) throws Exception{
		return (String) selectByPk("pagmkt.counsel.selectPaQnaDtMaxSeq", paQna);
	}
	
	/**
	 * G마켓 상품Q&A답변처리 - 상담완료 데이터 저장.
	 * @param PaqnamVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPaQnaDt(PaqnamVO paQna) throws Exception{
		return insert("pagmkt.counsel.insertPaQnaDt", paQna);
	}
	
	/**
     * G마켓 긴급메세지 등록 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
    public List<PaqnamVO> selectPaGmktUrgentQna(String siteGb) throws Exception{
    	return (List<PaqnamVO>) list("pagmkt.counsel.selectPaGmktUrgentQna", siteGb);
    }
    
    /**
     * 제휴사 : G마켓 상담 등록 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
    public List<PaqnamVO> selectPaIacQna() throws Exception{
    	return (List<PaqnamVO>) list("pagmkt.counsel.selectPaIacQna", null);
    }
}
