package com.cware.netshopping.domain.paintp.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 구매확정목록 > 주문정보 xml mapping VO
 * @author FIC05202
 *
 */
@XmlRootElement(name = "ORDER")
@XmlAccessorType(value = XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class PaIntpOrderCompleteVO implements Serializable {
	/** id */
	@XmlAttribute(name = "ID")
	private Integer id;
	
	/** 인터파크주문번호 */
	@XmlElement(name = "ORD_NO")
	private String ordNo;
	
	/** 주문자명 */
	@XmlElement(name = "ORD_NM")
	private String ordNm;

	/** 주문일자(yyyyMMddHHmiss) */
	@XmlElement(name = "ORDER_DTS")
	private String orderDts;
	
	/** 주문 상품 목록 */
	@XmlElementWrapper(name = "PRODUCT")
	@XmlElement(name = "PRD")
	private List<PaIntpOrderCompleteProductVO> prdList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrdNo() {
		return ordNo;
	}

	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}

	public String getOrdNm() {
		return ordNm;
	}

	public void setOrdNm(String ordNm) {
		this.ordNm = ordNm;
	}

	public String getOrderDts() {
		return orderDts;
	}

	public void setOrderDts(String orderDts) {
		this.orderDts = orderDts;
	}

	public List<PaIntpOrderCompleteProductVO> getPrdList() {
		return prdList;
	}

	public void setPrdList(List<PaIntpOrderCompleteProductVO> prdList) {
		this.prdList = prdList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
