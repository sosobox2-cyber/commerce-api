
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OptionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OptionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProductId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="SortType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SimpleList" type="{http://shopn.platform.nhncorp.com/}SimpleOptionItemListType" minOccurs="0"/>
 *         &lt;element name="CustomList" type="{http://shopn.platform.nhncorp.com/}CustomOptionItemListType" minOccurs="0"/>
 *         &lt;element name="Combination" type="{http://shopn.platform.nhncorp.com/}CombinationOptionType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OptionType", propOrder = {
    "productId",
    "sortType",
    "simpleList",
    "customList",
    "combination"
})
public class OptionType {

    @XmlElement(name = "ProductId")
    protected long productId;
    @XmlElement(name = "SortType")
    protected String sortType;
    @XmlElement(name = "SimpleList")
    protected SimpleOptionItemListType simpleList;
    @XmlElement(name = "CustomList")
    protected CustomOptionItemListType customList;
    @XmlElement(name = "Combination")
    protected CombinationOptionType combination;

    /**
     * Gets the value of the productId property.
     * 
     */
    public long getProductId() {
        return productId;
    }

    /**
     * Sets the value of the productId property.
     * 
     */
    public void setProductId(long value) {
        this.productId = value;
    }

    /**
     * Gets the value of the sortType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSortType() {
        return sortType;
    }

    /**
     * Sets the value of the sortType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSortType(String value) {
        this.sortType = value;
    }

    /**
     * Gets the value of the simpleList property.
     * 
     * @return
     *     possible object is
     *     {@link SimpleOptionItemListType }
     *     
     */
    public SimpleOptionItemListType getSimpleList() {
        return simpleList;
    }

    /**
     * Sets the value of the simpleList property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleOptionItemListType }
     *     
     */
    public void setSimpleList(SimpleOptionItemListType value) {
        this.simpleList = value;
    }

    /**
     * Gets the value of the customList property.
     * 
     * @return
     *     possible object is
     *     {@link CustomOptionItemListType }
     *     
     */
    public CustomOptionItemListType getCustomList() {
        return customList;
    }

    /**
     * Sets the value of the customList property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomOptionItemListType }
     *     
     */
    public void setCustomList(CustomOptionItemListType value) {
        this.customList = value;
    }

    /**
     * Gets the value of the combination property.
     * 
     * @return
     *     possible object is
     *     {@link CombinationOptionType }
     *     
     */
    public CombinationOptionType getCombination() {
        return combination;
    }

    /**
     * Sets the value of the combination property.
     * 
     * @param value
     *     allowed object is
     *     {@link CombinationOptionType }
     *     
     */
    public void setCombination(CombinationOptionType value) {
        this.combination = value;
    }

}
