package com.cware.netshopping.pagmkt.v2.domain;

public class ProductResponse {

	// 마스터상품번호 기준으로 G마켓/옥션의 상품정보를 업데이트
	private String goodsNo;

	private SiteDetail siteDetail;

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public SiteDetail getSiteDetail() {
		return siteDetail;
	}

	public void setSiteDetail(SiteDetail siteDetail) {
		this.siteDetail = siteDetail;
	}

	@Override
	public String toString() {
		return "ProductResponse [goodsNo=" + goodsNo + ", siteDetail=" + siteDetail + "]";
	}

}
