package com.cware.netshopping.patmon.goods.process.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaTmonGoodsVO;
import com.cware.netshopping.domain.PaTmonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.common.process.PaCommonProcess;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.patmon.goods.process.PaTmonGoodsProcess;
import com.cware.netshopping.patmon.goods.repository.PaTmonGoodsDAO;

@Service("patmon.goods.paTmonGoodsProcess")
public class PaTmonGoodsProcessImpl extends AbstractService implements PaTmonGoodsProcess{

	@Resource(name = "pacommon.common.pacommonProcess")
    private PaCommonProcess pacommonProcess;
	
	@Resource(name = "patmon.goods.paTmonGoodsDAO")
	private PaTmonGoodsDAO paTmonGoodsDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;

	@Override
	public List<PaEntpSlip> selectPaTmonEntpSlip(ParamMap paramMap) throws Exception {
		return paTmonGoodsDAO.selectPaTmonEntpSlip(paramMap);
	}

	@Override
	public PaTmonGoodsVO selectPaTmonGoodsInfo(ParamMap paramMap) throws Exception {
		return paTmonGoodsDAO.selectPaTmonGoodsInfo(paramMap);
	}

	@Override
	public List<PaGoodsOfferVO> selectPaTmonGoodsOfferList(ParamMap paramMap) throws Exception {
		return paTmonGoodsDAO.selectPaTmonGoodsOfferList(paramMap);
	}
	
	@Override
	public List<PaGoodsdtMapping> selectPaTmonGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paTmonGoodsDAO.selectPaTmonGoodsdtInfoList(paramMap);
	}
	
	@Override
	public String savePaTmonGoods(PaTmonGoodsVO paTmonGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception{
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			executedRtn = paTmonGoodsDAO.updatepaTmonGoods(paTmonGoods);
			if(executedRtn < 0) {
				log.info("TpaTmonGoods update fail");
				throw processException("msg.cannot_save", new String[] { "TpaTmonGoods UPDATE" });
			}
			
			if(goodsdtMapping != null && goodsdtMapping.size() > 0) {
				for(int i = 0; i < goodsdtMapping.size(); i++) {
					goodsdtMapping.get(i).setModifyId(paTmonGoods.getModifyId());
					goodsdtMapping.get(i).setModifyDate(paTmonGoods.getModifyDate());
					
					executedRtn = paTmonGoodsDAO.updatepaTmonGoodsdt(goodsdtMapping.get(i));
					if(executedRtn < 0) {
						log.info("TpaTmonGoodsDTMAPPING update fail");
						throw processException("msg.cannot_save", new String[] { "TpaTmonGoodsDTMAPPING UPDATE" });
					}
				}
			}
			
			if(paPromoTargetList != null && paPromoTargetList.size() > 0) { 
				for(PaPromoTarget paPromoTarget : paPromoTargetList) {
					if(paPromoTarget != null && paPromoTarget.getTransDate() == null) {
						paPromoTarget.setModifyId(paTmonGoods.getModifyId());
						paPromoTarget.setModifyDate(paTmonGoods.getModifyDate());
						
						executedRtn = paTmonGoodsDAO.updatePaPromoTarget(paPromoTarget);
						if(executedRtn < 0) {
							log.info("TPAPROMOTARGET update fail");
							throw processException("msg.cannot_save", new String[] { "TPAPROMOTARGET UPDATE" });
						}
					
					}else if(paPromoTarget.getTransDate() != null) { //기존 프로모션 재연동 
						executedRtn = paTmonGoodsDAO.insertNewPaPromoTarget(paPromoTarget);
						if(executedRtn < 0) {
							log.info("TPAPROMOTARGET insert fail");
							throw processException("msg.cannot_save", new String[] { "TPAPROMOTARGET INSERT" });
						}
					}
				}
			}
			
			executedRtn = paTmonGoodsDAO.updatepaTmonGoodsImage(paTmonGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSIMAGE update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSIMAGE UPDATE" });
			}
			
			executedRtn = paTmonGoodsDAO.updatepaTmonGoodsPrice(paTmonGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSPRICE update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICE UPDATE" });
			}
			
			executedRtn = paTmonGoodsDAO.updatepaGoodsTarget(paTmonGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSTARGET update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSTARGET UPDATE" });
			}
			
			executedRtn = paTmonGoodsDAO.updatepaTmonGoodsOffer(paTmonGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSOFFER update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSOFFER UPDATE" });
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		return rtnMsg;
	}
	
	@Override
	public int insertPaTmonGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception {
		return paCommonDAO.insertPaGoodsTransLog(paGoodsTransLog);
	}
	

	@Override
	public List<PaTmonGoodsVO> selectPaTmonSellStateList(ParamMap paramMap) throws Exception {
		return paTmonGoodsDAO.selectPaTmonSellStateList(paramMap);
	}

	@Override
	public String updateTmonGoodsStatus(PaTmonGoodsVO sellStateTarget) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		executedRtn = paTmonGoodsDAO.updateTmonGoodsStatus(sellStateTarget);
		if (executedRtn < 0) {
			rtnMsg = Constants.SAVE_FAIL;
			throw processException("msg.cannot_save", new String[] { "TPATMONGOODS UPDATE" });
		}
		
		return rtnMsg;
	}

	@Override
	public List<PaTmonGoodsdtMappingVO> selectPaTmonGoodsdtStockMappingList(ParamMap paramMap) throws Exception {
		return paTmonGoodsDAO.selectPaTmonGoodsdtStockMappingList(paramMap);
	}
	
	@Override
	public List<PaEntpSlip> selectPaTmonEntpSlipList(ParamMap paramMap) throws Exception {
		return paTmonGoodsDAO.selectPaTmonEntpSlipList(paramMap);
	}

	@Override
	public List<PaTmonGoodsVO> selectPaTmonGoodsInfoList(ParamMap paramMap) throws Exception {
		return paTmonGoodsDAO.selectPaTmonGoodsInfoList(paramMap);
	}

	@Override
	public List<PaTmonGoodsdtMappingVO> selectPaTmonGoodsdtStockList(ParamMap paramMap) throws Exception {
		return paTmonGoodsDAO.selectPaTmonGoodsdtStockList(paramMap);
	}

	@Override
	public String updatePaTmonGoodsdtMappingQty(List<PaTmonGoodsdtMappingVO> paGoodsdtList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		for(PaTmonGoodsdtMappingVO paTmonGoodsdtMappingVO : paGoodsdtList) {
			int executedRtn = paTmonGoodsDAO.updatePaTmonGoodsdtMappingQty(paTmonGoodsdtMappingVO);
			
			if (executedRtn < 0) {
				rtnMsg = Constants.SAVE_FAIL;
				throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
			}			
		}
		return rtnMsg;
	}

	@Override
	public List<PaTmonGoodsdtMappingVO> selectPaTmonGoodsdtAddedList(ParamMap paramMap) throws Exception {
		return paTmonGoodsDAO.selectPaTmonGoodsdtAddedList(paramMap);
	}

	@Override
	public String updatePaTmonGoodsdtMappingAdded(List<PaTmonGoodsdtMappingVO> goodsAddedMapping) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		for(int i=0; i < goodsAddedMapping.size(); i++) {
			
			int executedRtn = paTmonGoodsDAO.updatePaTmonGoodsdtMappingQty(goodsAddedMapping.get(i));
			
			if (executedRtn < 0) {
				rtnMsg = Constants.SAVE_FAIL;
				throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
			}
			
		}
		return rtnMsg;
	}
	
	@Override
	public List<PaTmonGoodsVO> selectPaTmonModifyOptionList(ParamMap paramMap) throws Exception {
		return paTmonGoodsDAO.selectPaTmonModifyOptionList(paramMap);
	}

	@Override
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception {
		
		List<PaPromoTarget> promotionList = new ArrayList<PaPromoTarget>();
		//1) 높은 가격 기준으로 프로모션을 조회한다
		paramMap.put("alcoutPromoYn", "1");
		List<PaPromoTarget> promotionList1 = paTmonGoodsDAO.selectPaPromoTarget(paramMap);
		
		//2) 가져온 데이터에 대한 마진률 계산
		checkPromoMargin(promotionList1);
		
		//3) 프로모션 결과가 없으면 가장 최신 만료된(D) 데이터를 찾아서 반납한다 -> why? 프로모션 연동 여부를 기록하기 위해 
		if(promotionList1.size() < 1) {
			promotionList1	= paTmonGoodsDAO.selectPaPromoDeleteTarget(paramMap);	
		}
		
		promotionList.addAll(promotionList1);
		
		return promotionList;
	}
	
	private void checkPromoMargin( List<PaPromoTarget> promotionList1 ) throws Exception {
		if(promotionList1 == null ) return;
		if(promotionList1.size() < 1) return;
		
		int marginCheckExceptYn     = 0;
		double margin 	  	   	    = 0;
		double limitMargin	   	    = 0;
		String sMode	   	   	    = "1";
		double outOwnCost 		 	= 0;
		PaPromoTarget promoTarget1 	= null;
		ParamMap paramMap	   	    = new ParamMap();
		ParamMap minMarginMap		= new ParamMap();
		
		minMarginMap.put("paGroupCode", promotionList1.get(0).getPaGroupCode());
		minMarginMap.put("paCode", promotionList1.get(0).getPaCode());

//		limitMargin = Double.parseDouble( ComUtil.NVL(systemDAO.getVal("PA_LIMIT_MARGIN") , "-99"));
		limitMargin  = Double.parseDouble(ComUtil.NVL(paCommonDAO.selectPaMinMarginRate(minMarginMap), "-99")); // 2022-09-20 TCONFIG -> TPAPROMOMINMARGINRATE 로 관리
		if(limitMargin == -99) return;

		outOwnCost 	 = promotionList1.get(0).getOwnCost();
		promoTarget1 = promotionList1.get(0);

		
		//1) 마진률 CHECK
		paramMap.put("goodsCode"	, promoTarget1.getGoodsCode());
		paramMap.put("outOwnPrice"	, outOwnCost);
		paramMap.put("mode"			, sMode);
		paramMap.put("paCode"	  	, promoTarget1.getPaCode());
		paramMap.put("paGroupCode"	, Constants.PA_TMON_GROUP_CODE);
		
		// 마진율 체크 제외 상품 확인
		marginCheckExceptYn = paCommonDAO.selectPromoMarginExceptYn(paramMap);
		if(marginCheckExceptYn > 0) return;
				
		margin 	 = paCommonDAO.selectGetPromoMargin(paramMap); 
		if(margin >= limitMargin) return;
		
		//2) 허용된 범위 안에서 최대 마진률 promotionList 세팅
		getMaxMarginPromo   ( promotionList1,  paramMap);
		//3) Except_REASON Insert
		setExceptReason		( promotionList1 );
	}

	private void getMaxMarginPromo( List<PaPromoTarget> promotionList1, ParamMap paramMap) throws Exception{
		if(promotionList1.size() < 1)	return;
		
		String promoNo = paCommonDAO.selectGetMaxPromoTarget(paramMap);  //FUN_GET_MAX_PAPROMOTARGET
		if(promoNo == null || promoNo.equals("")) {
			promotionList1.get(0).setDoCost(0);
			return;
		}
		paramMap.put("alcoutPromoYn" , "1");
		paramMap.put("promoNo", promoNo);
		
		List<PaPromoTarget> promotionList = paTmonGoodsDAO.selectPaPromoTarget(paramMap);

		if(promotionList1.size() > 0)	promotionList1.remove(0);
		promotionList1.add(promotionList.get(0));	
	}
	
	private void setExceptReason(List<PaPromoTarget> promotionList1 ) {
		if( promotionList1 == null ) return; 
		if( promotionList1.size() < 1 ) return; 
		if( promotionList1.get(0).getDoCost() < 1 ) return;
		ParamMap paramMap 	= new ParamMap();	
		
		paramMap.put("goodsCode"		, promotionList1.get(0).getGoodsCode());
		paramMap.put("paCode"			, promotionList1.get(0).getPaCode());
		paramMap.put("limitMarginAmt"	, promotionList1.get(0).getOwnCost());
		paramMap.put("paGroupCode"		,"");
		pacommonProcess.setExceptReason4OnePromotion(paramMap);
		return;
	}

	@Override
	public String savePaTmonGoodsError(PaTmonGoodsVO paTmonGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
	
		int executedRtn = paTmonGoodsDAO.updatePaTmonGoodsError(paTmonGoods);
		
		if (executedRtn < 0) {
			rtnMsg = Constants.SAVE_FAIL;
			throw processException("msg.cannot_save", new String[] { "TPATMONGOODS UPDATE" });
		}
	
		return rtnMsg;
	}

	@Override
	public int selectPaTmonExceptShipPolicy(String goodsCode) throws Exception {
		return paTmonGoodsDAO.selectPaTmonExceptShipPolicy(goodsCode);
	}
	
}
