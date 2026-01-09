package com.cware.netshopping.pawemp.goods.model;

/**
 * 위메프 제조사 조회 결과
 */
public class GetBrandResponse {
	
	private String brandNo;
	private String brandName;
	private String brandNameEnglish;
	
	public String getBrandNo() {
		return brandNo;
	}
	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandNameEnglish() {
		return brandNameEnglish;
	}
	public void setBrandNameEnglish(String brandNameEnglish) {
		this.brandNameEnglish = brandNameEnglish;
	}
	@Override
	public String toString() {
		return "GetBrandResponse [brandNo=" + brandNo + ", brandName="
				+ brandName + ", brandNameEnglish=" + brandNameEnglish + "]";
	}
}

