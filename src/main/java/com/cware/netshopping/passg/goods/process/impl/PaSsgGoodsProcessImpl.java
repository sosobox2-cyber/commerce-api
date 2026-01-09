package com.cware.netshopping.passg.goods.process.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.PaSsgGoodsVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaSsgDisplayMapping;
import com.cware.netshopping.domain.model.PaSsgGoodsdtMapping;
import com.cware.netshopping.passg.goods.process.PaSsgGoodsProcess;
import com.cware.netshopping.passg.goods.repository.PaSsgGoodsDAO;

@Service("passg.goods.paSsgGoodsProcess")
public class PaSsgGoodsProcessImpl extends AbstractService implements PaSsgGoodsProcess{
	
	@Resource(name = "passg.goods.paSsgGoodsDAO")
	private PaSsgGoodsDAO paSsgGoodsDAO;

	@Override
	public List<PaEntpSlip> selectPaSsgEntpSlip(ParamMap paramMap) throws Exception {
		return paSsgGoodsDAO.selectPaSsgEntpSlip(paramMap);
	}
	
	@Override
	public PaSsgGoodsVO selectPaSsgGoodsInfo(ParamMap paramMap) throws Exception {
		return paSsgGoodsDAO.selectPaSsgGoodsInfo(paramMap);
	}

	@Override
	public String savePaSsgGoods(PaSsgGoodsVO paSsgGoods, List<PaSsgGoodsdtMapping> goodsdtMapping) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;

		try {
			executedRtn = paSsgGoodsDAO.updatepaSsgGoods(paSsgGoods);
			if(executedRtn < 0) {
				log.info("TPASSGGOODS update fail");
				throw processException("msg.cannot_save", new String[] { "TPASSGGOODS UPDATE" });
			}
			
			if(goodsdtMapping != null && goodsdtMapping.size() > 0) {
				for(int i = 0; i < goodsdtMapping.size(); i++) {
					goodsdtMapping.get(i).setModifyId(paSsgGoods.getModifyId());
					goodsdtMapping.get(i).setModifyDate(paSsgGoods.getModifyDate());
					
					executedRtn = paSsgGoodsDAO.updatepaSsgGoodsdt(goodsdtMapping.get(i));
					if(executedRtn < 0) {
						log.info("TPASSGGOODSDTMAPPING update fail");
						throw processException("msg.cannot_save", new String[] { "TPASSGGOODSDTMAPPING UPDATE" });
					}
				}
			}
			
			executedRtn = paSsgGoodsDAO.updatepaSsgGoodsImage(paSsgGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSIMAGE update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSIMAGE UPDATE" });
			}
			
			executedRtn = paSsgGoodsDAO.updatepaSsgGoodsPrice(paSsgGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSPRICE update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICE UPDATE" });
			}
			
			executedRtn = paSsgGoodsDAO.updatepaGoodsTarget(paSsgGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSTARGET update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSTARGET UPDATE" });
			}
			
			executedRtn = paSsgGoodsDAO.updatepaSsgGoodsOffer(paSsgGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSOFFER update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSOFFER UPDATE" });
			}
			
			executedRtn = paSsgGoodsDAO.updatepaSsgPromoGoodsPrice(paSsgGoods);
			if(executedRtn < 0) {
				log.info("TPAPROMOGOODSPRICE update fail");
				throw processException("msg.cannot_save", new String[] { "TPAPROMOGOODSPRICE UPDATE" });
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		return rtnMsg;
		
	}

	@Override
	public List<PaSsgGoodsVO> selectPaSsgGoodsInfoList(ParamMap paramMap) throws Exception {
		return paSsgGoodsDAO.selectPaSsgGoodsInfoList(paramMap);
	}

	@Override
	public String savePaSsgGoodsError(PaSsgGoodsVO paSsgGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		int executedRtn = paSsgGoodsDAO.updatePaSsgGoodsError(paSsgGoods);
		
		if (executedRtn < 0) {
			rtnMsg = Constants.SAVE_FAIL;
			throw processException("msg.cannot_save", new String[] { "TPASSGGOODS UPDATE" });
		}
	
		return rtnMsg;
	}
	
	@Override
	public List<PaSsgGoodsVO> selectPaSsgGoodsApprovalList(ParamMap paramMap) throws Exception {
		return paSsgGoodsDAO.selectPaSsgGoodsApprovalList(paramMap);
	}
	
	@Override
	public String updatePaSsgGoodsApproval(PaSsgGoodsVO paSsgGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;

		executedRtn = paSsgGoodsDAO.updatePaSsgGoodsApproval(paSsgGoods);
		if(executedRtn < 0) {
			log.info("TPASSGGOODS update fail");
			rtnMsg = Constants.SAVE_FAIL;
			throw processException("msg.cannot_save", new String[] { "TPASSGGOODS UPDATE" });
		}
		return rtnMsg;
	}
	
	@Override
	public List<PaSsgGoodsVO> selectPaGoodsdtStockList(ParamMap paramMap) throws Exception {
		return paSsgGoodsDAO.selectPaGoodsdtStockList(paramMap);
	}
	
	@Override
	public List<PaSsgGoodsdtMapping> selectPaGoodsdtStockMappingList(PaSsgGoodsVO paSsgGoods) throws Exception {
		return paSsgGoodsDAO.selectPaGoodsdtStockMappingList(paSsgGoods);
	}
	
	@Override
	public String updatePaGoodsdtMappingQty(List<PaSsgGoodsdtMapping> paGoodsdtList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		for(PaSsgGoodsdtMapping paGoodsdtMappingVO : paGoodsdtList) {
			int executedRtn = paSsgGoodsDAO.updatepaSsgGoodsdt(paGoodsdtMappingVO);
			
			if (executedRtn < 0) {
				rtnMsg = Constants.SAVE_FAIL;
				throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
			}			
		}
		return rtnMsg;
	}
	
	@Override
	public List<PaSsgGoodsVO> selectPaSellStateList(ParamMap paramMap) throws Exception {
		return paSsgGoodsDAO.selectPaSellStateList(paramMap);
	}
	
	@Override
	public String updatePaGoodsStatus(PaSsgGoodsVO sellStateTarget) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		executedRtn = paSsgGoodsDAO.updatePaGoodsStatus(sellStateTarget);
		if (executedRtn < 0) {
			rtnMsg = Constants.SAVE_FAIL;
			throw processException("msg.cannot_save", new String[] { "TPASSGGOODS UPDATE" });
		}
		
		return rtnMsg;
	}
	
	@Override
	public List<PaSsgDisplayMapping> selectPaSsgDisplayList(ParamMap paramMap) throws Exception {
		return paSsgGoodsDAO.selectPaSsgDisplayList(paramMap);
	}

	@Override
	public List<PaSsgGoodsdtMapping> selectPaSsgGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paSsgGoodsDAO.selectPaSsgGoodsdtInfoList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectPaNoticeData(ParamMap paramMap) throws Exception {
		return paSsgGoodsDAO.selectPaNoticeData(paramMap);
	}
	
	@Override
	public HashMap<String, Object> selectSsgFoodInfo(ParamMap paramMap) throws Exception {
		return paSsgGoodsDAO.selectSsgFoodInfo(paramMap);
	}
}
