package com.cware.netshopping.panaver.counsel.process;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaNaverCounselProcess {

	/**
	 * 네이버 상품Q&A답변처리 대상 조회
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaNaverAnsQna() throws Exception;

	public List<PaqnamVO> selectPaNaverCustAnsQna() throws Exception;

}
