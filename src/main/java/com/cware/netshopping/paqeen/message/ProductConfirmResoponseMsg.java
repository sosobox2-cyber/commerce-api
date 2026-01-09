package com.cware.netshopping.paqeen.message;

import com.cware.framework.core.basic.AbstractMessage;
import com.cware.netshopping.paqeen.domain.ProductResponse;

public class ProductConfirmResoponseMsg extends AbstractMessage {

	private static final long serialVersionUID = 461036002870619572L;
	
	private ProductResponse productResponse; 
	
	
	public ProductConfirmResoponseMsg(String code, String message, ProductResponse productResponse) {
    	this.setCode(code);
    	this.setMessage(message);
    	this.setProductResponse(productResponse);
    }


	public ProductResponse getProductResponse() {
		return productResponse;
	}


	public void setProductResponse(ProductResponse productResponse) {
		this.productResponse = productResponse;
	}


}
