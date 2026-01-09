package com.cware.netshopping.domain;


import com.cware.netshopping.domain.model.PaGoodsdtMapping;


public class Pa11stGoodsdtMappingVO extends PaGoodsdtMapping {
	private static final long serialVersionUID = 1L;

	private String productNo;
	private String sellerStockCd;

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getSellerStockCd() {
		return sellerStockCd;
	}

	public void setSellerStockCd(String sellerStockCd) {
		this.sellerStockCd = sellerStockCd;
	}
	
}