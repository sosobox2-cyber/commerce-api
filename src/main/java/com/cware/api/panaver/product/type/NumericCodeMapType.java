package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for NumericCodeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NumericCodeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CertificationCategory>" type="{http://shopn.platform.nhncorp.com/}NumericCodeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NumericCodeMapType", propOrder = {
    "certificationCategory"
})
public class NumericCodeMapType {

	@XmlElement(name = "CertificationCategory")
    protected List<NumericCodeType> certificationCategory;
	
	public List<NumericCodeType> getCertificationCategory() {
        if (certificationCategory == null) {
        	certificationCategory = new ArrayList<NumericCodeType>();
        }
        return this.certificationCategory;
    }

}
