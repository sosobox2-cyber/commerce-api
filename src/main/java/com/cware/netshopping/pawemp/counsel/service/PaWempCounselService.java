package com.cware.netshopping.pawemp.counsel.service;

import java.util.List;

import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pawemp.counsel.model.NoticeData;

public interface PaWempCounselService {
	
	/**
	 * 상품Q&A답변처리 대상 조회
	 * @param 
	 * @return List<PaqnamVO>
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaWempAnsQna() throws Exception;
	
	/**
	 * 상품Q&A답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public String savePaWempQnaTransTx(PaqnamVO paQna) throws Exception;
	
	/**
	 * 판매자 공지사항 저장
	 * @param nData
	 * @return
	 * @throws Exception
	 */
	public String savePaWempNoticeTx(List<NoticeData> nData) throws Exception;

	public List<String> selectCounselDate(Paqnamoment paqnamoment) throws Exception;
	
}