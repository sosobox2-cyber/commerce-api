package com.cware.netshopping.panaver.v3.process.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.panaver.v3.domain.Shoes;
import com.cware.netshopping.panaver.v3.domain.Wear;
import com.cware.netshopping.panaver.v3.process.PaNaverV3GoodsProcess;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3GoodsDAO;

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
import com.cware.netshopping.panaver.v3.domain.RentalEtc;
import com.cware.netshopping.panaver.v3.domain.SeasonAppliances;
import com.cware.netshopping.panaver.v3.domain.SleepingGear;
import com.cware.netshopping.panaver.v3.domain.SportsEquipment;

@Service("panaver.v3.goods.paNaverV3GoodsProcess")
public class PaNaverV3GoodsProcessImpl extends AbstractService implements PaNaverV3GoodsProcess {
	
	@Resource(name = "panaver.v3.goods.paNaverV3GoodsDAO")
	private PaNaverV3GoodsDAO paNaverV3GoodsDAO;
		
	@Override
	public Wear selectWearSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectWearSummary(paramMap);
	}
	
	@Override
	public Shoes selectShoesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectShoesSummary(paramMap);
	}
	
	@Override
	public Bag selectBagSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectBagSummary(paramMap);
	}

	@Override
	public FashionItems selectFashionItemsSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectFashionItemsSummary(paramMap);
	}

	@Override
	public SleepingGear selectSleepingGearSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectSleepingGearSummary(paramMap);
	}

	@Override
	public Furniture selectFurnitureSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectFurnitureSummary(paramMap);
	}

	@Override
	public ImageAppliances selectImageAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectImageAppliancesSummary(paramMap);
	}

	@Override
	public HomeAppliances selectHomeAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectHomeAppliancesSummary(paramMap);
	}

	@Override
	public SeasonAppliances selectSeasonAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectSeasonAppliancesSummary(paramMap);
	}

	@Override
	public OfficeAppliances selectOfficeAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectOfficeAppliancesSummary(paramMap);
	}

	@Override
	public OpticsAppliances selectOpticsAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectOpticsAppliancesSummary(paramMap);
	}

	@Override
	public MicroElectronics selectMicroElectronicsSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectMicroElectronicsSummary(paramMap);
	}

	@Override
	public Navigation selectNavigationSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectNavigationSummary(paramMap);
	}

	@Override
	public CarArticles selectCarArticlesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectCarArticlesSummary(paramMap);
	}

	@Override
	public MedicalAppliances selectMedicalAppliancesSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectMedicalAppliancesSummary(paramMap);
	}

	@Override
	public KitchenUtensils selectKitchenUtensilsSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectKitchenUtensilsSummary(paramMap);
	}

	@Override
	public Cosmetic selectCosmeticSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectCosmeticSummary(paramMap);
	}

	@Override
	public Jewellery selectJewellerySummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectJewellerySummary(paramMap);
	}

	@Override
	public Food selectFoodSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectFoodSummary(paramMap);
	}

	@Override
	public GeneralFood selectGeneralFoodSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectGeneralFoodSummary(paramMap);
	}

	@Override
	public DietFood selectDietFoodSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectDietFoodSummary(paramMap);
	}

	@Override
	public Kids selectKidsSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectKidsSummary(paramMap);
	}

	@Override
	public MusicalInstrument selectMusicalInstrumentSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectMusicalInstrumentSummary(paramMap);
	}

	@Override
	public SportsEquipment selectSportsEquipmentSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectSportsEquipmentSummary(paramMap);
	}

	@Override
	public Books selectBooksSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectBooksSummary(paramMap);
	}

	@Override
	public RentalEtc selectRentalEtcSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectRentalEtcSummary(paramMap);
	}

	@Override
	public DigitalContents selectDigitalContentsSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectDigitalContentsSummary(paramMap);
	}

	@Override
	public EtcService selectEtcServiceSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectEtcServiceSummary(paramMap);
	}

	@Override
	public Etc selectEtcSummary(ParamMap paramMap) throws Exception {
		return paNaverV3GoodsDAO.selectEtcSummary(paramMap);
	}

}