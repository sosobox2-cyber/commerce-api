package com.cware.netshopping.paintp.counsel.service;

import java.util.List;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaIntpCounselService {
	
	/**
	 * 인터파크 상품Q&A답변처리 대상 조회
	 * @param 
	 * @return List<PaqnamVO>
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaIntpAnsQna(ParamMap paramMap) throws Exception;
	
	/**
	 * 인터파크 상품Q&A답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public String savePaIntpQnaTransTx(PaqnamVO paQna) throws Exception;	
	
}
