package com.cware.netshopping.pakakao.v2.domain;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductRequest {
	String productId; // 상품번호

	// 리프 카테고리 ID
	String categoryId;

	// 상품명
	String name;

	// 모델명 검색 API 로 검색하여 선택한 modelId
	// https://shopping-developers.kakao.com/hc/ko/articles/4578925831823
	String modelId;

	// 브랜드명
	String brand;

	// 제조사명
	String manufacturer;

	// 상품상세설명
	String productDetailDescription;

	// 부가세 타입 코드
	// TAX 과세
	// DUTYFREE 면세
	// SMALL 영세
	String taxType;

	// 판매가
	// - 판매가의 입력 단위는 10원 단위이며, 마이너스 및 소수점 금액은 허용하지 않습니다.
	// - 최소 입력 가능 금액은 10원 이상, 최대 입력 가능 금액은 1억 미만입니다.
	// - 즉시 할인이 설정 되어 있다면 즉시 할인 금액보다 커야 합니다.
	BigDecimal salePrice;

	// 판매기간 적용 여부
	// 모든 Boolean 필드는 필수 입력을 원칙으로 하며, 빈 값은 허용하지 않습니다.
	boolean useSalePeriod;

	// 판매기간
	Period salePeriod;

	// 본 상품 재고수량
	// 옵션 상품(조합형 이거나 조합형+작성형)일 경우 필드를 제거
	// - 본 상품의 재고 수량 최소 입력 수량은 1개 이상, 1억개 미만입니다.
	// - 조합형 옵션이 있는 경우 조합형 옵션의 옵션 재고를 입력해야 하며 본 상품 재고는 입력 불가합니다.
	Integer stockQuantity;

	// 상품 상태 코드
	// NEW 새상품
	// OLD 중고상품
	// STOCKED 재고상품
	// REFURBISH 리퍼상품
	// DISPLAY 전시상품
	String productCondition;

	// 제조일자
	// yyyyMMdd
	String manufactureDate;

	// 유효일자
	// yyyyMMdd
	String validDate;

	// 플러스 친구 구독회원 전용 상품 여부
	boolean plusFriendSubscriberExclusive;

	// 1회 최소 구매 수량
	// - 1회 최소 구매 수량 설정 시 최소 2개 이상 최대 1000개 이하로 설정 가능합니다.
	Integer minPurchaseQuantity;

	// 1회 최대 구매 수량
	// - 1회 최대 구매 수량 설정 시 최소 1개 이상 최대 1000개 이하로 설정 가능합니다.
	// - 1회 최소 수량이 설정되어 있다면 1회 최소 수량보다 크거나 같아야 합니다.
	Integer maxPurchaseQuantity;

	// 1인 최대 구매 수량
	// - 1인 최대 구매 수량 설정 시 최소 1개 이상 최대 1000개 이하로 설정 가능합니다.
	// - 1회 최소 수량이 설정되어 있다면 1회 최소 수량보다 크거나 같아야 합니다.
	// - 1회 최대 수량이 설정되어 있다면 1회 최대 수량보다 크거나 같아야 합니다.
	Integer maxPurchaseQuantityOfBuyer;

	// 공지사항 등록 번호
	Long noticeId;

	// 판매자 상품 코드
	String storeManagementCode;

	// 사은품 정보
	String gift;

	// 전시상태코드
	// OPEN 전시
	// HIDDEN 비전시
	String displayStatus;

	// 쇼핑하우 노출 여부
	boolean shoppingHowDisplayable;

	// 원산지 정보
	ProductOriginAreaInfo productOriginAreaInfo;

	// 상품 이미지 정보
	ProductImage productImage;

	// 고시정보
	AnnouncementInfo announcementInfo;

	// 즉시할인 정보
	Discount discount;

	// 배송정보
	Delivery delivery;

	// 옵션정보
	Option option;

	// 상품 인증 정보
	List<Cert> certs;

	// 쇼핑하기 노출여부. 기본값 true
	boolean talkDisplayable;

	// 소문내면 할인 정보
	ShareDiscountRequest shareDiscount;

	// 톡딜 할인 정보
	GroupDiscountRequest groupDiscount;

	// 카테고리 부가정보
	List<ProductCategorySupplement> categorySupplements;

	// 예약판매 설정여부
	boolean booked;

	// 예상출고일
	String bookedDate;

	// 연관상품 번호
	// - 연관상품 설정을 위해서는 판매자센터에서 연관상품그룹을 미리 등록하신 후, 연관상품번호 단위로 설정이 가능합니다.
	// - 현재 보고있는 상품을 제외하고 판매중&전시중 상품이 최소 2개 이상 설정된 경우에 해당 영역이 정상 노출됩니다.
	Long relatedGroupId;

	// 미성년자 구매가능 여부
	boolean minorPurchasable;

	// 판매자 포인트 적립 정보
	// - 구매 포인트 : 최소 1원 이상, 최대 최종 상품 할인가의 10% 이하(도서 카테고리는 5% 이하)로 설정 가능합니다.
	// (최대 10만원 이하로 설정 가능)
	// - 리뷰 포인트 : 최소 1원이상, 최대 최종 상품 할인가의 10% 이하로 설정 가능합니다.
	// (최대 10만원 이하로 설정 가능)
	// 포토 리뷰 포인트 금액은 텍스트 리뷰 포인트 금액 이상으로만 설정 가능합니다.
	// - 업데이트 시 필드가 없는 경우 기존값으로 설정됩니다.
	ProductPointsRequest productPoints;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getProductDetailDescription() {
		return productDetailDescription;
	}

	public void setProductDetailDescription(String productDetailDescription) {
		this.productDetailDescription = productDetailDescription;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public boolean isUseSalePeriod() {
		return useSalePeriod;
	}

	public void setUseSalePeriod(boolean useSalePeriod) {
		this.useSalePeriod = useSalePeriod;
	}

	public Period getSalePeriod() {
		return salePeriod;
	}

	public void setSalePeriod(Period salePeriod) {
		this.salePeriod = salePeriod;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public String getProductCondition() {
		return productCondition;
	}

	public void setProductCondition(String productCondition) {
		this.productCondition = productCondition;
	}

	public String getManufactureDate() {
		return manufactureDate;
	}

	public void setManufactureDate(String manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	public String getValidDate() {
		return validDate;
	}

	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}

	public boolean isPlusFriendSubscriberExclusive() {
		return plusFriendSubscriberExclusive;
	}

	public void setPlusFriendSubscriberExclusive(boolean plusFriendSubscriberExclusive) {
		this.plusFriendSubscriberExclusive = plusFriendSubscriberExclusive;
	}

	public Integer getMinPurchaseQuantity() {
		return minPurchaseQuantity;
	}

	public void setMinPurchaseQuantity(Integer minPurchaseQuantity) {
		this.minPurchaseQuantity = minPurchaseQuantity;
	}

	public Integer getMaxPurchaseQuantity() {
		return maxPurchaseQuantity;
	}

	public void setMaxPurchaseQuantity(Integer maxPurchaseQuantity) {
		this.maxPurchaseQuantity = maxPurchaseQuantity;
	}

	public Integer getMaxPurchaseQuantityOfBuyer() {
		return maxPurchaseQuantityOfBuyer;
	}

	public void setMaxPurchaseQuantityOfBuyer(Integer maxPurchaseQuantityOfBuyer) {
		this.maxPurchaseQuantityOfBuyer = maxPurchaseQuantityOfBuyer;
	}

	public Long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}

	public String getStoreManagementCode() {
		return storeManagementCode;
	}

	public void setStoreManagementCode(String storeManagementCode) {
		this.storeManagementCode = storeManagementCode;
	}

	public String getGift() {
		return gift;
	}

	public void setGift(String gift) {
		this.gift = gift;
	}

	public String getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}

	public boolean isShoppingHowDisplayable() {
		return shoppingHowDisplayable;
	}

	public void setShoppingHowDisplayable(boolean shoppingHowDisplayable) {
		this.shoppingHowDisplayable = shoppingHowDisplayable;
	}

	public ProductOriginAreaInfo getProductOriginAreaInfo() {
		return productOriginAreaInfo;
	}

	public void setProductOriginAreaInfo(ProductOriginAreaInfo productOriginAreaInfo) {
		this.productOriginAreaInfo = productOriginAreaInfo;
	}

	public ProductImage getProductImage() {
		return productImage;
	}

	public void setProductImage(ProductImage productImage) {
		this.productImage = productImage;
	}

	public AnnouncementInfo getAnnouncementInfo() {
		return announcementInfo;
	}

	public void setAnnouncementInfo(AnnouncementInfo announcementInfo) {
		this.announcementInfo = announcementInfo;
	}

	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public Option getOption() {
		return option;
	}

	public void setOption(Option option) {
		this.option = option;
	}

	public List<Cert> getCerts() {
		return certs;
	}

	public void setCerts(List<Cert> certs) {
		this.certs = certs;
	}

	public boolean isTalkDisplayable() {
		return talkDisplayable;
	}

	public void setTalkDisplayable(boolean talkDisplayable) {
		this.talkDisplayable = talkDisplayable;
	}

	public ShareDiscountRequest getShareDiscount() {
		return shareDiscount;
	}

	public void setShareDiscount(ShareDiscountRequest shareDiscount) {
		this.shareDiscount = shareDiscount;
	}

	public GroupDiscountRequest getGroupDiscount() {
		return groupDiscount;
	}

	public void setGroupDiscount(GroupDiscountRequest groupDiscount) {
		this.groupDiscount = groupDiscount;
	}

	public List<ProductCategorySupplement> getCategorySupplements() {
		return categorySupplements;
	}

	public void setCategorySupplements(List<ProductCategorySupplement> categorySupplements) {
		this.categorySupplements = categorySupplements;
	}

	public boolean isBooked() {
		return booked;
	}

	public void setBooked(boolean booked) {
		this.booked = booked;
	}

	public String getBookedDate() {
		return bookedDate;
	}

	public void setBookedDate(String bookedDate) {
		this.bookedDate = bookedDate;
	}

	public Long getRelatedGroupId() {
		return relatedGroupId;
	}

	public void setRelatedGroupId(Long relatedGroupId) {
		this.relatedGroupId = relatedGroupId;
	}

	public boolean isMinorPurchasable() {
		return minorPurchasable;
	}

	public void setMinorPurchasable(boolean minorPurchasable) {
		this.minorPurchasable = minorPurchasable;
	}

	public ProductPointsRequest getProductPoints() {
		return productPoints;
	}

	public void setProductPoints(ProductPointsRequest productPoints) {
		this.productPoints = productPoints;
	}

	@Override
	public String toString() {
		return "ProductRequest [productId=" + productId + ", categoryId=" + categoryId + ", name=" + name + ", modelId="
				+ modelId + ", brand=" + brand + ", manufacturer=" + manufacturer + ", productDetailDescription="
				+ productDetailDescription + ", taxType=" + taxType + ", salePrice=" + salePrice + ", useSalePeriod="
				+ useSalePeriod + ", salePeriod=" + salePeriod + ", stockQuantity=" + stockQuantity
				+ ", productCondition=" + productCondition + ", manufactureDate=" + manufactureDate + ", validDate="
				+ validDate + ", plusFriendSubscriberExclusive=" + plusFriendSubscriberExclusive
				+ ", minPurchaseQuantity=" + minPurchaseQuantity + ", maxPurchaseQuantity=" + maxPurchaseQuantity
				+ ", maxPurchaseQuantityOfBuyer=" + maxPurchaseQuantityOfBuyer + ", noticeId=" + noticeId
				+ ", storeManagementCode=" + storeManagementCode + ", gift=" + gift + ", displayStatus=" + displayStatus
				+ ", shoppingHowDisplayable=" + shoppingHowDisplayable + ", productOriginAreaInfo="
				+ productOriginAreaInfo + ", productImage=" + productImage + ", announcementInfo=" + announcementInfo
				+ ", discount=" + discount + ", delivery=" + delivery + ", option=" + option + ", certs=" + certs
				+ ", talkDisplayable=" + talkDisplayable + ", shareDiscount=" + shareDiscount + ", groupDiscount="
				+ groupDiscount + ", categorySupplements=" + categorySupplements + ", booked=" + booked
				+ ", bookedDate=" + bookedDate + ", relatedGroupId=" + relatedGroupId + ", minorPurchasable="
				+ minorPurchasable + ", productPoints=" + productPoints + "]";
	}

}
