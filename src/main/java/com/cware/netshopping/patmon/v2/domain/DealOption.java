package com.cware.netshopping.patmon.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DealOption {
	String vendorId; // 외부사코드
	String vendorDealNo; // 외부사딜번호
	String tmonDealNo; // 티몬딜번호
	String vendorDealOptionNo; // 외부사 딜옵션번호
	String tmonDealOptionNo; // 티몬 딜옵션번호
	String title; // 옵션명
	String description; // 파트너커스텀정보
	List<String> sections; // 옵션분류
	Boolean mainOption; // 대표옵션 여부
	Long price; // 상품가
	Long salesPrice; // 판매가(단가)
	Integer stock; // 재고
	Boolean display; // 노출 여부
	String status; // 딜옵션상태

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

	public String getVendorDealOptionNo() {
		return vendorDealOptionNo;
	}

	public void setVendorDealOptionNo(String vendorDealOptionNo) {
		this.vendorDealOptionNo = vendorDealOptionNo;
	}

	public String getTmonDealOptionNo() {
		return tmonDealOptionNo;
	}

	public void setTmonDealOptionNo(String tmonDealOptionNo) {
		this.tmonDealOptionNo = tmonDealOptionNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getSections() {
		return sections;
	}

	public void setSections(List<String> sections) {
		this.sections = sections;
	}

	public Boolean getMainOption() {
		return mainOption;
	}

	public void setMainOption(Boolean mainOption) {
		this.mainOption = mainOption;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(Long salesPrice) {
		this.salesPrice = salesPrice;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "DealOption [vendorId=" + vendorId + ", vendorDealNo=" + vendorDealNo + ", tmonDealNo=" + tmonDealNo
				+ ", vendorDealOptionNo=" + vendorDealOptionNo + ", tmonDealOptionNo=" + tmonDealOptionNo + ", title="
				+ title + ", description=" + description + ", sections=" + sections + ", mainOption=" + mainOption
				+ ", price=" + price + ", salesPrice=" + salesPrice + ", stock=" + stock + ", display=" + display
				+ ", status=" + status + "]";
	}

}
