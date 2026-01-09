package com.cware.netshopping.pakakao.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductPointsRequest {

	// 구매 시 포인트 적립 정보
	SellerPurchasePointRequest sellerPurchasePoint;

	// 리뷰 작성 시 포인트 적립 정보
	SellerReviewPointRequest sellerReviewPoint;

	public SellerPurchasePointRequest getSellerPurchasePoint() {
		return sellerPurchasePoint;
	}

	public void setSellerPurchasePoint(SellerPurchasePointRequest sellerPurchasePoint) {
		this.sellerPurchasePoint = sellerPurchasePoint;
	}

	public SellerReviewPointRequest getSellerReviewPoint() {
		return sellerReviewPoint;
	}

	public void setSellerReviewPoint(SellerReviewPointRequest sellerReviewPoint) {
		this.sellerReviewPoint = sellerReviewPoint;
	}

	@Override
	public String toString() {
		return "ProductPointsRequest [sellerPurchasePoint=" + sellerPurchasePoint + ", sellerReviewPoint="
				+ sellerReviewPoint + ", getSellerPurchasePoint()=" + getSellerPurchasePoint()
				+ ", getSellerReviewPoint()=" + getSellerReviewPoint() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

}
