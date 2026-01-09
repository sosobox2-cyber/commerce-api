package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PurchaseReviewInfo {
		
	// 리뷰 노출 여부
	private String purchaseReviewExposure;	
	// 리뷰 미노출 사유
	private String reviewUnExposeReason;
		
	
	public String getPurchaseReviewExposure() {
		return purchaseReviewExposure;
	}
	public void setPurchaseReviewExposure(String purchaseReviewExposure) {
		this.purchaseReviewExposure = purchaseReviewExposure;
	}
	public String getReviewUnExposeReason() {
		return reviewUnExposeReason;
	}
	public void setReviewUnExposeReason(String reviewUnExposeReason) {
		this.reviewUnExposeReason = reviewUnExposeReason;
	}

	@Override
	public String toString() {
		return "PurchaseReviewInfo [purchaseReviewExposure=" + purchaseReviewExposure + "reviewUnExposeReason="+ reviewUnExposeReason + "]";
	}

}
