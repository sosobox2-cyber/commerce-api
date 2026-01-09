package com.cware.netshopping.pa11st.delivery.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11storderlistVO;
import com.cware.netshopping.domain.model.Paorderm;

public interface Pa11stDeliveryProcess {

	/**
	 * 11번가 발주확인대상 저장
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String saveOrderConfirmProc(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 발주확인 처리대상 체크
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectOrderConfirmProcExists(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 발송처리 - 출고대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectPa11stSlipProcList() throws Exception;
	
	/**
	 * 11번가 발송처리 - 발송처리/부분발송처리
	 * @param HashMap<String, Object>
	 * @return ParamMap
	 * @throws Exception
	 */
	public ParamMap saveSlipOutProc(HashMap<String, Object> slipMap) throws Exception;
	
	/**
	 * 11번가 판매완료내역 구매확정일시 저장
	 * @param Paorderm
	 * @return String
	 * @throws Exception
	 */
	public String saveDeliveryCompleteList(List<Paorderm> arrPaordermlist) throws Exception;

	/**
	 * 11번가 발주확인목록 저장
	 * @param List<Pa11storderlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveOrderConfirmList(List<Pa11storderlistVO> arrPa11storderlist) throws Exception;
	
	/**
	 * 11번가 오늘발송 요청내역
	 * @param Pa11storderlistVO
	 * @return String
	 * @throws Exception
	 */
	public String updateTodayDeliveryList(List<Pa11storderlistVO> arrPa11storderlist) throws Exception;	
	
	/**
	 * 11번가 발송지연안내 처리
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectPa11stDelayList(String dateTime) throws Exception;
	
	/**
	 * 11번가 발송지연안내 처리 성공
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public int updateTpaordermDelaySendDt(Map<String, Object> paramMap) throws Exception;
}