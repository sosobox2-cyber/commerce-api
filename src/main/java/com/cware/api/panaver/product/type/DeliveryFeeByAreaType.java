
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DeliveryFeeByAreaType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeliveryFeeByAreaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AreaType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Area2ExtraFee" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Area3ExtraFee" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeliveryFeeByAreaType", propOrder = {
    "areaType",
    "area2ExtraFee",
    "area3ExtraFee"
})
public class DeliveryFeeByAreaType {

    @XmlElement(name = "AreaType", required = true)
    protected String areaType;
    @XmlElement(name = "Area2ExtraFee")
    protected int area2ExtraFee;
    @XmlElement(name = "Area3ExtraFee")
    protected Integer area3ExtraFee;

    /**
     * Gets the value of the areaType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAreaType() {
        return areaType;
    }

    /**
     * Sets the value of the areaType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAreaType(String value) {
        this.areaType = value;
    }

    /**
     * Gets the value of the area2ExtraFee property.
     * 
     */
    public int getArea2ExtraFee() {
        return area2ExtraFee;
    }

    /**
     * Sets the value of the area2ExtraFee property.
     * 
     */
    public void setArea2ExtraFee(int value) {
        this.area2ExtraFee = value;
    }

    /**
     * Gets the value of the area3ExtraFee property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getArea3ExtraFee() {
        return area3ExtraFee;
    }

    /**
     * Sets the value of the area3ExtraFee property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setArea3ExtraFee(Integer value) {
        this.area3ExtraFee = value;
    }

}
