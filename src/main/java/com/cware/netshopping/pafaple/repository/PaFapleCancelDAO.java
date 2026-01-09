package com.cware.netshopping.pafaple.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pafaple.domain.PaFapleCancelListVO;

@Repository("pafaple.cancel.PaFapleCancelDAO")
public class PaFapleCancelDAO extends AbstractPaDAO {

	/**
	 * 패션플러스 : 취소조회 중복데이터 체크 
	 * @param PaFapleCancelListVO
	 * @return int 
	 * @throws Exception
	 */
	public int selectPaFapleCancelListCount(PaFapleCancelListVO paFapleCancelListVO) throws Exception {
		return (Integer) selectByPk("pafaple.cancel.selectPaFapleCancelListCount", paFapleCancelListVO);
	}

	/**
	 * 패션플러스 : 취소조회 반품 데이터 생성 구분 체크  
	 * @param PaFapleCancelListVO
	 * @return int 
	 * @throws Exception
	 */
	public String selectOutBefClaimGb(PaFapleCancelListVO paFapleCancelListVO) throws Exception {
		return (String) selectByPk("pafaple.cancel.selectOutBefClaimGb", paFapleCancelListVO);
	}

	/**
	 * 패션플러스 : 취소조회 취소데이터 중복 체크 
	 * @param PaFapleCancelListVO
	 * @return int 
	 * @throws Exception
	 */
	public int checkCancelExistYn(PaFapleCancelListVO paFapleCancelListVO) throws Exception {
		return (Integer) selectByPk("pafaple.cancel.checkCancelExistYn", paFapleCancelListVO);
	}

	/**
	 * 패션플러스 : 취소조회 취소데이터 저장
	 * @param PaFapleCancelListVO
	 * @return int 
	 * @throws Exception
	 */
	public int insertTpaFapleCancelList(PaFapleCancelListVO paFapleCancelListVO) throws Exception {
		return insert("pafaple.cancel.insertTpaFapleCancelList", paFapleCancelListVO);
	}

	/**
	 * 패션플러스 : 취소조회 BO 데이터 생성 대상 조회
	 * @return int 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return list("pafaple.cancel.selectClaimTargetList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("pafaple.cancel.selectCancelInputTargetDtList", paramMap.get());
	}

	public int countOrderList(PaFapleCancelListVO paFapleCancelListVO) throws Exception {
		return (Integer) selectByPk("pafaple.cancel.countOrderList", paFapleCancelListVO);
	}

	public String selectCancelOrderDelyGb(PaFapleCancelListVO paFapleCancelListVO) throws Exception {
		return (String) selectByPk("pafaple.cancel.selectCancelOrderDelyGb", paFapleCancelListVO);
	}

	public String selectCancelOrderReasonType(HashMap<String, Object> hashMap) throws Exception {
		return (String) selectByPk("pafaple.cancel.selectCancelOrderReasonType", hashMap);
	}

	public String selectDoflag(PaFapleCancelListVO paFapleCancelListVO) throws Exception {
		return (String)  selectByPk("pafaple.cancel.selectDoflag", paFapleCancelListVO);
	}

	public Integer selectClaimQty(PaFapleCancelListVO paFapleCancelListVO)throws Exception {
		return (Integer) selectByPk("pafaple.cancel.selectClaimQty", paFapleCancelListVO);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception {
		return list("pafaple.cancel.selectPaMobileOrderAutoCancelList", null);
	}

}
