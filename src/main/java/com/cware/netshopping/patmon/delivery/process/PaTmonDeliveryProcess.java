package com.cware.netshopping.patmon.delivery.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaTmonOrderListVO;

public interface PaTmonDeliveryProcess {

	public String saveTmonOrderList(List<PaTmonOrderListVO> paTmonOrderList) throws Exception;
	
	public List<Map<String, Object>> selectDeliveryReadyList() throws Exception;
	
	public List<Map<String, Object>> selectDeliveryReadyDetailList(ParamMap paramMap) throws Exception;
	
	public List<Map<String, Object>> selectSlipOutProcList() throws Exception;
	
	public List<Map<String, Object>> selectUpdateSlipOutProcList() throws Exception;
	
	public List<Map<String, Object>> selectSlipOutProcOptionList(ParamMap paramMap) throws Exception;
	
	public List<Map<String, Object>> selectUpdateSlipOutProcOptionList(ParamMap paramMap) throws Exception;
	
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception;
	
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception;
	
	public OrderpromoVO selectOrderPromo(Map<String, Object> map) throws Exception;
	
	public String selectTmonOrderSeq(String tmonOrderNo, String deliveryNo) throws Exception;
	
	public int updatePaOrderMPreCancelYn(String tmonOrderNo) throws Exception;
	
	public int updatePreCanYn(Map<String, Object> map) throws Exception;
	
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception;
	
	public List<Map<String, Object>> selectDeliveryConfirmList() throws Exception;
	
	public List<Map<String, Object>> selectDeliveryCompleteList(int searchProcDate) throws Exception;

	public String updatePaTmonOrderListInitial(PaTmonOrderListVO vo) throws Exception;
	
	public String updatePaTmonDeliveryComplete(PaTmonOrderListVO vo) throws Exception;
	
	public int updatePaOrderMDoFlag(ParamMap paramMap) throws Exception;

}
