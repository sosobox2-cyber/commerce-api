package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class DetailAttribute {
		
	// 네이버쇼핑 검색 정보
	private NaverShoppingSearchInfo naverShoppingSearchInfo;
	// A/S 정보
	private AfterServiceInfo afterServiceInfo;
	// 구매 수량 설정 정보
	private PurchaseQuantityInfo purchaseQuantityInfo;
	// 원산지 정보
	private OriginAreaInfo originAreaInfo;
	// 판매자 코드 정보
	private SellerCodeInfo sellerCodeInfo;	
	// 옵션 정보
	private OptionInfo optionInfo;	
	// 추가 상품 정보
	private SupplementProductInfo supplementProductInfo;	
	// 리뷰 노출 설정 정보
	private PurchaseReviewInfo purchaseReviewInfo;	
	// ISBN 정보
	private IsbnInfo isbnInfo;
	// 이벤트 문구(홍보 문구 대체)
	private String eventPhraseCont;
	// 제조일자
	private String manufactureDate;
	// 유효일자
	private String validDate;
	// 부가가치세 타입 코드
	private String taxType;
	// 인증 정보 목록	
	private List<ProductCertificationInfos> productCertificationInfos;	
	// 인증 대상 제외 여부 정보
	private CertificationTargetExcludeContent certificationTargetExcludeContent;
	// 판매자 특이 사항
	private String sellerCommentContent;
	// 판매자 특이 사항 사용 여부
	private String sellerCommentUsable;
	// 미성년자 구매 가능 여부
	private String minorPurchasable;	
	// E쿠폰
	private Ecoupon ecoupon;
	// 상품 요약 정보
	private ProductInfoProvidedNotice productInfoProvidedNotice;
	// 상품 속성 목록
	private ProductAttributes productAttributes;
	// 문화비 소득공제 여부
	private String cultureCostIncomeDeductionYn;
	// 맞춤 제작 상품 여부
	private String customProductYn;
	// 자체 제작 상품 여부
	private String itselfProductionProductYn;
	// 브랜드 인증 여부
	private String brandCertificationYn;
	
	
	public NaverShoppingSearchInfo getNaverShoppingSearchInfo() {
		return naverShoppingSearchInfo;
	}

	public void setNaverShoppingSearchInfo(NaverShoppingSearchInfo naverShoppingSearchInfo) {
		this.naverShoppingSearchInfo = naverShoppingSearchInfo;
	}

	public AfterServiceInfo getAfterServiceInfo() {
		return afterServiceInfo;
	}

	public void setAfterServiceInfo(AfterServiceInfo afterServiceInfo) {
		this.afterServiceInfo = afterServiceInfo;
	}

	public PurchaseQuantityInfo getPurchaseQuantityInfo() {
		return purchaseQuantityInfo;
	}

	public void setPurchaseQuantityInfo(PurchaseQuantityInfo purchaseQuantityInfo) {
		this.purchaseQuantityInfo = purchaseQuantityInfo;
	}

	public OriginAreaInfo getOriginAreaInfo() {
		return originAreaInfo;
	}

	public void setOriginAreaInfo(OriginAreaInfo originAreaInfo) {
		this.originAreaInfo = originAreaInfo;
	}

	public SellerCodeInfo getSellerCodeInfo() {
		return sellerCodeInfo;
	}

	public void setSellerCodeInfo(SellerCodeInfo sellerCodeInfo) {
		this.sellerCodeInfo = sellerCodeInfo;
	}

	public OptionInfo getOptionInfo() {
		return optionInfo;
	}

	public void setOptionInfo(OptionInfo optionInfo) {
		this.optionInfo = optionInfo;
	}

	public SupplementProductInfo getSupplementProductInfo() {
		return supplementProductInfo;
	}

	public void setSupplementProductInfo(SupplementProductInfo supplementProductInfo) {
		this.supplementProductInfo = supplementProductInfo;
	}

	public PurchaseReviewInfo getPurchaseReviewInfo() {
		return purchaseReviewInfo;
	}

	public void setPurchaseReviewInfo(PurchaseReviewInfo purchaseReviewInfo) {
		this.purchaseReviewInfo = purchaseReviewInfo;
	}

	public IsbnInfo getIsbnInfo() {
		return isbnInfo;
	}

	public void setIsbnInfo(IsbnInfo isbnInfo) {
		this.isbnInfo = isbnInfo;
	}

	public String getEventPhraseCont() {
		return eventPhraseCont;
	}

	public void setEventPhraseCont(String eventPhraseCont) {
		this.eventPhraseCont = eventPhraseCont;
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

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public List<ProductCertificationInfos> getProductCertificationInfos() {
		return productCertificationInfos;
	}

	public void setProductCertificationInfos(List<ProductCertificationInfos> productCertificationInfos) {
		this.productCertificationInfos = productCertificationInfos;
	}

	public CertificationTargetExcludeContent getCertificationTargetExcludeContent() {
		return certificationTargetExcludeContent;
	}

	public void setCertificationTargetExcludeContent(CertificationTargetExcludeContent certificationTargetExcludeContent) {
		this.certificationTargetExcludeContent = certificationTargetExcludeContent;
	}

	public String getSellerCommentContent() {
		return sellerCommentContent;
	}

	public void setSellerCommentContent(String sellerCommentContent) {
		this.sellerCommentContent = sellerCommentContent;
	}

	public String getSellerCommentUsable() {
		return sellerCommentUsable;
	}

	public void setSellerCommentUsable(String sellerCommentUsable) {
		this.sellerCommentUsable = sellerCommentUsable;
	}

	public String getMinorPurchasable() {
		return minorPurchasable;
	}

	public void setMinorPurchasable(String minorPurchasable) {
		this.minorPurchasable = minorPurchasable;
	}

	public Ecoupon getEcoupon() {
		return ecoupon;
	}

	public void setEcoupon(Ecoupon ecoupon) {
		this.ecoupon = ecoupon;
	}

	public ProductInfoProvidedNotice getProductInfoProvidedNotice() {
		return productInfoProvidedNotice;
	}

	public void setProductInfoProvidedNotice(ProductInfoProvidedNotice productInfoProvidedNotice) {
		this.productInfoProvidedNotice = productInfoProvidedNotice;
	}

	public ProductAttributes getProductAttributes() {
		return productAttributes;
	}

	public void setProductAttributes(ProductAttributes productAttributes) {
		this.productAttributes = productAttributes;
	}

	public String getCultureCostIncomeDeductionYn() {
		return cultureCostIncomeDeductionYn;
	}

	public void setCultureCostIncomeDeductionYn(String cultureCostIncomeDeductionYn) {
		this.cultureCostIncomeDeductionYn = cultureCostIncomeDeductionYn;
	}

	public String getCustomProductYn() {
		return customProductYn;
	}

	public void setCustomProductYn(String customProductYn) {
		this.customProductYn = customProductYn;
	}

	public String getItselfProductionProductYn() {
		return itselfProductionProductYn;
	}

	public void setItselfProductionProductYn(String itselfProductionProductYn) {
		this.itselfProductionProductYn = itselfProductionProductYn;
	}

	public String getBrandCertificationYn() {
		return brandCertificationYn;
	}

	public void setBrandCertificationYn(String brandCertificationYn) {
		this.brandCertificationYn = brandCertificationYn;
	}

	@Override
	public String toString() {
		return "DetailAttribute [naverShoppingSearchInfo=" + naverShoppingSearchInfo +"afterServiceInfo=" + afterServiceInfo + "purchaseQuantityInfo=" + purchaseQuantityInfo + "originAreaInfo=" + originAreaInfo
				+ "sellerCodeInfo=" + sellerCodeInfo +"optionInfo=" + optionInfo +"supplementProductInfo=" + supplementProductInfo +"purchaseReviewInfo=" + purchaseReviewInfo
				+"isbnInfo=" + isbnInfo +"eventPhraseCont=" + eventPhraseCont +"manufactureDate=" + manufactureDate+"validDate=" + validDate
				+"taxType=" + taxType +"productCertificationInfos=" + productCertificationInfos+"certificationTargetExcludeContent=" + certificationTargetExcludeContent +"sellerCommentContent=" + sellerCommentContent
				+"sellerCommentUsable=" + sellerCommentUsable+"minorPurchasable=" + minorPurchasable+"productInfoProvidedNotice=" + productInfoProvidedNotice+"productAttributes=" + productAttributes
				+"cultureCostIncomeDeductionYn=" + cultureCostIncomeDeductionYn+"customProductYn=" + customProductYn+"itselfProductionProductYn=" + itselfProductionProductYn+"brandCertificationYn=" + brandCertificationYn
				+"]";
	}

}
