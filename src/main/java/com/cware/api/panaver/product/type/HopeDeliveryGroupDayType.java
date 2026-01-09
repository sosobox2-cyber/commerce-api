
package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>HopeDeliveryGroupDayType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="HopeDeliveryGroupDayType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="RegionName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Usable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HopeStartDay" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="HopeEndDay" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="ExpectationDeliveryFee" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HopeDeliveryGroupDayType", propOrder = {
    "id",
    "regionName",
    "usable",
    "hopeStartDay",
    "hopeEndDay",
    "expectationDeliveryFee"
})
public class HopeDeliveryGroupDayType {

    @XmlElement(name = "Id")
    protected Long id;
    @XmlElement(name = "RegionName", required = true)
    protected String regionName;
    @XmlElement(name = "Usable")
    protected String usable;
    @XmlElement(name = "HopeStartDay")
    protected Integer hopeStartDay;
    @XmlElement(name = "HopeEndDay")
    protected Integer hopeEndDay;
    @XmlElement(name = "ExpectationDeliveryFee")
    protected Integer expectationDeliveryFee;

    /**
     * id 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * id 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * regionName 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * regionName 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionName(String value) {
        this.regionName = value;
    }

    /**
     * usable 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsable() {
        return usable;
    }

    /**
     * usable 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsable(String value) {
        this.usable = value;
    }

    /**
     * hopeStartDay 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHopeStartDay() {
        return hopeStartDay;
    }

    /**
     * hopeStartDay 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHopeStartDay(Integer value) {
        this.hopeStartDay = value;
    }

    /**
     * hopeEndDay 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHopeEndDay() {
        return hopeEndDay;
    }

    /**
     * hopeEndDay 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHopeEndDay(Integer value) {
        this.hopeEndDay = value;
    }

    /**
     * expectationDeliveryFee 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getExpectationDeliveryFee() {
        return expectationDeliveryFee;
    }

    /**
     * expectationDeliveryFee 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setExpectationDeliveryFee(Integer value) {
        this.expectationDeliveryFee = value;
    }

}
