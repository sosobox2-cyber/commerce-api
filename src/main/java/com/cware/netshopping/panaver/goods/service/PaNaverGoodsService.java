package com.cware.netshopping.panaver.goods.service;

import java.util.HashMap;
import java.util.List;

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
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.PaNaverGoodsImageVO;
import com.cware.netshopping.domain.model.PaGoods;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;

public interface PaNaverGoodsService {
	/**
	 * 네이버 상품등록 - 상품 이미지 조회
	 * @param ParamMap
	 * @return paGoodsImage
	 * @throws Exception
	 */
	public PaGoodsImage selectPaGoodsImage(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품 이미지 등록/수정
	 * @param ParamMap
	 * @return paGoodsImage
	 * @throws Exception
	 */
	public int savePaNaverGoodsImageTx(PaNaverGoodsImageVO paNaverGoodsImageVO) throws Exception;

	/**
	 * 네이버 상품등록 - 네이버 상품 이미지 조회
	 * @param ParamMap
	 * @return paNaverGoodsImageVO
	 * @throws Exception
	 */
	public PaNaverGoodsImageVO selectPaNaverGoodsImage(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품정보  조회
	 * @param ParamMap
	 * @return paNaverGoodsVO
	 * @throws Exception
	 */
	public PaNaverGoodsVO selectPaNaverGoods(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보  조회
	 * @param ParamMap
	 * @return paGoods
	 * @throws Exception
	 */
	public PaGoods selectPaGoodsDetail(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 01.의류) 조회
	 * @param ParamMap
	 * @return WearSummaryType
	 * @throws Exception
	 */
	public WearSummaryType selectWearSummary(ParamMap paramMap) throws Exception;
	
	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 02.신발) 조회
	 * @param ParamMap
	 * @return ShoesSummaryType
	 * @throws Exception
	 */
	public ShoesSummaryType selectShoesSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 03.가방) 조회
	 * @param ParamMap
	 * @return BagSummaryType
	 * @throws Exception
	 */
	public BagSummaryType selectBagSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 04.패션잡화) 조회
	 * @param ParamMap
	 * @return FashionItemsSummaryType
	 * @throws Exception
	 */
	public FashionItemsSummaryType selectFashionItemsSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 05.침구류/커튼) 조회
	 * @param ParamMap
	 * @return SleepingGearSummaryType
	 * @throws Exception
	 */
	public SleepingGearSummaryType selectSleepingGearSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 06.가구) 조회
	 * @param ParamMap
	 * @return FurnitureSummaryType
	 * @throws Exception
	 */
	public FurnitureSummaryType selectFurnitureSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 07.영상가전) 조회
	 * @param ParamMap
	 * @return ImageAppliancesSummaryType
	 * @throws Exception
	 */
	public ImageAppliancesSummaryType selectImageAppliancesSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 08.가정용 전기제품) 조회
	 * @param ParamMap
	 * @return HomeAppliancesSummaryType
	 * @throws Exception
	 */
	public HomeAppliancesSummaryType selectHomeAppliancesSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 09.계절가전) 조회
	 * @param ParamMap
	 * @return SeasonAppliancesSummaryType
	 * @throws Exception
	 */
	public SeasonAppliancesSummaryType selectSeasonAppliancesSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 10.사무용기기) 조회
	 * @param ParamMap
	 * @return OfficeAppliancesSummaryType
	 * @throws Exception
	 */
	public OfficeAppliancesSummaryType selectOfficeAppliancesSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 11.광학기기) 조회
	 * @param ParamMap
	 * @return OpticsAppliancesSummaryType
	 * @throws Exception
	 */
	public OpticsAppliancesSummaryType selectOpticsAppliancesSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 12.소형전자) 조회
	 * @param ParamMap
	 * @return MicroElectronicsSummaryType
	 * @throws Exception
	 */
	public MicroElectronicsSummaryType selectMicroElectronicsSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 14.네비게이션) 조회
	 * @param ParamMap
	 * @return NavigationSummaryType
	 * @throws Exception
	 */
	public NavigationSummaryType selectNavigationSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 15.자동차용품) 조회
	 * @param ParamMap
	 * @return CarArticlesSummaryType
	 * @throws Exception
	 */
	public CarArticlesSummaryType selectCarArticlesSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 16.의료기기) 조회
	 * @param ParamMap
	 * @return MedicalAppliancesSummaryType
	 * @throws Exception
	 */
	public MedicalAppliancesSummaryType selectMedicalAppliancesSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 17.주방용품) 조회
	 * @param ParamMap
	 * @return KitchenUtensilsSummaryType
	 * @throws Exception
	 */
	public KitchenUtensilsSummaryType selectKitchenUtensilsSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 18.화장품) 조회
	 * @param ParamMap
	 * @return CosmeticSummaryType
	 * @throws Exception
	 */
	public CosmeticSummaryType selectCosmeticSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 19.귀금속/보석/시계류) 조회
	 * @param ParamMap
	 * @return JewellerySummaryType
	 * @throws Exception
	 */
	public JewellerySummaryType selectJewellerySummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 20.식품(농수산물)) 조회
	 * @param ParamMap
	 * @return FoodSummaryType
	 * @throws Exception
	 */
	public FoodSummaryType selectFoodSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 21.가공식품) 조회
	 * @param ParamMap
	 * @return GeneralFoodSummaryType
	 * @throws Exception
	 */
	public GeneralFoodSummaryType selectGeneralFoodSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 22.건강기능식품) 조회
	 * @param ParamMap
	 * @return DietFoodSummaryType
	 * @throws Exception
	 */
	public DietFoodSummaryType selectDietFoodSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 23.영유가용품) 조회
	 * @param ParamMap
	 * @return KidsSummaryType
	 * @throws Exception
	 */
	public KidsSummaryType selectKidsSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 24.악기) 조회
	 * @param ParamMap
	 * @return MusicalInstrumentSummaryType
	 * @throws Exception
	 */
	public MusicalInstrumentSummaryType selectMusicalInstrumentSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 25.스포츠용품) 조회
	 * @param ParamMap
	 * @return SportsEquipmentSummaryType
	 * @throws Exception
	 */
	public SportsEquipmentSummaryType selectSportsEquipmentSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 26.서적) 조회
	 * @param ParamMap
	 * @return BooksSummaryType
	 * @throws Exception
	 */
	public BooksSummaryType selectBooksSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 31.물품대여 서비스(정수기,비데,공기청정기 등) / 32.물품대여 서비스(서적,유아용품,행사용품 등)) 조회
	 * @param ParamMap
	 * @return RentalEtcSummaryType
	 * @throws Exception
	 */
	public RentalEtcSummaryType selectRentalEtcSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 33.디지털 콘텐츠(음원/게임/인터넷강의 등)) 조회
	 * @param ParamMap
	 * @return DigitalContentsSummaryType
	 * @throws Exception
	 */
	public DigitalContentsSummaryType selectDigitalContentsSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 37.기타 용역) 조회
	 * @param ParamMap
	 * @return EtcServiceSummaryType
	 * @throws Exception
	 */
	public EtcServiceSummaryType selectEtcServiceSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 상품상세정보 (카테고리 - 38.기타 재화) 조회
	 * @param ParamMap
	 * @return EtcSummaryType
	 * @throws Exception
	 */
	public EtcSummaryType selectEtcSummary(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 네이버 상품상세정보  조회
	 * @param ParamMap
	 * @return List<PaNaverGoodsVO>
	 * @throws Exception
	 */
	public List<PaNaverGoodsVO> selectPaNaverGoodsInfoList(ParamMap paramMap) throws Exception;

	/**
	 * 상품 가격 정보 조회
	 * @param goodsCode
	 * @return PaGoodsPriceVO
	 * @throws Exception
	 */
	public PaGoodsPriceVO selectGoodsPriceInfoByGoodsCode(String goodsCode) throws Exception;
	/**
	 * 오픈마켓 상품 - translog 저장
	 * @param PaGoodsTransLog
	 * @return int
	 * @throws Exception
	 */
	public int insertPaNaverGoodsTransLogTx(PaGoodsTransLog paGoodsTransLog) throws Exception;

	/**
	 * 네이버 상품등록 - 제휴사정보저장
	 * @param PaNaverGoodsVO, List<PaGoodsdtMapping>
	 * @return String
	 * @throws Exception
	 */
	public String savePaNaverGoodsTx(PaNaverGoodsVO paNaverGoods) throws Exception;

	/**
	 * 네이버 상품등록 - 상품 판매 여부 조회
	 * @param String
	 * @return String
	 * @throws Exception
	 */
	public String selectPaNaverSaleGb(String goodsCode) throws Exception;

	/**
	 * 네이버 상품등록 - 상품단품list조회
	 * @param ParamMap
	 * @return List<PaGoodsdtMapping>
	 * @throws Exception
	 */
	public List<PaGoodsdtMapping> selectPaNaverGoodsdtInfoList(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 제휴사 실패정보 저장
	 * @param paNaverGoods
	 * @return String
	 * @throws Exception
	 */
	public String savePaNaverGoodsFailTx(PaNaverGoodsVO paNaverGoods) throws Exception;

	/**
	 * 네이버 상품등록 - 옵션 정보 조회
	 * @param ParamMap
	 * @return PaGoodsdt
	 * @throws Exception
	 */
	public List<PaGoodsdtMapping> selectPaGoodsdt(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 옵션 정보 수정
	 * @param PaGoodsdtMapping
	 * @return int
	 * @throws Exception
	 */
	public int savePaNaverOptionTx(PaGoodsdtMapping paGoodsdtMapping) throws Exception;

	/**
	 * 네이버 판매상태 - 제휴사 상품정보 저장
	 * @param PaNaverGoodsVO
	 * @return String
	 * @throws Exception
	 */
	public String savePaNaverGoodsSellTx(PaNaverGoodsVO paNaverGoods) throws Exception;

	/**
	 * 네이버 판매상태 - 제휴 모니터링 저장 
	 * @param PaNaverGoodsVO
	 * @return String
	 * @throws Exception
	 */
	public String saveStopMonitering(PaNaverGoodsVO paNaverGoods) throws Exception;

	/**
	 * 네이버 상품등록 - 옵션 개수 조회
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectPaGoodsdtMapping(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 단품(옵션) 정보 수정 
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int saveSinglePaGoodsdtMappingTx(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 단품(옵션) 등록/수정 실패 
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updatePaNaverOptionFailTx(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 제휴사 유형코드 조회
	 * @param ParamMap
	 * @return String 
	 * @throws Exception
	 */
	public String selectPaOfferType(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 판매상태 변경이 필요한 상세정보  조회
	 * @param ParamMap
	 * @return List<PaNaverGoodsVO>
	 * @throws Exception
	 */
	public List<PaNaverGoodsVO> selectGoodsSellModifyAllObjectList(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 상품등록 - 오류로 변경안된 판매상태 수정
	 * @param PaNaverGoodsVO
	 * @return int
	 * @throws Exception
	 */
	public int saveTransSaleYNTx(ParamMap paramMap) throws Exception;

	//기술서 체크로직
	public int selectPaNaverGoodsDescCnt998(ParamMap paramMap) throws Exception;
	public String selectPaNaverGoodsDesc998(ParamMap paramMap) throws Exception;
	public int selectPaNaverGoodsDescCnt999(ParamMap paramMap) throws Exception;
	public String selectPaNaverGoodsDesc999(ParamMap paramMap) throws Exception;

	/**
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
	 * 네이버 상품등록 - 상품가격 - TRANS_ID, TRANS_DATE 수정
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int savePaGoodsPriceTx(ParamMap paramMap, List<PaPromoTarget> paPromoTargetList) throws Exception;

	// 모델 정보 - 모델명 추가 여부
	public String selectModelInputYN(ParamMap paramMap) throws Exception;

	// 인증 정보 조회
	public List<PaCertificationVO> selectPaCertificationList(ParamMap paramMap) throws Exception;

	// 출고지/회수지 주소 번호
	public PaDeliveryVO selectPaDelivery(ParamMap paramMap) throws Exception;

	// 판매 중지 처리
	public String savePaGoodsModifyFailTx(PaNaverGoodsVO paNaverGoods) throws Exception;

	// 프로모션 할인가 조회
	public List<HashMap<String,Object>> selectPaPromoTarget(ParamMap paramMap) throws Exception;
}