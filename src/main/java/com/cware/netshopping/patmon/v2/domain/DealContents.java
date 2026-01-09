package com.cware.netshopping.patmon.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DealContents {
	String title; // UI 노출 딜명
	String titleDecoration; // 딜 홍보 문구
	List<String> mainImages; // 메인이미지들
	String detailContents; // 딜상세 내용
	List<DealProductInfo> productInfos; // 상품정보제공고시 정보들
	String originCountryType; // 원산지 표기 방식
	String originCountryDetail; // 원산지 표기 방식 상세
	String homeRecommendedImage; // 홈추천 이미지
	DealMainVideo dealMainVideo; // 딜 대표이미지 내 동영상 정보

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleDecoration() {
		return titleDecoration;
	}

	public void setTitleDecoration(String titleDecoration) {
		this.titleDecoration = titleDecoration;
	}

	public List<String> getMainImages() {
		return mainImages;
	}

	public void setMainImages(List<String> mainImages) {
		this.mainImages = mainImages;
	}

	public String getDetailContents() {
		return detailContents;
	}

	public void setDetailContents(String detailContents) {
		this.detailContents = detailContents;
	}

	public List<DealProductInfo> getProductInfos() {
		return productInfos;
	}

	public void setProductInfos(List<DealProductInfo> productInfos) {
		this.productInfos = productInfos;
	}

	public String getOriginCountryType() {
		return originCountryType;
	}

	public void setOriginCountryType(String originCountryType) {
		this.originCountryType = originCountryType;
	}

	public String getOriginCountryDetail() {
		return originCountryDetail;
	}

	public void setOriginCountryDetail(String originCountryDetail) {
		this.originCountryDetail = originCountryDetail;
	}

	public String getHomeRecommendedImage() {
		return homeRecommendedImage;
	}

	public void setHomeRecommendedImage(String homeRecommendedImage) {
		this.homeRecommendedImage = homeRecommendedImage;
	}

	public DealMainVideo getDealMainVideo() {
		return dealMainVideo;
	}

	public void setDealMainVideo(DealMainVideo dealMainVideo) {
		this.dealMainVideo = dealMainVideo;
	}

	@Override
	public String toString() {
		return "DealContents [title=" + title + ", titleDecoration=" + titleDecoration + ", mainImages=" + mainImages
				+ ", detailContents=" + detailContents + ", productInfos=" + productInfos + ", originCountryType="
				+ originCountryType + ", originCountryDetail=" + originCountryDetail + ", homeRecommendedImage="
				+ homeRecommendedImage + ", dealMainVideo=" + dealMainVideo + "]";
	}

}
