package com.cware.netshopping.domain.paintp.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

@XmlRootElement(name = "PRD")
@XmlAccessorType(value = XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class PaIntpCancelReqResultProductVO extends AbstractPaIntpCancelProductVO {

	/** 클레임 요청승인 일자(yyyyMMddHHmmss) */
	@XmlElement(name = "CLMREQ_ACCEPT_DTS")
	private String clmreqAcceptDts;
	
	/** 클레임요청거부일자(yyyyMMddHHmmss) */
	@XmlElement(name = "CLMREQ_REFUSE_DTS")
	private String clmreqRefuseDts;
	
	/** 쿨레임요청거부사유 */
	@XmlElement(name = "CLMREQ_REFUSE_RSN_DTL")
	private String clmreqRefuseRsnDtl;
	
	public String getClmreqAcceptDts() {
		return clmreqAcceptDts;
	}

	public void setClmreqAcceptDts(String clmreqAcceptDts) {
		this.clmreqAcceptDts = clmreqAcceptDts;
	}

	public String getClmreqRefuseDts() {
		return clmreqRefuseDts;
	}

	public void setClmreqRefuseDts(String clmreqRefuseDts) {
		this.clmreqRefuseDts = clmreqRefuseDts;
	}

	public String getClmreqRefuseRsnDtl() {
		return clmreqRefuseRsnDtl;
	}

	public void setClmreqRefuseRsnDtl(String clmreqRefuseRsnDtl) {
		this.clmreqRefuseRsnDtl = clmreqRefuseRsnDtl;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
