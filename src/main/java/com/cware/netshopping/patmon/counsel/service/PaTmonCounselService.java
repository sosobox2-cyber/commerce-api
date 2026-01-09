package com.cware.netshopping.patmon.counsel.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaTmonCounselService {

	/**
	 * 티몬 상품문의 답변처리 대상 조회
	 * @param 
	 * @return List<PaqnamVO>
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaTmonAnsQna(ParamMap paramMap) throws Exception;

	/**
	 * 티몬 상품문의 답변처리 대상 저장
	 * @param 
	 * @return List<PaqnamVO>
	 * @throws Exception
	 */
	public String savePaTmonQnaTransTx(PaqnamVO paQna) throws Exception;
	
	/**
	 * 티몬 CS문의 중복 확인 
	 * @param String
	 * @return List<PaqnamVO>
	 * @throws Exception
	 */
	public int selectPaCounselNoCheck(String pa_counsel_no) throws Exception;

}
