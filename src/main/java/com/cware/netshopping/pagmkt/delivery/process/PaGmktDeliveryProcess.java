package com.cware.netshopping.pagmkt.delivery.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaGmkDeliveryComplete;
import com.cware.netshopping.domain.model.PaGmkNotRecive;
import com.cware.netshopping.domain.model.PaGmkOrder;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;

public interface PaGmktDeliveryProcess {

    public List<Map<String,String>> selectDeliveryCompleteList(ParamMap paramMap) throws Exception;
    public int updateDeliveryCompleteProc(Map<?, ?> rtnMap) throws Exception;
    public int updateDeliveryCompleteList(List<PaGmkDeliveryComplete> paGmkDeliveryCompleteList) throws Exception;
    public List<Map<String,Object>> selectSlipOutProcList(ParamMap paramMap) throws Exception;
    public int updateSlipOutProc(Map<?,?> slipOut) throws Exception;
	public int updatePaOrderMFail(Map<?,?> slipOutFail) throws Exception;
	public int insertPaGmktOrderList(PaGmkOrder paGmktOrder) throws Exception;
	public void insertPaGmktNotReceiveList(PaGmkNotRecive notReceive) throws Exception;
    public HashMap<String, String> selectTpaOrdermForNotReceive(Map<String,String> paramMap) throws Exception;  
    public List<Map<String,String>> selectNotReceiveListForCancel(ParamMap paramMap) throws Exception;    
    public List<Map<String,Object>> selectChangingDelyList(ParamMap paramMap) throws Exception;    
    public PaGmkNotRecive selectNotReceiveListDetail(Map<String, Object> paramMap) throws Exception;    
    public int updateUnReceiveListForCancle(Map<String, Object> paramMap) throws Exception;    
    public int updateTpaGmktShippingList(ParamMap paramMap) throws Exception;
	public int sendShipping(PaGmktAbstractRest rest, List<Map<String, Object>> slipOutProcList, ParamMap paramMap) throws Exception;
	public void updateReceiverInfo(PaGmkOrder paGmkOrder) throws Exception;
	public int updateRemark1TpaorderM(Map<String, Object> delyMap) throws Exception;
	public int checkPaGmktOrderList(PaGmkOrder paGmktOrder) throws Exception;
	public int updateTpaordermForCompleteOrderList(PaGmkOrder paGmktOrder) throws Exception;
	public int updatePayDate(PaGmkOrder paGmktOrder) throws Exception;
	public int checkUnpaidPaGmktOrderList(PaGmkOrder paGmktOrder) throws Exception;
	public int sendShipping4ChangeInvoice(PaGmktAbstractRest rest, List<Map<String, Object>> slipChangeProcList, ParamMap paramMap) throws Exception;
	public List<Map<String, Object>> selectSlipChangeProcList(ParamMap paramMap) throws Exception;

}