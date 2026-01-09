package com.cware.netshopping.pakakao.v2.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GroupDiscountRequest {

	// 톡딜할인 설정여부
	boolean useGroupDiscount;

	// 톡딜할인 정책 아이디
	Long id;

	// 할인액
	BigDecimal price;

	// 톡딜 구매가능 수량 사용여부
	boolean useStock;

	// 톡딜 구매가능 수량
	Integer stock;

	// 적용 기간
	Period period;

	public boolean isUseGroupDiscount() {
		return useGroupDiscount;
	}

	public void setUseGroupDiscount(boolean useGroupDiscount) {
		this.useGroupDiscount = useGroupDiscount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isUseStock() {
		return useStock;
	}

	public void setUseStock(boolean useStock) {
		this.useStock = useStock;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	@Override
	public String toString() {
		return "GroupDiscountRequest [useGroupDiscount=" + useGroupDiscount + ", id=" + id + ", price=" + price
				+ ", useStock=" + useStock + ", stock=" + stock + ", period=" + period + "]";
	}

}
