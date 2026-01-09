package com.cware.api.panaver.product;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import com.cware.api.panaver.product.type.AddressBookService;
import com.cware.api.panaver.product.type.AddressBookServicePortType;
import com.cware.api.panaver.product.type.AttributeCategoryMapType;
import com.cware.api.panaver.product.type.AttributeCategoryType;
import com.cware.api.panaver.product.type.CategoryListType;
import com.cware.api.panaver.product.type.CertificationInfoType;
import com.cware.api.panaver.product.type.CertificationListType;
import com.cware.api.panaver.product.type.CertificationType;
import com.cware.api.panaver.product.type.ChangeProductSaleStatusRequestType;
import com.cware.api.panaver.product.type.ChangeProductSaleStatusResponseType;
import com.cware.api.panaver.product.type.CombinationOptionItemListType;
import com.cware.api.panaver.product.type.CombinationOptionItemType;
import com.cware.api.panaver.product.type.CombinationOptionNamesType;
import com.cware.api.panaver.product.type.CombinationOptionType;
import com.cware.api.panaver.product.type.CustomOptionItemListType;
import com.cware.api.panaver.product.type.CustomOptionItemType;
import com.cware.api.panaver.product.type.DeliveryInfoService;
import com.cware.api.panaver.product.type.DeliveryInfoServicePortType;
import com.cware.api.panaver.product.type.DeliveryType;
import com.cware.api.panaver.product.type.EtcSummaryType;
import com.cware.api.panaver.product.type.FreeInterestType;
import com.cware.api.panaver.product.type.GetAddressBookListRequestType;
import com.cware.api.panaver.product.type.GetAddressBookListResponseType;
import com.cware.api.panaver.product.type.GetAllCategoryListRequestType;
import com.cware.api.panaver.product.type.GetAllCategoryListResponseType;
import com.cware.api.panaver.product.type.GetAllOriginAreaListRequestType;
import com.cware.api.panaver.product.type.GetAllOriginAreaListResponseType;
import com.cware.api.panaver.product.type.GetBrandListRequestType;
import com.cware.api.panaver.product.type.GetBrandListResponseType;
import com.cware.api.panaver.product.type.GetBundleGroupListRequestType;
import com.cware.api.panaver.product.type.GetBundleGroupListResponseType;
import com.cware.api.panaver.product.type.GetBundleGroupRequestType;
import com.cware.api.panaver.product.type.GetBundleGroupResponseType;
import com.cware.api.panaver.product.type.GetCategoryInfoRequestType;
import com.cware.api.panaver.product.type.GetCategoryInfoResponseType;
import com.cware.api.panaver.product.type.GetManufacturerListRequestType;
import com.cware.api.panaver.product.type.GetManufacturerListResponseType;
import com.cware.api.panaver.product.type.GetMobileImageInfoRequestType;
import com.cware.api.panaver.product.type.GetMobileImageInfoResponseType;
import com.cware.api.panaver.product.type.GetModelListRequestType;
import com.cware.api.panaver.product.type.GetModelListResponseType;
import com.cware.api.panaver.product.type.GetOptionRequestType;
import com.cware.api.panaver.product.type.GetOptionResponseType;
import com.cware.api.panaver.product.type.GetOriginAreaListRequestType;
import com.cware.api.panaver.product.type.GetOriginAreaListResponseType;
import com.cware.api.panaver.product.type.GetProductListRequestType;
import com.cware.api.panaver.product.type.GetProductListResponseType;
import com.cware.api.panaver.product.type.GetProductRequestType;
import com.cware.api.panaver.product.type.GetProductResponseType;
import com.cware.api.panaver.product.type.GetQuestionAnswerListRequestType;
import com.cware.api.panaver.product.type.GetQuestionAnswerListResponseType;
import com.cware.api.panaver.product.type.GetReturnsCompanyListRequestType;
import com.cware.api.panaver.product.type.GetReturnsCompanyListResponseType;
import com.cware.api.panaver.product.type.GetSubCategoryListRequestType;
import com.cware.api.panaver.product.type.GetSubCategoryListResponseType;
import com.cware.api.panaver.product.type.GetSubOriginAreaListRequestType;
import com.cware.api.panaver.product.type.GetSubOriginAreaListResponseType;
import com.cware.api.panaver.product.type.GiftType;
import com.cware.api.panaver.product.type.ImageService;
import com.cware.api.panaver.product.type.ImageServicePortType;
import com.cware.api.panaver.product.type.ImageType;
import com.cware.api.panaver.product.type.ImageURLListType;
import com.cware.api.panaver.product.type.ManageOptionRequestType;
import com.cware.api.panaver.product.type.ManageOptionResponseType;
import com.cware.api.panaver.product.type.ManageProductRequestType;
import com.cware.api.panaver.product.type.ManageProductResponseType;
import com.cware.api.panaver.product.type.MileageType;
import com.cware.api.panaver.product.type.ModelType;
import com.cware.api.panaver.product.type.MultiPurchaseDiscountType;
import com.cware.api.panaver.product.type.OptionType;
import com.cware.api.panaver.product.type.OriginAreaType;
import com.cware.api.panaver.product.type.ProductService;
import com.cware.api.panaver.product.type.ProductServicePortType;
import com.cware.api.panaver.product.type.ProductSummaryType;
import com.cware.api.panaver.product.type.ProductType;
import com.cware.api.panaver.product.type.QuestionAnswerService;
import com.cware.api.panaver.product.type.QuestionAnswerServicePortType;
import com.cware.api.panaver.product.type.RestoreProductRequestType;
import com.cware.api.panaver.product.type.RestoreProductResponseType;
import com.cware.api.panaver.product.type.SaleStatusType;
import com.cware.api.panaver.product.type.SellerDiscountType;
import com.cware.api.panaver.product.type.ShoesSummaryType;
import com.cware.api.panaver.product.type.SimpleOptionItemListType;
import com.cware.api.panaver.product.type.SimpleOptionItemType;
import com.cware.api.panaver.product.type.StampType;
import com.cware.api.panaver.product.type.StatusType;
import com.cware.api.panaver.product.type.StringCodeMapType;
import com.cware.api.panaver.product.type.StringCodeType;
import com.cware.api.panaver.product.type.URLType;
import com.cware.api.panaver.product.type.UploadImageRequestType;
import com.cware.api.panaver.product.type.UploadImageResponseType;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;

/**
 * @author NBP
 * 네어버 스마스트스토어 API 샘플소스
 */
@SuppressWarnings("unused")
public class ManageProductSample extends SampleBase {

	/**
	 * 이미지 정보 샘플
	 * <p/>
	 * 상품 이미지 정보를 생성한다.
	 * 이미지 URL은 이미지 업로드 API(UploadImage)로 업로드하고 반환받은 이미지 URL을 입력해야 한다.
	 *
	 * @return 이미지 정보
	 */
	private static ImageType createImageSample() {
		ImageType image = new ImageType();
		image.setRepresentative(new URLType());
		image.getRepresentative().setURL("http://beta.shop1.phinf.naver.net/20190906_290/1567751375322jVCfm_JPEG/20007626_g.jpg");
		return image;
	}

	/**
	 * 모델 정보 샘플 (모델코드, 브랜드명, 제조사명)
	 * <p/>
	 * 상품에 입력할 모델정보를 생성한다.
	 * 모델 코드는 GetModelList, 브랜드명은 GetBrandList, 제조사명은 GetManufacturerList API로 조회할 수 있다.
	 * 회
	 *
	 * @return 모델 정보
	 */
	private static ModelType createModelSample() {
		ModelType model = new ModelType();
		//model.setId(ComUtil.objToLong(1000001)); // 6154900444L 모델 ID. GetModelList API로 조회한 ModelReturnType의 ModelId를 입력
		model.setBrandName("에넥스 에니"); // 브랜드명. GetBrandList로 샵N DB에 등록되어 있는 브랜드명을 조회할 수 있다.
		// 모델ID와 함께 입력하는 경우 ModelReturnType의 BrandName을 입력하면 된다.
		model.setManufacturerName("에넥스"); // 제조사명. GetManufacturerList로 샵N DB에 등록되어 있는 제조사명을 조회할 수 있다.
		// 모델ID와 함께 입력하는 경우 ModelReturnType의 ManufacturerName을 입력하면 된다.
		model.setModelName("디럭스");
		return model;
	}

	/**
	 * 인증정보 샘플
	 * <p/>
	 * 상품에 입력할 인증정보를 생성한다.
	 * GetCategoryInfo API를 호출하면 해당 카테고리에 입력할 수 있는 인증정보코드를 조회할 수 있습니다.
	 *
	 * @return 인증정보 목록
	 */
	private static CertificationListType createCertificationListSample() {
		CertificationType certification1 = new CertificationType();
		certification1.setId(11128); // 인증유형 ID. GetCategoryInfo에서 조회하는 CertificationCategoryListType의 code 값을 입력
		certification1.setName("인증기관1"); // 인증기관
		certification1.setNumber("1234"); // 인증번호

		CertificationType certification2 = new CertificationType();
		certification2.setId(9053);
		certification2.setName("인증기관2");
		certification2.setNumber("2345");

		CertificationListType certificationList = new CertificationListType();
		certificationList.getCertification().add(certification1);
		certificationList.getCertification().add(certification2);
		return certificationList;
	}

	/**
	 * 원산지 정보 샘플
	 * <p/>
	 * 상품에 입력할 원산지 정보를 생성한다.
	 * 원산지 코드는 GetOriginAreaList, GetAllOriginAreaList, GetSubOriginAreaList API를 사용해서 조회할 수 있다.
	 *
	 * @return 원산지 정보
	 */
	private static OriginAreaType createOriginAreaSample() {
		OriginAreaType originArea = new OriginAreaType();
		originArea.setCode("0001"); // 원산지 코드
		originArea.setPlural("N"); // 복수 원산지 여부
		// originArea.setImporter("애플"); // 수입사. 원산지 코드가 수입산(ex: 미국, 중국 등)인 경우 입력한다.
		// originArea.setContent("원산지 직접입력"); // 원산지 직접입력. 원산지코드가 04 (직접입력)인 경우에만 입력한다.
		return originArea;
	}

	/**
	 * 배송 정보 샘플
	 * <p/>
	 * 상품 배송정보를 입력한다.
	 * 배송 없는 상품은 DeliveryType 값을 null로 입력해야 한다.
	 *
	 * @return 배송 정보
	 */
	private static DeliveryType createDeliverySample() {
		DeliveryType delivery = new DeliveryType();
		delivery.setType("1"); // 배송 방법 유형 코드 (택배, 소포, 등기 / 직접배송)
		delivery.setBundleGroupAvailable("Y"); // 묶음 배송 가능 여부
		delivery.setBundleGroupId(127112L); // 묶음 배송 그룹 코드. 묶음 배송 가능한 경우 GetBundleGroupList API로 배송비 묶음 그룹 코드 조회하여 입력한다.
		delivery.setVisitAddressId(594L); // 방문수령 주소 코드. GetAddressBookList API로 주소 코드를 조회하여 입력한다.
		//delivery.setQuickServiceAreaList(new QuickServiceAreaListType());
		//delivery.getQuickServiceAreaList().getQuickServiceAreaCode().add("01"); // 퀵서비스 배송 지역 코드 입력
		//delivery.getQuickServiceAreaList().getQuickServiceAreaCode().add("02"); // 퀵서비스 배송 지역 코드 입력
		delivery.setFeeType("3"); // 배송비 유형 코드 (무료, 조건부 무료, 유료, 수량별 부과 - 반복구간, 수량별 부과 - 구간 직접 설정)
		delivery.setBaseFee(2000); // 기본 배송비
		// delivery.setFreeConditionalAmount(50000); // 무료 조건 금액. '조건부 무료'일 경우 입력한다.
		// delivery.setRepeatQuantity(2); // 반복 수량. '수량별 부과 - 반복구간'일 경우 입력한다.
		// delivery.setSecondBaseQuantity(5); // 2구간 최소 수량. '수량별 부과 - 구간직접설정'일 경우 입력한다.
		// delivery.setSecondExtraFee(2000); // 2구간 추가 배송비. '수량별 부과 - 구간직접설정'일 경우 입력한다.
		// delivery.setThirdBaseQuantity(10); // 3구간 최소 수량. '수량별 부과 - 구간직접설정'일 경우 입력한다.
		// delivery.setThirdExtraFee(2000); // 3구간 추가 배송비. '수량별 부과 - 구간직접설정'일 경우 입력한다.
		delivery.setPayType("3"); // 배송비 결제 방식 타입 코드. (착불, 선결제, 착불 또는 선결제)
		delivery.setAreaType("2"); // 지역별 추가 배송 권역 (2권역 - 내륙/제주 및 도서산간, 3권역 - 내륙/제주 외 도서 산간)
		delivery.setArea2ExtraFee(1000); // 2권역 배송비
		// delivery.setArea3ExtraFee(1000); // 3권역 배송비
		delivery.setReturnDeliveryCompanyPriority("0"); // 반품/교환 택배사. GetReturnsCompanyList API로 택배사 코드를 조회하여 입력한다.
		delivery.setReturnFee(2500); // 반품 배송비
		delivery.setExchangeFee(3000); // 교환 배송비
		delivery.setShippingAddressId(594L); // 출고지 주소 코드. GetAddressBookList API로 주소 코드를 조회하여 입력한다.
		delivery.setReturnAddressId(594L); // 반품/교환지 주소 코드. GetAddressBookList API로 주소 코드를 조회하여 입력한다.

		return delivery;
	}

	/**
	 * @return 판매자 즉시할인 샘플
	 */
	private static SellerDiscountType createSellerDiscountSample() {
		SellerDiscountType sellerDiscount = new SellerDiscountType();
		sellerDiscount.setAmount("10%"); // 즉시 할인액/할인율
		// sellerDiscount.setStartDate("2011-12-10"); // 즉시할인 시작일
		// sellerDiscount.setEndDate("2012-12-10"); // 즉시할인 종료일
		return sellerDiscount;
	}

	/**
	 * @return 복수구매할인 정보 샘플
	 */
	private static MultiPurchaseDiscountType createMultiPurchaseDiscountSample() {
		MultiPurchaseDiscountType multiPurchaseDiscount = new MultiPurchaseDiscountType();
		multiPurchaseDiscount.setAmount("1000"); // 복수 구매 할인액/할인율
		multiPurchaseDiscount.setOrderAmount("3개"); // 복수 구매 할인 금액/개수
		// multiPurchaseDiscount.setStartDate("2011-12-10"); // 복수 구매 할인 시작일
		// multiPurchaseDiscount.setEndDate("2012-12-10"); // 복수 구매 할인 종료일
		return multiPurchaseDiscount;
	}

	/**
	 * @return 네이버마일리지 정보 샘플
	 */
	private static MileageType createMileageSample() {
		MileageType mileage = new MileageType();
		mileage.setAmount("1000"); // 네이버마일리지 적립액/적립율.
//		mileage.setStartDate("2011-12-10"); // 네이버마일리지 시작일
//		mileage.setEndDate("2012-12-10"); // 네이버마일리지 종료일
		return mileage;
	}

	/**
	 * @return N stamp 정보 샘플
	 */
	private static StampType createStampSample() {
		StampType stamp = new StampType();
		stamp.setPurchaseReviewPaymentCount(5); // N stamp 지급 (구매평)
		stamp.setPremiumPaymentCount(10); // N stamp 지급 (프리미엄 구매평)
		stamp.setReqularCustomerPaymentCount(2); // N stamp 지급 (구독 회원)
		stamp.setStartDate("2011-12-10"); // N stamp 시작일
		stamp.setEndDate("2012-12-10"); // N stamp 종료일
		return stamp;
	}

	/**
	 * @return 무이자 할부 정보 샘플
	 */
	private static FreeInterestType createFreeInterestSample() {
		FreeInterestType freeInterest = new FreeInterestType();
		freeInterest.setMonth(12); // 무이자 할부 개월 수
		freeInterest.setStartDate("2019-08-12"); // 무이자 할부 시작일
		freeInterest.setEndDate("2020-12-10"); // 무이자 할부 종료일\
		return freeInterest;
	}

	/**
	 * @return 사은품 정보 샘플
	 */
	private static GiftType createGiftSample() {
		GiftType gift = new GiftType();
		gift.setName("하이브리드 자전거"); // 사은품
		return gift;
	}
	
	private static ProductSummaryType createProductSummarySample() {
		ProductSummaryType productSummary = new ProductSummaryType();
//		productSummary.setShoes(createshoesSummarySample());
		productSummary.setEtc(createEtcSummarySample());
		return productSummary;
	}
	
	private static ShoesSummaryType createshoesSummarySample() {
		ShoesSummaryType shoesSummaryType = new ShoesSummaryType();
		shoesSummaryType.setNoRefundReason("상품상세참고");
		shoesSummaryType.setReturnCostReason("상품상세참고");
		shoesSummaryType.setQualityAssuranceStandard("상품상세참고");
		shoesSummaryType.setCompensationProcedure("상품상세참고");
		shoesSummaryType.setTroubleShootingContents("상품상세참고");
		shoesSummaryType.setMaterial("상품상세참고");
		shoesSummaryType.setColor("상품상세참고");
		shoesSummaryType.setSize("상품상세참고");
		shoesSummaryType.setHeight("상품상세참고");
		shoesSummaryType.setManufacturer("상품상세참고");
		shoesSummaryType.setCaution("상품상세참고");
		shoesSummaryType.setWarrantyPolicy("상품상세참고");
		shoesSummaryType.setAfterServiceDirector("상품상세참고");
		return shoesSummaryType;
	}
	
	private static EtcSummaryType createEtcSummarySample() {
		EtcSummaryType etcSummaryType = new EtcSummaryType();
		etcSummaryType.setNoRefundReason("상품상세참고");
		etcSummaryType.setReturnCostReason("상품상세참고");
		etcSummaryType.setQualityAssuranceStandard("상품상세참고");
		etcSummaryType.setCompensationProcedure("상품상세참고");
		etcSummaryType.setTroubleShootingContents("상품상세참고");
		etcSummaryType.setItemName("상품상세참고");
		etcSummaryType.setModelName("상품상세참고");
		etcSummaryType.setManufacturer("상품상세참고");
		etcSummaryType.setCustomerServicePhoneNumber("상품상세참고");
		return etcSummaryType;
	}
	
	private static void productTest() throws SignatureException {
//		URL path = new File("/resources/ShopNAPI.wsdl");
//
//	    System.out.println(path.getAbsolutePath());
//		String claspathValue = System.getProperty("java.class.path");
//		System.out.println(claspathValue);

		// 상품 객체 생성
		ProductType product = new ProductType();
//		product.setProductId(2000652141L); // 상품을 수정하는 경우에 해당 상품 번호를 입력해야 합니다.
		product.setStatusType("SALE"); // 판매상태 (판매중, 판매중지)
		product.setSaleType("NEW"); // 상품판매유형 (신상품, 중고, 진열, 리퍼)
		product.setCustomMade("N"); //주문 제작 상품 여부 ( “Y” 또는 “N” ) 
		product.setCategoryId("50001721"); // 카테고리 코드 API로 조회한 카테고리를 입력해야 합니다.
		product.setName("커피자판기2"); // 상품명
		product.setPublicityPhraseContent("아이스크림제조기 행사 중"); // 홍보문구
		product.setSellerManagementCode("10000002"); // 판매자가 관리하는 상품 코드
		product.setSellerBarCode("10000002"); // 판매자 바코드
		product.setModel(createModelSample()); //모델정보
//		product.setCertificationList(createCertificationListSample()); // 인증정보
		product.setOriginArea(createOriginAreaSample()); // 원산지 정보
		product.setManufactureDate("2019-08-10"); // 제조일자
		product.setValidDate("2020-12-10"); // 유효일자
		product.setTaxType("DUTYFREE"); // 부가세 타입 코드
		product.setMinorPurchasable("Y"); // 미성년자 구매가능 여부
		product.setImage(createImageSample()); // 이미지 정보
		product.setDetailContent("<b>상품 상세 내용 입력2</b>"); // 상품 상세 내용.
		//product.setSellerNoticeId(10001L); // 상품 공지사항 번호. GetSellerNoticeList API를 호출하여 해당 판매자의 공지사항 번호를 조회할 수 있다.
		product.setAfterServiceTelephoneNumber("010-1111-2222"); // A/S 전화번호
		product.setAfterServiceGuideContent("1년 무상 A/S"); // A/S 내용. HTML 입력 불가
		
		product.setRegularCustomerExclusiveProduct("N"); // 구독회원 전용 상품 여부
		product.setKnowledgeShoppingProductRegistration("Y"); // 지식쇼핑 등록 여부. 지식쇼핑 광고주가 아닌 경우 N으로 저장된다.
		// product.setGalleryId(100L); // 갤러리 번호. GetGalleryList API를 호출하여 해당 판매자의 갤러리 번호를 조회할 수 있다.
		product.setSaleStartDate("2019-10-23"); // 판매시작일
		product.setSaleEndDate("2019-10-23"); // 판매종료일
		product.setSalePrice(50000); // 판매가(원)
		product.setStockQuantity(8000); // 재고수량
		
		// product.setMinPurchaseQuantity(2); // 최소구매수량. 2개부터 설정 가능하다. 최소구매수량이 1개인 경우 아예 입력하지 않아야 한다.
		
		// 2019.08.26 현재 사용안됨
		//product.setMaxPurchaseQuantity(100L); // 최대구매수량 -> 최대 구매수량 관련 내용 변경 됨 현재 사용안함
		//product.setMaxPurchaseQuantityRestrictType("TM"); // 최대 구매 가능 수량 제한 타입 코드 (1회 구매시, 1인 구매시)
		
//		product.setDelivery(createDeliverySample()); // 배송정보. 배송 없는 상품인 경우 아예 입력하지 않는다.
		//product.setSellerDiscount(createSellerDiscountSample()); // 판매자 즉시할인
		//product.setMultiPurchaseDiscount(createMultiPurchaseDiscountSample()); // 복수구매할인
		
		//product.setMileage(createMileageSample()); // 마일리지
		
		// 2019.08.26 현재 사용안됨
		//product.setStamp(createStampSample()); // N stamp
		
//		product.setFreeInterest(createFreeInterestSample()); // 무이자 할부
		//product.setGift(createGiftSample()); // 사은품
		
		product.setProductSummary(createProductSummarySample());// 상품 요약 정보 , 상품 등록 시 필수

		// ProductType을 ManageProductRequestType에 담는다.
		ManageProductRequestType request = new ManageProductRequestType();
		request.setRequestID("skstoaTest"); // 메시지를 식별할 수 있는 ID를 입력한다. 특별한 의미는 없으며 response에 해당 requestId가 담겨서 반환된다.
		request.setVersion("2.0"); // API 버전을 입력한다. 역시 특별한 의미는 없고 정보성 데이터이다.
		request.setAccessCredentials(createAccessCredentials("ProductService", "ManageProduct"));
 		request.setSellerId("ncp_1njgo6_02"); // 판매자 ID (Sandbox는 테스트용 판매자 Id를 입력한다)
 		request.setProduct(product);

		// ManageProduct 호출
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		ManageProductResponseType response = port.manageProduct(request);

		// Response에서 상품번호 확인
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("상품번호 : [" + response.getProductId() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}
	
	private static void allCategoryListTest() throws SignatureException {
		GetAllCategoryListRequestType request = new GetAllCategoryListRequestType();
		request.setRequestID("skstoaTest"); 
		request.setVersion("2.0"); 
		
		request.setAccessCredentials(createAccessCredentials("ProductService", "GetAllCategoryList"));
 		request.setLast("Y");

		// ManageProduct 호출
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		GetAllCategoryListResponseType response = port.getAllCategoryList(request);

		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("카테고리 개수 : [" + response.getCategoryList().get(0).getCategory().size() + "]");
			int cnt = 0;
			for(CategoryListType category : response.getCategoryList().get(0).getCategory()){
				System.out.println("전체 카테고리 명    : [" + category.getCategoryName() + "]");
				System.out.println("카테고리 ID : [" + category.getId() + "]");
				System.out.println("카테고리 명    : [" + category.getName() + "]");
				System.out.println("최하위 여부    : [" + category.getLast() + "]");
				cnt++;
				if(cnt>100){
					break;
				} 
			}
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}
	
	private static void getAllOriginAreaListTest() throws SignatureException {
	    GetAllOriginAreaListRequestType request = new GetAllOriginAreaListRequestType();
		request.setRequestID("skstoaTest"); 
		request.setVersion("2.0");
		request.setAccessCredentials(createAccessCredentials("ProductService", "GetAllOriginAreaList"));
		
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		GetAllOriginAreaListResponseType response = port.getAllOriginAreaList(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("원산지 개수 : [" + response.getOriginAreaList().size() + "]");
			int cnt = 0;
			for(StringCodeType originArea : response.getOriginAreaList().get(0).getOriginArea()){
				System.out.println("원산지 코드    : [" + originArea.getCode() + "]");
				System.out.println("원산지 명    : [" + originArea.getName() + "]");
				cnt++;
				if(cnt>1000){
					break;
				}
			}
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void getModelListTest() throws SignatureException {
		GetModelListRequestType request = new GetModelListRequestType();
		request.setRequestID("skstoaTest"); 
		request.setVersion("2.0");
		request.setModelId(6154900444L);
//		request.setModelName("A");
		request.setAccessCredentials(createAccessCredentials("ProductService", "GetModelList"));
		
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		GetModelListResponseType response = port.getModelList(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("모델 개수 : [" + response.getModelList().size() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}
	
	private static void getCategoryInfoTest() throws SignatureException {
		GetCategoryInfoRequestType request = new GetCategoryInfoRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");
		request.setCategoryId("50000409");// 50002350 50000761  50003804
		request.setAccessCredentials(createAccessCredentials("ProductService", "GetCategoryInfo"));
		
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		GetCategoryInfoResponseType response = port.getCategoryInfo(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("카테고리 정보  개수 : [" + response.getCategory().size() + "]");
			System.out.println("카테고리 전체명 : [" + response.getCategory().get(0).getCategoryName() + "]");
			System.out.println("카테고리 명 : [" + response.getCategory().get(0).getName() + "]");
			System.out.println("카테고리 최하위 여부 : [" + response.getCategory().get(0).getLast() + "]");
			System.out.println("카테고리 예외 정보 개수 : [" + response.getCategory().get(0).getExceptionalCategoryList().size() + "]");
			
//			for(StringCodeType codeMap : response.getCategory().get(0).getExceptionalCategoryList().get(0).getModel()){
//				System.out.println("카테고리 예외 정보 코드 : ["+ codeMap.getCode()+ "]");
//				System.out.println("카테고리 예외 정보 명 : ["+ codeMap.getName() + "]");
//			}
//			
			System.out.println("카테고리 속성 정보 개수 : [" + response.getCategory().get(0).getAttributeCategoryList().size() + "]");
			
//			for(AttributeCategoryType categoryType : response.getCategory().get(0).getAttributeCategoryList().get(0).getAttributeCategory()){
//				
//			}
//			
			System.out.println("카테고리 인증 유형 정보 개수 : [" + response.getCategory().get(0).getCertificationInfoList().size() + "]");
			System.out.println("카테고리 인증 유형 정보 상세 개수: ["+response.getCategory().get(0).getCertificationInfoList().get(0).getCertificationInfo().size()+ "]");
			
			// certificationInfoList 출력
			int cnt = 0;
			for(CertificationInfoType certificationInfoType: response.getCategory().get(0).getCertificationInfoList().get(0).getCertificationInfo()){
//				System.out.println("인증 유형 번호  : [" + certificationInfoType.getCode() + "]");
//				System.out.println("인증 유형명  : [" + certificationInfoType.getName()+ "]");
				for(int i=0; i<certificationInfoType.getKindTypeList().size(); i++){
						System.out.println("getCode    : [" + certificationInfoType.getKindTypeList().get(i).getKindType().get(0).getCode() + "]");	
						System.out.println("getName    : [" + certificationInfoType.getKindTypeList().get(i).getKindType().get(0).getName() + "]\r\n");					
					
				}
				cnt++;
				if(cnt>1000){
//					break;
				}
			}
			
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void getSubCategoryListTest() throws SignatureException {
		GetSubCategoryListRequestType request = new GetSubCategoryListRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");
		request.setCategoryId("50001455");
		request.setAccessCredentials(createAccessCredentials("ProductService", "GetSubCategoryList"));
		
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		GetSubCategoryListResponseType response = port.getSubCategoryList(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("모델 개수 : [" + response.getCategoryList().size() + "]");
			for(CategoryListType subCategory : response.getCategoryList().get(0).getCategory()){
				System.out.println("ID       : [" + subCategory.getId() + "]");
				System.out.println("카테고리 명      : [" + subCategory.getCategoryName() + "]");
				System.out.println("name     : [" + subCategory.getName() + "]");
				System.out.println("last     : [" + subCategory.getLast() + "]\r\n");
			}
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void getBrandListTest() throws SignatureException {
		GetBrandListRequestType request = new GetBrandListRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");
		request.setBrandName("a");
		request.setAccessCredentials(createAccessCredentials("ProductService", "GetBrandList"));
		
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		GetBrandListResponseType response = port.getBrandList(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("모델 개수 : [" + response.getBrandList().size() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void getManufacturerListTest() throws SignatureException {
		GetManufacturerListRequestType request = new GetManufacturerListRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");
		request.setManufacturerName("a");
		request.setAccessCredentials(createAccessCredentials("ProductService", "GetManufacturerList"));
		
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		GetManufacturerListResponseType response = port.getManufacturerList(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("모델 개수 : [" + response.getBrandList().size() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}
	
	private static void getSubOriginAreaListTest() throws SignatureException {
	    GetSubOriginAreaListRequestType request = new GetSubOriginAreaListRequestType();
		request.setRequestID("skstoaTest"); 
		request.setVersion("2.0");
		request.setAccessCredentials(createAccessCredentials("ProductService", "GetSubOriginAreaList"));
		
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		GetSubOriginAreaListResponseType response = port.getSubOriginAreaList(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("원산지 개수 : [" + response.getOriginAreaList().size() + "]");
			int cnt = 0;
			for(StringCodeType originArea : response.getOriginAreaList().get(0).getOriginArea()){
				System.out.println("원산지 코드    : [" + originArea.getCode() + "]");
				System.out.println("원산지 명    : [" + originArea.getName() + "]");
				cnt++;
				if(cnt>1000){
					break;
				}
			}
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}
	
	private static void getOriginAreaListTest() throws SignatureException {
	    GetOriginAreaListRequestType request = new GetOriginAreaListRequestType();
		request.setRequestID("skstoaTest"); 
		request.setVersion("2.0");
		request.setOriginAreaCode("0009710");
		request.setAccessCredentials(createAccessCredentials("ProductService", "GetOriginAreaList"));
		
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		GetOriginAreaListResponseType response = port.getOriginAreaList(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("원산지 개수 : [" + response.getOriginAreaList().size() + "]");
			int cnt = 0;
			for(StringCodeType originArea : response.getOriginAreaList().get(0).getOriginArea()){
				System.out.println("원산지 코드    : [" + originArea.getCode() + "]");
				System.out.println("원산지 명    : [" + originArea.getName() + "]");
				cnt++;
				if(cnt>1000){
					break;
				}
			}
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void getProductListTest() throws SignatureException {
		GetProductListRequestType request = new GetProductListRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");
		request.setSellerId("ncp_1njgo6_02");
//		request.setProductId(2000644328); //2000642318
		List<StatusType> stautsTypeList = new ArrayList<StatusType>();
		
		StatusType stautsType = new StatusType();
//		stautsType.setStatusType("WAIT");
//		stautsTypeList.add(stautsType);
		
//		stautsType.setStatusType("SALE");
//		stautsTypeList.add(stautsType);
		
//		stautsType.setStatusType("OSTK");
//		stautsTypeList.add(stautsType);
//		
//		stautsType.setStatusType("SUSP");
//		stautsTypeList.add(stautsType);
//		
//		stautsType.setStatusType("CLOSE");
//		stautsTypeList.add(stautsType);
//		
//		stautsType.setStatusType("PHB");
//		stautsTypeList.add(stautsType);
		
//		request.setStatusTypeList(stautsTypeList);
		
//		request.setFromDate("2019-09-09");
//		request.setToDate("2019-09-18");
		
		request.setAccessCredentials(createAccessCredentials("ProductService", "GetProductList"));
		
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		GetProductListResponseType response = port.getProductList(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("모델 개수 : [" + response.getProductList().size() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void getMobileImageInfoTest() throws SignatureException {
		GetMobileImageInfoRequestType request = new GetMobileImageInfoRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");
		request.setSellerId("ncp_1njgo6_02");
		request.setProductId(1000000000);
		request.setAccessCredentials(createAccessCredentials("ProductService", "GetMobileImageInfo"));
		
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		GetMobileImageInfoResponseType response = port.getMobileImageInfo(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("모델 개수 : [" + response + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void getProductTest() throws SignatureException {
		GetProductRequestType request = new GetProductRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");
		request.setSellerId("ncp_1njgo6_02"); //개발:ncp_1njgo6_02
		request.setProductId(2000662111);
		request.setAccessCredentials(createAccessCredentials("ProductService", "GetProduct"));
		
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		GetProductResponseType response = port.getProduct(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("상품 코드 : [" + response.getProduct().getProductId() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void getOptionTest() throws SignatureException {
		GetOptionRequestType request = new GetOptionRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");
		request.setSellerId("ncp_1njgo6_02");
		request.setProductId(2000642318);
		request.setAccessCredentials(createAccessCredentials("ProductService", "GetOption"));
		
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		GetOptionResponseType response = port.getOption(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("모델 개수 : [" + response + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

//	private static void getModificationRequiredProductListTest() throws SignatureException {
//		GetModificationRequiredProductListRequestType request = new GetModificationRequiredProductListRequestType();
//		request.setRequestID("skstoaTest1"); 
//		request.setVersion("2.0");
//		request.setSellerId("ncp_1njgo6_02");
//		request.setAccessCredentials(createAccessCredentials("ProductService", "GetModificationRequiredProductList"));
//		
//		ProductService productService = new ProductService();
//		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
//		GetModificationRequiredProductListResponseType response = port.getModificationRequiredProductList(request);
//		
//		if ("SUCCESS".equals(response.getResponseType())) {
//			System.out.println("모델 개수 : [" + response.getModificationRequiredProductList().size() + "]");
//		} else {
//			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
//			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
//			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
//		}
//	}
	
	private static void restoreProductTest() throws SignatureException {
	    RestoreProductRequestType request = new RestoreProductRequestType();
		request.setRequestID("skstoaTest"); 
		request.setVersion("2.0");
		request.setSellerId("ncp_1njgo6_02");
		request.setProductId(1000000000);
		request.setAccessCredentials(createAccessCredentials("ProductService", "RestoreProduct"));
		
		ProductService productService = new ProductService();
		ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
		RestoreProductResponseType response = port.restoreProduct(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("원산지 개수 : [" + response + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void uploadImageTest() throws SignatureException {
		UploadImageRequestType request = new UploadImageRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");
		request.setSellerId("ncp_1njgo6_02");
		ImageURLListType imageURLList = new ImageURLListType();
//		List<String> list = new ArrayList<>();
//		list.add("http://image.bshopping.co.kr/goods/626/20007626_g.jpg");
		imageURLList.getURL().add("http://image.bshopping.co.kr/goods/626/20007626_g.jpg");
		request.setImageURLList(imageURLList);
		request.setAccessCredentials(createAccessCredentials("ImageService", "UploadImage"));
		
		ImageService imageService = new ImageService();
		ImageServicePortType port = imageService.getImageServiceSOAP12PortHttp();
		UploadImageResponseType response = port.uploadImage(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("모델 개수 : [" + response.getResponseType() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void getQuestionAnswerListTest() throws SignatureException {
		GetQuestionAnswerListRequestType request = new GetQuestionAnswerListRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");
		
		request.setSellerId("ncp_1njgo6_02");
		request.setFromDate("2018-01-01");
		request.setToDate("2019-09-09");
		
		request.setAccessCredentials(createAccessCredentials("QuestionAnswerService", "GetQuestionAnswerList"));
		
		QuestionAnswerService questionAnswerService = new QuestionAnswerService();
		QuestionAnswerServicePortType port = questionAnswerService.getQuestionAnswerServiceSOAP12PortHttp();
		GetQuestionAnswerListResponseType response = port.getQuestionAnswerList(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("모델 개수 : [" + response.getQuestionAnswerList().size() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void GetAddressBookListTest() throws SignatureException {
		GetAddressBookListRequestType request = new GetAddressBookListRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");		
		request.setSellerId("ncp_1njgo6_02");		
		request.setAccessCredentials(createAccessCredentials("AddressBookService", "GetAddressBookList"));
		
		AddressBookService addressBookService = new AddressBookService();
		AddressBookServicePortType port = addressBookService.getAddressBookServiceSOAP12PortHttp();
		GetAddressBookListResponseType response = port.getAddressBookList(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("모델 개수 : [" + response.getAddressBookList().size() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void GetBundleGroupTest() throws SignatureException {
		GetBundleGroupRequestType request = new GetBundleGroupRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");		
		request.setSellerId("ncp_1njgo6_02");	
		request.setBundleGroupId(10000L);
		request.setAccessCredentials(createAccessCredentials("DeliveryInfoService", "GetBundleGroup"));
		
		DeliveryInfoService deliveryInfoService = new DeliveryInfoService();
		DeliveryInfoServicePortType port = deliveryInfoService.getDeliveryInfoServiceSOAP12PortHttp();
		GetBundleGroupResponseType response = port.getBundleGroup(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("모델 개수 : [" + response.GetBundleGroup().size() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void GetBundleGroupListTest() throws SignatureException {
		GetBundleGroupListRequestType request = new GetBundleGroupListRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");		
		request.setSellerId("ncp_1njgo6_02");	
		request.setAccessCredentials(createAccessCredentials("DeliveryInfoService", "GetBundleGroupList"));
		
		DeliveryInfoService deliveryInfoService = new DeliveryInfoService();
		DeliveryInfoServicePortType port = deliveryInfoService.getDeliveryInfoServiceSOAP12PortHttp();
		GetBundleGroupListResponseType response = port.getBundleGroupList(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("모델 개수 : [" + response.getBundleGroupList().size() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	private static void GetReturnsCompanyListTest() throws SignatureException {
		GetReturnsCompanyListRequestType request = new GetReturnsCompanyListRequestType();
		request.setRequestID("skstoaTest1"); 
		request.setVersion("2.0");		
		request.setSellerId("ncp_1njgo6_02");	
		request.setAccessCredentials(createAccessCredentials("DeliveryInfoService", "GetReturnsCompanyList"));
		
		DeliveryInfoService deliveryInfoService = new DeliveryInfoService();
		DeliveryInfoServicePortType port = deliveryInfoService.getDeliveryInfoServiceSOAP12PortHttp();
		GetReturnsCompanyListResponseType response = port.getReturnsCompanyList(request);
		
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("모델 개수 : [" + response.getReturnsCompanyList().size() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}
	
	/**
	 * 옵션 정보 샘플
	 * <p/>
	 * 단독형 옵션
	 * 단독형 옵션, 조합형 옵션, 직접 입력형 옵션 중 최소 한 개는 입력 단독형 옵션과 조합형 옵션은 함께 사용 불가
	 *
	 * @return 원산지 정보
	 */
	private static SimpleOptionItemListType createSimpleOptionItemSample() {
		SimpleOptionItemType simpleOptionItem1 = new SimpleOptionItemType();
		simpleOptionItem1.setId(10000L); // 옵션 ID
		simpleOptionItem1.setName(""); // 옵션명
		simpleOptionItem1.setValue(""); // 옵션값
		simpleOptionItem1.setUsable("N"); // 사용 여부 (“Y” 또는 “N”)
		
		SimpleOptionItemListType SimpleOptionItemList = new SimpleOptionItemListType();
		SimpleOptionItemList.getSimple().add(simpleOptionItem1);

		return SimpleOptionItemList;
	}

	/**
	 * 옵션 정보 샘플
	 * <p/>
	 * 조합형 옵션
	 * 단독형 옵션, 조합형 옵션, 직접 입력형 옵션 중 최소 한 개는 입력 단독형 옵션과 조합형 옵션은 함께 사용 불가
	 *
	 * @return 원산지 정보
	 */
	private static CombinationOptionType createCombinationOptionSample() {
		List<CombinationOptionItemType> list = new ArrayList<CombinationOptionItemType>();
		CombinationOptionNamesType combinationOptionNames = null;
		CombinationOptionItemType combinationOptionItem = null;
		
		combinationOptionNames = new CombinationOptionNamesType();
		combinationOptionNames.setName1("NAME1"); // 옵션명 1
//		combinationOptionNames.setName2(); // 옵션명 2
//		combinationOptionNames.setName3(); // 옵션명 3

		for(int i=0; i<100; i++){
			combinationOptionItem = new CombinationOptionItemType();
//			combinationOptionItem.setId(); // 옵션 ID
			combinationOptionItem.setValue1("value"+(i+1)); // CombinationOptionNamesType 의 옵션명 1 에 해당하는 옵션값
//			combinationOptionItem.setValue2();
//			combinationOptionItem.setValue3();
//			combinationOptionItem.setPrice(); // 옵션가
			combinationOptionItem.setQuantity(1000L); // 재고 수량
			combinationOptionItem.setSellerManagerCode("2000642318"); // 판매자 관리 코드
			combinationOptionItem.setUsable("Y"); // 사용 여부 (“Y” 또는 “N”)	
			list.add(combinationOptionItem);
		}		
				
		CombinationOptionItemListType combinationOptionItemList = new CombinationOptionItemListType();
		combinationOptionItemList.setItem(list);
		
		CombinationOptionType combinationOption = new CombinationOptionType();
		combinationOption.setNames(combinationOptionNames); // 옵션명 목록
		combinationOption.setItemList(combinationOptionItemList); // 옵션 목록
		return combinationOption;
	}
	
	/**
	 * 옵션 정보 샘플
	 * <p/>
	 * 직접 입력형 옵션
	 * 단독형 옵션, 조합형 옵션, 직접 입력형 옵션 중 최소 한 개는 입력 단독형 옵션과 조합형 옵션은 함께 사용 불가
	 *
	 * @return 원산지 정보
	 */
	private static CustomOptionItemListType createCustomOptionItemSample() {
		CustomOptionItemType customOptionItem1 = new CustomOptionItemType();
//		customOptionItem1.setId();
//		customOptionItem1.setName();
//		customOptionItem1.setUsable();
		
		CustomOptionItemListType customOptionItemList = new CustomOptionItemListType();
		customOptionItemList.getCustom().add(customOptionItem1);
		
		return customOptionItemList;
	}
	
	private static void optionTest() throws SignatureException {

		// 옵션 객체 생성
		OptionType option = new OptionType();
		option.setProductId(2000642318);
		option.setSortType("CRE"); // CRE 등록순(디폴트) / ABC 가나다순 / LOPRC 낮은 가격순 / HIPRC 높은 가격순
//		option.setSimpleList(createSimpleOptionItemSample());
		option.setCombination(createCombinationOptionSample());
//		option.setCustomList(createCustomOptionItemSample());

		// OptionType을 ManageOptionRequestType에 담는다.
		ManageOptionRequestType request = new ManageOptionRequestType();
		request.setRequestID("skstoaTest");
		request.setVersion("2.0");
		request.setAccessCredentials(createAccessCredentials("ProductService", "ManageOption"));
 		request.setSellerId("ncp_1njgo6_02");
 		request.setOption(option);

		// ManageProduct 호출
		ProductService optionService = new ProductService();
		ProductServicePortType port = optionService.getProductServiceSOAP12PortHttp();
		ManageOptionResponseType response = port.manageOption(request);

		// Response에서 상품번호 확인
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("상품번호 : [" + response.getProductId() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}
	
	private static void ChangeProductSaleStatusTest() throws SignatureException {

		// SaleStatusType 객체 생성
		SaleStatusType saleStatus = new SaleStatusType();
		saleStatus.setProductId(2000662111); // 상품 ID
		saleStatus.setStatusType("OSTK"); // 판매 상태 (SALE(판매 중), SUSP(판매 중지), OSTK(품절)만 입력할 수 있다.)
//		saleStatus.setSaleStartDate(); // 판매 시작일
//		saleStatus.setSaleEndDate(); // 판매 종료일
//		saleStatus.setStockQuantity(); // 재고 수량
		
		// SaleStatusType을 ChangeProductSaleStatusRequestType에 담는다.
		ChangeProductSaleStatusRequestType request = new ChangeProductSaleStatusRequestType();
		request.setRequestID("skstoaTest");
		request.setVersion("2.0");
		request.setAccessCredentials(createAccessCredentials("ProductService", "ChangeProductSaleStatus"));
 		request.setSellerId("ncp_1njgo6_02");
 		request.setSaleStatus(saleStatus);

		// ManageProduct 호출
		ProductService optionService = new ProductService();
		ProductServicePortType port = optionService.getProductServiceSOAP12PortHttp();
		ChangeProductSaleStatusResponseType response = port.changeProductSaleStatus(request);

		// Response에서 상품번호 확인
		if ("SUCCESS".equals(response.getResponseType())) {
			System.out.println("상품번호 : [" + response.getProductId() + "]");
		} else {
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}

	
	public static void main(String args[]) throws SignatureException {
		// 상품 등록 및 수정
//		productTest();		
		// 전체 카테고리 목록을 조회
//		allCategoryListTest();		
		// 원산지 코드 전체를 조회
//		getAllOriginAreaListTest();		
		// 모델 코드의 목록을 조회
//		getModelListTest();		
		// 특정 카테고리의 상세 정보를 조회
//		getCategoryInfoTest();		
		// 카테고리 정보를 계층구조로 조회
//		getSubCategoryListTest();		
		// 브랜드 이름의 목록을 조회
//		getBrandListTest();		
		// 제조사 이름의 목록을 조회
//		getManufacturerListTest();		
		// 하위 원산지 코드 정보를 조회
//		getSubOriginAreaListTest();		
		// 원산지 코드 정보 조회
//		getOriginAreaListTest();		
		
		// 판매자의 상품 목록 조회
//		getProductListTest(); // 에러 메시지 : [요청하신 상품번호에 해당하는 상품이 존재하지 않거나 해당 판매자의 상품이 아닙니다.]		
		// 상품의 모바일 상세 정보를 조회
//		getMobileImageInfoTest(); // 에러 메시지 : [요청하신 상품번호에 해당하는 상품이 존재하지 않거나 해당 판매자의 상품이 아닙니다.]		
		// 상품 정보 조회
//		getProductTest(); // 상품코드 : 2000642318		
		// 옵션 정보 조회
//		getOptionTest(); // 에러 메시지 : [요청하신 상품번호에 해당하는 상품이 존재하지 않거나 해당 판매자의 상품이 아닙니다.]		
		
		// 판매자별 수정 요청 상품 목록을 조회 
//		getModificationRequiredProductListTest(); // 모델 개수 : [0]		
		// [판매자]수정 요청 상품에 대해 복원을 요청
//		restoreProductTest(); // 에러 메시지 : [요청하신 상품번호에 해당하는 상품이 존재하지 않거나 해당 판매자의 상품이 아닙니다.]		
		
		// 상품 등록 시 사용할 이미지를 업로드
//		uploadImageTest();		
		
		// 판매자의 상품에 등록된 Q&A 목록을 조회
		getQuestionAnswerListTest(); // 모델 개수 : [0]		
		// 판매자의 주소 목록을 조회
//		GetAddressBookListTest();		
		// 묶음 배송 그룹 정보를 조회
//		GetBundleGroupTest(); // 에러 메시지 : [권한이 없습니다.]		
		// 묶음 배송 그룹 코드 목록을 조회
//		GetBundleGroupListTest();		
		
		// 반품/교환 택배사 코드 목록 조회
//		GetReturnsCompanyListTest();		
		//옵션 등록 및 수정
//		optionTest();		
		// 상품 판매 상태를 수정
//		ChangeProductSaleStatusTest();

	}
}
