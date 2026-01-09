package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SiteProduct {

	// 사이트상품번호
	// 상품이 정상적으로 생성되면 사이트상품번호가 채번됨
	// 사이트상품번호 기준으로 주문~정산까지 데이터 묶임
	private String siteGoodsNo;

	// 사이트상품정보반영 결과
	private String siteGoodsComment;

	@JsonProperty("SiteGoodsNo")
	public String getSiteGoodsNo() {
		return siteGoodsNo;
	}

	@JsonProperty("SiteGoodsNo")
	public void setSiteGoodsNo(String siteGoodsNo) {
		this.siteGoodsNo = siteGoodsNo;
	}

	@JsonProperty("SiteGoodsComment")
	public String getSiteGoodsComment() {
		return siteGoodsComment;
	}

	@JsonProperty("SiteGoodsComment")
	public void setSiteGoodsComment(String siteGoodsComment) {
		this.siteGoodsComment = siteGoodsComment;
	}

	@Override
	public String toString() {
		return "SiteProduct [siteGoodsNo=" + siteGoodsNo + ", siteGoodsComment=" + siteGoodsComment + "]";
	}

}
