package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class Product {
	
	// 매입처 상품명
	private String supplierProductName;
	// 판매 수수료 타입
	private String commissionRateType;
	// 프로모션 적용 가능여부
	private String promotionYn;
	// 상품상세 헤더(HTML)
	private String contentHeader;
	// 매입가(유효상황: 판매방식-사입, 옵션-있음 or 없음)/공급가(유효상황: 판매방식-위탁, 판매수수료-공급가입력, 옵션-없음)
	private String purchasePrice;
	// 상품명
	private String productName;
	// 티딜 상품번호
	private String mallProductNo;
	// 배송 여부(default: N)
	private String deliveryYn;
	// 가격비교 사이트 타입
	private String comparingPriceSiteTypes;
	// 프로모션 홍보문구 노출 종료(YYYY-MM-DD HH:00:00) - 프로모션 사용 시 입력
	private String promotionTextEndYmdt;
	// 미성년자 구매가능 여부
	private String minorPurchaseYn;
	// 표준카테고리
	private String categoryNo;
	// 장바구니 사용 여부(default : Y)
	private String cartUseYn;
	// 검색어(,로 구분)
	private String keyword;
	// 브랜드 번호 (default : 0 / null 또는 0이 아닌 경우, brandName보다 우선으로 반영)
	private String brandNo;
	// 단위 가격
	private Long unitPrice;
	// 브랜드 이름 (brandNo가 우선으로 반영 - brandNo가 null or 0이 아닌 경우, brandNo 값이 없으면 해당 상품의 브랜드는 '없음'으로 반영)
	private String brandName;
	// 1회최대구매수량 (없으면 0)
	private int maxBuyTimeCnt;
	// 단위 값
	private String unitName;
	// 원산지 직접입력 값
	private String placeOrigin;
	// 리스트 이미지 URL(productListImageInfo 사용 시, null)
	private String productListImage;
	// 최대구매기간(수량, 없으면 0)
	private int maxBuyPeriodCnt;
	// 적립금 사용여부 (default : Y)
	private String accumulationUseYn;
	// 출고유형 [ PARTNER_SHIPPING_AREA: Partner Shipping, MALL_SHIPPING_AREA: Mall Shipping ]
	private String shippingAreaType;
	// 즉시할인 단위 [ WON: One, RATE: % ]
	private String immediateDiscountUnitType;
	// 환불가능여부 (default : Y)
	private String refundableYn;
	// 적립률(%), 사용하지 않을 경우 null
	private Double accumulationRate;
	// 단위
	private String unitNameType;
	// 재입고 알림 사용설정 (Y: 사용, N: 미사용 - default)
	private String useRestockNotiYn;
	// 판매시작일(YYYY-MMDD HH:00:00)
	private String saleStartYmdt;
	// 상품분류 [ DEFAULT: General product, OFFLINE: Offline product, RENTAL: Rental Product ]
	private String classType;
	// 추가 정보
	private String extraInfo;
	// 플랫폼 - 검색엔진 노출 여부 (PC, 모바일 웹, 모바일 앱, 검색엔진 중 적어도 하나는 노출되어야 함. default : N)
	private String searchengineDisplayYn;
	// 판매자 관리코드
	private String productManagementCd;
	// 최소구매수량 (2이상만 입력하세요. 없으면 0)
	private int minBuyCnt;
	// 판매수수료 비율 - 파트너 수수료인 경우는 파트너 계약 수수료로 들어갑니다
	private Double commissionRate;
	// 상품군 (default :DELIVERY) [ DELIVERY: Delivery Product Group, SERVICE: Service Product Group ]
	private String groupType;
	// 프로모션 기간 사용 여부 (default : N)
	private String promotionTextYn;
	// 플랫폼 - 모바일 웹 노출 여부 (PC, 모바일 웹, 모바일 앱, 검색엔진 중 적어도 하나는 노출되어야 함. default : N)
	private String platformDisplayMobileWebYn;
	// 1인최대구매수량 (없으면 0)
	private int maxBuyPersonCnt;
	// 상품정보고시(json 문자열)
	private String dutyInfo;
	// 옵션타입이 조합형일 경우 옵션 재고의 합이 자동으로 입력됨(단독형일 경우에만 전송, 조합형일 경우 0 / default : 0)
	private int productStockCnt;
	// 추가관리코드(옵션없음 상품의 추가관리코드를 설정하는 필드)
	private String extraManagementCd;
	// 플랫폼 - 모바일 앱 노출 여부 (PC, 모바일 웹, 모바일 앱, 검색엔진 중 적어도 하나는 노출되어야 함. default : N)
	private String platformDisplayMobileYn;
	// 플랫폼 노출 설정 여부('Y’일 경우 전체, 'N’일 경우 개별 default : N)
	private String platformDisplayYn;
	// 묶음배송 가능 여부
	private String deliveryCombinationYn;
	// 상품상세 본문
	private String content;
	// 비회원 구매가능 여부
	private String nonmemberPurchaseYn;
	// 전시카테고리
	private List<String> displayCategoryNo;
	// 판매방식 ( default - CONSIGNMENT ) [ PURCHASE: purchase, CONSIGNMENT: Consignment ]
	private String saleMethodType;
	// 정산시 파트너 분담금 (default : 0)
	private int partnerChargeAmt;
	// 해외배송 여부
	private String deliveryInternationalYn;
	// 리스트 이미지 정보(외부 이미지 여부, 이미지 타입 여부 설정)
	private ProductListImageInfo productListImageInfo;
	// 옵션 사용 유무 (default : false)
	@JsonProperty("isOptionUsed")
	private boolean isOptionUsed;
	// 배송관련 판매자 특이사항/고객안내사항
	private String deliveryCustomerInfo;
	// 등록된 옵션이미지사용. default = N
	private String addOptionImageYn;
	// 배송비 템플릿 번호(없을 경우 기본 배송비 템플릿 번호로 매핑됩니다) 배송 여부가 '배송안함’일 경우 0
	private String deliveryTemplateNo;
	// 즉시할인 값 (default: 0)
	private long immediateDiscountValue;
	// 원본 이미지 url 그대로 사용하는지 여부(nhn cdn 사용 X) default : false
	private String useOriginProductImageUrl;
	// 유효기간(유통기한) (YYYY-MM-DD HH:00:00)
	private String expirationYmdt;
	// 환불가능 세부정보
	private RefundableInfo refundableInfo;
	// 즉시할인 기간 설정 여부 (default : N)
	private String immediateDiscountPeriodYn;
	// 상품상세 푸터(HTML)
	private String contentFooter;
	// 플랫폼 - PC 노출 여부 (PC, 모바일 웹, 모바일 앱, 검색엔진 중 적어도 하나는 노출되어야 함. default: N)
	private String platformDisplayPcYn;
	// 판매가
	private long salePrice;
	// 제조일자(YYYY-MM-DD HH:00:00)
	private String manufactureYmdt;
	// 옵션 정보
	private OptionData optionData;
	// 즉시할인 시작일자(YYYYMM-DD HH:00:00)
	private String immediateDiscountStartYmdt;
	// 판매종료일(YYYY-MMDD HH:00:00)
	private String saleEndYmdt;
	// 판매기간설정  [ REGULAR: regular sale, PERIOD: Period Sale ]
	private String salePeriodType;
	// 과세 적용 기준 [ DUTY: Tax, DUTYFREE: dutyfree, SMALL: small ]
	private String valueAddedTaxType;
	// 홍보문구
	private String promotionText;
	private List<ProductImages> productImages;
	// hs code
	private String hsCode;
	// 즉시할인 종료일자(YYYYMM-DD HH:00:00)
	private String immediateDiscountEndYmdt;
	// 최대구매기간(일, 없으면 0)
	private int maxBuyDays;
	// 영문상품명
	private String productNameEn;
	// 프로모션 홍보문구 노출 시작일(YYYY-MM-DD HH:00:00) - 프로모션 사용 시 입력
	private String promotionTextStartYmdt;
	//상품항목추가정보
	private List<CustomProperty> customProperty;
	
	public List<CustomProperty> getCustomProperty() {
		return customProperty;
	}
	public void setCustomProperty(List<CustomProperty> customProperty) {
		this.customProperty = customProperty;
	}
	public String getSupplierProductName() {
		return supplierProductName;
	}
	public void setSupplierProductName(String supplierProductName) {
		this.supplierProductName = supplierProductName;
	}
	public String getCommissionRateType() {
		return commissionRateType;
	}
	public void setCommissionRateType(String commissionRateType) {
		this.commissionRateType = commissionRateType;
	}
	public String getPromotionYn() {
		return promotionYn;
	}
	public void setPromotionYn(String promotionYn) {
		this.promotionYn = promotionYn;
	}
	public String getContentHeader() {
		return contentHeader;
	}
	public void setContentHeader(String contentHeader) {
		this.contentHeader = contentHeader;
	}
	public String getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getDeliveryYn() {
		return deliveryYn;
	}
	public void setDeliveryYn(String deliveryYn) {
		this.deliveryYn = deliveryYn;
	}
	public String getComparingPriceSiteTypes() {
		return comparingPriceSiteTypes;
	}
	public void setComparingPriceSiteTypes(String comparingPriceSiteTypes) {
		this.comparingPriceSiteTypes = comparingPriceSiteTypes;
	}
	public String getPromotionTextEndYmdt() {
		return promotionTextEndYmdt;
	}
	public void setPromotionTextEndYmdt(String promotionTextEndYmdt) {
		this.promotionTextEndYmdt = promotionTextEndYmdt;
	}
	public String getMinorPurchaseYn() {
		return minorPurchaseYn;
	}
	public void setMinorPurchaseYn(String minorPurchaseYn) {
		this.minorPurchaseYn = minorPurchaseYn;
	}
	public String getCategoryNo() {
		return categoryNo;
	}
	public void setCategoryNo(String categoryNo) {
		this.categoryNo = categoryNo;
	}
	public String getCartUseYn() {
		return cartUseYn;
	}
	public void setCartUseYn(String cartUseYn) {
		this.cartUseYn = cartUseYn;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getBrandNo() {
		return brandNo;
	}
	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
	public Long getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Long unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public int getMaxBuyTimeCnt() {
		return maxBuyTimeCnt;
	}
	public void setMaxBuyTimeCnt(int maxBuyTimeCnt) {
		this.maxBuyTimeCnt = maxBuyTimeCnt;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getPlaceOrigin() {
		return placeOrigin;
	}
	public void setPlaceOrigin(String placeOrigin) {
		this.placeOrigin = placeOrigin;
	}
	public String getProductListImage() {
		return productListImage;
	}
	public void setProductListImage(String productListImage) {
		this.productListImage = productListImage;
	}
	public int getMaxBuyPeriodCnt() {
		return maxBuyPeriodCnt;
	}
	public void setMaxBuyPeriodCnt(int maxBuyPeriodCnt) {
		this.maxBuyPeriodCnt = maxBuyPeriodCnt;
	}
	public String getAccumulationUseYn() {
		return accumulationUseYn;
	}
	public void setAccumulationUseYn(String accumulationUseYn) {
		this.accumulationUseYn = accumulationUseYn;
	}
	public String getShippingAreaType() {
		return shippingAreaType;
	}
	public void setShippingAreaType(String shippingAreaType) {
		this.shippingAreaType = shippingAreaType;
	}
	public String getImmediateDiscountUnitType() {
		return immediateDiscountUnitType;
	}
	public void setImmediateDiscountUnitType(String immediateDiscountUnitType) {
		this.immediateDiscountUnitType = immediateDiscountUnitType;
	}
	public String getRefundableYn() {
		return refundableYn;
	}
	public void setRefundableYn(String refundableYn) {
		this.refundableYn = refundableYn;
	}
	public Double getAccumulationRate() {
		return accumulationRate;
	}
	public void setAccumulationRate(Double accumulationRate) {
		this.accumulationRate = accumulationRate;
	}
	public String getUnitNameType() {
		return unitNameType;
	}
	public void setUnitNameType(String unitNameType) {
		this.unitNameType = unitNameType;
	}
	public String getUseRestockNotiYn() {
		return useRestockNotiYn;
	}
	public void setUseRestockNotiYn(String useRestockNotiYn) {
		this.useRestockNotiYn = useRestockNotiYn;
	}
	public String getSaleStartYmdt() {
		return saleStartYmdt;
	}
	public void setSaleStartYmdt(String saleStartYmdt) {
		this.saleStartYmdt = saleStartYmdt;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public String getExtraInfo() {
		return extraInfo;
	}
	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
	public String getSearchengineDisplayYn() {
		return searchengineDisplayYn;
	}
	public void setSearchengineDisplayYn(String searchengineDisplayYn) {
		this.searchengineDisplayYn = searchengineDisplayYn;
	}
	public String getProductManagementCd() {
		return productManagementCd;
	}
	public void setProductManagementCd(String productManagementCd) {
		this.productManagementCd = productManagementCd;
	}
	public int getMinBuyCnt() {
		return minBuyCnt;
	}
	public void setMinBuyCnt(int minBuyCnt) {
		this.minBuyCnt = minBuyCnt;
	}
	 
	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getPromotionTextYn() {
		return promotionTextYn;
	}
	public void setPromotionTextYn(String promotionTextYn) {
		this.promotionTextYn = promotionTextYn;
	}
	public String getPlatformDisplayMobileWebYn() {
		return platformDisplayMobileWebYn;
	}
	public void setPlatformDisplayMobileWebYn(String platformDisplayMobileWebYn) {
		this.platformDisplayMobileWebYn = platformDisplayMobileWebYn;
	}
	public int getMaxBuyPersonCnt() {
		return maxBuyPersonCnt;
	}
	public void setMaxBuyPersonCnt(int maxBuyPersonCnt) {
		this.maxBuyPersonCnt = maxBuyPersonCnt;
	}
	public String getDutyInfo() {
		return dutyInfo;
	}
	public void setDutyInfo(String dutyInfo) {
		this.dutyInfo = dutyInfo;
	}
	public int getProductStockCnt() {
		return productStockCnt;
	}
	public void setProductStockCnt(int productStockCnt) {
		this.productStockCnt = productStockCnt;
	}
	public String getExtraManagementCd() {
		return extraManagementCd;
	}
	public void setExtraManagementCd(String extraManagementCd) {
		this.extraManagementCd = extraManagementCd;
	}
	public String getPlatformDisplayMobileYn() {
		return platformDisplayMobileYn;
	}
	public void setPlatformDisplayMobileYn(String platformDisplayMobileYn) {
		this.platformDisplayMobileYn = platformDisplayMobileYn;
	}
	public String getPlatformDisplayYn() {
		return platformDisplayYn;
	}
	public void setPlatformDisplayYn(String platformDisplayYn) {
		this.platformDisplayYn = platformDisplayYn;
	}
	public String getDeliveryCombinationYn() {
		return deliveryCombinationYn;
	}
	public void setDeliveryCombinationYn(String deliveryCombinationYn) {
		this.deliveryCombinationYn = deliveryCombinationYn;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getNonmemberPurchaseYn() {
		return nonmemberPurchaseYn;
	}
	public void setNonmemberPurchaseYn(String nonmemberPurchaseYn) {
		this.nonmemberPurchaseYn = nonmemberPurchaseYn;
	}
	public List<String> getDisplayCategoryNo() {
		return displayCategoryNo;
	}
	public void setDisplayCategoryNo(List<String> displayCategoryNo) {
		this.displayCategoryNo = displayCategoryNo;
	}
	public String getSaleMethodType() {
		return saleMethodType;
	}
	public void setSaleMethodType(String saleMethodType) {
		this.saleMethodType = saleMethodType;
	}
	public int getPartnerChargeAmt() {
		return partnerChargeAmt;
	}
	public void setPartnerChargeAmt(int partnerChargeAmt) {
		this.partnerChargeAmt = partnerChargeAmt;
	}
	public String getDeliveryInternationalYn() {
		return deliveryInternationalYn;
	}
	public void setDeliveryInternationalYn(String deliveryInternationalYn) {
		this.deliveryInternationalYn = deliveryInternationalYn;
	}
	public ProductListImageInfo getProductListImageInfo() {
		return productListImageInfo;
	}
	public void setProductListImageInfo(ProductListImageInfo productListImageInfo) {
		this.productListImageInfo = productListImageInfo;
	}
	public boolean isOptionUsed() {
		return isOptionUsed;
	}
	public void setIsOptionUsed(boolean isOptionUsed) {
		this.isOptionUsed = isOptionUsed;
	}
	public String getDeliveryCustomerInfo() {
		return deliveryCustomerInfo;
	}
	public void setDeliveryCustomerInfo(String deliveryCustomerInfo) {
		this.deliveryCustomerInfo = deliveryCustomerInfo;
	}
	public String getAddOptionImageYn() {
		return addOptionImageYn;
	}
	public void setAddOptionImageYn(String addOptionImageYn) {
		this.addOptionImageYn = addOptionImageYn;
	}
	public String getDeliveryTemplateNo() {
		return deliveryTemplateNo;
	}
	public void setDeliveryTemplateNo(String deliveryTemplateNo) {
		this.deliveryTemplateNo = deliveryTemplateNo;
	}
	public long getImmediateDiscountValue() {
		return immediateDiscountValue;
	}
	public void setImmediateDiscountValue(long immediateDiscountValue) {
		this.immediateDiscountValue = immediateDiscountValue;
	}
	public String getUseOriginProductImageUrl() {
		return useOriginProductImageUrl;
	}
	public void setUseOriginProductImageUrl(String useOriginProductImageUrl) {
		this.useOriginProductImageUrl = useOriginProductImageUrl;
	}
	public String getExpirationYmdt() {
		return expirationYmdt;
	}
	public void setExpirationYmdt(String expirationYmdt) {
		this.expirationYmdt = expirationYmdt;
	}
	public RefundableInfo getRefundableInfo() {
		return refundableInfo;
	}
	public void setRefundableInfo(RefundableInfo refundableInfo) {
		this.refundableInfo = refundableInfo;
	}
	public String getImmediateDiscountPeriodYn() {
		return immediateDiscountPeriodYn;
	}
	public void setImmediateDiscountPeriodYn(String immediateDiscountPeriodYn) {
		this.immediateDiscountPeriodYn = immediateDiscountPeriodYn;
	}
	public String getContentFooter() {
		return contentFooter;
	}
	public void setContentFooter(String contentFooter) {
		this.contentFooter = contentFooter;
	}
	public String getPlatformDisplayPcYn() {
		return platformDisplayPcYn;
	}
	public void setPlatformDisplayPcYn(String platformDisplayPcYn) {
		this.platformDisplayPcYn = platformDisplayPcYn;
	}
	public long getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(long salePrice) {
		this.salePrice = salePrice;
	}
	public String getManufactureYmdt() {
		return manufactureYmdt;
	}
	public void setManufactureYmdt(String manufactureYmdt) {
		this.manufactureYmdt = manufactureYmdt;
	}
	public OptionData getOptionData() {
		return optionData;
	}
	public void setOptionData(OptionData optionData) {
		this.optionData = optionData;
	}
	public String getImmediateDiscountStartYmdt() {
		return immediateDiscountStartYmdt;
	}
	public void setImmediateDiscountStartYmdt(String immediateDiscountStartYmdt) {
		this.immediateDiscountStartYmdt = immediateDiscountStartYmdt;
	}
	public String getSaleEndYmdt() {
		return saleEndYmdt;
	}
	public void setSaleEndYmdt(String saleEndYmdt) {
		this.saleEndYmdt = saleEndYmdt;
	}
	public String getValueAddedTaxType() {
		return valueAddedTaxType;
	}
	public void setValueAddedTaxType(String valueAddedTaxType) {
		this.valueAddedTaxType = valueAddedTaxType;
	}
	public String getPromotionText() {
		return promotionText;
	}
	public void setPromotionText(String promotionText) {
		this.promotionText = promotionText;
	}
	public List<ProductImages> getProductImages() {
		return productImages;
	}
	public void setProductImages(List<ProductImages> productImages) {
		this.productImages = productImages;
	}
	public String getHsCode() {
		return hsCode;
	}
	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}
	public String getImmediateDiscountEndYmdt() {
		return immediateDiscountEndYmdt;
	}
	public void setImmediateDiscountEndYmdt(String immediateDiscountEndYmdt) {
		this.immediateDiscountEndYmdt = immediateDiscountEndYmdt;
	}
	public int getMaxBuyDays() {
		return maxBuyDays;
	}
	public void setMaxBuyDays(int maxBuyDays) {
		this.maxBuyDays = maxBuyDays;
	}
	public String getProductNameEn() {
		return productNameEn;
	}
	public void setProductNameEn(String productNameEn) {
		this.productNameEn = productNameEn;
	}
	public String getPromotionTextStartYmdt() {
		return promotionTextStartYmdt;
	}
	public void setPromotionTextStartYmdt(String promotionTextStartYmdt) {
		this.promotionTextStartYmdt = promotionTextStartYmdt;
	}
	
	@Override
	public String toString() {
		return "Products [supplierProductName=" + supplierProductName + ", commissionRateType=" + commissionRateType
				+ ", promotionYn=" + promotionYn + ", contentHeader=" + contentHeader + ", purchasePrice="
				+ purchasePrice + ", productName=" + productName + ", deliveryYn=" + deliveryYn
				+ ", comparingPriceSiteTypes=" + comparingPriceSiteTypes + ", promotionTextEndYmdt="
				+ promotionTextEndYmdt + ", minorPurchaseYn=" + minorPurchaseYn + ", categoryNo=" + categoryNo
				+ ", cartUseYn=" + cartUseYn + ", keyword=" + keyword + ", brandNo=" + brandNo + ", unitPrice="
				+ unitPrice + ", brandName=" + brandName + ", maxBuyTimeCnt=" + maxBuyTimeCnt + ", unitName=" + unitName
				+ ", placeOrigin=" + placeOrigin + ", productListImage=" + productListImage + ", maxBuyPeriodCnt="
				+ maxBuyPeriodCnt + ", accumulationUseYn=" + accumulationUseYn + ", shippingAreaType="
				+ shippingAreaType + ", immediateDiscountUnitType=" + immediateDiscountUnitType + ", refundableYn="
				+ refundableYn + ", accumulationRate=" + accumulationRate + ", unitNameType=" + unitNameType
				+ ", useRestockNotiYn=" + useRestockNotiYn + ", saleStartYmdt=" + saleStartYmdt + ", classType="
				+ classType + ", extraInfo=" + extraInfo + ", searchengineDisplayYn=" + searchengineDisplayYn
				+ ", productManagementCd=" + productManagementCd + ", minBuyCnt=" + minBuyCnt + ", commissionRate="
				+  ", groupType=" + groupType + ", promotionTextYn=" + promotionTextYn
				+ ", platformDisplayMobileWebYn=" + platformDisplayMobileWebYn + ", maxBuyPersonCnt=" + maxBuyPersonCnt
				+ ", dutyInfo=" + dutyInfo + ", productStockCnt=" + productStockCnt + ", extraManagementCd="
				+ extraManagementCd + ", platformDisplayMobileYn=" + platformDisplayMobileYn + ", platformDisplayYn="
				+ platformDisplayYn + ", deliveryCombinationYn=" + deliveryCombinationYn + ", content=" + content
				+ ", nonmemberPurchaseYn=" + nonmemberPurchaseYn + ", displayCategoryNo=" + displayCategoryNo
				+ ", saleMethodType=" + saleMethodType + ", partnerChargeAmt=" + partnerChargeAmt
				+ ", deliveryInternationalYn=" + deliveryInternationalYn + ", productListImageInfo="
				+ productListImageInfo + ", isOptionUsed=" + isOptionUsed + ", deliveryCustomerInfo="
				+ deliveryCustomerInfo + ", addOptionImageYn=" + addOptionImageYn + ", deliveryTemplateNo="
				+ deliveryTemplateNo + ", immediateDiscountValue=" + immediateDiscountValue
				+ ", useOriginProductImageUrl=" + useOriginProductImageUrl + ", expirationYmdt=" + expirationYmdt
				+ ", refundableInfo=" + refundableInfo + ", immediateDiscountPeriodYn=" + immediateDiscountPeriodYn
				+ ", contentFooter=" + contentFooter + ", platformDisplayPcYn=" + platformDisplayPcYn + ", salePrice="
				+ salePrice + ", manufactureYmdt=" + manufactureYmdt + ", optionData=" + optionData
				+ ", immediateDiscountStartYmdt=" + immediateDiscountStartYmdt + ", saleEndYmdt=" + saleEndYmdt
				+ ", valueAddedTaxType=" + valueAddedTaxType + ", promotionText=" + promotionText
				+ ", productImages=" + productImages + ", hsCode=" + hsCode + ", immediateDiscountEndYmdt="
				+ immediateDiscountEndYmdt + ", maxBuyDays=" + maxBuyDays + ", productNameEn=" + productNameEn
				+ ", promotionTextStartYmdt=" + promotionTextStartYmdt + "]";
	}
	public String getMallProductNo() {
		return mallProductNo;
	}
	public void setMallProductNo(String mallProductNo) {
		this.mallProductNo = mallProductNo;
	}
	public String getSalePeriodType() {
		return salePeriodType;
	}
	public void setSalePeriodType(String salePeriodType) {
		this.salePeriodType = salePeriodType;
	}
	public Double getCommissionRate() {
		return commissionRate;
	}
	public void setCommissionRate(Double commissionRate) {
		this.commissionRate = commissionRate;
	}
	
}
