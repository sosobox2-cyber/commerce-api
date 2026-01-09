package com.cware.netshopping.pagmkt.counsel.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaGmktCounselService {
	
	/**
	 * G마켓 상품Q&A답변처리 대상 조회
	 * @param 
	 * @return List<PaqnamVO>
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaGmktAnsQna(ParamMap paramMap) throws Exception;
	
	/**
	 * G마켓 상품Q&A답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktQnaTransTx(PaqnamVO paQna) throws Exception;
        
}
