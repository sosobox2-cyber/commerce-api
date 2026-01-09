
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UploadImageResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UploadImageResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="ImageList" type="{http://shopn.platform.nhncorp.com/}ImageReturnListType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UploadImageResponseType", propOrder = {
    "imageList"
})
public class UploadImageResponseType
    extends BaseProductResponseType
{

    @XmlElement(name = "ImageList", namespace = "")
    protected ImageReturnListType imageList;

    /**
     * Gets the value of the imageList property.
     * 
     * @return
     *     possible object is
     *     {@link ImageReturnListType }
     *     
     */
    public ImageReturnListType getImageList() {
        return imageList;
    }

    /**
     * Sets the value of the imageList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageReturnListType }
     *     
     */
    public void setImageList(ImageReturnListType value) {
        this.imageList = value;
    }

}
