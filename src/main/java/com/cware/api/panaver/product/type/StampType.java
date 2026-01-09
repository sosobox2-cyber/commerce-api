
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StampType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StampType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PurchaseReviewPaymentCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="PremiumPaymentCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ReqularCustomerPaymentCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="StartDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StampType", propOrder = {
    "purchaseReviewPaymentCount",
    "premiumPaymentCount",
    "reqularCustomerPaymentCount",
    "startDate",
    "endDate"
})
public class StampType {

    @XmlElement(name = "PurchaseReviewPaymentCount")
    protected Integer purchaseReviewPaymentCount;
    @XmlElement(name = "PremiumPaymentCount")
    protected Integer premiumPaymentCount;
    @XmlElement(name = "ReqularCustomerPaymentCount")
    protected Integer reqularCustomerPaymentCount;
    @XmlElement(name = "StartDate")
    protected String startDate;
    @XmlElement(name = "EndDate")
    protected String endDate;

    /**
     * Gets the value of the purchaseReviewPaymentCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPurchaseReviewPaymentCount() {
        return purchaseReviewPaymentCount;
    }

    /**
     * Sets the value of the purchaseReviewPaymentCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPurchaseReviewPaymentCount(Integer value) {
        this.purchaseReviewPaymentCount = value;
    }

    /**
     * Gets the value of the premiumPaymentCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPremiumPaymentCount() {
        return premiumPaymentCount;
    }

    /**
     * Sets the value of the premiumPaymentCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPremiumPaymentCount(Integer value) {
        this.premiumPaymentCount = value;
    }

    /**
     * Gets the value of the reqularCustomerPaymentCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReqularCustomerPaymentCount() {
        return reqularCustomerPaymentCount;
    }

    /**
     * Sets the value of the reqularCustomerPaymentCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReqularCustomerPaymentCount(Integer value) {
        this.reqularCustomerPaymentCount = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartDate(String value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndDate(String value) {
        this.endDate = value;
    }

}
