package com.cware.netshopping.paintp.goods.process.impl;

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
import com.cware.netshopping.domain.PaIntpGoodsVO;
import com.cware.netshopping.domain.PaIntpGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.common.process.PaCommonProcess;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.paintp.goods.process.PaIntpGoodsProcess;
import com.cware.netshopping.paintp.goods.repository.paIntpGoodsDAO;

@Service("paintp.goods.paIntpGoodsProcess")
public class paIntpGoodsProcessImpl extends AbstractService implements PaIntpGoodsProcess{
	@Resource(name = "pacommon.common.pacommonProcess")
    private PaCommonProcess pacommonProcess;
	
	@Resource(name = "paintp.goods.paIntpGoodsDAO")
	private paIntpGoodsDAO paIntpGoodsDAO;	
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;

	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	
	@Override
	public PaEntpSlip selectPaIntpEntpSlip(ParamMap paramMap) throws Exception {
		return paIntpGoodsDAO.selectPaIntpEntpSlip(paramMap);
	}
	
	@Override
	public PaIntpGoodsVO selectPaIntpGoodsInfo(ParamMap paramMap) throws Exception {
		return paIntpGoodsDAO.selectPaIntpGoodsInfo(paramMap);
	}
	
	@Override
	public List<PaGoodsOffer> selectPaIntpGoodsOfferList(ParamMap paramMap) throws Exception {
		return paIntpGoodsDAO.selectPaIntpGoodsOfferList(paramMap);
	}
	
	@Override
	public List<PaGoodsdtMapping> selectPaIntpGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paIntpGoodsDAO.selectPaIntpGoodsdtInfoList(paramMap);
	}
	
	@Override
	public int insertPaIntpGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception {
		return paCommonDAO.insertPaGoodsTransLog(paGoodsTransLog);
	}
	
	@Override
	public String savePaIntpGoods(PaIntpGoodsVO paIntpGoods, List<PaGoodsdtMapping> paGoodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try{
			executedRtn = paIntpGoodsDAO.selectPaIntpGoodsModifyCheck(paIntpGoods);
			if( executedRtn == 0) {
				executedRtn = paIntpGoodsDAO.updatePaIntpGoods(paIntpGoods);
				if (executedRtn < 0) {
					log.info("tpaIntpgoods update fail");
					throw processException("msg.cannot_save", new String[] { "TPAINTPGOODS UPDATE" });
				}
				
				if(paGoodsdtMapping != null){
					for(int i=0; i<paGoodsdtMapping.size(); i++){
						paGoodsdtMapping.get(i).setModifyId(paIntpGoods.getModifyId());
						paGoodsdtMapping.get(i).setModifyDate(paIntpGoods.getModifyDate());
						
						executedRtn = paIntpGoodsDAO.updatePaIntpGoodsdt(paGoodsdtMapping.get(i));
						if (executedRtn < 0) {
							log.info("tpagoodsdtmapping update fail");
							throw processException("msg.cannot_save", new String[]{"TPAGOODSDTMAPPING UPDATE"});
						}
					}
				}
				
				executedRtn = paIntpGoodsDAO.updatePaIntpGoodsImage(paIntpGoods);
				if (executedRtn < 0) {
					log.info("tpagoodsimage update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSIMAGE UPDATE" });
				}
				
				executedRtn = paIntpGoodsDAO.updatePaIntpCustShipCost(paIntpGoods);
				if (executedRtn < 0) {
					log.info("tpacustshipcost update fail");
					throw processException("msg.cannot_save", new String[] { "TPACUSTSHIPCOST UPDATE" });
				}
				
				executedRtn = paIntpGoodsDAO.updatePaIntpGoodsOffer(paIntpGoods);
				if (executedRtn < 0) {
					log.info("tpaIntpgoodsoffer update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSOFFER UPDATE" });
				}
			}
			
			if(paPromoTargetList != null && paPromoTargetList.size() > 0) {
				for(PaPromoTarget paPromoTarget : paPromoTargetList) {
					
					if(paPromoTarget != null && paPromoTarget.getTransDate() == null) {
						paPromoTarget.setModifyId(paIntpGoods.getModifyId());
						paPromoTarget.setModifyDate(paIntpGoods.getModifyDate());
						executedRtn = paIntpGoodsDAO.updatePaPromoTarget(paPromoTarget);
						if (executedRtn < 0) {
							log.info("tpapromotarget update fail");
							throw processException("msg.cannot_save", new String[]{"TPAPROMOTARGET UPDATE"});
						}
					}else if(paPromoTarget != null && paPromoTarget.getTransDate() != null) {
						executedRtn = paIntpGoodsDAO.insertNewPaPromoTarget(paPromoTarget);
						if (executedRtn < 0) {
							log.info("tpapromotarget insert fail");
							throw processException("msg.cannot_save", new String[]{"TPAPROMOTARGET INSERT"});
						}
					}
				}
			}
			
			executedRtn = paIntpGoodsDAO.updatePaIntpGoodsPrice(paIntpGoods);
			if (executedRtn < 0) {
				log.info("tpagoodsprice update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICE UPDATE" });
			}
			
			//프로모션은 나중에 처리
			executedRtn = paIntpGoodsDAO.updatePaGoodsTarget(paIntpGoods);
			if (executedRtn < 0) {
				log.info("tpagoodstarget update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSTARGET UPDATE" });
			}			
			
		} catch (Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		return rtnMsg;
	}
	
	@Override
	public String savePaIntpFailGoods(PaIntpGoodsVO paIntpGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			executedRtn = paIntpGoodsDAO.updatePaIntpGoods(paIntpGoods);
			if (executedRtn < 0) {
				log.info("tpaIntpgoods update fail");
				throw processException("msg.cannot_save", new String[] { "TPAINTPGOODS UPDATE" });
			}
			
		} catch (Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		return rtnMsg;
	}
	
	@Override
	public List<PaEntpSlip> selectPaIntpEntpSlipList(ParamMap paramMap) throws Exception {
		return paIntpGoodsDAO.selectPaIntpEntpSlipList(paramMap);
	}
	
	@Override
	public List<PaIntpGoodsVO> selectPaIntpGoodsInfoList(ParamMap paramMap) throws Exception {
		return paIntpGoodsDAO.selectPaIntpGoodsInfoList(paramMap);
	}
	
	@Override
	public List<PaIntpGoodsdtMappingVO> selectPaIntpGoodsdtMappingStockList(ParamMap paramMap) throws Exception {
		return paIntpGoodsDAO.selectPaIntpGoodsdtMappingStockList(paramMap);
	}
	
	@Override
	public String savePaIntpGoodsStock(List<PaIntpGoodsdtMappingVO> paIntpGoodsMapping) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		PaIntpGoodsdtMappingVO paIntpGoodsdtMapping = null;
		
		for(int i=0; i<paIntpGoodsMapping.size(); i++) {
			paIntpGoodsdtMapping = paIntpGoodsMapping.get(i);
			paIntpGoodsdtMapping.setModifyId("PAINTP");
			executedRtn = paIntpGoodsDAO.updatePaIntpGoodsdtMappingQty(paIntpGoodsdtMapping);
			if (executedRtn < 0) {
				rtnMsg = Constants.SAVE_FAIL;
				throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
			}
		}		
		return rtnMsg;
	}

	@Override
	public List<PaIntpGoodsdtMappingVO> selectEmptyPaOptionCodeList(ParamMap paramMap) throws Exception {
		return paIntpGoodsDAO.selectEmptyPaOptionCodeList(paramMap);
	}
	
	@Override
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception {	
		List<PaPromoTarget> promotionList = new ArrayList<PaPromoTarget>();
		//1) 높은 가격 기준으로 프로모션을 조회한다
		paramMap.put("alcoutPromoYn", "1");
		List<PaPromoTarget> promotionList1 = paIntpGoodsDAO.selectPaPromoTarget(paramMap);
		
		//2) 가져온 데이터에 대한 마진률 계산 
		checkPromoMargin(promotionList1);
		
		//3) 프로모션 결과가 없으면 가장 최신 만료된(D) 데이터를 찾아서 반납한다 -> why? 프로모션 연동 여부를 기록하기 위해 
		if(promotionList1.size() < 1) {
			promotionList1	= paIntpGoodsDAO.selectPaPromoDeleteTarget(paramMap);	
		}
		
		promotionList.addAll(promotionList1);
		
		return promotionList;
	}

	@Override
	public List<HashMap<String, Object>> selectGoodsLimitCharList(String paCode) throws Exception {
		return paIntpGoodsDAO.selectGoodsLimitCharList(paCode);
	}

	@Override
	public List<PaIntpGoodsdtMappingVO> selectPaIntpGoodsdtMappingStock(ParamMap paramMap) throws Exception {
		return paIntpGoodsDAO.selectPaIntpGoodsdtMappingStock(paramMap);
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
		
		
		//1) 마진률 CHECK
		paramMap.put("goodsCode"	, promoTarget1.getGoodsCode());
		paramMap.put("outOwnPrice"	, outOwnCost);
		paramMap.put("mode"			, sMode);
		paramMap.put("paCode"	  	, promoTarget1.getPaCode());
		paramMap.put("paGroupCode"	, Constants.PA_INTP_GROUP_CODE);
		
		// 마진율 체크 제외 상품 확인
		marginCheckExceptYn = paCommonDAO.selectPromoMarginExceptYn(paramMap);
		if(marginCheckExceptYn > 0) return;
		
		margin 	 = paCommonDAO.selectGetPromoMargin(paramMap); 
		if(margin >= limitMargin) return;
		
		//2) 허용된 범위 안에서 최대 마진률 promotionList 세팅
		getMaxMarginPromo   (promotionList1,  paramMap);
		//3) Except_REASON Insert
		setExceptReason		(promotionList1);
	}

	private void getMaxMarginPromo(List<PaPromoTarget> promotionList1, ParamMap paramMap) throws Exception{
		if(promotionList1.size() < 1)	return;
		
		String promoNo = paCommonDAO.selectGetMaxPromoTarget(paramMap);  //FUN_GET_MAX_PAPROMOTARGET
		if(promoNo == null || promoNo.equals("")) {
			promotionList1.get(0).setDoCost(0);
			return;
		}
		
		paramMap.put("promoNo"		, promoNo);
		paramMap.put("alcoutPromoYn", "1");
		List<PaPromoTarget> promotionList = paIntpGoodsDAO.selectPaPromoTarget(paramMap);
		
		if(promotionList1.size() > 0)	promotionList1.remove(0);
		promotionList1.add(promotionList.get(0));
	}
	
	private void setExceptReason(List<PaPromoTarget> promotionList1) {
		if( promotionList1 == null ) return; 
		if( promotionList1.size() < 1 ) return; 
		if( promotionList1.get(0).getDoCost() < 1) return;
		
		ParamMap paramMap 	= new ParamMap();		
		paramMap.put("goodsCode"		, promotionList1.get(0).getGoodsCode());
		paramMap.put("paCode"			, promotionList1.get(0).getPaCode());
		paramMap.put("limitMarginAmt"	, promotionList1.get(0).getOwnCost());
		paramMap.put("paGroupCode"		,"");
		pacommonProcess.setExceptReason4OnePromotion(paramMap);		
	}

	@Override
	public List<HashMap<String, String>> selectPaIntpGoodsTrans(ParamMap paramMap) throws Exception {
		return paIntpGoodsDAO.selectPaIntpGoodsTrans(paramMap);
	}

	@Override
	public int updatePaIntpGoodsFail(HashMap<String, String> paGoodsTarget) throws Exception {
		return paIntpGoodsDAO.updatePaIntpGoodsFail(paGoodsTarget);
	}

	@Override
	public List<PaIntpGoodsVO> selectPaIntpGoodsInfoListMass(ParamMap paramMap) throws Exception {
		paramMap.put("modCase", "MODIFY");
		paCommonDAO.execSP_PA_MASS_GOODS_UPDATE(paramMap);
		return paIntpGoodsDAO.selectPaIntpGoodsInfoList(paramMap);
	}

	@Override
	public int updateMassTargetYn(PaIntpGoodsVO paIntpGoods) throws Exception {
		return paIntpGoodsDAO.updateMassTargetYn(paIntpGoods);
	}

	@Override
	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception {
		return paIntpGoodsDAO.updateMassTargetYnByEpCode(massMap);
	}

	@Override
	public HashMap<String, Object> selectPaNoticeData(ParamMap paramMap) throws Exception {
		return paIntpGoodsDAO.selectPaNoticeData(paramMap);
	}
	
}

