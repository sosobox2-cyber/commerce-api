package com.cware.netshopping.pa11st.counsel.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

public interface Pa11stCounselService {
	
	/**
	 * 11번가 상품Q&A답변처리 대상 조회
	 * @param 
	 * @return List<PaqnamVO>
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPa11stAnsQna(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품Q&A답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public String savePa11stQnaTransTx(PaqnamVO paQna) throws Exception;
	
	/**
	 * 11번가 상품Q&A 원본 글이 삭제된 QNA 완료처리
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public String saveQnaMonitering(PaqnamVO paQna) throws Exception;
	
	/**
	 * 11번가 긴급메세지 상담 존재 체크
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public String selectCheckPacounsel(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 긴급메세지 seq 채번
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String selectGetMaxPacounselSeq(ParamMap paramMap) throws Exception;	
	
}