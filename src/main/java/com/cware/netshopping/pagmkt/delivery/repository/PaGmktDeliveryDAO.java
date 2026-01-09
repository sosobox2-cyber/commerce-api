package com.cware.netshopping.pagmkt.delivery.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaGmkDeliveryComplete;
import com.cware.netshopping.domain.model.PaGmkNotRecive;
import com.cware.netshopping.domain.model.PaGmkOrder;

@Service("pagmkt.delivery.PaGmktDeliveryDAO")
public class PaGmktDeliveryDAO extends AbstractPaDAO{
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectDeliveryCompleteList(ParamMap paramMap) throws Exception{
		return list("pagmkt.delivery.selectDeliveryCompleteList", paramMap.get());
	}
	
	public int updateDeliveryCompleteProc(Map<?, ?> rtnMap) throws Exception{
	    return update("pagmkt.delivery.updateDeliveryCompleteProc", rtnMap);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectTpaOrdermForNotReceive(Map<String,String> paramMap) throws Exception{
		return (HashMap<String, String>)selectByPk("pagmkt.delivery.selectTpaOrdermForNotReceive", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectNotReceiveListForCancel(ParamMap paramMap) throws Exception{
		return list("pagmkt.delivery.selectNotReceiveListForCancel", paramMap.get());
	}
	
	public PaGmkNotRecive selectNotReceiveListDetail(Map<String, Object> paramMap) throws Exception{
		return (PaGmkNotRecive)selectByPk("pagmkt.delivery.selectNotReceiveListDetail", paramMap);
	}
	
	public int updateUnReceiveListForCancle(Map<String, Object> paramMap) throws Exception{
		return update("pagmkt.delivery.updateUnReceiveListForCancle", paramMap);
	}
	
	public int updateTpaGmktShippingList(ParamMap paramMap) throws Exception{
		return update("pagmkt.delivery.updateTpaGmktShippingList", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> selectChangingDelyList(ParamMap paramMap) throws Exception{
		return list("pagmkt.delivery.selectChangingDelyList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> selectSlipOutProcList(ParamMap paramMap) throws Exception{
		return list("pagmkt.delivery.selectSlipOutProcList", paramMap.get());
	}
	
	public int updateSlipOutProc(Map<?,?> slipOut) throws Exception{
		return update("pagmkt.delivery.updateSlipOutProc", slipOut);
	}

	public int updatePaOrderMFail(Map<?,?> slipOut) throws Exception{
		return update("pagmkt.delivery.updatePaOrderMFail", slipOut);
	}

	public int updateDeliveryCompleteList(PaGmkDeliveryComplete param) throws Exception{
	    return update("pagmkt.delivery.updateDeliveryCompleteList", param);
	}
	
	public int checkPaGmktOrderList(PaGmkOrder paGmktOrderList) throws Exception {
		return (Integer)selectByPk("pagmkt.delivery.checkPaGmktOrderList", paGmktOrderList);
	}
	
	public int checkUnpaidPaGmktOrderList(PaGmkOrder paGmktOrderList) throws Exception {
		return (Integer)selectByPk("pagmkt.delivery.checkUnpaidPaGmktOrderList", paGmktOrderList);
	}
	
	public int insertPaGmktOrderList(PaGmkOrder paGmktOrderList) throws Exception{
		return insert("pagmkt.delivery.insertPaGmktOrderList", paGmktOrderList);
	}
	
	public int selectPagmktNotReceiveListExist(Map<String, Object> paramMap) throws Exception {
		return (Integer)selectByPk("pagmkt.delivery.selectPagmktNotReceiveListExist", paramMap);
	}
	
	public int insertPaGmktNotReceiveList(PaGmkNotRecive notReceiveList) throws Exception {
		return insert("pagmkt.delivery.insertPaGmktNotReceiveList", notReceiveList);
	}

	public int updateReceiverInfo(PaGmkOrder paGmkOrder) throws Exception{
		return update("pagmkt.delivery.updateReceiverInfo", paGmkOrder);
	}
	
	public int updateTpagmktNotReceiveList(Map<String, Object> paramMap) throws Exception {
		return update("pagmkt.delivery.updateTpagmktNotReceiveList", paramMap);
	}
	
	public String selectCustRefId(Map<String, Object> param) throws Exception{
		return (String) selectByPk("pagmkt.delivery.selectCustRefId", param);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectCustInfoForSettingCustCounsel(Map<String, Object> paramMap) throws Exception{
		return (HashMap<String, String>)selectByPk("pagmkt.delivery.selectCustInfoForSettingCustCounsel", paramMap);
	}

	public int updateRemark1TpaorderM(Map<String, Object> delyMap) throws Exception {
		return update("pagmkt.delivery.updateRemark1TpaorderM", delyMap);
	}

	public int updateTpaordermForCompleteOrderList(PaGmkOrder paGmktOrder) throws Exception{
		return update("pagmkt.delivery.updateTpaordermForCompleteOrderList", paGmktOrder);
	}

	public int updatePayDate(PaGmkOrder paGmkOrder) throws Exception {
		return update("pagmkt.delivery.updatePayDate", paGmkOrder);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSlipChangeProcList(ParamMap paramMap) {
		return list("pagmkt.delivery.selectSlipChangeProcList", paramMap.get());
	}
}