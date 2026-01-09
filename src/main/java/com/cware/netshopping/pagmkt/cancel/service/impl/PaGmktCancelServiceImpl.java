package com.cware.netshopping.pagmkt.cancel.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaGmkCancel;
import com.cware.netshopping.pagmkt.cancel.process.PaGmktCancelProcess;
import com.cware.netshopping.pagmkt.cancel.service.PaGmktCancelService;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;


@Service("pagmkt.cancel.PaGmktCancelService")
public class PaGmktCancelServiceImpl  extends AbstractService implements PaGmktCancelService {
	

	@Resource(name = "pagmkt.cancel.PaGmktCancelProcess")
    private PaGmktCancelProcess PaGmktCancelProcess;

	@Override
	public int saveCancelReqListTx(PaGmkCancel paGmktCancel) throws Exception {
		return PaGmktCancelProcess.saveCancelReqList(paGmktCancel);	
	}

	@Override
	public List<Object> selectPaGmktOrdCancelList(ParamMap paramMap) throws Exception {
	return PaGmktCancelProcess.selectPaGmktOrdCancelList(paramMap);
	}
	
	@Override
	public List<Object> selectPaGmktOrdCancelListForBo() throws Exception {
		return PaGmktCancelProcess.selectPaGmktOrdCancelListForBo();
	}

	@Override
	public String saveCancelConfirmProcTx(HashMap<String, Object> cancelMap, PaGmktAbstractRest rest, ParamMap param) throws Exception {
		return PaGmktCancelProcess.saveCancelConfirmProc(cancelMap, rest, param);
	}
	
	public HashMap<String, Object> selectPaGmktOrdCancel(HashMap<String,Object> paramMap) throws Exception{
		return PaGmktCancelProcess.selectPaGmktOrdCancel(paramMap);
	}

	@Override
	public int updatePaGmktOrderListProcFlag(HashMap<String, Object> cancelMap) throws Exception {
		return PaGmktCancelProcess.updatePaGmktOrderListProcFlag(cancelMap);
	}

	@Override
	public int saveCancelConfirmTx(PaGmktAbstractRest rest , HashMap<String, Object> cancelMap, ParamMap param) throws Exception {
		return PaGmktCancelProcess.saveCancelConfirm(rest, cancelMap, param);
	}

	@Override
	public int saveCancelWithdrawList(PaGmkCancel paGmktCancel) throws Exception {
		return PaGmktCancelProcess.saveCancelWithdrawList(paGmktCancel);
	}

	@Override
	public int saveCancelCompleteListTx(PaGmkCancel paGmktCancel) throws Exception {
		return PaGmktCancelProcess.saveCancelCompleteList(paGmktCancel);
	}

	@Override
	public PaGmkCancel setPagmktCancelVo(HashMap<String, Object> cancelMap, ParamMap paramMap) throws Exception {
		return PaGmktCancelProcess.setPagmktCancelVo(cancelMap, paramMap);
	}
	
	@Override
	public PaGmkCancel setPagmktCancelVo2(HashMap<String, Object> cancelMap, ParamMap paramMap) throws Exception {
		return PaGmktCancelProcess.setPagmktCancelVo2(cancelMap, paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectUnpaidPreOrderList(ParamMap paramMap) throws Exception {
		return PaGmktCancelProcess.selectUnpaidPreOrderList(paramMap);
	}

	@Override
	public List<Object> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception {
		return PaGmktCancelProcess.selectPaMobileOrderAutoCancelList(paramMap);

	}
	

}