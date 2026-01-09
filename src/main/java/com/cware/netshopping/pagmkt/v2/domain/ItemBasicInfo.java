package com.cware.netshopping.pagmkt.v2.domain;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ItemBasicInfo {

	// 다국어 상품명은 검색용, 프로모션용 상품명 구분없이 100byte까지 입력 가능
	private MultiLanguage goodsName;

	private Category category;

	// 도서상품용
	private Map<String, Object> book;

	// 모델명/브랜드/바코드
	private Catalog catalog;

	private Boolean is3PL;

	public MultiLanguage getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(MultiLanguage goodsName) {
		this.goodsName = goodsName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Map<String, Object> getBook() {
		return book;
	}

	public void setBook(Map<String, Object> book) {
		this.book = book;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	@JsonProperty("isIs3PL")
	public Boolean isIs3PL() {
		return is3PL;
	}

	@JsonProperty("isIs3PL")
	public void setIs3PL(Boolean is3pl) {
		is3PL = is3pl;
	}

	@Override
	public String toString() {
		return "ItemBasicInfo [goodsName=" + goodsName + ", category=" + category + ", book=" + book + ", catalog="
				+ catalog + ", is3PL=" + is3PL + "]";
	}

}
