
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ImageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ImageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Representative" type="{http://shopn.platform.nhncorp.com/}URLType"/>
 *         &lt;element name="List" type="{http://shopn.platform.nhncorp.com/}URLType" minOccurs="0"/>
 *         &lt;element name="Image" type="{http://shopn.platform.nhncorp.com/}URLType" minOccurs="0"/>
 *         &lt;element name="Vertical" type="{http://shopn.platform.nhncorp.com/}URLType" minOccurs="0"/>
 *         &lt;element name="Gallery" type="{http://shopn.platform.nhncorp.com/}URLType" minOccurs="0"/>
 *         &lt;element name="OptionalList" type="{http://shopn.platform.nhncorp.com/}OptionalListType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImageType", propOrder = {
    "representative",
    "list",
    "image",
    "vertical",
    "gallery",
    "optionalList"
})
public class ImageType {

    @XmlElement(name = "Representative", required = true)
    protected URLType representative;
    @XmlElement(name = "List")
    protected URLType list;
    @XmlElement(name = "Image")
    protected URLType image;
    @XmlElement(name = "Vertical")
    protected URLType vertical;
    @XmlElement(name = "Gallery")
    protected URLType gallery;
    @XmlElement(name = "OptionalList")
    protected OptionalListType optionalList;

    /**
     * Gets the value of the representative property.
     * 
     * @return
     *     possible object is
     *     {@link URLType }
     *     
     */
    public URLType getRepresentative() {
        return representative;
    }

    /**
     * Sets the value of the representative property.
     * 
     * @param value
     *     allowed object is
     *     {@link URLType }
     *     
     */
    public void setRepresentative(URLType value) {
        this.representative = value;
    }

    /**
     * Gets the value of the list property.
     * 
     * @return
     *     possible object is
     *     {@link URLType }
     *     
     */
    public URLType getList() {
        return list;
    }

    /**
     * Sets the value of the list property.
     * 
     * @param value
     *     allowed object is
     *     {@link URLType }
     *     
     */
    public void setList(URLType value) {
        this.list = value;
    }

    /**
     * Gets the value of the image property.
     * 
     * @return
     *     possible object is
     *     {@link URLType }
     *     
     */
    public URLType getImage() {
        return image;
    }

    /**
     * Sets the value of the image property.
     * 
     * @param value
     *     allowed object is
     *     {@link URLType }
     *     
     */
    public void setImage(URLType value) {
        this.image = value;
    }

    /**
     * Gets the value of the vertical property.
     * 
     * @return
     *     possible object is
     *     {@link URLType }
     *     
     */
    public URLType getVertical() {
        return vertical;
    }

    /**
     * Sets the value of the vertical property.
     * 
     * @param value
     *     allowed object is
     *     {@link URLType }
     *     
     */
    public void setVertical(URLType value) {
        this.vertical = value;
    }

    /**
     * Gets the value of the gallery property.
     * 
     * @return
     *     possible object is
     *     {@link URLType }
     *     
     */
    public URLType getGallery() {
        return gallery;
    }

    /**
     * Sets the value of the gallery property.
     * 
     * @param value
     *     allowed object is
     *     {@link URLType }
     *     
     */
    public void setGallery(URLType value) {
        this.gallery = value;
    }

    /**
     * Gets the value of the optionalList property.
     * 
     * @return
     *     possible object is
     *     {@link OptionalListType }
     *     
     */
    public OptionalListType getOptionalList() {
        return optionalList;
    }

    /**
     * Sets the value of the optionalList property.
     * 
     * @param value
     *     allowed object is
     *     {@link OptionalListType }
     *     
     */
    public void setOptionalList(OptionalListType value) {
        this.optionalList = value;
    }

}
