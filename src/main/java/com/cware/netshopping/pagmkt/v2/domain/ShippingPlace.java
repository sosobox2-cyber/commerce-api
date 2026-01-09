package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ShippingPlace {

	// 출하지번호
	// 수정[PUT] / 조회[GET]일 경우만 필수
	// 등록시 response로 자동채번된 번호
	private String placeNo;

	// 출하지명
	private String placeName;

	// 판매자주소번호
	private String addrNo;

	// 추가배송비 설정여부
	// 제주도 및 부속도서 / 도서 및 기타산간지방
	// 추가배송비 설정할 것인지 여부
	// true : 설정
	// false : 미설정
	private boolean isSetAdditionalShippingFee;

	// 도서 및 기타 산간지방 추가배송비
	// 금액 입력
	// 추가배송비 사용 시, G마켓/옥션/G9 한진택배 우편번호 기준으로 자동 설정되며, 판매자가 우편번호 설정은 불가함
	private Integer backwoodsAdditionalShippingFee;

	// 제주도 및 부속도서 추가배송비	
	// 금액 입력
	// 추가배송비 사용 시, G마켓/옥션/G9 한진택배 우편번호 기준으로 자동 설정되며, 판매자가 우편번호 설정은 불가함
	private Integer jejuAdditionalShippingFee;

	// 기본출하지 여부
	// 해당출하지를 기본출하지로 설정할것인지 여부
	// true : 설정
	// false : 미설정
	private boolean isDefaultShippingPlace;

	public String getPlaceNo() {
		return placeNo;
	}

	public void setPlaceNo(String placeNo) {
		this.placeNo = placeNo;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getAddrNo() {
		return addrNo;
	}

	public void setAddrNo(String addrNo) {
		this.addrNo = addrNo;
	}

	@JsonProperty("isSetAdditionalShippingFee")
	public boolean isSetAdditionalShippingFee() {
		return isSetAdditionalShippingFee;
	}

	public void setSetAdditionalShippingFee(boolean isSetAdditionalShippingFee) {
		this.isSetAdditionalShippingFee = isSetAdditionalShippingFee;
	}

	public Integer getBackwoodsAdditionalShippingFee() {
		return backwoodsAdditionalShippingFee;
	}

	public void setBackwoodsAdditionalShippingFee(Integer backwoodsAdditionalShippingFee) {
		this.backwoodsAdditionalShippingFee = backwoodsAdditionalShippingFee;
	}

	public Integer getJejuAdditionalShippingFee() {
		return jejuAdditionalShippingFee;
	}

	public void setJejuAdditionalShippingFee(Integer jejuAdditionalShippingFee) {
		this.jejuAdditionalShippingFee = jejuAdditionalShippingFee;
	}

	@JsonProperty("isDefaultShippingPlace")
	public boolean isDefaultShippingPlace() {
		return isDefaultShippingPlace;
	}

	public void setDefaultShippingPlace(boolean isDefaultShippingPlace) {
		this.isDefaultShippingPlace = isDefaultShippingPlace;
	}

	@Override
	public String toString() {
		return "ShippingPlace [placeNo=" + placeNo + ", placeName=" + placeName + ", addrNo=" + addrNo
				+ ", isSetAdditionalShippingFee=" + isSetAdditionalShippingFee + ", backwoodsAdditionalShippingFee="
				+ backwoodsAdditionalShippingFee + ", jejuAdditionalShippingFee=" + jejuAdditionalShippingFee
				+ ", isDefaultShippingPlace=" + isDefaultShippingPlace + "]";
	}

	
}
