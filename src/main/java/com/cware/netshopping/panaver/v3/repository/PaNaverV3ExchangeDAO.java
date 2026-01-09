package com.cware.netshopping.panaver.v3.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;

@Repository("panaver.v3.exchange.paNaverV3ExchangeDAO")
public class PaNaverV3ExchangeDAO extends AbstractPaDAO {

	/**
	 * 교환 수거 완료 처리대상 조회
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Paorderm> selectExchangeReturnConfirmList() throws Exception {
		return list("panaver.v3.exchange.selectExchangeReturnConfirmList", null);
	}

	/**
	 * 교환 수거 완료 처리결과 업데이트 (TPAORDERM)
	 * 
	 * @param paorderm
	 * @return
	 */
	public int updatePaOrdermResult(Paorderm paorderm) throws Exception {
		return update("panaver.v3.exchange.updatePaOrdermResult", paorderm);
	}

	/**
	 * 교환 상품 재배송 처리대상 조회
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectRedeliveryExchangeList() throws Exception {
		return list("panaver.v3.exchange.selectRedeliveryExchangeList", null);
	}

	/**
	 * 교환 재배송 처리결과 업데이트 (TPAORDERM)
	 * 
	 * @param paorderm
	 * @return
	 */
	public int updateExchangePaOrdermResult(Paorderm paorderm) throws Exception {
		return update("panaver.v3.exchange.updateExchangePaOrdermResult", paorderm);
	}

	/**
	 * 교환 취소(철회) 처리결과 업데이트 (TPAORDERM)
	 * 
	 * @param paorderm
	 * @return
	 */
	public int updatePaOrdermPreCancel(ParamMap paramMap) throws Exception {
		return update("panaver.v3.exchange.updatePaOrdermPreCancel", paramMap.get());
	}

	/**
	 * 네이버 주문 교환 데이터 생성 대상 조회
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectChangeTargetDtList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("panaver.v3.exchange.selectChangeTargetDtList", paramMap.get());
	}

	/**
	 * 네이버 교환 거부 주문정보 조회 (TPAORDERM)
	 * 
	 * @param mappingSeq
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectExchangeRejectInfo(String mappingSeq) throws Exception {
		return (HashMap<String, String>) selectByPk("panaver.v3.exchange.selectExchangeRejectInfo", mappingSeq);
	}

	/**
	 * 미처리 교환 데이터 여부 조회
	 * 
	 * @param paramMap
	 * @return
	 */
	public int checkOrderChangeTargetList(ParamMap paramMap) throws Exception {
		return (int) selectByPk("panaver.v3.exchange.checkOrderChangeTargetList", paramMap.get());
	}

	/**
	 * 네이버 교환 기취소 처리
	 * 
	 * @param paorderm
	 * @return
	 */
	public int updatePaOrdermPreChangeCancel(Paorderm paorderm) throws Exception {
		return update("panaver.v3.exchange.updatePaOrdermPreChangeCancel", paorderm);
	}

	/**
	 * 네이버 주문 교환 거부 데이터 생성 대상 조회
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("panaver.v3.exchange.selectChangeCancelTargetDtList", paramMap.get());
	}

	/**
	 * 네이버 주문 교환 거부 데이터 생성 대상 조회
	 * 
	 * @param paramMap
	 * @return
	 */
	public String selectExchangeCollectDoFlag(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.v3.infocommon.selectExchangeCollectDoFlag", paramMap.get());
	}

	/**
	 * 네이버 주문 상품 단품정보 조회
	 * 
	 * @param productId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectGoodsdtInfo(String productId) throws Exception {
		return (List<HashMap<String, String>>) list("panaver.v3.exchange.selectGoodsdtInfo", productId);
	}

	/**
	 * 네이버 교환 데이터 상담원 처리건 데이터 미생성 여부 조회
	 * 
	 * @param paramMap
	 * @return
	 */
	public int checkOrderChangeInputTargetList(ParamMap paramMap) throws Exception {
		return (int) selectByPk("panaver.v3.exchange.checkOrderChangeInputTargetList", paramMap.get());
	}
}
