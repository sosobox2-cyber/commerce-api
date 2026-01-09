package com.cware.netshopping.panaver.v3.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PanaversettlementVO;

public interface PaNaverV3AccountService {

	/**
	 * 건별 정산 내역 조회
	 * 
	 * @param searchDate
	 * @param pageNumber
	 * @param pageSize
	 * @param delYn
	 * @param procId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	ResponseMsg getCaseSettleList(String searchDate, int pageNumber, int pageSize, String delYn, String procId, HttpServletRequest request) throws Exception;

	/**
	 * 정산일자 중복데이터 체크
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int selectChkPaNaverAccount(ParamMap paramMap) throws Exception;

	/**
	 * 정산 매출정보 저장
	 * 
	 * @param arrPaNaverAccountList
	 * @return
	 * @throws Exception
	 */
	String saveSettlementListTx(List<PanaversettlementVO> arrPaNaverAccountList) throws Exception;
}
