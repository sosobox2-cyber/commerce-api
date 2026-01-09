package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for BundleGroupType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BundleGroupType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Available" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Base" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CalculationMethod" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeliveryFeeByArea" type="{http://shopn.platform.nhncorp.com/}DeliveryFeeByAreaType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BundleGroupType", propOrder = {
	    "id",
	    "name",
	    "available",
	    "base",
	    "calculationMethod",
	    "deliveryFeeByArea"
})
public class BundleGroupType {

    @XmlElement(name = "Id")
    protected long id;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "Available")
    protected String available;
    @XmlElement(name = "Base")
    protected String base;
    @XmlElement(name = "CalculationMethod")
    protected String calculationMethod;
    @XmlElement(name = "DeliveryFeeByArea")
    protected List<DeliveryFeeByAreaType> deliveryFeeByArea;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvailable() {
		return available;
	}
	public void setAvailable(String available) {
		this.available = available;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getCalculationMethod() {
		return calculationMethod;
	}
	public void setCalculationMethod(String calculationMethod) {
		this.calculationMethod = calculationMethod;
	}
	public List<DeliveryFeeByAreaType> getDeliveryFeeByArea() {
        if (deliveryFeeByArea == null) {
        	deliveryFeeByArea = new ArrayList<DeliveryFeeByAreaType>();
        }
		return this.deliveryFeeByArea;
	}
	public void setDeliveryFeeByArea(List<DeliveryFeeByAreaType> deliveryFeeByArea) {
		this.deliveryFeeByArea = deliveryFeeByArea;
	}
    
    
}
