package com.cware.netshopping.passg.counsel.process;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaSsgCounselProcess {

	/**
	 * SSG 상품문의 저장
	 * @param paqnamoment
	 * @param msgGb
	 * @return
	 * @throws Exception
	 */
	public String savePaQna(PaqnamVO paqnamVo) throws Exception;
	
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
	public String saveCustCounsel(PaqnamVO paqnam) throws Exception;
	
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
	public String savePaSsgQnaTrans(PaqnamVO paQna) throws Exception;
	
	/**
	 * SSG 교환거부 대상 정보 조회
	 * @param mappingSeq
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> selectPaSsgChangeCounselDt(OrderClaimVO claimVO) throws Exception;

	public int selectPaCounselNoCheck(ParamMap paramMap) throws Exception;

	public List<PaqnamVO> selectPaSsgNote() throws Exception;
	
	public String saveNoteCounsel(PaqnamVO panotem) throws Exception;

	public List<PaqnamVO> selectPaNoteAnswerList(ParamMap paramMap) throws Exception;
}