package com.cware.netshopping.pacommon.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.CancelInputVO;
import com.cware.netshopping.domain.OrderInputVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PreoutOrderInputVO;
import com.cware.netshopping.domain.model.PaMonitering;


public interface PaOrderService {

	public HashMap<String, Object>[] saveOrderTx(OrderInputVO[] orderInputVO) throws Exception;

	public HashMap<String, Object> saveCancelTx(CancelInputVO cancelInputVO) throws Exception;

	public int updatePaOrdermTx(ParamMap paramMap) throws Exception;
	
	public int insertTpaMonitering(PaMonitering pamoniter) throws Exception;
	
	public int getTpaMonitering(PaMonitering pamoniter) throws Exception;
	
	public List<Object> selectOrderTagetDt(String paOrder) throws Exception;

	public int updatePreCancelOrder(String mappingSeq) throws Exception;

	public void upDateOrderTx(ParamMap paramMap) throws Exception;

	public HashMap<String, Object>[] newSaveOrderTx(OrderInputVO[] orderInputVO) throws Exception;

	public int updatePaOrderMFailConfrimPreOrder(HashMap<String, String> hmSheet, String message) throws Exception;

	public HashMap<String, Object> newSaveCancelTx(CancelInputVO cancelInputVO) throws Exception;

	public String getConfig(String string) throws Exception;

	public OrderpromoVO selectPaOrderPromo(ParamMap promoParam) throws Exception;

	public OrderpromoVO selectPaOrderPromo(Map<String, Object> map) throws Exception;
	
	public int updateOrderCancelYnTx(HashMap<String, String> map) throws Exception;

	public int insertTpaSlipInfoTx(Map<String, Object> oc) throws Exception;

	public String selectOrderGoodsDtName(ParamMap goodsDtParam) throws Exception;
	
	public int selectOrderGoodsDtDupleCheck(ParamMap goodsDtParam) throws Exception;

	public int updateRemark3NTx(HashMap<String, String> map)throws Exception;

	public List<Map<String, String>> selectPreoutOrderTargetList() throws Exception;

	public List<Map<String, Object>> selectPreoutOrderTargetDtList(ParamMap paramMap) throws Exception;

	public HashMap<String, Object>[] savePreoutOrderTx(PreoutOrderInputVO[] preoutOrderInputVO) throws Exception;

	public int updatePreoutPaOrdermTx(ParamMap paramMap) throws Exception;
	
}