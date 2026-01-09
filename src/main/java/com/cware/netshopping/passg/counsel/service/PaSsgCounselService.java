package com.cware.netshopping.passg.counsel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaSsgCounselService {

	/**
	 * SSG 상품문의 저장
	 * @param paqnamoment
	 * @param msgGb
	 * @return
	 * @throws Exception
	 */
	public String savePaQnaTx(PaqnamVO paqnamVo) throws Exception;
	
	/**
	 * 상담내역Async 대상 조회
	 * @param msgGb
	 * @return
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaSsgQna() throws Exception;
	
	/**
	 * 상담내역 저장
	 * @param paqnam
	 * @return
	 * @throws Exception
	 */
	public String saveCustCounselTx(PaqnamVO paqnam) throws Exception;
	
	/**
	 * SSG 상품문의 답변처리 대상 조회
	 * @param 
	 * @return List<PaqnamVO>
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaSsgAnsQna(ParamMap paramMap) throws Exception;
	
	/**
	 * SSG 상품문의 답변처리 대상 저장
	 * @param 
	 * @return List<PaqnamVO>
	 * @throws Exception
	 */
	public String savePaSsgQnaTransTx(PaqnamVO paQna) throws Exception;
	
	public HashMap<String, Object> selectPaSsgChangeCounselDt(OrderClaimVO claimVO) throws Exception;

	public int selectPaCounselNoCheck(ParamMap paramMap) throws Exception;

	public List<PaqnamVO> selectPaSsgNote() throws Exception;

	public String saveNoteCounselTx(PaqnamVO paqnam) throws Exception;

	public List<PaqnamVO> selectPaNoteAnswerList(ParamMap paramMap) throws Exception;
}
