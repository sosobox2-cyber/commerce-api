
package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetReturnsCompanyListResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetReturnsCompanyListResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="ReturnsCompanyList" type="{http://shopn.platform.nhncorp.com/}ReturnsCompanyStringMapType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetReturnsCompanyListResponseType", propOrder = {
    "returnsCompanyList"
})
public class GetReturnsCompanyListResponseType extends BaseProductResponseType {
	
	@XmlElement(name = "ReturnsCompanyList", namespace = "")
    protected List<ReturnsCompanyStringMapType> returnsCompanyList;
	
	public List<ReturnsCompanyStringMapType> getReturnsCompanyList() {
		if (returnsCompanyList == null) {
			returnsCompanyList = new ArrayList<ReturnsCompanyStringMapType>();
        }
		return this.returnsCompanyList;
	}
}
