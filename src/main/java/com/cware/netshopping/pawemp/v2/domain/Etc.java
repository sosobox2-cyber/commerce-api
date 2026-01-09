package com.cware.netshopping.pawemp.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Etc {

	// 병행수입여부 (필수) (Y:대상-병행수입 이미지 URL 필수입력, N:대상아님)
	String parallelImportYn;
	// 병행수입 이미지 URL (병행수입여부 대상일경우 입력 - 최대 200자)
	String parallelImportUrl;
	// 어린이제품 인증대상여부
	// (Y:인증대상(인증유형 및 인증번호 등록), N:인증대상아님-기본값, D:상세설명에 별도표기)
	String kcKidIsCertification;
	// 어린이제품 인증정보
	// - 인증유형 코드 (KD_SAFETY_CT:안전인증, KD_SAFETY_CK:안전확인, KD_SPL:공급자적합성확인) 최대 10개 복수등록
	// 가능
	List<Certification> kcKidCertificationList;
	// 생활용품 인증대상여부 (Y:인증대상(인증유형 및 인증번호 등록), N:인증대상 아님기본값, D:상세설명에 별도표기)
	String kcLifeIsCertification;
	// 생활용품 인증정보
	// - 인증유형 코드 (LF_SAFETY_CT:안전인증, LF_SAFETY_CK:안전확인, LF_SPL:공급자적합성확인,
	// LF_KD_PRT:어린이보호포장) 최대 10개 복수등록 가능
	List<Certification> kcLifeCertificationList;
	// 전기용품 인증대상여부 (Y:인증대상(인증유형 및 인증번호 등록), N:인증대상 아님기본값, D:상세설명에 별도표기)
	String kcElectricIsCertification;
	// 전기용품 인증정보
	// - 인증유형 코드 (ER_SAFETY_CT:안전인증, ER_SAFETY_CK:안전확인, ER_SPL:공급자적합성확인) 최대 10개 복수등록
	// 가능
	List<Certification> kcElectricCertificationList;
	// 방송통신기자재 인증대상여부
	// (Y:인증대상(인증유형 및 인증번호 등록), N:인증대상아님-기본값, D:상세설명에 별도표기)
	String kcReportIsCertification;
	// 방송통신기자재 인증정보
	// - 인증유형 코드 (RP_STB:적합성평가 (적합인증 적합등록)) 최대 10개 복수등록 가능
	List<Certification> kcReportCertificationList;
	// 생활화학제품 인증대상여부
	// (Y:인증대상(인증유형 및 인증번호 등록), N:인증대상아님-기본값, D:상세설명에 별도표기)
	String kcLifeChemistryIsCertification;
	// 생활화학제품 인증정보
	// - 인증유형 코드 (LC_SELF_CHECK:자가검사번호) 최대10개 복수등록 가능
	List<Certification> kcLifeChemistryCertificationList;
	// 업체상품코드 (최대 50자)
	String sellerProdCode;
	// 제휴채널 등록여부 (Y:신청, N:신청안함-기본값)
	String priceComparisonSiteYn;
	// 검색키워드 (제휴채널) (최대 10개까지 등록 가능 / 구분값은 ,(쉼표) 사용)
	String keywordPriceComparisonSite;
	// 검색키워드 (위메프) (최대 10개까지 등록 가능 / 구분값은 ,(쉼표) 사용)
	String keywordWemakeprice;
	// 13자리 ISBN 코드
	String isbn13;
	// 10자리 ISBN 코드
	String isbn10;
	// 딜에서만 노출여부(Y:딜에서만노출, N:제한없음-기본값)
	String displayOnlyDealYn;
	// 구매후기 노출제한(N:평점+후기 노출, P:평점만 노출)
	// ※ 카테고리 단위의 '구매후기 노출제한’값이 '평점+후기 노출' 일 경우, 입력값 상관없이 자동으로 'N' 설정
	String reviewDisp;
	// 상품속성라벨 (최대 4개까지 설정 가능)
	// ※ 단, 위메프 내부 설정값에 따라 라벨 노출가능 수는 조정될 수 있음 (기초데이터-상품속성라벨 조회 API 참고)
	List<String> labelNoList;
	// 명품여부 (Y:명품, N:명품아님(기본값))
	String luxuryYn;

	public String getParallelImportYn() {
		return parallelImportYn;
	}

	public void setParallelImportYn(String parallelImportYn) {
		this.parallelImportYn = parallelImportYn;
	}

	public String getParallelImportUrl() {
		return parallelImportUrl;
	}

	public void setParallelImportUrl(String parallelImportUrl) {
		this.parallelImportUrl = parallelImportUrl;
	}

	public String getKcKidIsCertification() {
		return kcKidIsCertification;
	}

	public void setKcKidIsCertification(String kcKidIsCertification) {
		this.kcKidIsCertification = kcKidIsCertification;
	}

	public List<Certification> getKcKidCertificationList() {
		return kcKidCertificationList;
	}

	public void setKcKidCertificationList(List<Certification> kcKidCertificationList) {
		this.kcKidCertificationList = kcKidCertificationList;
	}

	public String getKcLifeIsCertification() {
		return kcLifeIsCertification;
	}

	public void setKcLifeIsCertification(String kcLifeIsCertification) {
		this.kcLifeIsCertification = kcLifeIsCertification;
	}

	public List<Certification> getKcLifeCertificationList() {
		return kcLifeCertificationList;
	}

	public void setKcLifeCertificationList(List<Certification> kcLifeCertificationList) {
		this.kcLifeCertificationList = kcLifeCertificationList;
	}

	public String getKcElectricIsCertification() {
		return kcElectricIsCertification;
	}

	public void setKcElectricIsCertification(String kcElectricIsCertification) {
		this.kcElectricIsCertification = kcElectricIsCertification;
	}

	public List<Certification> getKcElectricCertificationList() {
		return kcElectricCertificationList;
	}

	public void setKcElectricCertificationList(List<Certification> kcElectricCertificationList) {
		this.kcElectricCertificationList = kcElectricCertificationList;
	}

	public String getKcReportIsCertification() {
		return kcReportIsCertification;
	}

	public void setKcReportIsCertification(String kcReportIsCertification) {
		this.kcReportIsCertification = kcReportIsCertification;
	}

	public List<Certification> getKcReportCertificationList() {
		return kcReportCertificationList;
	}

	public void setKcReportCertificationList(List<Certification> kcReportCertificationList) {
		this.kcReportCertificationList = kcReportCertificationList;
	}

	public String getKcLifeChemistryIsCertification() {
		return kcLifeChemistryIsCertification;
	}

	public void setKcLifeChemistryIsCertification(String kcLifeChemistryIsCertification) {
		this.kcLifeChemistryIsCertification = kcLifeChemistryIsCertification;
	}

	public List<Certification> getKcLifeChemistryCertificationList() {
		return kcLifeChemistryCertificationList;
	}

	public void setKcLifeChemistryCertificationList(List<Certification> kcLifeChemistryCertificationList) {
		this.kcLifeChemistryCertificationList = kcLifeChemistryCertificationList;
	}

	public String getSellerProdCode() {
		return sellerProdCode;
	}

	public void setSellerProdCode(String sellerProdCode) {
		this.sellerProdCode = sellerProdCode;
	}

	public String getPriceComparisonSiteYn() {
		return priceComparisonSiteYn;
	}

	public void setPriceComparisonSiteYn(String priceComparisonSiteYn) {
		this.priceComparisonSiteYn = priceComparisonSiteYn;
	}

	public String getKeywordPriceComparisonSite() {
		return keywordPriceComparisonSite;
	}

	public void setKeywordPriceComparisonSite(String keywordPriceComparisonSite) {
		this.keywordPriceComparisonSite = keywordPriceComparisonSite;
	}

	public String getKeywordWemakeprice() {
		return keywordWemakeprice;
	}

	public void setKeywordWemakeprice(String keywordWemakeprice) {
		this.keywordWemakeprice = keywordWemakeprice;
	}

	public String getIsbn13() {
		return isbn13;
	}

	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public String getIsbn10() {
		return isbn10;
	}

	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}

	public String getDisplayOnlyDealYn() {
		return displayOnlyDealYn;
	}

	public void setDisplayOnlyDealYn(String displayOnlyDealYn) {
		this.displayOnlyDealYn = displayOnlyDealYn;
	}

	public String getReviewDisp() {
		return reviewDisp;
	}

	public void setReviewDisp(String reviewDisp) {
		this.reviewDisp = reviewDisp;
	}

	public List<String> getLabelNoList() {
		return labelNoList;
	}

	public void setLabelNoList(List<String> labelNoList) {
		this.labelNoList = labelNoList;
	}

	public String getLuxuryYn() {
		return luxuryYn;
	}

	public void setLuxuryYn(String luxuryYn) {
		this.luxuryYn = luxuryYn;
	}

	@Override
	public String toString() {
		return "Etc [parallelImportYn=" + parallelImportYn + ", parallelImportUrl=" + parallelImportUrl
				+ ", kcKidIsCertification=" + kcKidIsCertification + ", kcKidCertificationList="
				+ kcKidCertificationList + ", kcLifeIsCertification=" + kcLifeIsCertification
				+ ", kcLifeCertificationList=" + kcLifeCertificationList + ", kcElectricIsCertification="
				+ kcElectricIsCertification + ", kcElectricCertificationList=" + kcElectricCertificationList
				+ ", kcReportIsCertification=" + kcReportIsCertification + ", kcReportCertificationList="
				+ kcReportCertificationList + ", kcLifeChemistryIsCertification=" + kcLifeChemistryIsCertification
				+ ", kcLifeChemistryCertificationList=" + kcLifeChemistryCertificationList + ", sellerProdCode="
				+ sellerProdCode + ", priceComparisonSiteYn=" + priceComparisonSiteYn + ", keywordPriceComparisonSite="
				+ keywordPriceComparisonSite + ", keywordWemakeprice=" + keywordWemakeprice + ", isbn13=" + isbn13
				+ ", isbn10=" + isbn10 + ", displayOnlyDealYn=" + displayOnlyDealYn + ", reviewDisp=" + reviewDisp
				+ ", labelNoList=" + labelNoList + ", luxuryYn=" + luxuryYn + "]";
	}

}
