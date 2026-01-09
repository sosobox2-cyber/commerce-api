package com.cware.netshopping.domain.paintp.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 주문취소요청목록(ORDER_LIST) > 주문취소요청(ORDER) xml mapping VO
 * @author FIC05202
 *
 */
@XmlRootElement(name = "ORDER_LIST")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PaIntpCancelReqListVO extends AbstractPaIntpOrderResultVO {
	private static final long serialVersionUID = 1L;

	/** 주문취소요청목록 */
	@XmlElement(name = "ORDER")
	private List<PaIntpCancelReqVO> orderList;

	public List<PaIntpCancelReqVO> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<PaIntpCancelReqVO> orderList) {
		this.orderList = orderList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
