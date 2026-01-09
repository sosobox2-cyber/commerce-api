package com.cware.netshopping.pakakao.claim.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaKakaoOrderListVO;

@Service("pakakao.delivery.paKakaoClaimDAO")
public class PaKakaoClaimDAO extends AbstractPaDAO{
	
	/**
	 * 원주문 데이터 유무 체크
	 * @param PaKakaoCancelListVO
	 * @return int
	 * @throws Exception
	 */
	public int countOrderList(PaKakaoOrderListVO vo) throws Exception {
		return (Integer) selectByPk("pakakao.claim.countOrderList", vo);
	}
	
	/**
	 * 취소 데이터 중복 체크
	 * @param PaKakaoCancelListVO
	 * @return int
	 * @throws Exception
	 */
	public int selectPaKakaoCancelListCount(PaKakaoOrderListVO vo) throws Exception {
		return (Integer) selectByPk("pakakao.claim.selectPaKakaoCancelListCount", vo);
	}
	
	/**
	 * 취소 데이터 중복 체크(TPAORDERM)
	 * @param PaKakaoCancelListVO
	 * @return int
	 * @throws Exception
	 */
	public int selectPaKakaoCancelMListCount(PaKakaoOrderListVO vo) throws Exception {
		return (Integer) selectByPk("pakakao.claim.selectPaKakaoCancelMListCount", vo);
	}
	
	public HashMap<String, Object> selectPaKakaoCancelInfo(PaKakaoOrderListVO vo) throws Exception {
		return (HashMap<String, Object>) selectByPk("pakakao.claim.selectPaKakaoCancelInfo", vo);
	}
	
	public int insertTpaKakaoCancelList(PaKakaoOrderListVO vo) throws Exception {
		return insert("pakakao.claim.insertTpaKakaoCancelList", vo);
	}
	
	public int updatePaKakaoCancelComplete(PaKakaoOrderListVO vo) throws Exception {
		return update("pakakao.claim.updatePaKakaoCancelComplete", vo);
	}
	
	/**
	 * 취소 승인/거부 처리 대상 조회
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaKakaoOrderCancelList() throws Exception {
		return list("pakakao.claim.selectPaKakaoOrderCancelList", null);
	}
	
	public int updatePaKakaoCancelList(ParamMap paramMap) throws Exception {
		return update("pakakao.claim.updatePaKakaoCancelList", paramMap.get());
	}
	
	public PaKakaoOrderListVO selectPaKakaoCancelList(ParamMap paramMap) throws Exception {
		return (PaKakaoOrderListVO) selectByPk("pakakao.claim.selectPaKakaoCancelList", paramMap.get());
	}
	
	public int updatePaOrderMSlipOut(ParamMap paramMap) throws Exception {
		return update("pakakao.claim.updatePaOrderMSlipOut", paramMap.get());
	}
	
	public String selectPaKakaoWithdrawCancelListCheck(PaKakaoOrderListVO vo) throws Exception {
		return (String) selectByPk("pakakao.claim.selectPaKakaoWithdrawCancelListCheck", vo);
	}
	
	public int updatePaKakaoCancelList4Withdraw(PaKakaoOrderListVO vo) throws Exception {
		return update("pakakao.claim.updatePaKakaoCancelList4Withdraw", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return list("pakakao.claim.selectClaimTargetList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("pakakao.claim.selectCancelInputTargetDtList", paramMap.get());
	}
	
	public int countOrgClaimList(PaKakaoOrderListVO vo) throws Exception {
		return (Integer) selectByPk("pakakao.claim.countOrgClaimList", vo);
	}
	
	public int countClaimList(PaKakaoOrderListVO vo) throws Exception {
		return (Integer) selectByPk("pakakao.claim.countClaimList", vo);
	}
	
	public int insertPaKakaoClaimList(PaKakaoOrderListVO vo) throws Exception {
		return insert("pakakao.claim.insertPaKakaoClaimList", vo);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectKakaoExchangeGoodsInfo(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("pakakao.claim.selectKakaoExchangeGoodsInfo", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return list("pakakao.claim.selectOrderCalimTargetDt30List", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return list("pakakao.claim.selectOrderCalimTargetDt20List", paramMap.get());
	}
	
	public String compareAddress(ParamMap paramMap) throws Exception {
		return (String) selectByPk("pakakao.claim.compareAddress", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("pakakao.claim.selectClaimCancelTargetDtList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return list("pakakao.claim.selectOrderChangeTargetDtList", paramMap.get());
	}
	
	public int updatePaOrdermChangeFlag(ParamMap paramMap) throws Exception {
		return update("pakakao.claim.updatePaOrdermChangeFlag", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectRefusalInfo(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("pakakao.claim.selectRefusalInfo", paramMap.get());
	}
	
	public int updatePreCanYn(ParamMap paramMap) throws Exception {
		return update("pakakao.claim.updatePreCanYn", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("pakakao.claim.selectChangeCancelTargetDtList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaKakaoClaimInvTgList(ParamMap paramMap) throws Exception {
		return list("pakakao.claim.selectPaKakaoClaimInvTgList", paramMap.get());
	}
	
	
	public int updatePaOrderMDoFlag(Map<String, Object> map) throws Exception {
		return update("pakakao.claim.updatePaOrderMDoFlag", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaKakaoClaimCollCmpTgList(ParamMap paramMap) throws Exception {
		return list("pakakao.claim.selectPaKakaoClaimCollCmpTgList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaKakaoReturnCmpTgList() throws Exception {
		return list("pakakao.claim.selectPaKakaoReturnCmpTgList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaKakaoExchangeSlipOutList() throws Exception {
		return list("pakakao.claim.selectPaKakaoExchangeSlipOutList", null);
	}
	
	public int countCancelList(ParamMap paramMap) throws Exception {
		return (Integer) selectByPk("pakakao.claim.countCancelList", paramMap.get());
	}
	
	/**
	 * 철회된 취소 조회
	 * @param PaKakaoOrderListVO
	 * @return int
	 * @throws Exception
	 */
	public int selectWithdrawCancelCount(PaKakaoOrderListVO vo) throws Exception {
		return (Integer) selectByPk("pakakao.claim.selectWithdrawCancelCount", vo);
	}
	
	public int updatePaKakaoCancelWithdrawYn(PaKakaoOrderListVO vo) throws Exception {
		return update("pakakao.claim.updatePaKakaoCancelWithdrawYn", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectWithdrawClaimCount(PaKakaoOrderListVO vo) throws Exception {
		return list("pakakao.claim.selectWithdrawClaimCount", vo);
	}
	
	public int selectGoodsdtInfoCount(ParamMap paramMap) {
		return (Integer) selectByPk("pakakao.claim.selectGoodsdtInfoCount", paramMap.get());
	}
	
	public int selectExchangeCompleteCount(Map<String, Object> map) {
		return (Integer) selectByPk("pakakao.claim.selectExchangeCompleteCount", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaKakaoClaimHoldTargetList(ParamMap paramMap) throws Exception {
		return list("pakakao.claim.selectPaKakaoClaimHoldTargetList", paramMap.get());
	}
	
	public int updatePaOrderMHoldYn(Map<String, Object> map) throws Exception {
		return update("pakakao.claim.updatePaOrderMHoldYn", map);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception {
		return list("pakakao.claim.selectPaMobileOrderAutoCancelList", null);
	}
}
