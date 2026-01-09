package com.cware.netshopping.panaver.counsel.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaNaverCounselService {

	/**
	 * 네이버 상품Q&A답변처리 대상 조회
	 * @param 
	 * @return List<PaqnamVO>
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaNaverAnsQna() throws Exception;

	public List<PaqnamVO> selectPaNaverCustAnsQna() throws Exception;

}
