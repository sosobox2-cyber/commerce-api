package com.cware.partner.common.domain;

import lombok.Data;

/**
 * 파트너 기준정보
 *
 */

@Data
public class PartnerBase {

	private String paCode;
	private double commission; // 제휴수수료율
	private double ableStockRate; // 주문가능재고비율
	private double minMarginRate; // 최소마진율
	private double minSalePrice; // 최저판매가

	public PartnerBase(String paCode) {
		this.paCode = paCode;
	}
}

