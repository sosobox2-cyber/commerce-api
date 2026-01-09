
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BaseRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AccessCredentials" type="{http://shopn.platform.nhncorp.com/}AccessCredentialsType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseRequestType", propOrder = {
    "requestID",
    "accessCredentials"
})
@XmlSeeAlso({
    BaseProductRequestType.class
})
public abstract class BaseRequestType {

    @XmlElement(name = "RequestID")
    protected String requestID;
    @XmlElement(name = "AccessCredentials", required = true)
    protected AccessCredentialsType accessCredentials;

    /**
     * Gets the value of the requestID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestID(String value) {
        this.requestID = value;
    }

    /**
     * Gets the value of the accessCredentials property.
     * 
     * @return
     *     possible object is
     *     {@link AccessCredentialsType }
     *     
     */
    public AccessCredentialsType getAccessCredentials() {
        return accessCredentials;
    }

    /**
     * Sets the value of the accessCredentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessCredentialsType }
     *     
     */
    public void setAccessCredentials(AccessCredentialsType value) {
        this.accessCredentials = value;
    }

}
