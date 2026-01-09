package com.cware.netshopping.pagmkt.cancel.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaGmkCancel;
import com.cware.netshopping.domain.model.PaGmkOrder;


@Service("pagmkt.cancel.PaGmktCancelDAO")
public class PaGmktCancelDAO extends AbstractPaDAO{
	
	public int checkPaGmktCancelList(PaGmkCancel paGmktCancel) throws Exception {
		return (Integer)selectByPk("pagmkt.cancel.checkPaGmktCancelList", paGmktCancel);
	}
	
	public int insertPaGmktCancelList(PaGmkCancel paGmktCancel) throws Exception {
		return insert("pagmkt.cancel.insertPaGmktCancelList", paGmktCancel);
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectPaGmktOrdCancelList(ParamMap paramMap) throws Exception {
		return list("pagmkt.cancel.selectPaGmktOrdCancelList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectPaGmktOrdCancelListForBo() throws Exception {
		return list("pagmkt.cancel.selectPaGmktOrdCancelListForBo", null);
	}
	
	public int updatePaGmktCancelListProcFlag(HashMap<String, Object> hashMap) throws Exception{
		return update("pagmkt.cancel.updatePaGmktCancelListProcFlag", hashMap);

	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaGmktOrdCancel(HashMap<String,Object> paramMap) throws Exception{
		return (HashMap<String, Object>)selectByPk("pagmkt.cancel.selectPaGmktOrdCancel",  paramMap);
	}
	
	public int updatePaGmktOrderListProcFlag(HashMap<String, Object> hashMap) throws Exception{
		return update("pagmkt.cancel.updatePaGmktOrderListProcFlag", hashMap);
	}
	
	
	public int selectBusinessDayAccount(HashMap<String, Object> hashMap) throws Exception{
		return (Integer) selectByPk("pagmkt.cancel.selectBusinessDayAccount", hashMap);
	}

	public int checkPaGmktCancelWithdawList(PaGmkCancel cancel) throws Exception {
		return (Integer) selectByPk("pagmkt.cancel.checkPaGmktCancelWithdawList", cancel);

	}

	public int updatePaGmktCancelListWithdraw(PaGmkCancel cancel) throws Exception {
		return update("pagmkt.cancel.updatePaGmktCancelListWithdraw", cancel);
	}

	public int checkPaGmktCancelDoneList(PaGmkCancel cancel) throws Exception {
		return (Integer) selectByPk("pagmkt.cancel.checkPaGmktCancelDoneList", cancel);
	}

	public int checkPaGmktClaimDoneDoFlag(PaGmkCancel cancel) throws Exception{
		return (Integer) selectByPk("pagmkt.cancel.checkPaGmktClaimDoneDoFlag", cancel);
	}

	public Object selectPaGmktCancelReqQty(PaGmkCancel cancel) throws Exception{
		return (String) selectByPk("pagmkt.cancel.selectPaGmktCancelReqQty", cancel);
	}

	public int checkExistOrgOrder(PaGmkCancel paGmktCancel) throws Exception{
		return (Integer) selectByPk("pagmkt.cancel.checkExistOrgOrder", paGmktCancel);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectUnpaidPreOrderList(ParamMap paramMap) throws Exception{
		return list("pagmkt.cancel.selectUnpaidPreOrderList", paramMap.get());
	}

	public String selectDelyType(String contrNo) throws Exception{
		return (String) selectByPk("pagmkt.cancel.selectDelyType", contrNo);

	}

	public String selectOrderdtDoFlag(PaGmkCancel paGmktCancel) throws Exception{
		return (String) selectByPk("pagmkt.cancel.selectOrderdtDoFlag", paGmktCancel);
	}

	public String selectPaGmktCancelProcFlag(PaGmkCancel paGmktCancel) throws Exception{
		return (String) selectByPk("pagmkt.cancel.selectPaGmktCancelProcFlag", paGmktCancel);

	}

	public PaGmkOrder selectPaGmktOrderList(PaGmkCancel paGmktCancel) throws Exception{
		return (PaGmkOrder) selectByPk("pagmkt.cancel.selectPaGmktOrderList", paGmktCancel);

	}
	
	public List<Object> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception{
		return list("pagmkt.cancel.selectPaMobileOrderAutoCancelList", paramMap.get());
	}	
	
	
}