package com.cware.netshopping.pacommon.order.process;

import java.util.*;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.CancelInputVO;
import com.cware.netshopping.domain.OrderInputVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PreoutOrderInputVO;
import com.cware.netshopping.domain.model.PaMonitering;


public interface PaOrderProcess {
	
	public HashMap<String, Object>[] saveOrder(OrderInputVO[] orderInputVO) throws Exception;
	
	public HashMap<String, Object> saveCancel(CancelInputVO cancelInputVO) throws Exception;
	
	public int updatePaOrderm(ParamMap paramMap) throws Exception;
	
    public String getSysdatetimeToString() throws Exception;
    
    public int insertTpaMonitering(PaMonitering pamoniter) throws Exception;
    
    public int getTpaMonitering(PaMonitering pamoniter) throws Exception;
    
    public List<Object> selectOrderTagetDt(String paOrderNo) throws Exception;

	public int updatePreCancelOrder(String mappingSeq) throws Exception;

	public void upDateOrder(ParamMap paramMap) throws Exception;

	public HashMap<String, Object>[] newSaveOrder(OrderInputVO[] orderInputVO) throws Exception;

	public int updatePaOrderMFailConfrimPreOrder(HashMap<String, String> hmSheet) throws Exception;

	public int updatePaOrderm20Approval(ParamMap param) throws Exception;

	public HashMap<String, Object> newSaveCancel(CancelInputVO cancelInputVO) throws Exception;

	public String getConfig(String key) throws Exception;

	public OrderpromoVO selectPaOrderPromo(ParamMap promoParam) throws Exception;

	public OrderpromoVO selectPaOrderPromo(Map<String, Object> map) throws Exception;
	
	public int updateOrderCancelYn(HashMap<String, String> map) throws Exception;

	public int insertTpaSlipInfo(Map<String, Object> slipOutProc) throws Exception;

	public String selectOrderGoodsDtName(ParamMap goodsDtParam) throws Exception;
	
	public int selectOrderGoodsDtDupleCheck(ParamMap goodsDtParam) throws Exception;

	public int updateRemark3N(HashMap<String, String> map) throws Exception;

	public List<Map<String, String>> selectPreoutOrderTargetList() throws Exception;

	public List<Map<String, Object>> selectPreoutOrderTargetDtList(ParamMap paramMap) throws Exception;

	public HashMap<String, Object>[] savePreoutOrder(PreoutOrderInputVO[] preoutOrderInputVO) throws Exception;

	public int updatePreoutPaOrderm(ParamMap paramMap) throws Exception;
    
}