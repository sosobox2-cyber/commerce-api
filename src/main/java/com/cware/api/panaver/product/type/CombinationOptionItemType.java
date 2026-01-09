
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CombinationOptionItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CombinationOptionItemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="Value1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Value2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Value3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Value4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Value5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Price" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="SellerManagerCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Usable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CombinationOptionItemType", propOrder = {
    "id",
    "value1",
    "value2",
    "value3",
    "value4",
    "value5",
    "price",
    "quantity",
    "sellerManagerCode",
    "usable"
})
public class CombinationOptionItemType {

    @XmlElement(name = "Id")
    protected Long id;
    @XmlElement(name = "Value1", required = true)
    protected String value1;
    @XmlElement(name = "Value2")
    protected String value2;
    @XmlElement(name = "Value3")
    protected String value3;
    @XmlElement(name = "Value4")
    protected String value4;
    @XmlElement(name = "Value5")
    protected String value5;
    @XmlElement(name = "Price")
    protected Long price;
    @XmlElement(name = "Quantity")
    protected Long quantity;
    @XmlElement(name = "SellerManagerCode")
    protected String sellerManagerCode;
    @XmlElement(name = "Usable", required = true)
    protected String usable;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the value1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue1() {
        return value1;
    }

    /**
     * Sets the value of the value1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue1(String value) {
        this.value1 = value;
    }

    /**
     * Gets the value of the value2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue2() {
        return value2;
    }

    /**
     * Sets the value of the value2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue2(String value) {
        this.value2 = value;
    }

    /**
     * Gets the value of the value3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue3() {
        return value3;
    }

    /**
     * Sets the value of the value3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue3(String value) {
        this.value3 = value;
    }

    /**
     * Gets the value of the value4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue4() {
        return value4;
    }

    /**
     * Sets the value of the value4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue4(String value) {
        this.value4 = value;
    }

    /**
     * Gets the value of the value5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue5() {
        return value5;
    }

    /**
     * Sets the value of the value5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue5(String value) {
        this.value5 = value;
    }

    /**
     * Gets the value of the price property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPrice(Long value) {
        this.price = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setQuantity(Long value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the sellerManagerCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellerManagerCode() {
        return sellerManagerCode;
    }

    /**
     * Sets the value of the sellerManagerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellerManagerCode(String value) {
        this.sellerManagerCode = value;
    }

    /**
     * Gets the value of the usable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsable() {
        return usable;
    }

    /**
     * Sets the value of the usable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsable(String value) {
        this.usable = value;
    }

}
