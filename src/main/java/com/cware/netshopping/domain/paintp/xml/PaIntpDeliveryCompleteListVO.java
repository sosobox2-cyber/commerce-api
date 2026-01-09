package com.cware.netshopping.domain.paintp.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 배송완료목록조회 xml mapping VO
 * @author FIC05202
 *
 */
@XmlRootElement(name = "ORDER_LIST")
@XmlAccessorType(value = XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class PaIntpDeliveryCompleteListVO extends AbstractPaIntpOrderResultVO {

	/** 주문정보 */
	@XmlElement(name = "ORDER")
	private List<PaIntpOrderVO> orderList;
	
	/** 페이징 정보 */
	@XmlElement(name = "PAGE_INFO")
	private PaIntpPageInfoVO pageInfo;
	
	public List<PaIntpOrderVO> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<PaIntpOrderVO> orderList) {
		this.orderList = orderList;
	}

	public PaIntpPageInfoVO getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PaIntpPageInfoVO pageInfo) {
		this.pageInfo = pageInfo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
