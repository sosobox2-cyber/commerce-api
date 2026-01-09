package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ReviewPointPolicy {
		
	// 텍스트 리뷰 포인트
	private int textReviewPoint;
	// 포토 동영상 리뷰 포인트
	private int photoVideoReviewPoint;
	// 한 달 사용 텍스트 리뷰 포인트
	private int afterUseTextReviewPoint;
	// 한 달 사용 포토 동영상 리뷰 포인트
	private int afterUsePhotoVideoReviewPoint;
	// 알림받기 동의/소식알림(톡톡친구) 회원 리뷰 작성 포인트
	private int storeMemberReviewPoint;
	// 적립 시작일
	private String startDate;
	// 적립 종료일
	private String endDate;
	
	
	public int getTextReviewPoint() {
		return textReviewPoint;
	}

	public void setTextReviewPoint(int textReviewPoint) {
		this.textReviewPoint = textReviewPoint;
	}

	public int getPhotoVideoReviewPoint() {
		return photoVideoReviewPoint;
	}

	public void setPhotoVideoReviewPoint(int photoVideoReviewPoint) {
		this.photoVideoReviewPoint = photoVideoReviewPoint;
	}

	public int getAfterUseTextReviewPoint() {
		return afterUseTextReviewPoint;
	}

	public void setAfterUseTextReviewPoint(int afterUseTextReviewPoint) {
		this.afterUseTextReviewPoint = afterUseTextReviewPoint;
	}

	public int getAfterUsePhotoVideoReviewPoint() {
		return afterUsePhotoVideoReviewPoint;
	}

	public void setAfterUsePhotoVideoReviewPoint(int afterUsePhotoVideoReviewPoint) {
		this.afterUsePhotoVideoReviewPoint = afterUsePhotoVideoReviewPoint;
	}

	public int getStoreMemberReviewPoint() {
		return storeMemberReviewPoint;
	}

	public void setStoreMemberReviewPoint(int storeMemberReviewPoint) {
		this.storeMemberReviewPoint = storeMemberReviewPoint;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "ReviewPointPolicy [textReviewPoint=" + textReviewPoint +"photoVideoReviewPoint=" + photoVideoReviewPoint + "afterUseTextReviewPoint=" + afterUseTextReviewPoint + "afterUsePhotoVideoReviewPoint=" + afterUsePhotoVideoReviewPoint
				+ "storeMemberReviewPoint=" + storeMemberReviewPoint +"startDate=" + startDate +"endDate=" + endDate+"]";
	}

}
