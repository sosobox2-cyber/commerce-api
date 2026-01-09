package com.cware.netshopping.panaver.goods.repository;

import java.util.HashMap;
import java.util.List;

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
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.PaNaverGoodsImageVO;
import com.cware.netshopping.domain.model.PaGoods;
import com.cware.netshopping.domain.model.PaGoodsAuthYnLog;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.PaSaleNoGoods;

@Service("panaver.goods.paNaverGoodsDAO")
public class PaNaverGoodsDAO extends AbstractPaDAO{

	/**
	 * 네이버 상품등록 - 상품 이미지 조회
	 * @param ParamMap
	 * @return paGoodsImage
	 * @throws Exception
	 */
	public PaGoodsImage selectPaGoodsImage(ParamMap paramMap) throws Exception{
		return (PaGoodsImage) selectByPk("panaver.goods.selectPaGoodsImage", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품 이미지 등록
	 * @param PaNaverGoodsImageVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPaNaverGoodsImage(PaNaverGoodsImageVO paNaverGoodsImageVO) throws Exception{
		return insert("panaver.goods.insertPaNaverGoodsImage", paNaverGoodsImageVO);
	}

	/**
	 * 네이버 상품등록 - 상품 이미지 수정
	 * @param PaNaverGoodsImageVO
	 * @return int
	 * @throws Exception
	 */
	public int updateUploadPaNaverGoodsImage(PaNaverGoodsImageVO paNaverGoodsImageVO) throws Exception {
		return update("panaver.goods.updateUploadPaNaverGoodsImage", paNaverGoodsImageVO);
	}

	/**
	 * 네이버 상품등록 - 네이버 상품 이미지 수정
	 * @param ParamMap
	 * @return paNaverGoodsImageVO
	 * @throws Exception
	 */
	public PaNaverGoodsImageVO selectPaNaverGoodsImage(ParamMap paramMap) throws Exception {
		return (PaNaverGoodsImageVO) selectByPk("panaver.goods.selectPaNaverGoodsImage", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품정보 수정
	 * @param ParamMap
	 * @return paNaverGoodsVO
	 * @throws Exception
	 */
	public PaNaverGoodsVO selectPaNaverGoods(ParamMap paramMap) throws Exception {
		return (PaNaverGoodsVO) selectByPk("panaver.goods.selectPaNaverGoods", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보  조회
	 * @param ParamMap
	 * @return paGoods
	 * @throws Exception
	 */
	public PaGoods selectPaGoodsDetail(ParamMap paramMap) throws Exception {
		return (PaGoods) selectByPk("panaver.goods.selectPaGoodsDetail", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 01.의류) 조회
	 * @param ParamMap
	 * @return WearSummaryType
	 * @throws Exception
	 */
	public WearSummaryType selectWearSummary(ParamMap paramMap) throws Exception {
		return (WearSummaryType) selectByPk("panaver.goods.selectWearSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 02.신발) 조회
	 * @param ParamMap
	 * @return ShoesSummaryType
	 * @throws Exception
	 */
	public ShoesSummaryType selectShoesSummary(ParamMap paramMap) throws Exception {
		return (ShoesSummaryType) selectByPk("panaver.goods.selectShoesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 03.가방) 조회
	 * @param ParamMap
	 * @return BagSummaryType
	 * @throws Exception
	 */
	public BagSummaryType selectBagSummary(ParamMap paramMap) throws Exception{
		return (BagSummaryType) selectByPk("panaver.goods.selectBagSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 04.패션잡화) 조회
	 * @param ParamMap
	 * @return FashionItemsSummaryType
	 * @throws Exception
	 */
	public FashionItemsSummaryType selectFashionItemsSummary(ParamMap paramMap) throws Exception{
		return (FashionItemsSummaryType) selectByPk("panaver.goods.selectFashionItemsSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 05.침구류/커튼) 조회
	 * @param ParamMap
	 * @return SleepingGearSummaryType
	 * @throws Exception
	 */
	public SleepingGearSummaryType selectSleepingGearSummary(ParamMap paramMap) throws Exception{
		return (SleepingGearSummaryType) selectByPk("panaver.goods.selectSleepingGearSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 06.가구) 조회
	 * @param ParamMap
	 * @return FurnitureSummaryType
	 * @throws Exception
	 */
	public FurnitureSummaryType selectFurnitureSummary(ParamMap paramMap) throws Exception{
		return (FurnitureSummaryType) selectByPk("panaver.goods.selectFurnitureSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 07.영상가전) 조회
	 * @param ParamMap
	 * @return ImageAppliancesSummaryType
	 * @throws Exception
	 */
	public ImageAppliancesSummaryType selectImageAppliancesSummary(ParamMap paramMap) throws Exception{
		return (ImageAppliancesSummaryType) selectByPk("panaver.goods.selectImageAppliancesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 08.가정용 전기제품) 조회
	 * @param ParamMap
	 * @return HomeAppliancesSummaryType
	 * @throws Exception
	 */
	public HomeAppliancesSummaryType selectHomeAppliancesSummary(ParamMap paramMap) throws Exception{
		return (HomeAppliancesSummaryType) selectByPk("panaver.goods.selectHomeAppliancesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 09.계절가전) 조회
	 * @param ParamMap
	 * @return SeasonAppliancesSummaryType
	 * @throws Exception
	 */
	public SeasonAppliancesSummaryType selectSeasonAppliancesSummary(ParamMap paramMap) throws Exception{
		return (SeasonAppliancesSummaryType) selectByPk("panaver.goods.selectSeasonAppliancesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 10.사무용기기) 조회
	 * @param ParamMap
	 * @return OfficeAppliancesSummaryType
	 * @throws Exception
	 */
	public OfficeAppliancesSummaryType selectOfficeAppliancesSummary(ParamMap paramMap) throws Exception{
		return (OfficeAppliancesSummaryType) selectByPk("panaver.goods.selectOfficeAppliancesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 11.광학기기) 조회
	 * @param ParamMap
	 * @return OpticsAppliancesSummaryType
	 * @throws Exception
	 */
	public OpticsAppliancesSummaryType selectOpticsAppliancesSummary(ParamMap paramMap) throws Exception{
		return (OpticsAppliancesSummaryType) selectByPk("panaver.goods.selectOpticsAppliancesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 12.소형전자) 조회
	 * @param ParamMap
	 * @return MicroElectronicsSummaryType
	 * @throws Exception
	 */
	public MicroElectronicsSummaryType selectMicroElectronicsSummary(ParamMap paramMap) throws Exception{
		return (MicroElectronicsSummaryType) selectByPk("panaver.goods.selectMicroElectronicsSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 14.네비게이션) 조회
	 * @param ParamMap
	 * @return NavigationSummaryType
	 * @throws Exception
	 */
	public NavigationSummaryType selectNavigationSummary(ParamMap paramMap) throws Exception{
		return (NavigationSummaryType) selectByPk("panaver.goods.selectNavigationSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 15.자동차용품) 조회
	 * @param ParamMap
	 * @return CarArticlesSummaryType
	 * @throws Exception
	 */
	public CarArticlesSummaryType selectCarArticlesSummary(ParamMap paramMap) throws Exception{
		return (CarArticlesSummaryType) selectByPk("panaver.goods.selectCarArticlesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 16.의료기기) 조회
	 * @param ParamMap
	 * @return MedicalAppliancesSummaryType
	 * @throws Exception
	 */
	public MedicalAppliancesSummaryType selectMedicalAppliancesSummary(ParamMap paramMap) throws Exception{
		return (MedicalAppliancesSummaryType) selectByPk("panaver.goods.selectMedicalAppliancesSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 17.주방용품) 조회
	 * @param ParamMap
	 * @return KitchenUtensilsSummaryType
	 * @throws Exception
	 */
	public KitchenUtensilsSummaryType selectKitchenUtensilsSummary(ParamMap paramMap) throws Exception{
		return (KitchenUtensilsSummaryType) selectByPk("panaver.goods.selectKitchenUtensilsSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 18.화장품) 조회
	 * @param ParamMap
	 * @return CosmeticSummaryType
	 * @throws Exception
	 */
	public CosmeticSummaryType selectCosmeticSummary(ParamMap paramMap) throws Exception{
		return (CosmeticSummaryType) selectByPk("panaver.goods.selectCosmeticSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 19.귀금속/보석/시계류) 조회
	 * @param ParamMap
	 * @return JewellerySummaryType
	 * @throws Exception
	 */
	public JewellerySummaryType selectJewellerySummary(ParamMap paramMap) throws Exception{
		return (JewellerySummaryType) selectByPk("panaver.goods.selectJewellerySummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 20.식품(농수산물)) 조회
	 * @param ParamMap
	 * @return FoodSummaryType
	 * @throws Exception
	 */
	public FoodSummaryType selectFoodSummary(ParamMap paramMap) throws Exception{
		return (FoodSummaryType) selectByPk("panaver.goods.selectFoodSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 21.가공식품) 조회
	 * @param ParamMap
	 * @return GeneralFoodSummaryType
	 * @throws Exception
	 */
	public GeneralFoodSummaryType selectGeneralFoodSummary(ParamMap paramMap) throws Exception{
		return (GeneralFoodSummaryType) selectByPk("panaver.goods.selectGeneralFoodSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 22.건강기능식품) 조회
	 * @param ParamMap
	 * @return DietFoodSummaryType
	 * @throws Exception
	 */
	public DietFoodSummaryType selectDietFoodSummary(ParamMap paramMap) throws Exception{
		return (DietFoodSummaryType) selectByPk("panaver.goods.selectDietFoodSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 23.영유가용품) 조회
	 * @param ParamMap
	 * @return KidsSummaryType
	 * @throws Exception
	 */
	public KidsSummaryType selectKidsSummary(ParamMap paramMap) throws Exception{
		return (KidsSummaryType) selectByPk("panaver.goods.selectKidsSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 24.악기) 조회
	 * @param ParamMap
	 * @return MusicalInstrumentSummaryType
	 * @throws Exception
	 */
	public MusicalInstrumentSummaryType selectMusicalInstrumentSummary(ParamMap paramMap) throws Exception{
		return (MusicalInstrumentSummaryType) selectByPk("panaver.goods.selectMusicalInstrumentSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 25.스포츠용품) 조회
	 * @param ParamMap
	 * @return SportsEquipmentSummaryType
	 * @throws Exception
	 */
	public SportsEquipmentSummaryType selectSportsEquipmentSummary(ParamMap paramMap) throws Exception{
		return (SportsEquipmentSummaryType) selectByPk("panaver.goods.selectSportsEquipmentSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 26.서적) 조회
	 * @param ParamMap
	 * @return BooksSummaryType
	 * @throws Exception
	 */
	public BooksSummaryType selectBooksSummary(ParamMap paramMap) throws Exception{
		return (BooksSummaryType) selectByPk("panaver.goods.selectBooksSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 31.물품대여 서비스(정수기,비데,공기청정기 등) / 32.물품대여 서비스(서적,유아용품,행사용품 등)) 조회
	 * @param ParamMap
	 * @return RentalEtcSummaryType
	 * @throws Exception
	 */
	public RentalEtcSummaryType selectRentalEtcSummary(ParamMap paramMap) throws Exception{
		return (RentalEtcSummaryType) selectByPk("panaver.goods.selectRentalEtcSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 33.디지털 콘텐츠(음원/게임/인터넷강의 등)) 조회
	 * @param ParamMap
	 * @return DigitalContentsSummaryType
	 * @throws Exception
	 */
	public DigitalContentsSummaryType selectDigitalContentsSummary(ParamMap paramMap) throws Exception{
		return (DigitalContentsSummaryType) selectByPk("panaver.goods.selectDigitalContentsSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 37.기타 용역) 조회
	 * @param ParamMap
	 * @return EtcServiceSummaryType
	 * @throws Exception
	 */
	public EtcServiceSummaryType selectEtcServiceSummary(ParamMap paramMap) throws Exception{
		return (EtcServiceSummaryType) selectByPk("panaver.goods.selectEtcServiceSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 38.기타 재화) 조회
	 * @param ParamMap
	 * @return EtcSummaryType
	 * @throws Exception
	 */
	public EtcSummaryType selectEtcSummary(ParamMap paramMap) throws Exception{
		return (EtcSummaryType) selectByPk("panaver.goods.selectEtcSummary", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 네이버 상품상세정보  조회
	 * @param ParamMap
	 * @return List<PaNaverGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaNaverGoodsVO> selectPaNaverGoodsInfoList(ParamMap paramMap) throws Exception {
		return (List<PaNaverGoodsVO>) list("panaver.goods.selectPaNaverGoodsInfoList", paramMap.get());
	}

	// 상품코드로 조회하므로 단건 반환 (네이버연동구조개선)
	public PaNaverGoodsVO selectPaNaverGoodsInfo(ParamMap paramMap) throws Exception {
		return (PaNaverGoodsVO) selectByPk("panaver.goods.selectPaNaverGoodsInfoList", paramMap.get());
	}
	
	/**
	 * 상품 가격 정보 조회
	 * @param goodsCode
	 * @return PaGoodsPriceVO
	 * @throws Exception
	 */
	public PaGoodsPriceVO selectGoodsPriceInfoByGoodsCode(String goodsCode) throws Exception {
		return (PaGoodsPriceVO) selectByPk("panaver.goods.selectGoodsPriceInfoByGoodsCode", goodsCode);
	}
	/**
	 * 네이버  상품등록 - 상품 단품정보 조회
	 * @param paramMap
	 * @returnList<PaGoodsdtMapping>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsdtMapping> selectPaNaverGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return (List<PaGoodsdtMapping>) list("panaver.goods.selectPaNaverGoodsdtInfoList", paramMap.get());
		}
	
	/**
	 * 네이버  상품등록 - 네이버  상품정보 저장
	 * @param PaNaverGoodsVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePaNaverGoods(PaNaverGoodsVO paNaverGoods) throws Exception {
		return update("panaver.goods.updatePaNaverGoods", paNaverGoods);
	}
	
	/**
	 * 네이버 상품등록 - 네이버 상품 이미지 정보 저장
	 * @param PaNaverGoodsVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePaNaverGoodsImage(PaNaverGoodsVO paNaverGoods) throws Exception {
		return update("panaver.goods.updatePaNaverGoodsImage", paNaverGoods);
	}

	/**
	 * 네이버 상품등록 - 네이버 상품 배송비 정보 저장
	 * @param PaNaverGoodsVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePaNaverCustShipCost(PaNaverGoodsVO paNaverGoods) throws Exception {
		return update("panaver.goods.updatePaNaverCustShipCost", paNaverGoods);
	}

	/**
	 * 네이버 상품등록 - 네이버 상품정보고시내용 저장
	 * @param PaNaverGoodsVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePaNaverGoodsOffer(PaNaverGoodsVO paNaverGoods) throws Exception {
		return update("panaver.goods.updatePaNaverGoodsOffer", paNaverGoods);
	}
	
	/**	
	 * 네이버 상품등록 - 네이버 제휴입점대상상품 저장
	 * @param PaNaverGoodsVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGoodsTarget(PaNaverGoodsVO paNaverGoods) throws Exception {
		return update("panaver.goods.updatePaGoodsTarget", paNaverGoods);
	}

	/**
	 * 네이버 상품등록 - 제휴사 실패정보 저장
	 * @param paNaverGoods
	 * @return String
	 * @throws Exception
	 */
	public int updatePaNaverGoodsFail(PaNaverGoodsVO paNaverGoods) throws Exception {
		return update("panaver.goods.updatePaNaverGoodsFail", paNaverGoods);
	}

	/**
	 * 네이버 상품등록 - 옵션 정보 조회
	 * @param ParamMap
	 * @return PaGoodsdt
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsdtMapping> selectPaGoodsdt(ParamMap paramMap) throws Exception {
		return (List<PaGoodsdtMapping>) list("panaver.goods.selectPaGoodsdt", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 옵션 정보 수정
	 * @param PaGoodsdtMapping
	 * @return int
	 * @throws Exception
	 */
	public int updatePaNaverOption(PaGoodsdtMapping paGoodsdtMapping) throws Exception {
		return update("panaver.goods.updatePaNaverOption", paGoodsdtMapping);
	}

	/**
	 * 네이버 판매상태 - 제휴 모니터링 저장 
	 * @param PaNaverGoodsVO
	 * @return String
	 * @throws Exception
	 */
	public int insertStopMonitering(PaNaverGoodsVO paNaverGoods) throws Exception {
		return insert("panaver.goods.insertStopMonitering", paNaverGoods);
	}

	/**
	 * 네이버 상품등록 - 옵션 개수 조회
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectPaGoodsdtMapping(ParamMap paramMap) throws Exception {
		return (int) selectByPk("panaver.goods.selectPaGoodsdtMapping", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 단품(옵션) 정보 수정 
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updateSinglePaGoodsdtMapping(ParamMap paramMap) throws Exception {
		return update("panaver.goods.updateSinglePaGoodsdtMapping",paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 단품(옵션) 등록/수정 실패 
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updatePaNaverOptionFail(ParamMap paramMap) throws Exception {
		return update("panaver.goods.updatePaNaverOptionFail",paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 제휴사 유형코드 조회
	 * @param ParamMap
	 * @return String 
	 * @throws Exception
	 */
	public String selectPaOfferType(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.goods.selectPaOfferType", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품 이미지 수정 - TRANS_TARGET_YN 변경
	 * @param paNaverGoodsImageVO
	 * @return int
	 * @throws Exception
	 */
	public int updateTransTargetYN(PaNaverGoodsImageVO paNaverGoodsImageVO) throws Exception {
		return update("panaver.goods.updateTransTargetYN", paNaverGoodsImageVO);
	}

	/**
	 * 네이버 상품등록 - 판매상태 변경이 필요한 상세정보  조회
	 * @param ParamMap
	 * @return List<PaNaverGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaNaverGoodsVO> selectGoodsSellModifyAllObjectList(ParamMap paramMap) throws Exception {
		return (List<PaNaverGoodsVO>) list("panaver.goods.selectGoodsSellModifyAllObjectList", paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 오류로 변경안된 판매상태 수정
	 * @param PaNaverGoodsVO
	 * @return int
	 * @throws Exception
	 */
	public int updateTransSaleYN(ParamMap paramMap) throws Exception {
		return update("panaver.goods.updateTransSaleYN", paramMap.get());
	}

	//기술서 체크로직
	public int selectPaNaverGoodsDescCnt998(ParamMap paramMap) throws Exception {
		return (int)selectByPk("panaver.goods.selectPaNaverGoodsDescCnt998",paramMap.get());
	}
	public String selectPaNaverGoodsDesc998FR(ParamMap paramMap) throws Exception {
		return (String)selectByPk("panaver.goods.selectPaNaverGoodsDesc998FR",paramMap.get());
	}
	public String selectPaNaverGoodsDesc998TO(ParamMap paramMap) throws Exception {
		return (String)selectByPk("panaver.goods.selectPaNaverGoodsDesc998TO",paramMap.get());
	}
	public int selectPaNaverGoodsDescCnt999(ParamMap paramMap) throws Exception {
		return (int)selectByPk("panaver.goods.selectPaNaverGoodsDescCnt999",paramMap.get());
	}
	public String selectPaNaverGoodsDesc999FR(ParamMap paramMap) throws Exception {
		return (String)selectByPk("panaver.goods.selectPaNaverGoodsDesc999FR",paramMap.get());
	}
	public String selectPaNaverGoodsDesc999TO(ParamMap paramMap) throws Exception {
		return (String)selectByPk("panaver.goods.selectPaNaverGoodsDesc999TO",paramMap.get());
	}

	/**
	 * 네이버 상품등록 - 상품가격 - TRANS_ID, TRANS_DATE 수정
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGoodsPrice(ParamMap paramMap) throws Exception{
		return update("panaver.goods.updatePaGoodsPrice", paramMap.get());
	}

	// 모델 정보 - 모델명 추가 여부
	public String selectModelInputYN(ParamMap paramMap) throws Exception{
		return (String)selectByPk("panaver.goods.selectModelInputYN",paramMap.get());
	}

	// 인증 정보 조회
	@SuppressWarnings("unchecked")
	public List<PaCertificationVO> selectPaCertificationList(ParamMap paramMap) throws Exception{
		return (List<PaCertificationVO>) list("panaver.goods.selectPaCertificationList", paramMap.get());
	}

	// 출고지/회수지 주소 번호
	public PaDeliveryVO selectPaDelivery(ParamMap paramMap) throws Exception{
		return (PaDeliveryVO) selectByPk("panaver.goods.selectPaDelivery", paramMap.get());
	}

	// 판매 중지 처리
	public int updatePaGoodsModifyFail(PaNaverGoodsVO paNaverGoods) throws Exception{
		return insert("panaver.goods.updatePaGoodsModifyFail", paNaverGoods);
	}
	public int insertPaSaleNoGoods(PaSaleNoGoods paSaleNoGoods) throws Exception{
		return insert("panaver.goods.insertPaSaleNoGoods", paSaleNoGoods);
	}
	/** paGoodsTarget 자동 재입점 여부(AUTO_YN) UPDATE*/
	public int updatePaGoodsTargetForAuthYn(PaNaverGoodsVO paNaverGoods) throws Exception{
		return update("panaver.goods.updatePaGoodsTargetForAuthYn",paNaverGoods);
	}
	/** paGoodsTarget 자동 재입점 여부(AUTO_YN) LOG성 데이터 INSERT */
	public int insertPaGoodsAuthYnLog(PaGoodsAuthYnLog paGoodsAuthYnLog) throws Exception{
		return insert("panaver.goods.insertPaGoodsAuthYnLog", paGoodsAuthYnLog);
	}
	
	// 프로모션 할인가 조회
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectPaPromoTarget(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("panaver.goods.selectPaPromoTarget", paramMap.get());
	}
	
	public int updatePaPromoTargetCalc(PaPromoTarget paPromoTarget) throws Exception{
		return update("panaver.goods.updatePaPromoTargetCalc", paPromoTarget);
	}

	public int updatePaPromoTarget(PaPromoTarget paPromoTarget) throws Exception {
		return update("panaver.goods.updatePaPromoTarget", paPromoTarget);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectPaPromoDeleteTarget(ParamMap paramMap) throws Exception{
		return list("panaver.goods.selectPaPromoDeleteTarget", paramMap.get());
	}

	public int insertNewPaPromoTarget(PaPromoTarget paPromoTarget) throws Exception{
		return insert("panaver.goods.insertNewPaPromoTarget", paPromoTarget);
	}

	public int updateTPapromoTarget4ExceptMemo(HashMap<String, Object> promo) throws Exception{
		return update("panaver.goods.updateTPapromoTarget4ExceptMemo", promo);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaPromoTargetOne(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>)selectByPk("panaver.goods.selectPaPromoTargetOne", paramMap.get());
	}
	
	public int insertNewPaPromoTargetAmt0(PaPromoTarget paPromoTarget) throws Exception{
		return insert("panaver.goods.insertNewPaPromoTargetAmt0", paPromoTarget);
	}

	public int selectPaNaverGoodsModifyCheck(PaNaverGoodsVO paNaverGoods) {
		return (int)selectByPk("panaver.goods.selectPaNaverGoodsModifyCheck", paNaverGoods);
	}


	@SuppressWarnings("unchecked")
	public List<PaNaverGoodsVO> selectChannelSearchTarget(PaNaverGoodsVO paNaverGoodsVO) {
		return (List<PaNaverGoodsVO>) list("panaver.goods.selectChannelSearchTarget", paNaverGoodsVO );

	}

	
}