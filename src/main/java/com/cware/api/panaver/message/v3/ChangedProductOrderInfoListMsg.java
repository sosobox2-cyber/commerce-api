package com.cware.api.panaver.message.v3;

import java.util.List;

import com.cware.framework.core.basic.AbstractMessage;
import com.cware.netshopping.panaver.v3.domain.ChangedProductOrderInfo;

public class ChangedProductOrderInfoListMsg extends AbstractMessage {

	static final long serialVersionUID = -6387052718204789793L;
	
	// 네이버 상품주문변경 조회 결과
	private List<ChangedProductOrderInfo> changedProductOrderInfoList; 
	
	// 네이버 상품주문변경 조회 결과 테이블에 신규 저장된 주문 리스트 (TPANAVERORDERCHANGE INERT)
	// 재조회시 해당 리스트에 담기지 않음 (MERGE)
	private List<ChangedProductOrderInfo> productOrderInfoList;
	
	public ChangedProductOrderInfoListMsg(String code, String message, List<ChangedProductOrderInfo> productOrderInfoList, List<ChangedProductOrderInfo> changedProductOrderInfoList) {
    	this.setCode(code);
    	this.setMessage(message);
    	this.setChangedProductOrderInfoList(changedProductOrderInfoList);
    	this.setProductOrderInfoList(productOrderInfoList);
    }
	
	public List<ChangedProductOrderInfo> getChangedProductOrderInfoList() {
		return changedProductOrderInfoList;
	}

	public void setChangedProductOrderInfoList(List<ChangedProductOrderInfo> changedProductOrderInfoList) {
		this.changedProductOrderInfoList = changedProductOrderInfoList;
	}

	public List<ChangedProductOrderInfo> getProductOrderInfoList() {
		return productOrderInfoList;
	}

	public void setProductOrderInfoList(List<ChangedProductOrderInfo> productOrderInfoList) {
		this.productOrderInfoList = productOrderInfoList;
	}
}
