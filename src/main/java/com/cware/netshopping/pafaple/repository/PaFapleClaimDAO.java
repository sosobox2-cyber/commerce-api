package com.cware.netshopping.pafaple.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pafaple.domain.PaFapleExchangeListVO;
import com.cware.netshopping.pafaple.domain.PaFapleReturnListVO;

@Repository("pafaple.claim.PaFapleClaimDAO")
public class PaFapleClaimDAO extends AbstractPaDAO {

	/**
	 * 반품 원주문 데이터 유무 체크
	 * @param PaFapleReturnListVO
	 * @return int
	 * @throws Exception
	 */
	public int countOrderList(PaFapleReturnListVO returnListVO) throws Exception {
		return (Integer) selectByPk("pafaple.claim.countOrderList", returnListVO);
	}
	
	/**
	 * 교환 원주문 데이터 유무 체크
	 * @param PaFapleReturnListVO
	 * @return int
	 * @throws Exception
	 */
	public int countExchangeList(PaFapleExchangeListVO paFapleExchangeListVO) throws Exception {
		return (Integer) selectByPk("pafaple.claim.countExchangeList", paFapleExchangeListVO);
	}

	/**
	 * 원주문 중복데이터 체크 및 DO_FLAG 체크 
	 * @param PaFapleReturnListVO
	 * @return int
	 * @throws Exception
	 */
	public int selectPaFapleReturnListCount(PaFapleReturnListVO returnListVO)  throws Exception {
		return (Integer) selectByPk("pafaple.claim.selectPaFapleReturnListCount", returnListVO);
	}

	/**
	 * 직권반품완료 UPDATE 
	 * @param PaFapleReturnListVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePaOrderMDoFlag(Map<String, Object> map)  throws Exception {
		return update("pafaple.claim.updatePaOrderMDoFlag", map); 
	}

	/**
	 * 반품 데이터 등록 
	 * @param PaFapleReturnListVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPaFapleReturnList(PaFapleReturnListVO returnListVO) throws Exception {
		return insert("pafaple.claim.insertPaFapleReturnList", returnListVO);
	}

	/**
	 * TPAORDERM 원주문 체크 
	 * @param PaFapleReturnListVO
	 * @return int
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaFapleClaimInfo(PaFapleReturnListVO returnListVO) throws Exception {
		return (HashMap<String, Object>) selectByPk("pafaple.claim.selectPaFapleClaimInfo", returnListVO);
	}

	/**
	 * 패션플러스 BO 반품 데이터 조회 
	 * @param ParamMap
	 * @throws Exception
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return list("pafaple.claim.selectClaimTargetList", paramMap.get());
	}

	/**
	 * 패션플러스 BO 반품 데이터 조회 디테일 
	 * @param ParamMap
	 * @throws Exception
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderClaimTargetDt30List(ParamMap paramMap) throws Exception {
		return list("pafaple.claim.selectOrderClaimTargetDt30List", paramMap.get());
	}

	/**
	 * 패션플러스 BO 반품 데이터 조회 디테일 
	 * @param ParamMap
	 * @throws Exception
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return list("pafaple.claim.selectOrderCalimTargetDt20List", paramMap.get());
	}

	public String compareAddress(ParamMap paramMap) throws Exception {
		return (String)selectByPk("pafaple.claim.compareAddress", paramMap.get());
	}
	
	public String compareAddressExchange(ParamMap paramMap) throws Exception {
		return (String)selectByPk("pafaple.claim.compareAddressExchange", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectFapleReturnCompleList(ParamMap paramMap)  throws Exception {
		return list("pafaple.claim.selectFapleReturnCompleList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> selectPaFapleClaimSendBack(Map<String, Object> recall) {
		return (HashMap<String, Object>) selectByPk("pafaple.claim.selectPaFapleClaimSendBack", recall);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectNotEndClaimList(Map<String, Object> returnWithdraw)  throws Exception {
		return list("pafaple.claim.selectNotEndClaimList", returnWithdraw);
	}

	public int selectPaFapleExchangeListCount(PaFapleExchangeListVO exchangeListVO) throws Exception {
		return (Integer) selectByPk("pafaple.claim.selectPaFapleExchangeListCount", exchangeListVO);
	}

	public int insertPaFapleExchangeList(PaFapleExchangeListVO exchangeListVO) throws Exception {
		return insert("pafaple.claim.insertPaFapleExchangeList", exchangeListVO);
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return list("pafaple.claim.selectOrderChangeTargetDtList", paramMap.get());
	}

	public int updatePaOrdermChangeFlag(ParamMap paramMap) throws Exception {
		return update("pafaple.claim.updatePaOrdermChangeFlag", paramMap.get()); 
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("pafaple.claim.selectClaimCancelTargetDtList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("pafaple.claim.selectChangeCancelTargetDtList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectFapleExchangeSlipOutList(ParamMap paramMap) throws Exception {
		return list("pafaple.claim.selectFapleExchangeSlipOutList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectFapleExchangeSlipOutStartList(ParamMap paramMap) throws Exception {
		return list("pafaple.claim.selectFapleExchangeSlipOutStartList", paramMap.get() );
	}

	public int updatePaorderm(Map<String, Object> exchangeStart)  throws Exception {
		return update("pafaple.claim.updatePaorderm", exchangeStart); 
	}

	public int updateExchangeOrderNo(Map<String, Object> exchangeMap) throws Exception {
		return update("pafaple.claim.updateExchangeOrderNo", exchangeMap); 
	}

	public int checkPaFapleExcahngeOrderId(PaFapleReturnListVO returnListVO)  throws Exception {
		return (Integer) selectByPk("pafaple.claim.checkPaFapleExcahngeOrderId", returnListVO);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaFapleExchangeOrderId(PaFapleReturnListVO returnListVO) throws Exception {
		return  (HashMap<String, Object>) selectByPk("pafaple.claim.selectPaFapleExchangeOrderId", returnListVO);
	}

	public int insertPaOrderMReturn(Paorderm paorderm) throws Exception {
		return insert("pafaple.claim.insertPaOrderMReturn", paorderm);
	}

	public int countClaimList(Map<String, Object> returnWithdraw) throws Exception {
		return (Integer) selectByPk("pafaple.claim.countClaimList", returnWithdraw);
	}

	public Integer selectClaimQty(PaFapleReturnListVO returnListVO) throws Exception {
		return (Integer) selectByPk("pafaple.claim.selectClaimQty", returnListVO);
	}

	public int updateExchangePaorderm(Map<String, Object> exchangeStart)  throws Exception {
		return update("pafaple.claim.updateExchangePaorderm", exchangeStart); 
	}

}
