package com.cware.netshopping.pawemp.claim.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaWempClaimItemList;
import com.cware.netshopping.domain.model.PaWempClaimList;

@Service("pawemp.claim.paWempClaimDAO")
public class PaWempClaimDAO extends AbstractPaDAO{

	@SuppressWarnings("unchecked")
	public List<PaWempClaimItemList> selectPaWempClaimItemList(ParamMap paramMap) throws Exception {
		return list("pawemp.claim.selectPaWempClaimItemList", paramMap.get());
	}
	
	/**
	 * 위메프 반품접수 대상조회 - 상세(반품접수건)
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderReturnTargetDt30List(ParamMap paramMap) throws Exception{
		return list("pawemp.claim.selectOrderReturnTargetDt30List", paramMap.get());
	}
	
	/**
	 * 위메프 반품접수 대상조회 -  상세(출하지시 이후 취소건)
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderReturnTargetDt20List(ParamMap paramMap) throws Exception{
		return list("pawemp.claim.selectOrderReturnTargetDt20List", paramMap.get());
	}
	
	/**
	 * 위메프 반품생성 대상조회 
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderReturnTargetList() throws Exception{
		return list("pawemp.claim.selectOrderReturnTargetList", null);
	}

	/**
	 * 위메프 클레임신청목록 조회 - 데이터 중복 체크
	 * @param PaWempClaimItemlistVO
	 * @return Integer
	 * @throws Exception
	 */
	public int selectPaWempClaimListExists(PaWempClaimList paWempClaimList) throws Exception{
		return (Integer) selectByPk("pawemp.claim.selectPaWempClaimListExists", paWempClaimList);
	}
	
	/**
	 * TPAWEMPCLAIMLIST INSERT
	 * @param paCopnClaimList
	 * @return
	 * @throws Exception
	 */
	public int insertPaWempClaimList(PaWempClaimList paWempClaimList) throws Exception{
		return insert("pawemp.claim.insertPaWempClaimList", paWempClaimList);
	}
	
	/**
	 * TPAWEMPCLAIMITEMLIST INSERT
	 * @param paCopnClaimitemList
	 * @return
	 * @throws Exception
	 */
	public int insertPaWempClaimItemList(PaWempClaimItemList paCopnClaimitemList) throws Exception{
		return insert("pawemp.claim.insertPaWempClaimItemList", paCopnClaimitemList);
	}
	
	/**
	 * 위메프 반품취소 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectReturnCancelTargetDtList(ParamMap paramMap) throws Exception{
		return list("pawemp.claim.selectReturnCancelTargetDtList", paramMap.get());
	}
	
	/**
	 * 위메프 반품취소 대상조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectReturnCancelTargetList() throws Exception{
		return list("pawemp.claim.selectReturnCancelTargetList",null);
	}
	
	/**
	 * 반품수거등록 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectPickupList() throws Exception{
		return list("pawemp.claim.selectPickupList", null);
	}
	
	/**
	 * 반품수거요청후 결과 저장
	 * @param pickup
	 * @return
	 * @throws Exception
	 */
	public int updatePickupResult(HashMap<String, Object> pickup) throws Exception{
		return update("pawemp.claim.updatePickupResult", pickup);
	}
	
	/**
	 * 반품상품 입고 확인처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectReceiveConfirmList() throws Exception{
		return list("pawemp.claim.selectReceiveConfirmList", null);
	}
	
	/**
	 * 반품상품 입고 확인처리 후 결과 저장
	 * @param receive
	 * @return
	 * @throws Exception
	 */
	public int updateReceiveConfirmResult(HashMap<String, Object> receive) throws Exception{
		return update("pawemp.claim.updateReceiveConfirmResult", receive);
	}
	
	/**
	 * 반품요청 승인 처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectReturnApprovalList() throws Exception{
		return list("pawemp.claim.selectReturnApprovalList", null);
	}
	
	/**
	 * 반품요청 승인 처리 후 결과 저장
	 * @param returnApproval
	 * @return
	 * @throws Exception
	 */
	public int updateReturnApprovalResult(HashMap<String, Object> returnApproval) throws Exception{
		return update("pawemp.claim.updateReturnApprovalResult", returnApproval);
	}
	
	/**
	 * 판매중지, 판매재개 상품 조회
	 * 
	 * @param paramMap
	 * @return PaWempGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaWempClaimList> selectPaWempReturnExists(PaWempClaimList paWempClaimList) throws Exception {
		return list("pawemp.claim.selectPaWempReturnExists", paWempClaimList);
	}

	/**
	 * 기존 배송지와 비교
	 * 
	 * @param paramMap
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String compareAddress(ParamMap paramMap) throws Exception {
		return (String) selectByPk("pawemp.claim.compareAddress", paramMap.get());
	}
}
