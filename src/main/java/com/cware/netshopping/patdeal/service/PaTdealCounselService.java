package com.cware.netshopping.patdeal.service;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaTdealCounselService {
	
	/**
	 * 상품문의 조회
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg getGoodsCounselList(String fromDate, String toDate, HttpServletRequest request) throws Exception;
	
	/**
	 * 상품문의 답변 등록
	 * @param request
	 * @param paCounselNo
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg goodsCounselProc(String paCounselNo, HttpServletRequest request) throws Exception;
	
	/**
	 * 티딜 상품문의답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public void savePaTdealQnaTransTx(PaqnamVO paQna) throws Exception;

	/**
	 * 업무 메시지 조회하기
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg getTaskMessagesList(String fromDate, String toDate, HttpServletRequest request) throws Exception;
	
	/**
	 * 업무 메시지 답변 등록
	 * @param paCounselNo
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg getTaskMessagesProc(String paCounselNo, HttpServletRequest request) throws Exception;

}
