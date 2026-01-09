package com.cware.netshopping.pawemp.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Branch {
	// 판매자 지점아이디 (필수)
	String branchId;
	// 지점 정상가격 (0 ~999999999)
	Long originPrice;
	// 지점 판매가격 (0 ~999999999) (필수)
	Long salePrice;
	// 지점 사용여부 (Y:사용, N:미사용) (필수)
	String useYn;

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public Long getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(Long originPrice) {
		this.originPrice = originPrice;
	}

	public Long getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Long salePrice) {
		this.salePrice = salePrice;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	@Override
	public String toString() {
		return "Branch [branchId=" + branchId + ", originPrice=" + originPrice + ", salePrice=" + salePrice + ", useYn="
				+ useYn + "]";
	}

}
