package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Options {

	// 옵션 번호
	private String mallOptionNo;

	// 옵션값(조합형일 경우로 |로 구분)
	private String optionValue;
	// 추가관리코드
	private String extraManagementCd;
	// 옵션 추가 가격(첫 옵션가는 반드시 추가금이 0이어야 합니다.
	private String addPrice;
	// 공급가(유효상황: 판매방식-위탁, 판매수수료-공급가입력, 옵션-있음)
	private Long purchasePrice;
	// 옵션 선택 방식 (default : MULTI) [ MULTI: Separate, FLAT: Flat ]
	private String optionSelectType;
	// 옵션 이미지
	private List<OptionImages> optionImages;
	// 옵션 타입 [ STANDARD: Standalone Option (ShopByPro: Text Option), COMBINATION: Combination Option, DEFAULT: No option ]
	private String optionType;
	// 사용여부
	private String useYn;
	// 재고 수량(단독형일 경우 0, productStockCnt를 이용)
	private int stockCnt;
	// 옵션 판매자 관리코드
	private String optionManagementCd;
	// 옵션명(조합형일 경우로 |로 구분)
	private String optionName;
	// 임시 품절 여부(default : false)
	private boolean forcedSoldOut;
	// 옵션 순서
	private int order;
	// 상품번호
	private String mallProductNo;
	
	public String getMallOptionNo() {
		return mallOptionNo;
	}
	public void setMallOptionNo(String mallOptionNo) {
		this.mallOptionNo = mallOptionNo;
	}
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	public String getExtraManagementCd() {
		return extraManagementCd;
	}
	public void setExtraManagementCd(String extraManagementCd) {
		this.extraManagementCd = extraManagementCd;
	}
	public String getAddPrice() {
		return addPrice;
	}
	public void setAddPrice(String addPrice) {
		this.addPrice = addPrice;
	}
	public Long getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(Long purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getOptionSelectType() {
		return optionSelectType;
	}
	public void setOptionSelectType(String optionSelectType) {
		this.optionSelectType = optionSelectType;
	}
	public List<OptionImages> getOptionImages() {
		return optionImages;
	}
	public void setOptionImages(List<OptionImages> optionImages) {
		this.optionImages = optionImages;
	}
	public String getOptionType() {
		return optionType;
	}
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public int getStockCnt() {
		return stockCnt;
	}
	public void setStockCnt(int stockCnt) {
		this.stockCnt = stockCnt;
	}
	public String getOptionManagementCd() {
		return optionManagementCd;
	}
	public void setOptionManagementCd(String optionManagementCd) {
		this.optionManagementCd = optionManagementCd;
	}
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public boolean isForcedSoldOut() {
		return forcedSoldOut;
	}
	public void setForcedSoldOut(boolean forcedSoldOut) {
		this.forcedSoldOut = forcedSoldOut;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getMallProductNo() {
		return mallProductNo;
	}
	public void setMallProductNo(String mallProductNo) {
		this.mallProductNo = mallProductNo;
	}
	
	@Override
	public String toString() {
		return "Options [mallOptionNo=" + mallOptionNo + ", optionValue=" + optionValue
				+ ", extraManagementCd=" + extraManagementCd + ", addPrice=" + addPrice + ", purchasePrice="
				+ purchasePrice + ", optionSelectType=" + optionSelectType + ", optionImages=" + optionImages
				+ ", optionType=" + optionType + ", useYn=" + useYn + ", stockCnt=" + stockCnt + ", optionManagementCd="
				+ optionManagementCd + ", optionName=" + optionName + ", forcedSoldOut=" + forcedSoldOut + ", order="
				+ order + ", mallProductNo=" + mallProductNo + "]";
	}
	
}
