package com.cware.netshopping.pawemp.counsel.process;

import java.util.List;

import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pawemp.counsel.model.NoticeData;

public interface PaWempCounselProcess {

	/**
	 * 상품Q&A답변처리 대상 조회
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaWempAnsQna() throws Exception;
	
	/**
	 * 상품Q&A답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public String savePaWempQnaTrans(PaqnamVO paQna) throws Exception;
	
	/**
	 * 판매자 공지사항 저장
	 * @param List<NoticeData>
	 * @return
	 * @throws Exception
	 */
	public String savePaWempNotice(List<NoticeData> nData) throws Exception;

	public List<String> selectCounselDate(Paqnamoment paqnamoment) throws Exception;
	
}