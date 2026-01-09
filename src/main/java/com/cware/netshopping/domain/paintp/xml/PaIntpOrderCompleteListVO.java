package com.cware.netshopping.domain.paintp.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 구매확정목록 xml mapping VO
 * @author FIC05202
 *
 */
@XmlRootElement(name = "ORDER_LIST")
@XmlAccessorType(value = XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class PaIntpOrderCompleteListVO extends AbstractPaIntpOrderResultVO {

	@XmlElement(name = "ORDER")
	private List<PaIntpOrderCompleteVO> orderList;
	
	public List<PaIntpOrderCompleteVO> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<PaIntpOrderCompleteVO> orderList) {
		this.orderList = orderList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
