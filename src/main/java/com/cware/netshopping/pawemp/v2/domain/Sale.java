package com.cware.netshopping.pawemp.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Sale {

	// 판매기간 구분 (필수)
	// (P:기간설정-기본값, A:상시판매)
	// ※판매종료날짜는 기간설정으로만 수정할 수 있습니다
	String salePeriod;
	// 판매기간 시작일 (필수)
	// (yyyy-MM-dd HH:00) ※ 판매시작 이후 변경 불가
	String saleStartDate;
	// 판매기간 종료일 (필수)
	// (yyyy-MM-dd HH:00)
	String saleEndDate;
	// 정상가격 (0 ~ 999999999)
	Long originPrice;
	// 판매가격 (필수)
	// (0 ~999999999)
	Long salePrice;
	// 상품 재고수량 - 옵션이 없을 경우 입력 (0 ~ 99999)
	Integer stockCount;
	// 과세여부 (필수)
	// (Y:과세, N:면세)
	String taxYn;
	// 최소구매수량 (1 ~ 999) - 기본값 1
	Integer purchaseMinCount;
	// 1인당 구매제한 사용여부 (필수)
	// (Y:사용-구매제한타입과 구매제한개수 필수입력, N:미사용)
	String purchaseLimitYn;
	// 구매제한 타입 (O:1회, P:기간제한-구매제한일자 필수입력)
	String purchaseLimitDuration;
	// 구매제한 일자 (1 ~ 90)
	Integer purchaseLimitDay;
	// 구매제한 개수 - 구매제한 일자에 대한 구매제한 개수 (0 ~ 9999)
	Integer purchaseLimitCount;
	// 장바구니제한 여부
	// (Y: 제한사용, N: 제한미사용 / ※ 카테고리 기준에 따라 장바구니제한 여부는 변경 적용될 수 있음)
	String basketLimitYn;
	// 기준가격 근거정보 타입 (필수)
	// (ONL:온라인판매가, WMP:위메프가-미등록/정상가격 미입력, BIZ:사업자혜택가)
	String referencePriceType;

	public String getSalePeriod() {
		return salePeriod;
	}

	public void setSalePeriod(String salePeriod) {
		this.salePeriod = salePeriod;
	}

	public String getSaleStartDate() {
		return saleStartDate;
	}

	public void setSaleStartDate(String saleStartDate) {
		this.saleStartDate = saleStartDate;
	}

	public String getSaleEndDate() {
		return saleEndDate;
	}

	public void setSaleEndDate(String saleEndDate) {
		this.saleEndDate = saleEndDate;
	}

	public Long getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(Long originPrice) {
		this.originPrice = originPrice;
	}

	public Long getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Long salePrice) {
		this.salePrice = salePrice;
	}

	public Integer getStockCount() {
		return stockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}

	public String getTaxYn() {
		return taxYn;
	}

	public void setTaxYn(String taxYn) {
		this.taxYn = taxYn;
	}

	public Integer getPurchaseMinCount() {
		return purchaseMinCount;
	}

	public void setPurchaseMinCount(Integer purchaseMinCount) {
		this.purchaseMinCount = purchaseMinCount;
	}

	public String getPurchaseLimitYn() {
		return purchaseLimitYn;
	}

	public void setPurchaseLimitYn(String purchaseLimitYn) {
		this.purchaseLimitYn = purchaseLimitYn;
	}

	public String getPurchaseLimitDuration() {
		return purchaseLimitDuration;
	}

	public void setPurchaseLimitDuration(String purchaseLimitDuration) {
		this.purchaseLimitDuration = purchaseLimitDuration;
	}

	public Integer getPurchaseLimitDay() {
		return purchaseLimitDay;
	}

	public void setPurchaseLimitDay(Integer purchaseLimitDay) {
		this.purchaseLimitDay = purchaseLimitDay;
	}

	public Integer getPurchaseLimitCount() {
		return purchaseLimitCount;
	}

	public void setPurchaseLimitCount(Integer purchaseLimitCount) {
		this.purchaseLimitCount = purchaseLimitCount;
	}

	public String getBasketLimitYn() {
		return basketLimitYn;
	}

	public void setBasketLimitYn(String basketLimitYn) {
		this.basketLimitYn = basketLimitYn;
	}

	public String getReferencePriceType() {
		return referencePriceType;
	}

	public void setReferencePriceType(String referencePriceType) {
		this.referencePriceType = referencePriceType;
	}

	@Override
	public String toString() {
		return "Sale [salePeriod=" + salePeriod + ", saleStartDate=" + saleStartDate + ", saleEndDate=" + saleEndDate
				+ ", originPrice=" + originPrice + ", salePrice=" + salePrice + ", stockCount=" + stockCount
				+ ", taxYn=" + taxYn + ", purchaseMinCount=" + purchaseMinCount + ", purchaseLimitYn=" + purchaseLimitYn
				+ ", purchaseLimitDuration=" + purchaseLimitDuration + ", purchaseLimitDay=" + purchaseLimitDay
				+ ", purchaseLimitCount=" + purchaseLimitCount + ", basketLimitYn=" + basketLimitYn
				+ ", referencePriceType=" + referencePriceType + "]";
	}

}
