package com.cware.netshopping.panaver.v3.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.panaver.v3.process.PaNaverV3GoodsProcess;
import com.cware.netshopping.panaver.v3.service.PaNaverV3GoodsService;
import com.cware.netshopping.panaver.v3.domain.Bag;
import com.cware.netshopping.panaver.v3.domain.Books;
import com.cware.netshopping.panaver.v3.domain.CarArticles;
import com.cware.netshopping.panaver.v3.domain.Cosmetic;
import com.cware.netshopping.panaver.v3.domain.DietFood;
import com.cware.netshopping.panaver.v3.domain.DigitalContents;
import com.cware.netshopping.panaver.v3.domain.Etc;
import com.cware.netshopping.panaver.v3.domain.EtcService;
import com.cware.netshopping.panaver.v3.domain.FashionItems;
import com.cware.netshopping.panaver.v3.domain.Food;
import com.cware.netshopping.panaver.v3.domain.Furniture;
import com.cware.netshopping.panaver.v3.domain.GeneralFood;
import com.cware.netshopping.panaver.v3.domain.HomeAppliances;
import com.cware.netshopping.panaver.v3.domain.ImageAppliances;
import com.cware.netshopping.panaver.v3.domain.Jewellery;
import com.cware.netshopping.panaver.v3.domain.Kids;
import com.cware.netshopping.panaver.v3.domain.KitchenUtensils;
import com.cware.netshopping.panaver.v3.domain.MedicalAppliances;
import com.cware.netshopping.panaver.v3.domain.MicroElectronics;
import com.cware.netshopping.panaver.v3.domain.MusicalInstrument;
import com.cware.netshopping.panaver.v3.domain.Navigation;
import com.cware.netshopping.panaver.v3.domain.OfficeAppliances;
import com.cware.netshopping.panaver.v3.domain.OpticsAppliances;
import com.cware.netshopping.panaver.v3.domain.Wear;
import com.cware.netshopping.panaver.v3.domain.RentalEtc;
import com.cware.netshopping.panaver.v3.domain.SeasonAppliances;
import com.cware.netshopping.panaver.v3.domain.Shoes;
import com.cware.netshopping.panaver.v3.domain.SleepingGear;
import com.cware.netshopping.panaver.v3.domain.SportsEquipment;


@Service("panaver.v3.goods.paNaverV3GoodsService")
public class PaNaverV3GoodsServiceImpl extends AbstractService implements PaNaverV3GoodsService {
	
	@Resource(name = "panaver.v3.goods.paNaverV3GoodsProcess")
	private PaNaverV3GoodsProcess paNaverV3GoodsProcess;

	@Override
	public Wear selectWearSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectWearSummary(paramMap);
	}
	
	@Override
	public Shoes selectShoesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectShoesSummary(paramMap);
	}

	@Override
	public Bag selectBagSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectBagSummary(paramMap);
	}

	@Override
	public FashionItems selectFashionItemsSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectFashionItemsSummary(paramMap);
	}

	@Override
	public SleepingGear selectSleepingGearSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectSleepingGearSummary(paramMap);
	}

	@Override
	public Furniture selectFurnitureSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectFurnitureSummary(paramMap);
	}

	@Override
	public ImageAppliances selectImageAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectImageAppliancesSummary(paramMap);
	}

	@Override
	public HomeAppliances selectHomeAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectHomeAppliancesSummary(paramMap);
	}

	@Override
	public SeasonAppliances selectSeasonAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectSeasonAppliancesSummary(paramMap);
	}

	@Override
	public OfficeAppliances selectOfficeAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectOfficeAppliancesSummary(paramMap);
	}

	@Override
	public OpticsAppliances selectOpticsAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectOpticsAppliancesSummary(paramMap);
	}

	@Override
	public MicroElectronics selectMicroElectronicsSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectMicroElectronicsSummary(paramMap);
	}

	@Override
	public Navigation selectNavigationSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectNavigationSummary(paramMap);
	}

	@Override
	public CarArticles selectCarArticlesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectCarArticlesSummary(paramMap);
	}

	@Override
	public MedicalAppliances selectMedicalAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectMedicalAppliancesSummary(paramMap);
	}

	@Override
	public KitchenUtensils selectKitchenUtensilsSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectKitchenUtensilsSummary(paramMap);
	}

	@Override
	public Cosmetic selectCosmeticSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectCosmeticSummary(paramMap);
	}

	@Override
	public Jewellery selectJewellerySummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectJewellerySummary(paramMap);
	}

	@Override
	public Food selectFoodSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectFoodSummary(paramMap);
	}

	@Override
	public GeneralFood selectGeneralFoodSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectGeneralFoodSummary(paramMap);
	}

	@Override
	public DietFood selectDietFoodSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectDietFoodSummary(paramMap);
	}

	@Override
	public Kids selectKidsSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectKidsSummary(paramMap);
	}

	@Override
	public MusicalInstrument selectMusicalInstrumentSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectMusicalInstrumentSummary(paramMap);
	}

	@Override
	public SportsEquipment selectSportsEquipmentSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectSportsEquipmentSummary(paramMap);
	}

	@Override
	public Books selectBooksSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectBooksSummary(paramMap);
	}

	@Override
	public RentalEtc selectRentalEtcSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectRentalEtcSummary(paramMap);
	}

	@Override
	public DigitalContents selectDigitalContentsSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectDigitalContentsSummary(paramMap);
	}

	@Override
	public EtcService selectEtcServiceSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectEtcServiceSummary(paramMap);
	}

	@Override
	public Etc selectEtcSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsProcess.selectEtcSummary(paramMap);
	}
}