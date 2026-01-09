package com.cware.netshopping.panaver.goods.process.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.api.panaver.product.type.BagSummaryType;
import com.cware.api.panaver.product.type.BooksSummaryType;
import com.cware.api.panaver.product.type.CarArticlesSummaryType;
import com.cware.api.panaver.product.type.CosmeticSummaryType;
import com.cware.api.panaver.product.type.DietFoodSummaryType;
import com.cware.api.panaver.product.type.DigitalContentsSummaryType;
import com.cware.api.panaver.product.type.EtcServiceSummaryType;
import com.cware.api.panaver.product.type.EtcSummaryType;
import com.cware.api.panaver.product.type.FashionItemsSummaryType;
import com.cware.api.panaver.product.type.FoodSummaryType;
import com.cware.api.panaver.product.type.FurnitureSummaryType;
import com.cware.api.panaver.product.type.GeneralFoodSummaryType;
import com.cware.api.panaver.product.type.HomeAppliancesSummaryType;
import com.cware.api.panaver.product.type.ImageAppliancesSummaryType;
import com.cware.api.panaver.product.type.JewellerySummaryType;
import com.cware.api.panaver.product.type.KidsSummaryType;
import com.cware.api.panaver.product.type.KitchenUtensilsSummaryType;
import com.cware.api.panaver.product.type.MedicalAppliancesSummaryType;
import com.cware.api.panaver.product.type.MicroElectronicsSummaryType;
import com.cware.api.panaver.product.type.MusicalInstrumentSummaryType;
import com.cware.api.panaver.product.type.NavigationSummaryType;
import com.cware.api.panaver.product.type.OfficeAppliancesSummaryType;
import com.cware.api.panaver.product.type.OpticsAppliancesSummaryType;
import com.cware.api.panaver.product.type.PaCertificationVO;
import com.cware.api.panaver.product.type.PaDeliveryVO;
import com.cware.api.panaver.product.type.PaNaverGoodsVO;
import com.cware.api.panaver.product.type.RentalEtcSummaryType;
import com.cware.api.panaver.product.type.SeasonAppliancesSummaryType;
import com.cware.api.panaver.product.type.ShoesSummaryType;
import com.cware.api.panaver.product.type.SleepingGearSummaryType;
import com.cware.api.panaver.product.type.SportsEquipmentSummaryType;
import com.cware.api.panaver.product.type.WearSummaryType;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.PaNaverGoodsImageVO;
import com.cware.netshopping.domain.model.PaGoods;
import com.cware.netshopping.domain.model.PaGoodsAuthYnLog;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.PaSaleNoGoods;
import com.cware.netshopping.pacommon.common.process.PaCommonProcess;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.panaver.goods.process.PaNaverGoodsProcess;
import com.cware.netshopping.panaver.goods.repository.PaNaverGoodsDAO;

@Service("panaver.goods.paNaverGoodsProcess")
public class PaNaverGoodsProcessImpl extends AbstractService implements PaNaverGoodsProcess {
	
	@Resource(name = "pacommon.common.pacommonProcess")
    private PaCommonProcess pacommonProcess;
	
	@Resource(name = "panaver.goods.paNaverGoodsDAO")
	private PaNaverGoodsDAO paNaverGoodsDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pacommon.common.paCommonDAO")
    private PaCommonDAO pacommonDAO;	
   
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	
	@Override
	public PaGoodsImage selectPaGoodsImage(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectPaGoodsImage(paramMap);
	}

	@Override
	public int saveUploadPaNaverGoodsImage(PaNaverGoodsImageVO paNaverGoodsImageVO) throws Exception {
		int result = 0;
		
		if(paNaverGoodsImageVO.getNaverImageYn().equals("0")){
			result += paNaverGoodsDAO.insertPaNaverGoodsImage(paNaverGoodsImageVO);
			if (result < 0) {
				log.info("TPANAVERGOODSIMAGE insert fail");
				throw processException("msg.cannot_save", new String[] { "TPANAVERGOODSIMAGE INSERT" });
			}
		}else{
			result += paNaverGoodsDAO.updateUploadPaNaverGoodsImage(paNaverGoodsImageVO);
			if (result < 0) {
				log.info("TPANAVERGOODSIMAGE update fail");
				throw processException("msg.cannot_save", new String[] { "TPANAVERGOODSIMAGE UPDATE" });
			}
		}
		
		// UPDATE TRANS_TARGET_YN=0
		result += paNaverGoodsDAO.updateTransTargetYN(paNaverGoodsImageVO);;
		if (result < 0) {
			log.info("TPAGOODSIMAGE update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSIMAGE UPDATE" });
		}
		
		return result;
	}

	@Override
	public PaNaverGoodsImageVO selectPaNaverGoodsImage(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectPaNaverGoodsImage(paramMap);
	}

	@Override
	public PaNaverGoodsVO selectPaNaverGoods(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectPaNaverGoods(paramMap);
	}

	@Override
	public PaGoods selectPaGoodsDetail(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectPaGoodsDetail(paramMap);
	}

	@Override
	public WearSummaryType selectWearSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectWearSummary(paramMap);
	}

	@Override
	public ShoesSummaryType selectShoesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectShoesSummary(paramMap);
	}

	@Override
	public BagSummaryType selectBagSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectBagSummary(paramMap);
	}

	@Override
	public FashionItemsSummaryType selectFashionItemsSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectFashionItemsSummary(paramMap);
	}

	@Override
	public SleepingGearSummaryType selectSleepingGearSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectSleepingGearSummary(paramMap);
	}

	@Override
	public FurnitureSummaryType selectFurnitureSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectFurnitureSummary(paramMap);
	}

	@Override
	public ImageAppliancesSummaryType selectImageAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectImageAppliancesSummary(paramMap);
	}

	@Override
	public HomeAppliancesSummaryType selectHomeAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectHomeAppliancesSummary(paramMap);
	}

	@Override
	public SeasonAppliancesSummaryType selectSeasonAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectSeasonAppliancesSummary(paramMap);
	}

	@Override
	public OfficeAppliancesSummaryType selectOfficeAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectOfficeAppliancesSummary(paramMap);
	}

	@Override
	public OpticsAppliancesSummaryType selectOpticsAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectOpticsAppliancesSummary(paramMap);
	}

	@Override
	public MicroElectronicsSummaryType selectMicroElectronicsSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectMicroElectronicsSummary(paramMap);
	}

	@Override
	public NavigationSummaryType selectNavigationSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectNavigationSummary(paramMap);
	}

	@Override
	public CarArticlesSummaryType selectCarArticlesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectCarArticlesSummary(paramMap);
	}

	@Override
	public MedicalAppliancesSummaryType selectMedicalAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectMedicalAppliancesSummary(paramMap);
	}

	@Override
	public KitchenUtensilsSummaryType selectKitchenUtensilsSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectKitchenUtensilsSummary(paramMap);
	}

	@Override
	public CosmeticSummaryType selectCosmeticSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectCosmeticSummary(paramMap);
	}

	@Override
	public JewellerySummaryType selectJewellerySummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectJewellerySummary(paramMap);
	}

	@Override
	public FoodSummaryType selectFoodSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectFoodSummary(paramMap);
	}

	@Override
	public GeneralFoodSummaryType selectGeneralFoodSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectGeneralFoodSummary(paramMap);
	}

	@Override
	public DietFoodSummaryType selectDietFoodSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectDietFoodSummary(paramMap);
	}

	@Override
	public KidsSummaryType selectKidsSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectKidsSummary(paramMap);
	}

	@Override
	public MusicalInstrumentSummaryType selectMusicalInstrumentSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectMusicalInstrumentSummary(paramMap);
	}

	@Override
	public SportsEquipmentSummaryType selectSportsEquipmentSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectSportsEquipmentSummary(paramMap);
	}

	@Override
	public BooksSummaryType selectBooksSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectBooksSummary(paramMap);
	}

	@Override
	public RentalEtcSummaryType selectRentalEtcSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectRentalEtcSummary(paramMap);
	}

	@Override
	public DigitalContentsSummaryType selectDigitalContentsSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectDigitalContentsSummary(paramMap);
	}

	@Override
	public EtcServiceSummaryType selectEtcServiceSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectEtcServiceSummary(paramMap);
	}

	@Override
	public EtcSummaryType selectEtcSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectEtcSummary(paramMap);
	}

	@Override
	public List<PaNaverGoodsVO> selectPaNaverGoodsInfoList(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectPaNaverGoodsInfoList(paramMap);
	}
	
	@Override
	public PaGoodsPriceVO selectGoodsPriceInfoByGoodsCode(String goodsCode) throws Exception {
		return paNaverGoodsDAO.selectGoodsPriceInfoByGoodsCode(goodsCode);
	}
	@Override
	public int insertPaNaverGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception {
		return paCommonDAO.insertPaGoodsTransLog(paGoodsTransLog);
	}

	@Override
	public String savePaNaverGoods(PaNaverGoodsVO paNaverGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			// 1이상일 경우 수정도중 MODIFY 변동 , 0일경우 변동없음
			executedRtn = paNaverGoodsDAO.selectPaNaverGoodsModifyCheck(paNaverGoods);
			//MODIFY_DATE 변동 없을경우 TARGET 0 / 아닌경우 수정대상 재포함
			if( executedRtn == 0 ) {
				executedRtn = paNaverGoodsDAO.updatePaNaverGoods(paNaverGoods);
				if (executedRtn < 0) {
					log.info("tpaNavergoods update fail");
					throw processException("msg.cannot_save", new String[] { "TPANAVERGOODS UPDATE" });
				}
			
				executedRtn = paNaverGoodsDAO.updatePaNaverGoodsImage(paNaverGoods);
				if (executedRtn < 0) {
					log.info("tpagoodsimage update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSIMAGE UPDATE" });
				}
				
				executedRtn = paNaverGoodsDAO.updatePaNaverCustShipCost(paNaverGoods);
				if (executedRtn < 0) {
					log.info("tpacustshipcost update fail");
					throw processException("msg.cannot_save", new String[] { "TPACUSTSHIPCOST UPDATE" });
				}
				
				executedRtn = paNaverGoodsDAO.updatePaNaverGoodsOffer(paNaverGoods);
				if (executedRtn < 0) {
					log.info("tpaNavergoodsoffer update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSOFFER UPDATE" });
				}
			}
			
			executedRtn = paNaverGoodsDAO.updatePaGoodsTarget(paNaverGoods);
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
	public String selectPaNaverSaleGb(String goodsCode) throws Exception {
		return paCommonDAO.selectGoodsSaleGb(goodsCode);
	}

	@Override
	public List<PaGoodsdtMapping> selectPaNaverGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectPaNaverGoodsdtInfoList(paramMap);
	}

	@Override
	public String savePaNaverGoodsFail(PaNaverGoodsVO paNaverGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		executedRtn =  paNaverGoodsDAO.updatePaNaverGoodsFail(paNaverGoods);
		if (executedRtn < 0) {
			log.info("tpanavergoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPANAVERGOODS UPDATE" });
		}
		return rtnMsg;	
	}

	@Override
	public List<PaGoodsdtMapping> selectPaGoodsdt(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectPaGoodsdt(paramMap);
	}

	@Override
	public int savePaNaverOption(PaGoodsdtMapping paGoodsdtMapping) throws Exception {
		int result = 0;
		result = paNaverGoodsDAO.updatePaNaverOption(paGoodsdtMapping);
		if (result < 0) {
			log.info("TPAGOODSDTMAPPING update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
		}
		return result;
	}

	@Override
	public String savePaNaverGoodsSell(PaNaverGoodsVO paNaverGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;		
		
		executedRtn = paNaverGoodsDAO.updatePaNaverGoods(paNaverGoods);
		if (executedRtn < 0) {
			log.info("tpanavergoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPANAVERGOODS UPDATE" });
		}
		
		return rtnMsg;
	}

	@Override
	public String saveStopMonitering(PaNaverGoodsVO paNaverGoods) throws Exception {
		int executedRtn = 0;
		executedRtn = paNaverGoodsDAO.insertStopMonitering(paNaverGoods);
		if (executedRtn != 1) {
			log.error("STOP TPAMONITERING INSERT ERROR");
		}
		return Constants.SAVE_SUCCESS;
	}

	@Override
	public int selectPaGoodsdtMapping(ParamMap paramMap) throws Exception {
		int cnt = 0;
		
		cnt = paNaverGoodsDAO.selectPaGoodsdtMapping(paramMap);
		if (cnt < 0) {
			log.info("TPAGOODSDTMAPPING count fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING COUNT" });
		}
		return cnt;
	}

	@Override
	public int saveSinglePaGoodsdtMapping(ParamMap paramMap)  throws Exception {
		int cnt = 0;
		
		cnt = paNaverGoodsDAO.updateSinglePaGoodsdtMapping(paramMap);
		if (cnt < 0) {
			log.info("single TPAGOODSDTMAPPING update fail");
			throw processException("msg.cannot_save", new String[] { "SINGLE TPAGOODSDTMAPPING UPDATE ONE" });
		}
		return cnt;
	}

	@Override
	public int updatePaNaverOptionFail(ParamMap paramMap) throws Exception {
		int cnt = 0;
		
		cnt = paNaverGoodsDAO.updatePaNaverOptionFail(paramMap);
		if (cnt < 0) {
			log.info("TPANAVERGOODS update fail");
			throw processException("msg.cannot_save", new String[] { "TPANAVERGOODS UPDATE RETURN_NOTE" });
		}
		return cnt;
	}

	@Override
	public String selectPaOfferType(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectPaOfferType(paramMap);
	}

	@Override
	public List<PaNaverGoodsVO> selectGoodsSellModifyAllObjectList(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectGoodsSellModifyAllObjectList(paramMap);
	}

	@Override
	public int saveTransSaleYN(ParamMap paramMap) throws Exception {
		int cnt = 0;
		
		cnt = paNaverGoodsDAO.updateTransSaleYN(paramMap);
		if (cnt < 0) {
			log.info("TPANAVERGOODS - transSaleYN update fail");
			throw processException("msg.cannot_save", new String[] { "TPANAVERGOODS TRANS_SALE_YN UPDATE RETURN_NOTE" });
		}
		return cnt;
	}

	@Override
	public int selectPaNaverGoodsDescCnt998(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectPaNaverGoodsDescCnt998(paramMap);
	}
	@Override
	public String selectPaNaverGoodsDesc998(ParamMap paramMap) throws Exception {
		String desc1 = paNaverGoodsDAO.selectPaNaverGoodsDesc998FR(paramMap);
	    String desc2 = paNaverGoodsDAO.selectPaNaverGoodsDesc998TO(paramMap);
	    String desc = desc1+desc2;
		return desc;
	}
	@Override
	public int selectPaNaverGoodsDescCnt999(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectPaNaverGoodsDescCnt999(paramMap);
	}
	@Override
	public String selectPaNaverGoodsDesc999(ParamMap paramMap) throws Exception {
		String desc1 = paNaverGoodsDAO.selectPaNaverGoodsDesc999FR(paramMap);
	    String desc2 = paNaverGoodsDAO.selectPaNaverGoodsDesc999TO(paramMap);
	    String desc = desc1+desc2;
		return desc;
	}

	@Override
	public int updatePaGoodsPrice(ParamMap paramMap, List<PaPromoTarget> paPromoTargetList) throws Exception {
		int cnt = 0;

		cnt = paNaverGoodsDAO.updatePaGoodsPrice(paramMap);
		if (cnt < 0) {
			log.info("TPAGOODSPRICE - TRANS_ID, TRANS_DATE update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICE - TRANS_ID, TRANS_DATE UPDATE" });
		}
		// 프로모션 trans_date
		
		for(PaPromoTarget paPromoTarget : paPromoTargetList) {	
			
			if(paPromoTarget.getDoCost() > 0) {
	            cnt = paNaverGoodsDAO.updatePaPromoTargetCalc(paPromoTarget);
	            if (cnt < 0) {
	                log.info("TPAPROMOTARGET - update fail");
	                throw processException("msg.cannot_save", new String[] { "updatePaPromoTargetCalc" });
	            }
			}

            if (paPromoTarget.getTransDate() == null) {
				cnt = paNaverGoodsDAO.updatePaPromoTarget(paPromoTarget);
				if (cnt < 0) {
					log.info("TPAPROMOTARGET - update fail");
					throw processException("msg.cannot_save", new String[] { "TPAPROMOTARGET - UPDATE" });
				}
			}else {
				cnt = paNaverGoodsDAO.insertNewPaPromoTarget(paPromoTarget);
				if (cnt < 0) {
					log.info("TPAPROMOTARGET - insert fail");
					throw processException("msg.cannot_save", new String[] { "TPAPROMOTARGET - INSERT" });
				}
			}
		}
		
		return cnt;
	}

	@Override
	public String selectModelInputYN(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectModelInputYN(paramMap);
	}

	@Override
	public List<PaCertificationVO> selectPaCertificationList(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectPaCertificationList(paramMap);
	}

	@Override
	public PaDeliveryVO selectPaDelivery(ParamMap paramMap) throws Exception {
		return paNaverGoodsDAO.selectPaDelivery(paramMap);
	}

	@Override
	public String updatePaGoodsModifyFail(PaNaverGoodsVO paNaverGoods) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		PaSaleNoGoods paSaleNoGoods = null;
		
		executedRtn = paNaverGoodsDAO.updatePaGoodsModifyFail(paNaverGoods);
		if (executedRtn < 0) {
			log.error("tpaNavergoods update fail");
			throw processException("msg.cannot_save", new String[] { "TPANAVERGOODS UPDATE" });
		}
		
		paSaleNoGoods = new PaSaleNoGoods();
		paSaleNoGoods.setPaGroupCode(paNaverGoods.getPaGroupCode());
		paSaleNoGoods.setGoodsCode(paNaverGoods.getGoodsCode());
		paSaleNoGoods.setPaCode(paNaverGoods.getPaCode());
		paSaleNoGoods.setSeqNo(systemService.getMaxNo("TPASALENOGOODS", "SEQ_NO", "GOODS_CODE = '" + paNaverGoods.getGoodsCode() + "' AND PA_CODE = '"+paNaverGoods.getPaCode()+"'", 3));
		paSaleNoGoods.setProductNo(paNaverGoods.getProductId());
		paSaleNoGoods.setPaSaleGb("30");
		paSaleNoGoods.setInsertId("PANAVER");
		paSaleNoGoods.setNote(paNaverGoods.getReturnNote());
		
		executedRtn = paNaverGoodsDAO.insertPaSaleNoGoods(paSaleNoGoods);
		if (executedRtn < 0) {
			log.error("tpasalenogoods insert fail");
			throw processException("msg.cannot_save", new String[] { "TPASALENOGOODS INSERT" });
		}
		
		paNaverGoods.setModifyId("PANAVER");
		executedRtn = paNaverGoodsDAO.updatePaGoodsTargetForAuthYn(paNaverGoods);
		if (executedRtn < 0) {
			log.info("Naver tpagoodstarget update fail for authyn");
		}
		
		PaGoodsAuthYnLog paGoodsAuthYnLog = new PaGoodsAuthYnLog();
		paGoodsAuthYnLog.setPaGroupCode(paNaverGoods.getPaGroupCode());
		paGoodsAuthYnLog.setPaCode(paNaverGoods.getPaCode());
		paGoodsAuthYnLog.setGoodsCode(paNaverGoods.getGoodsCode());
		paGoodsAuthYnLog.setSeqNo(systemService.getMaxNo("TPAGOODSAUTHYNLOG", "SEQ_NO", 
			   "GOODS_CODE = '" + paNaverGoods.getGoodsCode() + "'"+
			   " AND PA_CODE = '"+paNaverGoods.getPaCode()+"'"+
			   " AND PA_GROUP_CODE = '"+paNaverGoods.getPaGroupCode()+"'"
			   , 3));
		paGoodsAuthYnLog.setAutoYn("0");
		paGoodsAuthYnLog.setInsertId("PANAVER");
		paGoodsAuthYnLog.setNote("NAVER 반려");
		
		executedRtn = paNaverGoodsDAO.insertPaGoodsAuthYnLog(paGoodsAuthYnLog);
		if (executedRtn < 0) {
			log.info("Naver tpaGoodsAuthYnLog insert fail");
		}
		
		return rtnMsg;
	}
	
	@Override
	public List<HashMap<String,Object>> selectPaPromoTarget(ParamMap paramMap) throws Exception{
		List<HashMap<String, Object>> promotionList = new ArrayList<HashMap<String, Object>>();
		//1) 높은 가격 기준으로 프로모션을 조회한다
		paramMap.put("alcoutPromoYn", "1");
		List<HashMap<String, Object>> promotionList1 = paNaverGoodsDAO.selectPaPromoTarget(paramMap);
		
		//2) 가져온 데이터에 대한 마진률 계산 
		checkPromoMargin(promotionList1);
		
		//3) 프로모션 결과가 없으면 가장 최신 만료된(D) 데이터를 찾아서 반납한다 -> why? 프로모션 연동 여부를 기록하기 위해 
		if(promotionList1.size() < 1) {
			promotionList1	= paNaverGoodsDAO.selectPaPromoDeleteTarget(paramMap);	
		}
		
		promotionList.addAll(promotionList1);
		return promotionList;
	}
	
	private void checkPromoMargin(List<HashMap<String,Object>> promotionList1 ) throws Exception {
		if(promotionList1 == null ) return;
		if(promotionList1.size() < 1) return;
		
		int marginCheckExceptYn     = 0;
		double margin 	  	   	    = 0;
		double limitMargin	   	    = 0;
		String sMode	   	   	    = "1";
		double outOwnCost 		 	= 0;
		ParamMap paramMap	   	    = new ParamMap();
		HashMap<String,Object> promoTarget1 	= null;
		ParamMap minMarginMap		= new ParamMap();
		
		minMarginMap.put("paGroupCode", promotionList1.get(0).get("PA_GROUP_CODE"));
		minMarginMap.put("paCode", promotionList1.get(0).get("PA_CODE"));

//		limitMargin = Double.parseDouble( ComUtil.NVL(systemDAO.getVal("PA_LIMIT_MARGIN") , "-99"));
		limitMargin  = Double.parseDouble(ComUtil.NVL(pacommonDAO.selectPaMinMarginRate(minMarginMap), "-99")); // 2022-09-20 TCONFIG -> TPAPROMOMINMARGINRATE 로 관리
		if(limitMargin == -99) return;
			
		outOwnCost  = Double.parseDouble( String.valueOf( ((HashMap<String,Object>)promotionList1.get(0)).get("DO_OWN_COST") ));
		promoTarget1 = promotionList1.get(0);
		
		//1) 마진률 CHECK
		paramMap.put("goodsCode"	, promoTarget1.get("GOODS_CODE"));
		paramMap.put("outOwnPrice"	, outOwnCost);
		paramMap.put("mode"			, sMode);
		paramMap.put("paCode"	  	, promoTarget1.get("PA_CODE"));
		paramMap.put("paGroupCode"	, "04");
		
		// 마진율 체크 제외 상품 확인
		marginCheckExceptYn = paCommonDAO.selectPromoMarginExceptYn(paramMap);
		if(marginCheckExceptYn > 0) return;
		
		margin 	 = pacommonDAO.selectGetPromoMargin(paramMap); 
		if(margin >= limitMargin) return;
		
		//2) 허용된 범위 안에서 최대 마진률 promotionList 세팅
		getMaxMarginPromo   (promotionList1,  paramMap);
		
		//3) Except_REASON UPDATE
		setExceptReason		( promotionList1 );
	}

	
	private void getMaxMarginPromo(List<HashMap<String,Object>> promotionList1, ParamMap paramMap) throws Exception {
		if(promotionList1.size() < 1) return;
		
		String promoNo = pacommonDAO.selectGetMaxPromoTarget(paramMap);  //FUN_GET_MAX_PAPROMOTARGET
		if(promoNo == null || promoNo.equals("")) {
			promotionList1.get(0).put("DO_COST", 0);
			return;
		}
		
		paramMap.put("alcoutPromoYn", "1");
		paramMap.put("promoNo"		, promoNo);
		List<HashMap<String,Object>> promotionList = paNaverGoodsDAO.selectPaPromoTarget(paramMap);
		
		if(promotionList1.size() > 0)	promotionList1.remove(0);
		promotionList1.add(promotionList.get(0));
	}

	private void setExceptReason(List<HashMap<String,Object>> promotionList1) {
		ParamMap paramMap 	= new ParamMap();
		
		if( promotionList1 == null ) return; 
		if( promotionList1.size() < 1 ) return; 
		if( Double.parseDouble( String.valueOf(promotionList1.get(0).get("DO_COST"))) < 1) return;
		
		paramMap.put("goodsCode"		, promotionList1.get(0).get("GOODS_CODE"));
		paramMap.put("paCode"			, promotionList1.get(0).get("PA_CODE"));
		paramMap.put("limitMarginAmt" 	, Double.parseDouble(String.valueOf(promotionList1.get(0).get("DO_OWN_COST"))));
		paramMap.put("paGroupCode"		, "");
		pacommonProcess.setExceptReason4OnePromotion(paramMap);	
	}
}