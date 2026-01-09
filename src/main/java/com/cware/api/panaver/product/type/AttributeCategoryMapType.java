package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for AttributeCategoryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AttributeCategoryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AttributeCategory>" type="{http://shopn.platform.nhncorp.com/}AttributeCategoryType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttributeCategoryMapType", propOrder = {
    "attributeCategory"
})
public class AttributeCategoryMapType {

	@XmlElement(name = "AttributeCategory")
    protected List<AttributeCategoryType> attributeCategory;
	
	public List<AttributeCategoryType> getAttributeCategory() {
        if (attributeCategory == null) {
        	attributeCategory = new ArrayList<AttributeCategoryType>();
        }
        return this.attributeCategory;
    }

}
