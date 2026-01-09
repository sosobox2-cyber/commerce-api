package com.cware.netshopping.patmon.claim.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaTmonCancelListVO;
import com.cware.netshopping.domain.PaTmonClaimListVO;
import com.cware.netshopping.domain.PaTmonRedeliveryListVO;

@Service("patmon.claim.paTmonClaimDAO")
public class PaTmonClaimDAO extends AbstractPaDAO {

	/**
	 * 원주문 데이터 유무 체크
	 * @param PaTmonCancelListVO
	 * @return int
	 * @throws Exception
	 */
	public int countOrderList(PaTmonCancelListVO vo) throws Exception {
		return (Integer) selectByPk("patmon.claim.countOrderList", vo);
	}

	/**
	 * 클레임 데이터 중복 체크
	 * @param PaTmonCancelListVO
	 * @return int
	 * @throws Exception
	 */
	public int selectPaTmonCancelListCount(PaTmonCancelListVO vo) throws Exception {
		return (Integer) selectByPk("patmon.claim.selectPaTmonCancelListCount", vo);
	}

	/**
	 * INSERT TPATMONCANCELLIST
	 * @param PaTmonCancelListVO
	 * @return int
	 * @throws Exception
	 */
	public int insertTpaTmonCancelList(PaTmonCancelListVO vo) throws Exception {
		return insert("patmon.claim.insertTpaTmonCancelList", vo);
	}

	/**
	 * 취소 데이터 존재 유무 확인
	 * @param PaTmonCancelListVO
	 * @return int
	 * @throws Exception
	 */
	public int selectPaTmonWithdrawCancelListCount(PaTmonCancelListVO vo) throws Exception {
		return (Integer) selectByPk("patmon.claim.selectPaTmonCancelListCount", vo);
	}

	/**
	 * UPDATE WITHDRAW_YN = 1
	 * @param PaTmonCancelListVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePaTmonCancelList4Withdraw(PaTmonCancelListVO vo) throws Exception {
		return update("patmon.claim.updatePaTmonCancelList4Withdraw", vo);
	}

	/**
	 * 취소 승인/거부 처리 대상 조회
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaTmonOrderCancelList() throws Exception {
		return list("patmon.claim.selectPaTmonOrderCancelList", null);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaTmonCancelInfo(PaTmonCancelListVO paTmonCancelListVO) throws Exception {
		return (HashMap<String, Object>) selectByPk("patmon.claim.selectPaTmonCancelInfo", paTmonCancelListVO);
	}

	public int updateProcFlag(Map<String, Object> failData) throws Exception {
		return update("patmon.claim.updateProcFlag", failData);
	}
	
	public int updatePaTmonCancelList(ParamMap paramMap) throws Exception {
		return update("patmon.claim.updatePaTmonCancelList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return list("patmon.claim.selectClaimTargetList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("patmon.claim.selectCancelInputTargetDtList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaTmonCancelListVO> selectPaTmonCancelList(ParamMap paramMap) throws Exception {
		return list("patmon.claim.selectPaTmonCancelList", paramMap.get());
	}
	
	public int insertTmonClaimList(List<PaTmonClaimListVO> paTmonClaimList) throws Exception {
		return insert("patmon.claim.insertTmonClaimList", paTmonClaimList);
	}
	
	/**
	 * 환불 대상 데이터 존재 여부 확인
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int countClaimList(PaTmonClaimListVO vo) throws Exception {
		return (Integer) selectByPk("patmon.claim.countClaimList", vo);
	}
	
	/**
	 * 배송 정보 조회
	 * @param paTmonCancelListVO
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaTmonClaimInfo(PaTmonClaimListVO vo) throws Exception {
		return (HashMap<String, Object>) selectByPk("patmon.claim.selectPaTmonClaimInfo", vo);
	}
	
	/**
	 * PATMONCLAIMLIST 데이터 삽입
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int insertPaTmonClaimList(PaTmonClaimListVO vo) throws Exception {
		return insert("patmon.claim.insertPaTmonClaimList", vo);
	}
	
	/**
	 * 클레임 승인 대상 데이터 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaTmonReturnExchangeApprovalList(ParamMap paramMap) throws Exception {
		return list("patmon.claim.selectPaTmonReturnExchangeApprovalList", paramMap.get());
	}
	
	/**
	 * PaOrderM doflag 수정
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int updatePaOrderMDoFlag(Map<String,Object> paorderm) throws Exception {
		return update("patmon.claim.updatePaOrderMDoFlag", paorderm); 
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectTmonExchangeGoodsInfo(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("patmon.claim.selectTmonExchangeGoodsInfo", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return list("patmon.claim.selectOrderChangeTargetDtList", paramMap.get());
	}

	public String compareAddress(ParamMap paramMap) throws Exception {
		return (String) selectByPk("patmon.claim.compareAddress", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectClaimDtTargetList(ParamMap paramMap) throws Exception {
		return list("patmon.claim.selectClaimDtTargetList",paramMap.get());
	}
	
	/**
	 * 반품 상세 정보 저장 (공제금, 반품배송비, 환불금액)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int updatePaTmonClaimListDetail(PaTmonClaimListVO vo) throws Exception {
		return update("patmon.claim.updatePaTmonClaimListDetail",vo);
	}
	
	/**
	 * 반품 상세 정보 저장 (클레임사진)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int updatePaTmonClaimListPictures(PaTmonClaimListVO vo) throws Exception {
		return update("patmon.claim.updatePaTmonClaimListPictures",vo);
	}
	
	/**
	 * PAORDERM do_flag 30으로 업데이트
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int updatePaOrderMDoFlag30(PaTmonClaimListVO vo) throws Exception {
		return update("patmon.claim.updatePaOrderMDoFlag30",vo);
	}
	
	/**
	 * 반품타켓 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return list("patmon.claim.selectOrderCalimTargetDt30List", paramMap.get());
	}
	
	/**
	 * 출고전 반품 건 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return list("patmon.claim.selectOrderCalimTargetDt20List", paramMap.get());
	}

	/**
	 * 반송장 등록 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectReturnInvoiceTargetList() throws Exception {
		return list("patmon.claim.selectReturnInvoiceTargetList", null);
	}
	
	/**
	 * 반품 철회 대상 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("patmon.claim.selectClaimCancelTargetDtList", paramMap.get());
	}

	/**
	 * 교환 철회 상세 데이터 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("patmon.claim.selectChangeCancelTargetDtList", paramMap.get());
	}

	public int updatePaOrderm4PreChangeCancel(Map<String, Object> preCancelMap) throws Exception {
		return update("patmon.claim.updatePaOrderm4PreChangeCancel", preCancelMap);
	}

	public int updatePaOrdermChangeFlag(ParamMap paramMap) throws Exception {
		return update("patmon.claim.updatePaOrdermChangeFlag", paramMap.get());
	}

	/**
	 * 교환 거부 목록 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaTmonExchangeRefuseList() throws Exception {
		return list("patmon.claim.selectPaTmonExchangeRefuseList", null );
	}

	public int updatePaOrderM4ChangeRefualReuslt(ParamMap apiDataMap) throws Exception {
		return update("patmon.claim.updatePaOrderM4ChangeRefualReuslt", apiDataMap.get());
	}
	
	public int updatePaOrderMSlipOut(ParamMap paramMap) throws Exception {
		return update("patmon.claim.updatePaOrderMSlipOut", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectClaimTmonDealList(PaTmonClaimListVO vo) throws Exception {
		return list("patmon.claim.selectClaimTmonDealList", vo );
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaTmonExchangeHoldProcList(ParamMap paramMap) throws Exception {
		return list("patmon.claim.selectPaTmonExchangeHoldProcList", paramMap.get());
	}

	public int countRedeliveryList(PaTmonRedeliveryListVO vo) throws Exception {
		return (Integer) selectByPk("patmon.claim.countRedeliveryList", vo);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaTmonClaimInfo4Redelivery(PaTmonRedeliveryListVO vo) throws Exception {
		return (HashMap<String, Object>) selectByPk("patmon.claim.selectPaTmonClaimInfo", vo);
	}

	public int insertPaTmonRedeliveryList(PaTmonRedeliveryListVO vo) throws Exception {
		return insert("patmon.claim.insertPaTmonRedeliveryList", vo);
	}

	public int updatePaTmonHoldYn(Map<String, Object> exchangeHoldData) throws Exception {
		return update("patmon.claim.updatePaTmonHoldYn", exchangeHoldData);
	}
	
	public int updatePaTmonClaimList(ParamMap paramMap) throws Exception {
		return update("patmon.claim.updatePaTmonClaimList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectReturnRefuseList(ParamMap paramMap) throws Exception {
		return list("patmon.claim.selectReturnRefuseList", paramMap.get());
	}

	public int selectPaTmonCancelDtCount(PaTmonCancelListVO vo) throws Exception {
		return (Integer) selectByPk("patmon.claim.selectPaTmonCancelDtCount", vo);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectMappingSeq(ParamMap claimMap) {
		return list("patmon.claim.selectMappingSeq", claimMap.get());
	}
	
	public int mergePaTmonCancelList(PaTmonCancelListVO vo) throws Exception {
		return insert("patmon.claim.mergePaTmonCancelList", vo);
	}
	
	/**
	 * 반품 요청일자 조회
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> selectPaTmonReturnRequestedDateList() throws Exception {
		return list("patmon.claim.selectPaTmonReturnRequestedDateList", null);
	}
}
