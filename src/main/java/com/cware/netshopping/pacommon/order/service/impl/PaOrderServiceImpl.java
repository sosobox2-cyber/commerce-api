package com.cware.netshopping.pacommon.order.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.CancelInputVO;
import com.cware.netshopping.domain.OrderInputVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PreoutOrderInputVO;
import com.cware.netshopping.domain.model.PaMonitering;
import com.cware.netshopping.pacommon.order.process.PaOrderProcess;
import com.cware.netshopping.pacommon.order.service.PaOrderService;


@Service("pacommon.order.paorderService")

public class PaOrderServiceImpl  extends AbstractService implements PaOrderService {
	
	@Resource(name = "pacommon.order.paorderProcess")
    private PaOrderProcess orderProcess;
	
	@Override
	public HashMap<String, Object>[] saveOrderTx(OrderInputVO[] orderInputVO) throws Exception {
		return orderProcess.saveOrder(orderInputVO);
	}
	
	@Override
	public HashMap<String, Object>[] newSaveOrderTx(OrderInputVO[] orderInputVO) throws Exception {
		return orderProcess.newSaveOrder(orderInputVO);
	}
	
	@Override
	public HashMap<String, Object> saveCancelTx(CancelInputVO cancelInputVO) throws Exception {
		return orderProcess.saveCancel(cancelInputVO);
	}
	
	@Override
	public int updatePaOrdermTx(ParamMap paramMap) throws Exception {
		return orderProcess.updatePaOrderm(paramMap);
	}

	@Override
	public int insertTpaMonitering(PaMonitering pamoniter) throws Exception {
		// TODO Auto-generated method stub
		return orderProcess.insertTpaMonitering(pamoniter);	
	}

	@Override
	public List<Object> selectOrderTagetDt(String paOrder) throws Exception {
		// TODO Auto-generated method stub
		
		return  orderProcess.selectOrderTagetDt(paOrder);
	}
	
	@Override
	public int getTpaMonitering(PaMonitering pamoniter) throws Exception {
		return orderProcess.getTpaMonitering(pamoniter);	
	}

	@Override
	public int updatePreCancelOrder(String mappingSeq) throws Exception {
		return orderProcess.updatePreCancelOrder(mappingSeq);	
	}

	@Override
	public void upDateOrderTx(ParamMap param) throws Exception {
		orderProcess.upDateOrder(param);	
	}

	@Override
	public int updatePaOrderMFailConfrimPreOrder(HashMap<String, String> hmSheet, String message) throws Exception {
		ParamMap param = new ParamMap();
		param.put("apiResultCode"		, "999999");
		param.put("apiResultMessage"	, ComUtil.subStringBytes(message, 300));
		param.put("paOrderNo"			, hmSheet.get("PA_ORDER_NO").toString());
		param.put("orderNo"				, hmSheet.get("ORDER_NO").toString());

		return orderProcess.updatePaOrderm20Approval(param);
	}

	@Override
	public HashMap<String, Object> newSaveCancelTx(CancelInputVO cancelInputVO) throws Exception {
		return orderProcess.newSaveCancel(cancelInputVO);		
	}

	@Override
	public String getConfig(String key) throws Exception {
		return orderProcess.getConfig(key);		
	}

	@Override
	public OrderpromoVO selectPaOrderPromo(ParamMap promoParam) throws Exception {
		return orderProcess.selectPaOrderPromo(promoParam);
	}

	@Override
	public OrderpromoVO selectPaOrderPromo(Map<String, Object> map) throws Exception {
		return orderProcess.selectPaOrderPromo(map);
	}

	@Override
	public int updateOrderCancelYnTx(HashMap<String, String> map) throws Exception {
		return orderProcess.updateOrderCancelYn(map);	
	}

	@Override
	public int insertTpaSlipInfoTx(Map<String, Object> oc) throws Exception {
		return orderProcess.insertTpaSlipInfo(oc);
	}

	@Override
	public String selectOrderGoodsDtName(ParamMap goodsDtParam) throws Exception {
		return orderProcess.selectOrderGoodsDtName(goodsDtParam);
	}

	@Override
	public int selectOrderGoodsDtDupleCheck(ParamMap goodsDtParam) throws Exception {
		return orderProcess.selectOrderGoodsDtDupleCheck(goodsDtParam);
	}

	@Override
	public int updateRemark3NTx(HashMap<String, String> map) throws Exception {
		return orderProcess.updateRemark3N(map);	

	}

	@Override
	public List<Map<String, String>> selectPreoutOrderTargetList() throws Exception {
		return orderProcess.selectPreoutOrderTargetList();	
	}
	
	@Override
	public List<Map<String, Object>> selectPreoutOrderTargetDtList(ParamMap paramMap) throws Exception {
		return orderProcess.selectPreoutOrderTargetDtList(paramMap);
	}

	@Override
	public HashMap<String, Object>[] savePreoutOrderTx(PreoutOrderInputVO[] preoutOrderInputVO) throws Exception {
		return orderProcess.savePreoutOrder(preoutOrderInputVO);
	}

	@Override
	public int updatePreoutPaOrdermTx(ParamMap paramMap) throws Exception {
		return orderProcess.updatePreoutPaOrderm(paramMap);
	}
}