package com.cware.api.panaver.message;

import com.cware.api.panaver.order.seller.NaverSignature;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderInfo;
import com.cware.framework.core.basic.AbstractMessage;

public class ProductOrderInfoMsg extends AbstractMessage {

	private static final long serialVersionUID = 1907138969008697334L;
	
	private ProductOrderInfo[] productOrderInfo;
	
	private NaverSignature naverSignature;
	
	public ProductOrderInfoMsg() {
		
	}
	
	public ProductOrderInfoMsg(String code, String message) {
		this.setCode(code);
		this.setMessage(message);
	}
	
	public ProductOrderInfoMsg(String code, String message, ProductOrderInfo[] productOrderInfo, NaverSignature naverSignature) {
		this.setCode(code);
		this.setMessage(message);
		this.setProductOrderInfo(productOrderInfo);
		this.setNaverSignature(naverSignature);
	}

	public ProductOrderInfo[] getProductOrderInfo() {
		return productOrderInfo;
	}

	public void setProductOrderInfo(ProductOrderInfo[] productOrderInfo) {
		this.productOrderInfo = productOrderInfo;
	}

	public NaverSignature getNaverSignature() {
		return naverSignature;
	}

	public void setNaverSignature(NaverSignature naverSignature) {
		this.naverSignature = naverSignature;
	}

}
