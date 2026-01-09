package com.cware.netshopping.palton.counsel.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

@Service("palton.counsel.paLtonCounselDAO")
public class PaLtonCounselDAO extends AbstractPaDAO{

	/** 롯데온 상품Q&A 조회 - 상담 등록 대상 조회
     * @param 
     * @return List
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaLtonQna() throws Exception{
    	return (List<PaqnamVO>) list("palton.counsel.selectPaLtonQna",null);
    }

    /**
     * 롯데온 상품Q&A답변처리 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaLtonAnsQna(ParamMap paramMap) {
		return (List<PaqnamVO>) list("palton.counsel.selectPaLtonAnsQna", paramMap.get());
	}

	/**
     * 롯데온 상품Q&A답변처리 - 결과처리
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public int updatePaQnaTrans(PaqnamVO paQna) {
		return update("palton.counsel.updatePaQnaTrans", paQna);
	}
	/**
     * 롯데온 상품Q&A답변처리 - TPAQNADT MAX SEQ 조회(PA_COUNSEL_SEQ + 1)
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public String selectPaQnaDtMaxSeq(PaqnamVO paQna) {
		return (String) selectByPk("palton.counsel.selectPaQnaDtMaxSeq", paQna);
	}
	
	/**
	 * 롯데온 상품Q&A답변처리 - 상담완료 데이터 저장.
	 * @param PaqnamVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPaQnaDt(PaqnamVO paQna) throws Exception{
		return insert("palton.counsel.insertPaQnaDt", paQna);
	}

	  /**
     * 롯데온 판매자 연락답변 등록 답변처리 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> updateSellerContact() throws Exception{
		return (List<PaqnamVO>) list("palton.counsel.updateSellerContact", null);
	}
	
	 /**
     * 롯데온 상품Q&A답변처리 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> updateSellerContact(ParamMap paramMap) {
		return (List<PaqnamVO>) list("palton.counsel.updateSellerContact", paramMap.get());
	}
	
	  /**
     * 롯데온 판매자연락답변등록 - 상담 등록 대상 조회
     * @param PaqnamVO
     * @return List
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaLtonSellerContract() throws Exception{
		return (List<PaqnamVO>) list("palton.counsel.selectPaLtonSellerContract", null);
	}

	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectSellerInquiryList() throws Exception{
		return (List<PaqnamVO>) list("palton.counsel.selectSellerInquiryList", null);
	}

	@SuppressWarnings("unchecked")
	public List<PaqnamVO> updateSellerInquiry(ParamMap paramMap) {
		return (List<PaqnamVO>) list("palton.counsel.updateSellerInquiry", paramMap.get());
	}
    
	
}
