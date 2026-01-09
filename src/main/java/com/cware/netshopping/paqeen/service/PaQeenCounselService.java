package com.cware.netshopping.paqeen.service;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.paqeen.message.CounselCommentsResoponseMsg;
import com.cware.netshopping.paqeen.message.CounselDetailResoponseMsg;

public interface PaQeenCounselService {
	
	
	 /**
	 *  퀸잇 문의 사항 목록 조회
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg getCxCounselList(String fromDate, String toDate, HttpServletRequest request) throws Exception;
	
	/**
	 *  퀸잇 문의 사항 상세 조회
	 * @param inquiryId
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public CounselDetailResoponseMsg getCxCounselDetail(String paCode, String inquiryId, HttpServletRequest request) throws Exception;
	
	/**
	 *  퀸잇 문의 사항 댓글 조회
	 * @param inquiryId
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public CounselCommentsResoponseMsg getCxCounselComments(String paCode, String inquiryId, HttpServletRequest request) throws Exception;
	
	 /**
	 *  퀸잇 문의 답변 등록
	 * @param paCounselNo
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg cxCounselProc(String paCounselNo, HttpServletRequest request) throws Exception;
	
	/**
	 * 퀸잇 문의 답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public void savePaQeenQnaTransTx(PaqnamVO paQna) throws Exception;
	
	
}
