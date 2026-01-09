package com.cware.netshopping.pahalf.counsel.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaHalfCounselService {

	/**
	 * QNA, CS 답변처리 대상 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaHalfAnsQna(ParamMap paramMap) throws Exception;

	/**
	 * 답변 처리 결과 저장
	 * 
	 * @param paQna
	 * @throws Exception
	 */
	public void savePaHalfQnaTransTx(PaqnamVO paQna) throws Exception;
	
	
}