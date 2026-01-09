package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for BundleGroupListMapType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BundleGroupListMapType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BundleGroup" type="{http://shopn.platform.nhncorp.com/}NumericCodeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BundleGroupListMapType", propOrder = {
	    "bundleGroup"
})
public class BundleGroupListMapType {
	
	@XmlElement(name = "BundleGroup")
    protected List<NumericCodeType> bundleGroup;
	
	public List<NumericCodeType> getBundleGroup() {
        if (bundleGroup == null) {
        	bundleGroup = new ArrayList<NumericCodeType>();
        }
        return this.bundleGroup;
    }
}
