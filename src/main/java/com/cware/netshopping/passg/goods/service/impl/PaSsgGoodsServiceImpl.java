package com.cware.netshopping.passg.goods.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaSsgGoodsVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaSsgDisplayMapping;
import com.cware.netshopping.domain.model.PaSsgGoodsdtMapping;
import com.cware.netshopping.passg.goods.process.PaSsgGoodsProcess;
import com.cware.netshopping.passg.goods.service.PaSsgGoodsService;

@Service("passg.goods.paSsgGoodsService")
public class PaSsgGoodsServiceImpl extends AbstractService implements PaSsgGoodsService{

	@Autowired 
	PaSsgGoodsProcess paSsgGoodsProcess;

	@Override
	public List<PaEntpSlip> selectPaSsgEntpSlip(ParamMap paramMap) throws Exception {
		return paSsgGoodsProcess.selectPaSsgEntpSlip(paramMap);
	}
	
	@Override
	public PaSsgGoodsVO selectPaSsgGoodsInfo(ParamMap paramMap) throws Exception {
		return paSsgGoodsProcess.selectPaSsgGoodsInfo(paramMap);
	}

	@Override
	public String savePaSsgGoodsTx(PaSsgGoodsVO paSsgGoods, List<PaSsgGoodsdtMapping> goodsdtMapping) throws Exception {
		return paSsgGoodsProcess.savePaSsgGoods(paSsgGoods, goodsdtMapping);
	}

	@Override
	public List<PaSsgGoodsVO> selectPaSsgGoodsInfoList(ParamMap paramMap) throws Exception {
		return paSsgGoodsProcess.selectPaSsgGoodsInfoList(paramMap);
	}

	@Override
	public String savePaSsgGoodsErrorTx(PaSsgGoodsVO paSsgGoods) throws Exception {
		return paSsgGoodsProcess.savePaSsgGoodsError(paSsgGoods);
	}
	
	@Override
	public List<PaSsgGoodsVO> selectPaSsgGoodsApprovalList(ParamMap paramMap) throws Exception {
		return paSsgGoodsProcess.selectPaSsgGoodsApprovalList(paramMap);
	}
	@Override
	public String updatePaSsgGoodsApproval(PaSsgGoodsVO paSsgGoods) throws Exception {
		return paSsgGoodsProcess.updatePaSsgGoodsApproval(paSsgGoods);
	}
	@Override
	public List<PaSsgGoodsVO> selectPaGoodsdtStockList(ParamMap paramMap) throws Exception {
		return paSsgGoodsProcess.selectPaGoodsdtStockList(paramMap);
	}
	@Override
	public List<PaSsgGoodsdtMapping> selectPaGoodsdtStockMappingList(PaSsgGoodsVO paSsgGoods) throws Exception {
		return paSsgGoodsProcess.selectPaGoodsdtStockMappingList(paSsgGoods);
	}
	@Override
	public String updatePaGoodsdtMappingQtyTx(List<PaSsgGoodsdtMapping> paGoodsdtList) throws Exception {
		return paSsgGoodsProcess.updatePaGoodsdtMappingQty(paGoodsdtList);
	}
	@Override
	public List<PaSsgGoodsVO> selectPaSellStateList(ParamMap paramMap) throws Exception {
		return paSsgGoodsProcess.selectPaSellStateList(paramMap);
	}
	@Override
	public String updatePaGoodsStatus(PaSsgGoodsVO sellStateTarget) throws Exception {
		return paSsgGoodsProcess.updatePaGoodsStatus(sellStateTarget);
	}
	@Override
	public List<PaSsgDisplayMapping> selectPaSsgDisplayList(ParamMap paramMap) throws Exception {
		return paSsgGoodsProcess.selectPaSsgDisplayList(paramMap);
	}

	@Override
	public List<PaSsgGoodsdtMapping> selectPaSsgGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paSsgGoodsProcess.selectPaSsgGoodsdtInfoList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectPaNoticeData(ParamMap paramMap) throws Exception {
		return paSsgGoodsProcess.selectPaNoticeData(paramMap);
	}
	
	@Override
	public HashMap<String, Object> selectSsgFoodInfo(ParamMap paramMap) throws Exception {
		return paSsgGoodsProcess.selectSsgFoodInfo(paramMap);
	}
}
