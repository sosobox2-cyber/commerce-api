package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaGoodsOffer;

public class PaGoodsOfferVO extends PaGoodsOffer {
	private static final long serialVersionUID = 1L;
	
	private String paOfferTypeName;
	private String paOfferCodeName;
	
	public String getPaOfferTypeName() {
		return paOfferTypeName;
	}
	public void setPaOfferTypeName(String paOfferTypeName) {
		this.paOfferTypeName = paOfferTypeName;
	}
	public String getPaOfferCodeName() {
		return paOfferCodeName;
	}
	public void setPaOfferCodeName(String paOfferCodeName) {
		this.paOfferCodeName = paOfferCodeName;
	}

}
