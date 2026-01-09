package com.cware.netshopping.panaver.v3.domain;



import java.util.List;

import com.cware.framework.core.basic.AbstractMessage;

public class ProductOrderInfoMsg extends AbstractMessage{

	private static final long serialVersionUID = -2919783066632209597L;
	
	List<ProductOrderInfoAll> productOrderInfoList ;
	
	public ProductOrderInfoMsg (String code, String message, List<ProductOrderInfoAll> productOrderInfoList) {
		this.setCode(code);
		this.setMessage(message);
		this.setProductOrderInfoList(productOrderInfoList);
	}

	public List<ProductOrderInfoAll> getProductOrderInfoList() {
		return productOrderInfoList;
	}

	public void setProductOrderInfoList(List<ProductOrderInfoAll> productOrderInfoList) {
		this.productOrderInfoList = productOrderInfoList;
	}
	
}
