package com.cware.netshopping.pa11st.order.service;


import java.util.HashMap;
import java.util.List;

import org.springframework.scheduling.annotation.EnableAsync;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11storderlistVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.domain.OrderpromoVO;

@EnableAsync
public interface Pa11stOrderService {
	
	/**
	 * 11번가 판매불가처리
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String saveOrderRejectProcTx(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 취소신청목록조회
	 * @param List<Pa11storderlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveCancelListTx(List<Pa11storderlistVO> arrPa11storderlist) throws Exception;
	
	/**
	 * 11번가 취소처리대상조회 
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectPa11stOrdCancelList() throws Exception;
	
	/**
	 * 11번가 주문취소승인/주문취소거부
	 * @param HashMap<String, Object>
	 * @return ParamMap
	 * @throws Exception
	 */
	public ParamMap saveCancelConfirmProcTx(HashMap<String, Object> cancelMap) throws Exception;
	
	/**
	 * 11번가 취소철회목록 조회 - 저장
	 * @param List<Pa11storderlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveCancelWithdrawListTx(List<Pa11storderlistVO> arrPa11storderlist) throws Exception;
	
	/**
	 * 11번가 취소완료목록 조회 - 저장
	 * @param List<Pa11storderlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveCancelCompleteListTx(List<Pa11storderlistVO> arrPa11storderlist) throws Exception;
	
	/**
	 * 11번가 주문생성 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderInputTargetList() throws Exception;
	
	/**
	 * 11번가 주문생성 대상 조회(상세)
	 * @param String
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderInputTargetDtList(HashMap<String, String> orderMap) throws Exception;

	/**
	 * 11번가 주문취소 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectCancelInputTargetList() throws Exception;
	
	/**
	 * 11번가 주문생성 결과처리 - 판매불가정보 parameter 조회.
	 * @param String
	 * @return HashMap<String, String>
	 * @throws Exception
	 */
	public HashMap<String, String> selectRefusalInfo(String mappingSeq) throws Exception;

	/**
	 * 11번가 주문취소 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 11번가 주문취소 처리 - 기취소
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updatePreCancelYnTx(ParamMap preCancelMap) throws Exception;

	/**
	 * 11번가 취소승인처리 - 취소승인대상조회
	 * @param String
	 * @return HashMap<String, String>
	 * @throws Exception
	 */
	public HashMap<String, Object> selectPa11stOrdCancelApprovalList(ParamMap cancelMap) throws Exception;

	/**
	 * 11번가 취소승인처리 - 저장
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String saveCancelApprovalProcTx(HashMap<String, Object> cancelMap) throws Exception;

	/**
	 * 11번가 주문생성이전취소 - 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectCancelWithoutOrderList(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 주문생성이전취소 - TPAORDERM update
	 * @param List<Object>
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePreCancelReason(Paorderm paorderm) throws Exception;
	
	/**
	 * 11번가 프로모션 조회 - 조회
	 * @param HashMap<String, String>
	 * @return OrderpromoVO
	 * @throws Exception
	 */
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception;
	
	/**
	 * 11번가 프로모션 조회 - 조회
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
	 * @param HashMap<String, String>
	 * @return OrderpromoVO
	 * @throws Exception
	 */
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception;

	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList(ParamMap paramMap)  throws Exception;
}