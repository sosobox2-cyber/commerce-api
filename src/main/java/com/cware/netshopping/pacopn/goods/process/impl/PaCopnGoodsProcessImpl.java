package com.cware.netshopping.pacopn.goods.process.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.CopnGoodsDeleteVO;
import com.cware.netshopping.domain.PaCopnGoodsVO;
import com.cware.netshopping.domain.PaCopnGoodsdtMappingVO;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.PaCopnGoodsAttri;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.common.process.PaCommonProcess;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacopn.goods.process.PaCopnGoodsProcess;
import com.cware.netshopping.pacopn.goods.repository.PaCopnGoodsDAO;
import com.google.gson.JsonObject;
import com.cware.netshopping.domain.model.PaCopnGoodsUserAttri;


@Service("pacopn.goods.paCopnGoodsProcess")
public class PaCopnGoodsProcessImpl extends AbstractService implements PaCopnGoodsProcess{

	@Resource(name = "pacommon.common.pacommonProcess")
    private PaCommonProcess pacommonProcess;
	
	@Resource(name = "pacopn.goods.paCopnGoodsDAO")
	private PaCopnGoodsDAO paCopnGoodsDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
    private PaCommonDAO pacommonDAO;	
   
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Autowired
	PaGoodsPriceApplyMapper priceMapper;
	
	@Override
	public PaEntpSlip selectPaCopnEntpSlip(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnEntpSlip(paramMap);
	}

	@Override
	public PaCopnGoodsVO selectPaCopnGoodsInfo(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnGoodsInfo(paramMap);
	}

	@Override
	public List<PaGoodsOfferVO> selectPaCopnGoodsOfferList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnGoodsOfferList(paramMap);
	}

	@Override
	public List<PaCopnGoodsAttri> selectPaCopnGoodsAttriList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnGoodsAttriList(paramMap);
	}

	@Override
	public List<PaGoodsdtMapping> selectPaCopnGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnGoodsdtInfoList(paramMap);
	}

	@Override
	public int insertPaCopnGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception {
		return paCopnGoodsDAO.insertPaCopnGoodsTransLog(paGoodsTransLog);
	}

	@Override
	public String savePaCopnGoodsAttri(List<PaCopnGoodsAttri> copnGoodsAttri) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		PaCopnGoodsAttri copnAttri = null;
		try {
			for(int i = 0; i < copnGoodsAttri.size(); i++) {
				copnAttri = copnGoodsAttri.get(i);
				
				if(copnAttri.getRequiredYn().equals("0") && (copnAttri.getAttributeTypeMapping()!=null || copnAttri.getAttributeValueName() != null)) {
					copnAttri.setRequiredYn("1");
					copnAttri.setModifyDate(copnGoodsAttri.get(0).getModifyDate());
					copnAttri.setModifyId(copnGoodsAttri.get(0).getModifyId());
					executedRtn = paCopnGoodsDAO.updatePaCopnGoodsAttri(copnAttri);
				}
			}
			
			if (executedRtn < 0) {
				log.info("tpacopngoods update fail");
				throw processException("msg.cannot_save", new String[]{"TPACOPNGOODS UPDATE"});
			} else {
				return rtnMsg;
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
	}

	@Override
	public String savePaCopnGoods(PaCopnGoodsVO paCopnGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception {
		return savePaCopnGoods(paCopnGoods, goodsdtMapping, paPromoTargetList, null);
	}

	public String savePaCopnGoods(PaCopnGoodsVO paCopnGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList, PaGoodsPriceApply goodsPriceApply) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;
		
//		executedRtn = paCopnGoodsDAO.selectPaCopnGoodsModifyCheck(paCopnGoods);
		try{
//			if( executedRtn == 0) {
				executedRtn = paCopnGoodsDAO.updatePaCopnGoods(paCopnGoods);
				if(executedRtn < 0){
					log.info("tpacopngoods update fail");
					throw processException("msg.cannot_save", new String[]{"TPACOPNGOODS UPDATE"});
				}
				
				executedRtn = paCopnGoodsDAO.updatePaCopnGoodsImage(paCopnGoods);
				if(executedRtn < 0){
					log.info("tpagoodsimage update fail");
					throw processException("msg.cannot_save", new String[]{"TPAGOODSIMAGE UPDATE"});
				}
				
				executedRtn = paCopnGoodsDAO.updatePaCopnCustShipCost(paCopnGoods);
				if(executedRtn < 0){
					log.info("tpacustshipcost update fail");
					throw processException("msg.cannot_save", new String[]{"TPACUSTSHIPCOST UPDATE"});
				}
				
				executedRtn = paCopnGoodsDAO.updatePaCopnGoodsOffer(paCopnGoods);
				if (executedRtn < 0) {
					log.info("tpaCopngoodsoffer update fail");
					throw processException("msg.cannot_save", new String[]{"TPAGOODSOFFER UPDATE"});
				}
				
				if(goodsdtMapping != null){
					for(int i=0; i<goodsdtMapping.size(); i++){
						goodsdtMapping.get(i).setModifyId(paCopnGoods.getModifyId());
						goodsdtMapping.get(i).setModifyDate(paCopnGoods.getModifyDate());
						if(goodsdtMapping.get(i).getRemark1() == null){
							executedRtn = paCopnGoodsDAO.updatePaCopnGoodsdt(goodsdtMapping.get(i));
							if(executedRtn < 0){
								log.info("tpagoodsdt update fail");
								throw processException("msg.cannot_save", new String[]{"TPAGOODSDT UPDATE"});
							}
						} else {
							if ("1".equals(goodsdtMapping.get(i).getTransTargetYn())) {
								executedRtn = paCopnGoodsDAO.updatePaCopnGoodsdtTargetYn(goodsdtMapping.get(i));
								if (executedRtn < 0) {
									log.info("tpagoodsdt update fail");
									throw processException("msg.cannot_save", new String[]{"TPAGOODSDT UPDATE"});
								}
							}
						}
					}
				}
//			}
			
			if(goodsdtMapping.get(0).getRemark1() == null){
				executedRtn = paCopnGoodsDAO.updatePaCopnGoodsPrice(paCopnGoods);
				if(executedRtn < 0){
					log.info("tpagoodsprice update fail");
					throw processException("msg.cannot_save", new String[]{"TPAGOODSPRICE UPDATE"});
				}
				
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
				if(paPromoTargetList != null && paPromoTargetList.size() > 0) {
					for(PaPromoTarget paPromoTarget : paPromoTargetList) {
						
						if(paPromoTarget != null && paPromoTarget.getTransDate() == null) {
							paPromoTarget.setModifyId(paCopnGoods.getModifyId());
							paPromoTarget.setModifyDate(paCopnGoods.getModifyDate());
							executedRtn = paCopnGoodsDAO.updatePaPromoTarget(paPromoTarget);
							if (executedRtn < 0) {
								log.info("tpapromotarget update fail");
								throw processException("msg.cannot_save", new String[]{"TPAPROMOTARGET UPDATE"});
							}
						} else if(paPromoTarget != null && paPromoTarget.getTransDate() != null) {
							executedRtn = executedRtn + paCopnGoodsDAO.insertNewPaPromoTarget(paPromoTarget);
							if (executedRtn < 0) {
								log.info("tpapromotarget insert fail");
								throw processException("msg.cannot_save", new String[]{"TPAPROMOTARGET INSERT"});
							}						
						}
					}
				}
				//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
				
				// 프로모션개선 적용 (구 프로모션은 운영에서 주문 프로모션개선 완료되면 삭제)
				if (goodsPriceApply != null) {
					goodsPriceApply.setTransId(paCopnGoods.getModifyId());
					priceMapper.updatePriceApplyTrans(goodsPriceApply);
				}
				
			}

			executedRtn = paCopnGoodsDAO.updatePaGoodsTarget(paCopnGoods);
			if (executedRtn < 0) {
				log.info("tpagoodstarget update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSTARGET UPDATE" });
			}
			
		}catch(Exception e){
			rtnMsg = e.getMessage();
			throw e;
		}
		
		return rtnMsg;
	}
	
	@Override
	public String savePaCopnGoodsFail(PaCopnGoodsVO paCopnGoods) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;
		
		try{
			executedRtn = paCopnGoodsDAO.updatePaCopnGoodsFail(paCopnGoods);
			if(executedRtn < 0){
				log.info("tpacopngoods update fail");
				throw processException("msg.cannot_save", new String[]{"TPACOPNGOODS UPDATE"});
			}
			
		}catch(Exception e){
			rtnMsg = e.getMessage();
			throw e;
		}
		
		return rtnMsg;
	}

	@Override
	public String savePaCopnGoodsDtOption(PaCopnGoodsdtMappingVO goodsdtMapping) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;
		
		try{
			executedRtn = paCopnGoodsDAO.savePaCopnGoodsDtOption(goodsdtMapping);
			if(executedRtn < 0){
				log.info("tpagoodsdtmapping update fail");
				throw processException("msg.cannot_save", new String[]{"TPAGOODSDTMAPPING UPDATE"});
			} else {
				return rtnMsg;
			}
		}catch(Exception e){
			rtnMsg = e.getMessage();
			throw e;
		}
	}

	@Override
	public String savePaCopnApprovalStatus(PaCopnGoodsVO goodsMap) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;
		try {
			executedRtn = paCopnGoodsDAO.savePaCopnApprovalStatus(goodsMap);
			
			if (executedRtn < 0) {
				log.info("tpacopngoods update fail");
				throw processException("msg.cannot_save", new String[]{"TPACOPNGOODS UPDATE"});
			} else {
				return rtnMsg;
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
	}

	@Override
	public List<PaEntpSlip> selectPaCopnEntpSlipList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnEntpSlipList(paramMap);
	}

	@Override
	public List<PaCopnGoodsdtMappingVO> selectEmptyVendorId(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectEmptyVendorId(paramMap);
	}
	
	@Override
	public List<PaCopnGoodsdtMappingVO> selectEmptyProductId(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectEmptyProductId(paramMap);
	}

	@Override
	public List<PaCopnGoodsVO> selectRegisterEmpty(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectRegisterEmpty(paramMap);
	}

	@Override
	public List<PaCopnGoodsdtMappingVO> selectPaCopnGoodsProductNo(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnGoodsProductNo(paramMap);
	}

	@Override
	public String savePaCopnGoodsSell(PaCopnGoodsdtMappingVO paCopnGoodsdtMapping) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;
		
		try {
			executedRtn = paCopnGoodsDAO.updatePaCopnGoodsSellStop(paCopnGoodsdtMapping);
			if (executedRtn < 0) {
				log.info("tpacopngoods update fail");
				throw processException("msg.cannot_save", new String[]{"TPACOPNGOODS UPDATE"});
			} else {
				return rtnMsg;
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
	}

	@Override
	public List<PaCopnGoodsVO> selectPaCopnGoodsSaleStopList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnGoodsSaleStopList(paramMap);
	}

	@Override
	public List<PaCopnGoodsVO> selectPaCopnGoodsSaleRestartList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnGoodsSaleRestartList(paramMap);
	}

	@Override
	public List<PaCopnGoodsVO> selectPaCopnGoodsInfoList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnGoodsInfoList(paramMap);
	}

	@Override
	public String savePaChangeOptionStatus(PaCopnGoodsVO paCopnGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			executedRtn = paCopnGoodsDAO.savePaChangeOptionStatus(paCopnGoods);
			
			if (executedRtn < 0) {
				log.info("tpacopngoods update fail");
				throw processException("msg.cannot_save", new String[]{"TPACOPNGOODS UPDATE"});
			} else {
				return rtnMsg;
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
	}

	@Override
	public List<PaCopnGoodsdtMappingVO> selectPaCopnGoodsdtMappingStockList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnGoodsdtMappingStockList(paramMap);
	}

	@Override
	public String updatePaCopnGoodsdtOrder(PaCopnGoodsdtMappingVO paCopnGoodsMapping) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;
		
		try {
			executedRtn = paCopnGoodsDAO.updatePaCopnGoodsdtOrder(paCopnGoodsMapping);
			if (executedRtn < 0) {
				log.info("tpagoodsdtmapping update fail");
				throw processException("msg.cannot_save", new String[]{"TPAGOODSDTMAPPING UPDATE"});
			} else {
				return rtnMsg;
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
	}

	@Override
	public List<PaGoodsPriceVO> selectCopnPriceModify(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectCopnPriceModify(paramMap);
	}

	@Override
	public List<PaCopnGoodsdtMappingVO> selectCopnPriceModifyVendorIdSearch(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectCopnPriceModifyVendorIdSearch(paramMap);
	}

	@Override
	public String updatePaCopnGoodsPriceDiscount(PaCopnGoodsdtMappingVO paCopnGoodsMapping, List<PaPromoTarget> paPromoTargetList) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;
		try {
			executedRtn = paCopnGoodsDAO.updatePaCopnGoodsPriceDiscount(paCopnGoodsMapping);
			
			//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
			if(paPromoTargetList != null && paPromoTargetList.size() > 0) {
				for(PaPromoTarget paPromoTarget : paPromoTargetList) {
					
					if(paPromoTarget != null && paPromoTarget.getTransDate() == null) {
						paPromoTarget.setModifyId(paCopnGoodsMapping.getModifyId());
						paPromoTarget.setModifyDate(paCopnGoodsMapping.getModifyDate());
						executedRtn = executedRtn + paCopnGoodsDAO.updatePaPromoTarget(paPromoTarget);
					}else if(paPromoTarget != null && paPromoTarget.getTransDate() != null) {
						executedRtn = executedRtn + paCopnGoodsDAO.insertNewPaPromoTarget(paPromoTarget);
					}
					
					if (executedRtn < 0) {
						log.info("tpagoodsprice update fail");
						throw processException("msg.cannot_save", new String[]{"TPAGOODSPRICE UPDATE"});
					}
				}
			}
			//프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : E
			
			return rtnMsg;
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
	}

	@Override
	public String savePaCopnProductId(PaCopnGoodsdtMappingVO goodsdtMapping) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;
		try{
			executedRtn = paCopnGoodsDAO.savePaCopnProductId(goodsdtMapping);
			if(executedRtn < 0){
				log.info("tpagoodsdtmapping update fail");
				throw processException("msg.cannot_save", new String[]{"TPAGOODSDTMAPPING UPDATE"});
			} else {
				return rtnMsg;
			}
		}catch(Exception e){
			rtnMsg = e.getMessage();
			throw e;
		}
	}
	
	@Override
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception {
		List<PaPromoTarget> promotionList = new ArrayList<PaPromoTarget>();
		//1) 높은 가격 기준으로 프로모션을 조회한다
		paramMap.put("alcoutPromoYn", "1");
		List<PaPromoTarget> promotionList1 = paCopnGoodsDAO.selectPaPromoTarget(paramMap);
		
		//2) 가져온 데이터에 대한 마진률 계산 
		checkPromoMargin(promotionList1);
		
		//3) 프로모션 결과가 없으면 가장 최신 만료된(D) 데이터를 찾아서 반납한다 -> why? 프로모션 연동 여부를 기록하기 위해 
		if(promotionList1.size() < 1) {
			promotionList1	= paCopnGoodsDAO.selectPaPromoDeleteTarget(paramMap);	
		}
		
		/* 2022.01.12 테스트 결과 필요없어서 일단 제거, 1월 20일 이후 주석도 제거 요망 HSBAEK
		paramMap.put("alcoutPromoYn", "0");
		List<PaPromoTarget> promotionList0 = paCopnGoodsDAO.selectPaPromoTarget(paramMap);
		if(promotionList0 != null && promotionList0.size() > 0) {
			promotionList0.get(0).setDoCost(0);
			promotionList0.get(0).setDoAmt(0);
			promotionList0.get(0).setDoOwnCost(0);
			promotionList0.get(0).setEntpCost(0);
		}else {
			promotionList0	= paCopnGoodsDAO.selectPaPromoDeleteTarget(paramMap);
		}
		promotionList.addAll(promotionList0);
		*/
		
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
		ParamMap minMarginMap	   	= new ParamMap();

		minMarginMap.put("paGroupCode", promotionList1.get(0).getPaGroupCode());
		minMarginMap.put("paCode", promotionList1.get(0).getPaCode());
		
//		limitMargin = Double.parseDouble( ComUtil.NVL(systemDAO.getVal("PA_LIMIT_MARGIN") , "-99"));
		limitMargin  = Double.parseDouble(ComUtil.NVL(pacommonDAO.selectPaMinMarginRate(minMarginMap), "-99")); // 2022-09-20 TCONFIG -> TPAPROMOMINMARGINRATE 로 관리
		if(limitMargin == -99) return;

		promoTarget1 = promotionList1.get(0);
		outOwnCost 	 = promoTarget1.getOwnCost();
		
		//1) 마진률 CHECK
		paramMap.put("goodsCode"	, promoTarget1.getGoodsCode());
		paramMap.put("outOwnPrice"	, outOwnCost);
		paramMap.put("mode"			, sMode);
		paramMap.put("paCode"	  	, promoTarget1.getPaCode());
		paramMap.put("paGroupCode"	, "05");
		
		// 마진율 체크 제외 상품 확인
		marginCheckExceptYn = pacommonDAO.selectPromoMarginExceptYn(paramMap);
		if(marginCheckExceptYn > 0) return;
				
		margin 	 = pacommonDAO.selectGetPromoMargin(paramMap); 
		if(margin >= limitMargin) return;
		
		//2) 허용된 범위 안에서 최대 마진률 promotionList 세팅
		getMaxMarginPromo   (promotionList1,  paramMap);

		//3) Except_REASON UPDATE
		setExceptReason		(promotionList1 );
	}
	
	@Override
	public List<HashMap<String, Object>> selectGoodsLimitCharList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectGoodsLimitCharList(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectGoodsForCopnPolicy(PaCopnGoodsVO paCopnGoods) throws Exception {
		HashMap<String,String> policyMap = new HashMap<String, String>();
		HashMap<String,String> exceptEntp;
		HashMap<String,String> exceptLmsd;
		
		String entpCode = "";
		String lmsdCode = "";
		String duration = "";
		String installYn = "";
		String createYn = "";
		String paCode = "";
		
		//goodsInfo를 조회
		entpCode = paCopnGoods.getEntpCode();
		lmsdCode = paCopnGoods.getLmsdCode();
		installYn = paCopnGoods.getInstallYn();
		createYn = paCopnGoods.getCreateYn();
		paCode = paCopnGoods.getPaCode();
		
		exceptEntp = paCopnGoodsDAO.selectExceptEntpForCopnPolicy(entpCode);
		exceptLmsd = paCopnGoodsDAO.selectExceptCategoryForCopnPolicy(lmsdCode);
		
		/**
		 * TPADELIVERYPOLICYEXCEPT 발송정책 조회 예외 업체/카테고리
		 * DURATION   : 기간
		 */
		
		//default => 순차배송 2일 
		// 21.11.02 방송상품 1일, 일반상품 2일 변경
		if ( "51".equals(paCode) ) {
			duration = "1";
		} else if ( "52".equals(paCode) ) {
			duration = "2";
		}
		
		//예외업체
		if (exceptEntp != null) {
			duration   = exceptEntp.get("DURATION");
		} else {
			//예외 카테고리[중분류]
			if (exceptLmsd != null) {
				duration   = exceptLmsd.get("DURATION");
			} else if("1".equals(paCopnGoods.getCopnFreshYn()) && createYn.equals("1")) {
				// 식품 카테고리 and 주문제작 의 경우 10일 세팅 시 에러발생 -> 7일로 세팅되도록 수정  2022.03.28 LEEJY
				duration = "7";
			} else {
				//설치배송여부, 주문제작 상품 => 10일 
				if (installYn.equals("1") || createYn.equals("1")) {
					duration = "10";
				}
			}
		}
		policyMap.put("DURATION", duration);
		return policyMap;
	}
	
	/* 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜 대표 정보 조회 */
	@Override
	public HashMap<String,Object> selectPaCopnAlcoutDealInfo(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnAlcoutDealInfo(paramMap);
	}
	
	/* 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜 대표 상품 정보 조회 */
	@Override
	public PaCopnGoodsVO selectPaCopnAlcoutDealGoodsInfo(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnAlcoutDealGoodsInfo(paramMap);
	}
	
	/* 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜 대표 상품 정보 조회 */
	@Override
	public List<HashMap<String,Object>> selectPaCopnAlcoutDealGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnAlcoutDealGoodsdtInfoList(paramMap);
	}
	
	/* 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜 상품 정보 조회 */
	@Override
	public List<HashMap<String,Object>> selectPaCopnAlcoutDealGoodsList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnAlcoutDealGoodsList(paramMap);
	}
	
	@Override
	public String savePaCopnAlcoutDealGoods(HashMap<String, Object> paCopnAlcoutDealInfo) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;
		
		try{
			executedRtn = paCopnGoodsDAO.savePaCopnAlcoutDealGoods(paCopnAlcoutDealInfo);
			if(executedRtn < 0){
				log.info("tpacopngoods update fail");
				throw processException("msg.cannot_save", new String[]{"TPACOPNGOODS UPDATE"});
			}
		}catch(Exception e){
			rtnMsg = e.getMessage();
			throw e;
		}
		
		return rtnMsg;
	}
	
	@Override
	public String saveAlcoutDealGoodsdtMappingPaOptionCode(HashMap<String, Object> paCopnAlcoutDealInfo) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;
		
		try{
			executedRtn = paCopnGoodsDAO.saveAlcoutDealGoodsdtMappingPaOptionCode(paCopnAlcoutDealInfo);
			if(executedRtn < 0){
				log.info("tpagoodsdtmapping update fail");
				throw processException("msg.cannot_save", new String[]{"TPAGOODSDTMAPPING UPDATE"});
			} else {
				return rtnMsg;
			}
		}catch(Exception e){
			rtnMsg = e.getMessage();
			throw e;
		}
	}
	
	@Override
	public List<HashMap<String,Object>> selectPaCopnModifyAlcoutDealList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnModifyAlcoutDealList(paramMap);
	}
	
	@Override
	public List<HashMap<String,Object>> selectAlcoutDealEmptyVendorId() throws Exception {
		return paCopnGoodsDAO.selectAlcoutDealEmptyVendorId();
	}
	
	@Override
	public List<HashMap<String,Object>> selectAlcoutDealEmptyProductId() throws Exception {
		return paCopnGoodsDAO.selectAlcoutDealEmptyProductId();
	}

	@Override
	public List<HashMap<String,Object>> selectRegisterAlcoutDealEmpty() throws Exception {
		return paCopnGoodsDAO.selectRegisterAlcoutDealEmpty();
	}
	
	@Override
	public List<PaCopnGoodsdtMappingVO> selectPaCopnAlcoutDealGoodsProductNo(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnAlcoutDealGoodsProductNo(paramMap);
	}
	
	@Override
	public String savePaCopnAlcoutDealGoodsSell(PaCopnGoodsdtMappingVO paCopnGoodsdtMapping) throws Exception {
		String rtnMsg = "000000";
		int executedRtn = 0;
		
		try {
			executedRtn = paCopnGoodsDAO.updatePaCopnAlcoutDealGoodsSell(paCopnGoodsdtMapping);
			if (executedRtn < 0) {
				log.info("tpacopngoods update fail");
				throw processException("msg.cannot_save", new String[]{"TPACOPNGOODS UPDATE"});
			} else {
				return rtnMsg;
			}
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
	}
	
	@Override
	public List<PaCopnGoodsdtMappingVO> selectPaCopnAlcoutDealGoodsdtMappingStockList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnAlcoutDealGoodsdtMappingStockList(paramMap);
	}
	
	@Override
	public List<PaCopnGoodsVO> selectPaCopnAlcoutDealGoodsSaleStopList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnAlcoutDealGoodsSaleStopList(paramMap);
	}

	@Override
	public List<PaCopnGoodsVO> selectPaCopnAlcoutDealGoodsSaleRestartList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnAlcoutDealGoodsSaleRestartList(paramMap);
	}
	
	@Override
	public List<HashMap<String,Object>> selectPaCopnNotExistsGoodsdtList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnNotExistsGoodsdtList(paramMap);
	}
	
	@Override
	public String insertPaCopnNotExistsGoodsdt(HashMap<String, Object> paCopnNotExistsGoodsdt) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		executedRtn = paCopnGoodsDAO.insertPaCopnNotExistsGoodsdt(paCopnNotExistsGoodsdt);
		if (executedRtn < 0) {
			log.info("TGDS_ALCOUT_GOODS_DEAL_MAPP INSERT fail");
			throw processException("msg.cannot_save", new String[] { "TGDS_ALCOUT_GOODS_DEAL_MAPP INSERT" });
		}
		
		return rtnMsg;
	}
	
	@Override
	public List<PaCopnGoodsVO> selectPaCopnAlcoutDealGoodsDescribe(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnAlcoutDealGoodsDescribe(paramMap);
	}

	private void getMaxMarginPromo( List<PaPromoTarget> promotionList1, ParamMap paramMap) throws Exception{
		if(promotionList1.size() < 1)	return;
		
		String promoNo = pacommonDAO.selectGetMaxPromoTarget(paramMap);  //FUN_GET_MAX_PAPROMOTARGET
		if(promoNo == null || promoNo.equals("")) {
			promotionList1.get(0).setDoCost(0);
			return;		
		}

		paramMap.put("alcoutPromoYn", "1");
		paramMap.put("promoNo"		, promoNo);
		List<PaPromoTarget> promotionList = paCopnGoodsDAO.selectPaPromoTarget(paramMap);
		
		if(promotionList1.size() > 0)	promotionList1.remove(0);
		promotionList1.add(promotionList.get(0));
	}
	
	private void setExceptReason(  List<PaPromoTarget> promotionList1 ) {	
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
	public List<HashMap<String, String>> selectPaCopnGoodsTrans(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnGoodsTrans(paramMap);
	}

	@Override
	public int updatePaCopnGoodsFail(PaCopnGoodsVO paCopnGoods) throws Exception {
		return paCopnGoodsDAO.updatePaCopnGoodsFail(paCopnGoods);
	}

	@Override
	public int updateMassTargetYn(PaCopnGoodsVO paCopnGoods) throws Exception {
		return paCopnGoodsDAO.updateMassTargetYn(paCopnGoods);
	}

	@Override
	public List<PaCopnGoodsVO> selectPaCopnGoodsInfoListMass(ParamMap paramMap) throws Exception {
		paramMap.put("modCase", "MODIFY");
		pacommonDAO.execSP_PA_MASS_GOODS_UPDATE(paramMap);
		return paCopnGoodsDAO.selectPaCopnGoodsInfoList(paramMap);
	}

	@Override
	public List<PaGoodsPriceVO> selectCopnPriceModifyMass(ParamMap paramMap) throws Exception {
		paramMap.put("modCase", "PRICE");
		pacommonDAO.execSP_PA_MASS_GOODS_UPDATE(paramMap);
		return paCopnGoodsDAO.selectCopnPriceModify(paramMap);
	}

	@Override
	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception {
		return paCopnGoodsDAO.updateMassTargetYnByEpCode(massMap);
	}	
	
	@Override
	public List<PaCopnGoodsUserAttri> selectPaCopnGoodsUserAttriList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnGoodsUserAttriList(paramMap);
	}

	@Override
	public List<PaCopnGoodsUserAttri> selectPaCopnGoodsUserSearchAttriList(ParamMap paramMap) throws Exception {
		return paCopnGoodsDAO.selectPaCopnGoodsUserSearchAttriList(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectDeleteGoodsSaleStatusList() throws Exception {
		return paCopnGoodsDAO.selectDeleteGoodsSaleStatusList();
	}

	@Override
	public int updateDeleteGoodsSaleStatus(HashMap<String, Object> goodsStatusMap) throws Exception {
		return paCopnGoodsDAO.updateDeleteGoodsSaleStatus(goodsStatusMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectDeleteGoodsList(HashMap<String, Object> goodsStatusMap) throws Exception {
		return paCopnGoodsDAO.selectDeleteGoodsList(goodsStatusMap);
	}

	@Override
	public int insertDeleteGoodsHistory(HashMap<String, String> cancelGoodsMap) throws Exception {
		return paCopnGoodsDAO.insertDeleteGoodsHistory(cancelGoodsMap);
	}

	@Override
	public int deleteTempDeleteGoodsList(HashMap<String, String> cancelGoodsMap) throws Exception {
		return paCopnGoodsDAO.deleteTempDeleteGoodsList(cancelGoodsMap);
	}

	@Override
	public int updateTempDeleteGoodsList(HashMap<String, String> cancelGoodsMap) throws Exception {
		return paCopnGoodsDAO.updateTempDeleteGoodsList(cancelGoodsMap);
	}
	
	// Async 쿠팡상품 삭제로직(1000개씩 처리)
	@Async("copnGoodsDelete")
	public CompletableFuture<CopnGoodsDeleteVO> asyncGoodsDelete(List<HashMap<String, String>> asyncGoodsDeleteList, ParamMap paramMap, HashMap<String, String> apiInfo) throws Exception {
		String goodsCode = "";
		int successCount = 0;
		int errorCount = 0;
		long startTime = System.currentTimeMillis();
		CopnGoodsDeleteVO result = new CopnGoodsDeleteVO();
		
		log.info("copnGoodsDelete 시작 - Thread: {}", Thread.currentThread().getName());
		for(HashMap<String, String> cancelGoodsMap : asyncGoodsDeleteList)  {
			try {
				JsonObject responseObj = null;
				ParamMap deleteProcParamMap = new ParamMap();
				HashMap<String,Object> resultMap = null; 
				
				goodsCode = ComUtil.objToStr(cancelGoodsMap.get("GOODS_CODE"));
				String sellerProductId	= ComUtil.objToStr(cancelGoodsMap.get("SELLER_PRODUCT_ID"));
				String paCode = ComUtil.objToStr(cancelGoodsMap.get("PA_CODE"));
				String paName = "";
				String resultCode = "";
				String resultMsg = "";
				
				if(paCode.equals(Constants.PA_COPN_BROAD_CODE)) {
					paName = Constants.PA_BROAD;
				} else {
					paName = Constants.PA_ONLINE;
				}
				log.info("쿠팡 상품삭제 API 호출 {}", Thread.currentThread().getName());
				responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(apiInfo.get("API_URL")
													.replaceAll("#sellerProductId#", sellerProductId)), "DELETE");
				
				log.info("쿠팡 상품삭제 API 호출완료", Thread.currentThread().getName());
				// 상품삭제 API 호출 이후 처리
				if(responseObj != null) {
					resultCode = responseObj.get("code").getAsString();
					resultMsg = responseObj.get("message").getAsString();
				} else {
					resultCode = "9999";
					resultCode = "responseObj == null";
				}
				
				paramMap.put("code",resultCode);
				paramMap.put("message",resultMsg);
				cancelGoodsMap.put("goodsCode", goodsCode);
				cancelGoodsMap.put("resultCode", resultCode);
				cancelGoodsMap.put("resultMsg", resultMsg);
				insertDeleteGoodsHistory(cancelGoodsMap);
				if(resultCode.equals("SUCCESS")) {
					log.info("쿠팡 상품삭제 성공 {} {}",resultMsg, Thread.currentThread().getName());
					deleteTempDeleteGoodsList(cancelGoodsMap); 
					deleteProcParamMap = new ParamMap();
					deleteProcParamMap.put("arg_pa_group_code", "05");
					deleteProcParamMap.put("arg_goods_code", goodsCode);
					deleteProcParamMap.put("arg_status", "D");
					resultMap = pacommonProcess.procPagoodsRetention(deleteProcParamMap);
					if(!resultMap.get("rtn_code").equals("0")) {
						log.warn(goodsCode + "[BO 쿠팡 상품 삭제 처리 실패]");
						errorCount++;
					} else {
						successCount++;
					}
				} else {
					log.info("쿠팡 상품삭제 실패 ", resultMsg, Thread.currentThread().getName());
					updateTempDeleteGoodsList(cancelGoodsMap);
					errorCount++;
				}
			} catch(Exception e) {
				String msg = "[goodsCode:" + goodsCode+"] " + e.getMessage();
				log.info(msg);
				paramMap.put("code", "9999");
				paramMap.put("message", "삭제시 에러 발생 : " + msg);
				
				cancelGoodsMap.put("goodsCode", goodsCode);
				cancelGoodsMap.put("resultCode", "ERROR");
				cancelGoodsMap.put("resultMsg", (msg.length()>255?msg.substring(0, 255):msg));
				insertDeleteGoodsHistory(cancelGoodsMap);
				errorCount++;
				continue;
			}
		}
		result.setSuccessCount(successCount);
		result.setErrorCount(errorCount);
		log.info("copnGoodsDelete 종료 - Thread: {} {}", Thread.currentThread().getName(), getExecutionTime(startTime));
		return CompletableFuture.completedFuture(result);
	}
	
	// 실행시간 구하는 메소드
	public String getExecutionTime(long startTime) {

		long execTime = System.currentTimeMillis() - startTime;
        long millis = execTime % 1000;	// 밀리초
        long seconds = (execTime / 1000) % 60; 	// 초
        long minutes = (execTime / 1000) / 60;	// 분
        
        String formattedTime = String.format("%d분 %d초 %d밀리초", minutes, seconds, millis);
        return formattedTime;
    }
}
