package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AddtionalInfo {

	// 판매자 할인
	private SellerDiscount sellerDiscount;

	// 사이트 부담 지원 할인
	// 옥션, G마켓 에서 부담하는 사이트 할인을 적용할지 여부
	private SiteValue siteDiscount;

	// 사용 X
	private Object gift;

	// 가격비교 사이트 노출 여부
	private Pcs pcs;

	// 해외판매 진행 시, 해외배송대행/고객응대/해외마케팅 지원 등 혜택이 있고, 진행하지 않을 경우 수출 대상에서 제외됨
	// 해외판매대행 이용료 추가 부과됨
	private OverseaSales overseaSales;

	public SellerDiscount getSellerDiscount() {
		return sellerDiscount;
	}

	public void setSellerDiscount(SellerDiscount sellerDiscount) {
		this.sellerDiscount = sellerDiscount;
	}

	public SiteValue getSiteDiscount() {
		return siteDiscount;
	}

	public void setSiteDiscount(SiteValue siteDiscount) {
		this.siteDiscount = siteDiscount;
	}

	public Object getGift() {
		return gift;
	}

	public void setGift(Object gift) {
		this.gift = gift;
	}

	public Pcs getPcs() {
		return pcs;
	}

	public void setPcs(Pcs pcs) {
		this.pcs = pcs;
	}

	public OverseaSales getOverseaSales() {
		return overseaSales;
	}

	public void setOverseaSales(OverseaSales overseaSales) {
		this.overseaSales = overseaSales;
	}

	@Override
	public String toString() {
		return "AddtionalInfo [sellerDiscount=" + sellerDiscount + ", siteDiscount=" + siteDiscount + ", gift=" + gift
				+ ", pcs=" + pcs + ", overseaSales=" + overseaSales + "]";
	}

}
