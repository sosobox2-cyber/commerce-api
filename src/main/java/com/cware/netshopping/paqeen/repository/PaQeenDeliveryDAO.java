package com.cware.netshopping.paqeen.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;

@Repository("paQeen.delivery.PaQeenDeliveryDAO")
public class PaQeenDeliveryDAO extends AbstractPaDAO {

	public int selectCountOrderList(ParamMap param) throws Exception {
		return (Integer) selectByPk("paqeen.delivery.selectCountOrderList", param.get());
	}

	public int insertPaQeenOrderList(ParamMap param) throws Exception {
		return insert("paqeen.delivery.insertPaQeenOrderList", param.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderConfirmTargetList(ParamMap paramMap) throws Exception {
		return list("paqeen.delivery.selectOrderConfirmTargetList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectOrderInputTargetList(int paOrderCreateCnt) throws Exception {
		return list("paqeen.delivery.selectOrderInputTargetList", paOrderCreateCnt);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return list("paqeen.delivery.selectOrderInputTargetDtList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception {
		return (HashMap<String, Object>) selectByPk("paqeen.delivery.selectRefusalInfo", mappingSeq);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSlipProcList(ParamMap paramMap) throws Exception {
		return list("paqeen.delivery.selectSlipProcList", paramMap.get());
	}

	/**
	 * 퀸잇 : 송장입력 TPAORDERM UPDATE
	 * @param HashMap
	 * @return int 
	 * @throws Exception
	 */
	public int updatePaorderm(Map<String, Object> order) throws Exception {
		return update("paqeen.delivery.updatePaorderm", order);
	}

	/**
	 * 퀸잇 : 배송완료 대상 조회 
	 * @param ParamMap
	 * @return List 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectDeliveryComplete(ParamMap paramMap) throws Exception {
		return list("paqeen.delivery.selectDeliveryComplete", paramMap.get());
	}

	public int updatePreCanYn(Map<String, Object> preCancelMap) throws Exception {
		return update("paqeen.delivery.updatePreCanYn", preCancelMap);
	}

	public int insertPaOrderM(Paorderm paorderm) throws Exception {
		return insert("paqeen.delivery.insertPaOrderM", paorderm);
	}
	
}
