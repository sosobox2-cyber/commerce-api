package com.cware.netshopping.panaver.v3.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cware.api.panaver.product.type.PaNaverGoodsVO;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaGoodsImage;
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
import com.cware.netshopping.panaver.v3.domain.Shoes;
import com.cware.netshopping.panaver.v3.domain.SleepingGear;
import com.cware.netshopping.panaver.v3.domain.SportsEquipment;
import com.cware.netshopping.panaver.v3.domain.Wear;

@Repository("panaver.v3.goods.paNaverV3GoodsDAO")
public class PaNaverV3GoodsDAO extends AbstractPaDAO{

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 01.의류) 조회
	 * @param ParamMap
	 * @return Wear
	 * @throws Exception
	 */
	public Wear selectWearSummary(ParamMap paramMap) throws Exception {
		return (Wear) selectByPk("panaver.v3.goods.selectWearSummary", paramMap.get());
	}
	
	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 02.신발) 조회
	 * @param ParamMap
	 * @return Shoes
	 * @throws Exception
	 */
	public Shoes selectShoesSummary(ParamMap paramMap) throws Exception {
		return (Shoes) selectByPk("panaver.v3.goods.selectShoesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 03.가방) 조회
	 * @param ParamMap
	 * @return Bag
	 * @throws Exception
	 */
	public Bag selectBagSummary(ParamMap paramMap) throws Exception{
		return (Bag) selectByPk("panaver.v3.goods.selectBagSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 04.패션잡화) 조회
	 * @param ParamMap
	 * @return FashionItems
	 * @throws Exception
	 */
	public FashionItems selectFashionItemsSummary(ParamMap paramMap) throws Exception{
		return (FashionItems) selectByPk("panaver.v3.goods.selectFashionItemsSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 05.침구류/커튼) 조회
	 * @param ParamMap
	 * @return SleepingGear
	 * @throws Exception
	 */
	public SleepingGear selectSleepingGearSummary(ParamMap paramMap) throws Exception{
		return (SleepingGear) selectByPk("panaver.v3.goods.selectSleepingGearSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 06.가구) 조회
	 * @param ParamMap
	 * @return Furniture
	 * @throws Exception
	 */
	public Furniture selectFurnitureSummary(ParamMap paramMap) throws Exception{
		return (Furniture) selectByPk("panaver.v3.goods.selectFurnitureSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 07.영상가전) 조회
	 * @param ParamMap
	 * @return ImageAppliances
	 * @throws Exception
	 */
	public ImageAppliances selectImageAppliancesSummary(ParamMap paramMap) throws Exception{
		return (ImageAppliances) selectByPk("panaver.v3.goods.selectImageAppliancesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 08.가정용 전기제품) 조회
	 * @param ParamMap
	 * @return HomeAppliances
	 * @throws Exception
	 */
	public HomeAppliances selectHomeAppliancesSummary(ParamMap paramMap) throws Exception{
		return (HomeAppliances) selectByPk("panaver.v3.goods.selectHomeAppliancesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 09.계절가전) 조회
	 * @param ParamMap
	 * @return SeasonAppliances
	 * @throws Exception
	 */
	public SeasonAppliances selectSeasonAppliancesSummary(ParamMap paramMap) throws Exception{
		return (SeasonAppliances) selectByPk("panaver.v3.goods.selectSeasonAppliancesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 10.사무용기기) 조회
	 * @param ParamMap
	 * @return OfficeAppliances
	 * @throws Exception
	 */
	public OfficeAppliances selectOfficeAppliancesSummary(ParamMap paramMap) throws Exception{
		return (OfficeAppliances) selectByPk("panaver.v3.goods.selectOfficeAppliancesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 11.광학기기) 조회
	 * @param ParamMap
	 * @return OpticsAppliances
	 * @throws Exception
	 */
	public OpticsAppliances selectOpticsAppliancesSummary(ParamMap paramMap) throws Exception{
		return (OpticsAppliances) selectByPk("panaver.v3.goods.selectOpticsAppliancesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 12.소형전자) 조회
	 * @param ParamMap
	 * @return MicroElectronics
	 * @throws Exception
	 */
	public MicroElectronics selectMicroElectronicsSummary(ParamMap paramMap) throws Exception{
		return (MicroElectronics) selectByPk("panaver.v3.goods.selectMicroElectronicsSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 14.네비게이션) 조회
	 * @param ParamMap
	 * @return Navigation
	 * @throws Exception
	 */
	public Navigation selectNavigationSummary(ParamMap paramMap) throws Exception{
		return (Navigation) selectByPk("panaver.v3.goods.selectNavigationSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 15.자동차용품) 조회
	 * @param ParamMap
	 * @return CarArticles
	 * @throws Exception
	 */
	public CarArticles selectCarArticlesSummary(ParamMap paramMap) throws Exception{
		return (CarArticles) selectByPk("panaver.v3.goods.selectCarArticlesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 16.의료기기) 조회
	 * @param ParamMap
	 * @return MedicalAppliances
	 * @throws Exception
	 */
	public MedicalAppliances selectMedicalAppliancesSummary(ParamMap paramMap) throws Exception{
		return (MedicalAppliances) selectByPk("panaver.v3.goods.selectMedicalAppliancesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 17.주방용품) 조회
	 * @param ParamMap
	 * @return KitchenUtensils
	 * @throws Exception
	 */
	public KitchenUtensils selectKitchenUtensilsSummary(ParamMap paramMap) throws Exception{
		return (KitchenUtensils) selectByPk("panaver.v3.goods.selectKitchenUtensilsSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 18.화장품) 조회
	 * @param ParamMap
	 * @return CosmeticSummaryType
	 * @throws Exception
	 */
	public Cosmetic selectCosmeticSummary(ParamMap paramMap) throws Exception{
		return (Cosmetic) selectByPk("panaver.v3.goods.selectCosmeticSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 19.귀금속/보석/시계류) 조회
	 * @param ParamMap
	 * @return Jewellery
	 * @throws Exception
	 */
	public Jewellery selectJewellerySummary(ParamMap paramMap) throws Exception{
		return (Jewellery) selectByPk("panaver.v3.goods.selectJewellerySummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 20.식품(농수산물)) 조회
	 * @param ParamMap
	 * @return Food
	 * @throws Exception
	 */
	public Food selectFoodSummary(ParamMap paramMap) throws Exception{
		return (Food) selectByPk("panaver.v3.goods.selectFoodSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 21.가공식품) 조회
	 * @param ParamMap
	 * @return GeneralFood
	 * @throws Exception
	 */
	public GeneralFood selectGeneralFoodSummary(ParamMap paramMap) throws Exception{
		return (GeneralFood) selectByPk("panaver.v3.goods.selectGeneralFoodSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 22.건강기능식품) 조회
	 * @param ParamMap
	 * @return DietFood
	 * @throws Exception
	 */
	public DietFood selectDietFoodSummary(ParamMap paramMap) throws Exception{
		return (DietFood) selectByPk("panaver.v3.goods.selectDietFoodSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 23.영유가용품) 조회
	 * @param ParamMap
	 * @return Kids
	 * @throws Exception
	 */
	public Kids selectKidsSummary(ParamMap paramMap) throws Exception{
		return (Kids) selectByPk("panaver.v3.goods.selectKidsSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 24.악기) 조회
	 * @param ParamMap
	 * @return MusicalInstrument
	 * @throws Exception
	 */
	public MusicalInstrument selectMusicalInstrumentSummary(ParamMap paramMap) throws Exception{
		return (MusicalInstrument) selectByPk("panaver.v3.goods.selectMusicalInstrumentSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 25.스포츠용품) 조회
	 * @param ParamMap
	 * @return SportsEquipment
	 * @throws Exception
	 */
	public SportsEquipment selectSportsEquipmentSummary(ParamMap paramMap) throws Exception{
		return (SportsEquipment) selectByPk("panaver.v3.goods.selectSportsEquipmentSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 26.서적) 조회
	 * @param ParamMap
	 * @return Books
	 * @throws Exception
	 */
	public Books selectBooksSummary(ParamMap paramMap) throws Exception{
		return (Books) selectByPk("panaver.v3.goods.selectBooksSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 31.물품대여 서비스(정수기,비데,공기청정기 등) / 32.물품대여 서비스(서적,유아용품,행사용품 등)) 조회
	 * @param ParamMap
	 * @return RentalEtc
	 * @throws Exception
	 */
	public RentalEtc selectRentalEtcSummary(ParamMap paramMap) throws Exception{
		return (RentalEtc) selectByPk("panaver.v3.goods.selectRentalEtcSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 33.디지털 콘텐츠(음원/게임/인터넷강의 등)) 조회
	 * @param ParamMap
	 * @return DigitalContents
	 * @throws Exception
	 */
	public DigitalContents selectDigitalContentsSummary(ParamMap paramMap) throws Exception{
		return (DigitalContents) selectByPk("panaver.v3.goods.selectDigitalContentsSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 37.기타 용역) 조회
	 * @param ParamMap
	 * @return EtcService
	 * @throws Exception
	 */
	public EtcService selectEtcServiceSummary(ParamMap paramMap) throws Exception{
		return (EtcService) selectByPk("panaver.v3.goods.selectEtcServiceSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 38.기타 재화) 조회
	 * @param ParamMap
	 * @return Etc
	 * @throws Exception
	 */
	public Etc selectEtcSummary(ParamMap paramMap) throws Exception{
		return (Etc) selectByPk("panaver.v3.goods.selectEtcSummary", paramMap.get());
	}
	
	/**
	 * 네이버 이미지등록 - 상품 이미지 조회
	 * @param paNaverGoodsVO
	 * @return
	 * @throws Exception
	 */
	public PaGoodsImage selectPaGoodsImage(ParamMap paramMap)  throws Exception{
		return (PaGoodsImage) selectByPk("panaver.v3.goods.selectPaGoodsImage", paramMap.get());
	}
}