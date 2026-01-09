
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ECouponType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ECouponType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PeriodType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ValidStartDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ValidEndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PeriodDays" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="PublicInformationContents" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ContactInformationContents" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UsePlaceType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UsePlaceContents" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RestrictCart" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ECouponType", propOrder = {
    "periodType",
    "validStartDate",
    "validEndDate",
    "periodDays",
    "publicInformationContents",
    "contactInformationContents",
    "usePlaceType",
    "usePlaceContents",
    "restrictCart"
})
public class ECouponType {

    @XmlElement(name = "PeriodType", required = true)
    protected String periodType;
    @XmlElement(name = "ValidStartDate")
    protected String validStartDate;
    @XmlElement(name = "ValidEndDate")
    protected String validEndDate;
    @XmlElement(name = "PeriodDays")
    protected Integer periodDays;
    @XmlElement(name = "PublicInformationContents", required = true)
    protected String publicInformationContents;
    @XmlElement(name = "ContactInformationContents", required = true)
    protected String contactInformationContents;
    @XmlElement(name = "UsePlaceType", required = true)
    protected String usePlaceType;
    @XmlElement(name = "UsePlaceContents", required = true)
    protected String usePlaceContents;
    @XmlElement(name = "RestrictCart", required = true)
    protected String restrictCart;

    /**
     * Gets the value of the periodType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeriodType() {
        return periodType;
    }

    /**
     * Sets the value of the periodType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeriodType(String value) {
        this.periodType = value;
    }

    /**
     * Gets the value of the validStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidStartDate() {
        return validStartDate;
    }

    /**
     * Sets the value of the validStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidStartDate(String value) {
        this.validStartDate = value;
    }

    /**
     * Gets the value of the validEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidEndDate() {
        return validEndDate;
    }

    /**
     * Sets the value of the validEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidEndDate(String value) {
        this.validEndDate = value;
    }

    /**
     * Gets the value of the periodDays property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPeriodDays() {
        return periodDays;
    }

    /**
     * Sets the value of the periodDays property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPeriodDays(Integer value) {
        this.periodDays = value;
    }

    /**
     * Gets the value of the publicInformationContents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublicInformationContents() {
        return publicInformationContents;
    }

    /**
     * Sets the value of the publicInformationContents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublicInformationContents(String value) {
        this.publicInformationContents = value;
    }

    /**
     * Gets the value of the contactInformationContents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactInformationContents() {
        return contactInformationContents;
    }

    /**
     * Sets the value of the contactInformationContents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactInformationContents(String value) {
        this.contactInformationContents = value;
    }

    /**
     * Gets the value of the usePlaceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsePlaceType() {
        return usePlaceType;
    }

    /**
     * Sets the value of the usePlaceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsePlaceType(String value) {
        this.usePlaceType = value;
    }

    /**
     * Gets the value of the usePlaceContents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsePlaceContents() {
        return usePlaceContents;
    }

    /**
     * Sets the value of the usePlaceContents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsePlaceContents(String value) {
        this.usePlaceContents = value;
    }

    /**
     * Gets the value of the restrictCart property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestrictCart() {
        return restrictCart;
    }

    /**
     * Sets the value of the restrictCart property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestrictCart(String value) {
        this.restrictCart = value;
    }

}
