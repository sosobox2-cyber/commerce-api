package com.cware.netshopping.panaver.goods.service.impl;

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
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.PaNaverGoodsImageVO;
import com.cware.netshopping.domain.model.PaGoods;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.panaver.goods.process.PaNaverGoodsProcess;
import com.cware.netshopping.panaver.goods.service.PaNaverGoodsService;

@Service("panaver.goods.paNaverGoodsService")
public class PaNaverGoodsServiceImpl extends AbstractService implements  PaNaverGoodsService {
	
	@Resource(name = "panaver.goods.paNaverGoodsProcess")
	private PaNaverGoodsProcess paNaverGoodsProcess;


	@Override
	public PaGoodsImage selectPaGoodsImage(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaGoodsImage(paramMap);
	}

	@Override
	public int savePaNaverGoodsImageTx(PaNaverGoodsImageVO paNaverGoodsImageVO) throws Exception {
		return paNaverGoodsProcess.saveUploadPaNaverGoodsImage(paNaverGoodsImageVO);
	}
	
	@Override
	public PaNaverGoodsImageVO selectPaNaverGoodsImage(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaNaverGoodsImage(paramMap);
	}

	@Override
	public PaNaverGoodsVO selectPaNaverGoods(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaNaverGoods(paramMap);
	}

	@Override
	public PaGoods selectPaGoodsDetail(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaGoodsDetail(paramMap);
	}

	@Override
	public WearSummaryType selectWearSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectWearSummary(paramMap);
	}

	@Override
	public ShoesSummaryType selectShoesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectShoesSummary(paramMap);
	}

	@Override
	public BagSummaryType selectBagSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectBagSummary(paramMap);
	}

	@Override
	public FashionItemsSummaryType selectFashionItemsSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectFashionItemsSummary(paramMap);
	}

	@Override
	public SleepingGearSummaryType selectSleepingGearSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectSleepingGearSummary(paramMap);
	}

	@Override
	public FurnitureSummaryType selectFurnitureSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectFurnitureSummary(paramMap);
	}

	@Override
	public ImageAppliancesSummaryType selectImageAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectImageAppliancesSummary(paramMap);
	}

	@Override
	public HomeAppliancesSummaryType selectHomeAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectHomeAppliancesSummary(paramMap);
	}

	@Override
	public SeasonAppliancesSummaryType selectSeasonAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectSeasonAppliancesSummary(paramMap);
	}

	@Override
	public OfficeAppliancesSummaryType selectOfficeAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectOfficeAppliancesSummary(paramMap);
	}

	@Override
	public OpticsAppliancesSummaryType selectOpticsAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectOpticsAppliancesSummary(paramMap);
	}

	@Override
	public MicroElectronicsSummaryType selectMicroElectronicsSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectMicroElectronicsSummary(paramMap);
	}

	@Override
	public NavigationSummaryType selectNavigationSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectNavigationSummary(paramMap);
	}

	@Override
	public CarArticlesSummaryType selectCarArticlesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectCarArticlesSummary(paramMap);
	}

	@Override
	public MedicalAppliancesSummaryType selectMedicalAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectMedicalAppliancesSummary(paramMap);
	}

	@Override
	public KitchenUtensilsSummaryType selectKitchenUtensilsSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectKitchenUtensilsSummary(paramMap);
	}

	@Override
	public CosmeticSummaryType selectCosmeticSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectCosmeticSummary(paramMap);
	}

	@Override
	public JewellerySummaryType selectJewellerySummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectJewellerySummary(paramMap);
	}

	@Override
	public FoodSummaryType selectFoodSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectFoodSummary(paramMap);
	}

	@Override
	public GeneralFoodSummaryType selectGeneralFoodSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectGeneralFoodSummary(paramMap);
	}

	@Override
	public DietFoodSummaryType selectDietFoodSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectDietFoodSummary(paramMap);
	}

	@Override
	public KidsSummaryType selectKidsSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectKidsSummary(paramMap);
	}

	@Override
	public MusicalInstrumentSummaryType selectMusicalInstrumentSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectMusicalInstrumentSummary(paramMap);
	}

	@Override
	public SportsEquipmentSummaryType selectSportsEquipmentSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectSportsEquipmentSummary(paramMap);
	}

	@Override
	public BooksSummaryType selectBooksSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectBooksSummary(paramMap);
	}

	@Override
	public RentalEtcSummaryType selectRentalEtcSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectRentalEtcSummary(paramMap);
	}

	@Override
	public DigitalContentsSummaryType selectDigitalContentsSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectDigitalContentsSummary(paramMap);
	}

	@Override
	public EtcServiceSummaryType selectEtcServiceSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectEtcServiceSummary(paramMap);
	}

	@Override
	public EtcSummaryType selectEtcSummary(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectEtcSummary(paramMap);
	}

	@Override
	public List<PaNaverGoodsVO> selectPaNaverGoodsInfoList(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaNaverGoodsInfoList(paramMap);
	}
	
	@Override
	public PaGoodsPriceVO selectGoodsPriceInfoByGoodsCode(String goodsCode) throws Exception {
		return paNaverGoodsProcess.selectGoodsPriceInfoByGoodsCode(goodsCode);
	}
	@Override
	public int insertPaNaverGoodsTransLogTx(PaGoodsTransLog paGoodsTransLog) throws Exception  {
		return paNaverGoodsProcess.insertPaNaverGoodsTransLog(paGoodsTransLog);		
	}

	@Override
	public String savePaNaverGoodsTx(PaNaverGoodsVO paNaverGoods) throws Exception {
		return paNaverGoodsProcess.savePaNaverGoods(paNaverGoods);
	}

	@Override
	public String selectPaNaverSaleGb(String goodsCode) throws Exception {
		return paNaverGoodsProcess.selectPaNaverSaleGb(goodsCode);
		}

	@Override
	public List<PaGoodsdtMapping> selectPaNaverGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaNaverGoodsdtInfoList(paramMap);
		}

	@Override
	public String savePaNaverGoodsFailTx(PaNaverGoodsVO paNaverGoods) throws Exception {
		return paNaverGoodsProcess.savePaNaverGoodsFail(paNaverGoods);
	}

	@Override
	public List<PaGoodsdtMapping> selectPaGoodsdt(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaGoodsdt(paramMap);
	}

	@Override
	public int savePaNaverOptionTx(PaGoodsdtMapping paGoodsdtMapping) throws Exception {
		return paNaverGoodsProcess.savePaNaverOption(paGoodsdtMapping);
	}

	@Override
	public String savePaNaverGoodsSellTx(PaNaverGoodsVO paNaverGoods) throws Exception {
		return paNaverGoodsProcess.savePaNaverGoodsSell(paNaverGoods);
	}

	@Override
	public String saveStopMonitering(PaNaverGoodsVO paNaverGoods) throws Exception {
		return paNaverGoodsProcess.saveStopMonitering(paNaverGoods);
	}

	@Override
	public int selectPaGoodsdtMapping(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaGoodsdtMapping(paramMap);
	}

	@Override
	public int saveSinglePaGoodsdtMappingTx(ParamMap paramMap)  throws Exception {
		return paNaverGoodsProcess.saveSinglePaGoodsdtMapping(paramMap);
	}

	@Override
	public int updatePaNaverOptionFailTx(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.updatePaNaverOptionFail(paramMap);
	}

	@Override
	public String selectPaOfferType(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaOfferType(paramMap);
	}

	@Override
	public List<PaNaverGoodsVO> selectGoodsSellModifyAllObjectList(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectGoodsSellModifyAllObjectList(paramMap);
	}

	@Override
	public int saveTransSaleYNTx(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.saveTransSaleYN(paramMap);
	}

	@Override
	public int selectPaNaverGoodsDescCnt998(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaNaverGoodsDescCnt998(paramMap);
	}
	@Override
	public String selectPaNaverGoodsDesc998(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaNaverGoodsDesc998(paramMap);
	}
	@Override
	public int selectPaNaverGoodsDescCnt999(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaNaverGoodsDescCnt999(paramMap);
	}
	@Override
	public String selectPaNaverGoodsDesc999(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaNaverGoodsDesc999(paramMap);
	}

	@Override
	public int savePaGoodsPriceTx(ParamMap paramMap, List<PaPromoTarget> paPromoTargetList) throws Exception {
		return paNaverGoodsProcess.updatePaGoodsPrice(paramMap, paPromoTargetList);
	}

	@Override
	public String selectModelInputYN(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectModelInputYN(paramMap);
	}

	@Override
	public List<PaCertificationVO> selectPaCertificationList(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaCertificationList(paramMap);
	}

	@Override
	public PaDeliveryVO selectPaDelivery(ParamMap paramMap) throws Exception {
		return paNaverGoodsProcess.selectPaDelivery(paramMap);
	}

	@Override
	public String savePaGoodsModifyFailTx(PaNaverGoodsVO paNaverGoods) throws Exception {
		return paNaverGoodsProcess.updatePaGoodsModifyFail(paNaverGoods);
	}
	
	@Override
	public List<HashMap<String,Object>> selectPaPromoTarget(ParamMap paramMap) throws Exception{
		return paNaverGoodsProcess.selectPaPromoTarget(paramMap);
	}
}