
package com.cware.netshopping.pa11st.exchange.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11stclaimlistVO;
import com.cware.netshopping.domain.model.Paorderm;


@Service("pa11st.exchange.pa11stExchangeDAO")
public class Pa11stExchangeDAO extends AbstractPaDAO{
	
	/**
	 * 11번가 교환신청목록조회(교환 요청 목록조회) - 교환단품건 정보조회.
	 * @param Pa11stclaimlistVO
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectGoodsdtInfo(Pa11stclaimlistVO pa11stclaimlistVO) throws Exception{
		return list("pa11st.exchange.selectGoodsdtInfo", pa11stclaimlistVO);
	}
	// 제휴OUT상품연동 REQ_PRM_041 : S
	/**
	 * 11번가 교환신청목록조회(교환 요청 목록조회) - 교환단품건 정보조회 딜상품정보 주문번호로 조회
	 * @param Pa11stclaimlistVO
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectDealGoodsdtInfo(Pa11stclaimlistVO pa11stclaimlistVO) throws Exception{
		return list("pa11st.exchange.selectDealGoodsdtInfo", pa11stclaimlistVO);
	}
	// 제휴OUT상품연동 REQ_PRM_041 : E
	/**
	 * 11번가 교환승인처리 - 교환출고 대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectExchangeSlipList() throws Exception{
		return list("pa11st.exchange.selectExchangeSlipList", null);
	}
	
	/**
	 * 11번가 교환승인처리 - 교환출고/회수 TPAORDERM 발송처리 결과 UPDATE
	 * @param  Paorderm
	 * @return Integer
	 * @throws Exception
	 */
	public int updateExchangePaOrdermResult(Paorderm paorderm) throws Exception{
		return update("pa11st.exchange.updateExchangePaOrdermResult", paorderm);
	}
	
	/**
	 * 교환거부처리 - 교환취소건 존재여부 확인
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectExchangeRefusalExists(ParamMap paramMap) throws Exception{
		return (Integer) selectByPk("pa11st.exchange.selectExchangeRefusalExists", paramMap.get());
	}
	
	/**
	 * 11번가 교환접수 대상조회 
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetList() throws Exception{
		return list("pa11st.exchange.selectOrderChangeTargetList", null);
	}
	
	/**
	 * 11번가 교환접수 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception{
		return list("pa11st.exchange.selectOrderChangeTargetDtList", paramMap.get());
	}

	/**
	 * 11번가 교환접수 결과처리 - 교환거부처리 parameter 조회.
	 * @param String
	 * @return HashMap<String, String>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectRejectInfo(String mappingSeq) throws Exception {
		return (HashMap<String, String>) selectByPk("pa11st.exchange.selectRejectInfo", mappingSeq);
	}
	
	/**
	 * 11번가 교환취소 대상조회 
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetList() throws Exception{
		return list("pa11st.exchange.selectChangeCancelTargetList", null);
	}
	
	/**
	 * 11번가 반품취소 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception{
		return list("pa11st.exchange.selectChangeCancelTargetDtList", paramMap.get());
	}
	
	
	public int refindOrderChangeTargetList(String paOrderNo) throws Exception{
		return (Integer) selectByPk("pa11st.exchange.refindOrderChangeTargetList", paOrderNo);
	}
	
	public int updatePaOrderm4preChangeCancle(Paorderm paorderm) throws Exception{
		return update("pa11st.exchange.updatePaOrderm4preChangeCancle", paorderm);
	}
	
	
	
}