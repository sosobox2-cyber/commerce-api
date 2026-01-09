package com.cware.netshopping.panaver.v3.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.v3.domain.ChangedProductOrderInfo;
import com.cware.netshopping.panaver.v3.domain.PaNaverV3AddressVO;
import com.cware.netshopping.panaver.v3.domain.PaNaverV3ClaimListVO;

@Repository("panaver.v3.infocommon.paNaverV3InfoCommonDAO")
public class PaNaverV3InfoCommonDAO extends AbstractPaDAO {
	
	/**
	 * 변경 상품 주문내역 조회 결과 저장 (TPANAVERORDERCHANGE)
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int mergeChangeOrderList(ChangedProductOrderInfo order) throws Exception {
		return update("panaver.v3.infocommon.mergeChangeOrderList", order);
	}

	/**
	 * 변경 내역 반영여부 업데이트 (TPANAVERORDERCHANGE.CHANGE_APPLIED_YN)
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int updateChangeApplied(ChangedProductOrderInfo order) throws Exception {
		return update("panaver.v3.infocommon.updateChangeApplied", order);
	}

	/**
	 * 주문 데이터 생성 대상 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderInputTargetList() throws Exception {
		return (List<Object>) list("panaver.v3.infocommon.selectOrderInputTargetList", null);
	}

	/**
	 * 주문 취소 데이터 생성 대상 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectCancelInputTargetList() throws Exception {
		return (List<Object>) list("panaver.v3.infocommon.selectCancelInputTargetList", null);
	}

	/**
	 * 반품 데이터 생성 대상 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderClaimTargetList() throws Exception {
		return (List<Object>) list("panaver.v3.infocommon.selectOrderClaimTargetList", null);
	}
	
	/**
	 * 교환 데이터 생성 대상 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetList() throws Exception {
		return (List<Object>) list("panaver.v3.infocommon.selectOrderChangeTargetList", null);
	}

	/**
	 * 반품 철회 데이터 생성 대상 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetList() throws Exception {
		return (List<Object>) list("panaver.v3.infocommon.selectClaimCancelTargetList", null);
	}

	/**
	 * 교환 철회 데이터 생성 대상 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetList() throws Exception {
		return (List<Object>) list("panaver.v3.infocommon.selectChangeCancelTargetList", null);
	}

	/**
	 * 클레임 순번 조회 (TPANAVERCLAIMLIST.CLAIM_SEQ)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String selectClaimSeq() throws Exception {
		return (String) selectByPk("panaver.v3.infocommon.selectClaimSeq", null);
	}

	/**
	 * 취소승인처리 결과 업데이트 (TPANAVERORDERLIST)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updateOrderListClaimInfo(ParamMap paramMap) throws Exception {
		return update("panaver.v3.infocommon.updateOrderListClaimInfo", paramMap.get());
	}

	/**
	 * 주소 시퀀스 조회
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String selectAddressSeq() throws Exception{
		return (String) selectByPk("panaver.v3.infocommon.selectAddressSeq", null);
	}

	/**
	 * TPANAVERORDERLIST 업데이트 
	 * 
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updateOrderListAddress(ParamMap paramMap) throws Exception{
		return update("panaver.v3.infocommon.updateOrderListAddress", paramMap.get());
	}
	
	/**
	 * 
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectWaitingPaordermCnt(ParamMap paramMap) throws Exception {
		return (int) selectByPk("panaver.v3.infocommon.selectWaitingPaordermCnt", paramMap.get());
	}
	
	/**
	 * TPAORDERM PA_PROC_QTY 업데이트
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updatePaOrdermOrderPlace(ParamMap paramMap) throws Exception{
		return update("panaver.v3.infocommon.updatePaOrdermOrderPlaceQuantity", paramMap.get());
	}
	
	/**
	 * TPANAVERORDERLIST ORDER_ID 업데이트
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updateOrderListOrderId(ParamMap paramMap) throws Exception{
		return update("panaver.v3.infocommon.updateOrderListOrderId", paramMap.get());
	}

	/**
	 * 네이버 주문 생성여부 조회 (TPANAVERORDERLIST)
	 * 
	 * @param productOrderId
	 * @return
	 * @throws Exception
	 */
	public int selectOrderListCnt(String productOrderId) throws Exception {
		return (int) selectByPk("panaver.v3.infocommon.selectOrderListCnt", productOrderId);
	}

	/**
	 * 취소완료건 시퀀스 정보 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String selectCancelDoneClaim(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.v3.infocommon.selectCancelDoneClaim", paramMap.get());
	}

	/**
	 * 네이버 주문 취소 저장 (TPANAVERCLAIMLIST)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int insertCancelClaim(PaNaverV3ClaimListVO claim) throws Exception {
		return insert("panaver.v3.infocommon.insertCancelClaim", claim);
	}

	/**
	 * 네이버 교환완료 여부 조회
	 * 
	 * @param productOrderId
	 * @return
	 * @throws Exception
	 */
	public int selectExchangeClaimDoneCnt(String productOrderId) throws Exception {
		return (int) selectByPk("panaver.v3.infocommon.selectExchangeClaimDoneCnt", productOrderId);
	}

	/**
	 * 네이버 교환 최근 클레임 번호 조회
	 * 
	 * @param productOrderId
	 * @return
	 * @throws Exception
	 */
	public String selectRecentExchangeClaimId(String productOrderId) throws Exception {
		return (String) selectByPk("panaver.v3.infocommon.selectRecentExchangeClaimID", productOrderId);
	}

	/**
	 * 네이버 취소완료 주문정보 조회
	 * 
	 * @param productOrderId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectCancelDoneInfo(String productOrderId) throws Exception {
		return (HashMap<String, String>) selectByPk("panaver.v3.infocommon.selectCancelDoneInfo", productOrderId);
	}

	/**
	 * 주문 진행단계 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String selectOrderdtDoFlag(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.v3.infocommon.selectOrderdtDoFlag", paramMap.get());
	}

	/**
	 * 네이버 주문 취소건수 조회 (TPAORDERM)
	 * 
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int selectPaOrderMCancelCnt(Paorderm paorderm) throws Exception {
		return (int) selectByPk("panaver.v3.infocommon.selectPaOrderMCancelCnt", paorderm);
	}
	
	/**
	 * 네이버 주문 제휴사 클레임 번호 업데이트 (TPAORDERM)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updatePaordermClaimId(ParamMap paramMap) throws Exception {
		return (int) update("panaver.v3.infocommon.updatePaordermClaimId", paramMap.get());
	}

	/**
	 * 네이버 교환 데이터 저장 (TPANAVERCLAIMLIST)
	 * 
	 * @param paNaverV3ClaimListVO
	 * @return
	 * @throws Exception
	 */
	public int insertExchangeClaim(PaNaverV3ClaimListVO paNaverV3ClaimListVO) throws Exception {
		return insert("panaver.v3.infocommon.insertExchangeClaim", paNaverV3ClaimListVO);
	}

	/**
	 * 네이버 교환 기취소 대상건 조회 (TPAORDERM)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectPreCancelExchangeTarget(ParamMap paramMap) throws Exception {
		return (HashMap<String, String>) selectByPk("panaver.v3.infocommon.selectPreCancelExchangeTarget", paramMap.get());
	}

	/**
	 * 네이버 교환 기취소 여부 업데이트 (TPAORDERM.PRE_CANCEL_YN)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updatePreCancelExchange(ParamMap paramMap) throws Exception {
		return update("panaver.v3.infocommon.updatePreCancelExchange", paramMap.get());
	}
	
	/**
	 * 네이버 교환 수거건 배송단계 조회 (TPAORDERM.PA_DO_FLAG)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String selectExchangeCollectDoFlag(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.v3.infocommon.selectExchangeCollectDoFlag", paramMap.get());
	}

	/**
	 * 네이버 주소 저장 (TPANAVERADDRESS)
	 * 
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public int insertPaNaverAddress(PaNaverV3AddressVO address) throws Exception {
		return insert("panaver.v3.infocommon.insertPaNaverAddress", address);
	}

	/**
	 * 네이버 반품 데이터 저장 (TPANAVERCLAIMLIST)
	 * 
	 * @param paNaverV3ClaimListVO
	 * @return
	 * @throws Exception
	 */
	public int insertReturnClaim(PaNaverV3ClaimListVO paNaverV3ClaimListVO) throws Exception {
		return insert("panaver.v3.infocommon.insertReturnClaim", paNaverV3ClaimListVO);
	}

	/**
	 * 네이버 반품 수거 배송단계 조회 (TPAORDERM)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String selectReturnCollectDoFlag(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.v3.infocommon.selectReturnCollectDoFlag", paramMap.get());
	}

	/**
	 * 최근 반품 클레임 시퀀스 조회
	 * 
	 * @param productOrderId
	 * @return
	 * @throws Exception
	 */
	public String selectRecentReturnClaimId(String productOrderId) throws Exception {
		return (String) selectByPk("panaver.v3.infocommon.selectRecentReturnClaimId", productOrderId);
	}

	/**
	 * 제휴 주문 진행단계 업데이트 (TPANAVERORDERLIST.PROC_FLAG)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updateProcFlag(ParamMap paramMap) throws Exception {
		return update("panaver.v3.infocommon.updateProcFlag", paramMap.get());
	}
	
	/**
	 * 진행중인 클레임 건수 조회
	 * 
	 * @param paramMap 
	 * @return
	 * @throws Exception
	 */
	public String selectExistingClaimCount(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.v3.infocommon.selectExistingClaimCount", paramMap.get());
	}

	/**
	 * 클레임 배송비 정보 조회
	 * 
	 * @param paramMap 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectClaimShipcostInfo(ParamMap paramMap) throws Exception {
		return (HashMap<String, String>) selectByPk("panaver.v3.infocommon.selectClaimShipcostInfo", paramMap.get());
	}

	/**
	 * 변경 상품 주문 내역 미반영건 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ChangedProductOrderInfo> selectUnappliedChangedInfo(ParamMap paramMap) throws Exception {
		return (List<ChangedProductOrderInfo>) list("panaver.v3.infocommon.selectUnappliedChangedInfo", paramMap.get());
	}

	/**
	 * 변경 상품 주문 내역 미반영건 대상 제외 처리
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int updateUnappliedChangedInfo(ChangedProductOrderInfo order) throws Exception {
		return delete("panaver.v3.infocommon.updateUnappliedChangedInfo", order);
	}

	/**
	 * 취소 승인 대상 여부 조회 (미처리건 > 취소 재승인 대상)
	 * 
	 * @param productOrderInfo
	 * @return
	 * @throws Exception
	 */
	public int selectCancelProcTarget(ParamMap paramMap) throws Exception {
		return (int) selectByPk("panaver.v3.infocommon.selectCancelProcTarget", paramMap.get());
	}

	/**
	 * 클레임 철회 데이터 생성 여부 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int selectExistingClaimReject(ParamMap paramMap) throws Exception {
		return (int) selectByPk("panaver.v3.infocommon.selectExistingClaimReject", paramMap.get());
	}

	/**
	 * 취소 철회 데이터 생성 여부 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int selectExistingCancelReject(ParamMap paramMap) throws Exception {
		return (int) selectByPk("panaver.v3.infocommon.selectExistingCancelReject", paramMap.get());
	}

	/**
	 * MAPPING_SEQ 조회 (TPAORDERM)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String selectMappingSeqByProductOrderInfo(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.v3.infocommon.selectMappingSeqByProductOrderInfo", paramMap.get());
	}
}
