package com.cware.netshopping.domain.paintp.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 구매확정목록 > 주문정보 > 주문상품정보 xml mapping VO
 * @author FIC05202
 *
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "PRD")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PaIntpOrderCompleteProductVO extends PaIntpProductVO {
	
	/** 주문수량 */
	@XmlElement(name = "ORD_QTY")
	private Integer ordQty;

	/** 구매확정일자(yyyyMMddHHmmss) */
	@XmlElement(name = "ORD_COMP_DTS")
	private String ordCompDts;

	public Integer getOrdQty() {
		return ordQty;
	}

	public void setOrdQty(Integer ordQty) {
		this.ordQty = ordQty;
	}

	public String getOrdCompDts() {
		return ordCompDts;
	}

	public void setOrdCompDts(String ordCompDts) {
		this.ordCompDts = ordCompDts;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
