package com.cware.netshopping.domain.paintp.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 주문내역조회 xml mapping VO
 * @author FIC05202
 *
 */
@XmlRootElement(name = "ORDER_LIST")
@XmlAccessorType(value = XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class PaIntpOrderListVO extends AbstractPaIntpOrderResultVO {

	@XmlElement(name = "ORDER")
	private List<PaIntpOrderVO> orderList;
	
	public List<PaIntpOrderVO> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<PaIntpOrderVO> orderList) {
		this.orderList = orderList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
