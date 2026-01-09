package com.cware.netshopping.pagmkt.counsel.process;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaGmktCounselProcess {
	
	/**
	 * G마켓 상품Q&A답변처리 대상 조회
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaGmktAnsQna(ParamMap paramMap) throws Exception;
	
	/**
	 * G마켓 상품Q&A답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktQnaTrans(PaqnamVO paQna) throws Exception;
	
}
