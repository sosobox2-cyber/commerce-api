
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BaseResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResponseType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ResponseTime" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="Error" type="{http://shopn.platform.nhncorp.com/}ErrorType" minOccurs="0"/>
 *         &lt;element name="WarningList" type="{http://shopn.platform.nhncorp.com/}WarningListType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseResponseType", propOrder = {
    "requestID",
    "responseType",
    "responseTime",
    "error",
    "warningList"
})
@XmlSeeAlso({
    BaseProductResponseType.class
})
public abstract class BaseResponseType {

    @XmlElement(name = "RequestID")
    protected String requestID;
    @XmlElement(name = "ResponseType", required = true)
    protected String responseType;
    @XmlElement(name = "ResponseTime")
    protected long responseTime;
    @XmlElement(name = "Error")
    protected ErrorType error;
    @XmlElement(name = "WarningList")
    protected WarningListType warningList;

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
     * Gets the value of the responseType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseType() {
        return responseType;
    }

    /**
     * Sets the value of the responseType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseType(String value) {
        this.responseType = value;
    }

    /**
     * Gets the value of the responseTime property.
     * 
     */
    public long getResponseTime() {
        return responseTime;
    }

    /**
     * Sets the value of the responseTime property.
     * 
     */
    public void setResponseTime(long value) {
        this.responseTime = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorType }
     *     
     */
    public ErrorType getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorType }
     *     
     */
    public void setError(ErrorType value) {
        this.error = value;
    }

    /**
     * Gets the value of the warningList property.
     * 
     * @return
     *     possible object is
     *     {@link WarningListType }
     *     
     */
    public WarningListType getWarningList() {
        return warningList;
    }

    /**
     * Sets the value of the warningList property.
     * 
     * @param value
     *     allowed object is
     *     {@link WarningListType }
     *     
     */
    public void setWarningList(WarningListType value) {
        this.warningList = value;
    }

}
