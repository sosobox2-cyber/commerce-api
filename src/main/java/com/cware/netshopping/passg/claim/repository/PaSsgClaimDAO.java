package com.cware.netshopping.passg.claim.repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaSsgCancelListVO;
import com.cware.netshopping.domain.PaSsgClaimListVO;
import com.cware.netshopping.domain.PaSsgOrderListVO;

@Service("passg.claim.paSsgClaimDAO")
public class PaSsgClaimDAO extends AbstractPaDAO {

	/**
	 * 원주문 데이터 유무 체크
	 * @param PaSsgCancelListVO
	 * @return int
	 * @throws Exception
	 */
	public int countOrderList(PaSsgCancelListVO vo) throws Exception {
		return (Integer) selectByPk("passg.claim.countOrderList", vo);
	}
	
	/**
	 * 취소 데이터 중복 체크
	 * @param PaSsgCancelListVO
	 * @return int
	 * @throws Exception
	 */
	public int selectPaSsgCancelListCount(PaSsgCancelListVO vo) throws Exception {
		return (Integer) selectByPk("passg.claim.selectPaSsgCancelListCount", vo);
	}
	
	/**
	 * 취소 데이터 중복 체크(TPAORDERM)
	 * @param PaSsgCancelListVO
	 * @return int
	 * @throws Exception
	 */
	public int selectPaSsgCancelMListCount(PaSsgCancelListVO vo) throws Exception {
		return (Integer) selectByPk("passg.claim.selectPaSsgCancelMListCount", vo);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaSsgCancelInfo(PaSsgCancelListVO vo) throws Exception {
		return (HashMap<String, Object>) selectByPk("passg.claim.selectPaSsgCancelInfo", vo);
	}
	
	public int insertTpaSsgCancelList(PaSsgCancelListVO vo) throws Exception {
		return insert("passg.claim.insertTpaSsgCancelList", vo);
	}
	
	public int insertTpaSsgCancelList2(PaSsgCancelListVO vo) throws Exception {
		return insert("passg.claim.insertTpaSsgCancelList2", vo);
	}
	
	/**
	 * 클레임 대상 데이터 존재 여부 확인
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int countClaimList(PaSsgClaimListVO vo) throws Exception {
		return (Integer) selectByPk("passg.claim.countClaimList", vo);
	}
	
	/**
	 * TPASSGCLAIMLIST 데이터 삽입
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int insertPaSsgClaimList(PaSsgClaimListVO vo) throws Exception{
		return insert("passg.claim.insertPaSsgClaimList", vo);
	}
	
	/**
	 * 상품정보 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectSsgExchangeGoodsInfo(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("passg.claim.selectSsgExchangeGoodsInfo", paramMap.get());
	}
	
	/**
	 * BO연동 위한 클레임 대상 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return list("passg.claim.selectClaimTargetList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("passg.claim.selectCancelInputTargetDtList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return list("passg.claim.selectOrderChangeTargetDtList", paramMap.get());
	}
	
	public String compareAddress(ParamMap paramMap) throws Exception {
		return (String) selectByPk("passg.claim.compareAddress", paramMap.get());
	}
	
	public int updatePaOrdermChangeFlag(ParamMap paramMap) throws Exception {
		return update("passg.claim.updatePaOrdermChangeFlag", paramMap.get());
	}
	
	/**
	 * 반품타켓 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return list("passg.claim.selectOrderCalimTargetDt30List", paramMap.get());
	}
	
	/**
	 * 출고전 반품 건 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return list("passg.claim.selectOrderCalimTargetDt20List", paramMap.get());
	}
	
	/**
	 * 반품 철회 대상 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("passg.claim.selectClaimCancelTargetDtList", paramMap.get());
	}
	
	/**
	 * 교환 철회 상세 데이터 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("passg.claim.selectChangeCancelTargetDtList", paramMap.get());
	}
	
	/**
	 * 회수확인,회수완료 대상 데이터 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectPaSsgRecoveryList(ParamMap paramMap) throws Exception {
		return list("passg.claim.selectPaSsgRecoveryList", paramMap.get());
	}

	/**
	 * PaOrderM doflag 수정
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int updatePaOrderMDoFlag(Map<String,Object> paorderm) throws Exception {
		return update("passg.claim.updatePaOrderMDoFlag", paorderm); 
	}
	
	/**
	 * 취소 승인/거부 처리 대상 조회
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaSsgOrderCancelList() throws Exception {
		return list("passg.claim.selectPaSsgOrderCancelList", null);
	}
	
	public int updatePaSsgCancelList(ParamMap paramMap) throws Exception {
		return update("passg.claim.updatePaSsgCancelList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaSsgCancelListVO> selectPaSsgCancelList(ParamMap paramMap) throws Exception {
		return list("passg.claim.selectPaSsgCancelList", paramMap.get());
	}
	
	public int updatePaOrdermReturnRefuse(ParamMap paramMap) throws Exception {
		return update("passg.claim.updatePaOrdermReturnRefuse", paramMap.get());
	}
	
	/**
	 * 원클레임 데이터 존재 여부 확인
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int countOrgClaimList(PaSsgClaimListVO vo) throws Exception {
		return (Integer) selectByPk("passg.claim.countOrgClaimList", vo);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectSsgExchangeDeliveryDt(PaSsgClaimListVO vo) throws Exception {
		return (HashMap<String, Object>) selectByPk("passg.claim.selectSsgExchangeDeliveryDt", vo);
	}
	
	public int updatePaSsgCancelList4Withdraw(ParamMap paramMap) throws Exception {
		return update("passg.claim.updatePaSsgCancelList4Withdraw", paramMap.get());
	}
	
	public int updatePaSsgCancelComplete(PaSsgCancelListVO vo) throws Exception {
		return update("passg.claim.updatePaSsgCancelComplete", vo);
	}
	
	public int selectPaSsgClaimCompleteCount(PaSsgClaimListVO vo) throws Exception {
		return (Integer) selectByPk("passg.claim.selectPaSsgClaimCompleteCount", vo);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaSsgClaimInfo(PaSsgClaimListVO vo) throws Exception {
		return (HashMap<String, Object>) selectByPk("passg.claim.selectPaSsgClaimInfo", vo);
	}

	public String selectDelyType(String itemId) throws Exception {
		return (String) selectByPk("passg.claim.selectDelyType", itemId);

	}

	public String selectPaSsgCancelListProcFlag(PaSsgCancelListVO paSsgCancelVo) throws Exception {
		return (String) selectByPk("passg.claim.selectPaSsgCancelListProcFlag", paSsgCancelVo);

	}

	public String selectOrderdtDoFlag(PaSsgCancelListVO paSsgCancelVo) throws Exception {
		return (String) selectByPk("passg.claim.selectOrderdtDoFlag", paSsgCancelVo);

	}

	public PaSsgOrderListVO selectPaSsgOrderList(PaSsgCancelListVO paSsgCancelVo) {
		return (PaSsgOrderListVO) selectByPk("passg.claim.selectPaSsgOrderList", paSsgCancelVo);

	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception {
		return list("passg.claim.selectPaMobileOrderAutoCancelList", paramMap.get());
	}
}
