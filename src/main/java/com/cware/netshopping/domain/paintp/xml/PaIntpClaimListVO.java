package com.cware.netshopping.domain.paintp.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

@XmlRootElement(name = "ORDER_LIST")
@XmlAccessorType(value = XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class PaIntpClaimListVO extends AbstractPaIntpClaimResultVO {

	@XmlElement(name = "ORDER")
	private List<PaIntpClaimVO> claimList;
	
	public List<PaIntpClaimVO> getClaimList() {
		return claimList;
	}

	public void setClaimList(List<PaIntpClaimVO> claimList) {
		this.claimList = claimList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
