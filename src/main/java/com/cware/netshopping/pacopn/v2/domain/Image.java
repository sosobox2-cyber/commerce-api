package com.cware.netshopping.pacopn.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Image {

	// 이미지표시순서
	// 0,1,2...
	int imageOrder;

	//	이미지타입
	//	대표이미지 타입
	//	3MB 이하의 정사각형 이미지를 JPG, PNG로 등록 가능 (최소 500 x 500px, 최대 5000 x 5000px)
	//	● 필수
	//	REPRESENTATION : 정사각형 대표이미지
	//	● 선택
	//	DETAIL : 기타이미지 (최대 9개까지 등록 가능)
	//	USED_PRODUCT : 중고상태 이미지 (최대 4개까지 등록 가능)
	String imageType;

	//	쿠팡CDN경로
	//	쿠팡 CDN 에 올린 경우 직접 입력, vendorPath와 cdnPath 중 하나 이상 필수
	String cdnPath;

	//	업체이미지경로
	//	업체에서 사용하는 이미지 경로, http://로 시작하는 경로일 경우 자동 다운로드하여 쿠팡 CDN에 추가됨, vendorPath와 cdnPath 중 하나 이상 필수
	String vendorPath;

	public int getImageOrder() {
		return imageOrder;
	}

	public void setImageOrder(int imageOrder) {
		this.imageOrder = imageOrder;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getCdnPath() {
		return cdnPath;
	}

	public void setCdnPath(String cdnPath) {
		this.cdnPath = cdnPath;
	}

	public String getVendorPath() {
		return vendorPath;
	}

	public void setVendorPath(String vendorPath) {
		this.vendorPath = vendorPath;
	}

	@Override
	public String toString() {
		return "Image [imageOrder=" + imageOrder + ", imageType=" + imageType + ", cdnPath=" + cdnPath + ", vendorPath="
				+ vendorPath + "]";
	}
	
}
