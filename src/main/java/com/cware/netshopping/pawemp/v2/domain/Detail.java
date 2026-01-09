package com.cware.netshopping.pawemp.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Detail {

	// 상품 대표 이미지 URL (최대 200자) (필수)
	// - [최적화 가이드] 사이즈: 460*460 / 최소:200*200 / 용량: 2MB 이하 / 파일 : JPG, JPEG, PNG
	String basicImgUrl;
	// 상품 추가 이미지 URL (최대 200자, 최대 2개)
	// - [최적화 가이드] 사이즈: 460*460 / 최소: 200*200 / 용량: 2MB 이하 / 파일 : JPG, JPEG, PNG
	List<String> addImgUrlList;
	// 리스팅 이미지 URL (최대 200자) (필수)
	// - [최적화 가이드] 사이즈: 580*320 / 최소:200*100 / 용량: 2MB 이하 / 파일 : JPG, JPEG, PNG
	String listImgUrl;
	// 상세정보 타입 (필수)
	// (IMG:이미지 - 아래 descImgUrlList 항목에 입력, HTML:html - 아래 descHtml 항목에 입력)
	String descType;
	// 상품 상세설명 이미지 URL (최대 200자, 최대 50개)
	// - [최적화 가이드] 사이즈:758*3000(최대) / 용량: 2MB 이하 / 파일 : JPG, JPEG, PNG
	List<String> descImgUrlList;
	// 상품 상세설명 HTML (최대 65535byte)
	String descHtml;

	public String getBasicImgUrl() {
		return basicImgUrl;
	}

	public void setBasicImgUrl(String basicImgUrl) {
		this.basicImgUrl = basicImgUrl;
	}

	public List<String> getAddImgUrlList() {
		return addImgUrlList;
	}

	public void setAddImgUrlList(List<String> addImgUrlList) {
		this.addImgUrlList = addImgUrlList;
	}

	public String getListImgUrl() {
		return listImgUrl;
	}

	public void setListImgUrl(String listImgUrl) {
		this.listImgUrl = listImgUrl;
	}

	public String getDescType() {
		return descType;
	}

	public void setDescType(String descType) {
		this.descType = descType;
	}

	public List<String> getDescImgUrlList() {
		return descImgUrlList;
	}

	public void setDescImgUrlList(List<String> descImgUrlList) {
		this.descImgUrlList = descImgUrlList;
	}

	public String getDescHtml() {
		return descHtml;
	}

	public void setDescHtml(String descHtml) {
		this.descHtml = descHtml;
	}

	@Override
	public String toString() {
		return "Detail [basicImgUrl=" + basicImgUrl + ", addImgUrlList=" + addImgUrlList + ", listImgUrl=" + listImgUrl
				+ ", descType=" + descType + ", descImgUrlList=" + descImgUrlList + ", descHtml=" + descHtml + "]";
	}

}
