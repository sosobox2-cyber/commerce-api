package com.cware.netshopping.domain.paintp.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 주문취소요청목록(ORDER_LIST) > 주문취소요청(ORDER) xml mapping class
 * @author FIC05202
 *
 */
@XmlRootElement(name = "ORDER")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaIntpCancelReqVO extends AbstractPaIntpCancelReqVO {
	private static final long serialVersionUID = 1L;

	/** 주문 취소 요청 상품 목록 */
	@XmlElementWrapper(name = "PRODUCT")
	@XmlElement(name = "PRD")
	private List<PaIntpCancelReqProductVO> productList;

	public List<PaIntpCancelReqProductVO> getProductList() {
		return productList;
	}

	public void setProductList(List<PaIntpCancelReqProductVO> productList) {
		this.productList = productList;
	}
	
}
