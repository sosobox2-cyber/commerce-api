package com.cware.netshopping.pagmkt.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Category {

	// G마켓/옥션에서 제공하는 최하위(Leaf)카테고리 코드 등록
	// *G마켓/옥션 카테고리 조회 API 참고
	private List<Site> site;

	// 미니샵 카테고리 코드 등록
	// * 미니샵 카테고리 조회 API 참고
	private List<Shop> shop;

	// ESM 카테고리 코드 등록
	// * ESM 카테고리 조회 API 참고
	private EsmSite esm;

	public List<Site> getSite() {
		return site;
	}

	public void setSite(List<Site> site) {
		this.site = site;
	}

	public List<Shop> getShop() {
		return shop;
	}

	public void setShop(List<Shop> shop) {
		this.shop = shop;
	}

	public EsmSite getEsm() {
		return esm;
	}

	public void setEsm(EsmSite esm) {
		this.esm = esm;
	}

	@Override
	public String toString() {
		return "Category [site=" + site + ", shop=" + shop + ", esm=" + esm + "]";
	}

}
