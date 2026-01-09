package com.cware.netshopping.pacopn.v2.domain;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Item {
	String sellerProductItemId; // 업체상품옵션아이디
	String vendorItemId; // 옵션아이디: 임시저장 상태일 경우 값이 null 입니다. 상품 승인완료 시에 값이 표시됩니다.

	// 업체상품옵션명
	// 각각의 아이템에 중복되지 않도록 기입
	// 사이트에 노출되는 옵션명이 아니며, 구매옵션에 따라 변경될 수 있음
	// 최대 길이 : 150자 제한
	String itemName;

	String sellerProductName; // 등록상품명: 발주서에 사용되는 상품명

	// 할인율기준가 (정가표시)
	// 할인율(%)표시를 위한 할인전 금액으로, 판매가격과 동일하게 입력시 '쿠팡가'로 노출. 승인완료 이후 할인율기준가 수정은 [옵션별 할인율기준가 변경] API를 통해 변경가능
	long originalPrice;

	// 판매가격
	// 판매가격을 입력.
	// '최초' 업체상품 등록시 판매가격은 상품 승인 요청 전에만 가능하며, 승인완료 이후 판매가격 수정은 [옵션별 가격 변경] API를 통해 변경가능
	long salePrice;

	// 판매가능수량
	// 판매가능한 재고수량을 입력.
	// '최초' 업체상품 등록시 판매수량은 상품 승인 요청 전에만 가능하며, 승인완료 이후 재고 수정은 [옵션별 수량 변경] API를 통해 변경가능
	// 최대값 : 99999
	int maximumBuyCount;

	// 인당 최대 구매 수량
	// 1인당 최대 구매 가능한 수량.
	// 제한이 없을 경우 ‘0’을 입력
	// (예: 인당 최대 구매 수량 100, 최대 구매 수량 기간 3 입력 시, 1인당 3일 동안 최대 100개까지 구매 가능함을 의미)
	int maximumBuyForPerson;

	// 최대 구매 수량 기간
	// 1인당 해당 상품을 구매할 수 있는 주기 설정.
	// 제한이 없을 경우 ‘1’을 입력
	// (예: 인당 최대 구매 수량 100, 최대 구매 수량 기간 3 입력 시, 1인당 3일 동안 최대 100개까지 구매 가능함을 의미)
	int maximumBuyForPersonPeriod;

	// 기준출고일(일)
	// 주문일(D-Day) 이후 배송을 위한 출고예정일자를 '일' 단위로 입력. (다음날(D+1) 출고일 경우 '1' 입력)
	int outboundShippingTimeDay;

	// 단위수량
	// 상품에 포함된 수량을 입력하면 (판매가격 ÷ 단위수량) 로 계산하여 (1개당 가격 #,000원) 으로 노출. 개당가격이 필요하지 않은 상품은 '0'을 입력
	int unitCount;

	// 19세이상
	// ADULT_ONLY: 19세이상 구입 가능 상품
	// EVERYONE: 전연령 구입 가능 상품 (기본값)
	String adultOnly;

	// 과세여부
	// TAX: 과세 (기본값)
	// FREE: 비과세
	String taxType;

	// 병행수입여부
	// PARALLEL_IMPORTED: 병행수입
	// NOT_PARALLEL_IMPORTED: 병행수입 아님 (기본값)
	String parallelImported;

	// 해외구매대행여부
	// OVERSEAS_PURCHASED: 구매대행
	// NOT_OVERSEAS_PURCHASED: 구매대행 아님 (기본값)
	// **구매대행의 경우, 상품 생성 시에 인보이스영수증을 URL을 함께 등록해야 함
	// - 등록방법 https://developers.coupangcorp.com/hc/ko/articles/360023111153
	String overseasPurchased;

	// PCC(개인통관부호) 필수/비필수 여부
	// 해외구매대행 상품인 경우, 상품 PCC(개인통관번호)
	// - 기본값 : 비필수(false)
	// true: 고객이 PCC 입력 후 구매 가능 (PCC는 발주서에 포함 됨)
	// false: 고객이 PCC를 입력하지 않고 상품 구매 가능
	boolean pccNeeded;

	boolean bestPriceGuaranteed3P; // 최저가 보장 여부

	// 판매자상품코드 (업체상품코드)
	// 업체고유의 item 코드값을 임의로 세팅할 수 있으며, 입력값은 발주서 조회 API response 에 포함됨.
	String externalVendorSku;

	// 바코드
	// 상품에 부착 된 유효한 표준상품 코드
	String barcode;

	// 바코드 없음
	// 바코드가 없으면 true
	boolean emptyBarcode;

	// 바코드 없음에 대한 사유
	// 최대길이 : 100 자 제한
	String emptyBarcodeReason;

	String modelNo; //모델번호

	// 업체상품옵션추가정보
	// Key : Value 형태로 필요한 만큼 반복입력가능
	Map<String, String> extraProperties;

	List<Certification> certifications; // 상품인증정보

	// 검색어
	// 필요한 만큼 반복입력가능. ['검색어1','검색어2']
	// 1개의 검색어 당 20자 이내로, 최대 20개의 검색어를 입력가능, !@#$%^&*-+;:’. 외의 특수문자는 입력불가
	List<String> searchTags;

	// 이미지목록
	// 필요한 만큼 반복 입력 가능
	List<Image> images;

	// 상품고시정보 목록
	// 카테고리 메타 정보 조회 API 또는 전체카테고리 리스트 Excel file을 통해, 필요한 고시정보 항목의 확인 및 선택 가능
	List<Notice> notices;

	// 옵션목록(속성)
	// 카테고리 기준으로 정해진 옵션 목록을 입력하는 객체
	// 등록하기 원하는 구매옵션 개수 만큼 반복 입력 가능
	// 구매옵션(attribute exposed)의 모든 값이 중복될 경우 등록 불가
	// 한개 이상 필수 등록
	// 구매옵션 자유 입력 가이드 (20201012 변경사항)
	// - https://developers.coupangcorp.com/hc/ko/articles/900002618063
	List<Attribute> attributes;

	// 컨텐츠목록
	// 필요한 만큼 반복 입력 가능
	List<Content> contents;

	// 상품상태
	// 상품 생성 후에는 offerCondition 변경 불가능
	// 노출카테고리 코드에 따라 아래 값을 선택가능, 없을 경우 NEW로 취급함
	// NEW: 새 상품
	// REFURBISHED: 리퍼
	// USED_BEST: 중고(최상)
	// USED_GOOD: 중고(상)
	// USED_NORMAL: 중고(중)
	String offerCondition;

	// 중고상품 상세설명
	// 중고상품 상태를 설명, 700자 제한
	// offerCondition을 중고로 입력한 경우에만 작성
	String offerDescription;

	public String getSellerProductItemId() {
		return sellerProductItemId;
	}

	public void setSellerProductItemId(String sellerProductItemId) {
		this.sellerProductItemId = sellerProductItemId;
	}

	public String getVendorItemId() {
		return vendorItemId;
	}

	public void setVendorItemId(String vendorItemId) {
		this.vendorItemId = vendorItemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSellerProductName() {
		return sellerProductName;
	}

	public void setSellerProductName(String sellerProductName) {
		this.sellerProductName = sellerProductName;
	}

	public long getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(long originalPrice) {
		this.originalPrice = originalPrice;
	}

	public long getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(long salePrice) {
		this.salePrice = salePrice;
	}

	public int getMaximumBuyCount() {
		return maximumBuyCount;
	}

	public void setMaximumBuyCount(int maximumBuyCount) {
		this.maximumBuyCount = maximumBuyCount;
	}

	public int getMaximumBuyForPerson() {
		return maximumBuyForPerson;
	}

	public void setMaximumBuyForPerson(int maximumBuyForPerson) {
		this.maximumBuyForPerson = maximumBuyForPerson;
	}

	public int getMaximumBuyForPersonPeriod() {
		return maximumBuyForPersonPeriod;
	}

	public void setMaximumBuyForPersonPeriod(int maximumBuyForPersonPeriod) {
		this.maximumBuyForPersonPeriod = maximumBuyForPersonPeriod;
	}

	public int getOutboundShippingTimeDay() {
		return outboundShippingTimeDay;
	}

	public void setOutboundShippingTimeDay(int outboundShippingTimeDay) {
		this.outboundShippingTimeDay = outboundShippingTimeDay;
	}

	public int getUnitCount() {
		return unitCount;
	}

	public void setUnitCount(int unitCount) {
		this.unitCount = unitCount;
	}

	public String getAdultOnly() {
		return adultOnly;
	}

	public void setAdultOnly(String adultOnly) {
		this.adultOnly = adultOnly;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getParallelImported() {
		return parallelImported;
	}

	public void setParallelImported(String parallelImported) {
		this.parallelImported = parallelImported;
	}

	public String getOverseasPurchased() {
		return overseasPurchased;
	}

	public void setOverseasPurchased(String overseasPurchased) {
		this.overseasPurchased = overseasPurchased;
	}

	public boolean isPccNeeded() {
		return pccNeeded;
	}

	public void setPccNeeded(boolean pccNeeded) {
		this.pccNeeded = pccNeeded;
	}

	public boolean isBestPriceGuaranteed3P() {
		return bestPriceGuaranteed3P;
	}

	public void setBestPriceGuaranteed3P(boolean bestPriceGuaranteed3P) {
		this.bestPriceGuaranteed3P = bestPriceGuaranteed3P;
	}

	public String getExternalVendorSku() {
		return externalVendorSku;
	}

	public void setExternalVendorSku(String externalVendorSku) {
		this.externalVendorSku = externalVendorSku;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public boolean isEmptyBarcode() {
		return emptyBarcode;
	}

	public void setEmptyBarcode(boolean emptyBarcode) {
		this.emptyBarcode = emptyBarcode;
	}

	public String getEmptyBarcodeReason() {
		return emptyBarcodeReason;
	}

	public void setEmptyBarcodeReason(String emptyBarcodeReason) {
		this.emptyBarcodeReason = emptyBarcodeReason;
	}

	public String getModelNo() {
		return modelNo;
	}

	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}

	public Map<String, String> getExtraProperties() {
		return extraProperties;
	}

	public void setExtraProperties(Map<String, String> extraProperties) {
		this.extraProperties = extraProperties;
	}

	public List<Certification> getCertifications() {
		return certifications;
	}

	public void setCertifications(List<Certification> certifications) {
		this.certifications = certifications;
	}

	public List<String> getSearchTags() {
		return searchTags;
	}

	public void setSearchTags(List<String> searchTags) {
		this.searchTags = searchTags;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public List<Notice> getNotices() {
		return notices;
	}

	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}

	public String getOfferCondition() {
		return offerCondition;
	}

	public void setOfferCondition(String offerCondition) {
		this.offerCondition = offerCondition;
	}

	public String getOfferDescription() {
		return offerDescription;
	}

	public void setOfferDescription(String offerDescription) {
		this.offerDescription = offerDescription;
	}

	@Override
	public String toString() {
		return "Item [sellerProductItemId=" + sellerProductItemId + ", vendorItemId=" + vendorItemId + ", itemName="
				+ itemName + ", sellerProductName=" + sellerProductName + ", originalPrice=" + originalPrice
				+ ", salePrice=" + salePrice + ", maximumBuyCount=" + maximumBuyCount + ", maximumBuyForPerson="
				+ maximumBuyForPerson + ", maximumBuyForPersonPeriod=" + maximumBuyForPersonPeriod
				+ ", outboundShippingTimeDay=" + outboundShippingTimeDay + ", unitCount=" + unitCount + ", adultOnly="
				+ adultOnly + ", taxType=" + taxType + ", parallelImported=" + parallelImported + ", overseasPurchased="
				+ overseasPurchased + ", pccNeeded=" + pccNeeded + ", bestPriceGuaranteed3P=" + bestPriceGuaranteed3P
				+ ", externalVendorSku=" + externalVendorSku + ", barcode=" + barcode + ", emptyBarcode=" + emptyBarcode
				+ ", emptyBarcodeReason=" + emptyBarcodeReason + ", modelNo=" + modelNo + ", extraProperties="
				+ extraProperties + ", certifications=" + certifications + ", searchTags=" + searchTags + ", images="
				+ images + ", notices=" + notices + ", attributes=" + attributes + ", contents=" + contents
				+ ", offerCondition=" + offerCondition + ", offerDescription=" + offerDescription + "]";
	}
	
}
