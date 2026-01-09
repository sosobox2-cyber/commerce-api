package com.cware.netshopping.pafaple.process;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaFapleCounselProcess {

	/**
	 *  업무 메시지 조회하기
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg getCsNoticeList(String fromDate, String toDate, HttpServletRequest request)throws Exception; 
	
	/**
	 * CS 알리미 답변 등록
	 * @param request
	 * @param paCounselNo
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg csNoticeProc(String paCounselNo, HttpServletRequest request) throws Exception;
	
	/**
	 * 패플 CS 알리미답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public void savePaFapleQnaTrans(PaqnamVO paQna) throws Exception;

	/**
	 * 고객게시판문의 조회
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg getBbsList(String fromDate, String toDate, HttpServletRequest request)throws Exception;

	/**
	 * 고객문의답변등록
	 */
	public ResponseMsg goodsCounselProc(String paCounselNo, HttpServletRequest request) throws Exception;
}
