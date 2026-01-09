package com.cware.netshopping.pacopn.counsel.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaCopnCounselService {
		
	/**
	 * 상품고객문의답변처리 결과 저장
	 * @return String
	 * @throws Exception
	 */
	public String savePaCopnCounselReplyTx(PaqnamVO qnaReply) throws Exception;

	/**
	 * 콜센터고객문의답변처리 대상 조회
	 * @return List<PaqnamVO>
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaCopnCounselReply(ParamMap paramMap) throws Exception;
	
}
