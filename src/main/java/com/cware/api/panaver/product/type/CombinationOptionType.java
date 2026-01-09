
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CombinationOptionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CombinationOptionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Names" type="{http://shopn.platform.nhncorp.com/}CombinationOptionNamesType" minOccurs="0"/>
 *         &lt;element name="ItemList" type="{http://shopn.platform.nhncorp.com/}CombinationOptionItemListType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CombinationOptionType", propOrder = {
    "names",
    "itemList"
})
public class CombinationOptionType {

    @XmlElement(name = "Names")
    protected CombinationOptionNamesType names;
    @XmlElement(name = "ItemList")
    protected CombinationOptionItemListType itemList;

    /**
     * Gets the value of the names property.
     * 
     * @return
     *     possible object is
     *     {@link CombinationOptionNamesType }
     *     
     */
    public CombinationOptionNamesType getNames() {
        return names;
    }

    /**
     * Sets the value of the names property.
     * 
     * @param value
     *     allowed object is
     *     {@link CombinationOptionNamesType }
     *     
     */
    public void setNames(CombinationOptionNamesType value) {
        this.names = value;
    }

    /**
     * Gets the value of the itemList property.
     * 
     * @return
     *     possible object is
     *     {@link CombinationOptionItemListType }
     *     
     */
    public CombinationOptionItemListType getItemList() {
        return itemList;
    }

    /**
     * Sets the value of the itemList property.
     * 
     * @param value
     *     allowed object is
     *     {@link CombinationOptionItemListType }
     *     
     */
    public void setItemList(CombinationOptionItemListType value) {
        this.itemList = value;
    }

}
