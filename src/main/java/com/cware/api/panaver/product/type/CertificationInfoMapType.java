package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for CertificationInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CertificationInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CertificationInfo>" type="{http://shopn.platform.nhncorp.com/}CertificationInfoType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CertificationInfoMapType", propOrder = {
    "certificationInfo"
})
public class CertificationInfoMapType {

	@XmlElement(name = "CertificationInfo")
    protected List<CertificationInfoType> certificationInfo;
	
	public List<CertificationInfoType> getCertificationInfo() {
        if (certificationInfo == null) {
        	certificationInfo = new ArrayList<CertificationInfoType>();
        }
        return this.certificationInfo;
    }

}
