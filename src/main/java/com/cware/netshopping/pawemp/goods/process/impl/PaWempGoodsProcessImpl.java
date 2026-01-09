package com.cware.netshopping.pawemp.goods.process.impl;

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
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaWempGoodsVO;
import com.cware.netshopping.domain.PaWempGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.PaWempEntpSlip;
import com.cware.netshopping.pacommon.common.process.PaCommonProcess;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pawemp.goods.process.PaWempGoodsProcess;
import com.cware.netshopping.pawemp.goods.repository.PaWempGoodsDAO;

@Service("pawemp.goods.paWempGoodsProcess")
public class PaWempGoodsProcessImpl extends AbstractService implements PaWempGoodsProcess {
	
	@Resource(name = "pacommon.common.pacommonProcess")
    private PaCommonProcess pacommonProcess;
	
	@Resource(name = "pawemp.goods.paWempGoodsDAO")
	private PaWempGoodsDAO paWempGoodsDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
    private PaCommonDAO pacommonDAO;	
   
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Override
	public PaWempGoodsVO selectPaWempGoodsInfo(ParamMap paramMap) throws Exception {
		return paWempGoodsDAO.selectPaWempGoodsInfo(paramMap);
	}

	@Override
	public List<PaGoodsdtMapping> selectPaWempGoodsdtInfoList(ParamMap paramMap)throws Exception {
		return paWempGoodsDAO.selectPaWempGoodsdtInfoList(paramMap);
	}
	
	public List<PaGoodsOfferVO> selectPaWempGoodsOfferList(ParamMap paramMap) throws Exception {
		return paWempGoodsDAO.selectPaWempGoodsOfferList(paramMap);
	}

	@Override
	public String savePaWempGoods(PaWempGoodsVO paWempGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try{
			executedRtn = paWempGoodsDAO.selectPaWempGoodsModifyCheck(paWempGoods);
			if( executedRtn == 0) {
				executedRtn = paWempGoodsDAO.updatePaWempGoods(paWempGoods);
				if (executedRtn < 0) {
					log.info("tpawempgoods update fail");
					throw processException("msg.cannot_save", new String[]{"TPAWEMPGOODS UPDATE"});
				} 
				
				if (goodsdtMapping != null) {
					for (int i = 0; i < goodsdtMapping.size(); ++i) {
						goodsdtMapping.get(i).setModifyId(paWempGoods.getModifyId());
						goodsdtMapping.get(i).setModifyDate(paWempGoods.getModifyDate());
						
						executedRtn = paWempGoodsDAO.updatePaWempGoodsdt(goodsdtMapping.get(i));
						if (executedRtn < 0) {
							log.info("tpagoodsdt update fail");
							throw processException("msg.cannot_save", new String[]{"TPAGOODSDT UPDATE"});
						}
					}
				}
				
				executedRtn = paWempGoodsDAO.updatePaWempGoodsImage(paWempGoods);
				if (executedRtn < 0) {
					log.info("tpagoodsimage update fail");
					throw processException("msg.cannot_save", new String[]{"TPAGOODSIMAGE UPDATE"});
				}
				
				executedRtn = paWempGoodsDAO.updatePaWempCustShipCost(paWempGoods);
				if (executedRtn < 0) {
					log.info("tpacustshipcost update fail");
					throw processException("msg.cannot_save", new String[]{"TPACUSTSHIPCOST UPDATE"});
				}
				
				executedRtn = paWempGoodsDAO.updatePaWempGoodsOffer(paWempGoods);
				if (executedRtn < 0) {
					log.info("tpagoodsoffer update fail");
					throw processException("msg.cannot_save", new String[]{"TPAGOODSOFFER UPDATE"});
				}
				
			}
			
			executedRtn = paWempGoodsDAO.updatePaWempGoodsPrice(paWempGoods);
			if (executedRtn < 0) {
				log.info("tpagoodsprice update fail");
				throw processException("msg.cannot_save", new String[]{"TPAGOODSPRICE UPDATE"});
			}
			
			//프로모션 정보 업데이트
			if(paPromoTargetList != null && paPromoTargetList.size() > 0) {
				for(PaPromoTarget paPromoTarget : paPromoTargetList) {
					
					if(paPromoTarget.getTransDate() == null) {
						paPromoTarget.setModifyId(paWempGoods.getModifyId());
						paPromoTarget.setModifyDate(paWempGoods.getModifyDate());
						executedRtn = paWempGoodsDAO.updatePaPromoTarget(paPromoTarget);
						if (executedRtn < 0) {
							log.info("tpapromotarget update fail");
							throw processException("msg.cannot_save", new String[]{"TPAPROMOTARGET UPDATE"});
						}
						
					} else if(paPromoTarget.getTransDate() != null) {
						executedRtn = paWempGoodsDAO.insertNewPaPromoTarget(paPromoTarget);
						if (executedRtn < 0) {
							log.info("tpapromotarget insert fail");
							throw processException("msg.cannot_save", new String[]{"TPAPROMOTARGET INSERT"});
						}
					}
				}
			}
			executedRtn = paWempGoodsDAO.updatePaGoodsTarget(paWempGoods);
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
	public PaWempEntpSlip selectPaWempEntpSlip(ParamMap paramMap) throws Exception {
		return paWempGoodsDAO.selectPaWempEntpSlip(paramMap);
	}
	
	@Override
	public List<PaWempEntpSlip> selectPaWempEntpSlipList(ParamMap paramMap) throws Exception {
		return paWempGoodsDAO.selectPaWempEntpSlipList(paramMap); 
	}
	
	@Override
	public int insertPaWempGoodsTransLog(PaGoodsTransLog paGoodsTransLog)throws Exception {
		return paWempGoodsDAO.insertPaWempGoodsTransLog(paGoodsTransLog);
	}

	@Override
	public List<PaWempGoodsVO> selectPaWempGoodsSaleStopList(ParamMap paramMap)	throws Exception {
		return paWempGoodsDAO.selectPaWempGoodsSaleStopList(paramMap);
	}

	@Override
	public List<PaWempGoodsVO> selectPaWempGoodsSaleRestartList(ParamMap paramMap) throws Exception {
		return paWempGoodsDAO.selectPaWempGoodsSaleRestartList(paramMap);
	}

	@Override
	public PaWempGoodsVO selectPaWempGoodsProductNo(ParamMap paramMap) throws Exception {
		return paWempGoodsDAO.selectPaWempGoodsProductNo(paramMap);
	}

	@Override
	public String savePaWempGoodsSell(PaWempGoodsVO paWempGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			executedRtn = paWempGoodsDAO.updatePaWempGoodsSellStop(paWempGoods);
			if (executedRtn < 0) {
				log.info("tpawempgoods update fail");
				throw processException("msg.cannot_save", new String[]{"TPAWEMPGOODS UPDATE"});
			} else {
				return rtnMsg;
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
	}

	@Override
	public List<PaWempGoodsdtMappingVO> selectPaWempGoodsdtMappingStockList(ParamMap paramMap) throws Exception {
		return paWempGoodsDAO.selectPaWempGoodsdtMappingStockList(paramMap);
	}
	
	@Override
	public String savePaWempGoodsGroupNoticeNoTx(ParamMap paramMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			executedRtn = paWempGoodsDAO.updatePaWempGoodsGroupNoticeNo(paramMap);
			
			if (executedRtn < 0) {
				log.info("tpawempgoods update fail");
				throw processException("msg.cannot_save", new String[]{"TPAWEMPGOODS UPDATE"});
			} else {
				return rtnMsg;
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
	}
	
	@Override
	public String savePaWempGoodsdtOrderAbleQty(List<PaGoodsdtMapping> paGoodsdtMappingList, ParamMap paramMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			if (paGoodsdtMappingList != null) {
				for (PaGoodsdtMapping paGoodsdtMapping : paGoodsdtMappingList) {
					paGoodsdtMapping.setModifyId(paramMap.getString("modifyId"));
					paGoodsdtMapping.setModifyDate(DateUtil.toTimestamp(paramMap.getString("startDate"), "yyyy/MM/dd HH:mm:ss"));
					
					executedRtn = paWempGoodsDAO.updatePaWempGoodsdtOrderAbleQty(paGoodsdtMapping);
					if (executedRtn < 0) {
						log.info("tpagoodsdt update fail");
						throw processException("msg.cannot_save", new String[]{"TPAGOODSDTMAPPING UPDATE"});
					} 
				}
			}
			else{
				log.info("tpagoodsdt update fail");
				throw processException("msg.cannot_save", new String[]{"TPAGOODSDTMAPPING UPDATE"});
			}
			return rtnMsg;
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
	}
	
	@Override
	public List<PaWempGoodsVO> selectPaWempGoodsInfoList(ParamMap paramMap) throws Exception {
		return paWempGoodsDAO.selectPaWempGoodsInfoList(paramMap);
	}
	
	@Override
	public List<PaWempGoodsVO> selectPaWempGoodsStockList(ParamMap paramMap) throws Exception {
		return paWempGoodsDAO.selectPaWempGoodsStockList(paramMap);
	}
	
	@Override
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception {
		List<PaPromoTarget> promotionList = new ArrayList<PaPromoTarget>();
		//1) 높은 가격 기준으로 프로모션을 조회한다
		paramMap.put("alcoutPromoYn", "1");
		List<PaPromoTarget> promotionList1 = paWempGoodsDAO.selectPaPromoTarget(paramMap);
		
		//2) 가져온 데이터에 대한 마진률 계산 
		checkPromoMargin(promotionList1);
		
		//3) 프로모션 결과가 없으면 가장 최신 만료된(D) 데이터를 찾아서 반납한다 -> why? 프로모션 연동 여부를 기록하기 위해 		
		if(promotionList1.size() < 1) {
			promotionList1	= paWempGoodsDAO.selectPaPromoDeleteTarget(paramMap);	
		}
		
		promotionList.addAll(promotionList1);
		
		return promotionList;
	}
	
	private void checkPromoMargin(List<PaPromoTarget> promotionList1 ) throws Exception {
		if(promotionList1 == null ) 	return;
		if(promotionList1.size() < 1) 	return;
				
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
		limitMargin  = Double.parseDouble(ComUtil.NVL(pacommonDAO.selectPaMinMarginRate(minMarginMap), "-99")); // 2022-09-20 TCONFIG -> TPAPROMOMINMARGINRATE 로 관리
		if(limitMargin == -99) return;
		
		outOwnCost 	 = promotionList1.get(0).getOwnCost();
		promoTarget1 = promotionList1.get(0);
	
		//1) 마진률 CHECK
		paramMap.put("goodsCode"	, promoTarget1.getGoodsCode());
		paramMap.put("outOwnPrice"	, outOwnCost);
		paramMap.put("mode"			, sMode);
		paramMap.put("paCode"	  	, promoTarget1.getPaCode());
		paramMap.put("paGroupCode"	, Constants.PA_WEMP_GROUP_CODE);
		
		// 마진율 체크 제외 상품 확인
		marginCheckExceptYn = pacommonDAO.selectPromoMarginExceptYn(paramMap);
		if(marginCheckExceptYn > 0) return;
				
		margin 	 = pacommonDAO.selectGetPromoMargin(paramMap); 
		if(margin >= limitMargin) return;
		
		//2) 허용된 범위 안에서 최대 마진률 promotionList 세팅
		getMaxMarginPromo   (promotionList1,  paramMap);
		//3) Except_REASON Insert
		setExceptReason		( promotionList1 );
	}
	
	private void setHoldPromotion(List<PaPromoTarget> promotionList0, List<PaPromoTarget> promotionList1, ParamMap paramMap) throws Exception {
		/**
		 * 상황 프로모션 : 4,000원이 넘으면 최저 마진률을 초과해서 SD+OUT 합이 4,000원을 넘기면 안된다. HSBAEK
		 * 
		 * PROMO_NO 	DO_AMT 		TRANS_DATE	 PROC_GB	PROMO_GB	MEMEO
		 *  0000001 	1000	    2021/07/21     I		 OUT
		 *  0000002		 500		 			   I		  SD		혜택이 낮으므로 연동 제외
		 *  0000003		1500		2021/07/21	   I		  SD
		 *  0000004		5000								 OUT		마진률 OVER로 인한 연동제외
		 *  0000005		3500		2021/07/22	   U		 OUT
		 *  0000003		   0		2021/07/22	   I		  SD		*설명
		 * 
		 *  조합상 연동할수 있는 가장 큰 금액은 3,500원이다. (0000004 하나만 연동)
		 *  만약 0000003  0 원 데이터를 생성하지 않으면  주문 생성시 0000004(3,500), 0000003(1,500)원 혜택 5,000원을 적용한다.
		 *  
		 *  해당 함수는 0000003, 0원 데이터를 생성할수 있게 SETTING 하는 함수이다.
		 *  
		 * **/
		if(promotionList0.size() > 0 && promotionList1.size() > 0) return;
			
		
		if(promotionList0.size() < 1) {		
			paramMap.put("alcoutPromoYn" , "0");
			
			List<PaPromoTarget> promotionList = paWempGoodsDAO.selectPaPromoTarget(paramMap);
			if(promotionList.size() < 1) return;
			PaPromoTarget papromo = promotionList.get(0);
			
			papromo.setDoCost(0);
			papromo.setDoAmt(0);
			papromo.setOwnCost(0);
			promotionList0.add(papromo);
		}
		
		if( promotionList1.size() < 1) {
			
			paramMap.put("alcoutPromoYn" , "1");
			
			List<PaPromoTarget> promotionList = paWempGoodsDAO.selectPaPromoTarget(paramMap);
			if(promotionList.size() < 1) return;
			PaPromoTarget papromo = promotionList.get(0);
			
			papromo.setDoCost(0);
			papromo.setDoAmt(0);
			papromo.setOwnCost(0);
			promotionList1.add(papromo);
		}
	}
	
	private void getMaxMarginPromo(List<PaPromoTarget> promotionList1, ParamMap paramMap) throws Exception{
		if(promotionList1.size() < 1)	return;
		
		String promoNo = pacommonDAO.selectGetMaxPromoTarget(paramMap);  //FUN_GET_MAX_PAPROMOTARGET
		if(promoNo == null || promoNo.equals("")) {
			promotionList1.get(0).setDoCost(0);
			return;
		}
		
		paramMap.put("alcoutPromoYn", "1");
		paramMap.put("promoNo"		, promoNo);
		List<PaPromoTarget> promotionList = paWempGoodsDAO.selectPaPromoTarget(paramMap);

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
		return;
	}

	@Override
	public List<HashMap<String, String>> selectPaWempGoodsTrans(ParamMap paramMap) throws Exception {
		return paWempGoodsDAO.selectPaWempGoodsTrans(paramMap);
	}

	@Override
	public int updatePaWempGoodsFail(HashMap<String, String> paGoods) throws Exception {
		return paWempGoodsDAO.updatePaWempGoodsFail(paGoods);
	}

	@Override
	public List<PaWempGoodsVO> selectPaWempGoodsInfoListMass(ParamMap paramMap) throws Exception {
		pacommonDAO.execSP_PA_MASS_GOODS_UPDATE(paramMap);
		return paWempGoodsDAO.selectPaWempGoodsInfoList(paramMap);
	}

	@Override
	public int updateMassTargetYn(PaWempGoodsVO paWempGoods) throws Exception {
		return paWempGoodsDAO.updateMassTargetYn(paWempGoods);
	}

	@Override
	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception {
		return paWempGoodsDAO.updateMassTargetYnByEpCode(massMap);
	}
	
	
}