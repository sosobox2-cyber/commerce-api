package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MultiLevelOption {

	// 옵션명
	private String label;
	// 옵션값
	private String value;
	// 옵션번호
	private int optionNo;
	// 추가금액
	private Long addPrice;
	// 판매수량 (재고 미노출의 경우 -999 재고 미노출 설정일때 실재고가 없는 경우, 0으로 표기)
	private int saleCnt;
	// 재고수량 (재고 미노출의 경우 -999 재고 미노출 설정일때 실재고가 없는 경우, 0으로 표기)
	private int stockCnt;
	// 예약재고수량 (재고 미노출의 경우 -999 재고 미노출 설정일때 실재고가 없는 경우, 0으로 표기)
	private int reservationStockCnt;
	// 판매타입 [AVAILABLE: Available for sale, SOLD_OUT: Sold out]
	private String saleType;
	// 대표 옵션 여부 (true: 대표 옵션, false:대표 옵션 아님)
	private boolean main;
	// 할인적용가
	private Long buyPrice;
	// 옵션 이미지
	private List<Image> images;
	// 추가관리코드
	private String extraManagementCd;
	// 렌탈료 정보
	private List<RentalInfo> rentalInfo;
	// 옵션 판매자 관리 코드
	private String optionManagementCd;
	// 임시 품절 여부 (true: 임시품절, false:임시품절 아님)
	private boolean forcedSoldOut;
	// 자식 옵션 목록
	private List<String> children;
	// 필수 옵션 여부 (개발중인 파라미터로 현재 사용불가)
	@JsonProperty("isRequiredOption")
	private boolean isRequiredOption;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getOptionNo() {
		return optionNo;
	}
	public void setOptionNo(int optionNo) {
		this.optionNo = optionNo;
	}
	public Long getAddPrice() {
		return addPrice;
	}
	public void setAddPrice(Long addPrice) {
		this.addPrice = addPrice;
	}
	public int getSaleCnt() {
		return saleCnt;
	}
	public void setSaleCnt(int saleCnt) {
		this.saleCnt = saleCnt;
	}
	public int getStockCnt() {
		return stockCnt;
	}
	public void setStockCnt(int stockCnt) {
		this.stockCnt = stockCnt;
	}
	public int getReservationStockCnt() {
		return reservationStockCnt;
	}
	public void setReservationStockCnt(int reservationStockCnt) {
		this.reservationStockCnt = reservationStockCnt;
	}
	public String getSaleType() {
		return saleType;
	}
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}
	public boolean isMain() {
		return main;
	}
	public void setMain(boolean main) {
		this.main = main;
	}
	public Long getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(Long buyPrice) {
		this.buyPrice = buyPrice;
	}
	public List<Image> getImages() {
		return images;
	}
	public void setImages(List<Image> images) {
		this.images = images;
	}
	public String getExtraManagementCd() {
		return extraManagementCd;
	}
	public void setExtraManagementCd(String extraManagementCd) {
		this.extraManagementCd = extraManagementCd;
	}
	public List<RentalInfo> getRentalInfo() {
		return rentalInfo;
	}
	public void setRentalInfo(List<RentalInfo> rentalInfo) {
		this.rentalInfo = rentalInfo;
	}
	public String getOptionManagementCd() {
		return optionManagementCd;
	}
	public void setOptionManagementCd(String optionManagementCd) {
		this.optionManagementCd = optionManagementCd;
	}
	public boolean isForcedSoldOut() {
		return forcedSoldOut;
	}
	public void setForcedSoldOut(boolean forcedSoldOut) {
		this.forcedSoldOut = forcedSoldOut;
	}
	public List<String> getChildren() {
		return children;
	}
	public void setChildren(List<String> children) {
		this.children = children;
	}
	public boolean isRequiredOption() {
		return isRequiredOption;
	}
	public void setRequiredOption(boolean isRequiredOption) {
		this.isRequiredOption = isRequiredOption;
	}
	
}
