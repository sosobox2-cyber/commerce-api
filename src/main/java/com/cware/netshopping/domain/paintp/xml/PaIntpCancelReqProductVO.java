package com.cware.netshopping.domain.paintp.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 주문취소 요청 상품
 * @author FIC05202
 *
 */
@XmlRootElement(name = "PRD")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PaIntpCancelReqProductVO extends AbstractPaIntpCancelProductVO {
	private static final long serialVersionUID = 1L;
	
	/** 클레임요청철회일자(yyyyMMddHHmmss) */
	@XmlElement(name = "CLMREQ_CNCL_DTS")
	private String clmreqCnclDts;

	public String getClmreqCnclDts() {
		return clmreqCnclDts;
	}

	public void setClmreqCnclDts(String clmreqCnclDts) {
		this.clmreqCnclDts = clmreqCnclDts;
	}

}
