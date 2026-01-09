package com.cware.netshopping.domain.paintp.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 주문취소요청목록(ORDER_LIST) > 주문취소요청(ORDER) xml mapping class
 * @author FIC05202
 *
 */
@XmlRootElement(name = "ORDER")
@XmlAccessorType(value = XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class PaIntpCancelReqResultVO extends AbstractPaIntpCancelReqVO {
	
	/** 주문 취소 요청 상품 목록 */
	@XmlElementWrapper(name = "PRODUCT")
	@XmlElement(name = "PRD")
	private List<PaIntpCancelReqResultProductVO> productList;

	public List<PaIntpCancelReqResultProductVO> getProductList() {
		return productList;
	}

	public void setProductList(List<PaIntpCancelReqResultProductVO> productList) {
		this.productList = productList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
