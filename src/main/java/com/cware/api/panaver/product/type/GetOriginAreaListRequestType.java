package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetOriginAreaListRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetOriginAreaListRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductRequestType">
 *       &lt;sequence>
 *         &lt;element name"OriginAreaName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name"OriginAreaCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 * 	 &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetOriginAreaListRequestType")
public class GetOriginAreaListRequestType extends BaseProductRequestType {
	@XmlElement(name = "OriginAreaName", namespace = "")
    protected String originAreaName;
	@XmlElement(name = "OriginAreaCode", namespace = "")
    protected String originAreaCode;

	public String getOriginAreaCode() {
		return originAreaCode;
	}
	public void setOriginAreaCode(String originAreaCode) {
		this.originAreaCode = originAreaCode;
	}
	public String getOriginAreaName() {
		return originAreaName;
	}
	public void setOriginAreaName(String originAreaName) {
		this.originAreaName = originAreaName;
	}		
}
