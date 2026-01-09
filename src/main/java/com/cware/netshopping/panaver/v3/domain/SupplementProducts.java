package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SupplementProducts {
	
	// 추가 상품 ID
	private long id;
	// 추가 상품 그룹명
	private String groupName;
	// 추가 상품명
	private String name;
	// 추가 상품가
	private double price;
	// 재고 수량
	private int stockQuantity;
	// 판매자 관리 코드
	private String sellerManagementCode;
	// 사용 여부
	private String usable;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getStockQuantity() {
		return stockQuantity;
	}
	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	public String getSellerManagementCode() {
		return sellerManagementCode;
	}
	public void setSellerManagementCode(String sellerManagementCode) {
		this.sellerManagementCode = sellerManagementCode;
	}
	public String getUsable() {
		return usable;
	}
	public void setUsable(String usable) {
		this.usable = usable;
	}

	@Override
	public String toString() {
		return "SupplementProducts [id=" + id +  "groupName=" + groupName +  "name=" + name+  "price=" + price + "stockQuantity=" + stockQuantity + "sellerManagementCode=" + sellerManagementCode + "usable=" + usable + "]";
	}

}
