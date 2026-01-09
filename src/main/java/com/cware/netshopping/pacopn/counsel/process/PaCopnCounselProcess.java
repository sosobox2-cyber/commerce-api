package com.cware.netshopping.pacopn.counsel.process;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaCopnCounselProcess {
		
	/**
	 * 상품고객문의답변처리 결과 저장
	 * @return String
	 * @throws Exception
	 */
	public String savePaCopnCounselReply(PaqnamVO qnaReply) throws Exception;
	
	/**
	 * 상품고객문의답변처리 대상 조회
	 * @return List<PaqnamVO>
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaCopnCounselReply(ParamMap paramMap) throws Exception;
	
}
