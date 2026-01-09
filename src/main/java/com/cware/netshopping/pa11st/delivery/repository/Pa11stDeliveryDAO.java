
package com.cware.netshopping.pa11st.delivery.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11storderlistVO;
import com.cware.netshopping.domain.model.Pa11storderlist;
import com.cware.netshopping.domain.model.Paorderm;


@Service("pa11st.delivery.pa11stDeliveryDAO")
public class Pa11stDeliveryDAO extends AbstractPaDAO{
	
	/**
	 * 발주확인처리 - TPA11STORDERLIST insert
	 * @param ParamMap
	 * @return Integer
	 * @throws Exception
	 */
	public int insertPa11stOrderList(ParamMap paramMap) throws Exception{
		return insert("pa11st.delivery.insertPa11stOrderList", paramMap.get());
	}
	
	/**
	 * 11번가 발주확인 처리대상 체크
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectOrderConfirmProcExists(ParamMap paramMap) throws Exception{
		return (Integer) selectByPk("pa11st.delivery.selectOrderConfirmProcExists", paramMap.get());
	}
	
	/**
	 * 11번가 발송처리 - 출고대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectPa11stSlipProcList() throws Exception{
		return list("pa11st.delivery.selectPa11stSlipProcList", null);
	}
	
	/**
	 * 11번가 발송처리 - 11번가 배송번호 기준 SK스토아TV쇼핑 운송장 사용개수 조회
	 * @param String
	 * @return List<Object>
	 * @throws Exception
	 */
	public int selectSlipNoUsedCnt(String paShipNo) throws Exception{
		return (Integer) selectByPk("pa11st.delivery.selectSlipNoUsedCnt", paShipNo);
	}
	
	/**
	 * 11번가 발송처리 - 출고대상 정보 조회
	 * @param String
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectSlipProcList(String paShipNo) throws Exception{
		return list("pa11st.delivery.selectSlipProcList", paShipNo);
	}
	
	/**
	 * 11번가 발송처리 - TPAORDERM 발송처리 결과 UPDATE
	 * @param  Paorderm
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePaOrdermResult(Paorderm paorderm) throws Exception{
		return update("pa11st.delivery.updatePaOrdermResult", paorderm);
	}
	
	/**
	 * 11번가 발주확인목록 조회 - 데이터 중복 체크
	 * @param Pa11storderlist
	 * @return int
	 * @throws Exception
	 */
	public int selectPa11stOrderListExists(Pa11storderlist pa11storderlist) throws Exception{
		return (Integer) selectByPk("pa11st.delivery.selectPa11stOrderListExists", pa11storderlist);
	}
	
	/**
	 * 11번가 발주확인목록 조회 - TPA11STORDERLIST MERGE
	 * @param Pa11storderlist pa11storderlist
	 * @return int
	 * @throws Exception
	 */
	public int mergePa11stOrderList(Pa11storderlist pa11storderlist) throws Exception{
		return insert("pa11st.delivery.mergePa11stOrderList", pa11storderlist);
	}
	
	/**
	 * 11번가 발송처리 처리대상 체크
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectShppDeliveryProcExists(HashMap<String, Object> paramMap) throws Exception{
		return (Integer) selectByPk("pa11st.delivery.selectShppDeliveryProcExists", paramMap);
	}
	
	
	public int selectCancelOrderExists(HashMap<String, Object> paramMap) throws Exception{
		return (Integer) selectByPk("pa11st.delivery.selectShppDeliveryProcExists", paramMap);
	}
	
	/**
	 * 11번가 오늘발송 요청내역  - 데이터 중복 체크
	 * @param Pa11storderlist
	 * @return int
	 * @throws Exception
	 */
	public int selectPa11stTodayDeliveryListExists(Pa11storderlistVO pa11storderlist) throws Exception{
		return (Integer) selectByPk("pa11st.delivery.selectPa11stTodayDeliveryListExists", pa11storderlist);
	}
	
	/**
	 * 11번가 오늘발송 요청내역 - TPA11STORDERLIST UPDATE
	 * @param Pa11storderlist pa11storderlist
	 * @return int
	 * @throws Exception
	 */
	public int updateTodayDeliveryList(Pa11storderlistVO pa11storderlist) throws Exception{
		return insert("pa11st.delivery.updateTodayDeliveryList", pa11storderlist);
	}
	
	/**
	 * 11번가 발송지연안내 처리
	 * @param String
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectPa11stDelayList(String dateTime) throws Exception{
		return list("pa11st.delivery.selectPa11stDelayList", dateTime);
	}
	
	/**
	 * 11번가 발송지연안내 처리 성공
	 * @param Map
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int updateTpaordermDelaySendDt(Map<String, Object> paramMap) throws Exception{
		return update("pa11st.delivery.updateTpaordermDelaySendDt", paramMap);
	}
	
}