
package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for StringCodeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StringMapType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OriginArea" type="{http://shopn.platform.nhncorp.com/}StringCodeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StringMapType", propOrder = {
	"originArea"
})
public class StringMapType {
	
	@XmlElement(name = "OriginArea")
    protected List<StringCodeType> originArea;
	
	public List<StringCodeType> getOriginArea() {
        if (originArea == null) {
        	originArea = new ArrayList<StringCodeType>();
        }
        return this.originArea;
    }
}
