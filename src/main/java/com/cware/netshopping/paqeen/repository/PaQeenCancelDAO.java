package com.cware.netshopping.paqeen.repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaQeenCancelListVO;
import com.cware.netshopping.domain.model.Paorderm;


@Repository("paqeen.cancel.paQeenCancelDAO")
public class PaQeenCancelDAO extends AbstractPaDAO {

	public int countOrderList(PaQeenCancelListVO paQeenCancelList) throws Exception {
		return (Integer) selectByPk("paqeen.cancel.countOrderList", paQeenCancelList);
	}

	public String selectCancelOrderDelyGb(PaQeenCancelListVO paQeenCancelList) throws Exception {
		return (String) selectByPk("paqeen.cancel.selectCancelOrderDelyGb", paQeenCancelList);
	}

	public String selectDoflag(PaQeenCancelListVO paQeenCancelList) throws Exception {
		return (String)  selectByPk("paqeen.cancel.selectDoflag",  paQeenCancelList);
	}

	public int selectPaQeenCancelListCount(PaQeenCancelListVO paQeenCancelList) throws Exception {
		return (Integer) selectByPk("paqeen.cancel.selectPaQeenCancelListCount", paQeenCancelList);
	}

	public String selectOutBefClaimGb(PaQeenCancelListVO paQeenCancelList) throws Exception {
		return (String) selectByPk("paqeen.cancel.selectOutBefClaimGb", paQeenCancelList);
	}

	public String selectCancelOrderReasonType(HashMap<String, Object> hashMap)  throws Exception {
		return (String)  selectByPk("paqeen.cancel.selectCancelOrderReasonType",  hashMap);
	}

	public Integer selectClaimQty(PaQeenCancelListVO paQeenCancelList) throws Exception {
		return (Integer) selectByPk("paqeen.cancel.selectPaQeenCancelListCount", paQeenCancelList);
	}

	public int checkCancelExistYn(PaQeenCancelListVO paQeenCancelList) throws Exception {
		return (Integer) selectByPk("paqeen.cancel.checkCancelExistYn", paQeenCancelList);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return list("paqeen.cancel.selectClaimTargetList", paramMap.get());
	}

	public int insertTpaQeenCancelList(PaQeenCancelListVO paQeenCancelList) throws Exception {
		return insert("paqeen.cancel.insertTpaQeenCancelList", paQeenCancelList);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("paqeen.cancel.selectCancelInputTargetDtList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaQeenOrderCancelList(ParamMap paramMap) throws Exception {
		return list("paqeen.cancel.selectPaQeenOrderCancelList", paramMap.get());
	}

	public int updatePaQeenCancelStatus(Paorderm paorderm)  throws Exception {
		return update("paqeen.cancel.updatePaQeenCancelStatus", paorderm);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaQeenOrderCancelRefusalList(Map<String, Object> hashMap) throws Exception {
		return list("paqeen.cancel.selectPaQeenOrderCancelRefusalList", hashMap);
	}

	public int updatePaQeenCancelRefusalList(ParamMap paramMap) throws Exception {
		return update("paqeen.cancel.updatePaQeenCancelRefusalList", paramMap.get());
	}

	public int updatePaQeenCustomerNegligence(ParamMap paramMap) throws Exception {
		return update("paqeen.cancel.updatePaQeenCustomerNegligence", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectPaQeenSoldOutordList(ParamMap paramMap) throws Exception {
		return list("paqeen.cancel.selectPaQeenSoldOutordList", paramMap.get());
	}

	public String selectCustCmpstnReason(HashMap<String, Object> cancelItem) throws Exception {
		return (String) selectByPk("paqeen.cancel.selectCustCmpstnReason", cancelItem);
	}

	public int updatePaOrderM(Paorderm paorderm) throws Exception {
		return update("paqeen.cancel.updatePaOrderM", paorderm);
	}

	public int updateTpaQeenCancelList(PaQeenCancelListVO paQeenCancelList) throws Exception {
		return update("paqeen.cancel.updateTpaQeenCancelList", paQeenCancelList);
	}

	public int checkOrdermExistYn(Paorderm paorderm) throws Exception {
		return (Integer) selectByPk("paqeen.cancel.checkOrdermExistYn", paorderm);
	}

	public int checkQeenCancelList(Paorderm paorderm) throws Exception {
		return (Integer) selectByPk("paqeen.cancel.checkQeenCancelList", paorderm);
	}

	public int updateCancelWithdrawYnTx(PaQeenCancelListVO paQeenCancelListVO) throws Exception {
		return update("paqeen.cancel.updateCancelWithdrawYnTx", paQeenCancelListVO);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() {
		return list("paqeen.cancel.selectPaMobileOrderAutoCancelList", null);
	}
}