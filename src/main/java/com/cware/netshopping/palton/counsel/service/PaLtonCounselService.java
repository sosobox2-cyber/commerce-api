package com.cware.netshopping.palton.counsel.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaLtonCounselService {
	
	/**
	 * 롯데온 상품Q&A답변처리 대상 조회
	 * @param 
	 * @return List<PaqnamVO>
	 * @throws Exception
	 */
	List<PaqnamVO> selectPaLtonAnsQna(ParamMap paramMap) throws Exception;

	/**
	 * 롯데온 상품Q&A답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	String savePaLtonQnaTransTx(PaqnamVO paQna) throws Exception;

	/**
	 * 롯데온 판매자 연락답변 등록 처리 결과저장.
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	List<PaqnamVO> updateSellerContact(ParamMap paramMap) throws Exception;

	/**
	 * 롯데온 판매자 문의 답변 등록 처리 결과저장.
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	List<PaqnamVO> updateSellerInquiry(ParamMap paramMap) throws Exception;


}
