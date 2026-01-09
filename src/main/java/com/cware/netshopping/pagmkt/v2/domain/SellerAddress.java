package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class SellerAddress {

	// 판매자주소록번호
	// 등록시 response로 자동채번된 번호
	private String addrNo;

	// 주소명
	// 판매자가 관리하려는 주소명 입력
	private String addrName;

	// 판매자명
	// 고객에게 반품수취인으로 노출되는 판매자명
	private String representativeName;

	// 우편번호
	// 우편번호 5자리/6자리 등록 가능
	// (단, 5자리 우편번호로 입력 권장)
	// 우편번호 6자리 등록 시, 하이픈 제거하고 입력
	private String zipCode;

	// 주소1
	// 우편번호 기준 주소
	private String addr1;

	// 주소2
	// 주소 상세
	private String addr2;

	// 일반전화번호
	// 하이픈 입력 필요
	private String homeTel;

	// 휴대폰번호
	// 하이픈 입력 필요
	private String cellPhone;

	// 위치설명
	// 방문수령의 상품일 경우 자세하게 기재 필요
	private String locationDescription;

	// 기본방문수령지여부
	private boolean isVisitAndTakeAddr;

	// 기본반품배송지주소여부
	private boolean isReturnAddr;

	public String getAddrNo() {
		return addrNo;
	}

	public void setAddrNo(String addrNo) {
		this.addrNo = addrNo;
	}

	public String getAddrName() {
		return addrName;
	}

	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public String getHomeTel() {
		return homeTel;
	}

	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	@JsonProperty("isVisitAndTakeAddr")
	public boolean isVisitAndTakeAddr() {
		return isVisitAndTakeAddr;
	}

	public void setVisitAndTakeAddr(boolean isVisitAndTakeAddr) {
		this.isVisitAndTakeAddr = isVisitAndTakeAddr;
	}

	@JsonProperty("isReturnAddr")
	public boolean isReturnAddr() {
		return isReturnAddr;
	}

	public void setReturnAddr(boolean isReturnAddr) {
		this.isReturnAddr = isReturnAddr;
	}

	@Override
	public String toString() {
		return "SellerAddress [addrNo=" + addrNo + ", addrName=" + addrName + ", representativeName="
				+ representativeName + ", zipCode=" + zipCode + ", addr1=" + addr1 + ", addr2=" + addr2 + ", homeTel="
				+ homeTel + ", cellPhone=" + cellPhone + ", locationDescription=" + locationDescription
				+ ", isVisitAndTakeAddr=" + isVisitAndTakeAddr + ", isReturnAddr=" + isReturnAddr + "]";
	}

}
