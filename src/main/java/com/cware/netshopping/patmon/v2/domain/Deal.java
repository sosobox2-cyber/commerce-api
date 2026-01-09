package com.cware.netshopping.patmon.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Deal {
	String vendorId; // 외부사코드
	String vendorDealNo; // 외부사딜번호
	String tmonDealNo; // 티몬딜번호
	String vendorPolicyNo; // 등록시 정책번호
	String deliveryTemplateNo; // 등록시 배송템플릿번호(묶음배송정책번호)
	String dealType; // 딜 구분 타입 (D고정)
	List<String> sections; // 옵션분류
	String managedTitle; // 딜명(관리용)
	DealSalesPeriod salesPeriod; // 판매기간
	String dealProductStatus; // 상품상태 메타정보
	String dealSellMethod; // 판매방식 메타정보
	Boolean adultOnly; // 성인물여부
	String productType; // 배송상품 유형 타입

	// 판매용 제목(딜명)
	// 최대 60자 까지만 가능
	String title;

	// 판매용 제목 상단 홍보 문구(딜 홍보 문구)
	// 최대 20자 까지만 가능
	String titleDecoration;

	// 사이트 노출용 메인이미지들
	// Reset. 720X758(200kb)
	List<String> mainImages;

	// 홈추천 이미지
	// 756X383(200kb) 앱 메인 특정 영역에 노출되는 이벤트성 이미지
	String homeRecommendedImage;

	// 딜 대표이미지 내 동영상 정보
	DealMainVideo dealMainVideo;

	// 사이트 노출용 딜상세
	// 최대 50만자까지 가능
	String detailContents;

	String categoryNo; // 티몬카테고리번호
	List<String> subcategoryNos; // 하위 카테고리
	String legalPermissionType; // 법적허가/신고대상 상품코드
	String importAdvertisementCertificate; // 광고심의필증
	Boolean additionalInput; // 추가입력문구사용여부
	String additionalInputTitle; // 추가입력안내문

	String originCountryType; // 원산지 표기 방식
	// 원산지 표기 방식 상세
	// 표기방식이 "직접입력"일 때 필수
	String originCountryDetail;

	List<DealProductInfo> productInfos; // 상품정보제공고시 정보

	// KC인증 제출방식
	// 코드표 참고
	String kcAuthSubmitType;

	// KC인증들
	// 제출타입일 경우 필수, 전체 삭제후 재등록임, 수정시 전체 다시 보낼 것
	List<DealKcAuth> kcAuths;

	String deliveryCorp; // 배송사(택배사)
	Boolean search; // 검색 노출 여부

	// 검색 키워드
	// 5개까지
	List<String> keywords;

	Boolean parallelImport; // 병행수입여부

	// 수입신고필증
	// 병행수입여부가 true이면 필수입력
	String importDeclarationCertificate;

	// 지역별 차등배송비 사용여부
	// default: false
	Boolean distanceFeeGradeUsing;

	// 지역별 차등배송비 또는 사유
	// 지역별차등배송비를 사용하는 경우 필수입력
	String distanceFeeGradeContents;

	// 별도 설치비 사용여부
	// default: false
	Boolean extraInstallationCostUsing;

	Boolean priceComparison; // 가격비교사이트노출동의
	String brandName; // 브랜드이름

	DealContents dealContents; // 딜컨텐츠정보
	DealDeliveryInfo dealDeliveryInfo; // 배송 정보
	Integer maxPurchaseQty; // 1인당 최대 구매수량
	Integer purchaseResetPeriod; // 1인당 최대 구매수량 초기화주기 (일)

	// 딜 상태
	// 코드조회API의 DealStatus 항목과 매칭
	String status;

	// 딜 판매 상태
	// PAUSE : 일시중지, READY : 판매예정, ON_SALE : 판매중, END : 판매종료
	String salesStatus;

	DealOptionsPageInfo dealOptionsPageInfo; // 딜옵션 페이지 정보
	List<DealOption> dealOptions; // 딜옵션 매핑 정보
	String creator; // 딜 등록자
	String createDate; // 딜 매핑 시간
	
	Integer exceptYn; // 배송템플릿 오류 여부

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorDealNo() {
		return vendorDealNo;
	}

	public void setVendorDealNo(String vendorDealNo) {
		this.vendorDealNo = vendorDealNo;
	}

	public String getTmonDealNo() {
		return tmonDealNo;
	}

	public void setTmonDealNo(String tmonDealNo) {
		this.tmonDealNo = tmonDealNo;
	}

	public String getVendorPolicyNo() {
		return vendorPolicyNo;
	}

	public void setVendorPolicyNo(String vendorPolicyNo) {
		this.vendorPolicyNo = vendorPolicyNo;
	}

	public String getDeliveryTemplateNo() {
		return deliveryTemplateNo;
	}

	public void setDeliveryTemplateNo(String deliveryTemplateNo) {
		this.deliveryTemplateNo = deliveryTemplateNo;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public List<String> getSections() {
		return sections;
	}

	public void setSections(List<String> sections) {
		this.sections = sections;
	}

	public String getManagedTitle() {
		return managedTitle;
	}

	public void setManagedTitle(String managedTitle) {
		this.managedTitle = managedTitle;
	}

	public DealSalesPeriod getSalesPeriod() {
		return salesPeriod;
	}

	public void setSalesPeriod(DealSalesPeriod salesPeriod) {
		this.salesPeriod = salesPeriod;
	}

	public String getDealProductStatus() {
		return dealProductStatus;
	}

	public void setDealProductStatus(String dealProductStatus) {
		this.dealProductStatus = dealProductStatus;
	}

	public String getDealSellMethod() {
		return dealSellMethod;
	}

	public void setDealSellMethod(String dealSellMethod) {
		this.dealSellMethod = dealSellMethod;
	}

	public Boolean getAdultOnly() {
		return adultOnly;
	}

	public void setAdultOnly(Boolean adultOnly) {
		this.adultOnly = adultOnly;
	}

	public String getCategoryNo() {
		return categoryNo;
	}

	public void setCategoryNo(String categoryNo) {
		this.categoryNo = categoryNo;
	}

	public List<String> getSubcategoryNos() {
		return subcategoryNos;
	}

	public void setSubcategoryNos(List<String> subcategoryNos) {
		this.subcategoryNos = subcategoryNos;
	}

	public String getLegalPermissionType() {
		return legalPermissionType;
	}

	public void setLegalPermissionType(String legalPermissionType) {
		this.legalPermissionType = legalPermissionType;
	}

	public String getImportAdvertisementCertificate() {
		return importAdvertisementCertificate;
	}

	public void setImportAdvertisementCertificate(String importAdvertisementCertificate) {
		this.importAdvertisementCertificate = importAdvertisementCertificate;
	}

	public Boolean getAdditionalInput() {
		return additionalInput;
	}

	public void setAdditionalInput(Boolean additionalInput) {
		this.additionalInput = additionalInput;
	}

	public String getAdditionalInputTitle() {
		return additionalInputTitle;
	}

	public void setAdditionalInputTitle(String additionalInputTitle) {
		this.additionalInputTitle = additionalInputTitle;
	}

	public DealContents getDealContents() {
		return dealContents;
	}

	public void setDealContents(DealContents dealContents) {
		this.dealContents = dealContents;
	}

	public DealDeliveryInfo getDealDeliveryInfo() {
		return dealDeliveryInfo;
	}

	public void setDealDeliveryInfo(DealDeliveryInfo dealDeliveryInfo) {
		this.dealDeliveryInfo = dealDeliveryInfo;
	}

	public Integer getMaxPurchaseQty() {
		return maxPurchaseQty;
	}

	public void setMaxPurchaseQty(Integer maxPurchaseQty) {
		this.maxPurchaseQty = maxPurchaseQty;
	}

	public Integer getPurchaseResetPeriod() {
		return purchaseResetPeriod;
	}

	public void setPurchaseResetPeriod(Integer purchaseResetPeriod) {
		this.purchaseResetPeriod = purchaseResetPeriod;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSalesStatus() {
		return salesStatus;
	}

	public void setSalesStatus(String salesStatus) {
		this.salesStatus = salesStatus;
	}

	public DealOptionsPageInfo getDealOptionsPageInfo() {
		return dealOptionsPageInfo;
	}

	public void setDealOptionsPageInfo(DealOptionsPageInfo dealOptionsPageInfo) {
		this.dealOptionsPageInfo = dealOptionsPageInfo;
	}

	public List<DealOption> getDealOptions() {
		return dealOptions;
	}

	public void setDealOptions(List<DealOption> dealOptions) {
		this.dealOptions = dealOptions;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleDecoration() {
		return titleDecoration;
	}

	public void setTitleDecoration(String titleDecoration) {
		this.titleDecoration = titleDecoration;
	}

	public List<String> getMainImages() {
		return mainImages;
	}

	public void setMainImages(List<String> mainImages) {
		this.mainImages = mainImages;
	}

	public String getHomeRecommendedImage() {
		return homeRecommendedImage;
	}

	public void setHomeRecommendedImage(String homeRecommendedImage) {
		this.homeRecommendedImage = homeRecommendedImage;
	}

	public DealMainVideo getDealMainVideo() {
		return dealMainVideo;
	}

	public void setDealMainVideo(DealMainVideo dealMainVideo) {
		this.dealMainVideo = dealMainVideo;
	}

	public String getDetailContents() {
		return detailContents;
	}

	public void setDetailContents(String detailContents) {
		this.detailContents = detailContents;
	}

	public String getOriginCountryType() {
		return originCountryType;
	}

	public void setOriginCountryType(String originCountryType) {
		this.originCountryType = originCountryType;
	}

	public String getOriginCountryDetail() {
		return originCountryDetail;
	}

	public void setOriginCountryDetail(String originCountryDetail) {
		this.originCountryDetail = originCountryDetail;
	}

	public List<DealProductInfo> getProductInfos() {
		return productInfos;
	}

	public void setProductInfos(List<DealProductInfo> productInfos) {
		this.productInfos = productInfos;
	}

	public String getKcAuthSubmitType() {
		return kcAuthSubmitType;
	}

	public void setKcAuthSubmitType(String kcAuthSubmitType) {
		this.kcAuthSubmitType = kcAuthSubmitType;
	}

	public List<DealKcAuth> getKcAuths() {
		return kcAuths;
	}

	public void setKcAuths(List<DealKcAuth> kcAuths) {
		this.kcAuths = kcAuths;
	}

	public String getDeliveryCorp() {
		return deliveryCorp;
	}

	public void setDeliveryCorp(String deliveryCorp) {
		this.deliveryCorp = deliveryCorp;
	}

	public Boolean getSearch() {
		return search;
	}

	public void setSearch(Boolean search) {
		this.search = search;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public Boolean getParallelImport() {
		return parallelImport;
	}

	public void setParallelImport(Boolean parallelImport) {
		this.parallelImport = parallelImport;
	}

	public String getImportDeclarationCertificate() {
		return importDeclarationCertificate;
	}

	public void setImportDeclarationCertificate(String importDeclarationCertificate) {
		this.importDeclarationCertificate = importDeclarationCertificate;
	}

	public Boolean getDistanceFeeGradeUsing() {
		return distanceFeeGradeUsing;
	}

	public void setDistanceFeeGradeUsing(Boolean distanceFeeGradeUsing) {
		this.distanceFeeGradeUsing = distanceFeeGradeUsing;
	}

	public String getDistanceFeeGradeContents() {
		return distanceFeeGradeContents;
	}

	public void setDistanceFeeGradeContents(String distanceFeeGradeContents) {
		this.distanceFeeGradeContents = distanceFeeGradeContents;
	}

	public Boolean getExtraInstallationCostUsing() {
		return extraInstallationCostUsing;
	}

	public void setExtraInstallationCostUsing(Boolean extraInstallationCostUsing) {
		this.extraInstallationCostUsing = extraInstallationCostUsing;
	}

	public Boolean getPriceComparison() {
		return priceComparison;
	}

	public void setPriceComparison(Boolean priceComparison) {
		this.priceComparison = priceComparison;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Integer getExceptYn() {
		return exceptYn;
	}

	public void setExceptYn(Integer exceptYn) {
		this.exceptYn = exceptYn;
	}

	@Override
	public String toString() {
		return "Deal [vendorId=" + vendorId + ", vendorDealNo=" + vendorDealNo + ", tmonDealNo=" + tmonDealNo
				+ ", vendorPolicyNo=" + vendorPolicyNo + ", deliveryTemplateNo=" + deliveryTemplateNo + ", dealType="
				+ dealType + ", sections=" + sections + ", managedTitle=" + managedTitle + ", salesPeriod="
				+ salesPeriod + ", dealProductStatus=" + dealProductStatus + ", dealSellMethod=" + dealSellMethod
				+ ", adultOnly=" + adultOnly + ", categoryNo=" + categoryNo + ", subcategoryNos=" + subcategoryNos
				+ ", legalPermissionType=" + legalPermissionType + ", importAdvertisementCertificate="
				+ importAdvertisementCertificate + ", additionalInput=" + additionalInput + ", additionalInputTitle="
				+ additionalInputTitle + ", dealContents=" + dealContents + ", dealDeliveryInfo=" + dealDeliveryInfo
				+ ", maxPurchaseQty=" + maxPurchaseQty + ", purchaseResetPeriod=" + purchaseResetPeriod + ", status="
				+ status + ", salesStatus=" + salesStatus + ", dealOptionsPageInfo=" + dealOptionsPageInfo
				+ ", dealOptions=" + dealOptions + ", creator=" + creator + ", createDate=" + createDate + "]";
	}

}
