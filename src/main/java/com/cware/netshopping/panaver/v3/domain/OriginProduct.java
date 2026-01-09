package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class OriginProduct {
		
	// 상품 판매 상태 코드
	private String statusType;
	// 상품 판매 유형 코드
	private String saleType;
	// 리프 카테고리 ID
	private String leafCategoryId;
	// 상품명
	private String name;	
	// 상품 상세 정보
	private String detailContent;
	//상품 이미지
	@JsonProperty("images")
	private ProductImages productImages;		
	// 판매 시작 일시
	private String saleStartDate;
	// 판매 종료 일시
	private String saleEndDate;
	// 상품 판매 가격
	private long salePrice;
	// 재고 수량
	private int stockQuantity;
	// 배송 정보
	private DeliveryInfo deliveryInfo;
	// 원상품 상세 속성
	private DetailAttribute detailAttribute;
	// 상품 고객 혜택 정보
	private CustomerBenefit customerBenefit;

	
	public String getStatusType() {
		return statusType;
	}
	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}
	public String getSaleType() {
		return saleType;
	}
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}
	public String getLeafCategoryId() {
		return leafCategoryId;
	}
	public void setLeafCategoryId(String leafCategoryId) {
		this.leafCategoryId = leafCategoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	public ProductImages getProductImages() {
		return productImages;
	}
	public void setProductImages(ProductImages productImages) {
		this.productImages = productImages;
	}
	public String getDetailContent() {
		return detailContent;
	}
	public void setDetailContent(String detailContent) {
		this.detailContent = detailContent;
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
	public long getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(long salePrice) {
		this.salePrice = salePrice;
	}
	public int getStockQuantity() {
		return stockQuantity;
	}
	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	public DeliveryInfo getDeliveryInfo() {
		return deliveryInfo;
	}
	public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}
	public DetailAttribute getDetailAttribute() {
		return detailAttribute;
	}
	public void setDetailAttribute(DetailAttribute detailAttribute) {
		this.detailAttribute = detailAttribute;
	}	
	public CustomerBenefit getCustomerBenefit() {
		return customerBenefit;
	}
	public void setCustomerBenefit(CustomerBenefit customerBenefit) {
		this.customerBenefit = customerBenefit;
	}
	
	@Override
	public String toString() {
		return "OriginProduct [statusType=" + statusType + "saleType="+ saleType + "leafCategoryId="+ leafCategoryId + "name="+ name
				 + "detailContent=" + detailContent + "productImages="+ productImages + "saleStartDate="+ saleStartDate + "saleEndDate="+ saleEndDate
				 + "salePrice=" + salePrice + "stockQuantity="+ stockQuantity + "deliveryInfo="+ deliveryInfo + "detailAttribute="+ detailAttribute
				 + "customerBenefit="+ customerBenefit + "]";
	}
	
}
