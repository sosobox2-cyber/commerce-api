
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OriginAreaType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OriginAreaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Importer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Plural" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OriginAreaType", propOrder = {
    "code",
    "importer",
    "plural",
    "content"
})
public class OriginAreaType {

    @XmlElement(name = "Code", required = true)
    protected String code;
    @XmlElement(name = "Importer")
    protected String importer;
    @XmlElement(name = "Plural")
    protected String plural;
    @XmlElement(name = "Content")
    protected String content;

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the importer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImporter() {
        return importer;
    }

    /**
     * Sets the value of the importer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImporter(String value) {
        this.importer = value;
    }

    /**
     * Gets the value of the plural property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlural() {
        return plural;
    }

    /**
     * Sets the value of the plural property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlural(String value) {
        this.plural = value;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

}
