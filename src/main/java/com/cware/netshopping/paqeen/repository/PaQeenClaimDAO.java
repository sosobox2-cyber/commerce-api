package com.cware.netshopping.paqeen.repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaQeenClaimListVO;


@Repository("paqeen.claim.paQeenClaimDAO")
public class PaQeenClaimDAO extends AbstractPaDAO {

	/**
	 * 원주문 데이터 유무 체크
	 * @param PaQeenClaimListVO
	 * @return int
	 * @throws Exception
	 */
	public int countOrderList(PaQeenClaimListVO paQeenClaimList) throws Exception {
		return (Integer) selectByPk("paqeen.claim.countOrderList", paQeenClaimList);
	}

	/**
	 * 퀸잇 클레임 리스트 체크 
	 * @param PaQeenClaimListVO
	 * @return int
	 * @throws Exception
	 */
	public int selectPaQeenClaimListCount(PaQeenClaimListVO paQeenClaimList) throws Exception {
		return (Integer) selectByPk("paqeen.claim.selectPaQeenClaimListCount", paQeenClaimList);
	}

	/**
	 * 클레임 리스트 추가 
	 * @param PaQeenClaimListVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPaQeenClaimList(PaQeenClaimListVO paQeenClaimList) throws Exception {
		return insert("paqeen.claim.insertPaQeenClaimList", paQeenClaimList);
	}

	public int selectPaQeenClaimCount(PaQeenClaimListVO paQeenClaimList) throws Exception {
		return (Integer) selectByPk("paqeen.claim.selectPaQeenClaimCount", paQeenClaimList);
	}

	/**
	 * 클레임 리스트 승인대상 조회 
	 * @param PaQeenClaimListVO
	 * @return int
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaQeenOrderClaimList(ParamMap paramMap) throws Exception {
		return list("paqeen.claim.selectPaQeenOrderClaimList", paramMap.get());
	}

	/**
	 * BO 교환데이터 생성 대상 조회 
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return list("paqeen.claim.selectClaimTargetList", paramMap.get());
	}

	/**
	 * BO 교환데이터 생성 대상 상세조회 
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return list("paqeen.claim.selectOrderChangeTargetDtList", paramMap.get());
	}

	public String compareAddress(ParamMap paramMap) throws Exception {
		return (String) selectByPk("paqeen.claim.compareAddress", paramMap.get());
	}

	public int updatePaOrdermChangeFlag(ParamMap paramMap) throws Exception {
		return update("paqeen.claim.updatePaOrdermChangeFlag", paramMap);
	}

	/**
	 * 교환 승인 결과 UPDATE
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updatePaOrderMDoFlag(Map<String, Object> map) throws Exception {
		return update("paqeen.claim.updatePaOrderMDoFlag", map); 
	}

	/**
	 * 교환 상품 출고 대상 조회
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaQeenClaimDeliveryList(ParamMap paramMap) throws Exception {
		return list("paqeen.claim.selectPaQeenClaimDeliveryList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaQeenOrderClaimGroup(ParamMap paramMap) throws Exception {
		return list("paqeen.claim.selectPaQeenOrderClaimGroup", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaQeenClaimDelyInfo(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("paqeen.claim.selectPaQeenClaimDelyInfo", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaQeenClaimDeliveryGroupList(ParamMap paramMap) throws Exception {
		return list("paqeen.claim.selectPaQeenClaimDeliveryGroupList", paramMap.get());
	}
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("paqeen.claim.selectChangeCancelTargetDtList", paramMap.get());
	}
	/**
	 * BO 반품데이터 생성 대상 상세조회 
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return list("paqeen.claim.selectOrderCalimTargetDt30List", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return list("paqeen.claim.selectOrderCalimTargetDt20List", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("paqeen.claim.selectClaimCancelTargetDtList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaQeenReturnClaimList(ParamMap paramMap) throws Exception {
		return list("paqeen.claim.selectPaQeenReturnClaimList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaQeenReturnClaimGroupList(ParamMap paramMap) throws Exception {
		return list("paqeen.claim.selectPaQeenReturnClaimGroupList", paramMap.get());
	}
	
	public int updatePaOrderMDoFlagClaimNo(Map<String, Object> map) throws Exception {
		return update("paqeen.claim.updatePaOrderMDoFlagClaimNo", map); 
	}

	public int countClaimList(PaQeenClaimListVO paQeenClaimList) throws Exception {
		return (Integer) selectByPk("paqeen.claim.countClaimList", paQeenClaimList);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaQeenReturnClaimList20(ParamMap paramMap) throws Exception {
		return list("paqeen.claim.selectPaQeenReturnClaimList20", paramMap.get());
	}

	public int selectPaQeenReturnClaimCount(ParamMap paramMap) throws Exception {
		return (Integer) selectByPk("paqeen.claim.selectPaQeenReturnClaimCount", paramMap.get());
	}


}