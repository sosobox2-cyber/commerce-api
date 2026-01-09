package com.cware.netshopping.pafaple.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pafaple.domain.PaFapleOrderListVO;

@Repository("pafaple.delivery.PaFapleDeliveryDAO")
public class PaFapleDeliveryDAO extends AbstractPaDAO {

	/**
	 * 패션플러스 : 송장입력 대상 조회 
	 * @param HashMap
	 * @return int 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSlipProcList(ParamMap paramMap) throws Exception {
		return list("pafaple.delivery.selectSlipProcList", paramMap.get());
	}

	/**
	 * 패션플러스 : 송장입력 TPAORDERM UPDATE
	 * @param HashMap
	 * @return int 
	 * @throws Exception
	 */
	public int updatePaorderm(Map<String, Object> order) throws Exception {
		return update("pafaple.delivery.updatePaorderm", order);
	}

	/**
	 * 패션플러스 : 주문내역(TPAFAPLE_ORDERLIST) 중복확인
	 * @param PaFapleOrderListVO
	 * @return int 
	 * @throws Exception
	 */
	public int checkOrderExistYn(PaFapleOrderListVO paFapleOrderListVO) {
		return (Integer) selectByPk("pafaple.delivery.checkOrderExistYn", paFapleOrderListVO);
	}

	/**
	 * 패션플러스 : 주문내역(TPAFAPLE_ORDERLIST) INSERT 
	 * @param PaFapleOrderListVO
	 * @return int 
	 * @throws Exception
	 */
	public int insertPaFapleOrderList(PaFapleOrderListVO paFapleOrderListVO) {
		return insert("pafaple.delivery.insertPaFapleOrderList", paFapleOrderListVO);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderConfirmTargetList() throws Exception {
		return list("pafaple.delivery.selectOrderConfirmTargetList", null);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectOrderInputTargetList(int paOrderCreateCnt) throws Exception {
		return list("pafaple.delivery.selectOrderInputTargetList", paOrderCreateCnt);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return list("pafaple.delivery.selectOrderInputTargetDtList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectRefusalInfo(List<HashMap<String, Object>> itemList) throws Exception {
		return list("pafaple.delivery.selectRefusalInfo", itemList);
	}

	public String selectFapleMappingSeq(Map<String, Object> cancelInfo) throws Exception {
		return (String) selectByPk("pafaple.delivery.selectFapleMappingSeq", cancelInfo);
	}

	public int updatePreCanYn(Map<String, Object> preCancelMap) throws Exception {
		return update("pafaple.delivery.updatePreCanYn", preCancelMap);
	}
}
