package com.cware.netshopping.pacopn.exchange.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pacopnexchangeitemlist;
import com.cware.netshopping.domain.model.Pacopnexchangelist;
import com.cware.netshopping.domain.model.Paorderm;

@Service("pacopn.exchange.paCopnExchangeDAO")
public class PaCopnExchangeDAO extends AbstractPaDAO {

	/**
	 * 교환신청/취소 목록 데이터 중복 체크
	 *
	 * @param pa11stClaimList
	 * @return
	 * @throws Exception
	 */
	public int selectPaCopnClaimListExists(Pacopnexchangelist exchangelist) throws Exception {
		return (Integer) selectByPk("pacopn.exchange.selectPaCopnExchangeListExists", exchangelist);
	}

	/**
	 * 교환신청/취소 목록 저장(Pacopnexchangelist)
	 *
	 * @param pacopnexchangelist
	 * @return
	 * @throws Exception
	 */
	public int insertPacopnexchangelist(Pacopnexchangelist exchangelist) throws Exception {
		return insert("pacopn.exchange.insertPacopnexchangelist", exchangelist);
	}

	/**
	 * 교환신청/취소 목록 저장(Pacopnexchangeitemlist)
	 *
	 * @param exchangeitemlist
	 * @return
	 * @throws Exception
	 */
	public int insertPacopnexchangeitemlist(Pacopnexchangeitemlist exchangeitemlist) throws Exception {
		return insert("pacopn.exchange.insertPacopnexchangeitemlist", exchangeitemlist);
	}

	/**
	 * 교환신청/취소 목록 교환 배송 상품 정보 조회
	 *
	 * @param exchangeitemlist
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectExchangeGoodsdt(Pacopnexchangeitemlist exchangeitemlist) throws Exception {
		return (HashMap<String, Object>) selectByPk("pacopn.exchange.selectExchangeGoodsdt", exchangeitemlist);
	}

	/**
	 * 교환접수 대상 중계 데이터 조회
	 *
	 * @param
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetList() throws Exception {
		return list("pacopn.exchange.selectOrderChangeTargetList", null);
	}

	/**
	 * 교환접수 대상조회 - 상세
	 *
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return list("pacopn.exchange.selectOrderChangeTargetDtList", paramMap.get());
	}

	/**
	 * 교환취소 대상조회
	 *
	 * @param
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetList() throws Exception {
		return list("pacopn.exchange.selectChangeCancelTargetList", null);
	}

	/**
	 * 반품취소 대상조회 - 상세
	 *
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("pacopn.exchange.selectOrderChangeCancelTargetDtList", paramMap.get());
	}

	/**
	 * 교환 배송 상품 재고 부족으로 Paorderm update
	 *
	 * @param ParamMap
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePaOrdermPreCancel(ParamMap paramMap) throws Exception {
		return update("pacopn.exchange.updatePaOrdermPreCancel", paramMap.get());
	}

	/**
	 * 교환요청상품 입고 확인처리 대상 조회
	 *
	 * @param paCode
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectExchangeReturn(String paCode) throws Exception {
		return list("pacopn.exchange.selectExchangeReturn", paCode);
	}

	/**
	 * 교환요청상품 입고 확인처리 내역 저장
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updateExchangeReturnConfirm(ParamMap paramMap) throws Exception {
		return update("pacopn.exchange.updateExchangeReturnConfirm", paramMap.get());
	}

	/**
	 * 교환상품 송장 업로드 처리 대상 조회
	 *
	 * @param ParamMap
	 * @return List<Map>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectExchangeSlipOutTargetList() throws Exception {
		return list("pacopn.exchange.selectExchangeSlipOutTargetList", null);
	}

	/**
	 * 교환상품 송장 업로드 처리 내역 저장
	 *
	 * @param paramMap
	 * @return int
	 * @throws Exception
	 */
	public int updateExchangeCompleteResult(ParamMap paramMap) throws Exception {
		return update("pacopn.exchange.updateExchangeCompleteResult", paramMap.get());
	}

	/**
	 * 교환 생성전 취소 건 업데이트
	 *
	 * @param preCancelMap
	 * @return
	 * @throws Exception
	 */
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception {
		return update("pacopn.exchange.updatePreCancelYn", preCancelMap.get());
	}
	
	/**
	 * 교환배송완료 처리 내역 저장
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updateExchangeDeliveryComplete(Paorderm paOrderm) throws Exception{
		return update("pacopn.exchange.updateExchangeDeliveryComplete", paOrderm);
	}
	
	/**
	 * 교환배송완료 처리상태 조회
	 * @param paOrderm
	 * @return
	 * @throws Exception
	 */
	public String selectExchangeDeliveryDoFlag(Paorderm paOrderm) throws Exception{
		return (String) selectByPk("pacopn.exchange.selectExchangeDeliveryDoFlag", paOrderm);
	}
	
	/**
	 * 교환배송완료 처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectExchangeCompleteList() throws Exception{
		return list("pacopn.exchange.selectExchangeCompleteList", null);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectOrgShipmentBoxId(ParamMap paramMap) throws Exception {
		return (HashMap<String, String>) selectByPk("pacopn.exchange.selectOrgShipmentBoxId", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectExchangeDetail(String paClaimNo) throws Exception{
		return (HashMap<String, String>) selectByPk("pacopn.exchange.selectExchangeDetail",paClaimNo);
	}
 
	@SuppressWarnings("unchecked")
	public List<String> selectExchangeCreatedDate() throws Exception{
		return list("pacopn.exchange.selectExchangeCreatedDate", null);
	} 
}
