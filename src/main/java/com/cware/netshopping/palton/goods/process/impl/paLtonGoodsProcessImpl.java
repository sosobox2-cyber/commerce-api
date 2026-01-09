package com.cware.netshopping.palton.goods.process.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.PaLtonGoodsVO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.common.process.PaCommonProcess;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.palton.goods.process.PaLtonGoodsProcess;
import com.cware.netshopping.palton.goods.repository.paLtonGoodsDAO;

@Service("palton.goods.paLtonGoodsProcess")
public class paLtonGoodsProcessImpl extends AbstractService implements PaLtonGoodsProcess {
	@Resource(name = "pacommon.common.pacommonProcess")
    private PaCommonProcess pacommonProcess;
	
	@Resource(name = "palton.goods.paLtonGoodsDAO")
	private paLtonGoodsDAO paLtonGoodsDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Override
	public PaEntpSlip selectPaLtonEntpSlip(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectPaLtonEntpSlip(paramMap);
	}
	
	@Override
	public List<PaEntpSlip> selectPaLtonEntpSlipList(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectPaLtonEntpSlipList(paramMap);
	}

	@Override
	public PaLtonGoodsVO selectPaLtonGoodsInfo(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectPaLtonGoodsInfo(paramMap);
	}

	@Override
	public List<PaGoodsOffer> selectPaLtonGoodsOfferList(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectPaLtonGoodsOfferList(paramMap);
	}

	@Override
	public List<PaLtonGoodsdtMappingVO> selectPaLtonGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectPaLtonGoodsdtInfoList(paramMap);
	}

	@Override
	public int insertPaLtonGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception {
		return paCommonDAO.insertPaGoodsTransLog(paGoodsTransLog);
	}

	@Override
	public String savePaLtonGoods(PaLtonGoodsVO paLtonGoods, List<PaLtonGoodsdtMappingVO> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try{
			executedRtn = paLtonGoodsDAO.selectPaLtonGoodsModifyCheck(paLtonGoods);
			if( executedRtn == 0) {
				executedRtn = paLtonGoodsDAO.updatePaLtonGoods(paLtonGoods);
				if(executedRtn < 0) {
					log.info("TPALTONGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPALTONGOODS UPDATE" });
				}
				
				if(goodsdtMapping != null && goodsdtMapping.size() > 0) {
					for(int i = 0; i < goodsdtMapping.size(); i++) {
						goodsdtMapping.get(i).setModifyId(paLtonGoods.getModifyId());
						goodsdtMapping.get(i).setModifyDate(paLtonGoods.getModifyDate());
						
						executedRtn = paLtonGoodsDAO.updatePaLtonGoodsdt(goodsdtMapping.get(i));
						if(executedRtn < 0) {
							log.info("TPALTONGOODSDTMAPPING update fail");
							throw processException("msg.cannot_save", new String[] { "TPALTONGOODSDTMAPPING UPDATE" });
						}
					}
				}
				
				executedRtn = paLtonGoodsDAO.updatePaLtonGoodsImage(paLtonGoods);
				if(executedRtn < 0) {
					log.info("TPAGOODSIMAGE update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSIMAGE UPDATE" });
				}
				
				executedRtn = paLtonGoodsDAO.updatePaLtonCustShipCost(paLtonGoods);
				if(executedRtn < 0) {
					log.info("TPACUSTSHIPCOST update fail");
					throw processException("msg.cannot_save", new String[] { "TPACUSTSHIPCOST UPDATE" });
				}
				
				executedRtn = paLtonGoodsDAO.updatePaLtonGoodsOffer(paLtonGoods);
				if(executedRtn < 0) {
					log.info("TPAGOODSOFFER update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSOFFER UPDATE" });
				}
			
			}
			
			if(paPromoTargetList != null && paPromoTargetList.size() > 0) { // paPromoTargetLsit 정보는 추후 controller 부분에서 개발 필요
				for(PaPromoTarget paPromoTarget : paPromoTargetList) {
					if(paPromoTarget != null && paPromoTarget.getTransDate() == null) {
						paPromoTarget.setModifyId(paLtonGoods.getModifyId());
						paPromoTarget.setModifyDate(paLtonGoods.getModifyDate());
						
						executedRtn = paLtonGoodsDAO.updatePaPromoTarget(paPromoTarget);
						if(executedRtn < 0) {
							log.info("TPAPROMOTARGET update fail");
							throw processException("msg.cannot_save", new String[] { "TPAPROMOTARGET UPDATE" });
						}
					
					}else if(paPromoTarget.getTransDate() != null) { //기존 프로모션 재연동 
						executedRtn = paLtonGoodsDAO.insertNewPaPromoTarget(paPromoTarget);
						if(executedRtn < 0) {
							log.info("TPAPROMOTARGET insert fail");
							throw processException("msg.cannot_save", new String[] { "TPAPROMOTARGET INSERT" });
						}
					}
				}
			}
			
			executedRtn = paLtonGoodsDAO.updatePaLtonGoodsPrice(paLtonGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSPRICE update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICE UPDATE" });
			}
			
			executedRtn = paLtonGoodsDAO.updatePaLtonGoodsTarget(paLtonGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSTARGET update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSTARGET UPDATE" });
			}

		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		return rtnMsg;
	}

	@Override
	public List<HashMap<String, String>> selectPaApprovalStatusList(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectPaApprovalStatusList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectEmptyPaOptionCodeList(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectEmptyPaOptionCodeList(paramMap);
	}

	@Override
	public String updateGoodsdtPaOptionCode(PaLtonGoodsdtMappingVO ltonGoodsDtMapping) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			executedRtn = paLtonGoodsDAO.updatePaLtonGoodsdt(ltonGoodsDtMapping);
		
			if(executedRtn < 0) {
				log.info("TPALTONGOODSDTMAPPING update fail");
				throw processException("msg.cannot_save", new String[] { "TPALTONGOODSDTMAPPING UPDATE" });
			}
		}catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		return rtnMsg;
	}

	@Override
	public List<PaLtonGoodsVO> selectPaLtonSellStateList(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectPaLtonSellStateList(paramMap);
	}
	
	@Override
	public String savePaLtonGoodsSell(PaLtonGoodsVO paLtonGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			executedRtn = paLtonGoodsDAO.updatePaLtonGoodsSellState(paLtonGoods);
			if(executedRtn < 0) {
				log.info("TPALTONGOODS update fail");
				throw processException("msg.cannot_save", new String[] { "TPALTONGOODS UPDATE" });
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		return rtnMsg;
	}

	@Override
	public List<PaLtonGoodsdtMappingVO> selectPaLtonGoodsdtMappingStockList(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectPaLtonGoodsdtMappingStockList(paramMap);
	}

	@Override
	public String savePaLtonGoodsStock(List<PaLtonGoodsdtMappingVO> paGoodsdtList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		for(PaLtonGoodsdtMappingVO paLtonGoodsdtMappingVO : paGoodsdtList) {
			executedRtn = paLtonGoodsDAO.updatePaLtonGoodsdt(paLtonGoodsdtMappingVO);
			if (executedRtn < 0) {
				rtnMsg = Constants.SAVE_FAIL;
				throw processException("msg.cannot_save", new String[] { "TPALTONGOODSDTMAPPING UPDATE" });
			}
		}
		return rtnMsg;
	}

	@Override
	public List<PaLtonGoodsVO> selectPaLtonGoodsInfoList(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectPaLtonGoodsInfoList(paramMap);
	}
	
	@Override
	public List<PaLtonGoodsdtMappingVO> selectPaLtonGoodsdtMappingStock(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectPaLtonGoodsdtMappingStock(paramMap);
	}

	@Override
	public String updatePaLtonApprovalStatus(PaLtonGoodsVO paltonGoodsVo) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		executedRtn = paLtonGoodsDAO.updatePaLtonApprovalStatus(paltonGoodsVo);
		if (executedRtn < 0) {
			rtnMsg = Constants.SAVE_FAIL;
			throw processException("msg.cannot_save", new String[] { "TPALTONGOODS UPDATE" });
		}
		return rtnMsg;
	}
	
	@Override
	public List<PaLtonGoodsdtMappingVO> selectPaLtonDtSellState(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectPaLtonDtSellState(paramMap);
	}
	
	@Override
	public List<PaLtonGoodsdtMappingVO> selectPaLtonDtSellStateList(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectPaLtonDtSellStateList(paramMap);
	}

	@Override
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception {
		List<PaPromoTarget> promotionList = new ArrayList<PaPromoTarget>();
		//1) 높은 가격 기준으로 프로모션을 조회한다
		paramMap.put("alcoutPromoYn", "1");
		List<PaPromoTarget> promotionList1 = paLtonGoodsDAO.selectPaPromoTarget(paramMap);
		
		//2) 가져온 데이터에 대한 마진률 계산
		checkPromoMargin(promotionList1);
		
		//3) 프로모션 결과가 없으면 가장 최신 만료된(D) 데이터를 찾아서 반납한다 -> why? 프로모션 연동 여부를 기록하기 위해 
		if(promotionList1.size() < 1) {
			promotionList1	= paLtonGoodsDAO.selectPaPromoDeleteTarget(paramMap);	
		}
		
		promotionList.addAll(promotionList1);
		
		return promotionList;
	}
		
	private void checkPromoMargin(List<PaPromoTarget> promotionList1 ) throws Exception {
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
	
		
		//1) SD프로모션 , OUT 프로모션 동시에 새로 적용될때 마진률 계산 -> 속도때문에 사용
		paramMap.put("goodsCode"	, promoTarget1.getGoodsCode());
		paramMap.put("outOwnPrice"	, outOwnCost);
		paramMap.put("mode"			, sMode);
		paramMap.put("paCode"	  	, promoTarget1.getPaCode());
		paramMap.put("paGroupCode"	, Constants.PA_LTON_GROUP_CODE);
		
		// 마진율 체크 제외 상품 확인
		marginCheckExceptYn = paCommonDAO.selectPromoMarginExceptYn(paramMap);
		if(marginCheckExceptYn > 0) return;
				
		margin 	 = paCommonDAO.selectGetPromoMargin(paramMap); 
		if(margin >= limitMargin) return;
		
		//2) 허용된 범위 안에서 최대 마진률 promotionList 세팅
		getMaxMarginPromo   (promotionList1,  paramMap);
		//3) Except_REASON Insert
		setExceptReason		(promotionList1 );
	}

	private void getMaxMarginPromo(List<PaPromoTarget> promotionList1, ParamMap paramMap) throws Exception{
		if(promotionList1.size() < 1)	return;
		
		String promoNo = paCommonDAO.selectGetMaxPromoTarget(paramMap);  //FUN_GET_MAX_PAPROMOTARGET
		if(promoNo == null || promoNo.equals("")) {
			promotionList1.get(0).setDoCost	(0);
			return;
		}
		
		paramMap.put("promoNo"		 , promoNo);
		paramMap.put("alcoutPromoYn" , "1");
		List<PaPromoTarget> promotionList = paLtonGoodsDAO.selectPaPromoTarget(paramMap);
		
		if(promotionList1.size() > 0)	promotionList1.remove(0);
		promotionList1.add(promotionList.get(0));
		
	}
	
	private void setExceptReason(List<PaPromoTarget> promotionList1 ) {
		if( promotionList1 == null ) return; 
		if( promotionList1.size() < 1 ) return; 
		if( promotionList1.get(0).getDoAmt() == 0) return;
		ParamMap paramMap 	= new ParamMap();	
		
		paramMap.put("goodsCode"		, promotionList1.get(0).getGoodsCode());
		paramMap.put("paCode"			, promotionList1.get(0).getPaCode());
		paramMap.put("limitMarginAmt"	, promotionList1.get(0).getOwnCost());
		paramMap.put("paGroupCode"		,"");
		pacommonProcess.setExceptReason4OnePromotion(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaLtonGoodsTrans(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.selectPaLtonGoodsTrans(paramMap);
	}

	@Override
	public int updatePaLtonGoodsFail(HashMap<String, String> paGoodsTarget) throws Exception {
		return paLtonGoodsDAO.updatePaLtonGoodsFail(paGoodsTarget);
	}

	@Override
	public List<PaLtonGoodsVO> selectPaLtonGoodsInfoListMass(ParamMap paramMap) throws Exception {
		paramMap.put("modCase", "MODIFY");
		paCommonDAO.execSP_PA_MASS_GOODS_UPDATE(paramMap);
		return paLtonGoodsDAO.selectPaLtonGoodsInfoList(paramMap);
	}

	@Override
	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception {
		return paLtonGoodsDAO.updateMassTargetYnByEpCode(massMap);
	}

	@Override
	public int updateMassTargetYn(PaLtonGoodsVO paltonGoods) throws Exception {
		return paLtonGoodsDAO.updateMassTargetYn(paltonGoods);
	}

	@Override
	public int updatePaLtonPaStatus(ParamMap paramMap) throws Exception {
		return paLtonGoodsDAO.updatePaLtonPaStatus(paramMap);
	}
	
}
