
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UploadImageRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UploadImageRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductRequestType">
 *       &lt;sequence>
 *         &lt;element name="SellerId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ImageURLList" type="{http://shopn.platform.nhncorp.com/}ImageURLListType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UploadImageRequestType", propOrder = {
    "sellerId",
    "imageURLList"
})
public class UploadImageRequestType
    extends BaseProductRequestType
{

    @XmlElement(name = "SellerId", namespace = "", required = true)
    protected String sellerId;
    @XmlElement(name = "ImageURLList", namespace = "", required = true)
    protected ImageURLListType imageURLList;

    /**
     * Gets the value of the sellerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellerId() {
        return sellerId;
    }

    /**
     * Sets the value of the sellerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellerId(String value) {
        this.sellerId = value;
    }

    /**
     * Gets the value of the imageURLList property.
     * 
     * @return
     *     possible object is
     *     {@link ImageURLListType }
     *     
     */
    public ImageURLListType getImageURLList() {
        return imageURLList;
    }

    /**
     * Sets the value of the imageURLList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageURLListType }
     *     
     */
    public void setImageURLList(ImageURLListType value) {
        this.imageURLList = value;
    }

}
