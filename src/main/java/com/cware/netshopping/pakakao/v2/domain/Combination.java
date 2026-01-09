package com.cware.netshopping.pakakao.v2.domain;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Combination {

	// 조합된 최종 옵션
	List<CombinationName> name;

	// 사용여부
	boolean usable;

	// 옵션가
	BigDecimal price;

	// 옵션재고
	Integer stockQuantity;

	// 옵션별 판매자 관리코드
	String managedCode;

	public List<CombinationName> getName() {
		return name;
	}

	public void setName(List<CombinationName> name) {
		this.name = name;
	}

	public boolean isUsable() {
		return usable;
	}

	public void setUsable(boolean usable) {
		this.usable = usable;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public String getManagedCode() {
		return managedCode;
	}

	public void setManagedCode(String managedCode) {
		this.managedCode = managedCode;
	}

	@Override
	public String toString() {
		return "Combination [name=" + name + ", usable=" + usable + ", price=" + price + ", stockQuantity="
				+ stockQuantity + ", managedCode=" + managedCode + "]";
	}

}
